/* SoccerMaster.java
   This class presents a gui to player,... it can be run as an application or an applet

   Copyright (C) 2001  Yu Zhang

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the
   Free Software Foundation, Inc.,
   59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

/* This file modifed to fix bug #430844
							jdm, June 7 2001
*/

/* Modified to add functionality of the Arena class that can be instantiated
   with either the Field class or the Rink class.  The Rink class is used if
   the command line parameter "-hockey" is used.
						        jdm, June 7 2001
*/

/* Modified to add Java3D capabilities - the -3D option now starts it with
   a FieldJ3D for its Arena.
   In addition to calling FieldJ3D rather than just Field, this meant making 
   the menus 'heavyweight' so that the Java3D part is not rendered over them.
   
                                fcm, August 29 2001
*/

/* Cleaned up, mainly move actions, dialogs and sounds out of this messy class 
								Yu Zhang, Nov 18 2004
								
	Modifications by Vadim Kyrylov 
							January 2006
*/

package soccer.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;

import soccer.client.action.*;

import soccer.client.dialog.ActiveCommand;
import soccer.client.dialog.DialogManager;
import soccer.client.view.Arena;
import soccer.client.view.Field;
import soccer.client.view.j3d.FieldJ3D;
import soccer.common.*;

public class SoccerMaster extends JApplet 
{

	public static boolean isApplet = true;

	public static final int MAJOR_VERSION = 1;
	public static final int MINOR_VERSION = 5;
	public static final int PATCH_VERSION = 4;
	public static final String VERSION_LABEL = "";
	public final static String APP_VERSION =
		MAJOR_VERSION
			+ "."
			+ MINOR_VERSION
			+ "."
			+ PATCH_VERSION
			+ VERSION_LABEL;
	public final static String APP_NAME = "Tao Of Soccer - Education";

	// server and AI processes setup
	public static Runtime runtime = Runtime.getRuntime();
	public static Process serverP = null;
	public static int maxCommands = 21; // max commands can be started
	public static Vector activeCommands = new Vector(maxCommands);

	// server interface threads
	public Cplayer player = null;
	// soccer server data accepting and sending for playing
	public Cviewer viewer = null;
	// soccer server data accepting and sending for viewing
	public Replayer replayer = null; // log file data translating for viewing

	// log file
	public RandomAccessFile logFile = null;

	// game interface
	public JPanel mainPane;
	public Field arena2D;
	public Arena arena3D;

	public JLabel leftName;
	public JLabel leftScore;
	public JLabel periodJLabel;
	public JLabel modeJLabel;
	public JLabel timeJLabel;	
	public JLabel gameJLabel;
	
	public JLabel rightName;
	public JLabel rightScore;

	public JTextField replicaJTextField;
	public JTextField input;
	
	// game data
	private World world;
	private int state;

	// networking
	private InetAddress address;
	private int port;
	private Transceiver transceiver;

	private boolean in3D = false;
	private boolean displayID = true;
	private boolean displayChat = false;
	private boolean showBallCoord = false;

	private Actions m_actions;
	private DialogManager m_dlgManager;
	private SndSystem m_soundSystem;
    
    private JWindow splashScreen = null;
    private JLabel splashLabel = null;      
    
    private JToolBar jtb; 
	private int loadBtnIdx;
	private int stepBtnIdx;
	private int saveBtnIdx;
	private int fwdBtnIdx;
	
	public void init() 
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {

		}
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
       
        // Create and throw the splash screen up. Since this will
        // physically throw bits on the screen, we need to do this
        // on the GUI thread using invokeLater.
        URL imgURL = SoccerMaster.class.getResource("/imag/splash.png");
        ImageIcon icon = new ImageIcon(imgURL);
        splashLabel = new JLabel(icon);
        splashScreen = new JWindow();
        splashScreen.getContentPane().add(splashLabel);
        splashScreen.pack();
        splashScreen.setLocation(
        screenSize.width/2 - splashScreen.getSize().width/2,
        screenSize.height/2 - splashScreen.getSize().height/2);  
        
        // do the following on the gui thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	showSplashScreen();
            }
        });              

		address = null;
		port = 7777;
		transceiver = new Transceiver(false);

		m_actions = new Actions(this);
		m_dlgManager = new DialogManager(this);
		m_soundSystem = new SndSystem();

		m_soundSystem.addMusic("butterfly");
		m_soundSystem.addMusic("casablan");
		m_soundSystem.addMusic("conquest");
		m_soundSystem.addMusic("luvtheme");
		m_soundSystem.addMusic("mohicans");
		m_soundSystem.addMusic("mymind");
		m_soundSystem.addMusic("somewit");
		m_soundSystem.addMusic("twinpeaks");

		JPopupMenu.setDefaultLightWeightPopupEnabled(false);

		if (isApplet) {
			String temp;

			temp = getParameter("in3D");
			if (temp != null) {
				if (temp.compareTo("true")
					== 0 | temp.compareTo("TRUE")
					== 0 | temp.compareTo("True")
					== 0)
					in3D = true;
				else
					System.out.println("in3D paramater not true.");
			} else
				System.out.println("in3D paramater not found.");

			temp = getParameter("port");
			try {
				port = Integer.valueOf(temp).intValue();
			} catch (Exception e) {
				port = 7777;
			}

		}

		getContentPane().setLayout(new BorderLayout());

		setupMenus();
		setupToolbar();
		setupMainPane();
		state = GState.INIT;
        
        // Show the demo and take down the splash screen. Note that
        // we again must do this on the GUI thread using invokeLater.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	hideSplash();
            }
        });

	}

	private void setupMainPane() 
	{
		Dimension d;

		JPanel statusPane;
		JPanel leftStatus;
		JPanel gameStatus;
		JPanel timePane;
		JPanel rightStatus;

		// Create mainPane
		mainPane = new JPanel();
		mainPane.setLayout(new BorderLayout());
		mainPane.setBackground(Color.gray);

		// Create arena Pane
        arena2D = new Field(this);
        arena3D = new FieldJ3D(this);

		// Create status Pane
		statusPane = new JPanel();
		statusPane.setLayout(new GridLayout(1, 4, 3, 3));
		d = new Dimension(500, 38);
		statusPane.setSize(d);
		statusPane.setBackground(Color.gray);
		statusPane.setBorder(BorderFactory.createRaisedBevelBorder());
		
		int fontSize = (int)(0.35*statusPane.getHeight());
		Font aFont = new Font( "Dialog", Font.BOLD, fontSize );
		Font bFont = new Font( "Dialog", Font.BOLD, (int)(1.5*fontSize) );

		// left team status
		leftStatus = new JPanel();
		leftStatus.setLayout(new FlowLayout());
		leftStatus.setBackground(Color.yellow);
		leftStatus.setBorder(BorderFactory.createLoweredBevelBorder());
		leftName = new JLabel("Alliance:", SwingConstants.CENTER);
		leftName.setBackground(Color.yellow);
		leftName.setForeground(Color.black);
		leftName.setFont( aFont );
		
		leftScore = new JLabel("0", SwingConstants.CENTER);
		leftScore.setBackground(Color.yellow);
		leftScore.setForeground(Color.black);
		leftScore.setFont( bFont );
		
		leftStatus.add(leftName);
		leftStatus.add(leftScore);

		// general game status
		
		gameStatus = new JPanel();
		gameStatus.setLayout( new GridLayout(1, 4, 5, 0) );
		gameStatus.setBackground(Color.orange);
		gameStatus.setBorder(BorderFactory.createLoweredBevelBorder());
		
		periodJLabel = new JLabel("Before Match:", SwingConstants.CENTER);
		periodJLabel.setBackground(Color.gray);
		periodJLabel.setForeground(Color.black);
		periodJLabel.setFont( aFont );
		
		modeJLabel = new JLabel("Before Kick Off:", SwingConstants.CENTER);
		modeJLabel.setBackground(Color.gray);
		modeJLabel.setForeground(Color.black);
		modeJLabel.setFont( aFont );
		
		gameStatus.add(periodJLabel);
		gameStatus.add(modeJLabel);

		timePane = new JPanel();
		timePane.setLayout( new GridLayout(1, 2, 5, 0) );
		timePane.setBackground(Color.orange);
		timePane.setBorder(BorderFactory.createLoweredBevelBorder());

		timeJLabel = new JLabel("00:00", SwingConstants.CENTER);
		timeJLabel.setBackground(Color.gray);
		timeJLabel.setForeground(Color.black);
		timeJLabel.setFont( aFont );
			
		gameJLabel = new JLabel("Game:  0", SwingConstants.CENTER);
		gameJLabel.setBackground(Color.gray);
		gameJLabel.setForeground(Color.black);
		gameJLabel.setFont( aFont );
		gameJLabel.setVisible(true); 
			
		timePane.add(timeJLabel);
		timePane.add(gameJLabel);

		// right team status
		rightStatus = new JPanel();
		rightStatus.setLayout(new FlowLayout());
		rightStatus.setBackground(Color.red);
		rightStatus.setBorder(BorderFactory.createLoweredBevelBorder());
		rightName = new JLabel("Empire:");
		rightName.setBackground(Color.red);
		rightName.setForeground(Color.black);
		rightName.setFont( aFont );
		
		rightScore = new JLabel("0");
		rightScore.setBackground(Color.red);
		rightScore.setForeground(Color.black);
		rightScore.setFont( bFont );
		rightStatus.add(rightName);
		rightStatus.add(rightScore);

		statusPane.add(leftStatus);
		statusPane.add(gameStatus);
		statusPane.add(timePane);
		statusPane.add(rightStatus);

		mainPane.add(statusPane, BorderLayout.NORTH);
		if (!in3D) {
			mainPane.add(arena2D, BorderLayout.CENTER);
			arena2D.requestFocus();
		} else {
			mainPane.add(arena3D, BorderLayout.CENTER);
			arena3D.requestFocus();
		}		

		getContentPane().add(mainPane, BorderLayout.CENTER);
	}

	/**
	 * Set up the main window menus.
	 */
	private void setupMenus() 
	{
		JMenuBar jmb = new JMenuBar();
		for (int i = 0; i < MenuDefinitions.MENUS.length; i++) {
			JMenu menu = new JMenu((String) MenuDefinitions.MENUS[i][0]);
			for (int j = 1; j < MenuDefinitions.MENUS[i].length; j++) {
				if (MenuDefinitions.MENUS[i][j] == null) {
					menu.addSeparator();
				} else {
					Action a = getAction((Class) MenuDefinitions.MENUS[i][j]);
					if (a instanceof AbstractToggleAction) {
						menu.add(
							new ToggleActionMenuItem((AbstractToggleAction) a));
					} else {
						menu.add(new ActionMenuItem((AbstractClientAction) a));
					}
				}
			}
			jmb.add(menu);
		}
		setJMenuBar(jmb);
	}

	/**
	 * Set up the main window toolbar.
	 */
	private void setupToolbar() 
	{
		JLabel comm;

		JButton button = null;
		Action a = null;

		jtb = new JToolBar();
		jtb.setFloatable(false);
		jtb.setRollover(true);
		jtb.setBorderPainted(true);

		a = getAction((Class) SetUpServerAction.class);
		button = jtb.add(a);
		button.setText(""); //an icon-only button
		button.setToolTipText("Set Up Local Server");

		a = getAction((Class) SetUpAIAction.class);
		button = jtb.add(a);
		button.setText(""); //an icon-only button
		button.setToolTipText("Set Up Local AI players");

		jtb.addSeparator();

		a = getAction((Class) PlayGameAction.class);
		button = jtb.add(a);
		button.setText(""); //an icon-only button
		button.setToolTipText("Join/Play Game");

		a = getAction((Class) ViewGameAction.class);
		button = jtb.add(a);
		button.setText(""); //an icon-only button
		button.setToolTipText("Join/View Game");

		a = getAction((Class) StopGameAction.class);
		button = jtb.add(a);
		button.setText(""); //an icon-only button
		button.setToolTipText("Leave Playing/Viewing Game");

		jtb.addSeparator();
		
		a = getAction((Class) LoadLogAction.class);
		button = jtb.add(a);
		button.setText(""); //an icon-only button
		button.setToolTipText("Load the log file");

		a = getAction((Class) RewindLogPlayAction.class);
		button = jtb.add(a);
		button.setText(""); //an icon-only button
		button.setToolTipText("Rewind the log file");

		//jtb.addSeparator();	

		a = getAction((Class) PlayBackLogPlayAction.class);
		button = jtb.add(a);
		button.setText(""); //an icon-only button
		button.setToolTipText("Play Back the log file");

		a = getAction((Class) PauseLogPlayAction.class);
		button = jtb.add(a);
		button.setText(""); //an icon-only button
		button.setToolTipText("Pause the log file");

		a = getAction((Class) PlayLogPlayAction.class);
		button = jtb.add(a);
		button.setText(""); //an icon-only button
		button.setToolTipText("Play the log file");

		a = getAction((Class) FastForwardLogPlayAction.class);
		button = jtb.add(a);
		button.setText(""); //an icon-only button
		button.setToolTipText("Fast forward the log file");

		a = getAction((Class) StopLogPlayAction.class);
		button = jtb.add(a);
		button.setText(""); //an icon-only button
		button.setToolTipText("Stop replaying the log file");

		jtb.addSeparator();
		
        a = getAction((Class) CoachLoadFileAction.class);
        button = jtb.add(a);
        loadBtnIdx = jtb.getComponentIndex(button); // index of this button must be saved 
        button.setText(""); //an icon-only button
        button.setToolTipText("Load situation file");

		a = getAction((Class) CoachStepAction.class); 
		button = jtb.add(a);
		stepBtnIdx = jtb.getComponentIndex(button); // index of this button must be saved 
		//System.out.println("stepBtnIdx = " + stepBtnIdx );
		button.setText(""); //an icon-only button
		button.setToolTipText("Step forward the Game");

		a = getAction((Class) CoachPlayAction.class);
		button = jtb.add(a);
		button.setText(""); //an icon-only button
		button.setToolTipText("Play the Game");

        a = getAction((Class) CoachForwardAction.class);
        button = jtb.add(a);
        fwdBtnIdx = jtb.getComponentIndex(button); // index of this button must be saved 
        button.setText(""); //an icon-only button
        button.setToolTipText("Forward the Game");

        a = getAction((Class) SaveSnapshotAction.class); 
        button = jtb.add(a);
		saveBtnIdx = jtb.getComponentIndex(button); // index of this button must be saved 
        button.setText(""); //an icon-only button
        button.setToolTipText("Save a game snapshot");
		jtb.addSeparator();

		JLabel replicaJLabel = new JLabel("Replica:");
		jtb.add(replicaJLabel);
		replicaJTextField = new JTextField(5);
		replicaJTextField.setEditable(false);
		replicaJTextField.setText( "" ); // not used yet
		jtb.add(replicaJTextField);

		comm = new JLabel("Chat:");
		jtb.add(comm);
		input = new JTextField(25);
		jtb.add(input);

		getContentPane().add(jtb, BorderLayout.NORTH);
	}

	public static void main(String s[]) 
	{
		System.out.println("\n ***  Starting Tao of Soccer Monitor  *** \n");
		isApplet = false;

		JFrame aJFrame;
		aJFrame = new JFrame(APP_NAME + " " + APP_VERSION);

		aJFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (serverP != null) {
					serverP.destroy();
					serverP = null;
					for (int i = 0; i < activeCommands.size(); i++) {
						ActiveCommand ac =
							(ActiveCommand) activeCommands.elementAt(i);
						ac.getProcess().destroy();
					}
				}
				System.exit(0);
			}
		});
		SoccerMaster sm = new SoccerMaster();

		// set windows icon
		ImageIcon img =
			new ImageIcon(
				SoccerMaster.class.getClass().getResource("/imag/icon.gif"));
		aJFrame.setIconImage(img.getImage());

		sm.init();
		sm.setEnabled();
		aJFrame.getContentPane().add("Center", sm);

		aJFrame.pack();
		aJFrame.setResizable(false);
		aJFrame.show();
	}

	public void setEnabled() 
	{
		getAction((Class) ExitAction.class).setEnabled(true);
		getAction((Class) SetUpServerAction.class).setEnabled(true);
		getAction((Class) SetUpAIAction.class).setEnabled(true);

		getAction((Class) LoadLogAction.class).setEnabled(true);

	}

	
	public int getGState() 
	{
		return state;
	}
	
	public void setGState( int newstate ) 
	{
		state = newstate;
	}
	
	public void setHeartRate( double hRate )
	{
		world.SIM_STEP_SECONDS = hRate;
	}
	
	/**
	 * @return
	 */
	public boolean isDisplayID() 
	{
		return displayID;
	}

	/**
	 * @param b
	 */
	public void setDisplayID(boolean b) 
	{
		displayID = b;
	}

	/**
	 * Get the action instance for the specified action class.
	 *
	 * @param actionClass an action class
	 * @return the stored instance of the specified action.
	 */
	public AbstractClientAction getAction(Class actionClass) 
	{
		return m_actions.getAction(actionClass);
	}

	/**
	 * Get the dialog manager.
	 */
	public DialogManager getDialogManager() 
	{
		if (m_dlgManager == null) {
			m_dlgManager = new DialogManager(this);
		}
		return m_dlgManager;
	}

	/**
	 * Get the sound system.
	 */
	public SndSystem getSoundSystem() 
	{
		if (m_soundSystem == null) {
			m_soundSystem = new SndSystem();
		}
		return m_soundSystem;
	}

	/**
	 * Terminates the client. You really ought to disconnect & stuff
	 * before calling this, as it just basically system.exits
	 */
	public synchronized void quit() 
	{
		if (serverP != null) {
			serverP.destroy();
			serverP = null;
			for (int i = 0; i < activeCommands.size(); i++) {
				ActiveCommand ac = (ActiveCommand) activeCommands.elementAt(i);
				ac.getProcess().destroy();
			}
		}
		System.exit(0);
	}

	/**
	 * @return
	 */
	public boolean isDisplayChat() 
	{
		return displayChat;
	}

	/**
	 * @param b
	 */
	public void setDisplayChat(boolean b) 
	{
		displayChat = b;
	}

	/**
	 * @return
	 */
	public InetAddress getAddress() 
	{
		return address;
	}

	/**
	 * @return
	 */
	public int getPort() 
	{
		return port;
	}

	/**
	 * @param address
	 */
	public void setAddress(InetAddress address) 
	{
		this.address = address;
	}

	/**
	 * @param i
	 */
	public void setPort(int i) 
	{
		port = i;
	}

	/**
	 * @return
	 */
	public Transceiver getTransceiver() 
	{
		return transceiver;
	}

	/**
	 * @return
	 */
	public boolean isIn3D() 
	{
		return in3D;
	}

	/**
	 * @param b
	 */
	public void setIn3D(boolean b) 
	{
		in3D = b;
	}

	/**
	 * @return
	 */
	public World getWorld() 
	{
		return world;
	}

	/**
	 * @param world
	 */
	public void setWorld(World world) 
	{
		this.world = world;
	}
    
    public void showSplashScreen() 
    {

        splashScreen.show();

    }
  
    /**
     * pop down the spash screen
     */
    public void hideSplash() 
    {

        splashScreen.setVisible(false);
        splashScreen = null;
        splashLabel = null;

    }    

	public JToolBar getJToolBar() 
	{
		return jtb;
	} 

	public int getLoadBtnIdx() 
	{
		return loadBtnIdx;
	}

	public int getStepBtnIdx() 
	{
		return stepBtnIdx;
	}

	public int getSaveBtnIdx() 
	{
		return saveBtnIdx;
	}

	public int getFwdBtnIdx() 
	{
		return fwdBtnIdx;
	}
	
	public void setShowBallCoord( boolean show )
	{
		showBallCoord = show; 	
	}

	public boolean getShowBallCoord()
	{
		return showBallCoord; 	
	}


}
