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

package jtarot;

import  nu.lazy8.oracle.engine.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.MediaTracker;
import java.awt.Image;
import java.net.URL;
import java.net.URLClassLoader;
import java.awt.Graphics2D;

public class OracleLazy8IO implements OracleIO {
	private Hashtable persistantSetup = new Hashtable();
	private Hashtable persistantStaticSetup = new Hashtable();
	private final static String IO_EXT_PROPERTIES_DIRECTORY = ".jTarot";
	private final static String IO_EXT_PROPERTIES_FILE_NAME = "jTarot.xml";
	private final static String IO_INT_PROPERTIES_FILE_NAME = "OracleIO.properties.xml";
	private final static String IO_INT_STATIC_PROPERTIES_FILE_NAME = "OracleIO.properties.static.xml";
	private final static String IO_SAVED_READINGS_FILE_NAME = "savedreadings";
	private final static String CHARACTER_SET = "UTF-8";
	public final static String RESOURCES_ROOT = "/res/";
    public static final String ISO_LANGUAGE = "ISO.Language";

	public OracleLazy8IO()
			 throws Exception  {
        //make sure the IO_EXT_PROPERTIES_DIRECTORY exists
		File f = new File( System.getProperty( "user.home" ) + File.separator +IO_EXT_PROPERTIES_DIRECTORY );
        if ( f.isFile() && f.canRead() ) {
            //this is wrong, it must be a directory not a file
            f.delete();
            f = new File( System.getProperty( "user.home" ) + File.separator +IO_EXT_PROPERTIES_DIRECTORY );
        }
        if ( ! f.isDirectory() ) {
            f.mkdirs();
        }
        
		//get the names of all the properties and their default values
        OracleStandardFileParser parser=new OracleStandardFileParser(getClass().getResourceAsStream( RESOURCES_ROOT + IO_INT_PROPERTIES_FILE_NAME ));
        String record;
        String key;
        for ( Enumeration e = parser.keyValuePairs.keys(); e.hasMoreElements();  ) {
            key = ( String ) ( e.nextElement() );
            record = ( ( String ) ( parser.keyValuePairs.get( key ) ) );
            persistantSetup.put(key,record);
        }
        
		//get the names of all the STATIC properties and their default values
        parser=new OracleStandardFileParser(getClass().getResourceAsStream( RESOURCES_ROOT + IO_INT_STATIC_PROPERTIES_FILE_NAME ));
        for ( Enumeration e = parser.keyValuePairs.keys(); e.hasMoreElements();  ) {
            key = ( String ) ( e.nextElement() );
            record = ( ( String ) ( parser.keyValuePairs.get( key ) ) );
            persistantStaticSetup.put(key,record);
        }

        //try to load the external properties list which has the updated values (not just default values)
        try{
            parser=new OracleStandardFileParser(getInputStream(System.getProperty("user.home") +File.separator 
                +  IO_EXT_PROPERTIES_DIRECTORY +File.separator +  IO_EXT_PROPERTIES_FILE_NAME));
            for ( Enumeration e = parser.keyValuePairs.keys(); e.hasMoreElements();  ) {
                key = ( String ) ( e.nextElement() );
                record = ( ( String ) ( parser.keyValuePairs.get( key ) ) );
                persistantSetup.put(key,record);
            }
        }catch(Exception eee){
                //this is no problem the file may in fact not exist.
                //here we can do some initialization..
                setProperty("ISO.Language",System.getProperty( "user.language" ));
        }

        //try to open the external files list
        InputStream streamExternalFiles=null;
        try{
            streamExternalFiles=getInputStream(System.getProperty("user.home") +File.separator +  
                IO_EXT_PROPERTIES_DIRECTORY +File.separator + "ExternalFiles.xml");
        }catch(Exception eee){
            //this is completely ok, this file may actually not exist!
        }
         
        OracleFileManager.startXml(this,getClass().getResourceAsStream(RESOURCES_ROOT + "InternalFiles.xml"),streamExternalFiles);
        //make sure the language exists.
        FileProperties[] foundFiles=OracleFileManager.findAllMatches(
          "*","OracleTranslations","",getProperty("ISO.Language"));
        if (foundFiles.length==0)
          setProperty("ISO.Language","en");
	}
    public boolean LoadSetupFile(String filename,boolean doAdd, StringBuffer errorMessage){
        errorMessage.setLength(0);
        File f = new File(filename);
        if(f.isFile() && f.canRead()){
            OracleStandardFileParser parser=null;
            try{
                parser=new OracleStandardFileParser(new FileInputStream(f));
            }catch(Exception eee){
               errorMessage.append(eee.getMessage());
               return false;
           }
           if(parser.getFileAtt("classification").equals("Oracle")){
               //check some of the major things
               if (parser.getFileAtt("groupname").length()==0 || parser.getValue("numberofobjects")==null){
                   errorMessage.append("This does not appear to be a valid 'oracle' file");
                   return false;
               }
           }
           else if (parser.getFileAtt("classification").equals("OracleTranslations")){
               //check some of the major things
               if (parser.getFileAtt("groupname").length()==0 || parser.getValue("displayname")==null
                   || parser.getValue("languagename")==null || parser.getFileAtt("language").length()==0){
                   errorMessage.append("This does not appear to be a valid 'OracleTranslations' file");
                   return false;
               }
                FileProperties[] foundFiles=OracleFileManager.findAllMatches(parser.getFileAtt("groupname"),"Oracle","*","*");
                if (foundFiles==null || foundFiles.length==0)
                {
                   errorMessage.append("The 'Oracle' groupname is invalid");
                   return false;
               }
           }
           else if (parser.getFileAtt("classification").equals("Layout")){
               //check some of the major things
               if (parser.getFileAtt("groupname").length()==0 || parser.getFileAtt("subclass").length()==0 || parser.getValue("numberofobjects")==null){
                   errorMessage.append("This does not appear to be a valid 'Layout' file");
                   return false;
               }
                FileProperties[] foundFiles=OracleFileManager.findAllMatches(parser.getFileAtt("groupname"),"Oracle","*","*");
                if (foundFiles==null || foundFiles.length==0)
                {
                   errorMessage.append("The 'Oracle' groupname is invalid");
                   return false;
               }
           }
           else if (parser.getFileAtt("classification").equals("LayoutTranslations")){
               //check some of the major things
               if (parser.getFileAtt("groupname").length()==0 || parser.getFileAtt("subclass").length()==0 || 
                       parser.getFileAtt("language").length()==0 || parser.getValue("PositionTitle0")==null || 
                       parser.getValue("Title")==null){
                   errorMessage.append("This does not appear to be a valid 'Layout' file");
                   return false;
               }
                FileProperties[] foundFiles=OracleFileManager.findAllMatches(parser.getFileAtt("groupname"),"Layout",parser.getFileAtt("subclass"),"*");
                if (foundFiles==null || foundFiles.length==0)
                {
                   errorMessage.append("The 'Oracle' groupname or 'Layout name' is invalid");
                   return false;
               }
           }
           else if (parser.getFileAtt("classification").equals("GraphicObjects")){
               //check some of the major things
               if (!filename.endsWith("index.xml")){
                   errorMessage.append("The 'GraphicObjects' file must have the name 'index.xml'");
                   return false;
               }
               if (parser.getFileAtt("subclass").length()==0 || parser.getFileAtt("groupname").length()==0 || 
                   parser.getValue("copyright")==null || parser.getValue("imagetype")==null ){
                   errorMessage.append("This does not appear to be a valid 'GraphicObjects' file.\n Groupname,subclass,imagetype or copyright are missing");
                   return false;
               }
                FileProperties[] foundFiles=OracleFileManager.findAllMatches(parser.getFileAtt("groupname"),"Oracle","*","*");
                if (foundFiles==null || foundFiles.length==0)
                {
                   errorMessage.append("The 'Oracle' groupname is invalid");
                   return false;
               }
               int numObjects=0;
               try{
                  OracleStandardFileParser parser2=new OracleStandardFileParser(
                    getInputStream(foundFiles[0].get( FileProperties.F_filename )));
                  numObjects=Integer.parseInt( parser2.getValue( "numberofobjects" ) );
               }catch(Exception eee){
                   errorMessage.append("This does not appear to be a valid 'GraphicObjects' file,group bad," + 
                       foundFiles[0].get( FileProperties.F_filename ) + " ; " + eee.getMessage());
                   return false;
               }
              String outDirectory = filename.substring(0,filename.length()-"/index.xml".length());
              //make sure all the graphic files exist.
              for ( int i = 0; i <= numObjects; i++ ) {
                  if( ! (new File(outDirectory +File.separator  + "r" + i +"." + parser.getValue("imagetype"))).isFile())
                  {
                       errorMessage.append("The file r" + i + "." + parser.getValue("imagetype") + " is not in this directory.  Put it there and try again.");
                       return false;
                  }
              }
              //correct the filename, it must be a directory.
              filename=outDirectory;
           }
           else if (parser.getFileAtt("classification").equals("GraphicObjTranslations")){
               //check some of the major things
               if (parser.getFileAtt("groupname").length()==0 || parser.getFileAtt("subclass").length()==0 || 
                       parser.getFileAtt("language").length()==0 || parser.getValue("ObjectTitle0")==null || 
                       parser.getValue("ObjectMeaning0")==null){
                   errorMessage.append("This does not appear to be a valid 'GraphicObjTranslations' file");
                   return false;
               }
                FileProperties[] foundFiles=OracleFileManager.findAllMatches(parser.getFileAtt("groupname"),"Oracle","*","*");
                if (foundFiles==null || foundFiles.length==0)
                {
                   errorMessage.append("The 'Oracle' groupname or 'GraphicObj name' is invalid");
                   return false;
               }
           }
           else if (parser.getFileAtt("classification").equals("Translations")){
               if (!parser.getFileAtt("subclass").equals("menus") || parser.getFileAtt("language").length()==0){
                   errorMessage.append("This does not appear to be a valid 'menu translations' file");
                   return false;
               }
           }
           else{
                errorMessage.append("The classification is unknown. It must be Oracle.OracleTranslations,Layout,\nLayoutTranslations,GraphicObjects,GraphicObjTranslations or Translations");
                return false;
           }
           //This is a good file, add it to the list and save the list.
           if (doAdd){
               String values[]=new String[FileProperties.F_downloadURL+1];
               values[FileProperties.F_groupname]=parser.getFileAtt("groupname");
               values[FileProperties.F_classification]=parser.getFileAtt("classification");
               values[FileProperties.F_subclass]=parser.getFileAtt("subclass");
               values[FileProperties.F_language]=parser.getFileAtt("language");
               values[FileProperties.F_version]=parser.getFileAtt("version");
               values[FileProperties.F_copyright]="";
               values[FileProperties.F_size]="";
               values[FileProperties.F_filename]=filename;
               values[FileProperties.F_displayname]="";
               values[FileProperties.F_comments]="";
               values[FileProperties.F_downloadablegroup]="";
               values[FileProperties.F_downloadURL]="";
               FileProperties fprops=new FileProperties(values,false);
               OracleFileManager.addExternalFile(fprops);
           }
           return true;
        }
        else{
            errorMessage.append("The file is not readable.");
            return false;
        }
    }
	public void resetSetup(boolean resetSetup,boolean resetFiles,boolean resetSavedReadings){
        if (resetSetup){
            try{
//                RecordStore.deleteRecordStore( IO_INT_PROPERTIES_FILE_NAME );
            }catch(Exception ee){
            }
        }
        if (resetFiles){
            try{
            }catch(Exception ee){
            }
        }
        if (resetSavedReadings){
            try{
            }catch(Exception ee){
            }
        }
    }
	public void savePersistantSetup()throws Exception {
        OracleStandardFileParser parser=new OracleStandardFileParser(persistantSetup,"","","Properties","","","");
        parser.writeXmlFile( new FileOutputStream(new File(System.getProperty("user.home") +File.separator 
                +  IO_EXT_PROPERTIES_DIRECTORY +File.separator +  IO_EXT_PROPERTIES_FILE_NAME )));
        //try to save the external files list
        FileOutputStream streamExternalFiles=null;
        try{
            streamExternalFiles=new FileOutputStream(new File(System.getProperty("user.home") +File.separator +  
                IO_EXT_PROPERTIES_DIRECTORY +File.separator + "ExternalFiles.xml"));
            FileProperties.writeXmlFile((OutputStream)streamExternalFiles,OracleFileManager.externalFiles);
        }catch(Exception eee){
            
        }
	}

	public void setProperty( String key, String value ) {
		persistantSetup.put( key, value );
	}


	public void setProperty( String key, int value ) {
		persistantSetup.put( key, Integer.toString( value ) );
	}


	public void setProperty( String key, boolean value ) {
		persistantSetup.put( key, ( new Boolean( value ) ).toString() );
	}


	public int getIntProperty( String key ) {
		try {
			return Integer.parseInt( getProperty( key ) );
		} catch ( Exception e ) {
			return 0;
		}
	}


	public boolean getBooleanProperty( String key ) {
		String strBool = getProperty( key );
		if ( strBool == null )
			strBool = "false";
		return ( strBool.equals( "true" ) || strBool.equals( "TRUE" ) || strBool.equals( "True" ) );
	}
    public void setBooleanProperty( String key,boolean value )  {
		String strBool = "false";
        if (value)strBool="true";
        setProperty(key,strBool);
	}

	public String getProperty( String key ) {
        String retVal=( String ) ( persistantSetup.get( key ) );
        if (retVal!=null)
            return retVal;
        else{
            retVal=( String ) ( persistantStaticSetup.get( key ) );
            if (retVal!=null)
                return retVal;
            else
                return "";
        }
            
	}
	public String getProperty( String key,String defaultValue ) {
        String retVal=getProperty( key ) ;
        if (retVal!=null)
            return retVal;
        else
		    return defaultValue;
	}


	public SaveableReading[] getReadings() throws Exception {
        return null;
	}


	public void saveReadings( SaveableReading[] saveIt ) throws Exception {
	}


	public Object getImage( String fileName, int imageNumber ) {
		//these image numbers are zero based with -1 being the backside of the card
		Image retImage = null;
		if ( OracleFileManager.isFileInternal( fileName ) )
            retImage = getResourceImage(  "res/" + fileName + "/r" + Integer.toString( imageNumber + 1 ) + "." + jTarot.oReading.graphObjects.getValue("imagetype")  ) ;
		else
        {
            File f = new File(fileName );
            if ( ! f.isDirectory() ) return null;
            f = new File(fileName + "/r" + Integer.toString( imageNumber + 1 ) + "." + jTarot.oReading.graphObjects.getValue("imagetype") );
            if (! f.isFile() || ! f.canRead() )return null;
            retImage = getImage(fileName + "/r" + Integer.toString( imageNumber + 1 ) + "." + jTarot.oReading.graphObjects.getValue("imagetype") );
        }
		return (Object)retImage;
	}
    public BufferedImage getBufferedImage( String fileName, int imageNumber){
		Image img=(Image)getImage( fileName, imageNumber );
       
        BufferedImage bi=null;
        try {
			bi = new BufferedImage( img.getWidth(  jTarot.ThisjTarot ), img.getHeight(  jTarot.ThisjTarot ), BufferedImage.TYPE_INT_RGB );
            Graphics2D big = bi.createGraphics();
            big.drawImage( img, 0, 0, jTarot.ThisjTarot );
		} catch ( Exception e ) {
			System.out.println( "Error reading image " + fileName );
			e.printStackTrace();
		}
        return bi;
    }
	private static Image getImage( String fileName ) {
		Image img = null;
		try {
			img = jTarot.ThisjTarot.getToolkit().createImage( fileName );
		} catch ( Exception ex ) {
			//ex.printStackTrace();
			//System.out.println( "Error loading image " + fileName );
			return null;
		}

		MediaTracker tracker = new MediaTracker( jTarot.ThisjTarot );
		tracker.addImage( img, 0 );
		try {
			tracker.waitForID( 0 );
			if ( tracker.isErrorAny() )
				System.out.println( "Error loading image " + fileName );

		} catch ( Exception ex ) {
			ex.printStackTrace();
			return null;
		}
		return img;
	}


	public static Image getResourceImage( String resName ) {
		Image img = null;
		ClassLoader urlLoader = jTarot.ThisjTarot.getClass().getClassLoader();
		URL fileLoc = urlLoader.getResource( resName );
//System.err.println("getResourceImage resName=" + resName);
        if ( fileLoc == null )
			return null;
//System.err.println("getResourceImage FOUND resName=" + resName);
		img = jTarot.ThisjTarot.getToolkit().createImage( fileLoc );

		MediaTracker tracker = new MediaTracker( jTarot.ThisjTarot );
		tracker.addImage( img, 0 );
		try {
			tracker.waitForID( 0 );
			if ( tracker.isErrorAny() )
				System.out.println( "Error loading image " + resName );

		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
		return img;
	}


	public InputStream getImageFileContentsStream( String fileName )throws Exception{
		InputStream retStream = null;
		if ( OracleFileManager.isFileInternal( fileName ) )
			try {
				retStream = getClass().getResourceAsStream( RESOURCES_ROOT + fileName + "/index.xml" );
			} catch ( Exception ee ) {
				//System.out.println( "res/" + fileName + " is not readable.  " + ee.getMessage() );
				//ee.printStackTrace();
                throw ee;
			}

		else
			//this works only because the contents are in the zero record.
			retStream = getInputStream( fileName  + "/index.xml");

		return retStream;
	}

	public InputStream getInputStream( String fileName ) throws Exception {
		//note that this will work even for the image files xml header because
		//the header is in the files zero record.
		InputStream retStream = null;
		if ( OracleFileManager.isFileInternal( fileName ) )
			try {
				retStream = getClass().getResourceAsStream( RESOURCES_ROOT + fileName );
			} catch ( Exception ee ) {
                throw ee;
//				System.out.println( "res/" + fileName + " is not readable.  " + ee.getMessage() );
//				ee.printStackTrace();
			}

		else
        {
            File f = new File(fileName );
            if (! f.isFile() || ! f.canRead() )throw new Exception(fileName  + " does not exist or not readable");
            try {
                retStream=new FileInputStream( f ) ;
			} catch ( Exception ee ) {
				System.out.println( fileName + " is not readable.  " + ee.getMessage() );
				ee.printStackTrace();
			}
        }

		return retStream;
	}

}


