// SpiderMonster.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import javax.media.j3d.*;

/** The SpiderMonster is a jumping spider with 2 poses: sit and jump.
 * Spiders sit still until an enemy
 * comes close. When that happens, the spider will jump towards the enemy.
 * If it hits the enemy, then the enemy takes damage. AFter it lands,
 * it then sits and waits 1 second before jumping again.
 *
 * When the spider dies, it makes a new particle with the appearance of a
 * spider corpse.
 */
public class SpiderMonster extends Monster
{
  private Pose sitPose;
  private Pose jumpPose;
  private Appearance corpseApp;

  private final static float scale = 2;

  private float speed;
  private long waitTime;

  // the venom is activated when the spider jumps and deactivated when it collides
  // with the player, so the damage is dealt only once per jump
  private boolean venom = false;

  private int state;

  private final static int STATE_SITTING = 1;
  private final static int STATE_JUMPING = 2;
  private final static int STATE_WAITING = 3;

  // this is the time that the spider landed
  private long landTime;

  public SpiderMonster(TeamInfo team, float x, float z)
  {
    setTeam(team);
    sitPose = team.getSpiderSitPose();
    jumpPose = team.getSpiderJumpPose();
    corpseApp = team.getSpiderCorpseApp();

    setScale(scale);
    setPosition(x, 0, z);
    setAltitude(scale);
    setPose(sitPose);
    state = STATE_SITTING;

    setFaceAngle((float)(Math.random()*2*Math.PI));

    // each gets randomized stats
    speed = (float)(GameFrame.SPIDER_SPEED + Math.random()*GameFrame.SPIDER_SPEED/5);
    waitTime = (long)(GameFrame.SPIDER_WAIT + Math.random()*GameFrame.SPIDER_WAIT*.2);
    setDamage(GameFrame.SPIDER_DAMAGE);
    setHealth((int)(GameFrame.SPIDER_HEALTH_BASE+Math.random()*GameFrame.SPIDER_HEALTH_VAR));
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

  public void collided(Particle particle)
  {
    if (particle == getTarget())
    {
      if (venom)
      {
        if (particle instanceof Damageable)
        {
          Damageable mon = (Damageable)particle;
          if (mon.getTeam() == this.getTeam()) return;
          mon.damage(getDamage(), this);
          GameFrame.sound.playSound(GameFrame.SOUND_BOOM, this);
        }
      }
      venom = false;
    }
  }

  /** if it takes damage, jump at the source */
  public void damage(int damage, Damageable source)
  {
    super.damage(damage, source);

    if (state == STATE_SITTING)
    {
      jump(source);
    }
  }

  public void think(long time)
  {
    if (state == STATE_JUMPING)
    {
      y_vel -= GameFrame.SPIDER_FALL * time / 1000;

      // if the spider lands, change to waiting
      if (getAltitude() < scale)
      {
        setPose(sitPose);
        setVelocity(0,0,0);
        setAltitude(scale);
        state = STATE_WAITING;
        venom = false;
        landTime = GameFrame.CURRENT_TIME;
        setPose(sitPose);
      }
    }
    else if (state == STATE_SITTING)
    {
      // by default, follow the team wizard
      if (state == STATE_SITTING && getTeam().getWizard() != null && distance(getTeam().getWizard()) > GameFrame.SIGHT_RANGE / 3)
      {
        jump(getTeam().getWizard());
      }

      for (int i=0; i<getTeam().enemies(); i++)
      {
        // if an enemy is spotted, jump at it and change to jumping
        setTarget(getTeam().getEnemy(i));
        if (canSee(getTarget()))
        {
          jump(getTarget());
          break;
        }
      }
    }
    else if (state == STATE_WAITING)
    {
      // if enough time has elapsed, change to sitting
      if (GameFrame.CURRENT_TIME > landTime+waitTime) state = STATE_SITTING;
    }
  }

  private void jump(Particle target)
  {
    setPose(jumpPose);
    float direction = this.direction(target);
    setVelocity(-speed*(float)Math.sin(direction), calculateJumpYvel(target), speed*(float)Math.cos(direction));
    setFaceAngle((float)Math.PI+direction);
    state = STATE_JUMPING;
    venom = true;
    GameFrame.sound.playSound(GameFrame.SOUND_HISS, this);
  }

  // this calculates the y_vel, in m/s, necessary to intersect with the target
  private float calculateJumpYvel(Particle target)
  {
    float time = distance(target)/speed;
    float deltay = (target.y_pos - y_pos);

    if (deltay < 0) deltay = 0;

    float vel = deltay/time + GameFrame.SPIDER_FALL * time;

    // limit the y_vel
    if (vel > speed) vel = speed;
    if (vel < speed*.1f) vel = speed*.1f;

    return vel;
  }
}