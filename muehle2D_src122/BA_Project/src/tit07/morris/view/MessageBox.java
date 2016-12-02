package tit07.morris.view;

import java.awt.Component;
import javax.swing.JOptionPane;


/**
 * Die Klasse MessageBox dient zur Anzeige einer Nachricht in einem Dialog,
 * welcher das Parent-Fenster blockiert.
 */
public class MessageBox extends JOptionPane {

    /** Referenz auf das abhängige Fenster */
    private Component parent;


    /**
     * Erzeugt eine neue Nachrichtenbox
     * 
     * @param parent Parent-Fenster, welches blockiert werden soll.
     */
    public MessageBox( Component parent ) {

        this.parent = parent;
    }

    /**
     * Zeigt eine Fehlermeldung an.
     * 
     * @param msg Text der Fehlermeldung
     */
    public void showErrorMessage( String msg ) {

        JOptionPane.showMessageDialog( parent,
                                       msg,
                                       Messages.getString("MessageBox.error"), //$NON-NLS-1$
                                       JOptionPane.ERROR_MESSAGE );
    }

    /**
     * Zeigt eine Infomeldung an.
     * 
     * @param msg Text der Infomeldung
     */
    public void showInfoMessage( String msg ) {

        JOptionPane.showMessageDialog( parent,
                                       msg,
                                       Messages.getString("MessageBox.information"), //$NON-NLS-1$
                                       JOptionPane.INFORMATION_MESSAGE );
    }
}
