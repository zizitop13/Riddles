package task.twoLevelCache.TwoLevelTaskDemo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cache.Cache;
import cache.common.CacheMonitor;
import cache.common.CacheNotAvailableException;
import cache.common.SerializableBufferdImage;
import cache.twolevel.TwoLevelCache;

/**
 * Hello world!
 *
 */
public class App extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DefaultListModel<String> dlm1 = new DefaultListModel<String>();
	
	private DefaultListModel<String> dlm2 = new DefaultListModel<String>();
	
	private DefaultListModel<String> dlm3 = new DefaultListModel<String>();

	private ImagesLoader imagesLoader;
	
	private static final String DEFAULT_URL = "http://vao-priut.org/image/venya-s-26";
	
	Cache<String, SerializableBufferdImage> twoLevelCache;
	
	

	public App() {
		
		super("TwoLevelTaskDemo");
		
		
		

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setLayout(new BorderLayout());

		this.setPreferredSize(new Dimension(1200, 600));

		this.pack();

		this.setLocationRelativeTo(null);

		JPanel contents = new JPanel(new GridBagLayout());		
		
		MyImage myImage = new MyImage();

		JButton refresh = new JButton("Refresh");
		
		refresh.setSize(100, 20);
		
		refresh.addActionListener(new ActionListener() {
			
			

			public void actionPerformed(ActionEvent e) {

				try {

					dlm1.clear();
					dlm2.clear();					
					dlm3.clear();
					
					twoLevelCache = new TwoLevelCache<String, SerializableBufferdImage>(6);		
					
					
					imagesLoader = new ImagesLoader(DEFAULT_URL);

					for (int i = 0; i < imagesLoader.getLinks().size(); i++) {

						String name = "image_" + (i + 1);

						dlm1.add(i, name);

					}

				} catch (IOException e1) {

					e1.printStackTrace();
				}

			}

		});
		
		
		
		JPanel jpanel1 = new JPanel(new GridLayout(3, 2));

		JList<String> list1 = new JList<String>(dlm1);
		
		jpanel1.add(new JLabel("From WEB"));
		
		JList<String> list2 = new JList<String>(dlm2);	
		
		jpanel1.add(new JScrollPane(list1));	
		
		jpanel1.add(new JLabel("First level cache"));
		
		jpanel1.add(new JScrollPane(list2));
		
		
		
		jpanel1.add(new JLabel("Second level cache"));
		
		JList<String> list3 = new JList<String>(dlm3);
		
		jpanel1.add(new JScrollPane(list3));		
	
		list1.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {

				try {					
					
					if(e.getValueIsAdjusting()) {
						
						int index = list1.getSelectedIndex();						

						BufferedImage image = null;
						
						if(!twoLevelCache.contains(dlm1.getElementAt(index))) {						
	
							image = imagesLoader.loadImage(index);	
							
							twoLevelCache.put(dlm1.getElementAt(index), new SerializableBufferdImage(image));
							
							dlm2.clear();
							
							CacheMonitor.getInstance().getLevel(1).forEach((key) -> dlm2.addElement((String) key));
							
							dlm3.clear();
							
							CacheMonitor.getInstance().getLevel(2).forEach((key) -> dlm3.addElement((String) key));
							
							
						} else {
							
							image = twoLevelCache.get(dlm1.getElementAt(index)).getImage();
						
						}
						
						myImage.setImage(image);
						
						myImage.repaint();
					
					}

				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (NullPointerException e1) {
					e1.printStackTrace();
				} catch (CacheNotAvailableException e1) {
					e1.printStackTrace();
				} 
			}
		});

		contents.add(refresh);
		
	    GridBagConstraints textFieldConstraints =
	            new GridBagConstraints();
	    
	    textFieldConstraints.fill = GridBagConstraints.BOTH;
	    


		contents.add(jpanel1, textFieldConstraints);
	
		contents.add(myImage);

		setContentPane(contents);

	}

	public static void main( String[] args )
	    {          
	    	
	    	Runnable doSwingLater = new Runnable(){
	            public void run() {
	            	App app = new App();
	            	app.setVisible(true);
	            }	        
	    };
	    SwingUtilities.invokeLater(doSwingLater);
    }

	private static class MyImage extends JComponent {

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
	

}
