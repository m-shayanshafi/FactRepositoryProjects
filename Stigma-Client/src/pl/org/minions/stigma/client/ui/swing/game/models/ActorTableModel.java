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
package pl.org.minions.stigma.client.ui.swing.game.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import pl.org.minions.stigma.game.actor.Actor;

/**
 * Model which represents table of actors.
 */
public class ActorTableModel extends AbstractTableModel
{
    private static final long serialVersionUID = 1L;

    private int playerActorId;
    private HashMap<Integer, Actor> actors;
    private ArrayList<Actor> actorsRows;

    private HashMap<Integer, ActorTableColumnModel.Column> columns;

    /**
     * Constructor.
     * @param columns
     *            columns mapping
     * @param playerActorId
     *            player's actor's id - this actor will be
     *            ignored in add/remove methods
     */
    public ActorTableModel(HashMap<Integer, ActorTableColumnModel.Column> columns,
                           int playerActorId)
    {
        this.actors = new HashMap<Integer, Actor>();
        this.actorsRows = new ArrayList<Actor>();
        this.playerActorId = playerActorId;
        this.columns = columns;
    }

    /**
     * Adds collection of actors.
     * @param data
     *            collection of actors
     */
    public void addActors(Collection<Actor> data)
    {
        for (Actor a : data)
            addActor(a);
    }

    /**
     * Adds actor to model.
     * @param a
     *            actor to add
     */
    public void addActor(Actor a)
    {
        if (a.getId() == playerActorId)
            return;
        if (this.actors.put(a.getId(), a) == null)
            this.actorsRows.add(a);
    }

    /**
     * Checks whether actor is in model.
     * @param a
     *            actor
     * @return true if actor is in model
     */
    public boolean containsActor(Actor a)
    {
        return containsActor(a.getId());
    }

    /**
     * Checks whether actor is in model.
     * @param id
     *            id of actor
     * @return true if actor is in model
     */
    public boolean containsActor(int id)
    {
        if (id == playerActorId)
            return false;
        return this.actors.containsKey(id);
    }

    /**
     * Removes actor from model.
     * @param a
     *            actor to remove
     */
    public void removeActor(Actor a)
    {
        this.actors.remove(a.getId());
        this.actorsRows.remove(a);
    }

    /**
     * Removes actor from model by id. It is less efficient
     * than remove actor by actor.
     * @param id
     *            id of actor to remove
     */
    public void removeActor(int id)
    {
        this.actors.remove(id);
        for (Actor a : actorsRows)
        {
            if (a.getId() == id)
            {
                actorsRows.remove(a);
                break;
            }
        }
    }

    /**
     * Returns collection of actors.
     * @return collection of actors.
     */
    public Iterable<Actor> getActors()
    {
        return actorsRows;
    }

    /** {@inheritDoc} */
    @Override
    public int getColumnCount()
    {
        return columns.size();
    }

    /** {@inheritDoc} */
    @Override
    public int getRowCount()
    {
        return actorsRows.size();
    }

    /** {@inheritDoc} */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return actorsRows.get(rowIndex);
    }

    /**
     * Returns actor on given row.
     * @param row
     *            row for which actor should be returned
     * @return actor represented on given row
     */
    public Actor getActor(int row)
    {
        return actorsRows.get(row);
    }

}
