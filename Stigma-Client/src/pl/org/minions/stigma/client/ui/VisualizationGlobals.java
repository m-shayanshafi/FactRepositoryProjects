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
package pl.org.minions.stigma.client.ui;

import java.awt.Color;
import java.awt.image.BufferedImage;

import pl.org.minions.stigma.databases.Resourcer;

/**
 * Class that contains some values global to game
 * visualization on client.
 */
public final class VisualizationGlobals
{
    public static final String PATH_TO_BUNNY = "img/client/bun.png";

    public static final BufferedImage BUNNY_IMAGE =
            Resourcer.loadImage(PATH_TO_BUNNY);

    public static final String DEFAULT_ITEM_PATH =
            "img/client/icons/items/default_item.png";
    public static final BufferedImage DEFAULT_ITEM_IMAGE =
            Resourcer.loadImage(DEFAULT_ITEM_PATH);

    public static final String ITEM_STACK_PATH = "img/client/item_stack.png";
    public static final BufferedImage ITEM_STACK_IMAGE =
            Resourcer.loadImage(ITEM_STACK_PATH);

    public static final int MAP_TILE_WIDTH = 32;
    public static final int MAP_TILE_HEIGHT = 32;

    public static final Color COLOR_SELF = Color.GREEN;
    public static final Color COLOR_FOE = Color.RED;

    public static final String CUTTING_IMG_PATH =
            "img/client/icons/damage/cutting.png";
    public static final String PIERCING_IMG_PATH =
            "img/client/icons/damage/piercing.png";
    public static final String BLUNT_IMG_PATH =
            "img/client/icons/damage/blunt.png";
    public static final String BURN_IMG_PATH =
            "img/client/icons/damage/fire.png";
    public static final String FROSTBITE_IMG_PATH =
            "img/client/icons/damage/ice.png";
    public static final String POISON_IMG_PATH =
            "img/client/icons/damage/poison.png";
    public static final String SHOCK_IMG_PATH =
            "img/client/icons/damage/shock.png";

    public static final BufferedImage CUTTING_IMG =
            Resourcer.loadImage(CUTTING_IMG_PATH);
    public static final BufferedImage PIERCING_IMG =
            Resourcer.loadImage(PIERCING_IMG_PATH);
    public static final BufferedImage BLUNT_IMG =
            Resourcer.loadImage(BLUNT_IMG_PATH);
    public static final BufferedImage BURN_IMG =
            Resourcer.loadImage(BURN_IMG_PATH);
    public static final BufferedImage SHOCK_IMG =
            Resourcer.loadImage(SHOCK_IMG_PATH);
    public static final BufferedImage POISON_IMG =
            Resourcer.loadImage(POISON_IMG_PATH);
    public static final BufferedImage FROSTBITE_IMG =
            Resourcer.loadImage(FROSTBITE_IMG_PATH);

    public static final String COOLDOWN_IMG_PATH =
            "img/client/icons/cooldown.png";
    public static final BufferedImage COOLDOWN_IMG =
            Resourcer.loadImage(COOLDOWN_IMG_PATH);

    public static final String ATTACK_PATH = "img/client/icons/aim.png";
    public static final BufferedImage ATTACK_IMG =
            Resourcer.loadImage(ATTACK_PATH);

    public static final String CRITICAL_PATH = "img/client/icons/critical.png";
    public static final BufferedImage CRITICAL_IMG =
            Resourcer.loadImage(CRITICAL_PATH);

    public static final String WEIGHT_PATH = "img/client/icons/weight.png";
    public static final BufferedImage WEIGHT_IMG =
            Resourcer.loadImage(WEIGHT_PATH);

    private VisualizationGlobals()
    {
    }
}
