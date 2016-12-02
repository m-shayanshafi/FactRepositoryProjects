/*
 * ProfilePopup.java
 *
 * Created on 27 June 2006, 13:51
 * @author Guy
 * Copywrite 27 June 2006 Guy Wittig
 *
 */

package mvplan.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import mvplan.gui.components.*;
import mvplan.model.*;

public class ProfilePopup extends JDialog {
	private static final long serialVersionUID = -385349049947610918L;

	ModelDisplayComponent dc;
    Model model;
    /** Creates a new instance of ProfilePopup */
    public ProfilePopup(JFrame frame, String title, Model model) {
        super(frame, title);
        this.model=model;        
        
        setSize(new Dimension(200,200));                
        dc = new ModelDisplayComponent(model);       
        dc.setMinimumSize(new Dimension(100,100));
        add(dc,BorderLayout.CENTER );
        
        // Set ESCAPE key to close dialog
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction(){
			private static final long serialVersionUID = 1004342196844948595L;

			public void actionPerformed(ActionEvent e){
                setVisible(false);
            }
        };
        getRootPane().getInputMap(JComponent.WHEN_FOCUSED).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE",escapeAction);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(frame);
        setVisible(true);           
    }
    
    public void setModel(Model model){
        this.model=model;
        dc.setModel(model);        
    }
    
}
