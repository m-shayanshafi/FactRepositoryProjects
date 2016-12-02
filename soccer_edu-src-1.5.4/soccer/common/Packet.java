/* Packet.java

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


package soccer.common;

import java.io.*;
import java.util.*;
import java.net.*;

/**
 * This class defines the specialized UDP soccer packet between server and client
 *
 * @author Yu Zhang 
 */

public class Packet
{

  /**
   * Open Token value for a packet, which is '('
   */
  public static final char OPEN_TOKEN  = '(';
  /**
   * Close Token value for a packet, which is ')'
   */
  public static final char CLOSE_TOKEN = ')';

  // Packet Types.
  /**
   * packet type appears as the letter at the start of the packet, used as
   * an identifer
   */

  public static final char INFO        = 'a';   // server -> client
  public static final char BYE         = 'b';   // client -> server  
  public static final char CONNECT     = 'c'; 	// client -> server
  public static final char DRIVE       = 'd';   // client -> server
  public static final char EMPTY       = 'e';   // client -> server
  public static final char HEAR        = 'h'; 	// server -> client  
  public static final char INIT        = 'i';   // server -> client
  public static final char KICK        = 'k';   // client -> server
  public static final char PERIOD      = 'p';   // client -> server 
  public static final char REFEREE     = 'r'; 	// server -> client  
  public static final char SEE         = 's';   // server -> client
  public static final char SITUATION   = 'y';   // client -> server  
  public static final char TALK        = 't';   // client -> server  
  public static final char TELEPORT    = 'z';   // client -> server  
  public static final char VIEW        = 'v';   // server <-> client 


  /**
   * Describes the type of packet.  This is used to determine the
   * expected data content.
   */
  public char packetType;

  /**
   * The actual data content in the packet.
   */
  public Data data;
  
  /** 
   * UDP packet address
   */
  public InetAddress address;

  /**
   * UDP packet port number
   */
  public int port;
  
  /**
   * Constructs a default empty packet for receiving and parsing data from UDP socket.
   */
  public Packet()
  {
    this.packetType = '?';
    this.data = null;
  } 

  /**
   * Constructs a packet for a particular packet type to be sent over UDP socket.
   * the network address is set to localhost, and port number is set to 7777.
   *
   * @param type         the packet type
   * @param data         the object which actually holds the data
   */
  public Packet(char type, Data data)
  {
    this.packetType = type;
    this.data = data;
    try
    {
      address = InetAddress.getByName("localhost");
    }
    catch(Exception e)
    {
		e.printStackTrace();
      	System.exit(1);
    }
    port = 7777;
  }
  
  /**
   * Constructs a packet for a particular packet type to be sent over UDP socket.
   *
   * @param type         the packet type
   * @param data         the object which actually holds the data
   * @param address      the UDP packet's destination 
   * @param port         the port number of the receiving application
   */
  public Packet(char type, Data data, InetAddress address, int port)
  {
    this.packetType = type;
    this.data = data;
    this.address = address;
    this.port = port;
  }
	 
  /**
   * ReadPacket will try to get packet information from a string.
   * It checks the first word to see which packet it is and procedes to 
   * read the Data content.
   * <p>
   * It uses StringTokenizer to parse the data stored in the string.
   * Once it finds out the type of the packet(data), it initializes
   * a related data object(among ConnectData, InitData, SeeData, RefereeData,
   * DriveData, KickData, and ViewData), and passes the rest of the
   * StringTokenizer to its Data object for further parsing. After it's
   * done, the newly created Data object will be populated by the parsed
   * data.
   * 
   * @param     s           the string which contains data
   * @exception IOException If any parsing problem occured, such as 
   *                        Invalid open token, or Unknown packet type
   * @see Data#readData(StringTokenizer)
   */
  public final void readPacket(String s) throws IOException 
  {

    // set up a StringTokenizer for parsing
    StringTokenizer st = new StringTokenizer(s, "() ", true);
    
    // It should get OPEN_TOKEN first.
    if(!(st.nextToken().charAt(0) == OPEN_TOKEN))
      throw new IOException( "readPacket: Invalid open token" );           

    // Identify the packet type.
    packetType = st.nextToken().charAt(0);

    // Read the data contents.
    if(packetType == CONNECT)
      data = new ConnectData();
    else if(packetType == INFO)
      data = new InfoData();
    else if(packetType == INIT)
      data = new InitData();
    else if(packetType == SEE)
      data = new SeeData();
    else if(packetType == REFEREE)
      data = new RefereeData();
    else if(packetType == DRIVE)
      data = new DriveData(); 
    else if(packetType == KICK)
      data = new KickData();
    else if(packetType == VIEW)
      data = new ViewData();
    else if(packetType == HEAR)
      data = new HearData();
    else if(packetType == TALK)
      data = new TalkData();      
    else if(packetType == EMPTY)
      data = new EmptyData();
    else if(packetType == BYE)
      data = new ByeData();  
    else if(packetType == PERIOD)
        data = new PeriodData();           
    else if(packetType == TELEPORT)
        data = new TeleportData();           
    else if(packetType == SITUATION)
        data = new SituationData();           
    else 
    	throw new IOException( "readPacket: Unknown packet type" );

    // Get the " "
    st.nextToken();             

    // Read data.
    data.readData(st);

  } 

  /**
   * writePacket will put a packet name, related data to a string.
   * <p>
   * This method first creates a StringBuffer, puts in general header
   * information. Then it checks packet(data) type to use related 
   * data object's writeData(StringBuffer) method to fill the data to
   * the StringBuffer. Finally, it appends the close token to the 
   * StringBuffer, converts the StringBuffer to String and return the
   * String. The returned String is then sent by class Transceiver over UDP. 
   *
   * @return the String contains the encoded packet data 
   * @see Data#writeData(StringBuffer)
   */
   
  public final String writePacket() throws IOException 
  {
    StringBuffer sb = new StringBuffer(); 
    
    sb.append(OPEN_TOKEN);

    // Write data.
    if(packetType == CONNECT)
      ((ConnectData)data).writeData(sb);
    else if(packetType == INFO)
      ((InfoData)data).writeData(sb);
    else if(packetType == INIT)
      ((InitData)data).writeData(sb);
    else if(packetType == SEE)
      ((SeeData)data).writeData(sb);
    else if(packetType == REFEREE)
      ((RefereeData)data).writeData(sb);
    else if(packetType == DRIVE)
      ((DriveData)data).writeData(sb);
    else if(packetType == KICK)
      ((KickData)data).writeData(sb);
    else if(packetType == VIEW)
      ((ViewData)data).writeData(sb);
    else if(packetType == HEAR)
      ((HearData)data).writeData(sb);      
    else if(packetType == TALK)
      ((TalkData)data).writeData(sb);
    else if(packetType == EMPTY)
      ((EmptyData)data).writeData(sb);
    else if(packetType == BYE)
      ((ByeData)data).writeData(sb);
    else if(packetType == PERIOD)
        ((PeriodData)data).writeData(sb);            
    else if(packetType == TELEPORT)
        ((TeleportData)data).writeData(sb);            
    else if(packetType == SITUATION)
        ((SituationData)data).writeData(sb);            
    else 
    	throw new IOException( "writePacket: Unknown packet type" );     

    // Write CLOSE_TOKEN
    sb.append(CLOSE_TOKEN);

    return sb.toString();

  }
  
  
  //---------------------------------------------------------------------------
  /**
   * Convert class to String
   */
  public String toString(){
    
    return "Packet type: " + packetType;
  }
  
}
