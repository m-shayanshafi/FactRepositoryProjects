/**
 *  Copyright (C) 2003-2007  Joe Hopkinson, Jay Ashworth
 *  
 *  JavaTrek is based on Chuck L. Peterson's MTrek.
 *
 *  This file is part of JavaTrek.
 *
 *  JavaTrek is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  
 *  JavaTrek is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with JavaTrek; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.gamehost.jtrek.monitor;

import javax.swing.*;
import com.borland.jbcl.layout.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Jay
 * Date: Mar 9, 2004
 * Time: 3:18:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class OptionsDialog extends JDialog {
	private static final long serialVersionUID = -8170448039012790669L;
	XYLayout xYLayout1 = new XYLayout();
    JLabel addressLabel = new JLabel();
    protected JTextField addressField = new JTextField();
    JLabel portLabel = new JLabel();
    protected JTextField portField = new JTextField();
    JLabel playerLabel = new JLabel();
    protected JTextField playerField = new JTextField();
    JLabel passwordLabel = new JLabel();
    protected JPasswordField passwordField = new JPasswordField();
    JButton okButton = new JButton();

    public OptionsDialog(JFrame parent) {
        super(parent);
        initUI();
    }

    private void initUI() {
        addressLabel.setText("Address");
        this.getContentPane().setLayout(xYLayout1);
        addressField.setText("mtrek.game-host.org");
        portLabel.setText("Port");
        portField.setText("1710");
        xYLayout1.setWidth(270);
        xYLayout1.setHeight(101);
        playerLabel.setText("Player Account");
        passwordLabel.setText("Password");
        okButton.setText("OK");
        playerField.setText("");
        passwordField.setText("");
        this.getContentPane().add(addressField,  new XYConstraints(54, 12, 132, -1));
        this.getContentPane().add(addressLabel, new XYConstraints(7, 14, -1, -1));
        this.getContentPane().add(portLabel, new XYConstraints(192, 13, -1, -1));
        this.getContentPane().add(portField, new XYConstraints(218, 13, 37, -1));
        this.getContentPane().add(playerField, new XYConstraints(100, 41, 86, -1));
        this.getContentPane().add(playerLabel, new XYConstraints(9, 45, -1, -1));
        this.getContentPane().add(passwordLabel, new XYConstraints(32, 72, -1, -1));
        this.getContentPane().add(passwordField, new XYConstraints(100, 69, 86, -1));
        this.getContentPane().add(okButton, new XYConstraints(208, 69, -1, -1));

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                saveOptions();
            }
        });
    }

    private void saveOptions() {
        // could persist the field settings to a config file here
        this.hide();
    }
}
