package kabalpackage.info;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import kabalpackage.*;

/**
 * Displays information on the application.
 */
public class AboutWindow extends JFrame{
    
    private final String aboutText[] = {
            "  Java Solitaire Project is a Klondike solitaire ",
            "  card game written by three first year software ",
            "  engineering students, with no former knowledge ",
            "  of programming.\n",
            " ",
            "\n  We do not provide any support for this program ",
            "  since, as students, we are very, very busy people. ",
            "  If you're feeling lucky, you can still try to ",
            "  contact us through the SourceForge-page."
    };
    
    /** Creates a new instance of AboutWindow */
    public AboutWindow() {
        
        // Set look and feel to GTK
	try {
	    String GTK = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
	    UIManager.setLookAndFeel(GTK);
	    UIManager.installLookAndFeel("GTK", GTK);
	} catch (Exception e) {
	    System.err.println("Could not install GTK");
	}
        
        try{
            Image iconImage = ImageIO.read(getClass()
                    .getResourceAsStream("../images/icon.gif") );
            setIconImage(iconImage);
        }
        catch(Exception e){
            System.err.println("Could not load icon.");
        }
        
        setTitle("About");
        setLayout(new BorderLayout(7, 7));
        setSize(new Dimension(400,300));
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);        
        
        JLabel appTitle = new JLabel(" Java Solitaire Project 1.0");
        appTitle.setFont(new Font("Sans", Font.BOLD, 24));
        add(appTitle, BorderLayout.NORTH);
        

        add(new TextPanel(), BorderLayout.CENTER);
        
        
        JPanel buttonPanel = new ButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    
    private class TextPanel extends JPanel{
        
        public TextPanel(){}
        
        protected void paintComponent(Graphics graphics){
            Graphics2D g = (Graphics2D)graphics;
            
            Rectangle panelBounds = this.getBounds();
            int panelXStart = (int)panelBounds.getX();
            int panelYStart = (int)panelBounds.getY();
            
            
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setFont(new Font("Sans", Font.BOLD, 14));
            g.setColor(Color.BLACK);
            int i = 0;
            for(String string : aboutText){
                g.drawString(string, 0, panelYStart+(i*15) );
                i++;
            }
        }
        
        
    }
    
    private class ButtonPanel extends JPanel{
        
        public ButtonPanel(){
            
            setLayout(new FlowLayout());
            
            JButton creditsButton = new JButton("Credits");
            JButton licenseButton = new JButton("License");
            JButton closeButton = new JButton("Close");
            
            creditsButton.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        new CreditsWindow();
                    }
                }
            );
            
            licenseButton.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        new LicenseWindow();
                    }
                }
            );
            
            closeButton.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        AboutWindow.this.dispose();
                    }
                }
            );
            
            add(creditsButton);
            add(licenseButton);
            add(closeButton);
            
        }
        
    }
    
   
    
    public static void main(String[] args){
        new AboutWindow();
    }
}
