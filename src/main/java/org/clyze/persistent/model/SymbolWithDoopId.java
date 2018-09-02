package org.clyze.persistent.model;

import java.util.Map;
import java.util.Objects;

import static java.util.UUID.randomUUID;

/**
 * A symbol that has a doop id.
 */
public abstract class SymbolWithDoopId extends Symbol {

	protected String doopId;

	public SymbolWithDoopId() {}
	
	public SymbolWithDoopId(Position position, String sourceFileName, String doopId) {
		super(position, sourceFileName);
		this.doopId = doopId;
	}	

	public String getDoopId() {
		return doopId;
	}	

	public void setDoopId(String doopId) {
		this.doopId = doopId;
	}

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        SymbolWithDoopId symbol = (SymbolWithDoopId) object;
        return Objects.equals(doopId, symbol.doopId);
    }

    public int hashCode() {

        return Objects.hash(super.hashCode(), doopId);
    }

    protected void saveTo(Map<String, Object> map) {
		super.saveTo(map);
		map.put("doopId", this.doopId);
	}

	protected void loadFrom(Map<String, Object> map){
		super.loadFrom(map);
		this.doopId = (String) map.get("doopId");
	}
}
