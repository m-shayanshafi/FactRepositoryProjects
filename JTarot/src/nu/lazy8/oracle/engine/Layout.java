/*
 *  :tabSize=4:indentSize=2:noTabs=true:
 *  :folding=explicit:collapseFolds=1:
 *
 *  Copyright (C) 2007 Thomas Dilts.  This program is free
 *  software; you can redistribute it and/or modify it under the terms of the
 *  GNU General Public License as published by the Free Software Foundation;
 *  either version 2 of the License, or (at your option) any later version. This
 *  program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details. You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software Foundation,
 *  Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA For more
 *  information, surf to www.tarot.lazy8.nu or email tarot@lazy8.nu
 */

package nu.lazy8.oracle.engine;

//import nu.lazy8.oracle.util.*;
import java.io.*;

//platform dependent
import java.util.StringTokenizer;

public class Layout {
	public int numberofobjects;
//	public Real[] screenLayoutxy;
	public Float[] screenLayoutxy;
	public int[] printoutxy;
	public int[] printoutsize;
	public int[] screensize;
	public int[] showHiddenOrder;
    public OracleStandardFileParser content;
	Layout( InputStream inStream ) throws Exception{
		content=new OracleStandardFileParser( inStream );
		numberofobjects = Integer.parseInt( content.getValue( "numberofobjects" ) );
		screenLayoutxy = new Float[3 * numberofobjects];
//		screenLayoutxy = new Real[3 * numberofobjects];
		StringTokenizer tokenizer = new StringTokenizer( content.getValue( "screenLayoutxy" ) );
		for ( int i = 0; i < screenLayoutxy.length; i++ )
			screenLayoutxy[i] = new Float( tokenizer.nextToken() );
//			screenLayoutxy[i] = new Real( tokenizer.nextToken() );

		printoutsize = new int[2];
		tokenizer = new StringTokenizer( content.getValue( "printoutsize" ) );
		for ( int i = 0; i < printoutsize.length; i++ )
			printoutsize[i] = Integer.parseInt( tokenizer.nextToken() );

		printoutxy = new int[printoutsize[0] * printoutsize[1]];
		tokenizer = new StringTokenizer( content.getValue( "printoutxy" ) );
		for ( int i = 0; i < printoutxy.length; i++ )
			printoutxy[i] = Integer.parseInt( tokenizer.nextToken() );

   		screensize = new int[2];
		tokenizer = new StringTokenizer( content.getValue( "screensize" ) );
		for ( int i = 0; i < screensize.length; i++ )
			screensize[i] = Integer.parseInt( tokenizer.nextToken() );

        showHiddenOrder = new int[numberofobjects];
		tokenizer = new StringTokenizer( content.getValue( "showHiddenOrder" ) );
		for ( int i = 0; i < showHiddenOrder.length; i++ )
			showHiddenOrder[i] = Integer.parseInt( tokenizer.nextToken() );
	}
/*
	public final static Image rotate180degrees( Image srcImage ) {
      Image tmpImage= Image.createImage( srcImage.getWidth(), srcImage.getHeight() );
      Graphics g=tmpImage.getGraphics();
      g.drawRegion(srcImage,0,0, srcImage.getWidth(), srcImage.getHeight(),Sprite.TRANS_ROT180,0,0,Graphics.LEFT
						 | Graphics.TOP );
      return  tmpImage;
	}

	public final static Image scale( Image srcImage, int newW, int newH,boolean isRotate180degrees ) {
		int srcW = srcImage.getWidth();
		int srcH = srcImage.getHeight();

		Image dst = Image.createImage( newW, newH );
		Graphics g = dst.getGraphics();
		for ( int x = 0; x < newW; x++ )
			for ( int y = 0; y < newH; y++ ) {
				g.setClip( x, y, 1, 1 );
				g.drawImage( srcImage, x - x * srcW/newW, y - y * srcH / newH, Graphics.LEFT
						 | Graphics.TOP );

			}

        if(isRotate180degrees) 
          return rotate180degrees( dst );
        else
		  return dst;
	}
*/

}


