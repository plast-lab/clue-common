package org.clyze.utils

import groovy.util.logging.Log4j
import org.apache.log4j.*
import org.apache.log4j.helpers.NullEnumeration;

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

	static boolean shouldInitializeLogging() {
		Logger logger = Logger.getRootLogger()
		Enumeration appenders = logger.getAllAppenders()
		boolean noAppenders = ((appenders == null) || (!appenders.hasMoreElements()) || (appenders instanceof NullEnumeration))
		if (noAppenders) {
			return true
		} else {
			boolean doopAppenderFound = false
			// Check that the appender of initLogging() is found.
			for (def appender : appenders) {
				if (appender instanceof DailyRollingFileAppender) {
					doopAppenderFound = true
				} else if (!(appender instanceof ConsoleAppender)) {
					System.err.println("Warning: non-Doop appender found: " + appender.class)
				}
			}
			return !doopAppenderFound
		}
	}

	static synchronized void tryInitLogging(String logLevel, String logDir, boolean console) throws IOException {
		if (shouldInitializeLogging()) {
			initLogging(logLevel, logDir, console)
		} else {
			println "Logging already initialized."
		}
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
}
