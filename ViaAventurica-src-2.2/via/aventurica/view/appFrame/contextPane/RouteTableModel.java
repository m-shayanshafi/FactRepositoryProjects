package via.aventurica.view.appFrame.contextPane;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import via.aventurica.ViaAventurica;
import via.aventurica.model.route.IRouteListener;
import via.aventurica.model.route.Route;
import via.aventurica.model.route.RouteUpdateEvent;
import via.aventurica.model.route.RouteUpdateEvent.EventType;
import via.aventurica.model.worksheet.IWorksheetListener;
import via.aventurica.model.worksheet.Worksheet;

public class RouteTableModel extends DefaultTableModel implements IWorksheetListener, IRouteListener {
	private final static long serialVersionUID = 1L; 
	private Worksheet ws; 
	private ArrayList<Route> routes; 
	
	public RouteTableModel() { 
		ViaAventurica.addWorksheetListener(this); 
	}
	
	public void worksheetChanged(Worksheet newWorksheet) {
		ws = newWorksheet; 
		if(ws!=null) { 
			routes = ws.routeManager.getRoutes();
			ws.routeManager.addRouteListener(this); 
		}
		fireTableStructureChanged(); 
	}
	
	public void routeChanged(RouteUpdateEvent event) {
	
		if(event.type == EventType.ROUTE_DELETED || event.type == EventType.NEW_ROUTE || event.type == EventType.ROUTE_FOCUSED)
			fireTableDataChanged(); 
		else { 
			int index = routes.indexOf(event.source); 
			fireTableRowsUpdated(index, index); 
		}
		
	}
	

	public Class<?> getColumnClass(int columnIndex) {
		return Route.class;
	}

	public int getColumnCount() {
		return 1;
	}

	public String getColumnName(int columnIndex) {
		return "Routen";
	}

	public int getRowCount() { 
		return ws == null ? 0 : ws.routeManager.getRouteCount();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return ws.routeManager.getRoutes().get(rowIndex);
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		
	}


	
}
