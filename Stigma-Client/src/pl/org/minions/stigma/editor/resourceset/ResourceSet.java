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
package pl.org.minions.stigma.editor.resourceset;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import pl.org.minions.stigma.databases.actor.server.ArchetypeDBSync;
import pl.org.minions.stigma.databases.map.server.MapDBSync;
import pl.org.minions.stigma.editor.EditorConfig;
import pl.org.minions.stigma.editor.exception.BaseEditorException;
import pl.org.minions.stigma.game.actor.Archetype;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.stigma.game.map.TerrainSet;

/**
 * Class wrapping all the resources within one Resource Set.
 */
public class ResourceSet
{
    private static final String PATH_SEPARATOR = "/";
    private String name;
    private MapDBSync mapDB;
    private ArchetypeDBSync archetypeDB;

    /**
     * Constructor.
     * @param uri
     *            URI of the resource set root
     */
    public ResourceSet(URI uri)
    {
        load(uri);
    }

    /**
     * Returns collection of archetypes in this resource
     * set.
     * @return collection of archetypes in this resource
     *         set.
     */
    public Collection<Archetype> getArchetypes()
    {
        return archetypeDB.values();
    }

    /**
     * Returns the description of the resource set.
     * @return description of the resource set
     */
    public String getDescription()
    {
        //TODO: add description
        return "TODO";
    }

    /**
     * Returns collection of map types in this resource set.
     * @return collection of map types in this resource set.
     */
    public Collection<MapType> getMapTypes()
    {
        for (MapType mapType : mapDB.values())
        {
            if (mapType.getTerrainSet() == null)
            {
                mapType.assignTerrainSet(mapDB.getTerrainSetDBSync()
                                              .getById(mapType.getTerrainSetId()));
            }
        }
        return mapDB.values();
    }

    /**
     * Returns collection of map type names in this resource
     * set.
     * @return collection of map type names in this resource
     *         set
     */
    public Collection<String> getUsedMapTypeNames()
    {
        Set<String> usedMapTypeNames = new HashSet<String>();
        for (MapType mapType : mapDB.values())
        {
            usedMapTypeNames.add(mapType.getName());
        }
        return usedMapTypeNames;
    }

    /**
     * Returns the name of the resource set.
     * @return name of the resource set
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns collection of terrain sets in this resource
     * set.
     * @return collection of map types in this resource set.
     */
    public Collection<TerrainSet> getTerrainSets()
    {
        return mapDB.getTerrainSetDBSync().values();
    }

    /**
     * Returns terrain set for given id.
     * @param terrainSetId
     *            terrain set id
     * @return terrain set for given id
     */
    public TerrainSet getTerrainSet(short terrainSetId)
    {
        return mapDB.getTerrainSet(terrainSetId);
    }

    /**
     * Checks if any of the resource set databases was
     * modified.
     * @return modification state of the resource set
     */
    public boolean isModified()
    {
        boolean result = false;

        result |= mapDB.getTerrainSetDBSync().isModified();
        result |= mapDB.isModified();
        result |= archetypeDB.isModified();

        return result;
    }

    /**
     * Loads the resource set.
     * @param uri
     *            URI of the resource set root
     */
    public void load(URI uri)
    {
        EditorConfig.globalInstance().setUri(uri);
        String path = uri.getPath();
        if (path.charAt(path.length() - 1) == '/')
        {
            path = path.substring(0, path.lastIndexOf(PATH_SEPARATOR));
            name =
                    path.substring(Math.max(0,
                                            path.lastIndexOf(PATH_SEPARATOR) + 1));
        }
        else
        {
            name =
                    path.substring(Math.max(0,
                                            path.lastIndexOf(PATH_SEPARATOR) + 1));
        }
        mapDB = new MapDBSync(uri);
        archetypeDB = new ArchetypeDBSync(uri);
    }

    /**
     * Saves the resource set.
     */
    public void save()
    {
        boolean result = true;
        result &= mapDB.getTerrainSetDBSync().saveAll(false);
        result &= mapDB.saveAll(false);
        result &= archetypeDB.saveAll(false);
        if (!result)
        {
            throw new BaseEditorException("Save failed.");
        }
    }

    /**
     * Adds new map to this resource set.
     * @param obj
     *            new map
     */
    public void addMap(MapType obj)
    {
        // TODO: (by KG) I added here 'forceIdSet==true' so obj will have id changed!
        mapDB.add(obj, true, false, true);
    }

    /**
     * Returns map type with given id.
     * @param id
     *            map type id
     * @return map type with given id
     */
    public MapType getMapType(short id)
    {
        return mapDB.getById(id);
    }
}
