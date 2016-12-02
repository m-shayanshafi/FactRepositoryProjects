package tit07.morris.model.config;

import java.awt.Color;
import java.awt.Font;


/**
 * Die Klasse GameStyle verwaltet die verschiedenen Farbthemen des Spiels.
 */
public class GameStyle {

    /** Name des Styles */
    private String styleName;

    /** Farbe des Spielbretts */
    private Color  boardColor;

    /** Farbe der Linien auf dem Spielbrett */
    private Color  lineColor;

    /** Farbe der schwarzen Steine */
    private Color  blackColor;

    /** Farbe der weißen Steine */
    private Color  whiteColor;

    /** Farbe der markierten schwarzen Steine */
    private Color  markBlackColor;

    /** Farbe der markierten weißen Steine */
    private Color  markWhiteColor;

    /** Hintergrundfarbe der Spielfläche */
    private Color  backgroundColor;

    /** Farbe der Schrift im Statusfenster */
    private Color  fontColor;

    /** Schriftart für den Spielernamen und Status des Spielers */
    private Font   nameAndStatusFont;

    /** Schriftart für die Anzeige der Steine im und außerhalb des Feldes */
    private Font   stoneCounterFont;

    /** Schriftart für die Beschriftungen */
    private Font   legendFont;

    /** Relativer Pfad zu den Grafiken */
    private String imagePath;


    /**
     * Privater Konstruktor, welcher einen neuen GameStyle erzeugt.
     * 
     * @param styleName Name des Styles
     * @param boardColor Farbe des Spielbretts
     * @param lineColor Farbe der Linien auf dem Spielbrett
     * @param blackColor Farbe der schwarzen Spielsteine
     * @param whiteColor Farbe der weißen Spielsteine
     * @param markBlackColor Farbe der markierten weißen Spielsteine
     * @param markWhiteColor Farbe der markierten schwarzen Spielsteine
     * @param backgroundColor Hintergrundfarbe
     * @param nameAndStatusFont Schriftart für Spielernamen und Status
     * @param stoneCounterFont Schriftart für die Statistik der Steine
     * @param legendFont Schriftart für die Beschriftungen
     * @param fontColor Farbe der Schriften
     * @param imagePath Relativer Pfad zu den Grafiken
     */
    private GameStyle( String styleName,
                       Color boardColor,
                       Color lineColor,
                       Color blackColor,
                       Color whiteColor,
                       Color markBlackColor,
                       Color markWhiteColor,
                       Color backgroundColor,
                       Font nameAndStatusFont,
                       Font stoneCounterFont,
                       Font legendFont,
                       Color fontColor,
                       String imagePath ) {

        this.styleName = styleName;
        this.boardColor = boardColor;
        this.lineColor = lineColor;
        this.blackColor = blackColor;
        this.whiteColor = whiteColor;
        this.markBlackColor = markBlackColor;
        this.markWhiteColor = markWhiteColor;
        this.backgroundColor = backgroundColor;
        this.nameAndStatusFont = nameAndStatusFont;
        this.stoneCounterFont = stoneCounterFont;
        this.legendFont = legendFont;
        this.fontColor = fontColor;
        this.imagePath = imagePath;
    }

    /**
     * Gibt einen klassischen Stil für die Visualisierung zurück
     * 
     * @return Klassischer Stil für die Visualisierung
     */
    public static GameStyle getClassicStyle() {

        return new GameStyle( "CLASSIC", //$NON-NLS-1$
                              // Hellbraun
                              new Color( 210,
                                         180,
                                         125 ),
                              Color.BLACK,
                              Color.BLACK,
                              Color.WHITE,
                              // Dunkelrot
                              new Color( 100,
                                         0,
                                         0 ),
                              // Hellrot
                              new Color( 250,
                                         200,
                                         200 ),
                              // Grau
                              new Color( 150,
                                         150,
                                         150 ),
                              new Font( Font.SANS_SERIF,
                                        Font.BOLD,
                                        17 ),
                              new Font( Font.SANS_SERIF,
                                        0,
                                        10 ),
                              new Font( Font.SANS_SERIF,
                                        0,
                                        12 ),
                              Color.BLACK,
                              "data/images/classic/" ); //$NON-NLS-1$

    }

    /**
     * Gibt einen modernen Stil für die Visualisierung zurück
     * 
     * @return Moderner Stil für die Visualisierung
     */
    public static GameStyle getModernStyle() {

        return new GameStyle( "MODERN", //$NON-NLS-1$
                              // Grau
                              new Color( 128,
                                         128,
                                         128 ),
                              Color.WHITE,
                              Color.BLACK,
                              Color.WHITE,
                              // Dunkelgrau
                              new Color( 50,
                                         50,
                                         50 ),
                              // Hellgrau
                              new Color( 210,
                                         210,
                                         210 ),
                              // Dunkelgrau
                              new Color( 100,
                                         100,
                                         100 ),
                              new Font( Font.SERIF,
                                        Font.BOLD,
                                        17 ),
                              new Font( Font.SERIF,
                                        0,
                                        10 ),
                              new Font( Font.SERIF,
                                        0,
                                        12 ),
                              Color.WHITE,
                              "data/images/modern/" ); //$NON-NLS-1$
    }

    /**
     * Gibt einen verrückten Stil für die Visualisierung zurück
     * 
     * @return Verrückter Stil für die Visualisierung
     */
    public static GameStyle getCrazyStyle() {

        return new GameStyle( "CRAZY", //$NON-NLS-1$
                              new Color( 255,
                                         0,
                                         128 ),
                              Color.BLACK,
                              // Dunkelgrün
                              new Color( 0,
                                         100,
                                         0 ),
                              // Hellgelb
                              new Color( 255,
                                         255,
                                         160 ),
                              // Gelb
                              new Color( 0,
                                         255,
                                         0 ),
                              // Grün
                              new Color( 236,
                                         236,
                                         0 ),
                              // Hellblau
                              new Color( 55,
                                         155,
                                         255 ),
                              new Font( Font.DIALOG_INPUT,
                                        Font.BOLD,
                                        17 ),
                              new Font( Font.DIALOG_INPUT,
                                        0,
                                        10 ),
                              new Font( Font.DIALOG_INPUT,
                                        0,
                                        12 ),
                              Color.BLACK,
                              "data/images/crazy/" ); //$NON-NLS-1$
    }

    /**
     * Gibt einen Matrix-Stil für die Visualisierung zurück
     * 
     * @return Matrix-Stil für die Visualisierung
     */
    public static GameStyle getMatrixStyle() {

        return new GameStyle( "MATRIX", //$NON-NLS-1$
                              Color.BLACK,
                              // Neongrün
                              new Color( 0,
                                         255,
                                         0 ),
                              // Dunkelgrün
                              new Color( 0,
                                         90,
                                         0 ),
                              // Hellgrün
                              new Color( 190,
                                         255,
                                         190 ),
                              // Dunkelgrün
                              new Color( 0,
                                         110,
                                         0 ),
                              // Hellgrün
                              new Color( 150,
                                         255,
                                         150 ),
                              Color.BLACK,
                              new Font( Font.MONOSPACED,
                                        Font.BOLD,
                                        17 ),
                              new Font( Font.MONOSPACED,
                                        0,
                                        10 ),
                              new Font( Font.MONOSPACED,
                                        0,
                                        12 ),
                              // Neongrün
                              new Color( 0,
                                         255,
                                         0 ),
                              "data/images/matrix/" ); //$NON-NLS-1$

    }

    /**
     * Gibt einen höllischen Stil für die Visualisierung zurück
     * 
     * @return Höllischer Stil für die Visualisierung
     */
    public static GameStyle getHellStyle() {

        return new GameStyle( "HELL", //$NON-NLS-1$
                              new Color( 255,
                                         0,
                                         0 ),
                              Color.BLACK,
                              Color.BLACK,
                              Color.WHITE,
                              new Color( 100,
                                         0,
                                         0 ),
                              new Color( 255,
                                         200,
                                         200 ),
                              new Color( 100,
                                         100,
                                         100 ),
                              new Font( Font.DIALOG,
                                        Font.BOLD,
                                        17 ),
                              new Font( Font.DIALOG,
                                        0,
                                        10 ),
                              new Font( Font.DIALOG,
                                        0,
                                        12 ),
                              Color.BLACK,
                              "data/images/hell/" ); //$NON-NLS-1$
    }

    /**
     * Gibt einen himmlischen Stil für die Visualisierung zurück
     * 
     * @return Himmlischer Stil für die Visualisierung
     */
    public static GameStyle getHeavenStyle() {

        return new GameStyle( "HEAVEN", //$NON-NLS-1$
                              new Color( 80,
                                         168,
                                         255 ),
                              Color.WHITE,
                              Color.BLACK,
                              Color.WHITE,
                              new Color( 0,
                                         0,
                                         128 ),
                              new Color( 210,
                                         210,
                                         255 ),
                              new Color( 100,
                                         100,
                                         100 ),
                              new Font( Font.SANS_SERIF,
                                        Font.BOLD,
                                        17 ),
                              new Font( Font.SANS_SERIF,
                                        0,
                                        10 ),
                              new Font( Font.SANS_SERIF,
                                        0,
                                        12 ),
                              Color.BLACK,
                              "data/images/heaven/" ); //$NON-NLS-1$
    }

    /**
     * Gibt den entsprechenden Stil anhand seines Namens zurück
     * 
     * @param styleName Name des Stils
     * @return Entsprechender Stil für die Visualisierung
     */
    public static GameStyle setGameStyle( String styleName ) {

        if( styleName.equals( "CLASSIC" ) ) { //$NON-NLS-1$
            return getClassicStyle();
        }
        else if( styleName.equals( "MODERN" ) ) { //$NON-NLS-1$
            return getModernStyle();
        }
        else if( styleName.equals( "CRAZY" ) ) { //$NON-NLS-1$
            return getCrazyStyle();
        }
        else if( styleName.equals( "MATRIX" ) ) { //$NON-NLS-1$
            return getMatrixStyle();
        }
        else if( styleName.equals( "HELL" ) ) { //$NON-NLS-1$
            return getHellStyle();
        }
        else if( styleName.equals( "HEAVEN" ) ) { //$NON-NLS-1$
            return getHeavenStyle();
        }
        return null;

    }

    /**
     * Gibt den Namen des Stils zurück
     * 
     * @return Namen des Stils
     */
    public String getStyleName() {

        return styleName;
    }

    /**
     * Gibt die Farbe des Spielbretts zurück
     * 
     * @return Farbe des Spielbretts
     */
    public Color getBoardColor() {

        return boardColor;
    }

    /**
     * Gibt die Farbe der Linien auf dem Spielbrett zurück
     * 
     * @return Farbe der Linien auf dem Spielbrett
     */
    public Color getLineColor() {

        return lineColor;
    }

    /**
     * Gibt die Farbe der schwarzen Spielsteine zurück
     * 
     * @return Farbe der schwarzen Spielsteine
     */
    public Color getBlackColor() {

        return blackColor;
    }

    /**
     * Gibt die Farbe der weißen Spielsteine zurück
     * 
     * @return Farbe der weißen Spielsteine
     */
    public Color getWhiteColor() {

        return whiteColor;
    }

    /**
     * Gibt die Farbe der markierten schwarzen Spielsteine zurück
     * 
     * @return Farbe der markierten schwarzen Spielsteine
     */
    public Color getMarkBlackColor() {

        return markBlackColor;
    }

    /**
     * Gibt die Farbe der markierten weißen Spielsteine zurück
     * 
     * @return Farbe der markierten weißen Spielsteine
     */
    public Color getMarkWhiteColor() {

        return markWhiteColor;
    }

    /**
     * Gibt die Hintergrundfarbe des Spiels zurück
     * 
     * @return Hintergrundfarbe des Spiels
     */
    public Color getBackgroundColor() {

        return backgroundColor;
    }

    /**
     * Gibt die Schrift für Spielernamen und Status zurück
     * 
     * @return Schrift für Spielernamen und Status
     */
    public Font getNameAndStatusFont() {

        return nameAndStatusFont;
    }

    /**
     * Gibt die Schrift für die Stein-Statikstik zurück
     * 
     * @return Schrift für die Steinstatikstik
     */
    public Font getStoneCounterFont() {

        return stoneCounterFont;
    }

    /**
     * Gibt die Schrift für die Beschriftungen zurück
     * 
     * @return Schrift für die Beschriftungen
     */
    public Font getLegendFont() {

        return legendFont;
    }

    /**
     * Gibt den relativen Pfad der Bilddateien zurück
     * 
     * @return Relativer Pfad der Bilddateien
     */
    public String getImagePath() {

        return imagePath;
    }

    /**
     * Gibt die Farbe für die Schriften zurück
     * 
     * @return Farbe für die Schriften
     */
    public Color getFontColor() {

        return fontColor;
    }
}
