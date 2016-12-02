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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import entities.KonstantenProtokoll;
import game.VerwaltungClient;
import tools.Parser;
import tools.Localization;

public class Chatfenster extends JPanel implements ActionListener, Observer, KeyListener{
	
	private JButton btnSenden;
	private JTextField tfSenden;
	private JTextArea taChat;
	private JTextArea taMsg;
	private VerwaltungClient meineVerwaltung;
	private JScrollPane scrollPane1;
	private JScrollPane scrollPane2;
	
	private final int BNDS_H_taChat=80;
	private final int BNDS_H_tfSenden=30;
	private final int BNDS_X_tfSenden=0;
	private final int BNDS_H_btnSenden=30;
	private final int BNDS_W_btnSenden=100;
	private final int BNDS_H_scrollPane=80;
	
	public Chatfenster(VerwaltungClient ss_verwaltung){
		
		meineVerwaltung = ss_verwaltung;
		meineVerwaltung.addObserver(this);

		btnSenden=new JButton(Localization.getInstance().getString("Send"));
		tfSenden=new JTextField("");
		tfSenden.addKeyListener(this);
		
		taChat= new JTextArea(3,1);
		taChat.setToolTipText(Localization.getInstance().getString("ChatMessages"));
		taMsg=new JTextArea(3,1);
		taMsg.setToolTipText(Localization.getInstance().getString("News"));
		scrollPane1=new JScrollPane(taChat);
		scrollPane2=new JScrollPane(taMsg);
				
		btnSenden.addActionListener(this);
		taChat.setEditable(false);
		taChat.setBackground(Color.WHITE);
		taMsg.setEditable(false);
		taMsg.setBackground(Color.WHITE);
		
		this.setLayout(null);
		this.add(scrollPane1);
		this.add(scrollPane2);
		this.add(tfSenden);
		this.add(btnSenden);
			
	}
	
	private void ausgabeNachricht(String ss_nachricht,String ss_chat){
		
		taChat.setText(ss_chat);
		taChat.setCaretPosition(taChat.getText().length());
		taMsg.setText(ss_nachricht);
		taMsg.setCaretPosition(taMsg.getText().length());
	}
	public void actionPerformed(ActionEvent e){
			this.sendeNachricht();

	}
	
	/**
	 * triggers sending the entered message from the input field to the server
	 */
	public void sendeNachricht(){
		String txt="";
		Parser prs=new Parser();
		txt=prs.encode(tfSenden.getText());
		if(tfSenden.getText()!=null&& (!tfSenden.getText().equals(""))){
			meineVerwaltung.nachrichtAnServer(KonstantenProtokoll.CHAT+KonstantenProtokoll.SEPARATORHEADER+txt);
			tfSenden.setText("");
		}
	}

	public void update(Observable obs, Object msg) {
		VerwaltungClient verClient;

		if(obs instanceof VerwaltungClient){
			verClient=(VerwaltungClient)obs;
			this.ausgabeNachricht(verClient.getS_msg(),verClient.getS_chat());
		}
		
	}
	
	/**
	 * Handles resize event from MainFrame. So the chat window is always placed
	 * at the bottom of the frame, and fills the whole width.
	 * 
	 * TODO: consider other java layout handling
	 */
	public void resize(int width, int height){
		
		this.setBounds(0,height-this.BNDS_H_tfSenden-this.BNDS_H_taChat,width,height);
		btnSenden.setBounds(width-this.BNDS_W_btnSenden,0+this.BNDS_H_taChat,this.BNDS_W_btnSenden,this.BNDS_H_btnSenden);
		tfSenden.setBounds(this.BNDS_X_tfSenden,0+this.BNDS_H_taChat,width-this.BNDS_W_btnSenden,this.BNDS_H_tfSenden);
		scrollPane1.setBounds(width/2,0,width/2,this.BNDS_H_scrollPane);
		taChat.setBounds(width/2,height-this.BNDS_H_tfSenden-this.BNDS_H_taChat,width/2,this.BNDS_H_taChat);
		scrollPane2.setBounds(0,0,width/2,this.BNDS_H_scrollPane);
		taMsg.setBounds(0,height-this.BNDS_H_tfSenden-this.BNDS_H_taChat,width/2,this.BNDS_H_taChat);
	}
	
	public void keyTyped(KeyEvent arg0) {

		
	}

	public void keyPressed(KeyEvent arg0) {

		
	}

	public void keyReleased(KeyEvent key) {

		if(key.getKeyCode()==KeyEvent.VK_ENTER){
			this.sendeNachricht();
		}
		
	}
}
