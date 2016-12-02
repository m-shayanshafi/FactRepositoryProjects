package thaigo.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import thaigo.utility.BlinkLabel;

/**
 * Panel show status of players.
 * 
 * @author Poramate Homprakob 5510546077
 * @version 2013.4.21
 *
 */
public class PlayerPanel extends JPanel {

	private JLabel name;
	private TimeLabel time;
	private JPanel timePanel;

	/** Initializes the PlayerPanel.
	 * 
	 * @param name Player name
	 */
	public PlayerPanel(String name) {
		BoxLayout box = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(box);
		this.setBorder(BorderFactory.createEtchedBorder());

		this.name = new JLabel(name);
		this.add(this.name);
		
		
		timePanel = new JPanel();
		timePanel.setBorder(new TitledBorder("Time Left (s) :"));
		time = new TimeLabel();
		time.setFont( new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		time.setForeground(Color.BLACK);
		time.setBlinkingColor(Color.BLACK, Color.RED);
		timePanel.add(time);
		this.add(timePanel);
		
	}
	/**
	 * Time per turn.
	 * 
	 * @author TG_Dream_Team
	 *
	 */
	class TimeLabel extends BlinkLabel {
		
		public TimeLabel() {
			super("00");
		}
		
		public void setTime(int second) {
			if(second <= 10 && second > 0)
				setBlinking(true);
			else
				setBlinking(false);
			super.setText((second < 10 ? "0" + second : second).toString());
		}
	}
	
	public void setTime(int second) {
		this.time.setTime(second);
	}
}