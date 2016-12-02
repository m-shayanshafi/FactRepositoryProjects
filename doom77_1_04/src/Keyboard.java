import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class Keyboard implements KeyListener, MouseListener {
  public static int lastDir;
  private static HashSet keys; //activated keys for this frame
  private static HashSet downKeys;

  public static void init() {
    lastDir = 0;
    keys = new HashSet(); //activated keys for this frame
    downKeys = new HashSet();
    Doom77.d.addKeyListener(new Keyboard());
    Doom77.d.addMouseListener(new Keyboard());
  }

  synchronized public static void next() {
    for (Iterator it=downKeys.iterator(); it.hasNext();) {
      String key = (String)it.next();
      if (!keys.contains(key)) {
        if (key.equals("up")) {
          lastDir = 0;
        } else if (key.equals("right")) {
          lastDir = 1;
        } else if (key.equals("down")) {
          lastDir = 2;
        } else if (key.equals("left")) {
          lastDir = 3;
        }
      }
    }
    keys = new HashSet();
    keys.addAll(downKeys);
  }

  synchronized public void keyTyped(KeyEvent ke) {
  }

  synchronized public void keyPressed(KeyEvent ke) {
    keyPressed(keyEventToString(ke));
  }

  private static void keyPressed(String key) {
    if (!downKeys.contains(key)) {
      downKeys.add(key);
    }
  }

  synchronized public static boolean mouseClicked() {
    boolean res = keys.contains("mouse");
    downKeys.remove("mouse");
    return res;
  }

  synchronized public void keyReleased(KeyEvent ke) {
    String key = keyEventToString(ke);
    downKeys.remove(key);
  }

  synchronized public static boolean up() {
    return keys.contains("up");
  }

  synchronized public static boolean right() {
    return keys.contains("right");
  }

  synchronized public static boolean down() {
    return keys.contains("down");
  }

  synchronized public static boolean left() {
    return keys.contains("left");
  }

  synchronized public static boolean ctrl() {
    return keys.contains("ctrl");
  }

  synchronized public static boolean shift() {
    return keys.contains("shift");
  }

  synchronized public static boolean space() {
    return keys.contains("space");
  }

  synchronized public static boolean anyKey() {
    return !keys.isEmpty();
  }

  synchronized public void mouseClicked(MouseEvent e) {
    downKeys.add("mouse");
  }

  synchronized public void mouseEntered(MouseEvent e) {
  }

  synchronized public void mouseExited(MouseEvent e) {
  }

  synchronized public void mousePressed(MouseEvent e) {
  }

  synchronized public void mouseReleased(MouseEvent e) {
  }

  private static String keyEventToString(KeyEvent ke) {
    int code = ke.getKeyCode();
    switch (code) {
      case KeyEvent.VK_UP:
        return "up";
      case KeyEvent.VK_DOWN:
        return "down";
      case KeyEvent.VK_LEFT :
        return "left";
      case KeyEvent.VK_RIGHT:
        return "right";
      case KeyEvent.VK_CONTROL:
        return "ctrl";
      case KeyEvent.VK_SHIFT:
        return "shift";
      case KeyEvent.VK_SPACE:
        return "space";
    }
    return "another";      
  }
}
