import javax.swing.JComponent;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
//////
////class to animate movement of the character////
//////
public class moveThreader extends Thread //creates new thread when constructed
{
    private volatile int d; //duration of movement
    private volatile Point p; //point to move to
    private volatile Entity e; //entity to move
    private static volatile boolean cancel = false; //cancel all threads
    public moveThreader(Entity _e, Point _p, int _d)
    {
        e=_e; // set explicit variables to those passed to the constructor
        p=_p;
        d=_d;
        this.start(); //start the thread
    }

    public void run() //method called by the threader
    {
        cancel = false; 
        long millis = System.currentTimeMillis(); //get start time
        double x_orig = e.getPosition().getX(); // get original x and y coords
        double y_orig = e.getPosition().getY();
        double x_distance = p.getX() - e.getPosition().getX(); //get total distance to move on x and y axis
        double y_distance = p.getY() - e.getPosition().getY();
        double curr_time = System.currentTimeMillis() - millis; // get current time, adjusted for start offset
        while (curr_time < d && !cancel) //while not canceled and not out of time
        {
            curr_time = System.currentTimeMillis() - millis; //uodate time
            e.setPosition((int)(((curr_time/d))*x_distance+x_orig), (int)(((curr_time/d))*y_distance+y_orig)); // determine current distance  as a ratio of elapsed time/total time
           //e.setPosition((int)(((curr_time/d))*x_distance+x_orig), piecewise_handler.getY((int)(((curr_time/d))*x_distance+x_orig), Starter.getCurrentFrame()));
        }

        

    }

    public static void cancel()
    {
        
        cancel = true; //cancel all threads
    }
}
