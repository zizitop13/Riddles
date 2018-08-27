package task.twoLevelCache.TwoLevelTaskDemo;

import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Test;

public class ImagesLoaderCase {
	
	@Test
	public void testLoadImages() throws Exception {
		ImagesLoader img = new ImagesLoader("https://gist.githubusercontent.com/akhorevich/5b849373dc9abaf921b3/raw/18e79ab6a0c0be007a2a4590e4e176184ced311a/links");
				
		assertFalse(img.getLinks().isEmpty());
		assertNotNull(img.loadImage(0));
	}

}
