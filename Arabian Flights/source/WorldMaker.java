// WorldMaker.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html

package arabian;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.media.j3d.Appearance;
import javax.vecmath.Point3f;

public class WorldMaker
{
  // the player starting location
  private static float PLAYER_START_X = 0;
  private static float PLAYER_START_Z = 0;

  // the enemy wizard starting location
  private static float WIZARD_START_X = 0;
  private static float WIZARD_START_Z = 0;

  // this is a vector of Point3f objects which define the starting locations of the wizards,
  /// including the player
  private static Vector starts = new Vector();

  private WorldMaker()
  {
  }

  /** The number of wizard start locations */
  public static int numWizards()
  {
    return starts.size();
  }

  /** returns a Point3f start location   */
  public static Point3f getStartLocation(int index)
  {
    return (Point3f)starts.get(index);
  }

  /** The .world file is a plain text file. Each line can define either an appearance
   * or a particle or a Monster. The formats as are follows:
   *
   * APPEARANCE <appname>=[<texture filename>]
   * PARTICLE <appname>|[<texture>] pos=(x,x,x) vel=(x,x,x) scale=(x) type=(ROLLING|LINEAR)
   *
   * PLAYER pos=(x,x)
   * CRANE_NEST pos=(x,x) num=(x)
   * SPIDER pos=(x,x)
   * FIRESTALK pos=(x,x)
   * GOLDMINE pos=(x,x)
   *
   * note, the pos, vel, scale, and type are all optional.
   */
  public static void makeWorld(String filename, boolean random, int numMonsters)
  {
    try
    {
      BufferedReader reader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(filename)));
      while (reader.ready())
      {
        String line = reader.readLine();

        if (line.startsWith("CMAP"))
        {
          GameFrame.URL_MAP_TEXTURE = ClassLoader.getSystemResource(line.
              substring(5));
        }

        if (line.startsWith("HMAP"))
        {
          GameFrame.URL_MAP_HEIGHTMAP = ClassLoader.getSystemResource(line.
              substring(5));
        }
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
      System.out.println("\n\nInvalid World File");
      System.exit(0);
    }

    // generate a starting location away from the edge of the map
    float scale = (GameFrame.MAP_GRID_SCALE*GameFrame.MAP_RESOLUTION - GameFrame.SIGHT_RANGE*2);
    PLAYER_START_X = (float)Math.random()*scale + GameFrame.SIGHT_RANGE;
    PLAYER_START_Z = (float)Math.random()*scale + GameFrame.SIGHT_RANGE;

    GameFrame.physics.addBuilding(new GoldMine(PLAYER_START_X, PLAYER_START_Z));

    // generate a coordinate pair outside of the player's sight
    float x,z;
    do
    {
      x = (float)Math.random()*GameFrame.MAP_GRID_SCALE*GameFrame.MAP_RESOLUTION;
      z = (float)Math.random()*GameFrame.MAP_GRID_SCALE*GameFrame.MAP_RESOLUTION;
    }
    while ((x-PLAYER_START_X)*(x-PLAYER_START_X) + (z-PLAYER_START_Z)*(z-PLAYER_START_Z)
        < GameFrame.SIGHT_RANGE*GameFrame.SIGHT_RANGE*3);

    // put the wizard there
    WIZARD_START_X = x;
    WIZARD_START_Z = z;
    GameFrame.physics.addBuilding(new GoldMine(WIZARD_START_X, WIZARD_START_Z));

    // set up the starts
    starts.clear();
    starts.add(new Point3f(PLAYER_START_X, 0, PLAYER_START_Z));
    starts.add(new Point3f(WIZARD_START_X, 0, WIZARD_START_Z));


    // generate the gold mines
    for (int i=0; i<4; i++)
    {
      // generate a coordinate pair outside of the player's and wizard's sight
      do
      {
        x = (float)Math.random()*GameFrame.MAP_GRID_SCALE*GameFrame.MAP_RESOLUTION;
        z = (float)Math.random()*GameFrame.MAP_GRID_SCALE*GameFrame.MAP_RESOLUTION;
      }
      while (((x-PLAYER_START_X)*(x-PLAYER_START_X) + (z-PLAYER_START_Z)*(z-PLAYER_START_Z)
          < GameFrame.SIGHT_RANGE*GameFrame.SIGHT_RANGE*4) ||
         ((x-WIZARD_START_X)*(x-WIZARD_START_X) + (z-WIZARD_START_Z)*(z-WIZARD_START_Z)
          < GameFrame.SIGHT_RANGE*GameFrame.SIGHT_RANGE*4));

      GameFrame.physics.addBuilding(new GoldMine(x, z));
      GameFrame.physics.addParticle(new CraneMonster.WildNest(GameFrame.monsterTeam, x, z,
          (int)(Math.random()*10)));
    }

    // generate the monsters
    for (int i=0; i<numMonsters; i++)
    {
      // generate a coordinate pair outside of the player's and wizard's sight
      do
      {
        x = (float)Math.random()*GameFrame.MAP_GRID_SCALE*GameFrame.MAP_RESOLUTION;
        z = (float)Math.random()*GameFrame.MAP_GRID_SCALE*GameFrame.MAP_RESOLUTION;
      }
      while (((x-PLAYER_START_X)*(x-PLAYER_START_X) + (z-PLAYER_START_Z)*(z-PLAYER_START_Z)
          < GameFrame.SIGHT_RANGE*GameFrame.SIGHT_RANGE*4) ||
         ((x-WIZARD_START_X)*(x-WIZARD_START_X) + (z-WIZARD_START_Z)*(z-WIZARD_START_Z)
          < GameFrame.SIGHT_RANGE*GameFrame.SIGHT_RANGE*4));


      if (Math.random()<GameFrame.RANDOM_MONSTERS_RATIO)
      {
        GameFrame.physics.addParticle(new SpiderMonster(GameFrame.monsterTeam,x, z));
      }
      else
      {
        GameFrame.physics.addParticle(new FirestalkMonster(GameFrame.monsterTeam,x, z));
      }
    }
  }

  public static void makeWorld(String filename)
  {
    // each new appearance is stored in appearances, and it's name is stored at the same index in appnames
    Vector appearances = new Vector();
    Vector appnames = new Vector();

    try
    {
      BufferedReader reader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(filename)));
      while (reader.ready())
      {
        String line = reader.readLine();

        if (line.startsWith("CMAP"))
        {
            GameFrame.URL_MAP_TEXTURE = ClassLoader.getSystemResource(line.substring(5));
        }

        if (line.startsWith("HMAP"))
        {
          GameFrame.URL_MAP_HEIGHTMAP = ClassLoader.getSystemResource(line.substring(5));
        }

        if (line.startsWith("CRANE_NEST"))
        {
          int posindex = line.indexOf("pos=(");
          int numindex = line.indexOf("num=(");

          float x = 0;
          float z = 0;
          int num = 1;

          // check for a position
          if (posindex > 0)
          {
            int par1 = line.indexOf("(", posindex);
            int com1 = line.indexOf(",", par1);
            int par2 = line.indexOf(")", com1);

            if (par1 < 0 || com1 < 0 || par2 < 0)
              throw new IOException();

            try
            {
              Float ix = new Float(line.substring(par1 + 1, com1));
              Float iz = new Float(line.substring(com1 + 1, par2));
              x = ix.floatValue();
              z = iz.floatValue();
            }
            catch (Exception e)
            {
              throw new IOException();
            }
          }

          // check for a number
          if (numindex > 0)
          {
            int par1 = line.indexOf("(", numindex);
            int par2 = line.indexOf(")", par1);

            if (par1 < 0 || par2 < 0)
              throw new IOException();
            try
            {
              Integer ix = new Integer(line.substring(par1 + 1, par2));
              num = ix.intValue();
            }
            catch (Exception e)
            {
              e.printStackTrace();
              System.exit(0);
            }
          }
          GameFrame.physics.addParticle(new CraneMonster.WildNest(GameFrame.
              monsterTeam, x, z, num));

        }
        else if (line.startsWith("FOREST"))
        {
          int posindex = line.indexOf("pos=(");
          int numindex = line.indexOf("num=(");

          float x = 0;
          float z = 0;
          int num = 1;

          // check for a position
          if (posindex > 0) {
            int par1 = line.indexOf("(", posindex);
            int com1 = line.indexOf(",", par1);
            int par2 = line.indexOf(")", com1);

            if (par1 < 0 || com1 < 0 || par2 < 0)
              throw new IOException();

            try
            {
              Float ix = new Float(line.substring(par1 + 1, com1));
              Float iz = new Float(line.substring(com1 + 1, par2));
              x = ix.floatValue();
              z = iz.floatValue();
            }
            catch (Exception e)
            {
              throw new IOException();
            }
            // check for a number
            if (numindex > 0) {
              par1 = line.indexOf("(", numindex);
              par2 = line.indexOf(")", par1);

              if (par1 < 0 || par2 < 0)
                throw new IOException();
              try
              {
                Integer ix = new Integer(line.substring(par1 + 1, par2));
                num = ix.intValue();
              }

              catch (Exception e)
              {
                e.printStackTrace();
                System.exit(0);
              }
            }

          }

          new Forest(x, z, num * 2, num);
        }

        else if (line.startsWith("GOLDMINE"))
        {
          int posindex = line.indexOf("pos=(");

          float x = 0;
          float z = 0;
          int num = 1;

          // check for a position
          if (posindex > 0) {
            int par1 = line.indexOf("(", posindex);
            int com1 = line.indexOf(",", par1);
            int par2 = line.indexOf(")", com1);

            if (par1 < 0 || com1 < 0 || par2 < 0)
              throw new IOException();
            try
            {
              Float ix = new Float(line.substring(par1 + 1, com1));
              Float iz = new Float(line.substring(com1 + 1, par2));
              x = ix.floatValue();
              z = iz.floatValue();
            }
            catch (Exception e)
            {
              throw new IOException();
            }
          }

          GameFrame.physics.addBuilding(new GoldMine(x, z));
        }
        else if (line.startsWith("PLAYER") || line.startsWith("WIZARD"))
        {
          int posindex = line.indexOf("pos=(");

          float x = 0;
          float z = 0;
          int num = 1;

          // check for a position
          if (posindex > 0) {
            int par1 = line.indexOf("(", posindex);
            int com1 = line.indexOf(",", par1);
            int par2 = line.indexOf(")", com1);

            if (par1 < 0 || com1 < 0 || par2 < 0)
              throw new IOException();
            try
            {
              Float ix = new Float(line.substring(par1 + 1, com1));
              Float iz = new Float(line.substring(com1 + 1, par2));
              x = ix.floatValue();
              z = iz.floatValue();
            }
            catch (Exception e)
            {
              throw new IOException();
            }
          }
          starts.add(new Point3f(x,0,z));
          if (starts.size() == 1)
          {
            PLAYER_START_X = x;
            PLAYER_START_Z = z;
          }
        }

        else if (line.startsWith("SPIDER"))
        {
          int posindex = line.indexOf("pos=(");

          float x = 0;
          float z = 0;
          int num = 1;

          // check for a position
          if (posindex > 0)
          {
            int par1 = line.indexOf("(", posindex);
            int com1 = line.indexOf(",", par1);
            int par2 = line.indexOf(")", com1);

            if (par1 < 0 || com1 < 0 || par2 < 0)
              throw new IOException();
            try {
              Float ix = new Float(line.substring(par1 + 1, com1));
              Float iz = new Float(line.substring(com1 + 1, par2));
              x = ix.floatValue();
              z = iz.floatValue();
            }
            catch (Exception e)
            {
              throw new IOException();
            }
          }
          GameFrame.physics.addParticle(new SpiderMonster(GameFrame.
              monsterTeam, x, z));
        }
        else if (line.startsWith("FIRESTALK"))
        {
          int posindex = line.indexOf("pos=(");

          float x = 0;
          float z = 0;
          int num = 1;

          // check for a position
          if (posindex > 0)
          {
            int par1 = line.indexOf("(", posindex);
            int com1 = line.indexOf(",", par1);
            int par2 = line.indexOf(")", com1);

            if (par1 < 0 || com1 < 0 || par2 < 0)
              throw new IOException();
            try
            {
              Float ix = new Float(line.substring(par1 + 1, com1));
              Float iz = new Float(line.substring(com1 + 1, par2));
              x = ix.floatValue();
              z = iz.floatValue();
            }
            catch (Exception e)
            {
              throw new IOException();
            }
          }
          GameFrame.physics.addParticle(new FirestalkMonster(GameFrame.
              monsterTeam, x, z));
        }
        else if (line.startsWith("APPEARANCE"))
        {
          int eq = line.indexOf("=");
          String appname = line.substring(11, eq);
          String texname;
          int brack1 = line.indexOf("[");

          if (brack1 >= 0)
          {
            int brack2 = line.indexOf("]");
            texname = line.substring(brack1 + 1, brack2);
          }
          else
            throw new IOException();
          //System.out.println("Made '" + appname + "' Appearance: " + texname);
          Appearance app = PhysicsEngine.createAppearance(texname);
          appnames.add(appname);
          appearances.add(app);
        }
        else if (line.startsWith("PARTICLE"))
        {
          Particle particle = new Particle();

          // first, find the appearance
          Appearance app;
          String texname = "";
          String appname = "";
          int brack1 = line.indexOf("[");
          if (brack1 >= 0)
          {
            int brack2 = line.indexOf("]");
            texname = line.substring(brack1 + 1, brack2);
            app = PhysicsEngine.createAppearance(texname);
          }
          else
          {
            int space1 = line.indexOf(" ");
            int space2 = line.indexOf(" ", space1 + 1);

            appname = line.substring(space1 + 1, space2);
            int index = appnames.indexOf(appname);
            app = (Appearance) appearances.get(index);
          }
          particle.setAppearance(app);

          int posindex = line.indexOf("pos=(");
          int velindex = line.indexOf("vel=(");
          int typeindex = line.indexOf("type=(");
          int scaleindex = line.indexOf("scale=(");
          int colorindex = line.indexOf("color=(");

          // check for a position
          if (posindex > 0)
          {
            int par1 = line.indexOf("(", posindex);
            int com1 = line.indexOf(",", par1);
            int com2 = line.indexOf(",", com1 + 1);
            int par2 = line.indexOf(")", com2);

            if (par1 < 0 || com1 < 0 || com2 < 0 || par2 < 0)
              throw new IOException();
            try
            {
              Float ix = new Float(line.substring(par1 + 1, com1));
              Float iy = new Float(line.substring(com1 + 1, com2));
              Float iz = new Float(line.substring(com2 + 1, par2));
              particle.setPosition(ix.floatValue(), iy.floatValue(),
                                   iz.floatValue());
            }
            catch (Exception e)
            {
              throw new IOException();
            }
          }

          // check for a velocity
          if (velindex > 0)
          {
            int par1 = line.indexOf("(", velindex);
            int com1 = line.indexOf(",", par1);
            int com2 = line.indexOf(",", com1 + 1);
            int par2 = line.indexOf(")", com2);

            if (par1 < 0 || com1 < 0 || com2 < 0 || par2 < 0)
              throw new IOException();
            try
            {
              Float ix = new Float(line.substring(par1 + 1, com1));
              Float iy = new Float(line.substring(com1 + 1, com2));
              Float iz = new Float(line.substring(com2 + 1, par2));
              particle.setVelocity(ix.floatValue(), iy.floatValue(),
                                   iz.floatValue());
            }
            catch (Exception e)
            {
              throw new IOException();
            }
          }

          // check for a scale
          if (scaleindex > 0)
          {
            int par1 = line.indexOf("(", scaleindex);
            int par2 = line.indexOf(")", par1);

            if (par1 < 0 || par2 < 0)
              throw new IOException();
            try
            {
              Float ix = new Float(line.substring(par1 + 1, par2));
              particle.setScale(ix.floatValue());
            }
            catch (Exception e)
            {
              throw new IOException();
            }
          }

          // check for a type
          if (typeindex > 0)
          {
            int par1 = line.indexOf("(", typeindex);
            int par2 = line.indexOf(")", par1);

            if (par1 < 0 || par2 < 0)
              throw new IOException();
            try
            {
              String type = line.substring(par1 + 1, par2);
              if (type.startsWith("LINEAR"))
                particle.setType(Particle.TYPE_LINEAR);
              if (type.startsWith("ROLLING"))
                particle.setType(Particle.TYPE_ROLLING);
            }
            catch (Exception e)
            {
              throw new IOException();
            }
          }

          GameFrame.physics.addParticle(particle);
        }
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
      System.out.println("\n\nInvalid World File");
      System.exit(0);
    }
  }

  /** This sets up the urls for the heightmap and landscape texture*/
  public static void setupMapURLs(String filename)
  {
    try
    {
      BufferedReader reader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(filename)));
      while (reader.ready())
      {
        String line = reader.readLine();

        // get the color map (texture)
        if (line.startsWith("CMAP"))
          GameFrame.URL_MAP_TEXTURE = ClassLoader.getSystemResource(line.substring(5));

        // get the heightmap
        if (line.startsWith("HMAP"))
          GameFrame.URL_MAP_HEIGHTMAP = ClassLoader.getSystemResource(line.substring(5));

        // get the sky type
        if (line.startsWith("SKY"))
        {
          if (line.endsWith("day"))
            GameFrame.SKY = GameFrame.SKY_DAY;

          if (line.endsWith("night"))
              GameFrame.SKY = GameFrame.SKY_NIGHT;

        }
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
      System.out.println("\n\nInvalid World File");
      System.exit(0);
    }

    System.out.println("CMap = " + GameFrame.URL_MAP_TEXTURE);
    System.out.println("HMap = " + GameFrame.URL_MAP_HEIGHTMAP);
  }
}