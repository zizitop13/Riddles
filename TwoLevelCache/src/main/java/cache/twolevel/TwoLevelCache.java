package cache.twolevel;

import java.io.Serializable;

import cache.Cache;
import cache.common.CacheNotAvailableException;
import cache.lfu.LFUFileSystemCache;
import cache.lfu.LFUMemoryCache;

public class TwoLevelCache<K, V extends Serializable> implements Cache<K, V>{
	
	private static final int max_size = 10;
	
	private Cache<K, V> fileSystemCache;
	
	private Cache<K, V> memoryCache;
	
		
	public TwoLevelCache(int max_size){
		
		setFileSystemCache(new LFUFileSystemCache<K, V>(max_size / 2, null));
		
		setMemoryCache(new LFUMemoryCache<K, V>(max_size / 2, getFileSystemCache()));
		
	}

	@Override
	public V get(K key) {
		V value = getMemoryCache().get(key);
		return value==null ? getFileSystemCache().get(key) : value;
	}

	@Override
	public void put(K key, V value) throws CacheNotAvailableException, NullPointerException {
		getMemoryCache().put(key, value);		
	}

	@Override
	public V remove(K key) throws NullPointerException {
		V value = getMemoryCache().remove(key);
		return value==null ? getFileSystemCache().remove(key) : value;
	}

	@Override
	public void delete(K key) throws NullPointerException {
		getMemoryCache().delete(key);		
	}

	@Override
	public void resize(int maxSize) {
		
		getMemoryCache().resize(maxSize / 2);
		
		getFileSystemCache().resize(maxSize / 2);	
	}

	@Override
	public void clear() {
		getMemoryCache().clear();
		getFileSystemCache().clear();		
	}

	@Override
	public boolean contains(K key) {
		return getMemoryCache().contains(key) || getFileSystemCache().contains(key);
	}

	@Override
	public int size() {
		return getMemoryCache().size() + getFileSystemCache().size();
	}

	public Cache<K, V> getFileSystemCache() {
		return fileSystemCache;
	}

	private void setFileSystemCache(Cache<K, V> fileSystemCache) {
		this.fileSystemCache = fileSystemCache;
	}

	public Cache<K, V> getMemoryCache() {
		return memoryCache;
	}

	private void setMemoryCache(Cache<K, V> memoryCache) {
		this.memoryCache = memoryCache;
	}

}
