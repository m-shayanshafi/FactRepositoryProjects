// SummonFireStalkSpell
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import javax.vecmath.*;

/** The SummonFireStalkSpell is a ball, and when it hits the ground, a new Firestalk
 * is created where it hit.
 */
public class SummonFirestalkSpell extends Spell
{
  public SummonFirestalkSpell(Damageable owner, Vector3f direction, TeamInfo team)
  {
    super(owner, direction, team);
    setAppearance(team.getSummonBallApp());
  }

  public void collidedGround()
  {
    if (getTeam().numFirestalks() > GameFrame.FIRESTALK_LIMIT)
    {
      GameFrame.renderer.getHud().setMessage("Too Many Firestalks on your team");
    }
    else
    {

      // check to be sure that there aren't too many firestalks in the area
      int count = 0;
      for (int i = 0; i < getTeam().members(); i++) {
        Particle p = getTeam().getMember(i);
        if (p instanceof FirestalkMonster && canSee(p)) {
          count++;
        }
      }

      if (count >= GameFrame.FIRESTALK_CROWD_SIZE) {
        GameFrame.renderer.getHud().setMessage("Too Many Firestalks Nearby");
        return;
      }

      GameFrame.physics.addParticle(new FirestalkMonster(getTeam(),
          getPosition().x, getPosition().z));
    }
    destroy();
  }
}