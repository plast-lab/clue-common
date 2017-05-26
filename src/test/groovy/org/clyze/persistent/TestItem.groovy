package org.clyze.persistent

import groovy.transform.EqualsAndHashCode
import org.clyze.persistent.model.ItemImpl

/**
 * Created by saiko on 9/11/2015.
 */
@EqualsAndHashCode
class TestItem extends ItemImpl {
    String id
    String aStr
    int anInt
    boolean aBool
}
