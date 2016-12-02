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

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tools.Localization;

import entities.Konstanten;
import game.Main;
import game.Spielverwaltung;

public class TeilnehmenPanel extends JPanel implements ActionListener{

	public static final int HEIGHT = 50;
	
	private Spielverwaltung meineVerwaltung;
	private JTextField txtIP;
	private JTextField txtPort;
	private JButton btnTeilnehmen;
	private JButton btnAbbruch;
	private JPanel meinPanel;
	
	public TeilnehmenPanel(Spielverwaltung ss_verwaltung){
		meineVerwaltung = ss_verwaltung;
		meinPanel = new JPanel();
		meinPanel.setLayout(new FlowLayout());
		JLabel lblIP=new JLabel(Localization.getInstance().getString("IPAddress")+":");
		txtIP = new JTextField(10);
		JLabel lblPort= new JLabel(Localization.getInstance().getString("Port")+":");
		txtPort=new JTextField(6);
		txtPort.setText(""+Konstanten.STANDARDPORT);
		btnTeilnehmen = new JButton(Localization.getInstance().getString("Participate"));
		btnTeilnehmen.setActionCommand("Teilnehmen");
		btnTeilnehmen.addActionListener(this);
		btnAbbruch = new JButton(Localization.getInstance().getString("Cancel"));
		btnAbbruch.addActionListener(this);
		btnAbbruch.setActionCommand("Abbruch");
		meinPanel.add(lblIP);
		meinPanel.add(txtIP);
		meinPanel.add(lblPort);
		meinPanel.add(txtPort);
		meinPanel.add(btnTeilnehmen);
		meinPanel.add(btnAbbruch);
		this.add(meinPanel);
		
		this.setVisible(true);	
	}	
	
	/**
	 * menu item was clicked
	 */
	public void actionPerformed(ActionEvent e){
		String cmd = e.getActionCommand();
		if (cmd.equals("Teilnehmen")){
			try {
				if(meineVerwaltung.nehmeAnSpielTeil(txtIP.getText(),Integer.parseInt(txtPort.getText()))){
					Main.showPlainMessage(Localization.getInstance().getString("SuccessOnClientConnect"));
				}
				else{
					Main.showErrorMessage(Localization.getInstance().getString("ErrorOnClientConnect"));
				}
			} catch (Exception e1) {
				Main.showErrorMessage(Localization.getInstance().getString("ErrorOnClientConnect"));
			}
		}
		if (cmd.equals("Abbruch")){
			meineVerwaltung.abbruchTeilehmen();
		}
	}
}
