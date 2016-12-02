/* HeartOfWorld.java
   The engine of the world to keep it going

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

import soccer.common.*;
import java.util.*;
import java.io.*;

public class HeartOfWorld extends Thread 
{
    // simulation step in seconds
    public static double SIM_STEP_SECONDS = 0.050;

	// time in seconds after that the server starts the game without the monitor
    public static double NO_GAME_SECONDS = 60; 	

    // Half time, players can have a rest and prepare the second half (in min)
    public static double HALF_TIME = 5.0; 	

	// number of games to play in this simulation run
    public static int GAMES_TO_PLAY = 50; 	

	// if true, reset the score with the next game
	public static boolean RESET_SCORE = false;
	
    // pause between periods and games in seconds
    public static double PAUSE_DURATION = 5.0;

    // Idle time needed for deleting a non-active client
    public static int IDLE = 2;

    // record game flag
    public boolean log = false;
    
    // halfTimeID values
    private int 		previousMode;
    private int 		previousPeriod;

    // replication paramenters
    private int numOfSteps = 0;			// steps to run in each relication
    private int numOfReplicas = 0;		// number of replicas to run 
    private int tickerToStartOver = 0; 
    private int replicaCount = 1;		
    private int numOfStepsLeft = 0;		// steps to run left
    private boolean situationIsSaved = false;
    
    // server performance statistics
    private int 	idleCount = 0;
    private int 	playOnCount = 0;
    private int 	deficitCount = 0;
    private double 	idleTimeSum = 0; 
    private double 	idleTimeSumOfSquares = 0; 

    // log file 
    private RandomAccessFile saved = null;

    private SoccerWorld soccerWorld = null;
    private SoccerRules soccerRules = null;
    
    // time parameters
    private int cyclesInHalfTime;
    private int cyclesInPause;

	private RWLock tickLock = null;
    private int ticker = 0; 		// simulation step number (discrete time)

	private RWLock stepLock = null;    
    private boolean isStepping = false;
    private int timer = 0;

    // constructor
    public HeartOfWorld(SoccerWorld soccerWorld, SoccerRules soccerRules) 
    {
		super("Heart");
        this.soccerWorld = soccerWorld;
        this.soccerRules = soccerRules;
        this.tickLock = new RWLock();
		this.stepLock = new RWLock();
		
		cyclesInHalfTime = (int)(HALF_TIME * 60 / SIM_STEP_SECONDS); 
		cyclesInPause 	 = (int)(PAUSE_DURATION / SIM_STEP_SECONDS); 
		
        previousMode = soccerRules.getMode();
        previousPeriod = soccerRules.getPeriod(); 
		
		if(log) {
			// do something	
		}
    }


    //---------------------------------------------------------------------------
    /**
     * this method implements the thread activity
     */
    public void run() {

        long startTimeMsec = System.currentTimeMillis();
        	
        while ( soccerRules.gameCount <= GAMES_TO_PLAY ) {

            // get the time before the step
            long timeBefore = System.currentTimeMillis();
            
			//if ( ticker%500 == 0 && ticker > 0 )
				//System.out.println("@ ticker " + ticker 
						//+ " Heart is still beating");
			 
            try {

                if(!isStepping()) {
                  
                    stepForward();
                }
		        
		        int idleTime = getIdleTime( timeBefore );
		        
		        if ( idleTime > 0 ) {
		        	//System.out.println("idling " + idleTime + " milliseconds");
		        	sleep( idleTime );
		        }
                    
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        
        	// allow the server running the games on its own
        	if ( soccerRules.getPeriod() == RefereeData.NO_GAME ) {
        		if ( System.currentTimeMillis() 
        					- startTimeMsec > (long)(NO_GAME_SECONDS*1000.0) ) {
        			
					if ( !soccerWorld.getViewers().hasMoreElements() ) {
						// no viewer client has registered yet
						
						soccerRules.setPeriod( RefereeData.PRE_GAME );
						
						if ( !situationIsSaved ) {
							// initialize for the replication
							soccerWorld.initSavedSituation();
							situationIsSaved = true;
						}
					}
			    }
        	}
        }
        System.out.println("======= Simulation done. " 
        			+ (soccerRules.gameCount-1) + " games played =======." );
    }

    
    // this method executed one stp of the game
    public synchronized void stepForward() throws Exception 
    {
		// *** freze everything while NO_GAME lasts ***
		if ( soccerRules.getPeriod() == RefereeData.NO_GAME ) {
            
            soccerRules.total_score_L = 0;
            soccerRules.total_score_R = 0;
            soccerRules.score_L = 0;
            soccerRules.score_R = 0;
            soccerRules.sqr_score_diff = 0;
		
		} else {

	        if ( soccerWorld.replicationIsOn ) {
	        	// *** replication mode ***
	        	
	        	if ( numOfStepsLeft == 0 ) {	
	        		
	        		System.out.println("-- starting replica " + replicaCount + " --" );
			    	sendReplicaInfo();
			    	
        			if ( replicaCount > 1 ) {	
        				// not the first replica; restore data
	        			this.setPriority( Thread.NORM_PRIORITY + 2 );
	        			//System.out.println("... restoring data for replica " + replicaCount );
						setTicker( tickerToStartOver );
        				soccerWorld.restoreSituation(); 
        				soccerWorld.setPlayerViewerActive( ticker ); 
	        			this.setPriority( Thread.NORM_PRIORITY  );	        			
						notifyAll();
	        		} 
	        			
        			if ( replicaCount  < numOfReplicas ) {
		        		// initialize next replica
		        		numOfStepsLeft = numOfSteps; 
		        		replicaCount++; 
		        	} else {
		        		// finish replication
		        		numOfReplicas = 0;
		        		replicaCount = 1;
	        			System.out.println("=== replication done === " );
	        			sendReplicaInfo();
		        	}
	        	}
	        	
	        	numOfStepsLeft--; 
	        } else {
	        	soccerWorld.replicationIsOn = false;
	        }
	        
	        advanceTickers();
		}
		
		// make a working copy of both teams (for the symmetry)
		soccerWorld.copyTeams( getTicker() );
		
        soccerRules.enforce( getTicker() );

        if (soccerRules.refereeSignal) {
            sendRefereeData( soccerRules.getMode() );
            soccerRules.refereeSignal = false;
            // forget ball was grabbed
            soccerWorld.stepBallWasGrabbed = Integer.MAX_VALUE; 
            // forget corner kick	
            soccerWorld.stepCornerKickDecided = -Integer.MAX_VALUE; 	
        }

        sendVisualData();
        sendAudioData();
        clean();

    }
    
    // this method determines how much time server can be waiting
    // until the end of current simulation cycle.
    // as a byproduct, this method measures the server performance
    // by collecting useful statistics and printing it out
    private int getIdleTime( long timeBefore )
    {
        // get the time after the step
        long timeAfter = System.currentTimeMillis();

        // figure out how long it takes to process
        long timeSpent = timeAfter - timeBefore;

        int idleTime = (int)(SIM_STEP_SECONDS * 1000 - timeSpent);
        
        // collect performance statistics.
        // this info allows to figure out whether the platform
        // performance is sufficient indeed
        if ( soccerRules.getMode() == RefereeData.PLAY_ON ) {
        	
        	if (idleTime > 0) {
			    idleCount++;
			    idleTimeSum = idleTimeSum + idleTime; 
			    idleTimeSumOfSquares = idleTimeSumOfSquares 
			    						+ idleTime * idleTime; 				
            } else
            	deficitCount++;		// server is too slow
            	
        	playOnCount++;
			
			if ( playOnCount % 2000 == 0 ) {
				double avgTime = idleTimeSum/idleCount;
				double stdDev = idleTimeSumOfSquares/idleCount 
											- avgTime*avgTime;
				// convert them into per cent scale
				avgTime = 100 * avgTime/(SIM_STEP_SECONDS * 1000);
				stdDev = 100 * Math.sqrt( stdDev )/(SIM_STEP_SECONDS * 1000);
				System.out.println("\n   == Idling time statistics == \n" 
					+ " average = " + (int)avgTime + "%,"
					+ " std dev = " + (int)stdDev + "%,"
					+ " deficit = " 
					+ (int)(100*deficitCount/playOnCount) + "% \n" );   	
			}
        }
        	
		// recalculate idle time 
        timeAfter = System.currentTimeMillis();
        timeSpent = timeAfter - timeBefore;
        idleTime = (int)(SIM_STEP_SECONDS * 1000 - timeSpent);
        
        return idleTime; 
    }
    
    
	// this method advances the period and resets the ticker accordingly
	public void periodForward() 
	{
        switch ( soccerRules.getPeriod() ) { 

        	case RefereeData.NO_GAME: 
				soccerRules.setPeriod( RefereeData.PRE_GAME );
        		soccerWorld.setPlayerViewerActive(getTicker());
			break; 

        	case RefereeData.PRE_GAME: 
				soccerRules.setPeriod( RefereeData.FIRST_HALF );
			break; 

        	case RefereeData.FIRST_HALF: 
				soccerRules.setPeriod( RefereeData.HALF_TIME );
			break; 
			
        	case RefereeData.HALF_TIME: 
				soccerRules.setPeriod( RefereeData.SECOND_HALF );
			break; 

        	case RefereeData.SECOND_HALF: 
				soccerRules.setPeriod( RefereeData.PRE_GAME );
			break; 
			
			default:
		}
		
        try {	
            sendRefereeData( soccerRules.getMode() );
        }
        catch (Exception e)	{
        	System.out.println( "Exception in periodForward: " + e );
        }
	}

    // this method advances the ticker and decides which game period is in
    // and set its game mode according to the period change
    private void advanceTickers() throws IOException 
    {
        switch ( soccerRules.getPeriod() ) { 
        	
        	case RefereeData.PRE_GAME: 
	        	timer++;
	        	if ( timer > cyclesInPause ) {
	        		// before kickoff pause ended
	        		soccerRules.setPeriod( RefereeData.FIRST_HALF );
	        		timer = 0;
	        	} else {
	        		// game started
	        		if ( RESET_SCORE ) {
		                soccerRules.total_score_L = 0;
		                soccerRules.total_score_R = 0;
		            }
			        soccerWorld.ball.set(0, 0);
			        // keep players off the ball
			        soccerRules.leftOff = true;
			        soccerRules.rightOff = true;
			        if ( !situationIsSaved ) {
			        	// initialize for the replication
			        	soccerWorld.initSavedSituation();
			        	situationIsSaved = true;
			        }
        		}	 
			break; 
		
        	case RefereeData.FIRST_HALF: 
	        case RefereeData.SECOND_HALF:
		        
		        // advance the ticker only during actually playing the game
		        setTicker(getTicker() + 1);
		
		        if ( getTicker() == cyclesInHalfTime ) {
	        		// set a break between two halves
	        		soccerRules.setPeriod( RefereeData.HALF_TIME );
	        	}
	        
		        if ( getTicker() == 2 * cyclesInHalfTime ) {
	        		
	        		// start over the next game
	        		soccerRules.setPeriod( RefereeData.PRE_GAME );
	        		setTicker( 0 );
	        		updateStatistics();
	        	}
			break; 
        
        	case RefereeData.HALF_TIME: 
	        	timer++;
	        	if ( timer > cyclesInPause ) {
	        		// half-time pause ended
	        		soccerRules.setPeriod( RefereeData.SECOND_HALF );
        			timer = 0;
	        	}	 
			break; 
			
			default: ; // do nothing
		}        
        
        if ( previousMode != soccerRules.getMode() 
        	|| previousPeriod != soccerRules.getPeriod() ) {
        	
        	sendRefereeData( soccerRules.getMode() );
        	
        	//System.out.println("period = " 
        			//+ RefereeData.periods[soccerRules.getPeriod()] 
        			//+ " mode = " + RefereeData.modes[soccerRules.getMode()] );
        }
        	
        previousMode = soccerRules.getMode();
        previousPeriod = soccerRules.getPeriod(); 	
    }
    

	// this method updates and prints game statistics
	private void updateStatistics()
	{
        // print game result and some statistics
        System.out.print("\n---------  Game " + soccerRules.gameCount + " over. "); 
        System.out.println("Score " + soccerRules.score_L 
        						+ ":" + soccerRules.score_R + "  ---------"); 
        System.out.println("Total score " + soccerRules.total_score_L 
        						+ ":" + soccerRules.total_score_R ); 
		// accumulate statistics
		int scoreDiff = soccerRules.score_L - soccerRules.score_R;
		soccerRules.sqr_score_diff = soccerRules.sqr_score_diff 
											+ scoreDiff * scoreDiff;
        
		// calculate 95% confidence interval half-width for score difference
        double halfWidth = 0;
    	// average score difference 
    	double avgScoreDiff = (double)( soccerRules.total_score_L 
    							- soccerRules.total_score_R )
    											/(double)soccerRules.gameCount;
        if ( soccerRules.gameCount > 1 ) {
        	// score difference variance
        	double varScoreDiff = soccerRules.sqr_score_diff
        						/( soccerRules.gameCount - 1 );
        	varScoreDiff = varScoreDiff - avgScoreDiff * avgScoreDiff;
			// confidence interval half-width
			halfWidth = 2.0 * Math.sqrt( varScoreDiff );
        }
        
        System.out.println("Average score difference: " + (float)avgScoreDiff 
        					+ ", half-width: " + (float)halfWidth ); 
	    
	    if ( Math.abs( avgScoreDiff ) > halfWidth && soccerRules.gameCount > 5 ) {					
	        if ( avgScoreDiff > 0 ) 					
				System.out.print("** left ** ");
			else  
				System.out.print("** right ** ");
			System.out.println("team is playing better with 95% confidence");	
		}
		System.out.println("------------------------------------------------");
			
		soccerRules.score_L = 0;
		soccerRules.score_R = 0;
		soccerRules.gameCount++;		
	}
	 

    // send packet to all clients
    private void sendPacket(Packet p) throws IOException 
    {
        // send the packet to clients
        Enumeration gamers = null;
        Splayer player = null;
        Sviewer viewer = null;

        gamers = soccerWorld.getLeftPlayers();
        while (gamers.hasMoreElements()) {
            player = (Splayer) gamers.nextElement();
            p.address = player.address;
            p.port = player.port;
            SoccerServer.transceiver.send(p);
        }

        gamers = soccerWorld.getRightPlayers();
        while (gamers.hasMoreElements()) {
            player = (Splayer) gamers.nextElement();
            p.address = player.address;
            p.port = player.port;
            SoccerServer.transceiver.send(p);
        }

        gamers = soccerWorld.getViewers();
        while (gamers.hasMoreElements()) {
            viewer = (Sviewer) gamers.nextElement();
            p.address = viewer.address;
            p.port = viewer.port;
            SoccerServer.transceiver.send(p);
        }

    }

    // send referee information to clients
    public void sendRefereeData(int mode) throws IOException 
    {
        // fill the referee data      
        RefereeData referee =
            new RefereeData(getTicker(),
			                soccerRules.getPeriod(),
			                mode,
			                soccerRules.sideToContinue, 
			                SoccerWorld.LEFTNAME,
			                soccerRules.score_L,
			                soccerRules.total_score_L,
			                SoccerWorld.RIGHTNAME,
			                soccerRules.score_R,
			                soccerRules.total_score_R,
			                soccerRules.gameCount,
			                GAMES_TO_PLAY);
        
        Packet refereePacket = new Packet(Packet.REFEREE, referee);

        sendPacket(refereePacket);
		
		//System.out.println( "sent Packet.REFEREE = " + refereePacket.writePacket() );

        if (log) {
            saved.writeBytes(refereePacket.writePacket());
            saved.writeByte('\n');
        }
    }

    
    // send information to clients about the server state
    private void sendInfoData(int state) throws IOException 
    {
        InfoData infoData =
            new InfoData(state);
        
        Packet infoPacket = new Packet(Packet.INFO, infoData);

        sendPacket(infoPacket);
		
		//System.out.println( "sent Packet.INFO = " + infoPacket.writePacket() );
    }
    
    
    // send audio information to clients
    private void sendAudioData() throws IOException 
    {
        Enumeration gamers = null;
        Splayer player = null;

        // fill the hear data and send it out
        gamers = soccerWorld.getLeftPlayers();
        while (gamers.hasMoreElements()) {
            player = (Splayer) gamers.nextElement();
            if (player.getMessage() != null && player.noWord == 0) {
                HearData hear =
                    new HearData(
                        getTicker(),
                        player.side,
                        player.id,
                        player.getMessage());
                Packet hearPacket = new Packet(Packet.HEAR, hear);
                // player can not speak in NOWORD sec
                player.noWord = (int) (Splayer.NOWORD / HeartOfWorld.SIM_STEP_SECONDS);
                player.setMessage(null);
                sendPacket(hearPacket);
                if (log) {
                    saved.writeBytes(hearPacket.writePacket());
                    saved.writeByte('\n');
                }
                return;
            }
        }
        gamers = soccerWorld.getRightPlayers();
        while (gamers.hasMoreElements()) {
            player = (Splayer) gamers.nextElement();
            if (player.getMessage() != null && player.noWord == 0) {
                HearData hear =
                    new HearData(
                        getTicker(),
                        player.side,
                        player.id,
                        player.getMessage());
                Packet hearPacket = new Packet(Packet.HEAR, hear);
                // player can not speak in NOWORD sec
                player.noWord = (int) (Splayer.NOWORD / HeartOfWorld.SIM_STEP_SECONDS);
                player.setMessage(null);
                sendPacket(hearPacket);
                if (log) {
                    saved.writeBytes(hearPacket.writePacket());
                    saved.writeByte('\n');
                }
                return;
            }
        }
    }

    
    // send visual information to all clients 
    public void sendVisualData() throws IOException 
    {
        // loop parameters
        Enumeration gamers = null;
        Splayer player = null;
        Sviewer viewer = null;

        // set up ball data
        Ball b =
            new Ball(
                soccerWorld.ball.position,
                soccerWorld.ball.controllerType,
                soccerWorld.ball.controllerId);
        b.isGrabbed = soccerWorld.ball.isGrabbed;
        if (soccerWorld.ball.free) {
            b.controllerType = 'f';
            b.controllerId = 0;
        }

        // create a left team vector
        Vector left = new Vector();
        gamers = soccerWorld.getLeftPlayers();
        while (gamers.hasMoreElements()) {
            player = (Splayer) gamers.nextElement();
            Player pp =
                new Player(
                    player.side,
                    player.id,
                    player.position,
                    player.getDirection());
            left.add(pp);
        }

        // create a right team vector
        Vector right = new Vector();
        gamers = soccerWorld.getRightPlayers();
        while (gamers.hasMoreElements()) {
            player = (Splayer) gamers.nextElement();
            Player pp =
                new Player(
                    player.side,
                    player.id,
                    player.position,
                    player.getDirection());
            right.add(pp);
        }
        
		// to ensure the symmetry, send visual info to teams in turns 
		if ( getTicker()%2 == 0 )
			sendSeeDataToPlayers(soccerWorld.getLeftPlayers(), b, left, right);
		sendSeeDataToPlayers(soccerWorld.getRightPlayers(), b, left, right);
		if ( getTicker()%2 != 0 )
			sendSeeDataToPlayers(soccerWorld.getLeftPlayers(), b, left, right);

        ViewData view = new ViewData();
        view.time = getTicker();
        view.ball = b;
        view.leftTeam = left;
        view.rightTeam = right;
    	
    	Packet viewPacket = new Packet();
        viewPacket.packetType = Packet.VIEW;
        viewPacket.data = view;

        if (log) {
            saved.writeBytes(viewPacket.writePacket());
            saved.writeByte('\n');
        }

        gamers = soccerWorld.getViewers();
        
        while (gamers.hasMoreElements()) {
            
            viewer = (Sviewer) gamers.nextElement();
            viewPacket.address = viewer.address;
            viewPacket.port = viewer.port;
            SoccerServer.transceiver.send(viewPacket);
        }

    }

	
	// this method send SEE packets to given set of clients and
	// \params
	// @clients is the set of recepients
	// @b is the ball data
	// @left, @right is the team data 
	
	private void sendSeeDataToPlayers( 	Enumeration clients, Ball b, 
								  		Vector left, Vector right )
								  		 			throws IOException 
	{
        SeeData see = new SeeData();
        see.time = getTicker();
        see.ball = b;
        see.leftTeam = left;
        see.rightTeam = right;

        Packet seePacket = new Packet();
        seePacket.packetType = Packet.SEE;
        seePacket.data = see;
        
        int i = 0;
        
        while ( clients.hasMoreElements() ) {
            
            Splayer player = (Splayer) clients.nextElement();
            
            Vector team = null;
            if ( player.side == 'l' )
            	team = left;
            else if ( player.side == 'r' )
            	team = right;
            
            see.player = (Player) team.remove(i); // the addressee of this info
            see.status = 0;
            if (player.iAmOffside)
                see.status = 1;
            else if ( player.side == 'l' && soccerRules.offsideL ||
            		  player.side == 'r' && soccerRules.offsideR )
                see.status = 2;

            seePacket.address = player.address;
            seePacket.port = player.port;
            
            SoccerServer.transceiver.send(seePacket);
            
            team.add(i, see.player);
            i++;
        }		
	}

    
    private void clean() 
    {
        // send the packet to clients
        Enumeration gamers = null;
        Splayer player = null;
        Sviewer viewer = null;

        int round = (int) ( 2*HALF_TIME * (60 / SIM_STEP_SECONDS));
        int idle = (int) (IDLE * (60 / SIM_STEP_SECONDS));

        gamers = soccerWorld.getLeftPlayers();
        while (gamers.hasMoreElements()) {
            player = (Splayer) gamers.nextElement();
            int diff = getTicker() - player.getLastTime();
            if (diff < 0)
                diff = diff + round;
            if (diff > idle) {
				soccerWorld.removePlayer(player);
				soccerWorld.returnLeftPlayerId(player.id);
            }
        }

        gamers = soccerWorld.getRightPlayers();
        while (gamers.hasMoreElements()) {
            player = (Splayer) gamers.nextElement();
            int diff = getTicker() - player.getLastTime();
            if (diff < 0)
                diff = diff + round;
            if (diff > idle) {
				soccerWorld.removePlayer(player);
				soccerWorld.returnRightPlayerId(player.id);
            }
        }

        gamers = soccerWorld.getViewers();
        while (gamers.hasMoreElements()) {
            viewer = (Sviewer) gamers.nextElement();
            int diff = getTicker() - viewer.getLastTime();
            if (diff < 0)
                diff = diff + round;
            if (diff > idle) {
				soccerWorld.removeViewer(viewer);
				soccerWorld.returnViewerId(viewer.viewerId);
            }
        }

    }

    //---------------------------------------------------------------------------
    public int getTicker() {
    	try{
			tickLock.lockRead();
			return ticker;
		}
		finally {
			tickLock.unlock();
		}
    }


    /**
     * @param i
     */
    public void setTicker(int i) 
    {
		try {
			tickLock.lockWrite();
			ticker = i;
		}    	
        finally {
			tickLock.unlock();
        }
    }
    
    public void resetTicker()
    {
    	setTicker( tickerToStartOver );	
    }

    
    /**
     * @return
     */
    public boolean isStepping() 
    {
		try{
			stepLock.lockRead();
			return isStepping;
		}
		finally {
			stepLock.unlock();
		}
    }

    /**
     * This method sets the Server state variable, |isStepping|, and sends  
     * messages to the clients about the changes of the Server state
     * @param dosteps
     */
    public void setStepping(boolean dosteps ) 
    {
		try {
			stepLock.lockWrite();
			isStepping = dosteps;
			if ( isStepping ) {
				try {
					sendInfoData(InfoData.WAIT_NEXT);	// send message
					//System.out.println(" RefereeData.WAIT_NEXT packet sent");	
				} catch (Exception e ) {}
			} else {
				try {
					sendInfoData(InfoData.RESUME);		// send message	
					//System.out.println(" RefereeData.RESUME packet sent");	
				} catch (Exception e ) {}
			}
		}    	
		finally {
			stepLock.unlock();
		}
    }

	/**
	 * @return
	 */
	public RandomAccessFile getSaved() {
		return saved;
	}

	/**
	 * @param file
	 */
	public void setSaved(RandomAccessFile file) {
		saved = file;
	}

	public void setReplication( int numOfSteps, int numOfReplicas, int stepID )
	{
		this.numOfSteps = numOfSteps;
		this.numOfReplicas = numOfReplicas;
		tickerToStartOver = stepID; 
		soccerWorld.replicationIsOn = true;
		setTicker( tickerToStartOver );
		numOfStepsLeft = 0;
		replicaCount = 1;
		soccerRules.setReplicationMode();
	}

	// this method send replica data to the viewer clients
	// (presumably, this client is just one)
	private void sendReplicaInfo()
	{
		try {
			InfoData aInfoData = 
				new InfoData( 
						InfoData.REPLICA, 	// info
						replicaCount,  		// info1
						numOfReplicas); 	// info2
					
			Packet packet =
				new Packet( 
						Packet.INFO,
						aInfoData,
						null,
						0);
        	
        	Enumeration viewers = soccerWorld.getViewers();
        	while ( viewers.hasMoreElements() ) {
	            Sviewer viewer = (Sviewer) viewers.nextElement();
	            packet.address = viewer.address;
	            packet.port = viewer.port;
	            SoccerServer.transceiver.send( packet );
	        }			
			//System.out.println("sending Packet.INFO - replica");
			
		} catch (IOException e) {

		}		
	}
}
