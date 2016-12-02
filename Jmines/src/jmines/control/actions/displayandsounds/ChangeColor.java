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
package jmines.control.actions.displayandsounds;

import java.awt.Color;

import javax.swing.Action;

import jmines.control.actions.JMinesAction;
import jmines.view.components.MainFrame;
import jmines.view.persistence.Configuration;

/**
 * The super class of all change color actions (for background, LCD and
 * buttons) used in JMines.
 *
 * @author Zleurtor
 */
public abstract class ChangeColor extends JMinesAction {

    //=========================================================================
    // Static attributes
    //=========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = -4830440285847430823L;
    /**
     * An identifier for a user customized color.
     */
    public static final byte CUSTOM = -3;
    /**
     * An identifier for a user customized color.
     */
    public static final byte ORIGINAL = -2;
    /**
     * An identifier for the look and feel dependent color.
     */
    public static final byte LOOK_AND_FEEL_DEPENDENT = -1;
    /**
     * An identifier for the black color.
     */
    public static final byte BLACK = 0;
    /**
     * An identifier for the blue color.
     */
    public static final byte BLUE = 1;
    /**
     * An identifier for the cyan color.
     */
    public static final byte CYAN = 2;
    /**
     * An identifier for the dark gray color.
     */
    public static final byte DARK_GRAY = 3;
    /**
     * An identifier for the gray color.
     */
    public static final byte GRAY = 4;
    /**
     * An identifier for the green color.
     */
    public static final byte GREEN = 5;
    /**
     * An identifier for the light gray color.
     */
    public static final byte LIGHT_GRAY = 6;
    /**
     * An identifier for the magenta color.
     */
    public static final byte MAGENTA = 7;
    /**
     * An identifier for the orange color.
     */
    public static final byte ORANGE = 8;
    /**
     * An identifier for the pink color.
     */
    public static final byte PINK = 9;
    /**
     * An identifier for the red color.
     */
    public static final byte RED = 10;
    /**
     * An identifier for the white color.
     */
    public static final byte WHITE = 11;
    /**
     * An identifier for the yellow color.
     */
    public static final byte YELLOW = 12;
    /**
     * An identifier for the color identifier key.
     */
    public static final String ID = "id";

    //=========================================================================
    // Attributes
    //=========================================================================
    /**
     * The color to set.
     */
    private Color color;
    /**
     * The id of the color to set.
     */
    private final byte colorId;

    //=========================================================================
    // Constructors
    //=========================================================================
    /**
     * Construct a new ChangeColor action.
     *
     * @param newColorId The identifier for the color to set.
     * @param mainFrame The main frame of the application.
     */
    protected ChangeColor(final byte newColorId, final MainFrame mainFrame) {
        super(null, mainFrame);

        colorId = newColorId;
        putValue(ID, colorId);

        String name = null;
        switch (newColorId) {
            case CUSTOM:
                name = Configuration.getInstance().getText(Configuration.KEY_TEXT_CUSTOM);
                color = null;
                break;
            case ORIGINAL:
                name = Configuration.getInstance().getText(Configuration.KEY_TEXT_ORIGINAL);
                color = null;
                break;
            case LOOK_AND_FEEL_DEPENDENT:
                name = Configuration.getInstance().getText(Configuration.KEY_TEXT_LOOKANDFEELDEPENDENT);
                color = null;
                break;
            case BLACK:
                name = Configuration.getInstance().getText(Configuration.KEY_TEXT_BLACK);
                color = Color.BLACK;
                break;
            case BLUE:
                name = Configuration.getInstance().getText(Configuration.KEY_TEXT_BLUE);
                color = Color.BLUE;
                break;
            case CYAN:
                name = Configuration.getInstance().getText(Configuration.KEY_TEXT_CYAN);
                color = Color.CYAN;
                break;
            case DARK_GRAY:
                name = Configuration.getInstance().getText(Configuration.KEY_TEXT_DARKGRAY);
                color = Color.DARK_GRAY;
                break;
            case GRAY:
                name = Configuration.getInstance().getText(Configuration.KEY_TEXT_GRAY);
                color = Color.GRAY;
                break;
            case GREEN:
                name = Configuration.getInstance().getText(Configuration.KEY_TEXT_GREEN);
                color = Color.GREEN;
                break;
            case LIGHT_GRAY:
                name = Configuration.getInstance().getText(Configuration.KEY_TEXT_LIGHTGRAY);
                color = Color.LIGHT_GRAY;
                break;
            case MAGENTA:
                name = Configuration.getInstance().getText(Configuration.KEY_TEXT_MAGENTA);
                color = Color.MAGENTA;
                break;
            case ORANGE:
                name = Configuration.getInstance().getText(Configuration.KEY_TEXT_ORANGE);
                color = Color.ORANGE;
                break;
            case PINK:
                name = Configuration.getInstance().getText(Configuration.KEY_TEXT_PINK);
                color = Color.PINK;
                break;
            case RED:
                name = Configuration.getInstance().getText(Configuration.KEY_TEXT_RED);
                color = Color.RED;
                break;
            case WHITE:
                name = Configuration.getInstance().getText(Configuration.KEY_TEXT_WHITE);
                color = Color.WHITE;
                break;
            case YELLOW:
                name = Configuration.getInstance().getText(Configuration.KEY_TEXT_YELLOW);
                color = Color.YELLOW;
                break;
            default:
                color = null;
                break;
        }
        putValue(Action.NAME, name);
    }

    //=========================================================================
    // Getters
    //=========================================================================
    /**
     * Returns the color to set.
     *
     * @return The color to set.
     */
    public final Color getColor() {
        return color;
    }

    /**
     * Returns the id of the color to set.
     *
     * @return The id of the color to set.
     */
    public final byte getColorId() {
        return colorId;
    }

    //=========================================================================
    // Setters
    //=========================================================================
    /**
     * Allows to set a new value to the color to set.
     *
     * @param newColor The new value for the color to set.
     */
    public final void setColor(final Color newColor) {
        this.color = newColor;
    }

    //=========================================================================
    // Inherited methods
    //=========================================================================

    //=========================================================================
    // Static methods
    //=========================================================================

    //=========================================================================
    // Methods
    //=========================================================================
    /**
     * Transform the color to its hexadecimal string representation.
     *
     * @param c The color to transform.
     * @return The hexadecimal string representation of the given color.
     */
    protected final String toHexadecimal(final Color c) {
        final int hexadecimalBasis = 16;

        String r = Integer.toString(c.getRed(), hexadecimalBasis);
        String g = Integer.toString(c.getGreen(), hexadecimalBasis);
        String b = Integer.toString(c.getBlue(), hexadecimalBasis);

        while (r.length() < 2) {
            r = "0" + r;
        }
        while (g.length() < 2) {
            g = "0" + g;
        }
        while (b.length() < 2) {
            b = "0" + b;
        }

        return r + g + b;
    }

}
