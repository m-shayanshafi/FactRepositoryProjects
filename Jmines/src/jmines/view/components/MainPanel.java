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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.MissingResourceException;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicBorders;

import jmines.control.actions.JMinesAction;
import jmines.control.listeners.ComponentListenerForMainPanel;
import jmines.control.listeners.MouseListenerForGamePanel.Action;
import jmines.model.GameBoard;
import jmines.model.Tile;
import jmines.model.TilesShapeUnsupportedException;
import jmines.view.AudioPlayer;
import jmines.view.Robot;
import jmines.view.Timer;
import jmines.view.dialogs.BestTimesDialog;
import jmines.view.dialogs.NameDialog;
import jmines.view.persistence.BoardAccess;
import jmines.view.persistence.Configuration;
import jmines.view.persistence.VideoAccess;

/**
 * This is the JMines main panel, it contains a top panel with the two LCDs
 * (for the number of flag and the time) the game panel and a status bar at the
 * bottom.
 *
 * @author Zleurtor
 */
public class MainPanel extends JPanel {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = 8845995089023282734L;
    /**
     * The top, bottom, left and right margin.
     */
    private static final int MARGIN = 6;

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The top panel containing the two LCDs and smiley button.
     */
    private final TopPanel topPanel;
    /**
     * The JMines game panel.
     */
    private final GamePanel gamePanel;
    /**
     * The JMines cheat code pixel.
     */
    private final CheatPixel cheatPixel;
    /**
     * The status bar (at the bottom of this panel).
     */
    private final JLabel statusBar;
    /**
     * The timer for the user game.
     */
    private Timer timer;
    /**
     * The object used to play the sounds (if enabled).
     */
    private AudioPlayer audioPlayer;
    /**
     * Tell whether this panel is paint using colors or gray scales.
     */
    private boolean colored;
    /**
     * The background color (a color or a gray scale).
     */
    private Color background;
    /**
     * The look and feel dependant background color (a color or a gray scale).
     */
    private final Color lookAndFeelDependentBackground;
    /**
     * Tell whether or not the sounds are enabled.
     */
    private boolean soundEnabled;
    /**
     * Tell whether or not the video saving is enabled.
     */
    private boolean saveVideoEnabled;
    /**
     * Tell whether or not we are playing a video.
     */
    private boolean playingVideo;
    /**
     * Tell whether or not the main panel is shown.
     */
    private boolean shown = false;
    /**
     * Tell whether or not the main panel constructor call has ended.
     */
    private boolean initialized;
    /**
     * Tell whether or not the current board has been loaded.
     */
    private boolean loaded = false;
    /**
     * The robot used to solve the game.
     */
    private final Robot robot;

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new main panel using the given game configuration.
     *
     * @param mainFrame The main frame of the application.
     */
    public MainPanel(final MainFrame mainFrame) {
        super(new GridBagLayout(), false);

        setBorder(BasicBorders.getInternalFrameBorder());

        topPanel = new TopPanel(this, mainFrame);
        gamePanel = new GamePanel(this);
        cheatPixel = new CheatPixel();
        statusBar = new JLabel(" ");
        statusBar.setBorder(BasicBorders.getTextFieldBorder());

        add(topPanel,
                new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(MARGIN, MARGIN, MARGIN / 2, MARGIN), 0, 0));
        add(gamePanel,
                new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(MARGIN / 2, MARGIN, MARGIN, MARGIN), 0, 0));
        add(statusBar,
                new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, MARGIN, MARGIN, MARGIN), 0, 0));

        timer = new Timer(this);
        audioPlayer = new AudioPlayer();

        background = getBackground();
        lookAndFeelDependentBackground = getBackground();

        addComponentListener(new ComponentListenerForMainPanel(this));
        initialized = true;

        robot = new Robot(this);
    }

    //==========================================================================
    // Getters
    //==========================================================================
    /**
     * Returns the top panel containing the two LCDs and smiley button.
     *
     * @return The top panel.
     */
    public final TopPanel getTopPanel() {
        return topPanel;
    }

    /**
     * Returns the JMines game panel.
     *
     * @return  The JMines game panel.
     */
    public final GamePanel getGamePanel() {
        return gamePanel;
    }

    /**
     * Returns the JMines cheat code pixel.
     *
     * @return The JMines cheat code pixel.
     */
    public final CheatPixel getCheatPixel() {
        return cheatPixel;
    }

    /**
     * Returns the status bar (at the bottom of this panel).
     *
     * @return The status bar (at the bottom of this panel).
     */
    public final JLabel getStatusBar() {
        return statusBar;
    }

    /**
     * Returns the timer for the user game.
     *
     * @return The timer for the user game.
     */
    public final Timer getTimer() {
        return timer;
    }

    /**
     * Returns the object used to play the sounds (if enabled).
     *
     * @return The object used to play the sounds (if enabled).
     */
    public final AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    /**
     * Returns a boolean telling whether this panel is paint using colors or
     * gray scales.
     *
     * @return True if the panel is painted using colors, false if it is done
     *         using gray scales.
     */
    public final boolean isColored() {
        return colored;
    }

    /**
     * Returns the look and feel dependant background color (a color or a gray
     * scale).
     *
     * @return The look and feel dependant background color (a color or a gray
     *         scale).
     */
    public final Color getLookAndFeelDependentBackground() {
        return lookAndFeelDependentBackground;
    }

    /**
     * Returns a boolean telling whether or not the sounds are enabled.
     *
     * @return True if the sounds are enabled, false otherwise.
     */
    public final boolean isSoundEnabled() {
        return soundEnabled;
    }

    /**
     * Returns a boolean telling whther or not a video is playing.
     *
     * @return True if a video is playing, false otherwise.
     */
    public final boolean isPlayingVideo() {
        return playingVideo;
    }

    /**
     * Returns a boolean telling whether or not the video saving is enabled.
     *
     * @return True if the video saving is enabled, false otherwise.
     */
    public final boolean isSaveVideoEnabled() {
        return saveVideoEnabled;
    }

    /**
     * Returns a boolean telling whether or not the main panel is shown.
     *
     * @return True if the panel is shown, false otherwise.
     */
    public final boolean isShown() {
        return shown;
    }

    /**
     * Returns a boolean telling whether or not the current board has been
     * loaded.
     *
     * @return True if the panel has been loaded, false otherwise.
     */
    public final boolean isLoaded() {
        return loaded;
    }

    /**
     * Returns the robot used to solve the game.
     *
     * @return The robot used to solve the game.
     */
    public final Robot getRobot() {
        return robot;
    }

    //==========================================================================
    // Setters
    //==========================================================================
    /**
     * Allows to set a new object value to the timer for the user game.
     *
     * @param newTimer The new object value for the timer for the user game.
     */
    public final void setTimer(final Timer newTimer) {
        this.timer = newTimer;
    }

    /**
     * Allows to set a new value to the colored attribute.
     *
     * @param newColored If true, this panel will be painted using colors,
     *                   using gray scales if false.
     */
    public final void setColored(final boolean newColored) {
        this.colored = newColored;

        if (!colored) {
            int l = JMinesAction.getLuminance(background);
            Color coloredBackground = getBackground();
            setBackground(new Color(l, l, l));
            background = coloredBackground;
        } else {
            setBackground(background);
        }

        getTopPanel().setColored(newColored);
        getGamePanel().setColored(newColored);

        repaint();
    }

    /**
     * Allows to set a new value to the sound enabled attribute.
     *
     * @param newSoundEnabled If true, the sounds will be enabled, disabled if
     *                        false.
     */
    public final void setSoundEnabled(final boolean newSoundEnabled) {
        this.soundEnabled = newSoundEnabled;
    }

    /**
     * Allows to set a new value to the video saving enabled attribute.
     *
     * @param newSaveVideoEnabled If true, the video saving will be enabled,
     *                            disabled if false.
     */
    public final void setSaveVideoEnabled(final boolean newSaveVideoEnabled) {
        this.saveVideoEnabled = newSaveVideoEnabled;
    }

    /**
     * Allows to set a new value to the playing video attribute.
     *
     * @param newPlayingVideo If true a video is playing, no if false.
     */
    public final void setPlayingVideo(final boolean newPlayingVideo) {
        this.playingVideo = newPlayingVideo;
    }

    /**
     * Allows to set a new value to the shown attribute.
     *
     * @param newShown If true, the panel is shown, hidden if false.
     */
    public final void setShown(final boolean newShown) {
        this.shown = newShown;
    }

    /**
     * Allows to set a new value to the loaded attribute.
     *
     * @param newLoaded If true, the current board has been loaded.
     */
    public final void setLoaded(final boolean newLoaded) {
        this.loaded = newLoaded;
    }

    //==========================================================================
    // Inherited methods
    //==========================================================================
    /**
     * Change the background color of this panel and all its sub panels.
     *
     * @param bg The new color to use for the panel background.
     * @see javax.swing.JComponent#setBackground(java.awt.Color)
     */
    @Override
    public final void setBackground(final Color bg) {
        if (!initialized) {
            super.setBackground(bg);
        } else {
            if (isColored()) {
                super.setBackground(bg);
            } else {
                int l = JMinesAction.getLuminance(bg);
                super.setBackground(new Color(l, l, l));
            }
            background = bg;

            if (getTopPanel() != null && getGamePanel() != null) {
                getTopPanel().setBackground(bg);
                getGamePanel().setBackground(bg);
            }
        }
    }

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
    /**
     * Save the JMines Video file.
     */
    public final void saveVideo() {
        final MainPanel mainPanel = this;
        final String suffix = Configuration.getInstance().getString(Configuration.KEY_FILE_VIDEO_SUFFIX);

        topPanel.getSmileyButton().setEnabled(false);

        new Thread() {
            /**
             * The method used to save all the actions.
             *
             * @see java.lang.Thread#run()
             */
            @Override
            public void run() {
                // Create the dialog used to ask the user the name of the file to write to.
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setMultiSelectionEnabled(false);
                fileChooser.setDialogTitle(Configuration.getInstance().getText(Configuration.KEY_TITLE_SAVEVIDEO));

                for (FileFilter filter : fileChooser.getChoosableFileFilters()) {
                    fileChooser.removeChoosableFileFilter(filter);
                }

                fileChooser.setFileFilter(Configuration.getVideoFileFilter());

                // Ask the user the filename
                fileChooser.showSaveDialog(mainPanel);
                File file = fileChooser.getSelectedFile();
                if (file != null) {
                    if (!file.getName().endsWith(suffix)) {
                        file = new File(file.getAbsolutePath() + suffix);
                    }
                    try {
                        VideoAccess.saveVideo(getGamePanel().getMouseListener().getStored(), mainPanel, file);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

            }
        } .start();

        topPanel.getSmileyButton().setEnabled(true);
    }

    /**
     * Returns the thread that will play the video actions.
     *
     * @param actions The list of actions to play.
     * @return The thread that will play the actions.
     */
    private Thread getPlayVideoThread(final List<Action> actions) {
        // Initialize times and thread
        long firstDate = 0;
        if (actions.size() > 0) {
            firstDate = actions.get(0).getDate();
        }
        final long startDate = System.currentTimeMillis() - firstDate;

        Thread playThread = new Thread() {
            /**
             * The method launched to play all the actions.
             *
             * @see java.lang.Thread#run()
             */
            @Override
            public void run() {
                setPlayingVideo(true);

                if (!isPlayingVideo()) {
                    return;
                }

                Tile lastTile = null;
                boolean firstTime = true;
                for (Action action : actions) {
                    if (!isPlayingVideo()) {
                        return;
                    }

                    try {
                        long toSleep = Math.max(0, (startDate + action.getDate()) - System.currentTimeMillis());
                        if (toSleep > 0) {
                            Thread.sleep(toSleep);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
                        System.exit(-1);
                    }

                    if (!isPlayingVideo()) {
                        return;
                    }

                    switch (action.getId()) {
                    case Action.ID_OPEN:
                        getGamePanel().getGameBoard().getTile(action.getLine(), action.getColumn()).setOpen(true);
                        break;
                    case Action.ID_FLAG:
                        getGamePanel().getGameBoard().getTile(action.getLine(), action.getColumn()).setFlagged(true);
                        getTopPanel().getFlagsPanel().setNumber(getTopPanel().getFlagsPanel().getNumber() - 1);
                        break;
                    case Action.ID_MARK:
                        getGamePanel().getGameBoard().getTile(action.getLine(), action.getColumn()).setMarked(true);
                        break;
                    case Action.ID_UNFLAG:
                        getGamePanel().getGameBoard().getTile(action.getLine(), action.getColumn()).setFlagged(false);
                        getTopPanel().getFlagsPanel().setNumber(getTopPanel().getFlagsPanel().getNumber() + 1);
                        break;
                    case Action.ID_UNMARK:
                        getGamePanel().getGameBoard().getTile(action.getLine(), action.getColumn()).setMarked(false);
                        break;
                    default:
                        break;
                    }

                    if (firstTime) {
                        getTimer().start();
                        firstTime = false;
                    }

                    if (!isPlayingVideo()) {
                        return;
                    }

                    getGamePanel().repaint();
                    lastTile = getGamePanel().getGameBoard().getTile(action.getLine(), action.getColumn());
                }

                if (!isPlayingVideo()) {
                    return;
                }

                if (lastTile != null) {
                    if (!isPlayingVideo()) {
                        return;
                    }

                    lastTile.setOpen(false);
                    getGamePanel().getGameBoard().setFirstOpen(false);
                    getGamePanel().getGameBoard().open(lastTile);

                    if (!isPlayingVideo()) {
                        return;
                    }
                }

                if (!isPlayingVideo()) {
                    return;
                }

                getTimer().cancel();
            }
        };

        return playThread;
    }

    /**
     * The method used to check if the realized time to win the game is better
     * than the previous ones, if yes display a dialog to ask the user name,
     * then save this time.
     */
    public final void manageBestTimes() {
        if (isPlayingVideo() || isLoaded()) {
            return;
        }

        // Searching the tiles shape
        String shape = "";
        switch (getGamePanel().getGameBoard().getTilesShape()) {
        case GameBoard.SHAPE_TRIANGULAR:
            shape = Configuration.SHAPE_TRIANGULAR;
            break;
        case GameBoard.SHAPE_TRIANGULAR_14:
            shape = Configuration.SHAPE_TRIANGULAR_14;
            break;
        case GameBoard.SHAPE_SQUARE:
            shape = Configuration.SHAPE_SQUARE;
            break;
        case GameBoard.SHAPE_PENTAGONAL:
            shape = Configuration.SHAPE_PENTAGONAL;
            break;
        case GameBoard.SHAPE_HEXAGONAL:
            shape = Configuration.SHAPE_HEXAGONAL;
            break;
        case GameBoard.SHAPE_OCTOSQUARE:
            shape = Configuration.SHAPE_OCTOSQUARE;
            break;
        case GameBoard.SHAPE_PARQUET:
            shape = Configuration.SHAPE_PARQUET;
            break;
        default:
            break;
        }

        // Searching the difficulty
        Configuration configuration = Configuration.getInstance();
        if (!getGamePanel().getDifficulty().equals(Configuration.DIFFICULTY_CUSTOM)) {
            try {
                String key = shape + Configuration.DOT + getGamePanel().getDifficulty() + Configuration.DOT + Configuration.SUFFIX_BEST;
                String tmp = null;
                try {
                    tmp = configuration.getString(key);
                    if (tmp == null) {
                        throw new MissingResourceException(null, null, null);
                    }
                } catch (MissingResourceException e) {
                    tmp = configuration.getText(Configuration.KEY_BEST_DEFAULT_NAME) + Configuration.COMA + configuration.getString(Configuration.KEY_BEST_DEFAULT_TIME);
                }

                String name = tmp.substring(0, tmp.lastIndexOf(Configuration.COMA));
                int time = Integer.parseInt(tmp.substring(tmp.lastIndexOf(Configuration.COMA) + 1));

                if (time > getTopPanel().getTimePanel().getNumber()) {
                    NameDialog dialog = new NameDialog(this);
                    if (!robot.isPlaying()) {
                        name = dialog.getValue(name);
                    } else {
                        name = robot.getName();
                    }
                    time = getTopPanel().getTimePanel().getNumber();
                    configuration.putRealTimeconfiguration(key, name + Configuration.COMA + time);
                    new BestTimesDialog(this).setVisible(true);
                }
            } catch (MissingResourceException e) {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Play a JMines Video using a given list of actions.
     *
     * @param difficulty A byte defining the difficulty used in the video has been saved (beginner, intermediate, expert or custom).
     * @param shape The tiles shape used in the video (triangular, square, hexagonal, etc.).
     * @param width The number of columns in the video grid.
     * @param height The number of lines in the video grid.
     * @param grid A bytes array defining which tiles contain a mine (a tile contains a mine if the byte value of the grid array is 1).
     * @param mines The number of mines contained in the video grid.
     */
    public final void playGrid(final byte difficulty, final byte shape, final byte width, final byte height, final byte[][] grid, final int mines) {
        manageSmiley();

        // Change the grid
        if (difficulty == Action.DIFFICULTY_BEGINNER) {
            getGamePanel().setDifficulty(Configuration.DIFFICULTY_BEGINNER);
        } else if (difficulty == Action.DIFFICULTY_INTERMEDIATE) {
            getGamePanel().setDifficulty(Configuration.DIFFICULTY_INTERMEDIATE);
        } else if (difficulty == Action.DIFFICULTY_EXPERT) {
            getGamePanel().setDifficulty(Configuration.DIFFICULTY_EXPERT);
        } else if (difficulty == Action.DIFFICULTY_CUSTOM) {
            getGamePanel().setDifficulty(Configuration.DIFFICULTY_CUSTOM);
        }
        getGamePanel().getGameBoard().setWidth(width);
        getGamePanel().getGameBoard().setHeight(height);
        try {
            getGamePanel().getGameBoard().setTilesShape(shape);
        } catch (TilesShapeUnsupportedException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            return;
        }
        getGamePanel().getGameBoard().setNumberOfMines(mines, false);

        if (difficulty == Action.DIFFICULTY_BEGINNER) {
            Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_USER_DIFFICULTY, Configuration.DIFFICULTY_BEGINNER);
        } else if (difficulty == Action.DIFFICULTY_INTERMEDIATE) {
            Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_USER_DIFFICULTY, Configuration.DIFFICULTY_INTERMEDIATE);
        } else if (difficulty == Action.DIFFICULTY_EXPERT) {
            Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_USER_DIFFICULTY, Configuration.DIFFICULTY_EXPERT);
        } else if (difficulty == Action.DIFFICULTY_CUSTOM) {
            Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_USER_DIFFICULTY, Configuration.DIFFICULTY_CUSTOM + Configuration.COMA + width + Configuration.COMA + height + Configuration.COMA + mines);
        }

        if (shape == GameBoard.SHAPE_TRIANGULAR) {
            Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_USER_SHAPE, Configuration.SHAPE_TRIANGULAR);
        } else if (shape == GameBoard.SHAPE_SQUARE) {
            Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_USER_SHAPE, Configuration.SHAPE_SQUARE);
        } else if (shape == GameBoard.SHAPE_HEXAGONAL) {
            Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_USER_SHAPE, Configuration.SHAPE_HEXAGONAL);
        } else if (shape == GameBoard.SHAPE_OCTOSQUARE) {
            Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_USER_SHAPE, Configuration.SHAPE_OCTOSQUARE);
        }

        getGamePanel().getGameBoard().initialize();
        Tile defeatTile = null;
        for (int l = 0; l < grid.length; l++) {
            for (int c = 0; c < grid[l].length; c++) {
                if (grid[l][c] != BoardAccess.MASK_NULL) {
                    if ((grid[l][c] & BoardAccess.MASK_MINED) != 0) {
                        getGamePanel().getGameBoard().getTile(l, c).setContainingMine(true);
                    } else {
                        getGamePanel().getGameBoard().getTile(l, c).setContainingMine(false);
                    }

                    if ((grid[l][c] & BoardAccess.MASK_OPENED) != 0) {
                        getGamePanel().getGameBoard().getTile(l, c).setOpen(true);
                        getGamePanel().getGameBoard().setFirstOpen(false);
                        if (getGamePanel().getGameBoard().getTile(l, c).isContainingMine()) {
                            defeatTile = getGamePanel().getGameBoard().getTile(l, c);
                        }
                    } else {
                        getGamePanel().getGameBoard().getTile(l, c).setOpen(false);
                    }

                    if ((grid[l][c] & BoardAccess.MASK_FLAGGED) != 0) {
                        getGamePanel().getGameBoard().getTile(l, c).setFlagged(true);
                    } else {
                        getGamePanel().getGameBoard().getTile(l, c).setFlagged(false);
                    }

                    if ((grid[l][c] & BoardAccess.MASK_MARKED) != 0) {
                        getGamePanel().getGameBoard().getTile(l, c).setMarked(true);
                    } else {
                        getGamePanel().getGameBoard().getTile(l, c).setMarked(false);
                    }
                }
            }
        }

        loaded = true;
        getGamePanel().getGameBoard().checkGameEnd(defeatTile);
        getGamePanel().paintBackground(null);
    }

    /**
     * Play a JMines Video using a given list of actions.
     *
     * @param difficulty A byte defining the difficulty used in the video has been saved (beginner, intermediate, expert or custom).
     * @param shape The tiles shape used in the video (triangular, square, hexagonal, etc.).
     * @param width The number of columns in the video grid.
     * @param height The number of lines in the video grid.
     * @param grid A bytes array defining which tiles contain a mine (a tile contains a mine if the byte value of the grid array is 1).
     * @param mines The number of mines contained in the video grid.
     * @param actions The List of game actions to play.
     */
    public final void playVideo(final byte difficulty, final byte shape, final byte width, final byte height, final byte[][] grid, final int mines, final List<Action> actions) {
        manageSmiley();

        // Change the grid
        if (difficulty == Action.DIFFICULTY_BEGINNER) {
            getGamePanel().setDifficulty(Configuration.DIFFICULTY_BEGINNER);
        } else if (difficulty == Action.DIFFICULTY_INTERMEDIATE) {
            getGamePanel().setDifficulty(Configuration.DIFFICULTY_INTERMEDIATE);
        } else if (difficulty == Action.DIFFICULTY_EXPERT) {
            getGamePanel().setDifficulty(Configuration.DIFFICULTY_EXPERT);
        } else if (difficulty == Action.DIFFICULTY_CUSTOM) {
            getGamePanel().setDifficulty(Configuration.DIFFICULTY_CUSTOM);
        }
        getGamePanel().getGameBoard().setWidth(width);
        getGamePanel().getGameBoard().setHeight(height);
        try {
            getGamePanel().getGameBoard().setTilesShape(shape);
        } catch (TilesShapeUnsupportedException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            return;
        }
        getGamePanel().getGameBoard().setNumberOfMines(mines, false);

        if (difficulty == Action.DIFFICULTY_BEGINNER) {
            Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_USER_DIFFICULTY, Configuration.DIFFICULTY_BEGINNER);
        } else if (difficulty == Action.DIFFICULTY_INTERMEDIATE) {
            Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_USER_DIFFICULTY, Configuration.DIFFICULTY_INTERMEDIATE);
        } else if (difficulty == Action.DIFFICULTY_EXPERT) {
            Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_USER_DIFFICULTY, Configuration.DIFFICULTY_EXPERT);
        } else if (difficulty == Action.DIFFICULTY_CUSTOM) {
            Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_USER_DIFFICULTY, Configuration.DIFFICULTY_CUSTOM + Configuration.COMA + width + Configuration.COMA + height + Configuration.COMA + mines);
        }

        if (shape == GameBoard.SHAPE_TRIANGULAR) {
            Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_USER_SHAPE, Configuration.SHAPE_TRIANGULAR);
        } else if (shape == GameBoard.SHAPE_SQUARE) {
            Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_USER_SHAPE, Configuration.SHAPE_SQUARE);
        } else if (shape == GameBoard.SHAPE_HEXAGONAL) {
            Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_USER_SHAPE, Configuration.SHAPE_HEXAGONAL);
        } else if (shape == GameBoard.SHAPE_OCTOSQUARE) {
            Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_USER_SHAPE, Configuration.SHAPE_OCTOSQUARE);
        }

        getGamePanel().getGameBoard().initialize();
        for (int l = 0; l < grid.length; l++) {
            for (int c = 0; c < grid[l].length; c++) {
                if (getGamePanel().getGameBoard().getTile(l, c) != null) {
                    if (grid[l][c] == 1) {
                        getGamePanel().getGameBoard().getTile(l, c).setContainingMine(true);
                    } else {
                        getGamePanel().getGameBoard().getTile(l, c).setContainingMine(false);
                    }
                }
            }
        }

        getGamePanel().paintBackground(null);

        getPlayVideoThread(actions).start();
    }

    /**
     * Manage and set the correct smiley to the center button of the top panel.
     */
    public final void manageSmiley() {
        new Thread() {

            /**
             * The method used to check and change the smiley.
             *
             * @see java.lang.Thread#run().
             */
            @Override
            public void run() {
                if (getGamePanel().isLost()) {
                    topPanel.setLooseIcon();
                } else if (getGamePanel().isWon()) {
                    topPanel.setWinIcon();
                } else if (gamePanel.getMouseListener().isLeftButtonPressed()) {
                    topPanel.setClickIcon();
                } else if (robot.isHelping()) {
                    statusBar.setText(Configuration.getInstance().getText(Configuration.KEY_STATUSTEXT_HELPING_ANALYSING));
                    if (robot.containsTrivialCases()) {
                        topPanel.setTrivialIcon();
                        statusBar.setText(Configuration.getInstance().getText(Configuration.KEY_STATUSTEXT_HELPING_TRIVIAL));
                    } else if (robot.containsSchemas()) {
                        topPanel.setSchemaIcon();
                        statusBar.setText(Configuration.getInstance().getText(Configuration.KEY_STATUSTEXT_HELPING_SCHEMA));
                    } else {
                        topPanel.setRandomIcon();
                        statusBar.setText(Configuration.getInstance().getText(Configuration.KEY_STATUSTEXT_HELPING_RANDOM));
                    }
                } else {
                    topPanel.setPlayIcon();
                }

                topPanel.getSmileyButton().repaint();
            }
        } .start();
    }
}
