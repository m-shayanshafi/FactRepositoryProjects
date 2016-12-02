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

public class ObjectLibrary
{

 /* current selected level */
   static private int current_level=-1;

 /* level list */
   static private String levels[];

 /* current selected player */
   static private int current_player=-1;

 /* player list */
   static private String players[];

 /* monster list */
   static private String monsters[];


//////////////////////////// LOADING LISTS ////////////////////////////////

 /** Loads the level list & player sprite list
  */

  static public void loadLibraryData()
  {
      levels = Tools.getPathList( "graphics/levels" );

        if(levels==null)
           errorDataMissing( "graphics/levels" );

      Tools.sort(levels);

      players = Tools.getPathList( "graphics/players" );

        if(players==null)
           errorDataMissing( "graphics/players" );

      Tools.sort(players);

      monsters = Tools.getPathList( "graphics/monsters" );

        if(monsters==null)
           errorDataMissing( "graphics/monsters" );
  }

///////////////////////////////////////////////////////////////////////////

 /** Data Missing... we quit with an error message...
  */
  
  static private void errorDataMissing( String filename_missing )
  {
     System.out.println("\nERROR: DATA MISSING !!!\n");
     System.out.println("The following path could not be found : "+filename_missing);
     System.out.println("If you were attempting to launch a multiplayer game");
     System.out.println("make sure you have all the levels & player sprites.");

     System.exit(0);
  }

/////////////////////////////// LEVELS ////////////////////////////////////

 /** if light_version == true we return a level that only contains the
  *  level name and its difficulty.
  */

  static public LevelInfo getLevelInfo( String level_name )
  {
     if( Tools.findStringIndex(levels,level_name)==-1 )
  	errorDataMissing( "graphics/levels/"+level_name );

     return new LevelInfo(level_name);
  }


/////////////////////////////// LEVELS ////////////////////////////////////

 /** if light_version == true we return a level that only contains the
  *  level name and its difficulty.
  */

  static public LevelData getLevelByName(String level_name )
  {
      if( Tools.findStringIndex(levels,level_name)==-1 )
         errorDataMissing( "graphics/levels/"+level_name );

      return (LevelData) Tools.loadObjectFromFile( "graphics/levels/"+level_name+"/level.dat" );
  }

  static public String getNextLevelName(){
      current_level = (current_level+1)%levels.length;
      return levels[current_level];
  }

  static public String getPrevLevelName(){
      current_level = (current_level-1)%levels.length;

      if(current_level<0) current_level += levels.length;

      return levels[current_level];
  }

/////////////////////////////// PLAYERS /////////////////////////////////

  static public AnimLibrary getPlayerSpriteByName(String sprite_name)
  {
      if( Tools.findStringIndex(players,sprite_name)==-1 )
         errorDataMissing( "graphics/players/"+sprite_name );

      return (AnimLibrary) Tools.loadObjectFromFile( "graphics/players/"+sprite_name+"/anims.dat" );
  }

  static public String getNextPlayerName(){
      current_player = (current_player+1)%players.length;
      return players[current_player];
  }

  static public String getPrevPlayerName(){
      current_player = (current_player-1)%players.length;

      if(current_player<0) current_player += players.length;

      return players[current_player];
  }

/////////////////////////////// MONSTERS /////////////////////////////////

  static public Monster getMonsterByNumber( byte  id,
                                            ServerLevelProcess level,
                                            byte stone, byte stage, byte speed, byte extra_param )
  {
      if( Tools.findStringIndex(monsters,"monstr"+id)==-1 )
         errorDataMissing( "graphics/monsters/monstr"+id );

      AnimMonsterLibrary anim = (AnimMonsterLibrary)
               Tools.loadObjectFromFile( "graphics/monsters/monstr"+id+"/anims.dat" );

      switch(anim.behaviour)
      {
      	case GameDefinitions.MB_HITTING :
      	       return new HittingMonster( (AnimLibrary) anim,level,stone,stage,speed);

      	case GameDefinitions.MB_FALLING :
      	       return new FallingMonster( (AnimLibrary) anim,level,stone,stage,speed);;

      	case GameDefinitions.MB_STONE :
      	       return new StoneMonster( (AnimLibrary) anim,level,stone,stage,speed);

      	case GameDefinitions.MB_ROLLING :
      	       return new RollingMonster( (AnimLibrary) anim,level,stone,stage,speed);

      	case GameDefinitions.MB_LEVELUP :
      	       return new LevelUpMonster( (AnimLibrary) anim,level,stone,stage,speed);

      	case GameDefinitions.MB_HOLEFILLER :
      	       return new HoleFillerMonster( (AnimLibrary) anim,level,stone,stage,speed);

      	case GameDefinitions.MB_FLYING :
      	       return null;

      	case GameDefinitions.MB_JUMPING :
      	       return null;

      	case GameDefinitions.MB_HEAVYSTONE :
      	       return new HeavyStoneMonster( (AnimLibrary) anim,level,extra_param,stage,speed);
        default:
               return null;
      }
  }


  static public AnimLibrary getMonsterSpriteByNumber( byte  id )
  {
      if( Tools.findStringIndex(monsters,"monstr"+id)==-1 )
         errorDataMissing( "graphics/monsters/monstr"+id );

      return (AnimLibrary) Tools.loadObjectFromFile( "graphics/monsters/monstr"+id+"/anims.dat" );
  }

 ///////////////////////////////////////////////////////////////////////////////
}
