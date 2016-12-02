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

import java.awt.*;

import javax.swing.*;

import src.game.GameWorld;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Frame used to resize the current level in the editor.
 * @author Darren Watts
 * date 1/29/08
 */
public class ResizeFrame extends JFrame {
	private static final long serialVersionUID = src.Constants.serialVersionUID;

	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JRadioButton rbnBottom = new JRadioButton();
	ButtonGroup grpVertical = new ButtonGroup();
	JRadioButton btnTop = new JRadioButton();
	JLabel lblRowDisplay = new JLabel();
	JLabel lblRowCount = new JLabel();
	JLabel lblNewRowDisplay = new JLabel();
	JTextField txtRowCount = new JTextField();
	ButtonGroup grpHorizontal = new ButtonGroup();
	JRadioButton rbnRight = new JRadioButton();
	JRadioButton rbnLeft = new JRadioButton();
	JLabel lblColDisplay = new JLabel();
	JLabel lblColumnCount = new JLabel();
	JLabel lblNewColumnDisplay = new JLabel();
	JTextField txtColCount = new JTextField();
	JButton btnCancel = new JButton();
	JButton btnOK = new JButton();
	
	private EditorControlFrame editControl;

	public ResizeFrame(EditorControlFrame editControl) {
		try {
			jbInit();
			this.editControl = editControl;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		getContentPane().setLayout(gridBagLayout1);
		rbnBottom.setText("Grow/Shrink Bottom");
		btnTop.setText("Grow/Shrink Top");
		lblRowDisplay.setText("CurrentRowCount:");
		lblRowCount.setText("0");
		lblNewRowDisplay.setText("New Row Count:");
		txtRowCount.setText("");
		rbnRight.setText("Grow/Shrink Right");
		rbnLeft.setText("Grow/Shrink Left");
		lblColDisplay.setText("Current Column Count:");
		lblColumnCount.setText("0");
		lblNewColumnDisplay.setText("New Column Count:");
		txtColCount.setText("");
		btnCancel.setText("Cancel");
		btnCancel.addActionListener(new ResizeFrame_btnCancel_actionAdapter(this));
		btnOK.setText("OK");
		btnOK.addActionListener(new ResizeFrame_btnOK_actionAdapter(this));
		grpVertical.add(rbnBottom);
		grpVertical.add(btnTop);
		grpHorizontal.add(rbnLeft);
		grpHorizontal.add(rbnRight);
		this.getContentPane().add(btnOK,
				new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
						, GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(10, 0, 0, 0), 0, 0));
		this.getContentPane().add(btnCancel,
				new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
						, GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(10, 0, 0, 0), 0, 0));
		this.getContentPane().add(txtColCount,
				new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
						, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(5, 5, 0, 5), 0, 0));
		this.getContentPane().add(lblNewColumnDisplay,
				new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
						, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 0, 0, 0), 0, 0));
		this.getContentPane().add(lblColumnCount,
				new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
						, GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(5, 5, 0, 0), 0, 0));
		this.getContentPane().add(lblColDisplay,
				new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
						, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 0, 0, 0), 0, 0));
		this.getContentPane().add(rbnLeft,
				new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
						, GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(10, 0, 0, 0), 0, 0));
		this.getContentPane().add(rbnRight,
				new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
						, GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(10, 0, 0, 0), 0, 0));
		this.getContentPane().add(lblNewRowDisplay,
				new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
						, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 0, 0, 0), 0, 0));
		this.getContentPane().add(txtRowCount,
				new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
						, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(5, 5, 0, 5), 0, 0));
		this.getContentPane().add(lblRowCount,
				new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
						, GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(5, 5, 0, 0), 0, 0));
		this.getContentPane().add(lblRowDisplay,
				new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
						, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 0, 0, 0), 0, 0));
		this.getContentPane().add(btnTop,
				new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
						, GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(rbnBottom,
				new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
						, GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));

		rbnBottom.setSelected(true);
		rbnRight.setSelected(true);
		
		lblRowCount.setText(String.valueOf(GameWorld.getInstance().getLand().length));
		lblColumnCount.setText(String.valueOf(GameWorld.getInstance().getLand()[0].length));
		txtRowCount.setText(lblRowCount.getText());
		txtColCount.setText(lblColumnCount.getText());

		setSize(320, 250);
		setTitle("Resize Level");
		src.Constants.centerFrame(this);
		setVisible(true);
	}

	public void btnCancel_actionPerformed(ActionEvent e) {
		this.dispose();
	}

	public void btnOK_actionPerformed(ActionEvent e) {
		int rows, columns;
		boolean bottom, right;
		if(rbnBottom.isSelected()) bottom = true;
		else bottom = false;
		if(rbnRight.isSelected()) right = true;
		else right = false;

		try{
			rows = Integer.parseInt(txtRowCount.getText());
			columns = Integer.parseInt(txtColCount.getText());
			
			if(rows < 1 || columns < 1) return;
			GameWorld.getInstance().resizeLevel(rows, columns, bottom, right);
		} catch(Exception ex){
			ex.printStackTrace();
			this.dispose();
		}
		editControl.updateForSizeChange();
		this.dispose();
	}
}


class ResizeFrame_btnOK_actionAdapter implements ActionListener {
	private ResizeFrame adaptee;
	ResizeFrame_btnOK_actionAdapter(ResizeFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.btnOK_actionPerformed(e);
	}
}


class ResizeFrame_btnCancel_actionAdapter implements ActionListener {
	private ResizeFrame adaptee;
	ResizeFrame_btnCancel_actionAdapter(ResizeFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.btnCancel_actionPerformed(e);
	}
}
