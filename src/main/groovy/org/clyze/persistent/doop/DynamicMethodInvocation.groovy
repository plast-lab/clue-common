package org.clyze.persistent.doop

class DynamicMethodInvocation {
	/**
	 * Creates an instruction ID for an invokedynamic that calls a
	 * lambda metafactory, passing it a method handle c::meth.
	 *
	 * @param c method handle: class name
	 * @param meth method handle: method name
	 * return          the invokedynamic ID
	 */
	static String genId(String c, String meth) {
		return "invokedynamic_${c}::${meth}"
	}
}
