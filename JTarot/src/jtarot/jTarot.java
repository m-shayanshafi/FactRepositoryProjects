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
import  nu.lazy8.oracle.engine.*;
import javax.imageio.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.geom.AffineTransform;
import java.awt.font.TextLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.net.URL;
import java.util.*;
import java.text.*;
import javax.swing.text.*;
import javax.swing.undo.*;
import javax.swing.event.*;
import java.beans.*;
import java.nio.charset.*;
import java.net.URL;
import java.net.URLClassLoader;

// Standard XML API
// DOM
import org.w3c.dom.Document;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
// SAX.
import org.xml.sax.*;
import org.xml.sax.helpers.*;

//JAXP 1.1
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
// Transform
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;

public class jTarot extends JFrame {
    public static OracleIO io;
    public static OracleReading oReading;
    public static OracleStandardFileParser menus=new OracleStandardFileParser();
	public static ArrayList fileHistory = new ArrayList();
	private Hashtable commands;
	public static Hashtable menuItems;
	private Hashtable menuRadioGroups;
	private JMenuBar menubar;
	public JScrollPane scroller;
	public static jTarot ThisjTarot = null;
	public CardReadings cardTranslations;
	JMenuBar menuBar;
	JMenu menu;
	JMenuItem menuItem;
	public FreeTextFrame textQuestion;
	public FreeTextFrame textTranslation;
	public static MultiFileFilter propertiesFileFilter, xmlMultiFileFilter, warMultiFileFilter, mhtmlMultiFileFilter;

	public static CardLayout cardlayout = null;
	public static double scale = 1.0;
	private static String customizedCardsDirectory = "";

	private String lastSelectedDeck = "";
	private String lastSelectedZoom = "";
	private boolean isMainScreenShowing = false;

	/**
	 * Suffix applied to the key used in resource file
	 * lookups for a label.
	 */
	public final static String labelSuffix = "Label";

	/**
	 * Suffix applied to the key used in resource file
	 * lookups for an action.
	 */
	public final static String actionSuffix = "Action";
	public final static String radiogroupSuffix = "RadioGroup";
	public final static String radiogroupdefaultSuffix = "RadioGroupDefault";
	public final static String checkboxSuffix = "CheckBox";
	public final static String dynamicgroupSuffix = "DynamicGroup";

	/**
	 * Suffix applied to the key used in resource file
	 * lookups for an image.
	 */
	public final static String imageSuffix = "Image";


	public jTarot() {
		//Construction Menu
		ThisjTarot = this;
        cardlayout = new CardLayout();
        cardTranslations = new CardReadings();
      try {
          io = new OracleLazy8IO();
          oReading=new OracleReading(io);
      } catch ( Exception ee ) {
          System.out.println(ee.getMessage() );
          ee.printStackTrace();
         if(io==null)
         {
            JOptionPane jOpt = new JOptionPane();
            jOpt.showMessageDialog( jTarot.ThisjTarot, "Failed to load the io module ; "+ee.getMessage() ,
                    "jTarot", JOptionPane.INFORMATION_MESSAGE );
            return;
         }else{
           try {
               io.resetSetup(true,false,false);
               io = new OracleLazy8IO();
               oReading=new OracleReading(io);
            } catch ( Exception e ) {
                System.out.println(e.getMessage() );
                JOptionPane jOpt = new JOptionPane();
                jOpt.showMessageDialog( jTarot.ThisjTarot, "Failed to load the standard setup ; "+e.getMessage() ,
                        "jTarot", JOptionPane.INFORMATION_MESSAGE );
               e.printStackTrace();
               return;
            }
         }
      }
      setIconImage(OracleLazy8IO.getResourceImage("res/icon.png"));
        loadLocalProperties();
        
		xmlMultiFileFilter = new MultiFileFilter( "xml", "XML eXtended Markup Language Files" );
		warMultiFileFilter = new MultiFileFilter( "war", "Web ARchive Files" );
		mhtmlMultiFileFilter = new MultiFileFilter( new String[]{"mht", "mhtml"}, "MHTML Multipart HyperText Markup Language Files" );
		propertiesFileFilter = new MultiFileFilter( "properties", "Java properties files" );
		textQuestion = new FreeTextFrame( "", true );
		textQuestion.setPreferredSize( new Dimension( 10, 80 ) );
		textTranslation = new FreeTextFrame( "", true );
		textTranslation.setPreferredSize( new Dimension( 10, 80 ) );
        cardTranslations=new CardReadings();
		cardTranslations.setPreferredSize( new Dimension( 300, 10 ) );

		scroller = new JScrollPane();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
         dim.setSize(dim.getWidth()*0.75,dim.getHeight()*0.75);
		setSize( dim );
		setTitle( menus.translate( "Title" ) + "   " + io.getProperty( "jTarotVersion" ) + "  " + menus.translate( "menulanguage" ) );
		Log.log(Log.NOTICE,jTarot.class,"jTarot version= " + io.getProperty( "jTarotVersion" ) );
        reloadScreenLanguages();

		addWindowListener(
			new WindowAdapter() {
				public void windowClosing( WindowEvent e ) {
                    saveLocalProperties();
                    Log.closeStream();
					System.exit( 0 );
				}
			} );
		addComponentListener(
			new ComponentListener() {
				public void componentResized( ComponentEvent e ) {
					DelayedSaveMainScreenGeometry();
				}


				public void componentMoved( ComponentEvent e ) {
					DelayedSaveMainScreenGeometry();
				}


				public void componentShown( ComponentEvent e ) {
				}


				public void componentHidden( ComponentEvent e ) {
				}
			} );

		SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {
					new LayoutWizard();
				}
			} );
	}

  public void reloadScreenLanguages()
  {
    try{
      menus=new OracleStandardFileParser(io.getInputStream(OracleFileManager.getTranslationFileName("menus",io.getProperty(OracleLazy8IO.ISO_LANGUAGE))));
    }catch(Exception e){
      e.printStackTrace();
      JOptionPane jOpt = new JOptionPane();
      jOpt.showMessageDialog( jTarot.ThisjTarot, "Failed to load menus, this program will not run. Hit Exit ; "+e.getMessage() ,
              "jTarot", JOptionPane.INFORMATION_MESSAGE );
      return;
    }
    try{
    textQuestion.setTitle( menus.translate( "screensQuestion" ) + " - " + DateFormat.getDateTimeInstance( DateFormat.SHORT, DateFormat.SHORT ).format( oReading.timestamp ) );
    }catch(Exception e99){}
    textTranslation.setTitle(menus.translate( "screensPersonalReading" ));
    ThisjTarot.setTitle( menus.translate( "Title" ) + "   " + io.getProperty( "jTarotVersion" ) + "  " + menus.translate( "menulanguage" ) );
    cardTranslations.writeTranslationToPane(  );
    ThisjTarot.menuBar = createMenubar();
    ThisjTarot.setJMenuBar( ThisjTarot.menuBar );
    ( ( JCheckBoxMenuItem ) jTarot.ThisjTarot.menuItems.get( "showreversedandnormal" ) ).setSelected(io.getBooleanProperty("showreversedandnormal"));
    ( ( JCheckBoxMenuItem ) jTarot.ThisjTarot.menuItems.get( "showpositionnumbers" ) ).setSelected(io.getBooleanProperty("showPositionNumbers"));
    ( ( JRadioButtonMenuItem ) jTarot.ThisjTarot.menuItems.get( io.getProperty("scaleText", "percent100")) ).setSelected(true);
    ThisjTarot.pack();
	loadGeometry( this, "MainWindow" );
  }
	private void DelayedSaveMainScreenGeometry() {
		SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {
					saveGeometry( ThisjTarot, "MainWindow" );
				}
			} );
	}


	public void ShowMainScreen( boolean isShow ) {
		remove( textQuestion );
		remove( textTranslation );
		remove( cardTranslations );
		remove( scroller );
		if ( isShow ) {
			add( textQuestion, "North" );
			add( textTranslation, "South" );
			add( cardTranslations, "East" );
			add( scroller, "Center" );
			pack();
		}
		else
			repaint();
		loadGeometry( this, "MainWindow" );
		( ( JMenu ) menuItems.get( "export" ) ).setEnabled( isShow );
		( ( JMenuItem ) menuItems.get( "save" ) ).setEnabled( isShow );
		( ( JMenuItem ) menuItems.get( "printtoweb" ) ).setEnabled( isShow );
		isMainScreenShowing = isShow;
	}


	private static void SetDefaultDeck() {
//		imageSubDir = "rider";
//        io.setProperty( "imageSubDir","rider" );
//		customizedCardsDirectory = "";
//		( ( JRadioButtonMenuItem ) menuItems.get( io.getProperty( "imageSubDir" ) ) ).setSelected( true );
	}


	/**
	 * Fast & simple file copy.
	 *
	 * @param  source  Description of the Parameter
	 * @param  dest    Description of the Parameter
	 */
	public static void copyFile( InputStream source, File dest ) {
		FileOutputStream bw = null;
		int readBytes = 0;
		try {
			bw = new FileOutputStream( dest );
			int fileLength = 500000;

			byte charBuff[] = new byte[fileLength];

			while ( ( readBytes = source.read( charBuff, 0, fileLength ) ) != -1 )
				bw.write( charBuff, 0, readBytes );
		} catch ( Exception fnfe ) {
			System.out.println( source + " Error copying.  " + fnfe.getMessage() );
			fnfe.printStackTrace();
			return;
		} finally {
			try {
				if ( source != null )
					source.close();

				if ( bw != null )
					bw.close();
			} catch ( IOException ioe ) {}
		}
	}

	/**
	 * Fetch the list of actions supported by this
	 * editor.  It is implemented to return the list
	 * of actions supported by the embedded JTextComponent
	 * augmented with the actions defined locally.
	 *
	 * @return    The actions value
	 */
	public Action[] getActions() {
		return defaultActions;
	}


	/**
	 * Actions defined by the Notepad class
	 */
	private Action[] defaultActions = {
			new CardLayoutAction( "newlayout", false ),
			new OpenAction(),
			new SaveAction(),
			new RecentAction(),
			new PrintToWebAction(),
			new ExitAction(),
			new ShowAllImages(),
			new ShowPositionNumbersAction(),
            new ShowReversedAndNormalTextsAction(),
			new ZoomAction( "percent200", 2.0 ),
			new ZoomAction( "percent150", 1.5 ),
			new ZoomAction( "percent100", 1.0 ),
			new ZoomAction( "percent75", 0.75 ),
			new ZoomAction( "percent50", 0.5 ),
			new ZoomAction( "percent25", 0.25 ),
			new ImagesAction(  ),
			new BrowserOptionsAction(),
			new AdvisorAction(  ),
    		new ExportHtmlAction(),
			new ExportMhtmlAction(),
			new ExportWarAction(),
			new LanguageAction(),
            new CreateSetupAction("createsetupfiles"),
            new EditSetupAction(),
            new LoadSetupAction(),
			new EditHelpAction(  ),
            new TroubleShootAction(),
			new HelpAction( "generalhelp", "generalhelpLabel","givetranslatehelp","" ),
			new HelpAction( "contribute", "generalhelpLabel",  "givetranslatehelp","title35" ),
			};

	class CardLayoutAction extends AbstractAction {
		boolean isIndex = false;


		CardLayoutAction( String cmd, boolean isIndex ) {
			super( cmd );
			this.isIndex = isIndex;
		}


		public void actionPerformed( ActionEvent e ) {
			if ( isIndex ) {
				cardlayout.setCardLayout( );
				scroller.getViewport().add( "Center", cardlayout );
				cardlayout.reset();
				jTarot.ThisjTarot.ShowMainScreen( true );
			}
			else
				new LayoutWizard();
		}
	}


	class OpenAction extends AbstractAction {
		OpenAction() {
			super( "open" );
		}


		public void actionPerformed( ActionEvent e ) {
			File f = ShowFileChooserDialog( "xml", false, xmlMultiFileFilter, "ReadingSave", false, jTarot.menus.translate( "OpenAFile") );
			if ( f != null && f.isFile() && f.canRead() ) {
              if ( cardlayout != null )
                  scroller.getViewport().remove( cardlayout );
              try{
                SaveableReading saveReading=new SaveableReading(new FileInputStream(f));
                saveReading.groupnametrans=io.getProperty("ISO.Language");
                saveReading.layoutlanguage=io.getProperty("ISO.Language");
                saveReading.graphicalobjects=jTarot.oReading.graph;
                saveReading.graphicalobjectsDef=jTarot.oReading.graphdef;
                textTranslation.setText( saveReading.comments );
                textQuestion.setText( saveReading.question);
                try{
	    		    textQuestion.setTitle( menus.translate( "screensQuestion" ) + " - " + DateFormat.getDateTimeInstance( DateFormat.SHORT, DateFormat.SHORT ).format( saveReading.timestamp ) );
                }catch(Exception e99){}
                saveReading.copyToOracleReading(oReading);
                jTarot.cardlayout.setCardLayout();
                scroller.getViewport().add( "Center", cardlayout );
                setSize( new Dimension( 550, 550 ) );
                pack();
                SaveToFileHistory(f);
                ShowMainScreen( true );
              }catch(Exception e8){
                //try the older reading technique
                e8.printStackTrace();
                if ( OpenJAXPTransformWithDOM( f ) ){
                  jTarot.cardlayout.setCardLayout();
                  scroller.getViewport().add( "Center", cardlayout );
                  setSize( new Dimension( 550, 550 ) );
                  pack();
                  SaveToFileHistory(f);
                  ShowMainScreen( true );
                }else {
                      JOptionPane jOpt = new JOptionPane();
                      jOpt.showMessageDialog( jTarot.ThisjTarot, menus.translate( "BadFileMessage" ),
                              menus.translate( "Title" ), JOptionPane.INFORMATION_MESSAGE );
                  }
              }
			}
		}
	}


	class SaveAction extends AbstractAction {
		SaveAction() {
			super( "save" );
		}


		public void actionPerformed( ActionEvent e ) {
			File f = ShowFileChooserDialog( "xml", false, xmlMultiFileFilter, "ReadingSave", true, jTarot.menus.translate( "SaveAFile") );
			if ( f != null ){
                  SaveableReading savedReading = new SaveableReading( jTarot.oReading.group,
                    jTarot.oReading.groupdef,jTarot.oReading.layoutdef,
                    jTarot.oReading.layoutdeftrans,jTarot.oReading.graph,
                    jTarot.oReading.graphdef,
                    jTarot.oReading.chosenObjects,
                    jTarot.oReading.chosenUpSideDown, textQuestion.getText(  ), textTranslation.getText(  ), new Date());
                  try {
                      savedReading.writeReadingXml( new FileOutputStream(f) );
                  } catch ( Exception ee ) {
					JOptionPane jOpt = new JOptionPane();
					jOpt.showMessageDialog( jTarot.ThisjTarot, menus.translate( "BadFileMessage" ),
							menus.translate( "Title" ), JOptionPane.INFORMATION_MESSAGE );
                  }finally{
                    SaveToFileHistory(f);
                  }
            }
        }
	}
	class RecentAction extends AbstractAction implements MenuListener{
		RecentAction() {
			super( "recent" );
		}
        class RecentMenuWatcher implements ActionListener{
          File f ;
          RecentMenuWatcher(String filepath,JMenuItem mi){
            f=new File(filepath);
            mi.addActionListener(this);
            mi.setText(f.getName());
          }
          public void  actionPerformed(ActionEvent e) {
			if ( f != null && f.isFile() && f.canRead() ) {
              if ( cardlayout != null )
                  scroller.getViewport().remove( cardlayout );
              try{
                SaveableReading saveReading=new SaveableReading(new FileInputStream(f));
                saveReading.groupnametrans=io.getProperty("ISO.Language");
                saveReading.layoutlanguage=io.getProperty("ISO.Language");
                saveReading.graphicalobjects=jTarot.oReading.graph;
                saveReading.graphicalobjectsDef=jTarot.oReading.graphdef;
                textTranslation.setText( saveReading.comments );
                textQuestion.setText( saveReading.question);
                try{
	    		    textQuestion.setTitle( menus.translate( "screensQuestion" ) + " - " + DateFormat.getDateTimeInstance( DateFormat.SHORT, DateFormat.SHORT ).format( saveReading.timestamp ) );
                }catch(Exception e99){}
                saveReading.copyToOracleReading(oReading);
                jTarot.cardlayout.setCardLayout();
                scroller.getViewport().add( "Center", cardlayout );
                setSize( new Dimension( 550, 550 ) );
                pack();
                SaveToFileHistory(f);
                ShowMainScreen( true );
              }catch(Exception e8){
                //try the older reading technique
                e8.printStackTrace();
                if ( OpenJAXPTransformWithDOM( f ) ){
                  jTarot.cardlayout.setCardLayout();
                  scroller.getViewport().add( "Center", cardlayout );
                  setSize( new Dimension( 550, 550 ) );
                  pack();
                  SaveToFileHistory(f);
                  ShowMainScreen( true );
                }else {
                      JOptionPane jOpt = new JOptionPane();
                      jOpt.showMessageDialog( jTarot.ThisjTarot, menus.translate( "BadFileMessage" ),
                              menus.translate( "Title" ), JOptionPane.INFORMATION_MESSAGE );
                  }
              }
			}
            else{
              JOptionPane jOpt = new JOptionPane();
              jOpt.showMessageDialog( jTarot.ThisjTarot, menus.translate( "BadFileMessage" ),
                      menus.translate( "Title" ), JOptionPane.INFORMATION_MESSAGE );
              RemoveFileFromHistory(f);
            }
          }
        }
        public JMenu m;
        public void menuSelected(MenuEvent e){
            m.removeAll();
            JMenuItem mi=null;
            if(fileHistory.size()==0){
              mi=new JMenuItem(menus.translate( "Lastusedreadingslistisempty" ));
              mi.setEnabled(false);
              m.add(mi);
            }
            else
            {
              JMenu subMenus=m;
              for ( int i = 0; i < fileHistory.size(); i++ ){
                if (fileHistory.get(i).toString().length()>0){
                  mi=new JMenuItem();
                  new RecentMenuWatcher(fileHistory.get(i).toString(),mi);
                  subMenus.add(mi);    
                  if (i==19){
                    subMenus=new JMenu(menus.translate( "More" ));
                    m.addSeparator();
                    m.add(subMenus);    
                  }
                }
              }
              subMenus.addSeparator();
              mi=new JMenuItem(menus.translate( "Removealllastusedreadings" ));
              subMenus.add(mi);    
              mi.addActionListener(new ActionListener(){
                  public void  actionPerformed(ActionEvent e) {
                    fileHistory.clear();
                  }
              });
            }
        }
        public void menuDeselected(MenuEvent e){
        }
        public void menuCanceled(MenuEvent e){
        }
        public void setListener(JMenu menu){
          this.m=menu;
          m.addMenuListener(this);
        }

		public void actionPerformed( ActionEvent e ) {
		}
	}

	class LanguageAction extends AbstractAction implements MenuListener{
		LanguageAction() {
			super( "language" );
		}
        class LanguageMenuWatcher implements ActionListener{
          String isoLanguageCode ;
          LanguageMenuWatcher(String isoLanguageCode,JMenuItem mi){
            this.isoLanguageCode=isoLanguageCode;
            mi.addActionListener(this);
          }
          public void  actionPerformed(ActionEvent e) {
            io.setProperty("ISO.Language",isoLanguageCode);
            io.setProperty("DefaultSetupLayoutTranslations",isoLanguageCode);
            io.setProperty("DefaultSetupOracleGroupTrans",isoLanguageCode);
            try{
            oReading.Reload(true);
            }catch(Exception e8){}
            saveGeometry( ThisjTarot, "MainWindow" );
            reloadScreenLanguages();
          }
        }
        public JMenu m;
        public void menuSelected(MenuEvent e){
            m.removeAll();
            JMenuItem mi=null;
            FileProperties[] foundFiles=OracleFileManager.findAllMatches(
              jTarot.io.getProperty( OracleReading.sDEFAULT_GROUP ),"OracleTranslations","","*");
            int foundIndex= 0;
            String defaultString=jTarot.io.getProperty( "ISO.Language" );
            OracleStandardFileParser parser=null;
            for ( int i = 0; i < foundFiles.length; i++ ){
//System.err.println("found lang file*********** =" +foundFiles[i].get( FileProperties.F_filename ) );
              try{
              parser=new OracleStandardFileParser (io.getInputStream(foundFiles[i].get( FileProperties.F_filename )));
              }catch(Exception e8){}
              mi=new JRadioButtonMenuItem(parser.translate( "languagename" ),defaultString.equals(parser.getFileAtt("language")));
              mi.setEnabled(true);
              new LanguageMenuWatcher(parser.getFileAtt("language"),mi);
              m.add(mi);
            }
        }
        public void menuDeselected(MenuEvent e){
        }
        public void menuCanceled(MenuEvent e){
        }
        public void setListener(JMenu menu){
          this.m=menu;
          m.addMenuListener(this);
        }

		public void actionPerformed( ActionEvent e ) {
		}
	}
	class AdvisorAction extends AbstractAction implements MenuListener{
		AdvisorAction() {
			super( "advisors" );
		}
        class AdvisorMenuWatcher implements ActionListener{
          String advisorName ;
          AdvisorMenuWatcher(String advisorName,JMenuItem mi){
            this.advisorName=advisorName;
            mi.addActionListener(this);
          }
          public void  actionPerformed(ActionEvent e) {
            io.setProperty("DefaultSetupGraphicalObjectTranslations",advisorName);
            try{
            oReading.Reload(true);
            }catch(Exception e8){}
            cardTranslations.writeTranslationToPane(  );
          }
        }
        public JMenu m;
        public void menuSelected(MenuEvent e){
            m.removeAll();
            JMenuItem mi=null;
            FileProperties[] foundFiles=OracleFileManager.findAllMatches(
              jTarot.io.getProperty( OracleReading.sDEFAULT_GROUP ),"GraphicObjTranslations","*",io.getProperty("ISO.Language"));
            int foundIndex= 0;
            String defaultString=jTarot.io.getProperty("DefaultSetupGraphicalObjectTranslations" );
            OracleStandardFileParser parser=null;
            for ( int i = 0; i < foundFiles.length; i++ ){
              try{
              parser=new OracleStandardFileParser (io.getInputStream(foundFiles[i].get( FileProperties.F_filename )));
              }catch(Exception e8){
                System.err.println("could not find advisor file =" +foundFiles[i].get( FileProperties.F_filename ) );
              }
              mi=new JRadioButtonMenuItem(parser.getFileAtt("author"),defaultString.equals(parser.getFileAtt("subclass")));
              mi.setEnabled(true);
              new AdvisorMenuWatcher(parser.getFileAtt("subclass"),mi);
              m.add(mi);
            }
        }
        public void menuDeselected(MenuEvent e){
        }
        public void menuCanceled(MenuEvent e){
        }
        public void setListener(JMenu menu){
          this.m=menu;
          m.addMenuListener(this);
        }

		public void actionPerformed( ActionEvent e ) {
		}
	}
	class ImagesAction extends AbstractAction implements MenuListener{
		ImagesAction() {
			super( "images" );
		}
        class ImagesMenuWatcher implements ActionListener{
          String imagesName ;
          ImagesMenuWatcher(String imagesName,JMenuItem mi){
            this.imagesName=imagesName;
            mi.addActionListener(this);
          }
          public void  actionPerformed(ActionEvent e) {
            io.setProperty("DefaultSetupGraphicalObject",imagesName);
            try{
            oReading.Reload(true);
            }catch(Exception e8){}
            cardlayout.reset();
            cardlayout.redraw();
          }
        }
        public JMenu m;
        public void menuSelected(MenuEvent e){
            m.removeAll();
            JMenuItem mi=null;
            FileProperties[] foundFiles=OracleFileManager.findAllMatches(
              jTarot.io.getProperty( OracleReading.sDEFAULT_GROUP ),"GraphicObjects","*","*");
            int foundIndex= 0;
            String defaultString=jTarot.io.getProperty( "DefaultSetupGraphicalObject" );
            for ( int i = 0; i < foundFiles.length; i++ ){
              mi=new JRadioButtonMenuItem(foundFiles[i].get( FileProperties.F_subclass ),defaultString.equals(foundFiles[i].get( FileProperties.F_subclass )));
              mi.setEnabled(true);
              new ImagesMenuWatcher(foundFiles[i].get( FileProperties.F_subclass ),mi);
              m.add(mi);
            }
        }
        public void menuDeselected(MenuEvent e){
        }
        public void menuCanceled(MenuEvent e){
        }
        public void setListener(JMenu menu){
          this.m=menu;
          m.addMenuListener(this);
        }

		public void actionPerformed( ActionEvent e ) {
		}
	}

	class PrintToWebAction extends AbstractAction {
		PrintToWebAction() {
			super( "printtoweb" );
		}


		public void actionPerformed( ActionEvent e ) {
			if ( cardlayout != null )
				try {
					File tempDir = File.createTempFile( "jTarotHtmlPrintout", "" );
					tempDir.delete();
					//remove the temp file
					tempDir.mkdir();
					//turn it into a directory
					ArrayList allFiles = new ArrayList();
					cardlayout.exportHtml( tempDir, allFiles );
					tempDir.deleteOnExit();
					for ( int i = 0; i < allFiles.size(); i++ )
						( new File( ( String ) ( allFiles.get( i ) ) ) ).deleteOnExit();
					Browser.init();
					Browser.displayURL( new File( tempDir.getAbsolutePath() + File.separatorChar + "index.html" ).toURL().toString() );
				} catch ( Exception eee ) {
					System.out.println( "Could not start web browser, error=  " + eee.getMessage() );
					eee.printStackTrace();
				}

		}
	}


	class ExitAction extends AbstractAction {
		ExitAction() {
			super( "exit" );
		}


		public void actionPerformed( ActionEvent e ) {
            saveLocalProperties();
			System.exit( 0 );
		}
	}


	class ShowAllImages extends AbstractAction {
		ShowAllImages() {
			super( "showallobjects" );
		}
		public void actionPerformed( ActionEvent e ) {
          try{
          oReading.Reload( io.getProperty( oReading.sDEFAULT_GROUP ),
					io.getProperty( oReading.sDEFAULT_GROUP_DEF ),
					io.getProperty( oReading.sDEFAULT_GRAPH_OBJ ),
					io.getProperty( oReading.sDEFAULT_GRAPH_OBJ_TRANS ),
					io.getProperty( oReading.sDEFAULT_GROUP ),
					io.getProperty( oReading.sDEFAULT_LAYOUT_TRANS ) );
          }catch(Exception eee){}
          oReading.resetReading();
          oReading.SetReading(oReading.layout.showHiddenOrder,oReading.chosenUpSideDown,new Date());
          cardlayout.setCardLayout( );
          cardlayout.redraw();
          cardTranslations.writeTranslationToPane(  );
            ThisjTarot.pack();
		}
	}
	class ShowPositionNumbersAction extends AbstractAction {
		ShowPositionNumbersAction() {
			super( "showpositionnumbers" );
		}


		public void actionPerformed( ActionEvent e ) {
			io.setBooleanProperty( "showPositionNumbers" ,( ( JCheckBoxMenuItem ) jTarot.ThisjTarot.menuItems.get( "showpositionnumbers" ) ).isSelected()) ;
			cardlayout.redraw();
		}
	}
	class ShowReversedAndNormalTextsAction extends AbstractAction {
		ShowReversedAndNormalTextsAction() {
			super( "showreversedandnormal" );
		}


		public void actionPerformed( ActionEvent e ) {
			io.setBooleanProperty( "showreversedandnormal" ,( ( JCheckBoxMenuItem ) jTarot.ThisjTarot.menuItems.get( "showreversedandnormal" ) ).isSelected()) ;
			cardTranslations.writeTranslationToPane(  );
		}
	}


	class ZoomAction extends AbstractAction {
		Double scale = 1.0;
		String scaleText = "";


		ZoomAction( String cmd, Double scale ) {
			super( cmd );
			this.scaleText = cmd;
			this.scale = scale;
		}


		public void actionPerformed( ActionEvent e ) {
			jTarot.io.setProperty( "scaleText", this.scaleText ) ;
            ThisjTarot.scale=scale;
			if ( cardlayout != null )
				cardlayout.reset();

			if ( isMainScreenShowing )
				ShowMainScreen( true );
		}
	}


	class ExportHtmlAction extends AbstractAction {
		ExportHtmlAction() {
			super( "exporthtml" );
		}


		public void actionPerformed( ActionEvent e ) {
			File f = ShowFileChooserDialog( "", true, null, "HtmlDirctorySave", true,jTarot.menus.translate( "SaveAFile") );
			if ( f != null && cardlayout != null )
				cardlayout.exportHtml( f, new ArrayList() );

		}
	}


	class ExportMhtmlAction extends AbstractAction {
		ExportMhtmlAction() {
			super( "exportmhtml" );
		}


		public void actionPerformed( ActionEvent e ) {
			File f = ShowFileChooserDialog( "mhtml", false, mhtmlMultiFileFilter, "MhtmlFileSave", true, jTarot.menus.translate( "SaveAFile") );
			if ( f != null && cardlayout != null )
				cardlayout.exportMhtml( f );

		}
	}


	class ExportWarAction extends AbstractAction {
		ExportWarAction() {
			super( "exportwar" );
		}


		public void actionPerformed( ActionEvent e ) {
			File f = ShowFileChooserDialog( "war", false, warMultiFileFilter, "WarFileSave", true,jTarot.menus.translate( "SaveAFile") );
			if ( f != null && cardlayout != null )
				cardlayout.exportWARfile( f );

		}
	}


	class BrowserOptionsAction extends AbstractAction {
		BrowserOptionsAction() {
			super( "browseroptions" );
		}


		public void actionPerformed( ActionEvent e ) {
			Browser.init();
			Browser.dialogConfiguration( jTarot.ThisjTarot );

		}
	}


	class CreateSetupAction extends AbstractAction {
		CreateSetupAction(String action) {
			super(action );
		}
		public void actionPerformed( ActionEvent e ) {
          new CreateSetupFileDialog();
		}
	}
	class EditSetupAction extends AbstractAction {
		EditSetupAction() {
			super("editsetupfile" );
		}
		public void actionPerformed( ActionEvent e ) {
			String extension = "xml";
			MultiFileFilter filterUsed = xmlMultiFileFilter;

			File f = ShowFileChooserDialog( "xml", false, xmlMultiFileFilter, "LoadSetupAction", false, jTarot.menus.translate( "OpenAFile") );
			if ( f != null  ) {
              StringBuffer errString=new StringBuffer();
              if(! io.LoadSetupFile(f.getAbsolutePath(),false,errString)){
                JOptionPane jOpt = new JOptionPane();
                jOpt.showMessageDialog( jTarot.ThisjTarot, jTarot.menus.translate( "BadFileMessage" ) + " ;\n " + errString.toString() ,
                        jTarot.menus.translate( "Title" ), JOptionPane.INFORMATION_MESSAGE );
              }
              else{
                OracleStandardFileParser parser=null;
                try{
                    parser=new OracleStandardFileParser(new FileInputStream(f));
                }catch(Exception eee){
                   //this would be really strange, it worked just microseconds before!!!!
                   return ;
               }
              JDialog frame = new JDialog( jTarot.ThisjTarot, "Edit tester", true );
              StandardSetupPanel tp = new StandardSetupPanel( parser,frame );
              //Open the window for editing in a modal dialog
              frame.getContentPane().add( tp );
              frame.pack();
              loadGeometry( frame, "editsetupfile" );
              frame.setVisible( true );
              saveGeometry( frame, "editsetupfile" );
              //save the edited values to the parser
              for ( int i = 0; i < tp.attributeKeys.length; i++ )
                parser.putFileAtt(tp.attributeKeys[i].getText(),tp.attributes[i].getText());
              for ( int i = 0; i < tp.keys.length; i++ )
                parser.putValue(tp.keys[i],tp.texts[i].getText());
              try{
                  parser.writeXmlFile(new FileOutputStream(f));
              }catch(Exception eee){
                JOptionPane jOpt = new JOptionPane();
                jOpt.showMessageDialog( jTarot.ThisjTarot, jTarot.menus.translate( "BadFileMessage" ) + " ;\n " + eee.getMessage() ,
                        jTarot.menus.translate( "Title" ), JOptionPane.INFORMATION_MESSAGE );
                 return ;
             }
           }
		 }
       }
	}
	class LoadSetupAction extends AbstractAction {
		LoadSetupAction() {
			super("loadsetupfile" );
		}
		public void actionPerformed( ActionEvent e ) {
			File f = ShowFileChooserDialog( "xml", false, xmlMultiFileFilter, "LoadSetupAction", false, jTarot.menus.translate( "OpenAFile") );
			if ( f != null  ) {
              if (f.getAbsolutePath().endsWith("allinone.xx.html")){
                try{
                  String lineSeperator=System.getProperty("line.separator") ;
                String newFileName=f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-16);
System.err.println("newFileName =" +newFileName );
                InputStreamReader fin = new InputStreamReader( new FileInputStream( f ), Charset.forName( "UTF-8" ) );
                BufferedReader bread=new  BufferedReader(fin);
                OutputStreamWriter owrite=null;
                String row=bread.readLine();
                while (row!=null){
                  if(row.startsWith("<file ")){
                    owrite=new OutputStreamWriter(new FileOutputStream( new File(newFileName + row.substring(12,row.length()-2)) ), Charset.forName( "UTF-8" ) );
                  }else if(row.startsWith("</file")){
                    owrite.close();
                    owrite=null;
                  }else if(owrite!=null && row.trim().length()>0){
                    row=row + lineSeperator;
                    owrite.write(row,0,row.length());
                  }
                  row=bread.readLine();
                }
                bread.close();
                }catch(Exception eeee){
System.err.println("crashed =" +eeee.getMessage());
                  eeee.printStackTrace();
                }
              }
              else{
                StringBuffer errString=new StringBuffer();
                if(! io.LoadSetupFile(f.getAbsolutePath(),true,errString)){
                  JOptionPane jOpt = new JOptionPane();
                  jOpt.showMessageDialog( jTarot.ThisjTarot, jTarot.menus.translate( "BadFileMessage" ) + " ;\n " + errString.toString() ,
                          jTarot.menus.translate( "Title" ), JOptionPane.INFORMATION_MESSAGE );
                }
              }
            }
		}
	}
    private void parseMultiFileFile(File f,String newFileName){
    }


	class EditHelpAction extends AbstractAction {
		private String defaultFile;
		private String cmd;
		EditHelpAction( ) {
			super( "givetranslatehelp" );
			this.cmd = "givetranslatehelp";
			this.defaultFile = "res/en/help.xml";
		}


		public void actionPerformed( ActionEvent e ) {
			String extension = "xml";
			MultiFileFilter filterUsed = xmlMultiFileFilter;

			//open the file from either a File object or a resource stream
			File f = ShowFileChooserDialog( extension, false, filterUsed, "HelpFileSave", false,
					jTarot.menus.translate( "OpenAFile"));
			InputStream in = null;
			InputStream inOrig = null;
			ClassLoader loader = this.getClass().getClassLoader();
			//if a file is requested, then we need to still get the defaults for comparisson
			if ( f == null )
				in = loader.getResourceAsStream( defaultFile );
			inOrig = loader.getResourceAsStream( defaultFile );
			//read the contents of the file into arrays
			ArrayList titles = new ArrayList();
			//read the contents of the file into arrays
			ArrayList texts = new ArrayList();
			//read the contents of the file into arrays
			ArrayList subtopics = new ArrayList();
			StringBuffer lang = new StringBuffer();
			Boolean[] subTopics = null;
			String[] origTitles = null;
			String[] origTexts = null;
			String[] transTitles = null;
			String[] transTexts = null;
			JPanel tp = null;
            //get the original translations for comparison only
            HelpTranslationPanel.OpenJAXPTranslationsWithDOM( null, inOrig, lang, titles, texts, subtopics );
            subTopics = new Boolean[subtopics.size()];
            for ( int i = 0; i < subtopics.size(); i++ )
                subTopics[i] = ( Boolean ) ( subtopics.get( i ) );
            origTitles = new String[titles.size()];
            for ( int i = 0; i < titles.size(); i++ )
                origTitles[i] = ( String ) ( titles.get( i ) );
            origTexts = new String[texts.size()];
            for ( int i = 0; i < texts.size(); i++ )
                origTexts[i] = ( String ) ( texts.get( i ) );
            //now get the texts to translate
            titles = new ArrayList();
            texts = new ArrayList();
            subtopics = new ArrayList();
            HelpTranslationPanel.OpenJAXPTranslationsWithDOM( f, in, lang, titles, texts, subtopics );
            transTitles = new String[titles.size()];
            for ( int i = 0; i < titles.size(); i++ )
                transTitles[i] = ( String ) ( titles.get( i ) );
            transTexts = new String[texts.size()];
            for ( int i = 0; i < titles.size(); i++ )
                transTexts[i] = ( String ) ( texts.get( i ) );
            tp = new HelpTranslationPanel( lang.toString(), origTitles, origTexts, transTitles,
                    transTexts );

			//Open the window for editing in a modal dialog
			JDialog frame = new JDialog( jTarot.ThisjTarot, "Edit tester", true );
			frame.getContentPane().add( tp );
			frame.pack();
			loadGeometry( frame, cmd );
			frame.setVisible( true );
			saveGeometry( frame, cmd );

			//at this point the modal dialog has closed.  Ask for a file to save the edited values to
			File fSave = ShowFileChooserDialog( extension, false, filterUsed, "HelpFileSave", true, jTarot.menus.translate( "SaveAFile") );
			if ( fSave != null ) {
                //save the edited values to the desired file
                for ( int i = 0; i < origTitles.length; i++ )
                    transTitles[i] = ( ( HelpTranslationPanel ) tp ).titles[i].getText();
                for ( int i = 0; i < origTitles.length; i++ )
                    transTexts[i] = ( ( HelpTranslationPanel ) tp ).texts[i].getText();
                HelpTranslationPanel.SaveJAXPTranlsationsWithDOM(
                        fSave, lang.toString(), transTitles, transTexts, subTopics );
				//ask if they want to make this their default file for usage at next restart
				JOptionPane jOpt = new JOptionPane();
				if ( jOpt.showConfirmDialog( jTarot.ThisjTarot,
						menus.translate( "DoYouWantToMakeThisDefault" ),
						menus.translate( "Title" ), JOptionPane.YES_NO_OPTION )
						 == JOptionPane.YES_OPTION )
					io.setProperty( cmd + ".defaultFile", fSave.getAbsolutePath() );
				else
					io.setProperty( cmd + ".defaultFile", "" );
			}
			else {
			}
		}
	}


	public static void ShowHelpWindow( String cmdFileId,  String frameIdentity, String subPosition ) {
		ClassLoader loader =jTarot.ThisjTarot.getClass().getClassLoader();
        String resourceToShow;
		String foundResource = "";
		String defaultLocaleFile = io.getProperty( cmdFileId + ".defaultFile", "" );
		File ff = new File( defaultLocaleFile );
		InputStream in = null;
		try {
			if ( defaultLocaleFile != null && defaultLocaleFile.length() != 0 &&  ff.isFile() && ff.canRead() )
				in = new FileInputStream( ff );
			else {
				if ( loader.getResource( "res/" + io.getProperty("ISO.Language") + "/help.xml" ) != null )
					foundResource ="res/" + io.getProperty("ISO.Language") + "/help.xml";
				else
					foundResource = "res/en/help.xml";
				in = loader.getResourceAsStream( foundResource );
			}
			ArrayList titles = new ArrayList();
			ArrayList texts = new ArrayList();
			ArrayList subtopics = new ArrayList();
			StringBuffer lang = new StringBuffer();
			HelpTranslationPanel.OpenJAXPTranslationsWithDOM( null, in, lang, titles, texts, subtopics );

			//here we will convert the xml to html.  I know this is nuts but my viewer cant show xml as far as I know
			File tmpFile = File.createTempFile( "jTarotXmlToHtml", ".html" );
			tmpFile.deleteOnExit();
			OutputStreamWriter fOut = new OutputStreamWriter( new FileOutputStream( tmpFile ), Charset.forName( "UTF-8" ) );
			String lineSeperator = System.getProperty( "line.separator" );
			String row;
			//print out html headers
			row = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" + lineSeperator;
			fOut.write( row, 0, row.length() );

			row = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"" + 
                io.getProperty("ISO.Language") + "\" lang=\"" + io.getProperty("ISO.Language") + "\"><head>" + lineSeperator;
			fOut.write( row, 0, row.length() );

			row = "<title>jTarot</title>" + lineSeperator;
			fOut.write( row, 0, row.length() );

			row = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /> " + lineSeperator;
			fOut.write( row, 0, row.length() );

			row = "</head><body>"+ lineSeperator;
			fOut.write( row, 0, row.length() );
            
			row = "<h2>" + menus.translate( frameIdentity ) + "    " + 	menus.translate( "Title" ) + "</h2><ul>" + lineSeperator;
			fOut.write( row, 0, row.length() );

			//write the table of contents
			for ( int i = 0; i < titles.size(); i++ )
				//dont show subtopics
				if ( !( ( Boolean ) ( subtopics.get( i ) ) ).booleanValue() ) {
					row = "<li><a href=\"#title" + i + "\">" + ( ( String ) ( titles.get( i ) ) ) + "</a></li>" + lineSeperator;
					fOut.write( row, 0, row.length() );
				}
			row = "</ul>" + lineSeperator;
			fOut.write( row, 0, row.length() );
			//write the topics and texts and subtopics.
			ClassLoader urlLoader = jTarot.ThisjTarot.getClass().getClassLoader();
			ClassLoader classLoader = jTarot.ThisjTarot.getClass().getClassLoader();
			for ( int i = 0; i < titles.size(); i++ ) {
				//do the title again
				if ( ( ( Boolean ) ( subtopics.get( i ) ) ).booleanValue() ) {
					//if it is a subtopic, then indent it
					row = "<dl><dt></dt><dd>" + lineSeperator;
					fOut.write( row, 0, row.length() );
				}
				row = "<table><tr><td><h3><a name=\"title" + i + "\">" + ( ( String ) ( titles.get( i ) ) ) + "</a></h3>" + lineSeperator;
				fOut.write( row, 0, row.length() );
				//the text
				row = ( ( String ) ( texts.get( i ) ) ) + lineSeperator ;
				fOut.write( row, 0, row.length() );
				URL fileLoc = urlLoader.getResource( "res/en/help" + i + ".jpg" );
				if(fileLoc!=null){
					File tempFile = File.createTempFile( "help", ".jpg" );
					tempFile.deleteOnExit();
                    if ( ((OracleLazy8IO)io).getResourceImage( "res/" + io.getProperty("ISO.Language") + "/help" + i + ".jpg" ) != null )
                        foundResource = "res/" + io.getProperty("ISO.Language") + "/help" + i + ".jpg";
                    else
                        foundResource = "res/en/help" + i + ".jpg";
                    
					Image img=((OracleLazy8IO)io).getResourceImage( foundResource);
					copyFile( classLoader.getResourceAsStream( foundResource), tempFile );
					row="</td><td><img src=\"" + tempFile.getAbsolutePath()  + "\" width=\"" +img.getWidth(ThisjTarot)  + "\" height=\"" + img.getHeight(ThisjTarot)  + "\" alt=\"" + ( ( String ) ( titles.get( i ) ) )  + "\" /> " + lineSeperator ;
					fOut.write( row, 0, row.length() );
				}
				row = "</td></tr></table>" + lineSeperator ;
				fOut.write( row, 0, row.length() );
				if ( ( ( Boolean ) ( subtopics.get( i ) ) ).booleanValue() ) {
					//if it is a subtopic, then close the indentation
					row = "</dd></dl>" + lineSeperator;
					fOut.write( row, 0, row.length() );
				}
			}
			row = "</body></html>" + lineSeperator;
			fOut.write( row, 0, row.length() );
			fOut.close();
			Browser.init();
			String urlString = new File( tmpFile.getAbsolutePath() ).toURL().toString();
			if ( subPosition.length() > 0 )
				urlString = urlString + "#" + subPosition;
			Browser.displayURL( urlString );
			//new HelpViewer(tmpFile.toURL().toString(),menus.translate(frameIdentity));
		} catch ( Exception eee ) {
			System.out.println( "Could not show " + cmdFileId + ", error=  " + eee.getMessage() );
			eee.printStackTrace();
		}

	}

    
	class TroubleShootAction extends AbstractAction {
		TroubleShootAction( ) {
			super( "reportbug" );
		}
		public void actionPerformed( ActionEvent e ) {
			final JFrame frame = new JFrame();
			frame.getContentPane().setLayout( new GridLayout(1,1) );
            LogViewer lv=new LogViewer();
			frame.getContentPane().add( lv );
			frame.addWindowListener(
				new WindowAdapter() {
					public void windowClosing( WindowEvent e ) {
                        jTarot.saveGeometry( frame, "Troubleshooter" );
						frame.dispose();
					}
				} );
			frame.pack();
			frame.setSize( 500, 600 );
            jTarot.loadGeometry( frame, "Troubleshooter" );
			frame.setVisible( true );
            jTarot.saveGeometry( frame, "Troubleshooter" );
		}
	}
	class HelpAction extends AbstractAction {
		String frameIdentity, cmdFileId,subPosition;


		HelpAction( String cmd, String frameIdentity, String cmdFileId,String subPosition ) {
			super( cmd );
			this.frameIdentity = frameIdentity;
			this.cmdFileId = cmdFileId;
			this.subPosition=subPosition;
		}


		public void actionPerformed( ActionEvent e ) {
			ShowHelpWindow( cmdFileId,  frameIdentity, subPosition );
		}
	}


	public File ShowFileChooserDialog( String saveSuffix, boolean isDirectoryOnly, MultiFileFilter filter, String savePrefix,
			boolean isSave, String title ) {
		String preMessage = "";
		if ( preMessage.length() > 0 ) {
			JOptionPane jOpt = new JOptionPane();
			jOpt.showMessageDialog( jTarot.ThisjTarot, menus.translate( preMessage ),
					menus.translate( "Title" ), JOptionPane.INFORMATION_MESSAGE );
		}
		Frame frame = getFrame();
		JFileChooser chooser = new JFileChooser();
		if ( title.length() > 0 )
			chooser.setDialogTitle( title );
		if ( isDirectoryOnly )
			chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
		if ( filter != null )
			chooser.addChoosableFileFilter( filter );
		String lastFilePath = io.getProperty( savePrefix + ".LastUsedFilePath", "" );
		if ( lastFilePath.length() != 0 ) {
			File f = new File( lastFilePath );
			if ( f.isDirectory() )
				chooser.setCurrentDirectory( f );
		}
		int ret = -1;
		if ( isSave ) {
			boolean isNoExit = true;
			while ( isNoExit ) {
				ret = chooser.showSaveDialog( frame );
				if ( ret == JFileChooser.APPROVE_OPTION )
					isNoExit = false;
				else {
					JOptionPane jOpt = new JOptionPane();
					if ( jOpt.showConfirmDialog( jTarot.ThisjTarot,
							menus.translate( "DoYouWantToThrowAwayYourChanges" ),
							menus.translate( "Title" ), JOptionPane.YES_NO_OPTION )
							 == JOptionPane.YES_OPTION )
						isNoExit = false;
				}
			}
		}
		else
			ret = chooser.showOpenDialog( frame );

		if ( ret != JFileChooser.APPROVE_OPTION )
			return null;
		File fileRet = chooser.getSelectedFile();
		//save the directory chosen in the dialog for reuse the next time...
		File fParent = fileRet;
		if ( fileRet != null ) {
			if ( !isDirectoryOnly )
				fParent = fileRet.getParentFile();
			io.setProperty( savePrefix + ".LastUsedFilePath", fParent.getAbsolutePath() );
			if ( isSave && !fileRet.isFile() && saveSuffix.length() != 0 && !isDirectoryOnly ) {
				//make the ending correct
				String fName = fileRet.getAbsolutePath();
				if ( !fName.toLowerCase().endsWith( saveSuffix.toLowerCase() ) )
					fileRet = new File( fName + "." + saveSuffix );

			}
		}

		return fileRet;
	}


	public boolean OpenJAXPTransformWithDOM( File f ) {
      
      //This is the old saved reading file format!
		
        textQuestion.setText( "" );
		textTranslation.setText( "" );
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document document = null;
		//factory.setValidating(true);
		//factory.setNamespaceAware(true);
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse( f );
			document.normalizeDocument();
		} catch ( SAXParseException spe ) {
			// Error generated by the parser
			System.out.println( "\n** Parsing error" + ", line " +
					spe.getLineNumber() + ", uri " + spe.getSystemId() );
			System.out.println( "   " + spe.getMessage() );

			// Use the contained exception, if any
			Exception x = spe;

			if ( spe.getException() != null )
				x = spe.getException();

			x.printStackTrace();
			return false;
		} catch ( SAXException sxe ) {
			// Error generated during parsing)
			Exception x = sxe;

			if ( sxe.getException() != null )
				x = sxe.getException();

			x.printStackTrace();
			return false;
		} catch ( ParserConfigurationException pce ) {
			// Parser with specified options can't be built
			pce.printStackTrace();
			return false;
		} catch ( IOException ioe ) {
			// I/O error
			ioe.printStackTrace();
			return false;
		}
		try {
			org.w3c.dom.Node root;
			org.w3c.dom.Node node;
			org.w3c.dom.Node cardnode;
			root = document.getFirstChild();
            Date timestamp=new Date();
			if ( !root.getNodeName().equals( "jTarotSaveFile" ) )
				return false;
			String nodename = "";
			cardlayout.displayReadingDate = "";
			try {
			    textQuestion.setTitle( menus.translate( "screensQuestion" ) + " - " + root.getAttributes().getNamedItem( "ReadingDate" ).getNodeValue() );
				timestamp = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).parse(root.getAttributes().getNamedItem( "ReadingDate" ).getNodeValue());
			} catch ( Exception eeee ) {
              timestamp=new Date();
            }
			String layoutname="";
            String groupname="Tarot Standard 78 Cards";
            int[] chosenObjects=new int[0];
            boolean[] choosenObjectsUpsideDown=new boolean[0];
            for ( int i = 0; i < root.getChildNodes().getLength(); i++ ) {
				node = root.getChildNodes().item( i );
				nodename = node.getNodeName();
				if ( nodename.equals( "UserTranslation" ) )
					textTranslation.setText( node.getTextContent() );
				else if ( nodename.equals( "UserQuestion" ) )
					textQuestion.setText( node.getTextContent() );
				else if ( nodename.equals( "Scale" ) )
					scale = Double.parseDouble( node.getTextContent() );
//				else if ( nodename.equals( "UseUpSideDown" ) )
//					isReverseUsed = Boolean.parseBoolean( node.getTextContent() );
//				else if ( nodename.equals( "imageSubDir" ) )
//					imageSubDir = node.getTextContent();
				else if ( nodename.equals( "Cards" ) ) {
					layoutname= node.getAttributes().getNamedItem( "CardLayout" ).getNodeValue() ;
					boolean isReversed;
					String cardName;
					String fileName;
					int posIndex;
					int cardIndex;
                    int numCards=0;
					for ( int j = 0; j < node.getChildNodes().getLength(); j++ ) {
						cardnode = node.getChildNodes().item( j );
						if ( cardnode.getNodeName().equals( "Card" ) ) {
                          numCards++;
                        }
                    }
                    
                    chosenObjects=new int[numCards];
                    choosenObjectsUpsideDown=new boolean[numCards];
					for ( int j = 0; j < node.getChildNodes().getLength(); j++ ) {
						cardnode = node.getChildNodes().item( j );
						if ( cardnode.getNodeName().equals( "Card" ) ) {
							posIndex = Integer.parseInt( cardnode.getAttributes().getNamedItem( "PosIndex" ).getNodeValue() );
							choosenObjectsUpsideDown[posIndex]=Boolean.parseBoolean( cardnode.getAttributes().getNamedItem( "IsReversed" ).getNodeValue() );
							chosenObjects[posIndex]=Integer.parseInt( cardnode.getAttributes().getNamedItem( "CardIndex" ).getNodeValue() );
                            //unused anyway....  cardName=cardnode.getAttributes().getNamedItem("CardName").getNodeValue();
						}
					}
					//cardlayout.reset();
				}
			}
            SaveableReading savedReading = new SaveableReading( groupname,
              jTarot.oReading.groupdef,layoutname,
              jTarot.oReading.layoutdef,jTarot.oReading.graph,
              jTarot.oReading.graphdef,
              chosenObjects,
              choosenObjectsUpsideDown, textQuestion.getText(  ), textTranslation.getText(  ),timestamp);
            savedReading.copyToOracleReading(oReading);
		} catch ( Exception eee ) {
			System.out.println( "   " + eee.getMessage() );
			eee.printStackTrace();
			return false;
		}
		return true;
	}


	/**
	 * Find the hosting frame, for the file-chooser dialog.
	 *
	 * @return    The frame value
	 */
	protected Frame getFrame() {
		for ( Container p = getParent(); p != null; p = p.getParent() )
			if ( p instanceof Frame )
				return ( Frame ) p;

		return null;
	}


	/**
	 * Create the menubar for the app.  By default this pulls the
	 * definition of the menu from the associated resource file.
	 *
	 * @return    Description of the Return Value
	 */
	protected JMenuBar createMenubar() {
		menuRadioGroups = new Hashtable();
		menuItems = new Hashtable();
		// install the command table
		commands = new Hashtable();
		Action[] actions = getActions();
		for ( int i = 0; i < actions.length; i++ ) {
			Action a = actions[i];
			//commands.put(a.getText(Action.NAME), a);
			commands.put( a.getValue( Action.NAME ), a );
		}
		JMenuItem mi;
		JMenuBar mb = new JMenuBar();

		String[] menuKeys = tokenize( io.getProperty( "menubar" ) );
		for ( int i = 0; i < menuKeys.length; i++ ) {
			JMenu m = createMenu( menuKeys[i] );
			if ( m != null ){
				mb.add( m );
            }

		}
		this.menubar = mb;
		return mb;
	}


	/**
	 * Create a menu for the app.  By default this pulls the
	 * definition of the menu from the associated resource file.
	 *
	 * @param  key  Description of the Parameter
	 * @return      Description of the Return Value
	 */
	protected JMenu createMenu( String key ) {
		String[] itemKeys = tokenize( io.getProperty( key ) );
		JMenu menu = new JMenu( menus.translate( key + "Label" ) );
		menuItems.put( key, menu );
		for ( int i = 0; i < itemKeys.length; i++ )
			if ( itemKeys[i].equals( "-" ) )
				menu.addSeparator();
			else {
				JMenuItem mi = createMenuItem( itemKeys[i] );
                if(mi != null){
                  menu.add( mi );
                }
			}
        if(itemKeys.length==0){
          return (JMenu)(createMenuItem( key ));
        }else
		  return menu;
	}


	/**
	 * This is the hook through which all menu items are
	 * created.  It registers the result with the menuitem
	 * hashtable so that it can be fetched with getMenuItem().
	 *
	 * @param  cmd  Description of the Parameter
	 * @return      Description of the Return Value
	 * @see         #getMenuItem
	 */
	protected JMenuItem createMenuItem( String cmd ) {
		String radioGroupName = io.getProperty( cmd + radiogroupSuffix );
		String isThisACheckbox = io.getProperty( cmd + checkboxSuffix );
		String isThisADynamicGroup = io.getProperty( cmd + dynamicgroupSuffix );
		JMenuItem mi = null;
		if ( radioGroupName.length() != 0 ) {
			//find the group, if not existing, create it.  Add to the group
			ButtonGroup butGroup = null;
			try {
				butGroup = ( ButtonGroup ) menuRadioGroups.get( radioGroupName );
			} catch ( Exception e ) {
				System.out.println( "error=" + e.getMessage() );
				e.printStackTrace();
			}
			if ( butGroup == null ) {
				butGroup = new ButtonGroup();
				menuRadioGroups.put( radioGroupName, butGroup );
			}
			String radioGroupDefault = io.getProperty( cmd + radiogroupdefaultSuffix );
			mi = new JRadioButtonMenuItem( menus.translate( cmd + labelSuffix ) );
			if ( radioGroupDefault.length() != 0)
				( ( JRadioButtonMenuItem ) mi ).setSelected( true );
			butGroup.add( ( JRadioButtonMenuItem ) mi );
		}
        else if ( isThisADynamicGroup.length() != 0){
			mi = new JMenu( menus.translate( cmd + labelSuffix ) );
        }
		else if ( isThisACheckbox.length() != 0 )
			mi = new JCheckBoxMenuItem( menus.translate( cmd + labelSuffix ) );
        else{
			mi = new JMenuItem( menus.translate( cmd + labelSuffix ) );
            }
		String urls = io.getProperty( cmd + imageSuffix );
		if ( urls.length() != 0 && urls.length()>0 ) {
            URL url;
            try{
                url = new URL(io.getProperty( cmd + imageSuffix ));
                mi.setHorizontalTextPosition( JButton.RIGHT );
                mi.setIcon( new ImageIcon( url ) );
            }catch(Exception e){}
		}
		String astr = io.getProperty( cmd + actionSuffix );
		if ( astr.length() != 0 )
			astr = cmd;

		mi.setActionCommand( astr );
		Action a = getAction( astr );
		if ( a != null ) {
			mi.addActionListener( a );
			a.addPropertyChangeListener( createActionChangeListener( mi ) );
			mi.setEnabled( a.isEnabled() );
            if (! a.isEnabled() ){
              return null;  //not enabled means non existing.
            }
            if ( isThisADynamicGroup.length() != 0){
              if(astr.equals("recent"))
                ((RecentAction)a).setListener((JMenu)mi);
              else if(astr.equals("images"))
                ((ImagesAction)a).setListener((JMenu)mi);
              else if(astr.equals("advisors"))
                ((AdvisorAction)a).setListener((JMenu)mi);
              else if(astr.equals("language"))
                ((LanguageAction)a).setListener((JMenu)mi);
            }
		}
		else{
			mi.setEnabled( false );
        }
		menuItems.put( cmd, mi );
		return mi;
	}


	/**
	 * Fetch the menu item that was created for the given
	 * command.
	 *
	 * @param  cmd  Name of the action.
	 * @return      The menuItem value
	 * @returns     item created for the given command or null
	 *  if one wasn't created.
	 */
	protected JMenuItem getMenuItem( String cmd ) {
		return ( JMenuItem ) menuItems.get( cmd );
	}


	protected Action getAction( String cmd ) {
//System.err.println("getAction cmd=" + cmd);
		return ( Action ) commands.get( cmd );
	}


	// Yarked from JMenu, ideally this would be public.

	protected PropertyChangeListener createActionChangeListener( JMenuItem b ) {
		return new ActionChangedListener( b );
	}


	// Yarked from JMenu, ideally this would be public.
	private class ActionChangedListener implements PropertyChangeListener {
		JMenuItem menuItem;


		ActionChangedListener( JMenuItem mi ) {
			super();
			this.menuItem = mi;
		}


		public void propertyChange( PropertyChangeEvent e ) {
			String propertyName = e.getPropertyName();
			if ( e.getPropertyName().equals( Action.NAME ) ) {
				String text = ( String ) e.getNewValue();
				menuItem.setText( text );
			}
			else if ( propertyName.equals( "enabled" ) ) {
				Boolean enabledState = ( Boolean ) e.getNewValue();
				menuItem.setEnabled( enabledState.booleanValue() );
			}
		}
	}


	/**
	 * Take the given string and chop it up into a series
	 * of strings on whitespace boundaries.  This is useful
	 * for trying to get an array of strings out of the
	 * resource file.
	 *
	 * @param  input  Description of the Parameter
	 * @return        Description of the Return Value
	 */
	public static String[] tokenize( String input ) {
		Vector v = new Vector();
		StringTokenizer t = new StringTokenizer( input );
		String cmd[];

		while ( t.hasMoreTokens() )
			v.addElement( t.nextToken() );
		cmd = new String[v.size()];
		for ( int i = 0; i < cmd.length; i++ )
			cmd[i] = ( String ) v.elementAt( i );

		return cmd;
	}


	public void loadLocalProperties() {
				scale = Double.parseDouble( io.getProperty( "scale", "1.0" ) );
				customizedCardsDirectory = io.getProperty( "customizedCardsDirectory", "" );
//				showPositionNumbers = Boolean.parseBoolean( io.getProperty( "showPositionNumbers", "true" ) );
                int numHistory= getIntegerProperty( "fileHistorySize", 0 ) ;
                fileHistory.clear();
                for ( int i = 0; i < numHistory; i++ ){
                  fileHistory.add( io.getProperty( "fileHistory" + i, "" ));
                }
	}

	public static void saveLocalProperties() {
		io.setProperty( "scale", "" + jTarot.scale );
//		io.setProperty( "scaleText", ThisjTarot.scaleText );
//		io.setProperty( "showPositionNumbers", "" + ThisjTarot.showPositionNumbers );

        io.setProperty( "fileHistorySize", "" + fileHistory.size() );
		for ( int i = 0; i < fileHistory.size(); i++ )
          io.setProperty( "fileHistory" + i, "" + fileHistory.get(i) );
        
		saveGeometry( ThisjTarot, "MainWindow" );
          try{
              io.savePersistantSetup();
          }catch(Exception e){
            e.printStackTrace();
            JOptionPane jOpt = new JOptionPane();
            jOpt.showMessageDialog( jTarot.ThisjTarot, "Failed to save the setup ; "+e.getMessage() ,
                    "jTarot", JOptionPane.INFORMATION_MESSAGE );
          }

	}


	/**
	 * Loads a windows's geometry from the properties.
	 * The geometry is loaded from the <code><i>name</i>.x</code>,
	 * <code><i>name</i>.y</code>, <code><i>name</i>.width</code> and
	 * <code><i>name</i>.height</code> properties.
	 *
	 * @param  win   The window
	 * @param  name  The window name
	 */
	public static void loadGeometry( Window win, String name ) {
		int x;
		int y;
		int width;
		int height;

		Dimension size = win.getSize();
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		Rectangle gcbounds = gd.getDefaultConfiguration().getBounds();

		x = gcbounds.x;
		y = gcbounds.y;

		width = getIntegerProperty( name + ".width", size.width );
		height = getIntegerProperty( name + ".height", size.height );

		Component parent = win.getParent();
		if ( parent == null ) {
			x += ( gcbounds.width - width ) / 2;
			y += ( gcbounds.height - height ) / 2;
		}
		else {
			Rectangle bounds = parent.getBounds();
			x += bounds.x + ( bounds.width - width ) / 2;
			y += bounds.y + ( bounds.height - height ) / 2;
		}

		x = getIntegerProperty( name + ".x", x );
		y = getIntegerProperty( name + ".y", y );

		int extState = getIntegerProperty( name + ".extendedState", 0 );
		setWindowBounds( win, x, y, width, height, extState );
	}
	//}}}


	//{{{ setWindowBounds() method
	/**
	 * Gives a window the specified bounds, ensuring it is within the
	 * screen bounds.
	 *
	 * @param  win       The new windowBounds value
	 * @param  x         The new windowBounds value
	 * @param  y         The new windowBounds value
	 * @param  width     The new windowBounds value
	 * @param  height    The new windowBounds value
	 * @param  extState  The new windowBounds value
	 */
	public static void setWindowBounds( Window win, int x, int y,
			int width, int height, int extState ) {
		// Make sure the window is displayed in visible region
		Rectangle osbounds = getScreenBounds( new Rectangle( x, y, width, height ) );

		if ( x < osbounds.x || x + width > osbounds.width ) {
			if ( width > osbounds.width )
				width = osbounds.width;
			x = ( osbounds.width - width ) / 2;
		}
		if ( y < osbounds.y || y + height > osbounds.height ) {
			if ( height >= osbounds.height )
				height = osbounds.height;
			y = ( osbounds.height - height ) / 2;
		}

		Rectangle desired = new Rectangle( x, y, width, height );
		win.setBounds( desired );
	}
	//}}}


	//{{{ saveGeometry() method
	/**
	 * Saves a window's geometry to the properties.
	 * The geometry is saved to the <code><i>name</i>.x</code>,
	 * <code><i>name</i>.y</code>, <code><i>name</i>.width</code> and
	 * <code><i>name</i>.height</code> properties.
	 *
	 * @param  win   The window
	 * @param  name  The window name
	 */
	public static void saveGeometry( Window win, String name ) {
		Rectangle bounds = win.getBounds();
		io.setProperty( name + ".x", "" + bounds.x );
		io.setProperty( name + ".y", "" + bounds.y );
		io.setProperty( name + ".width", "" + bounds.width );
		io.setProperty( name + ".height", "" + bounds.height );
	}
	//}}}


	public final static int getIntegerProperty( String name, int def ) {
		String value = io.getProperty( name );
		if ( value == null )
			return def;
		else
			try {
				return Integer.parseInt( value.trim() );
			} catch ( NumberFormatException nf ) {
				return def;
			}

	}
	//}}}


	/**
	 * Returns the bounds of the (virtual) screen that the window should be in
	 *
	 * @param  window  The bounds of the window to get the screen for
	 * @return         The screenBounds value
	 */
	public final static Rectangle getScreenBounds( Rectangle window ) {
		GraphicsDevice[] gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		Vector intersects = new Vector();

		// Get available screens
		// O(n^3), this is nasty, but since we aren't dealling with
		// many items it should be fine
		for ( int i = 0; i < gd.length; i++ ) {
			GraphicsConfiguration[] gc = gd[i].getConfigurations();
			L2 :
			for ( int j = 0; j < gc.length; j++ )
				// Don't add duplicates
				if ( window.intersects( gc[j].getBounds() ) ) {
					for ( Enumeration e = intersects.elements(); e.hasMoreElements();  ) {
						GraphicsConfiguration gcc = ( GraphicsConfiguration ) e.nextElement();
						if ( gcc.getBounds().equals( gc[j].getBounds() ) )
							continue L2;
					}
					intersects.add( gc[j] );
				}
		}

		GraphicsConfiguration choice = null;
		if ( intersects.size() > 0 )
			// Pick screen with largest intersection
			for ( Enumeration e = intersects.elements(); e.hasMoreElements();  ) {
				GraphicsConfiguration gcc = ( GraphicsConfiguration ) e.nextElement();
				if ( choice == null )
					choice = gcc;
				else {
					Rectangle int1 = choice.getBounds().intersection( window );
					Rectangle int2 = gcc.getBounds().intersection( window );
					int area1 = int1.width * int1.height;
					int area2 = int2.width * int2.height;
					if ( area2 > area1 )
						choice = gcc;
				}
			}

		else
			choice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

		// Make adjustments for some OS's
		int screenX = ( int ) choice.getBounds().x;
		int screenY = ( int ) choice.getBounds().y;
		int screenW = ( int ) choice.getBounds().width;
		int screenH = ( int ) choice.getBounds().height;
		int x;
		int y;
		int w;
		int h;

		String osName = System.getProperty( "os.name" );
		if ( System.getProperty( "mrj.version" ) != null ) {
			x = screenX;
			y = screenY + 22;
			w = screenW;
			h = screenH - y - 4;
			//shadow size
		}
		else if ( osName.indexOf( "Windows 9" ) != -1
				 || osName.indexOf( "Windows M" ) != -1
				 || osName.indexOf( "Windows" ) != -1 ) {
			x = screenX - 4;
			y = screenY - 4;
			w = screenW - 2 * x;
			h = screenH - 2 * y;
		}
		else {
			x = screenX;
			y = screenY;
			w = screenW;
			h = screenH;
		}

		// Yay, we're finally there
		return new Rectangle( x, y, w, h );
	}
	//}}}
    
    public void RemoveFileFromHistory(File f){
      for ( int i = 0; i < fileHistory.size(); i++ ){
          if (fileHistory.get(i).equals(f.getAbsolutePath()))
            fileHistory.remove(i);
      }
    }
    public void SaveToFileHistory(File f){
      RemoveFileFromHistory(f);
      fileHistory.add(0,f.getAbsolutePath());
      if (fileHistory.size()>40)fileHistory.remove(40);
    }
	public static void main( String s[] ) {
      		int level = Log.WARNING;
		Writer stream=null;
        try
        {
            stream = new BufferedWriter(new FileWriter(System.getProperty( "user.home" ) + File.separatorChar + ".jTarotActivity.log"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            stream = null;
        }
		Log.setLogWriter(stream);
		Log.init(true,level);
		jTarot f = new jTarot();
		f.setVisible( true );
	}
}

