package doop.persistent

/**
 * Created by saiko on 6/5/16.
 */
interface Bulk {

    void delete(Item item)

    void delete(String itemType, String itemId)

    void save(Item item)

    void save(String itemType, String itemId, String json)

    void execute()
}