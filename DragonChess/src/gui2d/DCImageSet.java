/*
 * Classname			: DCImageSet
 * Author				: Davy Herben <theblackunicorn@softhome.net>
 * Creation Date		: Sun Feb 24 20:58:10 CET 2002 
 * Last Updated			: Friday, October 18 2002, 13:19:37
 * Description			: A set of 14 Dragonchess Piece images
 * GPL disclaimer		:
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

package gui2d;

/* package import */
import java.lang.*;
import java.io.*;
import java.awt.*;
import main.*;

/**
 * This class is represents a set of Icons for Dragonchess
 * It contains an array of 15 Images of pieces, in the following order
 * Sylph, Griffin, Dragon, Oliphant, Unicorn, Hero, Thief, Cleric,
 * Mage, King, Paladin, Warrior, Basilisk, Elemental, Dwarf.
 * @author				Davy Herben
 * @author				Christophe Hertigers
 * @version				Friday, October 18 2002, 13:19:37
 */
public class DCImageSet {
	/*
	 * VARIABLES
	 */

	/* CLASS VARIABLES */

	/**
	 * The number of pieces per player
	 */
	public static final int	NROFPIECES				= 15;

	/**
	 * Sylph piece
	 */
	public static final int	KEY_SYLPH  				= 0;
	/**
	 * Griffin piece
	 */
	public static final int	KEY_GRIFFIN				= 1;
	/**
	 * Dragon piece
	 */
	public static final int	KEY_DRAGON				= 2;
	/**
	 * Oliphant piece
	 */
	public static final int	KEY_OLIPHANT			= 3;
	/**
	 * Unicorn piece
	 */
	public static final int	KEY_UNICORN				= 4;
	/**
	 * Hero piece
	 */
	public static final int	KEY_HERO				= 5;
	/**
	 * Thief piece
	 */
	public static final int	KEY_THIEF				= 6;
	/**
	 * Cleric piece
	 */
	public static final int	KEY_CLERIC				= 7;
	/**
	 * Mage piece
	 */
	public static final int	KEY_MAGE				= 8;
	/**
	 * King piece
	 */
	public static final int	KEY_KING				= 9;
	/**
	 * Paladin piece
	 */
	public static final int	KEY_PALADIN				= 10;
	/**
	 * Warrior piece
	 */
	public static final int	KEY_WARRIOR				= 11;
	/**
	 * Basilisk piece
	 */
	public static final int	KEY_BASILISK			= 12;
	/**
	 * Elemental piece
	 */
	public static final int	KEY_ELEMENTAL			= 13;
	/**
	 * Dwarf piece
	 */
	public static final int	KEY_DWARF				= 14;

	/**
	 * Gold Sylph Piece
	 */
	public static final int	KEY_GOLD_SYLPH 			= 0;
	/**
	 * Gold Griffin Piece
	 */
	public static final int	KEY_GOLD_GRIFFIN		= 1;
	/**
	 * Gold Dragon Piece
	 */
	public static final int	KEY_GOLD_DRAGON			= 2;
	/**
	 * Gold Oliphant Piece
	 */
	public static final int	KEY_GOLD_OLIPHANT		= 3;
	/**
	 * Gold Unicorn Piece
	 */
	public static final int	KEY_GOLD_UNICORN		= 4;
	/**
	 * Gold Hero Piece
	 */
	public static final int	KEY_GOLD_HERO			= 5;
	/**
	 * Gold Thief Piece
	 */
	public static final int	KEY_GOLD_THIEF			= 6;
	/**
	 * Gold Cleric Piece
	 */
	public static final int	KEY_GOLD_CLERIC			= 7;
	/**
	 * Gold Mage Piece
	 */
	public static final int	KEY_GOLD_MAGE			= 8;
	/**
	 * Gold King Piece
	 */
	public static final int	KEY_GOLD_KING			= 9;
	/**
	 * Gold Paladin Piece
	 */
	public static final int	KEY_GOLD_PALADIN		= 10;
	/**
	 * Gold Warrior Piece
	 */
	public static final int	KEY_GOLD_WARRIOR		= 11;
	/**
	 * Gold Basilisk Piece
	 */
	public static final int	KEY_GOLD_BASILISK		= 12;
	/**
	 * Gold Elemental Piece
	 */
	public static final int	KEY_GOLD_ELEMENTAL		= 13;
	/**
	 * Gold Dwarf Piece
	 */
	public static final int	KEY_GOLD_DWARF			= 14;


	
	/**
	 * Scarlet Sylph Piece
	 */
	public static final int	KEY_SCARLET_SYLPH 		= 15;
	/**
	 * Scarlet Griffin Piece
	 */
	public static final int	KEY_SCARLET_GRIFFIN		= 16;
	/**
	 * Scarlet Dragon Piece
	 */
	public static final int	KEY_SCARLET_DRAGON		= 17;
	/**
	 * Scarlet Oliphant Piece
	 */
	public static final int	KEY_SCARLET_OLIPHANT	= 18;
	/**
	 * Scarlet Unicorn Piece
	 */
	public static final int	KEY_SCARLET_UNICORN		= 19;
	/**
	 * Scarlet Hero Piece
	 */
	public static final int	KEY_SCARLET_HERO		= 20;
	/**
	 * Scarlet Thief Piece
	 */
	public static final int	KEY_SCARLET_THIEF		= 21;
	/**
	 * Scarlet Cleric Piece
	 */
	public static final int	KEY_SCARLET_CLERIC		= 22;
	/**
	 * Scarlet Mage Piece
	 */
	public static final int	KEY_SCARLET_MAGE		= 23;
	/**
	 * Scarlet King Piece
	 */
	public static final int	KEY_SCARLET_KING		= 24;
	/**
	 * Scarlet Paladin Piece
	 */
	public static final int	KEY_SCARLET_PALADIN		= 25;
	/**
	 * Scarlet Warrior Piece
	 */
	public static final int	KEY_SCARLET_WARRIOR		= 26;
	/**
	 * Scarlet Basilisk Piece
	 */
	public static final int	KEY_SCARLET_BASILISK	= 27;
	/**
	 * Scarlet Elemental Piece
	 */
	public static final int	KEY_SCARLET_ELEMENTAL	= 28;
	/**
	 * Scarlet Dwarf Piece
	 */
	public static final int	KEY_SCARLET_DWARF		= 29;
	


	 
	private 	static final int	KEY_EOF					= -1;
	private		static final int	KEY_NOT_WORD			= -2;
	private		static final int	KEY_NOT_KNOWN			= -3;
	
	/* INSTANCE VARIABLES */

	private 	DCImage2D[]		imageSet = new DCImage2D[DCConstants.PLAYERS * 
																	NROFPIECES];
	
	private 	StreamTokenizer	st;
	
	private 	Color			goldColor			=	new Color(255, 246, 97);
	private 	Color			goldOutlineColor	=	Color.black;
	private 	Color			scarletColor		=	new Color(221, 23, 23);
	private 	Color			scarletOutlineColor	=	Color.black;
	
	/*
	 * INNER CLASSES
	 */

	/*
	 * CONSTRUCTORS
	 */
	
	/**
	 * Creates an empty imageSet
	 */
	public DCImageSet() {
	}
	
	/**
	 * Creates an imageSet with the images specified in the given file
	 *
	 * @param 			fileString	the file containing the ImageSet information
	 * @exception		IOException
	 */
	public DCImageSet(String fileString) throws IOException,
												DCSvgParserException {
		
		/* load and parse ImageSet file */
		this.loadDCImageSet(fileString);
		 
	}
	
	
	/*
	 * METHODS
	 */
	
	/**
	 * Sets the image at the specified index in the array
	 * @param			img			the image to put into the array
	 * @param			player		the player to set the image for
	 * @param			index 		position of the piece
	 */
	public void setImage(DCImage2D img, int player, int index) {
		
		/* if index > NROFPIECES, do nothing */
		if (index > NROFPIECES - 1 || index < 0) {
			/* do nothing. could throw exception here */
		} else {
			imageSet[(player * NROFPIECES) + index] = img;
		}
	}

	/**
	 * Retrieves the image at the specified index
	 * @return						the requested DCImage2D
	 * @param			player		the player to get the image for
	 * @param			index		position of the piece in the array
	 * 								with player offset
	 */
	public DCImage2D getImage(int player, int index) {
		if (index > NROFPIECES - 1 
				|| index < 0
				|| player > DCConstants.PLAYERS -1
				|| player < 0
				) {
			/* do nothing. should throw exception here */
			System.out.println("ERROR--index:" + index);
			System.out.println("ERROR--moet < dan:" + (NROFPIECES-1));
			System.out.println("ERROR--en   > dan: 0");
			System.out.println("ERROR--player:" + player);
			System.out.println("ERROR--moet < dan:" + (DCConstants.PLAYERS-1));
			System.out.println("ERROR--en   > dan: 0");
			return null;
		} else {
			return imageSet[(player * NROFPIECES) + index];
		}
	}

	/**
	 * Retrieves the image at the specified player dependent index
	 * @return						the requested DCImage2D
	 * @param			imageInt	position of the piece in the array
	 *								without player offset
	 */
	public DCImage2D getImage(int imageInt) {
		if (imageInt > (NROFPIECES * DCConstants.PLAYERS -1)
			|| imageInt < 0) {
			return null;
		} else {
			return imageSet[imageInt];
		}
	}

    /**
     * Makes a new DCImage2D to put on the DCboard.
     * @return  the new piece
     *
     */
    public DCImage2D getImage(int player, char typeChar) {
        DCImage2D myDCPiece;

        switch(typeChar) {
        case 'R':
	        myDCPiece = getImage(player, KEY_GOLD_DRAGON);
	        break;
	    case 'G':
	        myDCPiece = getImage(player, KEY_GOLD_GRIFFIN);
	        break;
	    case 'S':
	        myDCPiece = getImage(player, KEY_GOLD_SYLPH);
	        break;
	    case 'O':
	        myDCPiece = getImage(player, KEY_GOLD_OLIPHANT);
	        break;
	    case 'C':
	        myDCPiece = getImage(player, KEY_GOLD_CLERIC);
	        break;
	    case 'H':
	        myDCPiece = getImage(player, KEY_GOLD_HERO);
	        break;
	    case 'K':
	        myDCPiece = getImage(player, KEY_GOLD_KING);
	        break;
	    case 'M':
	        myDCPiece = getImage(player, KEY_GOLD_MAGE);
	        break;
	    case 'P':
	        myDCPiece = getImage(player, KEY_GOLD_PALADIN);
	        break;
	    case 'T':
	        myDCPiece = getImage(player, KEY_GOLD_THIEF);
	        break;
	    case 'U':
	        myDCPiece = getImage(player, KEY_GOLD_UNICORN);
	        break;
	    case 'W':
    	    myDCPiece = getImage(player, KEY_GOLD_WARRIOR);
			break;
	    case 'B':
        	myDCPiece = getImage(player, KEY_GOLD_BASILISK);
        	break;
	    case 'D':
	        myDCPiece = getImage(player, KEY_GOLD_DWARF);
	        break;
	    case 'E':
	        myDCPiece = getImage(player, KEY_GOLD_ELEMENTAL);
	        break;
	    default:
			System.out.println("INVALID PIECE!!!");
	        myDCPiece = null;
	    }
	return myDCPiece;
	}
	
	/**
	 * loads and parses a DCImageSet from a file
	 *
	 * @param	fileString	the file to load the ImageSet from
	 * @exception IOException
	 */
	public void loadDCImageSet(String fileString) throws IOException,
														 DCSvgParserException {
	
		int posInt = 0;
		String keyString;
		String valueString;
		
		/* Create a new StreamTokenizer */
		try {
			File setFile = new File(fileString);
			Reader r = new BufferedReader(new FileReader(setFile));
			st = new StreamTokenizer(r);
		} catch (IOException e) {
			throw new IOException("Error trying to open " + fileString + 
				": \n" + e.getMessage());
		}
		
		/* Set StreamTokenizer Characteristics */

		// '/' is a comment char by default, this is not needed (nor wanted) 
		st.ordinaryChar('/');
		// lines beginning with # should be comments */
		st.commentChar('#');
		//underscores should always be seen as wordchars
		st.wordChars('_', '_');
		
		
		do {
			int keyInt = parseKey();
			posInt++;

			switch (keyInt) {
				case KEY_NOT_KNOWN:
				case KEY_NOT_WORD:
					throw new DCSvgParserException("Error: Unknown key " +
						"ecnountered in iconset file, aborting");
				case KEY_EOF:
					/* do nothing */
					break;
				default:
					/* getting here should mean that the Key is a valid piece
					 * number. Check it anyway */
					if (keyInt >= 0 && keyInt < NROFPIECES * 
														DCConstants.PLAYERS) {
						/* read the rest of the line as a single String */
						valueString = parseValue();

						/* parse the file in valueString and store the
						 * resulting DCImage2D in the imageSet
						 */
						
						try {
							DCImage2D img = (new 
									DCSvgParser(valueString)).getDCImage2D();
							imageSet[keyInt] = img;

						} catch (Exception e) {
							throw new IOException("IO Error while parsing svg " 
											+ "file " + valueString + ": \n" 
											+ e.getMessage());
						}
						
					}
			}
		} while (st.ttype != StreamTokenizer.TT_EOF);
	}

	private int parseKey() throws IOException {
		String keyString;

		
		/* check if we're not at the end of the file */
		if (st.nextToken() == StreamTokenizer.TT_EOF) {
			return KEY_EOF;
		} 

		/* if not, check if the parsed token is a word */
		if (st.ttype != StreamTokenizer.TT_WORD) {
			return KEY_NOT_WORD;
		}

		/* if it is a word, fill keyString */
		keyString = st.sval;
		
		if (keyString.equalsIgnoreCase("gold_sylph")) {
			return KEY_GOLD_SYLPH;
		} else if (keyString.equalsIgnoreCase("gold_griffin")) {
			return KEY_GOLD_GRIFFIN;
		} else if (keyString.equalsIgnoreCase("gold_dragon")) {
			return KEY_GOLD_DRAGON;
		} else if (keyString.equalsIgnoreCase("gold_oliphant")) {
			return KEY_GOLD_OLIPHANT;
		} else if (keyString.equalsIgnoreCase("gold_unicorn")) {
			return KEY_GOLD_UNICORN;
		} else if (keyString.equalsIgnoreCase("gold_thief")) {
			return KEY_GOLD_THIEF;
		} else if (keyString.equalsIgnoreCase("gold_cleric")) {
			return KEY_GOLD_CLERIC;
		} else if (keyString.equalsIgnoreCase("gold_hero")) {
			return KEY_GOLD_HERO;
		} else if (keyString.equalsIgnoreCase("gold_mage")) {
			return KEY_GOLD_MAGE;
		} else if (keyString.equalsIgnoreCase("gold_king")) {
			return KEY_GOLD_KING;
		} else if (keyString.equalsIgnoreCase("gold_paladin")) {
			return KEY_GOLD_PALADIN;
		} else if (keyString.equalsIgnoreCase("gold_warrior")) {
			return KEY_GOLD_WARRIOR;
		} else if (keyString.equalsIgnoreCase("gold_basilisk")) {
			return KEY_GOLD_BASILISK;
		} else if (keyString.equalsIgnoreCase("gold_elemental")) {
			return KEY_GOLD_ELEMENTAL;
		} else if (keyString.equalsIgnoreCase("gold_dwarf")) {
			return KEY_GOLD_DWARF;
			
		}else if (keyString.equalsIgnoreCase("scarlet_sylph")) {
			return KEY_SCARLET_SYLPH;
		} else if (keyString.equalsIgnoreCase("scarlet_griffin")) {
			return KEY_SCARLET_GRIFFIN;
		} else if (keyString.equalsIgnoreCase("scarlet_dragon")) {
			return KEY_SCARLET_DRAGON;
		} else if (keyString.equalsIgnoreCase("scarlet_oliphant")) {
			return KEY_SCARLET_OLIPHANT;
		} else if (keyString.equalsIgnoreCase("scarlet_unicorn")) {
			return KEY_SCARLET_UNICORN;
		} else if (keyString.equalsIgnoreCase("scarlet_thief")) {
			return KEY_SCARLET_THIEF;
		} else if (keyString.equalsIgnoreCase("scarlet_cleric")) {
			return KEY_SCARLET_CLERIC;
		} else if (keyString.equalsIgnoreCase("scarlet_hero")) {
			return KEY_SCARLET_HERO;
		} else if (keyString.equalsIgnoreCase("scarlet_mage")) {
			return KEY_SCARLET_MAGE;
		} else if (keyString.equalsIgnoreCase("scarlet_king")) {
			return KEY_SCARLET_KING;
		} else if (keyString.equalsIgnoreCase("scarlet_paladin")) {
			return KEY_SCARLET_PALADIN;
		} else if (keyString.equalsIgnoreCase("scarlet_warrior")) {
			return KEY_SCARLET_WARRIOR;
		} else if (keyString.equalsIgnoreCase("scarlet_basilisk")) {
			return KEY_SCARLET_BASILISK;
		} else if (keyString.equalsIgnoreCase("scarlet_elemental")) {
			return KEY_SCARLET_ELEMENTAL;
		} else if (keyString.equalsIgnoreCase("scarlet_dwarf")) {
			return KEY_SCARLET_DWARF;
		} else {
			return KEY_NOT_KNOWN;
		}
	}
		
	
	private String parseValue() throws IOException, DCSvgParserException {

		/* set parser wordchars */
		st.ordinaryChar('.');
		st.wordChars(32, 255);
	
		String valueString = "";
				
		if (st.nextToken() == StreamTokenizer.TT_WORD) {
			valueString = st.sval;
		} else {
			throw new DCSvgParserException("Error : a token in this file is " +
					"not a word, aborting.");
		}
		return valueString;
	}

	/**
	 * Sets the outline and fill Color for the pieces of the specified player
	 * @param			player			number of the player
	 * @param			fillColor		the fill Color
	 * @param			outlineColor	the outline Color
	 */
	public void	setColors(int player, Color fillColor, Color outlineColor) {
		if (player == DCConstants.PLAYER_GOLD) {
			goldColor = fillColor;
			goldOutlineColor = outlineColor;
			for (int i = 0; i < NROFPIECES; i++) {
				imageSet[i].setFillColor(fillColor);
				imageSet[i].setOutlineColor(outlineColor);
			}
		} else if (player == DCConstants.PLAYER_SCARLET) {
			scarletColor = fillColor;
			scarletOutlineColor = outlineColor;
			for (int i = NROFPIECES; i< NROFPIECES * DCConstants.PLAYERS; i++) {
				imageSet[i].setFillColor(fillColor);
				imageSet[i].setOutlineColor(outlineColor);
			}
		}
	}

	/**
	 * Sets the fill Color for the pieces of the specified player
	 * @param			player			number of the player
	 * @param			fillColor		the fill Color
	 */
	public void	setFillColor(int player, Color fillColor) {
		if (player == DCConstants.PLAYER_GOLD) {
			goldColor = fillColor;
			for (int i = 0; i < NROFPIECES; i++) {
				imageSet[i].setFillColor(fillColor);
			}
		} else if (player == DCConstants.PLAYER_SCARLET) {
			scarletColor = fillColor;
			for (int i = NROFPIECES; i< NROFPIECES * DCConstants.PLAYERS; i++) {
				imageSet[i].setFillColor(fillColor);
			}
		}
	}
	
	/**
	 * Sets the outline and fill Color for the pieces of the specified player
	 * @param			player			number of the player
	 * @param			outlineColor	the outline Color
	 */
	public void	setOutlineColor(int player, Color outlineColor) {
		if (player == DCConstants.PLAYER_GOLD) {
			goldOutlineColor = outlineColor;
			for (int i = 0; i < NROFPIECES; i++) {
				imageSet[i].setOutlineColor(outlineColor);
			}
		} else if (player == DCConstants.PLAYER_SCARLET) {
			scarletOutlineColor = outlineColor;
			for (int i = NROFPIECES; i< NROFPIECES * DCConstants.PLAYERS; i++) {
				imageSet[i].setOutlineColor(outlineColor);
			}
		}
	}

	/**
	 * Sets the bounds for the images
	 * @param 			xDouble		horizontal offset
	 * @param			yDouble		verical offset
	 * @param			width		image width
	 * @param			height		image height
	 */
	public void setBounds(double xDouble, double yDouble, 
						  double width, double height) {
		for (int i = 0; i < NROFPIECES * DCConstants.PLAYERS; i++) {
			imageSet[i].translateToOrigin();
			imageSet[i].scaleTo(width, height);
			imageSet[i].translateTo(xDouble, yDouble);
		}
	}

	/**
	 * sets whether the images should be drawn with anti aliasing
	 * @param			antiAlias	antialias toggle
	 */
	public void setAntiAlias(boolean antiAlias) {
		for (int i = 0; i< NROFPIECES * DCConstants.PLAYERS; i++) {
			imageSet[i].setAntiAlias(antiAlias);
		}
	}
}

/* END OF FILE */
            
            
            
            

