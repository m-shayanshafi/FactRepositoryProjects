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
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pl.org.minions.stigma.client.ui.VisualizationGlobals;
import pl.org.minions.stigma.databases.Resourcer;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.globals.Direction;
import pl.org.minions.stigma.globals.GlobalConfig;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.utils.ui.images.AlterHSFilter;
import pl.org.minions.utils.ui.images.BufferedImageFiltering;
import pl.org.minions.utils.ui.sprite.ImageSprite;
import pl.org.minions.utils.ui.sprite.MoveAnimation;
import pl.org.minions.utils.ui.sprite.SetPositionAnimation;
import pl.org.minions.utils.ui.sprite.SpriteAnimation;

/**
 * Class that handles animating an actor sprite in response
 * to high-level commands.
 */
public class ActorAnimator
{

    private static final int TARGET_DEPTH_HINT = -2;
    private static final int ELLIPSE_DEPTH_HINT = -1;

    private static final Dimension MAP_TILE_SIZE =
            new Dimension(VisualizationGlobals.MAP_TILE_WIDTH,
                          VisualizationGlobals.MAP_TILE_HEIGHT);

    //Temporary
    private static final Point BUNNY_IMAGE_OFFSET = new Point(16, 24);

    private static final String PATH_TO_ELLIPSE = "img/client/ellipse.png";
    private static final BufferedImage ELLIPSE_IMAGE =
            Resourcer.loadImage(PATH_TO_ELLIPSE);
    private static final Point ELLIPSE_IMAGE_OFFSET = new Point(16, 24);

    private static final String PATH_TO_TARGET = "img/client/target.png";
    private static final BufferedImage TARGET_IMAGE =
            Resourcer.loadImage(PATH_TO_TARGET);
    private static final Point TARGET_IMAGE_OFFSET = new Point(24, 12);

    private static final Dimension BARS_SIZE = new Dimension(24, 7);
    private static final Point BARS_OFFSET = new Point(12, 23);

    private static final float BARS_STANDARD_ALPHA = .75f;
    private static final float BARS_HIGHLIGHTED_ALPHA = 1;

    private static final Map<Color, BufferedImage> COLORED_ELLIPSE_IMAGES =
            new HashMap<Color, BufferedImage>();

    private SpriteAnimation currentAnimation;
    private BlockingQueue<SpriteAnimation> pendingAnimations =
            new LinkedBlockingQueue<SpriteAnimation>();

    private int actorId;
    private Actor actor;

    private ForegroundLayerGroup foreground;
    //    private SpriteLayer spriteLayer;
    //    private SpriteLayer groundLayer;

    private ImageSprite actorSprite;
    private ImageSprite ellipseSprite;
    private ImageSprite targetSprite;
    private ActorStatisticsSprite barsSprite;

    private World world;

    /**
     * Creates a new actor animator.
     * @param actorId
     *            id of the actor to represent
     * @param world
     *            world to get the actor from, once the
     *            actor is completely loaded
     * @param foreground
     *            foreground layer group
     */
    public ActorAnimator(int actorId,
                         World world,
                         ForegroundLayerGroup foreground)
    {
        this.actorId = actorId;
        this.world = world;
        fetchActor();

        this.foreground = foreground;

        actorSprite = new ImageSprite();
        actorSprite.setImage(VisualizationGlobals.BUNNY_IMAGE);
        actorSprite.setOffset(BUNNY_IMAGE_OFFSET);
        actorSprite.resizeToContainImage(true);
        foreground.getForegroundLayer().addSprite(actorSprite);

        barsSprite = new ActorStatisticsSprite();
        barsSprite.setOffset(BARS_OFFSET);
        barsSprite.setSize(BARS_SIZE);
        barsSprite.setAlpha(BARS_STANDARD_ALPHA);
        foreground.getOverlayLayer().addSprite(barsSprite);

        ellipseSprite = new ImageSprite(ELLIPSE_IMAGE, ELLIPSE_IMAGE_OFFSET);
        foreground.getGroundEffectLayer().addSprite(ellipseSprite);
        ellipseSprite.setDepthHint(ELLIPSE_DEPTH_HINT);

        targetSprite = new ImageSprite(TARGET_IMAGE, TARGET_IMAGE_OFFSET);
        foreground.getGroundEffectLayer().addSprite(targetSprite);
        targetSprite.setVisible(false);
        targetSprite.setDepthHint(TARGET_DEPTH_HINT);
    }

    /**
     * Sets the color associated with the actor (friend or
     * foe).
     * <p>
     * Used to mark the actor.
     * @param color
     *            color to set
     */
    public void setActorColor(Color color)
    {
        BufferedImage coloredEllipseImage;
        if (!COLORED_ELLIPSE_IMAGES.containsKey(color))
        {
            coloredEllipseImage =
                    BufferedImageFiltering.globalInstance()
                                          .filter(ELLIPSE_IMAGE,
                                                  new AlterHSFilter(color));

            COLORED_ELLIPSE_IMAGES.put(color, coloredEllipseImage);
        }
        else
            coloredEllipseImage = COLORED_ELLIPSE_IMAGES.get(color);

        ellipseSprite.setImage(coloredEllipseImage);
    }

    /**
     * Used to visually mark the actor.
     * @param highlighted
     *            should the actor be highlighted
     */
    public void setHighlighted(boolean highlighted)
    {
        targetSprite.setVisible(highlighted);
        barsSprite.setAlpha(highlighted ? BARS_HIGHLIGHTED_ALPHA
                                       : BARS_STANDARD_ALPHA);
    }

    /**
     * Checks if animator has any animations left to finish.
     * @return <code>true</code> if there are no current or
     *         pending animations, <code>false</code>
     *         otherwise
     */
    public boolean isAnimationFinished()
    {
        return currentAnimation == null && pendingAnimations.isEmpty();
    }

    /**
     * Updates as much of current and pending animations as
     * the elapsed time enables.
     * @param milliseconds
     *            time elapsed
     * @see SpriteAnimation#animate(long)
     */
    public void animate(long milliseconds)
    {
        assert milliseconds >= 0;

        if (currentAnimation != null)
        {
            milliseconds -= currentAnimation.animate(milliseconds);
            if (currentAnimation.hasFinished())
                currentAnimation = null;
        }

        assert currentAnimation == null ? milliseconds >= 0 : milliseconds == 0;

        while (!pendingAnimations.isEmpty()
            && (milliseconds > 0 || pendingAnimations.peek().getDuration() == 0))
        {
            currentAnimation = pendingAnimations.poll();
            milliseconds -= currentAnimation.animate(milliseconds);
            if (!currentAnimation.hasFinished())
            {
                assert milliseconds == 0;
                break;
            }
            else
                currentAnimation = null;
        }

        ellipseSprite.setPosition(actorSprite.getPosition());
        targetSprite.setPosition(actorSprite.getPosition());

        barsSprite.setHealth(actor.getCurrentHealth());
        barsSprite.setMaxHealth(actor.getMaxHealth());
        barsSprite.setStamina(actor.getCurrentStamina());
        barsSprite.setMaxStamina(actor.getMaxStamina());
        barsSprite.getPosition().x = actorSprite.getPosition().x;
        barsSprite.getPosition().y = actorSprite.getPosition().y - 1;
        barsSprite.setPosition(barsSprite.getPosition());
    }

    /**
     * Sets the position of the actor.
     * <p>
     * The actor representation is moved to the requested
     * position on map after other animations end.
     * @param position
     *            new position of the actor on map
     */
    public void setPosition(Position position)
    {
        appendAnimation(new SetPositionAnimation(actorSprite,
                                                 0,
                                                 new Point(position.getX()
                                                               * MAP_TILE_SIZE.width
                                                               + MAP_TILE_SIZE.width
                                                               / 2,
                                                           position.getY()
                                                               * MAP_TILE_SIZE.height
                                                               + MAP_TILE_SIZE.height
                                                               / 2)));
    }

    /**
     * Animates walking in chosen direction to the nearest
     * tile over specified number of game turns.
     * @param direction
     *            walking direction
     */
    public void walk(Direction direction)
    {
        fetchActor();
        int moveDuration =
                (actor != null ? actor.getMoveSpeed() : 0)
                    * GlobalConfig.globalInstance().getMillisecondsPerTurn();

        Dimension moveVector;
        switch (direction)
        {
            case E:
                moveVector = new Dimension(MAP_TILE_SIZE.width, 0);
                break;
            case N:
                moveVector = new Dimension(0, -MAP_TILE_SIZE.height);
                break;
            case NE:
                moveVector =
                        new Dimension(MAP_TILE_SIZE.width,
                                      -MAP_TILE_SIZE.height);
                break;
            case NW:
                moveVector =
                        new Dimension(-MAP_TILE_SIZE.width,
                                      -MAP_TILE_SIZE.height);
                break;
            case S:
                moveVector = new Dimension(0, MAP_TILE_SIZE.height);
                break;
            case SE:
                moveVector =
                        new Dimension(MAP_TILE_SIZE.width, MAP_TILE_SIZE.height);
                break;
            case SW:
                moveVector =
                        new Dimension(-MAP_TILE_SIZE.width,
                                      MAP_TILE_SIZE.height);
                break;
            case W:
                moveVector = new Dimension(-MAP_TILE_SIZE.width, 0);
                break;
            case NONE:
            default:
                moveVector = new Dimension(0, 0);
        }
        appendAnimation(new MoveAnimation(actorSprite, moveDuration, moveVector));
    }

    /**
     * Removes all graphic elements associated with this
     * animator from canvas layers.
     */
    public void remove()
    {
        foreground.getForegroundLayer().removeSprite(actorSprite);
        foreground.getOverlayLayer().removeSprite(barsSprite);
        foreground.getGroundEffectLayer().removeSprite(ellipseSprite);
        foreground.getGroundEffectLayer().removeSprite(targetSprite);
    }

    private void appendAnimation(SpriteAnimation animation)
    {
        pendingAnimations.add(animation);
    }

    private void fetchActor()
    {
        if (actor == null)
            actor = world.getActor(actorId);
    }

    /**
     * Returns the location on canvas of the actor
     * representation.
     * @return position in pixels
     */
    public Point getActorLocation()
    {
        return actorSprite.getPosition();
    }

    /**
     * Returns whether health and stamina bars are shown for
     * animated actor.
     * @return <code>true</code> if the bars are visible
     */
    public final boolean areBarsVisible()
    {
        return barsSprite.isVisible();
    }

    /**
     * Sets whether health and stamina bars are to be shown
     * for animated actor.
     * @param visible
     *            if the bars are to be visible
     */
    public final void setBarsVisible(boolean visible)
    {
        barsSprite.setVisible(visible);
    }

}
