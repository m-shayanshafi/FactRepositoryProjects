package via.aventurica.view.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JOptionPane;

import via.aventurica.ViaAventurica;
import via.aventurica.io.WorksheetIO;
import via.aventurica.view.utils.AppIcons;
import via.aventurica.view.utils.ExtendedWorksheetAction;
import via.aventurica.view.utils.JFileChooserEnchanced;

public class SaveRouteAction extends ExtendedWorksheetAction {
	private final static long serialVersionUID = 1L;

	private final JFileChooserEnchanced saveFilechooser; 
	
	public SaveRouteAction() { 
		super("Route Speichern", AppIcons.SAVE_ROUTE);

		saveFilechooser = new JFileChooserEnchanced(ViaAventurica.APPLICATION, JFileChooserEnchanced.FileChooserType.SAVE_FILE, "ViaAventurica Routen (XML)", ViaAventurica.SAVE_EXTENSION); 
		saveFilechooser.setDialogTitle("Route Speichern"); 
 
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(getWorksheet().routeManager.getCurrentRoute().getWaypointCount()==0) {
			JOptionPane.showMessageDialog(ViaAventurica.APPLICATION, "Bitte erst eine Route erstellen!"); 
			return; 
		}
			
		File saveFile = saveFilechooser.openDialog(); 
		if(saveFile!=null) {
			if(!saveFile.getName().endsWith(ViaAventurica.SAVE_EXTENSION))
				saveFile = new File(saveFile.getAbsolutePath()+ViaAventurica.SAVE_EXTENSION); 
			if(saveFile.exists()) { 
				int result = JOptionPane.showConfirmDialog(ViaAventurica.APPLICATION, "Die Datei "+saveFile.getName()+" existiert bereits\nÜberschreiben?", "Datei überschreiben", JOptionPane.YES_NO_OPTION);
				if(result != JOptionPane.YES_OPTION) {
					actionPerformed(e); 
					return; 
				}
			}
			try { 
				new WorksheetIO().saveWorksheet(getWorksheet(), saveFile); 
				//RouteIO.saveRoute(saveFile);
			} catch(Exception ex) { 
				ex.printStackTrace();
				JOptionPane.showMessageDialog(ViaAventurica.APPLICATION, "Beim Speichern der Route ist folgender Fehler aufgetreten:\n"+ex.getLocalizedMessage(), "Konnte Route nicht speichern", JOptionPane.ERROR_MESSAGE); 
			}
		
		}
	}
}
