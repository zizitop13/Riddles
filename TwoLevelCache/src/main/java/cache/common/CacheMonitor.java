package cache.common;

import java.util.Collection;


public class CacheMonitor {
		
	private Collection level[] = new Collection[3];
	
	private static CacheMonitor cacheMonitor;
	
	
	public static CacheMonitor getInstance() {
		if(cacheMonitor==null)
			cacheMonitor = new CacheMonitor();		
		return cacheMonitor;
	}
	
	private CacheMonitor() {}
	
	public Collection getLevel(int levelNumber){
		return level[levelNumber];
	}
	
	public void addLevel(int levelNumber, Collection list) {
		level[levelNumber] = list;
	}

}
