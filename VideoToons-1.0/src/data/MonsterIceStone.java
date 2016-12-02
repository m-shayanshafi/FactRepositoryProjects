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


class MonsterIceStone extends AnimMonsterLibrary
{
  public AnimMonsterLibrary anim = new AnimMonsterLibrary();

    public MonsterIceStone()
    {
         anim.name = new String("icestone");
         anim.nb_images = 3;

         anim.behaviour = GameDefinitions.MB_STONE;

      // Images offset & indexes

         int array1[] = { 0 };
         anim.move_right = new Anim( array1 );
        
         int array2[] = { 0 };
         anim.move_left = new Anim( array2 );

         int array3[] = null;
         anim.jump_offset = array3;
         anim.nb_jump_steps=0;

         int array4[] = { 0 };
         anim.leftside = new Anim( array4 );

         int array5[] = { 0 };
         anim.middle = new Anim( array5 );
         
         int array6[] = { 0 };
         anim.rightside = new Anim( array6 );
         
         anim.jump_left = null;
         anim.jump_right = null;
         anim.hit_up_right = null;
         anim.hit_up_left = null;

         int array11[] = { 0, 1, 2 };
         anim.fall_left = new Anim( array11 );
         
         int array12[] = { 0, 1, 2 };
         anim.fall_right = new Anim( array12 );

         anim.dead_offset = null;
         anim.nb_dead_steps = 0;
         anim.dead = null;

         int array15[] = { 0, 1, 1, 2, 2, };
         anim.hurt_left = new Anim( array15 );
         
         int array16[] = { 0, 1, 1, 2, 2, };
         anim.hurt_right = new Anim( array16 );

         int array17[] = { 0, 0, 0, 0, 0, };
         anim.hurt_offset = array17;
         anim.nb_hurt_steps=5;

         anim.hit_left = null;
         anim.hit_right = null;
         anim.hit_left_offset = null;
         anim.hit_right_offset = null;
         anim.nb_hit_steps = 0;
         
         anim.sprite_basicspeed = 2;
         anim.overlap_sprite = 7;
         anim.std_width = 14;
         anim.std_height = 14;

         anim.hit_interval = 2000;
         anim.invisible_interval = 3000;

         anim.jump_sound = SoundLibrary.JUMP_SOUND;
         anim.land_sound = SoundLibrary.LAND_SOUND;
         anim.hit_sound = SoundLibrary.FILLEDHOLE_SOUND;
         anim.hurt_sound = SoundLibrary.BROKENSTONE_SOUND;
         anim.dead_sound = SoundLibrary.DEAD_SOUND;
    }
}
