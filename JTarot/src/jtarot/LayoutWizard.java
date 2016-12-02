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

public class LayoutWizard {
	private JEditorPane editor;
	private static int CANCELLED_WINDOW = 1;
	private static int APPROVED_WINDOW = 2;
	private static JDialog frame = null;
	private static boolean isManualLayout = false;
	private static String cardlayoutclassname = "";
    private static int[] choosenObjects;
    private static boolean[] choosenObjectsUpsideDown;
    private static int choosenObjectsIndex = 0;


	LayoutWizard() {
		final String cmd = "LayoutWizard";
		Dimension ddm = new Dimension( 700, 500 );
		//Open the window for choosing a layout type in a modal dialog
		frame = new JDialog( jTarot.ThisjTarot, jTarot.ThisjTarot.menus.translate( "StartupWizard" ), true );
		ChooseLayoutWizard wiz = new ChooseLayoutWizard();
		frame.getContentPane().add( wiz );
		//frame.setPreferredSize(ddm);
		//frame.setMinimumSize(ddm);
		frame.pack();
		jTarot.loadGeometry( frame, cmd );
		frame.setVisible( true );
		//here we have returned from the modal dialog
		jTarot.saveGeometry( frame, cmd );
		if ( wiz.returnValue == CANCELLED_WINDOW )
			//jTarot.ThisjTarot.ShowMainScreen(false);
			return;
		//Open the window for choosing the cards
        frame.dispose();
		frame = new JDialog( jTarot.ThisjTarot, jTarot.ThisjTarot.menus.translate( "CardSelectionWizard" ), true );
		GetNextCardWizard wiz2 = new GetNextCardWizard(  );
		frame.getContentPane().add( wiz2 );
		frame.pack();
		if ( isManualLayout )
			jTarot.loadGeometry( frame, "ManualCardSelectionWizard" );
		else
			jTarot.loadGeometry( frame, "RandomCardSelectionWizard" );
		jTarot.ThisjTarot.scroller.getViewport().add( "Center", jTarot.cardlayout );
		jTarot.ThisjTarot.ShowMainScreen( true );
		frame.setVisible( true );
		//here we have returned from the modal dialog
		if ( isManualLayout )
			jTarot.saveGeometry( frame, "ManualCardSelectionWizard" );
		else
			jTarot.saveGeometry( frame, "RandomCardSelectionWizard" );
        frame.dispose();
		if ( wiz2.returnValue == CANCELLED_WINDOW ) {
			//clear the window and delete the cardlayout
			jTarot.ThisjTarot.ShowMainScreen( false );
			return;
		}
	}


	private class ChooseLayoutWizard extends JPanel implements ItemListener {
		public int returnValue = CANCELLED_WINDOW;
		private JLabel lblImage = null;
		private JEditorPane editor;
		private FreeTextFrame textQuestion;
		private String dateDisplayString = "";

        private JComboBox chGroup;
        //private JComboBox chGroupDef;
        private JList chLayout;
        //private JComboBox chLayoutDef;
        private JComboBox chObjects;
        private JComboBox chObjectsDef;
        private JComboBox chScreenLang;
        private JComboBox chSubgroup;
        private JComboBox chUseUpsideDown;
        private String[] groupKeys;
        private String[] layoutKeys;
        private boolean isInFillObjects=false;
        private int lastgroupname=-1;

		ChooseLayoutWizard() {
			setLayout( new GridLayout( 1, 1 ) );
			JPanel centerPane = new JPanel();
			centerPane.setLayout( new BoxLayout( centerPane, BoxLayout.Y_AXIS ) );
			JTextArea jta = new JTextArea( jTarot.ThisjTarot.menus.translate( "SelectLayoutAndCardSelection" ) );
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
			centerPane.add( scroller );

			dateDisplayString = DateFormat.getDateTimeInstance( DateFormat.SHORT, DateFormat.SHORT ).format( new Date() );
			textQuestion = new FreeTextFrame( jTarot.menus.translate( "screensQuestion" ) + " - " + dateDisplayString, true );
			textQuestion.setPreferredSize( new Dimension( 10, 60 ) );
			textQuestion.setMinimumSize( new Dimension( 10, 60 ) );

			centerPane.add( Box.createRigidArea( new Dimension( 0, 5 ) ) );
			centerPane.add( textQuestion );

			centerPane.add( Box.createRigidArea( new Dimension( 0, 5 ) ) );
            
            //add the main choices
            chGroup=new JComboBox();
            chGroup.addItemListener(this);
            chGroup.setBorder( BorderFactory.createTitledBorder( jTarot.menus.translate( "Type of oracle" ) ) );
			centerPane.add(chGroup);
			centerPane.add( Box.createRigidArea( new Dimension( 0, 5 ) ) );
           /* chGroupDef=new JComboBox();
            chGroupDef.addItemListener(this);
			centerPane.add(chGroupDef);
			centerPane.add( Box.createRigidArea( new Dimension( 0, 5 ) ) );
            */
            ////
  			chLayout = new JList( );
			ListSelectionListener changeListener =
				new ListSelectionListener() {
					public void valueChanged( ListSelectionEvent e ) {

                        itemStateChanged(null);
					}
				};
			chLayout.addListSelectionListener( changeListener );
			JScrollPane listScroller = new JScrollPane( chLayout );
			//listScroller.setPreferredSize(new Dimension(150, 150));
			JPanel listPane = new JPanel();
			listPane.setLayout( new BoxLayout( listPane, BoxLayout.LINE_AXIS ) );
			listPane.add( listScroller );
			listPane.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
			listPane.add( lblImage );
			listPane.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );

			editor.setEditable( false );

			JScrollPane scroller4 = new JScrollPane( editor );
			scroller4.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
			scroller4.setMinimumSize( new Dimension( 200, 300 ) );
			scroller4.setMaximumSize( new Dimension( 200, 300 ) );
			scroller4.setPreferredSize( new Dimension( 200, 300 ) );
			scroller4.setBorder( BorderFactory.createTitledBorder( "" ) );
			listPane.add( scroller4 );

            listPane.setBorder( BorderFactory.createTitledBorder( jTarot.menus.translate( "Layout" ) ) );
			centerPane.add( listPane );
			centerPane.add( Box.createRigidArea( new Dimension( 0, 10 ) ) );
            /////
            /*chLayoutDef=new JComboBox();
            chLayoutDef.addItemListener(this);
			centerPane.add(chLayoutDef);
			centerPane.add( Box.createRigidArea( new Dimension( 0, 5 ) ) );
            */
            JPanel panelObjectsAndAdvisors=new JPanel();
            panelObjectsAndAdvisors.setLayout( new BoxLayout( panelObjectsAndAdvisors, BoxLayout.X_AXIS ) );
   
            chObjects=new JComboBox();
            chObjects.addItemListener(this);
            chObjects.setBorder( BorderFactory.createTitledBorder( jTarot.menus.translate( "Graphical objects" ) ) );
			panelObjectsAndAdvisors.add(chObjects);
            chObjectsDef=new JComboBox();
            chObjectsDef.addItemListener(this);
            chObjectsDef.setBorder( BorderFactory.createTitledBorder( jTarot.menus.translate( "Advisor" ) ) );
			panelObjectsAndAdvisors.add(chObjectsDef);
			centerPane.add(panelObjectsAndAdvisors);
			centerPane.add( Box.createRigidArea( new Dimension( 0, 5 ) ) );

            JPanel panelSubgroupsReversed=new JPanel();
            panelSubgroupsReversed.setLayout( new BoxLayout( panelSubgroupsReversed, BoxLayout.X_AXIS ) );

            chSubgroup=new JComboBox();
            chSubgroup.addItemListener(this);
            chSubgroup.setBorder( BorderFactory.createTitledBorder( jTarot.menus.translate( "Smaller group of objects" ) ) );
			panelSubgroupsReversed.add(chSubgroup);
            chUseUpsideDown=new JComboBox();
            chUseUpsideDown.addItemListener(this);
            chUseUpsideDown.setBorder( BorderFactory.createTitledBorder( jTarot.menus.translate( "Use reversed objects" ) ) );
			panelSubgroupsReversed.add(chUseUpsideDown);
			centerPane.add(panelSubgroupsReversed);
            
            //fill the oracle group box. 
            isInFillObjects=true;
            int foundIndex= 0;
            FileProperties[] foundFiles=OracleFileManager.findAllMatches("*","OracleTranslations","*",jTarot.io.getProperty("ISO.Language"));
            String defaultString=jTarot.io.getProperty( OracleReading.sDEFAULT_GROUP );
            groupKeys=new String[foundFiles.length];
            for ( int i = 0; i < foundFiles.length; i++ ){
              groupKeys[i]=foundFiles[i].get( FileProperties.F_groupname );
              try{
                OracleStandardFileParser tGroupDef=new OracleStandardFileParser(jTarot.io.getInputStream( foundFiles[i].get( FileProperties.F_filename ) ) );
                chGroup.addItem(tGroupDef.translate("displayname"));
              }catch(Exception e5){
              }
                if(foundFiles[i].get( FileProperties.F_groupname ).equals(defaultString))foundIndex=i;
            }
            chGroup.setSelectedIndex(foundIndex);
            lastgroupname=-1; //this is a flag to load all the other things again.
            isInFillObjects=false;
            
           fillObjects();
            
 			centerPane.add( Box.createRigidArea( new Dimension( 0, 10 ) ) );
			JPanel southButtonPanel = new JPanel();
			southButtonPanel.setLayout( new BoxLayout( southButtonPanel, BoxLayout.LINE_AXIS ) );
			final JButton but1 = new JButton( jTarot.ThisjTarot.menus.translate( "RandomlySelectCards" ) );
			ActionListener action1Listener =
				new ActionListener() {
					public void actionPerformed( ActionEvent e ) {
						returnValue = APPROVED_WINDOW;
						isManualLayout = false;
//						jTarot.cardlayout.setCardLayout( list.getSelectedIndex() );
//						jTarot.cardlayout.displayReadingDate = dateDisplayString;
						jTarot.ThisjTarot.textQuestion.setText( textQuestion.getText() );
						jTarot.ThisjTarot.textQuestion.setTitle( jTarot.menus.translate( "screensQuestion" ) + " - " + dateDisplayString );
						frame.setVisible( false );
					}
				};
			but1.addActionListener( action1Listener );
			southButtonPanel.add( but1 );
			southButtonPanel.add( Box.createHorizontalGlue() );

			final JButton but2 = new JButton( jTarot.ThisjTarot.menus.translate( "ManuallySelectEachCard" ) );
			ActionListener action2Listener =
				new ActionListener() {
					public void actionPerformed( ActionEvent e ) {
						returnValue = APPROVED_WINDOW;
						isManualLayout = true;
//						jTarot.cardlayout.setCardLayout( list.getSelectedIndex() );
//						jTarot.cardlayout.displayReadingDate = dateDisplayString;
						jTarot.ThisjTarot.textQuestion.setText( textQuestion.getText() );
						jTarot.ThisjTarot.textQuestion.setTitle( jTarot.menus.translate( "screensQuestion" ) + " - " + dateDisplayString );
						frame.setVisible( false );
					}
				};
			but2.addActionListener( action2Listener );
			southButtonPanel.add( but2 );
			southButtonPanel.add( Box.createHorizontalGlue() );

			final JButton but3 = new JButton( jTarot.ThisjTarot.menus.translate( "helpLabel" ) );
			ActionListener action3Listener =
				new ActionListener() {
					public void actionPerformed( ActionEvent e ) {
						jTarot.ShowHelpWindow( "givetranslatehelp", "generalhelpLabel", "title9" );
					}
				};
			but3.addActionListener( action3Listener );
			southButtonPanel.add( but3 );
			southButtonPanel.add( Box.createHorizontalGlue() );

			final JButton but4 = new JButton( jTarot.ThisjTarot.menus.translate( "CancelButton" ) );
			ActionListener action4Listener =
				new ActionListener() {
					public void actionPerformed( ActionEvent e ) {
						returnValue = CANCELLED_WINDOW;
						frame.setVisible( false );
					}
				};
			but4.addActionListener( action4Listener );
			southButtonPanel.add( but4 );
			centerPane.add( southButtonPanel );
			add( centerPane );
			setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
            getLayoutImage();
		}
        public void itemStateChanged(ItemEvent e) {
          if (isInFillObjects)  return;
          saveAllSetup();
          fillObjects();
          saveAllSetup();
          getLayoutImage();
        }
        private void getLayoutImage(){
            try{
            jTarot.oReading.Reload( true );
            }catch(Exception e5){}
            jTarot.cardlayout.setCardLayout();
            Image img =jTarot.cardlayout.getLayoutImage();
            if ( img != null )
                lblImage.setIcon( new ImageIcon( img ) );
            else
                lblImage.setIcon( null );
        }
        private void fillObjects(){
            if (isInFillObjects)  return;
            isInFillObjects=true;
            FileProperties[] foundFiles=OracleFileManager.findAllMatches("*","Oracle","*","*");
            int foundIndex= 0;
            String defaultString=jTarot.io.getProperty( OracleReading.sDEFAULT_GROUP );
            for ( int i = 0; i < foundFiles.length; i++ ){
                if(foundFiles[i].get( FileProperties.F_groupname ).equals(defaultString))foundIndex=i;
            }
            String foundFileGroup=foundFiles[foundIndex].get( FileProperties.F_filename );
            if(chGroup.getSelectedIndex() != lastgroupname){
              lastgroupname=chGroup.getSelectedIndex();
              
              // do the layout
              foundFiles=OracleFileManager.findAllMatches(jTarot.io.getProperty( OracleReading.sDEFAULT_GROUP ),"LayoutTranslations","*",jTarot.io.getProperty("ISO.Language"));
              String[] options=new String[foundFiles.length];
              foundIndex= 0;
              defaultString=jTarot.io.getProperty( OracleReading.sDEFAULT_LAYOUT );
              layoutKeys=new String[foundFiles.length];
              for ( int i = 0; i < foundFiles.length; i++ ){
                  try{
                    layoutKeys[i]=foundFiles[i].get( FileProperties.F_subclass );
                    OracleStandardFileParser zGroupDef=new OracleStandardFileParser(jTarot.io.getInputStream( foundFiles[i].get( FileProperties.F_filename ) ) );
                    options[i]=zGroupDef.translate("Title");
                  }catch(Exception e5){
                  }
                  if(foundFiles[i].get( FileProperties.F_subclass ).equals(defaultString))foundIndex=i;
              }
              chLayout.setListData(options);
              chLayout.setSelectedIndex(foundIndex);
              //find the oracle translation
              foundFiles=OracleFileManager.findAllMatches(jTarot.io.getProperty( OracleReading.sDEFAULT_GROUP ),"OracleTranslations","*",jTarot.io.getProperty("ISO.Language"));
              String foundFileGroupDef=foundFiles[0].get( FileProperties.F_filename );
              
              //ok, do the subgroups.
              chSubgroup.removeAllItems();
              OracleStandardFileParser oGroupDef=new OracleStandardFileParser();
              try{
              oGroupDef=new OracleStandardFileParser(jTarot.io.getInputStream( foundFileGroupDef ) );
              }catch(Exception e){
              }
              int numSubgroups= -1;
              for (int i=0;i<6;i++){
                if (oGroupDef.getValue("subgroup" + i).length()>0)
                  numSubgroups=i;
                else
                  break;
              }
              if (numSubgroups>=0){
                chSubgroup.addItem( "");
                int defaultSubgroup= -1;
                if(jTarot.io.getProperty( OracleReading.sSUB_GROUP_USED ).length()>0)              
                  defaultSubgroup=jTarot.io.getIntProperty( OracleReading.sSUB_GROUP_USED );
                if (defaultSubgroup>numSubgroups)defaultSubgroup=-1;
                for (int i=0;i<=numSubgroups;i++)
                   chSubgroup.addItem(oGroupDef.getValue("subgroup" + i));          
                chSubgroup.setSelectedIndex(defaultSubgroup+1);
              }
              //get the upside down...
              chUseUpsideDown.removeAllItems();
              OracleStandardFileParser oGroup=new OracleStandardFileParser();
              try{
              oGroup=new OracleStandardFileParser( jTarot.io.getInputStream( foundFileGroup ));
              }catch(Exception e){
              }
              
              String strBool =oGroup.getValue("AllowUpsidedown");
              if ( strBool == null )
                  strBool = "false";
              if( strBool.equals( "true" ) || strBool.equals( "TRUE" ) || strBool.equals( "True" ) ){
                chUseUpsideDown.addItem(jTarot.menus.translate( "No" ));
                chUseUpsideDown.addItem(jTarot.menus.translate( "Yes" ));
                chUseUpsideDown.setSelectedIndex(jTarot.io.getBooleanProperty( OracleReading.sIS_UPSIDE_DOWN_USED )?1:0);
              }
              //graphic objects
              foundFiles=OracleFileManager.findAllMatches(jTarot.io.getProperty( OracleReading.sDEFAULT_GROUP ),
                "GraphicObjects","*","*");
              chObjects.removeAllItems();
              foundIndex= 0;
              defaultString=jTarot.io.getProperty( OracleReading.sDEFAULT_GRAPH_OBJ );
              for ( int i = 0; i < foundFiles.length; i++ ){
                  chObjects.addItem(foundFiles[i].get( FileProperties.F_subclass ));
                  if(foundFiles[i].get( FileProperties.F_subclass ).equals(defaultString))foundIndex=i;
              }
              chObjects.setSelectedIndex(foundIndex);
      
              //advisors
              foundFiles=OracleFileManager.findAllMatches(jTarot.io.getProperty( OracleReading.sDEFAULT_GROUP ),
                "GraphicObjTranslations","*",jTarot.io.getProperty("ISO.Language"));
              chObjectsDef.removeAllItems();
              foundIndex= 0;
              defaultString=jTarot.io.getProperty( OracleReading.sDEFAULT_GRAPH_OBJ_TRANS );
              for ( int i = 0; i < foundFiles.length; i++ ){
                  chObjectsDef.addItem(foundFiles[i].get( FileProperties.F_subclass ));
                  if(foundFiles[i].get( FileProperties.F_subclass ).equals(defaultString))foundIndex=i;
              }
              chObjectsDef.setSelectedIndex(foundIndex);
            }
            

           //get the definition of the layout
            foundFiles=OracleFileManager.findAllMatches(jTarot.io.getProperty( OracleReading.sDEFAULT_GROUP ),
              "LayoutTranslations",  layoutKeys[chLayout.getSelectedIndex()],"*");
            //chLayoutDef.removeAllItems();
            foundIndex= 0;
            defaultString=jTarot.io.getProperty( OracleLazy8IO.ISO_LANGUAGE );
            for ( int i = 0; i < foundFiles.length; i++ ){
                //chLayoutDef.addItem(foundFiles[i].get( FileProperties.F_language ));
                if(foundFiles[i].get( FileProperties.F_language ).equals(defaultString))foundIndex=i;
            }
            OracleStandardFileParser tGroupDef=new OracleStandardFileParser();
            try{
            tGroupDef=new OracleStandardFileParser(jTarot.io.getInputStream( foundFiles[foundIndex].get( FileProperties.F_filename ) ) );
            editor.setText(tGroupDef.translate("Description"));
            }catch(Exception e5){
            }
    
    
            foundFiles=OracleFileManager.findAllMatches(jTarot.io.getProperty( OracleReading.sDEFAULT_GROUP ),
              "LayoutTranslations", (String)(chLayout.getSelectedValue()),"*");
            //chLayoutDef.removeAllItems();
            foundIndex= 0;
            defaultString=jTarot.io.getProperty( OracleReading.sDEFAULT_LAYOUT_TRANS );
            for ( int i = 0; i < foundFiles.length; i++ ){
                //chLayoutDef.addItem(foundFiles[i].get( FileProperties.F_language ));
                if(foundFiles[i].get( FileProperties.F_language ).equals(defaultString))foundIndex=i;
            }
                
    
            isInFillObjects=false;
        }
        public void saveAllSetup()  {
          //save all the items
          jTarot.io.setProperty(OracleReading.sDEFAULT_GROUP,
            groupKeys[chGroup.getSelectedIndex()]);
          jTarot.io.setProperty(OracleReading.sDEFAULT_GROUP_DEF,
            jTarot.io.getProperty( OracleLazy8IO.ISO_LANGUAGE ) );
          jTarot.io.setProperty(OracleReading.sDEFAULT_LAYOUT,
            layoutKeys[chLayout.getSelectedIndex()]);
          jTarot.io.setProperty(OracleReading.sDEFAULT_LAYOUT_TRANS,
            jTarot.io.getProperty( OracleLazy8IO.ISO_LANGUAGE ) );
          jTarot.io.setProperty(OracleReading.sDEFAULT_GRAPH_OBJ,
            (String)(chObjects.getSelectedItem()) );
          jTarot.io.setProperty(OracleReading.sDEFAULT_GRAPH_OBJ_TRANS,
            (String)(chObjectsDef.getSelectedItem()) );
          if (chSubgroup!=null)
            jTarot.io.setProperty(OracleReading.sSUB_GROUP_USED,
              "" + (chSubgroup.getSelectedIndex()-1) );
          else
            jTarot.io.setProperty(OracleReading.sSUB_GROUP_USED,
              "-1" );
          if (chUseUpsideDown!=null)
            jTarot.io.setProperty(OracleReading.sIS_UPSIDE_DOWN_USED,
              chUseUpsideDown.getSelectedIndex()==0?"false":"true" );
          else
            jTarot.io.setProperty(OracleReading.sIS_UPSIDE_DOWN_USED,
              "false" );
        }
    }

	private class GetNextCardWizard extends JPanel {
		public int returnValue = CANCELLED_WINDOW;
		private JLabel lblImage = null;
		private JLabel selectedImage = null;
		private JEditorPane editor;


		GetNextCardWizard(  ) {
			setLayout( new GridLayout( 1, 1 ) );
            choosenObjects = new int[jTarot.oReading.layout.numberofobjects];
            choosenObjectsUpsideDown = new boolean[jTarot.oReading.layout.numberofobjects];
            for (int i=0;i<choosenObjects.length;i++){
              choosenObjects[i]=-1;
              choosenObjectsUpsideDown[i]=false;
            }
            choosenObjectsIndex = 0;
			JPanel centerPane = new JPanel();
			centerPane.setLayout( new BoxLayout( centerPane, BoxLayout.PAGE_AXIS ) );
			JTextArea jta;
			if ( isManualLayout )
				jta = new JTextArea( jTarot.ThisjTarot.menus.translate( "ManuallyChooseTheCardShownInImageCardAndHitOk" ) );
			else
				jta = new JTextArea( jTarot.ThisjTarot.menus.translate( "RandomlyChooseTheNextCard" ) );
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
			editor.setEditable( false );

			selectedImage = new JLabel();
			centerPane.add( scroller );
			centerPane.add( Box.createRigidArea( new Dimension( 0, 5 ) ) );

			JPanel listPane = new JPanel();
			listPane.setLayout( new BoxLayout( listPane, BoxLayout.LINE_AXIS ) );

			//create the tree of cards
			DefaultMutableTreeNode root = new DefaultMutableTreeNode( "" );
            //count how many groupings there are.
            int numSubgroups= -1;
            for (int i=0;i<6;i++){
              if (jTarot.oReading.oGroupDef.getValue("grouping" + i).length()>0)
                numSubgroups=i;
              else
                break;
            }
           DefaultMutableTreeNode[] nodes;
            if (numSubgroups>=0){
              nodes=new DefaultMutableTreeNode[numSubgroups+1];
              DefaultMutableTreeNode node;
              //found some branches. Make the branches to the tree.
              for (int i=0;i<=numSubgroups;i++){
                node=new DefaultMutableTreeNode(jTarot.oReading.oGroupDef.getValue("grouping" + i));
                nodes[i]=node;
                root.add(node);
                StringTokenizer tokenizer = new StringTokenizer( jTarot.oReading.oGroup.getValue( "grouping" + i ) );
				while ( tokenizer.hasMoreTokens() ) {
                  node.add(new DefaultMutableTreeNode(jTarot.oReading.graphObjTranslations.translate("ObjectTitle"+Integer.parseInt( tokenizer.nextToken() ))));
				}
              }
            }else{
              //just put them all under one branch...
              numSubgroups=jTarot.oReading.getTotalNumberOfObjectsNoSubgroups();
              for (int i=0;i<numSubgroups;i++){
                  root.add(new DefaultMutableTreeNode(jTarot.oReading.graphObjTranslations.translate("ObjectTitle"+i)));
              }
              nodes=new DefaultMutableTreeNode[1];
              nodes[0]=root;
            }
			final JTree list = new JTree( root );
			list.setRootVisible( false );
            for (int i=0;i<nodes.length;i++)
              list.makeVisible( new TreePath( ( ( DefaultMutableTreeNode ) ( nodes[i].getFirstChild() ) ).getPath() ) );
			if ( isManualLayout ) {
				DefaultTreeSelectionModel tselmodel = new DefaultTreeSelectionModel();
				tselmodel.setSelectionMode( tselmodel.SINGLE_TREE_SELECTION );
				list.setSelectionModel( tselmodel );
				MouseListener mouseListener =
					new MouseAdapter() {
						public void mouseClicked( MouseEvent e ) {
							TreePath tPath = list.getSelectionPath();

							if ( tPath != null && tPath.getPathCount() > 2 ) {
								Object[] paths = tPath.getPath();
								int i;
                                int totalObjects=jTarot.oReading.getTotalNumberOfObjectsNoSubgroups();
								for ( i = 0; i < totalObjects; i++ )
									if ( jTarot.oReading.graphObjTranslations.translate( "ObjectTitle" + i ).equals( paths[2].toString() ) )
										break;
								Image img = (Image)(jTarot.io.getImage(jTarot.oReading.fileNames[OracleFileManager.F_graphicalobj], i ));
								if ( img == null )
									img = (Image)(jTarot.io.getImage(jTarot.oReading.fileNames[OracleFileManager.F_graphicalobj], -1 ));
								if ( img != null )
									selectedImage.setIcon( new ImageIcon( img ) );
								else
									selectedImage.setIcon( null );
							}
							else {
								Image imgg = (Image)(jTarot.io.getImage(jTarot.oReading.fileNames[OracleFileManager.F_graphicalobj], -1 ));
								if ( imgg != null )
									selectedImage.setIcon( new ImageIcon( imgg ) );
								else
									selectedImage.setIcon( null );
							}
						}
					};
				mouseListener.mouseClicked( null );
				list.addMouseListener( mouseListener );
				JScrollPane listScroller = new JScrollPane( list );
				//listScroller.setPreferredSize(new Dimension(150, 150));
				listPane.add( listScroller );
				listPane.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
				listPane.add( selectedImage );
			}
			listPane.add( lblImage );
			listPane.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
            try{
            jTarot.oReading.Reload( true );
            }catch(Exception e5){}
            jTarot.oReading.resetReading();
            jTarot.cardlayout.setCardLayout();
            Image img =jTarot.cardlayout.getLayoutImage();
            if ( img != null )
                lblImage.setIcon( new ImageIcon( img ) );
            else
                lblImage.setIcon( null );

			JScrollPane scroller4 = new JScrollPane( editor );
			scroller4.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
			scroller4.setMinimumSize( new Dimension( 300, 300 ) );
			scroller4.setMaximumSize( new Dimension( 300, 300 ) );
			scroller4.setPreferredSize( new Dimension( 300, 300 ) );
			scroller4.setBorder( BorderFactory.createTitledBorder( "" ) );
			listPane.add( scroller4 );

			centerPane.add( listPane );
			centerPane.add( Box.createRigidArea( new Dimension( 0, 10 ) ) );
			final JCheckBox jcb = new JCheckBox( jTarot.ThisjTarot.menus.translate( "cardisreversed" ) );
			if ( jTarot.io.getBooleanProperty( "UseUpSideDown" ) && isManualLayout ) {
				JPanel reverseCardPanel = new JPanel();
				reverseCardPanel.setLayout( new BoxLayout( reverseCardPanel, BoxLayout.LINE_AXIS ) );
				reverseCardPanel.add( Box.createHorizontalGlue() );
				jcb.setSelected( false );
				reverseCardPanel.add( jcb );
				reverseCardPanel.add( Box.createHorizontalGlue() );
				centerPane.add( reverseCardPanel );
				centerPane.add( Box.createRigidArea( new Dimension( 0, 10 ) ) );
			}

			JPanel southButtonPanel = new JPanel();
			southButtonPanel.setLayout( new BoxLayout( southButtonPanel, BoxLayout.LINE_AXIS ) );
			final JButton but1 = new JButton( jTarot.ThisjTarot.menus.translate( "NextButton" ) );
			ActionListener action1Listener =
				new ActionListener() {
					public void actionPerformed( ActionEvent e ) {
						if ( isManualLayout ) {
							TreePath tPath = list.getSelectionPath();

							if ( tPath != null && tPath.getPathCount() > 2 ) {
								Object[] paths = tPath.getPath();
								int i;
                                int numObjects=jTarot.oReading.getTotalNumberOfObjectsNoSubgroups();
                                for (i=0;i<numObjects;i++){
									if ( jTarot.oReading.graphObjTranslations.translate("ObjectTitle"+i).equals( paths[2].toString() ) )
										break;
                                }
                                choosenObjects[choosenObjectsIndex]=i;
                                choosenObjectsUpsideDown[choosenObjectsIndex]=jcb.isSelected();
                                jTarot.oReading.SetReading(choosenObjects,choosenObjectsUpsideDown,new Date());
                                jTarot.cardlayout.setCardLayout();
                                choosenObjectsIndex++;
                                if (  jTarot.oReading.layout.numberofobjects-choosenObjectsIndex <= 0) {
                                    returnValue = APPROVED_WINDOW;
                                    frame.setVisible( false );
                                    return;
                                }
								jcb.setSelected( false );
							}
							else {
								JOptionPane.showMessageDialog( frame, jTarot.menus.translate( "YouMustFirstSelectAValidCard" ),
										jTarot.menus.translate( "Title" ), JOptionPane.ERROR_MESSAGE );
								return;
							}
						}
//System.out.println("getting image=" + "layout/" + cardlayoutclassname + nowGettingCardIndexNumber + "." + jTarot.oReading.graphObjects.getValue("imagetype"));
						/*
						 *  Image img=((OracleLazy8IO)jTarot.io).getResourceImage("layout/CardLayout" + jTarot.cardlayout.selectedLayoutIndex + "_" + (nowGettingCardIndexNumber+1) + "." + jTarot.oReading.graphObjects.getValue("imagetype"));
						 *  if(img==null)img=((OracleLazy8IO)jTarot.io).getResourceImage("rider/r0.jpg");
						 *  if(img!=null)
						 *  lblImage.setIcon(new ImageIcon(img));
						 *  else
						 *  lblImage.setIcon(null);
						 */
						editor.setText( jTarot.menus.translate( "theposition" ) + " : " + ( choosenObjectsIndex + 1 ) + "<h2>" +
								jTarot.oReading.layoutTranslations.translate( "PositionTitle" + choosenObjectsIndex)
								 + "</h2>" + jTarot.oReading.layoutTranslations.translate( "Meaning" + choosenObjectsIndex ) );
					}
				};
			but1.addActionListener( action1Listener );
			if ( isManualLayout ) {
				southButtonPanel.add( but1 );
				southButtonPanel.add( Box.createHorizontalGlue() );
			}

			final JButton but3 = new JButton( jTarot.ThisjTarot.menus.translate( "helpLabel" ) );
			ActionListener action3Listener =
				new ActionListener() {
					public void actionPerformed( ActionEvent e ) {
                      if ( isManualLayout )
						jTarot.ShowHelpWindow( "givetranslatehelp", "generalhelpLabel", "title14" );
                      else
						jTarot.ShowHelpWindow( "givetranslatehelp", "generalhelpLabel", "title15" );
					}
				};
			but3.addActionListener( action3Listener );
			southButtonPanel.add( but3 );
			southButtonPanel.add( Box.createHorizontalGlue() );

			final JButton but4 = new JButton( jTarot.ThisjTarot.menus.translate( "CancelButton" ) );
			ActionListener action4Listener =
				new ActionListener() {
					public void actionPerformed( ActionEvent e ) {
						returnValue = CANCELLED_WINDOW;
						frame.setVisible( false );
					}
				};
			but4.addActionListener( action4Listener );
			southButtonPanel.add( but4 );

			editor.setText( jTarot.menus.translate( "theposition" ) + " : 1<h2>" +
					jTarot.oReading.layoutTranslations.translate( "PositionTitle" + 0 )
					 + "</h2>" + jTarot.oReading.layoutTranslations.translate( "Meaning" + 0 ) );
			JPanel southPanel = new JPanel();
			southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.PAGE_AXIS ) );
			southPanel.add(
				new CardRandomizerPane(  ) {
					void cardPicked( int cardIndex, boolean isReversed ) {
                      choosenObjects[choosenObjectsIndex]=cardIndex;
                      choosenObjectsUpsideDown[choosenObjectsIndex]=isReversed && jTarot.io.getBooleanProperty( OracleReading.sIS_UPSIDE_DOWN_USED );
                      jTarot.oReading.SetReading(choosenObjects,choosenObjectsUpsideDown,new Date());
                      jTarot.cardlayout.setCardLayout();
                      choosenObjectsIndex++;
						if (  jTarot.oReading.layout.numberofobjects-choosenObjectsIndex <= 0) {
							returnValue = APPROVED_WINDOW;
							frame.setVisible( false );
							return;
						}
						/*
						 *  Image img=((OracleLazy8IO)jTarot.io).getResourceImage("layout/CardLayout" + jTarot.cardlayout.selectedLayoutIndex
						 *  + "_" + (jTarot.cardlayout.numTurnedOverCards+1) + "." + jTarot.oReading.graphObjects.getValue("imagetype"));
						 *  if(img==null)img=((OracleLazy8IO)jTarot.io).getResourceImage("rider/r0.jpg");
						 *  if(img!=null)
						 *  lblImage.setIcon(new ImageIcon(img));
						 *  else
						 *  lblImage.setIcon(null);
						 */
						editor.setText( jTarot.menus.translate( "theposition" ) + " : " + ( choosenObjectsIndex + 1 ) + "<h2>" +
								jTarot.oReading.layoutTranslations.translate( "PositionTitle" + choosenObjectsIndex)
								 + "</h2>" + jTarot.oReading.layoutTranslations.translate( "Meaning" + choosenObjectsIndex ) );
					}
				} );
			southPanel.add( Box.createRigidArea( new Dimension( 0, 10 ) ) );
			southPanel.add( southButtonPanel );
			if ( isManualLayout )
				centerPane.add( southButtonPanel );
			else
				centerPane.add( southPanel );
			add( centerPane );
			setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
		}
	}
}

