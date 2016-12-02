/**
 * File:    LicenseDialog.java
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


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import net.soureforge.acousticMemory.gui.Common;
import net.soureforge.acousticMemory.gui.Gui;




/**
 * Dialog showing the license.
 * 
 * @version $Id: LicenseDialog.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public class LicenseDialog
    extends AbstractDialog
    implements SelectionListener
{
  /**
   * @param parent A shell which will be the parent of the new instance.
   */
  public LicenseDialog(Shell parent)
  {
    super(parent);
  }



  /* (non-Javadoc)
   * @see akustikMemory.gui.swt.AbstractDialog#createGui(org.eclipse.swt.widgets.Shell)
   */
  @Override
  public void createGui(Shell parent)
  {
    setText(Gui.MESSAGES.getString("aboutLicense"));

    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    parent.setLayout(layout);

    Text textField = new Text(parent, SWT.MULTI | SWT.READ_ONLY | SWT.H_SCROLL
        | SWT.V_SCROLL);
    textField.setText(Common.getInstance().getLicense());
    textField.setLayoutData(new GridData(GridData.FILL_BOTH));


    Button okButton = new Button(parent, SWT.PUSH);
    okButton.setText(Gui.MESSAGES.getString("buttonOk"));
    okButton.addSelectionListener(this);
    okButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    parent.setSize(450, 400);
  }



  /* (non-Javadoc)
   * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
   */
  public void widgetSelected(SelectionEvent e)
  {
    dispose();
  }



  /* (non-Javadoc)
   * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
   */
  public void widgetDefaultSelected(SelectionEvent e)
  {
    // not used
  }
}
