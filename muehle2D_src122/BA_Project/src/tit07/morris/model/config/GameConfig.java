package tit07.morris.model.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Locale;
import java.util.Properties;

/**
 * Die Klasse GameConfig implementiert die zentrale Konfiguration des Spiels.
 * Alle relevanten Einstellungen werden in dieser Klasse verwaltet.
 */
public class GameConfig {

    /** Titel des Spiels */
    private final String TITLE             = "Mühle2D"; //$NON-NLS-1$

    /** Version des Spiels */
    private final String VERSION           = "v1.22 final release"; //$NON-NLS-1$

    /** Anzahl der Zugwiederholungen bevor ein Spiel unentschieden endet */
    private final int    REPEAT_MAX        = 4;

    /** Dateiendung für Savegames */
    private final String EXT               = "dat"; //$NON-NLS-1$

    /** Lokale Adresse mit der Bedienungsanleitung für das Spiel */
    private final String HELP_URL          = "data/doc/index.html"; //$NON-NLS-1$

    /** Name der Configdatei */
    private final String CONFIG_FILE       = "config.properties"; //$NON-NLS-1$

    /** Anzahl der weißen Steine zu Beginn des Spiels */
    public final int     INIT_STONES_WHITE = 9;

    /** Anzahl der schwarzen Steine zu Beginn des Spiels */
    public final int     INIT_STONES_BLACK = 9;

    /** Breite des Spielfensters */
    private int          xSize;

    /** Höhe des Spielfensters */
    private int          ySize;

    /** Namen des aktiven weißen Spielers im Statusfenster */
    private String       whiteName;

    /** Namen des aktiven schwarzen Spielers im Statusfenster */
    private String       blackName;

    /** Namen des weißen Spielers im Textfeld des Optionsmenüs */
    private String       whiteNameTextField;

    /** Namen des schwarzen Spielers im Textfeld des Optionsmenüs */
    private String       blackNameTextField;

    /** Inputquelle des weißen Spieler */
    private PlayerInput  whiteInput;

    /** Inputquelle des schwarzen Spieler */
    private PlayerInput  blackInput;

    /** Aktuelle KI-Auswahl des weißen Spielers */
    private String       aiWhite;

    /** Aktuelle KI-Auswahl des schwarzen Spielers */
    private String       aiBlack;

    /** Zustand des Sounds */
    private boolean      isSoundOn;

    /** Zustand der Animation */
    private boolean      isAnimationOn;

    /** Aktuelle Animationsgeschwindigkeit */
    private int          animationSpeed;

    /** Aktueller GameStyle */
    private GameStyle    gameStyle;

   
        

    /**
     * Erzeugt eine neue Instanz der Konfiguration und initialisiert die
     * Konfiguration anhand einer Konfigurationsdatei oder mit Defaultwerten,
     * wenn keine Konfigurationsdatei gefunden werden konnte.
     */
    public GameConfig() {                
      //Locale currentLocale = new Locale("en", "EN"); //$NON-NLS-1$ //$NON-NLS-2$
        Locale currentLocale = Locale.getDefault();
        Language.setLanguage( currentLocale );
        
        File configFile = new File( CONFIG_FILE );
        if( !configFile.exists() ) {
            this.setDefault();
        }
        else {
            try {
                this.loadFromDisk();
            }
            catch( IOException e ) {
                this.setDefault();
            }
        }               
    }

    /* Setzt die Standardwerte der Config */
    public void setDefault() {

        this.xSize = 670;
        this.ySize = 720;
        this.aiWhite = Messages.getString("GameConfig.normal_dif"); //$NON-NLS-1$
        this.aiBlack = Messages.getString("GameConfig.normal_dif"); //$NON-NLS-1$
        this.whiteName = Messages.getString("GameConfig.player1"); //$NON-NLS-1$
        this.blackName = Messages.getString("GameConfig.ai_normal"); //$NON-NLS-1$
        this.whiteNameTextField = this.whiteName;
        this.blackNameTextField = Messages.getString("GameConfig.player2"); //$NON-NLS-1$
        this.whiteInput = PlayerInput.HUMAN;
        this.blackInput = PlayerInput.CPU_NORMAL;
        this.isSoundOn = true;
        this.isAnimationOn = true;
        this.animationSpeed = 5;
        this.gameStyle = GameStyle.getClassicStyle();

        /* Versuche Konfigurationsdatei anzulegen */
        try {
            this.saveToDisk();
        }
        catch( IOException e ) {
        }
    }

    /**
     * Speichert die aktuelle Konfiguration als Property-Datei.
     * 
     * @throws IOException Wird geworfen, wenn Probleme beim Schreiben der Datei
     *             aufgetreten sind.
     */
    public void saveToDisk() throws IOException {

        Properties properties = new Properties();

        /* Zu speichernde Werte in Property schreiben */
        properties.setProperty( "xSize", "" + xSize ); //$NON-NLS-1$ //$NON-NLS-2$
        properties.setProperty( "ySize", "" + ySize ); //$NON-NLS-1$ //$NON-NLS-2$
        properties.setProperty( "White_Name", whiteName ); //$NON-NLS-1$
        properties.setProperty( "Black_Name", blackName ); //$NON-NLS-1$
        properties.setProperty( "White_Name_TextField", whiteNameTextField ); //$NON-NLS-1$
        properties.setProperty( "Black_Name_TextField", blackNameTextField ); //$NON-NLS-1$
        properties.setProperty( "White_Input", whiteInput.toString() ); //$NON-NLS-1$
        properties.setProperty( "Black_Input", blackInput.toString() ); //$NON-NLS-1$
        properties.setProperty( "Ki_White", aiWhite ); //$NON-NLS-1$
        properties.setProperty( "Ki_Black", aiBlack ); //$NON-NLS-1$
        properties.setProperty( "isAnimationOn", //$NON-NLS-1$
                                booleanToString( isAnimationOn ) );
        properties.setProperty( "isSoundOn", booleanToString( isSoundOn ) ); //$NON-NLS-1$
        properties.setProperty( "animationSpeed", "" + animationSpeed ); //$NON-NLS-1$ //$NON-NLS-2$
        properties.setProperty( "gameStyle", gameStyle.getStyleName() ); //$NON-NLS-1$

        /* Werte in Property schreiben */
        FileOutputStream fileOutputStream = new FileOutputStream( new File( CONFIG_FILE ) );
        properties.storeToXML( fileOutputStream, "XML-Datei mit GameConfig" ); //$NON-NLS-1$
        fileOutputStream.close();
    }

    /**
     * Lädt die aktuelle Konfiguration von der Property-Datei
     * 
     * @throws IOException Wenn Probleme beim laden der Daten von der Datei
     *             aufgetreten sind.
     */
    public void loadFromDisk() throws IOException {

        /* Konfigurationsdaten von Datei laden */
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream( new File( CONFIG_FILE ) );
        properties.loadFromXML( fileInputStream );
        fileInputStream.close();

        /* Konfiguration in Variablen schreiben */
        this.xSize = Integer.parseInt( properties.getProperty( "xSize" ) ); //$NON-NLS-1$
        this.ySize = Integer.parseInt( properties.getProperty( "ySize" ) ); //$NON-NLS-1$
        this.whiteName = properties.getProperty( "White_Name" ); //$NON-NLS-1$
        this.blackName = properties.getProperty( "Black_Name" ); //$NON-NLS-1$
        this.whiteNameTextField = properties
                                            .getProperty( "White_Name_TextField" ); //$NON-NLS-1$
        this.blackNameTextField = properties
                                            .getProperty( "Black_Name_TextField" ); //$NON-NLS-1$
        String whiteInput = properties.getProperty( "White_Input" ); //$NON-NLS-1$
        this.whiteInput = convertPlayerInput( whiteInput );
        String blackInput = properties.getProperty( "Black_Input" ); //$NON-NLS-1$
        this.blackInput = convertPlayerInput( blackInput );
        this.aiWhite = properties.getProperty( "Ki_White" ); //$NON-NLS-1$
        this.aiBlack = properties.getProperty( "Ki_Black" ); //$NON-NLS-1$
        this.isAnimationOn = stringToboolean( properties
                                                        .getProperty( "isAnimationOn" ) ); //$NON-NLS-1$
        this.isSoundOn = stringToboolean( properties.getProperty( "isSoundOn" ) ); //$NON-NLS-1$
        this.animationSpeed = Integer
                                     .parseInt( properties
                                                          .getProperty( "animationSpeed" ) ); //$NON-NLS-1$
        this.gameStyle = GameStyle
                                  .setGameStyle( properties
                                                           .getProperty( "gameStyle" ) ); //$NON-NLS-1$
    }

    /**
     * Wandelt den String des PlayerInputs in die entsprechende Enumeration um.
     * 
     * @param playerInput String repräsentation des PlayerInputs
     * @return Enumeration des PlayerInputs
     */
    private PlayerInput convertPlayerInput( String playerInput ) {

        if( playerInput.equals( "HUMAN" ) ) { //$NON-NLS-1$
            return PlayerInput.HUMAN;
        }
        else if( playerInput.equals( "CPU_VERY_WEAK" ) ) { //$NON-NLS-1$
            return PlayerInput.CPU_VERY_WEAK;
        }
        else if( playerInput.equals( "CPU_WEAK" ) ) { //$NON-NLS-1$
            return PlayerInput.CPU_WEAK;
        }
        else if( playerInput.equals( "CPU_NORMAL" ) ) { //$NON-NLS-1$
            return PlayerInput.CPU_NORMAL;
        }
        return null;
    }

    /**
     * Gibt den Titel des Spiels zurück
     * 
     * @return Titel des Spiels als String
     */
    public String getTitle() {

        return TITLE;
    }

    /**
     * Gibt die Version des Spiels zurück
     * 
     * @return Version des Spiels als String
     */
    public String getVersion() {

        return VERSION;
    }

    /**
     * Gibt die Fenstergröße in X-Richtung zurück
     * 
     * @return Fenstergröße in X-Richtung
     */
    public int getXSize() {

        return xSize;
    }

    /**
     * Setzt die Fenstergröße in X-Richtung
     * 
     * @param xSize Fenstergröße in X-Richtung
     */
    public void setXSize( int xSize ) {

        this.xSize = xSize;
    }

    /**
     * Gibt die Fenstergröße in Y-Richtung zurück
     * 
     * @return Fenstergröße in Y-Richtung
     */
    public int getYSize() {

        return ySize;
    }

    /**
     * Setzt die Fenstergröße in Y-Richtung
     * 
     * @param ySize Fenstergröße in Y-Richtung
     */
    public void setYSize( int ySize ) {

        this.ySize = ySize;
    }

    /**
     * Gibt den Namen des weißen Spielers zurücl
     * 
     * @return Namen des weißen Spielers
     */
    public String getWhiteName() {

        return whiteName;
    }

    /**
     * Setzt den Namen des weißen Spielers
     * 
     * @param whiteName Namen den weoßen Spielers
     */
    public void setWhiteName( String whiteName ) {

        this.whiteName = whiteName;
    }

    /**
     * Gibt den Namen des schwarzen Spielers
     * 
     * @return Name des schwarzen Spielers
     */
    public String getBlackName() {

        return blackName;
    }

    /**
     * Setzt den Namen des schwarzen Spielers
     * 
     * @param blackName Name des schwarzen Spielers
     */
    public void setBlackName( String blackName ) {

        this.blackName = blackName;
    }

    /**
     * Gibt die Inputquelle den weißen Spielers
     * 
     * @return Inputquelle des weißen Spielers
     */
    public PlayerInput getWhiteInput() {

        return whiteInput;
    }

    /**
     * Setzt die Inputquelle des weißen Spielers
     * 
     * @param whiteInput Inputquelle des weißen Spielers
     */
    public void setWhiteInput( PlayerInput whiteInput ) {

        this.whiteInput = whiteInput;
    }

    /**
     * Gibt die Inputquelle des schwarzen Spielers
     * 
     * @return Inputquelle des schwarzen Spielers
     */
    public PlayerInput getBlackInput() {

        return blackInput;
    }

    /**
     * Setzt die Inputquelle des schwarzen Spielers
     * 
     * @param blackInput Inputquelle des schwarzen Spielers
     */
    public void setBlackInput( PlayerInput blackInput ) {

        this.blackInput = blackInput;
    }

    /**
     * Gibt an, ob der Sound aktiviert ist
     * 
     * @return True, wenn der Sound aktiviert ist, ansonsten false.
     */
    public boolean isSoundOn() {

        return isSoundOn;
    }

    /**
     * Setzt die Aktivierung des Sounds
     * 
     * @param isSoundOn True für an / false für aus
     */
    public void setSoundOn( boolean isSoundOn ) {

        this.isSoundOn = isSoundOn;
    }

    /**
     * Gibt an, ob die Animation aktiviert ist
     * 
     * @return True, wenn die Animation aktiviert ist, ansonsten false.
     */
    public boolean isAnimationOn() {

        return isAnimationOn;
    }

    /**
     * Setzt die Aktivierung der Animation
     * 
     * @param isAnimationOn True für an / false für aus
     */
    public void setAnimationOn( boolean isAnimationOn ) {

        this.isAnimationOn = isAnimationOn;
    }

    /**
     * Gibt die Animationsgeschwindigkeit zurück
     * 
     * @return Die Animationsgeschwindigkeit von 1 - 10
     */
    public int getAnimationSpeed() {

        return animationSpeed;
    }

    /**
     * Setzt die Animationsgeschwindigkeit.
     * 
     * @param animationSpeed Animationsgeschwindigkeit von 1 - 10
     */
    public void setAnimationSpeed( int animationSpeed ) {

        if( animationSpeed < 1 ) {
            animationSpeed = 1;
        }
        else if( animationSpeed > 10 ) {
            animationSpeed = 10;
        }
        this.animationSpeed = animationSpeed;
    }

    /**
     * Gibt den aktuellen GameStyle des Spiels zurück
     * 
     * @return GameStyle des Spiels
     */
    public GameStyle getGameStyle() {

        return gameStyle;
    }

    /**
     * Setzt den GameStyle für das Spiel
     * 
     * @param gameStyle Neuer GameStyle
     */
    public void setGameStyle( GameStyle gameStyle ) {

        this.gameStyle = gameStyle;
    }

    /**
     * Gibt die KI für den weißen Spieler zurück
     * 
     * @return KI für den weißen Spieler
     */
    public String getKiWhite() {

        return aiWhite;
    }

    /**
     * Setzt die KI für den weißen Spieler
     * 
     * @param kiWhite KI für den weißen Spieler
     */
    public void setKiWhite( String kiWhite ) {

        this.aiWhite = kiWhite;
    }

    /**
     * Gibt die KI für den schwarzen Spieler zurück
     * 
     * @return KI für den schwarzen Spieler
     */
    public String getKiBlack() {

        return aiBlack;
    }

    /**
     * Setzt die KI für den schwarzen Spieler
     * 
     * @param kiBlack KI für den schwarzen Spieler
     */
    public void setKiBlack( String kiBlack ) {

        this.aiBlack = kiBlack;
    }

    /**
     * Gibt den weißen Spielernamen im Optionsmenü zurück
     * 
     * @return Weißer Spielernamen im Optionsmenü
     */
    public String getWhiteNameTextField() {

        return whiteNameTextField;
    }

    /**
     * Setzt den weißen Spielernamen im Optionsmenü
     * 
     * @param whiteNameTextField Weißer Spielernamen im Optionsmenü
     */
    public void setWhiteNameTextField( String whiteNameTextField ) {

        this.whiteNameTextField = whiteNameTextField;
    }

    /**
     * Gibt den schwarzen Spielernamen im Optionsmenü zurück
     * 
     * @return Schwarzer Spielernamen im Optionsmenü
     */
    public String getBlackNameTextField() {

        return blackNameTextField;
    }

    /**
     * Setzt den schwarzen Spielernamen im Optionsmenü
     * 
     * @param blackNameTextField Schwarzer Spielernamen im Optionsmenü
     */
    public void setBlackNameTextField( String blackNameTextField ) {

        this.blackNameTextField = blackNameTextField;
    }

    /**
     * Wandelt einen String in ein boolean um
     * 
     * @param stringValue Umzuwandelnder String
     * @return True, wenn der String true ist
     */
    private boolean stringToboolean( String stringValue ) {

        return ( stringValue.equalsIgnoreCase( "true" ) ); //$NON-NLS-1$
    }

    /**
     * Wandelt ein boolean in einen String um
     * 
     * @param boolValue Umzuwandelndes boolean
     * @return String repräsentation des übergeben boolean
     */
    private String booleanToString( boolean boolValue ) {

        return ( boolValue == true ) ? "true" : "false"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Gibt die Dateiendung für Savegames zurück
     * 
     * @return Dateiendung für Savegames
     */
    public String getSavegameExtension() {

        return EXT;
    }

    /**
     * Gibt die URL zu der Bedienungsanleitung zurück
     * 
     * @return URL zu der Bedienungsanleitung
     * @throws MalformedURLException Wird bei ungültiger URL geworfen
     */
    public String getLocalHelpURL() throws MalformedURLException {

        File helpFile = new File( this.HELP_URL );
        return helpFile.toURI().toURL().toString();
    }

    /**
     * Gibt die maximale Anzahl der Zugwiederholungen, bevor ein Spiel
     * unentschieden endet.
     * 
     * @return Maximale Anzahl der Zugwiederholungen
     */
    public int getMaxRepeat() {

        return REPEAT_MAX;
    }
}
