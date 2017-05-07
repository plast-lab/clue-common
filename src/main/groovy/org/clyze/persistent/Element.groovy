package org.clyze.persistent

/**
 * A persistent element of a clue analysis
 */
abstract class Element extends ItemImpl {

    /**
     * The element's id. Set by either a subclass or by external code
     */
    String id

    /**
     * The analysis id this element belong to.
     * (required by clue-server, safe to ignore in doop-jcplugin)
     */
    String anId

    /**
     * The id of the user that created the analysis of id "anId"
     * (required by clue-server, safe to ignore in doop-jcplugin)
     */
    String userId

    Element() {}

    Element(String json) {
        fromJSON(json)
    }
}
