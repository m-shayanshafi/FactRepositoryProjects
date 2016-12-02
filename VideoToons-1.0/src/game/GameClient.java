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


import java.net.*;
import java.io.*;


public class GameClient implements GameDefinitions
{

     // our socket client <--> Server
        private Socket socket;

     // Communication streams
        private DataInputStream  ds_rcv;
        private DataOutputStream ds_snd;

     // Our associated ClientLevelProcess
        ClientLevelProcess c_level;

     // User Data
        UserData u_dat;

     // IOLock to protect multiple socket access
        private Object IOLock;

     // are we alive ? (socket opened)
        private boolean game_client_alive;

  /****************************************************************************/
  /****************************** CONSTRUCTOR *********************************/
  /****************************************************************************/

       private GameClient( UserData u_dat, Socket socket, DataInputStream ds_rcv,
                           DataOutputStream ds_snd  )
       {
          this.u_dat = u_dat;
          this.socket = socket;
          this.ds_rcv = ds_rcv;
          this.ds_snd = ds_snd;

          IOLock = new Object();
          game_client_alive = true;
       }

  /****************************************************************************/

    // We try to build a connection with the server. On success we return a
    // new GameClient. Otherwise we return null and an error code is set.
    //
    // Needs : a user_data structure with the field server_name,
    //         host_player_name, host_sprite_name filled.
    // Waits for: a succesful connection with the server
    // Cancel : none

       static public GameClient connectToServer( UserData u_dat )
       {
       	  DataInputStream  ds_rcv;
          DataOutputStream ds_snd;
          Socket socket;
          GameClient gclient;

       	  u_dat.resetErrorCode();

          try
          {
            // Server. Connection.
               try{
                      socket = new Socket(u_dat.server_name,GAME_PORT);
                  }
                  catch(UnknownHostException e){
                      u_dat.setErrorCode(ERR_UNKNOWNHOST);
                      return null;
                  }

            // We inits the data buffered streams
               ds_rcv = new DataInputStream( new BufferedInputStream(socket.getInputStream()));
	       ds_snd = new DataOutputStream( new BufferedOutputStream(socket.getOutputStream()));


            // We wait for a handshake from the server...
                 switch( ds_rcv.readByte() )    // header
                 {
                    case MSG_WELCOME:
                                     break;
                    case MSG_ERROR:
                                     u_dat.setErrorCode( ds_rcv.readByte() );
                    default:
                                     return null;
                 }

            // Well... everything seems ok...
               gclient = new GameClient( u_dat, socket, ds_rcv, ds_snd );

               ds_snd.writeByte(MSG_THANKYOU);
               gclient.sendString(u_dat.host_player_name);
               gclient.sendString(u_dat.host_sprite_name);
               ds_snd.flush();

          }
          catch(IOException e){
            if(DEBUG_MODE)
              System.out.println( "GameClient: "+e.getMessage() );
              
            u_dat.setErrorCode(ERR_SOCKFAILED);
            gclient = null;
 	  }
 	  
 	  if(DEBUG_MODE && gclient!=null)
 	    System.out.println("GameClient: server contacted successfuly");
 	  
 	  return gclient;
       }


  /****************************************************************************/
  /***************************** MESSAGES ACTIONS *****************************/
  /****************************************************************************/

  // We stop this GameClient with 'advertise' (= { DONT_TELL_SERVER, TELL_SERVER }

     private void stopGameClient(boolean advertise)
     {
     	if(DEBUG_MODE)
     	  System.out.println("GameClient: we quit...");
     	
     	try
     	{
          synchronized( IOLock )
          {
             if( game_client_alive == false )
                return;

             if(advertise==TELL_SERVER )
             {
               if(u_dat.is_server_host == false){
                  ds_snd.writeByte(MSG_PLAYERQUITS);
                  ds_snd.flush();
               }
               else {
                  ds_snd.writeByte(MSG_KILLALL);
                  ds_snd.writeByte(MSG_END);
                  ds_snd.flush();
               }
             }

             ds_snd.close();
             ds_rcv.close();
             socket.close();
          }
        }
        catch (IOException e){}

        VideoToons.setGameStatus(GM_KILLED);
        game_client_alive = false;
    }


  /****************************************************************************/

     public void cancelGameClient() {
        stopGameClient( TELL_SERVER );
    }

  /****************************************************************************/

  // Needs :     an opened socket connection AND a getGameStatus()==GM_READY
  // Waits for:  a "Get Ready" ! (returns true) or a "Kill" (returns false)
  // Cancel   :  possible with setGameStatus(GM_KILLED) (returns false)

   public boolean waitGetReadySignal()
   {
     try
     {
    	if(DEBUG_MODE)
    	  System.out.println("GameClient: Waiting for game start...");
    	
      // we wait for an event from the user or the server
      	 while( VideoToons.getGameStatus()==GM_READY )
          synchronized( IOLock )
          {
             Tools.waitTime( 300 );  // let's spare our CPU a little...
          
             if ( ds_rcv.available() != 0 )
               switch( ds_rcv.readByte() )    // header
               {
               	  case MSG_GETREADY :
                             u_dat.host_player_ID = ds_rcv.readByte();
                             u_dat.level_name = receiveString();
                             u_dat.nb_players =  ds_rcv.readByte();

                             for(byte i=0; i<u_dat.nb_players; i++ )
                               if(i==u_dat.host_player_ID){
                                 u_dat.player_name[i] = u_dat.host_player_name;
                                 u_dat.sprite_name[i] = u_dat.host_sprite_name; 
                               }
                               else{
                                 u_dat.player_name[i] = receiveString();
                                 u_dat.sprite_name[i] = receiveString();                                
                               }

                             VideoToons.setGameStatus(GM_ACTIVE);
                             return true;

                  case MSG_ERROR:
                             u_dat.setErrorCode(ds_rcv.readByte());
                             
                             if(DEBUG_MODE)
                               System.out.println("GameClient: "+u_dat.getErrorString());
                  case MSG_KILL:
                  default:
                             stopGameClient( DONT_TELL_SERVER );
                             return false;
               }
          }

     }
     catch(IOException e){
     // IO error
        stopGameClient( TELL_SERVER );
        e.printStackTrace();
     }
  
     // if we are here it means the user has set the game status to GM_KILLED
     // (or an IOexception occured)
     return false;
   }


  /****************************************************************************/

   // Needs:  none
   // Waits For: a "Go play!" message (returns true) or a "Kill" (returns false)
   // Cancel: none

      public boolean waitGameStartSignal()
      {
      	try
      	{
           synchronized(IOLock)      	
           {
             // We tell the server we are ready !
                ds_snd.writeByte(MSG_ACK);
                ds_snd.flush();

             // We wait for the GOPLAY message
                  switch( waitForByte() )    // header
                  {
                     case MSG_GOPLAY:
                                       return true;
                     case MSG_ERROR:  
                                       u_dat.setErrorCode(ds_rcv.readByte());
                     case MSG_KILL:
                     default:
                                       stopGameClient( DONT_TELL_SERVER );
                 }
            }
         }
         catch(IOException e){
             e.printStackTrace();
             stopGameClient( TELL_SERVER );
         }

         return false;
      }


  /****************************************************************************/

  // We send client data
  // other types of messages are added to the stream by the Player Class.
  // Needs a ClientLevelProcess...

      public boolean sendClientData()
      {
         try
         {
            synchronized(IOLock)
            {
             // player data
                BasicSprite sp = VideoToons.getClientLevelProcess().getNetPlayer( u_dat.host_player_ID  );
                sp.sendData( MSG_CLIENTDATA, ds_snd );

             // Our current score
                ds_snd.writeByte(MSG_SCORE);
                ds_snd.writeInt( u_dat.host_player_score
                                  + VideoToons.getClientLevelProcess().getDefaultPlayerScore() );

             // and farewell !
                ds_snd.writeByte(MSG_END);
                ds_snd.flush();
            }
         }
         catch(IOException e) {
             e.printStackTrace();
             stopGameClient( TELL_SERVER );
             return false;
         }

        return true;
      }

  /****************************************************************************/

  // We add Broken Stone data to the ds_snd stream

      public void sendBrokenStoneData( byte stage, byte ston, byte lin, boolean rightside  )
      {
         try
         {
            synchronized(IOLock)
            {
               ds_snd.writeByte( MSG_BROKENSTONE );
               ds_snd.writeByte( stage );
               ds_snd.writeByte( ston );
               ds_snd.writeByte( lin );
               ds_snd.writeBoolean(rightside);
            }
         }
         catch(IOException e) {
             e.printStackTrace();
             stopGameClient( TELL_SERVER );
         }
      }


  /****************************************************************************/

  // We add the LEVEL_UP event to the ds_snd stream

      public void sendLevelUPData( byte new_level )
      {
         try
         {
            synchronized(IOLock)
            {
               ds_snd.writeByte( MSG_LEVEL_UP );
               ds_snd.writeByte( new_level );
            }
         }
         catch(IOException e) {
             e.printStackTrace();
             stopGameClient( TELL_SERVER );
         }
      }

  /****************************************************************************/

  // We add the PAUSE event to the ds_snd stream

      public void sendPauseMessage()
      {
         try
         {
            synchronized(IOLock)
            {
               ds_snd.writeByte( MSG_PAUSE );
            }
         }
         catch(IOException e) {
             e.printStackTrace();
             stopGameClient( TELL_SERVER );
         }
      }

  /****************************************************************************/

  // We add the PAUSE END event to the ds_snd stream

      public void sendPauseEndMessage()
      {
         try
         {
            synchronized(IOLock)
            {
               ds_snd.writeByte( MSG_PAUSEEND );
            }
         }
         catch(IOException e) {
             e.printStackTrace();
             stopGameClient( TELL_SERVER );
         }
      }

  /****************************************************************************/

  // We send a sound ID...

      public void sendSound( byte sound_ID )
      {
         try
         {
            synchronized(IOLock)
            {
               ds_snd.writeByte( MSG_SOUND );
               ds_snd.writeByte( sound_ID );
            }
         }
         catch(IOException e) {
             e.printStackTrace();
             stopGameClient( TELL_SERVER );
         }
      }

  /****************************************************************************/

      public boolean receiveClientData()
      {
        BasicSprite sp;
        ClientLevelProcess c_level = VideoToons.getClientLevelProcess();
        byte val,header;

        try
        {
          synchronized(IOLock)
          {
              do
              {
                header = waitForByte();

                 if(header==MSG_END)
              	   break;

                  switch( header )
                  {
                     case MSG_SERVERDATA :
                                            byte status = ds_rcv.readByte();
                                            
                                            if( status!=VideoToons.getGameStatus() )
                                                VideoToons.setGameStatus(status);

                                         // stage up ?
                                            if( ds_rcv.readByte() > c_level.getCurrentTopTunnel() ){
                                                  if(DEBUG_MODE)
                                            	      System.out.println("GameClient: Level Up !");

                                                  VideoToons.getGameScreen().setRefreshMode(
                                                                      GameScreen.MOVE_LEVEL_UP );
                                                  VideoToons.getGameScreen().repaint();
                                            }

                                            break;
                     case MSG_CLIENTDATA :
                                            sp = c_level.getNetPlayer( ds_rcv.readByte() );
                                            sp.receiveData( ds_rcv );
                                            break;
                     case MSG_MONSTERDATA :
                                            int index = ds_rcv.readByte();
                                            sp = c_level.getMonster( (byte)(index/2),(byte)(index%2) );
                                            sp.receiveData( ds_rcv );
                                            break;
                     case MSG_BROKENSTONE:
                                            c_level.setBrokenStone( ds_rcv );
                                            break;
                     case MSG_FILLEDHOLE:
                                            c_level.setFilledHole( ds_rcv );
                                            break;
                     case MSG_CLOUDSDATA:

                                            for(byte i=0; i<CLOUD_NUMBER; i++)
                                                if(c_level.getStageCloud(i)!=null)
                                                   c_level.getStageCloud(i).setX( ds_rcv.readShort() );

                                             break;
                     case MSG_SOUNDDATA:
                                           SoundLibrary sl = VideoToons.getSoundLibrary();

                                           for(byte i=0; i<SoundLibrary.NB_SOUND_SAMPLES; i++)
                                               if( ds_rcv.readBoolean() )
                                                   sl.playSound(i);
                                        break;
                     case MSG_KILL:
                     case MSG_ERROR:
                     default:
                                         stopGameClient( DONT_TELL_SERVER );
                                         return false;
                  }
               }
               while( ds_rcv.available() != 0 );   // any messages left ?
            }
         }
         catch(IOException e) {
             if(DEBUG_MODE)
                System.out.println(e.getMessage());

             stopGameClient( TELL_SERVER );
             return false;
         }

        return true;   // Ok, game over... let's go to the next cycle !
      }


  /****************************************************************************/

      public boolean receiveAllScores()
      {
        byte val,header;

        try
        {
           synchronized(IOLock)
           {
                header = waitForByte();

                  switch( header )
                  {
                     case MSG_ALLSCORES :
                                            for( byte i=0; i<u_dat.nb_players; i++ )
                                               u_dat.player_score[i] = ds_rcv.readInt();

                                            break;
                     case MSG_KILL:
                     case MSG_ERROR:
                     default:
                                         stopGameClient( DONT_TELL_SERVER );
                                         return false;
                  }
            }
         }
         catch(IOException e) {
             e.printStackTrace();
             stopGameClient( TELL_SERVER );
             return false;
         }

        u_dat.host_player_score = u_dat.player_score[u_dat.host_player_ID]; 


        return true;   // Ok, let's build the podium !
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

           for(int i=0; i<nb; i++)
              s[i] = ds_rcv.readChar();

       return new String(s,0,nb);
     }

  /****************************************************************************/

     private void sendString(String s) throws IOException
     {
       byte nb = (byte) s.length();
       ds_snd.writeByte(nb);

           for(int i=0; i<nb; i++)
              ds_snd.writeChar(s.charAt(i));
     }

  /****************************************************************************/

}