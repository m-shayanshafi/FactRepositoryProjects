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
import game.Spielverwaltung;
import game.VerwaltungServer;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tools.Localization;

public class Serversteuerung extends JPanel implements ActionListener,Observer{

	public static final int HEIGHT = 50;
	
	private static final String CMD_ADDNEWCOMPUTERPLAYER = "addcomputer";
	private static final String CMD_REMOVECOMPUTERPLAYER = "removecomputer";
	private static final String CMD_STARTGAME = "start";
	private static final String CMD_CANCEL = "cancel";
	
	
	Spielverwaltung meineVerwaltung;

	private JLabel lblPort;
	private JLabel lblZahlClients;

	public Serversteuerung(Spielverwaltung verwaltung){

		this.setVisible(false);

		meineVerwaltung = verwaltung;

		verwaltung.getMeineVerServer().addObserver(this);

		JButton btnNewComputer = new JButton(Localization.getInstance().getString("Add"));
		btnNewComputer.setActionCommand(CMD_ADDNEWCOMPUTERPLAYER);
		btnNewComputer.addActionListener(this);
		
		JButton btnRemoveComputer = new JButton(Localization.getInstance().getString("Remove"));
		btnRemoveComputer.setActionCommand(CMD_REMOVECOMPUTERPLAYER);
		btnRemoveComputer.addActionListener(this);

		JPanel computerPlayer = new JPanel();
		computerPlayer.add(new JLabel(Localization.getInstance().getString("ComputerPlayer")+":"));
		computerPlayer.add(btnNewComputer);
		computerPlayer.add(btnRemoveComputer);
		
		JPanel pnlHinzukommen = new JPanel();
		pnlHinzukommen.setLayout(new FlowLayout());
		lblPort = new JLabel(Localization.getInstance().getString("Port")+": "+Localization.getInstance().getString("?"));
		pnlHinzukommen.add(lblPort);
		
		lblZahlClients = new JLabel("0");

		JPanel pnlClients = new JPanel();
		pnlClients.setLayout(new FlowLayout());
		pnlClients.add(new JLabel(Localization.getInstance().getString("CountOfPLayers")));
		pnlClients.add(lblZahlClients);
		
		JButton btnAbbruch = new JButton(Localization.getInstance().getString("Cancel"));
		btnAbbruch.addActionListener(this);
		btnAbbruch.setActionCommand(CMD_CANCEL);

		JButton btnStart = new JButton(Localization.getInstance().getString("Start"));
		btnStart.setActionCommand(CMD_STARTGAME);
		btnStart.addActionListener(this);
		
		JPanel pnlSteuerung = new JPanel();
		pnlSteuerung.setLayout(new FlowLayout());
		pnlSteuerung.add(btnAbbruch);
		pnlSteuerung.add(btnStart);
	
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(pnlHinzukommen);
		panel.add(computerPlayer);
		panel.add(pnlClients);
		panel.add(pnlSteuerung);

		this.add(panel);

		this.setVisible(true);

	}

	public void actionPerformed(ActionEvent e){
		
		if(e.getActionCommand().equals(CMD_STARTGAME)){
			
				try {
					meineVerwaltung.fangeSpielAn();
					
				} catch (Exception e1) {
	
					Main.showErrorMessage(e1.getMessage());
				}
	
			}
		else if(e.getActionCommand().equals(CMD_ADDNEWCOMPUTERPLAYER)){
			meineVerwaltung.addNewComputerPlayer();
			
		}
		else if(e.getActionCommand().equals(CMD_REMOVECOMPUTERPLAYER)){
			meineVerwaltung.removeComputerPlayer();
			
		}
		else{
				//Abbruch
				meineVerwaltung.stoppe_Spiel();
			}
	
	}


	public void update(Observable obs, Object arg1) {

		VerwaltungServer meineVerwaltung;

		if(obs instanceof VerwaltungServer){

			meineVerwaltung=(VerwaltungServer)obs;

			lblZahlClients.setText(""+meineVerwaltung.getAnzahlSpieler());

		}

	}

	public void updatePort() {
		lblPort.setText(Localization.getInstance().getString("Port")+": "+meineVerwaltung.getMeineVerServer().getPort()+" / localhost");
	}

}


