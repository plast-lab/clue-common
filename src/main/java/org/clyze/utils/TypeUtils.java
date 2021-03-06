package org.clyze.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TypeUtils {
    ;

    private static final String BOOLEAN = "boolean";
    private static final String BOOLEAN_JVM = "Z";
    private static final String INT = "int";
    private static final String INT_JVM = "I";
    private static final String LONG = "long";
    private static final String LONG_JVM = "J";
    private static final String DOUBLE = "double";
    private static final String DOUBLE_JVM = "D";
    private static final String VOID = "void";
    private static final String VOID_JVM = "V";
    private static final String FLOAT = "float";
    private static final String FLOAT_JVM = "F";
    private static final String CHAR = "char";
    private static final String CHAR_JVM = "C";
    private static final String SHORT = "short";
    private static final String SHORT_JVM = "S";
    private static final String BYTE = "byte";
    private static final String BYTE_JVM = "B";

    private static final boolean DEBUG = false;

    private static final Map<String, String> cachedRaisedTypes = new ConcurrentHashMap<>();

    private static final Pattern slashPat = Pattern.compile("/", Pattern.LITERAL);
    private static final String dotRepl = Matcher.quoteReplacement(".");

    /**
     * Converts a type id of the form 'La/b/C;' to 'a.b.C'.
     * @param id     the type id
     * @return       the fixed type id
     */
    public static String raiseTypeId(String id) {
        String cached = cachedRaisedTypes.get(id);
        if (cached != null)
            return cached;

        int typePrefixEndIdx = 0;
        // Peel off array brackets.
        while (id.charAt(typePrefixEndIdx) == '[')
            typePrefixEndIdx++;

        StringBuilder sb;
        if ((id.charAt(typePrefixEndIdx) == 'L') && (id.charAt(id.length() -1) == ';'))
            sb = new StringBuilder(replaceSlashesWithDots(id.substring(typePrefixEndIdx + 1, id.length() - 1)));
        else
            sb = new StringBuilder(decodePrimType(id.substring(typePrefixEndIdx)));

        if (typePrefixEndIdx != 0) {
            for (int i = 0; i < typePrefixEndIdx; i++)
                sb.append("[]");
        }

        String ret = sb.toString();

        // Find multidimensional arrays in bytecode (e.g. '[[C' / 'char[][]') .
//        if (typePrefixEndIdx > 1) {
//            System.err.println("WARNING: found multidimensional array type: " + id + " -> " + ret);
//        }

        cachedRaisedTypes.put(id, ret);
        return ret;
    }

    /**
     * Raises a JVM signature to a list of human-readable strings.
     *
     * @param sig   The low-level JVM signature.
     * @return      A list of raised types (return type first, argument types follow).
     */
    public static List<String> raiseSignature(String sig) {
        int lParenIdx = sig.indexOf('(');
        int rParenIdx = sig.indexOf(')');

        if (lParenIdx < 0 || rParenIdx < 0)
            throw new RuntimeException("Malformed JVM signature found: " + sig);

        List<String> ret = new LinkedList<>();
        ret.add(raiseTypeId(sig.substring(rParenIdx + 1)));

        boolean array = false;
        int pos = lParenIdx + 1;
        while (pos < rParenIdx) {
            String ch = sig.substring(pos, pos + 1);
            try {
                ret.add(decodePrimType(ch) + (array ? "[]" : ""));
                array = false;
                pos++;
                continue;
            } catch (RuntimeException ex) {
                if (DEBUG)
                    ex.printStackTrace();
            }
            if (ch.equals("L")) {
                int semiPos = sig.indexOf(';', pos);
                if (semiPos >= 0) {
                    ret.add(raiseTypeId(sig.substring(pos, semiPos + 1)) + (array ? "[]" : ""));
                    array = false;
                    pos = semiPos + 1;
                    continue;
                }
            } else if (ch.equals("[")) {
                array = true;
                pos++;
                continue;
            }
            // When all else fails
            throw new RuntimeException("Could not raise signature: " + sig + ", problem at string position " + pos);
        }
        return ret;
    }

    private static String decodePrimType(String id) {
        switch (id) {
            case BOOLEAN_JVM : return BOOLEAN;
            case INT_JVM     : return INT;
            case LONG_JVM    : return LONG;
            case DOUBLE_JVM  : return DOUBLE;
            case VOID_JVM    : return VOID;
            case FLOAT_JVM   : return FLOAT;
            case CHAR_JVM    : return CHAR;
            case SHORT_JVM   : return SHORT;
            case BYTE_JVM    : return BYTE;
            default          : throw new RuntimeException("Invalid type id format: " + id);
        }
    }

    public static boolean isPrimitiveType(String s) {
        return (s.equals(BOOLEAN) || s.equals(INT) || s.equals(LONG) ||
                s.equals(DOUBLE) || s.equals(VOID) || s.equals(FLOAT) ||
                s.equals(CHAR) || s.equals(SHORT) || s.equals(BYTE));
    }

    public static boolean isLowLevelType(char first, String s) {
        return first == '[' || (first == 'L' && s.endsWith(";"));
    }

    public static String replaceSlashesWithDots(CharSequence s) {
        return slashPat.matcher(s).replaceAll(dotRepl);
    }

}
