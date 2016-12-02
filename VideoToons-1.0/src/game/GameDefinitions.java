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

public interface GameDefinitions
{

/******************************************************************************/
/**************************** GENERAL DEFINITIONS *****************************/
/******************************************************************************/

 // Screen size & dimensions
    public static final Dimension SCREENSIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int SCREEN_WIDTH = SCREENSIZE.width;
    public static final int SCREEN_HEIGHT = SCREENSIZE.height;

 // Screen top-left corner position
    public static final int XO = (SCREEN_WIDTH-640)/2;
    public static final int YO = (SCREEN_HEIGHT-480)/2;

 // Some Basic Colors
    public static final Color C_WHITE = new Color(255,255,255);
    public static final Color C_BLACK = new Color(0,0,0);
    public static final Color C_BLUE = new Color(127,127,255);

 // Some basic words
    public static final byte NONE  = 0;
    public static final byte LEFT  = 0;
    public static final byte RIGHT = 1;
    public static final boolean NOWAIT = false;
    public static final boolean WAIT = true;
    public static final boolean DONT_TELL_SERVER = false;
    public static final boolean TELL_SERVER = true;


 // Max number of letters in a name (<127)
    public static final byte MAXNAMELETTERS = 20;


 // Debug Mode
    public static final boolean DEBUG_MODE = false;


/******************************************************************************/
/************************** GAME ARCHITECTURE *********************************/
/******************************************************************************/

 //*** Environment Sprite definitions (in pixels)

  // Stone dimension
    public static final int STONE_HEIGHT = 15;
    public static final int STONE_WIDTH  = 20;

  // Mountain Tunnels defaults
    public static final int TUNNELS_HEIGHT = 100;
    public static final int TUNNELS_WIDTH  = 120;

 // Stone types
    public static final byte STONE_BROKEN = 0;   // air...
    public static final byte STONE_SIMPLE = 1;   // normal
    public static final byte STONE_LEFT   = 2;   // moving the player to the left
    public static final byte STONE_RIGHT  = 3;   // moving the player to the right
    public static final byte STONE_HARD   = 4;   // unbreakable
    public static final byte STONE_HLEFT  = 5;   // unbreakable, moving the player to the left
    public static final byte STONE_HRIGHT = 6;   // unbreakable, moving the player to the right
    public static final byte STONE_ACID   = 7;   // hurts the player if he walks on it


  // Number of stages where there are clouds, tunnels, ...
    public static final byte CLOUD_NUMBER  = 11;
    public static final byte TUNNEL_NUMBER =  8;
    public static final byte STAGE_NUMBER  = 11;
    public static final byte STONE_NUMBER  = 32;
    public static final byte LINE_NUMBER   =  2;
    public static final byte BLOCK_NUMBER  =  8;
    public static final byte MONSTER_NUMBER=  9;

  // Speeds
    public static final byte PLAYER_SPEED  = 5;
    public static final byte STONE_SPEED   = 4;
    public static final byte MONSTER_SPEED = 4;
    

  // Stages ( top y of the stone lines )
    public static final short STAGE_HEIGHT[] = { 1385, 1270, 1155, 1040, 925, 810, 695, 580, 465, 350, 235, 120, 0 };
    public static final int LEVEL_WIDTH = 640;
    public static final int LEVEL_HEIGHT = 1405;


/******************************* GAME STATES **********************************/

    public static final byte GM_NONE    =  0;     // no games
    public static final byte GM_READY   = 11;     // players can join
    public static final byte GM_ACTIVE  = 22;     // game running, no new players allowed
    public static final byte GM_PAUSE   = 33;     // game paused
    public static final byte GM_OVER    = 44;     // game has ended
    public static final byte GM_KILLED  = 55;     // game killed. Clients must leave.

/******************************************************************************/
/********************************* SPRITES ************************************/
/******************************************************************************/

 // Codes to identify sprites types
    public static final byte SP_STONE     = 11;
    public static final byte SP_TUNNEL    = 22;
    public static final byte SP_CLOUD     = 33;
    public static final byte SP_OBSTACLE  = 44;
    public static final byte SP_PLAYER    = 55;
    public static final byte SP_MONSTER   = 66;
    public static final byte SP_BLOCK     = 77;


/************************** BASIC SPRITE STATES *******************************/

  // Possible states of the life cycle of an animated sprite.
     public static final byte BS_INACTIVE   = 0;
     public static final byte BS_ACTIVE     = 1;
     public static final byte BS_HURT       = 2;    // (for players & monsters)
     public static final byte BS_DEAD       = 3;    // (for players)
     public static final byte BS_INVINCIBLE = 4;    // (for players & monsters)
     public static final byte BS_GAMEOVER   = 5;    // (for players)
     public static final byte BS_WINNER     = 6;    // (for players)
     public static final byte BS_HITRIGHT   = 7;    // sprite dangerous on the right
     public static final byte BS_HITLEFT    = 8;    // sprite dangerous on the left
     public static final byte BS_DANGEROUS  = 9;    // sprite constantly dangerous (monster)


/*****************************  MONSTERS  *************************************/

  // Monsters ID
     public static final byte MT_NONE       = 0;
     public static final byte MT_SNOWBALL   = 1;
     public static final byte MT_SAMOURAI   = 2;
     public static final byte MT_HAMMER     = 3;
     public static final byte MT_ROBBER     = 4;
     public static final byte MT_ICESTONE   = 5;
     public static final byte MT_STALACTITE = 6;
     public static final byte MT_ROCKSTONE  = 7;
     public static final byte MT_HEAVYROCK  = 8;
     public static final byte MT_FALLINGROCK= 9;
     public static final byte MT_ROCKBALL   = 10;

  // Monster behaviour
     public static final byte MB_HITTING       = 1;
     public static final byte MB_FALLING       = 2;
     public static final byte MB_STONE         = 3;
     public static final byte MB_ROLLING       = 4;
     public static final byte MB_LEVELUP       = 5;
     public static final byte MB_HOLEFILLER    = 6;
     public static final byte MB_FLYING        = 7;
     public static final byte MB_JUMPING       = 8;
     public static final byte MB_HEAVYSTONE    = 9;


/******************************************************************************/
/************************** NETWORK DEFINITIONS *******************************/
/******************************************************************************/

    public static final byte NBMAX_PLAYERS = 4;
    public static final int  GAME_PORT = 45617;

 // Game modes
    public static final byte ONEPLAYER       = 1;
    public static final byte MULTIPLAYER     = 2;
    public static final byte LASTMANSTANDING = 3;

 // Network messages types
    public static final byte MSG_WELCOME     = 1;
    public static final byte MSG_THANKYOU    = 2;
    public static final byte MSG_GETREADY    = 3;
    public static final byte MSG_GOPLAY      = 4;
    public static final byte MSG_ACK         = 5;    
    public static final byte MSG_CLIENTDATA  = 6;
    public static final byte MSG_SERVERDATA  = 7;
    public static final byte MSG_MONSTERDATA = 8;
    public static final byte MSG_KILLALL     = 9;
    public static final byte MSG_PLAYERQUITS = 10;
    public static final byte MSG_BROKENSTONE = 11;
    public static final byte MSG_LEVEL_UP    = 12;
    public static final byte MSG_NEWLEVEL    = 13;
    public static final byte MSG_GAMEOVER    = 14;
    public static final byte MSG_PAUSE       = 15;
    public static final byte MSG_PAUSEEND    = 16;
    public static final byte MSG_ERROR       = 17;
    public static final byte MSG_KILL        = 18;
    public static final byte MSG_SCORE       = 19;
    public static final byte MSG_ALLSCORES   = 20;
    public static final byte MSG_FILLEDHOLE  = 21;
    public static final byte MSG_CLOUDSDATA  = 22;
    public static final byte MSG_SOUND       = 23;
    public static final byte MSG_SOUNDDATA   = 24;
    public static final byte MSG_END         = 66;

 // Error codes
    public static final byte ERR_NONE        =  0;   // no errors
    public static final byte ERR_NOMOREROOMS =  2;   // max client number reached
    public static final byte ERR_RUNSALREADY =  4;   // game already running
    public static final byte ERR_BADMESSAGE  =  8;   // message not expected
    public static final byte ERR_UNKNOWNHOST = 16;   // unknown server name
    public static final byte ERR_SOCKFAILED  = 32;   // IO error... failed to init socket comm

/******************************************************************************/
/********************************** SCORE *************************************/
/******************************************************************************/

    public static final byte STONE_POINTS = 1;
    public static final byte LEVEL_UP_POINTS = 10;
    public static final int  WINNER_POINTS = 250;
    public static final byte MONSTER_POINTS = 1;
    public static final byte DEATH_POINTS = -10;
    public static final byte COWARD_POINTS = -25;
}