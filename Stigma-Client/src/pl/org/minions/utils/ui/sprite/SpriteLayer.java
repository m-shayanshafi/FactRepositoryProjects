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

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pl.org.minions.utils.ui.sprite.SpriteCanvas.CanvasLayer;
import pl.org.minions.utils.ui.sprite.SpriteCanvas.Viewport;

/**
 * {@link SpriteCanvas} layer that stores a collection of
 * {@link Sprite sprites}.
 * <p>
 * Only sprites that have bounds colliding with
 * {@link Viewport canvas viewport} are rendered.
 * <p>
 * Sprites are rendered in order enforced by a
 * {@link Comparator sprite comparator}.
 * <p>
 * Default comparator uses the following formula: <code>
 * <pre>
 * Long.signum(((long) sprite1.getPosition().y) &lt;&lt; Integer.SIZE | sprite1.getPosition().x
 *             - ((long) sprite2.getPosition().y) &lt;&lt; Integer.SIZE | sprite2.getPosition().x)
 * </pre></code>
 * <p>
 * The class is thread safe.
 */
public class SpriteLayer extends CanvasLayer
{
    /**
     * Default sprite comparator used by a sprite layer.
     * <p>
     * Sorts Sprite according to their y position, then to
     * their depth hint, then to their x position.
     */
    public static final class DefaultSpriteComparator implements
                                                     Comparator<Sprite>
    {
        /** {@inheritDoc} */
        @Override
        public int compare(Sprite one, Sprite other)
        {
            return (Integer.signum(one.getPosition().y - other.getPosition().y) << 2)
                + (Integer.signum(one.getDepthHint() - other.getDepthHint()) << 1)
                + Integer.signum(one.getPosition().x - other.getPosition().x);
        }
    }

    private Set<Sprite> sprites = new HashSet<Sprite>();
    private Collection<Sprite> visibleSprites = Collections.emptyList();

    private Object spritesModificationMutex = false;
    private Object visibleSpritesModificationMutex = false;

    private final Comparator<Sprite> spriteSorter;

    /**
     * Creates a new SpriteLayer on top of the canvas. Uses
     * default comparator to determine the rendering order
     * of the sprites.
     * @param canvas
     *            canvas for this layer to be a part of
     */
    public SpriteLayer(SpriteCanvas canvas)
    {
        this(canvas, new DefaultSpriteComparator());
    }

    /**
     * Creates a new SpriteLayer on top of the canvas. Uses
     * the given comparator to determine the rendering order
     * of the sprites.
     * @param canvas
     *            canvas for this layer to be a part of
     * @param spriteOrderComparator
     *            sprites on this layer will be rendered
     *            according to ordering defined by this
     *            comparator
     */
    public SpriteLayer(SpriteCanvas canvas,
                       Comparator<Sprite> spriteOrderComparator)
    {
        canvas.super();
        spriteSorter = spriteOrderComparator;
    }

    /**
     * Creates a new SpriteLayer and registers it in the
     * encapsulating canvas object as the topmost layer in
     * the selected layer group. Uses default comparator to
     * determine the rendering order of the sprites.
     * @param group
     *            layer group to contain the new layer
     */
    public SpriteLayer(SpriteCanvas.CanvasLayerGroup group)
    {
        this(group, new DefaultSpriteComparator());
    }

    /**
     * Creates a new SpriteLayer and registers it in the
     * encapsulating canvas object as the topmost layer in
     * the selected layer group. Uses the given comparator
     * to determine the rendering order of the sprites.
     * @param group
     *            layer group to contain the new layer
     * @param spriteOrderComparator
     *            sprites on this layer will be rendered
     *            according to ordering defined by this
     *            comparator
     */
    public SpriteLayer(SpriteCanvas.CanvasLayerGroup group,
                       Comparator<Sprite> spriteOrderComparator)
    {
        group.getCanvas().super(group);
        spriteSorter = spriteOrderComparator;
    }

    /**
     * Adds a sprite to the layer.
     * @param sprite
     *            sprite to be added
     */
    public void addSprite(Sprite sprite)
    {
        synchronized (spritesModificationMutex)
        {
            sprites.add(sprite);
        }
    }

    /**
     * Adds some sprite to the layer.
     * @param sprites
     *            sprites to be added
     */
    public void addSprites(Collection<Sprite> sprites)
    {
        synchronized (spritesModificationMutex)
        {
            this.sprites.addAll(sprites);
        }
    }

    /**
     * Adds some sprites to the layer.
     * @param sprites
     *            sprites to be added
     */
    public void addSprites(Sprite... sprites)
    {
        synchronized (spritesModificationMutex)
        {
            Collections.addAll(this.sprites, sprites);
        }
    }

    /**
     * Returns all sprites with a bounds that contain the
     * specified pixel in viewport coordinates.
     * <p>
     * The collection returned is ordered from the closest
     * to the farthest sprite.
     * <p>
     * NOTE: Under present implementation, only the sprites
     * that were intersecting the viewport during last
     * {@link #render(Graphics2D)} call are checked.
     * Modifying sprites between {@link #render(Graphics2D)}
     * and {@link #getSpritesAtPoinInViewport(Point)} calls
     * can results in undefined behavior.
     * @param point
     *            a point in viewport coordinates
     * @return collection of sprites under the mouse pointer
     */
    public final Collection<Sprite> getSpritesAtPoinInViewport(Point point)
    {
        Deque<Sprite> collidingSprites = new LinkedList<Sprite>();

        if (!getCanvas().getViewport().getBounds().contains(point))
            return collidingSprites;

        synchronized (visibleSpritesModificationMutex)
        {
            for (Sprite sprite : visibleSprites)
            {
                if (sprite.boundsContainPoint(point))
                    collidingSprites.addFirst(sprite);
            }
        }

        return collidingSprites;
    }

    /**
     * Retrieves all sprites intersecting with given
     * rectangle.
     * <p>
     * Sprites with either width or height equal to zero are
     * also included.
     * <p>
     * Returned sprites are sorted using
     * {@link #spriteSorter}.
     * @param rectangle
     *            rectangle to check intersections with
     * @return sorted collection of sprites intersecting
     *         with the rectangle
     */
    private Collection<Sprite> getSpritesIntersecting(Rectangle rectangle)
    {
        List<Sprite> intersectingSprites = new ArrayList<Sprite>();
        synchronized (spritesModificationMutex)
        {
            for (Sprite sprite : sprites)
                if (sprite.getWidth() * sprite.getHeight() == 0
                    || sprite.boundsIntersect(rectangle))
                    intersectingSprites.add(sprite);
        }
        if (spriteSorter != null)
            Collections.sort(intersectingSprites, spriteSorter);
        return intersectingSprites;
    }

    /**
     * Returns spriteSorter.
     * @return spriteSorter
     */
    public final Comparator<Sprite> getSpriteSorter()
    {
        return spriteSorter;
    }

    /**
     * Removes selected sprite from the layer.
     * @param sprite
     *            sprite to be removed
     */
    public void removeSprite(Sprite sprite)
    {
        boolean removed;
        synchronized (spritesModificationMutex)
        {
            removed = sprites.remove(sprite);
        }
        if (removed)
        {
            synchronized (visibleSpritesModificationMutex)
            {
                visibleSprites.remove(sprite);
            }
        }
    }

    /**
     * Removes selected sprites from the layer.
     * @param sprites
     *            sprites to be removed
     */
    public void removeSprites(Collection<Sprite> sprites)
    {
        boolean removed;

        synchronized (spritesModificationMutex)
        {
            removed = this.sprites.removeAll(sprites);
        }
        if (removed)
        {
            synchronized (visibleSpritesModificationMutex)
            {
                visibleSprites.removeAll(sprites);
            }
        }
    }

    /**
     * Removes selected sprites from the layer.
     * <p>
     * This is equivalent to call {@code
     * removeSprites(Arrays.asList(sprites))}
     * @see #removeSprites(Collection)
     * @param sprites
     *            sprites to be removed
     */
    public void removeSprites(Sprite... sprites)
    {
        removeSprites(Arrays.asList(sprites));
    }

    /** {@inheritDoc} */
    @Override
    public void render(Graphics2D graphics)
    {
        Collection<Sprite> newVisibleSprites =
                getSpritesIntersecting(getCanvas().getViewport().getBounds());

        synchronized (visibleSpritesModificationMutex)
        {
            visibleSprites = newVisibleSprites;
            for (Sprite sprite : visibleSprites)
            {
                sprite.render(graphics);
            }
        }
    }
}
