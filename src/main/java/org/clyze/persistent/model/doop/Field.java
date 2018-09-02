package org.clyze.persistent.model.doop;

import org.clyze.persistent.model.Position;
import org.clyze.persistent.model.SymbolWithDoopId;

import java.util.Map;
import java.util.Objects;

public class Field extends SymbolWithDoopId {

	private String name;

	private String type;

	private boolean isStatic;

	private String declaringClassDoopId;	

    public Field() {}

    public Field(String id) {
        this.id = id;
    }
	
	public Field(Position position, 
                 String sourceFileName, 
                 String name, 
                 String doopId, 
                 String type, 
                 String declaringClassDoopId,
                 boolean isStatic) {
		super(position, sourceFileName, doopId);
		this.name = name;		
		this.type = type;
		this.declaringClassDoopId = declaringClassDoopId;
		this.isStatic = isStatic;
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

    public void setType(String type) {
        this.type = type;
    }

    public boolean getIsStatic() {
        return isStatic;
    }

    public void setIsStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public String getDeclaringClassDoopId() {
        return declaringClassDoopId;
    }

    public void setDeclaringClassDoopId(String declaringClassDoopId) {
        this.declaringClassDoopId = declaringClassDoopId;
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Field field = (Field) object;
        /*
        return isStatic == field.isStatic &&
                Objects.equals(name, field.name) &&
                Objects.equals(type, field.type) &&
                Objects.equals(declaringClassDoopId, field.declaringClassDoopId) &&
                Objects.equals(doopId, field.doopId);
        */
        return Objects.equals(doopId, field.doopId);
    }

    public int hashCode() {
        //return Objects.hash(super.hashCode(), name, type, isStatic, declaringClassDoopId, doopId);
        return Objects.hash(super.hashCode(), doopId);
    }

    protected void saveTo(Map<String, Object> map) {
		super.saveTo(map);
		map.put("name", this.name);
		map.put("type", this.type);
		map.put("isStatic", this.isStatic);
		map.put("declaringClassDoopId", this.declaringClassDoopId);		
	}

	protected void loadFrom(Map<String, Object> map){
		super.loadFrom(map);
		this.name                 = (String) map.get("name");
		this.type                 = (String) map.get("type");
		this.isStatic             = (Boolean) map.get("isStatic");
		this.declaringClassDoopId = (String) map.get("declaringClassDoopId");		
	}
}
