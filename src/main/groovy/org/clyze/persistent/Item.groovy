package org.clyze.persistent

interface Item {

    String getId();

    void fromJSON(String json);

    String toJSON();

    Map<String, Object> toMap();

    //We don't need creation and modification dates to be analysis
    /*
    Date getCreated();

    Date getModified();

    void setCreated(Date created);

    void setModified(Date modified);
    */
}