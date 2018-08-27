package cache;

import java.util.Queue;

import cache.lfu.LFUCacheEntry;




/**
 * LFU (Least Frequently Used) — реже всего использованный вытесняется из кеша.
 * @author Maxim
 *
 */
public interface LFUCache<K, V> extends Cache<K, V>{
	
	  	public Queue<LFUCacheEntry<K, V>> getMostFrequentlyUsedEntry();
	  	
	  	public int getFrecquencyOf(K key);
    
	
}
