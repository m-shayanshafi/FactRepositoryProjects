import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.Timer;

public class GTextIO extends JComponent
{
    public static void dramatic(Graphics g, String dialogue){
        Graphics2D g2 = (Graphics2D) g;
        int x = 75;
        int y = 75;
        Font fixedsys;
        fixedsys = new Font ("Fixedsys", Font.PLAIN , 11);
        /*fixedsys = loadFont(); 
        GraphicsEnvironment ge = 
        GraphicsEnvironment.getLocalGraphicsEnvironment();                                          //add this block when loadFont() actually works
        ge.registerFont(fixedsys);*/
        g2.setFont(fixedsys);
        for(char c : dialogue.toCharArray()){
            g2.drawString(String.valueOf(c),x,y);
            x += 10;
            try{
                Thread.sleep(400);
            } catch(Exception e){

            }
            if (x > 1000){                                                                                //replace with intended cutoff
                x = 75;                                                                                         
                y = y + 20;                                                                             //replace with actual height or whatever
            }
        }
    }   

    public static Font loadFont(){
        Font font = null; 
        try (InputStream stream = GTextIO.class.getResourceAsStream("vgafix.ttf")) {
            font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(50f);
        } catch(FontFormatException | IOException e){
            System.out.println("Something went wrong" + e);            
        }
        return font;
    }

}