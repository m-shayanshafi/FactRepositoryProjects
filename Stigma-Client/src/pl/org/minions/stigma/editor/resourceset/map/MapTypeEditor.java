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

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JScrollPane;

import pl.org.minions.stigma.editor.command.Executor;
import pl.org.minions.stigma.editor.resourceset.ResourceEditor;
import pl.org.minions.stigma.editor.resourceset.ResourceEditorOutline;
import pl.org.minions.stigma.editor.resourceset.ResourceSetDocument;
import pl.org.minions.stigma.editor.resourceset.map.command.ChangeTilesCollectionCommand;
import pl.org.minions.stigma.editor.resourceset.map.command.ChangeZonePositionsCollectionCommand;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.stigma.game.map.TileType;
import pl.org.minions.stigma.game.map.Zone;
import pl.org.minions.stigma.globals.Position;

/**
 * Main map editor.
 */
public class MapTypeEditor extends ResourceEditor<MapType> implements
                                                          TileSelectionListener,
                                                          ZoneSelectionListener
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private MapTypeCanvas canvas;
    private ResourceSetDocument<MapType> mapTypeDocument;

    private MapTypeInfoOutline mapTypeInfoOutline;
    private MapTypeZonesOutline mapTypeZonesOutline;
    private TerrainPalette terrainPalette;

    private enum EditionMode
    {
        TILES, ENTRIES, EXITS
    };

    private EditionMode currentEditionMode = EditionMode.TILES;

    //private TerrainPalette terrainPaletteOutline;

    private Executor<MapType> executor;

    /**
     * Constructor.
     */
    public MapTypeEditor()
    {
        super();
        this.setLayout(new BorderLayout());
        //        terrainPaletteOutline = new TerrainPalette();
        mapTypeInfoOutline = new MapTypeInfoOutline();
        mapTypeZonesOutline = new MapTypeZonesOutline();
        mapTypeZonesOutline.addZoneSelectionListener(this);

        terrainPalette = new TerrainPalette();
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSetDocument<MapType> getDocument()
    {
        return mapTypeDocument;
    }

    /** {@inheritDoc} */
    @Override
    public String getName()
    {
        // TODO Nls
        return "Map editor";
    }

    /** {@inheritDoc} */
    @Override
    public List<? extends ResourceEditorOutline> getOutlines()
    {
        List<ResourceEditorOutline> result =
                new ArrayList<ResourceEditorOutline>();
        result.add(terrainPalette);
        result.add(mapTypeInfoOutline);
        result.add(mapTypeZonesOutline);
        //result.add(mapTypeExitZonesOutline);

        return result;
    }

    /** {@inheritDoc} */
    @Override
    public MapType getResource()
    {
        return mapTypeDocument.getResource();
    }

    /** {@inheritDoc} */
    @Override
    public void init(ResourceSetDocument<MapType> document)
    {

        this.mapTypeDocument = document;

        executor = new Executor<MapType>(mapTypeDocument.getResource());

        canvas = new MapTypeCanvas(mapTypeDocument.getResource());
        canvas.addTileSelectionListener(this);

        this.add(new JScrollPane(canvas));

        mapTypeInfoOutline.init(mapTypeDocument);
        mapTypeZonesOutline.init(mapTypeDocument);
        terrainPalette.init(mapTypeDocument.getResource().getTerrainSet());
        //        terrainPaletteOutline.init(mapTypeDocument.getResource()
        //                                                  .getTerrainSet());
    }

    /** {@inheritDoc} */
    @Override
    public void tileHighlighted(int x, int y)
    {
    }

    /** {@inheritDoc} */
    @Override
    public void tilesSelected(Position firstPosition, Set<Position> positions)
    {
        if (currentEditionMode == EditionMode.TILES)
        {
            paintTile(positions);
        }
        else if (currentEditionMode == EditionMode.ENTRIES)
        {
            paintEntryZone(firstPosition, positions);
        }

    }

    private void paintTile(Set<Position> positions)
    {
        if (terrainPalette.getSelectedTile() == null)
        {
            return;
        }
        short minX = Short.MAX_VALUE;
        short maxX = Short.MIN_VALUE;
        short minY = Short.MAX_VALUE;
        short maxY = Short.MIN_VALUE;

        Map<Position, TileType> changedTiles =
                new HashMap<Position, TileType>();
        for (Position p : positions)
        {
            changedTiles.put(p, terrainPalette.getSelectedTile());
            minX = (short) Math.min(minX, p.getX());
            maxX = (short) Math.max(maxX, p.getX());
            minY = (short) Math.min(minY, p.getY());
            maxY = (short) Math.max(maxY, p.getY());
        }

        ChangeTilesCollectionCommand changeTilesCollectionCommand =
                new ChangeTilesCollectionCommand(changedTiles);
        executor.execute(changeTilesCollectionCommand);

        canvas.mapRegionChanged(new Position(minX, minY), new Position(maxX,
                                                                       maxY));
    }

    private void paintEntryZone(Position firstPosition, Set<Position> positions)
    {
        for (Position position : positions)
        {

            Zone existingZoneOnThisPosition =
                    mapTypeZonesOutline.getZoneForPosition(position);
            Zone selectedZone = mapTypeZonesOutline.getSelectedZone();

            if (existingZoneOnThisPosition != null
                && existingZoneOnThisPosition != selectedZone)
            {
                mapTypeZonesOutline.selectZone(existingZoneOnThisPosition);
                return;
            }

            if (selectedZone == null)
            {
                return;
            }
        }
        ChangeZonePositionsCollectionCommand changeZonePositionCollectionCommand =
                new ChangeZonePositionsCollectionCommand(mapTypeZonesOutline.getSelectedZone(),
                                                         firstPosition,
                                                         positions);
        executor.execute(changeZonePositionCollectionCommand);

        //canvas.mapRegionChanged(position, position);
    }

    /** {@inheritDoc} */
    @Override
    public void outlineSelected(ResourceEditorOutline outline)
    {
        if (outline.equals(mapTypeZonesOutline))
        {
            currentEditionMode = EditionMode.ENTRIES;
            canvas.setShowEntryZones(true);
            canvas.setShowExitZones(true);
        }
        else
        {
            currentEditionMode = EditionMode.TILES;
            canvas.setShowEntryZones(false);
            canvas.setShowExitZones(false);
        }
        canvas.repaint();
    }

    /** {@inheritDoc} */
    @Override
    public void zoneSelected(Zone zone)
    {
        canvas.setSelectedZone(zone);
    }
}
