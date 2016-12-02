/**
 * File:    LicenseDialog.java
 * Created: 01.01.2006
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


package net.soureforge.acousticMemory.gui.swing;


import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.soureforge.acousticMemory.gui.Common;
import net.soureforge.acousticMemory.gui.Gui;




/**
 * Dialog showing the license.
 * 
 * @version $Id: LicenseDialog.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public class LicenseDialog
    extends JDialog
    implements ActionListener
{
  /**
   * Button to exit dialog.
   */
  private JButton okButton;




  /**
   * @param owner The <code>Frame</code> from which the dialog is displayed.
   */
  public LicenseDialog(Frame owner)
  {
    super(owner, Gui.MESSAGES.getString("aboutLicense"), true);
    initGui();
  }



  /**
   * Creations of components.
   */
  void initGui()
  {
    JTextArea textArea = new JTextArea();
    textArea.setEditable(false);
    textArea.setLineWrap(false);
    textArea.setText(Common.getInstance().getLicense());
    textArea.setCaretPosition(0);

    JScrollPane licenseScrollPane = new JScrollPane();
    licenseScrollPane.getViewport().add(textArea);

    okButton = new JButton(Gui.MESSAGES.getString("buttonOk"));
    okButton.addActionListener(this);

    setLayout(new BorderLayout());
    add(licenseScrollPane, BorderLayout.CENTER);
    add(okButton, BorderLayout.SOUTH);

    setSize(450, 400);
    setLocationRelativeTo(getOwner());
  }



  /* (non-Javadoc)
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e)
  {
    dispose();
  }
}
