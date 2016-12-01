package org.clyze.doop.persistent.elements
/**
 * Created by anantoni on 1/10/2015.
 */
class Position {

    long startLine
    long startColumn
    long endLine
    long endColumn

    Position() {}

    Position(long startLine, long startColumn, long endColumn) {
        this(startLine, startLine, startColumn, endColumn)
    }

    Position(long startLine, long endLine, long startColumn, long endColumn) {
        assert startLine <= endLine && startColumn <= endColumn : "Invalid symbol position"
        this.startLine = startLine
        this.endLine = endLine
        this.startColumn = startColumn
        this.endColumn = endColumn
    }
}
