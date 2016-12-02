
package kw.texasholdem.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import kw.texasholdem.config.Action;
import kw.texasholdem.config.AppConfig;

/**
 * Panel with buttons to let a human player select a poker action.
 * 
 * @author Ken Wu
 */
public class ControlPanel extends JPanel implements ActionListener , Serializable {
    
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;
    
    /** Monitor while waiting for user input. */
    private final Object monitor = new Object();
    
    /** The Check button. */
    private final JButton checkButton;
    
    /** The Call button. */
    private final JButton callButton;
    
    /** The Bet button. */
    private final JButton betButton;
    
    /** The Raise button. */
    private final JButton raiseButton;
    
    /** The Fold button. */
    private final JButton foldButton;
    
    /** The Continue button. */
    private final JButton continueButton;

    /** The selected action. */
    private Action action;
    
    /**
     * Constructor.
     * 
     * @param main
     *            The main frame.
     */
    public ControlPanel() {
    	//setOpaque(false);
        setLayout(new FlowLayout());
        continueButton = createActionButton(Action.CONTINUE);
        checkButton = createActionButton(Action.CHECK);
        callButton = createActionButton(Action.CALL);
        betButton = createActionButton(Action.BET);
        raiseButton = createActionButton(Action.RAISE);
        foldButton = createActionButton(Action.FOLD);
        
        this.add(continueButton);
        this.add(checkButton);
        this.add(callButton);
        this.add(betButton);
        this.add(raiseButton);
        this.add(foldButton);
        
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(AppConfig.APP_WIDTH_2, 40));
        
        hideAll();
    }
    
    /**
     * Waits for the user to click the Continue button.
     */
    public void waitForUserInput() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                hideAll();
                unHide(continueButton);
                validate();
                repaint();}
        });
        Set<Action> allowedActions = new HashSet<Action>();
        allowedActions.add(Action.CONTINUE);
        getUserInput(allowedActions);
    }
    
    protected void unHide(JButton butt) {
    	butt.setVisible(true);
	}

	protected void hideAll() {
        this.continueButton.setVisible(false);
        this.checkButton.setVisible(false);
        this.callButton.setVisible(false);
        this.betButton.setVisible(false);
        this.raiseButton.setVisible(false);
        this.foldButton.setVisible(false);
	}

	/**
     * Waits for the user to click an action button and returns the selected
     * action.
     * 
     * @param allowedActions
     *            The allowed actions.
     * 
     * @return The selected action.
     */
    public Action getUserInput(final Set<Action> allowedActions) {
    	
    	action = null;
    	
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Show the buttons for the allowed actions.
            	hideAll();
                if (allowedActions.contains(Action.CONTINUE)) {
                	unHide(continueButton);
                } else {
                    if (allowedActions.contains(Action.CHECK)) {
                    	unHide(checkButton);
                    }
                    if (allowedActions.contains(Action.CALL)) {
                    	unHide(callButton);
                    }
                    if (allowedActions.contains(Action.BET)) {
                    	unHide(betButton);
                    }
                    if (allowedActions.contains(Action.RAISE)) {
                    	unHide(raiseButton);
                    }
                    if (allowedActions.contains(Action.FOLD)) {
                    	unHide(foldButton);
                    }
                }
                validate();
                repaint();
            }
        });
        
        synchronized (monitor) {
            try {
                monitor.wait();
            } catch (InterruptedException e) {
                // Ignore.
            }
        }
        return action;
        
    }
    
    public void interruptOfTheUserWaitThread() {
    	synchronized (monitor) {
            monitor.notifyAll();
        }
    }
    
    /*
     * (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	hideAll();
                validate();
                repaint();
            }
        });
        
        if (source == continueButton) {
            action = Action.CONTINUE;
        } else if (source == checkButton) {
            action = Action.CHECK;
        } else if (source == callButton) {
            action = Action.CALL;
        } else if (source == betButton) {
            action = Action.BET;
        } else if (source == raiseButton) {
            action = Action.RAISE;
        } else {
            action = Action.FOLD;
        }
        synchronized (monitor) {
            monitor.notifyAll();
        }
    }
    
    /**
     * Creates an action button.
     * 
     * @param action
     *            The action.
     * 
     * @return The button.
     */
    private JButton createActionButton(Action action) {
        String label = action.getName();
        JButton button = new JButton(label);
        button.setMnemonic(label.charAt(0));
        button.setSize(100, 30);
        button.addActionListener(this);
        return button;
    }

}
