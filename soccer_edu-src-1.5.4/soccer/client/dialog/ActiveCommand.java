/* ActiveCommand.java
   This class stores necessary info for an active subprocess

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

import java.io.*;

public class ActiveCommand {
	private String command;
	private Process p;
	private OutputUpdater ou;
	private PrintStream ps;

	public ActiveCommand(
		String command,
		Process p,
		OutputUpdater ou,
		PrintStream ps) {
		this.command = command;
		this.p = p;
		this.ou = ou;
		this.ps = ps;
	}

	public String toString() {
		return command;
	}

	public Process getProcess() {
		return p;
	}

	public OutputUpdater getOutputUpdater() {
		return ou;
	}

	public PrintStream getPrintStream() {
		return ps;
	}
}
