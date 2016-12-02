package thaigo.utility;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
/** Determines location for a component.
 *  
 * @author Nol
 *
 */
public class CenterDeterminer {

	/** Determines the location for the child component that will make both components have the same center.
	 * 
	 * @param mother Mother component
	 * @param child Child component
	 * @return Location for child component
	 */
	public static Point determine(Component mother , Component child){
		Point point = null;
		Dimension mSize = mother.getSize();
		Dimension cSize = child.getSize();
		
		int mainX = mother.getLocation().x;
		int mainY = mother.getLocation().y;
		
		int shiftX = (int)((mSize.getWidth() / 2) - (cSize.getWidth() / 2));
		int shiftY = (int)((mSize.getHeight() / 2) - (cSize.getHeight() / 2));
		if(( mSize.height >= cSize.height ) && ( mSize.width >= cSize.width )){
			point = new Point((mainX + shiftX),(mainY + shiftY));
		}
		else if(( mSize.height < cSize.height ) && ( mSize.width >= cSize.width )){
			point = new Point((mainX + shiftX),(mainY - shiftY));
		}
		else if(( mSize.height >= cSize.height ) && ( mSize.width < cSize.width )){
			point = new Point((mainX - shiftX),(mainY + shiftY));
		}
		else if(( mSize.height < cSize.height ) && ( mSize.width < cSize.width )){
			point = new Point((mainX - shiftX),(mainY - shiftY));
		}
		return point;
	}
	
	/** Determines the location for the component to be on the center of the screen. 
	 * 
	 * @param component Component to be determined
	 * @return Location for component
	 */
	public static Point determineWithScreen(Component component){
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - component.getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - component.getHeight()) / 2);
		return new Point(x,y);
	}
}
