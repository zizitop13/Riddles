package cache.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cache.LFUCache;
import cache.Recacheble;
import cache.lfu.LFUMemoryCache;

public class RecacheCacheCase {
	
	LFUCache<String, Object> lfuCache_level_1;
	
	LFUCache<String, Object> lfuCache_level_2;
	
	@Before
	public void setUp() {
		
		lfuCache_level_2 = new LFUMemoryCache<String, Object>(10);
		
		lfuCache_level_1 = new LFUMemoryCache<String, Object>(10, lfuCache_level_2);
	}
	
	@Test
	public void testRecache() throws Exception {
		
		for(int i = 1; i <=10; i++)
			lfuCache_level_1.put("img_"+i, new Object());		
		
		for(int i =1; i <=9; i++)
			lfuCache_level_1.get("img_"+i);	
		
		((Recacheble) lfuCache_level_1).recache();
		
		assertEquals(8, lfuCache_level_1.size());
		
		assertEquals(2, lfuCache_level_2.size());
		
		assertTrue(lfuCache_level_2.contains("img_10"));

	}
	
	@Test
	public void testAutoRecache() throws Exception {
		
		for(int i = 1; i <=10; i++)
			lfuCache_level_1.put("img_"+i, new Object());		
		
		for(int i =1; i <=9; i++)
			lfuCache_level_1.get("img_"+i);	
		
		lfuCache_level_1.put("img_"+11, new Object());
		
		assertEquals(9, lfuCache_level_1.size());
		
		assertEquals(2, lfuCache_level_2.size());
		
		assertTrue(lfuCache_level_2.contains("img_10"));
		
		assertFalse(lfuCache_level_2.contains("img_11"));
		
	}

}
