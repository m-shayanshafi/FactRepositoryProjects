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
import javax.imageio.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.util.*;
import java.util.zip.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.FileChannel;
import  nu.lazy8.oracle.engine.*;
import java.awt.image.*;
import java.text.*;

public class CardLayout extends JPanel {
	public SelectedCard cards[];
	public String displayReadingDate = "";
	private boolean isShowHiddenCards = false;
	public int numTurnedOverCards = 0;
	private static Font fontLarge = new Font( "Monospaced", Font.BOLD, 18 );
	private static Font fontSmall = new Font( "Monospaced", Font.BOLD, 10 );
    private int lastFoundHitCard=-1;

	public CardLayout() {
        MouseMotionListener ml=new MouseMotionListener(){
				public void mouseDragged( MouseEvent e ) { }


				public void mouseMoved( MouseEvent e ) { 
                    int foundHitPos=-1;
                    int x=e.getX(),y=e.getY();
                    for ( int i = 0; i < jTarot.oReading.layout.screenLayoutxy.length / 3; i++ ) {
                        if(cards[i].containsPoint(x,y))
                          foundHitPos=i;
                    }
                    if(foundHitPos>=0 && foundHitPos!=lastFoundHitCard){
                        jTarot.ThisjTarot.cardTranslations.scrollToReference("position" + foundHitPos);
                        lastFoundHitCard=foundHitPos;
                    }
                }
        };
		addMouseMotionListener( ml );
		MouseListener l =
			new MouseListener() {
				public void mouseClicked( MouseEvent e ) { }


				public void mouseEntered( MouseEvent e ) { }


				public void mouseExited( MouseEvent e ) { }


				public void mousePressed( MouseEvent e ) {
					ShowHiddedCards( true );
				}


				public void mouseReleased( MouseEvent e ) {
					ShowHiddedCards( false );
				}
			};
		addMouseListener( l );
	}
    public Image getLayoutImage(){
		setBackground( Color.white );
		Image img = (Image)(jTarot.io.getImage(jTarot.oReading.fileNames[OracleFileManager.F_graphicalobj], -1 ));
		if ( img == null ) {
			System.err.println( "Error reading image"  );
			return null;
		}
		int iw = 300;
		int ih = 300;
		//this can crash if there is a problem with the image, report it
        BufferedImage bi=null;
		try {
			bi = new BufferedImage( iw, ih, BufferedImage.TYPE_INT_RGB );
		} catch ( Exception e ) {
			System.err.println( "Error reading image "  );
			e.printStackTrace();
            return null;
		}
		Graphics2D g = bi.createGraphics();
		//g.drawImage( img, 0, 0, this );
        //calculate what the scaleing is.
        //screensize[0]=width
        //screensize[1]=height
        double scale=1.0;
        //see if it is the width or the height that is the limit
        if (((double)iw)/((double)img.getWidth( this )) * jTarot.oReading.layout.screensize[1]
          < ((double)ih)/((double)img.getHeight( this )) * jTarot.oReading.layout.screensize[0]){
          //it is the width that determines the scale
          scale=((double)iw)/((double)img.getWidth( this ))/((double)(jTarot.oReading.layout.screensize[0]));
        }else{
          //it is the height that determines the scale
          scale= ((double)ih)/((double)img.getHeight( this ))/((double)(jTarot.oReading.layout.screensize[1]));
        }
        
      
		g.setColor( Color.white );
		g.fillRect( 0, 0, iw, ih );
		g.setColor( Color.MAGENTA );
        if(scale<0.4)
          g.setFont( fontSmall );
        else
          g.setFont( fontLarge );
		for ( int i = 0; i < jTarot.oReading.layout.screenLayoutxy.length / 3; i++ )
				cards[i].paint( g, jTarot.oReading.layout.screenLayoutxy[i * 3], jTarot.oReading.layout.screenLayoutxy[i * 3 + 1],
						jTarot.oReading.layout.screenLayoutxy[i * 3 + 2] == 0 ? false : true, this, i + 1,scale,true );
        return bi;
    }
	public void setCardLayout( ) {
		numTurnedOverCards = 0;
        lastFoundHitCard=-1;
        cards = new SelectedCard[jTarot.oReading.layout.screenLayoutxy.length / 3];
        for ( int i = 0; i < jTarot.oReading.layout.screenLayoutxy.length / 3; i++ ) {
            cards[i] = new SelectedCard();
            cards[i].setCardImage( jTarot.oReading.chosenUpSideDown[i], jTarot.oReading.chosenObjects[i], this );
        }
		Dimension cardsize = cards[0].getSize();
		setPreferredSize( new Dimension( jTarot.oReading.layout.screensize[0] * cardsize.width, jTarot.oReading.layout.screensize[1] * cardsize.height ) );
        jTarot.ThisjTarot.cardTranslations.writeTranslationToPane(  );
        repaint();
		invalidate();
	}


	public void reset() {
        lastFoundHitCard=-1;
		for ( int i = 0; i < jTarot.oReading.layout.screenLayoutxy.length / 3; i++ )
			cards[i].reloadImage( this );

		Dimension cardsize = cards[0].getSize();
		setPreferredSize( new Dimension( jTarot.oReading.layout.screensize[0] * cardsize.width, jTarot.oReading.layout.screensize[1] * cardsize.height ) );
		jTarot.ThisjTarot.cardTranslations.writeTranslationToPane(  );
		invalidate();
	}


	public void redraw() {
		repaint();
	}


	public void ShowHiddedCards( boolean isShow ) {
			isShowHiddenCards = isShow;
			repaint();
	}


	public void exportHtml( File directory, ArrayList allFiles ) {
        if ( directory.isFile() && directory.canRead() ) {
            //this is wrong, it must be a directory not a file
           directory.delete();
        }
        if ( ! directory.isDirectory() ) 
            directory.mkdirs();
      
         File htmlFile = new File( directory.getAbsolutePath() + File.separatorChar + "index.html" );
		
        allFiles.add( htmlFile.getAbsolutePath() );
		try {
			FileWriter out = new FileWriter( htmlFile );
			createHTML( out, "=", "" );
			out.close();
			copyAllGraphicFiles( directory.getAbsolutePath(), allFiles );
		} catch ( Exception e ) {
			System.out.println( "error=" + e.getMessage() );
			e.printStackTrace();
		}
	}


	public void exportMhtml( File f ) {
		/*
		 *  Creating a mhtml file.NOTE BLANK LINES IMPORTANT AND = BECOMES =3D IN HTML
		 *  MIME-Version: 1.0
		 *  Content-Type: multipart/related;
		 *  boundary="multipart-jTarot-boundry";
		 *  type="text/html"
		 */
		String mhtmltext = "";
		try {
			FileWriter out = new FileWriter( f );
			Base64Encoder out64 = new Base64Encoder( out );

			//print out mhtml headers
			mhtmltext = "MIME-Version: 1.0";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );
			//carrige return
			mhtmltext = "Content-Type: multipart/related;";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );
			//carrige return
			String boundry = "multipart-jTarot-boundry";
			mhtmltext = "    boundary=\"" + boundry + "\";";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );
			//carrige return
			boundry = "--" + boundry;
			mhtmltext = "    type=\"text/html\"";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );
			//carrige return
			out.write( 13 );
			//carrige return

			out.write( boundry, 0, boundry.length() );
			out.write( 13 );
			//carrige return
			mhtmltext = "Content-Type: text/html;";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );
			//carrige return
			mhtmltext = "	charset=\"iso-8859-1\"";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );
			//carrige return
			mhtmltext = "Content-Transfer-Encoding: quoted-printable";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );
			//carrige return
			mhtmltext = "Content-Location: http://www.zzseszzpopoigfdtssessdd.nu";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );
			//carrige return
			out.write( 13 );
			//carrige return

			//get the rest
			createHTML( out, "=3D", "http://images/" );
			byte[] b = new byte[57];
			InputStream in;
			int readchar = 0;

			out.write( 13 );
			//carrige return
			out.write( 13 );
			//carrige return
			for ( int i = 0; i < jTarot.oReading.layout.screenLayoutxy.length / 3; i++ ) {
				out.write( 13 );
				//carrige return
				out.write( boundry, 0, boundry.length() );
				out.write( 13 );
				//carrige return
				mhtmltext = "Content-Type: image/jpeg";
				out.write( mhtmltext, 0, mhtmltext.length() );
				out.write( 13 );
				//carrige return
				mhtmltext = "Content-Transfer-Encoding: base64";
				out.write( mhtmltext, 0, mhtmltext.length() );
				out.write( 13 );
				//carrige return
				mhtmltext = "Content-Location: http://images/r" + ( cards[i].getCardIndex() + 1 ) + "." + jTarot.oReading.graphObjects.getValue("imagetype");
				out.write( mhtmltext, 0, mhtmltext.length() );
				out.write( 13 );
				//carrige return
				out.write( 13 );
				//carrige return
				out.flush();
                //create a temporary file to hold the image
                File tempFile = File.createTempFile( "jTarotMHTMLImage", "" );
                tempFile.deleteOnExit();
                try{
                  ImageIO.write(cards[i].getOutputBuffImage(
                      jTarot.oReading.layout.screenLayoutxy[i * 3 + 2] == 0 ? false : true,jTarot.scale),"jpeg",tempFile);
                }catch(Exception eee){
                    System.out.println("Failed writing file " + tempFile + " error= " + eee.getMessage());
                }
				in = new FileInputStream(tempFile );
				while ( ( readchar = in.read( b, 0, 57 ) ) > 0 ) {
					out64.write( b, 0, readchar );
					out64.flush();
					out.write( 13 );
					//carrige return
					out.flush();
				}
				in.close();
				out64.flush();
                tempFile.delete();
			}
			out.write( 13 );
			//carrige return
			out.write( 13 );
			//carrige return
			out.write( boundry, 0, boundry.length() );
			out.write( 13 );
			//carrige return
			out.write( 13 );
			//carrige return
			out.flush();
			out.flush();
			out.close();
		} catch ( Exception e ) {
			System.out.println( "error=" + e.getMessage() );
			e.printStackTrace();
		}
	}


	public void exportWARfile( File f ) {
		try {
			File tempDir = File.createTempFile( "jTarotWAR", "" );
			tempDir.delete();
			//remove the temp file
			tempDir.mkdir();
			//turn it into a directory
			ArrayList allFiles = new ArrayList();
			exportHtml( tempDir, allFiles );
			//temporary directory
			tempDir.deleteOnExit();
			for ( int i = 0; i < allFiles.size(); i++ )
				( new File( ( String ) ( allFiles.get( i ) ) ) ).deleteOnExit();
			byte b[] = new byte[512];
			FileOutputStream fout = new FileOutputStream( f );
			GZIPOutputStream gout = new GZIPOutputStream( fout );
			TarOutputStream zout = new TarOutputStream( gout );
			File fileNameManipulator;
			for ( int i = 0; i < allFiles.size(); i++ ) {
				InputStream in = new FileInputStream( ( String ) ( allFiles.get( i ) ) );
				fileNameManipulator = new File( ( String ) ( allFiles.get( i ) ) );
				TarEntry e = new TarEntry( fileNameManipulator );
				e.setName( fileNameManipulator.getName() );
				zout.putNextEntry( e );
				int len = 0;
				while ( ( len = in.read( b ) ) != -1 )
					zout.write( b, 0, len );

				zout.closeEntry();
			}
			zout.close();
			gout.close();
		} catch ( Exception eee ) {
			System.out.println( "error=" + eee.getMessage() );
			eee.printStackTrace();
		}
	}


	private void copyAllGraphicFiles( String outputDir, ArrayList allFiles ) {
		String fileName;
		String urlPath;
		for ( int i = 0; i < jTarot.oReading.layout.screenLayoutxy.length / 3; i++ ) {
			fileName = File.separatorChar + "r" + ( cards[i].getCardIndex() + 1 ) + "." + jTarot.oReading.graphObjects.getValue("imagetype");
            try{
            ImageIO.write(cards[i].getOutputBuffImage(
                jTarot.oReading.layout.screenLayoutxy[i * 3 + 2] == 0 ? false : true,jTarot.scale),jTarot.oReading.graphObjects.getValue("imagetype"),new File(outputDir + fileName));
            }catch(Exception eee){
                System.out.println("Failed writing file " + outputDir + fileName + " error= " + eee.getMessage());
            }
			allFiles.add( outputDir + fileName );
		}
    }


	private void createHTML( FileWriter out, String equalSign, String imagePrefix ) {
		String mhtmltext = "";
		StringBuffer posName = new StringBuffer();
		StringBuffer posDesc = new StringBuffer();
		StringBuffer cardName = new StringBuffer();
		StringBuffer cardDesc = new StringBuffer();
        StringBuffer cardDescReversed = new StringBuffer();

		int posIndex;
		try {
			//print out html headers
			mhtmltext = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );

			mhtmltext = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang" + equalSign + "\"" + 
                jTarot.io.getProperty("ISO.Language") + "\" lang" + equalSign + "\"" + jTarot.io.getProperty("ISO.Language") + "\"><head>";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );

			mhtmltext = "<title>jTarot</title>";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );

			mhtmltext = "<meta http-equiv" + equalSign + "\"Content-Type\" content" + equalSign + "\"text/html; charset" + equalSign + "UTF-8\" />";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );

			mhtmltext = "<style type" + equalSign + "\"text/css\">";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );

			mhtmltext = "p.boldnotop {font-weight: bold ; margin-top: 0cm ; margin-bottom: 0cm}";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );

			mhtmltext = "p.boldnobot {font-weight: bold ; margin-bottom: 0cm}";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );

			mhtmltext = "</style></head><body>";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );
			//carrige return
            String dateText="";
            try{
                dateText=" - " + DateFormat.getDateTimeInstance( DateFormat.SHORT, DateFormat.SHORT ).format( jTarot.oReading.timestamp );
            }catch(Exception e99){}
			//print out question
			mhtmltext = "<h4>" + jTarot.menus.translate( "screensQuestion" ) + dateText + "</h4>";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );
			//carrige return
			mhtmltext = "<p>" + jTarot.ThisjTarot.textQuestion.getText() + "</p>";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );
			//carrige return

			;
			//print out the graphics
			mhtmltext = "<table>";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );
			//carrige return
			for ( int row = 0; row < jTarot.oReading.layout.printoutsize[1]; row++ ) {
				mhtmltext = "<tr>";
				out.write( mhtmltext, 0, mhtmltext.length() );
				out.write( 13 );
				//carrige return
				for ( int col = 0; col < jTarot.oReading.layout.printoutsize[0]; col++ ) {
					posIndex = jTarot.oReading.layout.printoutxy[col + row * jTarot.oReading.layout.printoutsize[0]];
					mhtmltext = "<td align" + equalSign + "\"center\">";
					out.write( mhtmltext, 0, mhtmltext.length() );
					out.write( 13 );
					//carrige return
					if ( posIndex >= 0 ) {
						jTarot.ThisjTarot.cardTranslations.getCardInfo( posIndex,
								jTarot.oReading.chosenObjects[posIndex],
								posName, posDesc, cardName, cardDesc,cardDescReversed );
						//print out the card position
                        if(posName.length()!=0){
                          mhtmltext = "<p class" + equalSign + "\"boldnobot\">" + posName + "</p>";
                          out.write( mhtmltext, 0, mhtmltext.length() );
                          out.write( 13 );
                        }
						//carrige return
						//print out the image
						mhtmltext = "<a name" + equalSign + "\"titleimg" + posIndex + "\" href" + equalSign + 
                                "\"#descriptionimg" + posIndex + "\"><img src" + equalSign + "\"" + imagePrefix + "r" +
								( jTarot.oReading.chosenObjects[posIndex] + 1 ) + "." + jTarot.oReading.graphObjects.getValue("imagetype") + "\" alt" + equalSign + "\"" + cardName + "\" /></a>";
						out.write( mhtmltext, 0, mhtmltext.length() );
						out.write( 13 );
						//carrige return
						//print out if it is reversed
						if ( cards[posIndex].getIsReversed() ) {
							mhtmltext = "<p class" + equalSign + "\"boldnotop\">" + jTarot.menus.translate( "cardisreversed" ) + "</p>";
							out.write( mhtmltext, 0, mhtmltext.length() );
							out.write( 13 );
							//carrige return
						}
						//print out the card name
						mhtmltext = "<p class" + equalSign + "\"boldnotop\">" + cardName + "</p>";
						out.write( mhtmltext, 0, mhtmltext.length() );
						out.write( 13 );
						//carrige return

					}
					mhtmltext = "</td>";
					out.write( mhtmltext, 0, mhtmltext.length() );
					out.write( 13 );
					//carrige return
				}
				mhtmltext = "</tr>";
				out.write( mhtmltext, 0, mhtmltext.length() );
				out.write( 13 );
			}
			mhtmltext = "</table>";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );
			//carrige return

			//print out personal reading translation
			mhtmltext = "<h4>" + jTarot.menus.translate( "screensPersonalReading" ) + "</h4><p>";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );
			//carrige return
			out.write( jTarot.ThisjTarot.textTranslation.getText(), 0, jTarot.ThisjTarot.textTranslation.getText().length() );
			out.write( 13 );
			//carrige return

			//print out the generated reading onto the html page.
            //adviser copyright
			mhtmltext = "</p><h2>" + jTarot.menus.translate( "copyrighttitle" ) + "</h2><p>";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );
			//carrige return
			out.write( jTarot.oReading.graphObjTranslations.translate("copyright"), 0,jTarot.oReading.graphObjTranslations.translate("copyright").length() );
			out.write( 13 );
			//carrige return

            //deck copyright
 			mhtmltext = "</p><h2>" + jTarot.menus.translate( "TheDeckCopyright" ) + "</h2><p>";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );
			//carrige return
            String deckCopyright=jTarot.oReading.graphObjects.translate( "copyright");
            if (deckCopyright!=null)
                out.write( deckCopyright, 0, deckCopyright.length() );
			out.write( 13 );
			//carrige return
            if(jTarot.oReading.layoutTranslations.getValue("Title")!=null){
              mhtmltext = "</p><h2>" + jTarot.oReading.layoutTranslations.translate("Title") + "</h2>";
              out.write( mhtmltext, 0, mhtmltext.length() );
              out.write( 13 );
              //carrige return
              mhtmltext = "<p>" + jTarot.oReading.layoutTranslations.translate("Description") + "</p>";
              out.write( mhtmltext, 0, mhtmltext.length() );
              out.write( 13 );
              //carrige return
            }            
			//print out the generated reading onto the html page.
			int index;
			String thecard = jTarot.menus.translate( "thecard" );
			String thepostion = jTarot.menus.translate( "theposition" );
			//dividing line
			mhtmltext = "<hr />";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );
			//carrige return
			for ( int i = 0; i < cards.length; i++ ) {
				index = cards[i].getCardIndex();
				if ( index >= 0 ) {
					jTarot.ThisjTarot.cardTranslations.getCardInfo( i, index, posName, posDesc, cardName, cardDesc,cardDescReversed );
					//show the image one more time.
					mhtmltext = "<table><tr><td><a name" + equalSign + "\"descriptionimg" + i + "\" href" + 
                            equalSign + "\"#titleimg" + i + "\"><img src" + equalSign + "\"" + imagePrefix + "r" +
							( index + 1 ) + "." + jTarot.oReading.graphObjects.getValue("imagetype") + "\" alt" + equalSign + "\"" + cardName + "\" /></a></td><td>";
					out.write( mhtmltext, 0, mhtmltext.length() );
					out.write( 13 );
					//carrige return
					//The Card Layout position name
                    if(posName.length()!=0){
                        mhtmltext = "<h3>" + thepostion + " " + ( i + 1 ) + " : " + posName + "</h3>";
                        out.write( mhtmltext, 0, mhtmltext.length() );
                        out.write( 13 );
                    }
                    //carrige return
					//The card layout postion description
					out.write( posDesc.toString(), 0, posDesc.length() );
					out.write( 13 );
					//carrige return
					//The card name
					mhtmltext = "<h3>" + thecard + " : " + cardName + "</h3>";
					out.write( mhtmltext, 0, mhtmltext.length() );
					out.write( 13 );
					//carrige return
					//print out if it is reversed
					if ( cards[i].getIsReversed() ) {
						mhtmltext = "<h3>" + jTarot.menus.translate( "cardisreversed" ) + "</h3>";
						out.write( mhtmltext, 0, mhtmltext.length() );
						out.write( 13 );
						//carrige return
					}
					//The card description
                    if(jTarot.io.getBooleanProperty( "showreversedandnormal") ){
                      mhtmltext = cardDesc.toString() ;
                      out.write( mhtmltext, 0, mhtmltext.length() );
                      out.write( 13 );
                      if(cardDescReversed.length()!=0){
                          mhtmltext =  "<h4>" + jTarot.menus.translate("ifreversed" ) + "</h4>";
                          out.write( mhtmltext, 0, mhtmltext.length() );
                          out.write( 13 );
                          mhtmltext = cardDescReversed.toString() ;
                          out.write( mhtmltext, 0, mhtmltext.length() );
                          out.write( 13 );
                      }
                    }else{
                      if (  jTarot.oReading.chosenUpSideDown[i] && cardDescReversed.length()!=0){
                          mhtmltext = cardDescReversed.toString() ;
                          out.write( mhtmltext, 0, mhtmltext.length() );
                          out.write( 13 );
                      }else{
                          mhtmltext = cardDesc.toString() ;
                          out.write( mhtmltext, 0, mhtmltext.length() );
                          out.write( 13 );
                      }
                    }
					mhtmltext =  "</td></tr></table>";
					out.write( mhtmltext, 0, mhtmltext.length() );
					out.write( 13 );
					//carrige return
					//dividing line
					mhtmltext = "<hr />";
					out.write( mhtmltext, 0, mhtmltext.length() );
					out.write( 13 );
					//carrige return
				}
			}

			//print out the ending html code.
			mhtmltext = "</body></html>";
			out.write( mhtmltext, 0, mhtmltext.length() );
			out.write( 13 );
			//carrige return
		} catch ( Exception eee ) {
			System.out.println( "error=" + eee.getMessage() );
			eee.printStackTrace();
		}
	}


	public void paint( Graphics g ) {
		if ( cards == null )
			return;
		g.setColor( getBackground() );
		g.fillRect( 0, 0, getWidth(), getHeight() );
		g.setColor( Color.MAGENTA );
		g.setFont( fontLarge );
		for ( int i = 0; i < jTarot.oReading.layout.screenLayoutxy.length / 3; i++ )
			if ( isShowHiddenCards )
				cards[jTarot.oReading.layout.showHiddenOrder[i]].paint( g, jTarot.oReading.layout.screenLayoutxy[jTarot.oReading.layout.showHiddenOrder[i] * 3],
						jTarot.oReading.layout.screenLayoutxy[jTarot.oReading.layout.showHiddenOrder[i] * 3 + 1],
						jTarot.oReading.layout.screenLayoutxy[jTarot.oReading.layout.showHiddenOrder[i] * 3 + 2] == 0 ? false : true, 
                        this, jTarot.oReading.layout.showHiddenOrder[i] + 1 ,jTarot.scale,jTarot.io.getBooleanProperty( "showPositionNumbers" ));
			else
				cards[i].paint( g, jTarot.oReading.layout.screenLayoutxy[i * 3], jTarot.oReading.layout.screenLayoutxy[i * 3 + 1],
						jTarot.oReading.layout.screenLayoutxy[i * 3 + 2] == 0 ? false : true, this, i + 1,jTarot.scale,
                        jTarot.io.getBooleanProperty( "showPositionNumbers" ));

	}

}

