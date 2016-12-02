/*
 * Classnam				: DCSvgNode 
 * Author				: Davy Herben <theblackunicorn@softhome.net>
 * Creation Date		: Fri Mar 01 22:52:44 CET 2002 
 * Last Updated			: Tue Mar 12 01:41:58 CET 2002 
 * Description			: superclass for elements of a DC Svg tree
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
import java.awt.geom.*;
import java.io.*;

/**
 * This is the abstract superclass for all nodes in a DC svg tree.
 * Basically it consists of a reference to it's parent node, an AffineTransform
 * which is applied to the data in this node, and a flag indicating whether
 * this node can contain other nodes (usually set in the constructor of the
 * subclasses.
 * @author				Davy Herben
 * @version				Tue Mar 12 01:41:49 CET 2002 
 */
abstract public class DCSvgNode {
	/*
	 * VARIABLES
	 */

	/* INSTANCE VARIABLES */

	/**
	 * DCSvgNode that is the direct parent of this node
	 */
	protected			DCSvgNode 			parentNode = null;

	/**
	 * An affineTransform that is applied to the data in this Node
	 */
	protected			AffineTransform 	at = new AffineTransform();

	/**
	 * flag indicating whether this node is of a container type, ie can
	 * contain more nodes
	 */
	protected			boolean				containerBoolean = false;

	/*
	 * INNER CLASSES
	 */

	/*
	 * CONSTRUCTORS
	 */
	
	/**
	 * class contstructor. Creates a new DCSvgNode with the specified parent
	 * Node, and with the specified content. This content should consist solely
	 * of the XML tag starting the node, NOT the entire contents of the node.
	 *
	 * <p>The constructor also reads the affinetransform of the parent node (if
	 * not null) and applies it to this node as well. This way, any node has
	 * the ENTIRE transform that should be applied to it, and no recursive
	 * treewalking is needed for this
	 * 
	 * @param			parent				the DCSvgNode that is the direct
	 * 										parent in the XML tree, or null 
	 * 										for the root node
	 * @param			contentString		String with the xml tag starting
	 * 										the node
	 */
	protected DCSvgNode(DCSvgNode parent, String contentString) {
	
		/* for both groups and paths, we need to find out if there's a
		 * transform associated with it
		 */

		String transformString = contentString;
		String searchString = "transform=\"matrix(";
		int posInt = transformString.indexOf(searchString);
		if (posInt != -1) {
			transformString = transformString.substring(posInt + searchString.length());
			posInt = transformString.indexOf(")\"");
			transformString = transformString.substring(0, posInt - 1 );
			
			//now, parse the numbers in this string
			StringReader r = new StringReader(transformString);
			StreamTokenizer st = new StreamTokenizer(r);
		
			double []argArray = new double[6];
			int argInt = 0;
			
			try {
				while (st.nextToken() != StreamTokenizer.TT_EOF) {
					if (st.ttype == StreamTokenizer.TT_NUMBER) {
						argArray[argInt] = st.nval;
						argInt++;
					} else if (st.ttype == StreamTokenizer.TT_WORD) {
						//if it starts with an e or E, assume it is followed
						//by a number
						String powerString = st.sval;
						if (powerString.startsWith("e") || powerString.startsWith("E")) {
							powerString = powerString.substring(1);
							double powerDouble = Double.parseDouble(powerString);
							powerDouble = Math.pow(10, powerDouble);
							argArray[argInt - 1] = argArray[argInt - 1] * powerDouble;
							
						}
					} else {
						System.out.println("Not recognized : " + st.toString());
					}
				}

				//now save the transformation
				at.setTransform(new AffineTransform(
							argArray[0],
							argArray[1],
							argArray[2],
							argArray[3],
							argArray[4],
							argArray[5]));

			} catch (IOException e) {
				e.printStackTrace();
			}

			r.close();
		}
		
		
		if (parent != null) {
			parentNode = parent;

			/* inherit the transformation from your parent.
			 * this way, the leaves will always contain the full transform that
			 * has to be applied to them in order to get the right result
			 */
			at.concatenate(parent.getTransform());
		}
	}
	
	/*
	 * METHODS
	 */

	/**
	 * returns the parent of this node in the XML tree
	 * @return			the Parent DCSvgNode
	 */
	public DCSvgNode getParentNode() {
		return parentNode;
	}

	/**
	 * returns the AffineTransform currently applied to this DCSvgNode
	 * @return			the applied AffineTransform
	 */
	public AffineTransform getTransform() {
		return at;
	}

	/**
	 * sets the affineTransform for this DCSvgNode. This method replaces any
	 * previously applied transformation.
	 *
	 * <p>Note : this method does NOT adjust the transforms of the child nodes
	 * of this Node and will thus produce unexpected results when used on
	 * container nodes. use with care
	 * 
	 * @param			transform			the AffineTransform to apply
	 */
	public void setTransform(AffineTransform transform) {
		at.setTransform(transform);
	}

	/**
	 * adds the affineTransform for this DCSvgNode. This method adds the
	 * specified transform to the end of the transform already implemented
	 *
	 * <p>Note : this method does NOT adjust the transforms of the child nodes
	 * of this Node and will thus produce unexpected results when used on
	 * container nodes. use with care
	 * 
	 * @param			transform			the AffineTransform to apply
	 */
	public void addTransform(AffineTransform transform) {
		at.concatenate(transform);
	}

	/**
	 * returns whether this DCSvgNode can contain other nodes
	 *
	 * @return			true if this Node is a container, false otherwise
	 */
	public boolean isContainer() {
		return containerBoolean;
	}

	/**
	 * adds a Child node to this node. Only useful for container Nodes
	 *
	 * @param 			ndoe				child node to add
	 */
	public abstract void addNode(DCSvgNode node);

}
/* END OF FILE */
            
            
            
            
