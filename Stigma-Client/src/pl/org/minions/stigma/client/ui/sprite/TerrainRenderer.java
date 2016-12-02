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
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import pl.org.minions.stigma.client.images.ImageDB;
import pl.org.minions.stigma.client.images.ImageProxy;
import pl.org.minions.stigma.client.images.TerrainTypeImageId;
import pl.org.minions.stigma.client.images.ImageProxy.LoadingState;
import pl.org.minions.stigma.client.images.precedence.TerrainTypesPrecedenceManager;
import pl.org.minions.stigma.client.ui.Clearable;
import pl.org.minions.stigma.client.ui.VisualizationGlobals;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.stigma.game.map.TerrainSet;
import pl.org.minions.stigma.game.map.TerrainType;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.utils.logger.Log;
import pl.org.minions.utils.ui.sprite.ImageGridLayer;
import pl.org.minions.utils.ui.sprite.SpriteCanvas;
import pl.org.minions.utils.ui.sprite.SpriteCanvas.CanvasLayer;

/**
 * The class manages {@link CanvasLayer sprite canvas
 * layers} that are used to represent a game map.
 * <p>
 * The manager does not actively check if new data is
 * available and relies on other objects calling the
 * resource applying methods.
 */
public class TerrainRenderer implements Clearable
{
    /**
     *
     */
    private static final Color ERASE_COLOR = new Color(0, 0, 0, 0);

    private class TerrainTypePriorityComparator implements
                                               Comparator<TerrainType>
    {
        @Override
        public int compare(TerrainType o1, TerrainType o2)
        {
            return o1.getPriority() - o2.getPriority();
        }
    }

    private static final String TERRAIN_SET_HAS_NOT_BEEN_SET =
            "Terrain set has not been set.";

    private static final String MAP_TYPE_HASS_NOT_BEEN_SET =
            "Map type has not been set.";

    private static final String INVALID_TERRAIN_TYPE =
            "Selected terrain type is not a type of the current terrain set.";

    private SpriteCanvas.CanvasLayerGroup layerGroup;

    private Map<TerrainType, ImageGridLayer> layers =
            new HashMap<TerrainType, ImageGridLayer>();

    private Set<ImageGridLayer> dirtyLayers = new HashSet<ImageGridLayer>();

    private Rectangle dirtyRegion = new Rectangle();
    private MapType mapType;

    private TerrainSet terrainSet;
    private Dimension segmentSizePixels = new Dimension();
    private Dimension segmentGridSize = new Dimension();
    private Dimension lastSegmentSizeTiles = new Dimension();

    private final Dimension mapTileSize =
            new Dimension(VisualizationGlobals.MAP_TILE_WIDTH,
                          VisualizationGlobals.MAP_TILE_HEIGHT);

    private Map<Short, TerrainTypesPrecedenceManager> precedenceManagers =
            new TreeMap<Short, TerrainTypesPrecedenceManager>();

    private TerrainTypesPrecedenceManager precedenceManager;

    private final TerrainTypePriorityComparator comparator =
            new TerrainTypePriorityComparator();

    /**
     * Creates a new TerrainLayers object that acts as a
     * collection of {@link CanvasLayer sprite canvas
     * layers}.
     * <p>
     * Each layer represents a single type of terrain.
     * @param canvas
     *            canvas to create layers on
     */
    public TerrainRenderer(SpriteCanvas canvas)
    {
        layerGroup = canvas.newLayerGroupAt(0);
    }

    /**
     * Creates a new TerrainLayers object that acts as a
     * collection of {@link CanvasLayer sprite canvas
     * layers}.
     * <p>
     * Each layer represents a single type of terrain.
     * <p>
     * Use this constructor when the size of a map tile is
     * available.
     * @param canvas
     *            canvas to create layers on
     * @param tileSize
     *            size of every map tile image, in pixels
     */
    @Deprecated
    public TerrainRenderer(SpriteCanvas canvas, Dimension tileSize)
    {
        this(canvas);
    }

    /**
     * Applies selected MapType attributes to terrain
     * layers, if <code>mapType</code> is not
     * <code>null</code>. Otherwise resets state of terrain
     * layers.
     * <p>
     * This method must be called before
     * {@link #applyTerrainSet(TerrainSet)}.
     * <p>
     * If the chosen map type has a not-null
     * {@link TerrainSet} already attached,
     * {@link #applyTerrainSet(TerrainSet)} set is called.
     * @param mapType
     *            map type to apply, <code>null</code> is
     *            acceptable
     */
    public final void applyMapType(MapType mapType)
    {
        layerGroup.clear();
        layers.clear();
        synchronized (dirtyLayers)
        {
            dirtyLayers.clear();
        }
        synchronized (dirtyRegion)
        {
            dirtyRegion.setBounds(0, 0, 0, 0);
        }

        this.mapType = mapType;
        this.terrainSet = null;

        if (mapType == null)
        {
            Log.logger.debug("MapType cleared.");
            return;
        }

        segmentGridSize.setSize(mapType.getXSegmentCount(),
                                mapType.getYSegmentCount());
        segmentSizePixels.setSize(mapType.getSegmentSizeX() * mapTileSize.width,
                                  mapType.getSegmentSizeY()
                                      * mapTileSize.height);

        int lastSegmentWidth = mapType.getSizeX() % mapType.getSegmentSizeX();
        if (lastSegmentWidth == 0)
            lastSegmentWidth = mapType.getSegmentSizeX();
        int lastSegmentHeight = mapType.getSizeY() % mapType.getSegmentSizeY();
        if (lastSegmentHeight == 0)
            lastSegmentHeight = mapType.getSegmentSizeY();
        lastSegmentSizeTiles.setSize(lastSegmentWidth, lastSegmentHeight);

        Log.logger.debug("MapType set.");

        final TerrainSet mapTerrainSet = mapType.getTerrainSet();
        if (mapTerrainSet != null)
            applyTerrainSet(mapTerrainSet);
    }

    /**
     * Applies selected terrain set attributes.
     * <p>
     * If a terrain set has already been applied for current
     * {@link MapType}, the cached data is cleared and build
     * again.
     * <p>
     * Must be called after at a call to
     * {@link #applyMapType(MapType)} has been made, with a
     * non-<code>null</code> {@link MapType} as a parameter,
     * and before a call to
     * {@link #rebuildTerrainImagesCache()}.
     * @param terrainSet
     *            terrain set to apply
     */
    public final void applyTerrainSet(TerrainSet terrainSet)
    {
        if (mapType == null)
            throw new IllegalStateException(MAP_TYPE_HASS_NOT_BEEN_SET);

        layerGroup.clear();
        layers.clear();
        synchronized (dirtyLayers)
        {
            dirtyLayers.clear();
        }
        synchronized (dirtyRegion)
        {
            dirtyRegion.setBounds(0, 0, 0, 0);
        }

        this.terrainSet = terrainSet;
        if (terrainSet == null)
            return;

        assert mapType.getTerrainSetId() == terrainSet.getId();

        final List<TerrainType> terrainTypes = new LinkedList<TerrainType>();
        terrainTypes.addAll(terrainSet.getTerrainTypes());
        Collections.sort(terrainTypes, comparator);

        for (TerrainType terrainType : terrainTypes)
        {
            final ImageGridLayer layer = new ImageGridLayer(layerGroup);
            layer.setGridSize(segmentGridSize.width, segmentGridSize.height);
            layer.setGridStep(segmentSizePixels.width, segmentSizePixels.height);
            layers.put(terrainType, layer);
            dirtyLayers.add(layer);
        }

        if (!precedenceManagers.containsKey(terrainSet.getId()))
        {
            precedenceManager = new TerrainTypesPrecedenceManager(terrainSet);
            precedenceManagers.put(terrainSet.getId(), precedenceManager);
        }
        else
            precedenceManager = precedenceManagers.get(terrainSet.getId());

        Log.logger.debug("TerrainSet applied.");
    }

    private void cacheLayer(TerrainType terrainType)
    {
        if (mapType == null)
            throw new IllegalStateException(MAP_TYPE_HASS_NOT_BEEN_SET);
        if (terrainSet == null)
            throw new IllegalStateException(TERRAIN_SET_HAS_NOT_BEEN_SET);

        final ImageGridLayer layer = layers.get(terrainType);

        if (layer == null)
            throw new IllegalArgumentException(INVALID_TERRAIN_TYPE);

        final boolean layerDirty;
        synchronized (dirtyLayers)
        {
            layerDirty = dirtyLayers.contains(layer);
        }
        final boolean dirtyRegionEmpty;
        synchronized (dirtyRegion)
        {
            dirtyRegionEmpty = dirtyRegion.isEmpty();
        }
        if (dirtyRegionEmpty && !layerDirty)
            return; //Already rendered

        final int startRow;
        final int endRow;
        final int startColumn;
        final int endColumn;

        if (!layerDirty)
        {
            startRow = dirtyRegion.y;
            endRow =
                    Math.min(dirtyRegion.y + dirtyRegion.height,
                             segmentGridSize.height);
            startColumn = dirtyRegion.x;
            endColumn =
                    Math.min(dirtyRegion.x + dirtyRegion.width,
                             segmentGridSize.width);
        }
        else
        {
            startRow = 0;
            endRow = segmentGridSize.height;
            startColumn = 0;
            endColumn = segmentGridSize.width;
        }
        boolean layerCached = true;
        for (int row = startRow; row < endRow; ++row)
        {
            for (int column = startColumn; column < endColumn; ++column)
            {
                layerCached &= cacheSegment(terrainType, layer, column, row);
            }
        }

        if (layerCached)
            synchronized (dirtyLayers)
            {
                dirtyLayers.remove(layer);
            }

    }

    private boolean cacheSegment(TerrainType terrainType,
                                 ImageGridLayer layer,
                                 int column,
                                 int row)
    {
        final ImageDB imageDB = ImageDB.globalInstance();

        final int segmentHeight =
                row == segmentGridSize.height - 1 ? lastSegmentSizeTiles.height
                                                 : mapType.getSegmentSizeY();

        final int segmentWidth =
                column == segmentGridSize.width - 1 ? lastSegmentSizeTiles.width
                                                   : mapType.getSegmentSizeX();

        Graphics2D graphics = null; //Graphics is created only when needed

        boolean segmentCached = true;

        for (short tilesRow = 0; tilesRow < segmentHeight; ++tilesRow)
        {
            for (short tilesColumn = 0; tilesColumn < segmentWidth; ++tilesColumn)
            {
                Position position = new Position();
                position.setX((short) (column * mapType.getSegmentSizeX() + tilesColumn));
                position.setY((short) (row * mapType.getSegmentSizeY() + tilesRow));

                List<TerrainTypeImageId> imageIds =
                        precedenceManager.getTerrainTypeImageIdsQueue(mapType,
                                                                      position,
                                                                      terrainType);
                if (graphics == null)
                {
                    graphics =
                            getSegmentImage(layer, column, row).createGraphics();
                    graphics.setBackground(ERASE_COLOR);
                }

                boolean needsErasing = true;
                for (TerrainTypeImageId terrainTypeImageId : imageIds)
                {
                    if (terrainTypeImageId.representsTile())
                        needsErasing = false;
                }
                if (needsErasing)
                    graphics.clearRect(tilesColumn * mapTileSize.width,
                                       tilesRow * mapTileSize.height,
                                       mapTileSize.width,
                                       mapTileSize.height);

                for (TerrainTypeImageId terrainTypeImageId : imageIds)
                {
                    ImageProxy proxy =
                            imageDB.getTerrainImage(terrainTypeImageId);

                    if (proxy.getState() == ImageProxy.LoadingState.LOADED)
                    {
                        //Draw the image if available
                        graphics.drawImage(proxy.getImage(), null, tilesColumn
                            * mapTileSize.width, tilesRow * mapTileSize.height);
                        //Segment counts as cached
                    }
                    else
                    {
                        if (proxy.getState() == LoadingState.LOADING)
                            segmentCached = false;
                        else
                            ;//Segment counts as cached (image is not available and never will)

                        //Draw a colored rectangle if image unavailable and this is a tile imageid
                        if (terrainTypeImageId.representsTile()
                            && terrainType.getColor() != null)
                        {
                            graphics.setColor(terrainType.getColor());
                            graphics.fillRect(tilesColumn * mapTileSize.width,
                                              tilesRow * mapTileSize.height,
                                              mapTileSize.width,
                                              mapTileSize.height);
                        }

                    }
                }
            }
        }
        if (graphics != null)
            graphics.dispose();

        return segmentCached;
    }

    /**
     * Centers the canvas viewport at the given background
     * tile.
     * @param position
     *            position on map
     */
    public void centerViewportAtPosition(Position position)
    {
        layerGroup.getCanvas().getViewport().centerAt(position.getX()
                                                          * mapTileSize.width
                                                          + mapTileSize.width
                                                          / 2,
                                                      position.getY()
                                                          * mapTileSize.height
                                                          + mapTileSize.height
                                                          / 2);
    }

    /** {@inheritDoc} */
    @Override
    public void clear()
    {
        applyMapType(null);
    }

    /**
     * Returns a position on map that contains the specified
     * location.
     * @param location
     *            point, in pixels, in canvas coordinates
     * @return position, representing a terrain grid cell,
     *         or <code>null</code> if the selected location
     *         is outside the map
     */
    public Position getMapTileAt(Point location)
    {
        if (mapType == null)
            return null;
        if (location.x < 0 || location.y < 0
            || location.x >= mapType.getSizeX() * mapTileSize.getWidth()
            || location.y >= mapType.getSizeY() * mapTileSize.getHeight())
            return null;
        return new Position((short) (location.x / mapTileSize.width),
                            (short) (location.y / mapTileSize.height));
    }

    /**
     * Returns the location in world coordinates of the
     * center of the selected tile if the tile is on map.
     * @param tilePosition
     *            position on map, representing a terrain
     *            grid cell
     * @return point, in pixels, in canvas coordinates, or
     *         <code>null</code> if the selected position is
     *         outside the map
     */
    public Point getMapTileCenter(Position tilePosition)
    {
        if (mapType == null || tilePosition.getX() < 0
            || tilePosition.getY() < 0
            || tilePosition.getX() >= mapType.getSizeX()
            || tilePosition.getY() >= mapType.getSizeY())
            return null;
        return new Point(tilePosition.getX() * mapTileSize.width
            + mapTileSize.width / 2, tilePosition.getY() * mapTileSize.height
            + mapTileSize.height / 2);
    }

    /**
     * Returns applied map type, if any.
     * @return map type applied, or <code>null</code>
     */
    public final MapType getMapType()
    {
        return mapType;
    }

    private BufferedImage getSegmentImage(ImageGridLayer layer,
                                          int column,
                                          int row)
    {
        BufferedImage segmentImage = layer.getImageAt(column, row);
        if (segmentImage == null)
        {
            segmentImage =
                    layerGroup.getCanvas()
                              .getGraphicsConfiguration()
                              .createCompatibleImage(segmentSizePixels.width,
                                                     segmentSizePixels.height,
                                                     Transparency.TRANSLUCENT);
            layer.setImageAt(segmentImage, column, row);
        }
        return segmentImage;
    }

    /**
     * Checks if a non-<code>null</code> map type has been
     * applied.
     * @return true, if the last {@link MapType} applied was
     *         not <code>null</code>.
     * @see #applyMapType(MapType)
     */
    public final boolean isMapTypeApplied()
    {
        return mapType != null;
    }

    /**
     * Checks if there are no terrain layers left that need
     * applying terrain images.
     * @return <code>true</code> if all terrain layers have
     *         their terrain images applied or have the
     *         terrain images are unavailable,
     *         <code>false</code> otherwise
     * @see #rebuildTerrainImagesCache()
     */
    public final boolean isTerrainCacheRebuildNeeded()
    {
        boolean dirty = false;
        synchronized (dirtyLayers)
        {
            dirty |= !dirtyLayers.isEmpty();
        }
        synchronized (dirtyRegion)
        {
            dirty |= !dirtyRegion.isEmpty();
        }
        return dirty;
    }

    /**
     * Checks if a non-<code>null</code> terrain set has
     * been applied.
     * @return true, if the last {@link TerrainSet} applied
     *         was not <code>null</code>.
     * @see #applyTerrainSet(TerrainSet)
     */
    public final boolean isTerrainSetApplied()
    {
        return terrainSet != null;
    }

    /**
     * Causes refreshing of cached terrain images for a
     * rectangular region spanning between two given
     * positions in map coordinates (inclusive).
     * <p>
     * Affects also a one-tile border around the region, as
     * the tile borders might be affected.
     * <p>
     * If a dirty region was specified previously, the
     * bounding rectangle of the old and new region is
     * stored.
     * <p>
     * The cached terrain images will be redrawn before next
     * rendering, but not necessarily immediately.
     * @param from
     *            one of the corners of the affected region
     * @param to
     *            the opposite corner of the affected region
     */
    public void mapRegionChanged(Position from, Position to)
    {
        int minX;
        int minY;
        int maxX;
        int maxY;

        if (from.getX() > to.getX())
        {
            minX = to.getX();
            maxX = from.getX();
        }
        else
        {
            minX = from.getX();
            maxX = to.getX();
        }

        if (from.getY() > to.getY())
        {
            minY = to.getY();
            maxY = from.getY();
        }
        else
        {
            minY = from.getY();
            maxY = to.getY();
        }

        minX = Math.max(--minX, 0) / mapType.getSegmentSizeX();
        minY = Math.max(--minY, 0) / mapType.getSegmentSizeY();
        maxX = Math.min(++maxX, mapType.getSizeX()) / mapType.getSegmentSizeX();
        maxY = Math.min(++maxY, mapType.getSizeY()) / mapType.getSegmentSizeY();

        synchronized (dirtyRegion)
        {

            dirtyRegion.setBounds(dirtyRegion.union(new Rectangle(minX,
                                                                  minY,
                                                                  maxX - minX
                                                                      + 1,
                                                                  maxY - minY
                                                                      + 1)));
        }
    }

    /**
     * Applies terrain for all terrain types in current
     * terrain set.
     * <p>
     * All the terrain layers marked as dirty and a
     * rectangular region, if specified, are cached again.
     * All the other cached images are left as they are.
     * <p>
     * A terrain layer is considered dirty also if the last
     * time the cache rebuild took place, the terrain image
     * was in {@link LoadingState#LOADING} state.
     * <p>
     * Clears map region and terrain types marked as dirty.
     * @see #terrainTypeChanged(TerrainType)
     * @see #mapRegionChanged(Position, Position)
     */
    public final void rebuildTerrainImagesCache()
    {
        //For each terrain type (thus layer)
        for (TerrainType terrainType : layers.keySet())
        {
            cacheLayer(terrainType);
        }

        synchronized (dirtyRegion)
        {
            dirtyRegion.setBounds(0, 0, 0, 0);
        }
    }

    /**
     * Causes refreshing of cached terrain images for
     * selected terrain type.
     * <p>
     * Use when only image for terrain type changes. If the
     * priority changes, the whole terrain set must be
     * applied again.
     * <p>
     * The cached terrain images will be redrawn before next
     * rendering, but not necessarily immediately.
     * @param type
     *            terrain type that changed
     */
    public void terrainTypeChanged(TerrainType type)
    {
        synchronized (dirtyLayers)
        {
            dirtyLayers.add(layers.get(type));
        }
    }
}
