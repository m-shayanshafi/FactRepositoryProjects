package kw.texasholdem.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import kw.texasholdem.config.AppConfig;
import kw.texasholdem.util.ResourceManager;

import javax.swing.SwingUtilities;

public class IntroPanel extends JPanel {

	Image img;
	private final int WIDTH;
	private final int HEIGHT;
	private TexasHoldemMainPanel parent;
	private JTextField thisPlayerNameTextBox;
	
	public IntroPanel(TexasHoldemMainPanel p) {
		parent = p;
		img = ResourceManager.getImage(AppConfig.BACKGROUND_IMG);
		WIDTH = img.getWidth(null);
		HEIGHT = img.getHeight(null);
		this.setSize(WIDTH, HEIGHT);
		
		
		JPanel jb = createInfoPanel();
		
		
		this.setLayout(null);
		
	    // Caculate panel location after showing the JFrame in order to get the right insets (window's title bar).
	    //int panelX = (getWidth() - jb.getWidth() - getInsets().left - getInsets().right) / 2;
	    //int panelY = ((getHeight() - jb.getHeight() - getInsets().top - getInsets().bottom) / 2);
	    //jb.setLocation(panelX, panelY);
	    
	    this.add(jb);
	    Insets insets = jb.getInsets();
	    Dimension size = jb.getPreferredSize();
	    jb.setBounds(268+insets.left, 215+insets.top, size.width, size.height);
		
	}
	
	 private JPanel createInfoPanel() {
		JPanel j = new JPanel(new SpringLayout());
		j.setOpaque(true);
		j.setBackground(new Color(0,0,0,0));
        j.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(null, "Configuration", TitledBorder.LEFT, TitledBorder.TOP, null, Color.WHITE),
                BorderFactory.createEmptyBorder(2,2,2,2)));
		
		thisPlayerNameTextBox = new JTextField("Ken Wu", 15);
		JLabel name = new JLabel("Name:", SwingConstants.LEFT);
		name.setForeground(Color.WHITE);
		name.setAlignmentX(Component.LEFT_ALIGNMENT);
		j.add(name);
		j.add(thisPlayerNameTextBox);
		JLabel num = new JLabel("Num of players:", SwingConstants.LEFT);
		num.setForeground(Color.WHITE);
		j.add(num);
		final JComboBox comboNumOfPlayer = (JComboBox) createComboBoxNumOfPlayersDesired();
		j.add(comboNumOfPlayer);
		
		
		j.add(Box.createRigidArea(new Dimension(0,0)));
		JButton jsubmit = new JButton("Okay");
		j.add(jsubmit, BorderLayout.WEST);
		
		jsubmit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				parent.setPlayerName(thisPlayerNameTextBox.getText());
				parent.setNumOfPlayer(Integer.parseInt((String) comboNumOfPlayer.getSelectedItem()));
				parent.switchToFrame(TexasHoldemMainPanel.GAME_PANEL);
				parent.gameStart();
			}
		});
		
        //Lay out the panel.
		
		SpringUtilities.makeGrid(j,
                                 3, 2, //rows, cols
                                 1, 1, //initialX, initialY
                                 2, 2);//xPad, yPad
		
		return j;
	}

	private Component createComboBoxNumOfPlayersDesired() {
		String[] nStrings = { "4", "5", "6", "7", "8" };
		JComboBox comboBoxnumOfPlayers = new JComboBox(nStrings);
		comboBoxnumOfPlayers.setSelectedIndex(2);
		comboBoxnumOfPlayers.setPreferredSize(new Dimension(30, 20));
		comboBoxnumOfPlayers.setSize(new Dimension(30, 20));
		comboBoxnumOfPlayers.setPrototypeDisplayValue("Number of players");
		return comboBoxnumOfPlayers;
	}

	@Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        g.drawImage(img, 0, 0, null); // see javadoc for more info on the parameters
	        
	    }

}
