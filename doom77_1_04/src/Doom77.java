import java.applet.*;
import java.awt.*;
import java.util.*;

public class Doom77 extends Applet implements Runnable {//main class
  public static Doom77 d;
  public static double time;
  public static double step;

  private static Thread thread;
  private static boolean exception;

  private static void loadSprites() {
    Sprite bluered = Gfx.loadSprite("bluered.png", 400, 400);
    bluered.tile();
    Sprite greenred = new Sprite("greenred", bluered);
    new B(greenred.p, greenred.w, greenred.h).bluegreen();
    Sprite kitch = Gfx.loadSprite("kitch.png", (int)Engine.size/3, (int)Engine.size/3);
    kitch.tile(3);
    Sprite greenkitch = new Sprite("greenkitch", kitch);
    new B(greenkitch).horizontalColor(0, 1, 0);
    Sprite redkitch = new Sprite("redkitch", kitch);
    new B(redkitch).horizontalColor(1, 0, 0);
    Sprite round = new Sprite("roundl", kitch);
    new B(round).leftrightroundColor(0, 1, 0, true);
    round = new Sprite("roundr", kitch);
    new B(round).leftrightroundColor(0, 1, 0, false);
    round = new Sprite("redroundl", kitch);
    new B(round).leftrightroundColor(1, 0, 0, true);
    round = new Sprite("redroundr", kitch);
    new B(round).leftrightroundColor(1, 0, 0, false);
    Sprite spotlight = new Sprite("spotlight", kitch);
    new B(spotlight).spotlight(0, 1, 0);
    Gfx.loadSprite("shot.png", 50, 50);
    Gfx.loadSprite("gahan.png", 253, 86);
    Sprite manson = Gfx.loadSprite("manson.png", 364, 113);
    Sprite mansonHead = Gfx.loadSprite("manson_head.png", 67, 71);
    Gfx.sprites.remove("manson_head");
    Sprite heartedManson = new Sprite(manson);
    for (int x=0; x<62; x++) {
      for (int y=0; y<113; y++) {
        heartedManson.p[y*364+x] = -1;
      }
    }
    new B(heartedManson).d(mansonHead, 0, 16);
    Gfx.sprites.put("heartedManson", heartedManson);
    Gfx.loadSprite("blood.png", 63, 177);
  }

  public void init() {
    d = this;
    time = System.currentTimeMillis();
    exception = false;
    Keyboard.init();
    enableEvents(AWTEvent.KEY_EVENT_MASK);
    enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    Gfx.init();
    F.init(0xffffffff, 0xff000000, Gfx.b);
    loadSprites();
    Map.init();
    Adventure.init();
    System.gc();
  }

  public void update(Graphics g) {
    paint(g);
  }

  public synchronized void paint(Graphics g) {
    Object[] ar = new Object[10];
    try {
      step = System.currentTimeMillis() - time;
      if (step <= 0) step = 0.00001;
      time += step;
      Keyboard.next();
      Adventure.act();
      Player.player.draw();
      Adventure.draw();
      Console.print();
      Gfx.draw();
    } catch(Exception e) {
      if (!exception) {
        e.printStackTrace();
        exception = true;
      }
    }
  }

  public void start() {
    if (thread == null) {
      thread = new Thread(this);
      thread.start();
    }
  }

  public void stop() {
    thread = null;
  }

  public void run() {
    while (thread != null) {
      repaint();
      try {
        Thread.sleep(3);
      } catch(Exception ignored) {}
    }
  }
}
