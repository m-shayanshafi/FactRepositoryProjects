/**
 * File:    GuiCard.java
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


package net.soureforge.acousticMemory.gui;


import java.beans.PropertyChangeListener;

import net.soureforge.acousticMemory.controller.PropertyChangeListenerSupport;




/**
 * The visual representation of a card..
 * 
 * @version $Id: GuiCard.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public interface GuiCard
    extends PropertyChangeListener, PropertyChangeListenerSupport
{
  /**
   * The property for a selected card.
   */
  public static final String PROPERTY_SELECTED = GuiCard.class.getName()
      + ".selected";
}
