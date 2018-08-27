package cache.lfu;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

import cache.Cache;
import cache.CacheEntryWrapper;
import cache.PriorityCache;
import cache.Recacheble;
import cache.common.CacheMonitor;
import cache.common.CacheNotAvailableException;

/**
 * 
 * @author Maxim
 *
 *         ���������� LFU ����
 *
 * @param <K>
 * @param <V>
 */
public class LFUMemoryCache<K, V> implements PriorityCache<K, V>, Recacheble {

	private Map<K, LFUCacheEntry<K,V>> heap;
	
	private Cache<K, V> nextLevelCache;
	
	private int maxSize;	
	
	private float recachePartion = 0.25f;
		

	public LFUMemoryCache(int maxSize) {
		init(maxSize);		
	}
	
	public LFUMemoryCache(int maxSize, Cache<K, V> nextLevelCache){
		
		Objects.requireNonNull(nextLevelCache);	
		
		this.nextLevelCache = nextLevelCache;
		
		init(maxSize);		
		
	}
	
	public LFUMemoryCache(int maxSize, Cache<K, V> nextLevelCache, float recachePartion){
		
		this.recachePartion = recachePartion;
		
		Objects.requireNonNull(nextLevelCache);	
		
		this.nextLevelCache = nextLevelCache;
		
		init(maxSize);		
		
	}
	
	private void init(int maxSize) {	
		
		if (maxSize <= 0) {
			throw new IllegalArgumentException("maxSize <= 0");
		}
		
		this.maxSize = maxSize;

		this.heap = new HashMap<K, LFUCacheEntry<K,V>>(maxSize, 2f);
		
		CacheMonitor.getInstance().addLevel(1, heap.keySet());
		
	}
	


	public V get(K key) {

		LFUCacheEntry<K,V> entry = heap.get(key);

		if (entry != null) {

			entry.incrementPriority();
		
			return (V) entry.getValue();

		}

		return null;
	}


	public void put(K key, V value) throws CacheNotAvailableException {

		Objects.requireNonNull(key);

		Objects.requireNonNull(value);
		
		LFUCacheEntry<K,V> entry = heap.get(key);
		
		if(entry!=null) {
			
			entry.setValue(value);

			entry.incrementPriority();
			
			return;
			
		}

		if (maxSize == heap.size()) {			
			
			if(nextLevelCache!=null)					
				recache();
			else 
				delete(getMostPriorityUsedEntry().poll().getKey());									
			
		}

		entry = new LFUCacheEntry<K,V>(key, value);

		heap.put(key, entry);		

	}


	public V remove(K key) {

		Objects.requireNonNull(key);

		if (heap.containsKey(key)) {

			V result = (V) heap.remove(key).getValue();

			return result;
		}

		return null;
	}


	public void delete(K key) {

		Objects.requireNonNull(key);

		heap.remove(key);
	}


	public void resize(int maxSize) {
		
		Map<K, LFUCacheEntry<K,V>> resizedMap = new HashMap<K, LFUCacheEntry<K,V>>(maxSize, 2f);
		
		if(this.maxSize > maxSize) {
			
			Queue<CacheEntryWrapper<K,V>> q = getMostPriorityUsedEntry();
			
			for(int i =0; i < this.maxSize-maxSize; i++) 
				delete(q.poll().getKey());			
				
		}	
		
		resizedMap.putAll(heap);
		
		heap = resizedMap;
			
		this.maxSize = maxSize;
		
		
	}

	public void clear() {
		heap.clear();
	}


	public boolean contains(K key) {
		return heap.containsKey(key);
	}


	public int size() {
		return heap.size();
	}


	public Queue<CacheEntryWrapper<K,V>> getMostPriorityUsedEntry() {
		
		Queue<CacheEntryWrapper<K,V>> frequencyQ;
		
		if(heap.isEmpty())
			return new PriorityQueue<CacheEntryWrapper<K,V>>();
		
		frequencyQ  = new PriorityQueue<CacheEntryWrapper<K,V>>(heap.size(),
				
				(entry_1, entry_2) -> 
				
				((Integer) entry_1.getPriority()).compareTo((Integer) entry_2.getPriority()) 
				
				);
		
		frequencyQ.addAll(heap.values());
		
		return frequencyQ;
	}


	public Number getPriorityOf(K key) {
		return heap.get(key).getPriority();
	}

	public void recache() throws CacheNotAvailableException  {		
		recache(recachePartion);		
	}
	

	public void recache(float recachePartion) throws CacheNotAvailableException {
		
		Objects.requireNonNull(nextLevelCache);

		Queue<CacheEntryWrapper<K,V>> frequencyQ  = getMostPriorityUsedEntry();
		
		int size = frequencyQ.size();
		
		for(int i = 0; i < size * recachePartion; i++) {
			
			LFUCacheEntry<K,V> entry =  heap.remove(frequencyQ.poll().getKey());
			
			try {
				
				nextLevelCache.put(entry.getKey(), (V) entry.getValue());
		
			} catch (CacheNotAvailableException e) {
				
				nextLevelCache.delete(entry.getKey());
				
				this.put(entry.getKey(), (V) entry.getValue());
				
				throw new CacheNotAvailableException(e, nextLevelCache);
			}			
			
		}	
	}



}
