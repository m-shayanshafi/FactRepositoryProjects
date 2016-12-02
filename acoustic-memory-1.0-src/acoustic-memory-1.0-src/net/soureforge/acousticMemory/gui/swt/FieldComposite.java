/**
 * File:    FieldComposite.java
 * Created: 03.01.2006
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


import java.awt.Dimension;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;




/**
 * Composite containing the fields of the memory.
 * 
 * @version $Id: FieldComposite.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public class FieldComposite
    extends Composite
{
  /**
   * The horizontal and vertical gap between the buttons.
   */
  private static final Dimension BUTTON_GAP = new Dimension(2, 2);
  
  
  /**
   * The Buttons in the field.
   */
  private ArrayList<Button> buttons = new ArrayList<Button>(); 
  
  
  /**
   * The layout used to order the buttons.
   */
  private GridLayout layout = new GridLayout();




  /**
   * @param parent A widget which will be the parent of the new instance (cannot
   *          be null).
   */
  public FieldComposite(Composite parent)
  {
    super(parent, SWT.NONE);
    
    layout.makeColumnsEqualWidth = true;
    layout.marginTop = BUTTON_GAP.height;
    layout.marginBottom = BUTTON_GAP.height;
    layout.marginLeft = BUTTON_GAP.width;
    layout.marginRight = BUTTON_GAP.width;
    setLayout(layout);
  }



  /**
   * Creates the field.
   * 
   * @param fieldDimension The dimension of the field.
   */
  public void createField(final Dimension fieldDimension)
  {
    for (Button button : buttons)
    {
      button.dispose();
    }
    buttons.clear();
    buttons.ensureCapacity(fieldDimension.width * fieldDimension.height);
    
    layout.numColumns = fieldDimension.width;
  }



  /**
   * Adds a card to the field.
   * 
   * @return The created cardbutton.
   */
  public CardButton addCard()
  {
    final CardButton button = new CardButton(this);
    buttons.add(button.getButton());
    return button;
  }
}
