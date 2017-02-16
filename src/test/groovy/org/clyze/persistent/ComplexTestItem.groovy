package org.clyze.persistent

import groovy.transform.EqualsAndHashCode

/**
 * Created by saiko on 9/11/2015.
 */
@EqualsAndHashCode
class ComplexTestItem extends TestItem {
    List<String> list
    Map<String, Integer> map
}
