/*	
	This file is part of NitsLoch.

	Copyright (C) 2007 Darren Watts

    NitsLoch is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    NitsLoch is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with NitsLoch.  If not, see <http://www.gnu.org/licenses/>.
 */

package src.exceptions;

/**
 * Exception thrown when there is no player.  This usually means that
 * you are using the map editor rather than playing the game.
 * @author Darren Watts
 * date 1/29/08
 */
public class NoPlayerException extends Exception {
	private static final long serialVersionUID = src.Constants.serialVersionUID;

}
