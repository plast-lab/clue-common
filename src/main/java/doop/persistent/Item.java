package doop.persistent;

import java.util.Date;

/**
 * Created by saiko on 24/8/2015.
 */
interface Item {

    String getId();

    Date getCreated();

    Date getModified();

    void setCreated(Date created);

    void setModified(Date modified);

    void fromJSON(String json);

    String toJSON();
}
