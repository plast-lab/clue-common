package org.clyze.persistent

import groovy.transform.EqualsAndHashCode


/**
 *
 */
@EqualsAndHashCode
class Position {

    long startLine
    long startColumn
    long endLine
    long endColumn

    /**
     *
     */
    Position() {

    }

    /**
     * May be used when the symbol exists only in one line in the source code.
     * This is not always the case, e.g. in a "HeapAllocation" symbol, the "new"
     * and the class name may be in different lines
     *
     * @param startLine
     * @param startColumn
     * @param endColumn
     */
    Position(long line, long startColumn, long endColumn) {

        this(line, line, startColumn, endColumn)
    }

    /**
     *
     * @param startLine
     * @param endLine
     * @param startColumn
     * @param endColumn
     */
    Position(long startLine, long endLine, long startColumn, long endColumn) {

        assert startLine <= endLine && startColumn <= endColumn : "Invalid symbol position"

        this.startLine = startLine
        this.endLine = endLine
        this.startColumn = startColumn
        this.endColumn = endColumn
    }

}
