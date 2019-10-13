package org.clyze.persistent.model;

import java.util.Map;
import java.util.Objects;

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
		if (!(object instanceof SymbolWithDoopId)) return false;
		SymbolWithDoopId symbol = (SymbolWithDoopId) object;

		return super.equals(symbol)
			&& Objects.equals(doopId, symbol.doopId);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), doopId);
    }

    protected void saveTo(Map<String, Object> map) {
		super.saveTo(map);
		map.put("doopId", this.doopId);
	}

	public void fromMap(Map<String, Object> map){
		super.fromMap(map);
		this.doopId = (String) map.get("doopId");
	}
}
