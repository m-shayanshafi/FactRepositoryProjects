package kw.texasholdem.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Map;

import kw.texasholdem.ai.impl.Player;
import kw.texasholdem.tool.SerializableHashMap;
import kw.texasholdem.tool.Table;

/**
 * Utility class responsible for storing and retrieving.
 * 
 * @author Ken Wu
 */
public class RestoreManager {


	/**
     * Saves the current state to a file object
	 * @param <K>
     * 
     */
	public static void save(Serializable ser, String filePath) {
		File file = new File(filePath);
	    OutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(file);
			ObjectOutputStream  outputStream = new ObjectOutputStream(fileOutputStream);
			outputStream.writeObject(ser);
		    outputStream.flush(); 
		    outputStream.close(); 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static Object restore (String filePath) {
		FileInputStream fis;
		Object object2 = null;
		try {
			fis = new FileInputStream(filePath);
			ObjectInputStream ois = new ObjectInputStream(fis); 
			object2 = ois.readObject();
			ois.close(); 
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
		return object2;
	}

}
