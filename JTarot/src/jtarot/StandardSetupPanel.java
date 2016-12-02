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
import  nu.lazy8.oracle.engine.*;

public class StandardSetupPanel extends JPanel implements ActionListener {
	private TransCell[] editCells;
	public JTextArea[] texts;
	public JLabel[] attributeKeys;
	public JTextArea[] attributes;
	public String[] keys;
    public JDialog frame=null;

	public StandardSetupPanel( OracleStandardFileParser parserTranslate, JDialog frame) {
		super( true );
        this.frame=frame;
        JPanel attributePanel=new JPanel();
		attributePanel.setLayout( new GridLayout(parserTranslate.fileAttrib.size(),2) );
		setLayout( new BorderLayout() );
        attributes=new JTextArea[parserTranslate.fileAttrib.size()];
        attributeKeys=new JLabel[parserTranslate.fileAttrib.size()];
        
        String key;
        String record;
        int i=0;
        for ( Enumeration e = parserTranslate.fileAttrib.keys(); e.hasMoreElements();  ) {
            key = ( String ) ( e.nextElement() );
            record = ( ( String ) ( parserTranslate.fileAttrib.get( key ).toString() ) );
          attributeKeys[i]=new JLabel(key);
          attributePanel.add(attributeKeys[i]);
          attributes[i]=new  JTextArea(record);
          attributePanel.add(attributes[i]);
          if(key.equals("classification")){
            attributeKeys[i].setEnabled(false);
            attributes[i].setEnabled(false);
          }
          i++;
        }
		add( attributePanel,BorderLayout.NORTH );

		//create the translation panel
		JPanel dataEntryPane = new JPanel();
		dataEntryPane.setBorder( BorderFactory.createTitledBorder( "Entries" ) );
		JScrollPane scroller1 = new JScrollPane( dataEntryPane );
		scroller1.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );

		add( scroller1,BorderLayout.CENTER );

		editCells = new TransCell[parserTranslate.keyValuePairs.size()];
		texts = new JTextArea[editCells.length];
		keys = new String[editCells.length];
		dataEntryPane.setLayout( new GridLayout( editCells.length, 1 ) );
        i=0;
        TreeMap sortedMap=new TreeMap(new ComparatorWithDigits(
          parserTranslate.getFileAtt("classification").equals("GraphicObjTranslations")));
        
        sortedMap.putAll(parserTranslate.keyValuePairs) ;
        for (Iterator iterator = sortedMap.entrySet (  ).iterator (  ); iterator.hasNext (  );  ) {
          Map.Entry entry =  ( Map.Entry ) iterator.next (  ) ; 
            key = ( String ) (entry.getKey (  ));
            record = ( String )(entry.getValue(  ));
			texts[i] = new JTextArea( record);
			texts[i].setWrapStyleWord( true );
			texts[i].setLineWrap( true );
            keys[i]=key;
			editCells[i] = new TransCell( key, texts[i] );
			dataEntryPane.add( editCells[i] );
            i++;
		}
        JPanel butPanel=new JPanel();
        butPanel.setLayout(new BoxLayout(butPanel, BoxLayout.X_AXIS));
        JButton but1 = new JButton( jTarot.ThisjTarot.menus.translate( "exitLabel" ) );
		butPanel.add( Box.createHorizontalGlue( ) );
        butPanel.add(but1);
		butPanel.add( Box.createHorizontalGlue( ) );
        but1.addActionListener( this );
        add( butPanel,BorderLayout.SOUTH );
	  }
      public void actionPerformed( ActionEvent e ) {
          frame.setVisible( false );
      }

      class ComparatorWithDigits implements Comparator  {  
        //this compares two Strings. If the strings end with a number, then the 
        //numbers are compared instead of the strings
        private boolean isAdvisorSetup=false;
        public ComparatorWithDigits(boolean isAdvisorSetup ){
          this.isAdvisorSetup=isAdvisorSetup;
        }
       public int compare ( Object o1, Object o2 )   {  
         String s1 =  ( String ) o1; 
         String s2 =  ( String ) o2; 
         StringBuffer sb1=new StringBuffer();
         StringBuffer sb2=new StringBuffer();
         String s1NoDigits =  parseOutDigits(s1,sb1);
         String s2NoDigits =  parseOutDigits(s2,sb2);
         if(isAdvisorSetup){
           //in this case, we want the order to be ObjectTitle1 ObjectMeaning1 ReversedMeaning1 ObjectTitle2.....
           int digitCompare=0;
           try{
           digitCompare = Integer.decode(sb1.toString()).compareTo(Integer.decode(sb2.toString()));
           }catch(Exception e){return sb1.length()>0?-1:1;}
           if (s1NoDigits.equals(s2NoDigits)){
             return digitCompare;
           }else if(s1NoDigits.equals("ObjectTitle")){
             return digitCompare==0?-1:digitCompare;
           }
           else if(s2NoDigits.equals("ObjectTitle")){
             return digitCompare==0?1:digitCompare;
           }
           else if(s1NoDigits.equals("ObjectMeaning")){
             return digitCompare==0?-1:digitCompare;
           }
           else if(s2NoDigits.equals("ObjectMeaning")){
             return digitCompare==0?1:digitCompare;
           }
           else if(s1NoDigits.equals("ReversedMeaning")){
             return digitCompare==0?-1:digitCompare;
           }
           else if(s2NoDigits.equals("ReversedMeaning")){
             return digitCompare==0?1:digitCompare;
           }else
             return s1.compareTo(s2);
         }else{
           //this is just the normal compare, If digits and otherwise equal, compare the digits.
           if (s1NoDigits.equals(s2NoDigits)){
             try{
             return Integer.decode(sb1.toString()).compareTo(Integer.decode(sb2.toString()));
             }catch(Exception e){return s1.compareTo(s2);}
           }else
           return s1.compareTo(s2);
         }
        }
        private String parseOutDigits(String s1,StringBuffer value){
          if(s1.matches(".*\\d\\d\\d")){
            value.append(s1.substring(s1.length()-3,s1.length()));
            return s1.substring(0,s1.length()-3);
          }else if(s1.matches(".*\\d\\d")){
            value.append(s1.substring(s1.length()-2,s1.length()));
            return s1.substring(0,s1.length()-2);
          }else if(s1.matches(".*\\d")){
            value.append(s1.substring(s1.length()-1,s1.length()));
            return s1.substring(0,s1.length()-1);
          }
          return s1;
        }
       public boolean equals ( Object o )   {  
         String s =  ( String ) o; 
         return compare ( this, o ) ==0; 
        }  
      }  
	class TransCell extends JPanel {
		public JTextArea transText;


		TransCell( String origText, JTextArea transText ) {
			this.transText = transText;

			setBorder( BorderFactory.createTitledBorder( origText ) );
			setLayout( new GridLayout( 1, 1 ) );

			setMinimumSize( new Dimension( 300, 100 ) );

			setMaximumSize( new Dimension( 300, 100 ) );

			setPreferredSize( new Dimension( 300, 100 ) );

            JScrollPane scroller2 = new JScrollPane( transText );
            scroller2.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
            scroller2.setMinimumSize( new Dimension( 300, 100 ) );
            scroller2.setMaximumSize( new Dimension( 300, 100 ) );
			add( scroller2 );
		}
	}
}

