package kabalpackage.info;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import kabalpackage.*;

/**
 * Displays information on the makers of this application.
 */
public class CreditsWindow extends JFrame{
        
    private final String[] credits = {
        "  This application was made by the following people ",
        "  over the course of 5-6 weeks:",
        "\n\n",
        "  Magnus Holmang Kleming: magnushk@stud.hist.no",
        "  \nOeyvind Valen-Sendstad: oyvindv@stud.hist.no",
        "  \nMartin Myhrstuen: martinmy@stud.hist.no",
        "\n\n",
        "  You may contact Magnus at any time, but an answer",
        "  to your inquiry is not in any way guaranteed."
    };
        

    public CreditsWindow(){
        
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

        setTitle("Credits");
        setLayout(new BorderLayout(7, 7));
        setSize(new Dimension(400,300));
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);        

        JLabel appTitle = new JLabel(" Credits");
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
            for(String string : credits){
                g.drawString(string, 0, panelYStart+(i*15) );
                i++;
            }
        }        
    }

    
    private class ButtonPanel extends JPanel{
        
        public ButtonPanel(){
            
            setLayout(new FlowLayout());
            
            JButton closeButton = new JButton("Close");
               
            closeButton.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        CreditsWindow.this.dispose();
                    }
                }
            );
            
            add(closeButton);      
        }
    }
    
    
    public static void main(String[] args){
        new CreditsWindow();
    }
    
}
