package via.aventurica.view.appFrame.markerDialog;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import via.aventurica.model.marker.MarkerIcon;

public class MarkerRenderer extends DefaultListCellRenderer{
	private final static long serialVersionUID = 1L;

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); 
		MarkerIcon icon = (MarkerIcon)value; 
		setText(icon.getFilename()); 
		setIcon(icon.getIcon()); 
		return this; 
	}
	
}
