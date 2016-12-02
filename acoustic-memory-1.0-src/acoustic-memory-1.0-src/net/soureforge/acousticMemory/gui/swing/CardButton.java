/**
 * File:    CardButton.java
 * Created: 27.12.2005
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


package net.soureforge.acousticMemory.gui.swing;


import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import net.soureforge.acousticMemory.controller.AbstractPropertyChangeListenerSupport;
import net.soureforge.acousticMemory.gui.GuiCard;
import net.soureforge.acousticMemory.model.Card;





/**
 * A graphical representation of a card as a button.
 * 
 * @version $Id: CardButton.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public class CardButton
    extends AbstractPropertyChangeListenerSupport
    implements GuiCard, ActionListener, ComponentListener
{
  /**
   * The button.
   */
  JButton button = new JButton();




  /**
   * Constructor.
   */
  public CardButton()
  {
    button.addActionListener(this);
    button.addComponentListener(this);
  }




  /**
   * @return Returns the button.
   */
  public JButton getButton()
  {
    return button;
  }




  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(final PropertyChangeEvent evt)
  {
    if (!SwingUtilities.isEventDispatchThread())
    {
      Runnable action = new Runnable()
      {
        public void run()
        {
          propertyChange(evt);
        }
      };
      SwingUtilities.invokeLater(action);
      return;
    }

    if (Card.PROPERTY_TEXT.equals(evt.getPropertyName()))
    {
      button.setText((String) evt.getNewValue());
    }
    else if (Card.PROPERTY_SELECTED.equals(evt.getPropertyName()))
    {
      if (Boolean.TRUE.equals(evt.getNewValue()))
      {
        button.setEnabled(false);
      }
      else
      {
        button.setEnabled(true);
      }
    }
    else if (Card.PROPERTY_GUESSED.equals(evt.getPropertyName()))
    {
      if (Boolean.TRUE.equals(evt.getNewValue()))
      {
        button.setVisible(false);
      }
    }
  }




  /* (non-Javadoc)
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e)
  {
    getPropertyChangeSupport().firePropertyChange(GuiCard.PROPERTY_SELECTED,
                                                  false, true);
  }




  /* (non-Javadoc)
   * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
   */
  public void componentResized(ComponentEvent e)
  {
    Font oldFont = button.getFont();
    float height = button.getHeight() * 0.4f;
    Font newFont = oldFont.deriveFont(height);
    button.setFont(newFont);
  }




  /* (non-Javadoc)
   * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
   */
  public void componentMoved(ComponentEvent e)
  {
  }




  /* (non-Javadoc)
   * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
   */
  public void componentShown(ComponentEvent e)
  {
  }




  /* (non-Javadoc)
   * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
   */
  public void componentHidden(ComponentEvent e)
  {
  }
}
