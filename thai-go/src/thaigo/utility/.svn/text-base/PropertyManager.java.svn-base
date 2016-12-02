package thaigo.utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class PropertyManager {

	/** Default location of properties file*/
	private static String FILENAME = "thaigo.prop";
	/** Number of trying using external file. */
	private static int count = 1;
	private static final int MAX = 10;

	/**
	 * Add property to the file
	 * @param key key of property
	 * @param value value of property
	 * @return true if could add property to the file, otherwise false
	 */
	public static boolean setProperty(String key, String value) {
		count = 1;
		if (key.equals("mode"))
			FILENAME = "thaigo." + value + ".prop";

		try {
			HashMap<String, String> deserialized = deserialize(FILENAME);

			if (deserialized == null) {
				HashMap<String, String> hashmap = new HashMap<String, String>();
				hashmap.put(key, value);
				serialize(hashmap, FILENAME);
			}
			else {
				deserialized.put(key, value);
				serialize(deserialized, FILENAME);
			}
			return true;
		}
		catch (Exception e) { 
			System.out.println("setProperty Exception");
			return false;
		}
	}

	/**
	 * Return property which is match with the key.
	 * @param key key of property
	 * @return value of property
	 */
	public static String getProperty(String key) {
		count = 1;
		HashMap<String, String> deserialized = deserialize(FILENAME);
		return deserialized.get(key);
	}

	/**
	 * Serialize data to a file.
	 * @param hashmap <code>HashMap<String, String></code> will be serialized to a file
	 * @param filename name of a file
	 * @throws IOException
	 */
	private static void serialize(HashMap<String, String> hashmap, String filename) throws IOException {
		try
		{
			FileOutputStream fileOut = new FileOutputStream(FILENAME);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(hashmap);
			out.close();
			fileOut.close();
			return;
		} catch(IOException e) {
			System.out.println("serialize IOException : " + count++);
			if (count == MAX)
				return;
			serialize(hashmap, filename);
		}
		return;
	}

	/**
	 * Deserialize a file to data.
	 * @param filename name of a file
	 * @return <code>HashMap<String, String></code> object
	 */
	private static HashMap<String, String> deserialize(String filename) {

		HashMap hashmap = new HashMap<String, String>();

		try {
			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			hashmap = (HashMap<String, String>)in.readObject();
			in.close();
			fileIn.close();

		} catch (FileNotFoundException e) {
				return null;
		} catch(ClassNotFoundException e) {
			System.out.println("deserialize ClassNotFoundException : " + count++);
			if (count == MAX)
				return null;
			deserialize(filename);
		} catch(IOException e) {
			System.out.println("deserialize IOException : " + count++);
			if (count == MAX)
				return null;
			deserialize(filename);
		}
		return hashmap;
	}

}
