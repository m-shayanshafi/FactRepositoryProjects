package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveManager {
	
	public static final String SAVE_NAME = "save.data";

	public void save(Object[] objs){
		try {
			ObjectOutputStream out = new ObjectOutputStream  (new FileOutputStream(SAVE_NAME));
			for(Object obj:objs){
				out.writeObject(obj);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	
	public Object[] load(int number){
		 Object[] ret = new Object[number];
		try {
			ObjectInputStream in = new ObjectInputStream (new FileInputStream(SAVE_NAME));
            for(int i = 0; i < number; i++)
				
					ret[i] = in.readObject();
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		return ret;
			
	}
}
