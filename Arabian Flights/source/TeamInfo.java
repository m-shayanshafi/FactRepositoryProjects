// TeamInfo.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import java.awt.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.util.Vector;

/** The TeamInfo class holds information for a single team. This information includes
 * all of the correctly-tinted sprites for the monsters, castle, balloons, player, etc.
 * It also contains a Vector of targettable particles, the player and his monsters.
 */

public class TeamInfo
{
  private static Vector allTeams = new Vector();

  // the color of the team
  private Color tint;
  private Color3f color;

  // the number of enemies this team has killed
  private int kills = 0;

  // the Vector of targets
  private Vector memberVector = new Vector();

  // the vector of enemy TeamInfo's
  private Vector enemyTeamVector = new Vector();

  // for the firestalk
  private Poseable.Pose firestalk_upPose;
  private Poseable.Pose firestalk_forePose;
  private Poseable.Pose firestalk_backPose;
  private Appearance firestalk_corpseApp;

  // for the Crane
  private Poseable.Pose crane_upPose;
  private Poseable.Pose crane_downPose;
  private Poseable.Pose crane_nestPose;
  private Appearance crane_nestApp;

  // for the Spider
  private Poseable.Pose spider_sitPose;
  private Poseable.Pose spider_jumpPose;
  private Appearance spider_corpseApp;

  // the wizard
  private Poseable.Pose wizard_floatPose;

  // for the flags
  public Appearance flagApp;
  public Appearance baloonApp;
  public Appearance summonBallApp;

  // this is the team's castle
  private Castle castle;

  // this is the team's wizard
  private Wizard wizard;

  public TeamInfo(Color tint)
  {
    allTeams.add(this);

    this.tint = tint;
    color = new Color3f(tint);

    // for the firestalk
    firestalk_upPose = new Monster.Pose(GameFrame.STRING_FIRESTALK_BASE + "_u", tint);
    firestalk_forePose = new Monster.Pose(GameFrame.STRING_FIRESTALK_BASE + "_f", tint);
    firestalk_backPose = new Monster.Pose(GameFrame.STRING_FIRESTALK_BASE + "_b", tint);
    firestalk_corpseApp = PhysicsEngine.createAppearance(GameFrame.STRING_FIRESTALK_BASE + "_dead.png", tint);

    // for the Crane
    crane_upPose = new Monster.Pose(GameFrame.STRING_CRANE_BASE + "_u", tint);
    crane_downPose = new Monster.Pose(GameFrame.STRING_CRANE_BASE + "_d", tint);
    crane_nestApp = PhysicsEngine.createAppearance(GameFrame.STRING_CRANE_BASE + "_nest.png", tint);

    // for the Spider
    spider_sitPose = new Monster.Pose(GameFrame.STRING_SPIDER_BASE + "_s", tint);
    spider_jumpPose = new Monster.Pose(GameFrame.STRING_SPIDER_BASE + "_j", tint);
    spider_corpseApp = PhysicsEngine.createAppearance(GameFrame.STRING_SPIDER_BASE + "_dead.png", tint);

    // for the Wizard
    wizard_floatPose = new Poseable.Pose(GameFrame.STRING_WIZARD_BASE, tint);

    // for the flag and baloon
    flagApp = PhysicsEngine.createAppearance(GameFrame.STRING_FLAG, 128, tint);
    baloonApp = PhysicsEngine.createAppearance(GameFrame.STRING_BALOON, tint);

    // for the summoning ball
    summonBallApp = PhysicsEngine.createAppearance(GameFrame.STRING_SUMMONBALL, 128, tint);
  }

  public Poseable.Pose getFirestalkUpPose() { return firestalk_upPose; }
  public Poseable.Pose getFirestalkForePose() { return firestalk_forePose; }
  public Poseable.Pose getFirestalkBackPose() { return firestalk_backPose; }
  public Poseable.Pose getSpiderSitPose() { return spider_sitPose; }
  public Poseable.Pose getSpiderJumpPose() { return spider_jumpPose; }
  public Poseable.Pose getCraneUpPose() { return crane_upPose; }
  public Poseable.Pose getCraneDownPose() { return crane_downPose; }
  public Poseable.Pose getWizardFloatPose() { return wizard_floatPose; }

  public Appearance getCraneNestApp() { return crane_nestApp; }
  public Appearance getFirestalkCorpseApp() { return firestalk_corpseApp; }
  public Appearance getSpiderCorpseApp() { return spider_corpseApp; }

  public Appearance getFlagApp() { return flagApp; }
  public Appearance getBaloonApp() { return baloonApp; }
  public Appearance getSummonBallApp() { return summonBallApp; }

  public int members() { return memberVector.size(); }
  public void addMember(Particle part) { memberVector.add(part); }
  public void removeMember(Particle part) { memberVector.remove(part); }
  public Particle getMember(int index) { return (Particle)memberVector.get(index); }

  public Color3f getColor3f() { return color; }

  /** informs each team if its enemies*/
  public static void setupEnemies()
  {
    // for each team
    for (int i=0; i<allTeams.size(); i++)
    {
      TeamInfo thisTeam = (TeamInfo)allTeams.get(i);

      // add all of the other teams to its enemy list
      for (int j=0; j<allTeams.size(); j++)
      {
        if (i != j)// dont add a team as an enemy of itself
        {
          // dont add the player's team if ghost mode is enabled
          if (!(GameFrame.GHOST_MODE && allTeams.get(j) == GameFrame.playerTeam))
          {
            thisTeam.addEnemyTeam( (TeamInfo) allTeams.get(j));
          }
        }
      }
    }
  }

  /** returns a referece to the teams wizard*/
  public Wizard getWizard()
  {
    return wizard;
  }

  /** Gets the team's castle.*/
  public Castle getCastle()
  {
    return castle;
  }

  /** returns true if the team has a castle.*/
  public boolean hasCastle()
  {
    if (castle == null) return false;
    return true;
  }

  /** Sets the team's castle.*/
  public void setCastle(Castle castle)
  {
    this.castle = castle;
  }

  /** Set's the team's wizard. */
  public void setWizard(Wizard wizard)
  {
    this.wizard = wizard;
  }

  /** The number of spiders on this team*/
  public int numSpiders()
  {
    int count = 0;
    for (int i=0; i<memberVector.size(); i++)
    {
      if (memberVector.get(i) instanceof SpiderMonster)
      {
        count++;
      }
    }
    return count;
  }

  /** The number of spiders on this team*/
  public int numFirestalks()
  {
    int count = 0;
    for (int i=0; i<memberVector.size(); i++)
    {
      if (memberVector.get(i) instanceof FirestalkMonster)
      {
        count++;
      }
    }
    return count;
  }

  /** called when an enemy is killed by this team*/
  public void killed(Damageable enemy)
  {
    kills++;
    if (getWizard() == GameFrame.player)
    {
      GameFrame.renderer.getHud().setKills(kills);
    }
  }


  /** returns the number of enemies of this team. */
  public int enemies()
  {
    int num = 0;
    TeamInfo team;

    for (int i=0; i<enemyTeamVector.size(); i++)
    {
      team = (TeamInfo)enemyTeamVector.get(i);
      num += team.members();
    }

    return num;
  }

  /** gets an enemy castle (returns null if that team has no castle)*/
  public Castle getEnemyCastle(int index)
  {
    return ((TeamInfo)(enemyTeamVector.get(index))).getCastle();
  }

  /** the number of enemy castles */
  public int numEnemyCastles()
  {
    return enemyTeamVector.size();
  }

  /** gets an enemy of this team. */
  public Particle getEnemy(int index)
  {
    int num = 0;
    TeamInfo team;

    for (int i=0; i<enemyTeamVector.size(); i++)
    {
      team = (TeamInfo)enemyTeamVector.get(i);

      if (num + team.members() > index)
      {
        return (Particle)team.getMember(index - num);
      }

      num += team.members();
    }

    return null;
  }

  public void addEnemyTeam(TeamInfo enemyTeam)
  {
    enemyTeamVector.add(enemyTeam);
  }

  public void removeEnemyTeam(TeamInfo enemyTeam)
  {
    enemyTeamVector.remove(enemyTeam);
  }
}