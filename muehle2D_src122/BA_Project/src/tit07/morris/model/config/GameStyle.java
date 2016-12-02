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

    /** Farbe der wei�en Steine */
    private Color  whiteColor;

    /** Farbe der markierten schwarzen Steine */
    private Color  markBlackColor;

    /** Farbe der markierten wei�en Steine */
    private Color  markWhiteColor;

    /** Hintergrundfarbe der Spielfl�che */
    private Color  backgroundColor;

    /** Farbe der Schrift im Statusfenster */
    private Color  fontColor;

    /** Schriftart f�r den Spielernamen und Status des Spielers */
    private Font   nameAndStatusFont;

    /** Schriftart f�r die Anzeige der Steine im und au�erhalb des Feldes */
    private Font   stoneCounterFont;

    /** Schriftart f�r die Beschriftungen */
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
     * @param whiteColor Farbe der wei�en Spielsteine
     * @param markBlackColor Farbe der markierten wei�en Spielsteine
     * @param markWhiteColor Farbe der markierten schwarzen Spielsteine
     * @param backgroundColor Hintergrundfarbe
     * @param nameAndStatusFont Schriftart f�r Spielernamen und Status
     * @param stoneCounterFont Schriftart f�r die Statistik der Steine
     * @param legendFont Schriftart f�r die Beschriftungen
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
     * Gibt einen klassischen Stil f�r die Visualisierung zur�ck
     * 
     * @return Klassischer Stil f�r die Visualisierung
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
     * Gibt einen modernen Stil f�r die Visualisierung zur�ck
     * 
     * @return Moderner Stil f�r die Visualisierung
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
     * Gibt einen verr�ckten Stil f�r die Visualisierung zur�ck
     * 
     * @return Verr�ckter Stil f�r die Visualisierung
     */
    public static GameStyle getCrazyStyle() {

        return new GameStyle( "CRAZY", //$NON-NLS-1$
                              new Color( 255,
                                         0,
                                         128 ),
                              Color.BLACK,
                              // Dunkelgr�n
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
                              // Gr�n
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
     * Gibt einen Matrix-Stil f�r die Visualisierung zur�ck
     * 
     * @return Matrix-Stil f�r die Visualisierung
     */
    public static GameStyle getMatrixStyle() {

        return new GameStyle( "MATRIX", //$NON-NLS-1$
                              Color.BLACK,
                              // Neongr�n
                              new Color( 0,
                                         255,
                                         0 ),
                              // Dunkelgr�n
                              new Color( 0,
                                         90,
                                         0 ),
                              // Hellgr�n
                              new Color( 190,
                                         255,
                                         190 ),
                              // Dunkelgr�n
                              new Color( 0,
                                         110,
                                         0 ),
                              // Hellgr�n
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
                              // Neongr�n
                              new Color( 0,
                                         255,
                                         0 ),
                              "data/images/matrix/" ); //$NON-NLS-1$

    }

    /**
     * Gibt einen h�llischen Stil f�r die Visualisierung zur�ck
     * 
     * @return H�llischer Stil f�r die Visualisierung
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
     * Gibt einen himmlischen Stil f�r die Visualisierung zur�ck
     * 
     * @return Himmlischer Stil f�r die Visualisierung
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
     * Gibt den entsprechenden Stil anhand seines Namens zur�ck
     * 
     * @param styleName Name des Stils
     * @return Entsprechender Stil f�r die Visualisierung
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
     * Gibt den Namen des Stils zur�ck
     * 
     * @return Namen des Stils
     */
    public String getStyleName() {

        return styleName;
    }

    /**
     * Gibt die Farbe des Spielbretts zur�ck
     * 
     * @return Farbe des Spielbretts
     */
    public Color getBoardColor() {

        return boardColor;
    }

    /**
     * Gibt die Farbe der Linien auf dem Spielbrett zur�ck
     * 
     * @return Farbe der Linien auf dem Spielbrett
     */
    public Color getLineColor() {

        return lineColor;
    }

    /**
     * Gibt die Farbe der schwarzen Spielsteine zur�ck
     * 
     * @return Farbe der schwarzen Spielsteine
     */
    public Color getBlackColor() {

        return blackColor;
    }

    /**
     * Gibt die Farbe der wei�en Spielsteine zur�ck
     * 
     * @return Farbe der wei�en Spielsteine
     */
    public Color getWhiteColor() {

        return whiteColor;
    }

    /**
     * Gibt die Farbe der markierten schwarzen Spielsteine zur�ck
     * 
     * @return Farbe der markierten schwarzen Spielsteine
     */
    public Color getMarkBlackColor() {

        return markBlackColor;
    }

    /**
     * Gibt die Farbe der markierten wei�en Spielsteine zur�ck
     * 
     * @return Farbe der markierten wei�en Spielsteine
     */
    public Color getMarkWhiteColor() {

        return markWhiteColor;
    }

    /**
     * Gibt die Hintergrundfarbe des Spiels zur�ck
     * 
     * @return Hintergrundfarbe des Spiels
     */
    public Color getBackgroundColor() {

        return backgroundColor;
    }

    /**
     * Gibt die Schrift f�r Spielernamen und Status zur�ck
     * 
     * @return Schrift f�r Spielernamen und Status
     */
    public Font getNameAndStatusFont() {

        return nameAndStatusFont;
    }

    /**
     * Gibt die Schrift f�r die Stein-Statikstik zur�ck
     * 
     * @return Schrift f�r die Steinstatikstik
     */
    public Font getStoneCounterFont() {

        return stoneCounterFont;
    }

    /**
     * Gibt die Schrift f�r die Beschriftungen zur�ck
     * 
     * @return Schrift f�r die Beschriftungen
     */
    public Font getLegendFont() {

        return legendFont;
    }

    /**
     * Gibt den relativen Pfad der Bilddateien zur�ck
     * 
     * @return Relativer Pfad der Bilddateien
     */
    public String getImagePath() {

        return imagePath;
    }

    /**
     * Gibt die Farbe f�r die Schriften zur�ck
     * 
     * @return Farbe f�r die Schriften
     */
    public Color getFontColor() {

        return fontColor;
    }
}
