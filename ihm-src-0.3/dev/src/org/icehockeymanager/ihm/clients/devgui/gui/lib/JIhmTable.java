/************************************************************* 
 * 
 * Ice Hockey Manager 
 * ================== 
 * 
 * Copyright (C) by the IHM Team (see doc/credits.txt) 
 * 
 * This program is released under the GPL (see doc/gpl.txt) 
 * 
 * Further informations: http://www.icehockeymanager.org  
 * 
 *************************************************************/ 
  
package org.icehockeymanager.ihm.clients.devgui.gui.lib;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import org.icehockeymanager.ihm.lib.*;

/**
 * JIhmTable is a simple JTable class. Implements JIhmComponens.
 * 
 * @author Bernhard von Gunten
 * @created January, 2005
 */
public class JIhmTable extends JTable implements JIhmComponent {

  static final long serialVersionUID = -1490989515233345388L;

  private MouseAdapter listMouseListener = null;

  /** Constructor for the JIhmTable object */
  public JIhmTable() {
  }

  /** Activates sorting on table, adds mouslistener on collumn headers */
  public void activateSorting() {
    listMouseListener = new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        TableColumnModel columnModel = getColumnModel();
        int viewColumn = columnModel.getColumnIndexAtX(e.getX());
        int column = convertColumnIndexToModel(viewColumn);
        if (e.getClickCount() == 1 && column != -1) {
          int shiftPressed = e.getModifiers() & InputEvent.SHIFT_MASK;
          boolean std = (shiftPressed == 0);
          if (getModel() instanceof IhmTableModelSorter) {
            IhmTableModelSorter tmp = (IhmTableModelSorter) getModel();
            tmp.ihmSort(column, std);
          }
        }
      }
    };

    JTableHeader th = getTableHeader();
    th.addMouseListener(listMouseListener);
  }

  public void updateTranslation() {
  }

}
