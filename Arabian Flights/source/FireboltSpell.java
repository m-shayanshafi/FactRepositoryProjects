// FireboltSpell.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import javax.media.j3d.*;
import javax.vecmath.*;

/** The firebolt is a basic spell effect that flys in a straght line until it hits
 * a monster, a player, or the ground. It is thrown by wizards and firestalks. It also
 * serves as a basis for fireballs, firewaves, and meteors.
 */
public class FireboltSpell extends Spell
{
  // the time, in milliseconds, between flips
  private static final long flipTime = 100;

  // the time, in milliseconds, of the last flip
  private long lastFlipTime;

  // how much damage it does
  private int damage;

  // the last time it made a smoke
  protected long lastsmoketime;

  // what it looks like
  public static Appearance fireApp = PhysicsEngine.createAppearance(GameFrame.STRING_FIREBOLT, 128, null);

  /** Creates a new firebolt at the owner, flying towards the target. */
  public FireboltSpell(Damageable owner, Vector3f direction, TeamInfo team, int damage)
  {
    super(owner, direction, team);
    this.damage = damage;

    // set the appearance
    setAppearance(fireApp);
    setScale(GameFrame.FIREBOLT_SCALE);

    setSpeed(GameFrame.FIREBOLT_SPEED);

    // set the expiration
    setExpireTime(GameFrame.CURRENT_TIME+(long)(GameFrame.FIREBOLT_DURATION));
  }

  /** gets the amount of damage that this firebolt does. */
  public int getDamage()
  {
    return damage;
  }

  /** As it moves, it flips around and leaves a smoke trail. */
  public void move(long time)
  {
    super.move(time);

    // create a smoke trail
    if (GameFrame.CURRENT_TIME >= lastsmoketime + SmokeMaker.SMOKE_PERIOD)
    {
      SmokeMaker.makeSmoke(this);
      lastsmoketime = GameFrame.CURRENT_TIME;
    }
  }

  /** When the Firebolt collides with the ground, it is destroyed and makes boom sound. */
  public void collidedGround()
  {
    GameFrame.sound.playSound(GameFrame.SOUND_BOOM, this);
    destroy();
  }

  /** When the firebolt collides with a damageable (other then its owner), it deals damage to it and is destroyed. */
  public void collided(Particle particle)
  {
    if (particle == getOwner()) return;
    if (!particle.isAlive()) return;

    if (particle instanceof Damageable)
    {
      Damageable mon = (Damageable)particle;
      if (mon.getTeam() == getTeam()) return;
      mon.damage(damage, getOwner());
      GameFrame.sound.playSound(GameFrame.SOUND_BOOM, this);
      this.destroy();
    }
  }
}