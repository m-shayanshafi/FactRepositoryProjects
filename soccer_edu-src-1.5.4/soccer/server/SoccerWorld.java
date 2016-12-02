/* World.java
   This class simulates the rules used in Soccer Game
   
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

import java.net.InetAddress;
import java.util.*;

import soccer.common.*;

public class SoccerWorld {

	// Player/Viewer Number allowed
	public static int TEAM_FULL = 11;
	// the maximum number of clients for each team
	public static int VIEWER_FULL = 10;
	// the maximum number of viewer clients

	// Soccer Field data (in meter)
	public static double LENGTH = 100;	// use even value for this parameter
	public static double WIDTH = 66;	// use even value for this parameter
	public static double SIDEWALK = 5;
	public static double RADIUS = 9;
	public static double GOAL_DEPTH = 2;
	public static double GOAL_WIDTH = 8;
	public static double GOALAREA_WIDTH = 18;
	public static double GOALAREA_DEPTH = 6;
	public static double PENALTY_WIDTH = 40;
	public static double PENALTY_DEPTH = 16;
	public static double PENALTY_CENTER = 12;
	public static double CORNER = 1;

	// Team names
	public static String LEFTNAME = "Smarter";
	public static String RIGHTNAME = "Smart";

	// Available Id numbers for future players and viewers
	public Stack 	viewerAvailable;
	public Stack 	leftAvailable;
	public Stack 	rightAvailable;

	// Ball and team data 
	public  Sball 	ball;
    public int 		stepBallWasGrabbed; 
    public int 		stepCornerKickDecided; 
    public char 	sideGrabbedBall;
	public Vector 	leftTeam;
	public Vector 	rightTeam;
	public Vector 	leftInitPos;
	public Vector 	rightInitPos;

	// this vector has been created in order to ensure complete symmetry
	public Vector 	bothTeams;
	
	// Ball and team replication data  
	public Sball 	ballSaved;
	public Vector 	leftTeamSaved;
	public Vector 	rightTeamSaved;
	public boolean  replicationIsOn = false; // replication mode flag
    public int 		stepBallWasGrabbedSaved; 
    public int 		stepCornerKickDecidedSaved; 
    public char 	sideGrabbedBallSaved;

	public boolean throwInModeL = false; 	// keeps track of throwins
	public boolean throwInModeR = false; 	// keeps track of throwins
	
	private Vector 	viewers;
	

	/** 
	 * This vector is used to keep new players which should be added
	 * to the simulation
	 */
	private Vector addedPlayers = new Vector();
	/** 
	 * This vector is used to keep players which should be removed
	 * to the simulation
	 */
	private Vector removedPlayers = new Vector();

	private Vector addedViewers = new Vector();
	private Vector removedViewers = new Vector();

	//---------------------------------------------------------------------------
	/**
	 * Constructor
	 */
	public SoccerWorld() 
	{
		// Set up available ID numbers 
		leftAvailable = new Stack();
		rightAvailable = new Stack();
		viewerAvailable = new Stack();

		for (int i = TEAM_FULL; i >= 1; i--) {
			Integer num = new Integer(i);
			leftAvailable.push(num);
			num = new Integer(i);
			rightAvailable.push(num);
		}

		for (int i = VIEWER_FULL; i >= 1; i--) {
			Integer num = new Integer(i);
			viewerAvailable.push(num);
		}

		// initialize team player and viewers vector
		leftTeam = new Vector();
		rightTeam = new Vector();
		leftTeamSaved = new Vector();
		rightTeamSaved = new Vector();
		viewers = new Vector();

		// initialize the ball
		ball = new Sball();
		ballSaved = new Sball();
		stepBallWasGrabbed = Integer.MAX_VALUE;
		stepCornerKickDecided = -Integer.MAX_VALUE;
		
		// assign default positions to players on both teams.
		// (this can me overriden by the player clients, once 
		// they are connected)
		setInitPlayerPos();
	}


	// this method makes a working copy of both teams with the purpose 
	// to ensure symmetry on the average.
	// in even cycles, the first player is copied from the left team;
	// in the odd cycles, the first player is copied from the right team
	
	public void copyTeams( int time ) 
	{
		int turn = time%2;
		try {
			bothTeams = new Vector(); 
			int size = Math.max( leftTeam.size(), rightTeam.size() );
			for (int i = 0; i < size; i++ ) {
				if ( turn == 0 ) {
					if ( i < leftTeam.size() ) {
						Splayer playerL = (Splayer)leftTeam.elementAt( i );
						bothTeams.addElement( playerL );
					}	
				}
				if ( i < rightTeam.size() ) {
					Splayer playerR = (Splayer)rightTeam.elementAt( i );
					bothTeams.addElement( playerR );
				}
				if ( turn != 0 ) {
					if ( i < leftTeam.size() ) {
						Splayer playerL = (Splayer)leftTeam.elementAt( i );
						bothTeams.addElement( playerL );
					}
				}	
			}
		} catch (Exception e) {
			System.out.println("Exception caught in copyTeams() \n" + e );	
		}
	}
	
	
	// this method restores the situation from the saved snapshot
	public void restoreSituation() 
	{
		// restore ball data
		ball.copy( ballSaved );
		
		// restore left team cordinate data 
		restoreTeam( leftTeamSaved, leftTeam );

		// restore right team cordinate data
		restoreTeam( rightTeamSaved, rightTeam );
		
    	stepBallWasGrabbed = stepBallWasGrabbedSaved; 
    	stepCornerKickDecided = stepCornerKickDecidedSaved; 
    	sideGrabbedBall = sideGrabbedBallSaved;

		//System.out.println("Situation restored. leftTeam.size()=" 
				//+ leftTeam.size() + " rightTeam.size()=" + rightTeam.size() );
	}


	// this method restores team cordinate data 
	private void restoreTeam( Vector savedTeam, Vector team )
	{
		for ( int i = 0; i < savedTeam.size(); i++ ) {
			try {
				Splayer splr = (Splayer)team.elementAt(i);
				Splayer splrSaved = (Splayer)savedTeam.elementAt(i);
				splr.copy( splrSaved );
				
				//System.out.println("team.elementAt(" + i + ") restored."
								//+ splr.position ); 
			} catch (Exception e ) {}
		}
	}


	// this method saves current situation to ballSaved,
	// leftTeamSaved, and rightTeamSaved
	public void initSavedSituation() 
	{
		// save ball data
		ballSaved.copy( ball );
		
		// init left team cordinate data
		for ( int i = 0; i < leftTeam.size(); i++ ) {
			Splayer splr = (Splayer)leftTeam.elementAt(i);
			Splayer splrSaved = new Splayer();
			splrSaved.copy( splr );
			leftTeamSaved.addElement(splrSaved);
		}

		// init right team cordinate data
		for ( int i = 0; i < rightTeam.size(); i++ ) {
			Splayer splr = (Splayer)rightTeam.elementAt(i);
			Splayer splrSaved = new Splayer();
			splrSaved.copy( splr );
			rightTeamSaved.addElement(splrSaved);
		}

		saveStepIDs();

		System.out.println("Situation saved. leftTeam.size()=" 
				+ leftTeam.size() + " rightTeam.size()=" + rightTeam.size() );
	}

	private void saveStepIDs()
	{
    	stepBallWasGrabbedSaved = stepBallWasGrabbed; 
    	stepCornerKickDecidedSaved = stepCornerKickDecided; 
    	sideGrabbedBallSaved = sideGrabbedBall;
	}


	// get a left player Id for the new connected player   
	public int getLeftPlayerId() {
		int Id;
		try {
			Id = ((Integer) leftAvailable.pop()).intValue();
		} catch (EmptyStackException e) {
			Id = 0;
		}

		return Id;
	}

	//---------------------------------------------------------------------------    
	// return a left player Id   
	public void returnLeftPlayerId(int id) {
		Integer num = new Integer(id);
		leftAvailable.push(num);
	}

	// get a right player Id for the new connected player  
	public int getRightPlayerId() {
		int Id;
		try {
			Id = ((Integer) rightAvailable.pop()).intValue();
		} catch (EmptyStackException e) {
			Id = 0;
		}

		return Id;
	}

	//---------------------------------------------------------------------------
	// return a right player Id   
	public void returnRightPlayerId(int id) {
		Integer num = new Integer(id);
		rightAvailable.push(num);
	}

	//---------------------------------------------------------------------------
	// get a viewer Id for the new connected viewer       
	public int getViewerId() {
		int Id;
		try {
			Id = ((Integer) viewerAvailable.pop()).intValue();
		} catch (EmptyStackException e) {
			Id = 0;
		}

		return Id;
	}

	//---------------------------------------------------------------------------    
	// return a viewer Id   
	public void returnViewerId(int id) 
	{
		Integer num = new Integer(id);
		viewerAvailable.push(num);
	}

	//---------------------------------------------------------------------------
	// get the player by team side and Id
	public synchronized Splayer getPlayer(char type, int id) 
	{
		Enumeration players = null;
		Splayer player = null;

		if (type == 'l') {
			players = leftTeam.elements();
			while (players.hasMoreElements()) {
				player = (Splayer) players.nextElement();
				if (player.id == id)
					return player;
			}
			return null;
		} else if (type == 'r') {
			players = rightTeam.elements();
			while (players.hasMoreElements()) {
				player = (Splayer) players.nextElement();
				if (player.id == id)
					return player;
			}
			return null;
		} else
			return null;
	}

	// get the goalie by team side 
	public synchronized Splayer getGoalie(char type) 
	{
		//System.out.println("entered getGoalie");
		
		Enumeration players = null;
		Splayer player = null;

		if (type == 'l') {
			players = leftTeam.elements();
			while (players.hasMoreElements()) {
				player = (Splayer) players.nextElement();
				if (player.isGoalie) {
					//System.out.println("Left goalie found: id=" + player.id );
					return player;
				}
			}
			return null;
		} else if (type == 'r') {
			players = rightTeam.elements();
			while (players.hasMoreElements()) {
				player = (Splayer) players.nextElement();
				if (player.isGoalie) {
					//System.out.println("Right goalie found: id=" + player.id );
					return player;
				}
			}
			return null;
		} else
			return null;

	}

	//---------------------------------------------------------------------------
	// get the player copy by team side and Id
	public synchronized Splayer getPlayerSaved(char type, int id) 
	{
		//System.out.println( "leftTeamSaved.size()=" + leftTeamSaved.size() 
							//+ "rightTeamSaved.size()=" + rightTeamSaved.size() );
			
		Enumeration players = null;
		Splayer player = null;

		if (type == 'l') {
			players = leftTeamSaved.elements();
			while (players.hasMoreElements()) {
				player = (Splayer) players.nextElement();
				if (player.id == id)
					return player;
			}
			return null;
		} else if (type == 'r') {
			players = rightTeamSaved.elements();
			while (players.hasMoreElements()) {
				player = (Splayer) players.nextElement();
				if (player.id == id)
					return player;
			}
			return null;
		} else
			return null;

	}

	//---------------------------------------------------------------------------
	// get the player by its network address
	public synchronized Splayer getPlayer(InetAddress address, int port) 
	{
		Enumeration players = null;
		Splayer player = null;

		players = leftTeam.elements();
		while (players.hasMoreElements()) {
			player = (Splayer) players.nextElement();
			if (player.port == port && player.address.equals(address))
				return player;
		}

		players = rightTeam.elements();
		while (players.hasMoreElements()) {
			player = (Splayer) players.nextElement();
			if (player.port == port && player.address.equals(address))
				return player;
		}

		return null;

	}

	//---------------------------------------------------------------------------    
	public synchronized Sviewer getViewer(InetAddress address, int port) 
	{
		Enumeration observers = viewers.elements();
		Sviewer viewer = null;
		while (observers.hasMoreElements()) {
			viewer = (Sviewer) observers.nextElement();
			if (viewer.port == port && viewer.address.equals(address))
				return viewer;
		}
		return null;
	}


	//---------------------------------------------------------------------------
	public void setPlayerViewerActive(int ticker) 
	{
		// variable used to loop
		Enumeration players = null;
		Enumeration viewers = null;
		Sviewer viewer = null;
		Splayer player = null;

		// for each player in left team, set up their last active time
		players = leftTeam.elements();
		while (players.hasMoreElements()) {
			player = (Splayer) players.nextElement();
			player.setLastTime(ticker);

		}

		// for each player in right team, set up their last active time
		players = rightTeam.elements();
		while (players.hasMoreElements()) {
			player = (Splayer) players.nextElement();
			player.setLastTime(ticker);
		}

		// for each viewer, set up their last active time
		viewers = getViewers();
		while (viewers.hasMoreElements()) {
			viewer = (Sviewer) viewers.nextElement();
			viewer.setLastTime(ticker);
		}
	}
	//---------------------------------------------------------------------------
	/**
	 * @return enumeration with left team players
	 */
	public Enumeration getLeftPlayers() {

		return leftTeam.elements();
	}

	//---------------------------------------------------------------------------
	/**
	 * @return enumeration with right team players
	 */
	public Enumeration getRightPlayers() {

		return rightTeam.elements();
	}

	public Enumeration getViewers() {

		return viewers.elements();
	}

	//---------------------------------------------------------------------------
	/**
	 * Add new player to simulation.
	 * Because we have two active thread we can't add player now.
	 * We have to wait until the second thread will do it.
	 */
	public void addPlayer(Splayer player) {

		synchronized (addedPlayers) {
			addedPlayers.add(player);
		}
	}

	//---------------------------------------------------------------------------
	/**
	 * Remove player from simulation.
	 * Because we have two active thread we can't remove player now.
	 * We have to wait until the second thread will do it.
	 */
	public void removePlayer(Splayer player) {
		synchronized (removedPlayers) {
			removedPlayers.add(player);
			System.out.println("### Player " + player.id + " side " + player.side + " removed");
		}
	}

	public void addViewer(Sviewer viewer) {

		synchronized (addedViewers) {
			addedViewers.add(viewer);
		}
	}

	public void removeViewer(Sviewer viewer) {
		synchronized (removedViewers) {
			removedViewers.add(viewer);
			System.out.println("@@@ Viewer " + viewer.viewerId + " removed");
		}
	}
	//---------------------------------------------------------------------------
	/**
	 * This function updates player list
	 */
	public synchronized void updatePlayerList() {

		Enumeration e;
		Splayer p;
		Sviewer v;

		synchronized (addedPlayers) {

			e = addedPlayers.elements();
			while (e.hasMoreElements()) {

				p = (Splayer) e.nextElement();
				if (p.side == 'l') {
					leftTeam.add(p);
				} else {
					rightTeam.add(p);
				}
			}

			addedPlayers.clear();
		}

		synchronized (removedPlayers) {

			e = removedPlayers.elements();
			while (e.hasMoreElements()) {

				p = (Splayer) e.nextElement();
				if (p.side == 'l') {
					leftTeam.remove(p);
				} else {
					rightTeam.remove(p);
				}
			}

			removedPlayers.clear();
		}

		synchronized (addedViewers) {

			e = addedViewers.elements();
			while (e.hasMoreElements()) {

				v = (Sviewer) e.nextElement();
				viewers.add(v);
			}

			addedViewers.clear();
		}

		synchronized (removedViewers) {

			e = removedViewers.elements();
			while (e.hasMoreElements()) {

				v = (Sviewer) e.nextElement();
				viewers.remove(v);
			}

			removedViewers.clear();
		}
	}
	
	
	// this method returns true if the position is inside given penalty area
	// within some margin (negative inside, positive outside)
	public boolean inPenaltyArea( char side0, Vector2d pos, double margin )
	{
		boolean horizontalOK = false;
		boolean verticalOK = Math.abs( pos.getY() ) 
								< PENALTY_WIDTH/2.0 + margin; 
		
		if ( side0 == 'l' ) 
			if ( pos.getX() < 0 ) {
				horizontalOK =  ( LENGTH/2 + pos.getX() ) 
									< ( PENALTY_DEPTH + margin );
			} else
				return false; 
		else if ( side0 == 'r' )
			if ( pos.getX() >= 0 )			
				horizontalOK = ( LENGTH/2 - pos.getX() ) 
									< ( PENALTY_DEPTH + margin ); 
			else
				return false; 
		else
			return false;
			
		return 
			( horizontalOK && verticalOK && Math.abs( pos.getX() ) <= LENGTH/2 );
	}
	

	// this method assigns delault initial player positions
	private void setInitPlayerPos()
	{
		leftInitPos = new Vector();
		leftInitPos.addElement( new Vector2d(-45, 0) );	 	// #1		
		leftInitPos.addElement( new Vector2d(-35, -10) );	// #2	
		leftInitPos.addElement( new Vector2d(-35, 10) );	// #3	
		leftInitPos.addElement( new Vector2d(-25, -20) ); 	// #4
		leftInitPos.addElement( new Vector2d(-25, 20) );	// #5	
		leftInitPos.addElement( new Vector2d(-10, -15) );	// #6		
		leftInitPos.addElement( new Vector2d(-10, 15) );	// #7		
		leftInitPos.addElement( new Vector2d(-2, -28) );	// #8	
		leftInitPos.addElement( new Vector2d(-2, -5) );	 	// #9	
		leftInitPos.addElement( new Vector2d(-5, 3) );	 	// #10 kicker		
		leftInitPos.addElement( new Vector2d(-2, 28) );		// #11
	
		
		rightInitPos = new Vector();
		rightInitPos.addElement( new Vector2d(45, 0) );	 	// #1	
		rightInitPos.addElement( new Vector2d(35, 10) );	// #2	
		rightInitPos.addElement( new Vector2d(35, -10) );	// #3	
		rightInitPos.addElement( new Vector2d(25, 20) ); 	// #4	
		rightInitPos.addElement( new Vector2d(25, -20) );	// #5
		rightInitPos.addElement( new Vector2d(10, 15) );	// #6			
		rightInitPos.addElement( new Vector2d(10, -15) );	// #7
		rightInitPos.addElement( new Vector2d(2, 28) );		// #8	
		rightInitPos.addElement( new Vector2d(2, 5) );	 	// #9		
		rightInitPos.addElement( new Vector2d(5, -3) );	 	// #10 kicker		
		rightInitPos.addElement( new Vector2d(2, -28) );	// #11			
	}
}
