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

import pl.org.minions.utils.ui.sprite.SpriteCanvas;
import pl.org.minions.utils.ui.sprite.SpriteLayer;
import pl.org.minions.utils.ui.sprite.SpriteCanvas.CanvasLayerGroup;

/**
 * A layer group that contains all the foreground sprite
 * layers used to draw game content in stigma game.
 */
public class ForegroundLayerGroup extends CanvasLayerGroup
{
    private SpriteLayer shadowLayer;
    private SpriteLayer groundEffectLayer;
    private SpriteLayer foregroundLayer;
    private SpriteLayer overlayLayer;

    /**
     * Creates a ForegroundLayerGroup instance on given
     * sprite canvas.
     * @param canvas
     *            parent canvas
     */
    public ForegroundLayerGroup(SpriteCanvas canvas)
    {
        canvas.super();

        shadowLayer = new SpriteLayer(this);
        groundEffectLayer = new SpriteLayer(this);
        foregroundLayer = new SpriteLayer(this);
        overlayLayer = new SpriteLayer(this, null);
    }

    /**
     * Returns shadow layer.
     * @return shadow layer
     */
    public final SpriteLayer getShadowLayer()
    {
        return shadowLayer;
    }

    /**
     * Returns ground effect layer.
     * @return ground effect layer
     */
    public final SpriteLayer getGroundEffectLayer()
    {
        return groundEffectLayer;
    }

    /**
     * Returns foreground layer.
     * @return foreground layer
     */
    public final SpriteLayer getForegroundLayer()
    {
        return foregroundLayer;
    }

    /**
     * Returns overlay layer.
     * @return overlay layer
     */
    public SpriteLayer getOverlayLayer()
    {
        return overlayLayer;
    }

}
