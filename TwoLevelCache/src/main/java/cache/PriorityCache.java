package cache;

import java.util.Queue;

import cache.lfu.LFUCacheEntry;




/**
 * LFU (Least Frequently Used)<br>
 * LRU (Least Recently Used) 
 * @author Maxim
 *
 */
public interface PriorityCache<K, V> extends Cache<K, V>{
	
	  	public Queue<CacheEntryWrapper<K, V>> getMostPriorityUsedEntry();
	  	
	  	public Number getPriorityOf(K key);
    
	
}
