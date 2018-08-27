package cache.lru;

import java.util.Date;

import cache.CacheEntryWrapper;

/**
 * 
 * Object wrapper for the LFU cache
 * 
 * @author Maxim
 *
 */
public class LRUCacheEntry<K, V> implements CacheEntryWrapper<K, V> {
	
	private V value;
	
	private K key;
	
	private Long recently;
	
	private String filePath;
	
	public LRUCacheEntry(K key, V value, Long recently){
		this.value = value;
		this.key = key;
		this.recently = recently;
	}
	
	
	
	public int hashCode(){
		return getValue().hashCode();		
	}
	
	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public Long getPriority() {
		return recently;
	}
	
	
	public K getKey() {		
		return key;
	}
	
	
	@Override
	public String toString() {
		return key + ": " + recently;
	}



	public String getFilePath() {
		return filePath;
	}


	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}



	@Override
	public void incrementPriority() {
		recently+=1000;
	}



	@Override
	public void setPriority(Number priority) {
		this.recently = (Long) priority;		
	}




	
	

}
