package thaigo.utility;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

/** JLabel that can blink.
 * 
 * @author Nol 5510546018
 * @version 2013.04.11
 */
public class BlinkLabel extends JLabel {
	
	/** The blink rate. */
	private static final int BLINK_RATE = 500;
	
	/** Turn on or off the blink ability. */
	private boolean blinkingOn = false;
	
	/** Use to set the lable color back to normal when stop blinking. */
	private boolean setToDefault = false;
	
	/** Timer that make the lable blink at a constant rate. */
	private Timer timer;
	
	/** Default foreground color. */
	private Color fg;
	
	/** Default background color. */
	private Color bg;
	
	/** Preferred colors when the label blinks. */
	private Color color1,color2;
	
	/** Initializes the BlinkLabel.
	 * 
	 * @param text Text in the label
	 */
	public BlinkLabel(String text){
		super(text);
		fg = this.getForeground();
		bg = this.getBackground();
		timer = new Timer(BLINK_RATE , new TimerListener(this));
		timer.setInitialDelay(0);
		timer.start();
	}
	
	/** Toggles blink ability on or off.
	 * 
	 * @param bool true = On / false = Off
	 */
	public void setBlinking(boolean bool){
		if(!bool) setToDefault = true;
		blinkingOn = bool;
	}
	
	/** Tells if the label is blinking or not.
	 * 
	 * @return true if label is blinking, else return false
	 */
	public boolean getBlinking(){
		return blinkingOn;
	}
	
	/** Sets the default foreground color
	 * 
	 * @param color default foreground color
	 */
	public void setDefaultForeground(Color color){
		super.setForeground(color);
		fg = color;
	}
	
	/** Gets the default foreground color.
	 * 
	 * @return default foreground color.
	 */
	public Color getDefaultForeground(){
		return fg;
	}
	
	/** Sets the default background color
	 * 
	 * @param color default background color
	 */
	public void setDefaultBackground(Color color){
		super.setBackground(color);
		bg = color;
	}
	
	/** Sets the colors the will be used when the label blinks.
	 * 
	 * @param c1 first color
	 * @param c2 second color
	 */
	public void setBlinkingColor(Color c1, Color c2){
		color1 = c1;
		color2 = c2;
	}
	
	/** Timer that makes the label blink in a constant rate.
	 * 
	 * @author Nol 5510546018
	 *
	 */
	private class TimerListener implements ActionListener{
		/** The blinkable label. */
		private BlinkLabel bl;
		
		/** Uses to switch the foreground color between 2 colors. */
		private boolean isForeground = true;
		
		/** If the user set the preferred blink color or not. */
		private boolean nullPreferedColor = false;
		
		/** Initializes the TimerListener. 
		 * 
		 * @param bl BlinkLabel
		 */
		public TimerListener(BlinkLabel bl){
			this.bl = bl;
		}
		
		/** Makes the label blink (if the blink ability is turned on) between 2 preferred colors or
		 *  between its default foreground and background colors if the preferred colors are not set.
		 *  
		 *  @param event Event
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			if(color1 == null && color2 == null){
				nullPreferedColor = true;
				color1 = fg;
				color2 = bg;
			}
			if(bl.getBlinking()){
				if(isForeground)
					bl.setForeground(color1);
				else
					bl.setForeground(color2);
				isForeground = !isForeground;
			}
			else{
				if(setToDefault){
					bl.setForeground(fg);
					setToDefault = false;
				}
				if(nullPreferedColor){
					nullPreferedColor = false;
					color1 = null;
					color2 = null;
				}
			}
			
		}
		
	}
}
