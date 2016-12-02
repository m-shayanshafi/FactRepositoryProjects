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


import java.util.Vector;
import java.awt.*;
import java.io.*;


class ServerLevelProcess extends IOControlThread
{

 /****************************** OUR SERVER *********************************/

    private GameServer server;

 // Game mode : ONEPLAYER, MULTIPLAYER or LASTMANSTANDING
    private byte game_mode;

 /*************************** SERVER CUSTOM DATA ****************************/

 // Stone states stage by stage... see GamesDefinitions for the != stone types
    private byte stone_state[][];

 // initial Stone states stage by stage (only first lines of stones for each stage)
    private byte initial_stone_state[][];

 // Blocks
    private Block blocks[];

 // Stage clouds
    private Cloud clouds[];

 // Dimension Library (monsters images dimension)
    private DimensionLibrary dimlib;

 // Time before the LevelUpMonster comes (ms)
    private long time_before_next_stage;
    
 // current time (ms)...
    private long current_time;

 // time when the game ends when there is a winner...
    private long end_time;

 /*************************** SERVER SHARED DATA ****************************/

 // Number of players
    private byte player_number;

 // Level name
    private String level_name;

 // Player Data
    private BasicSprite net_player[];

 // Player scores
    private int players_score[];

 // Monster Data
    private Monster monster[][];

 // Current tunnel number on top of the screen
    private byte current_top_tunnel;

 // broken stone list.
    private Vector b_stone;

 // holes to fill list
    private Vector f_hole;

 // Sound received
    private boolean sounds[];


 /**************************************************************************/
 /******************************* CONSTRUCTOR ******************************/
 /**************************************************************************/
 
    public ServerLevelProcess( GameServer server )
    {
         super("ServerLevelProcess",server.getClientsNumber());

      // Inits
         this.server = server;
         level_name = server.getLevelName();
         game_mode = server.getGameMode();
         player_number = server.getClientsNumber();
         end_time = -1;

         start();
    }


 /******************************** LEVEL LOADER ****************************/

    private void loadLevelData()
    {
    	LevelData ldat = ObjectLibrary.getLevelByName(level_name);

      // ***** LEVEL INITS ***** //

        // Top level
           current_top_tunnel = 4;

        // Stone data
           stone_state = ldat.stone_state;

           initial_stone_state = new byte[STAGE_NUMBER][STONE_NUMBER];

           for(byte i=0; i<STAGE_NUMBER; i++ )
           {
             // we save the initial state...
                for(byte j=0; j<STONE_NUMBER; j++)
                    initial_stone_state[i][j] = stone_state[STAGE_NUMBER-1-i][j];

             // do we have to destroy the stone of this stage ?
                if( ldat.pre_destroyed_stones[i] )
                    for(byte j=0; j<STONE_NUMBER; j++)
                    {
                       byte status = stone_state[STAGE_NUMBER-1-i][j];
                
                       if( status==STONE_SIMPLE || status==STONE_LEFT || status==STONE_RIGHT
                           || status==STONE_ACID)
                            stone_state[STAGE_NUMBER-1-i][j]= STONE_BROKEN;
                    }
           }

        // Blocks
           blocks = new Block[BLOCK_NUMBER];

           for(byte i=0; i<BLOCK_NUMBER; i++)
           {
              if( ldat.block_image_index[i]<0 )
                  continue;

               blocks[i] = new Block(i,ldat.block_stone[i]);
           }

        // *** Clouds
           clouds = new Cloud[CLOUD_NUMBER];

           short cloud_dims[][] = DimensionLibrary.getCloudDims( ldat.path_name,
                                               ldat.nb_cloud_images,ldat.cloud_image_index);

           for(byte i=0; i<CLOUD_NUMBER; i++)
           {
             if( ldat.cloud_image_index[i]<0 )
               continue;
            
             clouds[i] = new Cloud(i,ldat.cloud_speed[i]);
             clouds[i].initDimension( cloud_dims[i][0], cloud_dims[i][1] );
           }

        // Broken stones
           b_stone = new Vector( 4, 1 );

        // filled holes
           f_hole = new Vector( 4, 1 );

        // Dimension Library
           dimlib = new DimensionLibrary();

      // ***** PLAYERS INITS ***** //
      
         net_player = new BasicSprite[player_number];
         players_score = new int[player_number];

         for(byte i=0; i<player_number; i++ )
         {
           GameServerThread sthread = server.getClient((byte)i);
           AnimLibrary alib = ObjectLibrary.getPlayerSpriteByName(sthread.getSpriteName());

           net_player[i] = new BasicSprite( (7+3*i)*STONE_WIDTH,
               	                            STAGE_HEIGHT[0]-alib.std_height,
               	                            alib.std_width,
               	                            alib.std_height );

           net_player[i].setSpriteID( i );
         }


      // ***** MONSTERS INITS ***** //

         monster = new Monster[MONSTER_NUMBER][2];
         time_before_next_stage = ldat.time_before_next_stage;

         for(byte i=0; i<MONSTER_NUMBER; i++)
           for( byte k=LEFT; k<=RIGHT; k++)
              if( ldat.monsters_id[i][k] != MT_NONE )
              {
                monster[i][k] = ObjectLibrary.getMonsterByNumber( ldat.monsters_id[i][k],
                                                                  this, (byte) (k*31), i,
                                                                  ldat.monsters_speed[i][k],
                                                                  ldat.monster_parameter[i][k]);
                monster[i][k].setSpriteID( (byte) (2*i+k) );
                monster[i][k].setDimensionLibraryIndex( 
                                  dimlib.loadMonsterDimensions( ldat.monsters_id[i][k] ) );
              }

      // sounds
         sounds = new boolean[SoundLibrary.NB_SOUND_SAMPLES];
         resetSounds();
    }


 /**********************************************************************/
 /*************************** THREAD ACTION ****************************/
 /**********************************************************************/

  public void run()
  {
     loadLevelData();

       do
       {
          resetSounds();

           if( server.getGameStatus() != GM_PAUSE ){
               updateGameStatus();
               updateLevelProcess();
           }

         // We let clients update their position...
            setIOMode( DATA_INPUT );

         // ... and we wait until clients have finished their data transfert.
            waitForIOMode( DATA_TRANSFERT_DONE );
       }
       while( server.getGameStatus()!=GM_OVER && server.getGameStatus()!=GM_KILLED );


     // Do we have to stop the server ?
        try
        {
             if( server.getGameStatus() == GM_KILLED )
                server.stopServer();
        }
        catch(Exception e) {}  // the server could already be dead
  }


 /**********************************************************************/
 /************************ LOCAL UPDATE PROCESS ************************/
 /**********************************************************************/
 
  public void updateLevelProcess()
  {
       //*** 0 - INITS
         eraseAllBrokenStone();
         eraseFilledHoles();

         updateCurrentTime();


       //*** I - POSITION UPDATE ***

          // 1.1 - Monsters
             for(byte i=0; i<MONSTER_NUMBER; i++)
                for( byte k=LEFT; k<=RIGHT; k++)
                   if( monster[i][k] != null )
                       monster[i][k].updateSpritePosition();

          // 1.2 - Clouds
             for(byte i=0; i<CLOUD_NUMBER; i++)
                  if(clouds[i]!=null)
                       clouds[i].updateCloudPosition();

       //*** II - COLLISION PROCESS

          // 2.1 - Monsters / Players
             for(byte j=0; j<net_player.length; j++ )
             {
             	byte state = net_player[j].getStatus();
             	
                if(state==BS_DEAD || state==BS_INVINCIBLE || state==BS_GAMEOVER )
                   continue;

                for(byte i=0; i<MONSTER_NUMBER; i++ )
                  for(byte k=LEFT; k<=RIGHT; k++)
                     if(monster[i][k]!=null)
                        monster[i][k].collisionBehaviour( net_player[j] );
             }

          // 2.2 - Monsters / Blocks
             for(byte i=0; i<MONSTER_NUMBER; i++ )
                for(byte k=LEFT; k<=RIGHT; k++)
                   if( monster[i][k]!=null )
                   {
                   	byte m_stage = monster[i][k].getStage();

                   	if( 0<=m_stage && m_stage<BLOCK_NUMBER && blocks[m_stage]!=null)
                           monster[i][k].collisionWithBlock( blocks[m_stage] );
                   }

       //*** III - Old position update
             for(byte i=0; i<MONSTER_NUMBER; i++ )
                for(byte k=LEFT; k<=RIGHT; k++)
                   if( monster[i][k]!=null )
                      monster[i][k].updateOldRectangle();

  }

 /**************************************************************************/
 /*************************** LEVEL DATA ACCESS ****************************/
 /**************************************************************************/

    public byte getStoneState( int stage, int ston, int lin){
      return stone_state[STAGE_NUMBER-1-stage][ston+STONE_NUMBER*lin];
    }

    // is always line 0 of stones
    public byte getInitialStoneState( int stage, int ston){
      return initial_stone_state[stage][ston];
    }

    public Block getStageBlock( int stage ){
        return blocks[stage];
    }

    public DimensionLibrary getDimensionLibrary() {
        return dimlib;
    }

    public long getTimeBeforeNextStage() {
         return time_before_next_stage;
    }

    private void updateCurrentTime() {
            current_time = System.currentTimeMillis();
    }

    public long getCurrentTime() {
            return current_time;
    }


 /**************************************************************************/
 /*************************** SHARED DATA ACCESS ***************************/
 /**************************************************************************/

    public BasicSprite getNetPlayer( int i ){
      if(net_player==null)
        return null;

      return net_player[i];
    }

    public int getNetPlayerNumber(){
      return net_player.length;
    }

    public Monster getMonster( int i, int side ){
      if(monster==null)
        return null;

      return monster[i][side];
    }

    public Cloud getStageCloud( byte stage ){
        return clouds[stage];
    }

    public int[] getPlayersScore() {
       return players_score;
    }

    public void setPlayerScore( int id, int val ) {
        players_score[id] = val;
    }

 /**************************************************************************/

    public byte getCurrentTopTunnel(){
      return current_top_tunnel;
    }

    synchronized public void setCurrentTopTunnel( byte val ){
      current_top_tunnel = val;
    }

 /**************************************************************************/
 /******************************** SOUNDS **********************************/
 /**************************************************************************/

    public synchronized void resetSounds() {
       for(byte i=0; i<SoundLibrary.NB_SOUND_SAMPLES; i++)
    	   sounds[i] = false;
    }

    public synchronized void addSound( byte sound_ID ) {
       sounds[sound_ID] = true;
    }

    public synchronized boolean[] getSounds() {
    	return sounds;
    }

 /**************************************************************************/
 /****************************** GAME STATUS *******************************/
 /**************************************************************************/

    synchronized public void updateGameStatus()
    {
      byte nb_game_over=0;

        for(byte i=0; i<player_number; i++ )
        {
           if( net_player[i].getStatus()==BS_WINNER ){
               if(end_time!=-1 && end_time-current_time<0) {
                   server.setGameStatus( GM_OVER );
                   return;
               }
               else if(end_time==-1)
                   end_time = current_time+1000;
           }
           else if( net_player[i].getStatus()==BS_GAMEOVER )
                nb_game_over++;
        }

        if(nb_game_over==player_number) {
           server.setGameStatus( GM_OVER );
           return;
        }

        if((nb_game_over==player_number-1) && game_mode==LASTMANSTANDING ){
           server.setGameStatus( GM_OVER );
           return;
        }
    }


 /**************************************************************************/
 /************************* ABOUT BROKEN STONE *****************************/  
 /**************************************************************************/

    public BrokenStone getBrokenStone( int index ){
      if( index >= b_stone.size() )
        return null;

      return (BrokenStone) b_stone.get( index );
    }

 /**************************************************************************/

    public void eraseAllBrokenStone(){
      b_stone.clear();
    }

 /**************************************************************************/

    public void setBrokenStone( DataInputStream ds_rcv ) throws IOException
    {
      byte stage, stone, line;
      boolean rightside;

      // We read network data
    	 stage = ds_rcv.readByte();
    	 stone = ds_rcv.readByte();
    	 line = ds_rcv.readByte();
         rightside = ds_rcv.readBoolean();

       b_stone.add( (Object) new BrokenStone( stage, stone, line, rightside ) );
       stone_state[STAGE_NUMBER-1-stage][stone+STONE_NUMBER*line] = STONE_BROKEN;
    }

 /****************************************************************************/
 /**************************  ABOUT HOLES ************************************/  
 /****************************************************************************/

    public FilledHole getFilledHole( int index ){
      if( index >= f_hole.size() )
        return null;

      return (FilledHole) f_hole.get( index );
    }


    public void eraseFilledHoles(){
      f_hole.removeAllElements();
    }


    public void setFilledHole( byte stage, byte ston )
    {
      byte initial_state = getInitialStoneState(stage,ston);
    	
    	if(initial_state == STONE_BROKEN)
    	     return;

      f_hole.add( (Object) new FilledHole( stage, ston ) );  
      stone_state[STAGE_NUMBER-1-stage][ston] = initial_state;
    }


 /**************************************************************************/
 /**************************************************************************/

 // byte[0]=-1 means no stage found !
 // returns byte[0]=stage byte[1]=side

    public byte[] findHighestStageToRespawn()
    {
     byte result[] = { -1, LEFT };
     boolean found;

     byte top_tun1, top_tun2, top = current_top_tunnel;

        if(top==12 || top==9){
           return result;
        }
        else{
          top_tun1 = (byte) (top-4);
          top_tun2 = (byte) (top-1);
        }
     
   
     // we search for a non broken stone
      for( byte stage=top_tun2; stage>top_tun1; stage-- )
      {
      	 found=true;
      	
         for( byte stone=0; stone<8; stone++ )
         {
            byte stone_state = getStoneState(stage,stone,0);

            if( stone_state==STONE_BROKEN || stone_state==STONE_ACID) {
                 found=false;
                 break;
            }
         }

         if(found) {
            result[0]=stage;
            return result;
         }

         found=true;

         for( byte stone=31; stone>24; stone-- )
         {
            byte stone_state = getStoneState(stage,stone,0);

            if( stone_state==STONE_BROKEN || stone_state==STONE_ACID) {
                 found=false;
                 break;
            }
         }

         if(found) {
            result[0]=stage;
            result[1]=RIGHT;
            return result;
         }
      }
        
     return result;
    
    }

 /**************************************************************************/
}
