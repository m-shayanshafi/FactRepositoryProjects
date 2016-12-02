package tit07.morris.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import tit07.morris.view.ActionCommand;
import tit07.morris.view.ViewControllable;


/**
 * Das ChangeHandle stellt fest, ob Einträge im Optionsmenü verändert wurden.
 * Veränderungen im Optionsmenü haben Einfluss, welche Elemente im Optionsmenü
 * aktiviert und deaktiviert werden sollen. Außerdem muss der Übernehmen-Button
 * bei Veränderungen aktiviert werden.
 */
public class ChangeHandle implements ActionListener, ChangeListener,
                         DocumentListener {

    /** Referenz auf den View */
    private ViewControllable view;


    /**
     * Erzeugt eine neue Instanz des ChangeHandlers
     * 
     * @param view Referenz auf den View
     */
    public ChangeHandle( ViewControllable view ) {

        this.view = view;
    }

    /**
     * Wird bei Benutzaktionen im Optionsmenü aufgerufen
     * 
     * @param e Ausgelöstes ActionEvent
     */
    @Override
    public void actionPerformed( ActionEvent e ) {

        /* Übernehmen-Button aktivieren */
        if( e.getActionCommand().equals( ActionCommand.ENABLE_APPLY ) ) {
            this.enableApplyButton();
        }
        /* Weißer Spieler: Mensch */
        else if( e.getActionCommand().equals( ActionCommand.HUMAN_WHITE ) ) {
            this.view.enableWhiteAI( false );
            this.view.enableWhiteName( true );
            this.enableApplyButton();
        }
        /* Schwarzer Spieler: Mensch */
        else if( e.getActionCommand().equals( ActionCommand.HUMAN_BLACK ) ) {
            this.view.enableBlackAI( false );
            this.view.enableBlackName( true );
            this.enableApplyButton();
        }
        /* Weißer Spieler: KI */
        else if( e.getActionCommand().equals( ActionCommand.AI_WHITE ) ) {
            this.view.enableWhiteAI( true );
            this.view.enableWhiteName( false );
            this.enableApplyButton();
        }
        /* Schwarzer Spieler: KI */
        else if( e.getActionCommand().equals( ActionCommand.AI_BLACK ) ) {
            this.view.enableBlackAI( true );
            this.view.enableBlackName( false );
            this.enableApplyButton();
        }
    }

    /**
     * Wird bei Benutzaktionen im Optionsmenü aufgerufen
     * 
     * @param arg0 Ausgelöstes ChangeEvent
     */
    @Override
    public void stateChanged( ChangeEvent arg0 ) {

        /* Aktiviert den Übernehmen-Button */
        this.enableApplyButton();
    }

    /**
     * Wird bei Benutzaktionen im Optionsmenü aufgerufen
     * 
     * @param arg0 Ausgelöstes DocumentEvent
     */
    @Override
    public void changedUpdate( DocumentEvent arg0 ) {

        /* Aktiviert den Übernehmen-Button */
        this.enableApplyButton();
    }

    /**
     * Wird bei Benutzaktionen im Optionsmenü aufgerufen
     * 
     * @param arg0 Ausgelöstes DocumentEvent
     */
    @Override
    public void insertUpdate( DocumentEvent arg0 ) {

        /* Aktiviert den Übernehmen-Button */
        this.enableApplyButton();
    }

    /**
     * Wird bei Benutzaktionen im Optionsmenü aufgerufen
     * 
     * @param arg0 Ausgelöstes DocumentEvent
     */
    @Override
    public void removeUpdate( DocumentEvent arg0 ) {

        /* Aktiviert den Übernehmen-Button */
        this.enableApplyButton();
    }

    /**
     * Aktiviert den Übernehmen-Button
     */
    private void enableApplyButton() {

        this.view.enableApplyButton( true );
    }
}
