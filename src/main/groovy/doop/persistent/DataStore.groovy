package doop.persistent
/**
 * Created by saiko on 24/8/2015.
 */
interface DataStore {

    public void connect() throws RuntimeException

    public void disconnect() throws RuntimeException

    public void clear() throws RuntimeException

    public boolean exists(String id, Class cl) throws RuntimeException

    public <T extends Item> T load(String id, Class<T> cl) throws RuntimeException

    public void save(Item item) throws RuntimeException

    public void delete(Item item, Closure callback)

    public void search(String freeText,
                       Map<String, Object> filters,
                       List<String> fields,
                       SearchLimits limits,
                       Closure resultProcessor)

    public void deleteItems(Map<String, Object> filters)

    public Map<String, Long> groupBy(String text, Map<String, Object> filters, String field)

    Bulk createBulk()

}