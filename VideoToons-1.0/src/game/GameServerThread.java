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

import java.net.*;
import java.io.*;


class GameServerThread extends IOControlThread
{

  /****************************************************************************/

     // our socket client <--> Server
        private Socket socket;

     // Communication streams
        private DataInputStream  ds_rcv;
        private DataOutputStream ds_snd;

     // our master server
        private GameServer server;


  /**************************** THREAD IDENTITY *******************************/

     // Our client ID. The client_ID list is CONTINUOUS.
     // It is used by Client & Server level processes to reference players.
        private byte client_ID;

     // Our thread ID. The thread_ID list has HOLES in it (players can quit...).
     // It is used by the GameServer to reference its player threads.
        private byte thread_ID;

     // Our player name
        private String pl_name;

     // Our player sprite name.
        private String spr_name;

  /****************************************************************************/
  /****************************** CONSTRUCTOR *********************************/
  /****************************************************************************/
  
       public GameServerThread( byte thread_ID, Socket socket, GameServer server )
       {
         super("Thread"+thread_ID);

         this.socket = socket;
         this.server = server;
         this.thread_ID = thread_ID;
         client_ID = -1;

           try{
                // We inits the data buffered streams
                   ds_rcv = new DataInputStream( new BufferedInputStream(socket.getInputStream()));
	           ds_snd = new DataOutputStream( new BufferedOutputStream(socket.getOutputStream()));
              }
            catch(IOException e){
            	e.printStackTrace();
                return;
 	    }

         if(DEBUG_MODE)
            System.out.println("GameServerThread: thread"+thread_ID+" opens streams...");

         start();
       }

  /****************************************************************************/
  /***************************** THREAD ACTION ********************************/
  /****************************************************************************/

     public void run()
     {
        try
        {
         // I - We meet the client
            if( handShakeClient() == false )
                   return;

         // window update (if it exists)
            ServerWaitWindow.updateClientList(server.getClientList());

            do
            {
             // II - We wait for the Server host to start the game
                if( waitGameToStart() == false )
                     return;

             // III - We ask our client to get ready and we wait for his answer
                if( pleaseGetReady() == false )
                     return;
            }
            while( gameLoop() == true );    // IV - Game Loop

            if(DEBUG_MODE)
               System.out.println("GameServerThread: "+getName()+" exiting after game loop...");

        }
        catch(IOException e)
        {
            if(DEBUG_MODE)
              System.out.println("GameServerThread: ioexception: "+e.getMessage() );
 
         // We decrement the IO_max limit
            decrPlayerLimit();       

         // We try to end properly...
            try{
                 ds_snd.writeByte(MSG_ERROR);
                 ds_snd.writeByte(ERR_SOCKFAILED);
                 ds_snd.flush();
            }
            catch(IOException e2){}

           msg_playerquits();
        }
     }


  /****************************************************************************/
  /***************************** MESSAGES ACTIONS *****************************/
  /****************************************************************************/

     private void msg_playerquits()
     {
     	if(DEBUG_MODE)
     	   System.out.println("GameServerThread: Client "+getName()+" quits...");
     	
     	try{
               ds_snd.close();
               ds_rcv.close();
               socket.close();
           }
           catch (IOException e){
                if(DEBUG_MODE)	
           	   System.out.println("GameServerThread: failed to close streams.");
           }

       // We clean our basic sprite
          BasicSprite sp;
    
          try{
                sp = server.getServerLevelProcess().getNetPlayer( client_ID );
             }
            catch(Exception e){
                sp = null;
            }  // The ServerLevelProcess could be dead

          if(sp!=null) {
              sp.setStatus(BS_GAMEOVER);
              sp.setVisibility(false);
          }

       // We unregister from the Server
          server.removeClient(thread_ID);
    }

  /****************************************************************************/

      private boolean handShakeClient() throws IOException
      {
       // Warm welcome
          ds_snd.writeByte(MSG_WELCOME);
          ds_snd.flush();

          if(DEBUG_MODE)
             System.out.println("GameServerThread: Waiting for client's response.");

          switch( waitForByte() )    // header
          {
             case MSG_THANKYOU:
                            // we store the player name...
                               pl_name = receiveString();

                            // ...and its sprite name.
                               spr_name = receiveString();
                               return true;
             default:
                            // Error
                               ds_snd.writeByte(MSG_ERROR);
                               ds_snd.writeByte(ERR_BADMESSAGE);
                               ds_snd.flush();

             case MSG_PLAYERQUITS:
                               msg_playerquits();
                               return false;
          }
      } 


  /****************************************************************************/

      private boolean waitGameToStart() throws IOException
      {
         if(DEBUG_MODE)
           System.out.println("GameServerThread: Waiting for game to start");

      	 while( server.getGameStatus()!=GM_ACTIVE )
         {
             Tools.waitTime( 300 );  // let's spare our CPU a little...

          // Any network message ?
             if ( ds_rcv.available() != 0 )
               switch( ds_rcv.readByte() )    // header
               {
                   case MSG_KILLALL:
                               server.setGameStatus(GM_KILLED);
                               break;
                   default:
                            // Error
                               ds_snd.writeByte(MSG_ERROR);
                               ds_snd.writeByte(ERR_BADMESSAGE);
                               ds_snd.flush();

                    case MSG_PLAYERQUITS:
                               msg_playerquits();
                               return false;
               }

         // Any KILL game event ?
            if(server.getGameStatus()==GM_KILLED)
            {
               ds_snd.writeByte(MSG_KILL);
               ds_snd.flush();
               
               if(DEBUG_MODE)
                 System.out.println("GameServerThread: Kill message sent...");
               
               msg_playerquits();
               return false;
            }
         }

        return true;
      }


  /****************************************************************************/

      private boolean pleaseGetReady() throws IOException
      {
      	if(DEBUG_MODE)
      	   System.out.println("GameServerThread: let's get ready !!! ("+getName()+")");
      	
        client_ID = server.getClientID( thread_ID );

       // If we are Client 0 we create the ServerLevelProcess
          if(client_ID==0)
              server.createNewServerLevelProcess();

       // We ask our client to get ready
          ds_snd.writeByte(MSG_GETREADY);
          ds_snd.writeByte(client_ID);
          sendString( server.getLevelName() );


          ds_snd.writeByte( server.getClientsNumber() );

       // WARNING: We only send the data of the other players
          for(byte i=0; i<server.getClientsNumber(); i++ )
          {
              if(i==client_ID)  continue;
            
              GameServerThread sthread = server.getClient((byte)i);
              sendString( sthread.getPlayerName() );
              sendString( sthread.getSpriteName() );
          }

          ds_snd.flush();

          if(DEBUG_MODE)
             System.out.println("GameServerThread: Data sent... waiting for ack ("+getName()+")");

       // We wait for the response
           switch( waitForByte() )    // header
           {
              case MSG_ACK:
                            // We increment the IO counter & wait for late clients
                               incrIOCounter(WAIT);

                            // Ok ! let's play !
                               ds_snd.writeByte(MSG_GOPLAY);
                               ds_snd.flush();
                               break;
              case MSG_KILLALL:
                               server.setGameStatus(GM_KILLED);
                               break;
              default:
                            // Error
                               ds_snd.writeByte(MSG_ERROR);
                               ds_snd.writeByte(ERR_BADMESSAGE);
                               ds_snd.flush();

              case MSG_PLAYERQUITS:
                            // We decrement the IO_max limit
                               decrPlayerLimit();
                               msg_playerquits();
                               return false;
           }

       // Any KILL game event ?
          if(server.getGameStatus()==GM_KILLED)
          {
              ds_snd.writeByte(MSG_KILL);
              ds_snd.flush();
              decrPlayerLimit();
              msg_playerquits();
              return false;
          }

        if(DEBUG_MODE)
           System.out.println("GameServerThread: "+getName()+" entering game loop...");

        return true;
      }


  /****************************************************************************/

      private boolean gameLoop() throws IOException
      {
        BasicSprite sp;
        BrokenStone bs;
        FilledHole fh;
        ServerLevelProcess s_level = server.getServerLevelProcess();
        byte val,game_mode,header;
        boolean sounds[];

         do
         {
           // 1 - We wait for the input mode
              waitForIOMode(s_level.DATA_INPUT);

           // 2 - server data update
              do
              {
                header = waitForByte();
              	
                 if(header==MSG_END)
              	   break;

                  switch( header )
                  {
                     case MSG_CLIENTDATA :
                                            if( ds_rcv.readByte()!=client_ID ) {
                                               System.out.println("Error: Client mismatch !");
                                               BasicSprite.flushReceivedData( ds_rcv );
                                               break;
                                            }

                                            sp = s_level.getNetPlayer( client_ID );
                                            sp.receiveData( ds_rcv );
                                            break;
                     case MSG_SOUND :
                                         s_level.addSound( ds_rcv.readByte() );
                                         break;

                     case MSG_BROKENSTONE:
                                            s_level.setBrokenStone( ds_rcv );
                                            break;
                     case MSG_LEVEL_UP:
                                         val = ds_rcv.readByte();
                                         s_level.setCurrentTopTunnel( val );
                                         break;
                     case MSG_SCORE:
                                         s_level.setPlayerScore( client_ID, ds_rcv.readInt() );
                                         break;
                     case MSG_PAUSE:
                                         server.setGameStatus(GM_PAUSE);
                                         s_level.addSound(SoundLibrary.PAUSE_SOUND);
                                         break;
                     case MSG_PAUSEEND:
                                         server.setGameStatus(GM_ACTIVE);
                                         break;
                     case MSG_KILLALL:
                                         server.setGameStatus(GM_KILLED);
                                         break;
                     default:
                            // Error
                               ds_snd.writeByte(MSG_ERROR);
                               ds_snd.writeByte(ERR_BADMESSAGE);
                               ds_snd.flush();

                     case MSG_PLAYERQUITS:
                            // We decrement the IO_max limit
                               decrPlayerLimit();
                               msg_playerquits();
                               return false;
                  }

               }
               while( ds_rcv.available() != 0 );   // any messages left ?


           // 3 - We wait for other clients... and set the OUTPUT mode
              if( incrIOCounter(WAIT)== true )
                  setIOMode( s_level.DATA_OUTPUT );


           // 4 - We can now send data to our client process
              game_mode = server.getGameStatus();

              // Game killed ?
                 if( game_mode==GM_KILLED )
                 {
                    ds_snd.writeByte(MSG_KILL);
                    ds_snd.flush();
                    msg_playerquits();
                    return false;
                 }

               // Sound Data
                  ds_snd.writeByte(MSG_SOUNDDATA);
                  sounds = s_level.getSounds();

                  for(byte i=0; i<SoundLibrary.NB_SOUND_SAMPLES; i++)
                         ds_snd.writeBoolean( sounds[i] );

               // Server data
                  ds_snd.writeByte(MSG_SERVERDATA);
                  ds_snd.writeByte(server.getGameStatus());
                  ds_snd.writeByte(s_level.getCurrentTopTunnel());

               // player data
                  for(byte i=0; i<s_level.getNetPlayerNumber(); i++ ){
                     if(i==client_ID) continue;
                       sp = s_level.getNetPlayer( i );
                       sp.sendData( MSG_CLIENTDATA, ds_snd );
                  }

               // broken stones
                  byte k=0;
                  bs = s_level.getBrokenStone(0);

                    while( bs!=null )
                    {
                      bs.sendData( ds_snd );
                      bs = s_level.getBrokenStone(++k);
                    }

               // filled holes
                  k=0;
                  fh = s_level.getFilledHole(0);

                    while( fh!=null )
                    {
                      fh.sendData( ds_snd );
                      fh = s_level.getFilledHole(++k);
                    }

               // monster data
                  for(byte i=0; i<MONSTER_NUMBER; i++ )
                    for(byte j=LEFT; j<=RIGHT; j++)
                    {
                       sp = (BasicSprite) s_level.getMonster(i,j);

                       if(sp==null) continue;

                       sp.sendData( MSG_MONSTERDATA, ds_snd );
                    }

               // Clouds data
                  ds_snd.writeByte(MSG_CLOUDSDATA);

                  for(byte i=0; i<CLOUD_NUMBER; i++)
                      if(s_level.getStageCloud(i)!=null)
                         ds_snd.writeShort( s_level.getStageCloud(i).getX() );

               // and farewell !
                  ds_snd.writeByte(MSG_END);
                  ds_snd.flush();              

           // 5 - when every client has finished its turn, we wake up the serverlevelprocess
              if( incrIOCounter(NOWAIT) == true )
                  setIOMode( s_level.DATA_TRANSFERT_DONE );
         }
         while( game_mode!=GM_OVER && game_mode!=GM_READY );



        // We wait for everybody to quit the main loop
           waitForIOMode(s_level.DATA_TRANSFERT_DONE);

        // scores
           int players_score[] = s_level.getPlayersScore();
           ds_snd.writeByte(MSG_ALLSCORES);

           for(int i=0; i<s_level.getNetPlayerNumber(); i++ )
               ds_snd.writeInt( players_score[i] );

           ds_snd.flush();

        return true;   // Ok, game over... let's see the scores !
      }


  /****************************************************************************/
  /******************************** DATA ACCESS *******************************/
  /****************************************************************************/

      public String getPlayerName(){
           return pl_name;
      }

      public String getSpriteName(){
           return spr_name;
      }


  /****************************************************************************/
  /*************************** BASIC NET ACCESS *******************************/
  /****************************************************************************/

     private byte waitForByte() throws IOException {
         return ds_rcv.readByte();
     }

  /****************************************************************************/

     private String receiveString() throws IOException
     {
       int nb = ds_rcv.readByte();
       char s[] = new char[MAXNAMELETTERS];

           for(short i=0; i<nb; i++)
              s[i] = ds_rcv.readChar();

       return new String(s,0,nb);
     }

  /****************************************************************************/

     private void sendString(String s) throws IOException
     {
       byte nb = (byte) s.length();
       ds_snd.writeByte(nb);

           for(short i=0; i<nb; i++)
              ds_snd.writeChar(s.charAt(i));
     }

  /****************************************************************************/

     public String toString() {
     	if(pl_name==null)
     	  return new String("-player registering-");
     	
        return new String(""+pl_name);
     }
  
  /****************************************************************************/
}