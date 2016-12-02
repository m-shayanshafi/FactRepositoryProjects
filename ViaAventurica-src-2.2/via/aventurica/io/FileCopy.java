package via.aventurica.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileCopy {
	private final static long serialVersionUID = 1L;
	
	public final static void copyFile(final File in, final File out) throws IOException {
	    InputStream fis  = new BufferedInputStream(new FileInputStream(in));
	    OutputStream fos = new BufferedOutputStream(new FileOutputStream(out));
	    try {
	        byte[] buf = new byte[1024];
	        int i = 0;
	        while ((i = fis.read(buf)) != -1) {
	            fos.write(buf, 0, i);
	        }
	    } 
	    catch (IOException e) {
	        throw e;
	    }
	    finally {
	    	try { 
	    		if (fis != null) fis.close();
	    		if (fos != null) fos.close();
	    	} catch(Exception ex) { }
	    }
	}
}
