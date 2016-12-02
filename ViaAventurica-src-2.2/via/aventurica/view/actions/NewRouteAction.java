package via.aventurica.view.actions;

import java.awt.event.ActionEvent;

import via.aventurica.view.utils.AppIcons;
import via.aventurica.view.utils.ExtendedWorksheetAction;

public class NewRouteAction extends ExtendedWorksheetAction {
	private final static long serialVersionUID = 1L;

	public NewRouteAction() {
		super("Neue Route", AppIcons.NEW_ROUTE); 
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		getWorksheet().routeManager.newRoute(); 
	}
}
