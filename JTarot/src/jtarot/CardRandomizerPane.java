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
import java.util.Random;
import  nu.lazy8.oracle.engine.*;
import java.awt.image.*;
import java.awt.geom.AffineTransform;

public abstract class CardRandomizerPane extends JPanel {
	ButtonCard[] but;
	static Image img = null;
    private int[] randomizedObjects;
	CardRandomizerPane( ) {
		//create a deck of cards
        randomizedObjects=jTarot.oReading.getRandomizedObjectIndexes();
        initializeImage();
		setLayout( new GridLayout( 4, 20 ) );
		but = new ButtonCard[randomizedObjects.length];
		for ( int i = 0; i < randomizedObjects.length; i++ ) {
			//display the card in the pane as JButton.
			but[i] = new ButtonCard( randomizedObjects[i] );
			add( but[i] );
		}
		setMinimumSize( new Dimension( 370, 170 ) );
		//setMaximumSize(new Dimension(370,170));
		setPreferredSize( new Dimension( 370, 170 ) );
	}

	//Must overide this function

	abstract void cardPicked( int cardIndex, boolean isReversed );


	private void initializeImage(  ) {
		img = (Image)(jTarot.io.getImage(jTarot.oReading.fileNames[OracleFileManager.F_graphicalobj], -1 ));
		if ( img == null ) {
			System.out.println( "Error reading image "  );
			return;
		}
		int iw = img.getWidth( this );
		int ih = img.getHeight( this );
		//this can crash if there is a problem with the image, report it
        BufferedImage bi=null;
		try {
			bi = new BufferedImage( iw, ih, BufferedImage.TYPE_INT_RGB );
		} catch ( Exception e ) {
			System.out.println( "Error reading image "  );
			e.printStackTrace();
            return;
		}
		Graphics2D big = bi.createGraphics();
		big.setColor( Color.white );
		big.fillRect( 0, 0, iw, ih );
		big.drawImage( img, 0, 0, this );
        //shrink the image.
        double scale=9 / Math.sqrt((double)(randomizedObjects.length)) * 0.2;
		BufferedImage bimg = new BufferedImage( ( int ) ( iw * scale ), ( int ) ( ih * scale ), BufferedImage.TYPE_INT_RGB );
        AffineTransform atScale = new AffineTransform();
        atScale.scale( scale, scale );
        AffineTransformOp rop = new AffineTransformOp( atScale,
                AffineTransformOp.TYPE_BILINEAR );
        rop.filter( bi, bimg );
        img=bimg;
	}
	private class ButtonCard extends JButton {
		int index = 0;


		ButtonCard( int i ) {
			if ( img != null )
				setIcon( new ImageIcon( img ) );
			Dimension ddm = new Dimension( 25, 40 );
			setPreferredSize( ddm );
			setMinimumSize( ddm );
			setMaximumSize( ddm );
			index = i;
			ActionListener action1Listener =
				new ActionListener() {
					public void actionPerformed( ActionEvent e ) {
						setVisible( false );
						//wait until the card is hit to choose the "reversed or not" because this way
						//whether or not the card is reversed depends on "when" the person hits the card
						//and thus reversed or not can still depend upon the input of the person and not some
						//mathmatical algorithm.  Therefore, the subconscience still has control over the outcome.
						Random random = new Random();
						cardPicked( index, random.nextBoolean() );
					}
				};
			addActionListener( action1Listener );
		}
	}


	public static void main( String[] args ) {
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout( new GridLayout( 1, 1 ) );
		frame.getContentPane().add(
			new CardRandomizerPane(  ) {
				void cardPicked( int cardIndex, boolean isReversed ) {
					System.out.println( "selected card " + cardIndex + " " + isReversed );
				}
			} );
		frame.addWindowListener(
			new WindowAdapter() {
				public void windowClosing( WindowEvent e ) {
					System.exit( 0 );
				}
			} );
		frame.pack();
		frame.setSize( 400, 200 );
		frame.setVisible( true );
	}
}

