/*
 *  :tabSize=4:indentSize=2:noTabs=true:
 *  :folding=explicit:collapseFolds=1:
 *
 *  Copyright (C) 2006 Free Tarot Foundation.  This program is free
 *  software; you can redistribute it and/or modify it under the terms of the
 *  GNU General Public License as published by the Free Software Foundation;
 *  either version 2 of the License, or (at your option) any later version. This
 *  program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details. You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software Foundation,
 *  Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA For more
 *  information, surf to www.lazy8.nu/tarot or email tarot@lazy8.nu
 */
 
package jtarot;

 
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;

public class PropertyTranslationPanel extends JPanel {
	private TransCell[] editCells;
	public JTextArea[] texts;


	public PropertyTranslationPanel( String[] origTexts, String[] transTexts ) {
		super( true );

		setBorder( BorderFactory.createTitledBorder( "Property translation" ) );
		setLayout( new BorderLayout() );

		//create the translation panel
		JPanel dataEntryPane = new JPanel();
		JScrollPane scroller1 = new JScrollPane( dataEntryPane );
		scroller1.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );

		add( scroller1 );

		editCells = new TransCell[origTexts.length];
		texts = new JTextArea[origTexts.length];
		dataEntryPane.setLayout( new GridLayout( origTexts.length, 1 ) );
		for ( int i = 0; i < origTexts.length; i++ ) {
			texts[i] = new JTextArea( transTexts[i] );
			texts[i].setWrapStyleWord( true );
			texts[i].setLineWrap( true );
			editCells[i] = new TransCell( origTexts[i], texts[i] );
			dataEntryPane.add( editCells[i] );
		}
	}


	class TransCell extends JPanel {
		private JTextArea transText;


		TransCell( String origText, JTextArea transText ) {
			this.transText = transText;
			JPanel originalPanel = new JPanel();
			JPanel title1Panel = new JPanel();
			JPanel translatedPanel = new JPanel();
			originalPanel.setBorder( BorderFactory.createTitledBorder( "English Texts" ) );
			translatedPanel.setBorder( BorderFactory.createTitledBorder( "Translations" ) );
			setLayout( new GridLayout( 1, 2 ) );

			originalPanel.setLayout( new GridLayout( 1, 1 ) );
			originalPanel.add( new JLabel( origText ) );
			translatedPanel.setLayout( new GridLayout( 1, 1 ) );
			translatedPanel.add( transText );

			originalPanel.setMinimumSize( new Dimension( 300, 50 ) );
			translatedPanel.setMinimumSize( new Dimension( 300, 50 ) );

			originalPanel.setMaximumSize( new Dimension( 300, 50 ) );
			translatedPanel.setMaximumSize( new Dimension( 300, 50 ) );

			originalPanel.setPreferredSize( new Dimension( 300, 50 ) );
			translatedPanel.setPreferredSize( new Dimension( 300, 50 ) );

			add( originalPanel );
			add( translatedPanel );
		}
	}
}

