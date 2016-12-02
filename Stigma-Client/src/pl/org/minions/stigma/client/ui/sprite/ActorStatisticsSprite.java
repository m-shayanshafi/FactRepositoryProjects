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
package pl.org.minions.stigma.client.ui.sprite;

import java.awt.Color;
import java.awt.Graphics2D;

import pl.org.minions.utils.ui.sprite.Sprite;

/**
 * A sprite that represents health and stamina of a player
 * as a two horizontal bars that together fill the bounds of
 * this sprite.
 */
public class ActorStatisticsSprite extends Sprite
{
    private static final Color HEALTH_BAR_COLOR = Color.red;
    private static final Color STAMINA_BAR_COLOR = Color.green.brighter();
    private static final Color BACKGROUND_COLOR = new Color(25, 25, 25, 127);
    private static final Color DEFAULT_BORDER_COLOR = Color.black;
    private static final int BORDER_WIDTH = 1;

    private Color borderColor = DEFAULT_BORDER_COLOR;
    private int health;
    private int stamina;
    private int mhealth;
    private int mstamina;

    /**
     * Creates a new sprite for presenting health and
     * stamina of an actor.
     */
    public ActorStatisticsSprite()
    {
        setColor(BACKGROUND_COLOR);
    }

    /**
     * Returns health.
     * @return health
     */
    public final int getHealth()
    {
        return health;
    }

    /**
     * Returns max health.
     * @return max health
     */
    public final int getMaxHealth()
    {
        return mhealth;
    }

    /**
     * Returns max stamina.
     * @return max stamina
     */
    public final int getMaxStamina()
    {
        return mstamina;
    }

    /**
     * Returns stamina.
     * @return stamina
     */
    public final int getStamina()
    {
        return stamina;
    }

    /** {@inheritDoc} */
    @Override
    protected void renderSpriteContents(Graphics2D graphics)
    {
        super.renderSpriteContents(graphics); //Render background

        final int verticalBorderSpace = 3 * BORDER_WIDTH;
        final int halfHeight = (getHeight() - verticalBorderSpace) / 2;

        graphics.setColor(HEALTH_BAR_COLOR);
        graphics.fillRect(getMinX() + BORDER_WIDTH,
                          getMinY() + BORDER_WIDTH,
                          mhealth != 0 ? health / mhealth
                              * (getWidth() - BORDER_WIDTH) : getWidth()
                              - BORDER_WIDTH,
                          halfHeight);

        graphics.setColor(STAMINA_BAR_COLOR);
        graphics.fillRect(getMinX() + BORDER_WIDTH,
                          getMinY() + halfHeight + BORDER_WIDTH + BORDER_WIDTH,
                          mstamina != 0 ? stamina / mstamina
                              * (getWidth() - BORDER_WIDTH) : getWidth()
                              - BORDER_WIDTH,
                          getHeight() - verticalBorderSpace - halfHeight);

        graphics.setColor(borderColor);
        graphics.drawRect(getMinX(), getMinY(), getWidth(), getHeight()
            - BORDER_WIDTH);
        graphics.drawLine(getMinX(),
                          getMinY() + halfHeight + BORDER_WIDTH,
                          getMaxX(),
                          getMinY() + halfHeight + BORDER_WIDTH);

    }

    /**
     * Sets new value of health.
     * @param health
     *            the health to set
     */
    public final void setHealth(int health)
    {
        this.health = health;
    }

    /**
     * Sets new value of max health.
     * @param mhealth
     *            the max health to set
     */
    public final void setMaxHealth(int mhealth)
    {
        this.mhealth = mhealth;
    }

    /**
     * Sets new value of max stamina.
     * @param mstamina
     *            the max stamina to set
     */
    public final void setMaxStamina(int mstamina)
    {
        this.mstamina = mstamina;
    }

    /**
     * Sets new value of stamina.
     * @param stamina
     *            the stamina to set
     */
    public final void setStamina(int stamina)
    {
        this.stamina = stamina;
    }

    /**
     * Sets new border color.
     * @param borderColor
     *            the border color to set
     */
    public void setBorderColor(Color borderColor)
    {
        this.borderColor = borderColor;
    }
}
