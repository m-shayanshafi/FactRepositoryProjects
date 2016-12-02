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

public class ChessBoard {

    private ChessPosition[][] board = new ChessPosition[8][8];
    private boolean whitesTurn = false; // gets switched to true at beginning of move

    private int moveNumber = 0;

    public ChessBoard( boolean init ) {
        createBoard();
        if (init) {
            initPieces();
        }
    }

    public ChessPosition[][] getBoard() {
        return board;
    }

    public void setWhitesTurn( boolean b ) {
        whitesTurn = b;
    }

    public void setBoard( ChessPosition[][] positions ) {
        for (int row=7; row>-1; row--) {
            for (int col=0; col<8; col++) {
                this.board[row][col] = (ChessPosition)(positions[row][col].clone());
            }
        }
    }

    /*
    public void movePiece( int from_row, int from_col, int to_row, int to_col ) 
    throws IllegalChessMoveException {
        try {
            movePiece( board[from_row][from_col], board[to_row][to_col] );
        } catch (ArrayIndexOutOfBoundsException exception) {
            String err = "bad row/col specified: from "
                         +from_row+","+from_col+" to "+to_row+","+to_col;
            throw new IllegalChessMoveException( err );
        }
    }
    */

    public void movePiece( ChessMove move ) 
    throws IllegalChessMoveException {
        if (move==ChessMove.CASTLE_LONG) {
            System.out.println("castling long");
            castleLong();
        } else if (move==ChessMove.CASTLE_SHORT) {
            System.out.println("castling short");
            castleShort();
        } else {
            movePiece( board[move.getFromRow()][move.getFromCol()], 
                       board[move.getToRow()][move.getToCol()]  );
        }
    }

    // this is the main one, called by the others
    private void movePiece( ChessPosition from, ChessPosition to )
    throws IllegalChessMoveException {
        whitesTurn = !whitesTurn;
        moveNumber++;
        ChessPiece piece = from.getPiece();
        if (piece!=null) {
            // later the move needs to be checked for legality
            from.setPiece(null);
            to.setPiece(piece);
        }
    }

    public void castleShort() throws IllegalChessMoveException {
        int row;
        whitesTurn = !whitesTurn;
        moveNumber++;
        if (whitesTurn) {
            row = 0;
        } else {
            row = 7;
        }
        board[row][5].setPiece(board[row][7].getPiece()); // rook
        board[row][6].setPiece(board[row][4].getPiece()); // king
        board[row][7].setPiece(null);
        board[row][4].setPiece(null);
    }

    public void castleLong() throws IllegalChessMoveException {
        whitesTurn = !whitesTurn;
        moveNumber++;
        int row;
        if (whitesTurn) {
            row = 0;
        } else {
            row = 7;
        }
        board[row][3].setPiece(board[row][0].getPiece()); // rook
        board[row][2].setPiece(board[row][4].getPiece()); // king
        board[row][0].setPiece(null);
        board[row][4].setPiece(null);
    }


    private void createBoard() {
        boolean white = false;
        for (int row=0; row<8; row++) {
            for (int col=0; col<8; col++) {
                board[row][col] = new ChessPosition( row, col, white );
                white = !white;
            }
        }
    }

    private void initPieces() {
        
        // create pawn rows
        for (int col=0; col<8; col++) {
            board[1][col].setPiece( new Pawn( true ) );
            board[6][col].setPiece( new Pawn( false ) );
        }
        //create black backrow
        board[0][0].setPiece( new Rook( true ) );
        board[0][1].setPiece( new Knight( true ) );
        board[0][2].setPiece( new Bishop( true ) );
        board[0][3].setPiece( new Queen( true ) );
        board[0][4].setPiece( new King( true ) );
        board[0][5].setPiece( new Bishop( true ) );
        board[0][6].setPiece( new Knight( true ) );
        board[0][7].setPiece( new Rook( true ) );

        //create white backrow
        board[7][0].setPiece( new Rook( false ) );
        board[7][1].setPiece( new Knight( false ) );
        board[7][2].setPiece( new Bishop( false ) );
        board[7][3].setPiece( new Queen( false ) );
        board[7][4].setPiece( new King( false ) );
        board[7][5].setPiece( new Bishop( false ) );
        board[7][6].setPiece( new Knight( false ) );
        board[7][7].setPiece( new Rook( false ) );

    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        //int turn = ((moveNumber-1)/2)+1;
        //buffer.append( "Turn="+turn+", " );
        //buffer.append( whitesTurn ? "White":"Black" );
        //buffer.append( " move:\n" );
        //buffer.append( "abcdefgh\n--------\n" );
        for (int row=7; row>-1; row--) {
            for (int col=0; col<8; col++) {
                buffer.append(board[row][col].toCharacter());
            }
            buffer.append("|"+(row+1)+"\n");
        }
        return buffer.toString();
    }

    public static void main(String[] args) {

        ChessBoard me = new ChessBoard(true);
        System.out.println(me);
    }

    public Object clone() {
        ChessBoard cb = new ChessBoard(false);
        cb.setBoard( board );
        cb.setWhitesTurn( whitesTurn );

        System.out.println("CLONE:");
        System.out.println(cb);
        return cb;
    }
}
