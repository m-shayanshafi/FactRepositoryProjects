// Damageable.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

/** The Damageable class encompasses all game objects that take damage, including
 * monsters, wizards, buildings, baloons, etc...
 * Each of these objects is also associated with a team, so that friendly fire can
 * be ignored.
 */
public class Damageable extends Particle
{
  // the health of the Damageable
  private int health = 0;

  // all Damageable belong on a team
  private TeamInfo team;

  /** deals damage to the Damageable, reducing its health. */
  public void damage(int damage, Damageable source)
  {
    // no friendly fire
    if (source.getTeam() == this.getTeam()) return;

    decreaseHealth(damage);

    if (getHealth() <= 0)
    {
      // kill this
      source.getTeam().killed(this);
      this.destroy();
    }
  }

  /** when a damageable is destroyed, it is removed from it's team. */
  public void destroy()
  {
    getTeam().removeMember(this);
    super.destroy();
  }

  /** Sets the health of the Damageable. */
  public void setHealth(int health)
  {
    this.health = health;
  }

  /** Gets the health of the Damageable. */
  public int getHealth()
  {
    return health;
  }

  /** Increase the Damageable's health. */
  public void increaseHealth(int amount)
  {
    setHealth(health + amount);
  }

  /** Decrease the Damageable's health. */
  public void decreaseHealth(int amount)
  {
    setHealth(health - amount);
  }

  /** returns the team this Damageable is on. */
  public TeamInfo getTeam()
  {
    return team;
  }

  /** returns true if the Damageable is an enemy */
  public boolean isEnemy(Damageable d)
  {
    return (d.getTeam() != getTeam());
  }

  /** sets the team this Damageable is on.
   * Setting it to null removes it from the team its on. */
  public void setTeam(TeamInfo newteam)
  {
    if (team != null) team.removeMember(this);
    if (newteam != null) newteam.addMember(this);
    team = newteam;

    if (team != null) setColor3f(team.getColor3f());
  }

  /** Checks if the Damageable is on the same team as this one. */
  public boolean sameTeam(Damageable damageable)
  {
    return (damageable.getTeam() == getTeam());
  }
}