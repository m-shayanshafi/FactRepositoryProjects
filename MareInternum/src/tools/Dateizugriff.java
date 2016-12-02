/*	 
*    This file is part of Mare Internum.
*
*	 Copyright (C) 2008,2009  Johannes Hoechstaedter
*
*    Mare Internum is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    Mare Internum is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with Mare Internum.  If not, see <http://www.gnu.org/licenses/>.
*
*/

package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * Manages all file specific operations
 * @author johannes
 *
 */
public class Dateizugriff {
	
	public static final String PATH_GUI_OBJECTS = "gui.map";
	
	/**
	 * gets full path to the target filename
	 * @param file
	 */
	private static String getFullPath(String filename){
		try {
			File f= new File(".");
			filename = f.getCanonicalPath() + "/" + filename;		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filename;
	}
	
	/**
 	 * reads the content of a whole file into a single String.
	 * Line feeds are included.
	 * @param filename
	 * @return
	 */
	public String readWholeFile(String filename){
		String infos="";
		String ausgabe="";
		
		filename = getFullPath(filename);
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while((infos = reader.readLine())!=null){
				ausgabe=ausgabe+infos+"\n";
				infos="";
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ausgabe;
	}
	
	
	/**
	 * stores an object into a file
	 * @param filename
	 * @param o
	 */
	public static void speicherObject(String filename, Object o){
		try {

			filename = getFullPath(filename);
			
			FileOutputStream ostream = new FileOutputStream(filename);
			ObjectOutputStream output = new ObjectOutputStream(ostream);
			output.writeObject(o);
			output.flush();

		}
		catch(Exception e){
			}
		}
	
	/**
	 * loads an object from a file
	 * @param filename
	 * @return
	 */
	public static Object ladeObject(String filename){
		Object o =null;
		
		try {
			
			filename = getFullPath(filename);
			
			File f = new File(filename);
			
			if(f.exists()){
				FileInputStream istream = new FileInputStream(filename);
				ObjectInputStream input = new ObjectInputStream(istream);
				o = input.readObject();
			}
			
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return o;
	}
}
