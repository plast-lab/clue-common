package org.clyze.persistent.model;

import java.util.Map;

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

    public boolean isInIIB() {
        return inIIB;
    }

    public void setInIIB(boolean inIIB) {
        this.inIIB = inIIB;
    }

    protected void saveTo(Map<String, Object> map) {
		super.saveTo(map);
		map.put("name", this.name);
		map.put("invokingMethodDoopId", this.invokingMethodDoopId);     
		map.put("inIIB", this.inIIB);
	}

	public void fromMap(Map<String, Object> map){
		super.fromMap(map);
		this.name                 = (String) map.get("name");
		this.invokingMethodDoopId = (String) map.get("invokingMethodDoopId");        
		this.inIIB                = (Boolean) map.get("inIIB");
	}
}
