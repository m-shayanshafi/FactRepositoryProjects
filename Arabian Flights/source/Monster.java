// Monster.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

/** The monster class is the superclass the Firestalk, the Spider, and the Crane.
 * Each of them has a target particle and a damage amount, which are stored here.
 * Also, the method canSee() is provided for all three. Furthermore, the move() in this
 * class calls the think() of the subclasses.
 */

public abstract class Monster extends Poseable
{
  // the current target of this monster's aggression
  private Particle target;

  // the amount of damage this monster inflicts
  private int damage;

  public Monster()
  {
    setVisibility(VISIBLE);
  }

  /** sets the damage done by this monster.  */
  public void setDamage(int damage)
  {
    this.damage = damage;
  }

  /** returns the damage done by this monster. */
  public int getDamage()
  {
    return damage;
  }

  /** returns the particle that the monster is attacking. */
  public Particle getTarget()
  {
    return target;
  }

  /** sets the target that the monster is attacking. */
  public void setTarget(Particle target)
  {
    this.target = target;
  }

  /** When a monster is damaged, it targets whatever hurt it. */
  public void damage(int damage, Damageable source)
  {
    super.damage(damage, source);
    setTarget(source);
  }

  /** This is overridden to call think() every frame, before super.move() is called.
   * Note: this is not done by Poseable.move() because wizards have a different AI then
   * monsters. */
  public void move(long time)
  {
    Profiler.setRenderThreadBlock(Profiler.BLOCK_THINK);

    think(time);

    Profiler.setRenderThreadBlock(Profiler.BLOCK_MOVE);

    super.move(time);
  }

  /** this method will be called every frame, and should be overridden. */
  public abstract void think(long time);
}