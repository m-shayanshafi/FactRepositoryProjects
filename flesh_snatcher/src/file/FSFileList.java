//	Copyright 2009 Nicolas Devere
//
//	This file is part of FLESH SNATCHER.
//
//	FLESH SNATCHER is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//
//	FLESH SNATCHER is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with FLESH SNATCHER; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

package file;

import java.io.File;
import java.util.Vector;

/**
 * 
 * @author Nicolas Devere
 *
 */
public class FSFileList {
	
	
	private static String directory = "save/";
	
	private Vector files;
	
	
	public FSFileList() {
		
		try {
			files = new Vector();
			
			File directoryFile = new File(directory);
			if (!directoryFile.exists())
				directoryFile.createNewFile();
			String[] fStrings = directoryFile.list();
			int lenght = fStrings.length;
			
			for (int i=0; i<lenght; i++) {
				FSFile f = new FSFile(directory + i + ".txt");
				f.load();
				files.add(f);
			}
		}
		catch(Exception ex) {System.out.println(ex.getMessage());}
	}
	
	
	public void createFile(int map) {
		try {
			FSFile f = new FSFile(directory + files.size() + ".txt");
			f.setMap(map);
			f.create();
			f.save();
			files.add(f);
		}
		catch(Exception ex) {System.out.println(ex.getMessage());}
	}
	
	
	public void changeFile(int index, int map) {
		FSFile f = (FSFile)files.get(index);
		f.setMap(map);
		f.save();
	}
	
	
	public Vector getFiles() {
		return files;
	}
	
	
	
	public static void main(String[] args) {
		FSFileList fsl = new FSFileList();
		Vector f = fsl.getFiles();
		System.out.println("FILES :");
		for (int i=0; i<f.size(); i++)
			System.out.println( ((FSFile)f.get(i)).getMap() );
		
	}
	
}
