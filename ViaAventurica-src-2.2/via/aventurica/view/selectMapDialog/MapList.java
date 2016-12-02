package via.aventurica.view.selectMapDialog;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import via.aventurica.model.map.MapCollection;
import via.aventurica.model.map.MapInfo;



/**
 * Ein Panel, welches eine Liste enth�lt, aus der man die Karte ausw�hlen kann. 
 * Hierbei handelt es sich um Swing Klassen, da diese Renderer unterst�tzen.
 */
@SuppressWarnings("serial")
public class MapList extends JPanel {

	private JList list; 
	
	MapListModel model; 
	
	/**
	 * Initialisiert die Komponente und legt fest, welche Karten ausw�hlbar sein sollen. 
	 * @param set das {@link MapSet}, dessen Karten ausw�hlbar sein sollen. 
	 */
	public MapList(List<MapCollection> mapCollections)
	{	
		super(new BorderLayout());  
		
		model = new MapListModel(mapCollections); 
		list = new JList(model); 
		list.setSelectedIndex(1); 
		list.setCellRenderer(new MapListRenderer());
		
		
		add(new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER); 
	}
	
	/**
	 * @return gibt die momentan ausgew�hlte karte zur�ck. 
	 */
	public MapInfo getSelectedMapInfo()
	{
		Object selected = list.getSelectedValue(); 
		return selected instanceof MapInfo ? (MapInfo)selected : null; 
	}

	public void updateData() {
		model.fireListChanged(); 
	}
	
}
