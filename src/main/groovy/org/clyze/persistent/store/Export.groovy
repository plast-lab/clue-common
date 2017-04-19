package org.clyze.persistent.store

class Export {

    private final DataStore store
    private final Map<String, Object> filters
    private final Exporter exporter

    Export(DataStore store, Map<String, Object> filters, Exporter exporter) {
        this.store = store
        this.filters = filters
        this.exporter = exporter
    }


    void export() {
        store.scan(filters, exporter.&export)
    }
}
