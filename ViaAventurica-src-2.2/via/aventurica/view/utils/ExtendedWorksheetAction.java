package via.aventurica.view.utils;

import java.awt.event.ActionEvent;

import via.aventurica.ViaAventurica;
import via.aventurica.model.worksheet.IWorksheetListener;
import via.aventurica.model.worksheet.Worksheet;

/**
 * Erweiterte Action, die ein Icon, eine Beschriftung und einen Tooltip Text verwalten kann. 
 */
public abstract class ExtendedWorksheetAction extends ExtAction implements IWorksheetListener{

	private static final long serialVersionUID = 1L;
	private Worksheet worksheet; 
	
	public ExtendedWorksheetAction(String title, AppIcons image) {
		super(title, image);
		ViaAventurica.addWorksheetListener(this); 
	}

	public ExtendedWorksheetAction(String title, String toolTip, AppIcons image) {
		super(title, toolTip, image);
		ViaAventurica.addWorksheetListener(this); 
	}

	public ExtendedWorksheetAction(String title) {
		super(title);
		ViaAventurica.addWorksheetListener(this); 
	}

	public abstract void actionPerformed(ActionEvent e);  
	
	
	public void worksheetChanged(Worksheet newWorksheet) {
		worksheet = newWorksheet; 
	}
	

	public Worksheet getWorksheet() {
		return worksheet;
	}

}
