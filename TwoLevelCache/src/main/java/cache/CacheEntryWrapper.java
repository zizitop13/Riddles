package cache;

/**
 * Wrapper for entry of priority cache
 * @author Maxim
 *
 * @param <K>
 * @param <V>
 */
public interface CacheEntryWrapper<K, V> {
	
	public Number getPriority();
	
	public void setPriority(Number priority);
	
	public V getValue();
	
	public K getKey();
	
	public String getFilePath();
	
	public void setFilePath(String path);
	
	public void incrementPriority();
	
	public void setValue(V value);
	

}
