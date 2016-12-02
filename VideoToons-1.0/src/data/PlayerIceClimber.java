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

package videotoons.data;

import videotoons.game.*;


class PlayerIceClimber
{
   public AnimLibrary anim;

    public PlayerIceClimber()
    {
    	anim = new AnimLibrary();

         anim.name = new String("iceclimber");
         anim.nb_images = 20;

      // Images offset & indexes

         int array1[] = { 12, 13, 14, 15 };
         anim.move_right = new Anim( array1 );
        
         int array2[] = { 0, 1, 2, 3 };
         anim.move_left = new Anim( array2 );
         
         int array3[] = { 20, 19, 18, 17, 16, 15, 13, 10, 6, 2, 1 };
         anim.jump_offset = array3;
         anim.nb_jump_steps=11;
         
         int array4[] = { 0 };
         anim.leftside = new Anim( array4 );

         int array5[] = { 12 };
         anim.middle = new Anim( array5 );
         
         int array6[] = { 12 };
         anim.rightside = new Anim( array6 );
         
         int array7[] = { 4 };
         anim.jump_left = new Anim( array7 );
         
         int array8[] = { 16 };
         anim.jump_right = new Anim( array8 );

         int array9[] = { 19 };
         anim.hit_up_right = new Anim( array9 );         

         int array10[] = { 7 };
         anim.hit_up_left = new Anim( array10 );

         int array11[] = { 7 };
         anim.fall_left = new Anim( array11 );
         
         int array12[] = { 19 };
         anim.fall_right = new Anim( array12 );

         int array13[] = { -20, -20, -15, -10, -6, -2, 0, 2, 6, 10, 15, 20, 25 };
         anim.dead_offset = array13;
         anim.nb_dead_steps = 13;

         int array14[] = { 8, 8, 8, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9 };
         anim.dead = new Anim( array14 );
         
         int array15[] = { 10, 10, 10, 11, 11, 11 };
         anim.hurt_left = new Anim( array15 );
         
         int array16[] = { 10, 10, 10, 11, 11, 11 };
         anim.hurt_right = new Anim( array16 );

         int array17[] = { -5, -2, 0, -5, -2, 0 };
         anim.hurt_offset = array17;
         anim.nb_hurt_steps=6;

         int array18[] = { 4, 5, 6, 6, 6, 5, 4, 0  };
         anim.hit_left = new Anim( array18 );
         
         int array19[] = { 16, 17, 18, 18, 18, 17, 16, 12  };
         anim.hit_right = new Anim( array19 );

         int array20[] = { 0, -18, 0, 0, 0, 18, 0, 0 };
         anim.hit_left_offset = array20;
         
         int array21[] = { -26, 26, 0, 0, 0, 0, -26, 26 };
         anim.hit_right_offset =array21;
         anim.nb_hit_steps = 8;

         anim.sprite_basicspeed = 4;
         anim.overlap_sprite = 10;
         anim.std_width = 24;
         anim.std_height = 40;

         anim.hit_interval = 400;

         anim.jump_sound = SoundLibrary.JUMP_SOUND;
         anim.land_sound = SoundLibrary.LAND_SOUND;
         anim.hit_sound = SoundLibrary.HARD_HIT_SOUND;
         anim.hurt_sound = SoundLibrary.LONG_HURT_SOUND;
         anim.dead_sound = SoundLibrary.DEAD_SOUND;
    }
}
