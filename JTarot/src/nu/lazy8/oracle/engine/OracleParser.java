/*
 *  :tabSize=4:indentSize=4:noTabs=true:
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

 
import java.util.*;
import java.io.*;

//platform dependent
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
// xml
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
// personal sax
//import org.xml.minsax.*;
//import uk.co.wilson.xml.MinML2;

	
/**
 * Parser handler class to parse xml files.  This parser is very limited in its parsing abilities.
 * It does not handle attributes and it can only handle one level of groupings
 *
 * @author     tarot@lazy8.nu
 * @created    den 2 september 2007
 */
 //this is the way to parse with the standard java parser
//class OracleParser extends DefaultHandler {
//
//this is how to parse with the MinML2 parser
public class OracleParser /*extends MinML2*/ extends DefaultHandler {

	private String[] groupName;
	private String[][] itemNames;
	private Hashtable[][] returnedItems;
	private StringBuffer itemString = new StringBuffer();
	private boolean[] isInGroup;
	private int[] itemCounter;

	public static void parseXml(InputStream inStr, String[] groupName, String[][] itemNames, Hashtable[][] returnedItems ) throws Exception {
		OracleParser parser=new OracleParser(groupName,itemNames,returnedItems);
        parser.reset();
		try {
            //this is the way to parse with the standard java parser
            SAXParser saxParser;
            //can be errors if the factory does not exist.
            SAXParserFactory factory = SAXParserFactory.newInstance();
            saxParser = factory.newSAXParser();
			saxParser.parse( inStr, parser );
            
            //this is how to parse with the MinML2 parser
            //parser.parse(new InputSource( inStr) );
		} catch ( Exception ee ) {
			System.out.println( ee.getMessage() );
			ee.printStackTrace();
			throw ee;
		}
	}
    private OracleParser(String[] groupName, String[][] itemNames, Hashtable[][] returnedItems){
		this.groupName = groupName;
		this.itemNames = itemNames;
		this.returnedItems = returnedItems;
		isInGroup = new boolean[this.groupName.length];
		itemCounter = new int[this.groupName.length];
		for ( int i = 0; i < this.groupName.length; i++ ) {
			isInGroup[i] = false;
			itemCounter[i] = 0;
		}
    }

	/**
	 * Reset parser.
	 */
	public  void reset() {
	}


	/**
	 * Decode element and create corresponding objects.
	 *
	 * @param  uri               Description of the Parameter
	 * @param  localName         Description of the Parameter
	 * @param  qName             Description of the Parameter
	 * @param  attributes        Description of the Parameter
	 * @exception  SAXException  Description of the Exception
	 */
	public  void startElement( String uri, String localName, String qName, Attributes attributes )
			 throws SAXException {
		itemString.setLength( 0 );
		for ( int i = 0; i < groupName.length; i++ )
			if ( groupName[i].equals( qName ) )
				isInGroup[i] = true;
//System.err.println("startElement isInGroup=" +qName);

	}


	/**
	 * Handles document character data.
	 *
	 * @param  ch      Description of the Parameter
	 * @param  start   Description of the Parameter
	 * @param  length  Description of the Parameter
	 */
	public  void characters( char[] ch, int start, int length ) {
		itemString.append( new String( ch, start, length ) );
//System.err.println("characters=" + itemString.toString());
	}


	/**
	 * Handles closing tags.
	 *
	 * @param  uri        Description of the Parameter
	 * @param  localName  Description of the Parameter
	 * @param  qName      Description of the Parameter
	 */
	public  void endElement( String uri, String localName, String qName ) {
//System.err.println("endElement " + uri +"," +localName+","+ qName );
		for ( int i = 0; i < groupName.length; i++ )
			if ( isInGroup[i] ) {
//System.err.println("is in group " + groupName[i] );
				if ( groupName[i].equals( qName ) ) {
					itemCounter[i]++;
					for ( int j = 0; j < itemNames[i].length; j++ )
                    if ( returnedItems[i][j].size() < itemCounter[i] ){
//System.err.println("saving NULL = " +new Integer(itemCounter[i]-1) + "," +itemString.toString() );
							returnedItems[i][j].put( new Integer( itemCounter[i] - 1 ), new String() );
                    }

					isInGroup[i] = false;
				}
				else
					for ( int j = 0; j < itemNames[i].length; j++ )
						if ( itemNames[i][j].equals( qName ) ) {
							if ( returnedItems[i][j].size() == itemCounter[i] ){
//System.err.println("saving = " +new Integer(itemCounter[i]) + "," +itemString.toString() );
								returnedItems[i][j].put( new Integer( itemCounter[i] ), itemString.toString() );
                            }

						}

			}
//            itemString.setLength(0);
//System.err.println("exiting end element" );
	}
}

