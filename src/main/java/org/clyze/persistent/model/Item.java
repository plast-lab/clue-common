package org.clyze.persistent.model;

import java.util.Map;

public interface Item {

	/**
	 * @return The item id
	 */
	String getId();

	/**
	 * Set the item id
	 *
	 * @param id   the item id
	 */
	void setId(String id);	

	/**
	 * @param json   the JSON data to populate the object
	 *
	 * @return		 the instance that corresponds to the JSON data
	 */
	Item fromJSON(String json);

	/**
	 * @return the object state in JSON representation
	 */
	String toJSON();

	/**
	 * @return the object state as a map from item properties to property values
	 */
	Map<String, Object> toMap();

	void fromMap(Map<String, Object> map);
}
