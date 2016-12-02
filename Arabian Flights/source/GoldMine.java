// GoldMine.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;

/** The GoldMine is a structure that has a reserviour of gold. Every second, it
 * gains a small amount. When a baloon on it's team arrives, it transfers the gold
 * that it has stored onto the baloon.
 */

public class GoldMine extends Building
{
  // what it looks like
  private static Appearance app = createAppearance(GameFrame.STRING_MINEWALL);

  // the last time it got gold
  private long lastgoldtime;

  // the time between gold spurts, in ms
  private long goldtime = 1000;

  // the amount of gold stored
  private int gold = 0;

  // its flag
  private Flag flag;

  // the custom geometry
  private static GeometryInfo geometry;

  /** Makes a new gold mine at the given position. */
  public GoldMine(float x, float z)
  {
    super(app, x, z, 4);

    flag = new Flag(this);

    // make the custom geometry if necessary.
    if (geometry == null)
    {
      System.out.println("Making Goldmine Geometry...");

      geometry = new GeometryInfo(GeometryInfo.QUAD_ARRAY);

      Point3f[] coordinates = {
          new Point3f( -1.0f, -1.0f, 1.0f),
          new Point3f(  1.0f, -1.0f, 1.0f),
          new Point3f(  0.5f,  1.0f, 0.5f),
          new Point3f( -0.5f,  1.0f, 0.5f),

          new Point3f( -1.0f, -1.0f, -1.0f),
          new Point3f(  1.0f, -1.0f, -1.0f),
          new Point3f(  0.5f,  1.0f, -0.5f),
          new Point3f( -0.5f,  1.0f, -0.5f),

          new Point3f(  1.0f, -1.0f, -1.0f),
          new Point3f(  1.0f, -1.0f,  1.0f),
          new Point3f(  0.5f,  1.0f,  0.5f),
          new Point3f(  0.5f,  1.0f, -0.5f),

          new Point3f( -1.0f, -1.0f, -1.0f),
          new Point3f( -1.0f, -1.0f,  1.0f),
          new Point3f( -0.5f,  1.0f,  0.5f),
          new Point3f( -0.5f,  1.0f, -0.5f),

          new Point3f( -0.5f, 1.0f, -0.5f),
          new Point3f( -0.5f, 1.0f,  0.5f),
          new Point3f(  0.5f, 1.0f,  0.5f),
          new Point3f(  0.5f, 1.0f, -0.5f), };
      geometry.setCoordinates(coordinates);

      TexCoord2f[] texcoords = {
          // the side texture
          new TexCoord2f(0.0f, 0.0f),
          new TexCoord2f(1.0f, 0.0f),
          new TexCoord2f(1.0f, 0.75f),
          new TexCoord2f(0.0f, 0.75f),

          new TexCoord2f(0.0f, 0.0f),
          new TexCoord2f(1.0f, 0.0f),
          new TexCoord2f(1.0f, 0.75f),
          new TexCoord2f(0.0f, 0.75f),

          new TexCoord2f(0.0f, 0.0f),
          new TexCoord2f(1.0f, 0.0f),
          new TexCoord2f(1.0f, 0.75f),
          new TexCoord2f(0.0f, 0.75f),

          new TexCoord2f(0.0f, 0.0f),
          new TexCoord2f(1.0f, 0.0f),
          new TexCoord2f(1.0f, 0.75f),
          new TexCoord2f(0.0f, 0.75f),

          // the top texture
          new TexCoord2f(0.0f, 0.75f),
          new TexCoord2f(1.0f, 0.75f),
          new TexCoord2f(1.0f, 1.0f),
          new TexCoord2f(0.0f, 1.0f), };

      geometry.setTextureCoordinateParams(1, 2);
      geometry.setTextureCoordinates(0, texcoords);
    }

    setGeometry(geometry.getGeometryArray());
  }

  /** returns the flag of the gold mine (a particle)   */
  public Flag getFlag()
  {
    return flag;
  }

  /** every frame it checks to see if it should gain gold. */
  public void move(long time)
  {
    super.move(time);

    if (lastgoldtime + goldtime < GameFrame.CURRENT_TIME)
    {
      lastgoldtime = GameFrame.CURRENT_TIME;

      // it only generates gold if it is on a team
      if (flag.getTeam() != null) gold += GameFrame.GOLDMINE_GAINS;
    }
  }

  /** This is called by the baloon when it arrives to get the gold.*/
  public int consumeGold()
  {
    int amount = gold;
    gold = 0;
    return amount;
  }

  /** The flag is the part of the mine that actions are performed on.
   * It starts invisible, and in this neutral state it ignores damage. When
   * a claim spell hits it, the it is added to the team of the caster. At this point
   * the flag gets the team's flag's appearance and baloons will come to it to get gold.
   * It can be damaged, and if it is destroyed, it returns to the neutral state. */
  public static class Flag extends Damageable
  {
    private static float neutralscale = 5f;
    private static float teamscale = 1f;
    private static Color3f neutralColor = new Color3f(1, 1, 0);

    private GoldMine mine;

    public Flag(GoldMine mine)
    {
      this.mine = mine;

      // add itself to the world
      GameFrame.physics.addParticle(this);
      setVisibility(VISIBLE);

      // place it
      Point3f pos = mine.getPosition();
      setPosition(pos.x, pos.y, pos.z);
      setAltitude(teamscale);

      // go to the neutral state
      destroy();
    }

    public int consumeGold()
    {
      return mine.consumeGold();
    }

    /** If it collides with a Claim spell while in the neutral spell, it is claimed.*/
    public void collided(Particle p)
    {
      if (getTeam() == null && p instanceof ClaimSpell)
      {
        ClaimSpell c = (ClaimSpell)p;
        setTeam(c.getTeam());
        setAppearance(c.getTeam().getFlagApp());
        setHealth(GameFrame.GOLDMINE_HEALTH);
        setScale(teamscale);
        p.destroy();
      }
    }

    /** it ignores damage when it is neutral.   */
    public void damage(int health, Damageable source)
    {
      if (getTeam() != null) super.damage(health, source);
    }

    /** if it is destroyed, it returns to the neutral state. */
    public void destroy()
    {
      setTeam(null);
      setAppearance(null);
      setScale(neutralscale);
      setColor3f(neutralColor);
    }
  }
}