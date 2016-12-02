package tit07.morris.view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import tit07.morris.model.ModelViewable;
import tit07.morris.model.State;
import tit07.morris.model.StoneColor;
import tit07.morris.model.config.GameStyle;
import tit07.morris.exception.IllegalColorException;
import net.miginfocom.swing.MigLayout;


/**
 * Diese Klasse implementiert das Status-Fenster, um dem Benutzer Informationen
 * über den aktuellen Spielverlauf anzuzeigen.
 */
public class StatusWindow extends JPanel {

    /** Referenz auf das Model */
    private ModelViewable model;

    /** Referenz auf den aktuellen GameStyle */
    private GameStyle     currentStyle;

    /** Pfad zu den Pfeil-Grafiken */
    private String        imagePath;

    /** Aktuelle Breite des Fensters in Pixel */
    private int           currentXSize = 0;

    /** Zeiger des weißen Spielers */
    private Icon          iconWhite;

    /** Zeiger des schwarzen Spielers */
    private Icon          iconBlack;

    /** Statusfenster des weißen Spielers */
    private JPanel        panelWhite;

    /** Statusfenster des schwarzen Spielers */
    private JPanel        panelBlack;

    /** Fenster des weißen Zeigers */
    private JPanel        panelArrowWhite;

    /** Fenster des schwarzen Zeigers */
    private JPanel        panelArrowBlack;

    /** Beschriftung des weißen Spielers */
    private JLabel        labelWhite;

    /** Beschriftung des schwarzen Spielers */
    private JLabel        labelBlack;

    /** Name des weißen Spielers */
    private JLabel        whitePlayer;

    /** Name des schwarzen Spielers */
    private JLabel        blackPlayer;

    /** Status des weißen Spielers */
    private JLabel        stateWhite;

    /** Status des schwarzen Spielers */
    private JLabel        stateBlack;

    /** Anzahl der weißen Steine auf dem Spielbrett */
    private JLabel        stonesInWhite;

    /** Anzahl der schwarzen Steine auf dem Spielbrett */
    private JLabel        stonesInBlack;

    /** Beschriftung des weißen Status */
    private JLabel        labelStatusWhite;

    /** Beschriftung des schwarzen Status */
    private JLabel        labelStatusBlack;

    /** Anzahl der weißen Steine außerhalb des Spielbretts */
    private JLabel        stonesOutWhite;

    /** Anzahl der schwarzen Steine außerhalb des Spielbretts */
    private JLabel        stonesOutBlack;

    /** Beschriftung für weißen Fingerzeig */
    private JLabel        labelCueWhite;

    /** Beschriftung für schwarzen Fingerzeig */
    private JLabel        labelCueBlack;

    /** Statuszeile für Meldungen */
    private JTextField    statusLine;

    /** Text der Statuszeile */
    private String        statusText;


    /**
     * Erzeugt eine neue Instanz des Statusfensters
     * 
     * @param model Referenz auf das Model
     */
    public StatusWindow( ModelViewable model ) {

        super();
        this.model = model;
        this.setDoubleBuffered( true );
        this.setLayout( new MigLayout( "", //$NON-NLS-1$
                                       "[0:0,grow 28,pref!]" //$NON-NLS-1$
                                               + "[0:0,grow,fill]" //$NON-NLS-1$
                                               + "[0:0,grow,fill]" //$NON-NLS-1$
                                               + "" //$NON-NLS-1$
                                               + "[0:0,grow 28,pref!]", //$NON-NLS-1$
                                       "[][][][]" ) ); //$NON-NLS-1$

        /* Initialisierung der vier großen Container und der Statuszeile */
        this.panelWhite = new JPanel( new MigLayout( "CENTER", //$NON-NLS-1$
                                                     "10[left]10[left]10", //$NON-NLS-1$
                                                     "10[][]20[][]20[]10" ) ); //$NON-NLS-1$
        this.panelBlack = new JPanel( new MigLayout( "CENTER", //$NON-NLS-1$
                                                     "10[right]10[right]10", //$NON-NLS-1$
                                                     "10[][]20[][]20[]10" ) ); //$NON-NLS-1$
        this.panelArrowWhite = new JPanel( new MigLayout( "align left" ) ); //$NON-NLS-1$
        this.panelArrowBlack = new JPanel( new MigLayout( "align right" ) ); //$NON-NLS-1$
        this.statusLine = new JTextField( "" ); //$NON-NLS-1$
        this.statusLine.setColumns( 800 );
        this.statusLine.setEnabled( false );
        this.statusLine.setDisabledTextColor( Color.RED );
        this.statusLine.setFont( new Font( Font.DIALOG,
                                           Font.BOLD,
                                           12 ) );

        /* Initialisierung der einzelnen Labels */
        this.labelWhite = new JLabel( Messages.getString("StatusWindow.white") ); //$NON-NLS-1$
        this.labelBlack = new JLabel( Messages.getString("StatusWindow.black") ); //$NON-NLS-1$
        this.labelStatusWhite = new JLabel( Messages.getString("StatusWindow.status") ); //$NON-NLS-1$
        this.labelStatusBlack = new JLabel( Messages.getString("StatusWindow.status") ); //$NON-NLS-1$
        this.whitePlayer = new JLabel( "" ); //$NON-NLS-1$
        this.blackPlayer = new JLabel( "" ); //$NON-NLS-1$
        this.stateWhite = new JLabel( "" ); //$NON-NLS-1$
        this.stateBlack = new JLabel( "" ); //$NON-NLS-1$
        this.stonesInWhite = new JLabel( "" ); //$NON-NLS-1$
        this.stonesInBlack = new JLabel( "" ); //$NON-NLS-1$
        this.stonesOutWhite = new JLabel( "" ); //$NON-NLS-1$
        this.stonesOutBlack = new JLabel( "" ); //$NON-NLS-1$

        /* Fülle die vier großen Container mit Inhalt */
        this.fillPanelWhite();
        this.fillPanelBlack();
        this.fillPanelArrowWhite();
        this.fillPanelArrowBlack();

        /* Füge die vier großen Container hinzu */
        this.add( panelWhite );
        this.add( panelArrowWhite );
        this.add( panelArrowBlack );
        this.add( panelBlack, "wrap" ); //$NON-NLS-1$
        this.add( statusLine, "span" ); //$NON-NLS-1$

        /* Lade die aktuellen Daten */
        this.setVisible( true );
    }

    /**
     * Füllt den Container vom weißen Spieler mit Inhalt
     */
    private void fillPanelWhite() {

        this.panelWhite.add( labelWhite, "cell 0 0 1 1" ); //$NON-NLS-1$
        this.panelWhite.add( whitePlayer, "cell 1 0 1 2" ); //$NON-NLS-1$
        this.panelWhite.add( labelStatusWhite, "cell 0 2 1 1" ); //$NON-NLS-1$
        this.panelWhite.add( stateWhite, "cell 1 2 1 2" ); //$NON-NLS-1$
        this.panelWhite.add( stonesInWhite, "cell 0 4 2 1" ); //$NON-NLS-1$
    }

    /**
     * Füllt den Container vom schwarzen Spieler mit Inhalt
     */
    private void fillPanelBlack() {

        this.panelBlack.add( labelBlack, "cell 1 0 1 1" ); //$NON-NLS-1$
        this.panelBlack.add( blackPlayer, "cell 0 0 1 2" ); //$NON-NLS-1$
        this.panelBlack.add( labelStatusBlack, "cell 1 2 1 1" ); //$NON-NLS-1$
        this.panelBlack.add( stateBlack, "cell 0 2 1 2" ); //$NON-NLS-1$
        this.panelBlack.add( stonesInBlack, "cell 0 4 2 1" ); //$NON-NLS-1$
    }

    /**
     * Füllt den Container vom weißen Pfeil mit Inhalt
     */
    private void fillPanelArrowWhite() {

        this.iconWhite = new ImageIcon( this.model.getConfig().getGameStyle()
                                                  .getImagePath()
                + "left_M.gif" ); //$NON-NLS-1$
        this.labelCueWhite = new JLabel( iconWhite );
        this.panelArrowWhite.add( labelCueWhite );
    }

    /**
     * Füllt den Container vom schwarzen Pfeil mit Inhalt
     */
    private void fillPanelArrowBlack() {

        this.iconBlack = new ImageIcon( this.model.getConfig().getGameStyle()
                                                  .getImagePath()
                + "right_M.gif" ); //$NON-NLS-1$
        this.labelCueBlack = new JLabel( iconBlack );
        this.panelArrowBlack.add( labelCueBlack );
    }

    /**
     * Gleicht das Statusmenü mit den aktuellen Werten des Spiels ab
     */
    public void update( int width ) {

        /* Spielernamen */
        this.whitePlayer.setText( this.model.getConfig().getWhiteName() );
        this.blackPlayer.setText( this.model.getConfig().getBlackName() );

        /* Status der Spieler */
        this.stateWhite.setText( this.getPlayerState( StoneColor.WHITE ) );
        this.stateBlack.setText( this.getPlayerState( StoneColor.BLACK ) );

        /* Statistik der Spielsteine */
        try {
            this.stonesInWhite.setText( Messages.getString("StatusWindow.stones_field") //$NON-NLS-1$
                    + " "+model.getStonesIn( StoneColor.WHITE )
                    + Messages.getString("StatusWindow.to_set") //$NON-NLS-1$
                    + " "+model.getStonesOut( StoneColor.WHITE ) );
            this.stonesInBlack.setText( Messages.getString("StatusWindow.stones_field") //$NON-NLS-1$
                    + " "+model.getStonesIn( StoneColor.BLACK )
                    + Messages.getString("StatusWindow.to_set") //$NON-NLS-1$
                    + " "+model.getStonesOut( StoneColor.BLACK ) );
            this.stonesOutWhite.setText( Messages.getString("StatusWindow.to_set_stones") //$NON-NLS-1$
                    + " "+model.getStonesOut( StoneColor.WHITE ) );
            this.stonesOutBlack.setText( Messages.getString("StatusWindow.to_set_stones") //$NON-NLS-1$
                    + " "+model.getStonesOut( StoneColor.BLACK ) );
        }
        catch( Exception e ) {
        }

        /* Pfeile welcher Spieler dran ist */
        this.updateImage( width );

        /* Schriftarten und Hintergrund */
        this.updateFontsAndColors();

        /* Setze Fingerzeig auf aktiven spieler */
        this.setCue();

        /* Statuszeile */
        this.statusLine.setText( this.statusText );
    }

    /**
     * Beschriftet die StatusLine mit dem übergebenen Text
     * 
     * @param msg Die Nachricht die erscheinen soll
     */
    public void setStatusLine( String msg ) {

        this.statusText = msg;
    }

    /**
     * Gibt den aktuellen Status des jeweiligen Spielers als String zurück
     */
    private String getPlayerState( StoneColor stoneColor ) {

        String state = ""; //$NON-NLS-1$
        try {
            switch( model.getState( stoneColor ) ) {
            case SET:
                state = state + Messages.getString("StatusWindow.set_stone"); //$NON-NLS-1$
                break;
            case MOVE:
                state = state + Messages.getString("StatusWindow.move"); //$NON-NLS-1$
                break;
            case JUMP:
                state = state + Messages.getString("StatusWindow.jump"); //$NON-NLS-1$
                break;
            case REMOVE:
                state = state + Messages.getString("StatusWindow.remove"); //$NON-NLS-1$
                break;
            case WAIT:
                state = state + Messages.getString("StatusWindow.wait"); //$NON-NLS-1$
                break;
            case LOSER:
                state = state + Messages.getString("StatusWindow.lost"); //$NON-NLS-1$
                break;
            case WINNER:
                state = state + Messages.getString("StatusWindow.won"); //$NON-NLS-1$
                break;
            case DRAW:
                state = state + Messages.getString("StatusWindow.draw"); //$NON-NLS-1$
                break;
            }
        }
        catch( IllegalColorException e ) {
        }
        return state;
    }

    /**
     * Aktualisiert die Schriftarten und Farben anhand der Config
     */
    private void updateFontsAndColors() {

        if( this.currentStyle != this.model.getConfig().getGameStyle() ) {

            /* Schriftarten */
            this.currentStyle = model.getConfig().getGameStyle();
            Font legendFont = currentStyle.getLegendFont();
            Font nameAndStatusFont = currentStyle.getNameAndStatusFont();
            Font stoneCounterFont = currentStyle.getStoneCounterFont();
            this.labelWhite.setFont( legendFont );
            this.labelWhite.setForeground( currentStyle.getFontColor() );
            this.labelBlack.setFont( legendFont );
            this.labelBlack.setForeground( currentStyle.getFontColor() );
            this.whitePlayer.setFont( nameAndStatusFont );
            this.whitePlayer.setForeground( currentStyle.getFontColor() );
            this.blackPlayer.setFont( nameAndStatusFont );
            this.blackPlayer.setForeground( currentStyle.getFontColor() );
            this.stateWhite.setFont( nameAndStatusFont );
            this.stateWhite.setForeground( currentStyle.getFontColor() );
            this.stateBlack.setFont( nameAndStatusFont );
            this.stateBlack.setForeground( currentStyle.getFontColor() );
            this.stonesInWhite.setFont( stoneCounterFont );
            this.stonesInWhite.setForeground( currentStyle.getFontColor() );
            this.stonesInBlack.setFont( stoneCounterFont );
            this.stonesInBlack.setForeground( currentStyle.getFontColor() );
            this.labelStatusWhite.setFont( legendFont );
            this.labelStatusWhite.setForeground( currentStyle.getFontColor() );
            this.labelStatusBlack.setFont( legendFont );
            this.labelStatusBlack.setForeground( currentStyle.getFontColor() );

            /* Hintergrundfarbe */
            this.panelWhite.setBackground( currentStyle.getBoardColor() );
            this.panelBlack.setBackground( currentStyle.getBoardColor() );
            this.panelArrowWhite
                                .setBackground( currentStyle
                                                            .getBackgroundColor() );
            this.panelArrowBlack
                                .setBackground( currentStyle
                                                            .getBackgroundColor() );
            this.setBackground( model.getConfig().getGameStyle()
                                     .getBackgroundColor() );
        }
    }

    /**
     * Aktualisiert die Zeiger-Grafik anhand der Fenstergröße
     */
    private void updateImage( int width ) {

        if( this.currentStyle != model.getConfig().getGameStyle()
                || this.currentXSize != width ) {

            /* Pfad mit den Grafikdateien */
            this.imagePath = model.getConfig().getGameStyle().getImagePath();

            int xSize = width;
            this.currentXSize = xSize;

            /* Je nach Breite das Fenster, desto größer die Grafik */
            if( xSize >= 1000 ) {
                this.iconWhite = new ImageIcon( imagePath + "left_XL.gif" ); //$NON-NLS-1$
                this.iconBlack = new ImageIcon( imagePath + "right_XL.gif" ); //$NON-NLS-1$
            }
            else if( xSize < 1000 && xSize >= 850 ) {
                this.iconWhite = new ImageIcon( imagePath + "left_L.gif" ); //$NON-NLS-1$
                this.iconBlack = new ImageIcon( imagePath + "right_L.gif" ); //$NON-NLS-1$
            }
            else if( xSize < 850 && xSize >= 750 ) {
                this.iconWhite = new ImageIcon( imagePath + "left_M.gif" ); //$NON-NLS-1$
                this.iconBlack = new ImageIcon( imagePath + "right_M.gif" ); //$NON-NLS-1$
            }
            else if( xSize < 750 && xSize >= 700 ) {
                this.iconWhite = new ImageIcon( imagePath + "left_S.gif" ); //$NON-NLS-1$
                this.iconBlack = new ImageIcon( imagePath + "right_S.gif" ); //$NON-NLS-1$
            }
            else if( xSize < 700 ) {
                this.iconWhite = new ImageIcon( imagePath + "left_XS.gif" ); //$NON-NLS-1$
                this.iconBlack = new ImageIcon( imagePath + "right_XS.gif" ); //$NON-NLS-1$
            }
        }

        /* Fügt dem Label das neue Icon hinzu und lässt es neu zeichnen */
        this.labelCueWhite.setIcon( this.iconWhite );
        this.labelCueWhite.paintComponents( labelCueWhite.getGraphics() );
        this.labelCueBlack.setIcon( this.iconBlack );
        this.labelCueBlack.paintComponents( labelCueBlack.getGraphics() );
    }

    /**
     * Setzt den Fingerzeig auf den Spieler der dran ist
     */
    private void setCue() {

        try {
            if( ( this.model.getState( StoneColor.WHITE ) ) == State.WAIT
                    || ( model.getState( StoneColor.WHITE ) ) == State.LOSER ) {

                this.labelCueWhite.setVisible( false );
                this.labelCueBlack.setVisible( true );
            }
            else {
                this.labelCueWhite.setVisible( true );
                this.labelCueBlack.setVisible( false );
            }
        }
        catch( IllegalColorException e ) {
        }
    }
}
