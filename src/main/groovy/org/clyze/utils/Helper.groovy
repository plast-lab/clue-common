package org.clyze.utils

import groovy.transform.CompileStatic
import groovy.util.logging.Log4j
import java.lang.reflect.Method

/**
 * Various helper methods.
 */
@Log4j
@CompileStatic
class Helper {

	private static boolean DEBUG = false

	/**
	 * Executes the given Java main class using the supplied class
	 * loader. Returns false if the method throws an exception.
	 *
	 * @param cl		  the classloader
	 * @param mainClass	  the main class
	 * @param params	  the parameters of the main class
	 */
	static boolean execJava(ClassLoader cl, String mainClass, String[] params) {
		Class theClass = Class.forName(mainClass, true, cl)
		Method mainMethod = theClass.getMethod("main", [String[].class] as Class[])
        try {
            mainMethod.invoke(null, [params] as Object[])
            return true
        } catch (ex) {
			if (DEBUG)
				ex.printStackTrace()
            return false
        }
	}

	/**
	 * Alternate version of execJava() that executes a Java main class
	 * using a given classloader and passes exceptions to the
	 * caller. Note that since the caller may be using a different
	 * classloader, "instanceof A" checks on the exceptions may fail
	 * even if they are of type A (if "A" is loaded by the given
	 * classloader and the "A" in the caller context has been loaded
	 * by a different classloader).
	 *
	 * @param cl          the classloader
	 * @param mainClass   the main class
	 * @param params      the parameters of the main class
	 * @throws Exception  (see description above)
	 */
	static boolean execJavaNoCatch(ClassLoader cl, String mainClass, String[] params) throws Exception {
		Class theClass = Class.forName(mainClass, true, cl)
		Method mainMethod = theClass.getMethod("main", [String[].class] as Class[])
		mainMethod.invoke(null, [params] as Object[])
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
		} catch(e) {
			throw e
		}
		// We measure time only in error-free cases
		return ((System.currentTimeMillis() - now) / 1000).longValue()
	}

	/**
	 * Print elapsed time in seconds along with the given message.
	 */
	static void timingWithLogging(String message, Closure c) {
		// Do not simplify the following code or the closure may fail to run when
		// the logger is not configured with the "debug" level.
		long t = timing(c)
		if (log.isDebugEnabled())
			log.debug(message + " took ${t} sec")
	}

	/**
	 * Read the line of a seeds file which should refer to either a method or a constructor.	 
	 */
	static String readMethodDoopId(String seedFileLine) throws RuntimeException {
		
		//The seeds file notation does not use doopIds for constructors.
		//e.g. in a seeds file we have:
		//package.class$innerClass: class$innerClass(args...)
		//instead of: 
		//package.class$innerClass: void <init>(args...)
				
		int colonIndex     = seedFileLine.indexOf(':')
		int leftParenIndex = seedFileLine.indexOf('(')
		String fqcn        = seedFileLine.substring(0, colonIndex)
		String mn          = seedFileLine.substring(colonIndex + 2, leftParenIndex)
		return fqcn.endsWith(mn) ?
			   "<" + fqcn + ": void <init>" + seedFileLine.substring(leftParenIndex) + ">" :
			   "<" + seedFileLine + ">"
					
	}

	/**
	 * Replacement of Groovy's eachLine(), to work with large files.
	 */
	static void forEachLineIn(String path, Closure cl) {
		File file = new File(path)
		BufferedReader br = new BufferedReader(new FileReader(file))
		br.withCloseable {
			String line
			while ((line = it.readLine()) != null) { cl(line) }
		}
	}
}
