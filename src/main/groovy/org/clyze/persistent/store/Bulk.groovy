package org.clyze.persistent.store

import org.clyze.persistent.Item

/**
 * Created by saiko on 6/5/16.
 */
interface Bulk {

    public void delete(Item item)

    public void delete(String itemType, String itemId)

    public void save(Item item)

    public void save(String itemType, String itemId, String json)

    public void execute()
}