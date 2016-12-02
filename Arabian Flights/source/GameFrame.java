// GameFrame.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.media.j3d.BoundingSphere;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

/** GameFrame
 * by Mike Prosser
 *
 * The GameFrame serves as a common reference point for the many parts of the game engine.
 * This allows them to find each other by calling, for example, GameFrame.listener.keyDown();
 * It also has methods for major game events, like starting, ending, and exiting.
 *
 * Furthermore, it holds all global constants.
 */
public class GameFrame
{
  // gameplay constants, all in metric standard units
  // hp = health points
  // mp = mana points
  // gp = gold points
  // m = meters
  // s = seconds
  public static float BALOON_SPEED = 10f; // m/s
  public static float BALOON_ALT = 30f; // m
  public static float BALOON_HEALTH_BASE = 100; // hp
  public static float BALOON_HEALTH_VAR = 20; // hp

  public static int CASTLE_LEVEL1COST = 0; // gp
  public static int CASTLE_LEVEL2COST = 30; // gp
  public static int CASTLE_LEVEL3COST = 100; // gp
  public static int CASTLE_LEVEL4COST = 300; // gp
  public static int CASTLE_LEVEL5COST = 1000; // gp

  public static int CASTLE_LEVEL1HEALTH = 100; // hp
  public static int CASTLE_LEVEL2HEALTH = 300; // hp
  public static int CASTLE_LEVEL3HEALTH = 1000; // hp
  public static int CASTLE_LEVEL4HEALTH = 3000; // hp
  public static int CASTLE_LEVEL5HEALTH = 10000; // hp

  public static int CRANE_DAMAGE = 10; // hp
  public static int CRANE_HEALTH_BASE = 10; // hp
  public static int CRANE_HEALTH_VAR = 0; // hp
  public static float CRANE_SPEED = 10f; // m/s
  public static float CRANE_ORBIT_DISTANCE = 1f; // m/s
  public static float CRANE_ORBIT_DISTANCE_VAR = 0.5f; // m/s

  public static int SPIDER_DAMAGE = 20; // hp
  public static int SPIDER_HEALTH_BASE = 25; // hp
  public static int SPIDER_HEALTH_VAR = 10; // hp
  public static float SPIDER_SPEED = 12f; // m/s
  public static float SPIDER_FALL = 10f; // m/s/s
  public static long SPIDER_WAIT = 800; // ms
  public static int SPIDER_LIMIT = 10; // ms

  public static int FIRESTALK_DAMAGE = 30; // hp
  public static int FIRESTALK_HEALTH_BASE = 95; // hp
  public static int FIRESTALK_HEALTH_VAR = 30; // hp
  public static long FIRESTALK_WAIT = 200; // ms
  public static int FIRESTALK_CROWD_SIZE = 3;
  public static int FIRESTALK_LIMIT = 30;

  public static float FIREBALL_SPEED = 15f; // m/s
  public static long FIREBALL_DURATION = 4000; // ms

  public static float FIREBOLT_SPEED = 50f; // m/s
  public static long FIREBOLT_DURATION = 2000; // ms

  public static int GOLDMINE_HEALTH = 100; // hp
  public static int GOLDMINE_GAINS = 3; // gp/s

  public static float SPELL_SPEED = 50f; // m/s
  public static long SPELL_DURATION = 5000; // ms

  public static float SPELL_SCALE = 1f; // m
  public static float CASTLE_SPELL_SCALE = 1.5f; // m
  public static float SPEED_SPELL_MULT = 3f; // m
  public static float FIREBOLT_SCALE = .6f; // m
  public static float FIREBALL_SCALE = 1.8f; // m
  public static float METEOR_SCALE = 2f; // m

  public static float METEOR_FALL = 0.7f; // m/s/s
  public static int   METEOR_BALLS = 40; // number of
  public static float METEOR_BALL_TIME = 1f; // s
  public static float METEOR_BALL_SPEED = 15f; // m/s

  public static int SPELL_CASTLE_MANA_COST = 30; // mp
  public static int SPELL_CLAIM_MANA_COST = 2; // mp
  public static int SPELL_SPIDER_MANA_COST = 5; // mp
  public static int SPELL_FIREBOLT_MANA_COST = 4; // mp
  public static int SPELL_SPEED_MANA_COST = 6; // mp
  public static int SPELL_FIRESTALK_MANA_COST = 10; // mp
  public static int SPELL_FIREWAVE_MANA_COST = 30; // mp
  public static int SPELL_METEOR_MANA_COST = 50; // mp

  public static int SPELL_SPEED_LEVEL = 0; // mp
  public static int SPELL_CASTLE_LEVEL = 0; // mp
  public static int SPELL_FIREBOLT_LEVEL = 0; // mp
  public static int SPELL_CLAIM_LEVEL = 1; // mp
  public static int SPELL_SPIDER_LEVEL = 2; // mp
  public static int SPELL_FIREWAVE_LEVEL = 3; // mp
  public static int SPELL_FIRESTALK_LEVEL = 4; // mp
  public static int SPELL_METEOR_LEVEL = 5; // mp

  public static int SPELL_SPIDER_GOLD_COST = 10; // gp
  public static int SPELL_FIRESTALK_GOLD_COST = 30; // gp

  public static int FIREBOLT_DAMAGE = 5; // hp
  public static int METEOR_DAMAGE = 10; // hp

  public static int FIREWAVE_DAMAGE = 5; // hp
  public static float FIREWAVE_SPREAD = .2f; // ratio
  public static int FIREWAVE_COUNT = 25; // number of

  public static int REGEN_HEALTH_RATE = 1; // hp/s
  public static int REGEN_MANA_RATE = 5; // mp/s

  public static long CAST_TIME = 500;

  public static boolean GHOST_MODE = false;


  // technical constants
  public static float NEAR_CLIP_DISTANCE = .05f;
  public static float FAR_CLIP_DISTANCE = 20.0f;
  public static float SIGHT_RANGE = 80;

  public static int   MAP_RESOLUTION = 257; // 256+1
  public static float HEIGHT_SCALE = .25f;
  public static float MAP_GRID_SCALE = 2.5f;
  public static int   COLLISION_GRID_RESOLUTION = 64;
  public static float MOUSEX_SCALE = -0.002f;
  public static float MOUSEY_SCALE = 0.002f;
  public static float WIZARD_SPEED = 10f;
  public static float WIZARD_SCALE = 1.5f;
  public static float WIZARD_FLOAT_HEIGHT = 2f;
  public static float WIZARD_FLOAT_FALL_RATE = .005f;
  public static float WIZARD_MOVEMENT_DAMPER = 300f;
  public static float HUD_MINIMAP_RANGE = .25f;
  public static int   HUD_MINIMAP_DOTS = 128;
  public static float HUD_MINIMAP_SIZE = .095f;
  public static float HUD_BIG_MINIMAP_SIZE = .35f;
  public static long  CORPSE_LINGER_TIME = 60000;
  public static long  CHECK_PERIOD = 50;
  public static int   PORT = 4242;
  public static int   SCREEN_WIDTH = 0;
  public static int   SCREEN_HEIGHT = 0;
  public static boolean SCREEN_EXCLUSIVE = true;
  public static int   REMAINDER_THRESHOLD = 9; // how many monsters must be left for a notification to be shown

  public static boolean RANDOM_MONSTERS = false;
  public static int     NUM_RANDOM_MONSTERS = 20;
  public static float   RANDOM_MONSTERS_RATIO = 0.7f;

  // note, these derived values are recalculated in loadConsts()!!
  public static float HUD_MINIMAP_SCALE = 160f/GameFrame.MAP_RESOLUTION*GameFrame.MAP_GRID_SCALE;
  public static float HUD_MINIMAP_DOT_SCALE = 620f*HUD_MINIMAP_RANGE;
  public static float HUD_MINIMAP_BIG_DOT_SCALE = HUD_MINIMAP_DOT_SCALE*2;
  public static float COLLISION_GRID_SCALE = (MAP_GRID_SCALE*MAP_RESOLUTION)/COLLISION_GRID_RESOLUTION;
  public static float SOUND_MAX_DISTANCE = SIGHT_RANGE;
  public static float HUD_MINIMAP_BIG_RANGE = HUD_MINIMAP_RANGE*2;
  public static float HUD_DISTANCE = NEAR_CLIP_DISTANCE * 10;
  public static float BUILDING_DISTANCE = GameFrame.MAP_GRID_SCALE * 22; // m

  public static BoundingSphere bounds = new BoundingSphere(new Point3d(0,0,0), 1000);

  public final static URL URL_LOADINGPIC = ClassLoader.getSystemResource("sprites/afloading.jpg");

  public static URL URL_MAP_HEIGHTMAP;
  public static URL URL_MAP_TEXTURE;

  public static final int SKY_NIGHT = 0;
  public static final int SKY_DAY = 1;

  public static int SKY = SKY_NIGHT;

  // define movement keys
  public final static int KEY_FORWARD = KeyEvent.VK_W;
  public final static int KEY_BACK = KeyEvent.VK_S;
  public final static int KEY_LEFT = KeyEvent.VK_A;
  public final static int KEY_RIGHT = KeyEvent.VK_D;

  // define spell keys
  public final static int KEY_NEXT_SPELL = KeyEvent.VK_E;
  public final static int KEY_PREV_SPELL = KeyEvent.VK_Q;

  // these are the locations of the .wav files
  public static URL URL_SOUND_ZAP = ClassLoader.getSystemResource("sounds/zap1.wav");
  public static URL URL_SOUND_HUM = ClassLoader.getSystemResource("sounds/hum1.wav");
  public static URL URL_SOUND_BOOM = ClassLoader.getSystemResource("sounds/boom1.wav");
  public static URL URL_SOUND_HISS = ClassLoader.getSystemResource("sounds/hiss1.wav");
  public static URL URL_SOUND_WHOOSH = ClassLoader.getSystemResource("sounds/whoosh1.wav");

  // these are the keys for the sounds
  public static int SOUND_ZAP;
  public static int SOUND_HUM;
  public static int SOUND_BOOM;
  public static int SOUND_HISS;
  public static int SOUND_WHOOSH;

  // these are the bases for the monsters' sprites
  public final static String STRING_CRANE_BASE = "sprites/monsters/crane/crane";
  public final static String STRING_SPIDER_BASE = "sprites/monsters/spider/spider";
  public final static String STRING_FIRESTALK_BASE = "sprites/monsters/firestalk/firestalk";

  // this is for the wizard sprite
  public final static String STRING_WIZARD_BASE = "sprites/wizard/wizard";

  // these are the strings for the spell sprites
  public final static String STRING_FIREBALL = "sprites/fireball.png";
  public final static String STRING_FIREBOLT = "sprites/firebolt.png";
  public final static String STRING_ROCK = "sprites/rock.png";
  public final static String STRING_SUMMONBALL = "sprites/summonball.png";
  public final static String STRING_METEOR = "sprites/meteor.png";
  public final static String STRING_SMOKE = "sprites/smoke.png";
  public final static String STRING_SMOKE_MASK = "sprites/smokemask.png";

  // the tree sprite
  public final static String STRING_TREE = "sprites/tree.png";

  // flag and baloon sprites
  public final static String STRING_FLAG = "sprites/flag.png";
  public final static String STRING_BALOON = "sprites/baloon.png";

  // the string for the gold mine sprite
  public final static String STRING_MINEWALL = "sprites/minewall.png";

  // the strings for the castle sprites
  public final static String STRING_CASTLETOWER = "sprites/castletower.png";
  public final static String STRING_CASTLEWALL = "sprites/castlewall.png";

  // this is the world file
  public static String STRING_WORLD_FILE;
  public static String STRING_WORLD_LIST_FILE = "maps/worlds.txt";

  // this is the manual file
  public static String STRING_MANUAL_FILE = "manual.txt";

  // this is the config file
  public static String STRING_CONFIG_FILE = "config.cfg";

  // this is the current time
  public static long CURRENT_TIME;
  
  // sound is disabled if silentMode is true
  public static boolean silentMode = false;

  // these are for multiplayer
  public static ArabianServerSocket serverSocket = null;
  public static ArabianClientSocket clientSocket = null;

  // these are the parts of the engine
  public static Renderer renderer;
  public static PhysicsEngine physics;
  public static SoundPlayer sound;
  public static InputListener listener;

  public static Player player;
  public static TeamInfo playerTeam;
  public static TeamInfo monsterTeam;

  public static Frame menuFrame;
  public static LoadingWindow loadingWindow;

  /** This method handles loading and starting the game for singleplayer. */
  public static void loadSingleplayer()
  {
    System.out.println("Starting at: " + Runtime.getRuntime().totalMemory()/(1024*1024) + "MB RAM");

    // load the pieces
    loadingMessage("   Loading physics engine");
    physics = new PhysicsEngine();

    loadingMessage("   Loading sound player");
    sound = new SoundPlayer();

    loadingMessage("   Loading renderer");
    WorldMaker.setupMapURLs(GameFrame.STRING_WORLD_FILE);
    renderer = new Renderer(menuFrame);

    loadingMessage("   Loading input listener");
    listener = new InputListener();

    // load the sounds
    loadingMessage("   Loading sounds");
    loadSounds();

    // start the game
    loadingMessage("   Starting game...");
    startGame();

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    if (SCREEN_HEIGHT == 0 || SCREEN_WIDTH == 0)
    {
      // set up and show the fullscreen window
      renderer.setBounds(0, 0, (int) screenSize.getWidth(),(int) screenSize.getHeight());
    }
    else
    {
      int wdiff = ((int)screenSize.getWidth() - SCREEN_WIDTH)/2;
      int hdiff = ((int)screenSize.getHeight() - SCREEN_HEIGHT)/2;

      // set up the window
      renderer.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    renderer.setVisible(true);

    // setup the listener to hear all key and mouse events
    System.out.println("registering listeners...");
    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(listener);
    renderer.getCanvas().addMouseListener(listener);
    renderer.getCanvas().addMouseMotionListener(listener);
    renderer.getCanvas().addKeyListener(listener);

    System.out.println("Finished Loading at: " + Runtime.getRuntime().totalMemory()/(1024*1024) + "MB RAM");

    listener.startMouseRead();
    physics.unFreeze();
    Profiler.start();
    renderer.start();

    loadingWindow.hide();
  }

  public static void loadServer()
  {

  }

  public static void loadClient(String host)
  {

  }

  // loads the sounds
  private static void loadSounds()
  {
  	if (silentMode) return;
  	
    // load the sounds
    SOUND_ZAP = sound.loadSound(URL_SOUND_ZAP);
    SOUND_HUM = sound.loadSound(URL_SOUND_HUM);
    SOUND_BOOM = sound.loadSound(URL_SOUND_BOOM);
    SOUND_HISS = sound.loadSound(URL_SOUND_HISS);
    SOUND_WHOOSH = sound.loadSound(URL_SOUND_WHOOSH);
  }

  /** This method starts the game */
  public static void startGame()
  {
    System.out.println("Starting Game");

    System.out.println("Making Wild Monster Team");
    loadingMessage("   Loading wild monster sprites");
    monsterTeam = new TeamInfo(Color.red);

    System.out.println("Making World");
    loadingMessage("   Loading world entities");

    GameFrame.CURRENT_TIME = System.currentTimeMillis();

    // initialize the smokemaker
    SmokeMaker.init();

    if (RANDOM_MONSTERS)
    {
      WorldMaker.makeWorld(STRING_WORLD_FILE, true, NUM_RANDOM_MONSTERS);
    }
    else
    {
      WorldMaker.makeWorld(STRING_WORLD_FILE);
    }

    loadingMessage("   Loading player");
    playerTeam = new TeamInfo(Color.cyan);
    player = new Player(playerTeam);

    Point3f playerStart = WorldMaker.getStartLocation(0);
    player.setPosition(playerStart.x,0,playerStart.z);
    player.setLevel(0);
    player.setHealth(100);
    player.setGold(0);
    player.setMana(200);
    physics.addParticle(player);


    loadingMessage("   Loading enemy wizards");
    for (int i=1; i<WorldMaker.numWizards(); i++)
    {
      // choose color
      //Color color = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
      Color color = Color.magenta;

      TeamInfo aiTeam = new TeamInfo(color);
      AiWizard ai = new AiWizard(aiTeam);

      Point3f aiStart = WorldMaker.getStartLocation(i);
      ai.setPosition(aiStart.x,0,aiStart.z);
      ai.setLevel(0);
      ai.setHealth(100);
      ai.setGold(0);
      ai.setMana(200);
      physics.addParticle(ai);
      System.out.println("AI Wizard added of color " + ai.getTeam().getColor3f());
    }

    // inform each team of its enemies
    TeamInfo.setupEnemies();

    renderer.getHud().setFps(0);
  }


  /** This method is called when the game ends in defeat. */
  public static void endGame()
  {
    System.out.println("Game Over!");
    renderer.getHud().setMessage("GAME OVER");
    physics.freeze();
  }

  /** This method is called when the player wins */
  public static void victory()
  {
    renderer.getHud().setMessage("---!!! A WINNER IS YOU! !!!---");
  }

  /** This method is called when the game exits. */
  public static void exit()
  {
    renderer.finish();
    Profiler.report();

    System.gc();System.gc();
    System.out.println("Exited at: " + Runtime.getRuntime().totalMemory()/(1024*1024) + "MB RAM");
    System.exit(0);
  }

  /** This puts a message on the loading window. */
  public static void loadingMessage(String string)
  {
    loadingWindow.setMessage(string);
  }

  /** this is used to load the constants from the config.cfg file */
  public static void loadConsts()
  {
    try
    {
      BufferedReader reader = new BufferedReader(new InputStreamReader(
          ClassLoader.getSystemResourceAsStream(STRING_CONFIG_FILE)));
      while (reader.ready())
      {
        String line = reader.readLine();

        if (line.startsWith("BALOON_SPEED=")) BALOON_SPEED = getFloat(line);
        else if (line.startsWith("BALOON_ALT=")) BALOON_ALT = getFloat(line);
        else if (line.startsWith("BALOON_HEALTH_BASE=")) BALOON_HEALTH_BASE = getInt(line);
        else if (line.startsWith("BALOON_HEALTH_VAR=")) BALOON_HEALTH_VAR = getInt(line);

        else if (line.startsWith("CASTLE_LEVEL1COST=")) CASTLE_LEVEL1COST = getInt(line);
        else if (line.startsWith("CASTLE_LEVEL2COST=")) CASTLE_LEVEL2COST = getInt(line);
        else if (line.startsWith("CASTLE_LEVEL3COST=")) CASTLE_LEVEL3COST = getInt(line);
        else if (line.startsWith("CASTLE_LEVEL4COST=")) CASTLE_LEVEL4COST = getInt(line);
        else if (line.startsWith("CASTLE_LEVEL5COST=")) CASTLE_LEVEL5COST = getInt(line);

        else if (line.startsWith("CASTLE_LEVEL1HEALTH=")) CASTLE_LEVEL1HEALTH = getInt(line);
        else if (line.startsWith("CASTLE_LEVEL2HEALTH=")) CASTLE_LEVEL2HEALTH = getInt(line);
        else if (line.startsWith("CASTLE_LEVEL3HEALTH=")) CASTLE_LEVEL3HEALTH = getInt(line);
        else if (line.startsWith("CASTLE_LEVEL4HEALTH=")) CASTLE_LEVEL4HEALTH = getInt(line);
        else if (line.startsWith("CASTLE_LEVEL5HEALTH=")) CASTLE_LEVEL5HEALTH = getInt(line);

        else if (line.startsWith("CRANE_DAMAGE=")) CRANE_DAMAGE = getInt(line);
        else if (line.startsWith("CRANE_HEALTH_BASE=")) CRANE_HEALTH_BASE = getInt(line);
        else if (line.startsWith("CRANE_HEALTH_VAR=")) CRANE_HEALTH_VAR = getInt(line);
        else if (line.startsWith("CRANE_SPEED=")) CRANE_SPEED = getFloat(line);
        else if (line.startsWith("CRANE_ORBIT_DISTANCE=")) CRANE_ORBIT_DISTANCE = getFloat(line);
        else if (line.startsWith("CRANE_ORBIT_DISTANCE_VAR=")) CRANE_ORBIT_DISTANCE_VAR = getFloat(line);

        else if (line.startsWith("SPIDER_DAMAGE=")) SPIDER_DAMAGE = getInt(line);
        else if (line.startsWith("SPIDER_HEALTH_BASE=")) SPIDER_HEALTH_BASE = getInt(line);
        else if (line.startsWith("SPIDER_HEALTH_VAR=")) SPIDER_HEALTH_VAR = getInt(line);
        else if (line.startsWith("SPIDER_SPEED=")) SPIDER_SPEED = getFloat(line);
        else if (line.startsWith("SPIDER_FALL=")) SPIDER_FALL = getFloat(line);
        else if (line.startsWith("SPIDER_WAIT=")) SPIDER_WAIT = getLong(line);
        else if (line.startsWith("SPIDER_LIMIT=")) SPIDER_LIMIT = getInt(line);

        else if (line.startsWith("FIRESTALK_DAMAGE=")) FIRESTALK_DAMAGE = getInt(line);
        else if (line.startsWith("FIRESTALK_HEALTH_BASE=")) FIRESTALK_HEALTH_BASE = getInt(line);
        else if (line.startsWith("FIRESTALK_HEALTH_VAR=")) FIRESTALK_HEALTH_VAR = getInt(line);
        else if (line.startsWith("FIRESTALK_WAIT=")) FIRESTALK_WAIT = getLong(line);
        else if (line.startsWith("FIRESTALK_CROWD_SIZE=")) FIRESTALK_CROWD_SIZE = getInt(line);
        else if (line.startsWith("FIRESTALK_LIMIT=")) FIRESTALK_LIMIT = getInt(line);

        else if (line.startsWith("FIREBALL_SPEED=")) FIREBALL_SPEED = getFloat(line);
        else if (line.startsWith("FIREBALL_DURATION=")) FIREBALL_DURATION = getLong(line);

        else if (line.startsWith("FIREBOLT_SPEED=")) FIREBOLT_SPEED = getFloat(line);
        else if (line.startsWith("FIREBOLT_DURATION=")) FIREBOLT_DURATION = getLong(line);

        else if (line.startsWith("GOLDMINE_HEALTH=")) GOLDMINE_HEALTH = getInt(line);
        else if (line.startsWith("GOLDMINE_GAINS=")) GOLDMINE_GAINS = getInt(line);

        else if (line.startsWith("SPELL_SCALE=")) SPELL_SCALE = getFloat(line);
        else if (line.startsWith("CASTLE_SPELL_SCALE=")) CASTLE_SPELL_SCALE = getFloat(line);
        else if (line.startsWith("SPEED_SPELL_MULT=")) SPEED_SPELL_MULT = getFloat(line);
        else if (line.startsWith("FIREBOLT_SCALE=")) FIREBOLT_SCALE = getFloat(line);
        else if (line.startsWith("FIREBALL_SCALE=")) FIREBALL_SCALE = getFloat(line);
        else if (line.startsWith("METEOR_SCALE=")) METEOR_SCALE = getFloat(line);

        else if (line.startsWith("SPELL_SPEED=")) SPELL_SPEED = getFloat(line);
        else if (line.startsWith("SPELL_DURATION=")) SPELL_DURATION = getLong(line);

        else if (line.startsWith("METEOR_FALL=")) METEOR_FALL = getFloat(line);
        else if (line.startsWith("METEOR_BALLS=")) METEOR_BALLS = getInt(line);
        else if (line.startsWith("METEOR_BALL_TIME=")) METEOR_BALL_TIME = getLong(line);
        else if (line.startsWith("METEOR_BALL_SPEED=")) METEOR_BALL_SPEED = getFloat(line);

        else if (line.startsWith("SPELL_CASTLE_MANA_COST=")) SPELL_CASTLE_MANA_COST = getInt(line);
        else if (line.startsWith("SPELL_CLAIM_MANA_COST=")) SPELL_CLAIM_MANA_COST = getInt(line);
        else if (line.startsWith("SPELL_SPIDER_MANA_COST=")) SPELL_SPIDER_MANA_COST = getInt(line);
        else if (line.startsWith("SPELL_FIREBOLT_MANA_COST=")) SPELL_FIREBOLT_MANA_COST = getInt(line);
        else if (line.startsWith("SPELL_SPEED_MANA_COST=")) SPELL_SPEED_MANA_COST = getInt(line);
        else if (line.startsWith("SPELL_FIRESTALK_MANA_COST=")) SPELL_FIRESTALK_MANA_COST = getInt(line);
        else if (line.startsWith("SPELL_FIREWAVE_MANA_COST=")) SPELL_FIREWAVE_MANA_COST = getInt(line);
        else if (line.startsWith("SPELL_METEOR_MANA_COST=")) SPELL_METEOR_MANA_COST = getInt(line);

        else if (line.startsWith("SPELL_SPIDER_GOLD_COST=")) SPELL_SPIDER_GOLD_COST = getInt(line);
        else if (line.startsWith("SPELL_FIRESTALK_GOLD_COST=")) SPELL_FIRESTALK_GOLD_COST = getInt(line);

        else if (line.startsWith("FIREBOLT_DAMAGE=")) FIREBOLT_DAMAGE = getInt(line);
        else if (line.startsWith("METEOR_DAMAGE=")) METEOR_DAMAGE = getInt(line);

        else if (line.startsWith("FIREWAVE_DAMAGE=")) FIREWAVE_DAMAGE = getInt(line);
        else if (line.startsWith("FIREWAVE_SPREAD=")) FIREWAVE_SPREAD = getFloat(line);
        else if (line.startsWith("FIREWAVE_COUNT=")) FIREWAVE_COUNT = getInt(line);

        else if (line.startsWith("REGEN_HEALTH_RATE=")) REGEN_HEALTH_RATE = getInt(line);
        else if (line.startsWith("REGEN_MANA_RATE=")) REGEN_MANA_RATE = getInt(line);

        else if (line.startsWith("NEAR_CLIP_DISTANCE=")) NEAR_CLIP_DISTANCE = getFloat(line);
        else if (line.startsWith("FAR_CLIP_DISTANCE=")) FAR_CLIP_DISTANCE = getFloat(line);
        else if (line.startsWith("SIGHT_RANGE=")) SIGHT_RANGE = getFloat(line);

        else if (line.startsWith("MAP_RESOLUTION=")) MAP_RESOLUTION = getInt(line);
        else if (line.startsWith("HEIGHT_SCALE=")) HEIGHT_SCALE = getFloat(line);
        else if (line.startsWith("MAP_GRID_SCALE=")) MAP_GRID_SCALE = getFloat(line);
        else if (line.startsWith("COLLISION_GRID_RESOLUTION=")) COLLISION_GRID_RESOLUTION = getInt(line);
        else if (line.startsWith("MOUSEX_SCALE=")) MOUSEX_SCALE = getFloat(line);
        else if (line.startsWith("MOUSEY_SCALE=")) MOUSEY_SCALE = getFloat(line);
        else if (line.startsWith("WIZARD_SPEED=")) WIZARD_SPEED = getFloat(line);
        else if (line.startsWith("WIZARD_SCALE=")) WIZARD_SCALE = getFloat(line);
        else if (line.startsWith("WIZARD_FLOAT_HEIGHT=")) WIZARD_FLOAT_HEIGHT = getFloat(line);
        else if (line.startsWith("WIZARD_FLOAT_FALL_RATE=")) WIZARD_FLOAT_FALL_RATE = getFloat(line);
        else if (line.startsWith("WIZARD_MOVEMENT_DAMPER=")) WIZARD_MOVEMENT_DAMPER = getFloat(line);
        else if (line.startsWith("HUD_MINIMAP_RANGE=")) HUD_MINIMAP_RANGE = getFloat(line);
        else if (line.startsWith("HUD_MINIMAP_DOTS=")) HUD_MINIMAP_DOTS = getInt(line);
        else if (line.startsWith("HUD_MINIMAP_SIZE=")) HUD_MINIMAP_SIZE = getFloat(line);
        else if (line.startsWith("HUD_BIG_MINIMAP_SIZE=")) HUD_BIG_MINIMAP_SIZE = getFloat(line);
        else if (line.startsWith("CORPSE_LINGER_TIME=")) CORPSE_LINGER_TIME = getLong(line);
        else if (line.startsWith("CHECK_PERIOD=")) CHECK_PERIOD = getLong(line);
        else if (line.startsWith("PORT=")) PORT = getInt(line);
        else if (line.startsWith("SCREEN_WIDTH=")) SCREEN_WIDTH = getInt(line);
        else if (line.startsWith("SCREEN_HEIGHT=")) SCREEN_HEIGHT = getInt(line);
        else if (line.startsWith("SCREEN_EXCLUSIVE=")) SCREEN_EXCLUSIVE = getBoolean(line);
        else if (line.startsWith("REMAINDER_THRESHOLD=")) REMAINDER_THRESHOLD = getInt(line);

        else if (line.startsWith("RANDOM_MONSTERS=")) RANDOM_MONSTERS = getBoolean(line);
        else if (line.startsWith("NUM_RANDOM_MONSTERS=")) NUM_RANDOM_MONSTERS = getInt(line);
        else if (line.startsWith("RANDOM_MONSTERS_RATIO=")) RANDOM_MONSTERS_RATIO = getFloat(line);

        else if (line.startsWith("GHOST_MODE=")) GHOST_MODE = getBoolean(line);
      }
    }
    catch (IOException e)
    {
      System.out.println("\n\nInvalid Config File, defaults used");
    }
    catch (NullPointerException e)
    {
      System.out.println("no config file, defaults used");
    }

    // recalculate derived values
    HUD_MINIMAP_SCALE = 160f/GameFrame.MAP_RESOLUTION*GameFrame.MAP_GRID_SCALE;
    HUD_MINIMAP_DOT_SCALE = 620f*HUD_MINIMAP_RANGE;
    HUD_MINIMAP_BIG_DOT_SCALE = HUD_MINIMAP_DOT_SCALE*2;
    COLLISION_GRID_SCALE = (MAP_GRID_SCALE*MAP_RESOLUTION)/COLLISION_GRID_RESOLUTION;
    SOUND_MAX_DISTANCE = SIGHT_RANGE;
    HUD_MINIMAP_BIG_RANGE = HUD_MINIMAP_RANGE*2;
    HUD_DISTANCE = NEAR_CLIP_DISTANCE * 10;
    BUILDING_DISTANCE = GameFrame.MAP_GRID_SCALE * 22; // m
  }

  // used with loadConsts()
  private static float getFloat(String line)
  {
    String sub = line.substring(line.indexOf('=')+1);
    float val = Float.valueOf(sub).floatValue();
    return val;
  }

  // used with loadConsts()
  private static int getInt(String line)
  {
    String sub = line.substring(line.indexOf('=')+1);
    return Integer.valueOf(sub).intValue();
  }

  // used with loadConsts()
  private static long getLong(String line)
  {
    String sub = line.substring(line.indexOf('=')+1);
    return Long.valueOf(sub).longValue();
  }

    // used with loadConsts()
    private static boolean getBoolean(String line)
    {
      String sub = line.substring(line.indexOf('=')+1);
      return Boolean.valueOf(sub).booleanValue();
    }


  public static void main(String[] args)
  {
  	for (int i=0; i<args.length; i++)
  	{
  		if (args[i].equals("-silent"))
  		{ 
		  GameFrame.silentMode = true;
		  System.out.println("Using Silent Mode");
  		}
     }
  		
  	
    try
    {
      loadConsts();

      menuFrame = new MenuFrame();

      loadingWindow = new LoadingWindow(URL_LOADINGPIC, menuFrame);

      menuFrame.setVisible(true);
    }
    catch (OutOfMemoryError e)
    {
      renderer.finish();
      System.exit(0);
    }
  }
}