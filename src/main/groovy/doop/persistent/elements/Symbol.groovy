package doop.persistent.elements

import doop.persistent.ItemImpl
import static java.util.UUID.*

/**
 * Created by anantoni on 1/10/2015.
 */
abstract class Symbol extends ItemImpl {
    String id;
    Position position;
    String sourceFIleName;

    Symbol() {}

    Symbol(Position position, String sourceFIleName) {
        this.position = position;
        this.sourceFIleName = sourceFIleName;
        this.id = randomUUID().toString();
    }
}
