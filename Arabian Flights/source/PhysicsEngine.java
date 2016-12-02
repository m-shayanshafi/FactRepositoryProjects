// PhysicsEngine
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import com.sun.j3d.utils.image.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.net.URL;
import java.util.Vector;


/** The Physics Engine contains all of the particles. It moves them and checks them for
 * collisions at each frame. It also handles reading in the particles from
 * a .world file.
 */
public class PhysicsEngine
{
  private BranchGroup branchGroup;
  private BranchGroup particleGroup;
  private BranchGroup buildingGroup;

  // when frozen is true, particles don't move.
  private boolean frozen = true;

  // each element is a vector of particles in that grid
  private Vector[][] collisionGrid = new Vector[GameFrame.COLLISION_GRID_RESOLUTION][GameFrame.COLLISION_GRID_RESOLUTION];

  private CheckThread checkThread = new CheckThread();

  public PhysicsEngine()
  {
    particleGroup = new BranchGroup();
    particleGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
    particleGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
    particleGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);

    buildingGroup = new BranchGroup();
    buildingGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
    buildingGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
    buildingGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);

    branchGroup = new BranchGroup();
    branchGroup.addChild(buildingGroup);
    branchGroup.addChild(particleGroup);

    // set up the collision grid
    for (int i=0; i<GameFrame.COLLISION_GRID_RESOLUTION; i++)
      for (int j=0; j<GameFrame.COLLISION_GRID_RESOLUTION; j++)
        collisionGrid[i][j] = new Vector();
  }

  /** Returns a Vector of particles that are in the same collision grid cell. */
  public Vector getCollisionVector(float x, float z)
  {
    // adjust X and Y for the quadrant
    if (x<0) x += GameFrame.COLLISION_GRID_SCALE*(GameFrame.COLLISION_GRID_RESOLUTION);
    if (z<0) z += GameFrame.COLLISION_GRID_SCALE*(GameFrame.COLLISION_GRID_RESOLUTION);

    // get the height of the four corners of the grid the camera is in
    try
    {
      return collisionGrid[(int)(x/GameFrame.COLLISION_GRID_SCALE)][(int)(z/GameFrame.COLLISION_GRID_SCALE)];
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      System.out.println("Collision index out of bounds:"+
         (int)(x/GameFrame.COLLISION_GRID_SCALE)+","+(int)(z/GameFrame.COLLISION_GRID_SCALE));
      return null;
    }
  }

  /** Returns the branchgroup containing all of the particles. */
  public BranchGroup getBranchGroup()
  {
    return branchGroup;
  }

  /** Add a particle to the world. */
  public synchronized void addParticle(Particle particle)
  {
    particleGroup.addChild(particle);
  }

  /** Add a building to the world. */
  public synchronized void addBuilding(Building building)
  {
    buildingGroup.addChild(building);
  }

  /** Remove a particle from the world. This returns true if the particle
   * was in the collision grid, false if not. Particles should be removed by destroy()ing them. */
  protected boolean removeParticle(Particle particle)
  {
    particleGroup.removeChild(particle);
    if (particle.getGridVector() != null)
    {
      particle.getGridVector().remove(particle);
      return true;
    }
    return false;
  }

  /** Remove a building from the world. Buildings should be removed by destroy()ing them. */
  protected void removeBuilding(Building building)
  {
    buildingGroup.removeChild(building);
    building.unsetHeights();
  }

  /** Add a particle to the world with a specified texture and position. */
  public Particle addParticle(String texname, float x, float y, float z)
  {
    Particle particle = new Particle();
    particle.setPosition(x, y, z);
    particle.setAppearance(createAppearance(texname));
    addParticle(particle);
    return particle;
  }

  /** Get one of the particles in the world. */
  public Particle getParticle(int index)
  {
    return (Particle)particleGroup.getChild(index);
  }

  /** Get one of the buildings in the world. */
  public Building getBuilding(int index)
  {
    return (Building)buildingGroup.getChild(index);
  }

  /** This starts the particles moving. */
  public void unFreeze()
  {
    GameFrame.CURRENT_TIME = System.currentTimeMillis();
    frozen  = false;

    checkThread.start();
  }

  /** This stops the particles moving. */
  public void freeze()
  {
    frozen = true;

    checkThread.interrupt();
  }

  /** Returns true if the physics are frozen. */
  public boolean isFrozen()
  {
    return frozen;
  }

  /** This method is used to find the colors of all the particles near the camera.   */
  public void getDotColors(float[] colors, float range)
  {
    int index = 6;

    // set up the two constant dot colors
    colors[0] = 1;
    colors[1] = 1;
    colors[2] = 1;

    colors[3] = 0;
    colors[4] = 0;
    colors[5] = 1;

    for (int i=0; i<numParticles(); i++)
    {
      Particle part = getParticle(i);
      if (index < colors.length && GameFrame.renderer.getCamera().getParticle() != null)
        if (!(part instanceof Player))
        {
          if (GameFrame.renderer.getCamera().getParticle().distance(part) < range)
          {
            if (part.getVisibility() != Particle.INVISIBLE)
            {
              Color3f color = part.getColor3f();
              colors[index] = color.get().getRed()/256f;
              colors[index+1] = color.get().getGreen()/256f;
              colors[index+2] = color.get().getBlue()/256f;
              index += 3;
            }
          }
          else if (part.getVisibility() == Particle.ALWAYS_VISIBLE)
          {
            Color3f color = part.getColor3f();
            colors[index] = color.get().getRed()/256f;
            colors[index+1] = color.get().getGreen()/256f;
            colors[index+2] = color.get().getBlue()/256f;
            index += 3;
          }
        }
    }

    // make all the unused dots white
    while (index < colors.length)
    {
      colors[index] = 1;
      index++;
    }
  }

  /** This method is used to find the coordinates of all the particles near the camera.   */
  public float[] getDotCoords(float[] coords, float range)
  {
    float camx = GameFrame.renderer.getCamera().getCamX();
    float camz = GameFrame.renderer.getCamera().getCamZ();
    float size = GameFrame.HUD_MINIMAP_RANGE;

    int index = 6;

    // set up the center white dot and the north blue dot
    coords[0] = 0;
    coords[1] = 0;
    coords[2] = 0;

    coords[3] = 0;
    coords[4] = 1;
    coords[5] = 0;

    for (int i=0; i<numParticles(); i++)
    {
      Particle part = getParticle(i);
      if (index < coords.length && GameFrame.renderer.getCamera().getParticle() != null)
        if (!(part instanceof Player))
        {
          if (GameFrame.renderer.getCamera().getParticle().distance(part) < range)
          {
            if (part.getVisibility() != Particle.INVISIBLE)
            {
              Point3f pos = part.getPosition();
              coords[index] = -(camx-pos.x)/range;
              coords[index+1] = (camz-pos.z)/range;
              coords[index+2] = 0;
              index += 3;
            }
          }
          else if (part.getVisibility() == Particle.ALWAYS_VISIBLE)
          {
            float trueRange = GameFrame.renderer.getCamera().getParticle().distance(part);
            Point3f pos = part.getPosition();
            coords[index] = -(camx-pos.x)/trueRange;
            coords[index+1] = (camz-pos.z)/trueRange;
            coords[index+2] = 0;
            index += 3;
          }
        }
    }

    // make all the unused dots at the origin
    while (index < coords.length)
    {
      coords[index] = 0;
      index++;
    }

    return coords;
  }

  /** Returns the number of particles in the world. */
  public int numParticles()
  {
    return particleGroup.numChildren();
  }

  /** Returns the number of particles in the world. */
  public int numBuildings()
  {
    return buildingGroup.numChildren();
  }

  /** Called every frame, this moves all of the particles and buildings.
   *  It also clears out dead (.destroy()ed) particles.
   */
  public void move(long time)
  {
    moveParticles(time);
    moveBuildings(time);
  }

  /** Moves all the buildings. */
  protected void moveBuildings(long time)
  {
    if (frozen) return;

    // move the buildings
    for (int i=0; i<numBuildings(); i++)
    {
      Building b = getBuilding(i);
      if (b.isAlive()) b.move(time);
      else removeBuilding(b);
    }
  }

  /** Moves all the Particles. */
  protected void moveParticles(long time)
  {
    if (frozen) return;

    // move the particles
    for (int i=0; i<numParticles(); i++)
    {
      Particle p = getParticle(i);
      if (p.isAlive()) p.move(time);
      else removeParticle(p);
    }
  }

  /** Checks all particles for collisions and expirations. */
  protected void checkParticles()
  {
    if (frozen) return;

    // check() all the particles (sets up the collision grid and checks for expiration)
    for (int i=0; i<numParticles(); i++)
      getParticle(i).check();

    // --- check for collisions --- //

    // for each grid position
    for (int i=0; i<GameFrame.COLLISION_GRID_RESOLUTION; i++)
      for (int j=0; j<GameFrame.COLLISION_GRID_RESOLUTION; j++)
      {
        int i2 = i+1;
        int j2 = j+1;
        if (i2==GameFrame.COLLISION_GRID_RESOLUTION) i2 =0; // wrap i
        if (j2==GameFrame.COLLISION_GRID_RESOLUTION) j2 =0; // wrap j

        // if there is more then one particle in the four adjacent cells
        if (collisionGrid[i][j].size()+collisionGrid[i2][j].size()
            +collisionGrid[i][j2].size()+collisionGrid[i2][j2].size() > 1)
        {
          // compare each particle in the home grid to all
          // other particles in all four grids
          Vector vec11 = collisionGrid[i][j];
          Vector vec12 = collisionGrid[i][j2];
          Vector vec21 = collisionGrid[i2][j];
          Vector vec22 = collisionGrid[i2][j2];

          for (int k=0; k<collisionGrid[i][j].size(); k++)
          {
            Particle part = (Particle)vec11.elementAt(k);
            for (int l=k+1; l<vec11.size(); l++) collisionCheck(part,(Particle)vec11.elementAt(l));
            for (int l=0; l<vec12.size(); l++)   collisionCheck(part,(Particle)vec12.elementAt(l));
            for (int l=0; l<vec21.size(); l++)   collisionCheck(part,(Particle)vec21.elementAt(l));
            for (int l=0; l<vec22.size(); l++)   collisionCheck(part,(Particle)vec22.elementAt(l));
          }
        }
      }

    // update the minimap to reflect the new particle positions
    GameFrame.renderer.getHud().updateMinimap();
  }

  protected void collisionCheck(Particle part1, Particle part2)
  {
    // if the horizontal (x,z) distance is close enough and the vertical (y)
    // difference is not too great, notify both particles of the collision.
    if (Math.abs(part1.getPosition().y - part2.getPosition().y)
        < (part1.getScale() + part2.getScale())
        && part1.distance(part2) < 0 )
    {
      Profiler.setPeriodThreadBlock(Profiler.BLOCK_COLLIDED);

      part1.collided(part2);
      part2.collided(part1);

      Profiler.setPeriodThreadBlock(Profiler.BLOCK_CHECK);
    }
  }

  /** Creates an appearance based on a filename. */
  public static Appearance createAppearance(String filename)
  {
    return createAppearance(filename, 255, null, null);
  }

  /** Creates an appearance based on a filename.
   * A tint may be applied, which alters the colors of the sprite.
   * If the tint is null, no tint will be applied*/
  public static Appearance createAppearance(String filename, Color tint)
  {
    return createAppearance(filename, 255, tint, null);
  }

  /** Creates an appearance based n a filename.
   * A transparency may be specified, as well as a tint.
   * if the tint is null, no tint will be applied.
   */
  public static Appearance createAppearance(String filename, int transparency, Color tint)
  {
    return createAppearance(filename, 255, tint, null);
  }

  /** Creates an appearance based on a filename.
   * A transparency may be specified, as well as a tint.
   * if the tint is null, no tint will be applied.
   * In addition, a transparency mask may be specified.*/
  public static Appearance createAppearance(String filename, int transparency, Color tint, String maskfilename)
  {
    if (filename==null) throw new IllegalArgumentException();

    URL fileurl = ClassLoader.getSystemResource(filename);
    URL maskurl = null;

    if (maskfilename != null) maskurl = ClassLoader.getSystemResource(maskfilename);

    Appearance appearance = new Appearance();
    TextureUnitState texUnits[] = new TextureUnitState[1];

    // load the texture
    File file = new File(filename);
    if (tint == null) GameFrame.loadingMessage("   Loading texture:   " + file.getName());
    else GameFrame.loadingMessage("   Loading texture:   " + file.getName() + "       Trgb(" + tint.getRGB() + ")");
    System.out.println("loading texture: " + file.getName());

    TextureLoader loader = new TextureLoader(fileurl,GameFrame.menuFrame);
    ImageComponent2D image = loader.getImage();

    BufferedImage bim = image.getImage();
    WritableRaster wr = bim.getRaster();

    int[] pix = new int[4];
    int[] mask = new int[4];

    // load the mask
    File maskfile = null;
    TextureLoader maskloader = null;
    ImageComponent2D maskimage = null;
    BufferedImage mbim = null;
    WritableRaster mwr = null;
    if (maskfilename != null)
    {
      maskfile = new File(maskfilename);
      maskloader = new TextureLoader(maskurl, GameFrame.menuFrame);
      maskimage = maskloader.getImage();

      mbim = maskimage.getImage();
      mwr = mbim.getRaster();
    }

    for (int i=0; i<wr.getHeight(); i++)
      for (int j=0; j<wr.getWidth(); j++)
      {
        wr.getPixel(i,j,pix);
        if (mwr != null) mwr.getPixel(i,j,mask);

        if (pix[0] == 0 // pure blue pixels are changed to pure transparent pixels
         && pix[1] == 0
         && pix[2] == 255)
        {
          pix[2] = 0;
          pix[3] = 0;
        }
        else // all other pixels have the transparency and tint applied
        {
          if (tint != null)
          {
            pix[0] *= tint.getRed() / 255f;
            pix[1] *= tint.getGreen() / 255f;
            pix[2] *= tint.getBlue() / 255f;
          }
          if (mwr==null) pix[3] = transparency;
          else pix[3] = (int)((mask[1]/255f)*transparency); // apply the transparency mask
        }
        wr.setPixel(i,j,pix);
      }

    bim.setData(wr);
    image.set(bim);

    if(image == null)
    {
      System.out.println("load failed for texture " + fileurl);
      System.exit(-1);
    }
    else
    {
      Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());

      TransparencyAttributes tranattrib = new TransparencyAttributes();
      tranattrib.setTransparencyMode(TransparencyAttributes.NICEST);
      tranattrib.setTransparency(1f);

      texture.setImage(0, image);
      texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
      texture.setBoundaryModeS(Texture.CLAMP);
      texture.setBoundaryModeT(Texture.CLAMP);
      appearance.setTexture(texture);
      appearance.setTransparencyAttributes(tranattrib);
    }

    // set up the material
    Material material = new Material();
    appearance.setMaterial(material);

    // set up back culling
    PolygonAttributes pa = new PolygonAttributes();
    pa.setCullFace(PolygonAttributes.CULL_NONE);
    appearance.setPolygonAttributes(pa);

    return appearance;
  }

  // This method checks for victory
  protected void victoryCheck()
  {
    int enemies = GameFrame.playerTeam.enemies();

    // victory is acheived when no enemy combatants remain.
    if (enemies == 0)
    {
      GameFrame.victory();
      GameFrame.renderer.getHud().hideRemainder();
    }
    else
    {
      if (enemies <= GameFrame.REMAINDER_THRESHOLD)
      {
        GameFrame.renderer.getHud().setRemainder(enemies);
      }
      else
      {
        GameFrame.renderer.getHud().hideRemainder();
      }
    }
  }

  // A seperate thread for running particle checks periodically
  private class CheckThread extends Thread
  {
    public void run()
    {
      long time;
      boolean run = true;
      while (run)
      {
        time = System.currentTimeMillis();

        Profiler.setPeriodThreadBlock(Profiler.BLOCK_CHECK);

        checkParticles();

        victoryCheck();

        try
        {
          Profiler.setPeriodThreadBlock(Profiler.BLOCK_PSLEEP);

          sleep(GameFrame.CHECK_PERIOD - (time - System.currentTimeMillis()));

          Profiler.setPeriodThreadBlock(Profiler.BLOCK_UNKNOWN);
        }
        catch (InterruptedException e)
        {
          run = false;
        }
      }
    }
  }
}