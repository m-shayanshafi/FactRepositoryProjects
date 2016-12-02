package via.aventurica.view.actions;

import java.awt.event.ActionEvent;

import via.aventurica.view.utils.AppIcons;
import via.aventurica.view.utils.ExtendedWorksheetAction;

public class UndoRouteAction extends ExtendedWorksheetAction {
	private final static long serialVersionUID = 1L;

	public UndoRouteAction() { 
		super("Rückgängig", AppIcons.UNDO_ROUTE); 
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		getWorksheet().routeManager.getCurrentRoute().removeLastPoint(); 
	}
}
