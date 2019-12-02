package org.clyze.persistent.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Symbol used for classes, interfaces and enums
 */
public class Class extends AnnotateableSymbolWithDoopId {

	/**
	 * The symbol name (package name not included)
	 */
	private String name;
	
	/** The artifact name (e.g. foo-1.2.jar) */
	private String artifactName;

	private String packageName;

	/**
	 * Various flags determining the symbol type
	 * (possibly change this to bitmap)
	 */
	private boolean isInterface;
	private boolean isEnum;
	private boolean isStatic;
	private boolean isInner;
	private boolean isAnonymous;
	private boolean isAbstract;
	private boolean isFinal;
	private boolean isPublic;
	private boolean isProtected;
	private boolean isPrivate;

	/**
	 * The doopId of the type or method where this type is declared.
	 */
	private String declaringSymbolDoopId;

	private long sizeInBytes;

	/**
	 * A list of fully qualified type names (classes, interfaces) that
	 * this type (class, enum or interface) extends or implements.
	 */
	private List<String> superTypes = new ArrayList<>();

	public Class() {}

	public Class(String id) {
		this.id = id;
	}
	
	public Class(Position position, 
				 String sourceFileName, 
				 String name, 
				 String packageName, 
				 String doopId, 
				 boolean isInterface, 
				 boolean isEnum, 
				 boolean isStatic, 
				 boolean isInner, 
				 boolean isAnonymous, 
				 boolean isAbstract,
				 boolean isFinal,
				 boolean isPublic,
				 boolean isProtected,
				 boolean isPrivate) {
		super(position, sourceFileName, doopId);
		this.name = name;
		this.packageName = packageName;		
		this.isInterface = isInterface;
		this.isEnum = isEnum;
		this.isStatic = isStatic;
		this.isInner = isInner;
		this.isAnonymous = isAnonymous;
		this.isAbstract = isAbstract;
		this.isFinal = isFinal;
		this.isPublic = isPublic;
		this.isProtected = isProtected;
		this.isPrivate = isPrivate;
	}	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}	

	public String getArtifactName() {
		return artifactName;
	}

	public void setArtifactName(String artifactName) {
		this.artifactName = artifactName;
	}

	public boolean isInterface() {
		return isInterface;
	}

	public void setInterface(boolean anInterface) {
		isInterface = anInterface;
	}

	public boolean isEnum() {
		return isEnum;
	}

	public void setEnum(boolean anEnum) {
		isEnum = anEnum;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean aStatic) {
		isStatic = aStatic;
	}

	public boolean isInner() {
		return isInner;
	}

	public void setInner(boolean inner) {
		isInner = inner;
	}

	public boolean isAnonymous() {
		return isAnonymous;
	}

	public void setAnonymous(boolean anonymous) {
		isAnonymous = anonymous;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean anAbstract) {
		isAbstract = anAbstract;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean aFinal) {
		isFinal = aFinal;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean aPublic) {
		isPublic = aPublic;
	}

	public boolean isProtected() {
		return isProtected;
	}

	public void setProtected(boolean aProtected) {
		isProtected = aProtected;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean aPrivate) {
		isPrivate = aPrivate;
	}

	public String getDeclaringSymbolDoopId() {
		return declaringSymbolDoopId;
	}

	public void setDeclaringSymbolDoopId(String declaringSymbolDoopId) {
		this.declaringSymbolDoopId = declaringSymbolDoopId;
	}

	public long getSizeInBytes() {
		return sizeInBytes;
	}

	public void setSizeInBytes(long sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
	}

	public List<String> getSuperTypes() {
		return superTypes;
	}

	public void setSuperTypes(List<String> superTypes) {
		this.superTypes = superTypes;
	}

	protected void saveTo(Map<String, Object> map) {
		super.saveTo(map);
		map.put("name", this.name);
		map.put("artifactName", this.artifactName);
		map.put("packageName", this.packageName);
		map.put("isInterface", this.isInterface);
		map.put("isEnum", this.isEnum);
		map.put("isStatic", this.isStatic);
		map.put("isInner", this.isInner);
		map.put("isAnonymous", this.isAnonymous);
		map.put("isAbstract", this.isAbstract);
		map.put("isFinal", this.isFinal);
		map.put("isPublic", this.isPublic);
		map.put("isProtected", this.isProtected);
		map.put("isPrivate", this.isPrivate);
		map.put("declaringSymbolDoopId", this.declaringSymbolDoopId);
		map.put("sizeInBytes", this.sizeInBytes);
		map.put("superTypes", this.superTypes);
	}

	@SuppressWarnings("unchecked")
	public void fromMap(Map<String, Object> map){
		super.fromMap(map);
		this.name                  = (String) map.get("name");
		this.artifactName          = (String) map.get("artifactName");
		this.packageName           = (String) map.get("packageName");
		this.isInterface           = (Boolean) map.get("isInterface");
		this.isEnum                = (Boolean) map.get("isEnum");
		this.isStatic              = (Boolean) map.get("isStatic");
		this.isInner               = (Boolean) map.get("isInner");
		this.isAnonymous           = (Boolean) map.get("isAnonymous");
		this.isAbstract            = (Boolean) map.get("isAbstract");
		this.isFinal               = (Boolean) map.get("isFinal");
		this.isPublic              = (Boolean) map.get("isPublic");
		this.isProtected           = (Boolean) map.get("isProtected");
		this.isPrivate             = (Boolean) map.get("isPrivate");
		this.declaringSymbolDoopId = (String) map.get("declaringSymbolDoopId");
		this.sizeInBytes           = ((Number) map.get("sizeInBytes")).longValue();
		this.superTypes            = (List<String>) map.get("superTypes");
	}	
}
