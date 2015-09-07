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

    public void search(Map<String, Object> constraints, SearchLimits limits, Closure resultProcessor)

}