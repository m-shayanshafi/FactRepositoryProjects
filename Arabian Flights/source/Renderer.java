// Renderer.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import java.awt.*;
import java.awt.image.BufferedImage;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;

/** The renderer is the 3D rendering engine of the game. It also contains a CameraBehavior
 * object and a PhysicsBehavior object for controlling the camera and the physics. It also
 * contains an FpsBehavior to count how long it takes to render thirty frames.
 * note: The Renderer must be created after the InputListener and the PhysicsEngine
 */
public class Renderer extends Window
{
  private MapTile currentTile;
  private Canvas3D canvas3D;
  private Camera camera;
  private HeadsUpDisplay hud = new HeadsUpDisplay();

  private Color3f daySkyColor = new Color3f(0.7f, 0.7f, 1.0f);
  private Color3f nightSkyColor = new Color3f(0.0f, 0.0f, 0.0f);

  private SimpleUniverse simpleU;
  private Transform3D hudTransform = new Transform3D();
  private TransformGroup hudGroup = new TransformGroup();

  private GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

  /** Setup the fullscreen mode*/
  public void start()
  {
    // fullscreen exclusive DOESN"T WORK RELIABLY!
    if (GameFrame.SCREEN_EXCLUSIVE)
      device.setFullScreenWindow(this);
  }
  public void finish()
  {
    // fullscreen exclusive DOESN"T WORK RELIABLY!
    if (GameFrame.SCREEN_EXCLUSIVE)
      device.setFullScreenWindow(null);
  }

  /** The renderer needs a Frame object to get focus from. */
  public Renderer(Frame frame)
  {
    super(frame);
    frame.requestFocus();

    System.out.println("preping...");
    setLayout(new BorderLayout());
    GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
    canvas3D = new Canvas3D(config);
    canvas3D.requestFocus();

    add("Center", canvas3D);

    // hide the cursor
    BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
    setCursor(Toolkit.getDefaultToolkit().createCustomCursor(bi, new Point(0,0),"empty"));

    // SimpleUniverse is a conveniance utility class
    simpleU = new SimpleUniverse(canvas3D);

    simpleU.getViewingPlatform().setNominalViewingTransform();

	AudioDevice ad = null;
    if (!GameFrame.silentMode)
    {
      ad = simpleU.getViewer().createAudioDevice();
     
      System.out.println("Sound Chanels available: " + ad.getChannelsAvailable());
    }
    
    simpleU.getViewer().getView().setFieldOfView(Math.PI/2.0f);
    simpleU.getViewer().getView().setBackClipDistance(GameFrame.FAR_CLIP_DISTANCE);
    simpleU.getViewer().getView().setFrontClipDistance(GameFrame.NEAR_CLIP_DISTANCE);
    simpleU.getViewer().getView().setDepthBufferFreezeTransparent(true);
    simpleU.getViewer().getView().setTransparencySortingPolicy(View.TRANSPARENCY_SORT_GEOMETRY);

    BranchGroup scene = createSceneGraph(simpleU);

    System.out.println("compiling...");
    scene.compile();
    simpleU.addBranchGraph(scene);
  }

  public HeadsUpDisplay getHud()
  {
    return hud;
  }

  public Camera getCamera()
  {
    return camera;
  }

  public MapTile getMap()
  {
    return currentTile;
  }

  public Canvas3D getCanvas()
  {
    return canvas3D;
  }

  private BranchGroup createSceneGraph(SimpleUniverse su)
  {
    System.out.println("building...");

    // create the root of the branch graph
    BranchGroup objRoot = new BranchGroup();

    // Create the MapTile and load the heightmap geometry
    MapTile tile = new MapTile(GameFrame.URL_MAP_HEIGHTMAP, GameFrame.URL_MAP_TEXTURE);
    currentTile = tile;
    Shape3D landscape1 = tile.getShape3D();
    Shape3D landscape2 = tile.getShape3D();
    Shape3D landscape3 = tile.getShape3D();
    Shape3D landscape4 = tile.getShape3D();

    // create the translations for each quadrant
    Transform3D trans1 = new Transform3D();
    Transform3D trans2 = new Transform3D();
    Transform3D trans3 = new Transform3D();

    float dist = 0-(GameFrame.MAP_RESOLUTION-1)*(GameFrame.MAP_GRID_SCALE);
    trans1.setTranslation(new Vector3f(dist, 0.0f, 0.0f));
    trans2.setTranslation(new Vector3f(0.0f, 0.0f, dist));
    trans3.setTranslation(new Vector3f(dist, 0.0f, dist));

    // create the branchgroups for each quadrant
    TransformGroup tg1 = new TransformGroup();
    TransformGroup tg2 = new TransformGroup();
    TransformGroup tg3 = new TransformGroup();
    tg1.setTransform(trans1);
    tg2.setTransform(trans2);
    tg3.setTransform(trans3);

    // set up the camera;
    camera = new Camera(su.getViewingPlatform().getViewPlatformTransform());

    // set up the physics
    FrameBehavior frameBeh = new FrameBehavior();
    frameBeh.setSchedulingBounds(GameFrame.bounds);

    // set up the fps counter
    FpsBehavior fpsBeh = new FpsBehavior();
    fpsBeh.setSchedulingBounds(GameFrame.bounds);

    // set up some  fog
    LinearFog fog = new LinearFog();
    if (GameFrame.SKY == GameFrame.SKY_NIGHT)
    {
      fog.setColor(nightSkyColor);
      fog.setFrontDistance(5.0d*GameFrame.FAR_CLIP_DISTANCE/6f);
      fog.setBackDistance(35.0*GameFrame.FAR_CLIP_DISTANCE/6f);
    }
    else
    {
      fog.setColor(daySkyColor);
      fog.setFrontDistance(10.0d*GameFrame.FAR_CLIP_DISTANCE/6f);
      fog.setBackDistance(35.0*GameFrame.FAR_CLIP_DISTANCE/6f);
    }
    fog.setBounds(GameFrame.bounds);
    fog.setInfluencingBounds(GameFrame.bounds);
    BranchGroup fogGroup = new BranchGroup();
    fog.addScope(fogGroup);

    if (GameFrame.SKY == GameFrame.SKY_NIGHT)
    {
      // set up the starry night sky
      NightSky sky = new NightSky();
      Shape3D skyShape = sky.generate(33.2f*GameFrame.FAR_CLIP_DISTANCE/6f);
      Transform3D skyTrans = new Transform3D();
      skyTrans.setTranslation(new Vector3f(0.0f, 10.0f, 0.0f));
      TransformGroup skyGroup = new TransformGroup(skyTrans);
      skyGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
      camera.setSkyTG(skyGroup);
      skyGroup.addChild(skyShape);
      objRoot.addChild(skyGroup);
    }

    // set up the background color
    Background bg = new Background();
    if (GameFrame.SKY == GameFrame.SKY_DAY) bg.setColor(daySkyColor);
    else bg.setColor(nightSkyColor);
    bg.setApplicationBounds(GameFrame.bounds);
    objRoot.addChild(bg);

    // Build hierarchy
    System.out.println("hierarching...");
    objRoot.addChild(frameBeh);
    objRoot.addChild(fpsBeh);

    objRoot.addChild(fog);
    objRoot.addChild(fogGroup);

    fogGroup.addChild(GameFrame.physics.getBranchGroup());
    fogGroup.addChild(landscape1);
    fogGroup.addChild(tg1);
    fogGroup.addChild(tg2);
    fogGroup.addChild(tg3);

    tg1.addChild(landscape2);
    tg2.addChild(landscape3);
    tg3.addChild(landscape4);

    // set up HUD
    hudGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    hudGroup.setTransform(hudTransform);
    hudGroup.addChild(hud);

    simpleU.getViewingPlatform().getViewPlatformTransform().getTransform(hudTransform);

    objRoot.addChild(hudGroup);

    return objRoot;
  }

  /** The FrameBehavior is used to call the camera and physics engine every frame. */
  public class FrameBehavior extends Behavior
  {
    private long lasttime = 0;
    private long timedelta = 0;

    private WakeupCondition wakeupCondition= new WakeupOnElapsedFrames(0);

    public void initialize()
    {
      lasttime = GameFrame.CURRENT_TIME;
      System.out.println("Starting the Physics...");
      this.wakeupOn(wakeupCondition);
    }

    public void processStimulus(java.util.Enumeration e)
    {
      // calculate time
      GameFrame.CURRENT_TIME = System.currentTimeMillis();
      timedelta = GameFrame.CURRENT_TIME - lasttime;
      lasttime = GameFrame.CURRENT_TIME;

      // prevent large time steps
      if (timedelta > 200) timedelta = 200;

      Profiler.setRenderThreadBlock(Profiler.BLOCK_MOVE);

      // move all the particles
      GameFrame.physics.move(timedelta);

      Profiler.setRenderThreadBlock(Profiler.BLOCK_CAMERA);

      // update the camera
      camera.update();

      Profiler.setRenderThreadBlock(Profiler.BLOCK_UNKNOWN);

      this.wakeupOn(wakeupCondition);
    }
  }

  /** The FpsBehavior is used to update the FPS every half second */
  public class FpsBehavior extends Behavior
  {
    private long lasttime;
    private WakeupCondition wakeup = new WakeupOnElapsedFrames(30);

    public void initialize()
    {
      lasttime = GameFrame.CURRENT_TIME;
      this.wakeupOn(wakeup);
    }

    public void processStimulus(java.util.Enumeration e)
    {
      // update the fps counter
      long currenttime = GameFrame.CURRENT_TIME;
      long time = currenttime-lasttime+1;
      float fps = 1000000f/time/30f;
      lasttime = currenttime;
      GameFrame.renderer.hud.setFps(fps);
      this.wakeupOn(wakeup);
    }
  }

  /** The Camera class is used to control the camera, and also
   * can be queried about the camera's position and direction.
   */

  public class Camera
  {
    private boolean automode = false;

    private TransformGroup targetTG;
    private TransformGroup skyTG = null;
    private Transform3D transform = new Transform3D();
    private Transform3D skyTransform = new Transform3D();
    private WakeupCondition wakeupCondition= new WakeupOnElapsedFrames(0);

    private Vector3f translation = new Vector3f();

    private long lasttime = 0;
    private long nowtime = 0;
    private long timedelta = 0;

    // these specify the grid location of the camera
    private float x_pos = 0;
    private float y_pos = 0;
    private float z_pos = 0;

    private float x_vel = 0;
    private float z_vel = 0;

    // these specify the direction the camera is looking
    private float x_look = 0; // +PI/3 to -PI/3, up/down
    private float y_look = 0; // 0 to 2PI, left/right, wraps around

    // used for mousecam
    private Point mouseMoved;

    // used for camera rotations
    private Quat4f basequat = new Quat4f(); // represents 0,0,0,0
    private Quat4f quat = new Quat4f();
    private Quat4f newquat = new Quat4f();
    private AxisAngle4f axan = new AxisAngle4f();

    //private CameraParticle camParticle;

    /** The Camera needs a transformgroup to operate on. */
    Camera(TransformGroup tg)
    {
      // get the starting transform
      this.targetTG = tg;
      targetTG.getTransform(transform);

      transform.get(basequat);
    }

    public Vector3f getLookVector()
    {
      return new Vector3f(-(float)(Math.sin(y_look)*Math.cos(x_look)),
                          (float)Math.sin(x_look),
                          -(float)(Math.cos(y_look)*Math.cos(x_look)));
    }

    /** returns the particle that the camera is attached to. */
    public Particle getParticle()
    {
      return GameFrame.player;
    }

    /** Set the sky transform group. */
    public void setSkyTG(TransformGroup tg)
    {
      this.skyTG = tg;
    }

    /** Sets the camera's position, in meters from the origin.
     * This is called by Player.move()*/
    public void setCamPosition(float x, float y, float z)
    {
      x_pos = x;
      y_pos = y;
      z_pos = z;

      // set the transform
      translation.set(x_pos, y_pos, z_pos);
      transform.setTranslation(translation);
      targetTG.setTransform(transform);
    }

    /** sets the direction that the camera is looking, in degrees */
    public void setCamLook(float angle, float upangle)
    {
      y_look = (float)(angle*2.0f*Math.PI/360.0f);
      x_look = (float)(upangle*2.0f*Math.PI/360.0f);
    }

    /** get the camera rotation in radians */
    public float getCamRot()
    {
      return y_look;
    }

    /** get the camera X position, in units from the origin. */
    public float getCamX()
    {
      return x_pos;
    }
    /** get the camera Z position, in units from the origin. */
    public float getCamZ()
    {
      return z_pos;
    }

    /** This is called every frame to control the camera. */
    public void update()
    {
      if (GameFrame.physics.isFrozen()) return;

      // calculate time
      nowtime = GameFrame.CURRENT_TIME;
      timedelta = nowtime - lasttime;

      // go fast while speeding
      float speed = GameFrame.WIZARD_SPEED;
      if (GameFrame.player.isSpeeding()) speed *= GameFrame.SPEED_SPELL_MULT;

      if (lasttime != 0)
      {
        boolean going = false;
        float old_x = x_vel;
        float old_z = z_vel;
        x_vel = 0;
        z_vel = 0;

        // handle horizontal movment
        if (GameFrame.listener.keyDown(GameFrame.KEY_FORWARD) || automode)
        {
          z_vel += -(float)Math.cos(y_look)*speed;
          x_vel += -(float)Math.sin(y_look)*speed;
          going = true;
        }
        if (GameFrame.listener.keyDown(GameFrame.KEY_BACK))
        {
          z_vel += (float)Math.cos(y_look)*speed;
          x_vel += (float)Math.sin(y_look)*speed;
          going = true;
        }
        if (GameFrame.listener.keyDown(GameFrame.KEY_LEFT))
        {
          z_vel += -(float)Math.cos(y_look+Math.PI/2)*speed;
          x_vel += -(float)Math.sin(y_look+Math.PI/2)*speed;
          going = true;
        }
        if (GameFrame.listener.keyDown(GameFrame.KEY_RIGHT))
        {
          z_vel += (float)Math.cos(y_look+Math.PI/2)*speed;
          x_vel += (float)Math.sin(y_look+Math.PI/2)*speed;
          going = true;
        }
        if (!going)
        {
          x_vel = old_x;
          z_vel = old_z;
          if (x_vel != 0) x_vel *= (1-timedelta/GameFrame.WIZARD_MOVEMENT_DAMPER);
          if (z_vel != 0) z_vel *= (1-timedelta/GameFrame.WIZARD_MOVEMENT_DAMPER);
        }

        GameFrame.player.setVelocity(x_vel, z_vel);

        // handle mouse movement
        mouseMoved = GameFrame.listener.lastMouseMove();
        {
          // translate mouse movement to look position
          y_look += mouseMoved.getX()*GameFrame.MOUSEX_SCALE;
          x_look += mouseMoved.getY()*GameFrame.MOUSEY_SCALE;

          // constrain x
          if (x_look > Math.PI/3) x_look = (float)Math.PI/3;
          if (x_look < -Math.PI/3) x_look = (float)-Math.PI/3;

          // wrap y
          if (y_look > Math.PI*2) y_look -= Math.PI*2;
          if (y_look < 0) y_look += Math.PI*2;

          // start calculation of the quat
          quat.set(basequat);

          // rotate horizontaly
          axan.set(0f,1f,0f,y_look);
          newquat.set(axan);
          quat.mul(newquat);

          // rotate verticaly
          axan.set(1f,0f,0f,x_look);
          newquat.set(axan);
          quat.mul(newquat);

          // set the transform to the new quat
          transform.set(quat);
        }
      }

      // this must be done after the mouse is handled
      lasttime = nowtime;

      if (skyTG != null)
      {
        skyTransform.setTranslation(translation);
        skyTG.setTransform(skyTransform);
      }

      simpleU.getViewingPlatform().getViewPlatformTransform().getTransform(hudTransform);
      hudGroup.setTransform(hudTransform);
    }
  }
}