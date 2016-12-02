/**
 *   Stigma - Multiplayer online RPG - http://stigma.sourceforge.net
 *   Copyright (C) 2005-2009 Minions Studio
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */
package pl.org.minions.stigma.editor.gui;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;

import pl.org.minions.stigma.databases.Resourcer;

/**
 * Class used to define constant settings for the GUI.
 */
public final class GUIConstants
{
    //TODO: change this class to provide settings from a properties file.

    /*
     * Main frame constants.
     */
    public static final int MAIN_FRAME_WIDTH = 1024;
    public static final int MAIN_FRAME_HEIGHT = 768;

    /*
     * Main view constants.
     */

    // Tree view
    public static final int MAIN_VIEW_TREE_WIDTH = 200;
    public static final int MAIN_VIEW_TREE_HEIGHT = 200;

    // Outline view
    public static final int MAIN_VIEW_OUTLINE_WIDTH = 200;
    public static final int MAIN_VIEW_OUTLINE_HEIGHT = 200;

    //Divider
    public static final int MAIN_VIEW_DIVIDER_LOCATION = 600;
    public static final int MAIN_VIEW_DIVIDER_SIZE = 3;

    public static final Color VERY_LIGHT_GRAY = new Color(0xF5F5F5);

    public static final int DIALOG_FLOW_MARGIN = 10;

    public static final Color SELECTED_ENTRY_ZONE_COLOR =
            new Color(255, 0, 0, 100);
    public static final Color ENTRY_ZONE_COLOR = new Color(128, 0, 0, 100);

    public static final Color SELECTED_EXIT_ZONE_COLOR =
            new Color(0, 255, 0, 100);
    public static final Color EXIT_ZONE_COLOR = new Color(0, 128, 0, 100);

    public static final Color SELECTED_LIST_ROW_COLOR =
            new Color(218, 218, 235);

    public static final int ZONES_OUTLINE_WIDTH = 200;
    public static final int ZONES_OUTLINE_ITEM_ROW_WIDTH = 160;
    /*
     * Images & icons.
     */

    private static final String PATH_TO_STIGMA_ICON =
            "img/default/stigma_icon_64x64.png";
    private static final String PATH_TO_BUNNY = "img/editor/bun.png";
    private static final String PATH_TO_BUNNY_16PX = "img/editor/bun_16.png";
    private static final String PATH_TO_BUNNY_24PX = "img/editor/bun_24.png";

    private static final String PATH_TO_WARNING_ICON =
            "img/editor/icons/warning.png";
    private static final String PATH_TO_ERROR_ICON =
            "img/editor/icons/error.png";
    private static final String PATH_TO_INFO_ICON = "img/editor/icons/info.png";

    private static final String PATH_TO_CLOSE_ICON =
            "img/editor/icons/close_icon.png";

    private static final String PATH_TO_NEW_ICON =
            "img/editor/icons/new_bunny.png";
    private static final String PATH_TO_LOAD_ICON =
            "img/editor/icons/load_bunny.png";
    private static final String PATH_TO_SAVE_ICON =
            "img/editor/icons/save_bunny.png";

    private static final String PATH_TO_DELETE_ICON =
            "img/editor/icons/delete.png";
    private static final String PATH_TO_DELETE_ICON_PRESSED =
            "img/editor/icons/delete_pressed.png";
    private static final String PATH_TO_DELETE_ICON_ROLLOVER =
            "img/editor/icons/delete_rollover.png";

    private static final String PATH_TO_ENTRYZONE_ICON =
            "img/editor/icons/entryzone.png";
    private static final String PATH_TO_ENTRYZONE_ADD_ICON =
            "img/editor/icons/entryzone_add.png";
    private static final String PATH_TO_ENTRYZONE_ICON_PRESSED =
            "img/editor/icons/entryzone_pressed.png";
    private static final String PATH_TO_ENTRYZONE_ICON_ROLLOVER =
            "img/editor/icons/entryzone_rollover.png";

    private static final String PATH_TO_EXITZONE_ICON =
            "img/editor/icons/exitzone.png";
    private static final String PATH_TO_EXITZONE_ADD_ICON =
            "img/editor/icons/exitzone_add.png";
    private static final String PATH_TO_EXITZONE_ICON_PRESSED =
            "img/editor/icons/exitzone_pressed.png";
    private static final String PATH_TO_EXITZONE_ICON_ROLLOVER =
            "img/editor/icons/exitzone_rollover.png";

    private static final String PATH_TO_MAP_ICON = "img/editor/icons/map.png";
    private static final String PATH_TO_MAP_ICON_16PX =
            "img/editor/icons/map_16_1.png";

    public static final Image STIGMA_ICON_IMAGE =
            Resourcer.loadImage(PATH_TO_STIGMA_ICON);

    public static final Image BUNNY_IMAGE = Resourcer.loadImage(PATH_TO_BUNNY);
    public static final ImageIcon BUNNY_ICON =
            Resourcer.loadIcon(PATH_TO_BUNNY);
    public static final ImageIcon BUNNY_ICON_16PX =
            Resourcer.loadIcon(PATH_TO_BUNNY_16PX);
    public static final ImageIcon BUNNY_ICON_24PX =
            Resourcer.loadIcon(PATH_TO_BUNNY_24PX);

    public static final ImageIcon MAP_ICON =
            Resourcer.loadIcon(PATH_TO_MAP_ICON);
    public static final ImageIcon MAP_ICON_16PX =
            Resourcer.loadIcon(PATH_TO_MAP_ICON_16PX);

    public static final ImageIcon NEW_ICON =
            Resourcer.loadIcon(PATH_TO_NEW_ICON);
    public static final ImageIcon LOAD_ICON =
            Resourcer.loadIcon(PATH_TO_LOAD_ICON);
    public static final ImageIcon SAVE_ICON =
            Resourcer.loadIcon(PATH_TO_SAVE_ICON);

    public static final ImageIcon CLOSE_ICON =
            Resourcer.loadIcon(PATH_TO_CLOSE_ICON);

    public static final ImageIcon INFO_ICON =
            Resourcer.loadIcon(PATH_TO_INFO_ICON);
    public static final ImageIcon WARNING_ICON =
            Resourcer.loadIcon(PATH_TO_WARNING_ICON);
    public static final ImageIcon ERROR_ICON =
            Resourcer.loadIcon(PATH_TO_ERROR_ICON);

    public static final ImageIcon DELETE_ICON =
            Resourcer.loadIcon(PATH_TO_DELETE_ICON);
    public static final ImageIcon DELETE_ICON_PRESSED =
            Resourcer.loadIcon(PATH_TO_DELETE_ICON_PRESSED);
    public static final ImageIcon DELETE_ICON_ROLLOVER =
            Resourcer.loadIcon(PATH_TO_DELETE_ICON_ROLLOVER);

    public static final ImageIcon ENTRYZONE_ICON =
            Resourcer.loadIcon(PATH_TO_ENTRYZONE_ICON);
    public static final ImageIcon ENTRYZONE_ADD_ICON =
            Resourcer.loadIcon(PATH_TO_ENTRYZONE_ADD_ICON);
    public static final ImageIcon ENTRYZONE_ICON_PRESSED =
            Resourcer.loadIcon(PATH_TO_ENTRYZONE_ICON_PRESSED);
    public static final ImageIcon ENTRYZONE_ICON_ROLLOVER =
            Resourcer.loadIcon(PATH_TO_ENTRYZONE_ICON_ROLLOVER);

    public static final ImageIcon EXITZONE_ICON =
            Resourcer.loadIcon(PATH_TO_EXITZONE_ICON);
    public static final ImageIcon EXITZONE_ADD_ICON =
            Resourcer.loadIcon(PATH_TO_EXITZONE_ADD_ICON);
    public static final ImageIcon EXITZONE_ICON_PRESSED =
            Resourcer.loadIcon(PATH_TO_EXITZONE_ICON_PRESSED);
    public static final ImageIcon EXITZONE_ICON_ROLLOVER =
            Resourcer.loadIcon(PATH_TO_EXITZONE_ICON_ROLLOVER);

    private GUIConstants()
    {

    }
}
