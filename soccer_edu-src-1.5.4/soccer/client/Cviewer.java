/* Cviewer.java
   This class gets sensing info from server, and displays it.
   
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

package soccer.client;

import soccer.client.view.j3d.FieldJ3D;
import soccer.common.*;
import java.io.*;
import java.net.*;


public class Cviewer extends Thread 
{
	// used for collecting lost packet statistics
	private static final int  MODULUS = 1000;
	
	private SoccerMaster soccerMaster;
	private World world; //world view
	private Sensor sensor; // server data processing

	private boolean stop = false;

	// packet statistics
	private int 	receivedPacketCount = 0;
	private	int 	previousReceivedPacketID = -1;	
	private	int 	lostPacketCount = 0;
	private double 	lostPacketFactor = 0;

	// this allows the user to move objects 
	// around the field with the mouse
	private VController controller; 


	public Cviewer(SoccerMaster soccerMaster) 
	{
		super("Cviewer");
		this.soccerMaster = soccerMaster;
		world = new World();
		soccerMaster.setWorld(world);
		soccerMaster.arena2D.setWorld(world);
		soccerMaster.arena3D.setWorld(world);
		((FieldJ3D)(soccerMaster.arena3D)).enableMouseNavigation(true);

		controller = new VController(world, soccerMaster);
		soccerMaster.arena2D.addMouseListener(controller);
		soccerMaster.arena2D.addMouseMotionListener(controller);
	}

	public void run() 
	{
		stop = false;
		
		while (!stop) {
			
			if ( soccerMaster.getGState() != GState.WAITING ) {
				controller.clearGrabFlags();
			}
			
			try {
				// sensor collects info from server, builds its world
				Packet receivedPacket = soccerMaster.getTransceiver().receive();
				
				processViewingData( receivedPacket );
				
				receivedPacketCount++;
				checkLostPackets( receivedPacket );
				
				//if ( receivedPacketCount%100 == 0 )
					//System.out.println("* packet " + receivedPacketCount 
							//+ " Cviewer is still alive");

				if (receivedPacketCount % 600 == 0) {
					// keep sever aware of the viewer is alive
					EmptyData empty = new EmptyData();
					Packet command =
						new Packet(
							Packet.EMPTY,
							empty,
							soccerMaster.getAddress(),
							soccerMaster.getPort() );
					soccerMaster.getTransceiver().send(command);
				}
				//try {
					//sleep( 80 );	// a stub for testing packet loss
				//} catch (Exception e ) {}

			} catch (IOException e) {

			}

		}
	}

	public void end() 
	{
		try {
			stop = true;
			ByeData bye = new ByeData();
			Packet command =
				new Packet(
					Packet.BYE,
					bye,
					soccerMaster.getAddress(),
					soccerMaster.getPort());
			soccerMaster.getTransceiver().send(command);
			
			System.out.println("sending Packet.BYE");
			
		} catch (IOException e) {

		}
	}

	private void processViewingData(Packet receivedPacket) {

		int min;
		int sec;

		// process the received data
		switch ( receivedPacket.packetType ) {
			
			case Packet.VIEW:
			
				//System.out.println(" Packet.VIEW packet received");	
				world.view = (ViewData) receivedPacket.data;
	
				world.me = null;
				world.ball = world.view.ball;
				world.leftTeam = world.view.leftTeam;
				world.rightTeam = world.view.rightTeam;
				
				// some object could be grabbed with the mouse by the user;
				// clear all isGrabbed flags except the ball
				controller.updateGrabFlags( false );
	
				// get the time information
				sec = (int)( world.view.time * world.SIM_STEP_SECONDS + 0.5 );
				min = sec / 60;
				sec = sec % 60;
	
				//System.out.println("Packet.VIEW  time = " + world.view.time 
				//			+ " min=" + min + " sec=" + sec);
					
				soccerMaster.timeJLabel.setText(min + ":" + sec);
	
				// find out if somebody has kicked the ball
				if (world.ball.controllerType != world.preController) {
					world.preController = world.ball.controllerType;
					if (world.ball.controllerType == 'f')
						soccerMaster.getSoundSystem().playClip("kick");
				}
	
				// update the arena
				if(soccerMaster.isIn3D()) {
					soccerMaster.arena3D.repaint();
				}
				else {
					soccerMaster.arena2D.repaint();
				}
			break;
				
			case Packet.HEAR:
				world.message = (HearData) receivedPacket.data;
				if (world.message.side == 'l')
					world.leftM = world.message;
				else if (world.message.side == 'r')
					world.rightM = world.message;
			break;
				
			case Packet.INFO: 
				
				world.infoData = (InfoData) receivedPacket.data;
				
				switch ( world.infoData.info ) {
					
					case InfoData.WAIT_NEXT:
						// this is the server feedback from button pressed
						soccerMaster.setGState( GState.WAITING );
						soccerMaster.getSoundSystem().playClip("click01");
					break;
					
					case InfoData.RESUME:
						// this is the server feedback from button pressed
						soccerMaster.setGState( GState.RUNNING );	
					break;

					case InfoData.REPLICA:
						// this is the server feedback about next replica
						if ( world.infoData.info2 > 0 )
							soccerMaster.replicaJTextField
								.setText( world.infoData.info1 
								+ " of " + world.infoData.info2 );
						else 
							soccerMaster.replicaJTextField.setText( "" );
							
					break;
					
					default: ; 		// do nothing
				} 
			break;
				
			case Packet.REFEREE: 
				
				world.referee = (RefereeData) receivedPacket.data;
				
				//try {
					//System.out.println( "received Packet.REFEREE = " 
										//+ receivedPacket.writePacket() );
				//} catch (IOException e ) {}
				
				// process the referee commands
				sec = (int)(world.referee.time * world.SIM_STEP_SECONDS);
				min = sec / 60;
				sec = sec % 60;
				
				//System.out.println("Packet.REFEREE  period = " 
					//+ RefereeData.periods[world.referee.period] 
					//+ " mode = " + RefereeData.modes[world.referee.mode] );
	
				soccerMaster.periodJLabel.setText(
					RefereeData.periods[world.referee.period] + ":");
				soccerMaster.modeJLabel.setText(
					RefereeData.modes[world.referee.mode] + ":");
				soccerMaster.timeJLabel.setText(min + ":" + sec);
	
				soccerMaster.leftName.setText(world.referee.leftName);
				String scoreL = world.referee.score_L 
										+ " ("+ world.referee.total_score_L + ")";
				soccerMaster.leftScore.setText(":" + scoreL );
	
				soccerMaster.rightName.setText(world.referee.rightName);
				String scoreR = world.referee.score_R 
										+ " ("+ world.referee.total_score_R + ")";
				soccerMaster.rightScore.setText(":" + scoreR );

				
				// do not display the last message, as no games would follow
				if ( world.referee.game <= world.referee.games )
					soccerMaster.gameJLabel.setText("Game " 
						+ world.referee.game + " of " + world.referee.games);
	
				if (world.referee.total_score_L > world.leftGoal) {
					world.leftGoal = world.referee.total_score_L;
					soccerMaster.getSoundSystem().playClip("applause");
				} else if (world.referee.total_score_R > world.rightGoal) {
					world.rightGoal = world.referee.total_score_R;
					soccerMaster.getSoundSystem().playClip("applause");
				} else if (world.referee.period != world.prePeriod) {
					soccerMaster.getSoundSystem().playClip("referee2");
					world.prePeriod = world.referee.period;
				} else if (world.referee.mode != world.preMode) {
					soccerMaster.getSoundSystem().playClip("referee1");
					world.preMode = world.referee.mode;
				}
				
			break;
			
			case Packet.BYE: 
				System.out.println("Packet.BYE received");
			break;
				
			default:
				;
		}

	}

	/*******************************************
	 *
	 * 			special-purpose methods
	 *
	 *******************************************/

	// this method collects lost packet statistics that could be useful
	// for determining whether the client is running too slow.
	private void checkLostPackets( Packet aPacket ) 
	{
		if( aPacket.packetType == Packet.VIEW ) {
			// as the the VIEW packets are sent on each simulation step 
			// and the server inserts in them the step ID, this parameter
			// can be used to detect packets losses 

			ViewData aViewData = (ViewData)aPacket.data;
			
			if ( previousReceivedPacketID < 0 ) {
				// skip the first packet
			} else {
				
				
				int delta = aViewData.time  
						- ( previousReceivedPacketID + 1 );
				if ( delta <0 )
					delta = MODULUS - delta; 
	
				if ( delta > MODULUS/2 )
					delta = 0; 	// just ignore too big losses
				
				// this is the exponential smoothening method
				double weight = 0.5;			// a magic number
				lostPacketFactor = weight * delta + 
									(1 - weight) * lostPacketFactor;
				
				if ( lostPacketFactor > 0.7 ) {
					// print a warning that packets are being lost
					lostPacketCount = lostPacketCount + delta;
					System.out.println("** " + getName() + " lost " + delta 
						+ " packets" //+ " lostPacketCount = " + lostPacketCount 
						+ "  lostPacketFactor = " 
						+ ((int)(1000.0*lostPacketFactor))/1000.0 + "  **" );	
				}
			}
			previousReceivedPacketID = aViewData.time;
		}	
	}
	
}
