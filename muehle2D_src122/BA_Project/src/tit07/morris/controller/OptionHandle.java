package tit07.morris.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import tit07.morris.model.Configurateable;
import tit07.morris.view.ActionCommand;
import tit07.morris.view.ViewControllable;


;

/**
 * Die Klasse OptionHandle reagiert auf Benutzeraktionen im Optionsmenü
 */
public class OptionHandle implements ActionListener {

    /** Referenz auf das Model */
    private Configurateable  model;

    /** Referenz auf den View */
    private ViewControllable view;


    /**
     * Erzeugt eine neue Instanz des OptionHandles
     * 
     * @param model Referenz auf das Model
     * @param view Referenz auf den View
     */
    public OptionHandle( Configurateable model, ViewControllable view ) {

        this.view = view;
        this.model = model;
    }

    /**
     * Wird bei Benutzeraktionen automatisch aufgerufen
     * 
     * @param e Ausgelöstes ActionEvent
     */
    public void actionPerformed( ActionEvent e ) {

        /* OK-Button */
        if( e.getActionCommand().equals( ActionCommand.OK ) ) {

            /* Namen werden entsprechend in der Config gespeichert */
            this.setNamesInConfigAndTextField();

            /* Einstellungen werden in der Config gespeichert */
            try {
                this.view.saveOptionParameters();
            }
            catch( Exception e1 ) {
                view.showErrorMsg( e1.getMessage() );
            }

            /* Status Window wird aktualisiert */
            this.view.updateStatusWindow( view.getWidth() );

            /* Optionsmenü wird geschlossen */
            this.view.closeOptionMenu();
        }

        /* ABBRECHEN-Button */
        else if( e.getActionCommand().equals( ActionCommand.CANCEL ) ) {
            this.view.closeOptionMenu();
        }

        /* STANDARD-Button */
        else if( e.getActionCommand().equals( ActionCommand.DEFAULT ) ) {
            this.model.getConfig().setDefault();
            this.view.setIsCallFromStandardButton( true );
            this.view.setSound( this.model.getConfig().isSoundOn() );
            this.view.setAnimation( this.model.getConfig().isAnimationOn() );
            this.view.closeOptionMenu();
            String optionName = view.getOptionName();
            this.view.openOptionMenu( optionName );
        }

        /* ÜBERNEHMEN-Button */
        else if( e.getActionCommand().equals( ActionCommand.APPLY ) ) {
            this.setNamesInConfigAndTextField();
            try {
                this.view.saveOptionParameters();
            }
            catch( Exception e1 ) {
                view.showErrorMsg( e1.getMessage() );
            }
            this.view.enableApplyButton( false );
            this.view.updateStatusWindow( view.getWidth() );
            this.view.repaintAll();
        }
    }

    /**
     * Wenn die KI Auswahl aktiviert wurde, wird der Name der KI als Spielername
     * übernommen, der im Statusfenster angezeigt wird, aber nicht als Name im
     * Textfeld des Optionsmenüs. Wenn Auswahl HUMAN aktiviert, dann wird der
     * Spielername als Name im Statusfenster und als Name im Textfeld
     * übernommen.
     */
    private void setNamesInConfigAndTextField() {

        if( view.isWhiteAIEnabled() ) {

            /* Name für Anzeige im StatusMenu, TextFeld bleibt unverändert */
            String aiName = view.getWhiteAIName();
            this.model.getConfig().setWhiteName( Messages.getString("OptionHandle.ai") + aiName + ")" ); //$NON-NLS-1$ //$NON-NLS-2$
        }
        else {
            String whiteName = this.view.getWhiteName();

            /* Name für Anzeige im StatusMenu */
            this.model.getConfig().setWhiteName( whiteName );

            /* Name für Anzeige im TextFeld */
            this.model.getConfig().setWhiteNameTextField( whiteName );
        }
        if( view.isBlackAIEnabled() ) {

            /* Name für Anzeige im StatusMenu, TextFeld bleibt unverändert */
            String blackName = view.getBlackAIName();
            this.model.getConfig().setBlackName( Messages.getString("OptionHandle.ai") + blackName + ")" ); //$NON-NLS-1$ //$NON-NLS-2$
        }
        else {
            String blackName = view.getBlackName();

            /* Name für Anzeige im StatusMenu */
            this.model.getConfig().setBlackName( blackName );

            /* Name für Anzeige im TextFeld */
            this.model.getConfig().setBlackNameTextField( blackName );
        }
    }
}
