package org.clyze.persistent.model;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

/**
 * A persistent element of a clue analysis
 */
public abstract class Element extends ItemImpl {	

	/**
	 * The root element id -- the id of the "root element" this element belong to.
	 * Either the analysis or the bundle, depending on the type of this element.	 
	 */
	private String rootElemId;

	public Element() {}		

	public String getRootElemId() {
		return rootElemId;
	}

	public void setRootElemId(String rootElemId) {
		this.rootElemId = rootElemId;
	}

	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		if (!super.equals(object)) return false;
		Element element = (Element) object;
		return Objects.equals(rootElemId, element.rootElemId);
	}

	public int hashCode() {
		return Objects.hash(super.hashCode(), rootElemId);
	}

	protected void saveTo(Map<String, Object> map) {		
		//We don't serialize the id
		map.put("rootElemId", rootElemId);
	}

	protected void loadFrom(Map<String, Object> map){				
		this.rootElemId = (String) map.get("rootElemId");
	}
}