package via.aventurica.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.JToggleButton;

import via.aventurica.ViaAventurica;
import via.aventurica.view.appFrame.mapView.MouseClickBehaviour;
import via.aventurica.view.utils.AppIcons;
import via.aventurica.view.utils.ExtendedWorksheetAction;

public class ToggleClickBehaviourAction extends ExtendedWorksheetAction {

	private static final long serialVersionUID = 1L;

	public ToggleClickBehaviourAction() { 
		super("Notizen Modus/Routen Modus umschalten", AppIcons.NOTE); 
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JToggleButton btn = (JToggleButton)e.getSource(); 
		ViaAventurica.APPLICATION.getMapView().setMouseClickBehaviour(btn.isSelected() ? MouseClickBehaviour.ADD_MARKER : MouseClickBehaviour.ADD_WAYPOINT); 
	}

}
