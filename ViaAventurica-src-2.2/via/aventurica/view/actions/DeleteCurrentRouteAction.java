package via.aventurica.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import via.aventurica.ViaAventurica;
import via.aventurica.view.utils.AppIcons;
import via.aventurica.view.utils.ExtendedWorksheetAction;

public class DeleteCurrentRouteAction extends ExtendedWorksheetAction {
	private final static long serialVersionUID = 1L;

	public DeleteCurrentRouteAction() {
		super("Ausgewählte Route Löschen", AppIcons.DELETE); 
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int result = JOptionPane.showConfirmDialog(ViaAventurica.APPLICATION, "Soll die Route"+getWorksheet().routeManager.getCurrentRoute().getRouteName()+" wirklich gelöscht werden?", "Route Löschen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(result == JOptionPane.YES_OPTION)
			getWorksheet().routeManager.removeCurrentRoute(); 
	}
}
