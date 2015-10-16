package doop.persistent.elements

/**
 * Created by anantoni on 2/10/2015.
 */
class Field extends Symbol {
    String signature;
    String type;
    String enclosingClassUUID;
    boolean isStatic;

    public Field() {}

    public Field(Position position, String sourceFileName, String signature, String type, String enclosingClassUUID, boolean isStatic) {

        super(position, sourceFileName);
        this.signature = signature;
        this.type = type;
        this.enclosingClassUUID = enclosingClassUUID;
        this.isStatic = isStatic;
    }

}
