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


class HeavyStoneMonster extends Monster
{

  // collision speed (like extra_speed but ... for coll...)
     private byte col_speed;   

  // collision timer
     private byte col_timer;

 /**************************** MONSTER CONSTRUCTOR *****************************/

    public HeavyStoneMonster( AnimLibrary anm, ServerLevelProcess level, byte stone, byte stage, byte speed)
    {
      super(anm,level,stone,stage,speed);

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
           		 extra_speed = -STONE_SPEED*3-2;
                else if(state == STONE_RIGHT || state == STONE_HRIGHT )
           		 extra_speed = STONE_SPEED*3+2;

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
                spr_status = BS_ACTIVE;

                updateCurrentImage();
                r.y = STAGE_HEIGHT[current_stage] -r.height;

                level.addSound( anm.land_sound );
                return;
             }
        }

     // next fall step...
        translateY( anm.jump_offset[current_jump_cycle] );

        if( spr_status!=BS_DANGEROUS && anm.jump_offset[current_jump_cycle] > 12 )
             spr_status=BS_DANGEROUS;

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
     if( spr_status == BS_HURT || (spr_status==BS_DANGEROUS && !falling) )
     {
      // First cycle ?
     	if( current_hurt_cycle==0 )
     	{
     	  spr_status = BS_DANGEROUS;

          if(rightside == true)
             current_anim = anm.hurt_right;
          else
             current_anim = anm.hurt_left;

          current_anim.reset();
          updateCurrentImage();
        }
        else if( current_hurt_cycle==anm.nb_hurt_steps )
        {
     	 // End of hurt cycles
     	    current_hurt_cycle = 0;
            defaultBehaviour();
            return false;
     	}

        translateY( anm.hurt_offset[current_hurt_cycle] );

         if( hurt_right_side ) {
             r.x += 7;
             extra_speed = 18;
             limitXRangeRight();
         }
     	 else {
             r.x -= 7;
             extra_speed = -18;
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

     /***** SOME WORLD INTERACTION *****/

       // GROUND MOVEMENT UPGRADE ( falling ? )
          if( !falling )
                updateGroundSteady();

       // UPDATE EXTRA SPEED
          updateSpeedEffect();
          updateCollisionSpeed();

     /***** STATES CASES *****/

       // HURT CASE
          if( areWeHurt() )
                return;

       // FALL CASE
          if( falling ){
              updateFall();
              return;
          }

     }

 /********************************************************************************/

    private void defaultBehaviour()
    {
       defaultState();
       spr_status = BS_ACTIVE;
       spr_visible = true;
    }

 /********************************************************************************/

    private void updateBehaviour()
    {
       // collision timer
          if(col_timer!=0)
              col_timer--;
    }

 /********************************************************************************/

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
 /********************************************************************************/

     public void collisionBehaviour( BasicSprite bs )
     {
       // if the number of cycles since last collision is not elapsed we quit...
          if(col_timer!=0)
             return;

         byte state = bs.getStatus();     	
         Rectangle r_bs =  bs.getRectangle();

          if( falling || r.intersects(r_bs) == false )
                return;

       // YEAH Collision detected !!!
          if(r.x < r_bs.x && state==BS_HITLEFT )
          {
             youAreHurt(false);
             col_timer = 6;
          }
          else if( r.x >= r_bs.x && state==BS_HITRIGHT )
          {
             youAreHurt(true);
             col_timer = 6;
          }
          else if(r.x<r_bs.x)
             col_speed = -6;
          else
             col_speed = 6;
     }

 /********************************************************************************/

   public void collisionWithBlock( Block bl )
   {
      if( r.intersects(bl.getRectangle()) == false )
          return;

     // ok, I'm sure the previous pos was better...
        r.x = old_r.x;

     // let's improve it...
       if(bl.getX()<r.x)
          r.x = bl.getX()+bl.getWidth()+1;  // +1 to be sure to avoid a new col
       else
          r.x = bl.getX()-r.width-1;

       col_speed = (byte) -col_speed;
       extra_speed = (byte) -extra_speed;
       hurt_right_side = !hurt_right_side;

      level.addSound( anm.hit_sound );
   }

 /********************************************************************************/

}