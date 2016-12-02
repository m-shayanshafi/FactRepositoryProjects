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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicBorders;

import jmines.control.actions.JMinesAction;
import jmines.control.actions.game.New;
import jmines.control.listeners.MouseListenerForSmileyButton;
import jmines.view.persistence.Configuration;

/**
 * The top panel is the panel containing the time and flags LCD panels and the
 * start button.
 *
 * @author Zleurtor
 */
public class TopPanel extends JPanel {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = -751806333997208221L;
    /**
     * The margin at the top, bottom, left and right of this panel.
     */
    private static final int MARGIN = 4;
    /**
     * The length (in number of characters) of the LCD panels.
     */
    private static final int LCDLENGTH = 3;
    /**
     * An array of images containing all the smileys displayable in the start
     * button.
     */
    private static final Image[] ORIGINAL_SMILEYS;
    /**
     * The index of the "play" smiley in the ORIGINAL_SMILEYS array.
     */
    private static final int INDEX_PLAY = Configuration.getInstance().getInt(Configuration.KEY_SMILEYS_PLAY);
    /**
     * The index of the "loose" smiley in the ORIGINAL_SMILEYS array.
     */
    private static final int INDEX_LOOSE = Configuration.getInstance().getInt(Configuration.KEY_SMILEYS_LOOSE);
    /**
     * The index of the "click" smiley in the ORIGINAL_SMILEYS array.
     */
    private static final int INDEX_CLICK = Configuration.getInstance().getInt(Configuration.KEY_SMILEYS_CLICK);
    /**
     * The index of the "win" smiley in the ORIGINAL_SMILEYS array.
     */
    private static final int INDEX_WIN = Configuration.getInstance().getInt(Configuration.KEY_SMILEYS_WIN);
    /**
     * The index of the "win" smiley in the ORIGINAL_SMILEYS array.
     */
    private static final int INDEX_TRIVIAL = Configuration.getInstance().getInt(Configuration.KEY_SMILEYS_TRIVIAL);
    /**
     * The index of the "win" smiley in the ORIGINAL_SMILEYS array.
     */
    private static final int INDEX_SCHEMA = Configuration.getInstance().getInt(Configuration.KEY_SMILEYS_SCHEMA);
    /**
     * The index of the "win" smiley in the ORIGINAL_SMILEYS array.
     */
    private static final int INDEX_RANDOM = Configuration.getInstance().getInt(Configuration.KEY_SMILEYS_RANDOM);
    /**
     * The currently used smileys array.
     */
    private static Image[] smileys;

    static {
        final int seven = 7;

        // indexes are INDEX_PLAY, INDEX_LOOSE, INDEX_CLICK and INDEX_WIN
        Image[] tmp = new Image[seven];
        tmp[INDEX_PLAY] = Configuration.getInstance().getImage(Configuration.PREFIX_IMG_SMILEY, Configuration.STATE_SMILEY_PLAY);
        tmp[INDEX_LOOSE] = Configuration.getInstance().getImage(Configuration.PREFIX_IMG_SMILEY, Configuration.STATE_SMILEY_LOOSE);
        tmp[INDEX_CLICK] = Configuration.getInstance().getImage(Configuration.PREFIX_IMG_SMILEY, Configuration.STATE_SMILEY_CLICK);
        tmp[INDEX_WIN] = Configuration.getInstance().getImage(Configuration.PREFIX_IMG_SMILEY, Configuration.STATE_SMILEY_WIN);
        tmp[INDEX_TRIVIAL] = Configuration.getInstance().getImage(Configuration.PREFIX_IMG_SMILEY, Configuration.STATE_SMILEY_TRIVIAL);
        tmp[INDEX_SCHEMA] = Configuration.getInstance().getImage(Configuration.PREFIX_IMG_SMILEY, Configuration.STATE_SMILEY_SCHEMA);
        tmp[INDEX_RANDOM] = Configuration.getInstance().getImage(Configuration.PREFIX_IMG_SMILEY, Configuration.STATE_SMILEY_RANDOM);
        smileys = tmp;
        ORIGINAL_SMILEYS = tmp;
    }

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The flags counter LCD panel.
     */
    private final LCDPanel flagsPanel;
    /**
     * The time counter LCD panel.
     */
    private final LCDPanel timePanel;
    /**
     * The start button.
     */
    private final JButton smileyButton;
    /**
     * The background color of this panel.
     */
    private Color background;
    /**
     * The start button background color.
     */
    private Color buttonBackground;
    /**
     * The index of the currently displayed smiley.
     */
    private int selectedIndex;
    /**
     * The MainPanel containing this TopPanel.
     */
    private MainPanel mainPanel;

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new topPanel using a given configuration and a MainPanel.
     *
     * @param newMainPanel The main panel of the application.
     * @param mainFrame The main frame of the application.
     */
    public TopPanel(final MainPanel newMainPanel, final MainFrame mainFrame) {
        super(new GridBagLayout(), false);

        this.mainPanel = newMainPanel;

        setBorder(BasicBorders.getTextFieldBorder());

        flagsPanel = new LCDPanel(LCDLENGTH);
        timePanel = new LCDPanel(LCDLENGTH);

        smileyButton = new JButton(new New("", new ImageIcon(smileys[INDEX_PLAY]), mainFrame));
        Dimension buttonDimension = new Dimension(smileyButton.getPreferredSize().height, smileyButton.getPreferredSize().height);
        smileyButton.setPreferredSize(buttonDimension);
        smileyButton.setMinimumSize(buttonDimension);
        smileyButton.setMaximumSize(buttonDimension);
        smileyButton.setFocusable(false);
        smileyButton.addMouseListener(new MouseListenerForSmileyButton(mainPanel));

        add(flagsPanel,
                new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(MARGIN, MARGIN, MARGIN, 0), 0, 0));
        add(smileyButton,
                new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(MARGIN, 0, MARGIN, 0), 0, 0));
        add(timePanel,
                new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(MARGIN, 0, MARGIN, MARGIN), 0, 0));

        background = getBackground();
        buttonBackground = smileyButton.getBackground();
    }

    //==========================================================================
    // Getters
    //==========================================================================
    /**
     * Returns the flags counter LCD panel.
     *
     * @return The flags counter LCD panel.
     */
    public final LCDPanel getFlagsPanel() {
        return flagsPanel;
    }

    /**
     * Returns the time counter LCD panel.
     *
     * @return The time counter LCD panel.
     */
    public final LCDPanel getTimePanel() {
        return timePanel;
    }

    /**
     * Returns the start button.
     *
     * @return The start button.
     */
    public final JButton getSmileyButton() {
        return smileyButton;
    }

    //==========================================================================
    // Setters
    //==========================================================================

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
        if (mainPanel != null && mainPanel.isColored()) {
            super.setBackground(bg);
        } else {
            int l = JMinesAction.getLuminance(bg);
            super.setBackground(new Color(l, l, l));
        }
        background = bg;

        if (getSmileyButton() != null) {
            if (mainPanel != null && mainPanel.isColored()) {
                getSmileyButton().setBackground(bg);

                smileys = new Image[ORIGINAL_SMILEYS.length];
                for (int i = 0; i < smileys.length; i++) {
                    smileys[i] = new BufferedImage(ORIGINAL_SMILEYS[i].getWidth(this), ORIGINAL_SMILEYS[i].getHeight(this), BufferedImage.TYPE_INT_ARGB);
                    Graphics g = smileys[i].getGraphics();
                    g.drawImage(ORIGINAL_SMILEYS[i], 0, 0, this);
                }
            } else {
                int l = JMinesAction.getLuminance(bg);
                getSmileyButton().setBackground(new Color(l, l, l));

                smileys = new Image[ORIGINAL_SMILEYS.length];
                for (int i = 0; i < smileys.length; i++) {
                    smileys[i] = new BufferedImage(ORIGINAL_SMILEYS[i].getWidth(this), ORIGINAL_SMILEYS[i].getHeight(this), BufferedImage.TYPE_BYTE_GRAY);
                    Graphics g = smileys[i].getGraphics();
                    g.setColor(getBackground());
                    g.fillRect(0, 0, smileys[i].getWidth(this), smileys[i].getHeight(this));
                    g.drawImage(ORIGINAL_SMILEYS[i], 0, 0, this);
                }
            }
            smileyButton.setIcon(new ImageIcon(smileys[selectedIndex]));
            getSmileyButton().repaint();
            buttonBackground = bg;
        }
    }

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
    /**
     * Used to change between a colored panel or a gray scales panel.
     *
     * @param colored Tell if this TopPanel have to be colored or paint using
     *                gray scales.
     */
    final void setColored(final boolean colored) {
        if (!colored) {
            int l = JMinesAction.getLuminance(background);
            Color coloredBackground = getBackground();
            setBackground(new Color(l, l, l));
            background = coloredBackground;

            l = JMinesAction.getLuminance(buttonBackground);
            smileyButton.setBackground(new Color(l, l, l));

            smileys = new Image[ORIGINAL_SMILEYS.length];
            for (int i = 0; i < smileys.length; i++) {
                smileys[i] = new BufferedImage(ORIGINAL_SMILEYS[i].getWidth(this), ORIGINAL_SMILEYS[i].getHeight(this), BufferedImage.TYPE_BYTE_GRAY);
                Graphics g = smileys[i].getGraphics();
                g.setColor(getBackground());
                g.fillRect(0, 0, smileys[i].getWidth(this), smileys[i].getHeight(this));
                g.drawImage(ORIGINAL_SMILEYS[i], 0, 0, this);
            }
        } else {
            setBackground(background);
            smileyButton.setBackground(buttonBackground);
            smileys = ORIGINAL_SMILEYS;
        }
        smileyButton.setIcon(new ImageIcon(smileys[selectedIndex]));

        getFlagsPanel().setColored(colored);
        getTimePanel().setColored(colored);
    }

    /**
     * Set the start button icon to the "play" smiley.
     */
    public final void setPlayIcon() {
        if (!mainPanel.getGamePanel().isLost() && !mainPanel.getGamePanel().isWon()) {
            selectedIndex = INDEX_PLAY;
            smileyButton.setIcon(new ImageIcon(smileys[selectedIndex]));
        }
    }

    /**
     * Set the start button icon to the "loose" smiley.
     */
    public final void setLooseIcon() {
        if (mainPanel.getGamePanel().isLost()) {
            selectedIndex = INDEX_LOOSE;
            smileyButton.setIcon(new ImageIcon(smileys[selectedIndex]));
        }
    }

    /**
     * Set the start button icon to the "click" smiley.
     */
    public final void setClickIcon() {
        if (!mainPanel.getGamePanel().isLost() && !mainPanel.getGamePanel().isWon()) {
            selectedIndex = INDEX_CLICK;
            smileyButton.setIcon(new ImageIcon(smileys[selectedIndex]));
        }
    }

    /**
     * Set the start button icon to the "win" smiley.
     */
    public final void setWinIcon() {
        if (mainPanel.getGamePanel().isWon()) {
            selectedIndex = INDEX_WIN;
            smileyButton.setIcon(new ImageIcon(smileys[selectedIndex]));
        }
    }

    /**
     * Set the start button icon to the "trivial" smiley.
     */
    public final void setTrivialIcon() {
        if (!mainPanel.getGamePanel().isWon() && !mainPanel.getGamePanel().isLost()) {
            selectedIndex = INDEX_TRIVIAL;
            smileyButton.setIcon(new ImageIcon(smileys[selectedIndex]));
        }
    }

    /**
     * Set the start button icon to the "win" smiley.
     */
    public final void setSchemaIcon() {
        if (!mainPanel.getGamePanel().isWon() && !mainPanel.getGamePanel().isLost()) {
            selectedIndex = INDEX_SCHEMA;
            smileyButton.setIcon(new ImageIcon(smileys[selectedIndex]));
        }
    }

    /**
     * Set the start button icon to the "win" smiley.
     */
    public final void setRandomIcon() {
        if (!mainPanel.getGamePanel().isWon() && !mainPanel.getGamePanel().isLost()) {
            selectedIndex = INDEX_RANDOM;
            smileyButton.setIcon(new ImageIcon(smileys[selectedIndex]));
        }
    }

}
