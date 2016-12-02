// CraneMonster.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;


/**
 * The Crane is a Monster with 2 poses: wings up, wings down. Cranes will orbit their nest
 * until an enemy comes within a threatening distance, then they attack the
 * enemy by flying towards it. When they collide, the crane dies, the enemy
 * takes damage and a new crane is born from the nest. If the enemy gets a
 * certain distance away from the crane, it will return to orbiting it's nest.
 */
public class CraneMonster extends Monster
{
  private Pose upPose;
  private Pose downPose;

  private long lastflaptime = 0;
  private boolean lastflapup = true;

  // these are randomized for each crane
  private long flaptime;
  private float speed;
  private float orbit_distance;

  private TeamInfo team;
  private Nest nest;
  private int state;

  private static int STATE_FLYING_OUT_TO_ORBIT = 1;
  private static int STATE_ORBITING = 2;
  private static int STATE_ATTACKING = 3;
  private static int STATE_RETURNING_TO_ORBIT = 4;

  /** Make a new crane on a given team at a specified nest. */
  public CraneMonster(TeamInfo team, Nest nest)
  {
    this.nest = nest;
    setTeam(team);

    upPose = team.getCraneUpPose();
    downPose = team.getCraneDownPose();

    // each crane gets different stats
    flaptime = (long)(150+Math.random()*100);
    speed = (float)(GameFrame.CRANE_SPEED+Math.random()*GameFrame.CRANE_SPEED/5f);
    orbit_distance = (float)(GameFrame.CRANE_ORBIT_DISTANCE+Math.random()*GameFrame.CRANE_ORBIT_DISTANCE_VAR);

    setDamage(GameFrame.CRANE_DAMAGE);
    setHealth((int)(GameFrame.CRANE_HEALTH_BASE+Math.random()*GameFrame.CRANE_HEALTH_VAR));

    state = STATE_FLYING_OUT_TO_ORBIT;

    // the crane starts out at the nest flying randomly
    this.setType(Particle.TYPE_LINEAR);
    this.setFaceAngle((float)Math.PI);
    this.setPosition(nest);

    float angle = (float)(Math.random()*2*Math.PI);
    this.setVelocity(speed*(float)Math.cos(angle),0,speed*(float)Math.sin(angle));
    this.setPose(upPose);
  }

  /** Cranes orbit thier nests until an enemy is spotted. They then fly towards the enemy
   * until they collide or the enemy gets too far away, at which point they return to orbitting
   * their nest.
   */
  public void think(long time)
  {
    // if it's nest has gone dormant, self-destruct
    if (nest.isDormant()) permanentDestroy();

    // stay in the fly zone
    if (getAltitude() < GameFrame.WIZARD_FLOAT_HEIGHT) setAltitude(GameFrame.WIZARD_FLOAT_HEIGHT);

    // flap every half second
    long currentTime = GameFrame.CURRENT_TIME;
    if (lastflaptime+flaptime <= currentTime)
    {
      if (lastflapup)
      {
        lastflapup = false;
        lastflaptime = currentTime;
        setPose(downPose);
      }
      else
      {
        lastflapup = true;
        lastflaptime = currentTime;
        setPose(upPose);
      }
    }

    if (nest == null) return;

    if (state == STATE_FLYING_OUT_TO_ORBIT)
    {
      // if it has reached the orbit distance, return to orbit
      if (distance(nest) >= orbit_distance) state = STATE_RETURNING_TO_ORBIT;
    }
    if (state == STATE_ATTACKING)
    {
      // if the target is out of sight, return to the orbit
      if (!canSee(getTarget()))
      {
        // if the crane gets too far away from the nest, it self destructs.
        if (distance(nest) > 2*orbit_distance) destroy();

        // otherwise, it flys back.
        if (distance(nest) < orbit_distance) state = STATE_FLYING_OUT_TO_ORBIT;
        else state = STATE_RETURNING_TO_ORBIT;
      }

      // move towards the target
      moveTowards(getTarget(), speed);

      // face the target
      float direction = direction(getTarget());
      setFaceAngle((float)Math.PI+direction);
    }
    if (state == STATE_ORBITING)
    {
      // look for enemies
      for (int i=0; i<getTeam().enemies(); i++)
      {
        setTarget(getTeam().getEnemy(i));
        if (canSee(getTarget()))
        {
          state = STATE_ATTACKING;
          GameFrame.sound.playSound(GameFrame.SOUND_ZAP, this);
          break;
        }
      }

      // if the nest is dead, suicide
      if (!nest.isAlive())
        destroy();

      // fly perpindicular to the nest
      float angle = direction(nest)+(float)Math.PI/2;
      setVelocity(-speed*(float)Math.sin(angle), 0, speed*(float)Math.cos(angle));
      setFaceAngle(angle+(float)Math.PI);
    }
    if (state == STATE_RETURNING_TO_ORBIT)
    {
      // if it has reached the orbit distance, start orbiting
      if (distance(nest) <= orbit_distance) state = STATE_ORBITING;

      // fly towards the nest
      float angle = direction(nest);
      setVelocity(-speed*(float)Math.sin(angle), 0, speed*(float)Math.cos(angle));
    }
  }

  /** When cranes collide with their target, they explode and do damage. */
  public void collided(Particle particle)
  {
    // kamakazi the target
    if (particle == getTarget())
    {
      if (particle instanceof Damageable)
      {
        Damageable mon = (Damageable)particle;
        mon.damage(getDamage(), this);
        GameFrame.sound.playSound(GameFrame.SOUND_BOOM, this);
        this.destroy();
        SmokeMaker.makeSmoke(this);
      }
    }
  }

  public void destroy()
  {
    if (isAlive()) nest.newCrane();
    super.destroy();
  }

  public void permanentDestroy()
  {
    super.destroy();
  }


  public static class Nest extends Damageable
  {
    private boolean dormant = false;

   /** When a nest is dormant, it has no cranes. */
   public void setDormant(boolean dormant)
   {
     this.dormant = dormant;
   }

   /** When a nest is dormant, it has no cranes. */
   public boolean isDormant()
   {
     return dormant;
   }

    /** Creates a new crane. */
    public void newCrane()
    {
      if (isAlive() && !isDormant())
        GameFrame.physics.addParticle(new CraneMonster(getTeam(), this));
    }
  }

  public static class WildNest extends Nest
  {
    /** Creates a new nest, which produces the specified number of cranes automatically. */
    public WildNest(TeamInfo team, float x, float z, int numCranes)
    {
      setTeam(team);
      setHealth((int)(10+Math.random()*10));

      setPosition(x, 0, z);
      setType(TYPE_ROLLING);
      setAppearance(team.getCraneNestApp());
      setVisibility(Particle.VISIBLE);
      setColor3f(team.getColor3f());

      for (int i=0; i<numCranes; i++) newCrane();
    }
  }
}