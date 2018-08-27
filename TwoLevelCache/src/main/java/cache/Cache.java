package cache;

import cache.common.CacheNotAvailableException;

public interface Cache<K, V> {
	
	public V get(K key);
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @throws NullPointerException
	 */
	public void put(K key, V value) throws  CacheNotAvailableException, NullPointerException;		
	
	/**
	 * 
	 * @param key
	 * @return
	 * @throws NullPointerException
	 */
	public V remove(K key) throws NullPointerException;
	
	/**
	 * 
	 * @param key
	 * @throws NullPointerException
	 */	
	public void  delete(K key) throws NullPointerException;;   
	
	public void resize(int maxSize);	
		
	public void clear();	
    
	public boolean contains(K key);
    
	public int size();

	
	

}
