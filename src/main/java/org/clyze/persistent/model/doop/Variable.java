package org.clyze.persistent.model.doop;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

import org.clyze.persistent.model.Position;
import org.clyze.persistent.model.Symbol;

public class Variable extends Symbol {

	private String name;

	private String type;

	private boolean isLocal;

	private boolean isParameter;

	private String declaringMethodDoopId;

	private String doopId;

	/** is inside an instance initializer block */
	private boolean inIIB = false;

	public Variable() {}

	public Variable(Position position, 
					String sourceFileName, 
					String name, 
					String doopId, 
					String type, 
					String declaringMethodDoopId,
					boolean isLocal, 
					boolean isParameter, 
					boolean inIIB) {
		super(position, sourceFileName);
		this.name = name;
		this.doopId = doopId;
		this.type = type;
		this.declaringMethodDoopId = declaringMethodDoopId;
		this.isLocal = isLocal;
		this.isParameter = isParameter;
		this.inIIB = inIIB;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setIsLocal(boolean local) {
        isLocal = local;
    }

    public boolean getIsParameter() {
        return isParameter;
    }

    public void setIsParameter(boolean parameter) {
        isParameter = parameter;
    }

    public String getDeclaringMethodDoopId() {
        return declaringMethodDoopId;
    }

    public void setDeclaringMethodDoopId(String declaringMethodDoopId) {
        this.declaringMethodDoopId = declaringMethodDoopId;
    }

    public void setDoopId(String doopId) {
        this.doopId = doopId;
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
        Variable variable = (Variable) object;
        /*
        return isLocal == variable.isLocal &&
                isParameter == variable.isParameter &&
                inIIB == variable.inIIB &&
                Objects.equals(name, variable.name) &&
                Objects.equals(type, variable.type) &&
                Objects.equals(declaringMethodDoopId, variable.declaringMethodDoopId) &&
                Objects.equals(doopId, variable.doopId);
        */
        return Objects.equals(doopId, variable.doopId);
    }

    public int hashCode() {
        //return Objects.hash(super.hashCode(), name, type, isLocal, isParameter, declaringMethodDoopId, doopId, inIIB);
        return Objects.hash(super.hashCode(), doopId);
    }

    protected void saveTo(Map<String, Object> map) {
		super.saveTo(map);
		map.put("name", this.name);
		map.put("type", this.type);
		map.put("isLocal", this.isLocal);
		map.put("isParameter", this.isParameter);
		map.put("declaringMethodDoopId", this.declaringMethodDoopId);
		map.put("doopId", this.doopId);
		map.put("inIIB", this.inIIB);
	}

	protected void loadFrom(Map<String, Object> map){
		super.loadFrom(map);
		this.name                  = (String) map.get("name");
		this.type                  = (String) map.get("type");
		this.isLocal               = (Boolean) map.get("isLocal");
		this.isParameter           = (Boolean) map.get("isParameter");
		this.declaringMethodDoopId = (String) map.get("declaringMethodDoopId");
		this.doopId                = (String) map.get("doopId");
		this.inIIB                 = (Boolean) map.get("inIIB");
	}

	public String getDoopId() {
		return doopId;
	}

	public void setType(String type) {
		this.type = type;
	}
}
