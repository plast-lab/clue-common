package org.clyze.utils

// This class takes care of resolving dependencies on Android, using
// the local Android SDK and maven.org. Before calling resolution, you
// must call once findSDK(rootDir), to set the SDK path from property
// 'sdk.dir' (read from file rootDir/local.properties).

class AndroidDepResolver {

    private String cachedSDK = null

    // If set to true, then the resolver keeps track of resolved
    // artifacts with the same group/name and different versions and
    // returns the most recent version. This logic is followed only
    // for artifacts with groups belonging to 'groupsToKeepUpToDate'.
    private boolean useLatestVersion = false
    private static final List<String> groupsToKeepUpToDate = [
        "com.android.support",
        "com.google.android.gms"
    ]

    // The cache of resolved artifacts, from (group, name, version)
    // tuples to a set of JARs (artifact & its dependencies).
    private Map<ArtifactDesc, Set<String>> artifacts = new HashMap<>()
    static class ArtifactDesc {
        public String group
        public String name
        public String version
        public ArtifactDesc(String group, String name, String version) {
            this.group   = group
            this.name    = name
            this.version = version
        }
    }

    public void setUseLatestVersion(boolean u) {
        this.useLatestVersion = u
    }

    // Register an artifact given by a group:name:version tuple that
    // has been resolved to a local JAR and a set of local
    // dependencies (paths of other JARs). Used to pick latest
    // versions of resolved artifacts.
    private void registerArtifact(String group, String name, String version,
                                  String localJar, Set<String> artDeps) {
        ArtifactDesc ad = new ArtifactDesc(group, name, version)
        Map entry = artifacts.get(ad)
        if (entry == null) {
            Set<String> jars = new HashSet<>()
            jars << localJar
            jars.addAll(artDeps)
            artifacts.put(ad, jars)
        }
    }

    // Return the set of all JAR archives for the latest (already
    // resolved) version of an artifact (given as group:name:version).
    private Set<String> getLatestArtifactAndDeps(String group, String name, String version) {
        String latestVersion
        Set<String> ret
        artifacts.collect { ArtifactDesc ad, Set<String> jars ->
            boolean nameMatches = ad.group == group && ad.name == name
            if (nameMatches) {
                if (latestVersion == null ||
                                     dottedNumLessThan(latestVersion, ad.version,
                                                       group, name)) {
                    println "Switching to version ${ad.version} of ${group}:${name}" + (latestVersion == null? "" : " from ${latestVersion}")
                    latestVersion = ad.version
                    ret = jars
                }
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
                println "Warning: could not compare versions ${ver1} and ${ver2} for artifact ${group}:${name}"
                return false
            }
        }
        return false
    }

    // Some dependencies are ignored.
    static final List ignoredGroups = [
        "com.android.support.test.espresso",
        "junit"
    ]

    // Group of Android dependencies that are resolved locally, via
    // the installed Android SDK.
    static final List localAndroidDeps = [
        "com.android.support",
        "com.android.support.constraint"
    ]

    public Set<String> resolveDependency(String appBuildHome, String group, String name, String version) {
        if (ignoredGroups.contains(group)) {
            logMessage("Ignoring dependency group: ${group}")
            return null
        }

        Set<String> ret = [] as Set
        String extDepsDir = getExtDepsDir(appBuildHome)
        String depDir = "${extDepsDir}/${group}/${name}/${version}"
        String localJar = "${depDir}/${group}-${name}-${version}-classes.jar"
        String pom = "${depDir}/${name}-${version}.pom"

        // If the dependency exists, use it.
        if ((new File(localJar)).exists()) {
            logMessage("Using dependency ${group}:${name}:${version}: ${localJar}")
        } else {
            // Otherwise, resolve the dependency.
            try {
                // Generate subdirectory to contain the dependency.
                (new File(depDir)).mkdirs()

                if (localAndroidDeps.contains(group)) {
                    resolveAndroidDep(depDir, group, name, version, localJar, pom)
                } else {
                    resolveExtDep(depDir, group, name, version, localJar, pom)
                }
            } catch (Exception ex) {
                ex.printStackTrace()
                logMessage("Cannot resolve dependency ${group}:${name}:${version}, you may have to add it via the 'extraInputs' option.")
            }
        }

        // Read pom to resolve the dependencies of this dependency.
        if ((new File(pom)).exists()) {
            logMessage("Reading ${pom}...")
            def xml = new XmlSlurper().parse(new File(pom))
            xml.dependencies.children().each { dep ->
                String scope = dep?.scope
                if (scope == "compile") {
                    logMessage("Recursively resolving dependency: ${dep.artifactId}")
                    ret.addAll(resolveDependency(appBuildHome, dep.groupId.text(), dep.artifactId.text(), dep.version.text()))
                } else {
                    logMessage("Ignoring ${scope} dependency: ${dep.artifactId}")
                }
            }
        } else {
            logMessage("Warning: no pom file found for dependency ${group}:${name}:${version}")
        }

        final boolean isSpecialAndroidGroup = group in groupsToKeepUpToDate
        if (useLatestVersion && isSpecialAndroidGroup) {
            registerArtifact(group, name, version, localJar, ret)
            ret.addAll(getLatestArtifactAndDeps(group, name, version))
        } else {
            ret << localJar
        }

        return ret
    }

    // Decompress AAR and find its classes.jar.
    private static void unpackClassesJarFromAAR(File localAAR, String localJar) {
        boolean classesJarFound = false
        def zipFile = new java.util.zip.ZipFile(localAAR)
        zipFile.entries().each {
            if (it.getName() == 'classes.jar') {
                File cj = new File(localJar)
                cj.newOutputStream() << zipFile.getInputStream(it)
                classesJarFound = true
            }
        }
        if (!classesJarFound) {
            String aarPath = localAAR.getCanonicalPath()
            throwRuntimeException("No classes.jar found in ${aarPath}")
        }
    }

    // Resolves an Android dependency by finding its .aar/.jar in the
    // local Android SDK installation. Parameter 'localJar' is the
    // name of the .jar file that will contain the classes of the
    // dependency after this method finishes.
    private void resolveAndroidDep(String depDir, String group, String name,
                                   String version, String localJar, String pom) {
        String sdkHome = getSDK()
        String groupPath = group.replaceAll('\\.', '/')

        // Possible locations of .aar/.jar archives in the local
        // Android SDK installation.
        String path1 = "${sdkHome}/extras/android/m2repository/${groupPath}/${name}/${version}"
        String path2 = "${sdkHome}/extras/m2repository/${groupPath}/${name}/${version}"
        String path3 = "${sdkHome}/extras/android/m2repository/${groupPath}/${name}/${version}"
        String path4 = "${sdkHome}/extras/m2repository/${groupPath}/${name}/${version}"

        String pomPath = null
        File aarPath1 = new File("${path1}/${name}-${version}.aar")
        File aarPath2 = new File("${path2}/${name}-${version}.aar")
        String jarPath3 = "${path3}/${name}-${version}.jar"
        String jarPath4 = "${path4}/${name}-${version}.jar"

        if (aarPath1.exists()) {
            unpackClassesJarFromAAR(aarPath1, localJar)
            pomPath = path1
        } else if (aarPath2.exists()) {
            unpackClassesJarFromAAR(aarPath2, localJar)
            pomPath = path2
        } else if ((new File(jarPath3)).exists()) {
            copyFile(jarPath3, localJar)
            pomPath = path3
        } else if ((new File(jarPath4)).exists()) {
            copyFile(jarPath4, localJar)
            pomPath = path4
        } else {
            throwRuntimeException("Cannot find Android dependency: ${group}:${name}:${version}, tried: ${aarPath1}, ${aarPath2}, ${jarPath3}, ${jarPath4}")
        }
        copyFile("${pomPath}/${name}-${version}.pom", pom)
        logMessage("Resolved Android artifact ${group}:${name}:${version} -> ${localJar}")
    }

    private static void copyFile(String src, String dst) {
        File srcFile = new File(src)
        if (srcFile.exists()) {
            logMessage("Copying ${src} -> ${dst}")
            (new File(dst)).newOutputStream() << srcFile.newInputStream()
        } else {
            throwRuntimeException("File to copy does not exist: ${src}")
        }
    }

    // Resolve an external dependency as a local file. AAR libraries
    // are downloadad and their classes.jar extracted; JARs are
    // downloaded.
    private static void resolveExtDep(String depDir, String group, String name,
                                      String version, String localJar, String pom) {
        String mavenPrefix = genMavenURLPrefix(group, name, version)
        try {
            // Download AAR file.
            String localAARName = "${depDir}/${name}-${version}.aar"
            File localAAR = new File(localAARName)
            download("${mavenPrefix}.aar", localAARName)
            unpackClassesJarFromAAR(localAAR, localJar)
        } catch (FileNotFoundException ex) {
            // Download JAR file.
            logMessage("AAR not found for ${name}-${version}, looking for JAR...")
            download("${mavenPrefix}.jar", localJar)
        }
        download("${mavenPrefix}.pom", pom)
    }

    private static void download(String url, String localName) {
        File localFile = new File(localName)
        logMessage("Downloading ${url} -> ${localName}...")
        localFile.newOutputStream() << new URL(url).openStream()
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
    public String findSDK(String rootDir) {
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
            return cachedSDK
        } else {
            throw new RuntimeException("File ${localProperties.canonicalPath} does not exist.")
        }
    }

    private String getSDK() {
        // If findSDK() has not been called successfully, fail.
        return cachedSDK ?: throwRuntimeException("Internal error: SDK is null.")
    }

    // Throws a runtime exception with a message. The message is also
    // shown in the standard output. This utility helps debugging as
    // Gradle may report a different exception (e.g. the usual
    // IllegalStateException "buildToolsVersion is not specified").
    static void throwRuntimeException(String errMsg) {
        println errMsg
        throw new RuntimeException(errMsg)
    }

    private static void logMessage(String msg) {
        println "AndroidDepResolver: ${msg}"
    }
}
