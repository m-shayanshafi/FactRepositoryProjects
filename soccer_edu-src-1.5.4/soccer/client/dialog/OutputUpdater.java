/* OutputUpdater.java
   This class get info from an input stream and then update the JtextArea
   
   Copyright (C) 2001  Yu Zhang

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the 
   Free Software Foundation, Inc., 
   59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package soccer.client.dialog;

import soccer.common.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class OutputUpdater extends Thread {
	private JTextArea output;
	private DataInputStream input;
	private boolean OK;
	private Vector buffer;
	private int size;

	public OutputUpdater(JTextArea output, DataInputStream input, int size) {
		this.output = output;
		this.input = input;
		OK = true;
		this.size = size;
		buffer = new Vector(size);
	}

	public OutputUpdater(DataInputStream input, int size) {
		output = null;
		this.input = input;
		OK = true;
		this.size = size;
		buffer = new Vector(size);
	}

	public OutputUpdater(DataInputStream input) {
		output = null;
		this.input = input;
		OK = true;
		size = 50; // default buffer size
		buffer = new Vector(size);
	}

	public OutputUpdater() {
		output = null;
		input = null;
		OK = true;
		size = 50; // default buffer size
		buffer = new Vector(size);
	}

	//---------------------------------------------------------------------------
	/**
	 * run
	 */
	public void run() {

		while (OK) {

			try {
				if (input != null) {
					String newLine = Util.readLine(input);
					if (buffer.size() == size) {
						buffer.remove(0);
						buffer.add(newLine);
					} else
						buffer.add(newLine);

					if (output != null)
						display();
				}
			} catch (IOException e) {
				OK = false;
				output.setText("");
				return;
			}

		}
	}

	private void display() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buffer.size(); i++) {
			sb.append((String) buffer.elementAt(i) + "\n");
		}
		output.setText(sb.toString());
	}

	public boolean isOK() {
		return OK;
	}

	public void setOK(boolean OK) {
		this.OK = OK;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public DataInputStream getInput() {
		return input;
	}

	public void setInput(DataInputStream input) {
		this.input = input;
	}

	public JTextArea getOutput() {
		return output;
	}

	public void setOutput(JTextArea output) {
		this.output = output;

	}

	public void clearBuffer() {
		buffer.clear();
	}

}
