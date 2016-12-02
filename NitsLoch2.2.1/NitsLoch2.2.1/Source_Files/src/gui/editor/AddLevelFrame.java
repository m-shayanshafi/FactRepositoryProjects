/*
	This file is part of NitsLoch.

	Copyright (C) 2007 Darren Watts

    NitsLoch is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    NitsLoch is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with NitsLoch.  If not, see <http://www.gnu.org/licenses/>.
 */

package src.gui.editor;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.*;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import src.exceptions.CityNameExistsException;
import src.game.GameWorld;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This frame is used when adding a map with the editor.  It asks
 * the user for a map name and the size of the level.  It will
 * then create a map with the specified stats.
 * @author Darren Watts
 * date 1/27/08
 */
public class AddLevelFrame extends JFrame {
	private static final long serialVersionUID = src.Constants.serialVersionUID;

    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel lblLevelName = new JLabel();
    JTextField txtLevelName = new JTextField();
    JLabel lblSize = new JLabel();
    JLabel lblRows = new JLabel();
    JLabel lblColumns = new JLabel();
    JButton btnOK = new JButton();
    JTextField txtRows = new JTextField();
    JTextField txtColumns = new JTextField();
    
    private EditorControlFrame editControl;

    public AddLevelFrame(EditorControlFrame editControl) {
        try {
        	this.editControl = editControl;
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        getContentPane().setLayout(gridBagLayout1);
        lblLevelName.setText("Enter a level name:");
        txtLevelName.setText("");
        lblSize.setText("Enter size of world:");
        lblColumns.setText("Columns:");
        lblRows.setText("Rows:");
        btnOK.setText("OK");
        btnOK.addActionListener(new AddLevelFrame_btnOK_actionAdapter(this));
        txtRows.setText("");
        txtColumns.setText("");

        this.getContentPane().add(txtLevelName,
                                  new GridBagConstraints(0, 1, 3, 1, 1.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        this.getContentPane().add(txtRows,
                                  new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        this.getContentPane().add(lblSize,
                                  new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 0, 0, 0), 0, 0));
        this.getContentPane().add(lblLevelName,
                                  new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(lblRows,
                                  new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 0, 0, 0), 0, 0));
        this.getContentPane().add(lblColumns,
                                  new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 0, 0, 0), 0, 0));
        this.getContentPane().add(txtColumns,
                                  new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        this.getContentPane().add(btnOK,
                                  new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 0, 0, 0), 0, 0));
        
        setSize(200, 220);
		setTitle("New Level");
		src.Constants.centerFrame(this);
		setVisible(true);
    }

    public void btnOK_actionPerformed(ActionEvent e) {
    	try {
    		String levelName = "";
    		int rows = 0, cols = 0;
    		levelName = txtLevelName.getText();
    		rows = Integer.parseInt(txtRows.getText());
    		cols = Integer.parseInt(txtColumns.getText());
    		
    		if(levelName.equals("") || rows < 1 || cols < 1) return;

    		GameWorld.getInstance().newCity(levelName, rows, cols);
    		editControl.finishAddLevel();
    	} catch(CityNameExistsException nameExists) {
    		JOptionPane.showMessageDialog(
					this,
					"You must enter a unique city name.",
					"ROOF NOTIFICATION!", JOptionPane.ERROR_MESSAGE);
    		return;
    	} catch(Exception ex){ /*ex.printStackTrace();*/ }
    	this.dispose();
    }

}


class AddLevelFrame_btnOK_actionAdapter implements ActionListener {
    private AddLevelFrame adaptee;
    AddLevelFrame_btnOK_actionAdapter(AddLevelFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnOK_actionPerformed(e);
    }
}
