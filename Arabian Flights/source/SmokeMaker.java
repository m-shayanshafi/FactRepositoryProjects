// SmokeMaker.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import javax.media.j3d.Appearance;
import javax.media.j3d.OrientedShape3D;

/** The SmokeMaker class handles smoke creation and conserves memory by
 * using a pool of smoke objects. It is entirely static, and cannot be instantiated.
 * Call SmokeMaker.init() only once and then SmokeMaker.makeSmoke() many times as needed.
 */
public class SmokeMaker
{
  // what the smoke looks like
  private static Appearance smokeApp = PhysicsEngine.createAppearance(GameFrame.STRING_SMOKE, 225, null, GameFrame.STRING_SMOKE_MASK);

  // how long things should wait between making smoke (ms)
  public static long SMOKE_PERIOD = 200;

  // the size of the smoke pool
  public static int NUM_SMOKES = 128;

  // how long the smoke lingers
  public static int SMOKE_LINGER_TIME = 3000;

  // an array of smoke objects
  private static Smoke [] smokearray;

  // the time that each smoke object will expire
  private static long [] timearray;

  // SmokeMaker cannot be instantiated
  private SmokeMaker()
  {
  }

  /** This method must be called before SmokeMaker.makeSmoke().
   * It creates the pool of smoke particles. */
  public static void init()
  {
    System.out.println("Making Smoke Pool");

    smokearray = new Smoke[NUM_SMOKES];
    timearray = new long[NUM_SMOKES];

    for (int i=0; i<NUM_SMOKES; i++)
    {
      smokearray[i] = new Smoke();
      GameFrame.physics.addParticle(smokearray[i]);
      timearray[i] = 0;
    }
  }

  /** This method takes a smoke particle from the pool and puts it at the position
   * specified. SmokeMaker.init() must be called first or this will throw a NullPointerException.
   */
  public static void makeSmoke(Particle particle)
  {
    // find the oldest smoke and make it the new smoke
    long besttime = Long.MAX_VALUE;
    int bestindex = 0;
    for (int i=0; i<NUM_SMOKES; i++)
    {
      if (timearray[i] < besttime)
      {
        besttime = timearray[i];
        bestindex = i;
        if (besttime == 0) break;
      }
    }

    Smoke newSmoke = smokearray[bestindex];
    newSmoke.setupSmoke(particle);
    timearray[bestindex] = newSmoke.getExpireTime();
  }

  /** Each Smoke object represents a puff of smoke. They are held in a pool
   * inside SmokeMaker and are moved around and shown as needed.
   */
  public static class Smoke extends Particle
  {
    // how quickly the smoke goes up (m/s)
    private static float drift_speed = 1.5f;

    // how quickly the smoke grows (m/s)
    private static float grow_rate = 1;

    // the default scale
    private static float scale = .7f;

    public Smoke()
    {
      super(OrientedShape3D.ROTATE_ABOUT_POINT);
      setAppearance(null);
    }

    public void setupSmoke(Particle p)
    {
      setPosition(p);
      setAppearance(smokeApp);
      setExpireTime(GameFrame.CURRENT_TIME + SMOKE_LINGER_TIME);
      setScale(scale);

      setVelocity(0,drift_speed,0);
    }

    public void move(long time)
    {
      super.move(time);

      setScale(getScale() + grow_rate*time/1000);
    }

    public void destroy()
    {
      setAppearance(null);
    }
  }
}