package com.kbrahney.pkg.board;

/**
 *
 * @author Kieran
 */
public class Board
{

    private int numRows;
    private int numCols;
    private char defaultCharacter;

    private char[][] chars;

    private int player = 1;

    public Board(int numRows, int numCols, char c) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.defaultCharacter = c;

        chars = new char[numRows][numCols];
    }

    public void display() {
        for (int i = 0; i < chars.length; i++) {
            System.out.print("\t");
            for (int k = 0; k < chars[0].length; k++) {
                System.out.print(chars[i][k] + " ");
            }
            System.out.print("\n");
        }
    }

    public void initialise() {
        System.out.println("Initialising board game...");
        for (int i = 0; i < chars.length; i++) {
            for (int k = 0; k < chars[0].length; k++) {
                chars[i][k] = defaultCharacter;
            }
        }
    }

    public boolean isValidCoord(int numRow, int numCol) {
        if ((numRow >= 0 && numRow < this.numRows) && (numCol >= 0 && numCol < this.numCols))
            return true;

        else {
            System.out.println("error: cannot access a square with params: " + numRow + ", " + numCol);
            return false;
        }
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public char getDefaultChar() {
            return defaultCharacter;
    }

    public char getSquare(int numRow, int numCol) {
        if (isValidCoord(numRow, numCol))
            return chars[numRow][numCol];
        else
            System.out.println("error: cannot access a square with params: " + numRow + ", " + numCol);

        return (Character) null;
    }

    public void setSquare(int numRow, int numCol, char c) {
        if (isValidCoord(numRow, numCol))
            chars[numRow][numCol] = c;
        else
            System.out.println("error: cannot access a square with params: " + numRow + ", " + numCol);
    }

    public void switchPlayer() {
        if (player == 1)
            player = 2;
        else
            player = 1;
    }

    public void setPlayer(int p) {
        player = p;
    }

    public int getPlayer() {
        return player;
    }

}
