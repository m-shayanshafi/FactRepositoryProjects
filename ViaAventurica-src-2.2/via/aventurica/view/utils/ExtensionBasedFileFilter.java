package via.aventurica.view.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ExtensionBasedFileFilter extends FileFilter {
	private final static long serialVersionUID = 1L;

	private final String[] allowedExtensions; 
	private final String description; 
	
	public ExtensionBasedFileFilter(String filterDescription, String...saveExtensions) {
		allowedExtensions = new String[saveExtensions.length]; 
		StringBuffer extensions = new StringBuffer(filterDescription+": "); 
		for(int i=0; i<saveExtensions.length; i++) {
			allowedExtensions[i] = saveExtensions[i].toLowerCase();
			extensions.append(allowedExtensions[i]); 
			extensions.append("; "); 
		}
		description = extensions.toString(); 
			
	}
	
	@Override
	public boolean accept(File pathname) {
		if(pathname.isDirectory())
			return true; 
		String filename = pathname.getName().toLowerCase(); 
		for(String ext : allowedExtensions) {
			if(filename.endsWith(ext))
				return true; 
		}
		return false; 
	}

	@Override
	public String getDescription() {
		return description;
	}
}