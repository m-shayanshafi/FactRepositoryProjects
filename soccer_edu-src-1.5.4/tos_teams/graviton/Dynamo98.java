/*
 * AIPlayers.java The AI players start program
 * 
 * Copyright (C) 2001 Yu Zhang
 *
 * Modifications by Vadim Kyrylov 
 * February 2006
 * (changed onlythe the main class name)
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package tos_teams.graviton;

import java.io.*;
import java.util.*;
import java.net.*;
import soccer.common.*;

// this is the main class of a demo TOS team that could be used as 
// a prototype for further improvement

public class Dynamo98 {

	static InetAddress address;

	static String host = "localhost";

	static int port = 7777;

	static int left = 11;

	static int right = 11;

	Vector robots = new Vector();

	private InitData init = null;

	public Dynamo98() {
		try {
			address = InetAddress.getByName(host);
		} catch (Exception e) {
			System.out.println("Network error:" + e);
			System.exit(1);
		}
    	
    	System.out.println(" -- Left team: --" );		
		for (int i = 0; i < left; i++) {
    		System.out.println("starting player " + (i+1) );
			init(true, i);
		}
    	System.out.println("\n -- Right team: --" );		
		for (int i = 0; i < right; i++) {
    		System.out.println("starting player " + (i+1) );
			init(false, i);
		}
    	System.out.println();		
	}

	// initialize the robot
	private void init(boolean left, int role) {
		try {
			Transceiver transceiver = new Transceiver(false);

			// Send the connect packet to server
			ConnectData connect;
			if (left)
				connect = new ConnectData(ConnectData.PLAYER, ConnectData.LEFT);
			else
				connect = new ConnectData(ConnectData.PLAYER, ConnectData.RIGHT);
			Packet initPacket = new Packet(Packet.CONNECT, connect, address,
					port);
			transceiver.send(initPacket);

			// wait for the connect message from server
			transceiver.setTimeout(1000);
			int limit = 0;
			Packet packet = null;
			while (limit < 60)
				try {
					packet = transceiver.receive();
					if (packet.packetType == Packet.INIT) {
						init = (InitData) packet.data;
						Robot robot = new Robot(transceiver, init, role);
						robots.addElement(robot);
						robot.start();
						break;
					}
					transceiver.send(initPacket);
					limit++;
				} catch (Exception e) {
				}
				
			transceiver.setTimeout(0);
			if (packet == null) {
				System.out.println("waiting for server: Timeout.");
				return;
			}

		} catch (Exception e) {
			System.out.println("Error during start up: " + e);
			return;
		}
	}

	public static void main(String argv[]) throws IOException {

		Properties properties = new Properties();
		String configFileName = null;
		
		System.out.println("### starting Dynamo98. (c)2001 by Yu Zhan  ###\n");
		
		try {
			// First look for parameters
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
					throw new Exception();
				}
			}
		} catch (Exception e) {
			System.err.println("");
			System.err.println("USAGE: Dynamo98 -pf property_file_name]");
			return;
		}

		Dynamo98 aDynamo98 = new Dynamo98();

	}

	//---------------------------------------------------------------------------
	/**
	 * set properties
	 */
	public static void setProperties(Properties properties) {
		left = Integer.parseInt(properties.getProperty("left_ream_size", "11"));

		right = Integer.parseInt(properties
				.getProperty("right_ream_size", "11"));

		host = properties.getProperty("host_address", "localhost");

		port = Integer.parseInt(properties.getProperty("port_number", "7777"));

	}
}