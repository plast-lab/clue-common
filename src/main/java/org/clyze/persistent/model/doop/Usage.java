package org.clyze.persistent.model.doop;

import java.util.Map;
import java.util.Objects;

import org.clyze.persistent.model.Position;
import org.clyze.persistent.model.SymbolWithDoopId;

public class Usage extends SymbolWithDoopId {

	private UsageKind usageKind;	

    public Usage() {}

    public Usage(String id) {
        this.id = id;
    }

	public Usage(Position position, String sourceFileName, String doopId, UsageKind usageKind) {
		super(position, sourceFileName, doopId);		
		this.usageKind = usageKind;
	}

    public UsageKind getUsageKind() {
        return usageKind;
    }

    public void setUsageKind(UsageKind usageKind) {
        this.usageKind = usageKind;
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Usage)) return false;
        Usage usage = (Usage) object;

        return super.equals(object)
            && Objects.equals(usageKind, usage.usageKind);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), usageKind);
    }

    protected void saveTo(Map<String, Object> map) {
		super.saveTo(map);
		map.put("usageKind", this.usageKind.name());		
	}

	protected void loadFrom(Map<String, Object> map){
		super.loadFrom(map);
		this.usageKind = UsageKind.valueOf((String)map.get("usageKind"));		
	}
}
