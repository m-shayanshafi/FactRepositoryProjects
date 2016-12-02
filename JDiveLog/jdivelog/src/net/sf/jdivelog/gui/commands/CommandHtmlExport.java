/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: CommandHtmlExport.java
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

import net.sf.jdivelog.gui.MessageDialog;
import net.sf.jdivelog.gui.ExportException;
import net.sf.jdivelog.gui.HtmlExportUtil;
import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.gui.resources.Messages;

public class CommandHtmlExport implements Command {
    
    private MainWindow mainwindow;
    
    public CommandHtmlExport(MainWindow mainwindow) {
        this.mainwindow = mainwindow;
    }

    public void execute() {
        final HtmlExportUtil exportUtil = new HtmlExportUtil(mainwindow.getStatusBar(), mainwindow.getLogBook());
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    if (exportUtil.execute() > 0) { 
                        mainwindow.setChanged(true);
                    }
                } catch (ExportException ex) {
                    new MessageDialog(mainwindow, Messages.getString("error.export_failed"), ex.getReason(), null, MessageDialog.MessageType.ERROR);
                }
            }
        };
        t.start();
    }

}
