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
package pl.org.minions.stigma.editor.resourceset.map.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.org.minions.stigma.databases.xml.Modifiable;
import pl.org.minions.stigma.editor.command.EditorCommand;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.stigma.game.map.TileType;
import pl.org.minions.stigma.globals.Position;

/**
 * Command used to edit tiles grid on the map.
 */
public class ChangeTilesCollectionCommand extends EditorCommand<MapType>
{

    private Map<Position, TileType> changedTiles;
    private Map<Position, TileType> previousTiles;

    /**
     * Constructor.
     * @param changedTiles
     *            tiles that this command changes
     */
    public ChangeTilesCollectionCommand(Map<Position, TileType> changedTiles)
    {
        this.changedTiles = changedTiles;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean innerExecute(MapType context,
                                   List<Modifiable> modifiedList)
    {
        previousTiles = new HashMap<Position, TileType>();
        for (Position p : changedTiles.keySet())
        {
            previousTiles.put(p, context.getTile(p));
            context.setTile(p, changedTiles.get(p));
        }
        modifiedList.add(context);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean innerUndo(MapType context)
    {
        for (Position p : previousTiles.keySet())
        {
            context.setTile(p, previousTiles.get(p));
        }
        return true;
    }

}
