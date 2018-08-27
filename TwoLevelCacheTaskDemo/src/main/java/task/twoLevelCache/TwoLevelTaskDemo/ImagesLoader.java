package task.twoLevelCache.TwoLevelTaskDemo;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImagesLoader {
	
	private List<String> links = new LinkedList<String>();
		

	public ImagesLoader(String pageAddress) throws IOException {
		
		
            Document doc = Jsoup.connect(pageAddress).get();
                      
            Elements imgs = doc.select("div.node div.content img");

            for (Element img : imgs) {
            	getLinks().add(img.attr("src"));
            }
                
		
	}

	public BufferedImage loadImage(String url) throws IOException {
		 return ImageIO.read(new URL(url));
	}
	
	public BufferedImage loadImage(int idx) throws IOException {
		
		if(idx > links.size())
			return null;		
		
		return ImageIO.read(new URL(links.get(idx)));
		
	}


	public List<String> getLinks() {
		return links;
	}
	
	public String getLink(int index) {
		return links.get(index);
	}


	
	
}
