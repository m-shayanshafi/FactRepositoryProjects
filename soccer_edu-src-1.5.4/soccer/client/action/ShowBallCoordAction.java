/* ShowBallCoordAction.java
   
 	by Vadim Kyrylov
 	February, 2006
*/

package soccer.client.action;

/**
 *
 */
public class ShowBallCoordAction extends AbstractToggleAction 
{
	public ShowBallCoordAction() 
	{
		super();
		putValue(NAME, "Display Ball Coordinates");
		//setAccelerator( KeyEvent.VK_G, Event.CTRL_MASK );
		setEnabled(true);
		setToggledOn(false);
	}
	/**
	 * The toggle was changed
	 */
	protected void toggleStateChanged() 
	{
		getSoccerMaster().setShowBallCoord( isToggledOn() );
	}
}
