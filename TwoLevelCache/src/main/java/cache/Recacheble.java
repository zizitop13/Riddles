package cache;

import cache.common.CacheNotAvailableException;

/**
 * If cache implements this interface it means that cache auto recacheble 
 * @author Maxim
 *
 */
public interface Recacheble {
	
	/**
	 * Recache some determined part
	 * @throws CacheNotAvailableException
	 */
	public void recache() throws CacheNotAvailableException;
	
	
	/**
	 * Recache recachePartion
	 * @param recachePartion max = 1.0f
	 * @throws CacheNotAvailableException
	 */
	public void recache(float recachePartion) throws CacheNotAvailableException;


}
