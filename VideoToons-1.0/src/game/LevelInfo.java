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


public class LevelInfo
{
  private String world_name;
  private String level_name;
  private String difficulty;
  private String description;

   public LevelInfo(String path_name)
   {
      world_name = Tools.readConfigFile("graphics/levels/"+path_name+"/level.nfo", "WORLD_NAME");
      level_name = Tools.readConfigFile("graphics/levels/"+path_name+"/level.nfo", "LEVEL_NAME");
      difficulty = Tools.readConfigFile("graphics/levels/"+path_name+"/level.nfo", "DIFFICULTY");
      description = Tools.readConfigFile("graphics/levels/"+path_name+"/level.nfo", "DESCRIPTION");

      if(world_name==null)
         world_name = new String("???");

      if(level_name==null)
         level_name = new String("???");

      if(difficulty==null)
         difficulty = new String("???");

      if(description==null)
         description = new String("???");
   }


   public String getWorldName() {
      return world_name;
   }

   public String getLevelName() {
      return level_name;
   }

   public String getDifficulty() {
      return difficulty;
   }

   public String getDescription() {
      return description;
   }
}