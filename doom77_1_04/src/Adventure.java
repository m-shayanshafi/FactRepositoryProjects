import java.util.*;

public class Adventure {
  public static double startTime;//if not mouse clicked == 0
  public static List<Monster> monsters;
  public static final double shotAnimation = 60;

  private static boolean gahanActivated;
  private static boolean gahan1Activated;
  private static boolean mansonActivated;
  private static boolean koljaActivated;
  private static boolean koljaThreeActivated;

  private static double startLoadTime;
  private static boolean loaded;

  public static void init() {
    startTime = 0;
    gahanActivated = false;
    gahan1Activated = false;
    mansonActivated = false;
    koljaActivated = false;
    koljaThreeActivated = false;
    monsters = new ArrayList<Monster>();
    monsters.add(new Player());
    startLoadTime = 0;
    loaded = false;
  }

  public static void act() {
    if (startTime==0) {
      if (Keyboard.mouseClicked()) startTime = Doom77.time;
        else Console.set("CLICK MOUSE TO START");
    } else {
      if (xy(27, 5)) {
        Console.set("don't go here");
      } else if (xy(26, 5)) {
        Console.set("DOOM77\nv1.0 07/12/15\n(c) M77\nsrc: 33 KB\nmap: 25\ntextures:2+1\ntime: 15 sec\ngood: 100%");
      } else if (xy(25, 5)) {
        Console.set("DOOM77\nv1.01 07/12/19\n(c) M77\nsrc: 37+5=42 KB\nmap: 33\ntextures:2+2\nmon: 1\ntime: 23 sec\ngood: 100%");
      } else if (xy(24, 5)) {
        Console.set("v1.02\nplayer is killable\nGahan shoots\nscream on dmg\nrounded start of ln txt\njar\nbetter keywords\nsite bckgrnd\ntxtrs generation from parts\nchange log way");
      } else if (xy(23, 5)) {
        Console.set("v1.03\nManson\nanother history way\nredblue effect\nblackened wall\nblood on dmg\nmy shoes effect");
      } else if (xy(22, 5)) {
        Console.set("DOOM77\nv1.03 07/12/22\n(c) M77\nsrc: 38+12=50 KB\nmap: 237\ntextures:2+3\nmon: 2+1\ntime: 105 sec\ngood: 100%");
      } else if (xy(21, 5)) {
        Console.set("v1.04\nKolja\nMoses, James & Unfboy txtrs\nhealth\nloading\nwhitened sprites on dmg\nmonsters doesn't kill e.a.");
      } else if (xy(20, 5)) {
        Console.set("DOOM77\nv1.04 07/12/x\n(c) M77\nsrc: 16+36=52 KB\nmap: 62+163\ntextures:5+3\nmon: 3+4=7\ntime: x sec");
      } else if (xy(19, 5)) {
        Console.set("I said \"don't go here\"\nor the devil he may take ya");
      } else if (xy(Map.endx, Map.endy)) {
        Console.set("blackened is\nTHE END\nwinter it will send");
      } else if (xy(19, 5)) {
        Console.set("UNDER CONSTRUCTION\nwtf r u looking for?\n\nbtw i'm enjoyed to code\nthis text trigger\n07/12/16 01:01:15");
      } else if (xy(17, 1)) {
        Console.set("Enter Manson");
      } else if (xy(28, 2) || xy(28, 3)) {
        Console.set("a rising tide\nLIFE IS PAIN\nthat pushes to the other side");
      } else if (xy(27, 28)) {
        Console.set("neverending maze\ndrift on numbered days\nnow your life is out of season");
      }
      checkLoad();
      monsterTriggers();
      serveDeads();
      for (Monster m: monsters) {
        m.act();
      }
    }
  }

  private static void checkLoad() {
    if (xy(26, 1)) if (startLoadTime==0) startLoadTime = Doom77.time;
    if (time(startLoadTime, 10) && !loaded) {
      Console.set("loading...");
    } else if (startLoadTime != 0 && !loaded) {
      Sprite green = Gfx.loadSprite("greenwall.jpg", 500, 500);
      Sprite moses = Gfx.loadSprite("moses.png", 284, 226);
      Sprite mosesBlueRed = new Sprite("mosesBlueRed", Gfx.sprites.get("bluered"));
      new B(mosesBlueRed).dCenter(moses);
      Sprite james = Gfx.loadSprite("james.png", 249, 156);
      Sprite jamesGreen = new Sprite("jamesGreen", green);
      new B(jamesGreen).dCenter(james);
      Sprite unfboy = Gfx.loadSprite("unfboy.png", 350, 411);
      Sprite kitch = Gfx.sprites.get("kitch");
      Sprite boyKitch = new Sprite("boyKitch", kitch);
      new B(boyKitch).d(unfboy, 0, kitch.h-unfboy.h);
      Map.init();
      Gfx.loadSprite("kolja.png", 200, 167);
      loaded = true;
    }
  }

  public static boolean time(double time, double period) {
    return Doom77.time-time<period;
  }

  private static void serveDeads() {
    for (int i=0; i<monsters.size(); i++) {
      if (monsters.get(i).life <= 0) {
        if (monsters.get(i) == Player.player) {
          Console.set("DEATH IS NOT THE END\npress any key");
          if (!time(Player.player.deathTime, 1000) && Keyboard.anyKey()) {
            monsters.remove(i);
            monsters.add(i, new Player());
          }
        } else {
          monsters.remove(i--);
        }
      }
    }
  }

  public static void draw() {
    Player p = Player.player;
    if (time(p.lastShot, shotAnimation)) drawShot();
    if (p.afterDmg()) drawBlood();
  }

  private static void monsterTriggers() {
    if (xy(1, 5) && !gahanActivated) {
      monsters.add(new Gahan(9, 5));
      gahanActivated = true;
    } else if (xy(16, 2) && !gahan1Activated) {
      monsters.add(new Gahan(11, 1));
      gahan1Activated = true;
    } else if ((xy(20, 1) || xy(20, 2)) && !mansonActivated) {
      monsters.add(new Manson(27, 2));
      mansonActivated = true;
    } else if (xy(28, 4) && !koljaActivated) {
      monsters.add(new Kolja(27, 10));
      koljaActivated = true;
    } else if (xy(28, 27) && ! koljaThreeActivated) {
      monsters.add(new Kolja(24, 28));
      monsters.add(new Kolja(24, 27));
      monsters.add(new Kolja(26, 27));
      koljaThreeActivated = true;
    }
  }

  private static void drawShot() {
    Sprite shot = Gfx.sprites.get("shot");
    Gfx.b.d(shot, Gfx.wh-shot.wh, Gfx.hh-shot.hh);
  }

  private static void drawBlood() {
    Sprite blood = Gfx.sprites.get("blood");
    Gfx.b.d(blood, Gfx.wh-blood.wh, Gfx.hh-blood.hh);
  }

  private static boolean xy(int x, int y) {
    return (int)(Player.player.x/Engine.size)==x && (int)(Player.player.y/Engine.size)==y;
  }
}
