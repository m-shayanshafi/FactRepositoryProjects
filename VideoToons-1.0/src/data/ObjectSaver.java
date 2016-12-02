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

/** Creates new .dat files for levels, players & monsters anims
 */

public class ObjectSaver
{

   public static void main(String argv[])
   {
      saveLevels();
      savePlayerAnims();
      saveMonsterAnims();
   }

 /*********************************************************************************/

   private static void saveLevels()
   {
      System.out.println("saving levels...");
      
      // Bonus
         Tools.saveObjectToFile( "graphics/levels/z-bonus-01/level.dat", new Level_World00_Level01().ldat );

      // World 0
         Tools.saveObjectToFile( "graphics/levels/w00-l02/level.dat", new Level_World00_Level02().ldat );

         Tools.saveObjectToFile( "graphics/levels/w00-l03/level.dat", new Level_World00_Level03().ldat );

         Tools.saveObjectToFile( "graphics/levels/w00-l04/level.dat", new Level_World00_Level04().ldat );

      // World 1
         Tools.saveObjectToFile( "graphics/levels/w01-l01/level.dat", new Level_World01_Level01().ldat );

         Tools.saveObjectToFile( "graphics/levels/w01-l02/level.dat", new Level_World01_Level02().ldat );

         Tools.saveObjectToFile( "graphics/levels/w01-l03/level.dat", new Level_World01_Level03().ldat );

         Tools.saveObjectToFile( "graphics/levels/w01-l04/level.dat", new Level_World01_Level04().ldat );
   }

 /*********************************************************************************/

   private static void savePlayerAnims()
   {

      System.out.println("saving player anims...");

         Tools.saveObjectToFile( "graphics/players/bonk/anims.dat", new PlayerBonk().anim );

         Tools.saveObjectToFile( "graphics/players/goemon/anims.dat",  new PlayerGoemon().anim );

         Tools.saveObjectToFile( "graphics/players/goemon2/anims.dat",  new PlayerGoemon2().anim );

         Tools.saveObjectToFile( "graphics/players/mario/anims.dat",  new PlayerMario().anim );

         Tools.saveObjectToFile( "graphics/players/megaman/anims.dat",  new PlayerMegaman().anim );

         Tools.saveObjectToFile( "graphics/players/yang/anims.dat",  new PlayerYang().anim );

         Tools.saveObjectToFile( "graphics/players/sonic/anims.dat",  new PlayerSonic().anim );

         Tools.saveObjectToFile( "graphics/players/iceclimber/anims.dat",  new PlayerIceClimber().anim );
   }

 /*********************************************************************************/


   private static void saveMonsterAnims()
   {
      System.out.println("saving monster anims...");

         Tools.saveObjectToFile( "graphics/monsters/monstr3/anims.dat", new MonsterHammer().anim );

         Tools.saveObjectToFile( "graphics/monsters/monstr5/anims.dat", new MonsterIceStone().anim );

         Tools.saveObjectToFile( "graphics/monsters/monstr4/anims.dat", new MonsterRobber().anim );

         Tools.saveObjectToFile( "graphics/monsters/monstr2/anims.dat", new MonsterSamourai().anim );

         Tools.saveObjectToFile( "graphics/monsters/monstr1/anims.dat", new MonsterSnowBall().anim );

         Tools.saveObjectToFile( "graphics/monsters/monstr6/anims.dat", new MonsterStalactite().anim );

         Tools.saveObjectToFile( "graphics/monsters/monstr7/anims.dat", new MonsterRockStone().anim );

         Tools.saveObjectToFile( "graphics/monsters/monstr8/anims.dat", new MonsterHeavyRock().anim );

         Tools.saveObjectToFile( "graphics/monsters/monstr9/anims.dat", new MonsterFallingRock().anim );

         Tools.saveObjectToFile( "graphics/monsters/monstr10/anims.dat", new MonsterRockBall().anim );

   }

 /*********************************************************************************/

}