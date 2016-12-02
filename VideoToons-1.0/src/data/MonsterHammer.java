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


class MonsterHammer
{
  public AnimMonsterLibrary anim = new AnimMonsterLibrary();

    public MonsterHammer()
    {
         anim.name = new String("hammer");
         anim.nb_images = 24;

         anim.behaviour = GameDefinitions.MB_LEVELUP;

      // Images offset & indexes

         int array1[] = { 0, 1, 2, 2, 3, 4, 4};
         anim.move_right = new Anim( array1 );
        
         int array2[] = { 12, 13, 14, 14, 15, 16, 16 };
         anim.move_left = new Anim( array2 );

         int array3[] = { 20, 19, 18, 17, 16, 15, 13, 10, 6, 2, 1 };
         anim.jump_offset = array3;
         anim.nb_jump_steps=11;

         int array4[] = { 12 };
         anim.leftside = new Anim( array4 );

         int array5[] = { 0 };
         anim.middle = new Anim( array5 );
         
         int array6[] = { 0 };
         anim.rightside = new Anim( array6 );
         
         anim.jump_left = null;
         anim.jump_right = null;
         anim.hit_up_right = null;
         anim.hit_up_left = null;

         int array11[] = { 23 };
         anim.fall_left = new Anim( array11 );
         
         int array12[] = { 11 };
         anim.fall_right = new Anim( array12 );

         anim.dead_offset = null;
         anim.nb_dead_steps = 0;
         anim.dead = null;

         int array15[] = { 21, 21, 21, 22, 22, 22, 22, 22, 22 };
         anim.hurt_left = new Anim( array15 );
         
         int array16[] = { 9, 9, 9, 10, 10, 10, 10, 10, 10 };
         anim.hurt_right = new Anim( array16 );

         int array17[] = { -5, -10, -12, -8, -2, 0, };
         anim.hurt_offset = array17;
         anim.nb_hurt_steps=6;

         int array18[] = { 17, 17, 18, 19, 20, 20, 20, 19, 18, 17, 12  };
         anim.hit_left = new Anim( array18 );
         
         int array19[] = {  5, 5, 6, 7, 8, 8, 8, 7, 6, 5, 0 };
         anim.hit_right = new Anim( array19 );

         int array20[] = { 0, 0, -5, -15, -3, 0, 0, 3, 15, 5, 0 };
         anim.hit_left_offset = array20;
         
         int array21[] = { 0, 0, 15, 0, 0, 0, 0, 0, -15, 0, 0  };
         anim.hit_right_offset = array21;
         anim.nb_hit_steps = 11;
         
         anim.sprite_basicspeed = 4;
         anim.overlap_sprite = 15;
         anim.std_width = 53;
         anim.std_height = 48;

         anim.hit_interval = 2000;
         anim.invisible_interval = 40000;

         anim.jump_sound = SoundLibrary.JUMP_SOUND;
         anim.land_sound = SoundLibrary.LAND_SOUND;
         anim.hit_sound = SoundLibrary.HAMMER_SOUND;
         anim.hurt_sound = SoundLibrary.LOW_HURT_SOUND;
         anim.dead_sound = SoundLibrary.DEAD_SOUND;
    }
}