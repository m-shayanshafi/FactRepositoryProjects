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
  
package org.icehockeymanager.ihm.lib;

import javax.swing.tree.*;

/**
 * Description of the Class
 * 
 * @author Bernhard von Gunten
 * @created January 13, 2002
 */
public class IhmDebugInfoParser {

  private Object rootDebugInfo = null;

  private String name = null;

  /**
   * Constructor for the IhmDebugInfoParser object
   * 
   * @param rootDebugInfo
   *          Description of the Parameter
   * @param name
   *          Description of the Parameter
   */
  public IhmDebugInfoParser(Object rootDebugInfo, String name) {
    this.rootDebugInfo = rootDebugInfo;
    this.name = name;
  }

  /**
   * Gets the defaultTreeModel attribute of the IhmDebugInfoParser object
   * 
   * @return The defaultTreeModel value
   */
  public DefaultTreeModel getDefaultTreeModel() {
    IhmDebugInfoNode root = new IhmDebugInfoNode(name, rootDebugInfo);
    root.addChildren(0);
    return new DefaultTreeModel(root);
  }

}
