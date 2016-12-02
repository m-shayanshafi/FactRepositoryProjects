// FirewaveSpell.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import javax.vecmath.*;

/** The firewave spell is a fireball that becomes a rolling
 * particle when it hits the ground. They are typically fired off
 * in groups by the player.castFirewave() method.
 */
public class FirewaveSpell extends FireboltSpell
{
  public FirewaveSpell(Damageable owner, Vector3f direction, TeamInfo team, int damage)
  {
    super(owner, direction, team, damage);
  }

  public void collidedGround()
  {
    setType(Particle.TYPE_ROLLING);
  }
}