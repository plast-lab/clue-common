package org.clyze.persistent.model;

import java.util.Map;
import java.util.HashMap;
import com.google.gson.Gson;

public abstract class ItemImpl implements Item {

	protected String id; //The unique identifier of the item (it is optional and not serialized)

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public ItemImpl fromJSON(String json) {
		Map<String, Object> map = new Gson().fromJson(json, Map.class);
		fromMap(map);
		return this;
	}

	@Override
	public String toJSON() { 
		return new Gson().toJson(toMap());
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>();
		saveTo(map);		
		return map;
	}

	protected abstract void saveTo(Map<String, Object> map);
}
