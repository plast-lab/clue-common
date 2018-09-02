package org.clyze.persistent.model.doop;

import java.util.Map;
import java.util.Objects;

import org.clyze.persistent.model.Position;
import org.clyze.persistent.model.SymbolWithDoopId;

/**
 * Symbol used for classes, interfaces and enums
 */
public class Class extends SymbolWithDoopId {

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

	/**
	 * The doopId of the type or method where this type is declared.
	 */
	private String declaringSymbolDoopId;

	private long sizeInBytes;

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
				 boolean isAbstract) {		
		super(position, sourceFileName, doopId);
		this.name = name;
		this.packageName = packageName;		
		this.isInterface = isInterface;
		this.isEnum = isEnum;
		this.isStatic = isStatic;
		this.isInner = isInner;
		this.isAnonymous = isAnonymous;
		this.isAbstract = isAbstract;	
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

	public boolean getIsInterface() {
		return isInterface;
	}

	public void setIsInterface(boolean anInterface) {
		isInterface = anInterface;
	}

	public boolean getIsEnum() {
		return isEnum;
	}

	public void setIsEnum(boolean anEnum) {
		isEnum = anEnum;
	}

	public boolean getIsStatic() {
		return isStatic;
	}

	public void setIsStatic(boolean aStatic) {
		isStatic = aStatic;
	}

	public boolean getIsInner() {
		return isInner;
	}

	public void setIsInner(boolean inner) {
		isInner = inner;
	}

	public boolean getIsAnonymous() {
		return isAnonymous;
	}

	public void setIsAnonymous(boolean anonymous) {
		isAnonymous = anonymous;
	}

	public boolean getIsAbstract() {
		return isAbstract;
	}

	public void setIsAbstract(boolean anAbstract) {
		isAbstract = anAbstract;
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

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Class aClass = (Class) object;
        /*
        return isInterface == aClass.isInterface &&
                isEnum == aClass.isEnum &&
                isStatic == aClass.isStatic &&
                isInner == aClass.isInner &&
                isAnonymous == aClass.isAnonymous &&
                isAbstract == aClass.isAbstract &&
                sizeInBytes == aClass.sizeInBytes &&
                Objects.equals(name, aClass.name) &&
                Objects.equals(artifactName, aClass.artifactName) &&
                Objects.equals(packageName, aClass.packageName) &&
                Objects.equals(doopId, aClass.doopId) &&
                Objects.equals(declaringSymbolDoopId, aClass.declaringSymbolDoopId);
        */
        return Objects.equals(doopId, aClass.doopId); 
    }

    public int hashCode() {
        //return Objects.hash(super.hashCode(), name, artifactName, packageName, isInterface, isEnum, isStatic, isInner, isAnonymous, isAbstract, doopId, declaringSymbolDoopId, sizeInBytes);
        return Objects.hash(super.hashCode(), doopId);
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
		map.put("declaringSymbolDoopId", this.declaringSymbolDoopId);
		map.put("sizeInBytes", this.sizeInBytes);
	}

	protected void loadFrom(Map<String, Object> map){
		super.loadFrom(map);
		this.name                  = (String) map.get("name");
		this.artifactName          = (String) map.get("artifactName");
		this.packageName           = (String) map.get("packageName");
		this.isInterface           = (Boolean) map.get("isInterface");
		this.isEnum                = (Boolean) map.get("isEnum");
		this.isStatic              = (Boolean) map.get("isStatic");
		this.isInner               = (Boolean) map.get("isInner");
		this.isAnonymous           = (Boolean) map.get("isAnonymous");
		this.isAbstract            = (Boolean) map.get("isAbstract");		
		this.declaringSymbolDoopId = (String) map.get("declaringSymbolDoopId");
		this.sizeInBytes           = ((Number) map.get("sizeInBytes")).longValue();
	}	
}
