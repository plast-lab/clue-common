package org.clyze.persistent.model;

import org.clyze.persistent.model.Position;
import org.clyze.persistent.model.SymbolWithDoopId;

import java.util.*;

/**
 * An annotateable symbol with doop id (a class/type, a field or a method).
 */
public class AnnotateableSymbolWithDoopId extends SymbolWithDoopId {

    private Set<String> annotationTypes = new HashSet<>();

    public AnnotateableSymbolWithDoopId() {}

    public AnnotateableSymbolWithDoopId(Position position, String sourceFileName, String doopId) {
        super(position, sourceFileName, doopId);
    }

    public Set<String> getAnnotationTypes() {
        return annotationTypes;
    }

    public void setAnnotationTypes(Set<String> annotationTypes) {
        this.annotationTypes = annotationTypes;
    }

    protected void saveTo(Map<String, Object> map) {
        super.saveTo(map);
        map.put("annotationTypes", this.annotationTypes);
    }

    @SuppressWarnings("unchecked")
    public void fromMap(Map<String, Object> map){
        super.fromMap(map);
        this.annotationTypes = new HashSet<>((Collection<String>) map.get("annotationTypes"));
    }
}
