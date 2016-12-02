// Castle.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.*;
import javax.vecmath.*;

/** The Castle class consists of multiple buildings which make up the Castle.
 * The Castle levels up by adding more towers. It has five levels. The first
 * level has only the center tower. The second has four surrounding towers. The
 * third adds a walls, and the fourth level
 * adds eight towers around the outside. The fifth level has walls between all
 * the towers. To start or level up a castle, the wizard casts the CastleSpell.
 *
 * Each of the towers is guarded by 3 cranes.
 *
 * The baloons and nests can be destroyed. To restore them, the wizard casts the CastleSpell
 * again, and if there is not enough gold for an upgrade, a repair will be done, which will restore
 * all of the nests and baloons.
 */

public class Castle
{
  // the appearances of the towers and walls
  private static Appearance towerapp = Building.createAppearance(GameFrame.STRING_CASTLETOWER);
  private static Appearance wallapp = Building.createAppearance(GameFrame.STRING_CASTLEWALL);

  // the heights of the towers and walls (in meters)
  private static float towerscale = 12f;
  private static float wallscale = 8f;
  private static float flagscale = 2f;

  // the number of cranes per nest
  private static int numCranes = 2;

  // U is a unit used to position towers and walls
  private static float U = GameFrame.MAP_GRID_SCALE * 2;

  // the towers
  private Tower  towerC, // the center tower
      towerNE,towerNW,towerSE,towerSW, // the inner ring of towers
      towerNE2, towerNW2, towerSE2, towerSW2, towerN2, towerS2, towerE2, towerW2; // the outer ring of towers

  // the walls
  private Wall wallNW, wallNC, wallNE, wallSW, wallSC, wallSE, // the inner ring of walls
      wallES, wallEC, wallEN, wallWS, wallWC, wallWN,
      wallNNE1, wallNNE2, wallNNE3, wallNNW1, wallNNW2, wallNNW3, // the outer ring of walls
      wallENE1, wallENE2, wallENE3, wallWNW1, wallWNW2, wallWNW3,
      wallSSE1, wallSSE2, wallSSE3, wallSSW1, wallSSW2, wallSSW3,
      wallESE1, wallESE2, wallESE3, wallWSW1, wallWSW2, wallWSW3,
      wallCN, wallCS, wallCE, wallCW; // the connecting walls

  // the cranesnests
  private Nest nestNE, nestNW, nestSE, nestSW,
      nestNE2, nestNW2, nestSE2, nestSW2, nestN2, nestS2, nestE2, nestW2;

  // the baloons
  private Baloon bal1, bal2, bal3, bal4, bal5;

  // the flag
  private Flag flag;

  // the level of the castle
  private int level = 1;

  // the health of the current level
  private int health = 0;

  // the location of the castle
  private float x_pos, z_pos;

  // the team of the castle
  private TeamInfo team;

  /**  Creates a new castle on the specified team at the specified location. */
  public Castle(float x, float z, TeamInfo team)
  {
    this.team = team;
    team.setCastle(this);
    x_pos = x;
    z_pos = z;

    // set up the tower positions
    startCastle();
  }

  /** Reurns the team of the castle. */
  public TeamInfo getTeam()
  {
    return team;
  }

  /** Returns the position of the center of the castle   */
  public Point3f getPosition()
  {
    return towerC.getPosition();
  }

  /** Returns the level of the castle.*/
  public int getLevel()
  {
    return level;
  }

  private void startCastle()
  {
    // the center tower
    towerC = new Tower(this, x_pos, z_pos);

    // the flag
    flag = new Flag(this);

    setLevel(1);
    bal1 = new Baloon(this);
  }

  public Flag getFlag()
  {
    return flag;
  }

  private void upgradeLevel_1()
  {
    // the inner ring of towers
    towerNE = new Tower(this, x_pos + 2 * U, z_pos + 2 * U);
    towerNW = new Tower(this, x_pos + 2 * U, z_pos - 2 * U);
    towerSE = new Tower(this, x_pos - 2 * U, z_pos + 2 * U);
    towerSW = new Tower(this, x_pos - 2 * U, z_pos - 2 * U);

    // the nests
    nestNE = new Nest(this, towerNE.getX(), towerNE.getZ());
    nestNW = new Nest(this, towerNW.getX(), towerNW.getZ());
    nestSE = new Nest(this, towerSE.getX(), towerSE.getZ());
    nestSW = new Nest(this, towerSW.getX(), towerSW.getZ());

    repairLevel_1();
    setLevel(2);
    bal2 = new Baloon(this);
  }

  private void upgradeLevel_2()
  {
    // the inner ring of walls
    wallNE = new Wall(this, x_pos + 2 * U, z_pos + U);
    wallNC = new Wall(this, x_pos + 2 * U, z_pos);
    wallNW = new Wall(this, x_pos + 2 * U, z_pos - U);
    wallSE = new Wall(this, x_pos - 2 * U, z_pos + U);
    wallSC = new Wall(this, x_pos - 2 * U, z_pos);
    wallSW = new Wall(this, x_pos - 2 * U, z_pos - U);
    wallEN = new Wall(this, x_pos + U, z_pos + 2 * U);
    wallEC = new Wall(this, x_pos, z_pos + 2 * U);
    wallES = new Wall(this, x_pos - U, z_pos + 2 * U);
    wallWN = new Wall(this, x_pos + U, z_pos - 2 * U);
    wallWC = new Wall(this, x_pos, z_pos - 2 * U);
    wallWS = new Wall(this, x_pos - U, z_pos - 2 * U);

    repairLevel_2();
    setLevel(3);
    bal3 = new Baloon(this);
  }

  private void upgradeLevel_3()
  {
    // the outer ring of towers
    towerN2 = new Tower(this, x_pos + 4 * U, z_pos);
    towerS2 = new Tower(this, x_pos - 4 * U, z_pos);
    towerE2 = new Tower(this, x_pos, z_pos + 4 * U);
    towerW2 = new Tower(this, x_pos, z_pos - 4 * U);
    towerNE2 = new Tower(this, x_pos + 4 * U, z_pos + 4 * U);
    towerNW2 = new Tower(this, x_pos + 4 * U, z_pos - 4 * U);
    towerSE2 = new Tower(this, x_pos - 4 * U, z_pos + 4 * U);
    towerSW2 = new Tower(this, x_pos - 4 * U, z_pos - 4 * U);

    // the crane nests
    nestNE2 = new Nest(this, towerNE2.getX(), towerNE2.getZ());
    nestNW2 = new Nest(this, towerNW2.getX(), towerNW2.getZ());
    nestSE2 = new Nest(this, towerSE2.getX(), towerSE2.getZ());
    nestSW2 = new Nest(this, towerSW2.getX(), towerSW2.getZ());
    nestN2 = new Nest(this, towerN2.getX(), towerN2.getZ());
    nestS2 = new Nest(this, towerS2.getX(), towerS2.getZ());
    nestE2 = new Nest(this, towerE2.getX(), towerE2.getZ());
    nestW2 = new Nest(this, towerW2.getX(), towerW2.getZ());

    repairLevel_3();
    setLevel(4);
    bal4 = new Baloon(this);
  }

  private void upgradeLevel_4()
  {
    // the outer ring of walls
    wallNNE1 = new Wall(this, x_pos + 4 * U, z_pos + 1 * U);
    wallNNE2 = new Wall(this, x_pos + 4 * U, z_pos + 2 * U);
    wallNNE3 = new Wall(this, x_pos + 4 * U, z_pos + 3 * U);
    wallNNW1 = new Wall(this, x_pos + 4 * U, z_pos - 1 * U);
    wallNNW2 = new Wall(this, x_pos + 4 * U, z_pos - 2 * U);
    wallNNW3 = new Wall(this, x_pos + 4 * U, z_pos - 3 * U);
    wallENE1 = new Wall(this, x_pos + 1 * U, z_pos + 4 * U);
    wallENE2 = new Wall(this, x_pos + 2 * U, z_pos + 4 * U);
    wallENE3 = new Wall(this, x_pos + 3 * U, z_pos + 4 * U);
    wallWNW1 = new Wall(this, x_pos - 1 * U, z_pos + 4 * U);
    wallWNW2 = new Wall(this, x_pos - 2 * U, z_pos + 4 * U);
    wallWNW3 = new Wall(this, x_pos - 3 * U, z_pos + 4 * U);
    wallSSE1 = new Wall(this, x_pos - 4 * U, z_pos + 1 * U);
    wallSSE2 = new Wall(this, x_pos - 4 * U, z_pos + 2 * U);
    wallSSE3 = new Wall(this, x_pos - 4 * U, z_pos + 3 * U);
    wallSSW1 = new Wall(this, x_pos - 4 * U, z_pos - 1 * U);
    wallSSW2 = new Wall(this, x_pos - 4 * U, z_pos - 2 * U);
    wallSSW3 = new Wall(this, x_pos - 4 * U, z_pos - 3 * U);
    wallESE1 = new Wall(this, x_pos + 1 * U, z_pos - 4 * U);
    wallESE2 = new Wall(this, x_pos + 2 * U, z_pos - 4 * U);
    wallESE3 = new Wall(this, x_pos + 3 * U, z_pos - 4 * U);
    wallWSW1 = new Wall(this, x_pos - 1 * U, z_pos - 4 * U);
    wallWSW2 = new Wall(this, x_pos - 2 * U, z_pos - 4 * U);
    wallWSW3 = new Wall(this, x_pos - 3 * U, z_pos - 4 * U);

    // the connecting walls
    wallCN = new Wall(this, x_pos + 3 * U, z_pos);
    wallCS = new Wall(this, x_pos - 3 * U, z_pos);
    wallCE = new Wall(this, x_pos, z_pos + 3 * U);
    wallCW = new Wall(this, x_pos, z_pos - 3 * U);

    repairLevel_4();
    setLevel(5);
    bal5 = new Baloon(this);
  }

  private void downgradeLevel_2()
  {
    // the inner ring of towers
    towerNE.destroy();
    towerNW.destroy();
    towerSE.destroy();
    towerSW.destroy();

    // the nests
    nestNE.destroy();
    nestNW.destroy();
    nestSE.destroy();
    nestSW.destroy();

    setLevel(1);
    bal2.destroy();
  }

  private void downgradeLevel_3()
  {
    // the inner ring of walls
    wallNE.destroy();
    wallNC.destroy();
    wallNW.destroy();
    wallSE.destroy();
    wallSC.destroy();
    wallSW.destroy();
    wallEN.destroy();
    wallEC.destroy();
    wallES.destroy();
    wallWN.destroy();
    wallWC.destroy();
    wallWS.destroy();

    setLevel(2);
    bal3.destroy();
  }

  private void downgradeLevel_4()
  {
    // the outer ring of towers
    towerN2.destroy();
    towerS2.destroy();
    towerE2.destroy();
    towerW2.destroy();
    towerNE2.destroy();
    towerNW2.destroy();
    towerSE2.destroy();
    towerSW2.destroy();

    // the crane nests
    nestNE2.destroy();
    nestNW2.destroy();
    nestSE2.destroy();
    nestSW2.destroy();
    nestN2.destroy();
    nestS2.destroy();
    nestE2.destroy();
    nestW2.destroy();

    setLevel(3);
    bal4.destroy();
  }

  private void downgradeLevel_5()
  {
    // the outer ring of walls
    wallNNE1.destroy();
    wallNNE2.destroy();
    wallNNE3.destroy();
    wallNNW1.destroy();
    wallNNW2.destroy();
    wallNNW3.destroy();
    wallENE1.destroy();
    wallENE2.destroy();
    wallENE3.destroy();
    wallWNW1.destroy();
    wallWNW2.destroy();
    wallWNW3.destroy();
    wallSSE1.destroy();
    wallSSE2.destroy();
    wallSSE3.destroy();
    wallSSW1.destroy();
    wallSSW2.destroy();
    wallSSW3.destroy();
    wallESE1.destroy();
    wallESE2.destroy();
    wallESE3.destroy();
    wallWSW1.destroy();
    wallWSW2.destroy();
    wallWSW3.destroy();

    // the connecting walls
    wallCN.destroy();
    wallCS.destroy();
    wallCE.destroy();
    wallCW.destroy();

    setLevel(4);
    bal5.destroy();
  }

  private void repairLevel_1()
  {
    if (!bal1.isAlive()) bal1 = new Baloon(this);
  }

  private void repairLevel_2()
  {
    repairLevel_1();

    nestNE.destroy(); nestNE = new Nest(this, towerNE.getX(), towerNE.getZ());
    nestNW.destroy(); nestNW = new Nest(this, towerNW.getX(), towerNW.getZ());
    nestSE.destroy(); nestSE = new Nest(this, towerSE.getX(), towerSE.getZ());
    nestSW.destroy(); nestSW  = new Nest(this, towerSW.getX(),  towerSW.getZ());

    if (!bal2.isAlive()) bal2 = new Baloon(this);
  }

  private void repairLevel_3()
  {
    repairLevel_2();

    if (!bal3.isAlive()) bal3 = new Baloon(this);
  }

  private void repairLevel_4()
  {
    repairLevel_3();

    nestNE2.destroy();  nestNE2 = new Nest(this, towerNE2.getX(), towerNE2.getZ());
    nestNW2.destroy();  nestNW2 = new Nest(this, towerNW2.getX(), towerNW2.getZ());
    nestSE2.destroy();  nestSE2 = new Nest(this, towerSE2.getX(), towerSE2.getZ());
    nestSW2.destroy();  nestSW2 = new Nest(this, towerSW2.getX(), towerSW2.getZ());
    nestN2.destroy();  nestN2  = new Nest(this, towerN2.getX(),  towerN2.getZ());
    nestS2.destroy();  nestS2  = new Nest(this, towerS2.getX(),  towerS2.getZ());
    nestE2.destroy();  nestE2  = new Nest(this, towerE2.getX(),  towerE2.getZ());
    nestW2.destroy();  nestW2  = new Nest(this, towerW2.getX(),  towerW2.getZ());

    if (!bal4.isAlive()) bal4 = new Baloon(this);
  }

  private void repairLevel_5()
  {
    repairLevel_4();

    if (!bal5.isAlive()) bal5 = new Baloon(this);
  }

  private void destroy()
  {
    towerC.destroy();
    team.setCastle(null);
    setLevel(0);
    bal1.destroy();
  }

  /** Sets the level of the castle.*/
  private void setLevel(int level)
  {
    this.level = level;
    team.getWizard().setLevel(level);
    team.getWizard().checkGold();
  }

  /** the Tower is a single block of the castle which is taller then the Wall. */
  public static class Tower extends Building
  {
    // the owning castle
    private Castle castle;

    // the custom geometry
    private static GeometryInfo geometry = null;

    // constants used to tune the geometry
    private static final float F = 0.9f; // the hight where the slope starts
    private static final float G = 0.5f; // the relative size of the top surface

    public float getX()
    {
      return super.x_pos;
    }

    public float getZ()
    {
      return super.z_pos;
    }

    public Tower(Castle castle, float x, float z)
    {
      super(towerapp, x,z,towerscale);
      this.castle = castle;

      // set up the static geometry
      if (geometry == null)
      {
         System.out.println("Making Castle Geometry...");

         geometry = new GeometryInfo(GeometryInfo.QUAD_ARRAY);

         Point3f[] coordinates = {
             // North face
             new Point3f(-1.0f, -1.0f, 1.0f) ,
             new Point3f(1.0f, -1.0f, 1.0f),
             new Point3f(1.0f, F, 1.0f),
             new Point3f( -1.0f, F, 1.0f),

             // South face
             new Point3f( -1.0f, -1.0f, -1.0f),
             new Point3f(1.0f, -1.0f, -1.0f),
             new Point3f(1.0f, F, -1.0f),
             new Point3f( -1.0f, F, -1.0f),

             // East face
             new Point3f(1.0f, -1.0f, -1.0f),
             new Point3f(1.0f, -1.0f, 1.0f),
             new Point3f(1.0f, F, 1.0f),
             new Point3f(1.0f, F, -1.0f),

             // West face
             new Point3f( -1.0f, -1.0f, -1.0f),
             new Point3f( -1.0f, -1.0f, 1.0f),
             new Point3f( -1.0f, F, 1.0f),
             new Point3f( -1.0f, F, -1.0f),

             // Upper North face
             new Point3f(-1.0f, F, 1.0f) ,
             new Point3f(1.0f, F, 1.0f),
             new Point3f(G, 1.0f, G),
             new Point3f( -G, 1.0f, G),

             // Upper South face
             new Point3f( -1.0f, F, -1.0f),
             new Point3f(1.0f, F, -1.0f),
             new Point3f(G, 1.0f, -G),
             new Point3f( -G, 1.0f, -G),

             // Upper East face
             new Point3f(1.0f, F, -1.0f),
             new Point3f(1.0f, F, 1.0f),
             new Point3f(G, 1.0f, G),
             new Point3f(G, 1.0f, -G),

             // Upper West face
             new Point3f( -1.0f, F, -1.0f),
             new Point3f( -1.0f, F, 1.0f),
             new Point3f( -G, 1.0f, G),
             new Point3f( -G, 1.0f, -G),

             // top face
             new Point3f( -0.5f, 1.0f, -G),
             new Point3f( -0.5f, 1.0f, G),
             new Point3f(0.5f, 1.0f, G),
             new Point3f(0.5f, 1.0f, -G),  };
         geometry.setCoordinates(coordinates);

         TexCoord2f[] texcoords = {
             // North Face
             new TexCoord2f(0.0f, 0.0f),
             new TexCoord2f(1.0f, 0.0f),
             new TexCoord2f(1.0f, 0.625f),
             new TexCoord2f(0.0f, 0.625f),

             // South face
             new TexCoord2f(0.0f, 0.0f),
             new TexCoord2f(1.0f, 0.0f),
             new TexCoord2f(1.0f, 0.625f),
             new TexCoord2f(0.0f, 0.625f),

             // East face
             new TexCoord2f(0.0f, 0.0f),
             new TexCoord2f(1.0f, 0.0f),
             new TexCoord2f(1.0f, 0.625f),
             new TexCoord2f(0.0f, 0.625f),

             // West Face
             new TexCoord2f(0.0f, 0.0f),
             new TexCoord2f(1.0f, 0.0f),
             new TexCoord2f(1.0f, 0.625f),
             new TexCoord2f(0.0f, 0.625f),

             // Upper North Face
             new TexCoord2f(0.0f, 0.625f),
             new TexCoord2f(1.0f, 0.625f),
             new TexCoord2f(0.75f, 0.75f),
             new TexCoord2f(0.25f, 0.75f),

             // Upper South face
             new TexCoord2f(0.0f, 0.625f),
             new TexCoord2f(1.0f, 0.625f),
             new TexCoord2f(0.75f, 0.75f),
             new TexCoord2f(0.25f, 0.75f),

             // Upper East face
             new TexCoord2f(0.0f, 0.625f),
             new TexCoord2f(1.0f, 0.625f),
             new TexCoord2f(0.75f, 0.75f),
             new TexCoord2f(0.25f, 0.75f),

             // Upper West Face
             new TexCoord2f(0.0f, 0.625f),
             new TexCoord2f(1.0f, 0.625f),
             new TexCoord2f(0.75f, 0.75f),
             new TexCoord2f(0.25f, 0.75f),

             // Top face
             new TexCoord2f(0.0f, 0.75f),
             new TexCoord2f(1.0f, 0.75f),
             new TexCoord2f(1.0f, 1.0f),
             new TexCoord2f(0.0f, 1.0f), };

         geometry.setTextureCoordinateParams(1, 2);
         geometry.setTextureCoordinates(0, texcoords);
       }

       // set up the custom geometry
       setGeometry(geometry.getGeometryArray());

      // add itself to the world
      GameFrame.physics.addBuilding(this);
    }
  }

  /** the Wall is a single block of the castle which is shorter then the Tower. */
  public static class Wall extends Building
  {
    // the owning castle
    private Castle castle;

    public Wall(Castle castle, float x, float z)
    {
      super(wallapp, x, z, wallscale);
      this.castle = castle;

      // add itself to the world
      GameFrame.physics.addBuilding(this);
    }
  }

  /** the flag sits over the center tower and takes damage. If it is hit by a friendly
   * castlespell, it is upgraded if the team has enough money, else it is repaired. If it
   * is damaged enough, the castle is reduced in level or destroyed.  */
  public static class Flag extends Damageable
  {
    // the owning castle
    private Castle castle;

    // the owning wizard
    private Wizard wizard;

    public Flag(Castle castle)
    {
      this.castle = castle;
      this.wizard = castle.getTeam().getWizard();
      setAppearance(castle.getTeam().flagApp);
      Point3f pos = castle.getPosition();
      setPosition(pos.x, pos.y + towerscale + 2 * flagscale, pos.z);
      setScale(flagscale);
      setColor3f(castle.getTeam().getColor3f());
      setVisibility(ALWAYS_VISIBLE);

      // add itself to the team
      setTeam(castle.getTeam());

      // add itself to the world
      GameFrame.physics.addParticle(this);
      setHealth(GameFrame.CASTLE_LEVEL1HEALTH);
    }

    /** When the flag is damaged, notify the wizard */
    public void damage(int damage, Damageable owner)
    {
      super.damage(damage, owner);
      this.getTeam().getWizard().castleDamaged();
    }

    /** when the flag  is destroyed, the castle is reduced in level and a new flag
     is created*/
    public void destroy()
    {
      if (castle.getLevel() == 1)
      {
        castle.destroy();
        super.destroy();
      }
      else if (castle.getLevel() == 2)
      {
        castle.downgradeLevel_2();
        setHealth(GameFrame.CASTLE_LEVEL1HEALTH);
      }
      else if (castle.getLevel() == 3)
      {
        castle.downgradeLevel_3();
        setHealth(GameFrame.CASTLE_LEVEL2HEALTH);
      }
      else if (castle.getLevel() == 4)
      {
        castle.downgradeLevel_4();
        setHealth(GameFrame.CASTLE_LEVEL3HEALTH);
      }
      else if (castle.getLevel() == 5)
      {
        castle.downgradeLevel_5();
        setHealth(GameFrame.CASTLE_LEVEL4HEALTH);
      }
    }

    /** When the flag collides with a castle spell, repair or upgrade it. */
    public void collided(Particle particle)
    {
      if (particle instanceof CastleSpell)
      {
        CastleSpell c = (CastleSpell)particle;
        if (c.getTeam() == castle.getTeam())
        {
          if (castle.getLevel() == 1 && wizard.getGold() >= GameFrame.CASTLE_LEVEL2COST)
          {
            castle.upgradeLevel_1();
            wizard.setGold(wizard.getGold() - GameFrame.CASTLE_LEVEL2COST);
            setHealth(GameFrame.CASTLE_LEVEL2HEALTH);
          }
          else if (castle.getLevel() == 2 && wizard.getGold() >= GameFrame.CASTLE_LEVEL3COST)
          {
            castle.upgradeLevel_2();
            wizard.setGold(wizard.getGold() - GameFrame.CASTLE_LEVEL3COST);
            setHealth(GameFrame.CASTLE_LEVEL3HEALTH);
          }
          else if (castle.getLevel() == 3 && wizard.getGold() >= GameFrame.CASTLE_LEVEL4COST)
          {
            castle.upgradeLevel_3();
            wizard.setGold(wizard.getGold() - GameFrame.CASTLE_LEVEL4COST);
            setHealth(GameFrame.CASTLE_LEVEL4HEALTH);
          }
          else if (castle.getLevel() == 4 && wizard.getGold() >= GameFrame.CASTLE_LEVEL5COST)
          {
            castle.upgradeLevel_4();
            wizard.setGold(wizard.getGold() - GameFrame.CASTLE_LEVEL5COST);
            setHealth(GameFrame.CASTLE_LEVEL5HEALTH);
          }
          else if (castle.getLevel() == 1 && wizard.getGold() >= GameFrame.CASTLE_LEVEL1COST/10)
          {
            castle.repairLevel_1();
            wizard.setGold(wizard.getGold() - GameFrame.CASTLE_LEVEL1COST/10);
            setHealth(GameFrame.CASTLE_LEVEL1HEALTH);
          }
          else if (castle.getLevel() == 2 && wizard.getGold() >= GameFrame.CASTLE_LEVEL2COST/10)
          {
            castle.repairLevel_2();
            wizard.setGold(wizard.getGold() - GameFrame.CASTLE_LEVEL2COST/10);
            setHealth(GameFrame.CASTLE_LEVEL2HEALTH);
          }
          else if (castle.getLevel() == 3 && wizard.getGold() >= GameFrame.CASTLE_LEVEL3COST/10)
          {
            castle.repairLevel_3();
            wizard.setGold(wizard.getGold() - GameFrame.CASTLE_LEVEL3COST/10);
            setHealth(GameFrame.CASTLE_LEVEL3HEALTH);
          }
          else if (castle.getLevel() == 4 && wizard.getGold() >= GameFrame.CASTLE_LEVEL4COST/10)
          {
            castle.repairLevel_4();
            wizard.setGold(wizard.getGold() - GameFrame.CASTLE_LEVEL4COST/10);
            setHealth(GameFrame.CASTLE_LEVEL4HEALTH);
          }
          else if (castle.getLevel() == 5 && wizard.getGold() >= GameFrame.CASTLE_LEVEL5COST/10)
          {
            castle.repairLevel_5();
            wizard.setGold(wizard.getGold() - GameFrame.CASTLE_LEVEL5COST/10);
            setHealth(GameFrame.CASTLE_LEVEL5HEALTH);
          }

          c.destroy();
        }
      }
    }
  }

  /** The nests produce guard cranes. */
  public static class Nest extends CraneMonster.Nest
  {
    /** Creates a new nest, which produces the specified number of cranes automatically. */
    public Nest(Castle castle, float x, float z)
    {
      setTeam(castle.getTeam());
      setHealth( (int) (10 + Math.random() * 10));

      setPosition(x, 0, z);
      setType(TYPE_ROLLING);
      setAppearance(this.getTeam().getCraneNestApp());
      setVisibility(Particle.VISIBLE);
      setColor3f(this.getTeam().getColor3f());

      for (int i = 0; i < numCranes; i++)
        newCrane();

      // add itself to the world
      GameFrame.physics.addParticle(this);
    }

    /** When a nest is damaged, notify the wizard */
    public void damage(int damage, Damageable owner)
    {
      super.damage(damage, owner);
      this.getTeam().getWizard().castleDamaged();
    }
  }
}