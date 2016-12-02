package via.aventurica.view.utils;

import java.io.File;
import java.io.FilenameFilter;

public class ExtensionBasedFilenameFilter implements FilenameFilter {
	private final static long serialVersionUID = 1L;

	private final String[] allowedExtensions; 

	
	public ExtensionBasedFilenameFilter(String...saveExtensions) {
		allowedExtensions = saveExtensions; 
			
	}


	public boolean accept(File dir, String name) {
		for(String ext : allowedExtensions)
			if(name.endsWith(ext))
				return true; 
		return false; 
	}
}
