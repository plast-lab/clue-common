package doop.persistent

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * Created by saiko on 24/8/2015.
 */
abstract class ItemImpl implements Item {

    static final String ID_FIELD = "id"

    Date created, modified

    @Override
    String getId() {
        return this[ID_FIELD]
    }

    @Override
    void fromJSON(String json) {
        def map = new JsonSlurper().parseText(json)
        map.each { String key, Object value ->
            this[key] = value
        }
    }

    @Override
    String toJSON() {
        def map = [:]
        this.class.declaredFields.findAll { !it.synthetic }.each {
            map[it.name] = this[it.name]
        }
        return JsonOutput.toJson(map)
    }
}
