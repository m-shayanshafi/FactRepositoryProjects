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
package pl.org.minions.stigma.editor.resourceset.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import pl.org.minions.stigma.client.ui.VisualizationGlobals;
import pl.org.minions.stigma.client.ui.sprite.TerrainRenderer;
import pl.org.minions.stigma.editor.gui.GUIConstants;
import pl.org.minions.stigma.game.map.EntryZone;
import pl.org.minions.stigma.game.map.ExitZone;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.stigma.game.map.TerrainType;
import pl.org.minions.stigma.game.map.Zone;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.utils.ui.sprite.SpriteCanvas;

/**
 * Canvas displaying the Map Type object.
 */
public class MapTypeCanvas extends JPanel implements SelectionTool
{

    private static final long serialVersionUID = 1L;

    private SpriteCanvas canvas;
    private TerrainRenderer terrainRenderer;
    private MapType mapType;

    private List<TileSelectionListener> tileSelectionListeners;

    private boolean showEntryZones;
    private boolean showExitZones;

    private Zone selectedZone;

    private SelectionTool currentSelectionTool;

    private Position currentMousePosition;

    private boolean isSelecting;

    private Position startPosition;

    private Position endPosition;

    /**
     * Creates a new canvas that displays a rendered map
     * terrain just as it would be displayed in game client.
     * @param mapType
     *            map type to render
     */
    public MapTypeCanvas(MapType mapType)
    {
        super();
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(mapType.getSizeX()
            * VisualizationGlobals.MAP_TILE_WIDTH, mapType.getSizeY()
            * VisualizationGlobals.MAP_TILE_HEIGHT));
        canvas = new SpriteCanvas();
        this.mapType = mapType;

        this.currentSelectionTool = this;
        this.currentMousePosition = new Position((short) -1, (short) -1);
        this.tileSelectionListeners = new LinkedList<TileSelectionListener>();

        this.addMouseMotionListener(new MouseAdapter()
        {
            @Override
            public void mouseMoved(MouseEvent event)
            {
                //  System.out.println("mouseMoved");
                Position newMousePosition = getMousePosition(event);
                if (!currentMousePosition.equals(newMousePosition))
                {
                    currentMousePosition = newMousePosition;
                    MapTypeCanvas.this.repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent event)
            {
                //  System.out.println("mouseDragged");
                Position newMousePosition = getMousePosition(event);
                if (!currentMousePosition.equals(newMousePosition))
                {
                    currentMousePosition = newMousePosition;

                    currentSelectionTool.selectionChange(newMousePosition);

                    MapTypeCanvas.this.repaint();
                }
            }

        });

        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent event)
            {
                //                System.out.println("mousePressed");
                Position newMousePosition = getMousePosition(event);

                currentMousePosition = newMousePosition;
                currentSelectionTool.selectionStart(newMousePosition);
                MapTypeCanvas.this.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent event)
            {
                //   System.out.println("mouseReleased");
                Position newMousePosition = getMousePosition(event);
                currentMousePosition = newMousePosition;

                currentSelectionTool.selectionEnd(newMousePosition);

                MapTypeCanvas.this.repaint();
            }
        });
    }

    /**
     * Causes the recalculation of map terrain images for
     * the whole map.
     * <p>
     * Call when one of the attributes of the map type has
     * changed.
     * <p>
     * Causes repaint.
     */
    public void mapChanged()
    {
        terrainRenderer.applyMapType(mapType);
        repaint();
    }

    /**
     * Causes the recalculation of map terrain images for
     * given region during the next repaint.
     * <p>
     * If a non-empty region has been set before, the
     * bounding rectangle of the two regions is used.
     * <p>
     * Causes repaint.
     * @param from
     *            one of the corners of the rectangular
     *            region (inclusive)
     * @param to
     *            the other corner of the rectangular region
     *            (inclusive)
     * @see pl.org.minions.stigma.client.ui.sprite.TerrainRenderer#mapRegionChanged(pl.org.minions.stigma.globals.Position,
     *      pl.org.minions.stigma.globals.Position)
     */
    public void mapRegionChanged(Position from, Position to)
    {
        terrainRenderer.mapRegionChanged(from, to);
        repaint(); //TODO: smart repaint can be made here
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (canvas.getGraphicsConfiguration() == null)
        {
            canvas.setGraphicsConfiguration(this.getGraphicsConfiguration());
            canvas.setBackgroundColor(Color.red);
            canvas.getViewport().setBounds(new Rectangle(mapType.getSizeX()
                * VisualizationGlobals.MAP_TILE_WIDTH, mapType.getSizeY()
                * VisualizationGlobals.MAP_TILE_HEIGHT));

        }
        if (terrainRenderer == null)
        {
            terrainRenderer = new TerrainRenderer(canvas);
            terrainRenderer.applyMapType(mapType);
        }

        if (terrainRenderer.isTerrainCacheRebuildNeeded())
            terrainRenderer.rebuildTerrainImagesCache();

        canvas.render((Graphics2D) g);

        if (terrainRenderer.isTerrainCacheRebuildNeeded())
        {
            repaint();
        }

        renderZones(g);
        renderSelection(g);
    }

    private void renderSelection(Graphics g)
    {
        int mouseTileX = -1;
        int mouseTileY = -1;
        int selectionBoxSizeX = 1;
        int selectionBoxSizeY = 1;

        if (isSelecting)
        {
            mouseTileX = Math.min(startPosition.getX(), endPosition.getX());
            mouseTileY = Math.min(startPosition.getY(), endPosition.getY());
            selectionBoxSizeX =
                    Math.abs(endPosition.getX() - startPosition.getX()) + 1;
            selectionBoxSizeY =
                    Math.abs(endPosition.getY() - startPosition.getY()) + 1;
        }
        else
        {
            mouseTileX = currentMousePosition.getX();
            mouseTileY = currentMousePosition.getY();
        }

        g.setColor(Color.white);
        g.drawRect(mouseTileX * VisualizationGlobals.MAP_TILE_WIDTH,
                   mouseTileY * VisualizationGlobals.MAP_TILE_HEIGHT,
                   VisualizationGlobals.MAP_TILE_WIDTH * selectionBoxSizeX,
                   VisualizationGlobals.MAP_TILE_HEIGHT * selectionBoxSizeY);

        g.drawRect(mouseTileX * VisualizationGlobals.MAP_TILE_WIDTH + 2,
                   mouseTileY * VisualizationGlobals.MAP_TILE_HEIGHT + 2,
                   VisualizationGlobals.MAP_TILE_WIDTH - 2 * 2
                       + VisualizationGlobals.MAP_TILE_WIDTH
                       * (selectionBoxSizeX - 1),
                   VisualizationGlobals.MAP_TILE_HEIGHT - 2 * 2
                       + VisualizationGlobals.MAP_TILE_HEIGHT
                       * (selectionBoxSizeY - 1));
    }

    private void renderZones(Graphics g)
    {

        if (showEntryZones)
        {

            for (EntryZone entryZone : mapType.getEntryZoneMap().values())
            {
                renderZone(g,
                           entryZone,
                           GUIConstants.ENTRY_ZONE_COLOR,
                           GUIConstants.SELECTED_ENTRY_ZONE_COLOR);
            }
        }

        if (showExitZones)
        {
            for (ExitZone exitZone : mapType.getExitZoneMap().values())
            {
                renderZone(g,
                           exitZone,
                           GUIConstants.EXIT_ZONE_COLOR,
                           GUIConstants.SELECTED_EXIT_ZONE_COLOR);
            }
        }

    }

    private void renderZone(Graphics g,
                            Zone zone,
                            Color color,
                            Color selectedColor)
    {
        boolean selected = selectedZone != null && selectedZone.equals(zone);
        for (Position p : zone.getPositionsList())
        {
            if (selected)
            {
                g.setColor(selectedColor);
            }
            else
            {
                g.setColor(color);
            }

            g.fillRect(p.getX() * VisualizationGlobals.MAP_TILE_WIDTH,
                       p.getY() * VisualizationGlobals.MAP_TILE_HEIGHT,
                       VisualizationGlobals.MAP_TILE_WIDTH,
                       VisualizationGlobals.MAP_TILE_HEIGHT);

            if (selected)
            {
                g.setColor(selectedColor.brighter());
                g.drawRect(p.getX() * VisualizationGlobals.MAP_TILE_WIDTH,
                           p.getY() * VisualizationGlobals.MAP_TILE_HEIGHT,
                           VisualizationGlobals.MAP_TILE_WIDTH,
                           VisualizationGlobals.MAP_TILE_HEIGHT);
            }

        }
    }

    /**
     * Causes the recalculation of map terrain images for
     * the whole map.
     * <p>
     * Call when terrain types changed priorities, or a
     * different terrain set has been chosen for the map.
     * <p>
     * Causes repaint.
     */
    public void terrainSetChanged()
    {
        terrainRenderer.applyTerrainSet(mapType.getTerrainSet());
        repaint();
    }

    /**
     * Causes the recalculation of map terrain images for
     * selected terrain type.
     * <p>
     * Call when a terrain type in the terrain set
     * associated with this map has changed (for example,
     * terrain image file was changed).
     * <p>
     * Change of terrain type priority requires calling
     * {@link #terrainSetChanged()} instead.
     * <p>
     * Causes repaint.
     * @param type
     *            terrain type that changed
     * @see pl.org.minions.stigma.client.ui.sprite.TerrainRenderer#terrainTypeChanged(pl.org.minions.stigma.game.map.TerrainType)
     */
    public void terrainTypeChanged(TerrainType type)
    {
        terrainRenderer.terrainTypeChanged(type);
        repaint();
    }

    private Position getMousePosition(MouseEvent event)
    {
        short newMouseTileX =
                (short) (event.getX() / VisualizationGlobals.MAP_TILE_WIDTH);
        short newMouseTileY =
                (short) (event.getY() / VisualizationGlobals.MAP_TILE_HEIGHT);

        if (newMouseTileX < 0 || newMouseTileX >= mapType.getSizeX()
            || newMouseTileY < 0 || newMouseTileY >= mapType.getSizeY())
        {
            return new Position((short) -1, (short) -1);
        }

        return new Position(newMouseTileX, newMouseTileY);
    }

    //    private boolean updateMouseTiles(MouseEvent event, boolean pressed)
    //    {
    //        int newMouseTileX = event.getX() / VisualizationGlobals.MAP_TILE_WIDTH;
    //        int newMouseTileY = event.getY() / VisualizationGlobals.MAP_TILE_HEIGHT;
    //
    //        if (newMouseTileX < 0 || newMouseTileX >= mapType.getSizeX()
    //            || newMouseTileY < 0 || newMouseTileY >= mapType.getSizeY())
    //        {
    //            mouseTileX = -1;
    //            mouseTileY = -1;
    //            return false;
    //        }
    //
    //        if (mouseTileX != newMouseTileX || mouseTileY != newMouseTileY)
    //        {
    //            for (TileSelectionListener tileSelectionListener : tileSelectionListeners)
    //            {
    //                tileSelectionListener.tileHighlighted(newMouseTileX,
    //                                                      newMouseTileY);
    //            }
    //        }
    //
    //        if (event.getButton() != MouseEvent.NOBUTTON || pressed
    //            && (clickedTileX != newMouseTileX || clickedTileY != newMouseTileY))
    //        {
    //            clickedTileX = newMouseTileX;
    //            clickedTileY = newMouseTileY;
    //
    //            for (TileSelectionListener tileSelectionListener : tileSelectionListeners)
    //            {
    //                tileSelectionListener.tileSelected(newMouseTileX, newMouseTileY);
    //            }
    //        }
    //
    //        mouseTileX = newMouseTileX;
    //        mouseTileY = newMouseTileY;
    //
    //        return true;
    //    }

    /**
     * Registers a tileSelectionListener.
     * @param tileSelectionListener
     *            tile selection listener
     */
    public void addTileSelectionListener(TileSelectionListener tileSelectionListener)
    {
        tileSelectionListeners.add(tileSelectionListener);
    }

    /**
     * Returns showEntryZones.
     * @return showEntryZones
     */
    public boolean isShowEntryZones()
    {
        return showEntryZones;
    }

    /**
     * Sets new value of showEntryZones.
     * @param showEntryZones
     *            the showEntryZones to set
     */
    public void setShowEntryZones(boolean showEntryZones)
    {
        this.showEntryZones = showEntryZones;
    }

    /**
     * Returns showExitZones.
     * @return showExitZones
     */
    public boolean isShowExitZones()
    {
        return showExitZones;
    }

    /**
     * Sets new value of showExitZones.
     * @param showExitZones
     *            the showExitZones to set
     */
    public void setShowExitZones(boolean showExitZones)
    {
        this.showExitZones = showExitZones;
    }

    /**
     * Returns selectedZone.
     * @return selectedZone
     */
    public Zone getSelectedZone()
    {
        return selectedZone;
    }

    /**
     * Sets new value of selectedEntryZone.
     * @param selectedZone
     *            the selectedZone to set
     */
    public void setSelectedZone(Zone selectedZone)
    {
        this.selectedZone = selectedZone;
        this.repaint();
    }

    private Set<Position> getSelectedTiles()
    {
        Set<Position> result = new HashSet<Position>();
        short startX =
                (short) Math.min(startPosition.getX(), endPosition.getX());
        short endX = (short) Math.max(startPosition.getX(), endPosition.getX());
        short startY =
                (short) Math.min(startPosition.getY(), endPosition.getY());
        short endY = (short) Math.max(startPosition.getY(), endPosition.getY());

        for (short x = startX; x <= endX; x++)
        {
            for (short y = startY; y <= endY; y++)
            {
                result.add(new Position(x, y));
            }
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public void selectionChange(Position position)
    {
        endPosition = position;
    }

    /** {@inheritDoc} */
    @Override
    public void selectionEnd(Position position)
    {

        endPosition = position;
        isSelecting = false;
        Set<Position> positions = getSelectedTiles();
        for (TileSelectionListener tileSelectionListener : tileSelectionListeners)
        {
            tileSelectionListener.tilesSelected(startPosition, positions);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void selectionStart(Position position)
    {

        startPosition = position;
        isSelecting = true;
        endPosition = position;
    }

}
