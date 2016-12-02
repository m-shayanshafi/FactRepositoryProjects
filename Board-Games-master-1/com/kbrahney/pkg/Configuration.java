package com.kbrahney.pkg;


/**
 *
 * @author Kieran
 * @File Configuration.java
 * Created: 20:32:12
 */
public class Configuration {
    
    private static final String[] games = {
        "Noughts_And_Crosses"
    };

    private static final char defaultChar = '-';

    private static int boardWidth;      // Number of squares (in width)
    private static int boardHeight;     // Number of squares (in height)

    public static char getDefaultChar() {
        return defaultChar;
    }

    public static String getGame(int i) {
        if (i < games.length)
            return games[i];
        else
            return null;
    }
    
    public static String[] getGames() {
        String[] g = new String[games.length];
        for (int i = 0; i < games.length; i++)
            g[i] = games[i].replace("_", " ");
        
        return g;
    }

    public static int getBoardWidth() {
        return boardWidth;
    }
    public static void setBoardWidth(int w) {
        boardWidth = w;
    }

    public static int getBoardHeight() {
        return boardHeight;
    }
    public static void setBoardHeight(int h) {
        boardHeight = h;
    }

}
