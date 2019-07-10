package org.clyze.persistent.model.doop;

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
    protected void loadFrom(Map<String, Object> map){
        super.loadFrom(map);
        Set<String> annotations = new HashSet<>();
        annotations.addAll((Collection<String>) map.get("annotationTypes"));
        this.annotationTypes = annotations;
    }
}