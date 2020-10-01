package org.clyze.input

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import groovy.util.logging.Log4j
import java.nio.file.Paths
import org.apache.commons.io.FileUtils
import org.clyze.utils.JHelper

@CompileStatic
@Log4j
@TupleConstructor
class PlatformManager {

	private static final boolean DEBUG = false

	@SuppressWarnings('unused')
	static final String ARTIFACTORY_PLATFORMS_URL = "http://centauri.di.uoa.gr:8081/artifactory/Platforms"

	static final Map<String, Set<String>> ARTIFACTS_FOR_PLATFORM = platformArtifacts

	String platformsLib
	String androidSdkDir
	/* A directory to use for caching generated platform JARs. */
	String cacheDir

	/**
	 * Find the files of an analysis platform.
	 * @param platform      the platform name (such as "java_8")
	 * @param useServer     if true, an external platforms server may be used to download files
	 * @param useLocalJava  a local Java installation to use for retrieving platform files (optional)
	 * @return
	 */
	List<String> find(String platform, boolean useServer = false, String useLocalJava = null) {
        List<String> platformParts = platform.split("_").toList()
        int partsCount = platformParts.size()
        String platformKind = platformParts.get(0)
        String version = partsCount > 1 ? platformParts.get(1) : ""
        String variant = partsCount > 2 ? platformParts.get(2) : ""
		switch (platformKind) {
			case "java":
				// Special case: generate "rt.jar" for Java 9+ from local installation.
				if (useLocalJava) {
					try {
						int javaVersion = Integer.parseInt(version)
						if (javaVersion >= 9) {
							String rtJar = getJava9PlusJar(platform, new File(useLocalJava, 'jmods'), false).canonicalPath
							return [rtJar] as List<String>
						}
					} catch (Exception ex) {
						log.debug "Could not parse Java platform version: ${version}"
					}
				}
				String vVersion = variant ? "${version}_$variant" : version
				return find0(platform, getJavaPlatformPath(platform, vVersion, useServer, useLocalJava))
			case "android":
				def platformSuffix = "platforms/android-${version}"
				List<String> files = null
				if ((variant == 'stubs') && androidSdkDir) {
					log.info "Looking for platform in Android SDK directory: ${androidSdkDir}"
					files = find0(platform, "${androidSdkDir}/${platformSuffix}")
				}
				if (files == null || !allPlatformFilesExist(files)) {
					log.info "Could not resolve platform '${platform}' via Android SDK, trying platforms library: ${platformsLib}"
					files = find0Platform(platform, platformsLib, variant, platformSuffix)
				}
				if (!allPlatformFilesExist(files) && useServer) {
					log.info "Could not resolve platform '${platform}', trying platforms server: ${ARTIFACTORY_PLATFORMS_URL}"
					files = find0Platform(platform, ARTIFACTORY_PLATFORMS_URL, variant, platformSuffix)
				}
				if (variant == "robolectric") {
					log.info "Using Robolectric with Java 8"
					files += find0("java_8", "${platformsLib}/JREs/jre1.8/lib")
				}
				return files
			default:
				log.debug "Cannot handle platform kind: ${platformKind}"
		}
		return [] as List
	}

	private String getJavaPlatformPath(String platform, String vVersion, boolean useServer, String useJavaPath) {
		// Special case: a Java installation path has been provided.
		if (useJavaPath) {
			File javaPath = new File(useJavaPath)
			if (javaPath.exists()) {
				File releaseFile = new File(javaPath, 'release')
				if (releaseFile.exists()) {
					Properties releaseProps = new Properties()
					releaseProps.load(new FileInputStream(releaseFile.canonicalPath))
					String javaVersion = releaseProps.getProperty('JAVA_VERSION')
					if (javaVersion) {
						javaVersion = javaVersion.trim()
						javaVersion = javaVersion.startsWith('"') ? javaVersion.substring(1) : javaVersion
						javaVersion = javaVersion.endsWith('"') ? javaVersion.substring(0, javaVersion.length() - 1) : javaVersion
						println "Using local Java platform ${javaVersion}"
						try {
							String releaseVer = JHelper.getJavaVersion(javaVersion)
							if (!releaseVer.equals(vVersion))
								log.warn "WARNING: requested Java platform ${vVersion} but local platform is ${releaseVer}"
						} catch (Exception ex) {
							log.warn "WARNING: could not parse Java version: ${javaVersion}"
						}
					}

				}
				return new File(javaPath, 'lib').canonicalPath
			} else
				log.warn "WARNING: Java library does not exist: ${javaPath}"
		}
		// If no platforms library is provided, use the server (or crash if this is not allowed).
		if (platformsLib == null) {
			if (useServer) {
				platformsLib = ARTIFACTORY_PLATFORMS_URL
			} else {
				throw new RuntimeException("ERROR: no platforms library available, cannot find platform '${platform}'.")
			}
		}
		return "${platformsLib}/JREs/jre1.${vVersion}/lib"
	}

	static final List<String> find0Platform(String platform, String platformsLib,
											String variant, String platformSuffix) {
		return find0(platform, "${platformsLib}/Android/${variant}/Android/Sdk/${platformSuffix}")
	}

	static final List<String> find0(String platform, String path) {
		def artifacts = ARTIFACTS_FOR_PLATFORM[platform]
		if (!artifacts)
			throw new RuntimeException("Invalid platform: $platform")
		artifacts.collect { "$path/$it" as String }
	}

	static boolean allPlatformFilesExist(Collection<String> paths) {
		return paths.findAll { !(new File(it)).exists() }.size() == 0
	}

    /**
     * Returns the artifacts for every supported platform.
     */
    static Map<String, Set<String>> getPlatformArtifacts() {

        // Different versions of the Android SDK share the same file tree structure.
        def androidTree1 = ["android.jar", "data/layoutlib.jar"]
        def androidTree2 = ["android.jar", "data/layoutlib.jar", "uiautomator.jar"]
        def androidTree3 = ["android.jar", "data/icu4j.jar", "data/layoutlib.jar", "uiautomator.jar"]
        def androidTree4 = ["android.jar", "data/layoutlib.jar", "uiautomator.jar", "optional/org.apache.http.legacy.jar"]
        def androidTree5 = ["android.jar", "data/layoutlib.jar", "uiautomator.jar", "optional/org.apache.http.legacy.jar", "android-stubs-src.jar"]
        def androidTree6 = ["android.jar", "data/layoutlib.jar", "uiautomator.jar", "optional/android.test.base.jar", "optional/android.test.runner.jar", "optional/android.test.mock.jar", "optional/org.apache.http.legacy.jar", "android-stubs-src.jar"]
        return [
            // JDKs
            "java_3"                : ["rt.jar"],
            "java_4"                : ["rt.jar", "jce.jar", "jsse.jar"],
            "java_5"                : ["rt.jar", "jce.jar", "jsse.jar"],
            "java_6"                : ["rt.jar", "jce.jar", "jsse.jar"],
            "java_7"                : ["rt.jar", "jce.jar", "jsse.jar", "tools.jar"],
            "java_7_debug"          : ["rt.jar", "jce.jar", "jsse.jar", "tools.jar"],
            "java_8"                : ["rt.jar", "jce.jar", "jsse.jar"],
            "java_8_debug"          : ["rt.jar", "jce.jar", "jsse.jar"],
            "java_8_mini"           : ["rt.jar", "jce.jar", "jsse.jar"],
            "java_9"                : ["rt.jar"],
            "java_10"               : ["rt.jar"],
            "java_11"               : ["rt.jar"],
            // Android compiled from sources
            "android_22_fulljars"   : androidTree3,
            "android_25_fulljars"   : androidTree4,
            // Android API stubs (from the SDK)
            "android_2_stubs"       : androidTree1,
            "android_3_stubs"       : androidTree1,
            "android_4_stubs"       : androidTree1,
            "android_5_stubs"       : androidTree1,
            "android_6_stubs"       : androidTree1,
            "android_7_stubs"       : androidTree1,
            "android_8_stubs"       : androidTree1,
            "android_9_stubs"       : androidTree1,
            "android_10_stubs"      : androidTree1,
            "android_11_stubs"      : androidTree1,
            "android_12_stubs"      : androidTree1,
            "android_13_stubs"      : androidTree1,
            "android_14_stubs"      : androidTree1,
            "android_15_stubs"      : androidTree1,
            "android_16_stubs"      : androidTree2,
            "android_17_stubs"      : androidTree3,
            "android_18_stubs"      : androidTree3,
            "android_19_stubs"      : androidTree3,
            "android_20_stubs"      : androidTree3,
            "android_21_stubs"      : androidTree3,
            "android_22_stubs"      : androidTree3,
            "android_23_stubs"      : androidTree4,
            "android_24_stubs"      : androidTree5,
            "android_25_stubs"      : androidTree5,
            "android_26_stubs"      : androidTree5,
            "android_27_stubs"      : androidTree5,
            "android_28_stubs"      : androidTree6,
            "android_29_stubs"      : androidTree6,
            // Android Dalvik equivalent
            "android_25_apks"       : [ "android_accessibilityservice.apk",
                                       "android_accounts.apk",
                                       "android_animation.apk",
                                       "android_annotation.apk",
                                       "android_app.apk",
                                       "android_appwidget.apk",
                                       "android_bluetooth.apk",
                                       "android_content.apk",
                                       "android_database.apk",
                                       "android_ddm.apk",
                                       "android_drm.apk",
                                       "android_filterfw.apk",
                                       "android_filterpacks.apk",
                                       "android_gesture.apk",
                                       "android_graphics.apk",
                                       "android_hardware.apk",
                                       "android_hidl.apk",
                                       "android_icu.apk",
                                       "android_inputmethodservice.apk",
                                       "android_location.apk",
                                       "android_media.apk",
                                       "android_mtp.apk",
                                       "android_net.apk",
                                       "android_nfc.apk",
                                       "android_opengl.apk",
                                       "android_os.apk",
                                       "android_permissionpresenterservice.apk",
                                       "android_preference.apk",
                                       "android_print.apk",
                                       "android_printservice.apk",
                                       "android_provider.apk",
                                       "android_renderscript.apk",
                                       "android_sax.apk",
                                       "android_security.apk",
                                       "android_service.apk",
                                       "android_speech.apk",
                                       "android_system.apk",
                                       "android_telecom.apk",
                                       "android_telephony.apk",
                                       "android_text.apk",
                                       "android_transition.apk",
                                       "android_util.apk",
                                       "android_view.apk",
                                       "android_webkit.apk",
                                       "android_widget.apk",
                                       "com.apk",
                                       "dalvik.apk",
                                       "java.apk",
                                       "javax.apk",
                                       "jdk.apk",
                                       "libcore.apk",
                                       "org.apk",
                                       "sun.apk"
            ],
            // Android-Robolectric
            "android_26_robolectric": androidTree5,
            // Python
            "python_2"              : [],
        ] as Map
    }

    /**
     * Find all locally installed Android SDKs.
     *
     * @param minVersion   the minimum Android SDK version (level)
     * @param maxVersion   the maximum Android SDK version (level)
     * @return             a list of version numbers
     */
    List<String> findInstalledAndroidSdks(int minVersion, int maxVersion) {
        List<String> sdks = []
        for (int version = minVersion; version <= maxVersion; version++) {
            def platform = "android_${version}_stubs"
            try {
                def files = find(platform)?.findAll { (new File(it)).exists() }
                if ((files != null) && files.size() > 0) {
                    sdks.add("" + version)
                }
            } catch (Throwable t) {
				if (DEBUG)
					t.printStackTrace()
            }
        }
        return sdks
    }

	/**
	 * Return a JAR that contains all platform classes from Java 9+ installations.
	 * @param platformId  the name of the platform (such as 'java_9')
	 * @param jmodsDir    the 'jmods' directory of the target Java installation
	 * @param useCached   if true, a cached local version may be used
	 * @return            the path to the platform JAR
	 */
	File getJava9PlusJar(String platformId, File jmodsDir, boolean useCached = true) {
		File rtJar = Paths.get(cacheDir, platformId, 'rt.jar').toFile()
		if (!useCached || !rtJar.exists()) {
			File jarOutDir = rtJar.parentFile
			if (!jarOutDir.exists())
				jarOutDir.mkdirs()
			File tmpDir = File.createTempDir()
			if (!jmodsDir.exists())
				throw new RuntimeException("ERROR: 'jmods' directory does not exist: ${jmodsDir}")
			jmodsDir.eachFile { File f ->
				if (f.name.endsWith('.jmod')) {
					String fPath = f.canonicalPath
					println "Processing: ${fPath}"
					String[] jmodCmd = ['jmod', 'extract', '--dir', tmpDir.canonicalPath, fPath]
					JHelper.runWithOutput(jmodCmd, 'JMOD')
				}
			}
			String rtJarPath = rtJar.canonicalPath
			String[] jarCmd = ['jar', '-cf', rtJarPath, '-C', "${tmpDir}/classes", '.']
			JHelper.runWithOutput(jarCmd, 'JAR')
			println "Generated platform JAR for '${platformId}': ${rtJarPath}"
			FileUtils.deleteQuietly(tmpDir)
		}
		return rtJar
	}
}
