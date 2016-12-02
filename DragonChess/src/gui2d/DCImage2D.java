/*
 * Classname			: DCImage2D
 * Author				: Davy Herben <theblackunicorn@softhome.net>
 * Creation Date		: Sun Feb 24 10:58:25 CET 2002 
 * Last Updated			: Thursday, October 17 2002, 23:32:05
 * Description			: 2D graphical representation of a dragonchess piece
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
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

/**
 * Represents the graphical representation of a Dragonchess piece on a 2D board
 * @author				Davy Herben
 * @author				Christophe Hertigers
 * @version				Thursday, October 17 2002, 23:32:05
 */
public class DCImage2D implements Shape {
	/*
	 * VARIABLES
	 */

	/* INSTANCE VARIABLES */
	private ArrayList			l				= new ArrayList();
	private Color				outlineColor	= Color.black;
	private Color				fillColor		= Color.yellow;
	private boolean				antiAliasBoolean = false;
	
	/**
	 * Contains the entire image merged into a single path, for determining
	 * bounds etc. Shape is equal to the entire image, but filled wrongly
	 */
	private GeneralPath			outline			= new GeneralPath(
													GeneralPath.WIND_EVEN_ODD);
	
	/*
	 * INNER CLASSES
	 */

	/*
	 * CONSTRUCTORS
	 */
	
	/**
	 * Class constructor. Creates an empty DCImage2D
	 */
	public DCImage2D() {
	}
	
	/**
	 * Class Constructor, creates an empty DCImage2d with specified stroke and
	 * fill colors.
	 * @param			oColor		The Color of the outline (stroke color)
	 * @param			fColor		The Color of the piece (fill color)
	 */
	public DCImage2D(Color oColor, Color fColor) { 
		outlineColor = oColor;
		fillColor = fColor;
	}
	
	/*
	 * METHODS
	 */ 

	/**
	 * Tests if the specified coordinates are inside the boundary of the 
	 * DCImage2D.
	 * @param 			x			double specifying x coordinate
	 * @param			y			double specifying y coordinate
	 * @return			true if the specified coordinates are within the
	 * DCImage2D boundary; false otherwise
	 */
	public boolean contains(double x, double y) {
		/* a location is in the Image if it's in any part of the image
		 * do not use the outline because it has wrong fill settings
		 */

		for (int i = 0; i < l.size(); i++) {
			GeneralPath gp = get(i);
			if (gp.contains(x, y)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Tests if the interior of the DCImage2D contains the interior of a 
	 * specified rectangular area. 
	 *
	 * @param			x			x coord of the upper left corner of the
	 * rectangle
	 * @param			y			y coord of the upper left corner of the
	 * rectangle
	 * @param			width		double specifying width of rectangle
	 * @param			height		double specifying height of rectangle
	 * @return			true if rectangular area is contained in DCImage2D;
	 * false otherwise
	 * @see java.awt.Shape
	 */
	public boolean contains(double x, double y, double w, double h) {
		/* an area is in the Image if it's in any part of the image */
		for (int i = 0; i < l.size(); i++) {
			GeneralPath gp = get(i);
			if (gp.contains(x, y, w, h)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Tests if the interior of the DCImage contains a specific Point2D
	 * @param			p			a specific Point2D
	 * @return			true if Point2D is within the DCImage2D boundaries;
	 * false otherwise
	 * @see java.awt.Shape
	 */
	public boolean contains(Point2D p) {
		/* a Point is in the Image if it's in any part of the image */
		for (int i = 0; i < l.size(); i++) {
			GeneralPath gp = get(i);
			if (gp.contains(p)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Tests if the interior of the DCImage contains a specific Rectangle2D
	 * @param			r			a specific Rectangle2D
	 * @return			true if the Rectangle2D is within the DCImage2D 
	 * 					boundaries; false otherwise
	 * 					
	 * @see java.awt.Shape
	 */
	public boolean contains(Rectangle2D r) {
		/* a Rectangle2D is in the Image if it's in any part of the image */
		for (int i = 0; i < l.size(); i++) {
			GeneralPath gp = get(i);
			if (gp.contains(r)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns an integer Rectangle that completely encloses the DCImage2D. 
	 * Note that there is no guarantee that the returned Rectangle is 
	 * the smallest bounding box that encloses the DCImage2D, only that the 
	 * DCImage2D lies entirely within the indicated Rectangle. The returned 
	 * Rectangle might also fail to completely enclose the DCImage2D if the 
	 * DCImage2D overflows the limited range of the integer data type. The 
	 * getBounds2D method generally returns a tighter bounding box due 
	 * to its greater flexibility in representation. 
	 * @return		an integer Rectangle that completely encloses
	 * 			the DCImage2D
	 */
	public Rectangle getBounds() {
		return outline.getBounds();
	}

	/**
	 * Returns a high precision and more accurate bounding box of the 
	 * DCImage2D than the getBounds method. Note that there is no guarantee
	 * that the returned Rectangle2D is the smallest bounding box that 
	 * encloses the DCImage2D, only that the DCImage2D lies entirely 
	 * within the indicated Rectangle2D. The bounding box returned by this
	 * method is usually tighter than that returned by the getBounds
	 * method and never fails due to overflow problems since the return
	 * value can be an instance of the Rectangle2D that uses double 
	 * precision values to store the dimensions.	
	 * @return		an instance of Rectangle2D that is a 
	 *			high-precision bounding box of the Shape.
	 */
	public Rectangle2D getBounds2D() {
		return outline.getBounds2D();
	}
	
	/**
	 * returns a PathIterator that travels along the outline of the
	 * DCImage2D. This method uses the GeneralPath representation
	 * of the image stored in the class. The accuracy of this method
	 * is untested.
	 * @param		at	an AffineTransform to be applied to
	 *				the coordinates as they are returned
	 *				by the iterator, or null 
	 * @return		a new PathIterator that independently traverses
	 *			the DCImage2D boundaries
	 */
	public PathIterator getPathIterator(AffineTransform at) {
		/* not sure how to do this. using pathIterator from outline */
		return outline.getPathIterator(at);
	}

	/**
	 * returns a PathIterator that travels along the outline of the
	 * DCImage2D, with a specified flatness. 
	 * This method uses the GeneralPath representation
	 * of the image stored in the class. The accuracy of this method
	 * is untested.
	 * @param		at	an AffineTransform to be applied to
	 *				the coordinates as they are returned
	 *				by the iterator, or null 
	 * @param		flatness the maximum distance that the line 
	 *				segments used to approximate the curved
	 *				segments are allowed to deviate from 
	 *				any point on the original curve 
	 * @return		a new PathIterator that independently traverses
	 *			the DCImage2D boundaries
	 */
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		/* probably not functional. using pathIterator from outline */
		return outline.getPathIterator(at, flatness);
	}
	
	/**
	 * tests if the DCImage2D intersects with the rectangular area
	 * specified. Warning : Untested!
	 * @param	x	horizontal position
	 * @param	y	vertical position
	 * @param	w	width of the rectangle
	 * @param	h	height of the rectangle
	 * @return	true if the DCImage2D intersects with the rectangular
	 *		area, false otherwise.
	 */
	public boolean intersects(double x, double y, double w, double h) {
		/* intersects if it intersects with outline */
		return outline.intersects(x, y, w, h);
	}
	
	/**
	 * tests if the DCImage2D intersects with the Rectangle2D
	 * specified. Warning : Untested!
	 * @param	r	Rectangle2D to check against
	 * @return	true if the DCImage2D intersects with the rectangular
	 *		area, false otherwise.
	 */
	public boolean intersects(Rectangle2D r) {
		/* intersects if it intersects with outline */
		return outline.intersects(r);
	}
	
	/**
	 * Draws the image. Currently not implemented. use the paintComponent
	 * function instead.
	 */
	public void draw() {
	}
	
	/**
	 * Draws all parts of the Image2D on top of each other on a component.
	 * This method still needs to be changed to also support unfilled paths.
	 * @param	g	the Graphics environment
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		/* NOTE : may need to change this as there's a possibility that
		 * unfilled paths (eg. simple curved lines) won't display correctly
		 * when fill is called on them. need to test this
		 */
		
		/* simply fill and draw all members of List */
		if (antiAliasBoolean) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		}
		for (int i = 0; i < l.size(); i++) {
			GeneralPath gp = get(i);
			g2.setColor(fillColor);
			g2.fill(gp);
			g2.setColor(outlineColor);
			g2.draw(gp);
		}
	}
	
	/**
	 * Adds a GeneralPath to the top of the DCImage2D
	 * @param	gp	the GeneralPath to add
	 */
	public void add(GeneralPath gp) {
		outline.append(gp, false);
		l.add(gp);
	}

	/**
	 * empties the image. This method clears the list of
	 * GeneralPaths as well as the GeneralPath outline
	 */
	public void clear() {
		outline.reset();
		l.clear();
	}

	/**
	 * returns whether this DCImage2D is empty, this means it contains
	 * no GeneralPaths
	 * @return	true if the DCImage2D is empty, false otherwise
	 */	
	public boolean isEmpty() {
		return l.isEmpty();
	}

	/**
	 * returns the number of GeneralPaths in the Image.
	 * @return	the number of GeneralPaths
	 */
	public int size() {
		return l.size();
	}

	/**
	 * Retrieves a specific GeneralPath of the DCImage2D. This functionality
	 * will rarely be needed.
	 * @param	index	the position of the GeneralPath in the list
	 * @return	the requested GeneralPath
	 */
	public GeneralPath get(int index) {
		return (GeneralPath) l.get(index);
	}

	/**
	 * translates the DCImage2D so that the upper left corner of it's
	 * bounding box is on the specified coordinates.
	 * @param	xTargetDouble	horizontal target
	 * @param	yTargetDouble	vertical target
	 */
	public void translateTo(double xTargetDouble, double yTargetDouble) {
		/* translating means translating all parts of the image */
		AffineTransform t = new AffineTransform();
		
		/* first, find out where the picture is located */
		double xSourceDouble = (double) ((Rectangle2D.Float) 
													outline.getBounds2D()).x;
		double ySourceDouble = (double) ((Rectangle2D.Float) 
													outline.getBounds2D()).y;
		
		/* translation = 0 - source + target */
		double xTransDouble = 0 - xSourceDouble + xTargetDouble;
		double yTransDouble = 0 - ySourceDouble + yTargetDouble;

		t.translate(xTransDouble, yTransDouble);
		
		/* translate all */
		for (int i = 0; i < l.size(); i++) {
			GeneralPath gp = get(i);
			gp.transform(t);
		}

		/* now, reconstruct outline
		 * this could be done by simply applying the transformation to the gp
		 * but I choose to completely rebuild it from its parts for greater
		 * accuracy. this will be changed if it proves too slow
		 */

		reconstructOutline();
		
	}

	/**
	 * translates the DCImage2D so that the upper left corner of its
	 * bounding box is at the origin (0, 0)
	 */
	public void translateToOrigin() {
		translateTo(0, 0);
	}
	
	/**
	 * scales the DCImage2D so that it's width and height are equal to
	 * the specified parameters.
	 * <p>Note : this method actually scales the entire environment. this
	 * means that the position of the scaled image will change if the image
	 * was not in the origin. The proper way to do this is :
	 * <ul><li>Translate the DCImage to the Origin (0,0)
	 *     <li>Scale to desired width and height
	 *     <li>Translate to desired offset
	 * </ul>
	 * @param	wTargetDouble	target width
	 * @param	hTargetDouble	target height
	 */
	public void scaleTo(double wTargetDouble, double hTargetDouble) {
		/* scaling means scaling all parts of the image */
		AffineTransform t = new AffineTransform();

		/* first, find out current dimensions */
		double wSourceDouble = (double) ((Rectangle2D.Float) 
												outline.getBounds2D()).width;
		double hSourceDouble = (double) ((Rectangle2D.Float) 
												outline.getBounds2D()).height;

		/* scaling = target width / current width */
		double xScalingDouble = wTargetDouble / wSourceDouble;
		double yScalingDouble = hTargetDouble / hSourceDouble;

		t.scale(xScalingDouble, yScalingDouble);

		/* scale all */
		for (int i = 0; i < l.size(); i++) {
			GeneralPath gp = get(i);
			gp.transform(t);
		}

		/* now, reconstruct outline
		 * this could be done by simply applying the transformation to the gp
		 * but I choose to completely rebuild it from its parts for greater
		 * accuracy. this will be changed if it proves too slow
		 */

		reconstructOutline();
		
	}

	/**
	 * Reconstructs the included GeneralPath which represents the
	 * outline of the DcImage2D. it will not usually be necessary
	 * to call this method manually, as it is already called
	 * when performing transforms
	 */
	public void reconstructOutline() {
		/* empty outline */
		outline.reset();

		/* add all parts */
		for (int i = 0; i < l.size(); i++) {
			outline.append(get(i), false);
		}
	}

	/**
	 * Returns the Outline color
	 * @return	the outline color
	 */
	public Color getOutlineColor() {
		return outlineColor;
	}

	/**
	 * Sets the outline color. this is the color used to paint the strokes
	 * @param 	c	the outline color
	 */
	public void setOutlineColor(Color c) {
		outlineColor = c;
	}

	/**
	 *Returns the Fill Color
	 * @return		the fill color
	 */
	public Color getFillColor() {
		return fillColor;
	}
	
	/**
	 * sets the fill color. this is the color used for the inside of the
	 * Image.
	 * @param c	the fill color
	 */
	public void setFillColor(Color c) {
		fillColor = c;
	}

	/**
	 * sets whether antialiasing should be applied when drawing this image.
	 *
	 * @param antiAlias	whether antialiasing should be applied
	 */
	public void setAntiAlias(boolean antiAlias) {
		antiAliasBoolean = antiAlias;
	}
}
/* END OF FILE */
            
            
           
