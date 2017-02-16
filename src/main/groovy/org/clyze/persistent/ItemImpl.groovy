package org.clyze.persistent

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * Created by saiko on 24/8/2015.
 */
abstract class ItemImpl implements Item {

    static final String ID_FIELD = "id"

    @Override
    String getId() {
        return this[ID_FIELD]
    }

    @Override
    Map<String, Object> toMap() {
        return properties.findAll{ String key, Object value ->
            value != null && key != "class" && key != "ID_FIELD"
        }
    }

    @Override
    void fromJSON(String json) {
        def map = new JsonSlurper().parseText(json)
        map.each { String key, Object value ->
            if (this.properties.containsKey(key)) {
                this[key] = value
            }
        }
    }

    @Override
    String toJSON() {
        return JsonOutput.toJson(toMap())
    }
}
