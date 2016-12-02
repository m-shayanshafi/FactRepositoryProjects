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

import java.lang.reflect.*;
import java.util.*;

import javax.swing.tree.*;

public class IhmDebugInfoNode extends DefaultMutableTreeNode {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3257008769598042681L;

  Object object = null;

  boolean loaded = false;

  public IhmDebugInfoNode(String name, Object object) {
    super(name);
    this.object = object;
  }

  public IhmDebugInfoNode(Object object) {
    super(object.toString());
    this.object = object;
  }

  public void addChildren(int depht) {
    depht++;
    if (depht > 3)
      return;
    if (object == null)
      return;

    if (object instanceof Object[]) {
      Object[] tmp = (Object[]) object;
      for (int q = 0; q < tmp.length; q++) {
        if (tmp[q] != null) // !? entries of a hashtable can be null, e.g. see
        // PlayerAttributes
        {
          IhmDebugInfoNode newNode = new IhmDebugInfoNode(tmp[q]);
          newNode.addChildren(depht); // Blow up once ...
          this.add(newNode);
        }
      }
    }
    // else if (object instanceof Vector) {
    // Vector<> tmp = (Vector) object;
    // for (int q = 0; q < tmp.size(); q++) {
    // // same problem as above ?
    // Object obj = (Object) tmp.get(q);
    // IhmDebugInfoNode newNode = new IhmDebugInfoNode(obj);
    // newNode.addChildren(depht); // Blow up once ...
    // this.add(newNode);
    // }
    // }
    else {

      Field[] publInh = object.getClass().getDeclaredFields();
      Field[] decl = object.getClass().getSuperclass().getDeclaredFields();

      Field[] fields = new Field[publInh.length + decl.length];
      System.arraycopy(publInh, 0, fields, 0, publInh.length);
      System.arraycopy(decl, 0, fields, publInh.length, decl.length);

      for (int i = 0; i < fields.length; i++) {
        try {
          fields[i].setAccessible(true);
          Class subClass = fields[i].getType();
          Object subElement = fields[i].get(object);
          String subName = fields[i].getName();
          // Empty if Object
          String value = "";
          // Null if Null ;-)
          if (subElement == null)
            value = " : NULL";
          // Value if primitive
          if (subClass.isPrimitive() || subElement instanceof String)
            value = " : " + subElement.toString();

          // Class Name
          String className = subClass.getName();
          if (subElement instanceof Vector || subElement instanceof Object[] || subElement instanceof Hashtable)
            className = "...";

          IhmDebugInfoNode newNode = new IhmDebugInfoNode(subName + " (" + className + ")" + value, subElement);
          newNode.addChildren(depht); // Blow up once ...
          this.add(newNode);
        } catch (Exception err) {
          err.printStackTrace();
        }
      }

    }

  }

}