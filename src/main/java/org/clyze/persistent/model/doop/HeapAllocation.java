package org.clyze.persistent.model.doop;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

import org.clyze.persistent.model.Position;
import org.clyze.persistent.model.SymbolWithDoopId;

public class HeapAllocation extends SymbolWithDoopId {

	private String allocatedTypeDoopId;

	private String allocatingMethodDoopId;	

	/** is inside instance initializer block */
	private boolean inIIB = false;

	/** is an array type */
	private boolean isArray = false;

    public HeapAllocation() {}

    public HeapAllocation(String id) {
        this.id = id;
    }
	
	public HeapAllocation(Position position, 
                          String sourceFileName, 
                          String doopId, 
                          String allocatedTypeDoopId, 
                          String allocatingMethodDoopId,
                          boolean inIIB, 
                          boolean isArray) {
		super(position, sourceFileName, doopId);		
		this.allocatedTypeDoopId = allocatedTypeDoopId;
		this.allocatingMethodDoopId = allocatingMethodDoopId;
		this.inIIB = inIIB;
		this.isArray = isArray;
	}

    public String getAllocatedTypeDoopId() {
        return allocatedTypeDoopId;
    }

    public void setAllocatedTypeDoopId(String allocatedTypeDoopId) {
        this.allocatedTypeDoopId = allocatedTypeDoopId;
    }

    public String getAllocatingMethodDoopId() {
        return allocatingMethodDoopId;
    }

    public void setAllocatingMethodDoopId(String allocatingMethodDoopId) {
        this.allocatingMethodDoopId = allocatingMethodDoopId;
    }
    
    public boolean getInIIB() {
        return inIIB;
    }

    public void setInIIB(boolean inIIB) {
        this.inIIB = inIIB;
    }

    public boolean getIsArray() {
        return isArray;
    }

    public void setIsArray(boolean array) {
        isArray = array;
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        HeapAllocation that = (HeapAllocation) object;
        return inIIB == that.inIIB &&
                isArray == that.isArray &&
                Objects.equals(allocatedTypeDoopId, that.allocatedTypeDoopId) &&
                Objects.equals(allocatingMethodDoopId, that.allocatingMethodDoopId) &&
                Objects.equals(doopId, that.doopId);
    }

    public int hashCode() {

        return Objects.hash(super.hashCode(), allocatedTypeDoopId, allocatingMethodDoopId, doopId, inIIB, isArray);
    }

    protected void saveTo(Map<String, Object> map) {
		super.saveTo(map);
		map.put("allocatedTypeDoopId", this.allocatedTypeDoopId);
		map.put("allocatingMethodDoopId", this.allocatingMethodDoopId);		
		map.put("inIIB", this.inIIB);
		map.put("isArray", this.isArray);
	}

	protected void loadFrom(Map<String, Object> map){
		super.loadFrom(map);
		this.allocatedTypeDoopId    = (String) map.get("allocatedTypeDoopId");
		this.allocatingMethodDoopId = (String) map.get("allocatingMethodDoopId");		
		this.inIIB                  = (Boolean) map.get("inIIB");
		this.isArray                = (Boolean) map.get("isArray");
	}
}
