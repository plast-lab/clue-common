package org.clyze.signing

import groovy.io.FileType
import groovy.transform.CompileStatic
import groovy.util.logging.Log4j
import java.nio.file.Files
import java.util.function.Consumer
import org.clyze.utils.Executor
import org.clyze.utils.OS
import org.clyze.utils.JHelper

/**
 * This utility class provides signing functionality via jarsigner or apksigner.
 */
@Log4j @CompileStatic
class Signer {

    /**
     * Sign a code archive using the jarsigner tool.
     *
     * @param dir            the directory containing the code archive
     * @param archive        the name of the code archive (.jar or .apk)
     * @param sigAlg         the signature algorithm to use
     * @param digestAlg      the digest algorithm to use
     * @param keystorePath   the keystore path
     * @param keystorePass   the keystore password
     * @param keystoreAlias  the keystore alias
     * @return               the name of the signed archive (in the same directory)
     */
    static String signWithJarsigner(File dir, String archive, String sigAlg,
                                    String digestAlg, String keystorePath,
                                    String keystorePass, String keystoreAlias) {
        String javaHome = System.getProperty("java.home");
        if (javaHome == null) {
            log.debug "Cannot sign ${archive}: Java home not found."
            return null
        }
        String jsBin = OS.win ? 'bin\\jarsigner.exe' : 'bin/jarsigner'
        File jarsigner1 = new File(javaHome, jsBin)
        File jarsigner2 = new File("${javaHome}${File.separator}..${File.separator}", jsBin)
        File jarsigner = jarsigner1.exists() ? jarsigner1 :
            jarsigner2.exists() ? jarsigner2 : null
        if (!jarsigner) {
            log.debug "Cannot sign ${archive}: jarsigner not found: ${jarsigner1} or ${jarsigner2}"
            return null
        } else
            log.debug "Using jarsigner binary: ${jarsigner}"

        // Work on a copy, since jarsigner does not take an output.
        String signedArchiveName = "signed-${archive}"
        File signedArchive = new File(dir, signedArchiveName)
        Files.copy((new File(dir, archive)).toPath(), signedArchive.toPath())
        List<String> cmd = [ jarsigner.canonicalPath, '-verbose',
                            "-sigalg", sigAlg,
                            "-digestalg", digestAlg,
                            "-keystore", keystorePath,
                            "-storepass", keystorePass,
                            signedArchive.canonicalPath, keystoreAlias ] as List<String>
        log.debug("Signing command: " + String.join(" ", cmd))
        JHelper.runWithOutput(cmd as String[], 'JARSIGNER', log.&debug as Consumer<String>)
        return signedArchiveName
    }

    /**
     * Calls the 'apksigner' tool from the Android SDK to sign an .apk file.
     *
     * @param androidSdkHome  the path to the Android SDK
     * @param dir             the directory containing the code archive
     * @param apk             the name of the .apk file
     * @param messages        a list to return messages
     * @param debug           if true, generate extra debug messages
     * @param keystorePath    the store file to use for signing
     * @param keystorePass    the store password
     * @param keyAlias        the key alias in the store
     * @param keyPassword     the key password
     * @return  the name of the signed file (or null on error)
     */
    static String signWithApksigner(String androidSdkHome, File dir, String apk,
                                    List<String> messages, boolean debug,
                                    String keystorePath, String keystorePass,
                                    String keyAlias, String keyPassword) {
        if (!androidSdkHome) {
            messages << 'ERROR: cannot run apksigner, empty Android SDK home'
            return null
        }

        File sdkDir = new File(androidSdkHome, 'build-tools')
        List<String> apkSigners = []
        if (sdkDir.exists()) {
            sdkDir.eachFileRecurse (FileType.DIRECTORIES) { d ->
                // TODO: Windows
                File apkSigner = new File(d, 'apksigner')
                if (apkSigner.exists())
                    apkSigners << apkSigner.canonicalPath
            }
            if (apkSigners.size() == 0)
                messages.add("ERROR: No apksigner found, are build-tools installed (ANDROID_SDK=${androidSdkHome})?" as String)
            else {
                String apkSignerBinary = apkSigners.sort().reverse().get(0)
                String signedApk = "signed-" + apk
                String apkPathIn = (new File(dir, apk)).canonicalPath
                String apkPathOut = (new File(dir, signedApk)).canonicalPath
                List<String> cmd = [
                    apkSignerBinary, 'sign',
                    '--ks', keystorePath,
                    '--ks-pass', "pass:${keystorePass}" as String,
                    '--ks-key-alias', keyAlias,
                    '--key-pass', "pass:${keyPassword}" as String,
                    '--in', apkPathIn, '--out', apkPathOut
                ]
                Map<String, String> env = [:]
                env.putAll(System.getenv())
                if (debug)
                    messages.add("Running command: " + cmd.join(" "))
                (new Executor(environment: env)).execute(cmd)
                return signedApk
            }
        }
        return null
    }
}
