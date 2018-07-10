package org.clyze.persistent.model;

import java.util.Objects;

public class Position {

	private long startLine;
	private long startColumn;
	private long endLine;
	private long endColumn;

	public Position() {}

	/**
	 * May be used when the symbol exists only in one line in the source code.
	 * This is not always the case, e.g. in a "HeapAllocation" symbol, the "new"
	 * and the class name may be in different lines
	 *
	 * @param startLine
	 * @param startColumn
	 * @param endColumn
	 */
	public Position(long line, long startColumn, long endColumn) {
		this(line, line, startColumn, endColumn);
	}

	/**
	 * @param startLine
	 * @param endLine
	 * @param startColumn
	 * @param endColumn
	 */
	public Position(long startLine, long endLine, long startColumn, long endColumn) {
		//TODO: Either this check should be performed by the caller or a new Factory method should be introduced
		//assert startLine < endLine || (startLine == endLine && startColumn <= endColumn): "Invalid symbol position"

		this.startLine = startLine;
		this.endLine = endLine;
		this.startColumn = startColumn;
		this.endColumn = endColumn;
	}

	public long getStartLine() {
		return startLine;
	}

	public long getStartColumn() {
		return startColumn;
	}

	public long getEndLine() {
		return endLine;
	}

	public long getEndColumn() {
		return endColumn;
	}

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Position position = (Position) object;
        return startLine == position.startLine &&
                startColumn == position.startColumn &&
                endLine == position.endLine &&
                endColumn == position.endColumn;
    }

    public int hashCode() {

        return Objects.hash(super.hashCode(), startLine, startColumn, endLine, endColumn);
    }
}
