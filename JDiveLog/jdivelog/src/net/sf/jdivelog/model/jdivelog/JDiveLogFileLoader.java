/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: JDiveLogFileLoader.java
 *
 * @author Volker Holthaus <v.holthaus@procar.de>
 *
 * This file is part of JDiveLog.
 * JDiveLog is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * JDiveLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JDiveLog; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sf.jdivelog.model.jdivelog;

import java.io.File;

import net.sf.jdivelog.gui.ImportWindow;
import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.gui.commands.CommandLoadFile;
import net.sf.jdivelog.gui.commands.CommandManager;
import net.sf.jdivelog.model.JDiveLog;

public class JDiveLogFileLoader {
	
    private File file = null;       
    
    private MainWindow mainWindow = null;
    
    private JDiveLog import_logbook = new JDiveLog();

	public JDiveLogFileLoader(MainWindow mainWindow, File file) {
        this.mainWindow = mainWindow;
        this.file = file;
	}
	
	public void load() {
        CommandLoadFile cmd = new CommandLoadFile(mainWindow, file, true, this);
        CommandManager.getInstance().execute(cmd);
        ImportWindow iw = new ImportWindow(mainWindow, import_logbook);
        iw.setVisible(true);    		
	}
	
	public void setImport_logbook(JDiveLog import_logbook) {
		this.import_logbook = import_logbook;
	}

}
