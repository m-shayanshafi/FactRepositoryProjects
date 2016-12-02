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
package pl.org.minions.stigma.server.collections;

/**
 * Interface for object which can be scheduled in
 * {@link TurnScheduleQueue}.
 */
public interface TurnSchedulable
{
    /**
     * Returns last turn for which this object was
     * scheduled. Should return value < 0 if object was not
     * scheduled yet.
     * @return last turn for which this object was scheduled
     */
    int getLastQueuedTurn();

    /**
     * Sets last turn for which this object was scheduled.
     * Should be called only by {@link TurnScheduleQueue}.
     * @param lastQueuedTurn
     *            new value for last turn for which this
     *            object was scheduled
     */
    void setLastQueuedTurn(int lastQueuedTurn);
}
