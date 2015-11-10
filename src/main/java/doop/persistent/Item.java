package doop.persistent;

/**
 * Created by saiko on 24/8/2015.
 */
public interface Item {

    String getId();

    void fromJSON(String json);

    String toJSON();

    //We don't need creation and modification dates to be common
    /*
    Date getCreated();

    Date getModified();

    void setCreated(Date created);

    void setModified(Date modified);
    */
}
