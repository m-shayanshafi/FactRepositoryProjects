package via.aventurica.view.actions;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import via.aventurica.ViaAventurica;
import via.aventurica.view.utils.AppIcons;
import via.aventurica.view.utils.ExtendedWorksheetAction;
import via.aventurica.view.utils.JFileChooserEnchanced;

public class ScreenshotSaveAsAction extends ExtendedWorksheetAction {
	private final static long serialVersionUID = 1L;
	
	private JFileChooserEnchanced fileChooser; 
	
	public ScreenshotSaveAsAction() { 
		super("Screenshot Speichern", AppIcons.SCREENSHOT_SAVE);
		fileChooser = new JFileChooserEnchanced(ViaAventurica.APPLICATION, JFileChooserEnchanced.FileChooserType.SAVE_FILE, "PNG Bild", ".png"); 
		fileChooser.setDialogTitle("Screenshot Speichern"); 
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(getWorksheet().routeManager.getCurrentRoute().getWaypointCount()==0) {
			JOptionPane.showMessageDialog(ViaAventurica.APPLICATION, "Um einen Screenshot zu machen,\nmuss eine Route erstellt werden.", "Screenshot nicht verfügbar", JOptionPane.ERROR_MESSAGE);
			return; 
		}
		File saveFile = fileChooser.openDialog(); 
		if(saveFile!=null) {
		
			if(saveFile.exists() && JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(ViaAventurica.APPLICATION, "Die Datei: "+saveFile.getName()+" existiert bereits.\nÜberschreiben?", "Datei überschreiben?", JOptionPane.YES_NO_OPTION)) {
				actionPerformed(e); 
				return; 
			}
			
			BufferedImage img = ViaAventurica.APPLICATION.createScreenshot(); 
			if(img == null) {
				JOptionPane.showMessageDialog(ViaAventurica.APPLICATION, "Um einen Screenshot zu machen,\nmuss eine Route erstellt werden.", "Screenshot nicht verfügbar", JOptionPane.ERROR_MESSAGE);
				return; 
			}
			OutputStream imageOutputBuffer = null; 
			try { 
				imageOutputBuffer = new BufferedOutputStream(new FileOutputStream(saveFile)); 
				ImageIO.write(img, "png", imageOutputBuffer);
				 
			} catch(Exception ex) { 
				JOptionPane.showMessageDialog(ViaAventurica.APPLICATION, "Screenshot konnte nicht erstellt werden:\n."+ex.getLocalizedMessage(), "Screenshot nicht verfügbar", JOptionPane.ERROR_MESSAGE);				
			} finally { 
				try {
					imageOutputBuffer.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			
		}
	}
}
