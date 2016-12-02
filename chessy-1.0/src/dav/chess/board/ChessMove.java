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
package dav.chess.board;

import dav.chess.piece.*;

public class ChessMove {
    private int fromRow = -1;
    private int fromCol = -1;
    private int toRow = -1;
    private int toCol = -1;

    public static final ChessMove CASTLE_SHORT = new ChessMove( 0, 0, 0, 0 );
    public static final ChessMove CASTLE_LONG = new ChessMove( 0, 0, 0, 0 );

    public ChessMove( int from_row, int from_col, int to_row, int to_col ) {
        this.fromRow  = from_row;
        this.fromCol  = from_col;
        this.toRow  = to_row;
        this.toCol  = to_col;
    }

    public int getFromRow() {
        return fromRow;
    }
    public int getFromCol() {
        return fromCol;
    }

    public int getToRow() {
        return toRow;
    }
    public int getToCol() {
        return toCol;
    }

}
