/*
 * Classname			: DCSvgParser 
 * Author				: Davy Herben <theblackunicorn@softhome.net>
 * Creation Date		: Fri Mar 01 20:51:24 CET 2002 
 * Last Updated			: Tue Mar 12 01:38:00 CET 2002 
 * Description			: svg file -> DCImage2D converter
 * GPL disclaimer		:
 *   This program is free software; you can redistribute it and/or modify it
 *   under the terms of the GNU General Public License as published by the
 *   Free Software Foundation; version 2 of the License.
 *   This program is distributed in the hope that it will be useful, but
 *   WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *   or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *   for more details. You should have received a copy of the GNU General
 *   Public License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package gui2d;

/* package import */
import java.lang.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * Takes a svg file as input and turns it into a DCImage2D drawing.
 *
 * This class first constructs a tree, then calls other classes to turn the
 * leaves of the tree into GeneralPaths, which are then combined into a
 * DCImage2D.
 * @author				Davy Herben
 * @version				Tue Mar 12 01:38:47 CET 2002 
 */
public class DCSvgParser {
	/*
	 * VARIABLES
	 */

	/* INSTANCE VARIABLES */
	private				DCImage2D			img = new DCImage2D(Color.black, Color.yellow);
	private				DCSvgNode			rootNode;
	private				String				fileString;
	
	/*
	 * INNER CLASSES
	 */

	/*
	 * CONSTRUCTORS
	 */

	/**
	 * Class constructor. This constructor accepts a String representing an Svg
	 * file, parses it into a tree structure and then converts this tree into
	 * a DCImage2D.
	 *
	 * @param			fileString			name of the svg file
	 */
	public DCSvgParser(String fileString) throws IOException {
		/*
		 * OPEN FILE AND STORE IN STRING
		 */
		try {
			File svgFile = new File(fileString);
			BufferedReader r = new BufferedReader(new FileReader(svgFile));

			/* put everything into a String */
			String sString = new String();
			while((sString = r.readLine())!= null) {
				fileString += sString;
			}
		} catch (IOException e) {
			throw new IOException("Can't open " + fileString + "for reading.");
		}
			
		/* now cut this string up into tokens. tokens go from the opening <
		 * to the closing > */
		ArrayList stringList = new ArrayList();
		/* always cut until the next < (<not included)
		 * if there's no more <, cut till end of String
		 */
			
		int posInt = 0;
		String compString = new String();
		while (fileString.length() != 0) {
			posInt = fileString.indexOf('<', 2);
			if (posInt == -1) {
				//return whole string
				stringList.add(fileString);
				fileString = "";
			} else {
				compString = fileString.substring(0, posInt);
				stringList.add(compString);
				fileString = fileString.substring(posInt);
			}
		}

		
		/*
		 * BUILD TREE STRUCTURE
		 */
		DCSvgNode currentNode = null;
		
		//locate the root <svg> tag
		int index = 0;
		while (!((String) stringList.get(index)).startsWith("<svg")) {
			index++;
		}

		//create a list to store PathNodes in as they are found
		//this way, we don't have to develop a Treewalker later
		ArrayList pathList = new ArrayList();
		
		// set root node
		String rootString = (String) stringList.get(index);
		rootNode = new DCSvgGroupNode(null, rootString);
		currentNode = rootNode;
		
		//now make rest of tree
		String currentString;
		for (int i = index + 1; i < stringList.size(); i++) {
			currentString = (String) stringList.get(i);
			
			if (currentString.startsWith("<g")) {
				//add groupNode
				DCSvgGroupNode myGroupNode = new DCSvgGroupNode(currentNode, currentString);
				currentNode.addNode(myGroupNode);
				//descend 1 level
				currentNode = myGroupNode;
			} else if (currentString.startsWith("</g")) {
				//ascend 1 level
				currentNode = currentNode.getParentNode();
			} else if (currentString.startsWith("<path")) {
				//add path to currentNode
				DCSvgPathNode myPathNode = new DCSvgPathNode(currentNode, currentString);
				currentNode.addNode(myPathNode);
				pathList.add(myPathNode);
			} else if (currentString.startsWith("</path")) {
				//nothing needs to be done here
			} else if (currentString.startsWith("</svg")) {
				// test file integrity. currentNode should now be rootNode
				if (!(currentNode == rootNode)) {
					System.err.println("File integrity check failure");
				}
			} else {
				//do nothing
			}
		}
		

		/*
		 * CREATE DCIMAGE2D FROM DATA 
		 */
		
		/* Note : this parser does not traverse the built tree in order to
		 * obtain the necessary Paths. instead, it uses a linear list of paths
		 * that was created even while building the tree. this behaviour may
		 * change later
		 */
		DCSvgPathNode tempNode;
		for (int i = 0; i < pathList.size(); i++) {
			tempNode = (DCSvgPathNode) pathList.get(i);
			img.add(tempNode.getTransFormedPath());
		}
		
		
	}
		
	
	/*
	 * METHODS
	 */

	/**
	 * Returns the DCImage2D that is the result of parsing this file.
	 * @return			the DCImage2D
	 */
	public DCImage2D getDCImage2D() {
		return img;
	}
}
/* END OF FILE */
            
            
            
           
