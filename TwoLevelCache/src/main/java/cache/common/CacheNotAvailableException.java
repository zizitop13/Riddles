package cache.common;

import cache.Cache;

public class CacheNotAvailableException extends Exception {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CacheNotAvailableException(Exception e, Cache<?, ?> cache) {
		super("Cache " +cache.toString() + " is not available");
		e.printStackTrace();
	}

}
