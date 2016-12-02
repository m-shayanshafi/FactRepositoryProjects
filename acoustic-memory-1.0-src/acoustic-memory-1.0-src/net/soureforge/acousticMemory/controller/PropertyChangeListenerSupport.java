/**
 * File:    PropertyChangeListenerSupport.java
 * Created: 31.12.2005
 *
 *
 * Copyright (c) 2005  Markus Bauer <markusbauer@users.sourceforge.net>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */


package net.soureforge.acousticMemory.controller;


import java.beans.PropertyChangeListener;




/**
 * Support PropertyChangeListener.
 * 
 * @version $Id: PropertyChangeListenerSupport.java 18 2005-12-31 18:22:47Z
 *          markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public interface PropertyChangeListenerSupport
{
  /**
   * @param listener The PropertyChangeListener to be added.
   * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
   */
  public void addPropertyChangeListener(PropertyChangeListener listener);



  /**
   * @param propertyName The name of the property to listen on.
   * @param listener The PropertyChangeListener to be added.
   * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String,
   *      java.beans.PropertyChangeListener)
   */
  public void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener);



  /**
   * @param listener The PropertyChangeListener to be removed.
   * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(PropertyChangeListener listener);



  /**
   * @param propertyName The name of the property to listen on.
   * @param listener The PropertyChangeListener to be removed.
   * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String,
   *      java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener);
}
