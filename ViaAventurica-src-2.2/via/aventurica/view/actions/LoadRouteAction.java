package via.aventurica.view.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JOptionPane;

import via.aventurica.ViaAventurica;
import via.aventurica.io.WorksheetIO;
import via.aventurica.view.utils.AppIcons;
import via.aventurica.view.utils.ExtendedWorksheetAction;
import via.aventurica.view.utils.JFileChooserEnchanced;

public class LoadRouteAction extends ExtendedWorksheetAction {
	private final static long serialVersionUID = 1L;

	private JFileChooserEnchanced openFilechooser; 
	
	public LoadRouteAction() { 
		super("Route Laden", AppIcons.LOAD_ROUTE);
		openFilechooser = new JFileChooserEnchanced(ViaAventurica.APPLICATION, JFileChooserEnchanced.FileChooserType.OPEN_FILE, "ViaAventurica Routen (XML)", ViaAventurica.SAVE_EXTENSION);
		openFilechooser.setDialogTitle("ViaAventurica Route laden"); 
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		File f = openFilechooser.openDialog(); 
		if(f!=null)
			try { 
				new WorksheetIO().loadWorksheet(f); 
			} catch(Exception ex) { 
				ex.printStackTrace(); 
				JOptionPane.showMessageDialog(ViaAventurica.APPLICATION, "Fehler beim Laden der Route:\n"+ex.getLocalizedMessage(), "Fehler beim Laden", JOptionPane.ERROR_MESSAGE); 
			}
	
	}
}
