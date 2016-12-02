package via.aventurica.view.utils;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

public class JFileChooserEnchanced extends JFileChooser{
	private final static long serialVersionUID = 1L;
	
	public static enum FileChooserType { OPEN_FILE, SAVE_FILE; }
	
	private final Component owner; 
	private final FileChooserType fileChooserType;
	private final String[] allowedExtensions; 
	private final String defaultExtension; 
	
	public JFileChooserEnchanced(final Component owner, final FileChooserType fileChooserType, final String filetypeDescription, final String... allowedExtensios) { 
		super(); 
		setFileFilter(new ExtensionBasedFileFilter(filetypeDescription, allowedExtensios));
		this.allowedExtensions = allowedExtensios; 
		this.defaultExtension = allowedExtensios[0].startsWith(".") ? allowedExtensios[0] : "."+allowedExtensios[0]; 
		this.fileChooserType = fileChooserType; 
		this.owner = owner; 	
	}
	
	public File openDialog() { 
		int dialogResult; 
			if(fileChooserType == FileChooserType.OPEN_FILE)
				dialogResult = showOpenDialog(owner); 
			else 
				dialogResult = showSaveDialog(owner); 
			
		if(dialogResult == APPROVE_OPTION) { 
			File selectedFile = getSelectedFile();
			String filename = selectedFile.getName(); 
			if(fileChooserType == FileChooserType.SAVE_FILE) {
				boolean rightExtension = false; 
				for(String ext : allowedExtensions)
					if(filename.endsWith(ext)) {
						rightExtension = true; 
						break; 
					}
				if(rightExtension == false)
					selectedFile = new File(selectedFile.getAbsolutePath()+defaultExtension);
			}
			return selectedFile; 
		} else return null; 
		 
	}
	
}
