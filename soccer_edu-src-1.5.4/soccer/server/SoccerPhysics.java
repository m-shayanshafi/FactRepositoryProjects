/* SoccerPhysics.java
   This class simulates motions used in the Soccer Game
   
   Copyright (C) 2004  Yu Zhang

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the 
   Free Software Foundation, Inc., 
   59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package soccer.server;

import java.util.Enumeration;
import java.util.Vector;

import soccer.common.RefereeData;
import soccer.common.Util;
import soccer.common.Vector2d;

/**
 * @author Yu Zhang
 *
 */
public class SoccerPhysics {

	private double  COLLISION_ACCELERATION = 12.0*HeartOfWorld.SIM_STEP_SECONDS;		
	private SoccerWorld world;
	private Sball 		ball;
	private Vector 		bothTeams;

	private int 		stepID;
	private int 		mode;
	private boolean 	leftOff;	// if true, keep left team off the ball
	private boolean 	rightOff;	// if true, keep right team off the ball
	
	private double xp, yp; 		// player coordinates
	
	public SoccerPhysics(SoccerWorld world) {
		this.world = world;
	}

	//---------------------------------------------------------------------------
	/**
	 * Enforce rules of physics on current simulation step
	 */
	public void apply( 	int stepID, // so far is used for debuging only
						int mode, 
						boolean leftOff, 
						boolean rightOff, 
						int period) 
	{
		ball = world.ball;
		bothTeams = world.bothTeams;
		
		this.stepID = stepID;
		this.mode = mode;
		this.leftOff = leftOff;
		this.rightOff = rightOff;
		
		world.updatePlayerList();

		// a group of players trying to control the ball.
		// they are all close enough to the ball to kick it.
		// who eventually has the ball is randomly decided.
		Vector fighters = new Vector();

		// update player positions
		Enumeration players = bothTeams.elements();				
		while (players.hasMoreElements()) {
			Splayer player = (Splayer) players.nextElement();
			updatePlayer( player, fighters );
		}

		updateBallState( getLastBallController( fighters ) );
		/*
		// clear kickBall flags
		players = bothTeams.elements();				
		while (players.hasMoreElements()) {
			Splayer player = (Splayer) players.nextElement();
			player.setKickBall(false);			
		}
		*/
		if (period == RefereeData.PRE_GAME
				|| period == RefereeData.HALF_TIME) {
			ball.position.setXY(0, 0);
		}		
	}

	
	private void updatePlayer( Splayer player, Vector fighters ) 
	{
		//System.out.println(stepID + " updatePlayer " + player.id + "-" + player.side
						//+ " size = " + bothTeams.size() );
		
		// find out if player can have ball again after he kick it out
		if (player.noBall > 0)
			player.noBall--;

		// find out if player can communicate again after he shouts
		if (player.noWord > 0)
			player.noWord--;

		// the player's next position if nothing else happens
		// (used in several methods)
		xp = player.position.getX() + player.velocity.getX();
		yp = player.position.getY() + player.velocity.getY();

		// if the player is close to ball enough to be a potential ball controller
		if (ball.position.distance(xp, yp) < Sball.CONTROLRANGE
			&& player.noBall == 0)
			fighters.addElement(player);

		// the flag to indicate if there's collision between players
		boolean collision = updatePlayerPosition( player );
		updatePlayerVelAcc( player, collision );	
	}

	// this method updates player position
	private boolean updatePlayerPosition( Splayer player )
	{
		adjustXYOffBall( player.side );
		adjustXYOutsideField();
		
		// set the new player position     
		player.position.setXY(xp, yp);
		
		return collisionDetected( bothTeams, player );
	}
	
	
	// this method adjusts player position if it must be kept of the ball
	private void adjustXYOffBall( char side )
	{
		// if the player is not allowed to move close to ball
		double dis = world.ball.position.distance(xp, yp);
		
		boolean enforceOffBall = ( side == 'r' && rightOff 
								|| side == 'l' && leftOff );
		
		if (dis < SoccerWorld.RADIUS && enforceOffBall) {
			double x1 =
				(xp - world.ball.position.getX()) * SoccerWorld.RADIUS / dis;
			double y1 =
				(yp - world.ball.position.getY()) * SoccerWorld.RADIUS / dis;
			xp = world.ball.position.getX() + x1;
			yp = world.ball.position.getY() + y1;
		}
	}
	
	// this method adjusts player position if it leaves the field
	private void adjustXYOutsideField()
	{
		// if the player is moving outside of the soccer field
		if (Math.abs(xp) > SoccerWorld.LENGTH / 2 + SoccerWorld.SIDEWALK - 1) {
			if (xp > 0)
				xp = SoccerWorld.LENGTH / 2 + SoccerWorld.SIDEWALK - 1;
			else
				xp = - (SoccerWorld.LENGTH / 2 + SoccerWorld.SIDEWALK - 1);
		}
	
		if (Math.abs(yp) > SoccerWorld.WIDTH / 2 + SoccerWorld.SIDEWALK - 1) {
			if (yp > 0)
				yp = SoccerWorld.WIDTH / 2 + SoccerWorld.SIDEWALK - 1;
			else
				yp = - (SoccerWorld.WIDTH / 2 + SoccerWorld.SIDEWALK - 1);
		}		
	} 
	


	// this method determines whether the player collides
	private boolean collisionDetected( Vector team, Splayer player )
	{
		boolean collides = false;
		
		Splayer player2 = null;
		Enumeration players2 = null;
		
		players2 = team.elements();
		while (players2.hasMoreElements()) {
			player2 = (Splayer) players2.nextElement();
			boolean myself = (player.id == player2.id) 
				  			&& (player.side == player2.side);
			if ( player2.position.distance(xp, yp) < Splayer.COLLIDERANGE) {
				if ( !myself ) {
					collides = true;
				}
			}
		}
		
		return collides;
	}


	// this method updates player velocity and acceleration
	private void updatePlayerVelAcc( Splayer player, boolean collision )
	{
		if (collision) {
			//System.out.println("collision: player " + player.id + "-" + player.side );
			
			// bounce the player back if it collides; set velocity to zero and
			// apply some acceleration to the player in the opposite direction
			
			double speed = player.velocity.norm();
			double eps = 0.5/HeartOfWorld.SIM_STEP_SECONDS;
			if ( speed < eps )
				speed = eps;
			player.velocity.timesV(1.0/speed ); // almost unit vector
			// prevent player from getting stuck by randomization
			double accX = -player.velocity.getX() + 0.5*(Math.random() - 0.5);
			double accY = -player.velocity.getY() + 0.5*(Math.random() - 0.5);
			player.velocity.setXY(0, 0);
			player.acceleration.setXY( accX*COLLISION_ACCELERATION, 
										accY*COLLISION_ACCELERATION ); 
		}
		
		// increment player velocity 
		player.velocity.add(player.acceleration);
			
		// if the ball has been grabbed by the goalie, keep the player away 
		// from the ball (except the goalie himself)
		if ( world.ball.isGrabbed && !player.isGoalie ) {
			double myRadius = SoccerWorld.RADIUS/2.0;
			double dist = player.position.distance( world.ball.position );
			if (  dist < myRadius ) {
				double x1 =
					(xp - world.ball.position.getX()) * myRadius / dist;
				double y1 =
					(yp - world.ball.position.getY()) * myRadius / dist;
				double xnew = world.ball.position.getX() + x1;
				double ynew = world.ball.position.getY() + y1;
				player.position.setXY( xnew, ynew );
			}
		} 	

		if (!collision) { 
			// determine force to move the player and update acceleration			
			double force = 0;
			
			if (player.haveBall)
				force = player.getForce() * Splayer.DRIBBLEFACTOR;
			else
				force = player.getForce();
	
			if ( !player.isKickBall() ) {
				// set player's acceleration, a1 = (force * k1 - v0 * k2) * (1 +/- 0.05);
				player.acceleration.setX(
					(force
						* Math.cos(Util.Deg2Rad(player.getDirection()))
						* Splayer.K1
						- player.velocity.getX() * Splayer.K2)
						* (1 + 2 * (Math.random() - 0.5) * Splayer.RANDOM));
	
				player.acceleration.setY(
					(force
						* Math.sin(Util.Deg2Rad(player.getDirection()))
						* Splayer.K1
						- player.velocity.getY() * Splayer.K2)
						* (1 + 2 * (Math.random() - 0.5) * Splayer.RANDOM));
			} else {
				// set player's acceleration when force is 0, a1 = ( (- v0) * k2) * (1 +/- 0.05);
				player.acceleration.setX(
					(-player.velocity.getX() * Splayer.K2)
						* (1 + 2 * (Math.random() - 0.5) * Splayer.RANDOM));
	
				player.acceleration.setY(
					(-player.velocity.getY() * Splayer.K2)
						* (1 + 2 * (Math.random() - 0.5) * Splayer.RANDOM));
	
			}
		}
	}
	

	// this method returns the player, if any, who has last controlled the ball 
	private Splayer getLastBallController(Vector fighters)
	{
		Splayer player = null;
		int size = fighters.size();
		
		if (size > 0) {
			//System.out.println("Entering getLastBallController. fighters.size() = " + fighters.size() );
			int controller = (int) Math.floor(Math.random() * size);
			player = (Splayer) fighters.elementAt(controller);
			ball.controllerType = player.side;
			ball.controllerId = player.id;
			ball.free = false;
			player.haveBall = true;
			
			//if ( fighters.size() > 1 )
				//System.out.println(stepID + " selected controller = " + controller 
								//+ " of total " + fighters.size() 
								//+ ", player.id = " + player.id + "-" + player.side );

		} else {
			ball.free = true;
		}
		return player;
	}

	
	// this method updates the ball state variables
	private void updateBallState( Splayer ballPlayer )
	{		
		if ( mode == RefereeData.OFFSIDE_L 
				|| mode == RefereeData.OFFSIDE_R ) {
			ball.velocity.setXY( 0, 0 );
			ball.acceleration.setXY( 0, 0 );
			ball.isGrabbed = false;
			return;
	//<=====			
		}
		
		//System.out.println(stepID + " ballPlayer = " + ballPlayer );
		
		if (ballPlayer != null) {
			
			//System.out.println( stepID + " ballPlayer.id = " + ballPlayer.id 
							//+ ", kicking: " + ballPlayer.isKickBall() + " noBall=" + ballPlayer.noBall );
			
			if ( ballPlayer.isKickBall() ) {
				// the player is kicking the ball.
							
				// (this player cannot have ball in another NOBALL sec)
				ballPlayer.noBall =
					(int) (Sball.NOBALL / HeartOfWorld.SIM_STEP_SECONDS);

				// set the new ball direction and acceleration
				double kickDir =
					2 * (Math.random() - 0.5) * Splayer.KICKRANDOM
						+ ballPlayer.getDirection();
				ball.acceleration.setXY(
					ballPlayer.getForce()
						* Math.cos(Util.Deg2Rad(kickDir))
						* Sball.K1,
					ballPlayer.getForce()
						* Math.sin(Util.Deg2Rad(kickDir))
						* Sball.K1);
				// on each kick, the ball stops
				ball.velocity.setXY(0, 0);	
				
				//System.out.println( stepID + " Ball kicked:  v = " + ball.velocity );

			} else {	
				// player is dribbling the ball (as this is just visual effect, 
				// do not do this near the field borders)
				// it applies to the goalies only; the field players must be
				// taking care of close dribbling themselves
				//
				if ( ballPlayer.isGoalie ) {
					if ( Math.abs(ballPlayer.position.getX()) 
							< SoccerWorld.LENGTH / 2 - Sball.CONTROLRANGE &&
						Math.abs(ballPlayer.position.getY()) 
							< SoccerWorld.WIDTH / 2 - Sball.CONTROLRANGE ) {
						double angle = Math.random() * 2 * Math.PI;
						ball.position.setXY(
							ballPlayer.position.getX()
								+ (0.95*Sball.CONTROLRANGE - 0.5) * Math.cos(angle),
							ballPlayer.position.getY()
								+ (0.95*Sball.CONTROLRANGE - 0.5) * Math.sin(angle));
						ball.velocity.setXY(0, 0);
						ball.acceleration.setXY(0, 0);
					}
				}
			}

			ballPlayer.haveBall = false;
			
		} else  {	// the ball is under nobody's control
			
			ball.position.add(ball.velocity);
			ball.velocity.add(ball.acceleration);
			ball.acceleration.setXY(
				(-Sball.FRICTIONFACTOR)
					* ball.velocity.getX()
					* (1 + 2 * (Math.random() - 0.5) * Sball.RANDOM),
				(-Sball.FRICTIONFACTOR)
					* ball.velocity.getY()
					* (1 + 2 * (Math.random() - 0.5) * Sball.RANDOM));
			
			ball.isGrabbed = false;
			
			//System.out.println(stepID + " Ball is free:  v = " + ball.velocity );
		}
		//if ( fighters.size() > 0 )	
			//System.out.println("Leaving updateBallState. fighters.size() = " + fighters.size() );
	}

}

