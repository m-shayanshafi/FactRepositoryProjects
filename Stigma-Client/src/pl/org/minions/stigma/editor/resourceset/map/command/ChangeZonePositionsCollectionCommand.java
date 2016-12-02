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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.org.minions.stigma.databases.xml.Modifiable;
import pl.org.minions.stigma.editor.command.EditorCommand;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.stigma.game.map.Zone;
import pl.org.minions.stigma.globals.Position;

/**
 * Command used to edit zone tiles on the map.
 */
public class ChangeZonePositionsCollectionCommand extends
                                                 EditorCommand<MapType>
{

    private Zone zone;
    private Set<Position> changedPositions;
    private List<Position> previousPositions;
    private boolean setting;

    /**
     * Constructor.
     * @param zone
     *            zone
     * @param firstPosition
     *            first position
     * @param changedPositions
     *            set of changed positions
     */
    public ChangeZonePositionsCollectionCommand(Zone zone,
                                                Position firstPosition,
                                                Set<Position> changedPositions)
    {
        this.zone = zone;
        this.changedPositions = changedPositions;
        setting = !zone.getPositionsList().contains(firstPosition);
    }

    /** {@inheritDoc} */
    @Override
    protected boolean innerExecute(MapType context,
                                   List<Modifiable> modifiedList)
    {
        previousPositions = new ArrayList<Position>(zone.getPositionsList());
        Set<Position> positionSet =
                new HashSet<Position>(zone.getPositionsList());
        if (setting)
        {
            positionSet.addAll(changedPositions);
        }
        else
        {
            positionSet.removeAll(changedPositions);
        }
        zone.setPositionsList(new ArrayList<Position>(positionSet));
        modifiedList.add(context);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean innerUndo(MapType context)
    {
        zone.setPositionsList(previousPositions);
        return true;
    }

}
