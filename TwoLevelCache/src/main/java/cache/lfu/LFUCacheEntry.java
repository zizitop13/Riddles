package cache.lfu;

import cache.CacheEntryWrapper;

/**
 * 
 * Object wrapper for the LFU cache
 * 
 * @author Maxim
 *
 */
public class LFUCacheEntry<K, V> implements CacheEntryWrapper<K, V> {
	
	private V value;
	
	private K key;
	
	private Integer frequency;
	
	private String filePath;
	
	public LFUCacheEntry(K key, V value){
		this.value = value;
		this.key = key;
		this.frequency = 1;
	}
	
	
	
	public int hashCode(){
		return getValue().hashCode();		
	}
	
	public void incrementPriority() {
		this.frequency++;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public Integer getPriority() {
		return frequency;
	}
	
	public void setPriority(Integer frequency) {
		this.frequency = frequency;
	}

	
	public K getKey() {		
		return key;
	}
	
	
	@Override
	public String toString() {
		return key + ": " + frequency;
	}



	public String getFilePath() {
		return filePath;
	}


	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}




	
	

}
