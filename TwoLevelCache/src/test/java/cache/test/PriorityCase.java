package cache.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import cache.CacheEntryWrapper;
import cache.PriorityCache;
import cache.lfu.LFUCacheEntry;
import cache.lfu.LFUFileSystemCache;
import cache.lfu.LFUMemoryCache;



public class PriorityCase {
	
	PriorityCache<String, Object> lfuCache;
	
	private static final int MAX_SIZE = 10;
	
	@Parameters
	public static List<Object[]> getParameters() {
		List<Object[]> params = new ArrayList<Object[]>();

		params.add(new Object[] { new LFUMemoryCache<String, String>(MAX_SIZE) });
		
		params.add(new Object[] { new LFUFileSystemCache<String, String>(MAX_SIZE, null) });
		
		return params;
	}
	
	@After
	public void after() {
		lfuCache.clear();
	}
	
	@Before
	public void setUp() {
		lfuCache = new LFUMemoryCache<String, Object>(10);
	}
	
	@Test
	public void testGetFrecquencyOf() throws Exception {
		
		lfuCache.put("img_1", new Object());
		
		lfuCache.get("img_1");
		
		assertEquals(2, lfuCache.getPriorityOf("img_1"));
		
	}
	
	@Test
	public void testGetMostFrequentlyUsedEntry() throws Exception {
		
		lfuCache.put("img_1", new Object());
		
		lfuCache.put("img_2", new Object());
		
		lfuCache.put("img_3", new Object());
		
		lfuCache.get("img_1");
		
		lfuCache.get("img_1");
		
		lfuCache.get("img_3");
		
		Queue<CacheEntryWrapper<String, Object>> q = lfuCache.getMostPriorityUsedEntry();
		
		assertEquals(new Integer(1), q.poll().getPriority());
		
		assertEquals(new Integer(2), q.poll().getPriority());
		
		assertEquals(new Integer(3), q.poll().getPriority());
		
	}
	
	
		

}
