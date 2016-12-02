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

import java.io.*;
import java.util.*;

public class OracleFileManager {
	private static FileProperties[] internalFiles = null;
	public static FileProperties[] externalFiles = new FileProperties[0] ;
	private static FileProperties[] downloadedFiles = null;
	private static Hashtable isInternal = new Hashtable();
	public final static int F_group = 0;
	public final static int F_groupdef = 1;
	public final static int F_layout = 2;
	public final static int F_layoutdef = 3;
	public final static int F_graphicalobj = 4;
	public final static int F_graphicaldef = 5;


	OracleFileManager() { }
    
    public static void addExternalFile(FileProperties fprops){
      FileProperties[] externalFilesNew=new FileProperties[externalFiles.length+1];
      //check if this is a repeated file
      for(int i=0;i<externalFiles.length;i++)
        if(externalFiles[i].get(FileProperties.F_filename).equals(fprops.get(FileProperties.F_filename))){
          //this is repeated.  Replace with the new one.
          externalFiles[i]=fprops;
          return;
        }
      //add to the list.
      for(int i=0;i<externalFiles.length;i++)
        externalFilesNew[i]=externalFiles[i];
      externalFilesNew[externalFiles.length]=fprops;
      externalFiles=externalFilesNew;
    }
	public static void startXml( OracleIO io,InputStream internalFilesStream, InputStream externalFilesStream ) throws Exception {
		internalFiles = FileProperties.readXmlFile( internalFilesStream, true );
		for ( int i = 0; i < internalFiles.length; i++ )
			isInternal.put( internalFiles[i].get( FileProperties.F_filename ), new String() );
        if (externalFilesStream!=null){
          externalFiles = FileProperties.readXmlFile( externalFilesStream, false );
          //make sure all the files are valid.
          StringBuffer errbuf=new StringBuffer();
          String filename="";
          for ( int i = 0; i < externalFiles.length; i++ ){
            //GraphicObjects must have /index.xml added to the filename.
            filename=externalFiles[i].get(FileProperties.F_filename);
            if(externalFiles[i].get(FileProperties.F_classification).equals("GraphicObjects"))
              filename+=File.separator + "index.xml";
//System.err.println("file test=" + filename);
            if(!io.LoadSetupFile(filename,false,errbuf)){
                //bad file, remove it from the list.
//System.err.println("file bad=" + filename + "  ;  " + errbuf);
                FileProperties[] externalFilesNew=new FileProperties[externalFiles.length-1];
                int counter=0;
                for(int j=0;j<externalFiles.length;j++){
                  if(j!=i)
                      externalFilesNew[counter++]=externalFiles[j];
                }
                externalFiles=externalFilesNew;
                i-=1;//must correct the index since we removed this item.
            }
          }
        }
	}


	public static void startSerial( InputStream internalFilesStream, InputStream externalFilesStream ) throws Exception {
		//the internal file is always xml..  Never serial
		internalFiles = FileProperties.readXmlFile(  internalFilesStream , true );
        if (externalFilesStream!=null)
          externalFiles = FileProperties.readSerialFile( new DataInputStream( externalFilesStream ), false );
          for ( int i = 0; i < internalFiles.length; i++ ){
			isInternal.put( internalFiles[i].get( FileProperties.F_filename ), new String() );
          }
	}


	public static void loadDownloadableFile( InputStream inStr ) {
	}


	public static boolean isFileInternal( String filename ) {
		return isInternal.containsKey( filename );
	}


	public static String[] getReadingFileNames( String group,
			String groupdef,
			String layout,
			String layoutdef,
			String graphicalobj,
			String graphicalobjdef ) {
		String[] retValue = new String[F_graphicaldef + 1];
		//search for each item

//System.err.println( "0 " + group);

		//get the group .  This must be an exact hit!
		FileProperties foundFile = searchField( group, "Oracle", "*", "*" );
		if ( foundFile == null )
			return null;
		//must exit cannot continue without a valid group def
		else
			retValue[F_group] = foundFile.get( FileProperties.F_filename );
//System.err.println( "1");

        //the layout might be the SHOW ALL layout which exists in the group file
        if (layout.equals(group)){
          retValue[F_layout] = retValue[F_group];
        }else{
           //get the layout .  This must be an exact hit!
            foundFile = searchField( group, "Layout", layout, "" );
            if ( foundFile == null ){
    //System.err.println( "cant find layout=" + layout);
                return null;
            }
            //must exit cannot continue without a valid group def
            else
                retValue[F_layout] = foundFile.get( FileProperties.F_filename );
        }
//System.err.println( "3");
		//get the groupdef, if not found, get simply the first found group def.
		foundFile = searchField( group, "OracleTranslations", "", groupdef );
		if ( foundFile == null ) {
			foundFile = searchField( group, "OracleTranslations", "*", "*" );
			if ( foundFile == null )
				return null;
			//must exit cannot continue without a valid group def
			else
				retValue[F_groupdef] = foundFile.get( FileProperties.F_filename );
		}
		else
			retValue[F_groupdef] = foundFile.get( FileProperties.F_filename );

//System.err.println( "4");
        //the layout might be the SHOW ALL layout which exists in the group file
        if (layout.equals(group)){
            //put any file here.  It does not matter.  We dont want any layout defs anyway...
          retValue[F_layoutdef] = retValue[F_group];
        }else{
            //get the layoutdef, if not found, get simply the first found layoutdef.
            foundFile = searchField( group, "LayoutTranslations", layout, layoutdef );
            if ( foundFile == null ) {
                foundFile = searchField( group, "LayoutTranslations", layout, "*" );
                if ( foundFile == null )
                    return null;
                //must exit cannot continue without a valid layoutdef
                else
                    retValue[F_layoutdef] = foundFile.get( FileProperties.F_filename );
            }
            else
                retValue[F_layoutdef] = foundFile.get( FileProperties.F_filename );
        }
//System.err.println( "5");
		//get the graphical objects, if not found, get simply the first found layoutdef.
		foundFile = searchField( group, "GraphicObjects", graphicalobj, "" );
		if ( foundFile == null ) {
			foundFile = searchField( group, "GraphicObjects", "*", "*" );
			if ( foundFile == null )
				return null;
			//must exit cannot continue without a valid layoutdef
			else
				retValue[F_graphicalobj] = foundFile.get( FileProperties.F_filename );
		}
		else
			retValue[F_graphicalobj] = foundFile.get( FileProperties.F_filename );

//System.err.println( "6");
          
          //get the layoutdef, if not found, get simply the first found layoutdef.
		foundFile = searchField( group, "GraphicObjTranslations", graphicalobjdef, groupdef );
		if ( foundFile == null ) {
			foundFile = searchField( group, "GraphicObjTranslations", "*", groupdef);
			if ( foundFile == null ){
              foundFile = searchField( group, "GraphicObjTranslations", "*", "*");
              if ( foundFile == null )
                  return null;
			//must exit cannot continue without a valid layoutdef
			}
            else
				retValue[F_graphicaldef] = foundFile.get( FileProperties.F_filename );
		}
		else
			retValue[F_graphicaldef] = foundFile.get( FileProperties.F_filename );
//System.err.println( "end");
		return retValue;
	}


	public static String getTranslationFileName( String type,
			String language ) {

		//get the groupdef, if not found, get simply the first found group def.
		FileProperties foundFile = searchField( "*", "Translations", type, language );
		if ( foundFile == null ) {
			foundFile = searchField( "*", "Translations", type, "*" );
			if ( foundFile == null )
				return null;
			//must exit cannot continue without a valid language
			else
				return foundFile.get( FileProperties.F_filename );
		}
		else
			return foundFile.get( FileProperties.F_filename );
	}


	public static FileProperties searchField( String group, String classification, String subclass, String language ) {
		//first look in the external files.  They have priority
		for ( int i = 0; i < externalFiles.length; i++ )
			if ( ( externalFiles[i].get( FileProperties.F_groupname ).equals( group ) || group.equals( "*" ) )
					 && ( externalFiles[i].get( FileProperties.F_classification ).equals( classification ) || classification.equals( "*" ) )
					 && ( externalFiles[i].get( FileProperties.F_subclass ).equals( subclass ) || subclass.equals( "*" ) )
					 && ( externalFiles[i].get( FileProperties.F_language ).equals( language ) || language.equals( "*" ) ) )
				return externalFiles[i];

		for ( int i = 0; i < internalFiles.length; i++ )
			if ( ( internalFiles[i].get( FileProperties.F_groupname ).equals( group ) || group.equals( "*" ) )
					 && ( internalFiles[i].get( FileProperties.F_classification ).equals( classification ) || classification.equals( "*" ) )
					 && ( internalFiles[i].get( FileProperties.F_subclass ).equals( subclass ) || subclass.equals( "*" ) )
					 && ( internalFiles[i].get( FileProperties.F_language ).equals( language ) || language.equals( "*" ) ) )
				return internalFiles[i];

		return null;
	}


	public static FileProperties[] findAllMatches( String group, String classification, String subclass, String language ) {
		Hashtable foundItems = new Hashtable();
		Hashtable repeatCheck = new Hashtable();
        String key;
		for ( int i = 0; i < externalFiles.length; i++ )
			if ( ( externalFiles[i].get( FileProperties.F_groupname ).equals( group ) || group.equals( "*" ) )
					 && ( externalFiles[i].get( FileProperties.F_classification ).equals( classification ) || classification.equals( "*" ) )
					 && ( externalFiles[i].get( FileProperties.F_subclass ).equals( subclass ) || subclass.equals( "*" ) )
					 && ( externalFiles[i].get( FileProperties.F_language ).equals( language ) || language.equals( "*" ) ) ){
				key= externalFiles[i].get( FileProperties.F_groupname ) + ";" + 
                  externalFiles[i].get( FileProperties.F_classification ) + ";" + 
                  externalFiles[i].get( FileProperties.F_subclass ) + ";" + 
                  externalFiles[i].get( FileProperties.F_language );
                if(!repeatCheck.containsKey(key)){
                  repeatCheck.put(key , "" );
                  foundItems.put(new Integer(foundItems.size()),externalFiles[i]);
                }
           }
		for ( int i = 0; i < internalFiles.length; i++ )
			if ( ( internalFiles[i].get( FileProperties.F_groupname ).equals( group ) || group.equals( "*" ) )
					 && ( internalFiles[i].get( FileProperties.F_classification ).equals( classification ) || classification.equals( "*" ) )
					 && ( internalFiles[i].get( FileProperties.F_subclass ).equals( subclass ) || subclass.equals( "*" ) )
					 && ( internalFiles[i].get( FileProperties.F_language ).equals( language ) || language.equals( "*" ) ) ){
				key= internalFiles[i].get( FileProperties.F_groupname ) + ";" + 
                  internalFiles[i].get( FileProperties.F_classification ) + ";" + 
                  internalFiles[i].get( FileProperties.F_subclass ) + ";" + 
                  internalFiles[i].get( FileProperties.F_language );
                if(!repeatCheck.containsKey(key)){
                  repeatCheck.put(key , "" );
                  foundItems.put(new Integer(foundItems.size()),internalFiles[i]);
                }
           }
          FileProperties[] retval = new FileProperties[foundItems.size()];
          for ( int i = 0; i < retval.length; i++ )
              retval[i] = ( FileProperties ) ( foundItems.get( new Integer( i ) ) );
          return retval;
	}

}

