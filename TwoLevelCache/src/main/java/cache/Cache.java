package cache;

import cache.common.CacheNotAvailableException;
/**
 * Common cache interface
 * @author Maxim
 *
 * @param <K>
 * @param <V>
 */
public interface Cache<K, V> {
	
	/**
	 * Get object from cache
	 * @param key
	 * @return
	 */
	public V get(K key);
	
	/**
	 * Put object inside cache
	 * @param key
	 * @param value
	 * @throws NullPointerException
	 */
	public void put(K key, V value) throws  CacheNotAvailableException, NullPointerException;		
	
	/**
	 * Remove object from cache and get it
	 * @param key
	 * @return value
	 * @throws NullPointerException
	 */
	public V remove(K key) throws NullPointerException;
	
	/**
	 * Delete object from cache
	 * @param key
	 * @throws NullPointerException
	 */	
	public void  delete(K key) throws NullPointerException;;   
	
	/**
	 * Resize cache
	 * The cache does not expand automatically 
	 * @param maxSize
	 */	
	public void resize(int maxSize);	
		
	/**
	 * Delete all object from cache
	 */
	public void clear();	
	
    /**
     * Answer is an object in the cache
     * @param key
     * @return
     */
	public boolean contains(K key);
    
	/**
	 * Return current size of cache
	 * @return
	 */
	public int size();

	
	

}
