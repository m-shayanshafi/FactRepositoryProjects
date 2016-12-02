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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import jmines.control.listeners.KeyListenerForMainFrame;
import jmines.control.listeners.WindowListenerForMainFrame;
import jmines.model.events.GameBoardEvent;
import jmines.model.events.GameBoardListener;
import jmines.view.persistence.Configuration;

/**
 * The class entry point of JMines applications.
 *
 * @author Zleurtor
 */
public final class MainFrame extends JFrame implements GameBoardListener {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = -2722222401045952366L;
    /**
     * The menu bar of the application.
     */
    private MenuBar menuBar;
    /**
     * The JMines main panel.
     */
    private MainPanel mainPanel;

    //==========================================================================
    // Attributes
    //==========================================================================

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new Launcher instance.
     *
     * @param title The title of the JMines main frame.
     */
    public MainFrame(final String title) {
        super(title);
    }

    //==========================================================================
    // Getters
    //==========================================================================
    /**
     * The JMines main panel.
     *
     * @return The JMines main panel.
     */
    public MainPanel getMainPanel() {
        return mainPanel;
    }

    //==========================================================================
    // Setters
    //==========================================================================

    //==========================================================================
    // Inherited methods
    //==========================================================================
    /**
     * The called method when the game board of the main panel is initialized.
     *
     * @param evt The event object relating the event that occurred.
     * @see jmines.model.events.GameBoardListener#initialized(jmines.model.events.GameBoardEvent)
     */
    public void initialized(final GameBoardEvent evt) {
        Dimension oldSize = getSize();
        pack();
        Dimension newSize = getSize();
        Point oldLocation = getLocation();
        Point newLocation = new Point(
                oldLocation.x + (oldSize.width - newSize.width) / 2,
                oldLocation.y + (oldSize.height - newSize.height) / 2);
        setLocation(newLocation);

        if (menuBar != null) {
            menuBar.disableSaveVideo();
        }
    }

    /**
     * The called method when the user has lost.
     *
     * @param evt The event object relating the event that occurred.
     * @see jmines.model.events.GameBoardListener#defeat(jmines.model.events.GameBoardEvent)
     */
    public void defeat(final GameBoardEvent evt) {
    }

    /**
     * The called method when the user has won.
     *
     * @param evt The event object relating the event that occurred.
     * @see jmines.model.events.GameBoardListener#victory(jmines.model.events.GameBoardEvent)
     */
    public void victory(final GameBoardEvent evt) {
    }

    //==========================================================================
    // Static methods
    //==========================================================================
    /**
     * Change the look and feel of the JMines window.
     *
     * @param laf The look and feel to set.
     * @param mainFrame The MainFrame for which the look and feel has to be
     *                  changed.
     * @return A MainFrame with the new Look and Feel.
     */
    public static MainFrame changeLookAndFeel(final LookAndFeel laf, final MainFrame mainFrame) {
        if (mainFrame != null) {
            mainFrame.dispose();
        }

        try {
            UIManager.setLookAndFeel(laf);
        } catch (UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(mainFrame, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        }

        MainFrame ret = new MainFrame(Configuration.getInstance().getText(Configuration.KEY_TITLE_JMINES));
        ret.initialize();
        return ret;
    }

    //==========================================================================
    // Methods
    //==========================================================================
    /**
     * Initialize the JMines main frame.
     */
    public void initialize() {
        mainPanel = new MainPanel(this);
        mainPanel.getGamePanel().getGameBoard().addGameBoardListener(this);
        mainPanel.getGamePanel().getGameBoard().initialize();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowListenerForMainFrame(this));

        addKeyListener(new KeyListenerForMainFrame(mainPanel));

        setIconImage(Configuration.getInstance().getImage(Configuration.KEY_ICON_FILENAME));
        menuBar = new MenuBar(this);
        setJMenuBar(menuBar);
        setLayout(new BorderLayout());
        add(mainPanel);

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        mainPanel.getTopPanel().getSmileyButton().getAction().actionPerformed(null);
    }

}
