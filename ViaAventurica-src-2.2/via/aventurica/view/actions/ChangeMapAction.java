package via.aventurica.view.actions;

import java.awt.event.ActionEvent;

import via.aventurica.ViaAventurica;
import via.aventurica.model.map.Map;
import via.aventurica.model.worksheet.Worksheet;
import via.aventurica.view.selectMapDialog.SelectMapDialog;
import via.aventurica.view.utils.AppIcons;
import via.aventurica.view.utils.ExtendedWorksheetAction;

public class ChangeMapAction extends ExtendedWorksheetAction {
	private final static long serialVersionUID = 1L;

	public ChangeMapAction() { 
		super("Karte Auswählen", AppIcons.MAP_CHOOSE); 
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Map map = SelectMapDialog.openDialog();
		if(map!=null)
			ViaAventurica.setCurrentWorksheet(new Worksheet(map));  
	}
}
