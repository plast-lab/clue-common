package org.clyze.doop.persistent;

import java.util.Map;

/**
 * Created by saiko on 24/8/2015.
 */
public interface Item {

    String getId();

    void fromJSON(String json);

    String toJSON();

    Map<String, Object> toMap();

    //We don't need creation and modification dates to be common
    /*
    Date getCreated();

    Date getModified();

    void setCreated(Date created);

    void setModified(Date modified);
    */
}
