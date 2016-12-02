package kw.sudoku.view;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;

import kw.sudoku.controller.ButtonController;

/**
 * This class controls all user actions from ButtonPanel.
 *
 * @author Ken Wu @ New York
 */

public class MenuPanel extends JMenuBar {
	private static final long serialVersionUID = 1L;
	JMenuItem m_fileItem1 = new JMenuItem("New");
	JMenuItem m_saveItem = new JMenuItem("Save");
	JMenuItem m_giveMeSolution = new JMenuItem("Solution");
    JMenuItem m_checkItem = new JMenuItem("Check");
    JMenuItem m_undoItem = new JMenuItem("Undo");
    JMenuItem m_fileItem3 = new JMenuItem("Exit");
    JMenuItem m_helpitem = new JMenuItem("About");
	
    JRadioButtonMenuItem m_rbLevelItem_1;
    JRadioButtonMenuItem m_rbLevelItem_2;
    JRadioButtonMenuItem m_rbLevelItem_3;
    JRadioButtonMenuItem m_rbLevelItem_4;
    JRadioButtonMenuItem m_rbLevelItem_5;
    JRadioButtonMenuItem m_rbLevelItem_6;
    
	public MenuPanel() {
		init();
	}
	
	void init() {
		
        
        JMenu gamemenu = new JMenu("Game");
        
        gamemenu.add(m_fileItem1);
        gamemenu.add(new JSeparator());
        gamemenu.add(m_giveMeSolution);
        gamemenu.add(new JSeparator());
        
        ButtonGroup  group = new ButtonGroup ();
        m_rbLevelItem_1 = new JRadioButtonMenuItem("Level - Very Easy");
        group.add(m_rbLevelItem_1);
        gamemenu.add(m_rbLevelItem_1);
        
        m_rbLevelItem_2 = new JRadioButtonMenuItem("Level - Easy");
        group.add(m_rbLevelItem_2);
        gamemenu.add(m_rbLevelItem_2);
        
        m_rbLevelItem_3 = new JRadioButtonMenuItem("Level - Medium");
        m_rbLevelItem_3.setSelected(true);
        group.add(m_rbLevelItem_3);
        gamemenu.add(m_rbLevelItem_3);
        
        m_rbLevelItem_4 = new JRadioButtonMenuItem("Level - Hard");
        group.add(m_rbLevelItem_4);
        gamemenu.add(m_rbLevelItem_4);
        
        m_rbLevelItem_5 = new JRadioButtonMenuItem("Level - Very Hard");
        group.add(m_rbLevelItem_5);
        gamemenu.add(m_rbLevelItem_5);
        
        m_rbLevelItem_6 = new JRadioButtonMenuItem("Level - Expert");
        group.add(m_rbLevelItem_6);
        gamemenu.add(m_rbLevelItem_6);
        
        gamemenu.add(new JSeparator());
        
        gamemenu.add(m_saveItem);
        gamemenu.add(new JSeparator());
        gamemenu.add(m_fileItem3);
        this.add(gamemenu);
        
        JMenu actionmenu = new JMenu("Action");
        actionmenu.add(m_checkItem);
        actionmenu.add(m_undoItem);
        this.add(actionmenu);
        
        JMenu helpmenu = new JMenu("Help");
        helpmenu.add(new JSeparator());
        helpmenu.add(m_helpitem);
        this.add(helpmenu);
	}
	
    /**
     * Adds controller to all components.
     *
     * @param buttonController  Controller which controls all user actions.
     */
    public void setController(ButtonController buttonController) {
    	m_giveMeSolution.addActionListener(buttonController);
    	m_fileItem1.addActionListener(buttonController);
    	m_saveItem.addActionListener(buttonController);
    	m_checkItem.addActionListener(buttonController);
    	m_undoItem.addActionListener(buttonController);
    	m_fileItem3.addActionListener(buttonController);
    	m_helpitem.addActionListener(buttonController);
    	
    	m_rbLevelItem_1.addActionListener(buttonController);
    	m_rbLevelItem_2.addActionListener(buttonController);
    	m_rbLevelItem_3.addActionListener(buttonController);
    	m_rbLevelItem_4.addActionListener(buttonController);
    	m_rbLevelItem_5.addActionListener(buttonController);
    	m_rbLevelItem_6.addActionListener(buttonController);
        
    }
	
}
