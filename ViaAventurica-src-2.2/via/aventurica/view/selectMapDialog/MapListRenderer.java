package via.aventurica.view.selectMapDialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import via.aventurica.model.map.MapCollection;
import via.aventurica.model.map.MapInfo;
import via.aventurica.view.appFrame.mapView.scrollpane.MapViewDecoration;


/**
 * Klasse zum Darstellen einer Karte in der Auswahlliste. 
 * Dieser Renderer zeigt Name und Bild an. 
 */
@SuppressWarnings("serial")
public class MapListRenderer extends JLabel implements ListCellRenderer {

	private HashMap<File, Icon> icons;
	private BufferedImage notFoundImage; 
	private ImageIcon defaultIcon; 
	
	private final JLabel headerListRenderer;
	
	public MapListRenderer()
	{
		super(); 
		setOpaque(true);
		setPreferredSize(new Dimension(50, 50)); 
		headerListRenderer = new JLabel();
		headerListRenderer.setOpaque(true); 
		headerListRenderer.setBackground(MapViewDecoration.RULER_COLOR_1); 
		headerListRenderer.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK), BorderFactory.createEmptyBorder(2,4,2,4))); 
		icons = new HashMap<File, Icon>();
		try {
			notFoundImage = ImageIO.read(getClass().getResourceAsStream("/via/aventurica/nopreview.png")); 
			defaultIcon = new ImageIcon(notFoundImage);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY), BorderFactory.createEmptyBorder(4,4,4,4))); 
 
	}
	
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if(value.getClass().equals(MapCollection.class)) {
			headerListRenderer.setText("<html><b>"+((MapCollection)value).getCollectionName()); 
			return headerListRenderer; 
		}
		
		MapInfo map = ((MapInfo)value); 
		setText("<html><b>"+map.getTitle()+"</b><br>Karte: "+map.getFile().getPath()+"<br>Lizenz: "+map.getLicenseInfo()+"</html>");
		setBackground( isSelected ? Color.GRAY : Color.WHITE);
		setForeground(isSelected ? Color.WHITE : Color.BLACK );
		setIcon(getIcon(map.getPreviewFile())); 
		return this; 
	}
	
	private Icon getIcon(File f)
	{
		if(f==null || !f.exists())
			return defaultIcon; 
		if(!icons.containsKey(f))
		{
			try { 
				BufferedImage img = ImageIO.read(f); 
				if(img.getWidth() != notFoundImage.getWidth() )
				{
					double ratio = (double)img.getHeight() / (double)img.getWidth();
					BufferedImage newImage = new BufferedImage(notFoundImage.getWidth(), (int)(notFoundImage.getWidth() * ratio), BufferedImage.TYPE_INT_ARGB);
					newImage.getGraphics().drawImage(img, 0, 0, newImage.getWidth(), newImage.getHeight(), 0, 0, img.getWidth(), img.getHeight(), null);
					img = newImage; 
 
				}
				Graphics imG = img.getGraphics(); 
				imG.setColor(Color.BLACK);  
				imG.drawRect(0, 0, img.getWidth()-1, img.getHeight()-1);
				icons.put(f, new ImageIcon(img));
			} catch(Exception e)
			{
				e.printStackTrace(); 
				return defaultIcon; 
			}
		}
		return icons.get(f); 
	}

}
