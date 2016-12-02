package tit07.morris;

import javax.swing.UIManager;
import tit07.morris.controller.Controller;


/**
 * Einstiegsklasse von welcher das Spiel aus gestartet wird.
 */
public final class Entry {

    /**
     * Privater Konstruktor, da keine Instanzen der Entry Klasse erzeugt werden
     * sollen.
     */
    private Entry() {

    }

    /**
     * Einstiegsmethode, welche alle für das Spiel erforderliche Objekte
     * erzeugt.
     * 
     * @param args Kommandozeilenparameter (werden nicht ausgewertet)
     */
    public static void main( String[] args ) {        
        try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
            //UIManager.setLookAndFeel( "com.sun.java.swing.plaf.motif.MotifLookAndFeel" );
            //UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
        }
        catch( Exception e ) {            
        }
        finally {
            new Controller();
        }
    }
}
