package via.aventurica.view.actions;

import java.awt.event.ActionEvent;

import via.aventurica.view.aboutDialog.AboutDialog;
import via.aventurica.view.utils.AppIcons;
import via.aventurica.view.utils.ExtendedWorksheetAction;

public class AboutInfoAction extends ExtendedWorksheetAction {
	private final static long serialVersionUID = 1L; 
	
	public AboutInfoAction() { 
		super("Über...", AppIcons.ABOUT);
	 
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new AboutDialog(); 
	}
}
