/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DiveImportAladinWindow.java
 * 
 * @author Andr&eacute; Schenk <andre_schenk@users.sourceforge.net>
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
package net.sf.jdivelog.gui;

import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.JDive;

/**
 * Description: window for loading Aladin log files
 * 
 * @author Andr&eacute; Schenk
 * @version $Revision: 712 $
 */
public class DiveImportAladinWindow extends DiveImportDataTrakWindow
{
	private static final long serialVersionUID = -5438951890127414354L;

    public DiveImportAladinWindow
        (MainWindow mainWindow, ArrayList<JDive> divesToAdd)
    {
        super (mainWindow, mainWindow, divesToAdd);
        setTitle (Messages.getString ("diveimportaladin"));
    }

    /**
     * This method initializes jTable like in DataTrak mode and hides the
     * columns "place" and "country" afterwards
     *
     * @return javax.swing.JTable
     */
    protected JTable getDiveFromDataTrakTable ()
    {
        JTable result = super.getDiveFromDataTrakTable ();

        if (result != null) {
            TableColumnModel tcm = result.getColumnModel ();

            tcm.removeColumn (tcm.getColumn (2));
            tcm.removeColumn (tcm.getColumn (1));
        }
        return result;
    }
}
