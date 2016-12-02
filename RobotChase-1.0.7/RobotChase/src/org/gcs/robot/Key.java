package org.gcs.robot;

import java.awt.event.KeyEvent;
import java.util.EnumSet;
import java.util.HashMap;

/**
 * The Key enumeration.
 * @author John B. Matthews
 */
public enum Key {
    JUMP, SW, SOUTH, SE, WEST, STAY, EAST, NW, NORTH, NE,
    Animation(KeyEvent.VK_A),
    ChangeSet(KeyEvent.VK_C),
    EditKeys(KeyEvent.VK_E),
    Help(KeyEvent.VK_H),
    NewGame(KeyEvent.VK_N),
    Quit(KeyEvent.VK_Q),
    ResetScore(KeyEvent.VK_R);
    
    /** The user's preferred keyCode for this Key. */
    private int keyCode;
    
    /** The game move for this Key, if defined. */
    private int moveCode;
    
    /** The set of all Keys. */
    private static EnumSet fullSet = EnumSet.allOf(Key.class);
    
    /** A Map to lookup Keys by keyCode. */
    private static HashMap<Integer, Key> keyMap =
        new HashMap<Integer, Key>(fullSet.size() * 4 / 3);
    
    /** Construct a new Key instance; default to numeric keys. */
    private Key() {
        this.moveCode = this.ordinal() + KeyEvent.VK_NUMPAD0;
        this.keyCode = this.moveCode;
    }
    
    /** Construct a new Key instance with the given keyCode. */
    private Key(int keyCode) {
        this.moveCode = KeyEvent.VK_UNDEFINED;
        this.keyCode = keyCode;
    }
    
    /** Get this Key's keyCode. */
    public int getKeyCode() {
        return this.keyCode;
    }

    /** Set this Key's keyCode; mark keyMap for rehash. */
    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
        if (!keyMap.isEmpty()) keyMap.clear();
    }
    
    /** Return a move corresponding to this Key. */
    public int getMove() {
        return this.moveCode;
    }
    
    /** Return the Key corresponding to the given keyCode. */
    public static Key lookup(int keyCode) {
        if (keyMap.isEmpty())
            for (Key k : Key.values())
                keyMap.put(k.keyCode, k);
        return keyMap.get(keyCode);
    }

}
