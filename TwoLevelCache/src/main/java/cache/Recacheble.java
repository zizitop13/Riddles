package cache;

import cache.common.CacheNotAvailableException;

public interface Recacheble {
	
	public void recache() throws CacheNotAvailableException;
	
	public void recache(float recachePartion) throws CacheNotAvailableException;


}
