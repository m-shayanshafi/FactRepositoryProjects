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
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.awt.image.*;
import java.awt.geom.AffineTransform;
import java.awt.font.TextLayout;
import java.net.URL;
import java.net.URLClassLoader;

public class CardReadings extends JPanel {
	private JEditorPane editor;
	public CardReadings() {
		super( true );

		setBorder( BorderFactory.createTitledBorder( jTarot.menus.translate(  "screensReading" ) ) );
		setLayout( new GridLayout(1,1) );

		editor = new JEditorPane("text/html", "");
		editor.setEditable( false );
		JScrollPane scroller = new JScrollPane();
		scroller.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		JViewport port = scroller.getViewport();
		port.add( editor );
		add( scroller );
		setPreferredSize( new Dimension( 60, 60 ) );
	}
    public void scrollToReference(String ref){
        editor.scrollToReference(ref);
    }
	public void getCardInfo( int posindex, int cardIndex, StringBuffer posName, StringBuffer posDesc,
			StringBuffer cardName, StringBuffer cardDesc, StringBuffer cardDescReversed ) {

		String textToWrite;
		//The Card Layout position name
		posName.setLength( 0 );
        if (jTarot.oReading.layoutTranslations.getValue( "PositionTitle" + posindex )!=null)
          posName.append( jTarot.oReading.layoutTranslations.translate( "PositionTitle" + posindex ) );
		//The card layout postion description
		posDesc.setLength( 0 );
        if (jTarot.oReading.layoutTranslations.getValue( "Meaning" + posindex )!=null)
          posDesc.append( jTarot.oReading.layoutTranslations.translate( "Meaning" + posindex ) );
		//The card name
		cardName.setLength( 0 );
		cardName.append( jTarot.oReading.graphObjTranslations.translate( "ObjectTitle" + cardIndex ));
		//The card description
		cardDesc.setLength( 0 );
		cardDesc.append( jTarot.oReading.graphObjTranslations.translate( "ObjectMeaning" + cardIndex ));
		//The card description reversed
		cardDescReversed.setLength( 0 );
        if (jTarot.oReading.graphObjTranslations.getValue( "ReversedMeaning" + posindex )!=null)
		  cardDescReversed.append( jTarot.oReading.graphObjTranslations.translate( "ReversedMeaning" + cardIndex ));
	}
	public void writeTranslationToPane( ) {
      setBorder( BorderFactory.createTitledBorder( jTarot.menus.translate(  "screensReading" ) ) );
		StringBuffer buffer = new StringBuffer();
		String textToWrite = jTarot.oReading.graphObjTranslations.translate("copyright");
		buffer.append( "<h3>" + jTarot.menus.translate("copyrighttitle" ) + "</h3>" );
		buffer.append( textToWrite );
		buffer.append( "<h3>" + jTarot.menus.translate( "TheDeckCopyright" ) + "</h3>" );
        String deckCopyright=jTarot.oReading.graphObjects.getValue("copyright");
        if (deckCopyright!=null)
            buffer.append( deckCopyright );
		buffer.append( "<h3>" + jTarot.oReading.layoutTranslations.translate("Title") + "</h3>" );
		buffer.append( jTarot.oReading.layoutTranslations.translate("Description") );
		buffer.append( "<hr size=1>" );
		String thecard = jTarot.menus.translate( "thecard" );
		String thepostion = jTarot.menus.translate( "theposition" );
		StringBuffer posName = new StringBuffer();
		StringBuffer posDesc = new StringBuffer();
		StringBuffer cardName = new StringBuffer();
		StringBuffer cardDesc = new StringBuffer();
        StringBuffer cardDescReversed = new StringBuffer();
		for ( int i = 0; i < jTarot.oReading.chosenObjects.length; i++ ) {
				getCardInfo( i, jTarot.oReading.chosenObjects[i], posName, posDesc, cardName, cardDesc,cardDescReversed );
				//The Card Layout position name
                if (posName.length()!=0)
                  buffer.append( "<a name=\"position" + i + "\"><h3>" + thepostion + " " + ( i + 1 ) + " : " + posName + "</h3></a>" );
				else
                  buffer.append( "<a name=\"position" + i + "\"><h3>" + ( i + 1 )  + "</h3></a>" );
                //The card layout postion description
                if (posName.length()!=0)
                  buffer.append( posDesc );
				//The card name
				buffer.append( "<h3>" + thecard + " : " + cardName + "</h3>" );
				if (  jTarot.oReading.chosenUpSideDown[i])
					buffer.append( "<h3>" + jTarot.menus.translate("cardisreversed" ) + "</h3>" );
				//The card description
                if(jTarot.io.getBooleanProperty( "showreversedandnormal") ){
                  buffer.append( cardDesc );
                  if(cardDescReversed.length()!=0){
                        buffer.append( "<h4>" + jTarot.menus.translate("ifreversed" ) + "</h4>" ); 
                        buffer.append( cardDescReversed );
                  }
                }else{
                  if (  jTarot.oReading.chosenUpSideDown[i] && cardDescReversed.length()!=0){
                        buffer.append( cardDescReversed );
                  }else{
                        buffer.append( cardDesc );
                  }
                }

				buffer.append( "<hr size=1>" );
        }
		editor.setText( buffer.toString() );
	}

}

