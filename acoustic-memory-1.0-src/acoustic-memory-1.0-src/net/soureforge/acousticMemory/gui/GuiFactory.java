/**
 * File:    GuiFactory.java
 * Created: 02.01.2006
 *
 *
 * Copyright (c) 2006  Markus Bauer <markusbauer@users.sourceforge.net>
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



/**
 * Factory for the GUI.
 * 
 * @version $Id: GuiFactory.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public class GuiFactory
{
  /**
   * The property which describe which GUI to create.
   */
  private static final String PROPERTY_NAME = "acoustic-memory.gui";


  /**
   * The value to create a SWT GUI.
   */
  private static final String VALUE_SWT = "swt";


  /**
   * The value to create a Swing GUI.
   */
  private static final String VALUE_SWING = "swing";




  /**
   * Creates the GUI.
   * 
   * It the property "acoustic-memory.gui" is set to "swing", try to create the
   * Swing GUI. If the property is set to "swt", try to create SWT GUI.
   * Otherwise first try to create the SWT GUI. If this fails, fall back to
   * Swing GUI.
   * 
   * @return An implementation of the Gui.
   * @throws Exception If the GUI can not be instanciated.
   */
  public static Gui createGui()
      throws Exception
  {
    Gui gui = null;

    String value = System.getProperty(PROPERTY_NAME);

    if (VALUE_SWT.equalsIgnoreCase(value))
    {
      gui = createSwtGui();
    }
    else if (VALUE_SWING.equalsIgnoreCase(value))
    {
      gui = createSwingGui();
    }
    else
    {
      try
      {
        gui = createSwtGui();
      }
      catch (Throwable e)
      {
        gui = createSwingGui();
      }
    }

    return gui;
  }



  /**
   * Creates the swing GUI.
   * 
   * @return An implementation of the Gui.
   * @throws Exception If the GUI can not be instanciated.
   */
  public static Gui createSwingGui()
      throws Exception
  {
    return (Gui) Class.forName(
                               GuiFactory.class.getPackage().getName()
                                   + ".swing.SwingGui").newInstance();
  }



  /**
   * Creates the SWT GUI.
   * 
   * @return An implementation of the Gui.
   * @throws Exception If the GUI can not be instanciated.
   */
  public static Gui createSwtGui()
      throws Exception
  {
    return (Gui) Class.forName(
                               GuiFactory.class.getPackage().getName()
                                   + ".swt.SwtGui").newInstance();
  }



  /**
   * Factory cannot be instanciated.
   */
  private GuiFactory()
  {
  }
}
