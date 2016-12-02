/* Observer.java
 A simple viewer agent that gathers statistics.

 Copyright (C) 2005  Yu Zhang
 * Created on Mar 12, 2005
 *
 *
 *	modified by Vadim Kyrylov
 *  January 2006
 *
 */
package tos_tools;

import java.io.*;
import java.net.InetAddress;
import java.util.Properties;

import soccer.common.*;
import tos_teams.sfu.WorldModel;

/**
 * this class collects data sent by the server and puts it in a text file.
 * the unique filename is generated automatically. by default, the file is 
 * located in the parent directory of /classes
 *  
 */
public class Observer extends Thread
{

	// default soccer server address settings
	static InetAddress address;

	static String host = "localhost";

	static int port = 7777;

	private Transceiver transceiver = null;

	private InitData init = null; // Initialization information from server.

	private ViewData view = null; // Visual information from server.

	private RefereeData referee = null; // Referee information from server.

	private int total_time = 0;

	private int ball_in_left_side = 0;

	private int ball_in_right_side = 0;

	private int ball_played_by_left = 0;

	private int ball_played_by_right = 0;

	// the file writing technique was acquired from 
	// http://www.particle.kth.se/~lindsey/JavaCourse/Book/Part1/Java/Chapter09/fileTextOut.html
	public 	String 		path = ""; // could be overriden from the Properties object
	public 	String 		filename = "TOSObserver";
	private PrintWriter print_writer; 
	private int 		recordCount;
	private int 		refereeCount;
	
	
	public Observer() 
	{
		// sets the server address.
		try {
			address = InetAddress.getByName(host);
		} catch (Exception e) {
			System.out.println("Network error:" + e);
			System.exit(1);
		}

		// sets up UDP networking utility;
		transceiver = new Transceiver(false);
		recordCount = 0;
		refereeCount = 0;
	}

	// establishes the connection to server, if it's sucessful, return true.
	private boolean init() 
	{
		try {

			//sends the connect packet to server
			ConnectData aConnectData = new ConnectData(ConnectData.VIEWER,
					ConnectData.ANYSIDE);

			Packet connectPacket = 
					new Packet(	Packet.CONNECT, 
								aConnectData, 
								address,
								port);
			
			transceiver.send(connectPacket);

			System.out.println("sending Packet.CONNECT");
			
			//wait for the initialization message from server
			transceiver.setTimeout(1000);
			int limit = 0;
			Packet mypacket = null;
			
			while (limit < 60)
				try {
					mypacket = transceiver.receive();
					
					if (mypacket.packetType == Packet.INIT) {
						// connection with server established
						System.out.println("Packet.INIT received");
						return fileCreated();
					}
					
					transceiver.send(connectPacket);
					
					System.out.println("Packet.CONNECT sent again");
					limit++;
				} catch (Exception e) {
					System.out.println("Error at the next step: " + e);
				}
			transceiver.setTimeout(0);
			if (mypacket == null) {
				System.out.println("unable to connect to server: Timeout.");
				return false;
			}
		} catch (Exception e) {
			System.out.println("Error during start up: " + e);
			return false;
		}
		return false;
	}

	// an endless loop of observing.
	// the Observer receives packets sent by the server and 
	// replies with empty packets thus maintaining the server awareness 
	// that this client is alive
	public void run() 
	{
		Packet packet = null;
		
		while (true) {
			try {	
				packet = transceiver.receive();
				
				packet.writePacket();
				
				getDataFromServer( packet );
								
			} catch (IOException ioe) {
				System.out.println("Error receiving packet: " + ioe);
			}
				
			total_time++;
			
			if (total_time % 600 == 0) {
				EmptyData empty = new EmptyData();
				Packet command =
					new Packet(
						Packet.EMPTY,
						empty,
						address,
						port);
						
				try {	
					transceiver.send(command);
				} catch (IOException e) {
					System.out.println("Error sending empty packet: " + e);
				}
			}			
		}
	}

	private void getDataFromServer( Packet packet ) 
	{
		//System.out.println("Entered getDataFromServer");
		try { 
			
			//packet.writePacket();
		
			// process the info
			if (packet.packetType == Packet.VIEW) {
				
				// same data is sent to the monitor.
				// this packet contains coordinate precise data about 
				// the ball and each player. see VewData class for details.
				
				view = (ViewData) packet.data;
				
				// collect statistics
				if (view.ball.position.getX() > 0)
					ball_in_right_side++;
				if (view.ball.position.getX() < 0)
					ball_in_left_side++;
				if (view.ball.controllerType == 'l')
					ball_played_by_left++;
				if (view.ball.controllerType == 'r')
					ball_played_by_right++;
					
				writeFile( view );	// write some data to file
				recordCount++;
				
				if ( recordCount%500 == 0 ) {
					System.out.println( "-- " + recordCount 
						+ " records written to this file so far --" );
				}
				
			} else if (packet.packetType == Packet.REFEREE) {
				
				referee = (RefereeData) packet.data;
				
				refereeCount++;
				
				if ( refereeCount % 10 == 0 ) {
					System.out.println();
					System.out.println("Current game period is: "
							+ RefereeData.periods[referee.period]);
					System.out.println("Current game mode is: "
							+ RefereeData.modes[referee.mode]);
					System.out.println("left  team score is: " + referee.score_L);
					System.out.println("right team score is: " + referee.score_R);
					System.out.println("Total play time(sec) is: " + total_time / 24);
					System.out
							.println("The time the ball stayed at the left  side is: "
									+ ball_in_left_side / 24);
					System.out
							.println("The time the ball stayed at the right side is: "
									+ ball_in_right_side / 24);
					System.out
							.println("The number of ball played by the left  side is: "
									+ ball_played_by_left);
					System.out
							.println("The number of ball played by the right side is: "
									+ ball_played_by_right);
					System.out.println();
				}

			}
		} catch (Exception e ) {
			System.out.println("Exception in getDataFromServer " + e);
		}
	}

	// this method could be customized for colecting different data and
	// possibly preprocessing them before writing to the file.
	// as it, it records ball location and ball possession data only.
	// the 'println' method here writes a line to the text file
	private void writeFile( ViewData view )  
	{
		String record = view.time + "     " +
						(int)( view.ball.position.getX()*100 )  + "    " + 
						(int)( view.ball.position.getY()*100 ) + "        " + 
						view.ball.controllerType; 
		
		if ( recordCount == 0 )
			print_writer.println( "step   ball_X    ball_Y    side" );
		
		print_writer.println( record );
		
		if (print_writer.checkError ()) {
            System.out.println("An output error occurred!" );
        }

	}
	
	// this method creates an output file with a standard name and
	// creates a wrapper object, print_writer, used for writing to this file
	// ** this is NOT the SoccerMaster log file **
	private boolean fileCreated() 
	{
		boolean success = false;
		String fullpath;
		
		try {
			// this would generate a unique file name every 10 seconds;
			// names will be repeated after about 3 years
			long time = System.currentTimeMillis()/10000;
			long num = 100000*100000;
			time = time % num; 
			fullpath = path + filename + time + ".txt";
			File logFile = new File( fullpath  );
			FileWriter file_writer = new FileWriter( logFile );
			BufferedWriter buf_writer = new BufferedWriter ( file_writer) ;
 			// this is a class variable
 			print_writer = new PrintWriter ( buf_writer, true );
			System.out.println("Created file << " + fullpath + " >>\n");
			
			// rename this thread 
			// (makes sense when more than on observer is running)
			setName( "Observer" + time%1000 );
	
			success = true;
		} catch ( Exception e ) {
			System.out.println("Unable to create " + path + filename );	
		}
		
		return success; 	
	}
	
	
	public static void main(String argv[]) throws IOException 
	{
		Properties properties = new Properties();
		String configFileName = null;

		System.out.println("Observer started \n");
		try {
			//	  First look for parameters
			for (int c = 0; c < argv.length; c += 2) {
				if (argv[c].compareTo("-pf") == 0) {
					configFileName = argv[c + 1];
					File file = new File(configFileName);
					if (file.exists()) {
						System.out.println("Load properties from file: "
								+ configFileName);
						properties = new Properties();
						properties.load(new FileInputStream(configFileName));
					} else {
						System.out.println("Properties file <" + configFileName
								+ "> does not exist.");
					}
				} else {
					System.out.println("Error parsing argument");
					throw new Exception();
				}
			}
		} catch (Exception e) {
			System.err.println("");
			System.err.println("USAGE: Observer -pf property_file_name]");
			return;
		}

		Observer viewer = new Observer();

		if ( viewer.init() )
				viewer.start();

	}

	//---------------------------------------------------------------------------
	/**
	 * set properties
	 */
	public static void setProperties(Properties properties) 
	{
		host = properties.getProperty("host_address", "localhost");

		port = Integer.parseInt(properties.getProperty("port_number", "7777"));

	}
}