package org.clyze.analysis

public final enum InputType {
    INPUT,
    LIBRARY,
    HPROF

    public String toString() {
        switch (this) {
        case INPUT  : return "input"
        case LIBRARY: return "library"
        case HPROF  : return "heap dump"
        }
        return "unknown input type"
    }
}
