package doop.persistent.elements

import doop.persistent.ItemImpl

/**
 * Created by anantoni on 1/10/2015.
 */
class Position extends ItemImpl{

    long startLine;
    long startColumn;
    long endLine;
    long endColumn;

    public Position() {}

    public Position(long startLine, long startColumn, long endColumn) {
        this(startLine, startLine, startColumn, endColumn)
    }

    public Position(long startLine, long endLine, long startColumn, long endColumn) {
        this.startLine = startLine;
        this.endLine = endLine;
        this.startColumn = startColumn;
        this.endColumn = endColumn;
    }
}
