package tit07.morris.view;

import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowListener;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;
import javax.swing.event.MenuListener;


/**
 * Legt die Schnittstellen fest welcher der Controller haben muss um, die Events
 * des Views abzufangen.
 */
public interface Reactable extends ActionListener, ChangeListener,
                          ComponentListener, DocumentListener, MenuListener,
                          MouseListener, WindowListener {
}
