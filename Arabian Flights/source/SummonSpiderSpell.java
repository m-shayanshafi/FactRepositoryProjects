// SummonSpiderSpell
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import javax.vecmath.*;

/** The SummonSpiderSpell shoots out a summoning ball, and when it hits the ground,
 * a new Spider is created where it lands.
 */
public class SummonSpiderSpell extends Spell
{
  public SummonSpiderSpell(Damageable owner, Vector3f direction, TeamInfo team)
  {
    super(owner, direction, team);
    setAppearance(team.getSummonBallApp());
  }

  public void collidedGround()
  {
    if (getTeam().numSpiders() > GameFrame.SPIDER_LIMIT)
    {
      GameFrame.renderer.getHud().setMessage("Too Many Spiders on your team");
    }
    else
    {
      GameFrame.physics.addParticle(new SpiderMonster(getTeam(),
          getPosition().x, getPosition().z));
    }
    destroy();
  }
}