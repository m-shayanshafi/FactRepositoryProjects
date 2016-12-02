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

 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;

public class FreeTextFrame extends JPanel {
	private JTextArea editor;


	FreeTextFrame( String title, boolean isEditable ) {
		super( true );

		setBorder( BorderFactory.createTitledBorder( title ) );
		setLayout( new BorderLayout() );

		// create the embedded JTextComponent
		editor = new JTextArea();
		editor.setWrapStyleWord( true );
		editor.setLineWrap( true );
		editor.setEditable( isEditable );
		editor.setDragEnabled( true );
		//editor.setFont(new Font("monospaced", Font.PLAIN, 12));
		JScrollPane scroller = new JScrollPane();
		scroller.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		JViewport port = scroller.getViewport();
		port.add( editor );

		//JPanel panel = new JPanel();
		//panel.setLayout(new BorderLayout());
		//panel.add("Center", scroller);
		setLayout( new BorderLayout() );
		add( "Center", scroller );
	}


	public void setTitle( String s ) {
		setBorder( BorderFactory.createTitledBorder( s ) );
	}


	public void setText( String s ) {
		editor.setText( s );
	}


	public String getText() {
		return editor.getText();
	}


	public static void main( String[] args ) {
		try {
			JFrame frame = new JFrame();
			frame.setBackground( Color.lightGray );
			frame.getContentPane().setLayout( new BorderLayout() );
			FreeTextFrame notepad = new FreeTextFrame( "Test from main", true );
			notepad.setText( "Here is the first row." );
			frame.getContentPane().add( "Center", notepad );
			frame.addWindowListener(
				new WindowAdapter() {
					public void windowClosing( WindowEvent e ) {
						System.exit( 0 );
					}
				} );
			frame.pack();
			frame.setSize( 500, 600 );
			frame.setVisible( true );
		} catch ( Throwable t ) {
			System.out.println( "uncaught exception: " + t );
			t.printStackTrace();
		}
	}
}

