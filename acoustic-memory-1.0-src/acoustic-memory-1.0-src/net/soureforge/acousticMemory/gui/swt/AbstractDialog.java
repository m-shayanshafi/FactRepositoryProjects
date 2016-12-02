/**
 * File:    AbstractDialog.java
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
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;




/**
 * Better implementation of a SWT dialog.
 * 
 * @version $Id: AbstractDialog.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public abstract class AbstractDialog
    extends Dialog
{
  /**
   * The shell that is the dialog.
   */
  private Shell shell;




  /**
   * @param parent A shell which will be the parent of the new instance.
   */
  public AbstractDialog(Shell parent)
  {
    super(parent);
  }



  /**
   * @param parent A shell which will be the parent of the new instance.
   * @param style The style of dialog to construct.
   */
  public AbstractDialog(Shell parent, int style)
  {
    super(parent, style);
  }



  /**
   * @return Returns the shell.
   */
  public Shell getShell()
  {
    return shell;
  }



  /* (non-Javadoc)
   * @see org.eclipse.swt.widgets.Dialog#setText(java.lang.String)
   */
  @Override
  public void setText(String string)
  {
    super.setText(string);

    if (shell != null)
    {
      shell.setText(string);
    }
  }



  /**
   * Create the GUI components here.
   * @param parent The parent shell.
   */
  public abstract void createGui(Shell parent);



  /**
   * Opens the dialog.
   */
  public void open()
  {
    Shell parent = getParent();
    shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE
        | SWT.APPLICATION_MODAL);
    shell.setText(getText());

    createGui(shell);

    shell.open();
    Display display = parent.getDisplay();
    while (!shell.isDisposed())
    {
      if (!display.readAndDispatch())
      {
        display.sleep();
      }
    }
  }



  /**
   * Disposes the dialog.
   */
  public void dispose()
  {
    if (shell == null)
    {
      SWT.error(SWT.ERROR_WIDGET_DISPOSED, new NullPointerException(),
                "Shell is not yet created.");
    }
    else
    {
      shell.dispose();
    }
  }
}
