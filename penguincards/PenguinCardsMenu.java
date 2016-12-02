package game.penguincards;

import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.*;
import game.penguincards.debug.*;

public class PenguinCardsMenu {

	private final static int LOW = 0;
	private final static int MEDIUM = 1;
	private final static int HIGH = 2;

	private final static int SIZE6 = 0;
	private final static int SIZE10 = 1;
	private final static int SIZE20 = 2;
	private final static int SIZE30 = 3;
	private final static int SIZE40 = 4;
	
	private Properties properties = new Properties();
	private String propFile = new String("PenguinCards.prop");

	private JMenu menuSize, menuSettings, menuDifficulty;
	private JMenuItem menuitemSize6,
		menuitemSize10,
		menuitemSize20,
		menuitemSize30,
		menuitemSize40;
	protected JMenuItem menuitemDifLow, menuitemDifMedium, menuitemDifHigh;
	protected JMenuItem menuitemH2H, menuitemH2C;
	public JMenuItem menuitemNewGame;
	
	public PenguinCardsMenu() {
		super();
	}

	public JMenuBar composeMenuBar() {
		try {
			properties.load(new FileInputStream(propFile));
		} catch (IOException exc) {
			Debug.debug(exc.getMessage());
		}

		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic('F');
		menuSettings = new JMenu("Settings");
		menuSettings.setMnemonic('S');
		JMenu menuHelp = new JMenu("Help");
		menuHelp.setMnemonic('H'); 
		
		menuBar.add(menuFile);
		menuBar.add(menuSettings);
		menuBar.add(menuHelp);

		menuSize = new JMenu("Size");
		menuSettings.add(menuSize);

		menuitemNewGame =
			createMenuItem(menuFile, 0, "New Game...", null, 'N', null);
		JMenuItem menuitemExit =
			createMenuItem(menuFile, 0, "Exit", null, 'E', null);
		JMenuItem menuitemAbout =
			createMenuItem(menuHelp, 0, "About...", null, 'A', null);

		menuFile.add(menuitemNewGame);
		menuFile.add(menuitemExit);
		
		menuHelp.add(menuitemAbout);

		menuitemSize6 =
			createMenuItem(menuSize, 2, "3*2 Cards (for Kids)", null, 0, null);

		menuitemSize10 =
			createMenuItem(menuSize, 2, "5*2 Cards", null, 0, null);

		menuitemSize20 =
			createMenuItem(menuSize, 2, "10*2 Cards", null, 0, null);
		menuitemSize30 =
			createMenuItem(menuSize, 2, "15*2 Cards", null, 0, null);
		menuitemSize40 =
			createMenuItem(menuSize, 2, "20*2 Cards", null, 0, null);

		menuitemSize6.addActionListener(new SizeListener());
		menuitemSize10.addActionListener(new SizeListener());
		menuitemSize20.addActionListener(new SizeListener());
		menuitemSize30.addActionListener(new SizeListener());
		menuitemSize40.addActionListener(new SizeListener());

		menuDifficulty = new JMenu("Difficulty");

		menuitemDifLow =
			createMenuItem(menuDifficulty, 2, "Low", null, 0, null);
		menuitemDifMedium =
			createMenuItem(menuDifficulty, 2, "Medium", null, 0, null);
		menuitemDifHigh =
			createMenuItem(menuDifficulty, 2, "High", null, 0, null);

		menuitemDifLow.addActionListener(new DifficultyListener());
		menuitemDifMedium.addActionListener(new DifficultyListener());
		menuitemDifHigh.addActionListener(new DifficultyListener());

		menuSettings.add(menuDifficulty);
		menuSettings.addSeparator();

		menuitemH2H =
			createMenuItem(menuSettings, 2, "Human vs. Human", null, 0, null);
		menuitemH2H.setName("h2h");

		menuitemH2C =
			createMenuItem(
				menuSettings,
				2,
				"Human vs. Computer",
				null,
				0,
				null);
		menuitemH2C.setName("h2c");

		menuitemH2H.addActionListener(new PlayerListener());
		menuitemH2C.addActionListener(new PlayerListener());

		menuitemAbout.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				new PenguinCardsAbout();
			}
		}
		);
		
		menuitemExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		});

		updateSelectedMenuItems();
		
		return menuBar;
	}

	private JMenuItem createMenuItem(
		JMenu menu,
		int iType,
		String sText,
		ImageIcon image,
		int acceleratorKey,
		String sToolTip) {
		// Create the item
		JMenuItem menuItem;

		switch (iType) {
			case 1 :
				menuItem = new JRadioButtonMenuItem();
				break;

			case 2 :
				menuItem = new JCheckBoxMenuItem();
				break;

			default :
				menuItem = new JMenuItem();
				break;
		}

		// Add the item test
		menuItem.setText(sText);

		// Add the optional icon
		if (image != null)
			menuItem.setIcon(image);

		// Add the accelerator key
		if (acceleratorKey > 0)
			menuItem.setMnemonic(acceleratorKey);

		// Add the optional tool tip text
		if (sToolTip != null)
			menuItem.setToolTipText(sToolTip);

		menu.add(menuItem);

		return menuItem;
	}

	private void updateSelectedMenuItems() {

		String size = properties.getProperty("Size");
		if (size.equals("3*2 Cards"))
			menuitemSize6.setSelected(true);
		else if (size.equals("5*2 Cards"))
			menuitemSize10.setSelected(true);
		else if (size.equals("10*2 Cards"))
			menuitemSize20.setSelected(true);
		else if (size.equals("15*2 Cards"))
			menuitemSize30.setSelected(true);
		else if (size.equals("20*2 Cards"))
			menuitemSize40.setSelected(true);

		String difficulty = properties.getProperty("Difficulty");
		if (difficulty.equals("Low"))
			menuitemDifLow.setSelected(true);
		else if (difficulty.equals("Medium"))
			menuitemDifMedium.setSelected(true);
		else if (difficulty.equals("High"))
			menuitemDifHigh.setSelected(true);

		String players = properties.getProperty("Players");
		if (players.equals("Human2Human"))
			menuitemH2H.setSelected(true);
		else if (players.equals("Human2Computer"))
			menuitemH2C.setSelected(true);

	}

	class SizeListener implements ActionListener {

		public void actionPerformed(ActionEvent ae) {

			int size = menuSize.getItemCount();
			JCheckBoxMenuItem tmpMenuItem = (JCheckBoxMenuItem) ae.getSource();

			// update properties file
			if (tmpMenuItem.equals(menuSize.getItem(PenguinCardsMenu.SIZE6)))
				properties.setProperty("Size", "3*2 Cards");
			else if (
				tmpMenuItem.equals(menuSize.getItem(PenguinCardsMenu.SIZE10)))
				properties.setProperty("Size", "5*2 Cards");
			else if (
				tmpMenuItem.equals(menuSize.getItem(PenguinCardsMenu.SIZE20)))
				properties.setProperty("Size", "10*2 Cards");
			else if (
				tmpMenuItem.equals(menuSize.getItem(PenguinCardsMenu.SIZE30)))
				properties.setProperty("Size", "15*2 Cards");
			else if (
				tmpMenuItem.equals(menuSize.getItem(PenguinCardsMenu.SIZE40)))
				properties.setProperty("Size", "20*2 Cards");

			//uncheck other checkboxes
			for (int i = 0; i < size; i++) {
				if (tmpMenuItem
					!= (((JCheckBoxMenuItem)) menuSize.getItem(i))) {
					((JCheckBoxMenuItem) menuSize.getItem(i)).setSelected(
						false);
				} else
					((JCheckBoxMenuItem) menuSize.getItem(i)).setSelected(true);
			}

			// save properties file
			try {
				properties.store(new FileOutputStream(propFile), null);
			} catch (IOException exc) {
				Debug.debug(exc.getMessage());
			}

		}
	}

	class DifficultyListener implements ActionListener {

		public void actionPerformed(ActionEvent ae) {

			int size = menuDifficulty.getItemCount();
			JCheckBoxMenuItem tmpMenuItem = (JCheckBoxMenuItem) ae.getSource();

			// update properties file
			if (tmpMenuItem
				.equals(menuDifficulty.getItem(PenguinCardsMenu.LOW)))
				properties.setProperty("Difficulty", "Low");
			else if (
				tmpMenuItem.equals(
					menuDifficulty.getItem(PenguinCardsMenu.MEDIUM)))
				properties.setProperty("Difficulty", "Medium");
			else if (
				tmpMenuItem.equals(
					menuDifficulty.getItem(PenguinCardsMenu.HIGH)))
				properties.setProperty("Difficulty", "High");

			//uncheck other checkboxes
			for (int i = 0; i < size; i++) {
				if (tmpMenuItem
					!= (((JCheckBoxMenuItem)) menuDifficulty.getItem(i))) {
					(
						(JCheckBoxMenuItem) menuDifficulty.getItem(
							i)).setSelected(
						false);
				} else
					(
						(JCheckBoxMenuItem) menuDifficulty.getItem(
							i)).setSelected(
						true);
			}

			// save properties file
			try {
				properties.store(new FileOutputStream(propFile), null);
			} catch (IOException exc) {
				Debug.debug(exc.getMessage());
			}

		}
	}

	class PlayerListener implements ActionListener {

		public void actionPerformed(ActionEvent ae) {

			int size = menuSettings.getItemCount();
			JCheckBoxMenuItem tmpMenuItem = (JCheckBoxMenuItem) ae.getSource();

			// update properties file
			if (tmpMenuItem.getName().equalsIgnoreCase("h2h"))
				properties.setProperty("Players", "Human2Human");
			else if (tmpMenuItem.getName().equalsIgnoreCase("h2c"))
				properties.setProperty("Players", "Human2Computer");

			for (int i = 0; i < size; i++) {
				if (menuSettings.getItem(i) instanceof JCheckBoxMenuItem) {
					if (tmpMenuItem
						!= (((JCheckBoxMenuItem)) menuSettings.getItem(i))) {
						(
							(JCheckBoxMenuItem) menuSettings.getItem(
								i)).setSelected(
							false);
					} else
						(
							(JCheckBoxMenuItem) menuSettings.getItem(
								i)).setSelected(
							true);
				}
			}

			//	save properties file
			try {
				properties.store(new FileOutputStream(propFile), null);
			} catch (IOException exc) {
				Debug.debug(exc.getMessage());
			}
		}
	}

}