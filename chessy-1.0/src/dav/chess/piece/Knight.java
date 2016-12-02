/*
Copyright (C) 2002 Dav Coleman

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
/*
http://www.danger-island.com/~dav/
*/
package dav.chess.piece;

public class Knight extends ChessPiece {
    public Knight( boolean white ) {
        this.name = "Knight";
        this.white_color = white;
    }

    /**
     * Override because Knight doesn't use first letter of name
     *
     * @param
     * @return
     * @author Dav
     */
    public Character toCharacter() {
        if (name==null) {
            return null;
        }
        if (white_color) {
            return new Character( 'N' );
        } else {
            return new Character( 'n' );
        }
    }

}
