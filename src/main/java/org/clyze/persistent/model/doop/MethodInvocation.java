package org.clyze.persistent.model.doop;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

import org.clyze.persistent.model.Position;
import org.clyze.persistent.model.SymbolWithDoopId;

public class MethodInvocation extends SymbolWithDoopId {

	private String name;

	private String invokingMethodDoopId;	

	/** is inside instance initializer block */
	private boolean inIIB = false;

    public MethodInvocation() {}

    public MethodInvocation(String id) {
        this.id = id;
    }

	public MethodInvocation(Position position, 
                            String sourceFileName, 
                            String name, 
                            String doopId, 
                            String invokingMethodDoopId,
                            boolean inIIB) {
		super(position, sourceFileName, doopId);
		this.name = name;		
		this.invokingMethodDoopId = invokingMethodDoopId;
		this.inIIB = inIIB;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInvokingMethodDoopId() {
        return invokingMethodDoopId;
    }

    public void setInvokingMethodDoopId(String invokingMethodDoopId) {
        this.invokingMethodDoopId = invokingMethodDoopId;
    }

    public boolean getInIIB() {
        return inIIB;
    }

    public void setInIIB(boolean inIIB) {
        this.inIIB = inIIB;
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        MethodInvocation that = (MethodInvocation) object;        
        return inIIB == that.inIIB &&
                Objects.equals(name, that.name) &&
                Objects.equals(invokingMethodDoopId, that.invokingMethodDoopId) &&
                Objects.equals(doopId, that.doopId);                    
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), name, invokingMethodDoopId, doopId, inIIB);       
    }

    protected void saveTo(Map<String, Object> map) {
		super.saveTo(map);
		map.put("name", this.name);
		map.put("invokingMethodDoopId", this.invokingMethodDoopId);     
		map.put("inIIB", this.inIIB);
	}

	protected void loadFrom(Map<String, Object> map){
		super.loadFrom(map);
		this.name                 = (String) map.get("name");
		this.invokingMethodDoopId = (String) map.get("invokingMethodDoopId");        
		this.inIIB                = (Boolean) map.get("inIIB");
	}
}
