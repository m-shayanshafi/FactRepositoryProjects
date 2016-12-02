/* Splayer.java

   Copyright (C) 2001  Yu Zhang

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

import java.net.*;

import soccer.common.RWLock;
import soccer.common.Player;

public class Splayer extends MovingObj 
{
	public int noBall = 0;

	// for each simulation step; p1 = p0 + V0; v1 = v0 + a0; 
	// a1 = (force * k1 - v0 * k2) * (1 +/- 0.05); to define a maximum speed, we have to satisfy
	// 0 = force * k1 - maxSpeed * k2;

	// player maximum speed (in m/s)
	public static double MAXSPEED = 7.5;

    // player maximal angular speed when it is not moving, degrees/sec
    public static double MAX_ANGULAR_SPEED = 360.0;

	// the time a player needs to reach full speed (in sec)
	// without friction
	public static double TIMETOMAX = 1;

	// player's maximum dash force 
	public static double MAXDASH = 100;

	// player's minimum dash force 
	public static double MINDASH = -30;

	// player's maximum kick force 
	public static double MAXKICK = 100;

	// player's minimum kick force (in the opposite direction)
	public static double MINKICK = -30;

	// K1 is the force factor, MAXSPEED speed divided by TIMETOMAX
	// MAXDASH * K1 * TIMETOMAX * (1 / SIM_STEP_SECONDS) = MAXSPEED * SIM_STEP_SECONDS
	public static double K1 =
		MAXSPEED
			* HeartOfWorld.SIM_STEP_SECONDS
			* HeartOfWorld.SIM_STEP_SECONDS
			/ TIMETOMAX
			/ MAXDASH;

	// K2 is the friction factor,
	// 0 = MAXDASH * k1 + MAXSPEED * SIM_STEP_SECONDS * K2;
	public static double K2 =
		MAXDASH / (MAXSPEED * HeartOfWorld.SIM_STEP_SECONDS) * K1;

	// max random factor for player movement
	public static double RANDOM = 0;

	// max dribble force factor, when player is dribbling, 
	//the max force he can use to dash is MAXDASH * DRIBBLE 
	public static double DRIBBLEFACTOR = 0.4;

	// kick direction random factor. When you decide to kick the ball to X direction,
	// the actual ball moving direction will be X +/- KICKRANDOM degrees. So, the closer to the
	// goal, the better chance to score.
	public static double KICKRANDOM = 3;

	// players collides when their distance is smaller than COLLIDERANGE
	public static double COLLIDERANGE = 0.6;

	// networking properties
	public InetAddress address;
	public int port;

	// player identifier
	public char side;
	public int id;
	
	// recommended, as using other varibles will be depricated
	public boolean isGoalie = false;
	public boolean isKicker = false;

	public boolean iAmOffside = false; // if the player is at Offside position  
	public boolean haveBall = false;
	// if the player has the ball under his control 
	// Once the player has sent the message, he has to wait at least NOWORD sec to   
	// communicate again
	public static double NOWORD = 30;
	public int 		noWord = 0;
	public boolean 	coach = false;	

	// dash/kick direction and force actually used in simulation
	private RWLock 	directionLock = null;
	public double 	direction = 0;
	private RWLock 	forceLock = null;
	private double 	force = 0;
	private RWLock 	kickBallLock = null;
	private boolean kickBall = false; // true if the player is trying to kick the ball
	private RWLock 	messageLock = null;
	private String 	message = null; // the message from player client

	private RWLock lastTimeLock = null;
	private int lastTime; // last time when viewer is active  

	
	// create dummy instance
	public Splayer()
	{
		super();
	}
	
	public Splayer( InetAddress address,
					int port,
					char side,
					int id,
					boolean isGoalie,
					boolean isKicker,
					int lastTime,
					boolean coach ) 
	{
		super();
		
		this.side = side;
		this.id = id;
		this.isGoalie = isGoalie;
		this.isKicker = isKicker;

		this.address = address;
		this.port = port;

		this.lastTime = lastTime;
		this.coach = coach;

		if (side == 'l') {
			position.setXY(
				-SoccerWorld.LENGTH / 4,
				3 * (5-id) );
			direction = 0;
		} else {
			position.setXY(
				SoccerWorld.LENGTH / 4,
				3 * (id-5) );
			direction = 180;
		}

		this.lastTimeLock = new RWLock();
		this.directionLock = new RWLock();
		this.forceLock = new RWLock();
		this.kickBallLock = new RWLock();
		this.messageLock = new RWLock();
	}


	/**
	 * @return
	 */
	public int getLastTime() {
		try {
			lastTimeLock.lockRead();
			return lastTime;
		} finally {
			lastTimeLock.unlock();
		}
	}

	/**
	 * @param i
	 */
	public void setLastTime(int i) {
		try {
			lastTimeLock.lockWrite();
			lastTime = i;
		} finally {
			lastTimeLock.unlock();
		}
	}

	/**
	 * @return
	 */
	public double getDirection() {
		try {
			directionLock.lockRead();
			return direction;
		} finally {
			directionLock.unlock();
		}
	}

	/**
	 * @return
	 */
	public double getForce() {
		try {
			forceLock.lockRead();
			return force;
		} finally {
			forceLock.unlock();
		}
	}

	/**
	 * @return
	 */
	public boolean isKickBall() {
		try {
			kickBallLock.lockRead();
			return kickBall;
		} finally {
			kickBallLock.unlock();
		}
	}

	/**
	 * @return
	 */
	public String getMessage() {
		try {
			messageLock.lockRead();
			return message;
		} finally {
			messageLock.unlock();
		}
	}

	/**
	 * @param d
	 */
	public void setDirection(double d) {
		try {
			directionLock.lockWrite();
			direction = d;
		} finally {
			directionLock.unlock();
		}
	}

	/**
	 * @param d
	 */
	public void setForce(double d) {
		try {
			forceLock.lockWrite();
			force = d;
		} finally {
			forceLock.unlock();
		}
	}

	/**
	 * @param b
	 */
	public void setKickBall(boolean b) {
		try {
			kickBallLock.lockWrite();
			kickBall = b;
		} finally {
			kickBallLock.unlock();
		}
	}

	/**
	 * @param string
	 */
	public void setMessage(String string) {
		try {
			messageLock.lockWrite();
			message = string;
		} finally {
			messageLock.unlock();
		}
	}

	
	// reset Splayer attributes using variables from the Player object 
	public void assign( Player player )
	{
		position.setXY( player.position );	
		direction = player.direction;
		velocity.setXY( 0, 0 );
		acceleration.setXY( 0, 0 );
	}

	// reset Splayer attributes using variables fom the Splayer object 
	public void copy( Splayer splayer )
	{
		position.setXY( splayer.position );	
		direction = splayer.direction;
		velocity.setXY( 0, 0 );
		acceleration.setXY( 0, 0 );
		side = splayer.side;
		id = splayer.id;
	}

}
