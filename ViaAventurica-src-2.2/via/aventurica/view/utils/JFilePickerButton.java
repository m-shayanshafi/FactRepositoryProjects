package via.aventurica.view.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.filechooser.FileSystemView;

public class JFilePickerButton extends JButton{
	private final static long serialVersionUID = 1L;
	
	private File selectedFile; 
	private JFileChooserEnchanced fileChooser; 
	private FileSystemView fsv = FileSystemView.getFileSystemView(); 
	public JFilePickerButton(String fileDescription, String...extensions) { 
		this(null, fileDescription, extensions);
		setIcon(fsv.getSystemIcon(new File("./")));
	}
	
	public JFilePickerButton(File initialFile, String fileDescription, String...extensions) {
		setSelectedFile(initialFile);
		fileChooser = new JFileChooserEnchanced(this, via.aventurica.view.utils.JFileChooserEnchanced.FileChooserType.OPEN_FILE, fileDescription, extensions); 
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File selected = fileChooser.openDialog(); 
				if(selected!=null) 
					setSelectedFile(selected);
			}
			
		}); 
	}
	
	public void setSelectedFile(File selectedFile) {
		this.selectedFile = selectedFile;
		
		if(selectedFile==null) { 
			setText("<html><i>Datei wählen</i></html>"); 
			setToolTipText("Klicken, um eine Datei auszuwählen"); 
			setIcon(fsv.getSystemIcon(new File("./")));
		} else { 
			setText(selectedFile.getName()); 
			setToolTipText(selectedFile.getAbsolutePath());
			setIcon(fsv.getSystemIcon(selectedFile));
		}
	}
	
	public File getSelectedFile() {
		return selectedFile;
	}
	

}
