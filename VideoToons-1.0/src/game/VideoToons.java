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

// VideoToons Swing windows
   import videotoons.swing.*;

// JFC
   import java.awt.*;
   import java.awt.event.*;


public class VideoToons implements GameDefinitions
{

 /*********************************** CONSTS **************************************/

  // To identify the current opened window
     static public final byte GAME_MODE_WINDOW     = 1;
     static public final byte PLAYER_SETUP_WINDOW  = 2;
     static public final byte MULTIPLAYER_WINDOW   = 3;
     static public final byte LEVEL_SETUP_WINDOW   = 4;
     static public final byte CLIENT_WAIT_WINDOW   = 5;
     static public final byte SERVER_WAIT_WINDOW   = 6;
     static public final byte GAME_WINDOW          = 7;     
     static public final byte SCORE_WINDOW         = 8;
     static public final byte ABOUT_WINDOW         = 9;
     static public final byte HELP_WINDOW          = 10; 

     static private byte window_num;
     static private byte WindowLock[] = new byte[1];

  // Frame & Windows
     static private Frame frame;
     static private Window current_w;
     static private Window old_w;

  // Author:
     static public String ath = "B-e-r-t-r-a-n-d--L-e-N-i-s-t-o-u-r";


 /******************************* GAME STATUS *********************************/

 // Game status : GM_READY, GM_ACTIVE, ... , GM_OVER
    static private byte gm_status;


 /****************************** CLIENT CLASSES ********************************/

  // User Data
     static private UserData u_dat;

  // Current GameScreen
     static private GameScreen g_screen;

  // Current ClientLevelProcess
     static private ClientLevelProcess c_level;

  // Current GameClient (server link)
     static private GameClient g_client;


 /****************************** SERVER CLASSES ********************************/

  // Current Server
     static private GameServer g_server;

 /****************************** AUDIO CLASSES ********************************/
  
  // Audio Library
     static private SoundLibrary sound_lib;
  
/*********************************************************************************/
/********************************** CONSTRUCTOR **********************************/
/*********************************************************************************/

    public static void main(String[] args)
    {
      // Basic Frame - support for windows
         frame = new Frame();
         frame.setResizable(false);

      // we load library data
         ObjectLibrary.loadLibraryData();

      // we initiaalize audio settings
         sound_lib = new SoundLibrary();

      // User data structure to gather user choices among the different windows
         u_dat = new UserData();
         gm_status = GM_NONE;

      // We now begin with an IntroWindow
         setWindowNumber( GAME_MODE_WINDOW );

         do
         {
              old_w = current_w;

           // we display the selected window and wait for or a change
              windowLoop();
         }
         while(true);
    }

 /***********************************************************************************/

    public static void disposeOldWindow()
    {
          if(old_w!=null)
          {

               if(current_w!=null) {
                 while( !current_w.isVisible() )
                      Tools.waitTime(200);

                 current_w.toFront();
               }

               old_w.dispose();

               if(current_w!=null)
               current_w.toFront();
          }
    }
    
    
 /*********************************************************************************/
 /******************************* WINDOW LOOP *************************************/
 /*********************************************************************************/

   static private void windowLoop()
   {
              switch( getWindowNumber() )
              {
                case GAME_MODE_WINDOW :
                                    current_w = new GameModeWindow(frame);
                                    sound_lib.setMusic( "music/game_mode.mid", SoundLibrary.MAX_VOLUME );
                                    break;
                case ABOUT_WINDOW :
                                    current_w = new AboutWindow(frame);
                                    sound_lib.setMusic( "music/about.mid", SoundLibrary.MAX_VOLUME );
                                    break;
                case HELP_WINDOW :
                                    current_w = new HelpWindow(frame);
                                    sound_lib.setMusic( "music/help.mid", SoundLibrary.MAX_VOLUME );
                                    break;
                case PLAYER_SETUP_WINDOW :
                                    current_w = new PlayerSetupWindow(frame, u_dat);
                                    break;
                case LEVEL_SETUP_WINDOW :
                                    current_w = new LevelSetupWindow(frame, u_dat);

                                   // One player game ?
                                      setGameStatus( GM_READY );

                                      if( u_dat.game_mode == ONEPLAYER )
                                      {

                                           disposeOldWindow();

                                           if(!g_client.waitGetReadySignal() ) {
   	                                       g_client.cancelGameClient();
   	                                       g_server = null;
   	                                       g_client = null;
                                               setWindowNumber( PLAYER_SETUP_WINDOW );
   	                                    }
   	                                    else
   	                                       setWindowNumber( GAME_WINDOW );
   	                                       
   	                                  return;
                                      }
                                    
                                    break;
                case MULTIPLAYER_WINDOW :
                                    current_w = new MultiplayerWindow(frame, u_dat);
                                    break;
                case CLIENT_WAIT_WINDOW :
                                    current_w = new ClientWaitWindow(frame);

                                 // We block here... waiting for the signal...
                                    setGameStatus( GM_READY );
                                    disposeOldWindow();

                                    if( g_client.waitGetReadySignal() )
                                        setWindowNumber( GAME_WINDOW );
                                    else {
              	                        g_client.cancelGameClient();
   	                                g_client = null;
                                        setWindowNumber( MULTIPLAYER_WINDOW );
                                    }

                                    return;
                case SERVER_WAIT_WINDOW :
                                    current_w = new ServerWaitWindow(frame, u_dat);

                                    setGameStatus( GM_READY );
                                    disposeOldWindow();

                                    if(!g_client.waitGetReadySignal() ) {
              	                        g_client.cancelGameClient();
   	                                g_server = null;
   	                                g_client = null;
                                        setWindowNumber( MULTIPLAYER_WINDOW );
                                    }
              	                    else
              	                        setWindowNumber( GAME_WINDOW );

                                    return;
                case GAME_WINDOW :
                                    current_w = null;

                                    if( startGame() == false ) {
                                    	
                                       if(u_dat.game_mode==ONEPLAYER)
                                          setWindowNumber( GAME_MODE_WINDOW );
                                       else{
                                          setWindowNumber( MULTIPLAYER_WINDOW );
                                          sound_lib.setMusic( "music/setup.mid", SoundLibrary.MAX_VOLUME );
                                       }

   	                               g_server = null;
   	                               g_client = null;
                                    }
                                    else
                                       setWindowNumber( SCORE_WINDOW );

                                    return;

                case SCORE_WINDOW :
                                    current_w = new ScoreWindow(frame, u_dat);
                                    sound_lib.setMusic( "music/scores.mid", SoundLibrary.MAX_VOLUME );
                                    break;
              }


           disposeOldWindow();

        // we wait for a change ...
           windowHasChanged();
   }

 /*********************************************************************************/
 /*************************** WINDOW CHANGES NOTIFY *******************************/
 /*********************************************************************************/

    static public byte getWindowNumber(){
       return window_num;	
    }

    static public void setWindowNumber( byte val ){
       window_num = val;
       
       synchronized(WindowLock) {
          WindowLock.notifyAll();
       }
    }

    static public void windowHasChanged(){
      synchronized(WindowLock) {
        try{
             WindowLock.wait();
        }catch(InterruptedException e) {} 
      }
    }

 /********************************* DATA ACCESS *********************************/

   static public GameScreen getGameScreen(){
   	return g_screen;
   }

   static public SoundLibrary getSoundLibrary(){
        return sound_lib;
   }

   static public ClientLevelProcess getClientLevelProcess(){
   	return c_level;
   } 

   static public UserData getUserData(){
   	return u_dat;
   }

   static public GameClient getGameClient(){
   	return g_client;
   }

   static public void setGameClient( GameClient gc ){
   	g_client = gc;
   }

   static public void resetScore() {
        u_dat.host_player_score = 0;
   }

 /******************************************************************************/

    synchronized static public byte getGameStatus(){
      return gm_status;
    }

 /**************************************************************************/

    synchronized static public void setGameStatus( byte val )
    {
      if( (gm_status==GM_KILLED || gm_status==GM_OVER) && (val!=GM_READY) )
         return;

      if(DEBUG_MODE)
         System.out.println("VideoToons: Client game status changed to "+val);

      gm_status = val;
    }

 /**************************************************************************/
 /****************************** SERVER ************************************/ 
 /**************************************************************************/
 
    public static boolean isServerAlive()
    {
       if(g_server == null)
          return false;
          
       try{
          return !g_server.serverShouldEnd();
       }
       catch(Exception e) {}  // server could be dead

       return false;
    }

 /******************************************************************************/

    public static void createNewServer() {
        g_server = new GameServer(u_dat.level_name,u_dat.game_mode);
    }

 /******************************************************************************/

    public static void deleteServerHandle() {
        g_server = null;
    }

 /******************************************************************************/

    public static void deleteGameServer() {
        g_server = null;
        g_client = null;
        u_dat.is_server_host = false;
    }


 /******************************************************************************/

   static public GameServer getGameServer(){
   	return g_server;
   }


 /******************************************************************************/
 /************************************ WINDOWS *********************************/ 
 /******************************************************************************/ 

     static private boolean startGame()
     {
        long t;

       // 1 - creates the game screen and loads the selected level's data.
          g_screen = new GameScreen( frame );
          c_level = new ClientLevelProcess(u_dat);
          sound_lib.stopMusic();

       // 2 - Now that the level exists we can ask for the GameScreen's first display
          StatusWindow sw = new StatusWindow( frame );

          g_screen.show();
          disposeOldWindow();
          g_screen.requestFocus();

           while( StatusWindow.doWeQuit() == false )
                Tools.waitTime(200);

           Tools.waitTime(1000);

          g_screen.setRefreshMode( GameScreen.WHOLE_SCREEN );
          g_screen.toFront();
          g_screen.repaint();

       // 3 - Start Level Music
          sound_lib.setMusic(c_level.getLevelMusicName(), SoundLibrary.MUSIC_VOLUME );

       // 4 - Wait for the server to say 'play !'
          if( g_client.waitGameStartSignal() == false ) {
             g_screen.dispose();
             current_w = g_screen;
             g_screen = null;
             c_level = null;
             return false;
         }

       // 5 - Game loop 
          do
          {
             t = System.currentTimeMillis();

             // 4.1 - POS UPDATE
                if( getGameStatus() != GM_PAUSE )
                  c_level.updateLevelProcess();

             // 4.2 - SEND DATA
                if( g_client.sendClientData() == false )
                  break;

             // 4.3 - SCREEN REPAINT
                g_screen.repaint();

             // 4.4 - RECEIVE DATA
                if( g_client.receiveClientData()== false )
                   break;

             // 4.5 - WAIT LOOP (if we were too fast)
                while( ( System.currentTimeMillis() - t ) < 14 );

          }
          while( getGameStatus() != GM_OVER && getGameStatus() != GM_KILLED );


       // 5 - Victory or Defeat image
          if( getGameStatus() == GM_OVER )
          {
           // ah ah... the scores...
              g_client.receiveAllScores();

           // victory or defeat message...
             Player p = c_level.getDefaultPlayer();

             if( p.getStatus()==BS_WINNER  ||  c_level.lastManWinner() ) {
                g_screen.setRefreshMode( GameScreen.VICTORY );
                sound_lib.setMusic( "music/winner.mid", SoundLibrary.MAX_VOLUME );
             }
             else if(p.getStatus()!=BS_GAMEOVER) {
                g_screen.setRefreshMode( GameScreen.DEFEAT );
                sound_lib.stopMusic();
                sound_lib.playSound( SoundLibrary.DEFEAT_SOUND );
             }
             else if(u_dat.game_mode == ONEPLAYER)
                Tools.waitTime(4000);
             else
                Tools.waitTime(2500);

             g_screen.repaint();
          }

       // 6 - End of game screen & network connection & level
          current_w = g_screen;
          g_screen = null;
          c_level = null;

          if(getGameStatus() == GM_KILLED) {
             if(u_dat.is_server_host) {
                g_server = null;
                u_dat.is_server_host = false;
             }

             g_client = null;
             return false;
          }
          else
             return true;
  }

/*********************************************************************************/
 
}


