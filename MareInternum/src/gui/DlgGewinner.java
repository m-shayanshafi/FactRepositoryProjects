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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import entities.Konstanten;
import game.VerwaltungClient;
import tools.Localization;

public class DlgGewinner extends JFrame implements ActionListener{
	
	private JPanel pnl3;
	private JPanel pnl2;
	private JPanel pnl1;
	private Object[] spalten={Localization.getInstance().getString("Player"),Localization.getInstance().getString("Points")};
	private Object[][] zeilen={{"1","1"},{"2","2"}};
	private JTable tabelle;
	private JScrollPane pane ;
	
	public DlgGewinner(VerwaltungClient verClient){
		super(Konstanten.NAME+" "+Konstanten.VERSION+" :: "+Localization.getInstance().getString("Summary"));
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(Bilder.ICON)));
		this.init(verClient);
	}
	
	public void  init(VerwaltungClient verClient){
		this.setLayout(new BorderLayout());
		pnl1=new JPanel();
		pnl1.setLayout(new FlowLayout());
		pnl1.add(new JLabel(Localization.getInstance().getString("GameOver")));
		pnl1.setBounds(10,10,200,10);
		pnl2=new JPanel();
		int a_punkte[];
		int punkte=0;
		String name;
		String[] a_namen;
		a_punkte=verClient.getA_spielEndpunkte();
		a_namen=verClient.getA_Spielernamen();
		zeilen=new Object[a_punkte.length][2];
		for(int i=0;i<a_punkte.length;i++){
			for(int j=0;j<a_punkte.length-1-i;j++){
				if(a_punkte[j]<a_punkte[j+1]){
					punkte=a_punkte[j];
					a_punkte[j]=a_punkte[j+1];
					a_punkte[j+1]=punkte;
					name=a_namen[j];
					a_namen[j]=a_namen[j+1];
					a_namen[j+1]=name;
				}
			}
			
		}
		for(int i=0;i<a_punkte.length;i++){
			zeilen[i][0]=a_namen[i];
			zeilen[i][1]=new String(""+a_punkte[i]);
		}
		
		tabelle = new JTable(zeilen, spalten);
		tabelle.setEnabled(false);
		pane = new JScrollPane(tabelle);
		pnl2.add(pane);
		pnl3=new JPanel();
		JButton btn=new JButton(Localization.getInstance().getString("OK"));
		btn.addActionListener(this);
		pnl3.add(btn);
		this.add(BorderLayout.CENTER,pnl2);
		this.add(BorderLayout.NORTH,pnl1);
		this.add(BorderLayout.SOUTH,pnl3);
		
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
		/*sound =new PlaySound();
		try {
			sound.playSampleFile("musik/ichchef.wav",1,1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public void actionPerformed(ActionEvent arg0) {
		this.dispose();
		
	}
	
}
