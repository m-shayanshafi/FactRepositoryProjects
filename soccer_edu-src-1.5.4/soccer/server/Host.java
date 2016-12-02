/* Host.java

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
import java.io.*;
import soccer.common.*;


/*
  * The Host thread waits for clients' data packet and put them
  * in players and viewers.
  */

public class Host extends Thread 
{
	private static final double maxDirChangePerCycle 
					= Splayer.MAX_ANGULAR_SPEED * HeartOfWorld.SIM_STEP_SECONDS;
					 
	public static int PORT = 7777; // soccer server 's listening port

	private SoccerWorld 	soccerWorld = null;
	private HeartOfWorld 	soccerHeart = null;
    private SoccerRules 	soccerRules = null;
	private int 			packetCounter = 0;

	
	public Host(SoccerWorld soccerWorld, 
				HeartOfWorld soccerHeart, 
				SoccerRules soccerRules) 
	{
		super("Host");
		this.soccerWorld = soccerWorld;
		this.soccerHeart = soccerHeart;
        this.soccerRules = soccerRules;
	}

	// in the infinite loop, receive data from clients, execute thier actions, 
	// and send them back commands and info about the new state of the world 
	public synchronized void run() 
	{
		Packet packet = null;
		
		while (true) {
			
			try {
				packet = SoccerServer.transceiver.receive();

				//packetCounter++;
				//if ( packetCounter%5000 == 0 )
					//System.out.println("* packet " + packetCounter 
							//+ " Host is still alive");

				if (packet.packetType == Packet.CONNECT) {
					welcome(packet);
				} else
					setAction(packet);

			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	private void welcome(Packet packet) throws IOException 
	{
		// figure out who is the sender
		InetAddress address = packet.address;
		int port = packet.port;
		int playerId;
		Sviewer viewer = null;
		Splayer player = soccerWorld.getPlayer(address, port);
		if (player == null)
			viewer = soccerWorld.getViewer(address, port);

		ConnectData aConnectData = (ConnectData) packet.data;
		
		switch ( aConnectData.clientType ) {
		
			case ConnectData.VIEWER: 
				connectToViewerClient( viewer, address, port, aConnectData );
			break; 
		
		
			case  ConnectData.PLAYER: 
				
				// if this player already connected
				if (player != null)
				{
					// reply the client, send the player team and number back.
					InitData init 
							= new InitData(	player.side, 
											player.id, 
											soccerHeart.SIM_STEP_SECONDS, 
											soccerRules.MAX_GRABBED_STEPS );
					Packet initPacket =
						new Packet(	Packet.INIT, 
									init, 
									address, 
									port );
					SoccerServer.transceiver.send(initPacket);	
					//System.out.println("Init packet repeatedly sent, player.id = " + player.id );
					return;
				}

				switch ( aConnectData.sideType ) {
					
					case ConnectData.LEFT: 
					case ConnectData.LEFT_COACH: 
					case ConnectData.RIGHT: 
					case ConnectData.RIGHT_COACH: 
					
						connectToPlayerClient( aConnectData, address, port );
						
					break; 
					
					case ConnectData.ANYSIDE: 
					case ConnectData.ANYSIDE_COACH: 
					
						if (soccerWorld.rightAvailable.size()
										>= soccerWorld.leftAvailable.size()) {
							if (soccerWorld.rightAvailable.size() > 0) {
								connectToPlayerClient( aConnectData, address, port );
							} else
								notAvailable(address, port); // no ID available
						} else {
							connectToPlayerClient( aConnectData, address, port );
						}
					break; 
					
					default:
						;
			}
			
			break; 
			
			default:
				;
		}
	}


	// this method registers viewer client 
	// and sends an acknowledgement to it
	private void connectToViewerClient( Sviewer viewer, 
						InetAddress address, 
						int port, ConnectData aConnectData ) throws IOException 
	{
		// if this viewer already connected
		if (viewer != null)
		{
			// reply to the client, send the viewer number back.
			InitData init = new InitData(	InitData.VIEWER, 
											viewer.viewerId, 
											soccerHeart.SIM_STEP_SECONDS, 
											soccerRules.MAX_GRABBED_STEPS );
			Packet initPacket =
				new Packet(	Packet.INIT, 
							init, 
							address, 
							port );
			SoccerServer.transceiver.send(initPacket);				
			//System.out.println("Packet.INIT sent. SIM_STEP_SECONDS = " + soccerHeart.SIM_STEP_SECONDS );			
			return;
		}
		int viewerId = soccerWorld.getViewerId();
		if (viewerId > 0) // position still available
			{
			boolean coach = false;
			if (aConnectData.sideType == ConnectData.ANYSIDE_COACH) {
				coach = true;
			}
			viewer =
				new Sviewer(
					address,
					port,
					viewerId,
					soccerHeart.getTicker(),
					coach);
					
			soccerWorld.addViewer(viewer);
			// reply to the client, send the viewer number back.
			InitData init = new InitData(	InitData.VIEWER, 
											viewerId, 
											soccerHeart.SIM_STEP_SECONDS, 
											soccerRules.MAX_GRABBED_STEPS );
			Packet initPacket =
				new Packet(Packet.INIT, init, address, port);
			SoccerServer.transceiver.send(initPacket);
			System.out.println("-- registered viewerId = " + viewerId 
								+ " address = " + address + " port = " + port );
			
			// this makes the HeartOfWorld advancing time
			//soccerRules.setPeriod( RefereeData.PRE_GAME );

		} else
			notAvailable(address, port); // no ID available		
	}

	// this method registers player client 
	// and sends an acknowledgement to it
	private void connectToPlayerClient( ConnectData aConnectData, 
										InetAddress address, 
										int port ) throws IOException 
	{
		// To make it possible asynchronious registration of player clients 
		// in the future, this method must analyze whether: 
		// (1) the  player attempting to register is the goalie or a field player;
		// (2) if it is the goalie, make sure that only the first registerd goalie
		//     is recognized as such, 
		// (3) resolve conflicts, if any, with player IDs, and
		// (4) send the acknowledgement of the player ID and role as the goalie or 
		//     a field player to the client (this will require adding one char datum
		//     in the InitData packet). 
		
		int playerId;
		char side = aConnectData.sideType;
		
		if ( side == 'l' )
			playerId = soccerWorld.getLeftPlayerId();
		else
			playerId = soccerWorld.getRightPlayerId();

		boolean isKicker = false;
		if ( aConnectData.playerRole == ConnectData.FIELD_PLAYER_KICKER )
			isKicker = true;
			
		boolean isGoalie = false;
		if ( aConnectData.playerRole == ConnectData.GOALIE )
			isGoalie = true;		
		
		if ( playerId > 0 ) 
		{
			Splayer player =
				new Splayer(
					address,
					port,
					side,
					playerId,
					isGoalie, 	
					isKicker, 
					soccerHeart.getTicker(),
					false);
			
			soccerWorld.addPlayer(player);
			
			// override player initial position
			if ( side == 'l' )
				soccerWorld.leftInitPos.setElementAt( aConnectData.pos, playerId-1 ); 
			else { 
				Vector2d pos = new Vector2d( -aConnectData.pos.getX(), -aConnectData.pos.getY() ); 
				soccerWorld.rightInitPos.setElementAt( pos, playerId-1 ); 
			}
								
			// reply to the client, send the player team and number back.
			InitData init; 
			if ( side == 'l')
				init = new InitData(InitData.LEFT, 
									playerId, 
									soccerHeart.SIM_STEP_SECONDS,  
									soccerRules.MAX_GRABBED_STEPS);
			else 
				init = new InitData(InitData.RIGHT, 
									playerId, 
									soccerHeart.SIM_STEP_SECONDS, 
									soccerRules.MAX_GRABBED_STEPS);
			Packet initPacket =
				new Packet(Packet.INIT, init, address, port);
			SoccerServer.transceiver.send(initPacket);
			System.out.print("-- registered playerId = " + playerId + " side = " + side 
				+ " addr = " + address + " port = " + port );
			if ( isGoalie )
				System.out.println(" ** goalie **" );
			else
				System.out.println();
			
			
		} else
			notAvailable(address, port); // no ID available
	}

	
	// reply to client there's no spot available
	private void notAvailable(InetAddress address, int port)
		throws IOException 
	{
		InitData init = new InitData(	InitData.FULL, 
										0, 
										soccerHeart.SIM_STEP_SECONDS,
										soccerRules.MAX_GRABBED_STEPS );
		Packet initPacket = new Packet(Packet.INIT, init, address, port);
		SoccerServer.transceiver.send(initPacket);
	}

	/**
	 * Execute action form client
	 * @param packet
	 */
	private void setAction(Packet packet) throws IOException
	{
		// figure out who is the sender
		InetAddress address = packet.address;
		int port = packet.port;
		Sviewer viewer = null;
		Splayer player = soccerWorld.getPlayer(address, port);
		if (player == null)
			viewer = soccerWorld.getViewer(address, port);
			
		switch ( packet.packetType ) {
		
			case Packet.TELEPORT: 
				// as for January 2006, TELEPORT packet is used for:
				// (1) teleporting objects with the mouse; and
				// (2) the goalie dragging the grabbed ball.   
			
				//System.out.println("Receiving Packet.TELEPORT: " + packet.writePacket() );
				
				TeleportData aTeleportData = (TeleportData) packet.data;
				// now update the world model...
				if ( teleport( aTeleportData, viewer, player ) ) {
					// ... and force the feedback (in the stepwise mode only)
					try {
						if ( soccerHeart.isStepping() )
							soccerHeart.sendVisualData();
					} catch (Exception e ) {
						// error can be generated here if during the registration 
						// not all agents have been connected yet
						System.out.println("Error while replying to Packet.TELEPORT " + e );
					}
				}
			break; 
			
			case Packet.VIEW: 
				//System.out.println("Receiving Packet.VIEW");			
				//System.out.println( packet.writePacket() ); 
				processViewPacket( packet );
			break; 

			case Packet.SITUATION: 
				System.out.println("Receiving Packet.SITUATION");
				processSituationPacket( packet );
				// let the Heart do its job now
				this.yield();	
					
			break; 
		
			case Packet.EMPTY:
				if (viewer != null)
					viewer.setLastTime(soccerHeart.getTicker());
				else
					return;
			break; 
		
			case Packet.BYE: 
				if (player != null) {
					if (player.side == 'l') {
						soccerWorld.removePlayer(player);
						soccerWorld.returnLeftPlayerId(player.id);
					} else {
						soccerWorld.removePlayer(player);
						soccerWorld.returnRightPlayerId(player.id);
					}
				} else {
					if (viewer != null) {
						soccerWorld.removeViewer(viewer);
						soccerWorld.returnViewerId(viewer.viewerId);
					} else
						return;
				}
			break; 
		
			case Packet.PERIOD: 
				//try {
					//System.out.println("Receiving Packet.PERIOD: " + packet.writePacket() );
				//} catch (Exception e ) {
					//System.out.println("Error while receiving Packet.PERIOD " + e );
				//}
				if (player != null) {
					setPlayerPeriodAction( packet, player );
				} else {
					if (viewer != null) {
						setViewerPeriodAction( packet, viewer );
					} else
						return;
				}
			break; 
			
			case Packet.DRIVE:
				if (player != null) {
		            //System.out.println("receiving Packet.DRIVE");
					synchronized (player) {						
						setPlayerMotion( packet, player );
					}
				} 
			break; 
			
			case Packet.KICK:
				if (player != null) {
		            //System.out.println("receiving Packet.KICK");
					synchronized (player) {						
						setBallMotion( packet, player );
					}
				} 
			break; 
			
			case Packet.TALK: 
				TalkData talk = (TalkData) packet.data;
				if (talk.message.length() > 30) {
					//System.out.println(talk.message.length() + talk.message);
					player.setMessage(talk.message.substring(0, 29));
					//System.out.println(player.message);
				} else
					player.setMessage(talk.message);
			break; 

			default: ;	// do nothing
		}
	}

	
	private void setViewerPeriodAction( Packet packet, Sviewer viewer )
	{
		viewer.setLastTime(soccerHeart.getTicker());
		if (viewer.coach) {
			PeriodData serverControl = (PeriodData) packet.data;
			
			switch(serverControl.actionType) {
				
				case PeriodData.STEP:
					soccerHeart.setStepping(true);
					try{
						soccerHeart.stepForward();
					}
					catch(Exception e){
					}
				break;
				
				case PeriodData.PLAY:
					soccerHeart.setStepping(false);
					//System.out.println("Receiving Packet.PERIOD PeriodData.PLAY  getTicker() = " + soccerHeart.getTicker() );
				break;
				
				case PeriodData.FORWARD:
					soccerHeart.setStepping(true);
					soccerHeart.periodForward();
                break;														
				
				default:
			}
		}
	}	
	
	// this will be possibly never used
	private void setPlayerPeriodAction( Packet packet, Splayer player )
	{
		player.setLastTime(soccerHeart.getTicker());
		if (player.coach) {
			PeriodData serverControl = (PeriodData) packet.data;
			
			switch(serverControl.actionType) {
				
				case PeriodData.STEP:
					soccerHeart.setStepping(true);
					try{
						soccerHeart.stepForward();
					}
					catch(Exception e){
					}
				break;
				
				case PeriodData.PLAY:
					soccerHeart.setStepping(false);
				break;
				
				case PeriodData.FORWARD:
                	soccerHeart.setStepping(true);
                	soccerHeart.periodForward();
                break;														
				
				default:
			}
		} 		
	}


	private void setPlayerMotion( Packet packet, Splayer player )
	{
		player.setLastTime(soccerHeart.getTicker());
		
		DriveData aDriveData = (DriveData) packet.data;

		// player cannot kick the ball if it is driving itself
		player.setKickBall(false);

		// ** set player direction **
		
		// requested angle change per cycle
		double delta = Util.normal_dir( aDriveData.dir - player.direction ); 
		
		// apply angular speed limit 
		delta = Util.sign( delta ) 
					* Math.min( Math.abs(delta), maxDirChangePerCycle );
		
		// actual player heading direction
		player.setDirection( Util.normal_dir( player.direction + delta ) );

		// ** set player driving force **
		
		if (aDriveData.force > player.MAXDASH)
			aDriveData.force = player.MAXDASH;
		else if (aDriveData.force < player.MINDASH)
			aDriveData.force = player.MINDASH;
		player.setForce(aDriveData.force);
	} 

	
	// this method applies some constraints on the kick ball force and 
	// direction contained in the received packet; 
	private void setBallMotion( Packet packet, Splayer player )
	{
		//try {
			//System.out.println("setBallMotion: packet = " + packet.writePacket() );
		//} catch ( IOException e ) {}
		
		player.setLastTime(soccerHeart.getTicker());

		soccerWorld.stepBallWasGrabbed 
						= Integer.MAX_VALUE;	// forget ball was grabbed
		
		KickData kick = (KickData) packet.data;

		player.setKickBall(true);

		if (kick.dir > 180)
			kick.dir = 180;
		else if (kick.dir < -180)
			kick.dir = -180;
		player.setDirection(kick.dir);

		if (kick.force > player.MAXKICK)
			kick.force = player.MAXKICK;
		else if (kick.force < player.MINKICK)
			kick.force = player.MINKICK;
			
		// limit the force of the first kicks in the throw-in mode
		// (this is somewhat enforcing the throw-in rules)
		if ( soccerWorld.throwInModeL ) {
			if ( player.side == 'l' ) {
				soccerWorld.throwInModeL = false;	// forget THROW_IN_L
				if ( kick.force > player.MAXKICK/3.0 )
					kick.force = player.MAXKICK/3.0;
			} else {
				kick.force = 0; 	// ignore this kick
			}
			//System.out.println("kicking force reduced to " + kick.force 
				//+ " for player id = " + player.id + " side = " + player.side );
		}
		if ( soccerWorld.throwInModeR ) {
			if ( player.side == 'r' ) {
				soccerWorld.throwInModeR = false;	// forget THROW_IN_R
				if ( kick.force > player.MAXKICK/3.0 )
					kick.force = player.MAXKICK/3.0;
			} else {
				kick.force = 0; 	// ignore this kick
			}
			//System.out.println("kicking force reduced to " + kick.force 
				//+ " for player id = " + player.id + " side = " + player.side );
		}
		
		player.setForce(kick.force);
	}
	
	
	// this method updates the **saved** world state 
	// with the data in the received packet
	// (the packet may contain not a whole team, though)
	private synchronized void processViewPacket( Packet packet ) throws IOException
	{	
		try {
			// update ball
			ViewData receivedData = (ViewData)packet.data; 	
			soccerWorld.ballSaved.assign( receivedData.ball );		
			
			// update left team players
			for ( int i=0; i<receivedData.leftTeam.size(); i++ ) {
				Player player = (Player)receivedData.leftTeam.elementAt( i );
				Splayer splayer = soccerWorld.getPlayerSaved( player.side, player.id );
				splayer.assign( player );
			}
	
			// update right team players
			for ( int i=0; i<receivedData.rightTeam.size(); i++ ) {
				Player player = (Player)receivedData.rightTeam.elementAt( i );
				Splayer splayer = soccerWorld.getPlayerSaved( player.side, player.id );
				splayer.assign( player );
			}
		} catch (Exception e) {
			System.out.println( "Exception caught in processViewPacket: " + e );
			System.out.println( "packet = " + packet.writePacket() );
		}
	}


	// this method updates the world with the data in the received packet
	private void processSituationPacket( Packet packet ) 
	{
		SituationData receivedData 	= (SituationData)packet.data; 
		
		soccerHeart.setReplication( receivedData.numOfSteps,
									receivedData.numOfReplicas,
									receivedData.stepID );
					
		// reset the world to the saved situation
		soccerWorld.restoreSituation();	
		
		// update all clients about change
		try {
			soccerHeart.sendVisualData();
		} catch (Exception e ) {
			System.out.println("Error while replying to Packet.SITUATION " + e );
		}		
		System.out.println("server reset to new situation. numOfReplicas = " 
			+ receivedData.numOfReplicas + " numOfSteps = " + receivedData.numOfSteps );
	}
	
	
	// this method teleports the object referred to in the input data
	// returns true if only the object can be teleported
	public boolean teleport( 	TeleportData aTeleportData, 
								Sviewer sendingViewer, 	
								Splayer sendingPlayer )	
	{
		char objType 	= aTeleportData.objType;
		char side 		= aTeleportData.side;
		int playerID 	= aTeleportData.playerID; 
		double newX 	= aTeleportData.newX;
		double newY 	= aTeleportData.newY;
		Vector2d newpos = new Vector2d(newX, newY);
		
		// determine whether the teleportation is allowed
		boolean	teleportationAllowed = false;
		
		if ( sendingViewer != null ) 
		
			teleportationAllowed = true;	// teleporting an object with the mouse
		
		else if ( sendingPlayer != null ) {
			if ( soccerRules.getMode() == RefereeData.PLAY_ON && 
				 sendingPlayer.isGoalie	)	
				 // the goalie must be in the penalty area on own side
				if ( soccerWorld.inPenaltyArea( side, newpos, 0 ) ) {
					teleportationAllowed = true;	
				}
		}
			
		if ( teleportationAllowed ) {
			
			//System.out.println("  objType = " + objType +" playerID = " + playerID 
						//+ "  side = " + side );
			
			switch ( objType ) {
				
				case TeleportData.GRAB:  
					
					if ( sendingPlayer != null ) {
						soccerWorld.ball = new Sball();
						soccerWorld.ball.set( sendingPlayer, true  );
						if ( soccerWorld.stepBallWasGrabbed ==Integer.MAX_VALUE ) {
							// first GRAB command received
							soccerWorld.stepBallWasGrabbed 
											= soccerHeart.getTicker(); 
							soccerWorld.sideGrabbedBall = side;
							//System.out.println("Ball grabbed. step = " + soccerHeart.getTicker()
								//+ " isGrabbed=" + soccerWorld.ball.isGrabbed );
						}
					}
				break;

				case TeleportData.BALL:  
					
					if ( ( sendingViewer != null ) 
							|| ( soccerWorld.ball.isGrabbed	
								&& soccerWorld.sideGrabbedBall == side ) ) {	
						
						// the viewer is dragging ball with the mouse 
						// OR the goalie teleports grabbed ball
						
						soccerWorld.ball = new Sball();
						soccerWorld.ball.set( newX, newY );
					}
				break;
				
				
				case TeleportData.LEFT_PLAYER:  
				case TeleportData.RIGHT_PLAYER:    
					
					Splayer player = soccerWorld.getPlayer( side, playerID );
					player.position.setXY( newpos ); 
					player.velocity.setXY( 0, 0 );
					player.acceleration.setXY( 0, 0 );

					
				default:
			}	
		}
		return teleportationAllowed;
	}

}
