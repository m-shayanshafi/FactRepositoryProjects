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
package pl.org.minions.utils.ui;

/**
 * Base interface for a user interface class that has a
 * working thread of some sorts.
 */
public interface UI
{

    /**
     * Starts the UI threads, if none are running.
     * <p>
     * If the specific UI implementation has no inner
     * thread, this method should do nothing.
     */
    void start();

    /**
     * Interrupts and detaches the UI threads.
     * <p>
     * If the specific UI implementation has no inner
     * thread, this method should do nothing.
     */
    void stop();

}
