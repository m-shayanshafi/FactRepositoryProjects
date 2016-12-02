/* VController.java

   by Vadim Kyrylov
   January 2006
*/
/* 
 * object of this class provides means for moving the ball and players
 * around by the mouse between steps
 *
*/
 
package soccer.client;

import java.awt.event.*;
import java.io.*;
import soccer.client.view.*;
import soccer.client.view.j3d.FieldJ3D;
import soccer.common.*;

public class VController extends Controller
{
	public boolean ballGrabbed = false;
	public boolean leftPlayerGrabbed = false;
	public boolean rightPlayerGrabbed = false;
	public int grabbedPlayerID = -1;
	
	private Field arena;
	private final double  R = 6;

	
	public VController(World world, SoccerMaster soccerMaster) 
	{
		this.world = world;
		this.soccerMaster = soccerMaster;
		this.arena = soccerMaster.arena2D;
	}

	
	// this method returns true is either the ball or some player was grabbed
	private boolean objectGrabbed( double x, double y ) 
	{		
		clearGrabFlags();
		
		Vector2d cursor = new Vector2d( x, y ); 
		
		Vector2d center = new Vector2d(); 
		center.setXY( world.ball.position );
		arena.soccer2user( center );
		if ( center.distance( cursor ) < R ) {
			ballGrabbed = true;
			return true;
		}
		
		for ( int i=0; i<world.leftTeam.size(); i++ ) {
			Player player = (Player)world.leftTeam.elementAt( i ); 
			center.setXY( player.position ); 
			arena.soccer2user( center );
			if ( center.distance( cursor ) < R ) {
				leftPlayerGrabbed = true;
				grabbedPlayerID = i;	// this is the location in the vector, not ID
				//System.out.println("Left player " + i + " grabbed");
				return true;
			}
		}
		
		for ( int i=0; i<world.rightTeam.size(); i++ ) {
			Player player = (Player)world.rightTeam.elementAt( i ); 
			center.setXY( player.position ); 
			arena.soccer2user( center );
			if ( center.distance( cursor ) < R ) {
				rightPlayerGrabbed = true;
				grabbedPlayerID = i;	// this is the location in the vector, not ID
				//System.out.println("Right player " + i + " grabbed");
				return true;
			}
		}
		
		return false;
	}

	public void clearGrabFlags() 
	{
		ballGrabbed = false;
		leftPlayerGrabbed = false;
		rightPlayerGrabbed = false;
		grabbedPlayerID = -1;
	}	
	
	public void updateGrabFlags( boolean updateBall ) 
	{
		if ( updateBall )
			// ball
			world.ball.isGrabbed = ballGrabbed;
			
		// left team	
		for ( int i=0; i<world.leftTeam.size(); i++ ) {
			Player player = (Player)world.leftTeam.elementAt( i ); 
			player.isGrabbed = ( leftPlayerGrabbed && grabbedPlayerID == i );
		}
		// right team
		for ( int i=0; i<world.rightTeam.size(); i++ ) {
			Player player = (Player)world.rightTeam.elementAt( i ); 
			player.isGrabbed = ( rightPlayerGrabbed && grabbedPlayerID == i );
		}
		
	}
	

	//*** Handle mouse events ***
	// once mouse is pressed, check whether an object was grabbed
	public void mousePressed(MouseEvent e) 
	{ 
		if ( soccerMaster.getGState() == GState.WAITING ) {
			if(soccerMaster.isIn3D())	
			{
				soccerMaster.arena3D.requestFocus();
				((FieldJ3D)(soccerMaster.arena3D)).myCanvas3D.requestFocus();
			}
			else {
				arena.requestFocus();
			} 
	
			if ( e.getModifiers() == InputEvent.BUTTON1_MASK ) {
				// left button clicked
				double x = e.getX();
				double y = e.getY();
				if ( objectGrabbed( x, y ) ) {
						
				}
				// just for the immediate display, clear all isGrabbed flags 
				updateGrabFlags( true );
						
				arena.repaint();
			} 
		}	
	}

	// once mouse is dragged, send message to server 
	// to teleport the grabbed object
	public void mouseDragged(MouseEvent e) 
	{
		if ( soccerMaster.getGState() == GState.WAITING ) {
			
			if ( ballGrabbed || leftPlayerGrabbed || rightPlayerGrabbed ) {
				
				char objType ='?';
				int playerID = -1;
				
				// determine object type and ID
				if ( ballGrabbed ) {
					objType = TeleportData.BALL;
				} else {
					Player player = null;
					if ( leftPlayerGrabbed ) {
						objType = TeleportData.LEFT_PLAYER;
						player = (Player)world.leftTeam.elementAt( grabbedPlayerID ); 
					} else if ( rightPlayerGrabbed ) {
						objType = TeleportData.RIGHT_PLAYER;
						player = (Player)world.rightTeam.elementAt( grabbedPlayerID ); 
					}	
					playerID = player.id;						
				}
				
				// determine the new position of the grabbed object
				Vector2d newpos = new Vector2d( e.getX(), e.getY() ); 
				arena.user2soccer( newpos ); 
				TeleportData sentData = 
						new TeleportData( objType,
										  playerID,
										  newpos.getX(),
										  newpos.getY()  );
									
				Packet command =
					new Packet(
						Packet.TELEPORT,
						sentData,
						soccerMaster.getAddress(),
						soccerMaster.getPort() );
				try {
					soccerMaster.getTransceiver().send(command);
				} catch (Exception ex ) {
					System.out.println("Error sending Packet.TELEPORT " + ex );
				}

			}
		}
	}
	
	public void mouseReleased(MouseEvent e) { 
	
	}



	public void mouseClicked(MouseEvent e) { }
	public void mouseMoved(MouseEvent e) { }

	//*** End of mouse events ***


}
