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


class Level_World01_Level01 implements GameDefinitions
{
  public LevelData ldat;

   public Level_World01_Level01()
   {
      ldat = new LevelData();

   // path name
        ldat.path_name = new String("w01-l01");

   // music
        ldat.music_name = new String("music/level-skistation.mid");

   // Stones pre destroyed ?
   
        boolean array0[]= {  false,   	// stage 0
                             false,  	// stage 1
                             false,  	// stage 2
                             false,	// stage 3
                             false, 	// stage 4
                             false,  	// stage 5          
                             false,  	// stage 6
                             false,	// stage 7
                             false,	// stage 8
                             false,	// stage 9
                             false, };	// stage 10

        ldat.pre_destroyed_stones = array0;

   // Stones states

     byte array1[][] = {
   //------1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2---------------
     	{  4,4,4,4,4,4,4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,4,4,4,4,4,4,4,4,
     	   0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0  }, // stage 10
   //------1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2---------------
     	{  0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     	   0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0  }, // stage 9
   //------1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2---------------
     	{  4,4,4,4,4,1,1,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,4,4,4,4,4,
     	   0,0,0,0,0,1,1,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0  }, // stage 8
   //------1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2---------------
     	{  4,4,4,4,4,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,4,4,4,4,4,
     	   0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0  }, // stage 7
   //------1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2---------------
     	{  0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     	   0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0  }, // stage 6
   //------1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2---------------
     	{  4,4,4,4,4,0,0,0,4,4,4,0,0,0,4,4,4,4,0,0,0,4,4,4,0,0,0,4,4,4,4,4,
     	   0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0  }, // stage 5
   //------1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2---------------
     	{  4,4,4,4,4,7,7,7,1,1,1,7,7,7,1,1,1,1,7,7,7,1,1,1,7,7,7,4,4,4,4,4,
     	   0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0  }, // stage 4
   //------1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2---------------
     	{  4,4,4,4,4,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,4,4,4,4,4,
     	   0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0  }, // stage 3
   //------1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2---------------
     	{  4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,4,4,4,4,
     	   0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0  }, // stage 2
   //------1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2---------------
     	{  4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,4,4,4,4,
     	   0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0  }, // stage 1
   //------1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2---------------
     	{  4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,
     	   0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0  }  // stage 0
     	};

     	ldat.stone_state = array1;

   // stage images ( -1 == no stage image)
     	ldat.nb_stage_images = 4;

     	int array2[] = { -1, -1, -1, 0, 1, -1, -1, 2, 3, -1, -1 };
     	ldat.stage_image_index = array2;
     	
     	
    // Tunnels
        ldat.nb_tunnel_images = 7;

        int array3[][] =    { { -1,  0 },  	// stage 0
                              {  4,  1 },  	// stage 1
                              {  3,  2 },  	// stage 2
                              {  5,  6 },	// stage 3
                              { -1, -1 },	// stage 4
                              { -1, -1 },  	// stage 5          
                              { -1, -1 },  	// stage 6
                              {  5,  6 } };	// stage 7
        ldat.tunnel_image_index = array3;

        int array4[][] =    { {   0, 14 },  	// stage 0
                              {   0, 14 },  	// stage 1
                              {   0, 14 },  	// stage 2
                              { -20, 20 },	// stage 3
                              {   0,  0 }, 	// stage 4
                              {   0,  0 },  	// stage 5 
                              {   0,  0 },  	// stage 6
                              { -37, 37 } };	// stage 7
        ldat.tunnel_offset = array4;

        boolean array5[][]= { { false, false  },  	// stage 0
                              { true,  true   },  	// stage 1
                              { true,  true   },  	// stage 2
                              { false, false  },	// stage 3
                              { false, false  }, 	// stage 4
                              { false, false  },  	// stage 5          
                              { false, false  },  	// stage 6
                              { false, false  } };	// stage 7

        ldat.tunnel_collision = array5;

        
     // Clouds at stage   0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10 
        int array6[] = { -1,  0,  0, -1, -1, -1,  1, -1, -1,  2, -1  };
        ldat.cloud_image_index = array6;

     // Clouds speed      0,  1,  2,  3,  4,  5,  6,  7,  8,  9,  10
        byte array7[] = { 0,  3, -4,  0,  0,  0,  4,  0,  0, -10, 0  };
        ldat.cloud_speed = array7;

     // Transparent cloud ?
        boolean array8[]= {  false,   	// stage 0
                             false,  	// stage 1
                             false,  	// stage 2
                             false,	// stage 3
                             false, 	// stage 4
                             false,  	// stage 5          
                             true,  	// stage 6
                             false,	// stage 7
                             false,	// stage 8
                             true,	// stage 9
                             false, };	// stage 10

        ldat.transparent_cloud = array8;
        ldat.nb_cloud_images=3;

     // Block at stage    0,  1,  2,  3,  4,  5,  6,  7  
        int array9[] = { -1, -1, -1, -1, -1, -1, -1, -1  };
        ldat.block_image_index = array9;
        
        int array10[] ={ -1, -1, -1, -1, -1, -1, -1, -1  };
        ldat.block_stone = array10;
        
        ldat.nb_block_images=0;


     // MONSTERS !!!
        byte array11[][]= { { MT_NONE,    MT_SAMOURAI  },  	// stage 0
                            { MT_NONE,    MT_NONE      },  	// stage 1
                            { MT_STALACTITE,    MT_STALACTITE      },  	// stage 2
                            { MT_ROBBER,  MT_ICESTONE  },	// stage 3
                            { MT_STALACTITE, MT_NONE      }, 	// stage 4
                            { MT_NONE,    MT_NONE      },  	// stage 5          
                            { MT_STALACTITE, MT_STALACTITE },  	// stage 6
                            { MT_ICESTONE,MT_ROBBER    },  	// stage 7
                            { MT_HAMMER, MT_SNOWBALL   } };	// stage 8

        ldat.monsters_id = array11;

    // monster speed (-1 means default)
        byte array12[][]= { { -1,  -1 },  // stage 0
                            { -1,  -1 },  // stage 1
                            { -1,  -1 },  // stage 2
                            { -1,  -1 },	// stage 3
                            { -1,  -1 }, 	// stage 4
                            { -1,  -1 },  // stage 5          
                            { -1,  -1 },  // stage 6
                            { -1,  -1 },  // stage 7
                            {  6,  -1 } };// stage 8

        ldat.monsters_speed = array12;
  
      // monster extra parameter (-1 means none)
        byte array13[][]= { { -1,  -1 },  // stage 0
                            { -1,  -1 },  // stage 1
                            { -1,  -1 },  // stage 2
                            { -1,  -1 },  // stage 3
                            { -1,  -1 },  // stage 4
                            { -1,  -1 },  // stage 5          
                            { -1,  -1 },  // stage 6
                            { -1,  -1 },  // stage 7
                            { -1,  -1 } };// stage 8

        ldat.monster_parameter = array13;
  
        ldat.time_before_next_stage = 50000;
   }

}