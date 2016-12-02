/*	 
*    This file is part of Mare Internum.
*
*	 Copyright (C) 2008,2009  Johannes Hoechstaedter
*
*    Mare Internum is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    Mare Internum is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with Mare Internum.  If not, see <http://www.gnu.org/licenses/>.
*
*/

package gui;

import game.Main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import tools.Localization;

/**
 * class represents a dialog for choosing game preferences (currently locale only)
 * @author johannes
 *
 */
public class DlgPreferences extends JDialog implements ActionListener{

	private String CMD_OK = Localization.getInstance().getString("OK");
	private String CMD_CANCEL = Localization.getInstance().getString("Cancel");
	private JList listLocs;
	private Main main;
	
	public DlgPreferences(Main main){
		
		super();
		
		this.setTitle(Localization.getInstance().getString("Preferences"));
		
		this.main = main;
		
		Container cp = this.getContentPane();
		
		JButton btnOk = new JButton(CMD_OK);
		btnOk.setActionCommand(CMD_OK);
		
		JButton btnCancel = new JButton(CMD_CANCEL);
		btnCancel.setActionCommand(CMD_CANCEL);
		
		Locale[] locs = {Locale.GERMAN, Locale.ENGLISH, Locale.FRENCH};
		listLocs = new JList(locs);
		JScrollPane scrlPaneLocs = new JScrollPane(listLocs);
		listLocs.setSelectedValue(Locale.getDefault(), true);
		
		JLabel lblCurLocale = new JLabel(Locale.getDefault().toString());
		
		JPanel buttons = new JPanel();
		buttons.add(btnOk);
		buttons.add(btnCancel);
		
		cp.setLayout(new BorderLayout());
		cp.add(BorderLayout.NORTH,lblCurLocale);
		cp.add(BorderLayout.CENTER,scrlPaneLocs);
		cp.add(BorderLayout.SOUTH, buttons);

		btnOk.addActionListener(this);
		btnCancel.addActionListener(this);
		
		this.pack();
		this.setModal(true);
		this.setVisible(true);
	
	}

	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals(CMD_OK)){
			Localization.setLocale((Locale)listLocs.getSelectedValue());
			main.init();
			this.dispose();
		}
		else {
			this.dispose();
		}
	}
}
