package cache.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import cache.Cache;
import cache.common.CacheNotAvailableException;
import cache.lfu.LFUFileSystemCache;
import cache.lfu.LFUMemoryCache;

@RunWith(Parameterized.class)
public class CommonCacheCase {

	private static final int MAX_SIZE = 10;

	Cache<String, String> cache;

	@Parameters
	public static List<Object[]> getParameters() {
		List<Object[]> params = new ArrayList<Object[]>();

		params.add(new Object[] { new LFUMemoryCache<String, String>(MAX_SIZE) });
		
		params.add(new Object[] { new LFUFileSystemCache<String, String>(MAX_SIZE, null) });
		
		return params;
	}
	
	@After
	public void after() {
		cache.clear();
	}
	
	public CommonCacheCase(Cache<String, String> cache) {
		this.cache = cache;
	}

	@Test
	public void testPut() throws CacheNotAvailableException {

		cache.put("img_1", new String());

		cache.put("img_2", new String());

		assertEquals(2, cache.size());

	}

	@Test
	public void testGet() throws CacheNotAvailableException {

		String image_1 = new String();

		cache.put("img_1", image_1);

		cache.put("img_2", new String());

		assertEquals(image_1, cache.get("img_1"));

	}

	@Test
	public void testRemove() throws CacheNotAvailableException {

		String image_1 = new String();

		cache.put("img_1", image_1);

		String removedOb = cache.remove("img_1");

		assertEquals(0, cache.size());

		assertEquals(image_1, removedOb);

		assertNull(cache.remove("img_2"));

	}

	@Test
	public void testDelete() throws CacheNotAvailableException {

		cache.put("img_1", new String());

		cache.delete("img_1");

		cache.delete("img_2");

		assertEquals(0, cache.size());

	}

	@Test
	public void testClear() throws CacheNotAvailableException {

		cache.put("img_1", new String());

		cache.put("img_2", new String());

		cache.clear();

		assertEquals(0, cache.size());

	}

	@Test
	public void testContains() throws CacheNotAvailableException {

		cache.put("img_1", new String());

		assertTrue(cache.contains("img_1"));

	}

	@Test
	public void testPulling() throws CacheNotAvailableException {

		for (int i = 0; i < 20; i++) {

			cache.put("img_" + i, new String());

		}

		assertEquals(MAX_SIZE, cache.size());

	}

	@Test
	public void testPullingLeastPriority() throws CacheNotAvailableException {

		for (int i = 0; i < 10; i++)
			cache.put("img_" + i, new String());

		for (int i = 0; i < 9; i++)
			cache.get("img_" + i);

		cache.put("img_10", new String());

		assertFalse(cache.contains("img_9"));

	}

	@Test
	public void testResize() throws CacheNotAvailableException {

		for (int i = 0; i < 10; i++)
			cache.put("img_" + i, new String());

		cache.resize(MAX_SIZE * 2);

		cache.put("img_10", new String());

		assertTrue(cache.contains("img_10"));

	}

	@Test
	public void testResizeLess() throws CacheNotAvailableException {

		for (int i = 0; i < 10; i++)
			cache.put("img_" + i, new String());

		cache.resize(MAX_SIZE / 2);

		assertEquals(MAX_SIZE / 2, cache.size());

	}

}
