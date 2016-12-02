package kabalpackage.utilities;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import kabalpackage.*;


/**
 * The timer class. A runnable JPanel.
 */
public class SolTimer extends JPanel implements Runnable{
    
    // The time the timer has run
    private int timeRun = 0;

    // The JLabel that displays the time
    private JLabel timerDisplay = new JLabel("");
    
    // Variables used to determine the timer status
    private boolean RUN = true;
    private final int PLAYING = 0;
    private final int PAUSED = 1;
    private final int GAME_OVER = -1;
    private int status = 1;
    
    
    /**
     * Creates a new instance of SolTimer.
     */
    public SolTimer(){
        setBackground(null);
        setLayout(new FlowLayout());
        timerDisplay.setText("Playing time: 0:00");
        try{
            // Create new font from file
            Font tmpCustomFont = Font.createFont(Font.TRUETYPE_FONT, 
                    ( getClass().getResourceAsStream("font.ttf")));
            // Creat yet another font derived from the other one, so that we
            // can set the font size.
            Font customFont = tmpCustomFont.deriveFont(14f);
            // Set the font on the JLabel to this new truetype font
            timerDisplay.setFont( customFont  );
        }
        catch(FontFormatException ffe){ System.err.println("FFE"); }
        catch(IOException IOe){ System.err.println("IOe"); }
        
        // Set font color
        timerDisplay.setForeground(Color.WHITE);
        
        // Add the JLabel
        add(timerDisplay);        
    }
    
    /**
     * Pauses the timer.
     */
    public void pauseTimer(){
        status = PAUSED;
        timerDisplay.setText("[ Pause ]");
    }
    
    /**
     * Resumes the timer.
     */
    public void resumeTimer(){
        status = PLAYING;
    }
    
    /**
     * Resets the timer.
     */
    public void resetTimer(){
        timeRun = 0;
        status = PAUSED;
        timerDisplay.setSize(400, (int)(timerDisplay.getSize().getHeight()) );
        timerDisplay.setText("Playing time: 0:00");
    }
    
    /**
     * Sets the timer to display [ Game over! ]
     */
    public void gameOver(){
        status = GAME_OVER;
        timerDisplay.setText("[ Game over! ]");
    }
    
    /**
     * Returns the time in seconds.
     */
    public int getTime(){
        return timeRun;
    }
    
    /**
     * The main method of the class.
     */
    public void run(){
        
        while(RUN){
            
            int sec = 0;
            int min = 0;
            
            while (status == PLAYING) {
                timeRun++;

                min = timeRun / 60;
                sec = timeRun - min*60;

                if(sec < 10) timerDisplay.setText("Playing time: " 
                        + min + ":0" + sec);
                else timerDisplay.setText("Playing time: " + min + ":" + sec);
                
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {}
            }
            
            // To prevent the unnecessary use of system resources we set the
            // loop to a 1 second cycle. This will result in a 1 second lag
            // when you pause and resume, but it's the only quick way that
            // we know of which will enable us to do this. If not, the loop
            // will execute constantly making the whole program painfully slow.
            try {
                Thread.sleep(1000);
            } 
            catch (Exception e) {}
        }
    }

    
    /**
     * Paints the background of the component.
     */
    protected void paintComponent(Graphics graphics) {

        Graphics g = graphics.create();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, LayoutVariables.WINDOW_WIDTH, 
                LayoutVariables.WINDOW_HEIGHT);
        for(int i=0; i<LayoutVariables.WINDOW_WIDTH; i++){
        }
        g.dispose();

    } 
}
