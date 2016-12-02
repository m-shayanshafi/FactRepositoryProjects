/*
 * Classname        : DCOptions
 * Author           : Christophe Hertigers <xof@pandora.be>
 * Creation Date    : Thursday, December 05 2002
 * Last Updated     : Thursday, December 05 2002
 * Description      : 2-Dimensional user interface for DragonChess.
 * GPL disclaimer   :
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
import java.util.Properties;
import java.awt.Rectangle;
import main.*;

/**
 * Class managing the users preferences.
 *
 * @author      Christophe Hertigers
 * @version     Thursday, December 05 2002
 */

public class DCOptions {

    /*
     * VARIABLES
     *
     */

    /* CLASS VARIABLES */    

	/* INSTANCE VARIABLES */
	private String userHome, sysDir;
	private Properties defaultProperties, userProperties;
	private File defaultPrefs, userPrefs;

	
    /* 
     * INNER CLASSES
	 */

    /*
     * CONSTRUCTORS
	 */
	public DCOptions() {

		userHome = System.getProperty("user.home");
		sysDir = System.getProperty("user.dir");
		
		//create defaults
		defaultPrefs = new File(sysDir + File.separator + "defaults.ini");
		defaultProperties = new Properties();
		try {
			FileInputStream in = new FileInputStream(defaultPrefs);
			defaultProperties.load(in);
			in.close(); 
		} catch (FileNotFoundException e) {
			System.err.println("ERROR [DCOptions()] - " + e.getMessage());
		} catch (IOException e) {
			System.err.println("ERROR [DCOptions()] - " + e.getMessage());
		}
			
		// create program properties with default
		userProperties = new Properties(defaultProperties);

		// check if the user has stored his own preferences
		// and load them if they exist, otherwise create
		// the necessary directories and files
		userPrefs = new File(userHome + File.separator + ".dragonchess" +
										File.separator + "options.ini");

		if (!userPrefs.exists()) {
			File dcDir = new File(userHome + File.separator + ".dragonchess");
			if (!dcDir.exists()) {
				dcDir.mkdir();
			}

			try {
				userPrefs.createNewFile();
			} catch (IOException e) {
				System.err.println("ERROR [DCOptions()] - " + e.getMessage());
			}
			
		} else {
			try {
				FileInputStream in = new FileInputStream(userPrefs);
				userProperties.load(in);
				in.close();		
			} catch (FileNotFoundException e) {
				System.err.println("ERROR [DCOptions()] - " + e.getMessage());
			} catch (IOException e) {
				System.err.println("ERROR [DCOptions()] - " + e.getMessage());
			}
		} 
		
		/* DEBUG
		userProperties.list(System.out); */
	
	}
		
    /*
     * METHODS
     */

	/**
	 * Saves the user preferences in /user_home/.dragonchess/options.ini 
	 *
	 */
	public void save() {
		try {
			FileOutputStream out = new FileOutputStream(userPrefs);
			userProperties.store(out, "DragonChess User Preferences File -- " + 
						 					"Do not manually edit this!");
			out.close();
		} catch (FileNotFoundException e) {
			System.err.println("ERROR [DCOptions.save()] - " + e.getMessage());
		} catch (IOException e) {
			System.err.println("ERROR [DCOptions.save()] - " + e.getMessage());
		}
	}

	/**
	 * Gets default username.
	 * @return	the default username
	 * 
	 */
	public String getDefaultUsername() {
		return userProperties.getProperty("user.defaultname");
	}

	/**
	 * Sets the default username.
	 * @param	the default username
	 *
	 */
	public void setDefaultUsername(String username) {
		userProperties.setProperty("user.defaultname", username);	
	}

	/**
	 * Gets the default port to be used when setting up a server of
	 * connecting to a server.
	 * @return	the default port
	 */
	public int getDefaultPort() {
		String value = userProperties.getProperty("user.defaultport");
		if (value != null) {
			return Integer.parseInt(value);
		} else {
			return -1;
		}
	}

	/**
	 * Sets the default port. The given portnumber must be positive and smaller
	 * than 2^16 = 65536. If the portnumber doesn't meet these requirements,
	 * nothing is done.
	 * @param	the default port
	 */
	public void setDefaultPort(int port) {
		if ((port > 0) && (port < 65536)) {
			userProperties.setProperty("user.defaultport", 
										String.valueOf(port));
		}
	}

	/**
	 * Gets the default server for the client to connect to.
	 * @return	the default server
	 */
	public String getDefaultServer() {
		return userProperties.getProperty("user.defaultserver");
	}

	/**
	 * Sets the default server.
	 * @param	the default server
	 */
	public void setDefaultServer(String server) {
		userProperties.setProperty("user.defaultserver", server);
	}

	/**
	 * Gets the default server description when setting up a server.
	 * @return	the default server description
	 */
	public String getDefaultServerDescription() {
		return userProperties.getProperty("user.defaultserverdescription");
	}

	/**
	 * Sets the default server description.
	 * @param	the default server description
	 */
	public void setDefaultServerDescription(String desc) {
		userProperties.setProperty("user.defaultserverdescription", desc);
	}

	/**
	 * Gets the preferred color of the player.
	 * @return	the preferred color [as int] (DCConstants.PLAYER_GOLD or
	 * 			DCConstants.PLAYER_SCARLET) 
	 */
	public int getPreferredColor() {
		return Integer.parseInt(
						userProperties.getProperty("user.preferredcolor"));
	}

	/**
	 * Sets the preferred color of the player.
	 * @param	the preferred color (possible values: DCConstants.PLAYER_GOLD,
	 * 			DCConstants.PLAYER_SCARLET) Other values are ignored.
	 */
	public void setPreferredColor(int player) {
		if ((player == DCConstants.PLAYER_GOLD) || 
			(player == DCConstants.PLAYER_SCARLET)) {
			userProperties.setProperty("user.preferredcolor", 
												String.valueOf(player));
		}
	}

	/**
	 * Gets if the pieces should be antialiased when drawn
	 * @return	true/false
	 */
	public boolean getAntialiased() {
		return Boolean.valueOf(userProperties.getProperty(
								"graphics.antialiased")).booleanValue();
	}

	/**
	 * Sets if the pieces should be antialiased
	 * @param	true/false
	 */
	public void setAntialiased(boolean antialiased) {
		userProperties.setProperty("graphics.antialiased", 
											String.valueOf(antialiased));
	}

	/**
	 * Gets the default width of the main frame.
	 * @return	the default width
	 */
	public int getDefaultFrameWidth() {
		String value = userProperties.getProperty("graphics.defaultframewidth");
		if (value != null) {
			return Integer.parseInt(value);
		} else {
			return -1;
		}
	}

	/**
	 * Sets the default width of the main frame.
	 * @param	the default width
	 */
	public void setDefaultFrameWidth(int width) {
		userProperties.setProperty("graphics.defaultframewidth", 
												String.valueOf(width));
	}

	/**
	 * Gets the default height of the main frame.
	 * @return	the default height
	 */
	public int getDefaultFrameHeight() {
		String value =userProperties.getProperty("graphics.defaultframeheight");
		if (value != null) {
			return Integer.parseInt(value);
		} else {
			return -1;
		}
	}

	/**
	 * Sets the default height of the main frame.
	 * @param	the default height
	 */
	public void setDefaultFrameHeight(int height) {
		userProperties.setProperty("graphics.defaultframeheight", 
												String.valueOf(height));
	}

	/**
	 * Gets the default x position of the main frame.
	 * @return	the default x position
	 */
	public int getDefaultFrameX() {
		String value =userProperties.getProperty("graphics.defaultframex");
		if (value != null) {
			return Integer.parseInt(value);
		} else {
			return -1;
		}
	}

	/**
	 * Sets the default x position of the main frame.
	 * @param	the default x position
	 */
	public void setDefaultFrameX(int x) {
		userProperties.setProperty("graphics.defaultframex", String.valueOf(x));
	}

	/**
	 * Gets the default y position of the main frame.
	 * @return	the default y position
	 */
	public int getDefaultFrameY() {
		String value =userProperties.getProperty("graphics.defaultframey");
		if (value != null) {
			return Integer.parseInt(value);
		} else {
			return -1;
		}
	}

	/**
	 * Sets the default y position of the main frame.
	 * @param	the default y position
	 */
	public void setDefaultFrameY(int y) {
		userProperties.setProperty("graphics.defaultframey", String.valueOf(y));
	}

	/**
	 * Gets the default bounds of the main frame.
	 * @return	the default bounds
	 */
	public Rectangle getDefaultFrameBounds() {
		int x = Integer.parseInt(
						userProperties.getProperty("graphics.defaultframex"));
		int y = Integer.parseInt(
						userProperties.getProperty("graphics.defaultframey"));
		int width = Integer.parseInt(
					userProperties.getProperty("graphics.defaultframewidth"));
		int height = Integer.parseInt(
					userProperties.getProperty("graphics.defaultframeheight"));
		if (x != -1 && y != -1 && width != -1 && height != -1) {
			return new Rectangle(x, y, width, height);
		} else {
			return null;
		}
	}

	/**
	 * Sets the default bounds of the main frame.
	 * @param	the default bounds
	 */
	public void setDefaultFrameBounds(Rectangle bounds) {
		setDefaultFrameBounds((int)	bounds.getX(),
							  (int) bounds.getY(),
							  (int) bounds.getWidth(),
							  (int) bounds.getHeight());
	}

	/**
	 * Sets the default bounds of the main frame.
	 * @param	the default x position
	 * @param	the default y position
	 * @param	the default width
	 * @param	the default height
	 */
	public void setDefaultFrameBounds(int x, int y, int width, int height) {
		userProperties.setProperty("graphics.defaultframex", String.valueOf(x));
		userProperties.setProperty("graphics.defaultframey", String.valueOf(y));
		userProperties.setProperty("graphics.defaultframewidth", 
													String.valueOf(width));
		userProperties.setProperty("graphics.defaultframeheight", 
													String.valueOf(height));
	}

	//FIXME : Still to add: graphics.pieceset
	//						graphics.defaultlookandfeel
	
	/**
	 * Gets if the debug output for the DC2dGUi class is switched on
	 * @return boolean
	 */
	public boolean getDebug2dGUI() {
		return Boolean.valueOf(userProperties.getProperty(
								"debug.dc2dgui")).booleanValue();
	}

	/**
	 * Sets the debug output setting for the DC2dGUI class
	 * @param debug	true/false
	 */
	public void setDebug2dGUI(boolean debug) {
		userProperties.setProperty("debug.dc2dgui",
											String.valueOf(debug));
	}
	
	/**
	 * Gets if the debug output for the DCButtonBoard class is switched on
	 * @return boolean
	 */
	public boolean getDebugButtonBoard() {
		return Boolean.valueOf(userProperties.getProperty(
								"debug.dcbuttonboard")).booleanValue();
	}
	
	/**
	 * Sets the debug output for the DCButtonBoard class
	 * @param debug
	 */
	public void setDebugButtonBoard(boolean debug) {
		userProperties.setProperty("debug.dcbuttonboard",
											String.valueOf(debug));
	}
	
	/**
	 * Gets if the debug option for the DCFrontEndDecoder class is switched on
	 * @return boolean
	 */
	public boolean getDebugFrontEndDecoder() {
		return Boolean.valueOf(userProperties.getProperty(
								"debug.dcfrontenddecoder")).booleanValue();
	}
	
	/**
	 * Sets the debug output for the DCFrontEndDecoder class
	 * @param debug
	 */
	public void setDebugFrontEndDecoder(boolean debug) {
		userProperties.setProperty("debug.dcfrontenddecoder",
											String.valueOf(debug));
	}
}


