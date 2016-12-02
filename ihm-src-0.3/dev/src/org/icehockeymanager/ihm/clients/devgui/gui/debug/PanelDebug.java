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
  
package org.icehockeymanager.ihm.clients.devgui.gui.debug;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.user.*;
import org.icehockeymanager.ihm.lib.*;

/**
 * The PanelDebug contains: A simple panel showing debug informations for the
 * developer.
 * 
 * @author Bernhard von Gunten
 * @created January 13, 2002
 */
public class PanelDebug extends JIhmPanel {
  static final long serialVersionUID = 1140461629585905736L;

  BorderLayout borderLayout1 = new BorderLayout();

  JScrollPane scrollPane = new JScrollPane();

  JTree jDebugInfoTree = new JTree();

  Object objectRoot = null;

  String objectName = "";

  ImageIcon debugIcon = new ImageIcon();

  /**
   * Constructs the frame, sets the user, calls ihmInit
   * 
   * @param user
   *          User to show this frame for
   * @param objectRoot 
   * @param objectName 
   */
  public PanelDebug(User user, Object objectRoot, String objectName) {
    super(user);
    this.objectRoot = objectRoot;
    this.objectName = objectName;
    try {
      jbInit();
      ihmInit();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * JBuilder stuff
   * 
   * @exception Exception
   *              Exception
   */
  private void jbInit() throws Exception {
    this.setSize(new Dimension(580, 400));
    this.setLayout(borderLayout1);
    setLocation(30, 30);
    jDebugInfoTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        jDebugInfoTree_valueChanged(e);
      }
    });
    jDebugInfoTree.addTreeWillExpandListener(new javax.swing.event.TreeWillExpandListener() {
      public void treeWillExpand(TreeExpansionEvent e) throws ExpandVetoException {
        jDebugInfoTree_treeWillExpand(e);
      }

      public void treeWillCollapse(TreeExpansionEvent e) {
      }
    });
    this.add(scrollPane, BorderLayout.CENTER);
    scrollPane.getViewport().add(jDebugInfoTree, null);
  }

  /** Create the panelLeagueStandings and add it to the frame */
  private void ihmInit() {
    IhmDebugInfoParser dip = null;
    if (objectRoot == null) {
      dip = new IhmDebugInfoParser(GameController.getInstance().getScenario(), "Scenario");
    } else {
      dip = new IhmDebugInfoParser(objectRoot, objectName);
    }
    this.jDebugInfoTree.setModel(dip.getDefaultTreeModel());
  }

  void jDebugInfoTree_valueChanged(TreeSelectionEvent e) {
    IhmDebugInfoNode currentNode = (IhmDebugInfoNode) jDebugInfoTree.getLastSelectedPathComponent();
    if (currentNode == null) {
      return;
    }
    currentNode.addChildren(0);
  }

  void jDebugInfoTree_treeWillExpand(TreeExpansionEvent e) throws ExpandVetoException {
    jDebugInfoTree_valueChanged(null);
  }

}
