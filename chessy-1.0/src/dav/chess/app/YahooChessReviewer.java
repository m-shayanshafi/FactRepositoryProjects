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
package dav.chess.app;

import java.io.*;
import java.util.*;

import dav.chess.board.*;

// see http://chess.liveonthenet.com/chess/beginner/notation/notation.html

public class YahooChessReviewer {

    Vector moves = new Vector(10);
    Vector gameHistory = null;
    private ChessBoard chessBoard = null;
    private String whitePlayer = "";
    private String blackPlayer = "";
    private String gameTime = "";

    public YahooChessReviewer () {
        chessBoard = new ChessBoard(true);
    }

    public Vector getMoves() {
        return moves;
    }

    public Vector getGameHistory() {
        return gameHistory;
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public String getWhitePlayer() {
        return whitePlayer;
    }

    public String getBlackPlayer() {
        return blackPlayer;
    }

    public String getDateString() {
        return gameTime;
    }

    public void loadHistory( String filename ) throws IOException {
        loadHistory( new BufferedReader( new FileReader( filename ) ) );
    }

    public void loadHistory( BufferedReader reader ) throws IOException {

        moves = new Vector(10);
        chessBoard = new ChessBoard(true);

        for (String text = reader.readLine(); text!=null; text = reader.readLine()) {
            if (text.startsWith(";Title")) {
            } else if (text.startsWith(";White")) {
                       whitePlayer = text.substring(7);
            } else if (text.startsWith(";Black")) {
                       blackPlayer = text.substring(7);
            } else if (text.startsWith(";Date")) {
                       gameTime = text.substring(6);
            } else if (text.length()==0) {
                // blank line
            } else {
                processTurnLine(text);
            }
        }


        gameHistory = new Vector(moves.size());
        gameHistory.add(chessBoard.clone());
        // make the moves and build the gameHistory vector
        for (Enumeration en = moves.elements(); en.hasMoreElements(); ) {
            ChessMove move = (ChessMove)en.nextElement();
            try {
                chessBoard.movePiece( move );
                gameHistory.add( chessBoard.clone() );
            } catch (IllegalChessMoveException exception) {
                exception.printStackTrace();
            }
        }

    }

    public void processTurnLine( String text ) {
        //System.out.println(text);
        int space1 = text.indexOf(' ');
        int space2 = text.indexOf(' ',space1+1);

        if (space1>-1 && space2>-1) {
            String move1 = text.substring(space1+1,space2);
            String move2 = text.substring(space2+1);
            //System.out.println("move1: "+move1);
            //System.out.println("move2: "+move2);
            processMoveString( move1 );
            processMoveString( move2 );
        }

    }

    public void processMoveString( String text ) {
        // check for castling
        if (text.equalsIgnoreCase("o-o-o")) {
            // castle queen side
            moves.add( ChessMove.CASTLE_LONG );
        } else if (text.equalsIgnoreCase("o-o")) {
            // castle king side
            moves.add( ChessMove.CASTLE_SHORT );
        } else {
            int hyphen = text.indexOf('-');
            if (hyphen==-1) {
                hyphen = text.indexOf('x');
            }
            if (hyphen!=-1) {
                String from = text.substring(0,hyphen);
                String to = text.substring(hyphen+1);

                int fromColumn = getColumnFromLetter(from);
                int fromRow = Integer.parseInt(String.valueOf(from.charAt(1)))-1;
                int toColumn = getColumnFromLetter(to);
                int toRow = Integer.parseInt(String.valueOf(to.charAt(1)))-1;

                moves.add( new ChessMove( fromRow, fromColumn, toRow, toColumn ) );

                //System.out.println("from: "+from);
                //System.out.println("  to: "+to);
            }
        }
    }

    public int getColumnFromLetter(String pos) {
        switch (pos.charAt(0)) {
        case 'a': return 0;
        case 'b': return 1;
        case 'c': return 2;
        case 'd': return 3;
        case 'e': return 4;
        case 'f': return 5;
        case 'g': return 6;
        case 'h': return 7;
        default:  return -1;
        }
    }

    public static void main(String[] args) {

        if (args.length<=0) {
            System.out.println("parameter is name of Yahoo History file");
            System.exit(0);
        }

        YahooChessReviewer me = new YahooChessReviewer();

        try {
            me.loadHistory( new BufferedReader( new FileReader( args[0] ) ) );

            Vector gameHistory = me.getGameHistory();
            for (Enumeration en = gameHistory.elements(); en.hasMoreElements(); ) {
                ChessBoard board = (ChessBoard)en.nextElement();
                System.out.println(board);
            }
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }


    }



}


