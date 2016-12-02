package com.kbrahney.pkg.board.games.Noughts_And_Crosses;

import com.kbrahney.pkg.board.Board;
import com.kbrahney.pkg.misc.Misc;

/**
 *
 * @author Kieran
 */
public class Noughts_And_Crosses extends Board {

    private final static char BLANK = '-';
    private final static char NOUGHT = 'O';
    private final static char CROSS = 'X';

    private char player1Char = CROSS;
    private char player2Char = NOUGHT;

    public Noughts_And_Crosses(int numRows, int numCols, char c) {
        super(numRows, numCols, c);

        Misc.println("Starting game: Noughts and Crosses");
        initialise();
    }

    private boolean isOccupied(int numRow, int numCol) {
        if (getSquare(numRow, numCol) == NOUGHT || getSquare(numRow, numCol) == CROSS)
            return true;
        else
            return false;
    }

    public char getPlayer1Char() {
            return player1Char;
    }

    public char getPlayer2Char() {
            return player2Char;
    }

    public boolean placeMark(int numRow, int numCol) {
        if (isValidCoord(numRow, numCol)) {
            if (isOccupied(numRow, numCol)) {
                Misc.println("\tError: Square already occupied, please try again.");
                return false;
            }
            else {
                if (getPlayer() == 1)
                    setSquare(numRow, numCol, player1Char);
                else
                    setSquare(numRow, numCol, player2Char);

                display();
                return true;
            }
        }
        return false;
    }

    public boolean isDraw() {
        for (int i = 0; i < getNumRows(); i++)
            for (int k = 0; k < getNumCols(); k++)
                if (getSquare(i, k) == BLANK)
                    return false;

        Misc.println("*** The game was a draw! ***");
        return true;
    }

    public boolean hasWon() {
        int p1Counter = 0, p2Counter = 0;
        int rows = getNumRows(), cols = getNumCols();

        /**
         * Check horizontally
         */
        for (int i = 0; i < rows; i++)
        {
            for (int k = 0; k < cols; k++) {
                if (getSquare(i, k) == player1Char)
                    p1Counter++;
                else if (getSquare(i, k) == player2Char)
                    p2Counter++;

                if (p1Counter == rows) {
                    Misc.println("*** Player 1 has won!!! ***");
                    return true;
                }
                else if (p2Counter == rows) {
                    Misc.println("*** Player 2 has won!!! ***");
                    return true;
                }
            }

            // reset counters
            p1Counter = 0; p2Counter = 0;
        }

        /**
         * Check vertically
         */
        for (int i = 0; i < cols; i++)
        {
            for (int k = 0; k < rows; k++) {
                if (getSquare(k, i) == player1Char)
                    p1Counter++;
                else if (getSquare(k, i) == player2Char)
                    p2Counter++;

                if (p1Counter == cols) {
                    Misc.println("*** Player 1 has won!!! ***");
                    return true;
                }
                else if(p2Counter == cols) {
                    Misc.println("*** Player 2 has won!!! ***");
                    return true;
                }
            }

            // reset counters
            p1Counter = 0; p2Counter = 0;
        }
        /**
         * Check right diagonally
         */
        for (int i = 0; i < rows; i++) {
            if (getSquare(i, i) == player1Char)
                p1Counter++;
            else if (getSquare(i, i) == player2Char)
                p2Counter++;

            if (p1Counter == rows) {
                Misc.println("*** Player 1 has won!!! ***");
                return true;
            }
            else if(p2Counter == rows) {
                Misc.println("*** Player 2 has won!!! ***");
                return true;
            }
        }

        // reset counters
        p1Counter = 0; p2Counter = 0;

        /**
         * Check left diagonally
         */
        for (int i = 0, k = rows-1; i < rows; i++, k--) {
            if (getSquare(i, k) == player1Char)
                p1Counter++;
            else if (getSquare(i, k) == player2Char)
                p2Counter++;

            if (p1Counter == rows) {
                Misc.println("*** Player 1 has won!!! ***");
                return true;
            }
            else if(p2Counter == rows) {
                Misc.println("*** Player 2 has won!!! ***");
                return true;
            }
        }

        return false;
    }

}
