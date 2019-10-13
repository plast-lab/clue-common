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

	public void fromMap(Map<String, Object> map) {
		this.id    = map.id
		this.aStr  = map.aStr
		this.anInt = map.anInt 
		this.aBool = map.aBool
	}

	protected void saveTo(Map<String, Object> map) {
		map.id    = this.id
		map.aStr  = this.aStr	
		map.anInt = this.anInt
		map.aBool = this.aBool
	}
}
