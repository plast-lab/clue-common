package org.clyze.persistent.model.doop

import groovy.transform.EqualsAndHashCode
import groovy.transform.InheritConstructors
import org.clyze.persistent.model.Position
import org.clyze.persistent.model.Symbol

/**
 * This class represents a string constant; i.e. a pair of a static final String
 * field and the string literal initializing it.
 *
 * Why we need this?
 * Javac chooses to inline some cases of static final fields. Such fields do not
 * appear in the bytecode and therefore are not analyzed. What this class does
 * is to provide a "possible value" for such fields.
 */
@EqualsAndHashCode(callSuper = true)
@InheritConstructors
class StringConstant extends Symbol {

    /** The doopId of the static final field that this string literal initializes. */
    String fieldDoopId

    /** The string literal value.
     *
     *  This value may differ from the corresponding source code, since it is
     *  intentionally retrieved after the compiler has applied any possible
     *  concatenations; e.g. for the string literal in the rhs of:
     *      static final String str = "abc" + "def";
     *  javac will give us "abcdef", which we store in "value".
     *
     *  Note that the reported position corresponds to the source code and not
     *  the concatenated string generated by the compiler.
     */
    String value

    /**
     *
     * @param position
     * @param sourceFileName
     * @param fieldDoopId
     */
    public StringConstant(Position position, String sourceFileName, String fieldDoopId, String value) {

        super(position, sourceFileName)

        this.fieldDoopId = fieldDoopId
        this.value = value
    }

}