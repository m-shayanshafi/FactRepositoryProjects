/*
 * VideoToons. A Tribute to old Video Games.
 * Copyright (C) 2001 - Bertrand Le Nistour
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package videotoons.game;

import videotoons.swing.ServerWaitWindow;

import java.net.*;           // sockets
import java.io.*;            // io & stream
import java.util.Vector;     // vectors


public class GameServer extends Thread implements GameDefinitions
{

  /****************************************************************************/

  // Server Socket
     private ServerSocket server;

  // Server Threads List
     private Vector v_sthread;

  // Server level process (positions, monsters and events management)
     private ServerLevelProcess s_level;

  // Server should quit ?
     private boolean should_end;

  // Game status : GM_READY, GM_ACTIVE, ... , GM_OVER
     private byte gm_status;

  // Current Level name
     private String level_name;

  // Game mode : ONEPLAYER, MULTIPLAYER or LASTMANSTANDING
     private byte game_mode;


  /****************************************************************************/
  /****************************** CONSTRUCTOR *********************************/
  /****************************************************************************/

        public GameServer( String level_name, byte game_mode )
        {
           // Inits
              super("NetworkServer");
              this.level_name = level_name;
              this.game_mode = game_mode;
              should_end = false;
              gm_status=GM_NONE;

           // Server info
              if(DEBUG_MODE)
                 System.out.println("Server creation...");

           // Thread list
              v_sthread = new Vector(NBMAX_PLAYERS);

           // ServerSocket inits
              try{
                  server = new ServerSocket(GAME_PORT); 
                  server.setSoTimeout(1000);                  
               }
               catch (IOException e){
                 System.out.println("Could not listen on port " + GAME_PORT + ", " + e.getMessage());
                 System.exit(1);
               }

           // Thread start
              start();
        }

  /**************************************************************************/

        public void createNewServerLevelProcess(){
            if(DEBUG_MODE)
                 System.out.println("Server: new ServerLevelProcess created...");

            s_level = new ServerLevelProcess( this );
        }


  /**************************************************************************/
  /*************************** THREAD ACTION ********************************/
  /**************************************************************************/

       public void run()
       {
         Socket client_socket;

         setGameStatus(GM_READY);

          if(DEBUG_MODE)
             System.out.println("Server waiting clients...");

         // We wait for players.
            do
            {
               client_socket = null;

                try
                {
                  try{
                      // we wait 1s for clients
                         client_socket = server.accept();

                      // Ok. a player is awaiting... do we accept him ?
                         if( ( v_sthread.size()>=NBMAX_PLAYERS
                               || getGameStatus()!=GM_READY ) && client_socket!=null  )
                         {
                            // max players reached... we send an error message.
                              DataOutputStream ds_snd = new DataOutputStream( 
                                                          new BufferedOutputStream(
                                                            client_socket.getOutputStream() ));
                              ds_snd.writeByte(MSG_ERROR);

                                 if( v_sthread.size()>=NBMAX_PLAYERS ) {                              
                                      ds_snd.writeByte(ERR_NOMOREROOMS);
                                      
                                      if(DEBUG_MODE)
                                          System.out.println("Server: Client not accepted. Too many players...");
                                 }
                                 else {
                                      ds_snd.writeByte(ERR_RUNSALREADY);                              

                                      if(DEBUG_MODE)
                                          System.out.println("Server: Client not accepted. Game already running...");
                                 }

                              ds_snd.flush();


                         }
                         else
                              registerClient( client_socket );

                     }
                     catch (InterruptedIOException ioe) { /* TimeOut exception */ }
                }
                catch (IOException e){
                   System.out.println("Accept ended on" + GAME_PORT + ": " + e.getMessage());
                   System.exit(1);
                }
             }
             while( serverShouldEnd()==false );

             if(DEBUG_MODE)
               System.out.println("Server quits...");

             try{
                server.close();
             }
             catch(IOException e){}
       }


  /**************************************************************************/
  /**************************** SERVER END ? ********************************/
  /**************************************************************************/

      synchronized public void stopServer(){
           VideoToons.deleteServerHandle();
           should_end = true;
      }

      synchronized public boolean serverShouldEnd(){
           return should_end;
      }


  /**************************************************************************/
  /****************************** GAME INFO *********************************/
  /**************************************************************************/

      synchronized public byte getGameStatus(){
           return gm_status;
      }

  /**************************************************************************/

      synchronized public void setGameStatus( byte val )
      {
         if( (gm_status==GM_KILLED || gm_status==GM_OVER) && val!=GM_READY )
               return;

         if(DEBUG_MODE)
            System.out.println("Server: Game mode changed to "+val);

         gm_status = val;
      }

  /**************************************************************************/

      public byte getGameMode(){
           return game_mode;
      }

  /**************************************************************************/

      public String getLevelName(){
         return level_name;
      }

  /**************************************************************************/
  /************************* USER EVENTS METHODS ****************************/
  /**************************************************************************/

      public void changeLevelName(String name){
         if(DEBUG_MODE)
           System.out.println("Server: Level changed...");

         level_name = name;
         s_level = null;
         setGameStatus(GM_READY);
      }

  /**************************************************************************/

      public void changeGameMode(byte mode){
         game_mode = mode;
      }

  /**************************************************************************/
  
      public void cancelGameServer() {
         setGameStatus(GM_KILLED);
      }

  /**************************************************************************/

      public void startGame() {
         setGameStatus(GM_ACTIVE);
      }

  /**************************************************************************/
  /************************* CLIENT MANAGEMENT ******************************/
  /**************************************************************************/

   /** We register a new client an create a new ServerThread.
    *
    *  @param socket a socket connected to the client. 
    */

      synchronized private void registerClient( Socket socket )
      {
        // We create a new thread to discuss with the client

           if(DEBUG_MODE)
     	         System.out.println("Server: new client accepted...");

           byte thread_ID = getNewThreadID();
           v_sthread.insertElementAt( new GameServerThread( thread_ID, socket, this ),
                                      thread_ID );

        // a little sound to warn that a new client has arrived...
           if( thread_ID!=0 )
               VideoToons.getSoundLibrary().playSound( SoundLibrary.HELLO_SOUND );
      }

  /**************************************************************************/

      synchronized public Vector getClientList(){
          return v_sthread;
      }

  /**************************************************************************/

   /** We return a new thread_ID for our new client
    *
    *  @return the new client's thread ID 
    */

      private byte getNewThreadID()
      {
        GameServerThread th;

        if(v_sthread.isEmpty())  // client list is empty
          return 0;

        // we search for a missing thread_ID
      	     for(byte i=0;i<v_sthread.size();i++) 
      	     {
      	        th = (GameServerThread) v_sthread.get(i);

                 if ( !Tools.equal("Thread"+i,th.getName()) )
                     return i;  // we fill the hole
      	     }
      	 
      	 return (byte) v_sthread.size();  // list end
      }


  /**************************************************************************/

   // To signal that a client quits ...
      synchronized public void removeClient(byte thread_ID)
      {
        GameServerThread th;
      	
      	     for(byte i=0;i<v_sthread.size();i++)
      	     {
      	        th = (GameServerThread) v_sthread.get(i);

                 if ( Tools.equal("Thread"+thread_ID,th.getName()) )
                 {
      	             v_sthread.removeElementAt(i);
      	             
      	             if(DEBUG_MODE)
      	                System.out.println("Server: client thread"+thread_ID+" quits...");

                   // window update (if it exists)
                      ServerWaitWindow.updateClientList(v_sthread);

      	           // if we've just removed the last client, we remove the server
      	              if(v_sthread.size()==0)
      	              {
      	              	// is there a ServerLevelProcess to stop ?
                           if(s_level!=null)
                              try{
                                  setGameStatus( GM_KILLED );
                                  s_level.setIOMode( s_level.DATA_TRANSFERT_DONE );
                              }
                              catch(Exception e){} // our ServerLevelProcess could already be dead

                          if(DEBUG_MODE)
                             System.out.println("Server: we will stop in a few ms...");

      	                  stopServer();
      	              }

      	           return;
      	         }
      	     }

             if(DEBUG_MODE)
                System.out.println("Server: client thread"+thread_ID+" not found ! removeClient aborted");
      }

  /**************************************************************************/
  
   // Number of clients... 
      synchronized public byte getClientsNumber(){
             return (byte) v_sthread.size();
      }


  /**************************************************************************/

   // To signal that a client quits ...
      synchronized public GameServerThread getClient( byte index ){
            return (GameServerThread) v_sthread.get(index);
      }

  /**************************************************************************/

   // Returns a client_ID considering the thread_ID ...
   // Warning : to use only when the game starts... (no more clients)

      public byte getClientID( byte thread_ID )
      {
        GameServerThread th;

      	   for(byte i=0;i<v_sthread.size();i++)
      	   {
      	      th = (GameServerThread) v_sthread.get(i);

                 if ( Tools.equal("Thread"+thread_ID,th.getName()) )
      	           return i;
      	   }

      	 return -1;  // should never happen if rightly used !
      }

  /**************************************************************************/
  /**************************** DATA ACCESS *********************************/
  /**************************************************************************/

      public ServerLevelProcess getServerLevelProcess(){
         return s_level;
      }

  /**************************************************************************/
}





