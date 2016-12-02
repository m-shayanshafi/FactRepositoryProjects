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
import javax.swing.*;
import java.awt.image.*;
import java.awt.geom.AffineTransform;
import java.awt.font.TextLayout;
import java.net.URL;
import java.net.URLClassLoader;
import  nu.lazy8.oracle.engine.*;

public class SelectedCard {
	private int cardIndex = -1;
	private boolean isReversed = false;
	private BufferedImage bi;
	private int iw;
	private int ih;
    private Rectangle hitRect=new Rectangle();


	public int getCardIndex() {
		return cardIndex;
	}


	private String fileName() {
		return "r" + ( cardIndex + 1 ) + "." + jTarot.oReading.graphObjects.getValue("imagetype");
	}


	public boolean getIsReversed() {
		return isReversed;
	}


	public Dimension getSize() {
		return new Dimension( ( int ) ( iw * jTarot.scale ), ( int ) ( ih * jTarot.scale ) );
	}


	public void setCardImage( boolean isReversed, int cardIndex, Component cmp ) {
		this.isReversed = isReversed;
		this.cardIndex = cardIndex;
		initializeImage( cmp );
	}


	public void reloadImage( Component cmp ) {
		initializeImage( cmp );
	}


	private void initializeImage( Component cmp ) {
		cmp.setBackground( Color.white );
		Image img = (Image)(jTarot.io.getImage(jTarot.oReading.fileNames[OracleFileManager.F_graphicalobj], cardIndex ));
		if ( img == null ) {
			System.out.println( "Error reading image=" + fileName() );
			return;
		}
		iw = img.getWidth( cmp );
		ih = img.getHeight( cmp );
		//this can crash if there is a problem with the image, report it
		try {
			bi = new BufferedImage( iw, ih, BufferedImage.TYPE_INT_RGB );
		} catch ( Exception e ) {
			System.out.println( "Error reading image " + fileName() );
			e.printStackTrace();
		}
		Graphics2D big = bi.createGraphics();
		big.setColor( Color.white );
		big.fillRect( 0, 0, iw, ih );
		big.drawImage( img, 0, 0, cmp );
	}

    public boolean containsPoint(int x,int y){
        return hitRect.contains(x,y);
    }
    public BufferedImage getOutputBuffImage(boolean isSideways,double scale){
		BufferedImage bimg = new BufferedImage( ( int ) ( iw * scale ), ( int ) ( ih * scale ), BufferedImage.TYPE_INT_RGB );
		if ( true
		/*
		 *  scale != 1.0
		 */
				 ) {
			AffineTransform atScale = new AffineTransform();
			atScale.scale( scale, scale );
			AffineTransformOp rop = new AffineTransformOp( atScale,
					AffineTransformOp.TYPE_BILINEAR );
			rop.filter( bi, bimg );
		}
		if ( isSideways ) {
			AffineTransform atRotate90 = new AffineTransform();
			atRotate90.rotate( Math.PI / 2, ih * scale / 2, ih * scale / 2 );
			AffineTransformOp rop = new AffineTransformOp( atRotate90,
					AffineTransformOp.TYPE_BILINEAR );
			//note that the width and height are switched in the new buffered image
			BufferedImage bimg2 = new BufferedImage( ( int ) ( ih * scale ), ( int ) ( iw * scale ), BufferedImage.TYPE_INT_RGB );
			rop.filter( bimg, bimg2 );
			bimg = bimg2;
		}
		else if ( isReversed ) {
			AffineTransform atRotate180 = new AffineTransform();
			atRotate180.rotate( Math.PI, iw * scale / 2, ih * scale / 2 );
			AffineTransformOp rop = new AffineTransformOp( atRotate180,
					AffineTransformOp.TYPE_BILINEAR );
			BufferedImage bimg2 = new BufferedImage( ( int ) ( iw * scale ), ( int ) ( ih * scale ), BufferedImage.TYPE_INT_RGB );
			rop.filter( bimg, bimg2 );
			bimg = bimg2;
		}
        return bimg;
    }
	public void paint( Graphics g, double col, double row,
			boolean isSideways, Component cmp, int positionNumber,double scale,boolean showPosNum ) {
		int yPos = ( int ) ( row * ih * scale );
		int xPos = ( int ) ( col * iw * scale );
        BufferedImage bimg=getOutputBuffImage(isSideways,scale);
		g.drawImage( bimg, xPos, yPos, cmp );
        hitRect.setBounds(xPos,yPos, bimg.getWidth(cmp), bimg.getHeight(cmp));
		if ( showPosNum )
			g.drawString( "" + positionNumber, xPos + 7, yPos + 18 );

	}

}

