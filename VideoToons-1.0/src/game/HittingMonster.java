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


class HittingMonster extends Monster
{

  // are we hunting someone ?
     private boolean hunting;

  // hunted player index
     private byte hunted_player;

  // default speed
     private byte default_speed;

  // did we turn (to avoid a hole) when seeking our target player ?
     private boolean has_turned;

  // current awareness after an action it is turned to 0. It progressively
  // increases to reach MAXIMUM_AWARENESS. When it is not to the MAXIMUM_AWARENESS
  // value, a monster will walk toward broken stones and fall...
     private byte awareness;
  
  // variable for respawn time measure
     private long invisible_interval_t0;
  
  // maximum monster awareness
     private static final byte MAXIMUM_AWARENESS = 25;
     
  // collision timer
     private byte col_timer;

 /**************************** MONSTER CONSTRUCTOR *****************************/

    public HittingMonster( AnimLibrary anm, ServerLevelProcess level, byte stone, byte stage, byte speed)
    {
      super(anm,level,stone,stage,speed);

      // default speed
         default_speed = anm.sprite_basicspeed;
    
      // initial behaviour
         defaultBehaviour();
    }

   
 /**************************************************************************/
 /********************* REDEFINITION OF ABSTRACT METHODS *******************/
 /**************************************************************************/
 
 
 /******************************* DO WE FALL ********************************/

  // returns true if the sprite falls... ALSO upgrades X pos if the sprite
  // is on moving stones ...

   protected boolean doWeFall()
   {
    // We are on a stage ground... do we fall ?
       byte state;
       byte current_stage = getStage();
       
    // is there any non-broken stone under us ?
         for( byte l=getLeftStoneUnderUs(); l<=getRightStoneUnderUs(); l++ )
           if( ( state=level.getStoneState( current_stage, l, 0) ) != STONE_BROKEN )
           {
             // ok, we found a stone... but is it moving ?
                if(state == STONE_LEFT || state == STONE_HLEFT )
           		 extra_speed = -5;
                else if(state == STONE_RIGHT || state == STONE_HRIGHT )
           		 extra_speed = 5;

                return false;
           }

     // if we arrive here it means every stone under us is broken
        falling = true;
        translateY( 1 );   // inits the fall...
        return true;
   }


 /***************************** UPDATE THE FALL ******************************/   

   protected void updateFall()
   {

     // does the fall ends ?
        byte current_stage = getStage();
        short current_y_limit = (short) ( STAGE_HEIGHT[current_stage]-anm.std_height+1 );
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
            	youAreHurt(rightside);
            	
                updateCurrentImage();
                r.y = STAGE_HEIGHT[current_stage] -r.height;
                
                level.addSound( anm.land_sound );
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
   {}

 /***************************** HURT UPGRADE *****************************/

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
          level.addSound( anm.hurt_sound );
        }
        else if( current_hurt_cycle==anm.nb_hurt_steps )
        {
     	 // End of hurt cycles
     	    current_hurt_cycle = 0;
            current_anim.reset();
     	}

      // Position update ?
         if(r.x<0 || r.x+r.width>=640) {
            defaultBehaviour();
            return false;
         }

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

 /**************************** POSITION UPGRADE ****************************/

  // To tell what to do after updating positions, images, sizes ...
  // This method is called before we redraw the sprite.
  
    public void updateSpritePosition()
    {
     /******* MONSTER BASIC IA *********/

          updateBehaviour();

          if(spr_status==BS_INVINCIBLE)
             return;

     /***** SOME WORLD INTERACTION *****/

       // GROUND MOVEMENT UPGRADE ( falling ? )
          if( !falling )
                updateGroundSteady();

       // UPDATE EXTRA SPEED
          updateSpeedEffect();


     /***** STATES CASES *****/

       // FALL CASE
          if( falling == true ){
              updateFall();
              return;
          }

       // HURT CASE
          if( areWeHurt() )
                return;

       // HIT CASE
          if(hitting==true){
              if(current_hit_cycle==1)
                 level.addSound( anm.hit_sound );
              
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

 /********************************************************************************/

    private void defaultBehaviour()
    {
       defaultState();

       setRectangle( first_stone[1]*STONE_WIDTH,
                     STAGE_HEIGHT[first_stone[0]]-anm.std_height,
                     anm.std_width, anm.std_height );

       spr_status = BS_INVINCIBLE;
       spr_visible = false;
       hunting=false;
       has_turned=false;
       
       anm.sprite_basicspeed = default_speed;
       awareness = MAXIMUM_AWARENESS;
       invisible_interval_t0 = System.currentTimeMillis();

       if(getSide()==LEFT)
            startRight();
       else
            startLeft();    
    }

 /********************************************************************************/


     private void updateBehaviour()
     {
         long t1 = level.getCurrentTime();
           
         if(col_timer!=0)
            col_timer--;

         if( !spr_visible && t1-invisible_interval_t0 < anm.invisible_interval )
              return;
         else if(!spr_visible)
         {
              invisible_interval_t0 = t1;
              spr_visible = true;
              spr_status = BS_ACTIVE;
         }


       // collision timer
          if(awareness<MAXIMUM_AWARENESS)
              awareness++;

          byte currenttop = level.getCurrentTopTunnel();

       // Did we fell out of the screen ?
          if( r.y-STAGE_HEIGHT[currenttop] >= 480)
          {
             defaultBehaviour();
             return;
          }
          
       // are we falling or hurt ?
          if(falling || spr_status == BS_HURT)
               return;

       // broken stone on our side ?
          if( movingleft && isStoneOnMyLeftBroken() && awareness==MAXIMUM_AWARENESS) {
              startRight();
              awareness=0;

              if(hunting)
                has_turned=true;
          }
          else if(movingright && isStoneOnMyRightBroken() && awareness==MAXIMUM_AWARENESS) {
              startLeft();
              awareness=0;

              if(hunting)
                has_turned=true;
          }

       // already hunting someone ?
          if(hunting)
          {
             BasicSprite bs = level.getNetPlayer(hunted_player);
             byte state = bs.getStatus();

             if(bs.getStage()!=getStage() || state == BS_DEAD || state == BS_GAMEOVER)
             {
                 hunting=false;
                 has_turned=false;
                 anm.sprite_basicspeed = default_speed;

                 if(movingleft)
                    startRight();
                 else
                    startLeft();
             }
             else
             {
             	 if( r.x<bs.getX() )
             	 {
             	    if(movingright)
             	       has_turned=false;
             	 	
             	    if( bs.getX()-r.x-r.width < 10 && rightside) {
             	       startHit();
                    }
             	    else if(!movingright && !has_turned) {
             	       startRight();
                       awareness=0;
                    }
             	 }
                 else
                 {
                    if(movingleft)
             	       has_turned=false;
                 	
             	    if( r.x-bs.getX()-bs.getWidth() < 10 && leftside) {
             	       startHit();
                    }
             	    else if(!movingleft && !has_turned) {
             	       startLeft();
                       awareness=0;
                    }
                 }
             }

             return;
          }

       // do we have someone to hunt ?
          for(byte i=0; i<level.getNetPlayerNumber(); i++ )
          {
             BasicSprite bs = level.getNetPlayer(i);
             byte state = bs.getStatus();

             if(state==BS_INACTIVE || state==BS_DEAD || state==BS_INVINCIBLE || state==BS_GAMEOVER)
                continue;

             if(bs.getStage()==getStage() &&  -200<bs.getX()-r.x && bs.getX()-r.x<200 )
             {
                // ok let's hunt !
                   hunting = true;
                   hunted_player = i;                   
                   anm.sprite_basicspeed = (byte) (default_speed + 3);
                   break;
             }
          }

     }

 /********************************************************************************/

     public void collisionBehaviour( BasicSprite bs )
     {
       // if the number of cycles since last collision is not elapsed we quit...
         byte state = bs.getStatus();     	
         Rectangle r_bs =  bs.getRectangle();

          if( falling || spr_status== BS_HURT || r.intersects(r_bs) == false )
                return;

       // YEAH Collision detected !!!
          if(( r.x < r_bs.x && state==BS_HITLEFT ) || (r.x >= r_bs.x && state==BS_HITRIGHT) ) {
             youAreHurt(leftside);
             col_timer=0;
          }
     }

 /********************************************************************************/

   public void collisionWithBlock( Block bl )
   {
      if( col_timer!=0 || r.intersects(bl.getRectangle()) == false )
          return;

       col_timer = 4;
       hurt_right_side = !hurt_right_side;

      if( movingright ) {
           startLeft();
           awareness=0;
           extra_speed -=6;

           if(hunting)
             has_turned=true;
       }
       else
       {
           startRight();
           awareness=0;
           extra_speed +=6;

           if(hunting)
             has_turned=true;
       }
   }

 /********************************************************************************/

}