import javax.swing.JComponent;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
public abstract class util
{
    public static final Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize(); //system screen size
    static Point map_to_frame(JFrame frame, Point p) //map a point on the image in 1920,1080 space to the screen
    {
        Dimension d = frame.getContentPane().getSize(); //get frame size
        int x = (int)((d.getWidth()*(p.getX()))/(screen_size.getWidth())); //use ratio to map point x and y
        int y = (int)((d.getHeight()*(p.getY()))/(screen_size.getHeight()));
        p.setLocation(x,y); //set location of the point
        return p; // return the point

    }

    static BufferedImage resize_img(Dimension d, BufferedImage i) //resize an image to a dimension d
    {
        //uses the getScaledInstance method of buffered image to provide a scaled version of the image
        return toBufferedImage(i.getScaledInstance((int)d.getWidth(),(int)d.getHeight(), Image.SCALE_FAST));

    }
    public static Point screen_point_adjust(Point p) //adjust for screen offset, just calibration
    {
        p.setLocation(p.getX() - 160, p.getY() -200); //calibrates point
        return p; //returns output
    }
    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
    public static BufferedImage blur(BufferedImage img) //gaussian blur method
    {
        //create an array used to adjust individual pixels for a blur effect
        float[] bits = {1.0f/9.0f, 1.0f/9.0f, 1.0f/9.0f, 1.0f/9.0f, 1.0f/9.0f, 1.0f/9.0f, 1.0f/9.0f, 1.0f/9.0f, 1.0f/9.0f};;
        Kernel kernel = new Kernel(3,3,bits); //create a kernel using the offset array created above
        ConvolveOp convolve_op = new ConvolveOp(kernel); //convolute the pixels using the kernel array
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), img.getType()); //create new image
        convolve_op.filter(img, out); //filter img into out using the convolveop created above
        return out; //return modified image
    }
}