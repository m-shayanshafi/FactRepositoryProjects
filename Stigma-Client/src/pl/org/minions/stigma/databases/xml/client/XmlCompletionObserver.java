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
package pl.org.minions.stigma.databases.xml.client;

/**
 * Interface for observing completion of asynchronous
 * objects.
 * @see XmlAsyncDB
 * @see XmlAsyncDB#addObserver(XmlCompletionObserver)
 */
public interface XmlCompletionObserver
{
    /**
     * This method will be called when object of given id is
     * complete. Object with given id should be available by
     * {@link XmlAsyncDB#getById(short)} in database in
     * which this observer was registered.
     * @param id
     *            identifier of completed object
     */
    void objectComplete(short id);
}
