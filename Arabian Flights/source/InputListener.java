// InputListener.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;


/** The input listener handles all input for the game. It allows direct checking
 * of key states and processes mouse movements.
 */
public class InputListener implements KeyListener, MouseListener, MouseMotionListener, KeyEventPostProcessor
{
  // used for calculating mouse movement
  private int mouselast_x;
  private int mouselast_y;

  // keeps track of which keys are down
  private boolean [] keysdown = new boolean[256];

  // keeps track of the mouse buttons being down
  private boolean lmousedown = false;
  private boolean rmousedown = false;

  // a java.awt.robot  is needed to keep the cursor in the center of the screen
  private Robot robot;

  // find the middle of the screen
  private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  public int midscreen_x = (int)screenSize.getWidth()/2;
  public int midscreen_y = (int)screenSize.getHeight()/2;

  // this is used to pass mX and mY to the cameraBehavior
  private volatile Point point;

  // these are the total movments of the mouse between frames
  private int mX = 0, mY = 0;

  private boolean ignore_next_mouse = false;
  private boolean ignore_all_mouse = true;

  /** this gets all key events. */
  public boolean postProcessKeyEvent(KeyEvent e)
  {
    if (e.getID() == KeyEvent.KEY_PRESSED) this.keyPressed(e);
    if (e.getID() == KeyEvent.KEY_RELEASED) this.keyReleased(e);
    return true;
  }

  // default constructor
  public InputListener()
  {
    try
    {
      robot = new Robot();
    }
    catch (AWTException e) { System.out.println("Robot Creation Failed");}
  }

  /** Sets the mouse position */
  private void setMousePos(int x, int y)
  {
    ignoreNextMouseMove();
    robot.mouseMove(x, y);
  }

  /** Tracks the state of keys. */
  public void keyReleased(KeyEvent e)
  {
    keysdown[e.getKeyCode()] = false;
  }

  /** Tracks the state of keys. */
  public void keyPressed(KeyEvent e)
  {
    int key = e.getKeyCode();

    if (keysdown[key] == false)
    {
      if (key == KeyEvent.VK_ESCAPE) GameFrame.exit();
      if (key == KeyEvent.VK_E) GameFrame.player.selectNextSpell();
      if (key == KeyEvent.VK_Q) GameFrame.player.selectPreviousSpell();

      if (key == KeyEvent.VK_1) GameFrame.player.selectCastle();
      if (key == KeyEvent.VK_2) GameFrame.player.selectFirebolt();
      if (key == KeyEvent.VK_3) GameFrame.player.selectClaim();
      if (key == KeyEvent.VK_4) GameFrame.player.selectSummonSpider();
      if (key == KeyEvent.VK_5) GameFrame.player.selectFirewave();
      if (key == KeyEvent.VK_6) GameFrame.player.selectSummonFirestalk();
      if (key == KeyEvent.VK_7) GameFrame.player.selectMeteor();


      if (key == KeyEvent.VK_R) Profiler.reset();
      if (key == KeyEvent.VK_T) Profiler.report();

        //cheat code!
      if (key == KeyEvent.VK_G) GameFrame.player.increaseGold(10);

      if (key == KeyEvent.VK_P) screenCapture();
    }

    keysdown[key] = true;
  }

  // make a screenshot
  private void screenCapture()
  {
    try
    {
      Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
      Rectangle rect = new Rectangle(0, 0, dim.width, dim.height);
      Robot robot = new Robot();
      BufferedImage bi = robot.createScreenCapture(rect);

      File file = new File(System.currentTimeMillis() + ".png");
      FileOutputStream out = new FileOutputStream(file);

      ImageIO.write(bi, "png", file);
      System.out.println("Screencapped: " + file.getName());
    }
    catch (Exception ex)
    {
      System.out.println(ex.getMessage());
    }
  }


  /** This adds each mouse movement to the current delta */
  public void mouseMoved(MouseEvent e)
  {
    if (ignore_all_mouse) return;

    if(!ignore_next_mouse)
    {
      Point newpoint = e.getPoint();
      mX -= midscreen_x - newpoint.getX(); //update x
      mY -= midscreen_y - newpoint.getY(); //update y

      setMousePos(midscreen_x,midscreen_y);
    }
    else
    {
      ignore_next_mouse=false; //next one is not to be ignored
    }
  }

  /** This causes the listner to ignore the next mouse movement. */
  public void ignoreNextMouseMove()
  {
    ignore_next_mouse = true;
  }

  /** This starts the mouse recognition. */
  public void startMouseRead()
  {
    ignore_all_mouse = false;
  }


  /** Tracks mouse events. */
  public void mouseDragged(MouseEvent e)
  {
    // MouseDrags need to be treated just like MouseMoves
    mouseMoved(e);
  }

  /** This returns the total change in the mouse position since it was last checked.  */
  public Point lastMouseMove()
  {
    if (point == null) point = new Point();
    point.setLocation(mX, mY);

    mX = 0;//<-reset x
    mY = 0;//<-reset y

    return point;
  }

  /** This returns true if the key is down.
   * (uses KeyEvent.VK_* key codes)
   */
  public boolean keyDown(int key)
  {
    return keysdown[key];
  }

  /** This returns true if the left mouse button is down. */
  public boolean LbuttonDown()
  {
    return this.lmousedown;
  }

  /** This returns true if the right mouse button is down. */
  public boolean RbuttonDown()
  {
    return this.rmousedown;
  }

  /** Tracks mouse events. */
  public void mouseReleased(MouseEvent e)
  {
    if (e.getButton() == MouseEvent.BUTTON1) this.lmousedown = false;
    else this.rmousedown = false; // any other button is considered rmouse
  }

  /** Tracks mouse events. */
  public void mousePressed(MouseEvent e)
  {
    if (e.getButton() == MouseEvent.BUTTON1) this.lmousedown = true;
    else this.rmousedown = true; // any other button is considered rmouse
  }

  // these are required by the interface, but unused.
  public void keyTyped(KeyEvent e) {  }
  public void mouseExited(MouseEvent e) { }
  public void mouseEntered(MouseEvent e) {  }
  public void mouseClicked(MouseEvent e) {  }

}