package org.gcs.robot;

import java.util.prefs.Preferences;

/**
 * The RCPref class manages persistent user data.
 * These include previous high score, screen dimensions,
 * chosen tile set, animation preference and key bindings.
 *
 * @see RCInfo
 * @serial exclude
 * @author John B. Matthews
 */
public class RCPrefs {

    public static final int DEFAULT_SCORE = 0; 
    public static final int DEFAULT_LEVEL = 1; 
    public static final int DEFAULT_JUMPS = 3; 
    private static final int DEFAULT_WIDTH = 25; 
    private static final int DEFAULT_HEIGHT = 20; 
    private static final String DEFAULT_TILESET = ".png"; 
    private static final boolean DEFAULT_ANIMATION = true; 
    private static Preferences p =
        Preferences.userRoot().node("org").node("gcs").node("RobotChase");
    private static final String scoreName = "highScore";
    private static final String levelName = "highLevel";
    private static final String jumpsName = "highJumps";
    private static final String tileSetName = "tileSet";
    private static final String widthName = "lastWidth";
    private static final String heightName = "lastHeight";
    private static final String animatedName = "isAnimated";
    private static final String keysNode = "userKeys";

    private RCPrefs () {
    }

    /** Get the user's previous high score. */
    public static int getHighScore() {
        return p.getInt(scoreName, DEFAULT_SCORE);
    }

    /** Set the user's new high score. */
    public static void putHighScore(int score) {
        p.putInt(scoreName, score);
    }

    /** Get the user's previous high level. */
    public static int getHighLevel() {
        return p.getInt(levelName, DEFAULT_LEVEL);
    }

    /** Set the user's new high level. */
    public static void putHighLevel(int level) {
        p.putInt(levelName, level);
    }

    /** Get the user's previous high jumps. */
    public static int getHighJumps() {
        return p.getInt(jumpsName, DEFAULT_JUMPS);
    }

    /** Set the user's new high jumps. */
    public static void putHighJumps(int jumps) {
        p.putInt(jumpsName, jumps);
    }

    /** Get the user's previous tile set. */
    public static String getTileSet() {
        return p.get(tileSetName, DEFAULT_TILESET);
    }

    /** Set the user's new tile set. */
    public static void putTileSet(String tileSet) {
        p.put(tileSetName, tileSet);
    }

    /** Get the user's last width in tiles. */
    public static int getWidth() {
        return p.getInt(widthName, DEFAULT_WIDTH);
    }

    /** Set the user's new last width in tiles. */
    public static void putWidth(int width) {
        p.putInt(widthName, width);
    }

    /** Get the user's last height in tiles. */
    public static int getHeight() {
        return p.getInt(heightName, DEFAULT_HEIGHT);
    }

    /** Set the user's new last height in tiles. */
    public static void putHeight(int height) {
        p.putInt(heightName, height);
    }

    /** Get the user's desired animation state. */
    public static boolean getAnimated() {
        return p.getBoolean(animatedName, DEFAULT_ANIMATION);
    }

    /** Set the user's desired animation state. */
    public static void putAnimated(boolean isAnimated) {
        p.putBoolean(animatedName, isAnimated);
    }

    /** Get the user's key preferences; values are stored in the Key enum. */
    public static void getKeys() {
       p = p.node(keysNode);
       for (Key k : Key.values())
           k.setKeyCode(p.getInt(k.name(), k.getKeyCode()));
       p = p.parent();
    }

    /** Set the user's key preferences; values are read from the Key enum. */
    public static void putKeys() {
       p = p.node(keysNode);
       for (Key k : Key.values())
           p.putInt(k.name(), k.getKeyCode());
       p = p.parent();
    }

}
