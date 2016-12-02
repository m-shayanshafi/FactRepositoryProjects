package via.aventurica.view.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import via.aventurica.ViaAventurica;
import via.aventurica.view.utils.AppIcons;
import via.aventurica.view.utils.ClipboardImageData;
import via.aventurica.view.utils.ExtendedWorksheetAction;

public class ScreenshotToClipboardAction extends ExtendedWorksheetAction {
	private final static long serialVersionUID = 1L;
	
	
	
	public ScreenshotToClipboardAction() { 
		super("Screenshot Kopieren", "Kopiert einen Screenshot der Route in die Zwischenablage", AppIcons.SCREENSHOT_CLIPBOARD);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		BufferedImage img = ViaAventurica.APPLICATION.createScreenshot(); 
		if(img == null) {
			JOptionPane.showMessageDialog(ViaAventurica.APPLICATION, "Um einen Screenshot zu machen,\nmuss eine Route erstellt werden.", "Screenshot nicht verfügbar", JOptionPane.ERROR_MESSAGE);
			return; 
		}
		ByteArrayOutputStream imageOutputBuffer = new ByteArrayOutputStream(); 
		try { 
			ImageIO.write(img, "png", imageOutputBuffer);
			imageOutputBuffer.close(); 
			byte[] serealizedImage = imageOutputBuffer.toByteArray(); 
			ClipboardImageData clipboardImageData = new ClipboardImageData(serealizedImage); 
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(clipboardImageData, null); 
		} catch(Exception ex) { 
			JOptionPane.showMessageDialog(ViaAventurica.APPLICATION, "Screenshot konnte nicht erstellt werden:\n."+ex.getLocalizedMessage(), "Screenshot nicht verfügbar", JOptionPane.ERROR_MESSAGE);
		}
	}
}
