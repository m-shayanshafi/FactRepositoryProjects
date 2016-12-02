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
package jmines.view.persistence;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.jar.JarEntry;

import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileFilter;

import jmines.control.actions.language.Language;
import jmines.view.dialogs.ConfigurationFrame;

/**
 * The class used to manage user configuration, default configuration and "real
 * time" configuration.
 *
 * @author Zleurtor
 */
public final class Configuration {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The name of the default configuration file.
     */
    private static final String DEFAULT_CONFIGURATION_FILE_NAME = "default.properties";
    /**
     * The name of the directory containing the user configuration file.
     */
    private static final String USER_CONFIGURATION_DIRECTORY = ".zleurtor";
    /**
     * The name of the user configuration file.
     */
    private static final String USER_CONFIGURATION_FILE_NAME = "jmines.properties";
    /**
     * The name of the class used to encrypt the user configuration file.
     */
    private static final String USER_CONFIGURATION_FILE_ENCRYPTOR_CLASS_NAME = "compressor.huffman.HuffmanFileOutputStream";
    /**
     * The name of the class used to decrypt the user configuration file.
     */
    private static final String USER_CONFIGURATION_FILE_DECRYPTOR_CLASS_NAME = "compressor.huffman.HuffmanFileInputStream";
    /**
     * The name of the exception that can be thrown by the encryptor/descryptor
     * of the user configuration file.
     */
    private static final String USER_CONFIGURATION_FILE_CRYPTOR_EXCEPTION_NAME = "compressor.InputException";
    /**
     * The name of the compressor library. This library is used to crypt the
     * configuration file.
     */
    private static final String COMPRESSOR_LIBRARY_FILENAME = "Compressor.jar";
    /**
     * The unique instance of Configuration.
     */
    private static final Configuration INSTANCE;

    /**
     * The key for the name of the file containing the game tiles.
     */
    public static final String KEY_TILES_FILENAME = "tiles.filename";
    /**
     * The key for the tiles width.
     */
    public static final String KEY_TILES_WIDTH = "tiles.width";
    /**
     * The key for the tiles height.
     */
    public static final String KEY_TILES_HEIGHT = "tiles.height";
    /**
     * The key for the index of the "0" tile.
     */
    public static final String KEY_TILES_0 = "tiles.0";
    /**
     * The key for the index of the "1" tile.
     */
    public static final String KEY_TILES_1 = "tiles.1";
    /**
     * The key for the index of the "2" tile.
     */
    public static final String KEY_TILES_2 = "tiles.2";
    /**
     * The key for the index of the "3" tile.
     */
    public static final String KEY_TILES_3 = "tiles.3";
    /**
     * The key for the index of the "4" tile.
     */
    public static final String KEY_TILES_4 = "tiles.4";
    /**
     * The key for the index of the "5" tile.
     */
    public static final String KEY_TILES_5 = "tiles.5";
    /**
     * The key for the index of the "6" tile.
     */
    public static final String KEY_TILES_6 = "tiles.6";
    /**
     * The key for the index of the "7" tile.
     */
    public static final String KEY_TILES_7 = "tiles.7";
    /**
     * The key for the index of the "8" tile.
     */
    public static final String KEY_TILES_8 = "tiles.8";
    /**
     * The key for the index of the "9" tile.
     */
    public static final String KEY_TILES_9 = "tiles.9";
    /**
     * The key for the index of the "10" tile.
     */
    public static final String KEY_TILES_10 = "tiles.10";
    /**
     * The key for the index of the "11" tile.
     */
    public static final String KEY_TILES_11 = "tiles.11";
    /**
     * The key for the index of the "12" tile.
     */
    public static final String KEY_TILES_12 = "tiles.12";
    /**
     * The key for the index of the "13" tile.
     */
    public static final String KEY_TILES_13 = "tiles.13";
    /**
     * The key for the index of the "14" tile.
     */
    public static final String KEY_TILES_14 = "tiles.14";
    /**
     * The key for the index of the "15" tile.
     */
    public static final String KEY_TILES_15 = "tiles.15";
    /**
     * The key for the index of the flag tile.
     */
    public static final String KEY_TILES_FLAG = "tiles.flag";
    /**
     * The key for the index of the mark "?" tile.
     */
    public static final String KEY_TILES_MARK = "tiles.mark";
    /**
     * The key for the index of the mine tile.
     */
    public static final String KEY_TILES_MINE = "tiles.mine";
    /**
     * The key for the index of the exploded mine tile.
     */
    public static final String KEY_TILES_EXPLODED = "tiles.exploded";
    /**
     * The key for the index of the no mine tile.
     */
    public static final String KEY_TILES_NO_MINE = "tiles.noMine";
    /**
     * The key for the name of the file containing the LCD figures.
     */
    public static final String KEY_LCD_FILENAME = "lcd.filename";
    /**
     * The key for the LCD figures width.
     */
    public static final String KEY_LCD_WIDTH = "lcd.width";
    /**
     * The key for the LCD figures height.
     */
    public static final String KEY_LCD_HEIGHT = "lcd.height";
    /**
     * The key for the index of the "0" figure.
     */
    public static final String KEY_LCD_0 = "lcd.0";
    /**
     * The key for the index of the "1" figure.
     */
    public static final String KEY_LCD_1 = "lcd.1";
    /**
     * The key for the index of the "2" figure.
     */
    public static final String KEY_LCD_2 = "lcd.2";
    /**
     * The key for the index of the "3" figure.
     */
    public static final String KEY_LCD_3 = "lcd.3";
    /**
     * The key for the index of the "4" figure.
     */
    public static final String KEY_LCD_4 = "lcd.4";
    /**
     * The key for the index of the "5" figure.
     */
    public static final String KEY_LCD_5 = "lcd.5";
    /**
     * The key for the index of the "6" figure.
     */
    public static final String KEY_LCD_6 = "lcd.6";
    /**
     * The key for the index of the "7" figure.
     */
    public static final String KEY_LCD_7 = "lcd.7";
    /**
     * The key for the index of the "8" figure.
     */
    public static final String KEY_LCD_8 = "lcd.8";
    /**
     * The key for the index of the "9" figure.
     */
    public static final String KEY_LCD_9 = "lcd.9";
    /**
     * The key for the index of the "-" sign.
     */
    public static final String KEY_LCD_SIGN = "lcd.sign";
    /**
     * The key for the name of the file containing smileys.
     */
    public static final String KEY_SMILEYS_FILENAME = "smileys.filename";
    /**
     * The key for the smileys width.
     */
    public static final String KEY_SMILEYS_WIDTH = "smileys.width";
    /**
     * The key for the smileys height.
     */
    public static final String KEY_SMILEYS_HEIGHT = "smileys.height";
    /**
     * The key for the index of the in game smiley.
     */
    public static final String KEY_SMILEYS_PLAY = "smileys.play";
    /**
     * The key for the index of the loosing smiley.
     */
    public static final String KEY_SMILEYS_LOOSE = "smileys.loose";
    /**
     * The key for the index of the clicking smiley.
     */
    public static final String KEY_SMILEYS_CLICK = "smileys.click";
    /**
     * The key for the index of the wining smiley.
     */
    public static final String KEY_SMILEYS_WIN = "smileys.win";
    /**
     * The key for the index of the trivial case smiley.
     */
    public static final String KEY_SMILEYS_TRIVIAL = "smileys.trivial";
    /**
     * The key for the index of the schema search case smiley.
     */
    public static final String KEY_SMILEYS_SCHEMA = "smileys.schema";
    /**
     * The key for the index of the random case smiley.
     */
    public static final String KEY_SMILEYS_RANDOM = "smileys.random";
    /**
     * The key for the name of the file containing the icon.
     */
    public static final String KEY_ICON_FILENAME = "icon.filename";
    /**
     * The key for the name of the file containing the bip (for each second)
     * sound.
     */
    public static final String KEY_SOUND_TIMER_FILENAME = "sound.timer.filename";
    /**
     * The key for the name of the file containing the defeat sound.
     */
    public static final String KEY_SOUND_DEFEAT_FILENAME = "sound.defeat.filename";
    /**
     * The key for the name of the file containing the victory sound.
     */
    public static final String KEY_SOUND_VICTORY_FILENAME = "sound.victory.filename";
    /**
     * The key for the name of the file containing the cell flagged sound.
     */
    public static final String KEY_SOUND_FLAG_FILENAME = "sound.flag.filename";
    /**
     * The key for the name of the file containing the cell open sound.
     */
    public static final String KEY_SOUND_OPEN_FILENAME = "sound.open.filename";
    /**
     * The key for the name of the file containing the new game sound.
     */
    public static final String KEY_SOUND_NEW_FILENAME = "sound.new.filename";
    /**
     * The key for the menu game title string.
     */
    public static final String KEY_MENU_GAME = "menu.game";
    /**
     * The key for the menu game | new string.
     */
    public static final String KEY_MENU_GAME_NEW = "menu.game.new";
    /**
     * The key for the menu game | new key stroke.
     */
    public static final String KEY_MENU_GAME_NEW_KEYSTROKE = "menu.game.new.keyStroke";
    /**
     * The key for the menu game | beginner string.
     */
    public static final String KEY_MENU_GAME_BEGINNER = "menu.game.beginner";
    /**
     * The key for the menu game | intermediate string.
     */
    public static final String KEY_MENU_GAME_INTERMEDIATE = "menu.game.intermediate";
    /**
     * The key for the menu game | expert string.
     */
    public static final String KEY_MENU_GAME_EXPERT = "menu.game.expert";
    /**
     * The key for the menu game | custom string.
     */
    public static final String KEY_MENU_GAME_CUSTOM = "menu.game.custom";
    /**
     * The key for the menu game | marks string.
     */
    public static final String KEY_MENU_GAME_MARKS = "menu.game.marks";
    /**
     * The key for the menu game | color string.
     */
    public static final String KEY_MENU_GAME_COLOR = "menu.game.color";
    /**
     * The key for the menu game | sound string.
     */
    public static final String KEY_MENU_GAME_SOUND = "menu.game.sound";
    /**
     * The key for the menu game | best times string.
     */
    public static final String KEY_MENU_GAME_BESTTIMES = "menu.game.bestTimes";
    /**
     * The key for the menu game | quit string.
     */
    public static final String KEY_MENU_GAME_QUIT = "menu.game.quit";
    /**
     * The key for the menu board title string.
     */
    public static final String KEY_MENU_BOARD = "menu.board";
    /**
     * The key for the menu board | triangular string.
     */
    public static final String KEY_MENU_BOARD_TRIANGULAR = "menu.board.triangular";
    /**
     * The key for the menu board | triangular 14 string.
     */
    public static final String KEY_MENU_BOARD_TRIANGULAR14 = "menu.board.triangular14";
    /**
     * The key for the menu board | square string.
     */
    public static final String KEY_MENU_BOARD_SQUARE = "menu.board.square";
    /**
     * The key for the menu board | pentagonal string.
     */
    public static final String KEY_MENU_BOARD_PENTAGONAL = "menu.board.pentagonal";
    /**
     * The key for the menu board | hexagonal string.
     */
    public static final String KEY_MENU_BOARD_HEXAGONAL = "menu.board.hexagonal";
    /**
     * The key for the menu board | octosquare string.
     */
    public static final String KEY_MENU_BOARD_OCTOSQUARE = "menu.board.octosquare";
    /**
     * The key for the menu board | parquet string.
     */
    public static final String KEY_MENU_BOARD_PARQUET = "menu.board.parquet";
    /**
     * The key for the menu board | 3d grid string.
     */
    public static final String KEY_MENU_BOARD_3DGRID = "menu.board.3dGrid";
    /**
     * The key for the menu display string.
     */
    public static final String KEY_MENU_DISPLAY = "menu.displayAndSounds";
    /**
     * The key for the menu display | antialiasing string.
     */
    public static final String KEY_MENU_DISPLAY_ANTIALIASING = "menu.displayAndSounds.antialiasing";
    /**
     * The key for the menu display | shadowed string.
     */
    public static final String KEY_MENU_DISPLAY_SHADOW = "menu.displayAndSounds.shadow";
    /**
     * The key for the menu display | change look and feel string.
     */
    public static final String KEY_MENU_DISPLAY_CHANGELOOKANDFEEL = "menu.displayAndSounds.changeLookAndFeel";
    /**
     * The key for the menu display | change background color string.
     */
    public static final String KEY_MENU_DISPLAY_CHANGEBGCOLOR = "menu.displayAndSounds.changeBgColor";
    /**
     * The key for the menu display | change buttons color string.
     */
    public static final String KEY_MENU_DISPLAY_CHANGEBUTTONSCOLOR = "menu.displayAndSounds.changeButtonsColor";
    /**
     * The key for the menu display | change LCD color string.
     */
    public static final String KEY_MENU_DISPLAY_CHANGELCDCOLOR = "menu.displayAndSounds.changeLCDColor";
    /**
     * The key for the menu language string.
     */
    public static final String KEY_MENU_LANGUAGE = "menu.language";
    /**
     * The key for the menu other string.
     */
    public static final String KEY_MENU_OTHER = "menu.other";
    /**
     * The key for the menu other | save board string.
     */
    public static final String KEY_MENU_OTHER_SAVEBOARD = "menu.other.saveBoard";
    /**
     * The key for the menu other | load board string.
     */
    public static final String KEY_MENU_OTHER_LOADBOARD = "menu.other.loadBoard";
    /**
     * The key for the menu other | save screen shot string.
     */
    public static final String KEY_MENU_OTHER_SAVESCREENSHOT = "menu.other.saveScreenshot";
    /**
     * The key for the menu other | save video string.
     */
    public static final String KEY_MENU_OTHER_SAVEVIDEO = "menu.other.saveVideo";
    /**
     * The key for the menu other | load video string.
     */
    public static final String KEY_MENU_OTHER_LOADVIDEO = "menu.other.loadVideo";
    /**
     * The key for the menu robot string.
     */
    public static final String KEY_MENU_ROBOT = "menu.robot";
    /**
     * The key for the menu robot | name string.
     */
    public static final String KEY_MENU_ROBOT_NAME = "menu.robot.name";
    /**
     * The key for the menu robot | cheat string.
     */
    public static final String KEY_MENU_ROBOT_CHEAT = "menu.robot.cheat";
    /**
     * The key for the menu robot | real click string.
     */
    public static final String KEY_MENU_ROBOT_REALCLICKS = "menu.robot.realClicks";
    /**
     * The key for the menu robot | real click string.
     */
    public static final String KEY_MENU_ROBOT_RANDOMTRIES = "menu.robot.randomTries";
    /**
     * The key for the menu robot | statistics string.
     */
    public static final String KEY_MENU_ROBOT_STATISTICS = "menu.robot.statistics";
    /**
     * The key for the menu robot | play string.
     */
    public static final String KEY_MENU_ROBOT_PLAY = "menu.robot.play";
    /**
     * The key for the menu robot | help string.
     */
    public static final String KEY_MENU_ROBOT_HELP = "menu.robot.help";
    /**
     * The key for the menu ? string.
     */
    public static final String KEY_MENU_INFO = "menu.?";
    /**
     * The key for the menu ? | about string.
     */
    public static final String KEY_MENU_INFO_ABOUT = "menu.?.about";
    /**
     * The key for the menu ? | thanks string.
     */
    public static final String KEY_MENU_INFO_THANKS = "menu.?.thanks";
    /**
     * The key for the menu game | new status string.
     */
    public static final String KEY_STATUSTEXT_GAME_NEW = "statusText.game.new";
    /**
     * The key for the menu game | beginner status string.
     */
    public static final String KEY_STATUSTEXT_GAME_BEGINNER = "statusText.game.beginner";
    /**
     * The key for the menu game | intermediate status string.
     */
    public static final String KEY_STATUSTEXT_GAME_INTERMEDIATE = "statusText.game.intermediate";
    /**
     * The key for the menu game | expert status string.
     */
    public static final String KEY_STATUSTEXT_GAME_EXPERT = "statusText.game.expert";
    /**
     * The key for the menu game | custom status string.
     */
    public static final String KEY_STATUSTEXT_GAME_CUSTOM = "statusText.game.custom";
    /**
     * The key for the menu game | marks status string.
     */
    public static final String KEY_STATUSTEXT_GAME_MARKS = "statusText.game.marks";
    /**
     * The key for the menu game | color status string.
     */
    public static final String KEY_STATUSTEXT_GAME_COLOR = "statusText.game.color";
    /**
     * The key for the menu game | sound status string.
     */
    public static final String KEY_STATUSTEXT_GAME_SOUND = "statusText.game.sound";
    /**
     * The key for the menu game | best times status string.
     */
    public static final String KEY_STATUSTEXT_GAME_BESTTIMES = "statusText.game.bestTimes";
    /**
     * The key for the menu game | quit status string.
     */
    public static final String KEY_STATUSTEXT_GAME_QUIT = "statusText.game.quit";
    /**
     * The key for the menu board | triangular status string.
     */
    public static final String KEY_STATUSTEXT_BOARD_TRIANGULAR = "statusText.board.triangular";
    /**
     * The key for the menu board | triangular 14 status string.
     */
    public static final String KEY_STATUSTEXT_BOARD_TRIANGULAR_14 = "statusText.board.triangular14";
    /**
     * The key for the menu board | square status string.
     */
    public static final String KEY_STATUSTEXT_BOARD_SQUARE = "statusText.board.square";
    /**
     * The key for the menu board | pentagonal status string.
     */
    public static final String KEY_STATUSTEXT_BOARD_PENTAGONAL = "statusText.board.pentagonal";
    /**
     * The key for the menu board | hexagonal status string.
     */
    public static final String KEY_STATUSTEXT_BOARD_HEXAGONAL = "statusText.board.hexagonal";
    /**
     * The key for the menu board | octosquare status string.
     */
    public static final String KEY_STATUSTEXT_BOARD_OCTOSQUARE = "statusText.board.octosquare";
    /**
     * The key for the menu board | parquet status string.
     */
    public static final String KEY_STATUSTEXT_BOARD_PARQUET = "statusText.board.parquet";
    /**
     * The key for the menu display | antialiasing status string.
     */
    public static final String KEY_STATUSTEXT_DISPAY_ANTIALIASING = "statusText.displayAndSounds.antialiasing";
    /**
     * The key for the menu display | shadow status string.
     */
    public static final String KEY_STATUSTEXT_DISPAY_SHADOW = "statusText.displayAndSounds.shadow";
    /**
     * The key for the menu display | change look and feel status string.
     */
    public static final String KEY_STATUSTEXT_DISPAY_CHANGELOOKANDFEEL = "statusText.displayAndSounds.changeLookAndFeel";
    /**
     * The key for the menu display | change background color status string.
     */
    public static final String KEY_STATUSTEXT_DISPAY_CHANGEBGCOLOR = "statusText.displayAndSounds.changBgColor";
    /**
     * The key for the menu display | change buttons color status string.
     */
    public static final String KEY_STATUSTEXT_DISPAY_CHANGEBUTTONS = "statusText.displayAndSounds.changeButtonsColor";
    /**
     * The key for the menu display | change LCD color status string.
     */
    public static final String KEY_STATUSTEXT_DISPAY_CHANGELCDCOLOR = "statusText.displayAndSounds.changeLCDColor";
    /**
     * The key for the menu language status string.
     */
    public static final String KEY_STATUSTEXT_LANGUAGE = "statusText.language";
    /**
     * The key for the menu other | save board status string.
     */
    public static final String KEY_STATUSTEXT_OTHER_SAVEBOARD = "statusText.other.saveBoard";
    /**
     * The key for the menu other | load board status string.
     */
    public static final String KEY_STATUSTEXT_OTHER_LOADBOARD = "statusText.other.loadBoard";
    /**
     * The key for the menu other | save video status string.
     */
    public static final String KEY_STATUSTEXT_OTHER_SAVEVIDEO = "statusText.other.saveVideo";
    /**
     * The key for the menu other | load video status string.
     */
    public static final String KEY_STATUSTEXT_OTHER_LOADVIDEO = "statusText.other.loadVideo";
    /**
     * The key for the menu other | save screenshot status string.
     */
    public static final String KEY_STATUSTEXT_OTHER_SAVESCREENSHOT = "statusText.other.saveScreenshot";
    /**
     * The key for the menu robot | name status string.
     */
    public static final String KEY_STATUSTEXT_ROBOT_NAME = "statusText.robot.name";
    /**
     * The key for the menu robot | cheat status string.
     */
    public static final String KEY_STATUSTEXT_ROBOT_CHEAT = "statusText.robot.cheat";
    /**
     * The key for the menu robot | real clicks status string.
     */
    public static final String KEY_STATUSTEXT_ROBOT_REALCLICKS = "statusText.robot.realClicks";
    /**
     * The key for the menu robot | random clicks status string.
     */
    public static final String KEY_STATUSTEXT_ROBOT_RANDOMTRIES = "statusText.robot.randomTries";
    /**
     * The key for the menu robot | statistics status string.
     */
    public static final String KEY_STATUSTEXT_ROBOT_STATISTICS = "statusText.robot.statistics";
    /**
     * The key for the menu robot | play status string.
     */
    public static final String KEY_STATUSTEXT_ROBOT_PLAY = "statusText.robot.play";
    /**
     * The key for the menu robot | help status string.
     */
    public static final String KEY_STATUSTEXT_ROBOT_HELP = "statusText.robot.help";
    /**
     * The key for the menu info | about status string.
     */
    public static final String KEY_STATUSTEXT_INFO_ABOUT = "statusText.?.about";
    /**
     * The key for the menu info | about status string.
     */
    public static final String KEY_STATUSTEXT_INFO_THANKS = "statusText.?.thanks";
    /**
     * The key for the helping, analysing text.
     */
    public static final String KEY_STATUSTEXT_HELPING_ANALYSING = "statusText.helping.analysing";
    /**
     * The key for the helping, trivial try text.
     */
    public static final String KEY_STATUSTEXT_HELPING_TRIVIAL = "statusText.helping.trivial";
    /**
     * The key for the helping, schema try text.
     */
    public static final String KEY_STATUSTEXT_HELPING_SCHEMA = "statusText.helping.schema";
    /**
     * The key for the helping, random try text.
     */
    public static final String KEY_STATUSTEXT_HELPING_RANDOM = "statusText.helping.random";
    /**
     * The key for the really quit text.
     */
    public static final String KEY_TEXT_REALLYQUIT = "text.reallyQuit";
    /**
     * The key for the height text.
     */
    public static final String KEY_TEXT_HEIGHT = "text.height";
    /**
     * The key for the width text.
     */
    public static final String KEY_TEXT_WIDTH = "text.width";
    /**
     * The key for the mines text.
     */
    public static final String KEY_TEXT_MINES = "text.mines";
    /**
     * The key for the default text.
     */
    public static final String KEY_TEXT_DEFAULT = "text.default";
    /**
     * The key for the ok text.
     */
    public static final String KEY_TEXT_OK = "text.ok";
    /**
     * The key for the cancel text.
     */
    public static final String KEY_TEXT_CANCEL = "text.cancel";
    /**
     * The key for the best time text.
     */
    public static final String KEY_TEXT_BESTTIME = "text.bestTime";
    /**
     * The key for the about text.
     */
    public static final String KEY_TEXT_ABOUT = "text.about";
    /**
     * The key for the gpl text.
     */
    public static final String KEY_TEXT_GPL = "text.gpl";
    /**
     * The key for the jvm name text.
     */
    public static final String KEY_TEXT_JVMNAME = "text.jvmName";
    /**
     * The key for the thanks text.
     */
    public static final String KEY_TEXT_THANKS = "text.thanks";
    /**
     * The key for the beginner text.
     */
    public static final String KEY_TEXT_BEGINNER = "text.beginner";
    /**
     * The key for the intermediate text.
     */
    public static final String KEY_TEXT_INTERMEDIATE = "text.intermediate";
    /**
     * The key for the expert text.
     */
    public static final String KEY_TEXT_EXPERT = "text.expert";
    /**
     * The key for the delete times text.
     */
    public static final String KEY_TEXT_DELETETIMES = "text.deleteTimes";
    /**
     * The key for the triangular text.
     */
    public static final String KEY_TEXT_TRIANGULAR = "text.triangular";
    /**
     * The key for the triangular text.
     */
    public static final String KEY_TEXT_TRIANGULAR_14 = "text.triangular14";
    /**
     * The key for the square text.
     */
    public static final String KEY_TEXT_SQUARE = "text.square";
    /**
     * The key for the hexagonal text.
     */
    public static final String KEY_TEXT_PENTAGONAL = "text.pentagonal";
    /**
     * The key for the hexagonal text.
     */
    public static final String KEY_TEXT_HEXAGONAL = "text.hexagonal";
    /**
     * The key for the hexagonal text.
     */
    public static final String KEY_TEXT_OCTOSQUARE = "text.octosquare";
    /**
     * The key for the hexagonal text.
     */
    public static final String KEY_TEXT_PARQUET = "text.parquet";
    /**
     * The key for the seconds text.
     */
    public static final String KEY_TEXT_SECONDS = "text.seconds";
    /**
     * The key for the custom text.
     */
    public static final String KEY_TEXT_CUSTOM = "text.custom";
    /**
     * The key for the custom text.
     */
    public static final String KEY_TEXT_ORIGINAL = "text.original";
    /**
     * The key for the original text.
     */
    public static final String KEY_TEXT_LOOKANDFEELDEPENDENT = "text.lookAndFeelDependant";
    /**
     * The key for the black text.
     */
    public static final String KEY_TEXT_BLACK = "text.black";
    /**
     * The key for the blue text.
     */
    public static final String KEY_TEXT_BLUE = "text.blue";
    /**
     * The key for the cyan text.
     */
    public static final String KEY_TEXT_CYAN = "text.cyan";
    /**
     * The key for the dark gray text.
     */
    public static final String KEY_TEXT_DARKGRAY = "text.darkGray";
    /**
     * The key for the gray text.
     */
    public static final String KEY_TEXT_GRAY = "text.gray";
    /**
     * The key for the green text.
     */
    public static final String KEY_TEXT_GREEN = "text.green";
    /**
     * The key for the light gray text.
     */
    public static final String KEY_TEXT_LIGHTGRAY = "text.lightGray";
    /**
     * The key for the magenta text.
     */
    public static final String KEY_TEXT_MAGENTA = "text.magenta";
    /**
     * The key for the orange text.
     */
    public static final String KEY_TEXT_ORANGE = "text.orange";
    /**
     * The key for the pink text.
     */
    public static final String KEY_TEXT_PINK = "text.pink";
    /**
     * The key for the red text.
     */
    public static final String KEY_TEXT_RED = "text.red";
    /**
     * The key for the white text.
     */
    public static final String KEY_TEXT_WHITE = "text.white";
    /**
     * The key for the yellow text.
     */
    public static final String KEY_TEXT_YELLOW = "text.yellow";
    /**
     * The key for the configuration file created text.
     */
    public static final String KEY_TEXT_CONFIGURATIONFILECREATED = "text.configurationFileCreated";
    /**
     * The key for the configuration file crypted text.
     */
    public static final String KEY_TEXT_CONFIGURATIONFILECRYPTED = "text.configurationFileCrypted";
    /**
     * The key for the screenshot file description text.
     */
    public static final String KEY_TEXT_SCREENSHOTFILEDESCRIPTION = "text.screenshotFileDescription";
    /**
     * The key for the board file description text.
     */
    public static final String KEY_TEXT_BOARDFILEDESCRIPTION = "text.boardFileDescription";
    /**
     * The key for the video file description text.
     */
    public static final String KEY_TEXT_VIDEOFILEDESCRIPTION = "text.videoFileDescription";
    /**
     * The key for the save board warning.
     */
    public static final String KEY_TEXT_SAVEWARNING = "text.saveWarning";
    /**
     * The key for the robot name dialog text.
     */
    public static final String KEY_TEXT_ROBOTNAME = "text.robotName";
    /**
     * The key for the robot played games text.
     */
    public static final String KEY_TEXT_PLAYEDGAMES = "text.playedGames";
    /**
     * The key for the robot won games text.
     */
    public static final String KEY_TEXT_WONGAMES = "text.wonGames";
    /**
     * The key for the robot won percent text.
     */
    public static final String KEY_TEXT_WONPERCENT = "text.wonPercent";
    /**
     * The key for the robot medium time text.
     */
    public static final String KEY_TEXT_MEDIUMTIME = "text.mediumTime";
    /**
     * The key for the robot minimum time text.
     */
    public static final String KEY_TEXT_MINIMUMTIME = "text.minimumTime";
    /**
     * The key for the robot maximum time text.
     */
    public static final String KEY_TEXT_MAXIMUMTIME = "text.maximumTime";
    /**
     * The key for the "variable" text.
     */
    public static final String KEY_TEXT_VARIABLE = "text.variable";
    /**
     * The key for the "value" text.
     */
    public static final String KEY_TEXT_VALUE = "text.value";
    /**
     * The key for the "default value" text.
     */
    public static final String KEY_TEXT_DEFAULT_VALUE = "text.defaultValue";
    /**
     * The key for the not implemented error.
     */
    public static final String KEY_ERROR_NOTIMPLEMENTED = "error.notImplemented";
    /**
     * The key for the corrupted error.
     */
    public static final String KEY_ERROR_CORRUPTED = "error.corrupted";
    /**
     * The key for the shape unsupported error.
     */
    public static final String KEY_ERROR_SHAPEUNSUPPORTED = "error.shapeUnsupported";
    /**
     * The key for a usage error.
     */
    public static final String KEY_ERROR_USAGE = "error.usage";
    /**
     * The key for the JMines title.
     */
    public static final String KEY_TITLE_JMINES = "title.jmines";
    /**
     * The key for the quit title.
     */
    public static final String KEY_TITLE_QUIT = "title.quit";
    /**
     * The key for the error title.
     */
    public static final String KEY_TITLE_ERROR = "title.error";
    /**
     * The key for the custom fields title.
     */
    public static final String KEY_TITLE_CUSTOMFIELDS = "title.cutomFields";
    /**
     * The key for the best times title.
     */
    public static final String KEY_TITLE_BESTTIMES = "title.bestTimes";
    /**
     * The key for the about title.
     */
    public static final String KEY_TITLE_ABOUT = "title.about";
    /**
     * The key for the save video title.
     */
    public static final String KEY_TITLE_THANKS = "title.thanks";
    /**
     * The key for the choose color title.
     */
    public static final String KEY_TITLE_CHOOSECOLOR = "title.chooseColor";
    /**
     * The key for the save video title.
     */
    public static final String KEY_TITLE_SAVEBOARD = "title.saveBoard";
    /**
     * The key for the save video title.
     */
    public static final String KEY_TITLE_SAVESCREENSHOT = "title.saveScreenshot";
    /**
     * The key for the save video title.
     */
    public static final String KEY_TITLE_SAVEVIDEO = "title.saveVideo";
    /**
     * The key for the load video title.
     */
    public static final String KEY_TITLE_LOADVIDEO = "title.loadVideo";
    /**
     * The key for the statistics title.
     */
    public static final String KEY_TITLE_STATISTICS = "title.statistics";
    /**
     * The key for the configuration title.
     */
    public static final String KEY_TITLE_CONFIGURATION = "title.configuration";
    /**
     * The key for the number of columns in beginner mode.
     */
    public static final String KEY_BEGINNER_WIDTH = "beginner.width";
    /**
     * The key for the number of lines in beginner mode.
     */
    public static final String KEY_BEGINNER_HEIGHT = "beginner.height";
    /**
     * The key for the number of mines in beginner mode.
     */
    public static final String KEY_BEGINNER_MINES = "beginner.mines";
    /**
     * The key for the number of columns in intermediate mode.
     */
    public static final String KEY_INTERMEDIATE_WIDTH = "intermediate.width";
    /**
     * The key for the number of lines in intermediate mode.
     */
    public static final String KEY_INTERMEDIATE_HEIGHT = "intermediate.height";
    /**
     * The key for the number of mines in intermediate mode.
     */
    public static final String KEY_INTERMEDIATE_MINES = "intermediate.mines";
    /**
     * The key for the number of columns in expert mode.
     */
    public static final String KEY_EXPERT_WIDTH = "expert.width";
    /**
     * The key for the number of lines in expert mode.
     */
    public static final String KEY_EXPERT_HEIGHT = "expert.height";
    /**
     * The key for the number of mines in expert mode.
     */
    public static final String KEY_EXPERT_MINES = "expert.mines";
    /**
     * The key for the number of columns in custom mode.
     */
    public static final String KEY_CUSTOM_MINWIDTH = "custom.minWidth";
    /**
     * The key for the number of lines in custom mode.
     */
    public static final String KEY_CUSTOM_MINHEIGHT = "custom.minHeight";
    /**
     * The key for the maximum number of columns in custom mode.
     */
    public static final String KEY_CUSTOM_MAXWIDTH = "custom.maxWidth";
    /**
     * The key for the maximum number of lines in custom mode.
     */
    public static final String KEY_CUSTOM_MAXHEIGHT = "custom.maxHeight";
    /**
     * The key for the maximum number of mines in custom mode.
     */
    public static final String KEY_CUSTOM_MINMINES = "custom.minMines";
    /**
     * The key for the best time in beginner, square mode.
     */
    public static final String KEY_SQUARE_BEGINNER_BEST = "square.beginner.best";
    /**
     * The key for the best time in intermediate, square mode.
     */
    public static final String KEY_SQUARE_INTERMEDIATE_BEST = "square.intermediate.best";
    /**
     * The key for the best time in expert, square mode.
     */
    public static final String KEY_SQUARE_EXPERT_BEST = "square.expert.best";
    /**
     * The key for the best time in beginner, triangular mode.
     */
    public static final String KEY_TRIANGULAR_BEGINNER_BEST = "triangular.beginner.best";
    /**
     * The key for the best time in intermediate, triangular mode.
     */
    public static final String KEY_TRIANGULAR_INTERMEDIATE_BEST = "triangular.intermediate.best";
    /**
     * The key for the best time in expert, triangular mode.
     */
    public static final String KEY_TRIANGULAR_EXPERT_BEST = "triangular.expert.best";
    /**
     * The key for the best time in beginner, triangular 14 mode.
     */
    public static final String KEY_TRIANGULAR_14_BEGINNER_BEST = "triangular14.beginner.best";
    /**
     * The key for the best time in intermediate, triangular 14 mode.
     */
    public static final String KEY_TRIANGULAR_14_INTERMEDIATE_BEST = "triangular14.intermediate.best";
    /**
     * The key for the best time in expert, triangular 14 mode.
     */
    public static final String KEY_TRIANGULAR_14_EXPERT_BEST = "triangular14.expert.best";
    /**
     * The key for the best time in beginner, pentagonal mode.
     */
    public static final String KEY_PENTAGONAL_BEGINNER_BEST = "pentagonal.beginner.best";
    /**
     * The key for the best time in intermediate, pentagonal mode.
     */
    public static final String KEY_PENTAGONAL_INTERMEDIATE_BEST = "pentagonal.intermediate.best";
    /**
     * The key for the best time in expert, pentagonal mode.
     */
    public static final String KEY_PENTAGONAL_EXPERT_BEST = "pentagonal.expert.best";
    /**
     * The key for the best time in beginner, hexagonal mode.
     */
    public static final String KEY_HEXAGONAL_BEGINNER_BEST = "hexagonal.beginner.best";
    /**
     * The key for the best time in intermediate, hexagonal mode.
     */
    public static final String KEY_HEXAGONAL_INTERMEDIATE_BEST = "hexagonal.intermediate.best";
    /**
     * The key for the best time in expert, hexagonal mode.
     */
    public static final String KEY_HEXAGONAL_EXPERT_BEST = "hexagonal.expert.best";
    /**
     * The key for the best time in beginner, octosquare mode.
     */
    public static final String KEY_OCTOSQUARE_BEGINNER_BEST = "octosquare.beginner.best";
    /**
     * The key for the best time in intermediate, octosquare mode.
     */
    public static final String KEY_OCTOSQUARE_INTERMEDIATE_BEST = "octosquare.intermediate.best";
    /**
     * The key for the best time in expert, octosquare mode.
     */
    public static final String KEY_OCTOSQUARE_EXPERT_BEST = "octosquare.expert.best";
    /**
     * The key for the best time in beginner, parquet mode.
     */
    public static final String KEY_PARQUET_BEGINNER_BEST = "parquet.beginner.best";
    /**
     * The key for the best time in intermediate, parquet mode.
     */
    public static final String KEY_PARQUET_INTERMEDIATE_BEST = "parquet.intermediate.best";
    /**
     * The key for the best time in expert, parquet mode.
     */
    public static final String KEY_PARQUET_EXPERT_BEST = "parquet.expert.best";
    /**
     * The key for the best default name.
     */
    public static final String KEY_BEST_DEFAULT_NAME = "best.default.name";
    /**
     * The key for the best default time.
     */
    public static final String KEY_BEST_DEFAULT_TIME = "best.default.time";
    /**
     * The key for the version text.
     */
    public static final String KEY_VERSION = "version";
    /**
     * The key for the saved user difficulty.
     */
    public static final String KEY_USER_DIFFICULTY = "user.difficulty";
    /**
     * The key for the saved user tiles shape.
     */
    public static final String KEY_USER_SHAPE = "user.shape";
    /**
     * The key for the saved user marks (true/false).
     */
    public static final String KEY_USER_MARKS = "user.marks";
    /**
     * The key for the saved user color (true/false).
     */
    public static final String KEY_USER_COLOR = "user.color";
    /**
     * The key for the saved user difficulty (true/false).
     */
    public static final String KEY_USER_SOUND = "user.sound";
    /**
     * The key for the saved user antialiasing (true/false).
     */
    public static final String KEY_USER_ANTIALIASING = "user.antialiasing";
    /**
     * The key for the saved user shadowed (true/false).
     */
    public static final String KEY_USER_SHADOWED = "user.shadowed";
    /**
     * The key for the saved user look and feel (a class name).
     */
    public static final String KEY_USER_LOOKANDFEEL = "user.lookAndFeel";
    /**
     * The key for the saved user background (a color name).
     */
    public static final String KEY_USER_BGCOLOR = "user.bgColor";
    /**
     * The key for the saved user buttons color (a color name).
     */
    public static final String KEY_USER_BUTTONSCOLOR = "user.buttonsColor";
    /**
     * The key for the saved user lcd color (a color name).
     */
    public static final String KEY_USER_LCDCOLOR = "user.lcdColor";
    /**
     * The key for the saved user language.
     */
    public static final String KEY_USER_LOCALE = "user.locale";
    /**
     * The key for the look and feels libraries names.
     */
    public static final String KEY_LOOKANDFEEL_LIBRARIESNAMES = "lookandfeel.librariesNames";
    /**
     * The key for the look and feels classes names.
     */
    public static final String KEY_LOOKANDFEEL_CLASSESNAMES = "lookandfeel.classesNames";
    /**
     * The key for the screenshot files suffix.
     */
    public static final String KEY_FILE_SCREENSHOT_SUFFIX = "file.screenshot.suffix";
    /**
     * The key for the board files suffix.
     */
    public static final String KEY_FILE_BOARD_SUFFIX = "file.board.suffix";
    /**
     * The key for the video files suffix.
     */
    public static final String KEY_FILE_VIDEO_SUFFIX = "file.video.suffix";
    /**
     * The key for the robot's default name.
     */
    public static final String KEY_ROBOT_DEFAULTNAME = "robot.defaultName";
    /**
     * The key for the robot's name.
     */
    public static final String KEY_ROBOT_NAME = "robot.name";
    /**
     * The key for the robot cheat value.
     */
    public static final String KEY_ROBOT_CHEAT = "robot.cheat";
    /**
     * The key for the robot real clicks value.
     */
    public static final String KEY_ROBOT_REALCLICKS = "robot.realClicks";
    /**
     * The key for the robot random clicks value.
     */
    public static final String KEY_ROBOT_RANDOMTRIES = "robot.randomTries";
    /**
     * The key for the robot random clicks value.
     */
    public static final String KEY_ROBOT_HELP = "robot.help";
    /**
     * The key for the robot statistics played games value.
     */
    public static final String KEY_ROBOT_STATISTICS_PLAYED = "robot.statistics.played";
    /**
     * The key for the robot statistics won games value.
     */
    public static final String KEY_ROBOT_STATISTICS_WON = "robot.statistics.won";
    /**
     * The key for the robot statistics medium time to win value.
     */
    public static final String KEY_ROBOT_STATISTICS_MEDIUM = "robot.statistics.medium";
    /**
     * The key for the robot statistics maximum time to win value.
     */
    public static final String KEY_ROBOT_STATISTICS_MINIMUM = "robot.statistics.minimum";
    /**
     * The key for the robot statistics minimum time to win value.
     */
    public static final String KEY_ROBOT_STATISTICS_MAXIMUM = "robot.statistics.maximum";
    /**
     * The key for the links color.
     */
    public static final String KEY_LINK_COLOR = "link.color";
    /**
     * The key for the active link color.
     */
    public static final String KEY_LINK_ACTIVECOLOR = "link.activeColor";
    /**
     * The key for the visited links color.
     */
    public static final String KEY_LINK_VISITEDCOLOR = "link.visitedColor";

    /**
     * The keys beginner difficulty part.
     */
    public static final String DIFFICULTY_BEGINNER = "beginner";
    /**
     * The keys intermediate difficulty part.
     */
    public static final String DIFFICULTY_INTERMEDIATE = "intermediate";
    /**
     * The keys expert difficulty part.
     */
    public static final String DIFFICULTY_EXPERT = "expert";
    /**
     * The keys custom difficulty part.
     */
    public static final String DIFFICULTY_CUSTOM = "custom";

    /**
     * The keys square mode part.
     */
    public static final String SHAPE_SQUARE = "square";
    /**
     * The keys triangular mode part.
     */
    public static final String SHAPE_TRIANGULAR = "triangular";
    /**
     * The keys triangular 14 mode part.
     */
    public static final String SHAPE_TRIANGULAR_14 = "triangular 14";
    /**
     * The keys pentagonal mode part.
     */
    public static final String SHAPE_PENTAGONAL = "pentagonal";
    /**
     * The keys hexagonal mode part.
     */
    public static final String SHAPE_HEXAGONAL = "hexagonal";
    /**
     * The keys octosquare mode part.
     */
    public static final String SHAPE_OCTOSQUARE = "octosquare";
    /**
     * The keys parquet mode part.
     */
    public static final String SHAPE_PARQUET = "parquet";

    /**
     * The keys "tiles" prefix.
     */
    public static final String PREFIX_IMG_TILES = "tiles";
    /**
     * The keys "lcd" prefix.
     */
    public static final String PREFIX_IMG_LCD = "lcd";
    /**
     * The keys "smiley" prefix.
     */
    public static final String PREFIX_IMG_SMILEY = "smileys";
    /**
     * The keys "text" prefix.
     */
    public static final String PREFIX_TEXT = "text";

    /**
     * The keys "smiley in game" part.
     */
    public static final String STATE_SMILEY_PLAY = "play";
    /**
     * The keys "smiley loosing" part.
     */
    public static final String STATE_SMILEY_LOOSE = "loose";
    /**
     * The keys "smiley clicking" part.
     */
    public static final String STATE_SMILEY_CLICK = "click";
    /**
     * The keys "smiley winning" part.
     */
    public static final String STATE_SMILEY_WIN = "win";
    /**
     * The keys "smiley winning" part.
     */
    public static final String STATE_SMILEY_TRIVIAL = "trivial";
    /**
     * The keys "smiley winning" part.
     */
    public static final String STATE_SMILEY_SCHEMA = "schema";
    /**
     * The keys "smiley winning" part.
     */
    public static final String STATE_SMILEY_RANDOM = "random";

    /**
     * The keys "width" suffix.
     */
    public static final String SUFFIX_WIDTH = "width";
    /**
     * The keys "height" suffix.
     */
    public static final String SUFFIX_HEIGHT = "height";
    /**
     * The keys "mines" suffix.
     */
    public static final String SUFFIX_MINES = "mines";
    /**
     * The keys "flag" suffix.
     */
    public static final String SUFFIX_FLAG = "flag";
    /**
     * The keys "mark" suffix.
     */
    public static final String SUFFIX_MARK = "mark";
    /**
     * The keys "mine" suffix.
     */
    public static final String SUFFIX_MINE = "mine";
    /**
     * The keys "exploded" suffix.
     */
    public static final String SUFFIX_EXPLODED = "exploded";
    /**
     * The keys "no mine" suffix.
     */
    public static final String SUFFIX_NO_MINE = "noMine";
    /**
     * The keys "best" suffix.
     */
    public static final String SUFFIX_BEST = "best";

    /**
     * The "," character.
     */
    public static final String COMA = ",";
    /**
     * The "." character.
     */
    public static final String DOT = ".";
    /**
     * The ";" character.
     */
    public static final String SEMICOLON = ";";

    /**
     * The prefix for a configurable token (configurable tokens are {n} where n
     * is an integer).
     */
    public static final String CONFIGURABLE_TOKEN_PREFIX = "{";
    /**
     * The suffix for a configurable token (configurable tokens are {n} where n
     * is an integer).
     */
    public static final String CONFIGURABLE_TOKEN_SUFFIX = "}";
    /**
     * The regular expression for a configurable token (configurable tokens are
     * {n} where n is an integer).
     */
    public static final String CONFIGURABLE_TOKEN = "[" + CONFIGURABLE_TOKEN_PREFIX + "][0-9]+[" + CONFIGURABLE_TOKEN_SUFFIX + "]";
    /**
     * The prefix used to retrieve language files.
     */
    public static final String LANGUAGE_FILENAME_PREFIX = "language.";
    /**
     * The suffix used to retrieve language files.
     */
    public static final String LANGUAGE_FILENAME_SUFFIX = ".properties";
    /**
     * The locale name of the default language file.
     */
    public static final String LANGUAGE_DEFAULT_LOCALE = "default";

    static {
        INSTANCE = new Configuration();
    }

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The bundle corresponding to the default configuration file.
     */
    private ResourceBundle defaultBundle;
    /**
     * The bundle corresponding to the user configuration file.
     */
    private ResourceBundle userBundle;
    /**
     * The bundle corresponding to the default language file.
     */
    private ResourceBundle defaultLanguageBundle;
    /**
     * The bundle corresponding to the user language file.
     */
    private ResourceBundle userLanguageBundle;
    /**
     * The List containing all the locales available in the game.
     */
    private final List<Locale> availableLocales = new ArrayList<Locale>();
    /**
     * The List containing all the look and feels available in the game.
     */
    private final Collection<LookAndFeel> availableLookAndFeels = new ArrayList<LookAndFeel>();
    /**
     * The map corresponding to the "real time" configuration.
     */
    private Map<String, String> realTimeConfigurations = new HashMap<String, String>();
    /**
     * The URL of the directory containing all the resources.
     */
    private URL resDirectoryURL;
    /**
     * The class loader in charge to load the libraries classes.
     */
    private JarDynamicLoader loader = new JarDynamicLoader();
    /**
     * The class of the objects used to write crypted configuration file.
     */
    private Class<?> classHuffmanFileOutputStream = null;
    /**
     * The class of the objects used to read crypted configuration file.
     */
    private Class<?> classHuffmanFileInputStream;
    /**
     * The class of an exception that can occur during the configuration file
     * reading.
     */
    private Class<?> classInputException = null;
    /**
     * The list of all the configurable attributes keys. This is used to save
     * the user configuration file.
     */
    private final String[] toSave = new String[] {
            KEY_TILES_FILENAME, KEY_TILES_WIDTH, KEY_TILES_HEIGHT, KEY_TILES_0, KEY_TILES_1, KEY_TILES_2, KEY_TILES_3, KEY_TILES_4, KEY_TILES_5, KEY_TILES_6, KEY_TILES_7, KEY_TILES_8, KEY_TILES_9, KEY_TILES_10, KEY_TILES_11, KEY_TILES_12, KEY_TILES_13, KEY_TILES_14, KEY_TILES_15, KEY_TILES_FLAG, KEY_TILES_MARK, KEY_TILES_MINE, KEY_TILES_EXPLODED, KEY_TILES_NO_MINE,
            null,
            KEY_LCD_FILENAME, KEY_LCD_WIDTH, KEY_LCD_HEIGHT, KEY_LCD_0, KEY_LCD_1, KEY_LCD_2, KEY_LCD_3, KEY_LCD_4, KEY_LCD_5, KEY_LCD_6, KEY_LCD_7, KEY_LCD_8, KEY_LCD_9, KEY_LCD_SIGN,
            null,
            KEY_SMILEYS_FILENAME, KEY_SMILEYS_WIDTH, KEY_SMILEYS_HEIGHT, KEY_SMILEYS_PLAY, KEY_SMILEYS_LOOSE, KEY_SMILEYS_CLICK, KEY_SMILEYS_WIN, KEY_SMILEYS_TRIVIAL, KEY_SMILEYS_SCHEMA, KEY_SMILEYS_RANDOM,
            null,
            KEY_ICON_FILENAME,
            null,
            KEY_SOUND_TIMER_FILENAME, KEY_SOUND_DEFEAT_FILENAME, KEY_SOUND_VICTORY_FILENAME,
            null,
            KEY_BEGINNER_WIDTH, KEY_BEGINNER_HEIGHT, KEY_BEGINNER_MINES,
            null,
            KEY_INTERMEDIATE_WIDTH, KEY_INTERMEDIATE_HEIGHT, KEY_INTERMEDIATE_MINES,
            null,
            KEY_EXPERT_WIDTH, KEY_EXPERT_HEIGHT, KEY_EXPERT_MINES,
            null,
            KEY_CUSTOM_MINWIDTH, KEY_CUSTOM_MINHEIGHT, KEY_CUSTOM_MAXWIDTH, KEY_CUSTOM_MAXHEIGHT, KEY_CUSTOM_MINMINES,
            null,
            KEY_TRIANGULAR_BEGINNER_BEST, KEY_TRIANGULAR_INTERMEDIATE_BEST, KEY_TRIANGULAR_EXPERT_BEST,
            null,
            KEY_TRIANGULAR_14_BEGINNER_BEST, KEY_TRIANGULAR_14_INTERMEDIATE_BEST, KEY_TRIANGULAR_14_EXPERT_BEST,
            null,
            KEY_SQUARE_BEGINNER_BEST, KEY_SQUARE_INTERMEDIATE_BEST, KEY_SQUARE_EXPERT_BEST,
            null,
            KEY_PENTAGONAL_BEGINNER_BEST, KEY_PENTAGONAL_INTERMEDIATE_BEST, KEY_PENTAGONAL_EXPERT_BEST,
            null,
            KEY_HEXAGONAL_BEGINNER_BEST, KEY_HEXAGONAL_INTERMEDIATE_BEST, KEY_HEXAGONAL_EXPERT_BEST,
            null,
            KEY_OCTOSQUARE_BEGINNER_BEST, KEY_OCTOSQUARE_INTERMEDIATE_BEST, KEY_OCTOSQUARE_EXPERT_BEST,
            null,
            KEY_PARQUET_BEGINNER_BEST, KEY_PARQUET_INTERMEDIATE_BEST, KEY_PARQUET_EXPERT_BEST,
            null,
            KEY_BEST_DEFAULT_TIME,
            null,
            KEY_USER_DIFFICULTY, KEY_USER_SHAPE, KEY_USER_MARKS, KEY_USER_COLOR, KEY_USER_SOUND, KEY_USER_ANTIALIASING, KEY_USER_SHADOWED, KEY_USER_LOOKANDFEEL, KEY_USER_BGCOLOR, KEY_USER_BUTTONSCOLOR, KEY_USER_LCDCOLOR, KEY_USER_LOCALE,
            null,
            KEY_ROBOT_DEFAULTNAME, KEY_ROBOT_NAME, KEY_ROBOT_CHEAT, KEY_ROBOT_REALCLICKS, KEY_ROBOT_RANDOMTRIES, KEY_ROBOT_HELP, KEY_ROBOT_STATISTICS_PLAYED, KEY_ROBOT_STATISTICS_WON, KEY_ROBOT_STATISTICS_MEDIUM, KEY_ROBOT_STATISTICS_MINIMUM, KEY_ROBOT_STATISTICS_MAXIMUM
    };

    /**
     * The list of all the non configurable attributes keys. This is used to
     * edit the user configuration file.
     */
    private final String[] nonEditable = new String[] {
            KEY_TRIANGULAR_BEGINNER_BEST, KEY_TRIANGULAR_INTERMEDIATE_BEST, KEY_TRIANGULAR_EXPERT_BEST,
            null,
            KEY_TRIANGULAR_14_BEGINNER_BEST, KEY_TRIANGULAR_14_INTERMEDIATE_BEST, KEY_TRIANGULAR_14_EXPERT_BEST,
            null,
            KEY_SQUARE_BEGINNER_BEST, KEY_SQUARE_INTERMEDIATE_BEST, KEY_SQUARE_EXPERT_BEST,
            null,
            KEY_PENTAGONAL_BEGINNER_BEST, KEY_PENTAGONAL_INTERMEDIATE_BEST, KEY_PENTAGONAL_EXPERT_BEST,
            null,
            KEY_HEXAGONAL_BEGINNER_BEST, KEY_HEXAGONAL_INTERMEDIATE_BEST, KEY_HEXAGONAL_EXPERT_BEST,
            null,
            KEY_OCTOSQUARE_BEGINNER_BEST, KEY_OCTOSQUARE_INTERMEDIATE_BEST, KEY_OCTOSQUARE_EXPERT_BEST,
            null,
            KEY_PARQUET_BEGINNER_BEST, KEY_PARQUET_INTERMEDIATE_BEST, KEY_PARQUET_EXPERT_BEST,
    };

    /**
     * The list of all the updatable attributes keys. This is used to update
     * the user configuration file.
     */
    private final String[] update = new String[] {
            KEY_TILES_FILENAME, KEY_TILES_WIDTH, KEY_TILES_HEIGHT, KEY_TILES_0, KEY_TILES_1, KEY_TILES_2, KEY_TILES_3, KEY_TILES_4, KEY_TILES_5, KEY_TILES_6, KEY_TILES_7, KEY_TILES_8, KEY_TILES_9, KEY_TILES_10, KEY_TILES_11, KEY_TILES_12, KEY_TILES_13, KEY_TILES_14, KEY_TILES_15, KEY_TILES_FLAG, KEY_TILES_MARK, KEY_TILES_MINE, KEY_TILES_EXPLODED, KEY_TILES_NO_MINE
    };

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new Configuration, read the default configuration
     * file and the user configuration file (if exists).
     */
    private Configuration() {
        manageURLs();

        loadDefaultConfigurationFile();
        loadDefaultLanguage();

        manageLibraries();
        manageLocales();
        manageLookAndFeels();

        loadUserConfigurationFile();
        loadUserLanguage();

        update();
    }

    //==========================================================================
    // Getters
    //==========================================================================
    /**
     * Returns all the locales available to display.
     *
     * @return All the locales available to display.
     */
    public List<Locale> getAvailableLocales() {
        return availableLocales;
    }

    /**
     * Returns all the look and feels available.
     *
     * @return All the look and feels available.
     */
    public Collection<LookAndFeel> getAvailableLookAndFeels() {
        return availableLookAndFeels;
    }

    /**
     * The loader use to dynamically load the libraries and look and feels
     * jars.
     *
     * @return The loader use to dynamically load the libraries and look and
     *         feels jars.
     */
    public JarDynamicLoader getLoader() {
        return loader;
    }

    /**
     * Returns the class of the objects used to write crypted configuration file.
     *
     * @return The class of the objects used to write crypted configuration file.
     */
    public Class<?> getClassHuffmanFileOutputStream() {
        return classHuffmanFileOutputStream;
    }

    /**
     * Returns the class of the objects used to read crypted configuration file.
     *
     * @return The class of the objects used to read crypted configuration file.
     */
    public Class<?> getClassHuffmanFileInputStream() {
        return classHuffmanFileInputStream;
    }

    /**
     * The entry point of the application used to display the content of the
     * user's configuration file.
     *
     * @param args The application arguments.
     */
    public static void main(final String[] args) {
        new ConfigurationFrame(Configuration.getInstance().toSave, Configuration.getInstance().nonEditable).setVisible(true);
    }

    /**
     * Returns the unique instance of Configuration.
     *
     * @return The unique instance of Configuration.
     */
    public static Configuration getInstance() {
        return INSTANCE;
    }

    /**
     * Returns a file filter accepting the directories.
     *
     * @return A file filter accepting the directories.
     */
    public static FileFilter getDirectoryFileFilter() {
        return new FileFilter() {

            /**
             * Tell whether or not the user is allowed to choose the given
             * file.
             *
             * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
             */
            @Override
            public boolean accept(final File f) {
                return f.isDirectory();
            }

            /**
             * Returns the descriptions of the files allowed by this file
             * filter.
             *
             * @see javax.swing.filechooser.FileFilter#getDescription()
             */
            @Override
            public String getDescription() {
                return null;
            }
        };
    }

    /**
     * Returns a file filter accepting the JMines screenshot files.
     *
     * @return A file filter accepting the JMines screenshot files.
     */
    public static FileFilter getScreenshotFileFilter() {
        final String suffix = getInstance().getString(KEY_FILE_SCREENSHOT_SUFFIX);

        return new FileFilter() {

            /**
             * Tell whether or not the user is allowed to choose the given
             * file.
             *
             * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
             */
            @Override
            public boolean accept(final File f) {
                return f.isDirectory() || f.getName().endsWith(suffix);
            }

            /**
             * Returns the descriptions of the files allowed by this file
             * filter.
             *
             * @see javax.swing.filechooser.FileFilter#getDescription()
             */
            @Override
            public String getDescription() {
                return Configuration.getInstance().getText(Configuration.KEY_TEXT_SCREENSHOTFILEDESCRIPTION);
            }
        };
    }

    /**
     * Returns a file filter accepting the JMines board files.
     *
     * @return A file filter accepting the JMines board files.
     */
    public static FileFilter getBoardFileFilter() {
        final String suffix = getInstance().getString(KEY_FILE_BOARD_SUFFIX);

        return new FileFilter() {

            /**
             * Tell whether or not the user is allowed to choose the given
             * file.
             *
             * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
             */
            @Override
            public boolean accept(final File f) {
                return f.isDirectory() || f.getName().endsWith(suffix);
            }

            /**
             * Returns the descriptions of the files allowed by this file
             * filter.
             *
             * @see javax.swing.filechooser.FileFilter#getDescription()
             */
            @Override
            public String getDescription() {
                return Configuration.getInstance().getText(Configuration.KEY_TEXT_BOARDFILEDESCRIPTION);
            }
        };
    }

    /**
     * Returns a file filter accepting the JMines video files.
     *
     * @return A file filter accepting the JMines video files.
     */
    public static FileFilter getVideoFileFilter() {
        final String suffix = getInstance().getString(KEY_FILE_VIDEO_SUFFIX);

        return new FileFilter() {

            /**
             * Tell whether or not the user is allowed to choose the given
             * file.
             *
             * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
             */
            @Override
            public boolean accept(final File f) {
                return f.isDirectory() || f.getName().endsWith(suffix);
            }

            /**
             * Returns the descriptions of the files allowed by this file
             * filter.
             *
             * @see javax.swing.filechooser.FileFilter#getDescription()
             */
            @Override
            public String getDescription() {
                return Configuration.getInstance().getText(Configuration.KEY_TEXT_VIDEOFILEDESCRIPTION);
            }
        };
    }

    //==========================================================================
    // Methods
    //==========================================================================
    /**
     * Used to load the default configuration file.
     */
    private void manageURLs() {
        URL classURL = Configuration.class.getResource(this.getClass().getSimpleName() + ".class");

        String protocol = classURL.getProtocol();

        String classURLString = classURL.toString();
        String persistencePackageURLString = classURLString.substring(0, classURLString.lastIndexOf('/'));
        String viewPackageURLString = persistencePackageURLString.substring(0, persistencePackageURLString.lastIndexOf('/'));
        String jminesPackageURLString = viewPackageURLString.substring(0, viewPackageURLString.lastIndexOf('/'));

        String resDirectoryURLString = null;
        if (protocol.equals("file")) {
            String srcDirectoryURL = jminesPackageURLString.substring(0, jminesPackageURLString.lastIndexOf('/'));
            String rootDirectoryURL = srcDirectoryURL.substring(0, srcDirectoryURL.lastIndexOf('/'));
            resDirectoryURLString = rootDirectoryURL + "/res";
        } else if (protocol.equals("jar")) {
            String rootDirectoryURL = jminesPackageURLString.substring(0, jminesPackageURLString.lastIndexOf('/'));
            resDirectoryURLString = rootDirectoryURL + "/res";
        }

        try {
            resDirectoryURL = new URL(resDirectoryURLString);
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

    /**
     * Used to load the default configuration file.
     */
    private void loadDefaultConfigurationFile() {
        // Load the default configuration properties file
        try {
            URL configurationProperties = new URL(resDirectoryURL  + "/" + DEFAULT_CONFIGURATION_FILE_NAME);
            defaultBundle = new PropertyResourceBundle(configurationProperties.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

    /**
     * Load the default language.
     */
    private void loadDefaultLanguage() {
        // Load the default language
        try {
            URL url = new URL(resDirectoryURL + "/lng/" + LANGUAGE_FILENAME_PREFIX + "default" + LANGUAGE_FILENAME_SUFFIX);
            defaultLanguageBundle = new PropertyResourceBundle(url.openStream());
        } catch (FileNotFoundException e) {
            System.exit(-1);
        } catch (IOException e) {
            System.exit(-1);
        }
    }

    /**
     * Use to load all the needed libraries.
     */
    private void manageLibraries() {
        try {
            // Add the library to encode/decode the user properties file
            loader.addURL(new URL(resDirectoryURL + "/lib/" + COMPRESSOR_LIBRARY_FILENAME));
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }

        // The library to encode/decode the user properties file
        try {
            classHuffmanFileOutputStream = Class.forName(USER_CONFIGURATION_FILE_ENCRYPTOR_CLASS_NAME, true, loader);
            classHuffmanFileInputStream = Class.forName(USER_CONFIGURATION_FILE_DECRYPTOR_CLASS_NAME, true, loader);
            classInputException = Class.forName(USER_CONFIGURATION_FILE_CRYPTOR_EXCEPTION_NAME, true, loader);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

    /**
     * Used to retrieve all the available locales.
     */
    private void manageLocales() {
        final int three = 3;

        try {
            URL lngDirectory = new URL(resDirectoryURL + "/lng/");

            if (lngDirectory.getProtocol().equals("file")) {
                // The language directory is a local file, we only have to read
                // the files it contains ...
                String[] files = new File(lngDirectory.toURI()).list();
                for (String file : files) {
                    if (file.startsWith(LANGUAGE_FILENAME_PREFIX) && file.endsWith(LANGUAGE_FILENAME_SUFFIX)) {
                        file = file.substring(LANGUAGE_FILENAME_PREFIX.length(), file.length() - LANGUAGE_FILENAME_SUFFIX.length());
                        if (!file.equals(LANGUAGE_DEFAULT_LOCALE)) {
                            String[] tmp = file.split("[_]");
                            if (tmp.length == 1) {
                                availableLocales.add(new Locale(tmp[0]));
                            } else if (tmp.length == 2) {
                                availableLocales.add(new Locale(tmp[0], tmp[1]));
                            } else if (tmp.length == three) {
                                availableLocales.add(new Locale(tmp[0], tmp[1], tmp[2]));
                            }
                        }
                    }
                }
            } else if (lngDirectory.getProtocol().equals("jar")) {
                // The language directory is a jar entry, we have to search the
                // entries in this directory ...
                URLConnection connection = lngDirectory.openConnection();
                if (connection instanceof JarURLConnection) {
                    Enumeration<JarEntry> entries = ((JarURLConnection) connection).getJarFile().entries();
                    for (JarEntry entry = entries.nextElement(); entries.hasMoreElements(); entry = entries.nextElement()) {
                        String simpleName = entry.getName();
                        if (simpleName.contains("/")) {
                            simpleName = simpleName.substring(simpleName.lastIndexOf('/') + 1);
                        }
                        if (simpleName.startsWith(LANGUAGE_FILENAME_PREFIX) && simpleName.endsWith(LANGUAGE_FILENAME_SUFFIX)) {
                            simpleName = simpleName.substring(LANGUAGE_FILENAME_PREFIX.length(), simpleName.length() - LANGUAGE_FILENAME_SUFFIX.length());
                            if (!simpleName.equals(LANGUAGE_DEFAULT_LOCALE)) {
                                String[] tmp = simpleName.split("[_]");
                                if (tmp.length == 1) {
                                    availableLocales.add(new Locale(tmp[0]));
                                } else if (tmp.length == 2) {
                                    availableLocales.add(new Locale(tmp[0], tmp[1]));
                                } else if (tmp.length == three) {
                                    availableLocales.add(new Locale(tmp[0], tmp[1], tmp[2]));
                                }
                            }
                        }
                    }
                }
            } else {
                // The language directory is distant, we can not do anything
                // other than trying to open all possible locales files ...
                for (String language : Locale.getISOLanguages()) {
                    new URL(lngDirectory + "language." + language + ".properties").openStream();
                    availableLocales.add(new Locale(language));
                    for (String country : Locale.getISOCountries()) {
                        new URL(lngDirectory + "language." + language + "_" + country + ".properties").openStream();
                        availableLocales.add(new Locale(language, country));
                    }
                }
            }
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        } catch (URISyntaxException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Used to instanciate the look and feels.
     */
    private void manageLookAndFeels() {
        //        // Add the look and feels libraries
        //        String[] lookAndFeelLibrariesNames = getString(KEY_LOOKANDFEEL_LIBRARIESNAMES).split("[;]");
        //        for (String lookAndFeelLibraryName : lookAndFeelLibrariesNames) {
        //            try {
        //                loader.addURL(new URL(resDirectory + "/laf/" + lookAndFeelLibraryName));
        //            } catch (MalformedURLException e) {
        //                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        //                System.exit(-1);
        //            }
        //        }

        //        // Instantiate the look and feels
        //        String[] lookAndFeelClassesNames = getString(KEY_LOOKANDFEEL_CLASSESNAMES).split("[;]");
        //        for (String lookAndFeelClassName : lookAndFeelClassesNames) {
        //            try {
        //                LookAndFeel laf = (LookAndFeel) Class.forName(lookAndFeelClassName, true, loader).newInstance();
        //                LookAndFeelInfo lafInfo = new LookAndFeelInfo(laf.getClass().getSimpleName(), laf.getClass().getName());
        //                boolean found = false;
        //                for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        //                    if (info.getClassName().equals(lafInfo.getClassName())) {
        //                        found = true;
        //                    }
        //                }
        //                if (!found) {
        //                    UIManager.installLookAndFeel(lafInfo);
        //                }
        //            } catch (InstantiationException e) {
        //                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", "Error", JOptionPane.ERROR_MESSAGE);
        //            } catch (IllegalAccessException e) {
        //                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", "Error", JOptionPane.ERROR_MESSAGE);
        //            } catch (ClassNotFoundException e) {
        //                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", "Error", JOptionPane.ERROR_MESSAGE);
        //            }
        //        }

        // Register the available look and feels
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            try {
                availableLookAndFeels.add((LookAndFeel) Class.forName(info.getClassName(), true, loader).newInstance());
            } catch (InstantiationException e) {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            } catch (IllegalAccessException e) {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }
        }
    }

    /**
     * Used to load the user configuration file.
     */
    private void loadUserConfigurationFile() {
        File userFile = new File(System.getProperty("user.home") + File.separator + USER_CONFIGURATION_DIRECTORY + File.separator + USER_CONFIGURATION_FILE_NAME);

        InputStream stream = null;
        try {
            Constructor<?> constructorHuffmanFileInputStream = classHuffmanFileInputStream.getConstructor(File.class);
            stream = (InputStream) constructorHuffmanFileInputStream.newInstance(userFile);
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (NoSuchMethodException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (InstantiationException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (IllegalAccessException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (InvocationTargetException e) {
            if (classInputException.isInstance(e.getTargetException())) {
                try {
                    userBundle = new PropertyResourceBundle(new FileInputStream(userFile));
                    saveUserConfigurationFile();
                    loadUserConfigurationFile();
                    JOptionPane.showMessageDialog(null, getText(KEY_TEXT_CONFIGURATIONFILECRYPTED));
                    return;
                } catch (FileNotFoundException e1) {
                    JOptionPane.showMessageDialog(null, e1.getClass().getSimpleName() + " (" + e1.getMessage() + ")", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, e1.getClass().getSimpleName() + " (" + e1.getMessage() + ")", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                }
            } else if (e.getTargetException() instanceof FileNotFoundException) {
                saveUserConfigurationFile();
                loadUserConfigurationFile();
                JOptionPane.showMessageDialog(null, getText(KEY_TEXT_CONFIGURATIONFILECREATED));
            } else {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }
        }

        // Load the user's configuration properties file
        if (userFile.exists() && stream != null) {
            try {
                userBundle = new PropertyResourceBundle(stream);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, getText(KEY_TEXT_CONFIGURATIONFILECREATED));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }
        }

        realTimeConfigurations.clear();
    }

    /**
     * Load the user language (found in the properties file) if the user
     * language is undefined, it loads the host default locale language.
     */
    private void loadUserLanguage() {
        final int three = 3;

        // Load the user language
        Locale locale = null;
        try {
            String tmp = getString(KEY_USER_LOCALE);
            String[] tmpArray = tmp.split("[_]");
            if (tmpArray.length == 0) {
                locale = Locale.getDefault();
            } else if (tmpArray.length == 1) {
                locale = new Locale(tmpArray[0]);
            } else if (tmpArray.length == 2) {
                locale = new Locale(tmpArray[0], tmpArray[1]);
            } else if (tmpArray.length == three) {
                locale = new Locale(tmpArray[0], tmpArray[1], tmpArray[2]);
            }
        } catch (MissingResourceException e) {
            locale = Locale.getDefault();
        }
        loadLanguage(locale);
    }

    //==========================================================================
    // Setters
    //==========================================================================

    //==========================================================================
    // Inherited methods
    //==========================================================================

    //==========================================================================
    // Static methods
    //==========================================================================
    /**
     */
    private void update() {
        for (String key : update) {
            putRealTimeconfiguration(key, getDefaultString(key));
        }
    }

    /**
     * Load a given language.
     *
     * @param locale The locale giving the language to load.
     */
    public void loadLanguage(final Locale locale) {
        // Load the user language
        try {
            URL url = new URL(resDirectoryURL + "/lng/" + LANGUAGE_FILENAME_PREFIX + locale.getLanguage() + "_" + locale.getCountry() + LANGUAGE_FILENAME_SUFFIX);
            userLanguageBundle = new PropertyResourceBundle(url.openStream());
            putRealTimeconfiguration(Configuration.KEY_USER_LOCALE, locale.getLanguage() + "_" + locale.getCountry());
        } catch (IOException e) {
            int nbFNFE = 0;
            try {
                URL url = new URL(resDirectoryURL + "/lng/" + LANGUAGE_FILENAME_PREFIX + locale.getLanguage() + LANGUAGE_FILENAME_SUFFIX);
                userLanguageBundle = new PropertyResourceBundle(url.openStream());
                putRealTimeconfiguration(Configuration.KEY_USER_LOCALE, locale.getLanguage());
            } catch (FileNotFoundException e1) {
                nbFNFE++;
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }
        }

        Language.setCurrentLocale(locale);
    }

    /**
     * Save new configurations in the user configuration file.
     */
    public void saveUserConfigurationFile() {
        File userFile = new File(System.getProperty("user.home") + File.separator + USER_CONFIGURATION_DIRECTORY + File.separator + USER_CONFIGURATION_FILE_NAME);

        // Empty the file
        if (userFile.exists()) {
            userFile.delete();
        }

        try {
            userFile.getParentFile().mkdirs();
            userFile.createNewFile();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        }

        OutputStream stream = null;
        try {
            Constructor<?> constructorHuffmanFileOutputStream = classHuffmanFileOutputStream.getConstructor(File.class);
            stream = (OutputStream) constructorHuffmanFileOutputStream.newInstance(userFile);
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (NoSuchMethodException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (InstantiationException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (IllegalAccessException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (InvocationTargetException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }

        // Create a writer for the file
        PrintWriter writer = null;
        writer = new PrintWriter(stream);

        // Write in the file
        for (String key : toSave) {
            if (key == null) {
                writer.println("");
            } else {
                try {
                    String value = getString(key);
                    if (value != null) {
                        writer.println(key + " = " + value);
                    }
                } catch (MissingResourceException e) {
                    writer.print("");
                }
            }
        }

        writer.flush();
        writer.close();
    }

    /**
     * Returns the text corresponding to a given key. First the key is searched
     * in the "real time" configuration map then (if not found) in the user
     * configuration bundle and then (if not found) in the default
     * configuration bundle.
     *
     * @param key The key we want the actual value. It's highly recommended to
     *            use one of the previously defined keys.
     * @return The more actual value associated to the key.
     */
    public String getText(final String key) {
        try {
            return userLanguageBundle.getString(key);
        } catch (MissingResourceException e) {
            return defaultLanguageBundle.getString(key);
        } catch (NullPointerException e) {
            return defaultLanguageBundle.getString(key);
        }
    }

    /**
     * Returns the String corresponding to a given key. First the key is
     * searched in the "real time" configuration map then (if not found) in the
     * user configuration bundle and then (if not found) in the default
     * configuration bundle.<br/>
     * Replace all the configurable tokens ({n} where n is an integer) by an
     * element of the parameters array ; number n configurable token is
     * replaced by the n-1<sup>th</sup> element of the parameters array.
     *
     * @param key The key we want the actual value. It's highly recommended to
     *            use one of the previously defined keys.
     * @param parameters The string by which the configurable tokens have to be
     *                   replaced.
     * @return The more actual value (with configurable tokens replaced by
     *         given strings) associated to the key.
     */
    public String getConfigurableText(final String key, final String[] parameters) {
        String template = getText(key);

        if (parameters == null) {
            return template;
        }

        String[] constants = template.split(CONFIGURABLE_TOKEN);

        String ret = "";
        for (String constant : constants) {
            ret = ret + constant;

            template = template.replace(constant, "");
            if (!template.equals("")) {
                String token = template.substring(1, template.indexOf(CONFIGURABLE_TOKEN_SUFFIX));
                template = template.substring(1 + template.indexOf(CONFIGURABLE_TOKEN_SUFFIX));
                try {
                    int n = Integer.parseInt(token);
                    if (n > 0) {
                        ret = ret + parameters[n - 1];
                    } else {
                        ret = ret + CONFIGURABLE_TOKEN_PREFIX + token + CONFIGURABLE_TOKEN_SUFFIX;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        return ret;
    }

    /**
     * Returns the boolean corresponding to a given key. First the key is
     * searched in the "real time" configuration map then (if not found) in the
     * user configuration bundle and then (if not found) in the default
     * configuration bundle.
     *
     * @param key The key we want the actual value. It's highly recommended to
     *            use one of the previously defined keys.
     * @return The more actual value associated to the key.
     */
    public boolean getBoolean(final String key) {
        return Boolean.parseBoolean(getString(key));
    }

    /**
     * Returns the integer corresponding to a given key. First the key is
     * searched in the "real time" configuration map then (if not found) in the
     * user configuration bundle and then (if not found) in the default
     * configuration bundle.
     *
     * @param key The key we want the actual value. It's highly recommended to
     *            use one of the previously defined keys.
     * @return The more actual value associated to the key.
     */
    public int getInt(final String key) {
        return Integer.parseInt(getString(key));
    }

    /**
     * Returns the integer corresponding to a given key in the given basis.
     * First the key is searched in the "real time" configuration map then (if
     * not found) in the user configuration bundle and then (if not found) in
     * the default configuration bundle.
     *
     * @param key The key we want the actual value. It's highly recommended to
     *            use one of the previously defined keys.
     * @param basis The basis in which the number has to be read.
     * @return The more actual value associated to the key.
     */
    public int getInt(final String key, final int basis) {
        return Integer.parseInt(getString(key), basis);
    }

    /**
     * Returns the String corresponding to a given key. First the key is
     * searched in the "real time" configuration map then (if not found) in the
     * user configuration bundle and then (if not found) in the default
     * configuration bundle.
     *
     * @param key The key we want the actual value. It's highly recommended to
     *            use one of the previously defined keys.
     * @return The more actual value associated to the key.
     */
    public String getString(final String key) {
        if (realTimeConfigurations.containsKey(key)) {
            return realTimeConfigurations.get(key);
        } else {
            try {
                return userBundle.getString(key);
            } catch (MissingResourceException e) {
                return defaultBundle.getString(key);
            } catch (NullPointerException e) {
                return defaultBundle.getString(key);
            }
        }
    }

    /**
     * Returns the default String corresponding to a given key.
     *
     * @param key The key we want the default value. It's highly recommended to
     *            use one of the previously defined keys.
     * @return The more actual value associated to the key.
     */
    public String getDefaultString(final String key) {
        try {
            return defaultBundle.getString(key);
        } catch (MissingResourceException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Returns the image corresponding to a given key. First the key is searched
     * in the "real time" configuration map then (if not found) in the user
     * configuration bundle and then (if not found) in the default
     * configuration bundle.

     * @param key The key corresponding to the name of the wanted image. It's
     *            highly recommended to use one of the previously defined keys.
     * @return The image which has for name the value associated to the given
     *         key.
     */
    public Image getImage(final String key) {
        BufferedImage ret;
        try {
            ret = ImageAccess.loadImage(new URL(resDirectoryURL + "/img/" + getString(key)));

            return ret;
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        }

        return null;
    }

    /**
     * Returns the image corresponding to a given prefix and state. First the
     * keys are searched in the "real time" configuration map then (if not
     * found) in the user configuration bundle and then (if not found) in the
     * default configuration bundle.
     *
     * @param prefix The prefix of the name of the wanted image.
     * @param indexKey The key of the index of the wanted image.
     * @return A part of the image which has a name prefixed by the given
     *         prefix, and cut a the given index.
     */
    public BufferedImage getImage(final String prefix, final String indexKey) {
        try {
            String filename = getString(prefix + ".filename");
            int width = getInt(prefix + Configuration.DOT + "width");
            int height = getInt(prefix + Configuration.DOT + "height");
            int index = getInt(prefix + Configuration.DOT + indexKey);

            BufferedImage ret = ImageAccess.loadImage(new URL(resDirectoryURL + "/img/" + filename));
            ret = ret.getSubimage(index * width, 0, width, height);

            return ret;
        } catch (MissingResourceException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", getText(KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            return new BufferedImage(0, 0, BufferedImage.TYPE_4BYTE_ABGR);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", getText(KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            return new BufferedImage(0, 0, BufferedImage.TYPE_4BYTE_ABGR);
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", getText(KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            return new BufferedImage(0, 0, BufferedImage.TYPE_4BYTE_ABGR);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", getText(KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            return new BufferedImage(0, 0, BufferedImage.TYPE_4BYTE_ABGR);
        }
    }

    /**
     * Returns the URL of the sound corresponding to the given name.
     *
     * @param key The name of the wanted sound file.
     * @return The URL to the given sound file.
     */
    public URL getSoundURL(final String key) {
        String filename = getString(key);

        if (filename.equals("")) {
            return null;
        } else {
            try {
                return new URL(resDirectoryURL + "/snd/" + filename);
            } catch (MalformedURLException e) {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
    }

    /**
     * Put a configuration modification in the real time matrix.
     *
     * @param key The key at which the new configuration has to be put.
     * @param value The value of the new configuration to put.
     */
    public void putRealTimeconfiguration(final String key, final String value) {
        realTimeConfigurations.put(key, value);
        saveUserConfigurationFile();
    }

    /**
     * Erase all the best times (in configuration file too).
     */
    public void clearBestTimes() {
        realTimeConfigurations.clear();

        String[] shapes = new String[] {SHAPE_TRIANGULAR, SHAPE_TRIANGULAR_14, SHAPE_SQUARE, SHAPE_PENTAGONAL, SHAPE_HEXAGONAL, SHAPE_OCTOSQUARE, SHAPE_PARQUET};
        String[] difficulties = new String[] {DIFFICULTY_BEGINNER, DIFFICULTY_INTERMEDIATE, DIFFICULTY_EXPERT};
        for (String shape : shapes) {
            for (String difficulty : difficulties) {
                putRealTimeconfiguration(shape + Configuration.DOT + difficulty + Configuration.DOT + SUFFIX_BEST, null);
            }
        }
        saveUserConfigurationFile();
    }
}
