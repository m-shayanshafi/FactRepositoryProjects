/*
 * This file is part of JMines.
 * Copyright (C) 2009 Zleurtor
 *
 * JMines is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JMines is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JMines.  If not, see <http://www.gnu.org/licenses/>.
 */
package jmines.view.components;

import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import jmines.control.actions.board.Hexagonal;
import jmines.control.actions.board.Octosquare;
import jmines.control.actions.board.Parquet;
import jmines.control.actions.board.Pentagonal;
import jmines.control.actions.board.Square;
import jmines.control.actions.board.Triangular;
import jmines.control.actions.board.Triangular14;
import jmines.control.actions.displayandsounds.Antialiasing;
import jmines.control.actions.displayandsounds.ChangeBackgroundColor;
import jmines.control.actions.displayandsounds.ChangeButtonsColor;
import jmines.control.actions.displayandsounds.ChangeColor;
import jmines.control.actions.displayandsounds.ChangeLCDColor;
import jmines.control.actions.displayandsounds.ChangeLookAndFeel;
import jmines.control.actions.displayandsounds.Shadow;
import jmines.control.actions.game.Beginner;
import jmines.control.actions.game.BestTimes;
import jmines.control.actions.game.Color;
import jmines.control.actions.game.Custom;
import jmines.control.actions.game.Expert;
import jmines.control.actions.game.Intermediate;
import jmines.control.actions.game.Marks;
import jmines.control.actions.game.New;
import jmines.control.actions.game.Quit;
import jmines.control.actions.game.Sound;
import jmines.control.actions.info.About;
import jmines.control.actions.info.Thanks;
import jmines.control.actions.language.Language;
import jmines.control.actions.other.LoadBoard;
import jmines.control.actions.other.LoadVideo;
import jmines.control.actions.other.SaveBoard;
import jmines.control.actions.other.SaveScreenshot;
import jmines.control.actions.other.SaveVideo;
import jmines.control.actions.robot.Cheat;
import jmines.control.actions.robot.Help;
import jmines.control.actions.robot.Name;
import jmines.control.actions.robot.Play;
import jmines.control.actions.robot.RandomTries;
import jmines.control.actions.robot.RealClicks;
import jmines.control.actions.robot.Statistics;
import jmines.control.listeners.MouseListenerForMenuItem;
import jmines.view.persistence.Configuration;

/**
 * The menu bar of the application.
 *
 * @author zleurtor
 */
public class MenuBar extends JMenuBar {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = -7333583263157045443L;

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The game popup menu.
     */
    private JMenu gameMenu;
    /**
     * The board popup menu.
     */
    private JMenu boardMenu;
    /**
     * The display popup menu.
     */
    private JMenu displayMenu;
    /**
     * The change look and feel popup menu.
     */
    private JMenu changeLookAndFeelMenu;
    /**
     * The change background color popup menu.
     */
    private JMenu changeBgColorMenu;
    /**
     * The change buttons color popup menu.
     */
    private JMenu changeButtonsColorMenu;
    /**
     * The change LCD color popup menu.
     */
    private JMenu changeLCDColorMenu;
    /**
     * The language popup menu.
     */
    private JMenu languageMenu;
    /**
     * The other popup menu.
     */
    private JMenu otherMenu;
    /**
     * The robot popup menu.
     */
    private JMenu robotMenu;
    /**
     * The info popup menu.
     */
    private JMenu infoMenu;

    /**
     * The save video menu item.
     */
    private JMenuItem saveVideoItem;
    /**
     * The save video JMines action.
     */
    private SaveVideo saveVideoAction;

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new menu bar.
     *
     * @param mainFrame The JMines main frame.
     */
    public MenuBar(final MainFrame mainFrame) {
        super();
        init(mainFrame);
    }

    //==========================================================================
    // Getters
    //==========================================================================

    //==========================================================================
    // Setters
    //==========================================================================

    //==========================================================================
    // Inherited methods
    //==========================================================================

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
    /**
     * Create the popup menu of this bar.
     *
     * @param mainFrame The JMines main frame.
     */
    private void createMenus(final MainFrame mainFrame) {
        Configuration configuration = Configuration.getInstance();

        // Create the menus
        gameMenu = new JMenu(configuration.getText(Configuration.KEY_MENU_GAME));
        boardMenu = new JMenu(configuration.getText(Configuration.KEY_MENU_BOARD));
        displayMenu = new JMenu(configuration.getText(Configuration.KEY_MENU_DISPLAY));
        changeLookAndFeelMenu = new JMenu(configuration.getText(Configuration.KEY_MENU_DISPLAY_CHANGELOOKANDFEEL));
        changeBgColorMenu = new JMenu(configuration.getText(Configuration.KEY_MENU_DISPLAY_CHANGEBGCOLOR));
        changeButtonsColorMenu = new JMenu(configuration.getText(Configuration.KEY_MENU_DISPLAY_CHANGEBUTTONSCOLOR));
        changeLCDColorMenu = new JMenu(configuration.getText(Configuration.KEY_MENU_DISPLAY_CHANGELCDCOLOR));
        languageMenu = new JMenu(configuration.getText(Configuration.KEY_MENU_LANGUAGE));
        otherMenu = new JMenu(configuration.getText(Configuration.KEY_MENU_OTHER));
        robotMenu = new JMenu(configuration.getText(Configuration.KEY_MENU_ROBOT));
        infoMenu = new JMenu(configuration.getText(Configuration.KEY_MENU_INFO));

        fillGameMenu(mainFrame);
        fillBoardMenu(mainFrame);
        fillDisplayMenu(mainFrame);
        fillLanguageMenu(mainFrame);
        fillOtherMenu(mainFrame);
        fillRobotMenu(mainFrame);
        fillInfoMenu(mainFrame);
    }

    /**
     * Create the items for the game menu, configure them and add them to the
     * menu.
     *
     * @param mainFrame The main frame of the application.
     */
    private void fillGameMenu(final MainFrame mainFrame) {
        Configuration configuration = Configuration.getInstance();

        // Create the menus items
        JMenuItem newItem = new JMenuItem(new New(configuration.getText(Configuration.KEY_MENU_GAME_NEW), mainFrame));
        JMenuItem beginnerItem = new JRadioButtonMenuItem(new Beginner(configuration.getText(Configuration.KEY_MENU_GAME_BEGINNER), mainFrame));
        JMenuItem intermediateItem = new JRadioButtonMenuItem(new Intermediate(configuration.getText(Configuration.KEY_MENU_GAME_INTERMEDIATE), mainFrame));
        JMenuItem expertItem = new JRadioButtonMenuItem(new Expert(configuration.getText(Configuration.KEY_MENU_GAME_EXPERT), mainFrame));
        JMenuItem customItem = new JRadioButtonMenuItem(new Custom(configuration.getText(Configuration.KEY_MENU_GAME_CUSTOM), mainFrame));
        JMenuItem marksItem = new JCheckBoxMenuItem(new Marks(configuration.getText(Configuration.KEY_MENU_GAME_MARKS), mainFrame));
        JMenuItem colorItem = new JCheckBoxMenuItem(new Color(configuration.getText(Configuration.KEY_MENU_GAME_COLOR), mainFrame));
        JMenuItem soundItem = new JCheckBoxMenuItem(new Sound(configuration.getText(Configuration.KEY_MENU_GAME_SOUND), mainFrame));
        JMenuItem bestTimesItem = new JMenuItem(new BestTimes(configuration.getText(Configuration.KEY_MENU_GAME_BESTTIMES), mainFrame));
        JMenuItem quitItem = new JMenuItem(new Quit(configuration.getText(Configuration.KEY_MENU_GAME_QUIT), mainFrame));

        // Configure the items
        newItem.setAccelerator(KeyStroke.getKeyStroke(configuration.getText(Configuration.KEY_MENU_GAME_NEW_KEYSTROKE)));
        marksItem.setSelected(configuration.getBoolean(Configuration.KEY_USER_MARKS));
        colorItem.setSelected(configuration.getBoolean(Configuration.KEY_USER_COLOR));
        soundItem.setSelected(configuration.getBoolean(Configuration.KEY_USER_SOUND));

        // Create the buttons groups
        ButtonGroup difficulty = new ButtonGroup();
        difficulty.add(beginnerItem);
        difficulty.add(intermediateItem);
        difficulty.add(expertItem);
        difficulty.add(customItem);
        if (configuration.getString(Configuration.KEY_USER_DIFFICULTY).equals(Configuration.DIFFICULTY_BEGINNER)) {
            beginnerItem.setSelected(true);
        } else if (configuration.getString(Configuration.KEY_USER_DIFFICULTY).equals(Configuration.DIFFICULTY_INTERMEDIATE)) {
            intermediateItem.setSelected(true);
        } else if (configuration.getString(Configuration.KEY_USER_DIFFICULTY).equals(Configuration.DIFFICULTY_EXPERT)) {
            expertItem.setSelected(true);
        } else {
            customItem.setSelected(true);
        }

        // Enable selected items
        mainFrame.getMainPanel().getGamePanel().getGameBoard().setMarksAuthorized(marksItem.isSelected());
        mainFrame.getMainPanel().setColored(colorItem.isSelected());
        mainFrame.getMainPanel().setSoundEnabled(soundItem.isSelected());

        // Add the items to the menus
        gameMenu.add(newItem);
        gameMenu.addSeparator();
        gameMenu.add(beginnerItem);
        gameMenu.add(intermediateItem);
        gameMenu.add(expertItem);
        gameMenu.add(customItem);
        gameMenu.addSeparator();
        gameMenu.add(marksItem);
        gameMenu.add(colorItem);
        gameMenu.add(soundItem);
        gameMenu.addSeparator();
        gameMenu.add(bestTimesItem);
        gameMenu.addSeparator();
        gameMenu.add(quitItem);
    }

    /**
     * Create the items for the game menu, configure them and add them to the
     * menu.
     *
     * @param mainFrame The main frame of the application.
     */
    private void fillBoardMenu(final MainFrame mainFrame) {
        Configuration configuration = Configuration.getInstance();

        // Create the menus items
        JMenuItem triangularItem = new JRadioButtonMenuItem(new Triangular(configuration.getText(Configuration.KEY_MENU_BOARD_TRIANGULAR), mainFrame));
        JMenuItem triangular14Item = new JRadioButtonMenuItem(new Triangular14(configuration.getText(Configuration.KEY_MENU_BOARD_TRIANGULAR14), mainFrame));
        JMenuItem squareItem = new JRadioButtonMenuItem(new Square(configuration.getText(Configuration.KEY_MENU_BOARD_SQUARE), mainFrame));
        JMenuItem pentagonalItem = new JRadioButtonMenuItem(new Pentagonal(configuration.getText(Configuration.KEY_MENU_BOARD_PENTAGONAL), mainFrame));
        JMenuItem hexagonalItem = new JRadioButtonMenuItem(new Hexagonal(configuration.getText(Configuration.KEY_MENU_BOARD_HEXAGONAL), mainFrame));
        JMenuItem octosquareItem = new JRadioButtonMenuItem(new Octosquare(configuration.getText(Configuration.KEY_MENU_BOARD_OCTOSQUARE), mainFrame));
        JMenuItem parquetItem = new JRadioButtonMenuItem(new Parquet(configuration.getText(Configuration.KEY_MENU_BOARD_PARQUET), mainFrame));
        JMenuItem threeDGridItem = new JRadioButtonMenuItem(configuration.getText(Configuration.KEY_MENU_BOARD_3DGRID));

        // Configure the items
        threeDGridItem.setEnabled(false);
        threeDGridItem.setVisible(false);

        // Create the buttons groups
        ButtonGroup shapes = new ButtonGroup();
        shapes.add(triangularItem);
        shapes.add(triangular14Item);
        shapes.add(squareItem);
        shapes.add(pentagonalItem);
        shapes.add(hexagonalItem);
        shapes.add(octosquareItem);
        shapes.add(parquetItem);
        shapes.add(threeDGridItem);
        if (configuration.getString(Configuration.KEY_USER_SHAPE).equals(Configuration.SHAPE_TRIANGULAR)) {
            triangularItem.setSelected(true);
        } else if (configuration.getString(Configuration.KEY_USER_SHAPE).equals(Configuration.SHAPE_SQUARE)) {
            squareItem.setSelected(true);
        } else if (configuration.getString(Configuration.KEY_USER_SHAPE).equals(Configuration.SHAPE_HEXAGONAL)) {
            hexagonalItem.setSelected(true);
        } else if (configuration.getString(Configuration.KEY_USER_SHAPE).equals(Configuration.SHAPE_OCTOSQUARE)) {
            octosquareItem.setSelected(true);
        } else if (configuration.getString(Configuration.KEY_USER_SHAPE).equals(Configuration.SHAPE_PARQUET)) {
            parquetItem.setSelected(true);
        }

        // Enable selected items

        // Add the items to the menus
        boardMenu.add(triangularItem);
        boardMenu.add(triangular14Item);
        boardMenu.add(squareItem);
        boardMenu.add(pentagonalItem);
        boardMenu.add(hexagonalItem);
        boardMenu.add(octosquareItem);
        boardMenu.add(parquetItem);
        boardMenu.add(threeDGridItem);
    }

    /**
     * Create the items for the display menu, configure them and add them to
     * the menu.
     *
     * @param mainFrame The JMines main frame.
     */
    private void fillDisplayMenu(final MainFrame mainFrame) {
        Configuration configuration = Configuration.getInstance();

        // Create the menus items
        JMenuItem antialiasingItem = new JCheckBoxMenuItem(new Antialiasing(configuration.getText(Configuration.KEY_MENU_DISPLAY_ANTIALIASING), mainFrame));
        JMenuItem shadowItem = new JCheckBoxMenuItem(new Shadow(configuration.getText(Configuration.KEY_MENU_DISPLAY_SHADOW), mainFrame));

        // Configure the items
        antialiasingItem.setSelected(configuration.getBoolean(Configuration.KEY_USER_ANTIALIASING));
        shadowItem.setSelected(configuration.getBoolean(Configuration.KEY_USER_SHADOWED));

        // Enable selected items
        mainFrame.getMainPanel().getGamePanel().setAntialiased(antialiasingItem.isSelected());
        mainFrame.getMainPanel().getGamePanel().setShadowed(shadowItem.isSelected());

        // Add the items to the menus
        displayMenu.add(antialiasingItem);
        displayMenu.add(shadowItem);

        displayMenu.addSeparator();

        fillDisplayChangeLookAndFeelMenu(mainFrame);
        fillDisplayChangeBgColorMenu(mainFrame);
        fillDisplayChangeButtonsColorMenu(mainFrame);
        fillDisplayChangeLCDColorMenu(mainFrame);
    }

    /**
     * Create the items for the change look and feel menu, configure them and
     * add them to the menu.
     *
     * @param mainFrame The JMines main frame.
     */
    private void fillDisplayChangeLookAndFeelMenu(final MainFrame mainFrame) {
        Configuration configuration = Configuration.getInstance();

        // Create the menus items
        ButtonGroup lafGroup = new ButtonGroup();
        for (LookAndFeel laf : configuration.getAvailableLookAndFeels()) {
            JMenuItem item = new JRadioButtonMenuItem(new ChangeLookAndFeel(laf, mainFrame));
            lafGroup.add(item);
            changeLookAndFeelMenu.add(item);

            if (UIManager.getLookAndFeel().getName().equals(item.getActionCommand())) {
                item.setSelected(true);
            }
        }

        // Add the items to the menus
        displayMenu.add(changeLookAndFeelMenu);
    }

    /**
     * Create the items for the change background color menu, configure them
     * and add them to the menu.
     *
     * @param mainFrame The main frame of the application.
     */
    private void fillDisplayChangeBgColorMenu(final MainFrame mainFrame) {
        final int hexadecimalBasis = 16;

        // Create the menus items
        JMenuItem lookAndFeelDependentBgColorItem = new JRadioButtonMenuItem(new ChangeBackgroundColor(ChangeColor.LOOK_AND_FEEL_DEPENDENT, mainFrame));
        JMenuItem blackBgColorItem = new JRadioButtonMenuItem(new ChangeBackgroundColor(ChangeColor.BLACK, mainFrame));
        JMenuItem blueBgColorItem = new JRadioButtonMenuItem(new ChangeBackgroundColor(ChangeColor.BLUE, mainFrame));
        JMenuItem cyanBgColorItem = new JRadioButtonMenuItem(new ChangeBackgroundColor(ChangeColor.CYAN, mainFrame));
        JMenuItem darkGrayBgColorItem = new JRadioButtonMenuItem(new ChangeBackgroundColor(ChangeColor.DARK_GRAY, mainFrame));
        JMenuItem grayBgColorItem = new JRadioButtonMenuItem(new ChangeBackgroundColor(ChangeColor.GRAY, mainFrame));
        JMenuItem greenBgColorItem = new JRadioButtonMenuItem(new ChangeBackgroundColor(ChangeColor.GREEN, mainFrame));
        JMenuItem lightGrayBgColorItem = new JRadioButtonMenuItem(new ChangeBackgroundColor(ChangeColor.LIGHT_GRAY, mainFrame));
        JMenuItem magentaBgColorItem = new JRadioButtonMenuItem(new ChangeBackgroundColor(ChangeColor.MAGENTA, mainFrame));
        JMenuItem orangeBgColorItem = new JRadioButtonMenuItem(new ChangeBackgroundColor(ChangeColor.ORANGE, mainFrame));
        JMenuItem pinkBgColorItem = new JRadioButtonMenuItem(new ChangeBackgroundColor(ChangeColor.PINK, mainFrame));
        JMenuItem redBgColorItem = new JRadioButtonMenuItem(new ChangeBackgroundColor(ChangeColor.RED, mainFrame));
        JMenuItem whiteBgColorItem = new JRadioButtonMenuItem(new ChangeBackgroundColor(ChangeColor.WHITE, mainFrame));
        JMenuItem yellowBgColorItem = new JRadioButtonMenuItem(new ChangeBackgroundColor(ChangeColor.YELLOW, mainFrame));
        JMenuItem customBgColorItem = new JRadioButtonMenuItem(new ChangeBackgroundColor(ChangeColor.CUSTOM, mainFrame));

        // Configure the items
        try {
            Byte id = new Byte(Configuration.getInstance().getString(Configuration.KEY_USER_BGCOLOR));
            if (id.equals(lookAndFeelDependentBgColorItem.getAction().getValue(ChangeColor.ID))) {
                lookAndFeelDependentBgColorItem.setSelected(true);
            } else if (id.equals(blackBgColorItem.getAction().getValue(ChangeColor.ID))) {
                blackBgColorItem.setSelected(true);
            } else if (id.equals(blueBgColorItem.getAction().getValue(ChangeColor.ID))) {
                blueBgColorItem.setSelected(true);
            } else if (id.equals(cyanBgColorItem.getAction().getValue(ChangeColor.ID))) {
                cyanBgColorItem.setSelected(true);
            } else if (id.equals(darkGrayBgColorItem.getAction().getValue(ChangeColor.ID))) {
                darkGrayBgColorItem.setSelected(true);
            } else if (id.equals(grayBgColorItem.getAction().getValue(ChangeColor.ID))) {
                grayBgColorItem.setSelected(true);
            } else if (id.equals(greenBgColorItem.getAction().getValue(ChangeColor.ID))) {
                greenBgColorItem.setSelected(true);
            } else if (id.equals(lightGrayBgColorItem.getAction().getValue(ChangeColor.ID))) {
                lightGrayBgColorItem.setSelected(true);
            } else if (id.equals(magentaBgColorItem.getAction().getValue(ChangeColor.ID))) {
                magentaBgColorItem.setSelected(true);
            } else if (id.equals(orangeBgColorItem.getAction().getValue(ChangeColor.ID))) {
                orangeBgColorItem.setSelected(true);
            } else if (id.equals(pinkBgColorItem.getAction().getValue(ChangeColor.ID))) {
                pinkBgColorItem.setSelected(true);
            } else if (id.equals(redBgColorItem.getAction().getValue(ChangeColor.ID))) {
                redBgColorItem.setSelected(true);
            } else if (id.equals(whiteBgColorItem.getAction().getValue(ChangeColor.ID))) {
                whiteBgColorItem.setSelected(true);
            } else if (id.equals(yellowBgColorItem.getAction().getValue(ChangeColor.ID))) {
                yellowBgColorItem.setSelected(true);
            }
        } catch (NumberFormatException e) {
            try {
                java.awt.Color color = new java.awt.Color(Configuration.getInstance().getInt(Configuration.KEY_USER_BGCOLOR, hexadecimalBasis));
                mainFrame.getMainPanel().setBackground(color);
                customBgColorItem.setSelected(true);
            } catch (NumberFormatException e1) {
                lightGrayBgColorItem.setSelected(true);
            }
        }

        // Create the buttons groups
        ButtonGroup bgColorGroup = new ButtonGroup();
        bgColorGroup.add(lookAndFeelDependentBgColorItem);
        bgColorGroup.add(blackBgColorItem);
        bgColorGroup.add(blueBgColorItem);
        bgColorGroup.add(cyanBgColorItem);
        bgColorGroup.add(darkGrayBgColorItem);
        bgColorGroup.add(grayBgColorItem);
        bgColorGroup.add(greenBgColorItem);
        bgColorGroup.add(lightGrayBgColorItem);
        bgColorGroup.add(magentaBgColorItem);
        bgColorGroup.add(orangeBgColorItem);
        bgColorGroup.add(pinkBgColorItem);
        bgColorGroup.add(redBgColorItem);
        bgColorGroup.add(whiteBgColorItem);
        bgColorGroup.add(yellowBgColorItem);
        bgColorGroup.add(customBgColorItem);

        // Enable selected items
        for (Enumeration<AbstractButton> e = bgColorGroup.getElements(); e.hasMoreElements();) {
            AbstractButton button = e.nextElement();
            if (button.isSelected() && !(button == customBgColorItem)) {
                button.doClick();
            }
        }

        // Add the items to the menus
        changeBgColorMenu.add(lookAndFeelDependentBgColorItem);
        changeBgColorMenu.add(blackBgColorItem);
        changeBgColorMenu.add(blueBgColorItem);
        changeBgColorMenu.add(cyanBgColorItem);
        changeBgColorMenu.add(darkGrayBgColorItem);
        changeBgColorMenu.add(grayBgColorItem);
        changeBgColorMenu.add(greenBgColorItem);
        changeBgColorMenu.add(lightGrayBgColorItem);
        changeBgColorMenu.add(magentaBgColorItem);
        changeBgColorMenu.add(orangeBgColorItem);
        changeBgColorMenu.add(pinkBgColorItem);
        changeBgColorMenu.add(redBgColorItem);
        changeBgColorMenu.add(whiteBgColorItem);
        changeBgColorMenu.add(yellowBgColorItem);
        changeBgColorMenu.add(customBgColorItem);
        displayMenu.add(changeBgColorMenu);
    }

    /**
     * Create the items for the change buttons color menu, configure them and
     * add them to the menu.
     *
     * @param mainFrame The main frame of the application.
     */
    private void fillDisplayChangeButtonsColorMenu(final MainFrame mainFrame) {
        final int hexadecimalBasis = 16;

        // Create the menus items
        JMenuItem lookAndFeelDependentButtonsColorItem = new JRadioButtonMenuItem(new ChangeButtonsColor(ChangeColor.LOOK_AND_FEEL_DEPENDENT, mainFrame));
        JMenuItem blackButtonsColorItem = new JRadioButtonMenuItem(new ChangeButtonsColor(ChangeColor.BLACK, mainFrame));
        JMenuItem blueButtonsColorItem = new JRadioButtonMenuItem(new ChangeButtonsColor(ChangeColor.BLUE, mainFrame));
        JMenuItem cyanButtonsColorItem = new JRadioButtonMenuItem(new ChangeButtonsColor(ChangeColor.CYAN, mainFrame));
        JMenuItem darkGrayButtonsColorItem = new JRadioButtonMenuItem(new ChangeButtonsColor(ChangeColor.DARK_GRAY, mainFrame));
        JMenuItem grayButtonsColorItem = new JRadioButtonMenuItem(new ChangeButtonsColor(ChangeColor.GRAY, mainFrame));
        JMenuItem greenButtonsColorItem = new JRadioButtonMenuItem(new ChangeButtonsColor(ChangeColor.GREEN, mainFrame));
        JMenuItem lightGrayButtonsColorItem = new JRadioButtonMenuItem(new ChangeButtonsColor(ChangeColor.LIGHT_GRAY, mainFrame));
        JMenuItem magentaButtonsColorItem = new JRadioButtonMenuItem(new ChangeButtonsColor(ChangeColor.MAGENTA, mainFrame));
        JMenuItem orangeButtonsColorItem = new JRadioButtonMenuItem(new ChangeButtonsColor(ChangeColor.ORANGE, mainFrame));
        JMenuItem pinkButtonsColorItem = new JRadioButtonMenuItem(new ChangeButtonsColor(ChangeColor.PINK, mainFrame));
        JMenuItem redButtonsColorItem = new JRadioButtonMenuItem(new ChangeButtonsColor(ChangeColor.RED, mainFrame));
        JMenuItem whiteButtonsColorItem = new JRadioButtonMenuItem(new ChangeButtonsColor(ChangeColor.WHITE, mainFrame));
        JMenuItem yellowButtonsColorItem = new JRadioButtonMenuItem(new ChangeButtonsColor(ChangeColor.YELLOW, mainFrame));
        JMenuItem customButtonsColorItem = new JRadioButtonMenuItem(new ChangeButtonsColor(ChangeColor.CUSTOM, mainFrame));

        // Configure the items
        try {
            Byte id = new Byte(Configuration.getInstance().getString(Configuration.KEY_USER_BUTTONSCOLOR));
            if (id.equals(lookAndFeelDependentButtonsColorItem.getAction().getValue(ChangeColor.ID))) {
                lookAndFeelDependentButtonsColorItem.setSelected(true);
            } else if (id.equals(blackButtonsColorItem.getAction().getValue(ChangeColor.ID))) {
                blackButtonsColorItem.setSelected(true);
            } else if (id.equals(blueButtonsColorItem.getAction().getValue(ChangeColor.ID))) {
                blueButtonsColorItem.setSelected(true);
            } else if (id.equals(cyanButtonsColorItem.getAction().getValue(ChangeColor.ID))) {
                cyanButtonsColorItem.setSelected(true);
            } else if (id.equals(darkGrayButtonsColorItem.getAction().getValue(ChangeColor.ID))) {
                darkGrayButtonsColorItem.setSelected(true);
            } else if (id.equals(grayButtonsColorItem.getAction().getValue(ChangeColor.ID))) {
                grayButtonsColorItem.setSelected(true);
            } else if (id.equals(greenButtonsColorItem.getAction().getValue(ChangeColor.ID))) {
                greenButtonsColorItem.setSelected(true);
            } else if (id.equals(lightGrayButtonsColorItem.getAction().getValue(ChangeColor.ID))) {
                lightGrayButtonsColorItem.setSelected(true);
            } else if (id.equals(magentaButtonsColorItem.getAction().getValue(ChangeColor.ID))) {
                magentaButtonsColorItem.setSelected(true);
            } else if (id.equals(orangeButtonsColorItem.getAction().getValue(ChangeColor.ID))) {
                orangeButtonsColorItem.setSelected(true);
            } else if (id.equals(pinkButtonsColorItem.getAction().getValue(ChangeColor.ID))) {
                pinkButtonsColorItem.setSelected(true);
            } else if (id.equals(redButtonsColorItem.getAction().getValue(ChangeColor.ID))) {
                redButtonsColorItem.setSelected(true);
            } else if (id.equals(whiteButtonsColorItem.getAction().getValue(ChangeColor.ID))) {
                whiteButtonsColorItem.setSelected(true);
            } else if (id.equals(yellowButtonsColorItem.getAction().getValue(ChangeColor.ID))) {
                yellowButtonsColorItem.setSelected(true);
            }
        } catch (NumberFormatException e) {
            try {
                java.awt.Color color = new java.awt.Color(Configuration.getInstance().getInt(Configuration.KEY_USER_BUTTONSCOLOR, hexadecimalBasis));
                mainFrame.getMainPanel().getGamePanel().setButtonsColor(color);
                customButtonsColorItem.setSelected(true);
            } catch (NumberFormatException e1) {
                lightGrayButtonsColorItem.setSelected(true);
            }
        }

        // Create the buttons groups
        ButtonGroup buttonsColorGroup = new ButtonGroup();
        buttonsColorGroup.add(lookAndFeelDependentButtonsColorItem);
        buttonsColorGroup.add(blackButtonsColorItem);
        buttonsColorGroup.add(blueButtonsColorItem);
        buttonsColorGroup.add(cyanButtonsColorItem);
        buttonsColorGroup.add(darkGrayButtonsColorItem);
        buttonsColorGroup.add(grayButtonsColorItem);
        buttonsColorGroup.add(greenButtonsColorItem);
        buttonsColorGroup.add(lightGrayButtonsColorItem);
        buttonsColorGroup.add(magentaButtonsColorItem);
        buttonsColorGroup.add(orangeButtonsColorItem);
        buttonsColorGroup.add(pinkButtonsColorItem);
        buttonsColorGroup.add(redButtonsColorItem);
        buttonsColorGroup.add(whiteButtonsColorItem);
        buttonsColorGroup.add(yellowButtonsColorItem);
        buttonsColorGroup.add(customButtonsColorItem);

        // Enable selected items
        for (Enumeration<AbstractButton> e = buttonsColorGroup.getElements(); e.hasMoreElements();) {
            AbstractButton button = e.nextElement();
            if (button.isSelected() && !(button == customButtonsColorItem)) {
                button.doClick();
            }
        }

        // Add the items to the menus
        changeButtonsColorMenu.add(lookAndFeelDependentButtonsColorItem);
        changeButtonsColorMenu.add(blackButtonsColorItem);
        changeButtonsColorMenu.add(blueButtonsColorItem);
        changeButtonsColorMenu.add(cyanButtonsColorItem);
        changeButtonsColorMenu.add(darkGrayButtonsColorItem);
        changeButtonsColorMenu.add(grayButtonsColorItem);
        changeButtonsColorMenu.add(greenButtonsColorItem);
        changeButtonsColorMenu.add(lightGrayButtonsColorItem);
        changeButtonsColorMenu.add(magentaButtonsColorItem);
        changeButtonsColorMenu.add(orangeButtonsColorItem);
        changeButtonsColorMenu.add(pinkButtonsColorItem);
        changeButtonsColorMenu.add(redButtonsColorItem);
        changeButtonsColorMenu.add(whiteButtonsColorItem);
        changeButtonsColorMenu.add(yellowButtonsColorItem);
        changeButtonsColorMenu.add(customButtonsColorItem);
        displayMenu.add(changeButtonsColorMenu);
    }

    /**
     * Create the items for the change LCD color menu, configure them and add
     * them to the menu.
     *
     * @param mainFrame The main frame of the application.
     */
    private void fillDisplayChangeLCDColorMenu(final MainFrame mainFrame) {
        final int hexadecimalBasis = 16;

        // Create the menus items
        JMenuItem originalLCDColorItem = new JRadioButtonMenuItem(new ChangeLCDColor(ChangeColor.ORIGINAL, mainFrame));
        JMenuItem blackLCDColorItem = new JRadioButtonMenuItem(new ChangeLCDColor(ChangeColor.BLACK, mainFrame));
        JMenuItem blueLCDColorItem = new JRadioButtonMenuItem(new ChangeLCDColor(ChangeColor.BLUE, mainFrame));
        JMenuItem cyanLCDColorItem = new JRadioButtonMenuItem(new ChangeLCDColor(ChangeColor.CYAN, mainFrame));
        JMenuItem darkGrayLCDColorItem = new JRadioButtonMenuItem(new ChangeLCDColor(ChangeColor.DARK_GRAY, mainFrame));
        JMenuItem grayLCDColorItem = new JRadioButtonMenuItem(new ChangeLCDColor(ChangeColor.GRAY, mainFrame));
        JMenuItem greenLCDColorItem = new JRadioButtonMenuItem(new ChangeLCDColor(ChangeColor.GREEN, mainFrame));
        JMenuItem lightGrayLCDColorItem = new JRadioButtonMenuItem(new ChangeLCDColor(ChangeColor.LIGHT_GRAY, mainFrame));
        JMenuItem magentaLCDColorItem = new JRadioButtonMenuItem(new ChangeLCDColor(ChangeColor.MAGENTA, mainFrame));
        JMenuItem orangeLCDColorItem = new JRadioButtonMenuItem(new ChangeLCDColor(ChangeColor.ORANGE, mainFrame));
        JMenuItem pinkLCDColorItem = new JRadioButtonMenuItem(new ChangeLCDColor(ChangeColor.PINK, mainFrame));
        JMenuItem redLCDColorItem = new JRadioButtonMenuItem(new ChangeLCDColor(ChangeColor.RED, mainFrame));
        JMenuItem whiteLCDColorItem = new JRadioButtonMenuItem(new ChangeLCDColor(ChangeColor.WHITE, mainFrame));
        JMenuItem yellowLCDColorItem = new JRadioButtonMenuItem(new ChangeLCDColor(ChangeColor.YELLOW, mainFrame));
        JMenuItem customLCDColorItem = new JRadioButtonMenuItem(new ChangeLCDColor(ChangeColor.CUSTOM, mainFrame));

        // Configure the items
        try {
            Byte id = new Byte(Configuration.getInstance().getString(Configuration.KEY_USER_LCDCOLOR));
            if (id.equals(originalLCDColorItem.getAction().getValue(ChangeColor.ID))) {
                originalLCDColorItem.setSelected(true);
            } else if (id.equals(blackLCDColorItem.getAction().getValue(ChangeColor.ID))) {
                blackLCDColorItem.setSelected(true);
            } else if (id.equals(blueLCDColorItem.getAction().getValue(ChangeColor.ID))) {
                blueLCDColorItem.setSelected(true);
            } else if (id.equals(cyanLCDColorItem.getAction().getValue(ChangeColor.ID))) {
                cyanLCDColorItem.setSelected(true);
            } else if (id.equals(darkGrayLCDColorItem.getAction().getValue(ChangeColor.ID))) {
                darkGrayLCDColorItem.setSelected(true);
            } else if (id.equals(grayLCDColorItem.getAction().getValue(ChangeColor.ID))) {
                grayLCDColorItem.setSelected(true);
            } else if (id.equals(greenLCDColorItem.getAction().getValue(ChangeColor.ID))) {
                greenLCDColorItem.setSelected(true);
            } else if (id.equals(lightGrayLCDColorItem.getAction().getValue(ChangeColor.ID))) {
                lightGrayLCDColorItem.setSelected(true);
            } else if (id.equals(magentaLCDColorItem.getAction().getValue(ChangeColor.ID))) {
                magentaLCDColorItem.setSelected(true);
            } else if (id.equals(orangeLCDColorItem.getAction().getValue(ChangeColor.ID))) {
                orangeLCDColorItem.setSelected(true);
            } else if (id.equals(pinkLCDColorItem.getAction().getValue(ChangeColor.ID))) {
                pinkLCDColorItem.setSelected(true);
            } else if (id.equals(redLCDColorItem.getAction().getValue(ChangeColor.ID))) {
                redLCDColorItem.setSelected(true);
            } else if (id.equals(whiteLCDColorItem.getAction().getValue(ChangeColor.ID))) {
                whiteLCDColorItem.setSelected(true);
            } else if (id.equals(yellowLCDColorItem.getAction().getValue(ChangeColor.ID))) {
                yellowLCDColorItem.setSelected(true);
            }
        } catch (NumberFormatException e) {
            try {
                java.awt.Color color = new java.awt.Color(Configuration.getInstance().getInt(Configuration.KEY_USER_LCDCOLOR, hexadecimalBasis));
                LCDPanel.changeFiguresColor(color);
                customLCDColorItem.setSelected(true);
            } catch (NumberFormatException e1) {
                originalLCDColorItem.setSelected(true);
            }
        }

        // Create the buttons groups
        ButtonGroup lcdColorGroup = new ButtonGroup();
        lcdColorGroup.add(originalLCDColorItem);
        lcdColorGroup.add(blackLCDColorItem);
        lcdColorGroup.add(blueLCDColorItem);
        lcdColorGroup.add(cyanLCDColorItem);
        lcdColorGroup.add(darkGrayLCDColorItem);
        lcdColorGroup.add(grayLCDColorItem);
        lcdColorGroup.add(greenLCDColorItem);
        lcdColorGroup.add(lightGrayLCDColorItem);
        lcdColorGroup.add(magentaLCDColorItem);
        lcdColorGroup.add(orangeLCDColorItem);
        lcdColorGroup.add(pinkLCDColorItem);
        lcdColorGroup.add(redLCDColorItem);
        lcdColorGroup.add(whiteLCDColorItem);
        lcdColorGroup.add(yellowLCDColorItem);
        lcdColorGroup.add(customLCDColorItem);

        // Enable selected items
        for (Enumeration<AbstractButton> e = lcdColorGroup.getElements(); e.hasMoreElements();) {
            AbstractButton button = e.nextElement();
            if (button.isSelected() && !(button == customLCDColorItem)) {
                button.doClick();
            }
        }

        // Add the items to the menus
        changeLCDColorMenu.add(originalLCDColorItem);
        changeLCDColorMenu.add(blackLCDColorItem);
        changeLCDColorMenu.add(blueLCDColorItem);
        changeLCDColorMenu.add(cyanLCDColorItem);
        changeLCDColorMenu.add(darkGrayLCDColorItem);
        changeLCDColorMenu.add(grayLCDColorItem);
        changeLCDColorMenu.add(greenLCDColorItem);
        changeLCDColorMenu.add(lightGrayLCDColorItem);
        changeLCDColorMenu.add(magentaLCDColorItem);
        changeLCDColorMenu.add(orangeLCDColorItem);
        changeLCDColorMenu.add(pinkLCDColorItem);
        changeLCDColorMenu.add(redLCDColorItem);
        changeLCDColorMenu.add(whiteLCDColorItem);
        changeLCDColorMenu.add(yellowLCDColorItem);
        changeLCDColorMenu.add(customLCDColorItem);
        displayMenu.add(changeLCDColorMenu);
    }

    /**
     * Create the items for the language menu, configure them and add them to
     * the menu.
     *
     * @param mainFrame The JMines main frame.
     */
    private void fillLanguageMenu(final MainFrame mainFrame) {
        Configuration configuration = Configuration.getInstance();

        ButtonGroup group = new ButtonGroup();
        for (Locale locale : configuration.getAvailableLocales()) {
            JMenuItem item = new JRadioButtonMenuItem(new Language(locale, mainFrame));
            try {
                if (Configuration.getInstance().getString(Configuration.KEY_USER_LOCALE).equals(locale)) {
                    item.setSelected(true);
                }
            } catch (MissingResourceException e) {
                // do nothing ...
                System.currentTimeMillis();
            }

            languageMenu.add(item);
            group.add(item);

            try {
                if (locale.equals(new Locale(configuration.getString(Configuration.KEY_USER_LOCALE)))) {
                    item.setSelected(true);
                }
            } catch (MissingResourceException e) {
                if (locale.equals(Locale.getDefault())) {
                    item.setSelected(true);
                }
            }
        }
    }

    /**
     * Create the items for the other menu, configure them and add them to
     * the menu.
     *
     * @param mainFrame The main frame of the application.
     */
    private void fillOtherMenu(final MainFrame mainFrame) {
        Configuration configuration = Configuration.getInstance();

        // Create the menus items
        JMenuItem saveBoardItem = new JMenuItem(new SaveBoard(configuration.getText(Configuration.KEY_MENU_OTHER_SAVEBOARD), mainFrame));
        JMenuItem loadBoardItem = new JMenuItem(new LoadBoard(configuration.getText(Configuration.KEY_MENU_OTHER_LOADBOARD), mainFrame));
        JMenuItem saveScreenshotItem = new JMenuItem(new SaveScreenshot(configuration.getText(Configuration.KEY_MENU_OTHER_SAVESCREENSHOT), mainFrame));
        saveVideoAction = new SaveVideo(configuration.getText(Configuration.KEY_MENU_OTHER_SAVEVIDEO), mainFrame);
        saveVideoItem = new JCheckBoxMenuItem(saveVideoAction);
        JMenuItem loadVideoItem = new JMenuItem(new LoadVideo(configuration.getText(Configuration.KEY_MENU_OTHER_LOADVIDEO), mainFrame));

        // Add the items to the menus
        otherMenu.add(saveBoardItem);
        otherMenu.add(loadBoardItem);
        otherMenu.addSeparator();
        otherMenu.add(saveScreenshotItem);
        otherMenu.addSeparator();
        otherMenu.add(saveVideoItem);
        otherMenu.add(loadVideoItem);
    }

    /**
     * Create the items for the robot menu, configure them and add them to
     * the menu.
     *
     * @param mainFrame The main frame of the application.
     */
    private void fillRobotMenu(final MainFrame mainFrame) {
        Configuration configuration = Configuration.getInstance();

        // Create the menus items
        JMenuItem nameItem = new JMenuItem(new Name(configuration.getText(Configuration.KEY_MENU_ROBOT_NAME), mainFrame));
        JMenuItem cheatItem = new JCheckBoxMenuItem(new Cheat(configuration.getText(Configuration.KEY_MENU_ROBOT_CHEAT), mainFrame));
        JMenuItem realClicksItem = new JCheckBoxMenuItem(new RealClicks(configuration.getText(Configuration.KEY_MENU_ROBOT_REALCLICKS), mainFrame));
        JMenuItem randomTriesItem = new JCheckBoxMenuItem(new RandomTries(configuration.getText(Configuration.KEY_MENU_ROBOT_RANDOMTRIES), mainFrame));
        JMenuItem statisticsItem = new JMenuItem(new Statistics(configuration.getText(Configuration.KEY_MENU_ROBOT_STATISTICS), mainFrame));
        JMenuItem playItem = new JMenuItem(new Play(configuration.getText(Configuration.KEY_MENU_ROBOT_PLAY), mainFrame));
        JMenuItem helpItem = new JCheckBoxMenuItem(new Help(configuration.getText(Configuration.KEY_MENU_ROBOT_HELP), mainFrame));

        // Configure the items
        cheatItem.setSelected(configuration.getBoolean(Configuration.KEY_ROBOT_CHEAT));
        realClicksItem.setSelected(configuration.getBoolean(Configuration.KEY_ROBOT_REALCLICKS));
        randomTriesItem.setSelected(configuration.getBoolean(Configuration.KEY_ROBOT_RANDOMTRIES));
        helpItem.setSelected(configuration.getBoolean(Configuration.KEY_ROBOT_HELP));

        // Add the items to the menus
        robotMenu.add(nameItem);
        robotMenu.add(cheatItem);
        robotMenu.add(realClicksItem);
        robotMenu.add(randomTriesItem);
        robotMenu.add(statisticsItem);
        robotMenu.addSeparator();
        robotMenu.add(playItem);
        robotMenu.add(helpItem);
    }

    /**
     * Create the items for the info menu, configure them and add them to the
     * menu.
     *
     * @param mainFrame The main frame of the application.
     */
    private void fillInfoMenu(final MainFrame mainFrame) {
        Configuration configuration = Configuration.getInstance();

        // Create the menus items
        JMenuItem aboutItem = new JMenuItem(new About(configuration.getText(Configuration.KEY_MENU_INFO_ABOUT), mainFrame));
        JMenuItem thanksItem = new JMenuItem(new Thanks(configuration.getText(Configuration.KEY_MENU_INFO_THANKS), mainFrame));

        // Add the items to the menu
        infoMenu.add(aboutItem);
        infoMenu.add(thanksItem);
    }

    /**
     * Initialize the component by creating and adding menus and items.
     *
     * @param mainFrame The JMines main frame.
     */
    public final void init(final MainFrame mainFrame) {
        final MainPanel mainPanel = mainFrame.getMainPanel();

        createMenus(mainFrame);

        // Add the menus
        add(gameMenu);
        add(boardMenu);
        add(displayMenu);
        add(languageMenu);
        add(otherMenu);
        add(robotMenu);
        add(infoMenu);

        // Add the mouse listener to each menu item
        for (int i = 0; i < getMenuCount(); i++) {
            JMenu menu = getMenu(i);
            if (menu != null) {

                for (int j = 0; j < menu.getItemCount(); j++) {
                    final JMenuItem item = menu.getItem(j);
                    if (item != null && item.getAction() != null) {
                        item.addMouseListener(new MouseListenerForMenuItem(item, mainPanel));
                    } else if (item instanceof JMenu) {
                        for (int k = 0; k < ((JMenu) item).getItemCount(); k++) {
                            final JMenuItem subItem = ((JMenu) item).getItem(k);
                            if (subItem != null) {
                                subItem.addMouseListener(new MouseListenerForMenuItem(subItem, mainPanel));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Disabled the saveVideo menu item and JMines action.
     */
    public final void disableSaveVideo() {
        saveVideoItem.setSelected(false);
        saveVideoAction.setChecked(false);
    }

}
