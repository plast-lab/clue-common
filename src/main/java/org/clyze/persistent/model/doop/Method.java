package org.clyze.persistent.model.doop;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.clyze.persistent.model.Position;
import org.clyze.persistent.model.SymbolWithDoopId;

public class Method extends SymbolWithDoopId {

	private String name;

	private String returnType;

	/**
	 * The parameter names
	 */
	private String[] params;

	private String[] paramTypes;

	private boolean isStatic;

	private boolean isInterface;

	private boolean isAbstract;

	private boolean isNative;

	private String declaringClassDoopId;		

	/**
	 * The place where the method definition begins (including any annotations and modifiers) and ends (i.e. right after
	 * the closing brace). If the starting or ending line contain nothing else except the method definition and whitespaces,
	 * then the corresponding column is set to zero
	 *
	 * Note: Differs from the "position" field inherited from "Symbol" in that the latter refers to the starting and
	 *       ending positions of the method name inside its definition
	 *
	 * todo: Add a constructor parameter for this field too. For now we simply set its value with the auto-produced setter
	 */
	private Position outerPosition;

	public Method() {}

	public Method(String id) {
		this.id = id;
	}

	public Method(Position position, 
				  String sourceFileName, 
				  String name, 
				  String declaringClassDoopId, 
				  String returnType,
				  String doopId, 
				  String[] params, 
				  String[] paramTypes, 
				  boolean isStatic,
				  boolean isInterface,
				  boolean isAbstract,
				  boolean isNative,
				  Position outerPosition) {
		super(position, sourceFileName, doopId);
		this.name = name;
		this.declaringClassDoopId = declaringClassDoopId;
		this.returnType = returnType;		
		this.params = params;
		this.paramTypes = paramTypes;
		this.isStatic = isStatic;		
		this.isInterface = isInterface;
		this.isAbstract = isAbstract;
		this.isNative = isNative;
		this.outerPosition = outerPosition;
	}	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	public String[] getParamTypes() {
		return paramTypes;
	}

	public void setParamTypes(String[] paramTypes) {
		this.paramTypes = paramTypes;
	}

	public boolean getIsStatic() {
		return isStatic;
	}

	public void setIsStatic(boolean aStatic) {
		isStatic = aStatic;
	}

	public boolean getIsInterface() {
		return isInterface;
	}

	public void setIsInterface(boolean anInterface) {
		isInterface = anInterface;
	}

	public boolean getIsAbstract() {
		return isAbstract;
	}

	public void setIsAbstract(boolean anAbstract) {
		isAbstract = anAbstract;
	}

	public boolean getIisNative() {
		return isNative;
	}

	public void setIsNative(boolean aNative) {
		isNative = aNative;
	}

	public String getDeclaringClassDoopId() {
		return declaringClassDoopId;
	}

	public void setDeclaringClassDoopId(String declaringClassDoopId) {
		this.declaringClassDoopId = declaringClassDoopId;
	}

	public Position getOuterPosition() {
		return outerPosition;
	}

	public void setOuterPosition(Position outerPosition) {
		this.outerPosition = outerPosition;
	}

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Method method = (Method) object;
        /*
        return isStatic == method.isStatic &&
                isInterface == method.isInterface &&
                isAbstract == method.isAbstract &&
                isNative == method.isNative &&
                Objects.equals(name, method.name) &&
                Objects.equals(returnType, method.returnType) &&
                Arrays.equals(params, method.params) &&
                Arrays.equals(paramTypes, method.paramTypes) &&
                Objects.equals(declaringClassDoopId, method.declaringClassDoopId) &&
                Objects.equals(doopId, method.doopId) &&
                Objects.equals(outerPosition, method.outerPosition);
        */
        return Objects.equals(doopId, method.doopId);
    }

    public int hashCode() {
    	/*
        int result = Objects.hash(super.hashCode(), name, returnType, isStatic, isInterface, isAbstract, isNative, declaringClassDoopId, doopId, outerPosition);
        result = 31 * result + Arrays.hashCode(params);
        result = 31 * result + Arrays.hashCode(paramTypes);
        return result;
        */
        return Objects.hash(super.hashCode(), doopId);
    }

    protected void saveTo(Map<String, Object> map) {
		super.saveTo(map);
		map.put("name", this.name);
		map.put("returnType", this.returnType);
		map.put("params", this.params == null ? null : Arrays.asList(this.params));
		map.put("paramTypes", this.paramTypes == null ? null : Arrays.asList(this.paramTypes));
		map.put("isStatic", this.isStatic);
		map.put("isInterface", this.isInterface);
		map.put("isAbstract", this.isAbstract);
		map.put("isNative", this.isNative);
		map.put("declaringClassDoopId", this.declaringClassDoopId);	
	}

	protected void loadFrom(Map<String, Object> map){
		super.loadFrom(map);
		this.name                 = (String) map.get("name");
		this.returnType           = (String) map.get("returnType");
		this.params               = loadArray(map.get("params"));
		this.paramTypes           = loadArray(map.get("paramTypes"));
		this.isStatic             = (Boolean) map.get("isStatic");
		this.isInterface          = (Boolean) map.get("isInterface");
		this.isAbstract           = (Boolean) map.get("isAbstract");
		this.isNative             = (Boolean) map.get("isNative");
		this.declaringClassDoopId = (String) map.get("declaringClassDoopId");		
	}

	private static final String[] loadArray(Object o) {
		if (o == null) {
			return null;
		}

		List<String> values = (List<String>) o;
		return values.toArray(new String[values.size()]);
	}
}
