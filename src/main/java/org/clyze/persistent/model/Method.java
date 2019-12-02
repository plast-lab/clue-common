package org.clyze.persistent.model;

import java.util.Map;
import java.util.Arrays;
import java.util.List;

public class Method extends AnnotateableSymbolWithDoopId {

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
	private boolean isSynchronized;
	private boolean isFinal;
	private boolean isSynthetic;
	private boolean isPublic;
	private boolean isProtected;
	private boolean isPrivate;

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
				  boolean isSynchronized,
				  boolean isFinal,
				  boolean isSynthetic,
				  boolean isPublic,
				  boolean isProtected,
				  boolean isPrivate,
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
		this.isSynchronized = isSynchronized;
		this.isFinal = isFinal;
		this.isSynthetic = isSynthetic;
		this.isPublic = isPublic;
		this.isProtected = isProtected;
		this.isPrivate = isPrivate;
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

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean aStatic) {
		isStatic = aStatic;
	}

	public boolean isInterface() {
		return isInterface;
	}

	public void setInterface(boolean anInterface) {
		isInterface = anInterface;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean anAbstract) {
		isAbstract = anAbstract;
	}

	public boolean isNative() {
		return isNative;
	}

	public void setNative(boolean aNative) {
		isNative = aNative;
	}

	public boolean isSynchronized() { return isSynchronized; }

	public void setSynchronized(boolean aSynchronized) { isSynchronized = aSynchronized; }

	public boolean isFinal() { return isFinal; }

	public void setFinal(boolean aFinal) { isFinal = aFinal; }

	public boolean isSynthetic() { return isSynthetic; }

	public void setSynthetic(boolean synthetic) { isSynthetic = synthetic;}

	public boolean isPublic() { return isPublic; }

	public void setPublic(boolean aPublic) {  isPublic = aPublic;  }

	public boolean isProtected() { return isProtected; }

	public void setProtected(boolean aProtected) {  isProtected = aProtected; }

	public boolean isPrivate() {  return isPrivate;  }

	public void setPrivate(boolean aPrivate) { isPrivate = aPrivate; }

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
		map.put("isSynchronized", this.isSynchronized);
		map.put("isFinal", this.isFinal);
		map.put("isSynthetic", this.isSynthetic);
		map.put("isPublic", this.isPublic);
		map.put("isProtected", this.isProtected);
		map.put("isPrivate", this.isPrivate);
		map.put("declaringClassDoopId", this.declaringClassDoopId);	
	}

	public void fromMap(Map<String, Object> map){
		super.fromMap(map);
		this.name                 = (String) map.get("name");
		this.returnType           = (String) map.get("returnType");
		this.params               = loadArray(map.get("params"));
		this.paramTypes           = loadArray(map.get("paramTypes"));
		this.isStatic             = (Boolean) map.get("isStatic");
		this.isInterface          = (Boolean) map.get("isInterface");
		this.isAbstract           = (Boolean) map.get("isAbstract");
		this.isNative             = (Boolean) map.get("isNative");
		this.isFinal              = (Boolean) map.get("isFinal");
		this.isSynthetic          = (Boolean) map.get("isSynthetic");
		this.isPublic             = (Boolean) map.get("isPublic");
		this.isProtected          = (Boolean) map.get("isProtected");
		this.isPrivate            = (Boolean) map.get("isPrivate");
		this.declaringClassDoopId = (String) map.get("declaringClassDoopId");		
	}

	private static String[] loadArray(Object o) {
		if (o == null) {
			return null;
		}

		List<String> values = (List<String>) o;
		return values.toArray(new String[0]);
	}
}
