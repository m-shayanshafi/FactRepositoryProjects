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

import java.io.*;
import java.util.StringTokenizer;

/**
 * 
 * @author Nicolas Devere
 *
 */
public class FSFile {
	
	private String path;
	private int map;
	
	
	public FSFile(String path) {
		this.path = path;
		map = 0;
	}
	
	
	public void setMap(int arg) {
		map = arg;
	}
	
	public int getMap() {
		return map;
	}
	
	
	public void create() {
		try {
			File file = new File(path);
			file.createNewFile();
		}
		catch(Exception ex) {System.out.println(ex.getMessage());}
	}
	
	
	public void load() {
		try {
			File file = new File(path);
			StringTokenizer st;
			BufferedReader br = new BufferedReader(
								new InputStreamReader(
								new FileInputStream(file)));
			
			st = new StringTokenizer(br.readLine());
			map = Integer.parseInt(st.nextToken());
			
			br.close();
		}
		catch(Exception ex) {System.out.println(ex.getMessage());}
	}
	
	
	public void save() {
		try {
			File file = new File(path);
			BufferedWriter bw = new BufferedWriter(
								new OutputStreamWriter(
								new FileOutputStream(file)));
			
			bw.write("" + map + "\n");
			
			bw.close();
		}
		catch(Exception ex) {System.out.println(ex.getMessage());}
	}
	
	
}
