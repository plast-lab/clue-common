package org.clyze.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.*;
import org.apache.log4j.helpers.NullEnumeration;
import org.mozilla.universalchardet.UniversalDetector;

/**
 * This class contains utilities that do not depend on Groovy and thus can be
 * code that does not include the Groovy runtime (such as plain Java programs).
 */
public class JHelper {

    static final String TIME_UTIL = "/usr/bin/time";

    private JHelper() {}

    /**
     * Throws a runtime exception with a message. The message is also
     * shown in the standard output. This utility helps debugging as
     * Gradle may report a different exception (e.g. the common
     * IllegalStateException "buildToolsVersion is not specified").
     * @param errMsg  the error message of the exception
     */
    public static void throwRuntimeException(String errMsg) {
        System.out.println(errMsg);
        throw new RuntimeException(errMsg);
    }

    /**
     * Deletes a set of paths. Used for manual garbage collection of temporary resources.
     * @param tmpDirs    a set of directory paths
     */
    public static void cleanUp(Set<String> tmpDirs) {
        tmpDirs.forEach(tmpDir -> FileUtils.deleteQuietly(new File(tmpDir)));
    }

    /**
     * Executes a command, printing all standard output/error messages
     * prefixed by a custom string.
     *
     * @param cmd       the command to run
     * @param prefix    the prefix
     * @param processor a line processor (can be null)
     * @throws IOException if no process can be built for the command
     */
    public static void runWithOutput(String[] cmd, String prefix, Consumer<String> processor) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(cmd);
        builder.redirectErrorStream(true);
        Process proc = builder.start();

        // Kill process if this VM shuts down.
        // System.err.println("Destroying process: " + String.join(" ", cmd));
        Runtime.getRuntime().addShutdownHook(new Thread(proc::destroyForcibly));

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        stdInput.lines().forEach(s -> processWithPrefix(s, prefix, processor));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        stdError.lines().forEach(s -> processWithPrefix(s, prefix, processor));
    }

    /**
     * Executes a command, printing all standard output/error messages
     * prefixed by a custom string.
     *
     * @param cmd       the command to run
     * @param prefix    the prefix
     * @throws IOException if no process can be built for the command
     */
    public static void runWithOutput(String[] cmd, String prefix) throws IOException {
        runWithOutput(cmd, prefix, null);
    }

    /**
     * Prints a line, prepending a prefix. An optional processor to
     * consume the line may also be given.
     *
     * @param s         the line
     * @param prefix    the prefix to print before the line (can be null to omit the whole line)
     * @param processor a line processor (can be null)
     */
    private static void processWithPrefix(String s, String prefix, Consumer<String> processor) {
        if (prefix != null)
            System.out.println(prefix + ": " + s);
        if (processor != null)
            processor.accept(s);
    }

    /**
     * If the given file is not encoded as UTF-8, its encoding is
     * detected and its contents are converted to UTF-8.
     *
     * @param filename   the path of the file to convert
     * @param debug      if true, show debug messages
     * @throws IOException if processing the file failed
     */
    public static void ensureUTF8(String filename, boolean debug) throws IOException {
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
            Charset targetEncoding = StandardCharsets.UTF_8;
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
	    if (debug)
		System.out.println("Converted " + encoding + " to UTF-8: " + filename + ", " + buf2.length + " vs. " + outDataLength + "bytes");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Cannot convert encoding " + encoding + " to UTF-8");
        }
    }

    /**
     * Runs a standalone JAR using 'java'.
     *
     * @param classpath   the classpath to use
     * @param jvmArgs     the JVM arguments to use
     * @param jar         the JAR to run
     * @param args        the command line arguments to pass
     * @param tag         a text prefix to mark output lines
     * @param debug       if true, print debug information
     * @param processor   a line processor (can be null)
     * @throws IOException if no process can be built for the command
     */
    public static void runJar(String[] classpath, String[] jvmArgs, String jar,
                              String[] args, String tag, boolean debug,
                              Consumer<String> processor) throws IOException {
        List<String> jvmArgs0 = new LinkedList<>(Arrays.asList(jvmArgs));
        jvmArgs0.add("-jar");
        jvmArgs0.add(jar);
        runJava(classpath, jvmArgs0.toArray(new String[0]), args, tag, debug, processor);
    }

    /**
     * Runs a Java class using 'java'.
     *
     * @param classpath   the classpath to use
     * @param jvmArgs     the JVM arguments to use
     * @param klass       the fully qualified name of the class to run
     * @param args        the command line arguments to pass
     * @param tag         a text prefix to mark output lines
     * @param debug       if true, print debug information
     * @param processor   a line processor (can be null)
     * @throws IOException if no process can be built for the command
     */
    public static void runClass(String[] classpath, String[] jvmArgs, String klass, String[] args,
                                String tag, boolean debug, Consumer<String> processor) throws IOException {
        List<String> jvmArgs0 = new LinkedList<>(Arrays.asList(jvmArgs));
        jvmArgs0.add(klass);
        runJava(classpath, jvmArgs0.toArray(new String[0]), args, tag, debug, processor);
    }

    /**
     * Runs a Java program using 'java'.
     *
     * @param classpath   the classpath to use
     * @param jvmArgs     the JVM arguments to use (including Java program)
     * @param args        the command line arguments to pass
     * @param tag         a text prefix to mark output lines
     * @param debug       if true, print debug information
     * @param processor   a line processor (can be null)
     * @throws IOException if no process can be built for the command
     */
    public static void runJava(String[] classpath, String[] jvmArgs,
                               String[] args, String tag, boolean debug,
                               Consumer<String> processor) throws IOException {
        String javaHome = System.getProperty("java.home");
        if (javaHome == null)
            throw new RuntimeException("Could not determine JAVA_HOME to run: " + Arrays.toString(jvmArgs));

        // Try to find 'java' in known locations.

        String javaBin = OS.win ? "java.exe" : "java";
        File java = new File(javaHome, javaBin);
        if (!java.exists()) {
            java = new File(new File(javaHome, "bin"), javaBin);
            if (!java.exists())
                throw new RuntimeException("Could not find 'java' in JAVA_HOME, cannot run: " + Arrays.toString(jvmArgs));
        }

        LinkedList<String> cmd = new LinkedList<>();
        if (debug && (new File(TIME_UTIL)).exists())
            cmd.add(TIME_UTIL);
        cmd.add(java.getAbsolutePath());
        if (classpath.length > 0) {
            cmd.add("-cp");
            cmd.add(String.join(":", classpath));
        }
        cmd.addAll(Arrays.asList(jvmArgs));
        cmd.addAll(Arrays.asList(args));
        if (debug)
            System.err.println("Running program: " + String.join(" ", cmd));
        runWithOutput(cmd.toArray(new String[]{}), tag, processor);
    }

    /**
     * Runs an external command. This method is inferior to
     * runWithOutput() but it can be useful to run an existing command
     * line (which is hard to split as an array).
     *
     * @param cmd        the command to run
     * @param tag        a text prefix to mark output lines
     * @param processor  a line processor (can be null)
     * @throws IOException if there was an I/O error during command execution
     * @throws InterruptedException if the command was interrupted
     * @return           the exit value of the command
     */
    public static int runCommand(String cmd, String tag,
                                 Consumer<String> processor)
        throws IOException, InterruptedException {
        Process proc = Runtime.getRuntime().exec(cmd);
        InputStream stdIn = proc.getInputStream();
        InputStream stdErr = proc.getErrorStream();
        BufferedReader brIn = new BufferedReader(new InputStreamReader(stdIn));
        BufferedReader brErr = new BufferedReader(new InputStreamReader(stdErr));

        String line;
        while ((line = brIn.readLine()) != null)
            processWithPrefix(line, tag, processor);
        while ((line = brErr.readLine()) != null)
            processWithPrefix(line, tag, processor);
        return proc.waitFor();
    }

    /**
     * Attempts to initialize logging (if it has not already been initialized).
     * @param logLevel the log level to use
     * @param logDir   the directory to place the log file
     * @param console  indicates whether log statements should be also written to the standard output.
     * @param logName  the file name of the log
     * @throws IOException  on log appender configuration
     */
    public static synchronized void tryInitLogging(String logLevel, String logDir, boolean console, String logName) throws IOException {
        if (shouldInitializeLogging())
            initLogging(logLevel, logDir, console, logName);
        else
            System.out.println("Logging already initialized.");
    }

    private static boolean shouldInitializeLogging() {
        Logger logger = Logger.getRootLogger();
        Enumeration<?> appenders = logger.getAllAppenders();
        if ((appenders == null) || (!appenders.hasMoreElements()) || (appenders instanceof NullEnumeration))
            return true;

        boolean doopAppenderFound = false;
        // Check that the appender of initLogging() is found.
        while (appenders.hasMoreElements()) {
            Appender appender = (Appender)appenders.nextElement();
            if (appender instanceof DailyRollingFileAppender) {
                doopAppenderFound = true;
            } else if (!(appender instanceof ConsoleAppender)) {
                System.err.println("Warning: non-Doop appender found: " + appender.getClass());
            }
        }
        return !doopAppenderFound;
    }

    /**
     * Initializes Log4j (logging framework).
     * Log statements are written to log file that is daily rolled.
     * Optionally, the log statements can be also written to the console (standard output).
     *
     * @param logLevel the log level to use
     * @param logDir   the directory to place the log file
     * @param console  indicates whether log statements should be also written to the standard output.
     * @param logName  the file name of the log
     * @throws IOException  on log appender configuration
     */
    private static void initLogging(String logLevel, String logDir, boolean console, String logName) throws IOException {
        Logger root = Logger.getRootLogger();
        root.setLevel(Level.toLevel(logLevel, Level.WARN));

        if (logDir != null) {
            File dir = new File(logDir);
            if (!dir.exists())
                //noinspection ResultOfMethodCallIgnored
                dir.mkdir();
            String logFile = logDir + File.separator + logName;
            PatternLayout layout = new PatternLayout("%d [%t] %-5p %c - %m%n");
            DailyRollingFileAppender appender = new DailyRollingFileAppender(layout, logFile, "'.'yyyy-MM-dd");
            root.addAppender(appender);
        }

        if (console)
            root.addAppender(new ConsoleAppender(new PatternLayout("%m%n")));
    }

    /**
     * Initializes Log4j (logging framework).
     * Log statements are written to the the console (standard output).
     *
     * @param logLevel - the log level to use
     */
    public static void initConsoleLogging(String logLevel) {
        Logger root = Logger.getRootLogger();
        root.setLevel(Level.toLevel(logLevel, Level.WARN));
        root.addAppender(new ConsoleAppender(new PatternLayout("%m%n")));
    }

    /**
     * Checks if the Java runtime is 9+.
     *
     * @return true if the runtime supports Java 9+
     */
    public static boolean java9Plus() {
        return !(System.getProperty("java.version").startsWith("1."));
        // Old method.
        // try {
        //     Class c = Class.forName("java.lang.Runtime$Version");
        //     return true;
        // } catch (ClassNotFoundException ex) {
        //     return false;
        // }
    }

    /**
     * Returns the Java version as a single number. Versions "1.x" become "x",
     * while versions "x.y" become "x".
     * @return      the Java version as a number
     */
    public static int getJavaVersion() {
        return Integer.parseInt(getJavaVersion(System.getProperty("java.version")));
    }

    /**
     * Translates a Java version to a single number. Versions "1.x" become "x",
     * while versions "x.y" become "x".
     * @param ver   the Java version reported by the JVM
     * @return      the Java version as a number
     */
    public static String getJavaVersion(String ver) {
        if (ver.startsWith("1."))
            return ver.substring(2, ver.indexOf('.', 3));
        else
            return ver.substring(0, ver.indexOf('.'));
    }

    /**
     * Returns the path to the Java installation that runs this code. Currently,
     * the environment variable "JAVA_HOME" and the system property "java.home"
     * are checked (in this order).
     * @return    the path to the Java installation
     */
    public static String getJavaHome() {
        String javaHome = System.getenv("JAVA_HOME");
        if (javaHome != null)
            return javaHome;
        return System.getProperty("java.home");
    }

    /**
     * Returns the version of the JAR where a class is defined. This
     * version is configured by two pieces of information in the JAR:
     * field 'Implementation-Version' in the manifest (software
     * release version) and file git-hash.txt (HEAD hash).
     *
     * @param c	 A class object to use for finding the manifest
     *			 information. Different classes may retrieve manifests
     *			 from different JARs.
     * @return	 A version string that may contain a release tag or a
     *			 git hash (or both).
     */
    public static String getVersionInfo(Class<?> c) {
        // Read "version" (may be empty if run via './gradlew run').
        String version = c.getPackage().getImplementationVersion();

        // Read Git commit hash.
        String hash = null;
        InputStream gitHashIS = c.getClassLoader().getResourceAsStream("git-hash.txt");
        if (gitHashIS != null) {
            String line;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(gitHashIS))) {
                while ((line = br.readLine()) != null)
                    if (!line.equals(""))
                        hash = line;
            } catch (IOException ignored) { }
        }

        if (hash == null && version == null)
            return "No version information available.";
        else if (hash != null && version != null)
            return "Version: " + version + " (Git hash: " + hash + ")";
        else if (version != null)
            return "Version: " + version;
        else // (hash != null)
            return "Git hash: " + hash;
    }
}
