package org.clyze.utils

// This class takes care of resolving dependencies on Android, using
// the local Android SDK and maven.org. Before calling resolution, you
// must call once findSDK(rootDir), to set the SDK path from property
// 'sdk.dir' (read from file rootDir/local.properties).

class AndroidDepResolver {

    private String cachedSDK = null

    // Some dependencies are ignored.
    static final List ignoredGroups = [
        "com.android.support.test.espresso",
        "junit"
    ]

    // Group of Android dependencies that are resolved locally, via
    // the installed Android SDK.
    static final List localAndroidDeps = [
        "com.android.support",
        "com.android.support.constraint",
        "bitcoin-wallet"
    ]

    public static Set<String> resolveDependency(String appBuildHome, String group, String name, String version) {
        if (ignoredGroups.contains(group)) {
            logMessage("Ignoring dependency group: ${group}")
            return null
        }

        Set<String> ret = [] as Set
        String extDepsDir = getExtDepsDir(appBuildHome)
        String depDir = "${extDepsDir}/${group}/${name}/${version}"
        String classesJar = "${depDir}/classes.jar"
        String pom = "${depDir}/${name}-${version}.pom"

        // If the dependency exists, use it.
        if ((new File(classesJar)).exists()) {
            logMessage("Using dependency ${group}:${name}:${version}: ${classesJar}")
        } else {
            // Otherwise, resolve the dependency.
            try {
                // Generate subdirectory to contain the dependency.
                (new File(depDir)).mkdirs()

                if (localAndroidDeps.contains(group)) {
                    resolveAndroidDep(depDir, group, name, version, classesJar, pom)
                } else {
                    // TODO: pom.xml for external dependencies
                    resolveExtDep(depDir, group, name, version, classesJar)
                }
            } catch (Exception ex) {
                ex.printStackTrace()
                throwRuntimeException("AndroidDepResolver: Cannot resolve dependency ${group}:${name}:${version}")
            }
        }

        ret << classesJar

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
        return ret
    }

    // Decompress AAR and find its classes.jar.
    private static void unpackClassesJarFromAAR(File localAAR, String classesJar) {
        boolean classesJarFound = false
        def zipFile = new java.util.zip.ZipFile(localAAR)
        zipFile.entries().each {
            if (it.getName() == 'classes.jar') {
                File cj = new File(classesJar)
                cj.newOutputStream() << zipFile.getInputStream(it)
                logMessage("Resolved dependency: ${classesJar}")
                classesJarFound = true
            }
        }
        if (!classesJarFound) {
            String aarPath = localAAR.getCanonicalPath()
            throwRuntimeException("No classes.jar found in ${aarPath}")
        }
    }

    // Resolves an Android dependency by finding its .aar/.jar in the
    // local Android SDK installation. Parameter 'classesJar' is the
    // name of the .jar file that will contain the classes of the
    // dependency after this method finishes.
    private void resolveAndroidDep(String depDir, String group, String name,
                                   String version, String classesJar, String pom) {
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
            unpackClassesJarFromAAR(aarPath1, classesJar)
            pomPath = path1
        } else if (aarPath2.exists()) {
            unpackClassesJarFromAAR(aarPath2, classesJar)
            pomPath = path2
        } else if ((new File(jarPath3)).exists()) {
            copyFile(jarPath3, classesJar)
            pomPath = path3
        } else if ((new File(jarPath4)).exists()) {
            copyFile(jarPath4, classesJar)
            pomPath = path4
        } else {
            throwRuntimeException("Cannot find Android dependency: ${group}:${name}:${version}, tried: ${aarPath1}, ${aarPath2}, ${jarPath3}")
        }
        copyFile("${pomPath}/${name}-${version}.pom", pom)
        logMessage("Resolved Android artifact ${group}:${name}:${version}")
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

    // Resolve an external dependency as a local classes.jar file. AAR
    // libraries are downloadad and their classes.jar extracted; JARs
    // are downloaded as classes.jar files.
    // TODO: .pom handling.
    private static void resolveExtDep(String depDir, String group, String name,
                                      String version, String classesJar) {
        try {
            // Download AAR file.
            File localAAR = new File("${depDir}/${name}-${version}.aar")
            String aarURL = genMavenURL(group, name, version, "aar")
            logMessage("Downloading ${aarURL}...")
            localAAR.newOutputStream() << new URL(aarURL).openStream()
            unpackClassesJarFromAAR(localAAR, classesJar)
        } catch (FileNotFoundException ex) {
            // Download JAR file.
            logMessage("AAR not found for ${name}-${version}, looking for JAR...")
            File localJAR = new File("${depDir}/classes.jar")
            String jarURL = genMavenURL(group, name, version, "jar")
            logMessage("Downloading ${jarURL}...")
            localJAR.newOutputStream() << new URL(jarURL).openStream()
        }
    }

    private static String genMavenURL(String group, String name, String version, String ext) {
        String groupPath = group.replaceAll('\\.', '/')
        return "http://repo1.maven.org/maven2/${groupPath}/${name}/${version}/${name}-${version}.${ext}"
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
                println("AndroidPlatform warning: Android SDK directory (${property} in ${localProp}) does not exist: " + sdkDir)
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
