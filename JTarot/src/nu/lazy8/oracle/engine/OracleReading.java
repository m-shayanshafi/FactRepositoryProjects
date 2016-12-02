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

//import nu.lazy8.oracle.util.*;
import java.util.*;
import java.io.*;

public class OracleReading {
	public OracleStandardFileParser oGroup;
	public OracleStandardFileParser oGroupDef;
	public Layout layout;
	public OracleStandardFileParser layoutTranslations;
	public OracleStandardFileParser graphObjects;
	public OracleStandardFileParser graphObjTranslations;
	private OracleIO io;
	public int[] chosenObjects=null;
	public boolean[] chosenUpSideDown=null;
	private boolean isStandardSetup = true;
	public String group;
	public String groupdef;
	public String graph;
	public String graphdef;
	public String layoutdef;
	public String layoutdeftrans;
	public String[] fileNames;
    public Date timestamp;

	public final static String sDEFAULT_GROUP = "DefaultSetupOracleGroup";
	public final static String sDEFAULT_GROUP_DEF = "DefaultSetupOracleGroupTrans";
	public final static String sDEFAULT_GRAPH_OBJ = "DefaultSetupGraphicalObject";
	public final static String sDEFAULT_GRAPH_OBJ_TRANS = "DefaultSetupGraphicalObjectTranslations";
	public final static String sDEFAULT_LAYOUT = "DefaultSetupLayout";
	public final static String sDEFAULT_LAYOUT_TRANS = "DefaultSetupLayoutTranslations";
	public final static String sSUB_GROUP_USED = "SubGroupUsed";
	public final static String sIS_UPSIDE_DOWN_USED = "UseUpSideDown";

	public OracleReading( OracleIO io ) throws Exception {
		this.io = io;
		Reload( true );
	}


	public void Reload( String group, String groupdef, String graph, String graphdef, String layoutdef, String layoutdeftrans ) throws Exception {
		fileNames = OracleFileManager.getReadingFileNames( group, groupdef, layoutdef, layoutdeftrans, graph, graphdef );
		if (fileNames==null)throw new Exception("Could not get all the files for the reading");
//System.err.println( group + " " + groupdef + " " + layoutdef + " " + layoutdeftrans + " " + graph + " " + graphdef);
//for(int i=0;i<fileNames.length;i++)System.err.println(fileNames[i]);
        oGroup= new OracleStandardFileParser( io.getInputStream( fileNames[OracleFileManager.F_group] ) );
		oGroupDef= new OracleStandardFileParser( io.getInputStream( fileNames[OracleFileManager.F_groupdef] ) );
		graphObjects= new OracleStandardFileParser( io.getImageFileContentsStream( fileNames[OracleFileManager.F_graphicalobj] ) );
		layout= new Layout(io.getInputStream( fileNames[OracleFileManager.F_layout] ) );

		layoutTranslations= new OracleStandardFileParser( io.getInputStream( fileNames[OracleFileManager.F_layoutdef] ) );
		graphObjTranslations= new OracleStandardFileParser( io.getInputStream( fileNames[OracleFileManager.F_graphicaldef] ) );
		isStandardSetup = false;
		this.group = group;
		this.groupdef = groupdef;
		this.graph = graph;
		this.graphdef = graphdef;
		this.layoutdef = layoutdef;
		this.layoutdeftrans = layoutdeftrans;
        if (chosenObjects==null || chosenObjects.length != layout.numberofobjects)
          resetReading();
        timestamp=new Date();
        io.setProperty( sDEFAULT_GROUP ,group);
        io.setProperty( sDEFAULT_GROUP_DEF,groupdef );
        io.setProperty( sDEFAULT_GRAPH_OBJ,graph );
        io.setProperty( sDEFAULT_GRAPH_OBJ_TRANS,graphdef );
        io.setProperty( sDEFAULT_LAYOUT,layoutdef );
        io.setProperty( sDEFAULT_LAYOUT_TRANS,layoutdeftrans ); 
	}
    public void resetReading(){
        chosenObjects=new int[layout.numberofobjects];
        chosenUpSideDown=new boolean[layout.numberofobjects];
        for(int i=0;i<chosenObjects.length;i++){
          chosenObjects[i] = -1;
          chosenUpSideDown[i] = false;
        }
    }

	public void Reload( boolean isForce ) throws Exception {
		if ( isForce || !isStandardSetup )
			Reload( io.getProperty( sDEFAULT_GROUP ),
					io.getProperty( sDEFAULT_GROUP_DEF ),
					io.getProperty( sDEFAULT_GRAPH_OBJ ),
					io.getProperty( sDEFAULT_GRAPH_OBJ_TRANS ),
					io.getProperty( sDEFAULT_LAYOUT ),
					io.getProperty( sDEFAULT_LAYOUT_TRANS ) );

		isStandardSetup = true;
	}


	public void SetReadingFromResource( String reading, String filename ) {
	}


	public int[] GetReading() {
		return chosenObjects;
	}


	public void SetReading( int[] chosenObjects, boolean[] chosenUpSideDown,Date timestamp ) {
		this.chosenObjects = chosenObjects;
		this.chosenUpSideDown = chosenUpSideDown;
        this.timestamp=timestamp;
	}


	public String getHyperTextOnlyReading() {
		return "";
	}


	public String getTextOnlyReading() {
		return "";
	}


	public String getTextOnlyReading( int chosenObjectNum, String positionText, String nameText, String reversedText ) {
		StringBuffer strBuf = new StringBuffer();
		//position name
		strBuf.append( positionText + ":  " + layoutTranslations.translate( "PositionTitle" + chosenObjectNum ) + "\n\n" );
		strBuf.append( layoutTranslations.translate( "Meaning" + chosenObjectNum ) + "\n\n" );
		strBuf.append( nameText + ":  " + graphObjTranslations.translate( "ObjectTitle" + chosenObjects[chosenObjectNum] ) + "\n\n" );
		if ( chosenUpSideDown[chosenObjectNum] )
			strBuf.append( reversedText + "\n" );
		strBuf.append( graphObjTranslations.translate( "ObjectMeaning" + chosenObjects[chosenObjectNum] ) + "\n" );
		//position description
		//object name
		//object meaning

		//copyrights


		return strBuf.toString();
	}


	public String[] getHtmlFileNames() {
		return new String[1];
	}


	public void exportHtmlReading( OutputStream[] osHtml, int percentGraphObjectSize ) {
	}


	public void exportWAR( OutputStream os, int percentGraphObjectSize ) {
	}


	public void exportMHTML( OutputStream os, int percentGraphObjectSize ) {
	}


	public int getTotalNumberOfObjects() {
		//must take consideration for sub groups
		int iUseSubGroup = Integer.parseInt( io.getProperty( sSUB_GROUP_USED ) );
		if ( iUseSubGroup > -1 ) {
			String nameSubGroup = "subgroup" + iUseSubGroup;
//System.err.println("nameSubGroup = " + nameSubGroup);
			if ( oGroup.getValue( nameSubGroup ) != null ) {
				StringTokenizer tokenizer = new StringTokenizer( oGroup.getValue( nameSubGroup ) );
				int i = 0;
				while ( tokenizer.hasMoreTokens() ) {
					tokenizer.nextToken();
					i++;
				}
				return i;
			}
		}
		return Integer.parseInt( oGroup.getValue( "numberofobjects" ) );
	}
	public int getTotalNumberOfObjectsNoSubgroups() {
		return Integer.parseInt( oGroup.getValue( "numberofobjects" ) );
	}


	public int[] getRandomizedObjectIndexes() {
		int totalObjects = getTotalNumberOfObjects();
		int[] retArray = new int[totalObjects];
		int[] randomArray = new int[totalObjects];
		for ( int i = 0; i < retArray.length; i++ )
			retArray[i] = i;
		//must take consideration for sub groups
		int iUseSubGroup = Integer.parseInt( io.getProperty( sSUB_GROUP_USED ) );
		if ( iUseSubGroup > -1 ) {
			String nameSubGroup = "subgroup" + iUseSubGroup;
			if ( oGroup.getValue( nameSubGroup ) != null ) {
				StringTokenizer tokenizer = new StringTokenizer( oGroup.getValue( nameSubGroup ) );
				for ( int i = 0; i < retArray.length; i++ )
					retArray[i] = Integer.parseInt( tokenizer.nextToken() );
			}
		}
		//now randomize the array..
		boolean graphicalObjects[] = new boolean[totalObjects];
		for ( int i = 0; i < totalObjects; i++ )
			graphicalObjects[i] = false;
		//false= card not selected yet.  true=card already used.
		RandomExtended random = new RandomExtended();
		int nextGraphObj = 0;
		int MAX_RAND = 0xFFF;
		for ( int i = 0; i < ( totalObjects - 1 ); i++ ) {
			//do the last object after this loop. Faster that way
			nextGraphObj = -1;
			// -1 means card not picked yet
			//randomly pick a card from the deck
			while ( nextGraphObj == -1 ) {
				nextGraphObj = random.get12bitInt() * ( totalObjects ) / MAX_RAND;
				//12 bits=0xff or 4095 max
//System.err.println("nextGraphObj = " + nextGraphObj);
				if ( nextGraphObj >= totalObjects || graphicalObjects[nextGraphObj] )
					nextGraphObj = -1;
				//repeated cards not allowed.  Try getting the card again.
				else
					graphicalObjects[nextGraphObj] = true;
				//found a new unrepeated card

			}
			randomArray[i] = retArray[nextGraphObj];
		}
		//get the last object.  It is just the last unused item.
		for ( int i = 0; i < totalObjects; i++ )
			if ( !graphicalObjects[i] )
				randomArray[totalObjects - 1] = retArray[i];

//for ( int i = 0; i < totalObjects; i++ )
//System.err.println("randomized  " + i + " = " +  randomArray[i]);
		return randomArray;
	}
}

