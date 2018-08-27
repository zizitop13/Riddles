package task.twoLevelCache.TwoLevelTaskDemo;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public  class MyImage extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	BufferedImage bufferedImage = null;
	
	Dimension myDimension = new Dimension(600, 600);

	public MyImage() {	}
	
	public void setImage(BufferedImage bufferedImage) {
		
		this.bufferedImage = bufferedImage;
		
		myDimension = new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
	}

	@Override
	public Dimension getPreferredSize() {
		return myDimension;
	}

	@Override
	public Dimension getMaximumSize() {
		return myDimension;
	}

	@Override
	public Dimension getMinimumSize() {
		return myDimension;
	}

	@Override
	protected void paintComponent(Graphics g) {

		g.drawImage(bufferedImage, 0, 0, null);

	}
	
}