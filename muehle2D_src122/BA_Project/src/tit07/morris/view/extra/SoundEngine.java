package tit07.morris.view.extra;

import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import tit07.morris.model.Configurateable;


/**
 * Die Klasse SoundEngine ist für das Abspielen der Sounds zuständig. Dafür
 * stellt sie Methoden zur Verfügung, um einen Sound abzuspielen.
 */
public class SoundEngine {

    /**
     * Pfad für den Sound: Setze Stein auf das Spielfeld
     */
    public final static String SET_SOUND    = "data/sound/set.sound"; //$NON-NLS-1$

    /**
     * Pfad für den Sound: Bewege Stein auf dem Spielfeld
     */
    public final static String MOVE_SOUND   = "data/sound/move.sound"; //$NON-NLS-1$

    /**
     * Pfad für den Sound: Springe mit Stein auf dem Spielfeld
     */
    public final static String JUMP_SOUND   = "data/sound/jump.sound"; //$NON-NLS-1$

    /**
     * Pfad für den Sound: Entferne Stein auf vom Spielfeld
     */
    public final static String REMOVE_SOUND = "data/sound/remove.sound"; //$NON-NLS-1$

    /**
     * Pfad für den Sound: Mensch hat gegen Computer verloren
     */
    public final static String GAME_OVER    = "data/sound/gameover.sound"; //$NON-NLS-1$

    /**
     * Pfad für den Sound: Mensch hat gewonnen
     */
    public final static String WINNER       = "data/sound/winner.sound"; //$NON-NLS-1$

    /** Referenz auf das Model */
    private Configurateable    model;


    /**
     * Erzeugt eine neue Instanz der Sound-Engine
     * 
     * @param model Referenz auf das Model
     */
    public SoundEngine( Configurateable model ) {

        this.model = model;
    }

    /**
     * Spielt den Sound vom übergebenen Pfad ab, wenn Sounds aktiviert sind.
     * Wenn der Pfad ungültig ist, wird der Sound nicht abgespielt
     * 
     * @param soundPath Pfad zu der Sounddatei
     */
    public void playSound( String soundPath ) {

        if( !model.getConfig().isSoundOn() ) {
            return;
        }
        try {
            /* Datei mit Audiodaten */
            File audioFile = new File( soundPath );

            /* Input-Stream öffnen */
            AudioInputStream audioStream = AudioSystem
                                                      .getAudioInputStream( audioFile );

            /* Audio-Format abfragen */
            AudioFormat audioFormat = audioStream.getFormat();

            /* Gesamtgröße abfragen */
            int audioSize = (int) ( audioFormat.getFrameSize() * audioStream
                                                                            .getFrameLength() );

            /* Audiodaten in Byte-Array schreiben */
            byte[] audioData = new byte[ audioSize ];
            audioStream.read( audioData, 0, audioSize );

            /* Informationen der Dataline abfragen */
            DataLine.Info info = new DataLine.Info( Clip.class,
                                                    audioFormat,
                                                    audioSize );

            /* Audio-Line abfragen */
            Clip clip = (Clip) AudioSystem.getLine( info );

            /* Clip öffnen und starten */
            clip.open( audioFormat, audioData, 0, audioSize );
            clip.start();
        }
        catch( Exception e ) {
        }
    }
}
