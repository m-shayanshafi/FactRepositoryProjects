import javax.swing.JComponent;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class ImageDrawer extends JComponent
{
    private int image_index; //index of image in array
    public static BufferedImage[] listOfImages = new BufferedImage[4]; //array of images

    public ImageDrawer(int index)
    {
        try {
            //get all images from resources folder and resize to screen size, add them to array
            listOfImages[0] = util.resize_img(util.screen_size,ImageIO.read(new File("Resources/Splashes/OpeningSplash.jpg")));
            listOfImages[1] =  util.resize_img(util.screen_size,ImageIO.read(new File("chapter1.jpg")));
            listOfImages[2] =  util.resize_img(util.screen_size,ImageIO.read(new File("chapter2.jpg")));
            listOfImages[3] =  util.resize_img(util.screen_size,ImageIO.read(new File("chapter3.jpg")));
        } catch (IOException e) {
        }

        image_index = index; //set current index from the explicit parameter of the constructor
    }

    public static BufferedImage getImage(int index) //get image at index
    {
        return listOfImages[index]; //return the image specified by index
    }

    public void paintComponent(Graphics g) //paint component
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(listOfImages[image_index], 0, 0, this); //draw the image across the entire screen
    }
}
