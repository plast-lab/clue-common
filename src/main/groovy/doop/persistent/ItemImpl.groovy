package doop.persistent

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.transform.EqualsAndHashCode

/**
 * Created by saiko on 24/8/2015.
 */
@EqualsAndHashCode abstract class ItemImpl implements Item {

    static final String ID_FIELD = "id"

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
        /*
        def map = [:]
        this.class.declaredFields.findAll { !it.synthetic }.each {
            map[it.name] = this[it.name]
        }
        return JsonOutput.toJson(map)
        */
        return JsonOutput.toJson(properties.findAll{ String key, Object value ->
            value != null && key != "class" && key != "ID_FIELD"
        })
    }
}
