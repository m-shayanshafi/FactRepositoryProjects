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
 
  /**
 *
 * This class provides a list of all available files divided up into those files
 * classes.  This is initialized with an xml file.
 * This xml file must have the following format :
 * <p>
 * &lt;oracleio&gt;
 *     &lt;file&gt;
 *         &lt;groupname&gt;&lt;/groupname&gt;
 *         &lt;classification&gt;&lt;/classification&gt;
 *         &lt;subclass&gt;&lt;/subclass&gt;
 *         &lt;language&gt;&lt;/language&gt;
 *         &lt;version&gt;&lt;/version&gt;
 *         &lt;copyright&gt;&lt;/copyright&gt;
 *         &lt;size&gt;&lt;/size&gt;
 *         &lt;filename&gt;&lt;/filename&gt;
 *         &lt;displayname&gt;&lt;/displayname&gt;
 *         &lt;comments&gt;&lt;/comments&gt;
 *         &lt;downloadablegroup&gt;&lt;/downloadablegroup&gt;
 *         &lt;downloadURL&gt;&lt;/downloadURL&gt;
 *     &lt;/file&gt;
 *  &lt;/oracleio&gt;
 *
 * There are 3
 *
 * @author Thomas Dilts, <thomas@lazy8.nu>
 */

 
package nu.lazy8.oracle.engine;

import java.io.*;
import java.util.*;

public class FileProperties {
	private static String[] fields = {"groupname", "classification", "subclass", "language", "version", "copyright",
			"size", "filename", "displayname", "comments", "downloadablegroup","downloadURL"};
	private String[] fieldValues = new String[fields.length];
    private boolean isInternal=false;

    public static int F_groupname = 0;
    public static int F_classification = 1;
    public static int F_subclass = 2;
    public static int F_language = 3;
    public static int F_version = 4;
    public static int F_copyright = 5;
    public static int F_size = 6;
    public static int F_filename = 7;
    public static int F_displayname = 8;
    public static int F_comments = 9;
    public static int F_downloadablegroup = 10;
    public static int F_downloadURL = 11;
    
	public FileProperties( String[] fieldValues,boolean isInternal ) {
		this.fieldValues = fieldValues;
        this.isInternal=isInternal;
	}

    public String get(int index){
      return fieldValues[index];
    }
	public static void writeXmlFile(OutputStream outStr, FileProperties[] list) throws Exception{
        OutputStreamWriter out=new OutputStreamWriter(outStr, "UTF-8") ;
        OracleStandardFileParser.WriteLine(out,"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<oracleio>");
		for ( int j = 0; j < list.length; j++ ){
		OracleStandardFileParser.WriteLine(out, "<file>" );
          for ( int i = 0; i < fields.length; i++ ){
              OracleStandardFileParser.WriteLine(out, "<" + fields[i] + ">" + 
                OracleStandardFileParser.replaceXmlEscapeCharacters( list[j].fieldValues[i] ) + "</" + fields[i] + ">" );
          }
		OracleStandardFileParser.WriteLine(out, "</file>" );
        }
		OracleStandardFileParser.WriteLine(out, "</oracleio>" );
        out.close();
	}
	public static FileProperties[] readXmlFile( InputStream inStr ,boolean isInternal) throws Exception {
		String[][] ioSetupKeyNames = new String[1][fields.length];
		String[] ioGroupNames = {"file"};
		for ( int i = 0; i < fields.length; i++ )
			ioSetupKeyNames[0][i] = fields[i];
		Hashtable[][] returnedItems = new Hashtable[1][fields.length];
		for ( int i = 0; i < returnedItems.length; i++ )
			for ( int j = 0; j < returnedItems[0].length; j++ )
				returnedItems[i][j] = new Hashtable();
		OracleParser.parseXml( inStr, ioGroupNames, ioSetupKeyNames, returnedItems );
		FileProperties[] returnedFiles = new FileProperties[returnedItems[0][0].size()];
		String[] newFieldValues;
		for ( int j = 0; j < returnedItems[0][0].size(); j++ ) {
          newFieldValues = new String[fields.length];
			for ( int i = 0; i < fields.length; i++ )
				newFieldValues[i] = ( String ) ( returnedItems[0][i].get( new Integer( j ) ) );

			returnedFiles[j] = new FileProperties( newFieldValues,isInternal );
//System.out.println("readXmlFile " + returnedFiles[j].get( FileProperties.F_filename ));
		}
		return returnedFiles;
	}

	public static FileProperties[] readSerialFile( DataInputStream dis ,boolean isInternal) throws Exception {
		FileProperties[] returnedFiles = new FileProperties[dis.readInt()];
		String[] newFieldValues = new String[fields.length];
		for ( int j = 0; j < returnedFiles.length; j++ ) {
			for ( int i = 0; i < fields.length; i++ )
				newFieldValues[i] = dis.readUTF();

			returnedFiles[j] = new FileProperties( newFieldValues,isInternal );
		}
		return returnedFiles;
	}
	public static void writeSerialFile( DataOutputStream das,FileProperties[] files ) throws Exception {
      das.writeInt( files.length );
      for ( int j = 0; j <files.length; j++ ) 
			for ( int i = 0; i < fields.length; i++ )
				das.writeUTF( files[j].fieldValues[i] );
	}
}

