// AiWizard.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import javax.vecmath.*;

/** This class is a computer controlled wizard. It builds and upgrades a castlem,'
 * pursues and guards gold mines, and fights enemies. It does not extend from monster
 * or use a think() method. Since the whole class is AI, a think method is redundant.
 */

public class AiWizard extends Wizard
{
  // the one and only pose
  private Pose floatPose;

  // the current mode of the ai
  private int mode;

  // the modes
  private static final int MODE_BUILD_CASTLE = 0;
  private static final int MODE_FIGHT = 1;
  private static final int MODE_FIND_GOLD = 2;
  private static final int MODE_ATTACK = 3;
  private static final int MODE_DEFEND = 4;

  // the last time the castle was attacked
  private long lastCastleDamageTime = 0;

  private Particle target;
  private Vector3f lookVector = new Vector3f();

  // the last time the wizard cast a spell
  private long lastCastTime = 0;

  // the last time the wizard cast a upgrade spell
  private long lastUpgradeCastTime = 0;

  // the last time the wizard cast a claim spell
  private long lastClaimCastTime = 0;

  /** Constructor */
  public AiWizard(TeamInfo team)
  {
    super(team);
    setPose(team.getWizardFloatPose());
    setVisibility(ALWAYS_VISIBLE);
    setColor3f(getTeam().getColor3f());

    mode = MODE_BUILD_CASTLE;
  }

  public void setColor3f(Color3f color)
  {
    super.setColor3f(color);
    System.out.println("setting ai wizard color " + color);
  }

  protected javax.vecmath.Vector3f getLookVector()
  {
    return lookVector;
  }

  // turns the ai to face its target
  protected void faceTarget()
  {
    lookVector.set(
        target.x_pos - x_pos,
        target.y_pos - y_pos,
        target.z_pos - z_pos);

    float angle = direction(getTarget()) + (float) Math.PI;
    setFaceAngle(angle);
  }

  // move towards the target
  protected void moveToTarget()
  {
    float angle = direction(getTarget());
    setVelocity(-GameFrame.WIZARD_SPEED*(float)Math.sin(angle), 0, GameFrame.WIZARD_SPEED*(float)Math.cos(angle));
  }

  // circle around target
  protected void orbitTarget()
  {
    float angle = direction(getTarget())+(float)Math.PI/2f;
    setVelocity(-GameFrame.WIZARD_SPEED*(float)Math.sin(angle), 0, GameFrame.WIZARD_SPEED*(float)Math.cos(angle));
  }

  // returns true if enough time has passed since the last spell
  // this should return true before the wizard casts any spell
  protected boolean readyToCast()
  {
    return (lastCastTime <= GameFrame.CURRENT_TIME - GameFrame.CAST_TIME);
  }

  // this should be called whenever the ai casts a spell
  protected void resetCastTime()
  {
    lastCastTime = GameFrame.CURRENT_TIME;
  }

  public void damage(int damage, Damageable source)
  {
    super.damage(damage, source);
    setTarget(source);
    mode = MODE_FIGHT;
  }

  private void claimVisibleMineCheck()
  {
    // dont even check if we cant cast the spell
    if (!readyToCast()) return;

    for (int i=0; i<GameFrame.physics.numBuildings(); i++)
    {
      Building b = GameFrame.physics.getBuilding(i);

      if (b instanceof GoldMine)
      {
        GoldMine g = (GoldMine)b;
        if (g.getFlag().getTeam() == null && canSee(g.getFlag()))
        {
          setTarget(g.getFlag());
          faceTarget();
          castClaim();
          resetCastTime();
        }
      }
    }

  }

  private void castUpgradeCheck()
  {
    // dont even check if we cant cast the spell
    if (!readyToCast()) return;

    // upgrade the castle if possible
    if (getLevel() == 1 && getGold() >= GameFrame.CASTLE_LEVEL2COST
        || getLevel() == 2 && getGold() >= GameFrame.CASTLE_LEVEL3COST
        || getLevel() == 3 && getGold() >= GameFrame.CASTLE_LEVEL4COST
        || getLevel() == 4 && getGold() >= GameFrame.CASTLE_LEVEL5COST)
    {
      // cast the upgrade castle spell
      if (getMana() >= GameFrame.SPELL_CASTLE_MANA_COST
          && lastUpgradeCastTime <= GameFrame.CURRENT_TIME - 20000) {
        castCastle();
        mode = MODE_FIND_GOLD;
        resetCastTime();
        lastUpgradeCastTime = GameFrame.CURRENT_TIME;
      }
    }
  }

  // this is the part of the AI that is called every frame
  public void move(long time)
  {
    super.move(time);

    Profiler.setRenderThreadBlock(Profiler.BLOCK_AITHINK);

    // check for unclaimed mines
    claimVisibleMineCheck();

    // upgrade the castle if possible
    castUpgradeCheck();

    // make a castle if we don't have one
    if (getTeam().getCastle() == null || getTeam().getCastle().getFlag().isAlive() == false)
    {
      mode = MODE_BUILD_CASTLE;
    }

    // try to find an enemy
    Particle oldTarget = getTarget();
    for (int i=0; i<getTeam().enemies(); i++)
    {
      // if an enemy is spotted, face it and target it
      setTarget(getTeam().getEnemy(i));
      if (canSee(getTarget()))
      {
        faceTarget();
        mode = MODE_FIGHT;
        break;
      }
      setTarget(oldTarget);
    }

    // act according to the mode
    switch (mode)
    {
      case MODE_ATTACK:
        modeAttack();
        break;

      case MODE_DEFEND:
        modeDefend();
        break;

      case MODE_FIND_GOLD:
        modeFindGold();
        break;

      case MODE_BUILD_CASTLE:
        modeBuildCastle();
        break;

      case MODE_FIGHT:
        modeFight();
        break;

      default:
        modeFindGold();
    }

    Profiler.setRenderThreadBlock(Profiler.BLOCK_MOVE);
  }

    private void modeBuildCastle()
    {
      // try to build a castle where it is, if it's too close to a gold mine, move away.

      // find the closest gold mine
      float nearestDistance = Float.MAX_VALUE;
      GoldMine nearestMine = null;

      for (int i=0; i<GameFrame.physics.numBuildings(); i++)
      {
        Building b = GameFrame.physics.getBuilding(i);
        if (b instanceof GoldMine)
        {
          float dist = b.distance(this);

          if (dist < nearestDistance)
          {
            nearestMine = (GoldMine)b;
            nearestDistance = dist;
          }
        }
      }

      // move away from the mine
      if (nearestMine != null)
      {
        moveTowards(nearestMine.getFlag(), -GameFrame.WIZARD_SPEED);
      }

      // fire a castle spell
      if (nearestDistance > GameFrame.BUILDING_DISTANCE*1.1 && getMana() >= GameFrame.SPELL_CASTLE_MANA_COST)
      {
        lookVector.set(0,-1,0);

        if (readyToCast() && getMana() >= GameFrame.SPELL_CASTLE_MANA_COST)
        {
          castCastle();
          mode = MODE_FIND_GOLD;
          setVelocity(0, 0, 0);
          resetCastTime();
          setTarget(null);
        }
      }
    }

    private void modeFight()
    {
      // attack the nearest enemy, and circle strafe it

      // if we have a visible enemy
      if (getTarget() != null && getTarget() instanceof Damageable
          && canSee(getTarget()) && isEnemy((Damageable)getTarget()))
      {
        // attack the enemy
        if (readyToCast())
        {
          if (getLevel() >= GameFrame.SPELL_FIREWAVE_LEVEL && getMana() >= GameFrame.SPELL_FIREWAVE_MANA_COST)
          {
            castFirewave();
            resetCastTime();
          }
          else if (getMana() >= GameFrame.SPELL_FIREBOLT_MANA_COST)
          {
            castFirebolt();
            resetCastTime();
          }
        }

        // circle strafe the target
        orbitTarget();
        faceTarget();
      }
      else // otherwise
      {
        setVelocity(0,0,0);
        mode = MODE_FIND_GOLD;
      }
    }

    private void modeFindGold()
    {
      // go to the nearest unclaimed (by this team) gold mine, and claim it

      // if we have targetted a mine
      if (getTarget() instanceof GoldMine.Flag && ((GoldMine.Flag)getTarget()).getTeam() == null)
      {
        // the claiming will be handled by claimVisibleMineCheck();
      }
      else // find a mine
      {
        // iterate through all the buildings
        float bestDistance = Float.MAX_VALUE;
        GoldMine mine = null;
        GoldMine nearestMine = null;
        for (int i=0; i<GameFrame.physics.numBuildings(); i++)
        {
          Building building = GameFrame.physics.getBuilding(i);

          // if the nearest unclaimed gold mine yet
          if (building instanceof GoldMine)
          {
            mine = (GoldMine)building;
            if (mine.getFlag().getTeam() == null && mine.distance(this) < bestDistance)
            {
              // set it as the nearest mine
              bestDistance = mine.distance(this);
              nearestMine = mine;
            }
          }
        }

        // if a nearest mine was found
        if (nearestMine != null)
        {
          setTarget(nearestMine.getFlag());
          faceTarget();
          moveToTarget();
        }
        else
        {
          // no mines left
          setVelocity(0,0,0);
          setTarget(null);
          mode = MODE_ATTACK;
        }
      }
    }

    private void modeAttack()
    {
      // go to the nearest enemy

      if (getTarget() instanceof Damageable && getTarget().isAlive() && ((Damageable)getTarget()).isEnemy(this))
      {
        // if we are already targetting an enemy, move towards it until we can see it
        faceTarget();
        moveToTarget();
      }
      else
      {
        // find the nearest enemy
        float bestDistance = Float.MAX_VALUE;
        Damageable bestEnemy = null;
        for (int i=0; i<getTeam().enemies(); i++)
        {
          Particle enemy = getTeam().getEnemy(i);
          if (enemy != null && distance(enemy) < bestDistance)
          {
            bestEnemy = (Damageable)enemy;
          }
        }

        // if a enemy was found, go to it
        if (bestEnemy != null)
        {
          setTarget(bestEnemy);
          faceTarget();
          moveToTarget();
        }
        else
        {
          // if no enemy was found, new mode
          setVelocity(0,0,0);
          setTarget(null);
          mode = MODE_DEFEND;
        }
      }
    }

    private void modeDefend()
    {
      if (distance(getTeam().getCastle().getFlag()) > GameFrame.CRANE_ORBIT_DISTANCE)
      {
        // go to own castle.
        setTarget(getTeam().getCastle().getFlag());
        faceTarget();
        moveToTarget();
      }
      else
      {
        // orbit the castle
        setTarget(getTeam().getCastle().getFlag());
        orbitTarget();

        // give up after 10 seconds
        if (lastCastleDamageTime < GameFrame.CURRENT_TIME - 10000)
        {
          mode = MODE_FIND_GOLD;
        }
      }
    }

  /** when the castle is damaged, defend it     */
  public void castleDamaged()
  {
    super.castleDamaged();
    mode = MODE_DEFEND;
    lastCastleDamageTime = GameFrame.CURRENT_TIME;
  }

  /** set the target */
  protected void setTarget(Particle p)
  {
    target = p;
  }

  /** get the target */
  protected Particle getTarget()
  {
    return target;
  }
}