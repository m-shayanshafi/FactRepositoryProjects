/* SoccerRules.java
   This class simulates rules used in the Soccer Game
   
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

	Modifications by Vadim Kyrylov 
							January 2006
*/
package soccer.server;

import java.util.Enumeration;
import java.util.Vector;

import soccer.common.*;

/**
 * @author Yu Zhang
 *
 */
public class SoccerRules 
{
	// the length of before_kick_off mode  in seconds
	public static int KICK_OFF_TIME = 5; 
	// the length of other paused modes  in seconds
	public static int TWROW_IN_TIME = 3; 

	private SoccerWorld 	world;
	private SoccerPhysics 	soccerPhysics;
	
	public int 				timer = 0; // before kick off timer
	
	// Game status
	private int 			period 	= RefereeData.NO_GAME;
	private int 			mode 	= RefereeData.BEFORE_KICK_OFF;

	public boolean 			leftOff = true;
	// A flag to indicate that left team players must be
	// 10 meters away from the ball
	public boolean 			rightOff = true;
	// A flag to indicate that right team players must be
	// 10 meters away from the ball
	public boolean 			refereeSignal = false;
	// A flag to indicate that referee is going to signal
	// all players and viewers the game situation
	public static boolean 	OFFSIDERULE = true;		
	
    // time in seconds allowed to keep the ball grabbed by the goalie 
    public static double MAX_GRABBED_TIME = 4.0;

    // number of cycles allowed keeping the ball grabbed by the goalie 
    public static int MAX_GRABBED_STEPS;

    // number of cycles to ignore offside rule after the corner kick
    public static int OFFSIDE_DELAY = 75;

	public int 				gameCount = 1;
	public int 				total_score_L = 0;    	// total left team's score
	public int 				total_score_R = 0;  	// total right team's score
	public int 				score_L = 0;    		// left team's score
	public int 				score_R = 0;  			// right team's score
	public int 				sqr_score_diff = 0;    	// sum of sqr score difference
	
	private int kickOffStepsToPause;		// wait this number of steps before continue
	private int otherStepsToPause;			// wait this number of steps before continue
	public char sideToContinue; 	// the side who continues the game after the pause

	public boolean 			offsideL = false; // one or more left players are at Offside position.
	public boolean 			offsideR = false; // one or more right players are at Offside position.
	
	private Sball ball;
	private Vector leftTeam, rightTeam, bothTeams;

	
	public SoccerRules(SoccerWorld world) 
	{
		//System.out.println("entered SoccerWorld");
		this.world = world;
		soccerPhysics = new SoccerPhysics(world);
		sideToContinue = '?'; 
		kickOffStepsToPause = (int)(KICK_OFF_TIME / HeartOfWorld.SIM_STEP_SECONDS);
		otherStepsToPause = (int)(TWROW_IN_TIME / HeartOfWorld.SIM_STEP_SECONDS);
		MAX_GRABBED_STEPS = (int)(MAX_GRABBED_TIME / HeartOfWorld.SIM_STEP_SECONDS);
	}

	
	public static void setMaxGrabbedSteps()
	{
		MAX_GRABBED_STEPS = (int)(MAX_GRABBED_TIME / HeartOfWorld.SIM_STEP_SECONDS);
	}
	
	//---------------------------------------------------------------------------
	// enforce soccer rules for each ticker     
	public void enforce( int stepID ) 
	{
		ball = world.ball;
		bothTeams = world.bothTeams;
		
		if ( enforceGameModes() ) { 
			
			// enforce the rules of the regular game play
			
			sideToContinue = '?';
			
			double x = ball.position.getX();
			double y = ball.position.getY();
			
			if (x < -SoccerWorld.LENGTH / 2) {
				
				enforceLeftGoalLine( x, y, stepID );
	
			} else if (x > SoccerWorld.LENGTH / 2) {		
	
				enforceRightGoalLine( x, y, stepID );
				
			} else if (Math.abs(y) > SoccerWorld.WIDTH / 2) {
				
				enforceThrowIns( x, y );
				
			} 
			
			if ( !refereeSignal ) {
				if ( ballGrabbedTooLong( stepID ) ) {
					
					System.out.println(stepID + " *** penalty: goalie " 
						+ ball.controllerType + " holding the ball for too long." );				
					// set the corner kick mode if the ball is grabbed for too long
					// (in the real soccer this rule is different)
					setCornerKick( y, world.sideGrabbedBall ); 
				
				} else if ( ballGrabbedOutsidePenaltyArea() ) {
				
					System.out.println(stepID + " *** penalty: goalie "
						+ ball.controllerType + " forced to release the grabbed ball." );				
					releaseGrabbedBall(); 	
					
				} else if ( OFFSIDERULE ) {
					
					// no offsides for awhile after the corner kick
					if ( (long)stepID - (long)world.stepCornerKickDecided 
										> (long)OFFSIDE_DELAY ) { 
						enforceOffSideRule();
						world.stepCornerKickDecided = -Integer.MAX_VALUE; 	// forget corner kick
					}
				}
			}	
		}
		
		if ( refereeSignal )
			printRefereeInfo( mode, stepID );	// display debug info
			
		// apply the laws of physics
		soccerPhysics.apply(stepID, mode, leftOff, rightOff, period);
	}

	
	// this method enforces standard game modes
	// returns true if no enforcement is needed
	private boolean enforceGameModes() 
	{
		boolean result = false;
		
		switch ( mode ) {
		
			case RefereeData.BEFORE_KICK_OFF: 
				
				try {
					// "before_kick_off" lasts for KICK_OFF_TIME seconds
					timer++;
					if ( timer > kickOffStepsToPause // "before_kick_off" ends
					 	|| world.replicationIsOn ) 	 // no delay if replication
					 {	
						timer = 0;
						if ( sideToContinue == 'l' ) { 
							mode = RefereeData.KICK_OFF_L;
							rightOff = true;
							leftOff = false;
						} else  if ( sideToContinue == 'r' ) {
							mode = RefereeData.KICK_OFF_R;
							rightOff = false;
							leftOff = true;
						}
						refereeSignal = true;
						teleportKicker( sideToContinue );
					} else {
						// keep all players off the ball on own side 
						ball.set(0, 0);
						rightOff = true;
						leftOff = true;
						teleportOffendingPlayers();
					}
				} catch (Exception e ) {
					// we may get here when not all clients have been comnnected
					System.out.println("Exception caught in enforceGameModes: " + e );
				}
			break;
		
			case RefereeData.KICK_OFF_L:
			case RefereeData.KICK_OFF_R:
				
				refereeSignal = true;
				mode = RefereeData.PLAY_ON;
				// allow all players approach the ball
				rightOff = false;
				leftOff = false;

			break;
			
			case RefereeData.CORNER_KICK_L:
			case RefereeData.THROW_IN_L:
			case RefereeData.GOAL_KICK_L:
			case RefereeData.OFFSIDE_R: 
			
				if (ball.controllerType == 'l') {
					mode = RefereeData.PLAY_ON;
					rightOff = false;
					leftOff = false;
					refereeSignal = true;
				} else {
					// wait until left player gets to the ball
					rightOff = true;
					leftOff = false;
				}
			break;
		
			case RefereeData.CORNER_KICK_R:
			case RefereeData.THROW_IN_R:
			case RefereeData.GOAL_KICK_R:
			case RefereeData.OFFSIDE_L: 
			
				if (ball.controllerType == 'r') {
					mode = RefereeData.PLAY_ON;
					rightOff = false;
					leftOff = false;
					refereeSignal = true;
				} else {
					// wait until right player gets to the ball
					rightOff = false;
					leftOff = true;
				}
			break;
			
			case RefereeData.PLAY_ON: 
				result = true;
			break; 
		}
		return result;
	}

	
	
	private void enforceLeftGoalLine( double x, double y, int stepID )
	{
		if (Math.abs(y) < SoccerWorld.GOAL_WIDTH / 2) // right team scores
			{
			if (period == RefereeData.FIRST_HALF
				|| period == RefereeData.SECOND_HALF) {
				total_score_R++;
				score_R++;
				System.out.println( "Right team scores the goal. " 
										+ total_score_L +":" + total_score_R );
			}
			mode = RefereeData.BEFORE_KICK_OFF;
			timer = 0;
			ball.set(0, 0);
			rightOff = false;
			leftOff = true;
			sideToContinue = 'r';
		} else if (
			ball.controllerType == 'l') // right team corner kick
			{
			mode = RefereeData.CORNER_KICK_R;
			world.stepCornerKickDecided = stepID; 
			if (y > 0)
				ball.set(-SoccerWorld.LENGTH / 2 + 1, SoccerWorld.WIDTH / 2 - 1);
			else
				ball.set(-SoccerWorld.LENGTH / 2 + 1, -SoccerWorld.WIDTH / 2 + 1);
			rightOff = false;
			leftOff = true;
			sideToContinue = 'r';
		} else if (ball.controllerType == 'r') // left team goal kick
			{
			mode = RefereeData.GOAL_KICK_L;
			if (y > 0)
				ball.set(
					-SoccerWorld.LENGTH / 2 + SoccerWorld.PENALTY_DEPTH,
				SoccerWorld.PENALTY_WIDTH / 2);
			else
				ball.set(
					-SoccerWorld.LENGTH / 2 + SoccerWorld.PENALTY_DEPTH,
					-SoccerWorld.PENALTY_WIDTH / 2);
			rightOff = true;
			leftOff = false;
			sideToContinue = 'l';
		}
		refereeSignal = true;
	}	
	
	private void enforceRightGoalLine( double x, double y, int stepID )
	{
		if (Math.abs(y) < SoccerWorld.GOAL_WIDTH / 2) // left team scores
			{
			if (period == RefereeData.FIRST_HALF
				|| period == RefereeData.SECOND_HALF) {
				total_score_L++;
				score_L++;
				System.out.println( "Left team scores the goal. " 
										+ total_score_L +":" + total_score_R );
			}
			mode = RefereeData.BEFORE_KICK_OFF;
			timer = 0;
			ball.set(0, 0);
			rightOff = true;
			leftOff = false;
			sideToContinue = 'l';
		} else if (ball.controllerType == 'l') // right team goal kick
			{
			mode = RefereeData.GOAL_KICK_R;
			if (y > 0)
				ball.set(SoccerWorld.LENGTH / 2 - SoccerWorld.PENALTY_DEPTH, SoccerWorld.PENALTY_WIDTH / 2);
			else
				ball.set(
				SoccerWorld.LENGTH / 2 - SoccerWorld.PENALTY_DEPTH,
					-SoccerWorld.PENALTY_WIDTH / 2);
			rightOff = false;
			leftOff = true;
			sideToContinue = 'r';
		} else if (ball.controllerType == 'r') // left team corner kick
			{
			mode = RefereeData.CORNER_KICK_L;
			world.stepCornerKickDecided = stepID; 
			if (y > 0)
				ball.set(SoccerWorld.LENGTH / 2 - 1, SoccerWorld.WIDTH / 2 - 1);
			else
				ball.set(SoccerWorld.LENGTH / 2 - 1, -SoccerWorld.WIDTH / 2 + 1);
			rightOff = true;
			leftOff = false;
			sideToContinue = 'l';
		}
		refereeSignal = true;
	}
	
	
	private void enforceThrowIns( double x, double y )
	{
		//System.out.println("enforceThrowIns: controllerType = " + ball.controllerType );
		 
		if (ball.controllerType == 'r') // left team throw in
			{
			mode = RefereeData.THROW_IN_L;
			if (y > 0)
				ball.set(x, SoccerWorld.WIDTH / 2 - 1);
			else
				ball.set(x, -SoccerWorld.WIDTH / 2 + 1);
			world.throwInModeL = true;
			rightOff = true;
			leftOff = false;
			sideToContinue = 'l';
		} else if (ball.controllerType == 'l') // right team throw in
			{
			mode = RefereeData.THROW_IN_R;
			if (y > 0)
				ball.set(x, SoccerWorld.WIDTH / 2 - 1);
			else
				ball.set(x, -SoccerWorld.WIDTH / 2 + 1);
			world.throwInModeR = true;
			rightOff = false;
			leftOff = true;
			sideToContinue = 'r';
		}
		refereeSignal = true;
	} 
				

	private void enforceOffSideRule()	
	{	
		//System.out.println("entered enforceOffSideRule" );
		
		// variable used to loop
		Enumeration players = null;
		Splayer player = null;
		double lastL, secondL, tmpL, posX;
		double lastR, secondR, tmpR;

		// determine the last and the second last two defenders in each team
		lastL = world.LENGTH/2;
		secondL = world.LENGTH/2;
		lastR = -lastL;
		secondR = -secondL;

		players = bothTeams.elements();
		
		while (players.hasMoreElements()) {
			
			player = (Splayer) players.nextElement();
			
			if ( player.side == 'l' ) {
				if ( player.position.getX() < secondL ) {
					secondL = player.position.getX();
					if (secondL < lastL) {
						tmpL = lastL;
						lastL = secondL;
						secondL = tmpL;
					}
				}
			} else if ( player.side == 'r' ) {
				if ( player.position.getX() > secondR ) {
					secondR = player.position.getX();
					if (secondR > lastR) {
						tmpR = lastR;
						lastR = secondR;
						secondR = tmpR;
					}
				}
			}
		}
		
		// for each player in each team, decide if any of them is at Offside
		players = bothTeams.elements();
		offsideR = false;
		offsideL = false;
		
		while (players.hasMoreElements()) {
			player = (Splayer) players.nextElement();
			player.iAmOffside = false;
			posX = player.position.getX();
			if ( player.side == 'r' ) {
				if (posX < 0
					&& posX < secondL
					&& ball.position.getX() < 0
					&& !ball.free
					&& posX < ball.position.getX()) {
					player.iAmOffside = true;
					offsideR = true;
					if (ball.controllerType == 'r'
						&& ball.controllerId != player.id) {
						mode = RefereeData.OFFSIDE_R;
						refereeSignal = true;
						sideToContinue = 'l';
					}
				}
			} else if ( player.side == 'l' ) {
				if (posX > 0
					&& posX > secondR
					&& ball.position.getX() > 0
					&& !ball.free
					&& posX > ball.position.getX()) {
					player.iAmOffside = true;
					offsideL = true;
					if (ball.controllerType == 'l'
						&& ball.controllerId != player.id) {
						mode = RefereeData.OFFSIDE_L;
						refereeSignal = true;
						sideToContinue = 'r';
					}
				}
			}
		}
	}
				

	private boolean ballGrabbedTooLong( int stepID )
	{
		// this can be out of range for 'int'
		long stepsHoldingBall = (long)stepID - (long)world.stepBallWasGrabbed;
		
		//if ( stepsHoldingBall > 0 )
			//System.out.println(stepID + " ball grabbed at: " 
				//+ world.stepBallWasGrabbed + ", kept for " + stepsHoldingBall );
		return ( stepsHoldingBall	> (long)MAX_GRABBED_STEPS );	
	}

	
	// set the corner kick mode 
	private void setCornerKick( double y, char offenderSide )
	{
		if ( offenderSide == 'r' ) {
			mode = RefereeData.CORNER_KICK_L;
			if (y > 0)
				ball.set(SoccerWorld.LENGTH / 2 - 1, SoccerWorld.WIDTH / 2 - 1);
			else
				ball.set(SoccerWorld.LENGTH / 2 - 1, -SoccerWorld.WIDTH / 2 + 1);
			rightOff = true;
			leftOff = false;
			sideToContinue = 'l';
		} else {
			mode = RefereeData.CORNER_KICK_R;
			if (y > 0)
				ball.set(-SoccerWorld.LENGTH / 2 + 1, SoccerWorld.WIDTH / 2 - 1);
			else
				ball.set(-SoccerWorld.LENGTH / 2 + 1, -SoccerWorld.WIDTH / 2 + 1);
			rightOff = false;
			leftOff = true;
			sideToContinue = 'r';
		}
		
		refereeSignal = true;
	}
	
	
	// this method returns true if the ball is grabbed
	// outside the penalty area.
	private boolean ballGrabbedOutsidePenaltyArea()
	{
		return ( ball.isGrabbed 
				&& !world.inPenaltyArea( 'l', ball.position, 0 )
				&& !world.inPenaltyArea( 'r', ball.position, 0 ) );
	}

	// this method generates a random kick by the goalie; 
	// the direction of the kick is random and the force is just moderate so
	// that this kick was kind of penalty. No referee signal is generated.
	private void releaseGrabbedBall()
	{
		ball.isGrabbed = false;
		world.stepBallWasGrabbed 
						= Integer.MAX_VALUE;	// forget ball was grabbed
		Splayer goalie 
			= world.getPlayer( ball.controllerType, ball.controllerId );
			
		if ( goalie != null ) {
			goalie.setForce(Splayer.MAXKICK/3.0 );
			double kickDir = (Math.random() - 0.5) * 135.0; 
			if ( goalie.side == 'r' )
				kickDir = Util.normal_dir( kickDir + 180 );
			goalie.setDirection( kickDir );
			goalie.setKickBall( true );	
			//System.out.println("releaseGrabbedBall: goalie side=" 
					//+ ball.controllerType + " id=" + ball.controllerId 
					//+ " kickDir = " + (float)kickDir );
		} else {
			System.out.println("Error in releaseGrabbedBall: goalie cannot be found");
		}
	}

	
	// this method moves the kicker closer to the ball in the kickoff location
	private void teleportKicker( char side )
	{
		Splayer player = getKicker( side );
		
		double x = 0;

		if ( side == 'l' ) {
			x = -2.0;
		} else if ( side == 'r' ) {
			x =  2.0;
		}
		if ( side == 'l' || side == 'r' )
			player.position.setXY( new Vector2d( x, 0 ) );
		
		//System.out.println("Kicker teleported: side = " 
							//+ player.side + "  id = " + player.id);
		
	}

	// this method moves the kicker closer to the ball for the 11m penalty kick
	private void teleport11mKicker( char side ) 
	{
		Splayer player = getKicker( side );
		
		double x;

		if ( side == 'l' ) {
			x = world.LENGTH/2 - 11 - 2.5;
		} else {
			x =  -(world.LENGTH/2 - 11 - 2.5);
		}
		
		player.position.setXY( new Vector2d( x, 0 ) );
	}
	
	
	// this method returns the kicker from given side/team
	private Splayer getKicker( char side ) 
	{
		Splayer player = null;
		Splayer player9 = null;
		boolean kickerFound = false;
		
		int i = 0;
		for (i = 0; i < bothTeams.size(); i++ ) {
			player = (Splayer) bothTeams.elementAt( i );
							
			if ( player.isKicker && player.side == side ) {
				kickerFound = true;
				break;
			}
			if ( player.id == 9 && player.side == side )
				player9 = player;
		}
		
		if ( kickerFound )
			return player;
		else 
			return player9;
	}
	
		
	// this method teleports players violating the kicikoff rule
	private void teleportOffendingPlayers()
	{
		Enumeration players = null;
		Splayer player = null;
		
		players = bothTeams.elements();
		int i = 0;
		while (players.hasMoreElements()) {
			player = (Splayer) players.nextElement();
			if ( player.side == 'l' ) {
				if ( player.position.getX() > -0.1 )
					player.position.setX( -5.0 );
			} else 	if ( player.side == 'r' ) {
				if ( player.position.getX() < 0.1 )
					player.position.setX( 5.0 );
			}
			i++; 
		}		
	}
	

	// ** not in use yet;
	// this methods moves all players outside penalty area on opposite side;
	// goalies are not moved
	private void teleportFor11mPenaltyKick( char side ) 
	{
		System.out.println("teleportFor11mPenaltyKick: side = " + side );
		
		// teleport ball
		double x;
		char side2;	// the penalty kick will be on this side of the field

		if ( side == 'l' ) {
			x = world.LENGTH/2 - 11.0;
			side2 = 'r';
		} else {
			x =  -(world.LENGTH/2 - 11.0);
			side2 = 'l';
		}		
		ball.set(x, 0);
		
		// teleport players 
		Enumeration players = bothTeams.elements();
		Splayer player = null;
		int i = 0;
		
		while (players.hasMoreElements()) {
			player = (Splayer) players.nextElement();
			if ( !player.isGoalie ) {
				if ( world.inPenaltyArea( side2, player.position, -0.1 ) ) {
					
					System.out.println("teleporting: id=" + player.id 
						+ " side=" + player.side + " pos=" + player.position );
					
					if ( i%2 == 0 ) {
						if ( side2 == 'l' )
							x = world.LENGTH/2 - world.PENALTY_DEPTH;
						else
							x = -(world.LENGTH/2 - world.PENALTY_DEPTH);						
						player.position.setX( x );
					} else { 
						double y;
						if ( player.position.getY() > 0 )
							y =  world.PENALTY_WIDTH/2;
						else
							y = -world.PENALTY_WIDTH/2;						
						player.position.setY( y );
					}
					
					System.out.println("new pos=" + player.position );
				}
			}
			i++; 
		}
	}



	//=========  public get/set methods  =========
	 
	public synchronized int getPeriod() 
	{
		return period;
	}

	public synchronized void setPeriod(int i) 
	{
		if ( period != i ) {
			refereeSignal = true; // as period has changed
			period = i;
			mode = RefereeData.BEFORE_KICK_OFF;
			timer = 0;
			if ( period == RefereeData.FIRST_HALF ) 
				sideToContinue = 'l';
			else if ( period == RefereeData.SECOND_HALF ) 
				sideToContinue = 'r';
			else
				sideToContinue = '?';
		}
	}

	 
	public synchronized int getMode() 
	{
		return mode;
	}

	public synchronized void setMode(int i) 
	{
		if ( mode != i ) {
			refereeSignal = true; // as mode has changed
			mode = i;
			timer = 0;
		}
	}

	// this method sets the standard game state for replication
	public void setReplicationMode()
	{
		period = RefereeData.FIRST_HALF;
		setMode( RefereeData.PLAY_ON );	
		rightOff = false;
		leftOff = false;
		sideToContinue = '?';
		timer = 0;
	}


	// print out the referee info
	private void printRefereeInfo( int mode, int stepID )
	{
		switch ( mode ) {
			case RefereeData.KICK_OFF_R:
				System.out.println( stepID + " RIGHT team kicks off. " );
			break;
			case RefereeData.KICK_OFF_L:
				System.out.println( stepID + " LEFT team kicks off." );
			break;
			
			case RefereeData.GOAL_KICK_R:
				System.out.println( stepID + " RIGHT team kicks from own goal." );
			break;
			case RefereeData.GOAL_KICK_L:
				System.out.println( stepID + " LEFT team kicks from own goal." );
			break;
			
			case RefereeData.PLAY_ON:
				//System.out.println( stepID + " Resume playing game." );
			break;
							
			case RefereeData.CORNER_KICK_R:
				System.out.println( stepID + " <<== corner kick on the LEFT side." );
			break;
			case RefereeData.CORNER_KICK_L:
				System.out.println( stepID + " ==>> corner kick on the RIGHT side." );
			break;
			
			case RefereeData.THROW_IN_R:
				System.out.println( stepID + " -- throw in by the RIGHT team." );
			break;
			case RefereeData.THROW_IN_L:
				System.out.println( stepID + " -- throw in by the LEFT team." );
			break;
			
			case RefereeData.OFFSIDE_R:
				System.out.println( stepID + " >>> Right team offside ***");
			break;
			case RefereeData.OFFSIDE_L: 
				System.out.println( stepID + " <<< Left team offside ***");
			break;
		}
	}

}
