package doop.persistent.elements

import doop.persistent.ItemImpl

/**
 * Created by anantoni on 1/10/2015.
 */
abstract class Symbol extends ItemImpl {
    Position position;
    String compilationUnit;

    Symbol() {}

    Symbol(Position position, String compilationUnit) {
        this.position = position;
        this.compilationUnit = compilationUnit;
    }
}
