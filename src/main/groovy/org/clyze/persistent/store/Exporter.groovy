package org.clyze.persistent.store

interface Exporter {
    void export(String id, String type, String json)
}
