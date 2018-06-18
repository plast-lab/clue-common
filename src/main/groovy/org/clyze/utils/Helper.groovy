package org.clyze.utils

import groovy.util.logging.Log4j
import org.apache.commons.io.FileUtils
import org.apache.log4j.*

import java.lang.reflect.Method

/**
 * Various helper methods.
 */
@Log4j
class Helper {

	/**
	 * Initializes Log4j (logging framework).
	 * Log statements are written to log file that is daily rolled.
	 * Optionally, the log statements can be also written to the console (standard output).
	 * @param logLevel - the log level to use
	 * @param logDir - the directory to place the log file
	 * @param console - indicates whether log statements should be also written to the standard output.
	 */
	static void initLogging(String logLevel, String logDir, boolean console) throws IOException {
		def dir = new File(logDir)
		if (!dir.exists())
			dir.mkdir()

		def logFile = "$logDir/doop.log"

		def root = Logger.rootLogger
		root.setLevel(Level.toLevel(logLevel, Level.WARN))
		PatternLayout layout = new PatternLayout("%d [%t] %-5p %c - %m%n")
		DailyRollingFileAppender appender = new DailyRollingFileAppender(layout, logFile, "'.'yyyy-MM-dd")
		root.addAppender(appender)

		if (console) {
			root.addAppender(new ConsoleAppender(new PatternLayout("%m%n")))
		}
	}

	/**
	 * Initializes Log4j (logging framework).
	 * Log statements are written to the the console (standard output).
	 * @param logLevel - the log level to use
	 */
	static void initConsoleLogging(String logLevel) throws IOException {
		def root = Logger.rootLogger
		root.setLevel(Level.toLevel(logLevel, Level.WARN))
		root.addAppender(new ConsoleAppender(new PatternLayout("%m%n")))
	}

	/**
	 * Executes the given Java main class using the supplied class
	 * loader. Returns false if the method throws an exception.
	 */
	static boolean execJava(ClassLoader cl, String mainClass, String[] params) {
		Class theClass = Class.forName(mainClass, true, cl)
		Method mainMethod = theClass.getMethod("main", [String[].class] as Class[])
        try {
            mainMethod.invoke(null, [params] as Object[])
            return true
        } catch (all) {
            return false
        }
	}

	/**
	 * Returns the stack trace of an exception as String.
	 */
	static String stackTraceToString(Throwable t) {
		def sb = new StringBuilder()
		t.getStackTrace().each { StackTraceElement elem ->
			sb.append(elem.toString()).append('\n')
		}
		return sb.toString()
	}

	/**
	 * Return elapsed time in seconds.
	 */
	static long timing(Closure c) {
		long now = System.currentTimeMillis()
		try {
			c.call()
		}
		catch(e) {
			throw e
		}
		// We measure time only in error-free cases
		return ((System.currentTimeMillis() - now) / 1000).longValue()
	}

	/**
	 * Print elapsed time in seconds along with the given message.
	 */
	static void timingWithLogging(String message, Closure c) { log.debug(message + " took ${timing(c)} sec") }

    // Throws a runtime exception with a message. The message is also
    // shown in the standard output. This utility helps debugging as
    // Gradle may report a different exception (e.g. the usual
    // IllegalStateException "buildToolsVersion is not specified").
    static void throwRuntimeException(String errMsg) {
        println errMsg
        throw new RuntimeException(errMsg)
    }

    static cleanUp(Set<String> tmpDirs) {
        tmpDirs.each { tmpDir -> FileUtils.deleteQuietly(new File(tmpDir)) }
    }
}
