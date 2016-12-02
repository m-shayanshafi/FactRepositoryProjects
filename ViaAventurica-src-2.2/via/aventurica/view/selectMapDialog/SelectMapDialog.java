package via.aventurica.view.selectMapDialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import via.aventurica.ViaAventurica;
import via.aventurica.model.map.Map;
import via.aventurica.model.map.MapCollection;
import via.aventurica.model.map.MapInfo;
import via.aventurica.model.map.MapLoaderFactory;
import via.aventurica.view.mapcreator.MapCreator;

public class SelectMapDialog extends JDialog {
	private final static long serialVersionUID = 1L;
	
	private final MapList mapList; 
	private MapInfo selectedMap; 
	
	private List<MapCollection> availableMaps; 
	
	@SuppressWarnings("serial")
	private SelectMapDialog() {
		
		//TODO: Haupt Frame hier einfügen!
		super(ViaAventurica.APPLICATION, "Karte Auswählen", true);
 
		setBounds(ViaAventurica.APPLICATION.getX()+30, ViaAventurica.APPLICATION. getY()+30, 480, 310 ); 
		availableMaps = MapLoaderFactory.listMaps(); 
		mapList = new MapList(availableMaps); 
		getRootPane().setBorder(new EmptyBorder(10, 10, 5, 10)); 
		

		
		setLayout(new BorderLayout(5,8)); 
		 
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); 

		 
		buttonPanel.add(new JButton(new AbstractAction("Neue Karte"){
			public void actionPerformed(ActionEvent e) {
				new MapCreator(availableMaps);
				mapList.updateData(); 
			}
		}));
		buttonPanel.add(new JSeparator());
		buttonPanel.add(new JButton(new AbstractAction("Karte Laden"){

			public void actionPerformed(ActionEvent e) {
				loadMapAndDispose(); 
			}
		}));
		buttonPanel.add(new JButton(new AbstractAction("Abbrechen") { 
			public void actionPerformed(ActionEvent e) {
				dispose(); 
			}	
		}));
		
		
		add(new JLabel("Bitte Karte auswählen:"), BorderLayout.NORTH);
		add(mapList, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH); 
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);		 
		setVisible(true); 
		
	}
	
	private void loadMapAndDispose() { 
		selectedMap = mapList.getSelectedMapInfo();
		if(selectedMap!=null)
			dispose(); 
	}
	
	private MapInfo getSelectedMapInfo() { 
		return selectedMap; 
	}
	
	public static Map openDialog() throws OutOfMemoryError{ 
		SelectMapDialog dialog = new SelectMapDialog();
		MapInfo info = dialog.getSelectedMapInfo(); 
		if(info != null) {
			return MapLoaderFactory.loadMap(info); 
		} else return null; 
		
		
	}
}
