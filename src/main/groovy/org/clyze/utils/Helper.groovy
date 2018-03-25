package org.clyze.utils

import org.apache.commons.io.FilenameUtils
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.log4j.*

import java.lang.reflect.Method
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

/**
 * Various helper methods.
 */
class Helper {

	private static final Log logger = LogFactory.getLog(getClass())

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
		//This is a better way to invoke the main method using a different
		//classloader (the runWithClassLoader/invokeMainMethod methods are
		//problematic and should be removed).
		Class theClass = Class.forName(mainClass, true, cl)
		Method mainMethod = theClass.getMethod("main", [String[].class] as Class[])
        try {
            mainMethod.invoke(null, [params] as Object[])
            return true
        } catch (Throwable t) {
            return false;
        }
	}

	/**
	 * Runs the closure in the current thread using the specified class loader
	 * @param cl - the class loader to use
	 * @param closure - the closure to run
	 */
	static void runWithClassLoader(ClassLoader cl, Closure closure) {
		Thread currentThread = Thread.currentThread()
		ClassLoader oldLoader = currentThread.getContextClassLoader()
		currentThread.setContextClassLoader(cl)
		try {
			closure.call()
		} catch (e) {
			throw new RuntimeException(e.getMessage(), e)
		}
		finally {
			currentThread.setContextClassLoader(oldLoader)
		}
	}

	/**
	 * Invokes the main method of the given mainClass, passing the supplied params.
	 */
	static void invokeMainMethod(String mainClass, String[] params) {
		Class[] parameterTypes = [String[].class]
		Object[] args = [params]
		Method main = Class.forName(mainClass).getMethod("main", parameterTypes)
		main.invoke(null, args)
	}

	/**
	 * Returns a set of the packages contained in the given jar.
	 * Any classes that are not included in packages are also retrieved.
	 */
	static Set<String> getPackages(File jar) {
		ZipFile zip = new ZipFile(jar)
		Enumeration<? extends ZipEntry> entries = zip.entries()
		List<ZipEntry> classes = entries?.findAll { ZipEntry entry ->
			entry.getName().endsWith(".class")
		}
		List<String> packages = classes.collect { ZipEntry entry ->
			String entryName = entry.getName()
			if (entryName.indexOf("/") > 0)
				return FilenameUtils.getPath(entry.getName()).replace('/' as char, '.' as char) + '*'
			else
				return FilenameUtils.getBaseName(entryName)
		}

		packages = packages.unique()

		return (packages as Set)
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
	 *
	 * TODO: Return millisecond precision (either as long or double)
	 *       and if needed fix usages of this method in other repos.
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
	static void timingWithLogging(String message, Closure c) {
		logger.debug(message + " took ${timing(c)} sec")
	}

}
