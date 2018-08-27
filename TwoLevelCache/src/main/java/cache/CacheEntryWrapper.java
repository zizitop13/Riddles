package cache;


public interface CacheEntryWrapper<K, V> {
	
	public Integer getPriority();
	
	public void incrementPriority();
	
	public V getValue();
	
	public K getKey();
	

}
