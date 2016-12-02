// MapTile.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import java.awt.*;
import java.awt.image.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.net.URL;

/** MapTile.
 * This class is used for building a Shape3D of the landscape. It builds this
 * from two images, one heightmap and one texturemap. It is also used for querying
 * the height of the landscape at a particular point.
 */
public class MapTile
{
  // used to build the trianglestrip and to check heights
  private int[][] heightmap = new int[GameFrame.MAP_RESOLUTION][GameFrame.MAP_RESOLUTION];

  // this is used when walls are removed
  private int[][] originalheightmap = new int[GameFrame.MAP_RESOLUTION][GameFrame.MAP_RESOLUTION];

  // contains the texture and color
  private Appearance appearance;

  // contains the geometry
  private GeometryInfo geometry;

  // needed for the textureloader
  private Component component;

  // the name of the texture
  private URL textureFilename;

  // the name of the greyscale heightmap
  private URL heightmapFilename;

  // used to define the geometry and appearance
  Point3f[] coords = new Point3f[(GameFrame.MAP_RESOLUTION-1)*GameFrame.MAP_RESOLUTION*2]; // geometric coordinates
  TexCoord2f[] texcoords = new TexCoord2f[(GameFrame.MAP_RESOLUTION-1)*GameFrame.MAP_RESOLUTION*2]; // main texture

  /** Creates the geometric and texture information needed for the Shape3D. */
  public MapTile(URL heightmapFile, URL textureFile)
  {
    GameFrame.loadingMessage("   Loading Map...");
    this.component = GameFrame.renderer;
    this.heightmapFilename = heightmapFile;
    this.textureFilename = textureFile;
    createAppearance(textureFilename);
    createGeometry(heightmapFilename);
  }

  /** Gets the height of the ground at a given point. */
  public float getHeightAt(float x, float y)
  {
    // adjust X and Y for the quadrant
    if (x<0) x += GameFrame.MAP_GRID_SCALE*(GameFrame.MAP_RESOLUTION-1);
    if (y<0) y += GameFrame.MAP_GRID_SCALE*(GameFrame.MAP_RESOLUTION-1);
    try
    {
      // get the height of the four corners of the grid the camera is in
      float h00 = heightmap[(int)(x/GameFrame.MAP_GRID_SCALE)][(int)(y/GameFrame.MAP_GRID_SCALE)];
      float h01 = heightmap[(int)(x/GameFrame.MAP_GRID_SCALE)][(int)(y/GameFrame.MAP_GRID_SCALE)+1];
      float h10 = heightmap[(int)(x/GameFrame.MAP_GRID_SCALE)+1][(int)(y/GameFrame.MAP_GRID_SCALE)];
      float h11 = heightmap[(int)(x/GameFrame.MAP_GRID_SCALE)+1][(int)(y/GameFrame.MAP_GRID_SCALE)+1];

      // determine the relative loaction in the grid (between 0 and 1)
      float modx = (x%GameFrame.MAP_GRID_SCALE)/GameFrame.MAP_GRID_SCALE;
      float mody = (y%GameFrame.MAP_GRID_SCALE)/GameFrame.MAP_GRID_SCALE;

      // calculate a weighted average for the height
      float avg_height =    h00*(1-modx)*(1-mody)
                       + h01*(1-modx)*(mody)
                       + h10*(modx)*(1-mody)
                       + h11*(modx)*(mody);

      // scale it and return it
      return avg_height*GameFrame.HEIGHT_SCALE;
    }
    catch (Exception e)
    {
      return 0;
    }
  }

  /** Gets the original height of the ground at a given point. */
   public float getOriginalHeightAt(float x, float y)
   {
     // adjust X and Y for the quadrant
     if (x<0) x += GameFrame.MAP_GRID_SCALE*(GameFrame.MAP_RESOLUTION-1);
     if (y<0) y += GameFrame.MAP_GRID_SCALE*(GameFrame.MAP_RESOLUTION-1);
     try
     {
       // get the height of the four corners of the grid the camera is in
       float h00 = originalheightmap[(int)(x/GameFrame.MAP_GRID_SCALE)][(int)(y/GameFrame.MAP_GRID_SCALE)];
       float h01 = originalheightmap[(int)(x/GameFrame.MAP_GRID_SCALE)][(int)(y/GameFrame.MAP_GRID_SCALE)+1];
       float h10 = originalheightmap[(int)(x/GameFrame.MAP_GRID_SCALE)+1][(int)(y/GameFrame.MAP_GRID_SCALE)];
       float h11 = originalheightmap[(int)(x/GameFrame.MAP_GRID_SCALE)+1][(int)(y/GameFrame.MAP_GRID_SCALE)+1];

       // determine the relative loaction in the grid (between 0 and 1)
       float modx = (x%GameFrame.MAP_GRID_SCALE)/GameFrame.MAP_GRID_SCALE;
       float mody = (y%GameFrame.MAP_GRID_SCALE)/GameFrame.MAP_GRID_SCALE;

       // calculate a weighted average for the height
       float avg_height =    h00*(1-modx)*(1-mody)
                        + h01*(1-modx)*(mody)
                        + h10*(modx)*(1-mody)
                        + h11*(modx)*(mody);

       // scale it and return it
       return avg_height*GameFrame.HEIGHT_SCALE;
     }
     catch (Exception e)
     {
       return 0;
     }
  }

  /** sets the height of the ground at a given point. */
   public void setHeightAt(float x, float y, float height)
   {
     // adjust X and Y for the quadrant
     if (x<0) x += GameFrame.MAP_GRID_SCALE*(GameFrame.MAP_RESOLUTION-1);
     if (y<0) y += GameFrame.MAP_GRID_SCALE*(GameFrame.MAP_RESOLUTION-1);
     if (x>GameFrame.MAP_GRID_SCALE*(GameFrame.MAP_RESOLUTION-1)) x -= GameFrame.MAP_GRID_SCALE*(GameFrame.MAP_RESOLUTION-1);
     if (y>GameFrame.MAP_GRID_SCALE*(GameFrame.MAP_RESOLUTION-1)) y -= GameFrame.MAP_GRID_SCALE*(GameFrame.MAP_RESOLUTION-1);

     // set the height of the nearest (rounded down) point
     heightmap[(int)(x/GameFrame.MAP_GRID_SCALE)][(int)(y/GameFrame.MAP_GRID_SCALE)] = (int)(height/GameFrame.HEIGHT_SCALE);
  }

  private void createGeometry(URL filename)
  {
    System.out.println("loading height: " + filename);
    Image img = Toolkit.getDefaultToolkit().createImage(filename);

    // use a PixelGrabber to convert the image into a int[][]
    int[] pixels = new int[(GameFrame.MAP_RESOLUTION-1) * (GameFrame.MAP_RESOLUTION-1)];
    PixelGrabber pg = new PixelGrabber(img, 0,0, (GameFrame.MAP_RESOLUTION-1), (GameFrame.MAP_RESOLUTION-1), pixels, 0, (GameFrame.MAP_RESOLUTION-1));
    try
    {
      pg.grabPixels();
    }
    catch (InterruptedException e)
    {
      System.err.println("interrupted waiting for pixels!");
      return;
    }
    if ((pg.getStatus() & ImageObserver.ABORT) != 0)
    {
      System.err.println("image fetch aborted or errored");
      return;
    }

    // Create the heightmap
    for (int i=0;i<GameFrame.MAP_RESOLUTION-1; i++)
    {
      for (int j=0;j<GameFrame.MAP_RESOLUTION-1; j++)
      {
        heightmap[i][j] = ((pixels[j*(GameFrame.MAP_RESOLUTION-1) + i]/(256*256)) + 256);
      }
    }

    // fill in the overlapping row
    for (int j=0;j<GameFrame.MAP_RESOLUTION-1; j++)
      heightmap[GameFrame.MAP_RESOLUTION-1][j] = heightmap[0][j];

    // fill in the overlapping column
    for (int i=0;i<GameFrame.MAP_RESOLUTION-1; i++)
      heightmap[i][GameFrame.MAP_RESOLUTION-1] = heightmap[i][0];

    // fill in the overlapping pixel
    heightmap[GameFrame.MAP_RESOLUTION-1][GameFrame.MAP_RESOLUTION-1] = heightmap[0][0];

    // copy the heightmap
    for (int i=0; i<heightmap.length; i++)
      for (int j=0; j<heightmap[0].length; j++)
        originalheightmap[i][j] = heightmap[i][j];

    // free the memory associated with building the heightmap
    img = null;
    pixels = null;
    System.gc(); System.gc(); System.gc();

    // create the triangle strip:
    System.out.print("stripping");

    for (int i=0;i<GameFrame.MAP_RESOLUTION-1; i+=1)
    {
      for (int j=0;j<GameFrame.MAP_RESOLUTION; j++)
      {
        coords[i*GameFrame.MAP_RESOLUTION*2+j+j] = new Point3f(i*GameFrame.MAP_GRID_SCALE, (heightmap[i][j])*GameFrame.HEIGHT_SCALE, j*GameFrame.MAP_GRID_SCALE);
        coords[i*GameFrame.MAP_RESOLUTION*2+j+j+1] = new Point3f((i+1)*GameFrame.MAP_GRID_SCALE, (heightmap[i+1][j])*GameFrame.HEIGHT_SCALE, j*GameFrame.MAP_GRID_SCALE);

        texcoords[i*GameFrame.MAP_RESOLUTION*2+j+j] = new TexCoord2f((float)i/(float)(GameFrame.MAP_RESOLUTION-1), (float)(GameFrame.MAP_RESOLUTION-1-j)/(float)(GameFrame.MAP_RESOLUTION-1));
        texcoords[i*GameFrame.MAP_RESOLUTION*2+j+j+1] = new TexCoord2f(((float)i+1.0f)/(float)(GameFrame.MAP_RESOLUTION-1), (float)(GameFrame.MAP_RESOLUTION-1-j)/(float)(GameFrame.MAP_RESOLUTION-1));
      }
      if (i%(GameFrame.MAP_RESOLUTION/10) == 0) System.out.print(".");
    }
    System.out.println("done");

    // set up the strip length information
    int[] stripVertexCounts = new int[GameFrame.MAP_RESOLUTION-1];
    for (int i = 0; i<stripVertexCounts.length; i++) stripVertexCounts[i]=GameFrame.MAP_RESOLUTION*2;

    // set up geometry info
    geometry = new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
    geometry.setStripCounts(stripVertexCounts);
    geometry.setCoordinates(coords);
    geometry.setTextureCoordinateParams(1, 2);
    geometry.setTextureCoordinates(0, texcoords);
  }

  /** Gets the Geometry of the Landscape. */
  public Geometry getGeometry()
  {
    return geometry.getGeometryArray();
  }

  /** Gets the Appearance of the landscape. */
  public Appearance getAppearance()
  {
    return appearance;
  }

  /** Gets the size of the landscape. */
  public float getSize()
  {
    return GameFrame.MAP_RESOLUTION*GameFrame.MAP_GRID_SCALE;
  }

  /** Returns a Shape3D object for the landscape. */
  public Shape3D getShape3D()
  {
    return new Shape3D(getGeometry(), getAppearance());
  }

  private void createAppearance(URL filename)
  {
    appearance = new Appearance();
    TextureUnitState texUnits[] = new TextureUnitState[1];

    // load the texture
    System.out.print("loading texture:" + filename);
    TextureLoader loader = new TextureLoader(filename, component);
    ImageComponent2D image = loader.getImage();
    if(image == null)
    {
      System.out.println("load failed for texture " + filename);
      System.exit(-1);
    }
    else
    {
      System.out.println("..");
      Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGB, image.getWidth(), image.getHeight());
      texture.setImage(0, image);
      texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
      texture.setBoundaryModeS(Texture.WRAP);
      texture.setBoundaryModeT(Texture.WRAP);
      appearance.setTexture(texture);
    }

    // set up the material
    Material material = new Material();
    appearance.setMaterial(material);

    // set up back culling
    PolygonAttributes pa = new PolygonAttributes();
    pa.setCullFace(PolygonAttributes.CULL_FRONT);
    appearance.setPolygonAttributes(pa);
  }
}

