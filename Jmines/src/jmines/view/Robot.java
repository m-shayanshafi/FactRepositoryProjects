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
package jmines.view;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;

import javax.swing.JOptionPane;

import jmines.control.listeners.MouseListenerForGamePanel;
import jmines.control.listeners.KeyListenerForMainFrame;
import jmines.model.Tile;
import jmines.model.TilesShapeUnsupportedException;
import jmines.view.components.MainPanel;
import jmines.view.persistence.Configuration;

/**
 * The class representing the robot that can play to JMines.<br/>
 * Thanks to waskol for its algorithms (with a little adaptation to work with
 * all the button shapes)
 * (<a href="http://delphi.developpez.com/defi/demineur/defieur/" target="_blank">http://delphi.developpez.com/defi/demineur/defieur/</a>).
 *
 * @author Zleurtor
 */
public final class Robot {

    //==========================================================================
    // Static attributes
    //==========================================================================

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The main panel in which the robot has to play.
     */
    private final MainPanel mainPanel;
    /**
     * The robot's name.
     */
    private String name;
    /**
     * Tell whether or not the robot is authorized to cheat.
     */
    private boolean cheater;
    /**
     * Tell whether or not the robot do real clicks.
     */
    private boolean reallyClicking;
    /**
     * Tell whether or not the robot is authorized to use random tries.
     */
    private boolean randomAuthorized;
    /**
     * The awtRobot used to simulate mouse movements.
     */
    private final java.awt.Robot awtRobot;
    /**
     * The centers of the game panel tiles.
     */
    private Point[][] centers;
    /**
     * The size of searched schemas. The more it is high the more the
     * computation is long.
     */
    private int schemasLevel = 1;
    /**
     * Tell whether or not the robot is playing.
     */
    private boolean playing = false;
    /**
     * Tell whether or not the robot is helping the player.
     */
    private boolean helping;

    /**
     * The number of games played by the robot.
     */
    private final Map<String, Integer> played = new HashMap<String, Integer>();
    /**
     * The number of games won by the robot.
     */
    private final Map<String, Integer> won = new HashMap<String, Integer>();
    /**
     * The medium time to win (in milliseconds).
     */
    private final Map<String, Long> mediumTime = new HashMap<String, Long>();
    /**
     * The minimum time to win (in milliseconds).
     */
    private final Map<String, Long> minimumTime = new HashMap<String, Long>();
    /**
     * The maximum time to win (in milliseconds).
     */
    private final Map<String, Long> maximumTime = new HashMap<String, Long>();

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new Robot.
     *
     * @param newMainPanel The main panel in which the robot has to play.
     */
    public Robot(final MainPanel newMainPanel) {
        this.mainPanel = newMainPanel;

        try {
            name = Configuration.getInstance().getString(Configuration.KEY_ROBOT_NAME);
        } catch (MissingResourceException e) {
            name = Configuration.getInstance().getString(Configuration.KEY_ROBOT_DEFAULTNAME);
        }
        cheater = Configuration.getInstance().getBoolean(Configuration.KEY_ROBOT_CHEAT);
        reallyClicking = Configuration.getInstance().getBoolean(Configuration.KEY_ROBOT_REALCLICKS);
        randomAuthorized = Configuration.getInstance().getBoolean(Configuration.KEY_ROBOT_RANDOMTRIES);
        helping = Configuration.getInstance().getBoolean(Configuration.KEY_ROBOT_HELP);

        java.awt.Robot tmp = null;
        try {
            tmp = new java.awt.Robot();
        } catch (AWTException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
        awtRobot = tmp;

        updateStatistics();
    }

    //==========================================================================
    // Getters
    //==========================================================================
    /**
     * Returns the robot's name.
     *
     * @return The robot's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Tell whether or not the robot is authorized to cheat.
     *
     * @return true if the robot is authorized to cheat, false otherwise.
     */
    public boolean isCheater() {
        return cheater;
    }

    /**
     * Tell whether or not the robot do real clicks.
     *
     * @return true if the robot do real clicks, false otherwise.
     */
    public boolean isReallyClicking() {
        return reallyClicking;
    }

    /**
     * Tell whether or not the robot is authorized to use random tries.
     *
     * @return true if the robot is authorized to use random tries, false
     *         otherwise.
     */
    public boolean isRandomAuthorized() {
        return randomAuthorized;
    }

    /**
     * Tell whether or not the robot is playing.
     *
     * @return true if the robot is playing, false otherwise.
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * Tell whether or not the robot is helping the player.
     *
     * @return true if the robot is helping the player, false otherwise.
     */
    public boolean isHelping() {
        return helping;
    }

    /**
     * Returns a map linking each [level, tiles shape] to a the number of times
     * it has been played.
     *
     * @return A map linking each [level, tiles shape] to a the number of times
     *         it has been played.
     */
    public Map<String, Integer> getPlayed() {
        return played;
    }

    /**
     * Returns a map linking each [level, tiles shape] to a the number of times
     * it has been won.
     *
     * @return A map linking each [level, tiles shape] to a the number of times
     *         it has been won.
     */
    public Map<String, Integer> getWon() {
        return won;
    }

    /**
     * Returns a map linking each [level, tiles shape] to the medium time used
     * to win.
     *
     * @return A map linking each [level, tiles shape] to the medium time used
     *         to win.
     */
    public Map<String, Long> getMediumTime() {
        return mediumTime;
    }

    /**
     * Returns a map linking each [level, tiles shape] to the minimum time used
     * to win.
     *
     * @return A map linking each [level, tiles shape] to the minimum time used
     *         to win.
     */
    public Map<String, Long> getMinimumTime() {
        return minimumTime;
    }

    /**
     * Returns a map linking each [level, tiles shape] to the maximum time used
     * to win.
     *
     * @return A map linking each [level, tiles shape] to the maximum time used
     *         to win.
     */
    public Map<String, Long> getMaximumTime() {
        return maximumTime;
    }

    //==========================================================================
    // Setters
    //==========================================================================
    /**
     * Allows to set a new value to the robot's name.
     *
     * @param newName The new robot's name.
     */
    public void setName(final String newName) {
        this.name = newName;
    }

    /**
     * Allows to set a new value to tell whether or not the robot is authorized
     * to cheat.
     *
     * @param newCheater If true, the robot is authorized to cheat, it is not
     *                   authorized if false.
     */
    public void setCheater(final boolean newCheater) {
        this.cheater = newCheater;
    }

    /**
     * Allows to set a new value to tell whether or not the robot has to do
     * real clicks.
     *
     * @param newReallyClicking If true the robot has to do real clicks, it
     *                          will not really click if false.
     */
    public void setReallyClicking(final boolean newReallyClicking) {
        this.reallyClicking = newReallyClicking;
    }

    /**
     * Allows to set a new value to tell whether or not the robot is authorized
     * to use random tries.
     *
     * @param newRandomAuthorized If true the robot is authorized to use random
     *                            tries, it's not authorized if false.
     */
    public void setRandomAuthorized(final boolean newRandomAuthorized) {
        this.randomAuthorized = newRandomAuthorized;
    }

    /**
     * Allows to set a new value to tell whether or not the robot is helping
     * the player.
     *
     * @param newHelping If true the robot will help the player, it will not do
     *                   this if false.
     */
    public void setHelping(final boolean newHelping) {
        this.helping = newHelping;
    }

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
     * Search the center of each tile present on the game board.
     */
    private void searchCenters() {
        // Search the center of each tile
        centers = new Point[mainPanel.getGamePanel().getPolygons().length][];

        for (int l = 0; l < centers.length; l++) {
            centers[l] = new Point[mainPanel.getGamePanel().getPolygons()[l].length];

            for (int c = 0; c < centers[l].length; c++) {
                Polygon polygon = mainPanel.getGamePanel().getPolygons()[l][c];

                if (polygon != null) {
                    double xSum = 0;
                    for (int i = 0; i < polygon.xpoints.length; i++) {
                        xSum += polygon.xpoints[i];
                    }

                    double ySum = 0;
                    for (int i = 0; i < polygon.ypoints.length; i++) {
                        ySum += polygon.ypoints[i];
                    }

                    centers[l][c] = new Point(
                            (int) (xSum / polygon.npoints),
                            (int) (ySum / polygon.npoints));
                }
            }
        }
    }

    /**
     * Search and solve the two trivial cases in the grid.
     *
     * @return true if a trivial case has been found and solved, false
     *         otherwise.
     */
    private boolean searchTrivialCases() {
        boolean actionWasDone = false;

        // Searching for trivial cases
        try  {
            for (int l = 0; l < centers.length; l++) {
                for (int c = 0; c < centers[l].length; c++) {
                    Tile tile = mainPanel.getGamePanel().getGameBoard().getTile(l, c);
                    double surroundingMines = mainPanel.getGamePanel().getGameBoard().getNumberOfSurroundingMines(tile);

                    double surroundingFlags = 0;
                    double surroundingClosed = 0;
                    if (tile != null && tile.isOpen() && surroundingMines > 0) {
                        for (Tile tmp : mainPanel.getGamePanel().getGameBoard().getNeighborhood(tile)) {
                            if (tmp != null && !tmp.isOpen() && !tmp.isFlagged()) {
                                surroundingClosed++;
                            } else if (tmp != null && tmp.isFlagged()) {
                                surroundingFlags++;
                            }
                        }

                        if (surroundingMines == surroundingFlags + surroundingClosed && surroundingClosed > 0) {
                            // We have to flag all the surrounding closed tiles
                            for (Tile tmp : mainPanel.getGamePanel().getGameBoard().getNeighborhood(tile)) {
                                if (tmp != null && !tmp.isOpen() && !tmp.isFlagged()) {
                                    Point coordinates = mainPanel.getGamePanel().getGameBoard().getTileCoordinates(tmp);
                                    clickTo(coordinates.y, coordinates.x, MouseEvent.BUTTON3_MASK);

                                    actionWasDone = true;
                                }
                            }
                        } else if (surroundingMines == surroundingFlags && surroundingClosed > 0) {
                            // We have to open all the surrounding closed tiles
                            Point coordinates = mainPanel.getGamePanel().getGameBoard().getTileCoordinates(tile);
                            clickTo(coordinates.y, coordinates.x, MouseEvent.BUTTON1_MASK | MouseEvent.BUTTON3_MASK);

                            actionWasDone = true;
                        }
                    }
                }
            }
        } catch (TilesShapeUnsupportedException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        }

        return actionWasDone;
    }

    /**
     * Search and solve schemas in the grid.
     *
     * @return true if a schema has been found and solved, false otherwise.
     */
    private boolean searchSchemas() {
        boolean actionWasDone = false;

        // Searching for schemas
        try {
            for (int l = 0; l < centers.length; l++) {
                for (int c = 0; c < centers[l].length; c++) {
                    Tile tile = mainPanel.getGamePanel().getGameBoard().getTile(l, c);

                    // Define the search grid
                    List<Tile> searchGrid = new ArrayList<Tile>();
                    searchGrid.add(tile);
                    for (int i = 0; i < schemasLevel; i++) {
                        int n = searchGrid.size();
                        for (int j = 0; j < n; j++) {
                            for (Tile tmp : mainPanel.getGamePanel().getGameBoard().getNeighborhood(searchGrid.get(j))) {
                                if (!searchGrid.contains(tmp)) {
                                    searchGrid.add(tmp);
                                }
                            }
                        }
                    }

                    // Identifiy the tiles to test
                    Collection<Tile> tilesToTest = new ArrayList<Tile>();
                    for (Tile toTest : searchGrid) {
                        int nbMines = mainPanel.getGamePanel().getGameBoard().getNumberOfSurroundingMines(toTest);
                        if (toTest != null && toTest.isOpen() && nbMines > 0) {
                            tilesToTest.add(toTest);
                        }
                    }

                    if (tilesToTest.size() > 1) {

                        // Search the schemas
                        List<Tile> unknownTilesInSearchGrid = new ArrayList<Tile>();
                        for (Tile tmp : searchGrid) {
                            if (tmp != null && !tmp.isOpen() && !tmp.isFlagged()) {
                                unknownTilesInSearchGrid.add(tmp);
                            }
                        }
                        if (unknownTilesInSearchGrid.size() == 0) {
                            continue;
                        }
                        Collection<String> schemas = getSchemas(unknownTilesInSearchGrid.size());

                        // Validate the schemas
                        Collection<String> validSchemas = new ArrayList<String>();
                        for (String schema : schemas) {
                            boolean isValid = true;

                            for (Tile toTest : tilesToTest) {

                                int flaggedNeighbors = 0;
                                int unknownNeighbors = 0;
                                int unknownNeighborsInSearchGrid = 0;
                                try {
                                    for (Tile neighbor : mainPanel.getGamePanel().getGameBoard().getNeighborhood(toTest)) {
                                        if (neighbor != null && neighbor.isFlagged()) {
                                            flaggedNeighbors++;
                                        } else if (neighbor != null && !neighbor.isOpen() && !neighbor.isFlagged()) {
                                            unknownNeighbors++;

                                            if (searchGrid.contains(neighbor)) {
                                                unknownNeighborsInSearchGrid++;
                                            }
                                        }

                                    }
                                } catch (TilesShapeUnsupportedException e) {
                                    JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
                                }

                                int nbSurroundingPotentialMines = unknownNeighbors - unknownNeighborsInSearchGrid;
                                if (mainPanel.getGamePanel().getGameBoard().getNumberOfSurroundingMines(toTest) < nbSurroundingPotentialMines) {
                                    nbSurroundingPotentialMines = mainPanel.getGamePanel().getGameBoard().getNumberOfSurroundingMines(toTest);
                                }

                                int nbSurroundingSupposedMines = 0;
                                for (Tile tmp : mainPanel.getGamePanel().getGameBoard().getNeighborhood(toTest)) {
                                    if (unknownTilesInSearchGrid.contains(tmp) && !tmp.isOpen() && !tmp.isFlagged() && schema.charAt(unknownTilesInSearchGrid.indexOf(tmp)) == '1') {
                                        nbSurroundingSupposedMines++;
                                    }
                                }

                                // 1st test
                                if (nbSurroundingSupposedMines > mainPanel.getGamePanel().getGameBoard().getNumberOfSurroundingMines(toTest)) {
                                    isValid = false;
                                }

                                // 2nd test
                                if (nbSurroundingPotentialMines == 0) {
                                    if (nbSurroundingSupposedMines + flaggedNeighbors != mainPanel.getGamePanel().getGameBoard().getNumberOfSurroundingMines(toTest)) {
                                        isValid = false;
                                    }
                                }

                                // 3rd test
                                if (nbSurroundingPotentialMines > 0) {
                                    if (nbSurroundingSupposedMines + nbSurroundingPotentialMines + flaggedNeighbors < mainPanel.getGamePanel().getGameBoard().getNumberOfSurroundingMines(toTest)) {
                                        isValid = false;
                                    }
                                }
                            }

                            if (isValid) {
                                validSchemas.add(schema);
                            }
                        }

                        // Analyzing the remaining valid schemas
                        String and = and(validSchemas);
                        String or = or(validSchemas);

                        for (int i = 0; and != null && i < and.length(); i++) {
                            if (and.charAt(i) == '1') {
                                Point coords = mainPanel.getGamePanel().getGameBoard().getTileCoordinates(unknownTilesInSearchGrid.get(i));
                                clickTo(coords.y, coords.x, MouseEvent.BUTTON3_MASK);

                                actionWasDone = true;
                            }
                        }

                        for (int i = 0; or != null && i < or.length(); i++) {
                            if (or.charAt(i) == '0') {
                                Point coords = mainPanel.getGamePanel().getGameBoard().getTileCoordinates(unknownTilesInSearchGrid.get(i));
                                clickTo(coords.y, coords.x, MouseEvent.BUTTON1_MASK);

                                actionWasDone = true;
                            }
                        }
                    }
                }
            }
        } catch (TilesShapeUnsupportedException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        }

        return actionWasDone;
    }

    /**
     * Search and solve all the constraints in the grid.
     *
     * @return true if constraints have been found and solved, false otherwise.
     */
    private boolean searchConstraintSatisfactionProblem() {
        // Using Constraint Satisfaction Problem
        return false;
    }

    /**
     * Search randomly a closed and non flagged tile.
     *
     * @return true a tile has been chosen and clicked, false otherwise.
     */
    private boolean searchRandomly() {
        List<Tile> closed = new ArrayList<Tile>();

        for (int l = 0; l < centers.length; l++) {
            for (int c = 0; c < centers[l].length; c++) {
                Tile tile = mainPanel.getGamePanel().getGameBoard().getTile(l, c);
                if (tile != null && !tile.isOpen() && !tile.isFlagged()) {
                    closed.add(tile);
                }
            }
        }

        if (closed.size() > 0) {
            Point coordinates = mainPanel.getGamePanel().getGameBoard().getTileCoordinates(closed.get((int) Math.floor(closed.size() * Math.random())));
            clickTo(coordinates.y, coordinates.x, MouseEvent.BUTTON1_MASK);

            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns all the possible schemas for a given number of unknown tiles.
     *
     * @param length The number of unknown tiles.
     * @return All the possible schemas for a given number of unknown tiles.
     */
    private Collection<String> getSchemas(final int length) {
        return getSchemas(length, "");
    }

    /**
     * Returns all the possible schemas for a given number of unknown tiles.
     *
     * @param length The number of unknown tiles.
     * @param actual A parameter only used for recursivity.
     * @return All the possible schemas for a given number of unknown tiles.
     */
    private Collection<String> getSchemas(final int length, final String actual) {
        Collection<String> ret = new ArrayList<String>();

        if (length == actual.length()) {
            ret.add(actual);
        } else {
            ret.addAll(getSchemas(length, actual + "0"));
            ret.addAll(getSchemas(length, actual + "1"));
        }

        return ret;
    }

    /**
     * Returns the binary and between the bits series defined by the strings
     * contained in the given Collection.
     *
     * @param schemas The definition of the bits series we want to do the
     *                binary and.
     * @return The binary and between the bits series defined by the strings
     *         contained in the given Collection.
     */
    private String and(final Collection<String> schemas) {
        String ret = null;

        for (String schema : schemas) {
            if (ret == null) {
                ret = schema;
            } else {
                String tmp = "";

                for (int i = 0; i < schema.length() && i < ret.length(); i++) {
                    if (ret.charAt(i) == '1' && schema.charAt(i) == '1') {
                        tmp = tmp + "1";
                    } else {
                        tmp = tmp + "0";
                    }
                }

                ret = tmp;
            }
        }

        return ret;
    }

    /**
     * Returns the binary or between the bits series defined by the strings
     * contained in the given Collection.
     *
     * @param schemas The definition of the bits series we want to do the
     *                binary or.
     * @return The binary or between the bits series defined by the strings
     *         contained in the given Collection.
     */
    private String or(final Collection<String> schemas) {
        String ret = null;

        for (String schema : schemas) {
            if (ret == null) {
                ret = schema;
            } else {
                String tmp = "";

                for (int i = 0; i < schema.length() && i < ret.length(); i++) {
                    if (ret.charAt(i) == '1' || schema.charAt(i) == '1') {
                        tmp = tmp + "1";
                    } else {
                        tmp = tmp + "0";
                    }
                }

                ret = tmp;
            }
        }

        return ret;
    }

    /**
     * Click to a given tile (given its line and column).
     *
     * @param l The line of the tile we have to click on.
     * @param c The column of the tile we have to click on.
     * @param buttons The mouse button(s) we have to click with.
     */
    private void clickTo(final int l, final int c, final int buttons) {
        int x = centers[l][c].x;
        int y = centers[l][c].y;

        if (isReallyClicking()) {
            awtRobot.mouseMove(x + mainPanel.getGamePanel().getLocationOnScreen().x + mainPanel.getGamePanel().getBorder().getBorderInsets(mainPanel.getGamePanel()).left,
                    y + mainPanel.getGamePanel().getLocationOnScreen().y + mainPanel.getGamePanel().getBorder().getBorderInsets(mainPanel.getGamePanel()).top);
        }

        if (buttons == MouseEvent.BUTTON1_MASK) {
            mainPanel.getGamePanel().getMouseListener().mousePressed(new MouseEvent(mainPanel.getGamePanel(), MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), buttons, x, y, 1, false));
            mainPanel.getGamePanel().getMouseListener().mouseReleased(new MouseEvent(mainPanel.getGamePanel(), MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), buttons, x, y, 1, false));
        } else if (buttons == MouseEvent.BUTTON3_MASK) {
            mainPanel.getGamePanel().getMouseListener().mousePressed(new MouseEvent(mainPanel.getGamePanel(), MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), buttons, x, y, 1, false));
            mainPanel.getGamePanel().getMouseListener().mouseReleased(new MouseEvent(mainPanel.getGamePanel(), MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), buttons, x, y, 1, false));
        } else if (buttons == (MouseEvent.BUTTON1_MASK | MouseEvent.BUTTON3_MASK)) {
            mainPanel.getGamePanel().getMouseListener().mousePressed(new MouseEvent(mainPanel.getGamePanel(), MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), MouseEvent.BUTTON1_MASK, x, y, 1, false));
            mainPanel.getGamePanel().getMouseListener().mousePressed(new MouseEvent(mainPanel.getGamePanel(), MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), MouseEvent.BUTTON3_MASK, x, y, 1, false));
            mainPanel.getGamePanel().getMouseListener().mouseReleased(new MouseEvent(mainPanel.getGamePanel(), MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), MouseEvent.BUTTON1_MASK, x, y, 1, false));
            mainPanel.getGamePanel().getMouseListener().mouseReleased(new MouseEvent(mainPanel.getGamePanel(), MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), MouseEvent.BUTTON3_MASK, x, y, 1, false));
        }
    }

    /**
     * Store the statistics to the Configuration.
     */
    private void storeRobotStatistics() {
        Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_ROBOT_STATISTICS_PLAYED, played.toString());
        Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_ROBOT_STATISTICS_WON, won.toString());
        Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_ROBOT_STATISTICS_MEDIUM, mediumTime.toString());
        Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_ROBOT_STATISTICS_MAXIMUM, maximumTime.toString());
        Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_ROBOT_STATISTICS_MINIMUM, minimumTime.toString());
    }

    /**
     * Retrieve the statistics from the Configuration.
     */
    private void updateStatistics() {
        String playedString = Configuration.getInstance().getString(Configuration.KEY_ROBOT_STATISTICS_PLAYED);
        String wonString = Configuration.getInstance().getString(Configuration.KEY_ROBOT_STATISTICS_WON);
        String mediumString = Configuration.getInstance().getString(Configuration.KEY_ROBOT_STATISTICS_MEDIUM);
        String maximumString = Configuration.getInstance().getString(Configuration.KEY_ROBOT_STATISTICS_MINIMUM);
        String minimumString = Configuration.getInstance().getString(Configuration.KEY_ROBOT_STATISTICS_MAXIMUM);

        playedString = playedString.substring(1, playedString.length() - 1);
        String[] playedArray = playedString.split("[,]{1}");
        for (String tmp : playedArray) {
            if (tmp.contains("=")) {
                String key = tmp.substring(0, tmp.indexOf('=')).trim();
                String value = tmp.substring(tmp.indexOf('=') + 1).trim();
                played.put(key, Integer.parseInt(value));
            }
        }

        wonString = wonString.substring(1, wonString.length() - 1);
        String[] wonArray = wonString.split("[,]{1}");
        for (String tmp : wonArray) {
            if (tmp.contains("=")) {
                String key = tmp.substring(0, tmp.indexOf('=')).trim();
                String value = tmp.substring(tmp.indexOf('=') + 1).trim();
                won.put(key, Integer.parseInt(value));
            }
        }

        mediumString = mediumString.substring(1, mediumString.length() - 1);
        String[] mediumArray = mediumString.split("[,]{1}");
        for (String tmp : mediumArray) {
            if (tmp.contains("=")) {
                String key = tmp.substring(0, tmp.indexOf('=')).trim();
                String value = tmp.substring(tmp.indexOf('=') + 1).trim();
                mediumTime.put(key, Long.parseLong(value));
            }
        }

        minimumString = minimumString.substring(1, minimumString.length() - 1);
        String[] minimumArray = minimumString.split("[,]{1}");
        for (String tmp : minimumArray) {
            if (tmp.contains("=")) {
                String key = tmp.substring(0, tmp.indexOf('=')).trim();
                String value = tmp.substring(tmp.indexOf('=') + 1).trim();
                minimumTime.put(key, Long.parseLong(value));
            }
        }

        maximumString = maximumString.substring(1, maximumString.length() - 1);
        String[] maximumArray = maximumString.split("[,]{1}");
        for (String tmp : maximumArray) {
            if (tmp.contains("=")) {
                String key = tmp.substring(0, tmp.indexOf('=')).trim();
                String value = tmp.substring(tmp.indexOf('=') + 1).trim();
                maximumTime.put(key, Long.parseLong(value));
            }
        }
    }

    /**
     * Tell whether or not the current grid contains trivial case(s).
     *
     * @return true if the current grid contains trivial case(s), false
     *         otherwise.
     */
    public boolean containsTrivialCases() {
        boolean ret = false;

        // Searching for trivial cases
        try  {
            for (int l = 0; l < mainPanel.getGamePanel().getGameBoard().getHeight() && !ret; l++) {
                for (int c = 0; c < mainPanel.getGamePanel().getGameBoard().getWidth() && !ret; c++) {
                    Tile tile = mainPanel.getGamePanel().getGameBoard().getTile(l, c);
                    double surroundingMines = mainPanel.getGamePanel().getGameBoard().getNumberOfSurroundingMines(tile);

                    double surroundingFlags = 0;
                    double surroundingClosed = 0;
                    if (tile != null && tile.isOpen() && surroundingMines > 0) {
                        for (Tile tmp : mainPanel.getGamePanel().getGameBoard().getNeighborhood(tile)) {
                            if (tmp != null && !tmp.isOpen() && !tmp.isFlagged()) {
                                surroundingClosed++;
                            } else if (tmp != null && tmp.isFlagged()) {
                                surroundingFlags++;
                            }
                        }

                        if (surroundingMines == surroundingFlags + surroundingClosed && surroundingClosed > 0) {
                            // We have to flag all the surrounding closed tiles
                            ret = true;
                        } else if (surroundingMines == surroundingFlags && surroundingClosed > 0) {
                            // We have to open all the surrounding closed tiles
                            ret = true;
                        }
                    }
                }
            }
        } catch (TilesShapeUnsupportedException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        }

        return ret;
    }

    /**
     * Tell whether or not the current grid contains solvable schema(s).
     *
     * @return true if the current grid contains solvable schema(s)., false
     *         otherwise.
     */
    public boolean containsSchemas() {
        boolean ret = false;

        // Searching for schemas
        try {
            for (int l = 0; l < mainPanel.getGamePanel().getGameBoard().getHeight() && !ret; l++) {
                for (int c = 0; c < mainPanel.getGamePanel().getGameBoard().getHeight() && !ret; c++) {
                    Tile tile = mainPanel.getGamePanel().getGameBoard().getTile(l, c);

                    // Define the search grid
                    List<Tile> searchGrid = new ArrayList<Tile>();
                    searchGrid.add(tile);
                    for (int i = 0; i < schemasLevel; i++) {
                        int n = searchGrid.size();
                        for (int j = 0; j < n; j++) {
                            for (Tile tmp : mainPanel.getGamePanel().getGameBoard().getNeighborhood(searchGrid.get(j))) {
                                if (!searchGrid.contains(tmp)) {
                                    searchGrid.add(tmp);
                                }
                            }
                        }
                    }

                    // Identifiy the tiles to test
                    Collection<Tile> tilesToTest = new ArrayList<Tile>();
                    for (Tile toTest : searchGrid) {
                        int nbMines = mainPanel.getGamePanel().getGameBoard().getNumberOfSurroundingMines(toTest);
                        if (toTest != null && toTest.isOpen() && nbMines > 0) {
                            tilesToTest.add(toTest);
                        }
                    }

                    if (tilesToTest.size() > 1) {

                        // Search the schemas
                        List<Tile> unknownTilesInSearchGrid = new ArrayList<Tile>();
                        for (Tile tmp : searchGrid) {
                            if (tmp != null && !tmp.isOpen() && !tmp.isFlagged()) {
                                unknownTilesInSearchGrid.add(tmp);
                            }
                        }
                        if (unknownTilesInSearchGrid.size() == 0) {
                            continue;
                        }
                        Collection<String> schemas = getSchemas(unknownTilesInSearchGrid.size());

                        // Validate the schemas
                        Collection<String> validSchemas = new ArrayList<String>();
                        for (String schema : schemas) {
                            boolean isValid = true;

                            for (Tile toTest : tilesToTest) {

                                int flaggedNeighbors = 0;
                                int unknownNeighbors = 0;
                                int unknownNeighborsInSearchGrid = 0;
                                try {
                                    for (Tile neighbor : mainPanel.getGamePanel().getGameBoard().getNeighborhood(toTest)) {
                                        if (neighbor != null && neighbor.isFlagged()) {
                                            flaggedNeighbors++;
                                        } else if (neighbor != null && !neighbor.isOpen() && !neighbor.isFlagged()) {
                                            unknownNeighbors++;

                                            if (searchGrid.contains(neighbor)) {
                                                unknownNeighborsInSearchGrid++;
                                            }
                                        }

                                    }
                                } catch (TilesShapeUnsupportedException e) {
                                    JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
                                }

                                int nbSurroundingPotentialMines = unknownNeighbors - unknownNeighborsInSearchGrid;
                                if (mainPanel.getGamePanel().getGameBoard().getNumberOfSurroundingMines(toTest) < nbSurroundingPotentialMines) {
                                    nbSurroundingPotentialMines = mainPanel.getGamePanel().getGameBoard().getNumberOfSurroundingMines(toTest);
                                }

                                int nbSurroundingSupposedMines = 0;
                                for (Tile tmp : mainPanel.getGamePanel().getGameBoard().getNeighborhood(toTest)) {
                                    if (unknownTilesInSearchGrid.contains(tmp) && !tmp.isOpen() && !tmp.isFlagged() && schema.charAt(unknownTilesInSearchGrid.indexOf(tmp)) == '1') {
                                        nbSurroundingSupposedMines++;
                                    }
                                }

                                // 1st test
                                if (nbSurroundingSupposedMines > mainPanel.getGamePanel().getGameBoard().getNumberOfSurroundingMines(toTest)) {
                                    isValid = false;
                                }

                                // 2nd test
                                if (nbSurroundingPotentialMines == 0) {
                                    if (nbSurroundingSupposedMines + flaggedNeighbors != mainPanel.getGamePanel().getGameBoard().getNumberOfSurroundingMines(toTest)) {
                                        isValid = false;
                                    }
                                }

                                // 3rd test
                                if (nbSurroundingPotentialMines > 0) {
                                    if (nbSurroundingSupposedMines + nbSurroundingPotentialMines + flaggedNeighbors < mainPanel.getGamePanel().getGameBoard().getNumberOfSurroundingMines(toTest)) {
                                        isValid = false;
                                    }
                                }
                            }

                            if (isValid) {
                                validSchemas.add(schema);
                            }
                        }

                        // Analyzing the remaining valid schemas
                        String and = and(validSchemas);
                        String or = or(validSchemas);

                        for (int i = 0; and != null && i < and.length() && !ret; i++) {
                            if (and.charAt(i) == '1') {
                                ret = true;
                            }
                        }

                        for (int i = 0; or != null && i < or.length() && !ret; i++) {
                            if (or.charAt(i) == '0') {
                                ret = true;
                            }
                        }
                    }
                }
            }
        } catch (TilesShapeUnsupportedException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        }

        return ret;
    }

    /**
     * Initialize the playing robot.
     */
    public void initialize() {
        playing = false;
    }

    /**
     * The method to start the robot playing.
     */
    public void play() {
        if (mainPanel.getGamePanel().isWon() || mainPanel.getGamePanel().isLost()) {
            // The game is yet terminated ...
            return;
        }

        playing = true;

        long start = System.currentTimeMillis();

        if (isCheater()) {
            cheat();
        } else {
            searchCenters();

            boolean actionWasDone = false;

            do {
                actionWasDone = false;

                if (!actionWasDone) {
                    actionWasDone = searchTrivialCases();
                }

                if (!actionWasDone) {
                    actionWasDone = searchSchemas();
                }

                if (!actionWasDone) {
                    actionWasDone = searchConstraintSatisfactionProblem();
                }

                if (!actionWasDone && randomAuthorized) {
                    actionWasDone = searchRandomly();
                }
            } while (actionWasDone && !mainPanel.getGamePanel().isWon() && !mainPanel.getGamePanel().isLost());
        }

        long time = System.currentTimeMillis() - start;

        String key = mainPanel.getGamePanel().getGameBoard().getTilesShape() + Configuration.DOT + mainPanel.getGamePanel().getDifficulty();

        if (played.containsKey(key)) {
            played.put(key, played.get(key) + 1);
        } else {
            played.put(key, 1);
        }

        if (mainPanel.getGamePanel().isWon()) {
            if (won.containsKey(key)) {
                won.put(key, won.get(key) + 1);
            } else {
                won.put(key, 1);
            }

            if (mediumTime.containsKey(key)) {
                mediumTime.put(key, ((mediumTime.get(key) * (won.get(key) - 1)) + time) / won.get(key));
            } else {
                mediumTime.put(key, time);
            }

            if (minimumTime.containsKey(key)) {
                minimumTime.put(key, Math.min(minimumTime.get(key), time));
            } else {
                minimumTime.put(key, time);
            }

            if (maximumTime.containsKey(key)) {
                maximumTime.put(key, Math.max(maximumTime.get(key), time));
            } else {
                maximumTime.put(key, time);
            }
        }

        storeRobotStatistics();
    }

    /**
     * The method to start the robot cheating.
     */
    public void cheat() {
        final int timeToWait = 300;

        searchCenters();

        // Activate the cheat mode
        if (!mainPanel.getCheatPixel().isVisible()) {
            for (int keyCode : KeyListenerForMainFrame.CHEAT_CODE) {
                awtRobot.keyPress(keyCode);
                awtRobot.keyRelease(keyCode);

                try {
                    Thread.sleep(timeToWait);
                } catch (InterruptedException e) {
                    JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // Start to "play"
        int x = mainPanel.getGamePanel().getLocationOnScreen().x + mainPanel.getGamePanel().getBorder().getBorderInsets(mainPanel.getGamePanel()).left;
        int y = mainPanel.getGamePanel().getLocationOnScreen().y + mainPanel.getGamePanel().getBorder().getBorderInsets(mainPanel.getGamePanel()).top;
        for (int l = 0; l < centers.length && !mainPanel.getGamePanel().isWon() && !mainPanel.getGamePanel().isLost(); l++) {
            for (int c = 0; c < centers[l].length && !mainPanel.getGamePanel().isWon() && !mainPanel.getGamePanel().isLost(); c++) {
                Point center = centers[l][c];
                if (center != null) {
                    if (isReallyClicking()) {
                        awtRobot.mouseMove(center.x + x, center.y + y);
                    }

                    try {
                        Thread.sleep(timeToWait);
                    } catch (InterruptedException e) {
                        JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
                    }

                    if (isReallyClicking()) {
                        if (awtRobot.getPixelColor(0, 0).equals(MouseListenerForGamePanel.MINED_COLOR)) {
                            clickTo(l, c, MouseEvent.BUTTON3_MASK);
                        } else if (awtRobot.getPixelColor(0, 0).equals(MouseListenerForGamePanel.NOT_MINED_COLOR)) {
                            clickTo(l, c, MouseEvent.BUTTON1_MASK);
                        }
                    } else {
                        if (mainPanel.getGamePanel().getGameBoard().getTile(l, c).isContainingMine()) {
                            clickTo(l, c, MouseEvent.BUTTON3_MASK);
                        } else {
                            clickTo(l, c, MouseEvent.BUTTON1_MASK);
                        }
                    }
                }
            }
        }

        // Dectivate the cheat mode
        for (int keyCode : KeyListenerForMainFrame.CHEAT_CODE) {
            awtRobot.keyPress(keyCode);
            awtRobot.keyRelease(keyCode);

            try {
                Thread.sleep(timeToWait);
            } catch (InterruptedException e) {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
