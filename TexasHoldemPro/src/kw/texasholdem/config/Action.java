package kw.texasholdem.config;

import java.io.Serializable;

/**
 * A Texas Hold'em poker action.
 * 
 * @author Ken Wu
 */
public enum Action implements Serializable{
    
    /** Posting the small blind. */
    SMALL_BLIND("Small Blind", "posts the small blind"),

    /** Posting the big blind. */
    BIG_BLIND("Big Blind", "posts the big blind"),
    
    /** Checking. */
    CHECK("Check", "checks"),
    
    /** Calling a bet. */
    CALL("Call", "calls"),
    
    /** Place an initial bet. */
    BET("Bet", "bets"),
    
    /** Raising the current bet. */
    RAISE("Raise", "raises"),
    
    ALL_IN("All-in", "goes all-in"),
    
    /** Folding. */
    FOLD("Fold", "folds"),
    
    /** Continuing the game. */
    CONTINUE("Continue", "continues"),
    
    

    /** Here are all the GUI actions*/
    ALLOW_SAVE("SaveOk", "Allow to save"),
    
    DISALLOW_SAVE("SaveNotOk", "Do not allow to save"), 
    
    SAVE("Save", "Save the application"),
    
    CLOSE_IF_NEEDED("Close if needed", "Close the game if needed"), 
    
    HIDE_PROFILE_LEFT_PANEL("Hide Left Profile", "Hide the left profile panel"), 
    
    RESET_ALL_PROFILE_LEFT_PANEL("Reset All Left Profiles", "Reset all left profile panels"), 
    
    UPDATE_LEFT_PLAYER_PANEL("Update the left player panel", "Update the left player panel");
    
    ;
    
    
    /** The name. */
    private final String name;
    
    /** The verb. */
    private final String verb;
    
    /**
     * Constructor.
     * 
     * @param name
     *            The name.
     */
    Action(String name, String verb) {
        this.name = name;
        this.verb = verb;
    }
    
    /**
     * Returns the name.
     * 
     * @return The name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the verb form of this action.
     * 
     * @return The verb.
     */
    public String getVerb() {
        return verb;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return name;
    }

}
