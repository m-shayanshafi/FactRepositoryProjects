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
package pl.org.minions.stigma.client.ui;

/**
 * Gathers interfaces for classes that provide some visual
 * representation of area around player actor.
 */
public interface AreaView
{
    /**
     * For classes visualizing actor selection.
     */
    public interface ActorSelectionView
    {
        /**
         * Selects the actor with given id. This actor will
         * be highlighted in a way to denote selection.
         * @param actorId
         *            selected actor id or <code>null</code>
         *            to clear selection
         */
        void setSelectedActor(Integer actorId);

        /**
         * Returns the id of currently selected actor or
         * <code>null</code> if no actor is selected.
         * @return id of selected actor or <code>null</code>
         */
        Integer getSelectedActor();
    }

    /**
     * Returns {@link ActorSelectionView} provided by this
     * AreaView or <code>null</code> if no actor selection
     * view is provided.
     * @return an actor selection view or <code>null</code>
     */
    ActorSelectionView getActorSelectionView();
}
