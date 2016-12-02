package fruitwar.supp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import fruitwar.util.Logger;

public class RobotProps {
	
	public static final String FRUITWAR_PROP_ENTRY_NAME = "fruitwar.properties";
	
	public static final String ROBOT_CLASS = "fwrobot.class";
	public static final String AUTHOR = "fwrobot.author";
	public static final String EMAIL = "fwrobot.email";	//author email
	public static final String DESCRIPTION = "fwrobot.description";
	
	protected Properties props;
	
	public RobotProps(){
		props = new Properties();
	}
	
	public String get(String key){
		return props.getProperty(key);
	}
	
	public void set(String key, String value){
		if(value == null)
			props.remove(key);
		else
			props.setProperty(key, value);
	}
	
	/**
	 * Check whether this object is a valid RobotProperties object.
	 * @return
	 */
	public boolean isValid(){
		String s = get(ROBOT_CLASS);
		return s != null && s.length() > 0;
	}
	

	/**
	 * Get the name of the robot.
	 * 
	 * @return
	 */
	public String getName() {
		return get(ROBOT_CLASS);
	}
	
	public String getAuthor() {
		return get(AUTHOR);
	}
	
	public static RobotProps loadFromFile(String fileName){
		
		JarFile jar = null;
		InputStream is = null;
		try{
			jar = new JarFile(fileName);
			ZipEntry ze = jar.getEntry(FRUITWAR_PROP_ENTRY_NAME);
			is = jar.getInputStream(ze);

			RobotProps cp = new RobotProps();
			cp.props.load(is);
			
			is.close();
			jar.close();
			
			if(!cp.isValid()){
				Logger.log("The loaded prop file is not a RobotProperties object: " + fileName);
				return null;
			}
			
			return cp;
		}catch(IOException e){
			
			Logger.log("Fail loading robot file: " + fileName);
			Logger.exception(e);
			
			try{
				if(jar != null)
					jar.close();
			}catch(IOException e1){}
			
			return null;
		}
	}

	

	static class ZipOutputFile {
		
		ZipOutputStream zos;
	
		public ZipOutputFile(String fileName) throws FileNotFoundException{
			FileOutputStream fos = new FileOutputStream(fileName);
			zos = new ZipOutputStream(fos);
		}
		
		/**
		 * Do NOT close the returned stream.
		 */
		public OutputStream getEntryStream(String entryName) throws IOException{
			
			ZipEntry e = new ZipEntry(entryName);
			zos.putNextEntry(e);
			return zos;
		}

		public void close() throws IOException {
			if(zos != null)
				zos.close();
		}
	}

	
	/**
	 * Update properties described by this object to the target file.
	 * @param fileName
	 */
	public boolean updateToFile(String fileName) {
		
		//delete old backup file
		String backupName = fileName + ".bak";
		File backupFile = new File(backupName);
		if(backupFile.exists())
			backupFile.delete();
		
		//move current file to backup
		File f = new File(fileName);
		if(!f.renameTo(backupFile)){
			Logger.error("Fail renaming file from " + fileName + " to " + backupFile);
			return false;
		}
		
		ZipFile oldFile = null;
		ZipOutputFile newFile = null;
		try{
			//copy all entries from backup file to new file, except the
			//entry we want to update.
			oldFile = new ZipFile(backupName);
			newFile = new ZipOutputFile(fileName);
				
			//for all the entries in old file, write them to target, except the new item entry we want to write.
			Enumeration enu = oldFile.entries();
			
			while(enu.hasMoreElements()){
				ZipEntry entry = (ZipEntry) enu.nextElement();
				//if it's our target object, skip writing the stream. we'll write it later
				if(entry.getName().equals(FRUITWAR_PROP_ENTRY_NAME))
					continue;
				InputStream in = oldFile.getInputStream(entry);
				OutputStream out = newFile.getEntryStream(entry.getName());
				
				//copy
				final int BUF_LEN = 1000;
				byte[] buf = new byte[BUF_LEN];
				for(;;){
					int n = in.read(buf);
					if(n == -1)	//stream end
						break;
					out.write(buf, 0, n);
					if(n < BUF_LEN)
						break;
				}
			}
			
			//write our entry
			OutputStream out = newFile.getEntryStream(FRUITWAR_PROP_ENTRY_NAME);
			props.store(out, null);
		}catch(IOException e){
			Logger.exception(e);
			return false;
		}
		
		if(newFile != null){
			try {
				newFile.close();
			} catch (IOException e) {}
		}
		
		if(oldFile != null){
			try {
				oldFile.close();
			} catch (IOException e) {}
		}
		
		//delete backup file
		new File(backupName).delete();
		
		return true;
	}

}
