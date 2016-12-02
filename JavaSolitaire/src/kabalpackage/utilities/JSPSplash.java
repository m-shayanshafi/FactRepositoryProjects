package kabalpackage.utilities;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.image.BufferedImage;

/**
 * Custom splash screen.
 */
public class JSPSplash extends JFrame implements Runnable{
    
    /** Creates a new instance of SplashScreen */
    public JSPSplash() {
        
        setTitle("Starting Java Solitaire Project");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        setUndecorated(true);
        setFocusable(true);
        setLayout(null);
        setBackground(null);        
        setSize(500,300);
        
        SplashPanel splashPanel = new SplashPanel();
        splashPanel.addMouseListener(
            new MouseInputAdapter(){
                public void mouseClicked(MouseEvent e){
                    JSPSplash.this.dispose();
                }
            }
        );
        
        // We want the splash screen to have focus all the time until it
        // is disposed.
        this.addFocusListener(
            new FocusListener(){
                public void focusLost(FocusEvent fe){
                    requestFocus();
                }
                public void focusGained(FocusEvent fe){}
            }
        );
        
        add(splashPanel);
        
        setLocationRelativeTo(null);
    }
    
    public void run(){
        
        int i = 0;      
        while(i == 0){
            
            i++;
            
            try{
                Thread.sleep(4000);
                System.out.println(i);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        
        this.dispose();
    }
    
    private class SplashPanel extends JPanel{
        
        private BufferedImage splashImage = null;
        
        public SplashPanel(){
            setSize(500,300);
            
            try{
                splashImage = ImageIO.read(getClass().getResourceAsStream(
                        "images/splash.png"));
            }
            catch(Exception e){
                System.err.println("Could not load splash image");
                e.printStackTrace();
            }
        }
        
        protected void paintComponent(Graphics graphics){
            Graphics2D g = (Graphics2D)graphics;
            
            if(splashImage != null){
                g.drawImage(splashImage, null, 0, 0);
            }
            
            g.dispose();
        }
        
    }
    
    public static void main(String[] args){
        JSPSplash splash = new JSPSplash();
        Thread splashThread = new Thread(splash);
        splashThread.start();
        splash.setVisible(true);
    }
}
