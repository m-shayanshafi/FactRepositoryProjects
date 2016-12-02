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

import java.awt.*;
import java.awt.event.*;


class Player extends AnimatedSprite
{
	
 /************************** USEFUL LINKS **********************************/

  // The GameScreen we are linked to ...
     private GameScreen gs;

  // The level process...
     private ClientLevelProcess level;


 /***************************** OTHER PROPERTIES ******************************/

  // Number of lives of this sprite
     private byte lives;

  // Quit menu activated ? quit option selected ?
     private boolean quit_menu;
     private boolean quit_selected;

  // collision speed (like extra_speed but ... for coll...)
     private byte col_speed;

 /**************************** PLAYER CONSTRUCTOR *****************************/

    public Player(AnimLibrary anm, ClientLevelProcess level, byte stone)
    {
      super(anm,stone,(byte)0);
    
      // Our Identity
         spr_TYPE = SP_PLAYER;

      // Useful Links
         this.level = level;
         gs = VideoToons.getGameScreen();
    
      // Default number of lives
         lives = 3;        

      // quit menu
         quit_menu = false;
         quit_selected = false;
    }


  /******************************  LIVES  **********************************/ 
    
    public int getLives(){
        return lives;
    }


 /**************************************************************************/
 /********************* REDEFINITION OF ABSTRACT METHODS *******************/
 /**************************************************************************/
 
 
 /**************************** UPDATE CURRENT IMAGE ************************/

  // Changes the current image to be displayed

    public void updateCurrentImage()
    {
    	current_im = current_anim.nextImageIndex();

    	resize( gs.getPlayerImageWidth(spr_ID,current_im),
                gs.getPlayerImageHeight(spr_ID,current_im) );
    }

 /********************* TO LIMIT THE SPRITE'S X RANGE ***********************/

  // On the right side ...
    protected void limitXRangeRight()
    {
      if( r.x>=640 )
         r.x += - 640 - gs.getPlayerImageWidth(spr_ID,current_im);
    }

  // On the left side ...
    protected void limitXRangeLeft()
    {
      int w = gs.getPlayerImageWidth(spr_ID,current_im);

      if( r.x <= -w )
            r.x += 639+w;
    }

 /******************************* DO WE FALL ********************************/

  // returns true if the sprite falls... ALSO upgrades X pos if the sprite
  // is on moving stones ...

   protected boolean doWeFall()
   {
    // We are on a stage ground... do we fall ?
       byte state;
       byte current_stage = getStage();
       
       byte l1,l2;
       
       l1 = getLeftStoneUnderUs();
       l2 = getRightStoneUnderUs();
       
       if(l1>l2){         // this happens sometimes with small sprites
       	  byte tmp = l1;
       	  l1 = l2;
       	  l2 = tmp;
        }
       
    // is there any non-broken stone under us ?
         for( byte l=l1; l<=l2; l++ )
           if( ( state=level.getStoneState( current_stage, l, (byte)0) ) != STONE_BROKEN )
           {
             // ok, we found a stone... but is it moving ?
                if(state == STONE_LEFT || state == STONE_HLEFT )
           		 extra_speed = -STONE_SPEED*3-2;
                else if(state == STONE_RIGHT || state == STONE_HRIGHT )
           		 extra_speed = STONE_SPEED*3+2;
                else if(state == STONE_ACID)
                         youAreHurt(!rightside);

                return false;
           }

     // Any level cloud ?
        if( CLOUD_NUMBER>current_stage && current_stage>0 && level.getStageCloud(current_stage)!=null )
        {
           Cloud c = level.getStageCloud(current_stage);
           
           if( (r.x+anm.overlap_sprite < c.getX()+c.getWidth()) &&
                     (r.x+getWidth()-anm.overlap_sprite>c.getX()) )
           {
              extra_speed= (byte) (c.getSpeed()*3);
              return false;
           }
        }

     // if we arrive here it means every stone under us is broken
        if(spr_status==BS_HITLEFT || spr_status==BS_HITRIGHT)
           spr_status = BS_ACTIVE;

        falling = true;
        hitting = false;
        translateY( 1 );   // inits the fall...
        return true;
   }


 /***************************** UPDATE THE FALL ******************************/   

   protected void updateFall()
   {
     // do we go right or left while falling ?
        updateMoveWhileFalling();
   	
     // do we stop hitting the stone ?
        if( (hitting_stone==true) && (current_jump_cycle == anm.nb_jump_steps-8 ) )
        {
            hitting_stone = false;

            if( rightside == true )
                  current_anim = anm.fall_right;
            else
                  current_anim = anm.fall_left;
        }

     // does the fall ends ?
            
        byte current_stage = getStage();
        short current_y_limit = (short) (STAGE_HEIGHT[current_stage]-anm.std_height+1);
        short land_offset;

          if(current_jump_cycle==0)
            land_offset = (short) anm.jump_offset[0];
           else
            land_offset = (short) anm.jump_offset[current_jump_cycle-1];

        if( (current_stage <= 10) && ( (r.y <= current_y_limit )
                                  && (r.y > (current_y_limit-land_offset)) ) )
        {
          // ok we should land... but are there any stones under us ?
             if( doWeFall() == false )
             {   	
                falling = false;  // ok we land !
                hitting_stone = false;
            	   
                 if( leftside == true)
                    current_anim = anm.leftside;
                 else {
                    rightside = true;   // in case we were with the anm.middle
                    current_anim = anm.rightside;
                 }

                // land sound
                   VideoToons.getGameClient().sendSound( anm.land_sound );
                     
                 updateCurrentImage();
                 r.y = STAGE_HEIGHT[current_stage] -r.height +1;

              // do we have to display a new stage ? 
                 int currenttop = level.getCurrentTopTunnel();

                 if( ( ( current_stage == (currenttop-1) ) && (currenttop<9) )
                                     || ( (currenttop==9)&&(current_stage==8) ) )
                 {
                    gs.setRefreshMode(GameScreen.MOVE_LEVEL_UP);
                    level.addDefaultPlayerScore(LEVEL_UP_POINTS);
                    gs.repaint();

                    VideoToons.getGameClient().sendLevelUPData( level.getCurrentTopTunnel() );
                 }

                 if( current_stage==10 && spr_status!=BS_WINNER) {
                    spr_status = BS_WINNER;
                    level.addDefaultPlayerScore(WINNER_POINTS);
                 }

                return;
             }
        }

     // next fall step...
        translateY( anm.jump_offset[current_jump_cycle] );

        if( current_jump_cycle!=0 )
                 current_jump_cycle--;

        updateCurrentImage();
   }


 /***************************** JUMP UPGRADE ******************************/
 
   protected void updateJump()
   {
      updateMoveWhileJumping();
   	
       if( current_jump_cycle == 0 )
       {         	
           if( rightside == true )
                current_anim = anm.jump_right;
           else
                current_anim = anm.jump_left;

          // jump sound
           VideoToons.getGameClient().sendSound( anm.jump_sound );
       }

    // GOING OUT ON THE TOP OF THE SCREEN ?
       if(r.y <= STAGE_HEIGHT[level.getCurrentTopTunnel()]+25)
       {
       	 if( level.getCurrentTopTunnel()!=12 ) {
             gs.setRefreshMode(GameScreen.MOVE_LEVEL_UP);
             level.addDefaultPlayerScore(LEVEL_UP_POINTS);
             gs.repaint();

             VideoToons.getGameClient().sendLevelUPData( level.getCurrentTopTunnel() );
         }
         else
         {
             spr_status = BS_WINNER;
             level.addDefaultPlayerScore(WINNER_POINTS);
             stopJump();
         }
       }


    // STONE ENCOUNTER ? falling mode to activate ? stone to break ?
       byte current_stage = getStage();

         if( current_stage<10 && r.y<=STAGE_HEIGHT[current_stage+1]+30 )
         {
           byte l1 = getLeftStoneUnderUs();
           byte l2 = getRightStoneUnderUs();
        
            for( byte k=1; k>=0; k-- )
              for( byte l=l1; l<=l2; l++ )
              {
                 if((k==0) && (r.y > STAGE_HEIGHT[current_stage+1]+15))
                      break;

               // we study what to do with the current stone
                  switch( level.getStoneState( (byte)(current_stage+1), l, k) )
                  {
                    case STONE_BROKEN : break;

                    case STONE_ACID   :
                    case STONE_LEFT   :
                    case STONE_RIGHT  :
                    case STONE_SIMPLE : hitting_stone = true;
                                        level.setBrokenStone( (byte)(current_stage+1), (byte)l,
                                                              (byte)k, rightside);
                                        VideoToons.getGameClient().sendBrokenStoneData(
                                          (byte)(current_stage+1), (byte)l, (byte)k, rightside );
                                        level.addDefaultPlayerScore(STONE_POINTS);
                    case STONE_HLEFT  :
                    case STONE_HRIGHT :
                    case STONE_HARD   : jumping = false;
                                        falling = true;
                                        break;
                  }

               // Any level cloud ?
                  if( k==0 && l==l1 && current_stage<10 )
                  {
                     Cloud c = level.getStageCloud( (byte) (current_stage+1) );
                   
                      if(c!=null )
                      {
                        if( (r.x+anm.overlap_sprite < c.getX()+c.getWidth()) &&
                                     (r.x+r.width-anm.overlap_sprite>c.getX()) )
                        {
                           jumping = false;
                           falling = true;
                        }
                      }
                   }

                  if(falling == true)  // falling ?
                  {
                      if( rightside==true )
                      {
                      	  if(hitting_stone==true)
                              current_anim = anm.hit_up_right;
                          else
                              current_anim = anm.fall_right;
                      }
                      else
                      {
                      	 if(hitting_stone==true)
                             current_anim = anm.hit_up_left;
                         else
                             current_anim = anm.fall_left;
                      }

                      current_anim.reset();
                      current_jump_cycle = (anm.nb_jump_steps-1);

                      updateCurrentImage();
                      return;
                   }
              }
         }


    if(jumping)
        translateY( -anm.jump_offset[current_jump_cycle] );


      // does the jump just only ends ?
         if( current_jump_cycle == (anm.nb_jump_steps-1) )
         {
            jumping = false;
            falling = true;
                
            if( rightside == true )
                current_anim = anm.fall_right;
             else
                current_anim = anm.fall_left;
                  
            current_anim.reset();
         }
         else
            current_jump_cycle++;

      updateCurrentImage();
   }


 /****************************** PANG ! DEAD !******************************/

  protected void youAreDead()
  {
    if(spr_status == BS_DEAD || spr_status == BS_INVINCIBLE ) return;

     lives--;
     spr_status = BS_DEAD;

     level.addDefaultPlayerScore(DEATH_POINTS);

      current_dead_cycle = 0;
      current_anim = anm.dead;
      current_anim.reset();
      
      updateCurrentImage();

      // dead sound
         VideoToons.getGameClient().sendSound( anm.dead_sound );
  }


 /****************************** EUH HURT... ******************************/

 // returns true (still hurt), false(not hurt anymore)

  protected boolean areWeHurt()
  {
     if( spr_status == BS_HURT )
     {
      // First cycle ?
     	if( current_hurt_cycle==0 )
     	{
          if(rightside == true)
             current_anim = anm.hurt_right;
          else
             current_anim = anm.hurt_left;

          current_anim.reset();
          updateCurrentImage();

          // hurt sound
           VideoToons.getGameClient().sendSound( anm.hurt_sound );
        }
        else if( current_hurt_cycle==anm.nb_hurt_steps )
        {
     	 // End of hurt cycles
            if(hurt_right_side == true)
                extra_speed = 12;
            else
                extra_speed = -12;

         // ok land
     	    if (jumping==true) {
                jumping = false;
           
                if(current_jump_cycle!=0) {
                      falling = true;
                      current_jump_cycle = (anm.nb_jump_steps-1);
                }
            }

            current_anim.reset();
            updateCurrentImage();
            spr_status = BS_ACTIVE;
            return false;
     	}

      // Position update
          translateY( anm.hurt_offset[current_hurt_cycle] );

           if( hurt_right_side==true ) {
             r.x += 5;
             limitXRangeRight();
     	   }
     	   else {
             r.x -= 5;
             limitXRangeLeft();
     	   }

        current_hurt_cycle++;
        updateCurrentImage();
        return true;
     }
     
    return false;
  }

 /*************************************************************************/

  // If we get out of the screen ( yLeveltoScreen()>480 )
  // then the sprite is dead...
  // returns true if we are dead...

  private boolean callMeDeath()
  {
    boolean yes_dead = false;
    byte currenttop = level.getCurrentTopTunnel();

   // Did we fell out of the screen ? 
      if( r.y-STAGE_HEIGHT[currenttop] > 480)
      {
          if( spr_status == BS_INVINCIBLE )
          {
             // We re-appear on the first_stone 
                if(currenttop>4)
                      first_stone = level.getANonBrokenStone();

                level.addDefaultPlayerScore(COWARD_POINTS);
                move( first_stone[1]*STONE_WIDTH, STAGE_HEIGHT[first_stone[0]]-r.height);
          }
          else if(!jumping)
          {
                youAreDead();
                yes_dead = true;
          }
      }

   // Are we dead ?
      if( spr_status == BS_DEAD )
      {
          // Pos upgrade
             if( r.y>STAGE_HEIGHT[currenttop]+20 
                                  || anm.dead_offset[current_dead_cycle]>0 )
                translateY( anm.dead_offset[current_dead_cycle] );

             yes_dead = true;

          // Are we out of the screen ?
             if( r.y-STAGE_HEIGHT[currenttop] > 480)
             {
             	// no lives ? euh well I'm afraid it's done for us ...
                  if( lives == -1) {
                    spr_status = BS_GAMEOVER;
                    VideoToons.getSoundLibrary().stopMusic();
                    VideoToons.getSoundLibrary().playSound( SoundLibrary.GAMEOVER_SOUND );
                    return yes_dead;
                  }

                // We re-appear on the first_stone 
                   if(currenttop>4)
                       first_stone = level.getANonBrokenStone();

                   defaultState();
                   spr_status = BS_INVINCIBLE;

                   updateCurrentImage();
                   move( first_stone[1]*STONE_WIDTH, STAGE_HEIGHT[first_stone[0]]-r.height);                   
             }
             else if( current_dead_cycle != anm.nb_dead_steps-1 )
             {
               current_dead_cycle++;
               updateCurrentImage();
             }
      }

    // Are we still dead but in the invicible state ?
      if( spr_status == BS_INVINCIBLE )
      {
         if( movingright==true || movingleft==true || jumping==true )
         {
            spr_status = BS_ACTIVE;
            spr_visible = true;
         }
         else
         {
            spr_visible = !spr_visible;
            yes_dead = true;
         }
      }

    return yes_dead;
  }


 /**************************** POSITION UPGRADE ****************************/

  // To tell what to do after updating positions, images, sizes ...
  // This method is called before we redraw the sprite.
  //
  // IMPORTANT NOTE : to avoid conflicts with keyPressed(), keyReleased()
  //                  (see below) we must be 'synchronized' here.
  
    synchronized public void updateSpritePosition()
    {

     /***** SOME WORLD INTERACTION *****/

       // spirits doesn't interact with the real world ...
          if( callMeDeath() == true )
                return;

       // GROUND MOVEMENT UPGRADE ( falling ? )
          if( falling==false && jumping==false )
                updateGroundSteady();

       // UPDATE EXTRA SPEED
          updateSpeedEffect();
          updateCollisionSpeed();

       // HURT CASE
          if( areWeHurt() == true )
            return;


     /***** STATES CASES *****/

       // FALL CASE
          if( falling == true ){
              updateFall();
              return;
          }

       // JUMP CASE
          if( jumping == true ){
              updateJump();
              return;
           }

       // HIT CASE
          if(hitting==true){
              if(current_hit_cycle==0)
                VideoToons.getGameClient().sendSound( anm.hit_sound );
              
              updateHitMovement();
              return;
          }

       // RIGHT MOVEMENT
          if( movingright == true ){
              updateRightMovement();
              return;
          }

       // LEFT MOVEMENT
          if( movingleft == true ){
              updateLeftMovement();
              return;
          }

       // If we arrive here it means we are not moving.
          updateNoMovement();
     }

  /***************************** Player Key process *********************************/
 
  // key access for this sprite (if it wants to use it... )
  // We don't move the sprite directly but we submit an action ( moving, jumping ...)
  // that can be refused or changed by 'updateSpritePosition()'

     synchronized public void keyPressed(final int keycode)
     {
     	byte g_status = VideoToons.getGameStatus();

        switch (keycode)
        {
            case KeyEvent.VK_RIGHT:
                                    if(!quit_menu && g_status!=GM_PAUSE)
                                       startRight();
                                    break;
            case KeyEvent.VK_LEFT:  if(!quit_menu && g_status!=GM_PAUSE)
                                       startLeft();
                                    break;
            case KeyEvent.VK_SHIFT:
                                    if(!quit_menu && g_status!=GM_PAUSE)
                                       startJump();
                                    break;                                    
            case KeyEvent.VK_CONTROL:
                                    if(!quit_menu && g_status!=GM_PAUSE)
                                    startHit();
                                    break;
            case KeyEvent.VK_UP:
                                    if(quit_menu)
                                       quit_selected = !quit_selected;

                                    break;
            case KeyEvent.VK_DOWN:  if(quit_menu)
                                       quit_selected = !quit_selected;
                                    break;
        }
     }

     synchronized public void keyReleased(final int keycode)
     {
        switch (keycode)
        {
            case KeyEvent.VK_K:    youAreDead();  // DEBUG
                                   break;
/*            case KeyEvent.VK_H:    youAreHurt(false); // DEBUG
                                   break;
            case KeyEvent.VK_J:    youAreHurt(true); // DEBUG
                                   break;
*/            case KeyEvent.VK_R:    VideoToons.getGameScreen().setRefreshMode( GameScreen.WHOLE_SCREEN );
                                   VideoToons.getGameScreen().repaint(); // DEBUG
                                   break;
/*            case KeyEvent.VK_L:    if(lives<3) lives++; // DEBUG
                                   break;
            case KeyEvent.VK_S :   System.out.println(""+this);
                                   break;
*/            case KeyEvent.VK_P :   
                                   byte g_status = VideoToons.getGameStatus();

                                   if(g_status==GM_ACTIVE) {
                                      VideoToons.getGameClient().sendPauseMessage();
                                   }
                                   else if(g_status==GM_PAUSE)
                                      VideoToons.getGameClient().sendPauseEndMessage();

                                   break;
                                   
/*            case KeyEvent.VK_U:    VideoToons.getGameScreen().setRefreshMode( GameScreen.MOVE_LEVEL_UP );
                                   VideoToons.getGameScreen().repaint();
                                   VideoToons.getGameClient().sendLevelUPData( level.getCurrentTopTunnel() );
                                   break; // DEBUG
 
            case KeyEvent.VK_X:    System.exit(1);
                                   break; // DEBUG
*/
            case KeyEvent.VK_LEFT: stopLeft();
                                   break;

            case KeyEvent.VK_RIGHT:stopRight();
                                   break;
            case KeyEvent.VK_SHIFT:
                                   stopJump();
                                   break;  
            case KeyEvent.VK_ESCAPE:
                                   // quit menu creation or destruction
                                     quit_menu = !quit_menu;
                                   break;

            case KeyEvent.VK_ENTER:
                                     if(quit_menu)
                                     {
                                     	if(quit_selected) {
                                     	  if(VideoToons.getUserData().game_mode==ONEPLAYER) {
                                     	     spr_status = BS_GAMEOVER;
quit_menu = false;
//                                             VideoToons.getGameScreen().setRefreshMode( GameScreen.NONE );
                                          }
                                     	  else
                                     	     VideoToons.getGameClient().cancelGameClient();
                                        }
                                        else
                                           quit_menu = false;
                                     }
                                   break;
         }
      }

  /************************* QUIT MENU STATUS ACCESS *********************************/
      
  // does the player wants to display the Quit Menu ?
     synchronized public boolean wantsQuitMenu() {
         return quit_menu;     
     }

  // has the player selected the Quit Option ?
     synchronized public boolean isQuitSelected() {
         return quit_selected;     
     }

  /********************************** COLLISIONS *************************************/

  // Collision behaviour with another player
     public void collisionBehaviour( BasicSprite bs )
     {
     	byte state = bs.getStatus();
     	
     	// is there something to do ?
     	
          if(state==BS_INACTIVE || state==BS_DEAD || state==BS_INVINCIBLE || state==BS_GAMEOVER)
              return;

          Rectangle r_bs =  bs.getRectangle();

          if( r.intersects(r_bs) == false )
                return;

       // YEAH Collision detected !!!

       // Performing some inits
          Rectangle r_bs_old =  bs.getOldRectangle();

          boolean bs_jumping=false, bs_falling=false;
          
          int jump_or_fall = r_bs_old.y+r_bs_old.height-r_bs.y-r_bs.height;

          if( jump_or_fall > 1 )
              bs_jumping=true;
          else if(jump_or_fall < 1 )
              bs_falling=false;
 
       // Y - jumping collision ?
          if( jumping && r.y > r_bs.y ) {
              stopJump();
              return;
          }
          else
              if( falling && bs_jumping && r.y+r.height-r_bs.y<=20) {
                  falling=false;
                  startJump();
                  return;
              }

      // Hitting collisions ?
          if( r.x < r_bs.x ){
              if(state == BS_HITLEFT){
                youAreHurt(false);
                return;
              }
              else
                if(getStatus()==BS_HITRIGHT)
                   return;
          }
          else
          {
              if(state == BS_HITRIGHT)
                youAreHurt(true);
              else
                if(getStatus()==BS_HITLEFT )
                    return;
          }

    // side collisions 
       int dist = (r_bs.x+r_bs.width/2)-(r.x+r.width/2);

       if(dist>0)
             col_speed = (byte) -getExtraSpeedFromDistance( dist );
       else
             col_speed = (byte) getExtraSpeedFromDistance( -dist );

     }

 /*********************************************************************************/

     private byte getExtraSpeedFromDistance( int dist )
     {
       	if( dist<5 )
     	   return 18;
        else if( dist < 15 )
           return 15;
        else
           return 9;
     }

 /*********************************************************************************/

   protected void updateCollisionSpeed()
   {
      if( col_speed == 0 )
         return;
      
      r.x += col_speed/3;

      if( col_speed < 0) {
            limitXRangeLeft();
            col_speed++;
      }
      else {
            limitXRangeRight();
            col_speed--;
      }
   }

 /*********************************************************************************/

  // Collision behaviour with a monster

     public void collisionBehaviourWithMonster( BasicSprite bs )
     {
     	byte state = bs.getStatus();
        Rectangle r_bs =  bs.getRectangle();

       // collision ?
          if( r.intersects(r_bs) == false )
                return;

       // YEAH Collision detected !!! hurted monster ?
          if(bs.getStatus()==BS_HURT)
                return;

       // is the monster behind a tunnel ?
          if( bs.getStage()<TUNNEL_NUMBER )
          {
             Tunnel tn = level.getTunnel( bs.getStage(), bs.getSide() );

             if( tn!=null ) {
               Rectangle r_tn = tn.getRectangle();

               if( (getSide()==LEFT && r.x+r.width-anm.overlap_sprite<r_tn.x+r_tn.width)
                   || (getSide()==RIGHT && r_tn.x<r.x+anm.overlap_sprite) )
                   return;
             }
          }

       // were we hitting the monster ?
          if( r.x < r_bs.x ){
              if(getStatus()==BS_HITRIGHT) {
                  level.addDefaultPlayerScore(MONSTER_POINTS);
          	return;
              }

              if(state == BS_HITLEFT || state == BS_DANGEROUS)
                youAreDead();
          }
          else
          {
              if(getStatus()==BS_HITLEFT) {
                  level.addDefaultPlayerScore(MONSTER_POINTS);
                return;
              }

              if(state == BS_HITRIGHT || state == BS_DANGEROUS)
                youAreDead();

          }
     }

 /*********************************************************************************/

  public String toString()
  {
   return "Player:\ncurrent_anim"+current_anim
        +"\n rightside:"+rightside
        +" lefttside:"+leftside 
        +" movingright:"+movingright
        +" movingleft:"+movingleft
        
        +"\n jumping: "+jumping
        +" falling: "+falling 
        +" hitting: "+hitting
        +" hitting_stone: "+hitting_stone
        +"\n spr_visible: "+spr_visible
        +" extra_speed: "+extra_speed
        +" spr_status: "+spr_status
        +"\n hit_cycle: "+current_hit_cycle
        +" hurt_cycle: "+current_hurt_cycle
        +" dead_cycle: "+current_dead_cycle;
  }

 /*********************************************************************************/
}