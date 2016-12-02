package kw.sudoku.model;

/**
 * Enumeration used to inform observers what to update.
 *
 * @author Ken Wu @ New York
 */
public enum UpdateAction {
    NEW_GAME,
    CHECK,
    SELECTED_NUMBER,
    CANDIDATES,
    HELP,
    SHOW_SOLUTIONS
}