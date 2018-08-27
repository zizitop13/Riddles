package cache.lru;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.UUID;

import cache.CacheEntryWrapper;
import cache.PriorityCache;
import cache.common.CacheMonitor;
import cache.common.CacheNotAvailableException;

public class LRUFileSystemCache<K, V extends Serializable> implements PriorityCache<K, V> {

	private Map<K, CacheEntryWrapper<K, V>> idMap;

	private int maxSize;

	private File tempFolder;

	public LRUFileSystemCache(int maxSize, String folderTempPath) {

		this.idMap = new HashMap<K, CacheEntryWrapper<K, V>>();

		this.maxSize = maxSize;

		File tempFolder = null;

		if (folderTempPath != null) {
			tempFolder = new File(folderTempPath);
			tempFolder.mkdirs();
		}
		
		CacheMonitor.getInstance().addLevel(2, idMap.keySet());
	}


	
	public V get(K key) {

		CacheEntryWrapper<K, V> entry = idMap.get(key);

		V deserializedObject = null;

		if (entry != null) {
			try (FileInputStream fileStream = new FileInputStream((String) entry.getFilePath());
					ObjectInputStream objectStream = new ObjectInputStream(fileStream);) {

				deserializedObject = (V) objectStream.readObject();

				entry.incrementPriority();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		return deserializedObject;
	}

	private void unloadUpdate(K key, V value, File tmpFile, Long recently) throws CacheNotAvailableException {

		try (FileOutputStream fos = new FileOutputStream(tmpFile);
				ObjectOutputStream objectStream = new ObjectOutputStream(fos)) {
			
			objectStream.writeObject(value);
			
			objectStream.flush();
			
		} catch (IOException e) {
			
			throw new CacheNotAvailableException(e, this);
			
		} finally {
			
			if (tmpFile != null)
				tmpFile.deleteOnExit();
			
		}

		CacheEntryWrapper<K, V> lfuFileSysEntry = new LRUCacheEntry(key, null, recently);

		lfuFileSysEntry.setFilePath(tmpFile.getAbsolutePath());

		idMap.put(key, lfuFileSysEntry);

	}

	
	public void put(K key, V value) throws CacheNotAvailableException, NullPointerException {

		Objects.requireNonNull(key);

		Objects.requireNonNull(value);

		CacheEntryWrapper<K, V> entry = idMap.get(key);
		
		File tmpFile = null;

		if(entry!=null)
			tmpFile = new File(entry.getFilePath());
		
		else 
			if (maxSize == idMap.size())				
				delete(getMostPriorityUsedEntry().poll().getKey());
		
		
		if(tmpFile==null || !tmpFile.exists()) {
			String id =  UUID.randomUUID().toString();
			
			try {
				
				tmpFile = File.createTempFile(id, null, tempFolder);
				
				unloadUpdate(key, value, tmpFile, System.currentTimeMillis());
				
			} catch (IOException e) {
				
				throw new CacheNotAvailableException(e, this);
				
			}
		}		

	}

	
	public V remove(K key) throws NullPointerException {

		Objects.requireNonNull(key);

		if (idMap.containsKey(key)) {
			V result = this.get(key);
			this.delete(key);
			return result;
		}
		return null;
	}

	
	public void delete(K key) throws NullPointerException {
		if (idMap.containsKey(key)) {
			File deletingFile = new File((String) idMap.remove(key).getFilePath());
			deletingFile.delete();
		}

	}

	
	public void resize(int maxSize) {
		Map<K, CacheEntryWrapper<K,V>> resizedMap = new HashMap<K, CacheEntryWrapper<K,V>>(maxSize, 2f);
		
		if(this.maxSize > maxSize) {
			
			Queue<CacheEntryWrapper<K,V>> q = getMostPriorityUsedEntry();
			
			for(int i =0; i < this.maxSize-maxSize; i++) 
				delete(q.poll().getKey());			
				
		}	
		
		resizedMap.putAll(idMap);
		
		idMap = resizedMap;
			
		this.maxSize = maxSize;

	}

	
	public void clear() {
		idMap.values().forEach(entry -> {
			File deletingFile = new File((String) entry.getFilePath());
			deletingFile.delete();
		});

		idMap.clear();
	}

	
	public boolean contains(K key) {
		return idMap.containsKey(key);
	}

	
	public int size() {
		return idMap.size();
	}

	
	public Queue<CacheEntryWrapper<K, V>> getMostPriorityUsedEntry() {

		Queue<CacheEntryWrapper<K, V>> frequencyQ;

		if (idMap.isEmpty())
			return new PriorityQueue<CacheEntryWrapper<K, V>>();

		frequencyQ = new PriorityQueue<CacheEntryWrapper<K, V>>(idMap.size(),

				(entry_1, entry_2) ->

				((Long) entry_1.getPriority()).compareTo((Long) entry_2.getPriority())

		);

		frequencyQ.addAll(idMap.values());

		return frequencyQ;
	}

	
	public Number getPriorityOf(K key) {
		return idMap.get(key).getPriority();
	}

}
