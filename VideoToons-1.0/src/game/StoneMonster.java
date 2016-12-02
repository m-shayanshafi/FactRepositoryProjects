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


class StoneMonster extends Monster
{
	
  // our initial side of the screen
    private byte initial_side;

 //  min cycles between two collision
    private byte col_timer;


 /******************************* MONSTER CONSTRUCTOR *****************************/

    public StoneMonster( AnimLibrary anm, ServerLevelProcess level, byte stone, byte stage, byte speed)
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

   protected void updateFall()  {}


 /***************************** JUMP UPGRADE ******************************/
 
   protected void updateJump()  {}

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
          level.addSound( anm.hurt_sound );
        }
        else if( current_hurt_cycle==anm.nb_hurt_steps )
        {
     	 // End of hurt cycles
     	    current_hurt_cycle = 0;
            defaultBehaviour();
            return true;
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
    }

 /********************************************************************************/

    public void pleaseFillHole( int x_start )
    {
       setRectangle( x_start,
                     STAGE_HEIGHT[first_stone[0]]-anm.std_height,
                     anm.std_width, anm.std_height );

       initial_side = getSide();

       if(getSide()==LEFT)
            startRight();
        else
            startLeft();

       spr_visible=true;
       spr_status = BS_DANGEROUS;
    }

 /********************************************************************************/

     private void updateBehaviour()
     {
       // visible ?
          if( !spr_visible )
              return;

          if(col_timer!=0)
             col_timer--;

          byte currenttop = level.getCurrentTopTunnel();

       // out of the screen ?
          if( r.y-STAGE_HEIGHT[currenttop] >= 480)
             return;
          
       // are we falling or hurt ?
          if(falling)
          {
            // searching broken stone
               byte leftstone = getLeftStoneUnderUs();
               byte rightstone = getRightStoneUnderUs();

               if(initial_side==LEFT && rightstone<31)
                  rightstone++; 
               else if(initial_side==RIGHT && leftstone>0)
                  leftstone--; 

               for(byte i=leftstone; i<=rightstone; i++)
                   if(level.getStoneState( getStage(), i, 0)==STONE_BROKEN)
                       level.setFilledHole( getStage(), i );

               defaultBehaviour();
               level.addSound( anm.hit_sound );
               return;
          }
     
      // arriving on a side ?
          if(initial_side==LEFT)
          {
              if(r.x+r.width>640)
                 defaultBehaviour();
          }
          else
          {
              if(r.x<0)
                 defaultBehaviour();
          }

     }

 /********************************************************************************/

     public void collisionBehaviour( BasicSprite bs )
     {
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

      col_timer=4;
      youAreHurt(leftside);
   }

 /********************************************************************************/

}