package cache.common;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class SerializableBufferdImage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	transient private BufferedImage image;
	
	public SerializableBufferdImage(BufferedImage image) {
		super();
		this.image = image;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		ImageIO.write(image, "png", out);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		 in.defaultReadObject();
		 image = ImageIO.read(in);
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	
	
	
}