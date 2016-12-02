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
import java.awt.*;
import java.util.*;
import java.text.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.io.*;

public class CreateSetupFileDialog {
	private static JDialog frame = null;
	CreateSetupFileDialog() {
		Dimension ddm = new Dimension( 700, 500 );
		//Open the window for choosing a layout type in a modal dialog
		frame = new JDialog( jTarot.ThisjTarot, jTarot.ThisjTarot.menus.translate( "createsetupfilesLabel" ), true );
		CreateSetupFilePanel wiz = new CreateSetupFilePanel();
		frame.getContentPane().add( wiz );
		//frame.setPreferredSize(ddm);
		//frame.setMinimumSize(ddm);
		frame.pack();
        jTarot.loadGeometry( frame, "CreateSetupFileDialog" );
		frame.setVisible( true );
        jTarot.saveGeometry( frame,  "CreateSetupFileDialog" );
		frame.dispose();
	}
	private class CreateSetupFilePanel extends JPanel implements ItemListener {
		private JLabel lblImage = null;
		private JEditorPane editor;

        
        private JComboBox chGroup;
        private JComboBox chGroupDef;
        private JComboBox chLayout;
        private JComboBox chLayoutDef;
        private JComboBox chObjects;
        private JComboBox chObjectsDef;
        private JComboBox chScreenLang;
        private boolean isInFillObjects=false;


		CreateSetupFilePanel() {
			setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
			JTextArea jta = new JTextArea( jTarot.ThisjTarot.menus.translate( "ChooseWhichFileToCreate" ) );
			JScrollPane scroller = new JScrollPane( jta );
			scroller.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
			scroller.setMinimumSize( new Dimension( 100, 40 ) );
			scroller.setMaximumSize( new Dimension( 10000, 40 ) );
			scroller.setPreferredSize( new Dimension( 100, 40 ) );
			jta.setWrapStyleWord( true );
			jta.setLineWrap( true );
			jta.setEditable( false );
			lblImage = new JLabel();
			editor = new JEditorPane( "text/html", "" );
			add( scroller );
            
		//add the main choices
		JPanel panel=new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS ) );
		chGroup=new JComboBox();
		chGroup.addItemListener(this);
		chGroup.setBorder( BorderFactory.createTitledBorder( jTarot.menus.translate( "Type of oracle" ) ) );
		panel.add(chGroup);
		final JButton but2 = new JButton( jTarot.ThisjTarot.menus.translate( "Create this file" ) );
		ActionListener action2Listener =
			new ActionListener() {
				public void actionPerformed( ActionEvent e ) {
                  FileProperties[] foundFiles=OracleFileManager.findAllMatches("*","Oracle","*","*");
                  OutputFile(foundFiles[chGroup.getSelectedIndex()].get( FileProperties.F_filename ));
				}
			};
		but2.addActionListener( action2Listener );
		panel.add(but2);
		add(panel);
		
		panel=new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS ) );
		chGroupDef=new JComboBox();
		chGroupDef.addItemListener(this);
		chGroupDef.setBorder( BorderFactory.createTitledBorder( jTarot.menus.translate( "Oracle translations" ) ) );
		panel.add(chGroupDef);
		final JButton but3 = new JButton( jTarot.ThisjTarot.menus.translate( "Create this file" ) );
		ActionListener action3Listener =
			new ActionListener() {
				public void actionPerformed( ActionEvent e ) {
                  FileProperties[] foundFiles=OracleFileManager.findAllMatches((String)(chGroup.getSelectedItem()),"OracleTranslations","*","*");
                  OutputFile(foundFiles[chGroupDef.getSelectedIndex()].get( FileProperties.F_filename ));
				}
			};
		but3.addActionListener( action3Listener );
		panel.add(but3);
		add(panel);
		
		panel=new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS ) );
		chLayout=new JComboBox();
		chLayout.addItemListener(this);
		chLayout.setBorder( BorderFactory.createTitledBorder( jTarot.menus.translate( "Layout" ) ) );
		panel.add(chLayout);
		final JButton but4 = new JButton( jTarot.ThisjTarot.menus.translate( "Create this file" ) );
		ActionListener action4Listener =
			new ActionListener() {
				public void actionPerformed( ActionEvent e ) {
                  FileProperties[] foundFiles=OracleFileManager.findAllMatches((String)(chGroup.getSelectedItem()),"Layout","*","*");
                  OutputFile(foundFiles[chLayout.getSelectedIndex()].get( FileProperties.F_filename ));
				}
			};
		but4.addActionListener( action4Listener );
		panel.add(but4);
		add(panel);
		
		panel=new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS ) );
		chLayoutDef=new JComboBox();
		chLayoutDef.addItemListener(this);
		chLayoutDef.setBorder( BorderFactory.createTitledBorder( jTarot.menus.translate( "Layout translations" ) ) );
		panel.add(chLayoutDef);
		final JButton but5 = new JButton( jTarot.ThisjTarot.menus.translate( "Create this file" ) );
		ActionListener action5Listener =
			new ActionListener() {
				public void actionPerformed( ActionEvent e ) {
                  FileProperties[] foundFiles=OracleFileManager.findAllMatches((String)(chGroup.getSelectedItem()),
                    "LayoutTranslations", (String)(chLayout.getSelectedItem()),"*");
                  OutputFile(foundFiles[chLayoutDef.getSelectedIndex()].get( FileProperties.F_filename ));
				}
			};
		but5.addActionListener( action5Listener );
		panel.add(but5);
		add(panel);
		
		panel=new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS ) );
		chObjects=new JComboBox();
		chObjects.addItemListener(this);
		chObjects.setBorder( BorderFactory.createTitledBorder( jTarot.menus.translate( "Graphical objects" ) ) );
		panel.add(chObjects);
		final JButton but6= new JButton( jTarot.ThisjTarot.menus.translate( "Create this file" ) );
		ActionListener action6Listener =
			new ActionListener() {
				public void actionPerformed( ActionEvent e ) {
                  File f = jTarot.ThisjTarot.ShowFileChooserDialog( "", false, null, "DeckDirectorySave", false,jTarot.menus.translate( "SaveAFile")  );
                  if ( f != null ) {
                    if ( f.isFile() && f.canRead() ) {
                        //this is wrong, it must be a directory not a file
                        f.delete();
                    }
                    if ( ! f.isDirectory() ) {
                        f.mkdirs();
                    }
                    try{
                      FileProperties[] foundFiles=OracleFileManager.findAllMatches("*","Oracle","*","*");
                      OracleStandardFileParser parser=new OracleStandardFileParser(
                        jTarot.io.getInputStream(foundFiles[chGroup.getSelectedIndex()].get( FileProperties.F_filename )));
                      int numObjects=Integer.parseInt( parser.getValue( "numberofobjects" ) );
                      foundFiles=OracleFileManager.findAllMatches((String)(chGroup.getSelectedItem()),
                         "GraphicObjects","*","*");
                      String outDirectory = f.getAbsolutePath();
                      String inDirectory = foundFiles[chObjects.getSelectedIndex()].get( FileProperties.F_filename );
                      //output all the index file as well!!!  create a directory instead
                      jTarot.copyFile( jTarot.ThisjTarot.getClass().getResourceAsStream( 
                        OracleLazy8IO.RESOURCES_ROOT + inDirectory + "/index.xml"), 
                        new File( outDirectory + File.separatorChar +  "index.xml"  ));
                      //output all the graphic files as well!!!  create a directory instead
                      for ( int i = 0; i <= numObjects; i++ ) {
                          jTarot.copyFile( jTarot.ThisjTarot.getClass().getResourceAsStream( 
                            OracleLazy8IO.RESOURCES_ROOT +inDirectory + "/r" + i +"." + jTarot.oReading.graphObjects.getValue("imagetype") ), 
                            new File( outDirectory + File.separatorChar +  "r" + i +"." + jTarot.oReading.graphObjects.getValue("imagetype")  ) );
                      }
                    }catch(Exception ee3){
                      JOptionPane jOpt = new JOptionPane();
                      jOpt.showMessageDialog( jTarot.ThisjTarot, jTarot.menus.translate( "BadFileMessage" ) + " ; " + ee3.getMessage() ,
                              jTarot.menus.translate( "Title" ), JOptionPane.INFORMATION_MESSAGE );
                    }
                }
            }
		};
		but6.addActionListener( action6Listener );
		panel.add(but6);
		add(panel);
		
		panel=new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS ) );
		chObjectsDef=new JComboBox();
		chObjectsDef.addItemListener(this);
		chObjectsDef.setBorder( BorderFactory.createTitledBorder( jTarot.menus.translate( "Advisor" ) ) );
		panel.add(chObjectsDef);
		final JButton but7 = new JButton( jTarot.ThisjTarot.menus.translate( "Create this file" ) );
		ActionListener action7Listener =
			new ActionListener() {
				public void actionPerformed( ActionEvent e ) {
                  FileProperties[] foundFiles=OracleFileManager.findAllMatches((String)(chGroup.getSelectedItem()),
                        "GraphicObjTranslations","*",(String)(chGroupDef.getSelectedItem()));
                  OutputFile(foundFiles[chObjectsDef.getSelectedIndex()].get( FileProperties.F_filename ));
				}
			};
		but7.addActionListener( action7Listener );
		panel.add(but7);
		add(panel);
		
		panel=new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS ) );
		chScreenLang=new JComboBox();
		chScreenLang.addItemListener(this);
		chScreenLang.setBorder( BorderFactory.createTitledBorder( jTarot.menus.translate( "Menu and message translations" ) ) );
		panel.add(chScreenLang);
		final JButton but8 = new JButton( jTarot.ThisjTarot.menus.translate( "Create this file" ) );
		ActionListener action8Listener =
			new ActionListener() {
				public void actionPerformed( ActionEvent e ) {
                  FileProperties[] foundFiles=OracleFileManager.findAllMatches("*",
                     "Translations","menus","*");
                  OutputFile(foundFiles[chScreenLang.getSelectedIndex()].get( FileProperties.F_filename ));
				}
			};
		but8.addActionListener( action8Listener );
		panel.add(but8);
		add(panel);

		final JButton but1 = new JButton( jTarot.ThisjTarot.menus.translate( "exitLabel" ) );
		ActionListener action1Listener =
			new ActionListener() {
				public void actionPerformed( ActionEvent e ) {
					frame.setVisible( false );
				}
			};
		but1.addActionListener( action1Listener );
		add(but1);
		fillObjects();
		}
        public void itemStateChanged(ItemEvent e) {
          fillObjects();
        }
        private void fillObjects(){
            if (isInFillObjects)  return ;
            isInFillObjects=true;
            FileProperties[] foundFiles=OracleFileManager.findAllMatches("*","Oracle","*","*");
            int foundIndex= chGroup.getSelectedIndex();
            chGroup.removeAllItems();
            String defaultString;
            for ( int i = 0; i < foundFiles.length; i++ ){
                chGroup.addItem( foundFiles[i].get( FileProperties.F_groupname ));
            }
            if(foundIndex>=0  && foundIndex<foundFiles.length)
              chGroup.setSelectedIndex(foundIndex);
            String foundFileGroup=foundFiles[chGroup.getSelectedIndex()].get( FileProperties.F_filename );
            
            foundFiles=OracleFileManager.findAllMatches((String)(chGroup.getSelectedItem()),"OracleTranslations","*","*");
            foundIndex= chGroupDef.getSelectedIndex();
            chGroupDef.removeAllItems();
            for ( int i = 0; i < foundFiles.length; i++ ){
                chGroupDef.addItem( foundFiles[i].get( FileProperties.F_language ));
            }
            if(foundIndex>=0  && foundIndex<foundFiles.length)
              chGroupDef.setSelectedIndex(foundIndex);
            String foundFileGroupDef=foundFiles[chGroupDef.getSelectedIndex()].get( FileProperties.F_filename );
                        
            // do the layout
            foundFiles=OracleFileManager.findAllMatches((String)(chGroup.getSelectedItem()),"Layout","*","*");
            foundIndex= chLayout.getSelectedIndex();
            chLayout.removeAllItems();
            for ( int i = 0; i < foundFiles.length; i++ ){
                chLayout.addItem( foundFiles[i].get( FileProperties.F_subclass ));
            }
            if(foundIndex>=0  && foundIndex<foundFiles.length)
              chLayout.setSelectedIndex(foundIndex);

           //get the definition of the layout
            foundFiles=OracleFileManager.findAllMatches((String)(chGroup.getSelectedItem()),
              "LayoutTranslations", (String)(chLayout.getSelectedItem()),"*");
            foundIndex= chLayoutDef.getSelectedIndex();
            chLayoutDef.removeAllItems();
            for ( int i = 0; i < foundFiles.length; i++ ){
                chLayoutDef.addItem( foundFiles[i].get( FileProperties.F_language ));
            }
            if(foundIndex>=0  && foundIndex<foundFiles.length)
              chLayoutDef.setSelectedIndex(foundIndex);    
      
            foundFiles=OracleFileManager.findAllMatches((String)(chGroup.getSelectedItem()),
              "GraphicObjects","*","*");
            foundIndex= chObjects.getSelectedIndex();
            chObjects.removeAllItems();
            for ( int i = 0; i < foundFiles.length; i++ ){
                chObjects.addItem(foundFiles[i].get( FileProperties.F_subclass ));
            }
            if(foundIndex>=0  && foundIndex<foundFiles.length)
              chObjects.setSelectedIndex(foundIndex);
    
    
            foundFiles=OracleFileManager.findAllMatches((String)(chGroup.getSelectedItem()),
              "GraphicObjTranslations","*",(String)(chGroupDef.getSelectedItem()));
            foundIndex= chObjectsDef.getSelectedIndex();
            chObjectsDef.removeAllItems();
            for ( int i = 0; i < foundFiles.length; i++ ){
                chObjectsDef.addItem(foundFiles[i].get( FileProperties.F_subclass ));
            }
            if(foundIndex>=0  && foundIndex<foundFiles.length)
              chObjectsDef.setSelectedIndex(foundIndex);

            foundFiles=OracleFileManager.findAllMatches("*",
              "Translations","menus","*");
            foundIndex= chScreenLang.getSelectedIndex();
            chScreenLang.removeAllItems();
            for ( int i = 0; i < foundFiles.length; i++ ){
                chScreenLang.addItem(foundFiles[i].get( FileProperties.F_language ));
            }
            if(foundIndex>=0  && foundIndex<foundFiles.length)
              chScreenLang.setSelectedIndex(foundIndex);

            isInFillObjects=false;
        }
        private void OutputFile(String filename){
			File f = jTarot.ThisjTarot.ShowFileChooserDialog( "xml", false, jTarot.xmlMultiFileFilter, "CreateSetupFileSave", false, jTarot.menus.translate( "SaveAFile" ) );
			if ( f != null ){
              try{
              jTarot.copyFile( jTarot.io.getInputStream(filename), f);
              }catch(Exception ee){
					JOptionPane jOpt = new JOptionPane();
					jOpt.showMessageDialog( jTarot.ThisjTarot, jTarot.menus.translate( "BadFileMessage" ) + " ; " + ee.getMessage() ,
							jTarot.menus.translate( "Title" ), JOptionPane.INFORMATION_MESSAGE );
              }
            }
        }
    }
}
