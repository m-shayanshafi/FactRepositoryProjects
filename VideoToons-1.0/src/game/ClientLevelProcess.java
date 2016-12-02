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


class ClientLevelProcess implements GameDefinitions
{

 /*************************** CLIENT CUSTOM DATA ****************************/

 // Player Data
    private Player p;
    private int score;

    private BasicSprite net_player[];
    
    private byte host_player_ID;

 // Monster Data
    private BasicSprite monster[][];

 // Stone states stage by stage... see GamesDefinitions for the != stone types
    private byte stone_state[][];
    
 // initial Stone states stage by stage (only first lines of stones for each stage)
    private byte initial_stone_state[][];
    
 // Tunnels
    private Tunnel tunnels[][];

 // Stage clouds
    private Cloud clouds[];

 // Blocks
    private Block blocks[];

 // Current tunnel number on top of the screen
    private byte current_top_tunnel;

 // broken stone list.
    private Vector b_stone;

 // holes to fill list
    private Vector f_hole;

 /*************************** LEVEL CONSTRUCTION ****************************/

 // Temporary variables to load/construct the level
    private LevelData ldat;

 // music name for this level
    private String music_name;

 /**************************************************************************/
 /******************************* CONSTRUCTOR ******************************/
 /**************************************************************************/

    public ClientLevelProcess( UserData u_dat )
    {
      // *** Level data
         ldat = ObjectLibrary.getLevelByName(u_dat.level_name);
         music_name = ldat.music_name;

      // *** Stone data
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

      // *** Tunnels
         tunnels = new Tunnel[TUNNEL_NUMBER][2];

         for(byte i=0; i<TUNNEL_NUMBER; i++)
           for( byte k=LEFT; k<=RIGHT; k++)
           {
             if( ldat.tunnel_image_index[i][k]<0 )
                  continue;

             int x0=ldat.tunnel_offset[i][k];

             if(k==RIGHT)
                x0+=520;

            tunnels[i][k] = new Tunnel( x0, STAGE_HEIGHT[i]-100, ldat.tunnel_collision[i][k] );
           }

      // *** Clouds
         clouds = new Cloud[CLOUD_NUMBER];
      
         for(byte i=0; i<CLOUD_NUMBER; i++)
         {
            if( ldat.cloud_image_index[i]<0 )
               continue;
            
            clouds[i] = new Cloud(i,ldat.cloud_speed[i]);
         }

      // *** Blocks
         blocks = new Block[BLOCK_NUMBER];

         for(byte i=0; i<BLOCK_NUMBER; i++)
         {
            if( ldat.block_image_index[i]<0 )
               continue;

            blocks[i] = new Block(i,ldat.block_stone[i]);
         }

      // *** We begin with top tunnel = 4
         current_top_tunnel = 4;

      // *** vectors for broken stones & holes
         b_stone = new Vector( 4, 1 );
         f_hole = new Vector( 4, 1 );

      // *** We create our player
         score=0;
         host_player_ID = u_dat.host_player_ID;
      
         p =  new Player( ObjectLibrary.getPlayerSpriteByName(u_dat.host_sprite_name),
                          this, (byte)(7+3*host_player_ID) );


      // and the other players...
         net_player = new BasicSprite[u_dat.nb_players];

         for(byte i=0; i<u_dat.nb_players; i++ )
         {
            if(i==host_player_ID)
                net_player[i] = (BasicSprite) p;
            else
            {
                AnimLibrary alib = ObjectLibrary.getPlayerSpriteByName(u_dat.sprite_name[i]);

               	net_player[i] = new BasicSprite( (7+3*i)*STONE_WIDTH,
               	                                 STAGE_HEIGHT[0]-alib.std_height,
               	                                 alib.std_width,
               	                                 alib.std_height );
            }

            net_player[i].setSpriteID( i );
         }

      // Monsters
         monster = new BasicSprite[MONSTER_NUMBER][2];

         for(byte i=0; i<MONSTER_NUMBER; i++)
           for( byte k=LEFT; k<=RIGHT; k++)
              if( ldat.monsters_id[i][k] != MT_NONE )
              {
                AnimLibrary alib = ObjectLibrary.getMonsterSpriteByNumber(ldat.monsters_id[i][k]);

                monster[i][k] = new BasicSprite( k*31*STONE_WIDTH,
                                                 STAGE_HEIGHT[i]-alib.std_height,
               	                                 alib.std_width,
               	                                 alib.std_height );
              }
    }


 /**********************************************************************/
 /************************ LOCAL UPDATE PROCESS ************************/
 /**********************************************************************/

  public void updateLevelProcess()
  {
    byte p_status = p.getStatus();

   //*** I - POSITION UPDATE ***

      // 1.1 - This Host Player
         if( p_status!=BS_GAMEOVER )
              p.updateSpritePosition();


   //*** II - COLLISION PROCESS
       p_status = p.getStatus();

       if( p_status==BS_DEAD || p_status==BS_GAMEOVER || p_status==BS_INVINCIBLE )
            return;

    // 2.1 - Host Player / Tunnels
       if( 0<=p.getStage() && p.getStage()<TUNNEL_NUMBER 
                          && tunnels[p.getStage()][p.getSide()]!=null)
             tunnels[p.getStage()][p.getSide()].collisionBehaviour( p );

    // 2.2 - Host Player / Monsters (Collision means death or hurt)
       for(byte i=0; i<MONSTER_NUMBER; i++ )
          for(byte k=LEFT; k<=RIGHT; k++)
            if(monster[i][k]!=null)
               p.collisionBehaviourWithMonster( monster[i][k] );


    // 2.3 - Host Player / Blocks at current stage & stage-1
       if( 0<=p.getStage() && p.getStage()<BLOCK_NUMBER )
       {
           if( blocks[p.getStage()]!=null )
             blocks[p.getStage()].collisionBehaviour( p );

           if( 0<p.getStage() && blocks[p.getStage()-1]!=null )
             blocks[p.getStage()-1].collisionBehaviour( p );
       }

    // 2.4 - Host Player / Other Players
       for(byte i=0; i<net_player.length; i++ )
       {
            if(i==host_player_ID)
               continue;
               
            p.collisionBehaviour( net_player[i] );
       }

  }



 /**************************************************************************/
 /**************************** NET PLAYERS DATA ****************************/
 /**************************************************************************/

    public Player getDefaultPlayer(){
      return p;
    }

    public BasicSprite getNetPlayer( byte i ){
      return net_player[i];
    }

    public int getNetPlayerNumber(){
      return net_player.length;
    }

    public BasicSprite getMonster( byte i, byte side ){
      if(monster==null)
        return null;

      return monster[i][side];
    }

 /**************************************************************************/
 /************************** DEFAULT PLAYER INFOS **************************/
 /**************************************************************************/

    public int getDefaultPlayerScore() {
      return score;
    }
  
    public void addDefaultPlayerScore( int val ) {
      score += val;

      if(score<0) score=0;
    }

    public boolean lastManWinner()
    {
      int one_alive=-1;

        for(int i=0; i<net_player.length; i++ )
           if( net_player[i].getStatus()==BS_WINNER) {
             if(i==host_player_ID)
                return true;
             else
                return false;
           }
           else if(net_player[i].getStatus()!=BS_GAMEOVER)
             one_alive=i;

        if(one_alive==-1)
           return false;

       // Last Man standing score correction
          VideoToons.getUserData().player_score[one_alive] += WINNER_POINTS;

        if(one_alive==host_player_ID) {
               VideoToons.getUserData().host_player_score += WINNER_POINTS;
           return true;
        }
        else
           return false;
     }

 /**************************************************************************/
 /**************************** LEVEL DATA ACCESS ***************************/
 /**************************************************************************/

    public String getLevelMusicName() {
       return music_name;
    }

    public byte getStoneState( byte stage, byte ston, byte lin){
      return stone_state[STAGE_NUMBER-1-stage][ston+STONE_NUMBER*lin];
    }

    // is always line 0 of stones
    public byte getInitialStoneState( byte stage, byte ston){
      return initial_stone_state[stage][ston];
    }

    public Tunnel getTunnel( byte stage, byte side){
      return tunnels[stage][side];
    }

    public Cloud getStageCloud( byte stage ){
        return clouds[stage];
    }

    public Block getStageBlock( byte stage ){
        return blocks[stage];
    }

    public byte getCurrentTopTunnel(){
      return current_top_tunnel;
    }
 
    public void setCurrentTopTunnel( byte val ){
      current_top_tunnel = val;
    }


 /**************************************************************************/
 /**************************** CONSTRUCTION DATA ***************************/
 /**************************************************************************/

    public LevelData getLevelConstructionData(){
      return ldat;
    }

    public void removeLevelConstructionData(){
      ldat=null;
    }

 /****************************************************************************/
 /************************** ABOUT BROKEN STONE ******************************/  
 /****************************************************************************/

    public BrokenStone getBrokenStone( byte index ){
      if( index >= b_stone.size() )
        return null;

      return (BrokenStone) b_stone.get( index );
    }


    public BrokenStone eraseBrokenStone( byte index ){
      b_stone.remove( index );

      return getBrokenStone(index);
    }


    public void setBrokenStone( byte stage, byte ston, byte lin, boolean rightside )
    {
      b_stone.add( (Object) new BrokenStone( stage, ston, lin, rightside ) );  
      stone_state[STAGE_NUMBER-1-stage][ston+STONE_NUMBER*lin] = STONE_BROKEN;
      VideoToons.getGameClient().sendSound( SoundLibrary.BROKENSTONE_SOUND );
    }

    public void setBrokenStone( DataInputStream ds_rcv ) throws IOException
    {
      byte stage, stone, line;
      boolean rightside;

      // We read network data
    	 stage = ds_rcv.readByte();
    	 stone = ds_rcv.readByte();
    	 line = ds_rcv.readByte();
         rightside = ds_rcv.readBoolean();

      // is the stone already broken ?
         if( stone_state[STAGE_NUMBER-1-stage][stone+STONE_NUMBER*line] == STONE_BROKEN )
             return;

         b_stone.add( (Object) new BrokenStone( stage, stone, line, rightside ) );
         stone_state[STAGE_NUMBER-1-stage][stone+STONE_NUMBER*line] = STONE_BROKEN;
    }



 /****************************************************************************/
 /**************************  ABOUT HOLES ************************************/  
 /****************************************************************************/

    public FilledHole getFilledHole( byte index ){
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

    public void setFilledHole( DataInputStream ds_rcv ) throws IOException
    {
      byte stage, stone;

      // We read network data
    	 stage = ds_rcv.readByte();
    	 stone = ds_rcv.readByte();

         byte initial_state = getInitialStoneState(stage,stone);
    	
      // is the stone already filled ? or never existed ?
         if( initial_state == STONE_BROKEN ||
             stone_state[STAGE_NUMBER-1-stage][stone] != STONE_BROKEN )
             return;

         f_hole.add( (Object) new FilledHole( stage, stone ) );
         stone_state[STAGE_NUMBER-1-stage][stone] = initial_state;
    }


 /**************************************************************************/

 // returns a non broken stone on the screen [stage][stone][level=0]
   public byte[] getANonBrokenStone()
   {
     byte tab[] = { (byte)0, (byte)0, (byte)0 };
     byte top_tun1, top_tun2, top = current_top_tunnel;

        if(top==12){
          top_tun1 = 8;
          top_tun2 = 10;
        }
        else{
          top_tun1 = (byte)(top-4);
          top_tun2 = top;
        }
     
   
     // we search for a non broken stone
      for( tab[0]=top_tun1; tab[0]<top_tun2; tab[0]++ )
      {
         byte start_st=0, end_st=31;

         if(tab[0]<TUNNEL_NUMBER)
         {
           Tunnel tn = getTunnel(tab[0], LEFT);
          
           if(tn!=null) {
              byte next_st = (byte) ( (tn.getX()+tn.getWidth()+5)/STONE_WIDTH );

              if(next_st>0)
                start_st = next_st;
           }

           tn = getTunnel(tab[0], RIGHT);
         
           if(tn!=null) {
              byte prev_st = (byte) ( (tn.getX()-5)/STONE_WIDTH );

              if(prev_st<31)
                  end_st = prev_st;
           }
         }

         for( tab[1]=start_st; tab[1]<end_st; tab[1]++ )
         {
            byte stone_state = getStoneState(tab[0],tab[1],(byte)0);

           if( stone_state!=STONE_BROKEN && stone_state!=STONE_ACID)
                 return tab;
         }
      }

     // none ? ok let's try again but with the tunnel sides this time...
        for( tab[0]=top_tun1; tab[0]<top_tun2; tab[0]++ )
         for( tab[1]=0; tab[1]<32; tab[1]++ )
           if( getStoneState(tab[0],tab[1],(byte)0)!=STONE_BROKEN )
                 return tab;
                 
     // gloups nothing ? some default pos then...
        tab[1] = 10;
        
        return tab;
   }

 /**************************************************************************/
}