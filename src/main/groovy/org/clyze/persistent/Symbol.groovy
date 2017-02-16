package org.clyze.persistent

import org.clyze.persistent.doop.Position

import static java.util.UUID.randomUUID


/**
 * A symbol element that exists in source code
 */
abstract class Symbol extends Element {

    /**
     *
     */
    Position position

    /**
     *
     */
    String sourceFileName

    /**
     * Properties will be populated later
     */
    Symbol() {

    }

    /**
     *
     * @param position          The symbol position in the source code
     * @param sourceFileName    The source code file name
     */
    Symbol(Position position, String sourceFileName) {

        this.position = position
        this.sourceFileName = sourceFileName

        this.id = randomUUID().toString()
    }

}
