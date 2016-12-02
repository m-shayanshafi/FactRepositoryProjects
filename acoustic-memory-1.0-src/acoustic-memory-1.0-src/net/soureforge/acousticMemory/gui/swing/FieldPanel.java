/**
 * File:    FieldPanel.java
 * Created: 24.10.2005
 *
 *
 * Copyright (C) 2005  Markus Bauer <markusbauer@users.sourceforge.net>
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, 
 * MA  02111-1307, USA.
 */


package net.soureforge.acousticMemory.gui.swing;


import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;




/**
 * Panel containing the fields of the memory.
 * 
 * @version $Id: FieldPanel.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public class FieldPanel
{
  /**
   * The horizontal and vertical gap between the buttons.
   */
  private static final Dimension BUTTON_GAP = new Dimension(5, 5);


  /**
   * The panel containing the buttons.
   */
  JPanel panel = new JPanel();




  /**
   * Constructor.
   */
  public FieldPanel()
  {
  }



  /**
   * @return Returns the panel.
   */
  public JPanel getPanel()
  {
    return panel;
  }



  /**
   * Creates the field.
   * 
   * @param fieldDimension The dimension of the field.
   */
  public void createField(final Dimension fieldDimension)
  {
    if (!SwingUtilities.isEventDispatchThread())
    {
      Runnable action = new Runnable()
      {
        public void run()
        {
          createField(fieldDimension);
        }
      };
      SwingUtilities.invokeLater(action);
      return;
    }

    panel.removeAll();
    panel.setLayout(new GridLayout(fieldDimension.width, fieldDimension.height,
                                   BUTTON_GAP.width, BUTTON_GAP.height));
  }



  /**
   * Adds a card to the field.
   * 
   * @return The created cardbutton.
   */
  public CardButton addCard()
  {
    final CardButton button = new CardButton();

    Runnable action = new Runnable()
    {
      public void run()
      {
        panel.add(button.getButton());
      }
    };
    SwingUtilities.invokeLater(action);

    return button;
  }
}
