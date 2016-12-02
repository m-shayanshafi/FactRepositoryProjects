/**
 * File:    CardButton.java
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


package net.soureforge.acousticMemory.gui.swt;


import java.beans.PropertyChangeEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

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
    implements GuiCard, SelectionListener, ControlListener, DisposeListener
{
  /**
   * The button.
   */
  private Button button;


  /**
   * Indiviual font for a button. Needs to be disposed after usage.
   */
  private Font font;




  /**
   * Constructor.
   * 
   * @param parent A composite control which will be the parent of the new
   *          instance (cannot be null).
   */
  public CardButton(Composite parent)
  {
    button = new Button(parent, SWT.PUSH);
    button.addSelectionListener(this);
    button.addControlListener(this);
    button.addDisposeListener(this);
    
    GridData gridData = new GridData(GridData.FILL_BOTH);
    button.setLayoutData(gridData);
  }



  /**
   * @return Returns the button.
   */
  protected Button getButton()
  {
    return button;
  }



  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(final PropertyChangeEvent evt)
  {
    Display display = button.getDisplay();
    if (display.getThread() != Thread.currentThread())
    {
      Runnable action = new Runnable()
      {
        public void run()
        {
          propertyChange(evt);
        }
      };
      display.asyncExec(action);
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
   * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
   */
  public void widgetSelected(SelectionEvent e)
  {
    getPropertyChangeSupport().firePropertyChange(GuiCard.PROPERTY_SELECTED,
                                                  false, true);
  }



  /* (non-Javadoc)
   * @see org.eclipse.swt.events.ControlListener#controlResized(org.eclipse.swt.events.ControlEvent)
   */
  public void controlResized(ControlEvent e)
  {
    FontData[] fontData = button.getFont().getFontData();
    int height = button.getBounds().height;

    for (FontData fontDatum : fontData)
    {
      fontDatum.setHeight((int) (height * 0.4));
    }

    if (font != null)
    {
      font.dispose();
    }
    font = new Font(button.getDisplay(), fontData);

    button.setFont(font);
    button.redraw();
  }



  /* (non-Javadoc)
   * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
   */
  public void widgetDisposed(DisposeEvent e)
  {
    if (font != null)
    {
      font.dispose();
      font = null;
    }
  }



  /* (non-Javadoc)
   * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
   */
  public void widgetDefaultSelected(SelectionEvent e)
  {
    // not used
  }



  /* (non-Javadoc)
   * @see org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt.events.ControlEvent)
   */
  public void controlMoved(ControlEvent e)
  {
    // not used
  }
}
