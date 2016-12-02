// MeteorSpell.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import javax.media.j3d.*;
import javax.vecmath.*;

/** The Meteor is a large rock that arches through the air, and when it hits the ground, it explodes into several rolling rocks,
 * which radiate in all directions. As it goes through the air, it leaves a smoke trail, and it also leaves lots of smoke where it lands.
 */
public class MeteorSpell extends FireboltSpell
{
  // what the meteor looks like
  private static Appearance meteorApp = PhysicsEngine.createAppearance(GameFrame.STRING_METEOR);

  // what the rocks looks like
  private static Appearance rockApp = PhysicsEngine.createAppearance(GameFrame.STRING_FIREBOLT);

  // a vector used for calculating the direction of the rocks
  private static Vector3f direction = new Vector3f();

  /** This creates a new meteor. */
  public MeteorSpell(Damageable owner, Vector3f direction, TeamInfo team, int damage)
  {
    super(owner, direction, team, damage);
    setAppearance(meteorApp);
    setScale(GameFrame.METEOR_SCALE);
    setColor3f(team.getColor3f());
    setVisibility(Particle.VISIBLE);
    setExpireTime(0);
  }

  /** As it moves, it falls slowly. */
  public void move(long time)
  {
    super.move(time);

    // fall
    Vector3f vel = this.getVelocity();
    this.setVelocity(vel.x, vel.y - GameFrame.METEOR_FALL, vel.z);
  }

  /** Anything in the way of the meteor is instantly killed. */
  public void collided(Particle p)
  {
    if (p instanceof Damageable && p != getOwner()) ((Damageable)p).damage(100*GameFrame.METEOR_DAMAGE, getOwner());
  }

  /** When the meteor hits the ground, many rolling rocks are sent out in all
   * directions. These provide a lot of collateral damage.
   */
  public void collidedGround()
  {
    if (!isAlive()) return;

    for (int i=0; i<GameFrame.METEOR_BALLS; i++)
    {
      // set up the velicity vector
      direction.x = (float)Math.sin(i*2*Math.PI/GameFrame.METEOR_BALLS);
      direction.y = 0;
      direction.z = (float)Math.cos(i*2*Math.PI/GameFrame.METEOR_BALLS);

      GameFrame.physics.addParticle(new Ball(getOwner(),this,direction, getDamage()));
    }

    super.collidedGround();
  }

  public static class Ball extends FireboltSpell
  {
    public Ball(Damageable owner, MeteorSpell origin, Vector3f direction, int damage)
    {
      super(owner, direction, owner.getTeam(), damage);

      setPosition(origin);
      setAppearance(rockApp);
      setExpireTime(GameFrame.CURRENT_TIME + (long)(GameFrame.METEOR_BALL_TIME*1000));
      setSpeed(GameFrame.METEOR_BALL_SPEED);

      // its a roller
      setType(Particle.TYPE_ROLLING);

      // delay the smoke generation
      lastsmoketime = GameFrame.CURRENT_TIME + SmokeMaker.SMOKE_PERIOD;
    }
  }
}