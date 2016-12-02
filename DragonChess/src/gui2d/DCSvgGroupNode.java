/*
 * Classname			: DCSvgGroupNode
 * Author				: Davy Herben <theblackunicorn@softhome.net>
 * Creation Date		: Fri Mar 01 23:49:38 CET 2002 
 * Last Updated			: Tue Mar 12 02:07:34 CET 2002 
 * Description			: Tree node representing a group
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
import java.util.ArrayList;

/**
 * This class represents a svg group structure, indicated with the XML tag <g>.
 * It contains other nodes and an AffineTransform which is applied to child
 * nodes.
 * 
 * @author				Davy Herben	
 * @version				Tue Mar 12 02:07:44 CET 2002 
 */
public class DCSvgGroupNode extends DCSvgNode {
	/*
	 * VARIABLES
	 */

	//ArrayList to store subnodes in
	private				ArrayList			nodeList = new ArrayList();
	
	/*
	 * INNER CLASSES
	 */

	/*
	 * CONSTRUCTORS
	 */
	
	/**
	 * Class constructor. Creates a new DCSvgGroupNode with the specified
	 * parent and content.
	 * @param			parentNode			the direct parent Node of this
	 * 										node, or null for the root node
	 * @param			contentString		String containing the XML <g> tag.
	 * 					The only part used from this String is the
	 * 					AffineTransform if present, all other properties are 
	 * 					ignored
	 */
	public DCSvgGroupNode(DCSvgNode parentNode, String contentString) {
		super(parentNode, contentString);
		containerBoolean = true; 
		
	}

	/*
	 * METHODS
	 */

	/**
	 * Adds a child node to this Group node.
	 * @param			node				Child node of this node
	 */
	public void addNode(DCSvgNode node) {
		nodeList.add( (DCSvgNode) node);
	}

	/**
	 * returns the child node of this node at the specified index.
	 * @param			index				position of the requested node
	 * @return			the child node at the given position
	 */
	public DCSvgNode getNode(int index) {
		return (DCSvgNode) nodeList.get(index);
	}
}
/* END OF FILE */
            
            
            
            
