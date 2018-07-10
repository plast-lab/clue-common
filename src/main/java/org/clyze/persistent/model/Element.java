package org.clyze.persistent.model;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

/**
 * A persistent element of a clue analysis
 */
public abstract class Element extends ItemImpl {

	/**
	 * The element's id. Set by either a subclass or by external code
	 */
	private String id;

	/**
	 * The root element id -- the id of the "root element" this element belong to.
	 * Either the analysis or the bundle, depending on the type of this element.	 
	 */
	private String rootElemId;

	public Element() {}

	public String getId() { 
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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
        return Objects.equals(id, element.id) &&
               Objects.equals(rootElemId, element.rootElemId);
    }

    public int hashCode() {

        return Objects.hash(super.hashCode(), id, rootElemId);
    }

    protected void saveTo(Map<String, Object> map) {
		map.put("id", id);
		map.put("rootElemId", rootElemId);
	}

	protected void loadFrom(Map<String, Object> map){		
		this.id         = (String) map.get("id");
		this.rootElemId = (String) map.get("rootElemId");
	}
}
