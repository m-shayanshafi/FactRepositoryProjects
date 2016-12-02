/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: CommandSaveFile.java
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
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
package net.sf.jdivelog.gui.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import net.sf.jdivelog.gui.JDiveLogException;
import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.gui.resources.Messages;

/**
 * Description: Save the logbook file into the defind files
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class CommandSaveFile implements Command {
    
    private MainWindow mainWindow = null;
    private File file = null;
    
    public CommandSaveFile(MainWindow mainWindow, File file) {
        this.mainWindow = mainWindow;
        this.file = file;
    }

    /**
     * @see net.sf.jdivelog.gui.commands.Command#execute()
     */
    public void execute() {
        try {
            String stream = mainWindow.getLogBook().toString();
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("<?xml version=\"1.0\" encoding=\""); //$NON-NLS-1$
            bw.write(fw.getEncoding());
            bw.write("\" ?>\n"); //$NON-NLS-1$
            bw.write(stream);
            bw.close();
            mainWindow.setFile(file);
            mainWindow.setChanged(false);
            mainWindow.getLogbookChangeNotifier().notifyLogbookDataChanged();
        } catch (IOException ioe) {
            StringWriter sw = new StringWriter();
            sw.write(Messages.getString("error.could_not_write_file")); //$NON-NLS-1$
            sw.write(" "); //$NON-NLS-1$
            sw.write(file.getName());
            sw.write("!"); //$NON-NLS-1$
            throw new JDiveLogException(Messages.getString("error.could_not_save_file"), sw.toString(), ioe);
        }
    }

}
