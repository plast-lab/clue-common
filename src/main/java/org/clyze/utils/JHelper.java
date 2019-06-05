package org.clyze.utils;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.io.*;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.mozilla.universalchardet.UniversalDetector;

public class JHelper {

    // Throws a runtime exception with a message. The message is also
    // shown in the standard output. This utility helps debugging as
    // Gradle may report a different exception (e.g. the usual
    // IllegalStateException "buildToolsVersion is not specified").
    public static void throwRuntimeException(String errMsg) {
        System.out.println(errMsg);
        throw new RuntimeException(errMsg);
    }

    public static void cleanUp(Set<String> tmpDirs) {
        tmpDirs.forEach(tmpDir -> FileUtils.deleteQuietly(new File(tmpDir)));
    }

    /**
     * Executes a command, printing all standard output/error messages
     * prefixed by a custom string.
     *
     * @param cmd       the command to run
     * @param prefix    the prefix
     */
    public static void runWithOutput(String[] cmd, String prefix) throws IOException {
        Process proc = Runtime.getRuntime().exec(cmd);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        stdInput.lines().forEach(s -> printWithPrefix(s, prefix));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        stdError.lines().forEach(s -> printWithPrefix(s, prefix));
    }

    private static void printWithPrefix(String s, String prefix) {
        System.out.println(prefix + ": " + s);
    }

    /**
     * If the given file is not encoded as UTF-8, its encoding is
     * detected and its contents are converted to UTF-8.
     *
     * @param filename   the path of the file to convert
     */
    public static void ensureUTF8(String filename) throws IOException {
        // Encoding detector.
        UniversalDetector detector = new UniversalDetector(null);

        FileInputStream fis = new FileInputStream(filename);
        byte[] buf = new byte[4096];
        int nread;
        while ((nread = fis.read(buf)) > 0 && !detector.isDone())
            detector.handleData(buf, 0, nread);
        detector.dataEnd();

        String encoding = detector.getDetectedCharset();
        detector.reset();
        fis.close();

        if ((encoding == null) || (encoding.equals("UTF-8")))
            return;

        // Try to convert source file to UTF-8.
        try {
            Charset sourceEncoding = Charset.forName(encoding);
            Charset targetEncoding = Charset.forName("UTF-8");
            byte[] buf2 = IOUtils.toByteArray(new FileInputStream(filename));
            CharBuffer data = sourceEncoding.decode(ByteBuffer.wrap(buf2));
            ByteBuffer outBuf = targetEncoding.encode(data);
            BufferedWriter bufWriter = new BufferedWriter(new FileWriter(filename));
            int outDataLength = 0;
            while (outBuf.remaining() > 0) {
                bufWriter.write(outBuf.get());
                outDataLength++;
            }
            bufWriter.close();
            System.out.println("Converted " + encoding + " to UTF-8: " + filename + ", " + buf2.length + " vs. " + outDataLength + "bytes");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Cannot convert encoding " + encoding + " to UTF-8");
        }
    }
}
