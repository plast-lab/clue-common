package org.clyze.persistent

import groovy.transform.EqualsAndHashCode

/**
 * Created by saiko on 9/11/2015.
 */
@EqualsAndHashCode
class ComplexTestItem extends TestItem {
    List<String> list
    Map<String, Integer> map

    protected void fromMap(Map<String, Object> map) {
    	super.fromMap(map)
		this.list  = map.list
		this.map   = map.map		
	}

	protected void saveTo(Map<String, Object> map) {
		super.saveTo(map)
		map.list = this.list
		map.map  = this.map
	}

}
