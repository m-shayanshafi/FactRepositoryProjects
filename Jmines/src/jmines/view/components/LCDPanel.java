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
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicBorders;

import jmines.control.actions.JMinesAction;
import jmines.view.persistence.Configuration;

/**
 * An LCD like panel that can only display integer numbers.
 *
 * @author Zleurtor
 */
public class LCDPanel extends JPanel {

    static {
        final int basis = 10;

        BufferedImage[] tmp = new BufferedImage[basis + 1];
        for (int i = 0; i < basis; i++) {
            tmp[i] = Configuration.getInstance().getImage(
                    Configuration.PREFIX_IMG_LCD,
                    Integer.toString(i));
        }
        tmp[basis] = Configuration.getInstance().getImage(
                Configuration.PREFIX_IMG_LCD,
        "sign");
        int tmpLcdWidth = 0, tmpLcdHeight = 0;
        try {
            tmpLcdWidth = Configuration.getInstance().getInt(Configuration.KEY_LCD_WIDTH);
            tmpLcdHeight = Configuration.getInstance().getInt(Configuration.KEY_LCD_HEIGHT);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        }

        figures = tmp;
        ORIGINAL_FIGURES = tmp;
        LCD_WIDTH = tmpLcdWidth;
        LCD_HEIGHT = tmpLcdHeight;
    }

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = -2651365294851698098L;
    /**
     * The inset surrounding the displayed number.
     */
    private static final int INSET = 1;
    /**
     * The images corresponding to all the figures (in decimal basis).
     */
    private static final BufferedImage[] ORIGINAL_FIGURES;
    /**
     * The width of an LCD like figure.
     */
    private static final int LCD_WIDTH;
    /**
     * The height of an LCD like figure.
     */
    private static final int LCD_HEIGHT;
    /**
     * The index of the minus sign in the images array.
     */
    private static final int SIGN_INDEX = 10;
    /**
     * The images corresponding to all the figures (in decimal basis).
     */
    private static Image[] figures;

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The length the displayed number.
     */
    private final int length;
    /**
     * The buffer in which this panel is first painted.
     */
    private Image buffer;
    /**
     * The graphics of the previous buffer.
     */
    private Graphics bufferGraphics;
    /**
     * The number to display.
     */
    private int number = 0;

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct an LCD like panel.
     *
     * @param newLength The length of the numbers to display.
     */
    public LCDPanel(final int newLength) {
        super(false);

        length = newLength;

        setBorder(BasicBorders.getTextFieldBorder());

        Dimension dimension = new Dimension(
                getBorder().getBorderInsets(this).left + length * (INSET + LCD_WIDTH + INSET) + getBorder().getBorderInsets(this).right,
                getBorder().getBorderInsets(this).top + INSET + LCD_HEIGHT + INSET + getBorder().getBorderInsets(this).bottom);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);

        buffer = new BufferedImage(dimension.width - getBorder().getBorderInsets(null).left - getBorder().getBorderInsets(null).right,
                dimension.height - getBorder().getBorderInsets(null).top - getBorder().getBorderInsets(null).bottom,
                BufferedImage.TYPE_4BYTE_ABGR);
        bufferGraphics = buffer.getGraphics();
    }

    //==========================================================================
    // Getters
    //==========================================================================
    /**
     * Returns the number to display.
     *
     * @return The number to display.
     */
    public final int getNumber() {
        return number;
    }

    //==========================================================================
    // Setters
    //==========================================================================
    /**
     * Allows to change the number to display.
     *
     * @param newNumber The new number to display.
     */
    public final void setNumber(final int newNumber) {
        this.number = newNumber;
        repaint();
    }

    //==========================================================================
    // Inherited methods
    //==========================================================================
    /**
     * Paint this panel.
     *
     * @param g The graphics in which this panel has to be painted.
     * @see javax.swing.JComponent#paint(java.awt.Graphics)
     */
    @Override
    public final void paint(final Graphics g) {
        bufferGraphics.setColor(Color.BLACK);
        bufferGraphics.fillRect(0, 0, buffer.getWidth(this), buffer.getHeight(this));

        String tmp = Integer.toString(Math.abs(number));
        if (tmp.length() > length) {
            tmp = "";
            while (tmp.length() < length) {
                tmp = "9" + tmp;
            }
        }
        while (tmp.length() < length) {
            tmp = "0" + tmp;
        }

        if (number < 0) {
            tmp = "-" + tmp.substring(1);
        }

        for (int i = 0; i < tmp.length(); i++) {
            if (tmp.charAt(i) == '-') {
                bufferGraphics.drawImage(figures[SIGN_INDEX], INSET + i * (INSET + LCD_WIDTH + INSET), INSET, this);
            } else {
                byte figure = Byte.parseByte(Character.toString(tmp.charAt(i)));
                bufferGraphics.drawImage(figures[figure], INSET + i * (INSET + LCD_WIDTH + INSET), INSET, this);
            }
        }

        g.drawImage(buffer, getBorder().getBorderInsets(this).left, getBorder().getBorderInsets(this).top, this);
        paintBorder(g);
    }

    //==========================================================================
    // Static methods
    //==========================================================================
    /**
     * Change the color of the displayed figures by the given color.
     *
     * @param color The new color to set to the figures.
     */
    public static final void changeFiguresColor(final Color color) {
        final int colorComponentMaxValue = 255;

        figures = new Image[ORIGINAL_FIGURES.length];
        for (int i = 0; i < ORIGINAL_FIGURES.length; i++) {
            BufferedImage tmp = new BufferedImage(ORIGINAL_FIGURES[i].getWidth(), ORIGINAL_FIGURES[i].getHeight(), BufferedImage.TYPE_INT_ARGB);

            int ml = 0;
            for (int x = 0; x < ORIGINAL_FIGURES[i].getWidth(); x++) {
                for (int y = 0; y < ORIGINAL_FIGURES[i].getHeight(); y++) {
                    int tmpl = JMinesAction.getLuminance(new Color(ORIGINAL_FIGURES[i].getRGB(x, y)));
                    if (ml < tmpl) {
                        ml = tmpl;
                    }
                }
            }

            for (int x = 0; x < ORIGINAL_FIGURES[i].getWidth(); x++) {
                for (int y = 0; y < ORIGINAL_FIGURES[i].getHeight(); y++) {
                    int ol = JMinesAction.getLuminance(new Color(ORIGINAL_FIGURES[i].getRGB(x, y)));
                    double ratio = (double) (ol) / (double) ml;
                    Color tmpColor = new Color(
                            Math.min(colorComponentMaxValue, (int) Math.round(color.getRed() * ratio)),
                            Math.min(colorComponentMaxValue, (int) Math.round(color.getGreen() * ratio)),
                            Math.min(colorComponentMaxValue, (int) Math.round(color.getBlue() * ratio)));
                    tmp.setRGB(x, y, tmpColor.getRGB());
                }
            }

            figures[i] = tmp;
        }
    }

    /**
     * Retrieve the original figures color.
     */
    public static final void setOriginalFiguresColor() {
        figures = ORIGINAL_FIGURES;
    }

    //==========================================================================
    // Methods
    //==========================================================================
    /**
     * Allows to change the coloring of the panel.
     *
     * @param colored If true this panel will be painted with colors, else it
     *                will be painted using gray scales.
     */
    final void setColored(final boolean colored) {
        if (!colored) {
            buffer = new BufferedImage(buffer.getWidth(this), buffer.getHeight(this), BufferedImage.TYPE_BYTE_GRAY);
        } else {
            buffer = new BufferedImage(buffer.getWidth(this), buffer.getHeight(this), BufferedImage.TYPE_INT_ARGB);
        }
        bufferGraphics = buffer.getGraphics();
    }

}
