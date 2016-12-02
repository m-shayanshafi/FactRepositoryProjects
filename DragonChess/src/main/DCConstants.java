/*
 * Classname            : DCConstants
 * Author               : Christophe Hertigers <xof@pandora.be>
 * Creation Date        : Friday, October 18 2002, 12:37:17
 * Last Updated         : Fri Dec 13 08:57:28 CET 2002 
 * Description          : Collection of globally used constants
 * GPL disclaimer       :
 *   This program is free software; you can redistribute it and/or modify it
 *   under the terms of the GNU General Public License as published by the
 *   Free Software Foundation; version 2 of the License.
 *   This program is distributed in the hope that it will be useful, but
 *   WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *   or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *   for more details. You should have received a copy of the GNU General
 *   Public License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package main;

/* package import */
import java.lang.*;

/**
 * Collection of globally used constants.
 *
 * @author	Christophe Hertigers
 * @author	Davy Herben
 * @version	021213 
 * 
 */
public class DCConstants {

    /*
     * VARIABLES
     *
     */

    /* CLASS VARIABLES */

	/* Game Status Variables */
	
    /** game status. game is initialising (backend being created) */
    public static final int                 INITIALISING = 0;
    /** game status: game ready to start */
    public static final int					READY = 1;
    /** game status. game is waiting for a piece to be selected */
    public static final int                 SELECTSTARTFIELD = 2;
    /** game status. game is waiting for a targetfield to be selected */
    public static final int                 SELECTTARGETFIELD = 3;
    /** game status. game is busy and performing calculations */
    public static final int                 BUSY = 4;
	/** game status. game is over, this can be undone */
	public static final int					OVER = 6;
	
		
	/* Game Physics Variables */
	
    /** number of boards in this game. */
    public static final int                 BOARDS = 3;
    /** number of Files (columns) on a board */
    public static final int     			FILES = 12;
    /** number of Ranks (rows) on a board */
    public static final int     			RANKS = 8;
    /** is the top board */
    public static final int     			BOARD_TOP = 2;
    /** is the middle board */
    public static final int     			BOARD_MIDDLE = 1;
    /** is the bottom board */
    public static final int     			BOARD_BOTTOM = 0;
    /** number of players in this game. */
    public static final int                 PLAYERS = 2;
    /** Number of {@link backend.DCPiece DCPieces} a player owns */
    public static final int                 NROFDCPIECES = 42;
 

	/* Move Type Variables */

	/** Move Type: A piece is simply moved */
	public static final int					MOVE = 0;
	/** Move Type: A piece captures another piece and takes it's place */
	public static final int					CAPT = 1;
	/** Move Type: A piece captures another piece but does not move */
	public static final int					CAFAR = 2;
	 
	/* Player Type Variables */
	
	/** The GOLD Player in a game */
    public static final int                 PLAYER_GOLD = 0;
    /** The SCARLET Player in a game */
    public static final int                 PLAYER_SCARLET = 1;
	/** Both players */
	public static final int					PLAYER_BOTH = 2;
    /** Designates that none of the players apply */
    public static final int                 PLAYER_NONE = -1;


	/* Connection Type Variables */
		
	/** Connection Type: Local game */
	public static final int					CONN_LOCAL = 300;
	/** Connection Type: Network game, Server */
	public static final int					CONN_SERVER = 301;
	/** Connection Type: Network game, Client */
	public static final int					CONN_CLIENT = 302;
	/** Connection Type: Network game, Spectator */
	public static final int					CONN_SPECTATOR = 303;

	/* Player State Variables */
	/** Player State : player is not registered */
	public static final int					PL_UNREGISTERED = 901;
	/** Player State : player has registered but is now on hold */
	public static final int					PL_HOLD = 902;
	/** Player State : player is registered for the next game */
	public static final int					PL_REGISTERED = 903;
	
	/* Network Exception reasons */
	/** Network Exception : HostName not known */
	public static final int					NW_HOST_NOT_FOUND = 801;
	/** Network Exception : Streams couldn't be created */
	public static final int					NW_STREAMS_NOT_CREATED = 802;
	/** Network Exception : Socket couldn't be opened */
	public static final int					NW_SOCKET_NOT_CREATED = 803;
	/** Network Exception : Server socket couldn't be opened */
	public static final int					NW_SERVERSOCKET_NOT_CREATED = 804;
	/** Network Exception : Error reading input stream */
	public static final int					NW_INPUT_STREAM_ERROR = 805;
	/** Network Exception : Error writing to output stream */
	public static final int					NW_OUTPUT_STREAM_ERROR = 806;
	/** Network Exception : Message to send is not serializable */
	public static final int					NW_NOT_SERIALIZABLE_ERROR = 807;
	
	/* Message Destination Variables */
	
	/** Message Destination: from backend to frontend */
	public static final int					TO_FRONTEND = 200;
	/** Message Destination: from frontend to backend */
	public static final int					TO_BACKEND = 201;
	
	/* Message Subtypes Variables */
	/** Message Subtype: This player has no permission to perform this action */
	public static final int					SUB_INVALID_PLAYER = 600;
	/** Message Subtype: Wrong gameState for this action */
	public static final int					SUB_INVALID_GAMESTATE = 601;
	/** Message Subtype: Invalid location for this action */
	public static final int					SUB_INVALID_LOCATION = 602;
	/** Message Subtype: King would be in check after this move */
	public static final int					SUB_KING_IN_CHECK = 603;
	/** Message Subtype: Piece frozen */
	public static final int					SUB_PIECE_FROZEN = 604;
		 
	/* GameOver conditions */
	/** GameOver Condition : Losing player has resigned */
	public static final int					GAMEOVER_RESIGNED=1;
	/** GameOver Condition : Losing player in CHECKMATE */
	public static final int					GAMEOVER_CHECKMATE=2;
	/** GameOver Condition : Losing player in MATE */
	public static final int					GAMEOVER_MATE=3;
	/** GameOver Condition : Connection of Winning player was broken */
	public static final int					GAMEOVER_CONNECTION_BROKEN = 4;
	
	/* Undo failed conditions */
	/** UndoFailed Condition : history is empty so nothing to undo */
	public static final int					UNDO_HISTORY_EMPTY = 1;
	/** UndoFailed Condition : undo is refused by opponent */
	public static final int					UNDO_REFUSED = 2;
	
	/* Message Types Variables */
	
	/** Message Type: Register a new player */
	public static final int					MSG_REGISTER_PLAYER = 100;
	/** Message Type: Change the Game State */
	public static final int					MSG_GAMESTATE = 101;
	/** Message Type: Player successfully registered */
	public static final int					MSG_REGISTER_SUCCESS = 102;
	/** Message Type: Player registration failed */
	public static final int					MSG_REGISTER_FAILURE = 103;
	/** Message Type: A new player has registered */
	public static final int					MSG_NEW_PLAYER_REGISTERED = 118;
	/** Message Type: Register a spectator */
	public static final	int					MSG_REGISTER_SPECT = 116;
	/** Message Type: Change active player */
	public static final int					MSG_ACTIVE_PLAYER = 104;
	/** Message Type: Piece is selected */
	public static final int					MSG_PIECE_SELECTED = 105;
	/** Message Type: Piece selection has failed */
	public static final int					MSG_PIECE_NOT_SELECTED = 106;
	/** Message Type: Piece deselected */
	public static final int					MSG_PIECE_DESELECTED = 107;
	/** Message Type: Sends list of valid targets for selected piece */
	public static final int					MSG_VALID_TARGETS = 108;
	/** Message Type: Piece moving has failed */
	public static final int					MSG_PIECE_NOT_MOVED = 109;
	/** Message Type: Unregister a player */
	public static final int					MSG_UNREGISTER_PLAYER = 110;
	/** Message Type: Start a new game */
	public static final int					MSG_START_GAME = 111;
	/** Message Type: Select a piece */
	public static final int					MSG_SELECT_PIECE = 112;
	/** Message Type: Move a piece (can be a capture or a deselect!!) */
	public static final int					MSG_MOVE_PIECE = 113;
	/** Message Type: Resign the current game */
	public static final int					MSG_RESIGN_GAME = 114;
	/** Message Type: Undo the last move */
	public static final int					MSG_REQUEST_UNDO = 115;
	/** Message Type: Set position of all pieces */
	public static final int					MSG_LOAD_DUMP = 117;
	/** Message Type: Set player info */
	public static final int					MSG_SET_PLAYER_INFO = 119;
	/** Message Type: Piece has been moved */
	public static final int					MSG_PIECE_MOVED = 120;
	/** Message Type: Piece has been promoted */
	public static final int					MSG_PIECE_PROMOTED = 121;
	/** Message Type: Set freeze status */
	public static final int					MSG_FREEZE = 122;
	/** Message Type: Game is in check */
	public static final int					MSG_CHECK = 123;
	/** Message Type: Game over */
	public static final int					MSG_GAMEOVER = 124;
	/** Message Type: Add history string */
	public static final int					MSG_ADD_HISTORY = 125;
	/** Message Type: Piece has been demoted */
	public static final int					MSG_PIECE_DEMOTED = 126;
	/** Message Type: Piece has been restored to board */
	public static final int					MSG_PIECE_RESTORED = 127;
	/** Message Type: Move has been undone */
	public static final int					MSG_MOVE_UNDONE = 128;
	/** Message Type: Move has not been undone */
	public static final int					MSG_MOVE_NOT_UNDONE = 129;
	/** Message Type: Remove history string */
	public static final int					MSG_REMOVE_HISTORY = 130;
	/** Message Type: Player unregistered */
	public static final int					MSG_PLAYER_UNREGISTERED = 131;
	/** Message Type: Game has started */
	public static final int					MSG_GAME_STARTED = 132;
	/** Message Type: Chat Message send*/
	public static final int					MSG_PLAYER_CHAT_SEND = 133;
	/** Message Type: Chat Message receive */
	public static final int					MSG_PLAYER_CHAT_RECV = 134;
	

    /*
     * CONSTRUCTORS
     *
     */
	public DCConstants() {

		/* Not much to do */

	}

	/*
	 * FUNCTIONS
	 *
	 */

	/**
	 * Translates the typeChar of a piece to the full name.
	 *
	 * @param	typeChar	the typeChar of the piece, eg 'R'
	 * @return	the full name of the piece, eg "Dragon"
	 */
	public static String pieceName(char typeChar) {

		String name;
			
        switch(typeChar) {
        case 'R':
	        name = "Dragon";
	        break;
	    case 'G':
	        name = "Griffin";
	        break;
	    case 'S':
	        name = "Sylph";
	        break;
	    case 'O':
	        name = "Oliphant";
	        break;
	    case 'C':
	        name = "Cleric";
	        break;
	    case 'H':
	        name = "Hero";
	        break;
	    case 'K':
	        name = "King";
	        break;
	    case 'M':
	        name = "Mage";
	        break;
	    case 'P':
	        name = "Paladin";
	        break;
	    case 'T':
	        name = "Thief";
	        break;
	    case 'U':
	   	     name = "Unicorn";
   		     break;
	    case 'W':
	        name = "Warrior";
	        break;
	    case 'B':
	        name = "Basilisk";
	        break;
	    case 'D':
	        name = "Dwarf";
	        break;
	    case 'E':
	        name = "Elemental";
	        break;
	    default:
	        name = "Unknown";
    }

        return name;

	}
}
