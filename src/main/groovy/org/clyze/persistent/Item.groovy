package org.clyze.persistent


/**
 *
 */
interface Item {

    /**
     *
     * @return      The item id
     */
    String getId()

    /**
     *
     * @param json  The json data to populate the object
     */
    void fromJSON(String json)

    /**
     *
     * @return      The object state in json representation
     */
    String toJSON()

    /**
     *
     * @return      The object state as a map [item property => property value]
     */
    Map<String, Object> toMap()

}
