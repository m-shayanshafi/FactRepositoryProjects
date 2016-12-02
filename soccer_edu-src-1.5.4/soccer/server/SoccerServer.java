/* SoccerServer.java

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
import java.io.*;
import java.util.*;

/*
 * SoccerServer
 *
 * The SoccerServer runs three main threads.  The first
 * is the Console Thread.  It gives you a simple
 * interface that responds to text commands, and quit.
 * The second is the Host Thread.  It creates a
 * SocketServer and waits...  The last is the HeartOfWorld Thread
 * that operates the main logic functions of the game.
 */

public class SoccerServer {

	public static final int MAJOR_VERSION = 1;
	public static final int MINOR_VERSION = 5;
	public static final int PATCH_VERSION = 4;
	
	//maximal size of received packet (may be critical for modifications) 
	public static final int MAX_PACKET_SIZE = 128;  // (default 1024)	
	
	public static final String VERSION_LABEL = "";
	public final static String APP_VERSION =
		MAJOR_VERSION
			+ "."
			+ MINOR_VERSION
			+ "."
			+ PATCH_VERSION
			+ VERSION_LABEL;
	public final static String APP_NAME = "Tao Of Soccer:- Server ";

	// The transceiver set up a UDP channel
	public static Transceiver transceiver = null;
	
	//---------------------------------------------------------------------------
	/**
	 * constructor
	 */
	public SoccerServer() 
	{
		// Initialize the application.
		soccerWorld = new SoccerWorld();
		soccerRules = new SoccerRules(soccerWorld);
		
		heartOfWorld = new HeartOfWorld(soccerWorld, soccerRules);
		host = new Host(soccerWorld, heartOfWorld, soccerRules);
		
		// ** server priority is +2 higher than that of the clients
		heartOfWorld.setPriority( Thread.NORM_PRIORITY + 2 );
		host.setPriority( Thread.NORM_PRIORITY + 2 );

	}

	//---------------------------------------------------------------------------
	// Console loop methods 
	// Reads lines (words actually) and performs the appropriate actions
	static void console() 
	{
		// A welcome message
		System.out.print(
			"\n"
				+ APP_NAME
				+ APP_VERSION
				+ "\nSServer:'quit' to end the server, or 'help' to get a list of commands.\nSServer>\n");

		// Drop into the loop.
		DataInputStream dis = new DataInputStream(System.in);

		boolean ok = true;

		while (ok) {
			try {
				StringTokenizer st;

				st = new StringTokenizer(Util.readLine(dis));

				if (st.hasMoreTokens()) {
					String command = st.nextToken();
					if (command.equals("quit")) {
						ok = false;
						System.out.print("\nSServer:Bye.\n");
					} else if (command.equals("left_name")) {
						if (st.hasMoreTokens())
							SoccerWorld.LEFTNAME = new String(st.nextToken());
						System.out.print("\nSServer>");

					} else if (command.equals("right_name")) {
						if (st.hasMoreTokens())
							SoccerWorld.RIGHTNAME = new String(st.nextToken());
						System.out.print("\nSServer>");
					} else {
						System.out.print(
							"\nSServer:Help for SoccerServer application:"
								+ "\nSServer:left_name  XXX - Set left team name to XXX."
								+ "\nSServer:right_name XXX - Set right team name to XXX."
								+ "\nSServer:help           - Is this.\n"
								+ "\nSServer:quit           - Exits the server killing all games."
								+ "\nSServer>");
					}
				}
			} catch (Exception e) {
			}
		}
	}

	//---------------------------------------------------------------------------
	/**
	 * Initialize the application, then drop into a console loop
	 * until it's time to quit.
	 */
	public static void main(String args[]) 
	{
		MyProperties 	properties 	= new MyProperties();
		boolean 	iAmOffside 	= true;
		boolean 	log 		= false;
		boolean 	fileexists 	= false;
		boolean 	verbose 	= false;
		String 		configFileName = null;

		try {
			// looking for config filename
			if (args[0].compareTo("-pf") == 0) {
				configFileName = args[1];
				File file = new File(configFileName);
				if (file.exists()) {
					fileexists = true;
				} else {
					System.out.println(
						"Properties file <"
							+ configFileName
							+ "> does not exist.");
				}
			} 
			// figure out about loading the file with verbous output
			try {
				if (args[2].compareTo("-verbose") == 0) 
					verbose = true;
			} catch (Exception e ) { }
			
			if ( fileexists ) {		
				System.out.println(
					"===  Load properties from file: " 
								+ configFileName + "  ===");
				properties = new MyProperties( verbose );
				try {
					properties.load(new FileInputStream(configFileName));
				} catch (Exception e ) {
					System.out.println("Error reading config file: " + e );	
					System.out.println("Using all default parameter values instead");	
				}
			}

		} catch (Exception e) {
			System.out.println("Error reading run parameters: " + e );	
			System.out.println("Using all default parameter values instead");	
			printStartMsg();  
		}

		SoccerServer server = new SoccerServer();

		try { 
			server.setProperties(properties);
			SoccerRules.setMaxGrabbedSteps(); 
		} catch (NumberFormatException e) {
			System.out.println( "Erorr reading property file."
					+ " Some propeties set to defaults. \n" + e  );
		}	
			

		if (log) {
			try {
				Calendar now = new GregorianCalendar();
				RandomAccessFile saved =
					new RandomAccessFile(
						"log_"
							+ now.get(Calendar.YEAR)
							+ (int)(now.get(Calendar.MONTH)+1)
							+ now.get(Calendar.DAY_OF_MONTH)
							+ now.get(Calendar.HOUR_OF_DAY)
							+ now.get(Calendar.MINUTE),
						"rw");
				server.heartOfWorld.setSaved(saved);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}

			server.heartOfWorld.log = true;

		}

		server.init();

		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		// Drop into console loop.
		console();

		// Do an exlpicit exit to terminate any running treads.
		System.exit(0);
	}

	private static void printStartMsg() 
	{
			System.out.println("\n ***  Starting Tao of Soccer Server. *** \n");
			System.out.print(
				"\nSServer:USAGE: java SoccerServer [-key1 value [-key2] ]");
			System.out.print("\nSServer:");
			System.out.print("\nSServer:Key    value           default");
			System.out.print("\nSServer:-------------------------------------");
			System.out.print("\nSServer:pf   property_file_name  ./properties");
			System.out.print("\nSServer:verbose                  ./print out property values flag");
			System.out.print("\nSServer:");
			System.out.print("\nSServer:Examples:");
			System.out.print("\nSServer:java soccer.server.SoccerServer -pf server.ini");
			System.out.print("\nSServer:java soccer.server.SoccerServer -pf server.ini -verbous");
			System.out.println();
			System.out.println();
	}

	//---------------------------------------------------------------------------
	/**
	 * Initialize server
	 */
	public void init() 
	{
		try {
			transceiver = new Transceiver(Host.PORT);
			transceiver.setSize( MAX_PACKET_SIZE );
		} catch (Exception e) {
			System.out.print(
				"\nSServer:start up at port("
					+ Host.PORT
					+ ")"
					+ " fails:"
					+ e);

			System.exit(1);
		}

		// Heart of World begins to pump 'blood'
		heartOfWorld.start();

		// Host begins to take care of visitors
		host.start();

	}

	//---------------------------------------------------------------------------
	/**
	 * @return world object
	 */
	public SoccerWorld getWorld() 
	{
		return soccerWorld;
	}

	//---------------------------------------------------------------------------
	/**
	 * set properties
	 */
	public void setProperties(MyProperties properties) 
	{
		SoccerWorld.TEAM_FULL =
			Integer.parseInt(
				properties.getProperty(
					"team_full",
					String.valueOf(SoccerWorld.TEAM_FULL)));

		SoccerWorld.VIEWER_FULL =
			Integer.parseInt(
				properties.getProperty(
					"viewer_full",
					String.valueOf(SoccerWorld.VIEWER_FULL)));

		SoccerRules.KICK_OFF_TIME =
			Integer.parseInt(
				properties.getProperty(
					"kick_off_time",
					String.valueOf(SoccerRules.KICK_OFF_TIME))); 
					
		SoccerRules.MAX_GRABBED_TIME =
			Double.parseDouble(
				properties.getProperty(
					"max_grabbed_time",
					String.valueOf(SoccerRules.MAX_GRABBED_TIME))); 

		SoccerWorld.LEFTNAME =
			properties.getProperty("left_name", SoccerWorld.LEFTNAME);
			
		SoccerWorld.RIGHTNAME =
			properties.getProperty("right_name", SoccerWorld.RIGHTNAME);

		Host.PORT =
			Integer.parseInt(
				properties.getProperty("port", String.valueOf(Host.PORT)));

		HeartOfWorld.GAMES_TO_PLAY = 
			Integer.parseInt(
				properties.getProperty(
					"games_to_play",
					String.valueOf(HeartOfWorld.GAMES_TO_PLAY)));  
		
		HeartOfWorld.RESET_SCORE = 
			Boolean.parseBoolean(
				properties.getProperty(
					"reset_score",
					String.valueOf(HeartOfWorld.RESET_SCORE)));  

		HeartOfWorld.SIM_STEP_SECONDS =
			Double.parseDouble(
				properties.getProperty(
					"sim_step_seconds",
					String.valueOf(HeartOfWorld.SIM_STEP_SECONDS)));

		HeartOfWorld.NO_GAME_SECONDS =
			Double.parseDouble(
				properties.getProperty(
					"no_game_seconds",
					String.valueOf(HeartOfWorld.NO_GAME_SECONDS)));

		HeartOfWorld.HALF_TIME =
			Double.parseDouble(
				properties.getProperty(
					"half_time",
					String.valueOf(HeartOfWorld.HALF_TIME)));

		HeartOfWorld.IDLE =
			Integer.parseInt(
				properties.getProperty(
					"idle",
					String.valueOf(HeartOfWorld.IDLE)));

		Splayer.MAXSPEED =
			Double.parseDouble(
				properties.getProperty(
					"maxspeed_p",
					String.valueOf(Splayer.MAXSPEED)));

		Splayer.TIMETOMAX =
			Double.parseDouble(
				properties.getProperty(
					"timetomax_p",
					String.valueOf(Splayer.TIMETOMAX)));

		Splayer.MAXDASH =
			Double.parseDouble(
				properties.getProperty(
					"maxdash",
					String.valueOf(Splayer.MAXDASH)));

		Splayer.MINDASH =
			Double.parseDouble(
				properties.getProperty(
					"mindash",
					String.valueOf(Splayer.MINDASH)));

		Splayer.MAXKICK =
			Double.parseDouble(
				properties.getProperty(
					"maxkick",
					String.valueOf(Splayer.MAXKICK)));

		Splayer.MINKICK =
			Double.parseDouble(
				properties.getProperty(
					"minkick",
					String.valueOf(Splayer.MINKICK)));

		Splayer.RANDOM =
			Double.parseDouble(
				properties.getProperty(
					"random_p",
					String.valueOf(Splayer.RANDOM)));

		Splayer.KICKRANDOM =
			Double.parseDouble(
				properties.getProperty(
					"kick_random",
					String.valueOf(Splayer.KICKRANDOM)));

		Splayer.DRIBBLEFACTOR =
			Double.parseDouble(
				properties.getProperty(
					"dribble_factor",
					String.valueOf(Splayer.DRIBBLEFACTOR)));

		Splayer.NOWORD =
			Double.parseDouble(
				properties.getProperty(
					"noword",
					String.valueOf(Splayer.NOWORD)));

		Splayer.COLLIDERANGE =
			Double.parseDouble(
				properties.getProperty(
					"collide_range",
					String.valueOf(Splayer.COLLIDERANGE)));

		Sball.NOBALL =
			Double.parseDouble(
				properties.getProperty(
					"noball",
					String.valueOf(Sball.NOBALL)));

		Sball.CONTROLRANGE =
			Double.parseDouble(
				properties.getProperty(
					"control_range",
					String.valueOf(Sball.CONTROLRANGE)));

		Sball.MAXSPEED =
			Double.parseDouble(
				properties.getProperty(
					"maxspeed_b",
					String.valueOf(Sball.MAXSPEED)));

		Sball.RANDOM =
			Double.parseDouble(
				properties.getProperty(
					"random_b",
					String.valueOf(Sball.RANDOM)));

		Sball.FRICTIONFACTOR =
			Double.parseDouble(
				properties.getProperty(
					"friction_factor",
					String.valueOf(Sball.FRICTIONFACTOR)));
		
		SoccerRules.OFFSIDERULE =
			Boolean.valueOf(
				properties.getProperty(
					"offside_on",
					"true")).booleanValue();
		
		log =
			Boolean.valueOf(
				properties.getProperty(
					"log_on",
					"false")).booleanValue();
		
		Host.PORT = 			
			Integer.parseInt(
				properties.getProperty(
						"port_number",
						"7777"));

	}

	//---------------------------------------------------------------------------
	// Private members
	/** The soccerWorld simulates the soccer game environment. */
	private SoccerWorld soccerWorld = null;
	private SoccerRules soccerRules = null;
	/** The HeartOfWorld operates the main logic of the game, and sends out sensing packets. */
	private HeartOfWorld heartOfWorld = null;
	/** The Host receives incoming packets. */
	private Host host = null;
	private boolean log = false;
}
