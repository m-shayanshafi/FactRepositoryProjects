import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Gfx {
  public static int w;
  public static int wh;//half width
  public static int h;
  public static int hh;//half height
  public static int[] screen;
  public static B b;
  public static java.util.Map<String, Sprite> sprites;

  private static Image screenImg;
  private static MemoryImageSource M = null;

  private static long time;
  private static int fps;
  private static int fpsCounter;

  public static void init() {
    w = Doom77.d.getSize().width;
    wh = w/2;
    h = Doom77.d.getSize().height;
    hh = h/2;
    screen = new int[w*h];
    b = new B(screen, w, h);
    M = new MemoryImageSource(w, h, new DirectColorModel(32, 0xff0000, 65280, 255, 0), screen, 0, w);
    M.setAnimated(true);
    M.setFullBufferUpdates(true);
    screenImg = Doom77.d.createImage(M);
    sprites = new HashMap<String, Sprite>();
    time = System.currentTimeMillis();
    fps = 0;
    fpsCounter = 0;
  }

  public static Sprite loadSprite(String name, int width, int height) {
    Sprite s = new Sprite("zz_"+name, width, height);
    sprites.put(name.substring(0, name.length()-4), s);
    return s;
  }

  public static void draw() {
    fpsCounter++;
    if (fpsCounter == 100) {
      fps = (int)(100000 / (System.currentTimeMillis()-time));
      time = System.currentTimeMillis();
      fpsCounter = 0;
    }
    F.p(""+fps+" FPS", w-60, h-20);
    F.p("HEALTH: "+health()+"%", wh-45, h-20);
    if (Adventure.startTime>0) F.p(""+(int)((Doom77.time-Adventure.startTime)/1000), 5, h-20);
    Doom77.d.getGraphics().drawImage(screenImg, 0, 0, Doom77.d);
    M.newPixels();
  }

  private static int health() {
    Player p = Player.player;
    int res = (int)((double)p.life/p.maxLife*100);
    return res<0?0:res;
  }
}
