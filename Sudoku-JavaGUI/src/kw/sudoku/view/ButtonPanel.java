package kw.sudoku.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import kw.sudoku.controller.ButtonController;
import kw.sudoku.model.Game;
import kw.sudoku.model.Solution;
import kw.sudoku.model.UpdateAction;

/**
 * This class draws the button panel and reacts to updates from the model.
 *
 * @author Ken Wu @ New York
 */
public class ButtonPanel extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;
    JButton m_btnNew, m_btnCheck, m_btnExit;   // Used buttons.
    JCheckBox m_cbHelp;               // Used check box.
    ButtonGroup bgNumbers;          // Group for grouping the toggle buttons.
    JToggleButton[] m_btnNumbers;     // Used toggle buttons.

    JPanel m_pnlAlign = new JPanel();
    
    JPanel m_defaultNumberPanel = null;
    JPanel m_solutionPanel = null;
	private ButtonController m_buttonController;
    
    
    /**
     * Constructs the panel and arranges all components.
     */
    public ButtonPanel() {
        super(new BorderLayout());

        
        m_pnlAlign.setLayout(new BoxLayout(m_pnlAlign, BoxLayout.PAGE_AXIS));
        add(m_pnlAlign, BorderLayout.NORTH);

                
        constructInstructionPanelAndSetIt();
        
        m_pnlAlign.add(m_defaultNumberPanel);
        
        /*
        m_cbHelp = new JCheckBox("Enable Help", true);
        m_cbHelp.setFocusable(false);
        pnlNumbersHelp.add(m_cbHelp);
		*/

    }

    private void constructInstructionPanelAndSetIt() {
    	if( m_defaultNumberPanel == null) {
    		m_defaultNumberPanel = new JPanel();
    		m_defaultNumberPanel.setLayout(new BoxLayout(m_defaultNumberPanel, BoxLayout.PAGE_AXIS));
    		m_defaultNumberPanel.setBorder(BorderFactory.createTitledBorder("Pick a number and fill into the Sudoku board"));
	        
	        JPanel pnlNumbersHelp = new JPanel(new FlowLayout(FlowLayout.LEADING));
	        m_defaultNumberPanel.add(pnlNumbersHelp);
	
	        JPanel pnlNumbersNumbers = new JPanel(new FlowLayout(FlowLayout.LEADING));
	        m_defaultNumberPanel.add(pnlNumbersNumbers);
	
	        bgNumbers = new ButtonGroup();
	        m_btnNumbers = new JToggleButton[9];
	        for (int i = 0; i < 9; i++) {
	        	m_btnNumbers[i] = new JToggleButton("" + (i + 1));
	        	m_btnNumbers[i].setPreferredSize(new Dimension(40, 40));
	        	m_btnNumbers[i].setFocusable(false);
	            bgNumbers.add(m_btnNumbers[i]);
	            pnlNumbersNumbers.add(m_btnNumbers[i]);
	        }
    	}
    	if(m_solutionPanel != null) {
    		this.m_pnlAlign.remove(m_solutionPanel);
    	}
		this.m_pnlAlign.add(m_defaultNumberPanel);
		
	}

    private void constructSolutionPanelAndSetIt(Game g) {
    	Set<Solution> sols = g.getCalculatedSols();
    	m_solutionPanel = new JPanel();
		m_solutionPanel.setLayout(new BoxLayout(m_solutionPanel, BoxLayout.PAGE_AXIS));
	
	    JPanel pnlNumbersNumbers = new JPanel(new FlowLayout(FlowLayout.LEADING));
	    m_solutionPanel.add(pnlNumbersNumbers);
		m_solutionPanel.setBorder(BorderFactory.createTitledBorder("There are "+sols.size()+" solutions found.  Please select below to view"));
		
		//Now construct a JList
		String [] solDescriptions = new String [sols.size()];
		for (int i=1; i<=sols.size(); i++) {
			solDescriptions[i-1] = "Solution #" + (i);
		}
		JComboBox  jlst = new JComboBox (solDescriptions);
		jlst.setSize(200, 20);
		jlst.setSelectedIndex(0);
		jlst.addActionListener(m_buttonController);
		JPanel listPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		listPanel.add(jlst);
		m_solutionPanel.add(listPanel);
		
		if(m_defaultNumberPanel != null) {
			this.m_pnlAlign.remove(m_defaultNumberPanel);
		}
		this.m_pnlAlign.add(m_solutionPanel);
	}
    
	/**
     * Method called when model sends update notification.
     *
     * @param o     The model.
     * @param arg   The UpdateAction.
     */
    public void update(Observable o, Object arg) {
        switch ((UpdateAction)arg) {
            case NEW_GAME:
            	constructInstructionPanelAndSetIt();
            	break;
            case CHECK:
                bgNumbers.clearSelection();
                break;
            case SHOW_SOLUTIONS:
            	constructSolutionPanelAndSetIt((Game)o);
                break;
		default:
			break;
        }
    }



	/**
     * Adds controller to all components.
     *
     * @param buttonController  Controller which controls all user actions.
     */
    public void setController(ButtonController buttonController) {
    	m_buttonController = buttonController;
    	//m_cbHelp.addActionListener(buttonController);
        for (int i = 0; i < 9; i++)
        	m_btnNumbers[i].addActionListener(m_buttonController);
    }

}