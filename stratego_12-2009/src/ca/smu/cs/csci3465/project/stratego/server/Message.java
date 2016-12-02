/*
    This file is part of Stratego.

    Stratego is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Stratego is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Stratego.  If not, see <http://www.gnu.org/licenses/>.
*/

package ca.smu.cs.csci3465.project.stratego.server;

public enum Message
{
	SETUP,
	MOVE,
	PLAY,
	GRID,
	TRAY,
	BYE,
	GAMEOVER,
	UPDATE;

	public static final int PROTOCOL_VERSION = 1;
	public static final String DOWNLOAD_URL = "http://cs.smu.ca/~c_malloy/";
}
