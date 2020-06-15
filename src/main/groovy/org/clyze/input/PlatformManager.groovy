package org.clyze.input

import groovy.transform.TupleConstructor
import groovy.transform.TypeChecked
import groovy.util.logging.Log4j

@Log4j
@TupleConstructor
@TypeChecked
class PlatformManager {

	@SuppressWarnings('unused')
	static final String ARTIFACTORY_PLATFORMS_URL = "http://centauri.di.uoa.gr:8081/artifactory/Platforms"

	static final Map<String, Set<String>> ARTIFACTS_FOR_PLATFORM = platformArtifacts

	String platformsLib
	String androidSdkDir

	List<String> find(String platform, boolean useServer = false) {
        List<String> platformParts = platform.split("_").toList()
        int partsCount = platformParts.size()
        String platformKind = platformParts.get(0)
        String version = partsCount > 1 ? platformParts.get(1) : ""
        String variant = partsCount > 2 ? platformParts.get(2) : ""
		switch (platformKind) {
			case "java":
				def vVersion = variant ? "${version}_$variant" : version
				// If no platform library is given, use the server (or crash if this is not allowed).
				if (platformsLib == null) {
					if (useServer) {
						platformsLib = ARTIFACTORY_PLATFORMS_URL
					} else {
						throw new RuntimeException("ERROR: no platforms library available, cannot find platform '${platform}'.")
					}
				}
				def platformPath = "${platformsLib}/JREs/jre1.${vVersion}/lib"
				return find0(platform, platformPath)
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
    public static Map<String, Set<String>> getPlatformArtifacts() {

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
            }
        }
        return sdks
    }
}
