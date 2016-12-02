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
package jmines.control.actions.other;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import jmines.control.actions.JMinesAction;
import jmines.view.components.MainFrame;
import jmines.view.persistence.BoardAccess;
import jmines.view.persistence.Configuration;
import jmines.view.persistence.BoardAccess.BoardData;

/**
 * The class representing the action used when the user click on the load board
 * menu item.
 *
 * @author Zleurtor
 */
public class LoadBoard extends JMinesAction {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = 5073524774158637185L;

    //==========================================================================
    // Attributes
    //==========================================================================

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new LoveBoard action.
     *
     * @param name The name of the menu item.
     * @param mainFrame The main frame of the application.
     */
    public LoadBoard(final String name, final MainFrame mainFrame) {
        super(name, mainFrame);

        setStatusText(Configuration.getInstance().getText(Configuration.KEY_STATUSTEXT_OTHER_LOADBOARD));
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
    /**
     * The Method used when the user click on the menu item.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public final void actionPerformed(final ActionEvent evt) {
        final String suffix = Configuration.getInstance().getString(Configuration.KEY_FILE_BOARD_SUFFIX);

        // Create the dialog used to ask the user the name of the file to write to.
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setDialogTitle(Configuration.getInstance().getText(Configuration.KEY_TITLE_LOADVIDEO));

        for (FileFilter filter : fileChooser.getChoosableFileFilters()) {
            fileChooser.removeChoosableFileFilter(filter);
        }

        fileChooser.setFileFilter(Configuration.getBoardFileFilter());

        // Ask the user the filename
        fileChooser.showOpenDialog(getMainPanel());
        File file = fileChooser.getSelectedFile();
        if (file == null) {
            super.emptyStatusBar();
            return;
        } else if (!file.getName().endsWith(suffix)) {
            file = new File(file.getAbsolutePath() + suffix);
        }

        try {
            BoardData data = BoardAccess.loadBoard(file);
            if (data != null) {
                getMainPanel().playGrid(data.getDifficulty(), data.getShape(), data.getWidth(), data.getHeight(), data.getGrid(), data.getMines());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        }

        super.emptyStatusBar();
    }

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================

}
