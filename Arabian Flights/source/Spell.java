// Spell.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import javax.media.j3d.*;
import javax.vecmath.*;

/** A spell is a particle that moves linearly until it hits the ground or a particle,
 * at which point some effect occurs. This is the superclass of all player spells.
 */
public class Spell extends Particle
{
  public static final Appearance defaultAppearance = PhysicsEngine.createAppearance(GameFrame.STRING_ROCK, 128, null);

  // the speed, in meters per second, that the spell travels
  private float speed = GameFrame.SPELL_SPEED;

  // the time, in milliseconds, between flips
  private static final long flipTime = 100;

  // the time, in milliseconds, of the last flip
  private long lastFlipTime;

  // who threw the spell
  private Damageable owner;

  // the team of the spell
  private TeamInfo team;

  // how far away the spell starts from the player
  private static float startDist = .4f;

  // A vector to recalculate speed
  private static Vector3f speedvec = new Vector3f();

  public Spell(Damageable owner, Vector3f direction, TeamInfo team)
  {
    super(OrientedShape3D.ROTATE_ABOUT_POINT);
    this.team = team;
    this.owner = owner;

    // set up the velicity vector
    Point3f position = owner.getPosition();
    direction.normalize();
    setVelocity(direction.x*speed, direction.y*speed, direction.z*speed);

    // set the position to the owner's position
    setPosition(position.x + direction.x*startDist, position.y + direction.y*startDist-0.3f, position.z + direction.z*startDist-0.4f);

    // set the expiration
    setExpireTime(GameFrame.CURRENT_TIME+(long)(GameFrame.SPELL_DURATION));

    setVisibility(VISIBLE);
    setColor3f(getTeam().getColor3f());

    setScale(GameFrame.SPELL_SCALE);
  }

  public void setSpeed(float speed)
  {
    this.speed = speed;

    speedvec = getVelocity();
    speedvec.normalize();
    speedvec.x *= speed;
    speedvec.y *= speed;
    speedvec.z *= speed;

    setVelocity(speedvec.x, speedvec.y, speedvec.z);
  }

  public float getSpeed()
  {
    return speed;
  }

  public void move(long time)
  {
    super.move(time);

    if(getType() != TYPE_ROLLING && getAltitude() < 0) collidedGround();

    if (GameFrame.CURRENT_TIME > lastFlipTime + flipTime)
    {
      flip();
      lastFlipTime = GameFrame.CURRENT_TIME;
    }
  }

  public void collidedGround()
  {
    this.destroy();
  }

  public TeamInfo getTeam()
  {
    return team;
  }

  public Damageable getOwner()
  {
    return owner;
  }
}