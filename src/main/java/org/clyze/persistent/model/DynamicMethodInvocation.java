package org.clyze.persistent.model;

public class DynamicMethodInvocation {
    /**
     * Creates an instruction ID for an invokedynamic that calls a
     * lambda metafactory, passing it a method handle c::meth.
     *
     * @param c     method handle: class name
     * @param meth  method handle: method name
     * @return      the invokedynamic ID
     */
    public static String genId(String c, String meth) {
        return "invokedynamic_" + c + "::" + meth;
    }

    /**
     * Creates an instruction ID for a generic invokedynamic.
     *
     * @param bootName     the simple name of the boot method
     * @param dynamicName  the simple name of the dynamic method
     * @return             the invokedynamic ID
     */
    public static String genericId(String bootName, String dynamicName) {
        return "invokedynamic_" + bootName + "::" + dynamicName;
    }
}
