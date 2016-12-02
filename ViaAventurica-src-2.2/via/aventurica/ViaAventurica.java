package via.aventurica;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import via.aventurica.io.WebUpdateNotifier;
import via.aventurica.model.map.Map;
import via.aventurica.model.worksheet.IWorksheetListener;
import via.aventurica.model.worksheet.Worksheet;
import via.aventurica.view.appFrame.AppFrame;
import via.aventurica.view.selectMapDialog.SelectMapDialog;

public class ViaAventurica {
	
	public final static File DEFAULT_SAVE_PATH = new File("Eigene Routen"); 
	public final static String SAVE_EXTENSION = ".va";
	public static final String VERSION = "Ferdok, 2.2"; 
	
	public final static String PROJECT_WEBSITE="http://viaaventurica.sourceforge.net/";
	public static final String UPDATE_URL = PROJECT_WEBSITE+"release-rss.php"; 
	
	public final static File USER_ICONS = new File("icons/"); 
	
	public static AppFrame APPLICATION;
	
	private static Worksheet currentWorksheet; 
	
	private final static ArrayList<IWorksheetListener> worksheetListeners = new ArrayList<IWorksheetListener>(10);

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			
		} 
		  
		Map newMap = null; 
		try { 
			newMap = ConfigManager.getInstance().getLastMap();
			
		} catch(OutOfMemoryError err) { 
			insufficientMemoryError(); 
		}
		APPLICATION = new AppFrame();
		WebUpdateNotifier.runUpdateCheck(); 
		Worksheet ws = null; 
		if(newMap == null)
			try { 
				newMap = SelectMapDialog.openDialog();
				ws = new Worksheet(newMap); 
			} catch(OutOfMemoryError err) { 
				insufficientMemoryError(); 
			}
		else {
			ws = new Worksheet(newMap); 
		}
		setCurrentWorksheet(ws); 
	}
	
	private static void insufficientMemoryError() { 
		JOptionPane.showMessageDialog(APPLICATION, 
				"<html><b>Nicht genügend Speicher</b><br>Starte ViaAventurica aus der Kommandozeile<br>mit folgendem Befehl:<p><code>java -jar -Xmx512M viaaventurica.jar</code><p>Dies löst in 90% aller Fälle das Problem",
				"ViaAventurica kann nicht starten", 
				JOptionPane.ERROR_MESSAGE); 
		System.exit(0); 
	}
	
	public static void addWorksheetListener(IWorksheetListener l) { 
		worksheetListeners.add(l); 
	}
	
	public static void setCurrentWorksheet(Worksheet ws) {
		System.out.println("New Worksheet: "+ws);
		currentWorksheet = ws; 
		for(IWorksheetListener l : worksheetListeners)
			l.worksheetChanged(ws); 
	}
	
	public static Worksheet getCurrentWorksheet() {
		return currentWorksheet;
	}
}
