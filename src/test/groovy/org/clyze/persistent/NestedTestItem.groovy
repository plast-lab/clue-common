package org.clyze.persistent

import groovy.transform.EqualsAndHashCode

/**
 * Created by saiko on 10/11/2015.
 */
@EqualsAndHashCode
class NestedTestItem extends ComplexTestItem {
    TestClass nested

    public void fromMap(Map<String, Object> map) {
    	super.fromMap(map)
    	def nestedMap = map.nested
    	def nested = new TestClass()
    	nested.memberStr = nestedMap.memberStr
    	nested.memberInt = nestedMap.memberInt
		this.nested = nested	
	}

	protected void saveTo(Map<String, Object> map) {
		super.saveTo(map)
		map.nested = [
			memberStr: nested.memberStr,
			memberInt: nested.memberInt		
		]
	}
}
