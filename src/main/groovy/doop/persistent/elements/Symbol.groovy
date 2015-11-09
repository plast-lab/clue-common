package doop.persistent.elements

import static java.util.UUID.randomUUID

/**
 * Created by anantoni on 1/10/2015.
 */
abstract class Symbol extends Element {

    Position position;
    String sourceFileName;

    Symbol() {}

    Symbol(Position position, String sourceFileName) {
        this.position = position;
        this.sourceFileName = sourceFileName;
        this.id = randomUUID().toString();
    }
}
