// CastleSpell.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import javax.vecmath.*;

/** The casltespell makes a new castle when it hits the ground. */
public class CastleSpell extends Spell
{
  // how much the spell turns in one second
  private static final float turnrate = .008f;

  // the speed of the spell
  private final float speed;

  private long lastsmoketime = 0;

  public CastleSpell(Damageable owner, Vector3f direction, TeamInfo team)
  {
    super(owner, direction, team);
    setAppearance(Spell.defaultAppearance);

    speed = getSpeed();

    setExpireTime(0);
    setColor3f(team.getColor3f());
    setVisibility(VISIBLE);
    setScale(GameFrame.CASTLE_SPELL_SCALE);
  }

  /** This spell homes in on the team's castle flag. */
  public void move(long time)
  {
    if (getTeam().hasCastle())
    {
      Point3f pos = this.getPosition(); // current position
      Point3f fpos = this.getTeam().getCastle().getFlag().getPosition(); // flag position
      Vector3f vel = this.getVelocity(); // current velocity
      float dist = distance(this.getTeam().getCastle().getFlag());
      if (dist > 3) dist = 3;

      // find the desired velocity (toward the flag)
      Vector3f fvel = new Vector3f(fpos.x-pos.x, fpos.y-pos.y, fpos.z-pos.z);
      fvel.normalize();
      fvel.scale(getSpeed());

      // perform a weighted average of the desired velocity and current velocity
      Vector3f newvel = new Vector3f(
          (fvel.x*turnrate*time/dist + vel.x*(1-turnrate*time/dist)),
          (fvel.y*turnrate*time/dist + vel.y*(1-turnrate*time/dist)),
          (fvel.z*turnrate*time/dist + vel.z*(1-turnrate*time/dist)));

      // set the new velocity
      setVelocity(newvel.x, newvel.y, newvel.z);
      setSpeed(speed);
    }

    // create a smoke trail
    if (GameFrame.CURRENT_TIME >= lastsmoketime + SmokeMaker.SMOKE_PERIOD)
    {
      SmokeMaker.makeSmoke(this);
      lastsmoketime = GameFrame.CURRENT_TIME;
    }

    super.move(time);
  }

  public void collidedGround()
  {
    if (getTeam().hasCastle())
    {
      setType(TYPE_ROLLING);
    }
    else
    {
      // ignore it if it's too close to another building
      for (int i=0; i < GameFrame.physics.numBuildings(); i++)
      {
        if (GameFrame.physics.getBuilding(i).distance(this) < GameFrame.BUILDING_DISTANCE)
        {
          destroy();

          if (getTeam().getWizard() instanceof Player)
          {
            GameFrame.renderer.getHud().setMessage("Castle needs more space!");
          }
          return;
        }
      }

      // make a castle here
      Point3f pos = getPosition();
      new Castle(pos.x,pos.z,getTeam());
      destroy();
      GameFrame.renderer.getHud().setMessage("");
    }
  }
}