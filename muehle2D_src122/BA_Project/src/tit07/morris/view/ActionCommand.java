package tit07.morris.view;

/**
 * Definiert die ActionCommands, welche der Controller verstehen muss um auf
 * Ereignisse des Views zu reagieren.
 */
public interface ActionCommand {

    /** �bernehmen-Button aktivieren */
    public static final String ENABLE_APPLY     = "ENABLE_APPLY"; //$NON-NLS-1$

    /** Wei�er Spieler: Mensch als Inputquelle */
    public static final String HUMAN_WHITE      = "HUMAN_WHITE"; //$NON-NLS-1$

    /** Schwarzer Spieler: Mensch als Inputquelle */
    public static final String HUMAN_BLACK      = "HUMAN_BLACK"; //$NON-NLS-1$

    /** Wei�er Spieler: KI als Inputquelle */
    public static final String AI_WHITE         = "AI_WHITE"; //$NON-NLS-1$

    /** Schwarzer Spieler: KI als Inputquelle */
    public static final String AI_BLACK         = "AI_BLACK"; //$NON-NLS-1$

    /** OK-Button wurde im Optionsmen� gedr�ckt */
    public static final String OK               = "OK"; //$NON-NLS-1$

    /** Abbrechen-Button wurde im Optionsmen� gedr�ckt */
    public static final String CANCEL           = "CANCEL"; //$NON-NLS-1$

    /** �bernehmen-Button wurde im Optionsmen� gedr�ckt */
    public static final String APPLY            = "APPLY"; //$NON-NLS-1$

    /** Standard-Button wurde im Optionsmen� gedr�ckt */
    public static final String DEFAULT          = "DEFAULT"; //$NON-NLS-1$

    /** Neues Spiel wurde im Men� ausgew�hlt */
    public static final String NEWGAME          = "NEWGAME"; //$NON-NLS-1$

    /** Spiel pausieren/fortsetzen wurde im Men� ausgew�hlt */
    public static final String TOGGLEGAME       = "TOGGLEGAME"; //$NON-NLS-1$

    /** Spiel laden wurde im Men� ausgew�hlt */
    public static final String LOADGAME         = "LOADGAME"; //$NON-NLS-1$

    /** Spiel speichern wurde im Men� ausgew�hlt */
    public static final String SAVEGAME         = "SAVEGAME"; //$NON-NLS-1$

    /** Spiel beenden wurde im Men� ausgew�hlt */
    public static final String QUITGAME         = "QUITGAME"; //$NON-NLS-1$

    /** Grafikoptionen wurde im Men� ausgew�hlt */
    public static final String GRAPHIC_OPTION   = "GRAPHIC_OPTION"; //$NON-NLS-1$

    /** Spieleroptionen wurde im Men� ausgew�hlt */
    public static final String PLAYER_OPTION    = "PLAYER_OPTION"; //$NON-NLS-1$

    /** Sound an/aus wurde im Men� ausgew�hlt */
    public static final String TOGGLE_SOUND     = "TOGGLE_SOUND"; //$NON-NLS-1$

    /** Animation an/aus wurde im Men� ausgew�hlt */
    public static final String TOGGLE_ANIMATION = "TOGGLE_ANIMATION"; //$NON-NLS-1$

    /** Hilfe anzeigen wurde im Men� ausgew�hlt */
    public static final String HELP             = "HELP"; //$NON-NLS-1$

    /** Infobox anzeigen wurde im Men� ausgew�hlt */
    public static final String INFO             = "INFO"; //$NON-NLS-1$
}
