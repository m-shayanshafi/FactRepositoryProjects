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

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import jmines.view.components.MainFrame;
import jmines.view.persistence.BoardAccess;
import jmines.view.persistence.Configuration;
import jmines.view.persistence.VideoAccess;
import jmines.view.persistence.BoardAccess.BoardData;
import jmines.view.persistence.VideoAccess.VideoData;

/**
 * The class entry point of JMines applications.
 *
 * @author Zleurtor
 */
public final class Launcher {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = -2722222401045952366L;
    /**
     * The configuration available for all the application.
     */
    private static final Configuration CONFIGURATION;
    /**
     * The main window of the application.
     */
    private static MainFrame frame;

    /**
     * The argument to use with the application in order to edit the user
     * configuration file.
     */
    public static final String OPTION_CONFIGURATION = "-configuration";

    static {
        final Configuration tmpConfiguration = Configuration.getInstance();
        CONFIGURATION = tmpConfiguration;
    }

    //==========================================================================
    // Attributes
    //==========================================================================

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new Launcher instance.
     */
    private Launcher() {
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

    /**
     * The entry point of the JMines application.
     *
     * @param args The application arguments.
     */
    public static void main(final String[] args) {
        String userLookAndFeelClassName = CONFIGURATION.getString(Configuration.KEY_USER_LOOKANDFEEL);

        if (args.length == 1 && args[0].equals(OPTION_CONFIGURATION)) {
            try {
                UIManager.setLookAndFeel(userLookAndFeelClassName);
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            } catch (InstantiationException e) {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            } catch (IllegalAccessException e) {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            } catch (UnsupportedLookAndFeelException e) {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            }

            // The user has passed the configuration argument
            Configuration.main(args);
            return;
        } else {
            // initialize the main frame
            boolean found = false;
            for (LookAndFeel laf : CONFIGURATION.getAvailableLookAndFeels()) {
                if (laf.getClass().getName().equals(userLookAndFeelClassName)) {
                    found = true;
                    frame = MainFrame.changeLookAndFeel(laf, frame);
                }
            }

            if (!found) {
                frame = MainFrame.changeLookAndFeel(UIManager.getLookAndFeel(), frame);
            }

            File file = null;
            if (args.length == 1) {
                // The user has passed an argument that is not the configuration
                file = new File(args[0]);

                if (args.length > 1 || (file != null && !file.exists())) {
                    JOptionPane.showMessageDialog(frame.getMainPanel(), Configuration.getInstance().getConfigurableText(Configuration.KEY_ERROR_USAGE, new String[] {OPTION_CONFIGURATION}), Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }

                if (Configuration.getVideoFileFilter().accept(file) && !Configuration.getDirectoryFileFilter().accept(file)) {
                    try {
                        VideoData data = VideoAccess.loadVideo(file);
                        frame.getMainPanel().playVideo(data.getDifficulty(), data.getShape(), data.getWidth(), data.getHeight(), data.getGrid(), data.getMines(), data.getActions());
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
                    }
                } else if (Configuration.getBoardFileFilter().accept(file) && !Configuration.getDirectoryFileFilter().accept(file)) {
                    try {
                        BoardData data = BoardAccess.loadBoard(file);

                        frame.getMainPanel().playGrid(data.getDifficulty(), data.getShape(), data.getWidth(), data.getHeight(), data.getGrid(), data.getMines());
                        frame.getMainPanel().repaint();
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
                    } catch (IllegalArgumentException e) {
                        JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
                    } catch (SecurityException e) {
                        JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

}
