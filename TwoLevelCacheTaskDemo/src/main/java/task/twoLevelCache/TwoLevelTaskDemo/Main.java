package task.twoLevelCache.TwoLevelTaskDemo;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.DefaultListModel;
import javax.swing.JFormattedTextField;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import javax.swing.border.LineBorder;

import cache.Cache;
import cache.common.CacheMonitor;
import cache.common.CacheNotAvailableException;
import cache.common.SerializableBufferdImage;
import cache.twolevel.TYPE;
import cache.twolevel.TwoLevelCache;

import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;


public class Main {

	private JFrame frame;
	private JTextField urlField;
	private JFormattedTextField maxSizeFirstField;
	private JFormattedTextField maxSizeSecondField;
	private JFormattedTextField recachePartField;

	private DefaultListModel<String> dlm1 = new DefaultListModel<String>();

	private DefaultListModel<String> dlm2 = new DefaultListModel<String>();

	private DefaultListModel<String> dlm3 = new DefaultListModel<String>();

	private ImagesLoader imagesLoader;

	private String url = "http://vao-priut.org/image/lada";

	private Cache<String, SerializableBufferdImage> twoLevelCache;

	private int firstLevelMaxSize = 3;

	private int secondLevelMaxSize = 3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("TwoLevelTaskDemo");
		frame.setBounds(100, 100, 828, 541);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 44, 0, 0, 0, 44, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 36, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);
		
		MyImage myImage = new MyImage();
		
				JLabel lblUrlWebPage = new JLabel("URL WEB Page:");
				GridBagConstraints gbc_lblUrlWebPage = new GridBagConstraints();
				gbc_lblUrlWebPage.anchor = GridBagConstraints.EAST;
				gbc_lblUrlWebPage.insets = new Insets(0, 0, 5, 5);
				gbc_lblUrlWebPage.gridx = 1;
				gbc_lblUrlWebPage.gridy = 1;
				frame.getContentPane().add(lblUrlWebPage, gbc_lblUrlWebPage);
		
				urlField = new JTextField(url);
				GridBagConstraints gbc_urlField = new GridBagConstraints();
				gbc_urlField.gridwidth = 6;
				gbc_urlField.fill = GridBagConstraints.HORIZONTAL;
				gbc_urlField.insets = new Insets(0, 0, 5, 5);
				gbc_urlField.gridx = 2;
				gbc_urlField.gridy = 1;
				frame.getContentPane().add(urlField, gbc_urlField);
				urlField.setColumns(10);
		
				JLabel lblWebResource = new JLabel("Web resource (select):");
				GridBagConstraints gbc_lblWebResource = new GridBagConstraints();
				gbc_lblWebResource.insets = new Insets(0, 0, 5, 5);
				gbc_lblWebResource.gridx = 5;
				gbc_lblWebResource.gridy = 2;
				frame.getContentPane().add(lblWebResource, gbc_lblWebResource);
				
				JLabel lblImage = new JLabel("Image:");
				GridBagConstraints gbc_lblImage = new GridBagConstraints();
				gbc_lblImage.insets = new Insets(0, 0, 5, 5);
				gbc_lblImage.gridx = 7;
				gbc_lblImage.gridy = 2;
				frame.getContentPane().add(lblImage, gbc_lblImage);
		
				JLabel lblOptions = new JLabel("Options");
				GridBagConstraints gbc_lblOptions = new GridBagConstraints();
				gbc_lblOptions.anchor = GridBagConstraints.SOUTH;
				gbc_lblOptions.gridwidth = 2;
				gbc_lblOptions.insets = new Insets(0, 0, 5, 5);
				gbc_lblOptions.gridx = 1;
				gbc_lblOptions.gridy = 3;
				frame.getContentPane().add(lblOptions, gbc_lblOptions);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 4;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 5;
		gbc_scrollPane.gridy = 3;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);

		JList list = new JList(dlm1);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				try {					
					
					if(arg0.getValueIsAdjusting()) {
						
						int index = list.getSelectedIndex();						

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
			}
		);
		scrollPane.setViewportView(list);

		JPanel panel = new JPanel();
		panel.add(myImage);
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridheight = 12;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 7;
		gbc_panel.gridy = 3;
		frame.getContentPane().add(panel, gbc_panel);

		JLabel lblFirstLevel = new JLabel("First Level");
		GridBagConstraints gbc_lblFirstLevel = new GridBagConstraints();
		gbc_lblFirstLevel.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirstLevel.gridx = 1;
		gbc_lblFirstLevel.gridy = 4;
		frame.getContentPane().add(lblFirstLevel, gbc_lblFirstLevel);

		JLabel lblSecondLevel = new JLabel("Second Level");
		GridBagConstraints gbc_lblSecondLevel = new GridBagConstraints();
		gbc_lblSecondLevel.insets = new Insets(0, 0, 5, 5);
		gbc_lblSecondLevel.gridx = 2;
		gbc_lblSecondLevel.gridy = 4;
		frame.getContentPane().add(lblSecondLevel, gbc_lblSecondLevel);

		JLabel lblType = new JLabel("Type:");
		GridBagConstraints gbc_lblType = new GridBagConstraints();
		gbc_lblType.gridwidth = 2;
		gbc_lblType.insets = new Insets(0, 0, 5, 5);
		gbc_lblType.gridx = 1;
		gbc_lblType.gridy = 6;
		frame.getContentPane().add(lblType, gbc_lblType);

		JRadioButton lfuFirstRB = new JRadioButton("LFU");
		lfuFirstRB.setSelected(true);
		GridBagConstraints gbc_lfuFirstRB = new GridBagConstraints();
		gbc_lfuFirstRB.insets = new Insets(0, 0, 5, 5);
		gbc_lfuFirstRB.gridx = 1;
		gbc_lfuFirstRB.gridy = 7;
		frame.getContentPane().add(lfuFirstRB, gbc_lfuFirstRB);

		JRadioButton lfuSecondRB = new JRadioButton("LFU");
		lfuSecondRB.setSelected(true);
		GridBagConstraints gbc_lfuSecondRB = new GridBagConstraints();
		gbc_lfuSecondRB.anchor = GridBagConstraints.WEST;
		gbc_lfuSecondRB.insets = new Insets(0, 0, 5, 5);
		gbc_lfuSecondRB.gridx = 2;
		gbc_lfuSecondRB.gridy = 7;
		frame.getContentPane().add(lfuSecondRB, gbc_lfuSecondRB);

		JLabel lblFirstLevelCache = new JLabel("First Level Cache");
		GridBagConstraints gbc_lblFirstLevelCache = new GridBagConstraints();
		gbc_lblFirstLevelCache.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirstLevelCache.gridx = 5;
		gbc_lblFirstLevelCache.gridy = 7;
		frame.getContentPane().add(lblFirstLevelCache, gbc_lblFirstLevelCache);

		JRadioButton lruFirstRB = new JRadioButton("LRU");
		GridBagConstraints gbc_lruFirstRB = new GridBagConstraints();
		gbc_lruFirstRB.insets = new Insets(0, 0, 5, 5);
		gbc_lruFirstRB.gridx = 1;
		gbc_lruFirstRB.gridy = 8;
		frame.getContentPane().add(lruFirstRB, gbc_lruFirstRB);

		JRadioButton lruSecondRB = new JRadioButton("LRU");
		GridBagConstraints gbc_lruSecondRB = new GridBagConstraints();
		gbc_lruSecondRB.anchor = GridBagConstraints.WEST;
		gbc_lruSecondRB.insets = new Insets(0, 0, 5, 5);
		gbc_lruSecondRB.gridx = 2;
		gbc_lruSecondRB.gridy = 8;
		frame.getContentPane().add(lruSecondRB, gbc_lruSecondRB);
		
		
		
		lfuFirstRB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lruFirstRB.setSelected(false);
			}
		});
		
		lruFirstRB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lfuFirstRB.setSelected(false);
			}
		});
		
		
		lfuSecondRB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lruSecondRB.setSelected(false);
			}
		});
		
		lruSecondRB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lfuSecondRB.setSelected(false);
			}
		});
		
		
		
		

		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.gridheight = 3;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 5;
		gbc_scrollPane_1.gridy = 8;
		frame.getContentPane().add(scrollPane_1, gbc_scrollPane_1);

		JList list_1 = new JList(dlm2);
		list_1.setEnabled(false);
		scrollPane_1.setViewportView(list_1);

		JLabel lblMaxSize = new JLabel("Max Size");
		GridBagConstraints gbc_lblMaxSize = new GridBagConstraints();
		gbc_lblMaxSize.insets = new Insets(0, 0, 5, 5);
		gbc_lblMaxSize.gridwidth = 2;
		gbc_lblMaxSize.gridx = 1;
		gbc_lblMaxSize.gridy = 9;
		frame.getContentPane().add(lblMaxSize, gbc_lblMaxSize);
		
		
		NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());		
		DecimalFormat decimalFormat = (DecimalFormat) numberFormat;

		maxSizeFirstField = new JFormattedTextField(decimalFormat);
		maxSizeFirstField.setText(String.valueOf(firstLevelMaxSize));
		GridBagConstraints gbc_maxSizeFirstField = new GridBagConstraints();
		gbc_maxSizeFirstField.fill = GridBagConstraints.HORIZONTAL;
		gbc_maxSizeFirstField.insets = new Insets(0, 0, 5, 5);
		gbc_maxSizeFirstField.gridx = 1;
		gbc_maxSizeFirstField.gridy = 10;
		frame.getContentPane().add(maxSizeFirstField, gbc_maxSizeFirstField);
		maxSizeFirstField.setColumns(10);

		maxSizeSecondField = new JFormattedTextField(decimalFormat);
		maxSizeSecondField.setText(String.valueOf(secondLevelMaxSize));
		GridBagConstraints gbc_maxSizeSecondField = new GridBagConstraints();
		gbc_maxSizeSecondField.fill = GridBagConstraints.HORIZONTAL;
		gbc_maxSizeSecondField.insets = new Insets(0, 0, 5, 5);
		gbc_maxSizeSecondField.gridx = 2;
		gbc_maxSizeSecondField.gridy = 10;
		frame.getContentPane().add(maxSizeSecondField, gbc_maxSizeSecondField);
		maxSizeSecondField.setColumns(10);

		JLabel lblRecachePart = new JLabel("Recache part:");
		GridBagConstraints gbc_lblRecachePart = new GridBagConstraints();
		gbc_lblRecachePart.insets = new Insets(0, 0, 5, 5);
		gbc_lblRecachePart.gridx = 1;
		gbc_lblRecachePart.gridy = 11;
		frame.getContentPane().add(lblRecachePart, gbc_lblRecachePart);

		JLabel lblSecondLevelCache = new JLabel("Second Level Cache");
		GridBagConstraints gbc_lblSecondLevelCache = new GridBagConstraints();
		gbc_lblSecondLevelCache.insets = new Insets(0, 0, 5, 5);
		gbc_lblSecondLevelCache.gridx = 5;
		gbc_lblSecondLevelCache.gridy = 11;
		frame.getContentPane().add(lblSecondLevelCache, gbc_lblSecondLevelCache);

		recachePartField = new JFormattedTextField(decimalFormat);
		recachePartField.setText("25");
		GridBagConstraints gbc_recachePartField = new GridBagConstraints();
		gbc_recachePartField.anchor = GridBagConstraints.NORTH;
		gbc_recachePartField.fill = GridBagConstraints.HORIZONTAL;
		gbc_recachePartField.insets = new Insets(0, 0, 5, 5);
		gbc_recachePartField.gridx = 1;
		gbc_recachePartField.gridy = 12;
		frame.getContentPane().add(recachePartField, gbc_recachePartField);
		recachePartField.setColumns(3);
		
		JLabel label = new JLabel("%");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.NORTHWEST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 2;
		gbc_label.gridy = 12;
		frame.getContentPane().add(label, gbc_label);

		JScrollPane scrollPane_2 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
		gbc_scrollPane_2.gridheight = 3;
		gbc_scrollPane_2.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_2.gridx = 5;
		gbc_scrollPane_2.gridy = 12;
		frame.getContentPane().add(scrollPane_2, gbc_scrollPane_2);

		JList list_2 = new JList(dlm3);
		list_2.setEnabled(false);
		scrollPane_2.setViewportView(list_2);
		
		JButton btnRefra = new JButton("Load\\Refresh");
		btnRefra.addActionListener(new ActionListener() {

	public void actionPerformed(ActionEvent arg0) {
		try {

			dlm1.clear();
			dlm2.clear();
			dlm3.clear();

			firstLevelMaxSize = Integer.parseInt(maxSizeFirstField.getText());

			secondLevelMaxSize = Integer.parseInt(maxSizeSecondField.getText());
			
			float part = Float.parseFloat(recachePartField.getText())/100;
			
			TYPE level1 = lfuFirstRB.isSelected() ? TYPE.LFU : TYPE.LRU;
			
			TYPE level2 = lfuSecondRB.isSelected() ? TYPE.LFU : TYPE.LRU;
			
			twoLevelCache =
			new TwoLevelCache<String, SerializableBufferdImage>(firstLevelMaxSize, secondLevelMaxSize, part,level1, level2 );
			
			

			imagesLoader = new ImagesLoader(urlField.getText());
			
			if(imagesLoader.getLinks().isEmpty()) {
				urlField.setText("Images not found");
			}

			for (int i = 0; i < imagesLoader.getLinks().size(); i++) {

				String name = "image_" + (i + 1);

				dlm1.add(i, name);

			}

		} catch (IOException e1) {

			e1.printStackTrace();
		}

	}});

	GridBagConstraints gbc_btnRefra = new GridBagConstraints();gbc_btnRefra.gridwidth=2;gbc_btnRefra.insets=new Insets(0,0,5,5);gbc_btnRefra.gridx=1;gbc_btnRefra.gridy=14;frame.getContentPane().add(btnRefra,gbc_btnRefra);
}

}
