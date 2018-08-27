package cache.lfu;

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

import cache.LFUCache;
import cache.common.CacheMonitor;
import cache.common.CacheNotAvailableException;

public class LFUFileSystemCache<K, V extends Serializable> implements LFUCache<K, V> {

	private Map<K, LFUCacheEntry<K, V>> idMap;

	private int maxSize;

	private File tempFolder;

	public LFUFileSystemCache(int maxSize, String folderTempPath) {

		this.idMap = new HashMap<K, LFUCacheEntry<K, V>>();

		this.maxSize = maxSize;

		File tempFolder = null;

		if (folderTempPath != null) {
			tempFolder = new File(folderTempPath);
			tempFolder.mkdirs();
		}
		
		CacheMonitor.getInstance().addLevel(2, idMap.keySet());
	}


	
	public V get(K key) {

		LFUCacheEntry<K, V> entry = idMap.get(key);

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

	private void unloadUpdate(K key, V value, File tmpFile) throws CacheNotAvailableException {

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

		LFUCacheEntry<K, V> lfuFileSysEntry = new LFUCacheEntry<K, V>(key, null);

		lfuFileSysEntry.setFilePath(tmpFile.getAbsolutePath());

		idMap.put(key, lfuFileSysEntry);

	}

	
	public void put(K key, V value) throws CacheNotAvailableException, NullPointerException {

		Objects.requireNonNull(key);

		Objects.requireNonNull(value);

		LFUCacheEntry<K, V> entry = idMap.get(key);
		
		File tmpFile = null;

		if(entry!=null)
			tmpFile = new File(entry.getFilePath());
		
		else 
			if (maxSize == idMap.size())				
				delete(getMostFrequentlyUsedEntry().poll().getKey());
		
		
		if(tmpFile==null || !tmpFile.exists()) {
			String id = "test"+ UUID.randomUUID().toString();
			
			try {
				
				tmpFile = File.createTempFile(id, null, tempFolder);
				
				unloadUpdate(key, value, tmpFile);
				
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
		Map<K, LFUCacheEntry<K,V>> resizedMap = new HashMap<K, LFUCacheEntry<K,V>>(maxSize, 2f);
		
		if(this.maxSize > maxSize) {
			
			Queue<LFUCacheEntry<K,V>> q = getMostFrequentlyUsedEntry();
			
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

	
	public Queue<LFUCacheEntry<K, V>> getMostFrequentlyUsedEntry() {

		Queue<LFUCacheEntry<K, V>> frequencyQ;

		if (idMap.isEmpty())
			return new PriorityQueue<LFUCacheEntry<K, V>>();

		frequencyQ = new PriorityQueue<LFUCacheEntry<K, V>>(idMap.size(),

				(entry_1, entry_2) ->

				entry_1.getPriority().compareTo(entry_2.getPriority())

		);

		frequencyQ.addAll(idMap.values());

		return frequencyQ;
	}

	
	public int getFrecquencyOf(K key) {
		return idMap.get(key).getPriority();
	}

}
