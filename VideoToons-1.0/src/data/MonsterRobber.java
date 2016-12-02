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


class MonsterRobber extends AnimMonsterLibrary
{
  public AnimMonsterLibrary anim = new AnimMonsterLibrary();

    public MonsterRobber()
    {
         anim.name = new String("robber");
         anim.nb_images = 10;

         anim.behaviour = GameDefinitions.MB_HOLEFILLER;


      // Images offset & indexes

         int array1[] = { 4, 5, 5, 5, 4, 4, 4, 6, 6, 6, 4, 4 };
         anim.move_right = new Anim( array1 );
        
         int array2[] = { 0, 1, 1, 1, 0, 0, 0, 2, 2, 2, 0, 0 };
         anim.move_left = new Anim( array2 );

         int array3[] = { 20, 19, 18, 17, 16, 15, 13, 10, 6, 2, 1 };
         anim.jump_offset = array3;
         anim.nb_jump_steps=11;

         int array4[] = { 0 };
         anim.leftside = new Anim( array4 );

         int array5[] = { 0 };
         anim.middle = new Anim( array5 );
         
         int array6[] = { 4 };
         anim.rightside = new Anim( array6 );
         
         anim.jump_left = null;
         anim.jump_right = null;
         anim.hit_up_right = null;
         anim.hit_up_left = null;

         int array11[] = { 3, 3, 3, 3, 8, 8, 8, 8 };
         anim.fall_left = new Anim( array11 );
         
         int array12[] = { 7, 7, 7, 7, 9, 9, 9, 9 };
         anim.fall_right = new Anim( array12 );

         anim.dead_offset = null;
         anim.nb_dead_steps = 0;
         anim.dead = null;

         int array15[] = { 3, 3, 3, 3, 8, 8, 8, 8 };
         anim.hurt_left = new Anim( array15 );
         
         int array16[] = { 7, 7, 7, 7, 9, 9, 9, 9 };
         anim.hurt_right = new Anim( array16 );

         int array17[] = { -8, -15, -12, -5, 0, 0, };
         anim.hurt_offset = array17;
         anim.nb_hurt_steps=6;

         anim.hit_left = null;
         anim.hit_right = null;
         anim.hit_left_offset = null;
         anim.hit_right_offset = null;
         anim.nb_hit_steps = 0;
         
         anim.sprite_basicspeed = 2;
         anim.overlap_sprite = 9;
         anim.std_width = 27;
         anim.std_height = 37;

         anim.hit_interval = 2000;
         anim.invisible_interval = 3000;

         anim.jump_sound = SoundLibrary.JUMP_SOUND;
         anim.land_sound = SoundLibrary.LAND_SOUND;
         anim.hit_sound = SoundLibrary.LIGHT_HIT_SOUND;
         anim.hurt_sound = SoundLibrary.HIGH_HURT_SOUND;
         anim.dead_sound = SoundLibrary.DEAD_SOUND;
    }
}
