package org.clyze.utils

import static org.clyze.utils.JHelper.throwRuntimeException

// This class takes care of resolving dependencies on Android, using
// the local Android SDK and maven.org. Before calling resolution, you
// must call once findSDK(rootDir), to set the SDK path from property
// 'sdk.dir' (read from file rootDir/local.properties).

class AndroidDepResolver {

    private static boolean verbose = false

    private String cachedSDK = null

    // If set to true, then the resolver keeps track of resolved
    // artifacts with the same group/name and different versions and
    // returns the most recent version. This logic is followed only
    // for artifacts with groups belonging to 'localAndroidDeps'.
    private boolean useLatestVersion = false

    void setUseLatestVersion(boolean u) { this.useLatestVersion = u }

    // If true, then the latest versions are not resolved when
    // requested but the artifact group:name is pushed in a stack and
    // can be resolved later by getLatestDelayedArtifacts().
    private boolean resolveLatestLast = false

    void setResolveLatestLast(boolean r) { this.resolveLatestLast = r }

    // The list of delayed artifacts for resolution. The 'version'
    // field in each element is assumed to be null.
    private List<ArtifactDesc> delayedArtifacts = [] as List

    // The cache of resolved artifacts, from (group, name, version)
    // tuples to a set of JARs (artifact & its dependencies).
    private Map<ArtifactDesc, Set<String>> artifacts = new HashMap<>()
    static class ArtifactDesc {
        public String group
        public String name
        public String version

        ArtifactDesc(String group, String name, String version) {
            this.group   = group
            this.name    = name
            this.version = version
        }
    }

    private Set<String> failedArtifacts = new HashSet<>()
    public Set<List> ignoredArtifacts = new HashSet<>()

    // Register an artifact given by a group:name:version tuple that
    // has been resolved to a local JAR and a set of local
    // dependencies (paths of other JARs). Used to pick latest
    // versions of resolved artifacts.
    private void registerArtifact(String group, String name, String version,
                                  String localJar, Set<String> artDeps) {
        ArtifactDesc ad = new ArtifactDesc(group, name, version)
        Set<String> entry = artifacts.get(ad)
        if (entry == null) {
            Set<String> jars = new HashSet<>()
            jars << localJar
            jars.addAll(artDeps)
            artifacts.put(ad, jars)
        }
    }

    // Return the set of all JAR archives for the latest (already
    // resolved) version of an artifact (given as group:name:version).
    private Set<String> getLatestArtifactAndDeps(String group, String name) {
        String latestVersion
        Set<String> ret = null
        artifacts.each { ArtifactDesc ad, Set<String> jars ->
            boolean nameMatches = ad.group == group && ad.name == name
            if (nameMatches && (latestVersion == null ||
                                dottedNumLessThan(latestVersion, ad.version, group, name))) {
                logVMessage("Switching to version ${ad.version} of ${group}:${name}" + (latestVersion == null? "" : " from ${latestVersion}"))
                latestVersion = ad.version
                ret = jars
            }
        }
        return ret
    }

    // Compares two version numbers, e.g. "1.2.3" and "1.2.0".
    private static boolean dottedNumLessThan(String ver1, String ver2,
                                             String group, String name) {
        List<String> values1 = ver1.tokenize(".")
        List<String> values2 = ver2.tokenize(".")
        int minLength = Math.min(values1.size(), values2.size())
        for (int i = 0; i < minLength; i++) {
            try {
                int i1 = values1[i].toInteger()
                int i2 = values2[i].toInteger()
                if (i1 < i2) {
                    return true
                } else if (i1 > i2) {
                    return false
                }
            } catch (RuntimeException ex) {
                println "Warning: could not compare versions ${ver1} and ${ver2} for artifact ${group}:${name} (${ex.message})"
                return false
            }
        }
        return false
    }

    // Some dependencies are ignored.
    static final List<String> ignoredGroups = [
        "com.android.support.test.espresso",
        "junit"
    ]

    // Group of Android dependencies that are resolved locally, via
    // the installed Android SDK.
    private static final List<String> localAndroidDeps = [
        "com.android.support",
        "com.android.support.constraint",
        "com.google.android.gms"
    ]

    Set<String> resolveDependency(String appBuildHome, String group, String name, String version) {
        if (ignoredGroups.contains(group)) {
            logMessage("Ignoring dependency group: ${group}")
            return null
        }

        String artifactId = "${group}:${name}:${version}"
        if (artifactId in failedArtifacts) {
            return null
        }

        if ([group, name] in ignoredArtifacts) {
            return null
        }

        if (version == null || version.equals("")) {
            println("No version of ${group}-${name}, debug Maven prefix: " + genMavenURLPrefix(group, name, version))
            if (useLatestVersion) {
                if (resolveLatestLast) {
                    delayArtifactResolution(group, name)
                } else {
                    Set<String> jars = getLatestArtifactAndDeps(group, name)
                    if (jars != null) {
                        logVMessage("Artifact cache has ${group}:${name}: ${jars}")
                        return jars
                    } else {
                        logVMessage("Cannot find ${group}:${name} in artifact cache.")
                        return null
                    }
                }
            }
        }

        Set<String> ret = [] as Set
        String extDepsDir = getExtDepsDir(appBuildHome)
        String depDir = "${extDepsDir}/${group}/${name}/${version}"
        String localPre = "${depDir}/${group}-${name}-${version}"
        String localAar = "${localPre}.aar"
        String localJar = "${localPre}.jar"
        String pom = "${depDir}/${name}-${version}.pom"
        String savedFile

        // If the dependency exists, use it.
        if ((new File(localAar)).exists()) {
            logVMessage("Using dependency ${artifactId}: ${localAar}")
            savedFile = localAar
        } else if ((new File(localJar)).exists()) {
            logVMessage("Using dependency ${artifactId}: ${localJar}")
            savedFile = localJar
        } else {
            // Otherwise, resolve the dependency.
            try {
                // Generate subdirectory to contain the dependency.
                (new File(depDir)).mkdirs()

                if (localAndroidDeps.contains(group)) {
                    savedFile = resolveAndroidDep(depDir, group, name, version, localPre, pom)
                } else {
                    savedFile = resolveExtDep(depDir, group, name, version, localPre, pom)
                }
            } catch (Exception ex) {
                if (verbose) {
                    ex.printStackTrace()
                }
                failedArtifacts << artifactId
                logMessage("Cannot resolve dependency ${artifactId}, you may have to add it via the 'extraInputs' option.")
            }
        }

        // Read pom to resolve the dependencies of this dependency.
        if ((new File(pom)).exists()) {
            logVMessage("Reading ${pom}...")
            def xml = new XmlSlurper().parse(new File(pom))
            xml.dependencies.children().each { dep ->
                String scope = dep?.scope
                if (scope == null || scope in [ "compile", "provided", "" ]) {
                    logVMessage("Recursively resolving dependency: ${dep.artifactId}")
                    Set<String> recDeps = resolveDependency(appBuildHome, dep.groupId.text(), dep.artifactId.text(), dep.version.text())
                    if (recDeps != null) {
                        ret.addAll(recDeps)
                    }
                } else {
                    logVMessage("Ignoring [${scope}] dependency: ${dep.artifactId.text()}:${dep.version.text()}")
                }
            }
        } else {
            logVMessage("Warning: no pom file found for dependency ${artifactId}")
        }

        final boolean isSpecialAndroidGroup = group in localAndroidDeps
        if (savedFile != null) {
            if (useLatestVersion && isSpecialAndroidGroup) {
                registerArtifact(group, name, version, savedFile, ret)
                if (resolveLatestLast) {
                    delayArtifactResolution(group, name)
                } else {
                    ret.addAll(getLatestArtifactAndDeps(group, name))
                }
            } else {
                ret << savedFile
            }
        }
        return ret
    }

    private void delayArtifactResolution(String group, String name) {
        logVMessage("Delaying resolution of artifact ${group}:${name}")
        delayedArtifacts << new ArtifactDesc(group, name, null)
    }

    Set<String> getLatestDelayedArtifacts() {
        logVMessage("Resolving delayed artifacts...")
        Set<String> ret = new HashSet<>()
        delayedArtifacts.each { ArtifactDesc ad ->
            Set<String> deps = getLatestArtifactAndDeps(ad.group, ad.name)
            if ([ad.group, ad.name] in ignoredArtifacts) {
                logVMessage("Not going to resolve ${ad.group}:${ad.name}.")
            } else if (deps != null) {
                logVMessage("Artifact ${ad.group}:${ad.name} was resolved: ${deps}")
                ret.addAll(deps)
            } else {
                logVMessage("Artifact ${ad.group}:${ad.name} could not be resolved.")
            }
        }
        return ret
    }

    // Resolves an Android dependency by finding its .aar/.jar in the
    // local Android SDK installation. Parameter 'localPre' is the
    // base name of the .aar/.jar file that will contain the
    // dependency after this method finishes.
    private String resolveAndroidDep(String depDir, String group, String name,
                                     String version, String localPre, String pom) {
        String savedFile = null
        String sdkHome = getSDK()
        String groupPath = group.replaceAll('\\.', '/')
        String pomPath = null

        // Possible locations of .aar/.jar archives in the local
        // Android SDK installation.
        final String aarPath1 = "${sdkHome}/extras/android/m2repository/${groupPath}/${name}/${version}"
        final String aarPath2 = "${sdkHome}/extras/google/m2repository/${groupPath}/${name}/${version}"
        final String aarPath3 = "${sdkHome}/extras/m2repository/${groupPath}/${name}/${version}"
        final Map<String, File> aars = new HashMap<>()
        aars.put(aarPath1, new File("${aarPath1}/${name}-${version}.aar"))
        aars.put(aarPath2, new File("${aarPath2}/${name}-${version}.aar"))
        aars.put(aarPath3, new File("${aarPath3}/${name}-${version}.aar"))

        // Search for .aar dependencies.
        def aarCandidate = aars.find { it.value.exists() }
        if (aarCandidate) {
            String aarDir = aarCandidate.key
            File aar = aarCandidate.value
            savedFile = "${localPre}.aar"
            copyFile(aar.canonicalPath, savedFile)
            pomPath = aarDir
        } else {
            // Search for .jar dependencies.
            final String jarPath1 = "${sdkHome}/extras/android/m2repository/${groupPath}/${name}/${version}"
            final String jarPath2 = "${sdkHome}/extras/m2repository/${groupPath}/${name}/${version}"
//            final String jarFullPath1 = "${jarPath1}/${name}-${version}.jar"
//            final String jarFullPath2 = "${jarPath2}/${name}-${version}.jar"
            final Map<String, String> jars = new HashMap<>()
            jars.put(jarPath1, "${jarPath1}/${name}-${version}.jar")
            jars.put(jarPath2, "${jarPath2}/${name}-${version}.jar")
            def jarCandidate = jars.find { (new File(it.value)).exists() }
            if (jarCandidate) {
                String jarDir = jarCandidate.key
                String jarFullPath = jarCandidate.value
                savedFile = "${localPre}.jar"
                copyFile(jarFullPath, savedFile)
                pomPath = jarDir
            } else {
                Set<String> aarPaths = aars.collect { it.value.canonicalPath }
                Set<String> jarPaths = jars.collect { it.value }
                throwRuntimeException("Cannot find Android dependency: ${group}:${name}:${version}, tried: ${aarPaths}, ${jarPaths}")
            }

        }
        copyFile("${pomPath}/${name}-${version}.pom", pom)
        logVMessage("Resolved Android artifact ${group}:${name}:${version} -> ${savedFile}")
        return savedFile
    }

    private static void copyFile(String src, String dst) {
        File srcFile = new File(src)
        if (srcFile.exists()) {
            logVMessage("Copying ${src} -> ${dst}")
            (new File(dst)).newOutputStream() << srcFile.newInputStream()
        } else {
            throwRuntimeException("File to copy does not exist: ${src}")
        }
    }

    // Resolve an external dependency as a local file. AAR libraries
    // are downloadad and their classes.jar extracted; JARs are
    // downloaded.
    private static String resolveExtDep(String depDir, String group, String name,
                                        String version, String localPre, String pom) {
        String savedFile
        String mavenPrefix = genMavenURLPrefix(group, name, version)
        try {
            // Download AAR file.
            savedFile = "${localPre}.aar"
            download("${mavenPrefix}.aar", savedFile)
        } catch (FileNotFoundException ex) {
            // Download JAR file.
            logVMessage("AAR not found for ${name}-${version}, looking for JAR... [${ex.message}]")
            savedFile = "${localPre}.jar"
            download("${mavenPrefix}.jar", savedFile)
        }
        download("${mavenPrefix}.pom", pom)
        return savedFile
    }

    private static void download(String url, String localName) {
        File localFile = new File(localName)
        try {
            localFile.newOutputStream() << new URL(url).openStream()
        } catch (Exception ex) {
            localFile.delete()
            throw ex
        }
        logMessage("Downloaded ${url} -> ${localName}")
    }

    private static String genMavenURLPrefix(String group, String name, String version) {
        String groupPath = group.replaceAll('\\.', '/')
        return "http://repo1.maven.org/maven2/${groupPath}/${name}/${version}/${name}-${version}"
    }

    private static String getExtDepsDir(String appBuildHome) {
        String dirName = "${appBuildHome}/extdeps"
        File extDepsDir = new File(dirName)
        if (!extDepsDir.exists()) {
            extDepsDir.mkdir()
        }
        return dirName
    }

    // Find the location of the Android SDK. Assumes it is property
    // 'sdk.dir' in file 'local.properties' located in 'rootDir'.
    void findSDK(String rootDir) {
        def localProp = "local.properties"
        def localProperties = new File(rootDir, localProp)
        if (localProperties.exists()) {
            Properties properties = new Properties()
            localProperties.withInputStream { instr ->
                properties.load(instr)
            }
            def property = 'sdk.dir'
            def sdkDir = properties.getProperty(property)
            // println("Android SDK = " + sdkDir)
            if (!(new File(sdkDir)).exists()) {
                logMessage("Warning: Android SDK directory (${property} in ${localProp}) does not exist: " + sdkDir)
            }
            cachedSDK = sdkDir
        } else {
            String androidEnv = "ANDROID_HOME"
            String androidHome = System.getenv(androidEnv)
            if (androidHome != null) {
                cachedSDK = androidHome
            } else {
                throw new RuntimeException("File ${localProperties.canonicalPath} does not exist and ${androidEnv} is not defined.")
            }
        }
    }

    private String getSDK() {
        // If findSDK() has not been called successfully, fail.
        return cachedSDK ?: throwRuntimeException("Internal error: SDK is null.")
    }

    private static void logMessage(String msg) {
        println "AndroidDepResolver: ${msg}"
    }

    private static void logVMessage(String msg) {
        if (verbose) { logMessage(msg) }
    }
}
