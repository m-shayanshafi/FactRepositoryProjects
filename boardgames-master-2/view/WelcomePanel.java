package view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class WelcomePanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton m;
	private JButton s;
	private JButton cc;
	private JCheckBox box;
	

	/**
	 * Constructors to create a WelcomePanel object
	 */
	public WelcomePanel(){
		setSize(710, 300);
		setLocation(50, 50);
		setLayout(null);
		//setBackground(Color.BLUE);
		initElementss();
	}
	
	/**
	 * Initialize the elements, buttons and check box.
	 */
	private void initElementss(){
		m = new JButton(Images.ICON_MANCALA);
		m.setSize(200, 200);
		m.setLocation(0, 50);
		m.addActionListener(new ButtonAction());
		add(m);
		s = new JButton(Images.ICON_SENET);
		s.setSize(200, 200);
		s.setLocation(250, 50);
		s.addActionListener(new ButtonAction());
		add(s);
		cc = new JButton(Images.ICON_CC);
		cc.setSize(200, 200);
		cc.setLocation(500, 50);
		cc.addActionListener(new ButtonAction());
		add(cc);
		box = new JCheckBox("Single Player", true);
		box.setLocation(300, 250);
		box.setSize(200, 50);
		add(box);
		
	}

	/**
	 * Override the paintComponent in the JComponent, draw String as title.
	 * @param g the Graphics context in which to paint.
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Font f = g.getFont();
		g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 50));
		g.drawString("Choose a game", 160, 30);
		g.setFont(f);
		//g.fillRect(0, 0, 30, 30);
	}

	/**
	 * 
	 * @author Libra
	 *The inner class design for the action listener
	 */
	private class ButtonAction implements ActionListener{
		
		/**
		 * Implements the ActionListener interface, build the action of buttons.
		 * @param e ActionEvent object
		 */
		public void actionPerformed(ActionEvent e) {
          UI ui =  (UI)SwingUtilities.windowForComponent(WelcomePanel.this);
  		System.out.println(ui == null);
			if(e.getSource().equals(m)){
				
				
				ui.setScorePanelVisible(true);
				ui.setRollPanelVisible(false);
				ui.launchMancala(box.isSelected());
			}
			else if(e.getSource().equals(s)){
				ui.setScorePanelVisible(true);
				ui.setRollPanelVisible(true);
				ui.launchSenet(box.isSelected());
				
			}
			else if(e.getSource().equals(cc)){
				ui.setScorePanelVisible(false);
				ui.setRollPanelVisible(false);
				ui.launchChineseChecker(box.isSelected());
			}
			else{
				System.out.println("Error");
			}
		}
		
	}
	
	
	
}
