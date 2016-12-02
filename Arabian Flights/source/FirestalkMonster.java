// FirestalkMonster.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import javax.media.j3d.*;
import javax.vecmath.*;

/** FireStalk is a Monster with 3 poses: still, back, forward. Firestalks are large plants that
 * shoot firebolts at nearby enemys. When it sees an enemy, it rears back for
 * one second, then it shoots the firbolt and leans forward for one second. When
 * the firestalk dies, it makes a new particle with the appearance of a firestalk
 * corpse.
 */
public class FirestalkMonster extends Monster
{
  private TeamInfo team;

  private Pose upPose;
  private Pose forePose;
  private Pose backPose;

  private Appearance corpseApp;

  // the last time the stalk took an action
  private long lastTime;

  // the time the stalk spends waiting between actions
  private long waitTime;

  private final static float scale = 3f;

  // a vector used for calculating the direction of the firebolts
  private static Vector3f direction = new Vector3f();

  private int state;
  private final static int STATE_UP = 1;
  private final static int STATE_FORE = 2;
  private final static int STATE_BACK = 3;

  /** Creates a new Firestalk at the specified location. */
  public FirestalkMonster(TeamInfo team, float x, float z)
  {
    // get the team info
    setTeam(team);

    upPose = team.getFirestalkUpPose();
    forePose = team.getFirestalkForePose();
    backPose = team.getFirestalkBackPose();
    corpseApp = team.getFirestalkCorpseApp();

    // random stats for each stalk
    waitTime = (long)(GameFrame.FIRESTALK_WAIT+Math.random()*GameFrame.FIRESTALK_WAIT*.2);
    setDamage(GameFrame.FIRESTALK_DAMAGE);
    setHealth((int)(GameFrame.FIRESTALK_HEALTH_BASE+Math.random()*GameFrame.FIRESTALK_HEALTH_VAR));

    // the stalk starts out upright
    setPosition(x, 0, z);

    state = STATE_UP;
    setPose(upPose);

    setType(TYPE_ROLLING);
    setScale(scale);
  }
  /** if it is damaged, fire at the source */
  public void damage(int damage, Damageable source)
  {
    super.damage(damage, source);

    if (state == STATE_UP)
    {
      state = STATE_BACK;
      setPose(backPose);
      lastTime = GameFrame.CURRENT_TIME;
    }
  }

  public void think(long time)
  {
    if (state == STATE_UP)
    {
      // if an enemy is spotted, rear back
      for (int i=0; i<getTeam().enemies(); i++)
      {
        setTarget(getTeam().getEnemy(i));
        if (canSee(getTarget()))
        {
          state = STATE_BACK;
          setPose(backPose);
          lastTime = GameFrame.CURRENT_TIME;
          break;
        }
      }
    }
    else if (state == STATE_FORE)
    {
      // if enough time has passed, return to upright position
      if (GameFrame.CURRENT_TIME > lastTime + waitTime)
      {
        state = STATE_UP;
        setPose(upPose);
      }
    }
    else if (state == STATE_BACK)
    {
      // if enough time has passed, fire the fireball and rear foreward
      if (GameFrame.CURRENT_TIME > lastTime + waitTime)
      {
        // set up the velicity vector
        Point3f position = getPosition();
        Point3f target = getTarget().getPosition();
        direction.x = target.x - position.x;
        direction.y = target.y - position.y;
        direction.z = target.z - position.z;
        direction.normalize();

        // shoot a firebolt at the target
        GameFrame.physics.addParticle(new FireboltSpell(this, direction, getTeam(), getDamage()));
        GameFrame.sound.playSound(GameFrame.SOUND_WHOOSH, this);

        // rear foreward
        setFaceAngle((float)Math.PI+direction(getTarget()));
        state = STATE_FORE;
        setPose(forePose);
        lastTime = GameFrame.CURRENT_TIME;
      }
    }
  }
  public void destroy()
  {
    // when this is destroyed, place a corpse here
    Particle corpse = new Particle();
    corpse.setPosition(this);
    corpse.setAppearance(corpseApp);
    corpse.setScale(scale);
    corpse.setAltitude(scale);
    corpse.setExpireTime(GameFrame.CURRENT_TIME + GameFrame.CORPSE_LINGER_TIME);
    GameFrame.physics.addParticle(corpse);

    super.destroy();
  }
}