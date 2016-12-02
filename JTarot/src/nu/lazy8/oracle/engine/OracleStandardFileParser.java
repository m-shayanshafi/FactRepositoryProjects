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
public class OracleStandardFileParser /*extends MinML2*/ extends DefaultHandler {
    public static String[] attribLabels={"groupname","subclass","classification","language","version","author"};
    public Hashtable keyValuePairs=new Hashtable();
    public Hashtable fileAttrib=new Hashtable();
    private String key;
	private StringBuffer itemString = new StringBuffer();
    private final static String lineSeperator=System.getProperty("line.separator") ;

    public void putFileAtt(String key,String value){
      fileAttrib.put(key,value);
    }
    public String getFileAtt(String key){
        String retVal= (String)(fileAttrib.get(key));
        if(retVal!=null)
          return retVal;
        else
          return "";
    }
      public String translate(String key){
        String retVal= (String)(keyValuePairs.get(key));
        if(retVal!=null)
          return retVal;
        else
          return "BadKey=" + key;
      }
      public String getValue(String key){
        return (String)(keyValuePairs.get(key));
      }
      public String getValue(String key,String defaultString){
        String retVal= (String)(keyValuePairs.get(key));
        if(retVal!=null)
          return retVal;
        else
          return defaultString;
      }
      public void putValue(String key,String value){
          keyValuePairs.put(key,value);
      }

	public OracleStandardFileParser( ) {
    }
	public OracleStandardFileParser(Hashtable keyValuePairs,String groupname,
        String subclass,String classification,String language,
        String version,String author) {
        this.keyValuePairs=keyValuePairs;
        fileAttrib.put("groupname",groupname);
        fileAttrib.put("subclass",subclass);
        fileAttrib.put("classification",classification);
        fileAttrib.put("language",language);
        fileAttrib.put("version",version);
        fileAttrib.put("author",author);
    }
	public OracleStandardFileParser(InputStream inStr ) throws Exception {
        reset();
		try {
            //this is the way to parse with the standard java parser
            SAXParser saxParser;
            //can be errors if the factory does not exist.
            SAXParserFactory factory = SAXParserFactory.newInstance();
            saxParser = factory.newSAXParser();
			saxParser.parse( inStr, this );
            
            //this is how to parse with the MinML2 parser
            //parse(new InputSource( inStr) );
		} catch ( Exception ee ) {
			System.out.println( ee.getMessage() );
			ee.printStackTrace();
			throw ee;
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
         if ( qName.equals( "oracleio" ) ){
             int i=0;
             while(attributes.getValue(i)!=null){
                 fileAttrib.put(attributes.getQName(i),attributes.getValue(i));
                 i++;
             }
         }
         else if (qName.equals( "entry" )){
             key=attributes.getValue("key");
//System.err.println("startElement key=" +key);
         }
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
         if (qName.equals( "entry" )){
             keyValuePairs.put(key.toString(),itemString.toString());
//System.err.println("endElement key=" + key +", value=" +itemString );
	     }
    }
    public void writeXmlFile(OutputStream outStr)throws Exception{
		try {
            OutputStreamWriter out=new OutputStreamWriter(outStr, "UTF-8") ;
			String record;
			String row;
			String key;
            WriteLine(out,"<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            row="<oracleio";
			for ( Enumeration e = fileAttrib.keys(); e.hasMoreElements();  ) {
				key = ( String ) ( e.nextElement() );
				record = ( ( String ) ( fileAttrib.get( key ).toString() ) );
                row=row + " "+ key + "=\"" +  record +  "\""  ;
			}
            row=row + ">";
            WriteLine(out,row);
			for ( Enumeration e = keyValuePairs.keys(); e.hasMoreElements();  ) {
				key = ( String ) ( e.nextElement() );
				record = ( ( String ) ( keyValuePairs.get( key ).toString() ) );
                WriteLine(out,"<entry key=\"" + key  + "\" >" + replaceXmlEscapeCharacters(record) + "</entry>");
			}
            WriteLine(out,"</oracleio>");
			out.close();
		} catch ( Exception mre ) {
			System.err.println( "Cannot save  ; " + mre.getMessage());
			mre.printStackTrace();
            throw mre;
		}
    }
    public static String replaceXmlEscapeCharacters(String toconvert){
        toconvert = toconvert.replace( "&", "&amp;"); //must be replaced first!!!!!!!
        toconvert = toconvert.replace( "<", "&lt;");
        toconvert = toconvert.replace( ">", "&gt;");
        return toconvert.replace( "%", "&#37;");
    }
    public static void WriteLine(OutputStreamWriter out,String txt)throws Exception{
        out.write(txt,0,txt.length());
        out.write(lineSeperator,0,lineSeperator.length());
    }
}

