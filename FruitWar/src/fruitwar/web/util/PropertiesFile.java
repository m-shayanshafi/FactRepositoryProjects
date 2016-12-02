package fruitwar.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import fruitwar.util.Logger;

/**
 * Represents a ".properties" file. 
 * Handles:
 * 		read
 * 		write (create)
 * Read operation checks file change time stamp to decide whether to do
 * the operation actually, and write operation checks a "changed" flag
 * of the current object to decide whether to do the write actually.
 * 
 * @author wangnan
 *
 */
public class PropertiesFile {
	private String fileName;
	private Properties props;
	private boolean needSave;
	private long lastUpdateTime;	//last update time
	
	public PropertiesFile(String fileName){
		this.fileName = fileName;
		props = new Properties();
		needSave = true;
		lastUpdateTime = -1;
	}
	
	/**
	 * Try to load the file. If fails, create an empty one.
	 * @return
	 */
	public boolean loadOrCreate(){
	
		boolean success = false;
		success = load();
		if(!success){
			Logger.log("PropertiesFile.loadOrCreate. File not found: " + fileName);
			success = save();
			Logger.log("PropertiesFile.loadOrCreate. Create file: " + success);
		}
		return success;
	}
	
	/**
	 * Load content from file content, if necessary
	 * @return
	 */
	public boolean load(){
		long fileModifiedTime = new File(fileName).lastModified();
		if(fileModifiedTime > lastUpdateTime)
			return forceReload();
		return true;
	}
	
	private boolean forceReload(){
				
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fileName);
			props.load(fis);
			needSave = false;
			lastUpdateTime = new File(fileName).lastModified();
			return true;
		} catch (IOException e) {
			Logger.exception(e);
			return false;
		}finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {}
			}
		}
	}
	
	/**
	 * Save the file content.
	 * Return true if the save succeeded or this object is not changed.
	 * Return false if the save failed.
	 * @return
	 */
	public boolean save(){
		if(!needSave)
			return true;

		//store properties to file.
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName);
			props.store(fos, null);
			needSave = false;
			lastUpdateTime = new File(fileName).lastModified();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}finally{
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {}
			}
		}
	}
	
	public void setChanged(boolean changed){
		needSave = changed;
	}
	
	public String get(String key){
		return props.getProperty(key);
	}
	
	public String get(String key, String defaultValue){
		return props.getProperty(key, defaultValue);
	}
	
	public String set(String key, String value){
		if(value == null){
			String old = (String)props.remove(value);
			if(old != null)
				needSave = true;
			return old;
		}
		
		String old = (String)props.getProperty(key);
		if(old == null || !old.equals(value)){
			props.setProperty(key, value);
			needSave = true;
		}
		
		return old;
	}
	
	/**
	 * Delete the file. After this operation this object is invalid.
	 * @return
	 */
	public boolean deleteFile(){
		props = null;
		boolean ret = new File(fileName).delete();
		fileName = null;
		return ret;
	}
}
