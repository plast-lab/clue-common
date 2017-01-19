package org.clyze.persistent

import org.clyze.persistent.ItemImpl

/**
 * A persistent element of a clue analysis.
 * Created by saiko on 9/11/2015.
 */
abstract class Element extends ItemImpl {

    //the element's id
    String id

    //required by the server side (safe to ignore them in the plugin)
    String anId
    String userId

    Element () {}
}
