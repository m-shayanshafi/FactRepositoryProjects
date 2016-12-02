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
package pl.org.minions.utils.ui.sprite;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.GlyphVector;

/**
 * Class that represents a line of text to be displayed by
 * SpriteCanvas.
 */
public class StringSprite extends Sprite
{
    private String text;
    private Font font;

    private GlyphVector glyphs;

    /**
     * Creates a new text sprite.
     * @param text
     *            text to be displayed by the sprite; line
     *            breaking is not supported yet
     * @param font
     *            font to write the text with
     */
    public StringSprite(String text, Font font)
    {
        super();
        this.text = text;
        this.font = font;
    }

    /**
     * Returns font.
     * @return font
     */
    public final Font getFont()
    {
        return font;
    }

    /**
     * Returns text.
     * @return text
     */
    public final String getText()
    {
        return text;
    }

    /**
     * Sets new font.
     * @param font
     *            the font to set
     */
    public final void setFont(Font font)
    {
        this.font = font;
        glyphs = null;
    }

    /**
     * Sets new value of text.
     * @param text
     *            the text to set
     */
    public final void setText(String text)
    {
        this.text = text;
        glyphs = null;
    }

    /** {@inheritDoc} */
    @Override
    protected void renderSpriteContents(Graphics2D graphics)
    {
        if (text == null)
            super.renderSpriteContents(graphics);

        if (glyphs == null)
        {
            glyphs =
                    font.createGlyphVector(graphics.getFontRenderContext(),
                                           text);
            final Rectangle pixelBounds =
                    glyphs.getPixelBounds(null,
                                          getPosition().x,
                                          getPosition().y);
            setOffset(new Point(pixelBounds.width / 2, -pixelBounds.height / 2));
            setSize(pixelBounds.getSize());
        }

        graphics.setColor(getColor());
        graphics.drawGlyphVector(glyphs, getMinX(), getMinY());
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return super.toString() + " Text:" + text;
    }
}
