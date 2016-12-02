import javax.swing.JComponent;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
//import javafx.embed.swing.JFXPanel;
//import javafx.scene.*;

public class Starter
{
    static final JFrame frame = new JFrame("Swords & Sworcery");
    static final OurGraphics graphics = new OurGraphics();
    static final JFileChooser fileChooser = new JFileChooser();
    static private int currentFrame = 0;
    public static void main (String Args[]){

        frame.setSize((int)util.screen_size.getWidth(),(int)util.screen_size.getHeight());

        String[] yesOrNo = {"Yes","No"};

        String response = (String) JOptionPane.showInputDialog(null, "Would you like to Load a Saved Game?", "Load Game?", JOptionPane.PLAIN_MESSAGE,null, yesOrNo, yesOrNo[0]);
        if (response.equals("Yes")) {

            System.out.println("This is working");

            int userSelection = fileChooser.showOpenDialog(new JLabel());

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToOpen = fileChooser.getSelectedFile();
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileToOpen)))   {
                    Inventory.setListOfPotions((Item[])in.readObject());
                } catch (IOException ioe) {
                    System.out.println("This is not working due to the first thing");
                    ioe.printStackTrace();
                } catch (ClassNotFoundException ioe)
                {
                    System.out.println("This is not working due to the second thing");
                    ioe.printStackTrace();
                }
            }
        }
        else    {

        }

        // //        final JFXPanel fxPanel = new JFXPanel(); 
        //         frame.add(fxPanel); 
        //         initFX(fxPanel); 
        //         frame.setDefaultfullScreenButtonOperation(JFrame.EXIT_ON_fullScreenButton);

        GameLogic.character.setSprites(new String[] {"Resources/Assets/character/front.PNG"});
        GameLogic.character.setPosition(100,100);
        GameLogic.character.set_curr_sprite(0);

        //e.moveTo(new Point(500,500), 5000);
        graphics.addEntity(GameLogic.character);
        frame.add(graphics.getGraphics(0));
        frame.setUndecorated(true);

        frame.setVisible(true);

    }
    //   private static void initFX(JFXPanel fxPanel) {
    //   }
    public static int getCurrentFrame()
    {
        return currentFrame;
    }
    public static void setCurrentFrame(int f)
    {
        currentFrame = f;
    }
}
