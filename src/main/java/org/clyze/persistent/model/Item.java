package org.clyze.persistent.model;

import java.util.Map;

public interface Item {

	/**
	 * @return The item id
	 */
	String getId();

	/**
	 * Set the item id
	 */
	void setId(String id);	

	/**
	 * @param json The json data to populate the object
	 */
	Item fromJSON(String json);

	/**
	 * @return The object state in json representation
	 */
	String toJSON();

	/**
	 * @return The object state as a map [item property => property value]
	 */
	Map<String, Object> toMap();

	void fromMap(Map<String, Object> map);
}
