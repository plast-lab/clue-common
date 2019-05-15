package org.clyze.utils;

import java.net.URL;
import java.net.URLClassLoader;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

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
     * Create a copy of the current classpath. Only supported in Java < 9.
     *
     * @param log  a logger to use (may be null)
     * @param obj  an object to use for reading a class loader
     * @return     a new class loader
     */
    public static ClassLoader copyOfCurrentClasspath(Logger log, Object obj) {
        ClassLoader cl = obj.getClass().getClassLoader();
        if (cl instanceof URLClassLoader) {
            if (log != null)
                log.debug("Reading URL entries from current class loader...");
            URL[] classpath = ((URLClassLoader)cl).getURLs();
            if (log != null)
                log.debug("Creating a new URL class loader with classpath = " + Arrays.toString(classpath));
            return new URLClassLoader(classpath, (ClassLoader)null);
        } else {
            return cl;
            // We currently don't support classpath copies for Java 9+. Solution:
            //
            // 1. The classpath can be parsed as follows:
            //   log.debug "Parsing current classpath to reconstruct URL entries..."
            //   String pathSeparator = System.getProperty("path.separator");
            //   classpath = System.getProperty("java.class.path").
            //               split(pathSeparator).
            //               collect { new URL("file://${it}") } as URL[]
            //
            // 2. And then a ModuleLayer must be constructed and loaded:
            //    https://docs.oracle.com/javase/9/docs/api/java/lang/ModuleLayer.html
            //
            // However, the technique above makes Java 9+ a compile-time dependency
            // and thus breaks Java 8 compatibility, unless all code is reflective.
        }
    }
}
