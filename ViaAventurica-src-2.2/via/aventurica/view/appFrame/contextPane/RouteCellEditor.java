package via.aventurica.view.appFrame.contextPane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

import via.aventurica.model.route.Route;
import via.aventurica.view.utils.AppIcons;
import via.aventurica.view.utils.ExtendedWorksheetAction;
import via.aventurica.view.utils.ToolbarButton;

public class RouteCellEditor extends RouteCellRenderer implements TableCellEditor {
	private final static long serialVersionUID = 1L;
	
	private ArrayList<CellEditorListener> listeners = new ArrayList<CellEditorListener>(); 
	private JTextField routeNameEditor = new JTextField(); 
	private JButton ok = new ToolbarButton(new ExtendedWorksheetAction("OK", AppIcons.ACCEPT) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			stopCellEditing(); 
		} });
	
	private Route route; 
	public RouteCellEditor() { 
		super(); 
		remove(getRouteName()); 
		add(routeNameEditor); 
		add(ok, BorderLayout.EAST);  
	}
	

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		
		route = (Route)value; 
		ws.routeManager.setCurrentRoute(route); 
		getPickerButton().setSelectedColor(route.getRouteColor()); 
		routeNameEditor.setText(route.getRouteName()); 
	
		return this;
	}


	public void cancelCellEditing() {
		fireCellEditingStopped(true); 
	}

	public Object getCellEditorValue() {
		return null;
	}

	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}



	public boolean shouldSelectCell(EventObject anEvent) {
		return false;
	}

	public boolean stopCellEditing() {
		if(routeNameEditor.getText().trim().length() > 0) { 
			route.setRouteColor(getPickerButton().getSelectedColor()); 
			route.setRouteName(routeNameEditor.getText()); 
			fireCellEditingStopped(false); 
			return true;
		} return false; 
	}
	
	private void fireCellEditingStopped(boolean wasCanceled) {
		
		ChangeEvent evt = new ChangeEvent(this); 
		if(wasCanceled) {
			for(CellEditorListener l : listeners) 
				l.editingCanceled(evt);
		} else {
			for(CellEditorListener l : listeners)
				l.editingStopped(evt); 
		}
		
	}
	
	public void addCellEditorListener(CellEditorListener l) {
		listeners.add(l); 
	}
	
	public void removeCellEditorListener(CellEditorListener l) {
		listeners.remove(l); 
	}
	
}
