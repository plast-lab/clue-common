package doop.persistent.elements

import doop.persistent.ItemImpl
import static java.util.UUID.*

/**
 * Created by anantoni on 1/10/2015.
 */
abstract class Symbol extends ItemImpl {
    String id;
    Position position;
    String sourceFileName;

    //required by the server side (safe to ignore them in the plugin)
    String an_id;
    String userId;

    Symbol() {}

    Symbol(Position position, String sourceFileName) {
        this.position = position;
        this.sourceFileName = sourceFileName;
        this.id = randomUUID().toString();
    }
}
