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
package pl.org.minions.stigma.game.command.data.sink;

import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.item.Item;

/**
 * Interface representing sink for different data requests.
 */
public interface DataRequestSinks
{
    /**
     * Returns sink for actors data requests.
     * @return sink for actors data requests.
     */
    DataRequestSink<Actor> getActorSink();

    /**
     * Returns sink for items data requests.
     * @return sink for items data requests.
     */
    DataRequestSink<Item> getItemSink();
}
