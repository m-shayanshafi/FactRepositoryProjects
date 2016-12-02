/*
 * Classname			: DCSvgPathNode
 * Author				: Davy Herben <theblackunicorn@softhome.net>
 * Creation Date		: Tue Mar 05 02:09:14 CET 2002 
 * Last Updated			: Tue Mar 12 01:55:57 CET 2002 
 * Description			: Tree node representing a path (a leaf on the tree)
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
import java.io.*;
import java.awt.geom.*;

/**
 * This class represents a <path> node in the Svg tree. A <path> contains a
 * GeneralPath. the d="" property is converted to a GeneralPath. Other
 * properties are ignored (for now).
 * 
 * @author				Davy Herben	
 * @version				Tue Mar 12 01:56:12 CET 2002 
 */
public class DCSvgPathNode extends DCSvgNode {
	/*
	 * VARIABLES
	 */
	
	private				GeneralPath			gp			
						= new GeneralPath(GeneralPath.WIND_EVEN_ODD);
	
	/*
	 * INNER CLASSES
	 */

	/*
	 * CONSTRUCTORS
	 */
	
	/**
	 * Class Constructor. This creates a new DCSvgPathNode with the Specified
	 * parent Node and the specified content.
	 * 
	 * @param			parentNode			the Node which is the parent of
	 * 										node. This can never be null
	 * @param			contentString		
	 * 					String containing the contents of the svg <path> tag.
	 * 					It should at least contain a d="" property as this is
	 * 					used to read data from
	 */
	public DCSvgPathNode(DCSvgNode parentNode, String contentString) {
		super(parentNode, contentString);
		containerBoolean = false;
		
		/* now we need to parse the data String */
		String dataString = contentString;
		String searchString = "d=\"";
		
		//ugly code to make sure no id= tags are found here
		boolean dBoolean = false;
		int testInt = 0;
		int posInt;
		do {
			posInt = dataString.indexOf(searchString, testInt);
			if (dataString.indexOf("id=\"", testInt) != posInt -1) {
				dBoolean = true;
			} else {
				testInt = posInt + 1;
				dBoolean = false;
			}
		} while (!dBoolean);
		
		
		/*
 		 * PARSE CONTENT
		 */
		if (posInt != -1) {
			dataString = dataString.substring(posInt + searchString.length());
			posInt = dataString.indexOf("\"");
			dataString = dataString.substring(0, posInt - 1 );
		
			//now, parse the tokens in this string
			try {
				StringReader r = new StringReader(dataString);
				StreamTokenizer st = new StreamTokenizer(r);
				
				//arguments are doubles
				float []argArray = new float[6];
	
				while (st.nextToken() != StreamTokenizer.TT_EOF) {
					if (st.ttype == StreamTokenizer.TT_WORD) {
						char opChar = st.sval.charAt(0);
						switch (opChar) {
							case 'M' :	//move to point
								for (int i=0; i < 2; i++) {
									st.nextToken();
									argArray[i] = (float) st.nval;
								}
								gp.moveTo(argArray[0], argArray[1]);
								break;
							case 'C' : //bezier curve
								for (int i=0; i < 6; i++) {
									st.nextToken();
									argArray[i] = (float) st.nval;
								}
								gp.curveTo(	argArray[0], 
											argArray[1],
											argArray[2],
											argArray[3],
											argArray[4],
											argArray[5]);
								break;
							case 'L' : //straight line
								for (int i=0; i < 2; i++) {
									st.nextToken();
									argArray[i] = (float) st.nval;
								}
								gp.lineTo(	argArray[0], 
											argArray[1]);
								break;
							case 'z' : //close path
								gp.closePath();
								break;
						}
					} else {
						System.err.println("Hmm, not a word ?? : [" + st.toString() + "]");
					}
				}
			} catch (IOException e) {
				System.out.println("WHOOPS !");
			}
		}
	}

	/*
	 * METHODS
	 */

	/**
	 * adds a DCSvgNode to this node. Since DCSvgPathNode is not a container
	 * node, this method does nothing
	 *
	 * @param			node				DCSvgNode to add
	 */
	public void addNode(DCSvgNode node) {
		//do nothing
	}
	
	/**
	 * returns the GeneralPath that was parsed from this Node. Note : this is
	 * the <b>untransformed</b> path. You probably want getTransformedPath
	 * @return			untransformed GeneralPath representation of this <path> tag
	 */
	public GeneralPath getPath() {
		return gp;
	}
	
	/**
	 * returns the GeneralPath that was parsed from this Node, with the
	 * AffineTransform already applied.
	 * @return			transformed GeneralPath representation of this <path>
	 * 					tag			
	 */
	public GeneralPath getTransFormedPath() {
		GeneralPath tp = new GeneralPath(gp);
		tp.transform(at);
		return tp;
	}
	
}
/* END OF FILE */
            
            
            
            
