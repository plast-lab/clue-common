package org.clyze.persistent.model;

import java.util.Map;
import java.util.HashMap;
import com.google.gson.Gson;

public abstract class ItemImpl implements Item {

	public ItemImpl fromJSON(String json) {
		Map<String, Object> map = new Gson().fromJson(json, Map.class);
		loadFrom(map);
		return this;
	}

	public String toJSON() { 
		return new Gson().toJson(toMap());
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>();
		saveTo(map);		
		return map;
	}

	protected abstract void loadFrom(Map<String, Object> map);

	protected abstract void saveTo(Map<String, Object> map);
}
