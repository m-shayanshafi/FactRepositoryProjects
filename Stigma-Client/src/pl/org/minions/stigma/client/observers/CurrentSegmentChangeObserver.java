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
package pl.org.minions.stigma.client.observers;

import java.util.Collection;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.stigma.game.map.MapInstance.Segment;

/**
 * An interface for objects that need to be notified when
 * the {@link Segment} that player {@link Actor} stands on
 * changes.
 * <p>
 * Current segment can be <code>null</code> while
 * {@link MapType} for current map is not available.
 * @see Client#addCurrentSegmentChangeObserver(CurrentSegmentChangeObserver)
 */
public interface CurrentSegmentChangeObserver
{
    /**
     * Used to notify that the current segment changed.
     * @param currentSegment
     *            new current map segment, can be
     *            <code>null</code>
     * @param previousSegment
     *            previous current map segment, can be
     *            <code>null</code>
     * @param appearingSegments
     *            segments that are now in field of view,
     *            but weren't previously, can include
     *            <code>currentSegment</code>
     * @param disappearingSegments
     *            segments that are no longer in field of
     *            view, can include
     *            <code>previousSegment</code>
     */
    void currentSegmentChanged(Segment currentSegment,
                               Segment previousSegment,
                               Collection<Segment> appearingSegments,
                               Collection<Segment> disappearingSegments);
}
