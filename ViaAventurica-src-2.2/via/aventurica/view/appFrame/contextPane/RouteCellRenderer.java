package via.aventurica.view.appFrame.contextPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableCellRenderer;

import via.aventurica.ViaAventurica;
import via.aventurica.model.route.Route;
import via.aventurica.model.route.RouteControl;
import via.aventurica.model.worksheet.IWorksheetListener;
import via.aventurica.model.worksheet.Worksheet;
import via.aventurica.view.utils.ColorPickerButton;

public class RouteCellRenderer extends JPanel implements TableCellRenderer, IWorksheetListener{
	private final static long serialVersionUID = 1L;

	private ColorPickerButton pickerButton = new ColorPickerButton(); 
	private JLabel routeName = new JLabel(); 
	
	protected Worksheet ws; 
	
	public RouteCellRenderer() { 
		setLayout(new BorderLayout(3, 3)); 
		pickerButton.setBorder(new EtchedBorder()); 
		pickerButton.setPreferredSize(new Dimension(16, 16));
		add(pickerButton, BorderLayout.WEST); 
		add(routeName); 
		
		ViaAventurica.addWorksheetListener(this); 
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Route r = (Route)value;
		String distanceFormat = ws!= null ? ws.map.getDistanceUnit() : "";  
		pickerButton.setSelectedColor(r.getRouteColor()); 
		routeName.setText(r.getRouteName()+" ( "+RouteControl.DISTANCE_FORMAT.format(r.getDistance()) + " "+distanceFormat+")"); 
		
		boolean isCurrent = ws!= null && ws.routeManager.getCurrentRoute() == r; 
		setBackground(isCurrent ? Color.WHITE : Color.LIGHT_GRAY); 
		
		return this; 
	}

	public JLabel getRouteName() {
		return routeName;
	}
	
	ColorPickerButton getPickerButton() {
		return pickerButton;
	}

	public void worksheetChanged(Worksheet newWorksheet) {
		ws = newWorksheet; 
	}
	
	
		
	
	
}
