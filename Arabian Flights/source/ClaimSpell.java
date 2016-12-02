// ClaimSpell.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import javax.vecmath.*;

/** The claim spell is a ball that claims gold mines when it hits them. */
public class ClaimSpell extends Spell
{
  public ClaimSpell(Damageable owner, Vector3f direction, TeamInfo team)
  {
    super(owner, direction, team);
    setAppearance(team.getFlagApp());
  }

  public void collided()
  {
    // if it hits a gold mine, claim it
    // this is handled by the GoldMine
  }
}