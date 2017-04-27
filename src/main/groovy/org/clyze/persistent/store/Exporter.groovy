package org.clyze.persistent.store

interface Exporter {

	void start()

    void export(String id, String type, String json)

    void end()
}
