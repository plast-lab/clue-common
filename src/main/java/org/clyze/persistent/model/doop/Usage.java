package org.clyze.persistent.model.doop;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

import org.clyze.persistent.model.Position;
import org.clyze.persistent.model.Symbol;

public class Usage extends Symbol {

	private UsageKind usageKind;

	private String doopId;

    public Usage() {}

	public Usage(Position position, String sourceFileName, String doopId, UsageKind usageKind) {
		super(position, sourceFileName);
		this.doopId = doopId;
		this.usageKind = usageKind;
	}

    public UsageKind getUsageKind() {
        return usageKind;
    }

    public void setUsageKind(UsageKind usageKind) {
        this.usageKind = usageKind;
    }

    public String getDoopId() {
        return doopId;
    }

    public void setDoopId(String doopId) {
        this.doopId = doopId;
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Usage usage = (Usage) object;
        return Objects.equals(usageKind, usage.usageKind) &&
               Objects.equals(doopId, usage.doopId);
    }

    public int hashCode() {

        return Objects.hash(super.hashCode(), usageKind, doopId);
    }

    protected void saveTo(Map<String, Object> map) {
		super.saveTo(map);
		map.put("usageKind", this.usageKind.name());
		map.put("doopId", this.doopId);
	}

	protected void loadFrom(Map<String, Object> map){
		super.loadFrom(map);
		this.usageKind = UsageKind.valueOf((String)map.get("usageKind"));
		this.doopId    = (String) map.get("doopId");
	}
}
