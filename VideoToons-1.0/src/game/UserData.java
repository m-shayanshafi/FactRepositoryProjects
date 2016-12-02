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

public class UserData implements GameDefinitions
{

 /**** NETWORK INFO ****/

  // Server Name
     public String server_name;

  // Game mode : ONEPLAYER, MULTIPLAYER or LASTMANSTANDING
     public byte game_mode;

  // Is this computer the server's host ?
     public boolean is_server_host = false;

 /**** ABOUT PLAYERS ****/

  // Player info
     public String player_name[] = new String[NBMAX_PLAYERS];

  // Players sprite name
     public String sprite_name[] = new String[NBMAX_PLAYERS];

  // Players score
     public int player_score[] = new int[NBMAX_PLAYERS];

  // Nb of players that registered for the current level
     public byte nb_players;

  // represents the position of the host in the different arrays, its name & sprite name
     public byte host_player_ID;
     public String host_player_name;
     public String host_sprite_name;
     public int host_player_score;


 /**** LEVEL OPTIONS ****/

  // Level name
     public String level_name;

  // Number of seconds before the screens goes up.
     public short seconds_before_up;

  // Go to next level automatically
     public boolean continue_after_this_level;


 /**** CONSTRUCTOR ****/
 
     public UserData() {
        host_player_name = new String("");
        host_sprite_name = ObjectLibrary.getNextPlayerName();
        level_name = ObjectLibrary.getNextLevelName();
     }


 /**** ERRORS... ****/

  // an error code in case of incorrect parameters in this data structure
     private byte error_code;

  // To reset the error state
     public void resetErrorCode(){
        error_code = ERR_NONE;
     }

  // To declare an error
     public void setErrorCode( byte err_code ) {
        error_code = err_code;
     }

  // To get the current error string... returns null if no error occurred
     public String getErrorString()
     {
     	switch(error_code)
     	{
     	   case ERR_NOMOREROOMS:
     	                  return new String("Server reached.\nError: Sorry, too much players !");
     	   case ERR_RUNSALREADY:
     	                  return new String("Server reached.\nError: Sorry, a game is already running !");
     	   case ERR_UNKNOWNHOST:
     	                  return new String("Unknown Server !");
     	   case ERR_SOCKFAILED:
     	                  return new String("Server reached.\nError: No multiplayer game found !");
     	   case ERR_NONE:
     	   default:
     	                  return new String("");
        }

     }
}