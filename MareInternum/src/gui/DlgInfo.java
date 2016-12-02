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
import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import tools.Localization;
import entities.Konstanten;

/**
 * This class displays the information dialog
 * @author johannes
 *
 */
public class DlgInfo extends JDialog {

	private JTabbedPane tabs;
	
	public DlgInfo(){
		
		super();
		this.setTitle(Konstanten.NAME + " " + Konstanten.VERSION + " :: " + Localization.getInstance().getString("Information"));
		
		this.setLayout(new FlowLayout());
		tabs=new JTabbedPane();
		JPanel pnlCredits=new JPanel();
		JTextArea txtCredits=new JTextArea(15,40);
		JPanel pnlHsql=new JPanel();
		JTextArea txtHsql=new JTextArea(15,40);
		JScrollPane scrlPaneHsql;
		JScrollPane scrlCredits;
		txtCredits.setText(this.getTextCredits());
		txtCredits.setCaretPosition(0);
		
		scrlPaneHsql=new JScrollPane(txtHsql);
		pnlHsql.setLayout(new GridLayout(1,1));
		pnlHsql.add(scrlPaneHsql);
		scrlCredits=new JScrollPane(txtCredits);
		pnlCredits.setLayout(new GridLayout(1,1));
		pnlCredits.add(scrlCredits);
		
		tabs.addTab(Localization.getInstance().getString("Information"),null,pnlCredits);
		this.add(tabs);
		this.pack();
		this.setModal(true);
		this.setResizable(false);
		this.setVisible(true);

	}
	public String getTextCredits(){
		
		String credits = Konstanten.NAME+" "+Konstanten.VERSION+"\nGNU General Public License V3" +
			"\n\n####\n"+Localization.getInstance().getString("Idea")+", "+Localization.getInstance().getString("GameConcept")+", "+Localization.getInstance().getString("Programming")+", "+Localization.getInstance().getString("Graphics")+":\n\n"+
			"Johannes H\u00F6chst\u00E4dter"+
			"\n\n####\n"+Localization.getInstance().getString("FirstTests")+", "+Localization.getInstance().getString("GameConcept")+":"+
			"\n\nDimitrios Sinanidis"+
			"\nOliver Przytulla"+
			"\n\n####\n"+Localization.getInstance().getString("RawMaps")+":"+
			"\n\nPrimap Software http://www.primap.com";
		return credits;
	}

}
