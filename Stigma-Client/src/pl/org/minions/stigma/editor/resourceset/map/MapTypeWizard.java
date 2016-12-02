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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pl.org.minions.stigma.editor.resourceset.NewElementWizard;
import pl.org.minions.stigma.editor.resourceset.ResourceSetModel;
import pl.org.minions.stigma.editor.resourceset.ValidationResult;
import pl.org.minions.stigma.editor.resourceset.Validator;
import pl.org.minions.stigma.editor.resourceset.ValidationResult.ResultType;
import pl.org.minions.stigma.game.map.EntryZone;
import pl.org.minions.stigma.game.map.ExitZone;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.stigma.game.map.TerrainSet;
import pl.org.minions.stigma.game.map.TerrainType;
import pl.org.minions.stigma.game.map.TileType;
import pl.org.minions.utils.i18n.Translated;

/**
 * Wizard component used to create a new map type object.
 */
public class MapTypeWizard extends NewElementWizard implements ChangeListener
{

    public static final int MAP_TYPE_DEFAULT_SIZE_X = 15;
    public static final int MAP_TYPE_DEFAULT_SIZE_Y = 15;

    public static final int MAP_TYPE_DEFAULT_SEGMENT_SIZE_X = 15;
    public static final int MAP_TYPE_DEFAULT_SEGMENT_SIZE_Y = 15;

    public static final int MAP_TYPE_DEFAULT_MAX_ACTORS = 15;

    @Translated
    private static String WIZARD_LABEL = "New map";
    @Translated
    private static String WIZARD_DESCRIPTION = "Create new map";

    @Translated
    private static String NEW_MAP_NAME = "MyMap_";

    @Translated
    private static String MAP_NAME_IS_EMPTY = "Map name is empty.";

    @Translated
    private static String DISCOURAGED_NAME = "Map";

    @Translated
    private static String MAP_NAME_DISCOURAGED =
            "Map name is discouraged.\n By convention, Stigma map names\n should sound awesome";

    @Translated
    private static String MAP_NAME_ALREADY_IN_USE = "Map name already in use.";

    private static final long serialVersionUID = 1L;

    private MapTypePropertiesPanel mapTypePropertiesPanel;

    private MapType mapType;

    /**
     * Constructor.
     */
    public MapTypeWizard()
    {
        super();
        mapTypePropertiesPanel = new MapTypePropertiesPanel();
        this.add(mapTypePropertiesPanel);

        mapTypePropertiesPanel.addChangeListener(this);

        this.addValidator(new MapNameEmptynessValidator());
        this.addValidator(new MapNameUniquenessValidator());
        this.addValidator(new MapNameLazynessValidator());
        init();
    }

    /** {@inheritDoc} */
    @Override
    public String getName()
    {
        return WIZARD_LABEL;
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription()
    {
        return WIZARD_DESCRIPTION;
    }

    /**
     * Initializes this wizard.
     */
    public void init()
    {
        short mapId = 0;
        short terrainSetId =
                ResourceSetModel.getInstance()
                                .getResourceSet()
                                .getTerrainSets()
                                .iterator()
                                .next()
                                .getId();

        short defaultSizeX = MAP_TYPE_DEFAULT_SIZE_X;
        short defaultSizeY = MAP_TYPE_DEFAULT_SIZE_Y;

        byte defaultSegmentSizeX = MAP_TYPE_DEFAULT_SEGMENT_SIZE_X;
        byte defaultSegmentSizeY = MAP_TYPE_DEFAULT_SEGMENT_SIZE_Y;

        Collection<String> usedMapTypeNames =
                ResourceSetModel.getInstance()
                                .getResourceSet()
                                .getUsedMapTypeNames();

        int newMapSequence = 0;
        String defaultName = "";
        do
        {
            defaultName = NEW_MAP_NAME + newMapSequence++;
        } while (usedMapTypeNames.contains(defaultName));

        String defaultDescription = "";

        short defaultMaxActors = MAP_TYPE_DEFAULT_MAX_ACTORS;
        byte defaultSafeEntryId = -1;

        mapType =
                new MapType(mapId,
                            terrainSetId,
                            defaultSizeX,
                            defaultSizeY,
                            defaultSegmentSizeX,
                            defaultSegmentSizeY,
                            new LinkedList<Short>(),
                            new LinkedList<EntryZone>(),
                            new LinkedList<ExitZone>(),
                            defaultName,
                            defaultDescription,
                            defaultMaxActors,
                            defaultSafeEntryId);

        mapTypePropertiesPanel.init(mapType);
    }

    /** {@inheritDoc} */
    @Override
    public Object saveNewElement()
    {
        if (mapType != null)
        {
            fillMapTypeWithLowestPriorityTiles(mapType);

            ResourceSetModel.getInstance().getResourceSet().addMap(mapType);
            return true;
        }
        return null;
    }

    private void fillMapTypeWithLowestPriorityTiles(MapType mapType)
    {
        TerrainSet terrainSet =
                ResourceSetModel.getInstance()
                                .getResourceSet()
                                .getTerrainSet(mapType.getTerrainSetId());

        TileType defaultTileType =
                getLowestPriorityTerrainType(terrainSet).getTileMap()
                                                        .values()
                                                        .iterator()
                                                        .next();

        List<Short> initList = mapType.getTilesList();
        initList.clear();

        for (int x = 0; x < mapType.getSizeX(); x++)
        {
            for (int y = 0; y < mapType.getSizeY(); y++)
            {
                initList.add((short) defaultTileType.getTerrainType().getId());
                initList.add((short) defaultTileType.getId());
            }
        }

        mapType.assignTerrainSet(terrainSet);
    }

    private TerrainType getLowestPriorityTerrainType(TerrainSet terrainSet)
    {
        TerrainType lowestPriorityTerrainType = null;
        int lowestPriority = Integer.MAX_VALUE;
        for (TerrainType terrainType : terrainSet.getTerrainTypes())
        {
            if (terrainType.getPriority() < lowestPriority)
            {
                lowestPriority = terrainType.getPriority();
                lowestPriorityTerrainType = terrainType;
            }
        }

        return lowestPriorityTerrainType;
    }

    private class MapNameEmptynessValidator implements Validator
    {

        /** {@inheritDoc} */
        @Override
        public ValidationResult validate()
        {
            String mapName =
                    MapTypeWizard.this.mapTypePropertiesPanel.getMapTypeFieldName();

            if (mapName == null || mapName.isEmpty())
            {
                return new ValidationResult(MAP_NAME_IS_EMPTY,
                                            ResultType.ERROR,
                                            ValidationResult.HIGH);
            }
            return null;
        }
    }

    private class MapNameLazynessValidator implements Validator
    {

        /** {@inheritDoc} */
        @Override
        public ValidationResult validate()
        {
            String mapName =
                    MapTypeWizard.this.mapTypePropertiesPanel.getMapTypeFieldName();

            if (mapName != null
                && (mapName.startsWith(NEW_MAP_NAME) || mapName.contains(DISCOURAGED_NAME)))
            {
                return new ValidationResult(MAP_NAME_DISCOURAGED,
                                            ResultType.WARNING,
                                            ValidationResult.LOW);
            }
            return null;
        }
    }

    private class MapNameUniquenessValidator implements Validator
    {

        /** {@inheritDoc} */
        @Override
        public ValidationResult validate()
        {
            String mapName =
                    MapTypeWizard.this.mapTypePropertiesPanel.getMapTypeFieldName();

            if (mapName != null
                && ResourceSetModel.getInstance()
                                   .getResourceSet()
                                   .getUsedMapTypeNames()
                                   .contains(mapName))
            {
                return new ValidationResult(MAP_NAME_ALREADY_IN_USE,
                                            ResultType.ERROR,
                                            ValidationResult.HIGH);
            }
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void stateChanged(ChangeEvent e)
    {
        notifyValidation();
    }

}
