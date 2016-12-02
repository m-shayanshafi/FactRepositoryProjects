// Building.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import java.io.File;
import java.net.URL;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Geometry;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureUnitState;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.image.TextureLoader;

/** A building is a 3d structure that does not move or have cylidrical collision
 * detection. It is rectangular and it modifies the height of the map. Gold Mines
 * and Castle towers are Buildings. When a building is created, it rises out of the
 * ground.
 */

public class Building extends BranchGroup
{
  // the rising speed, in meters per second
  private static float speed = 5f;

  // the position of the building in game units
  protected float x_pos;
  protected float y_pos;
  protected float z_pos;

  // the location on the maptile
  protected int x_loc;
  protected int y_loc;

  // the hieght of the building
  protected float height;

  // the final y position
  private float final_y_pos;

  // a boolean indicating the existance of the wall
  private boolean alive;

  private TransformGroup transformGroup;
  private Transform3D transform = new Transform3D();
  private Vector3f translation = new Vector3f();

  private static GeometryInfo geometry;

  private Shape3D shape;

  public Building(Appearance appearance, float x, float z, float height)
  {
    alive = true;
    this.height = height;

    // create the geometry if it is not yet created
    if (geometry == null)
    {
      System.out.println("Making Building Geometry...");

      geometry = new GeometryInfo(GeometryInfo.QUAD_ARRAY);

      Point3f[] coordinates = {
        new Point3f(-1.0f, -1.0f, 1.0f) ,
        new Point3f(1.0f, -1.0f, 1.0f) ,
        new Point3f(1.0f, 1.0f, 1.0f) ,
        new Point3f(-1.0f, 1.0f, 1.0f),

        new Point3f(-1.0f, -1.0f, -1.0f) ,
        new Point3f(1.0f, -1.0f, -1.0f) ,
        new Point3f(1.0f, 1.0f, -1.0f) ,
        new Point3f(-1.0f, 1.0f, -1.0f),

        new Point3f(1.0f, -1.0f, -1.0f) ,
        new Point3f(1.0f, -1.0f, 1.0f) ,
        new Point3f(1.0f, 1.0f, 1.0f) ,
        new Point3f(1.0f, 1.0f, -1.0f),

        new Point3f(-1.0f, -1.0f, -1.0f) ,
        new Point3f(-1.0f, -1.0f, 1.0f) ,
        new Point3f(-1.0f, 1.0f, 1.0f) ,
        new Point3f(-1.0f, 1.0f, -1.0f),

        new Point3f(-1.0f, 1.0f, -1.0f) ,
        new Point3f(-1.0f, 1.0f, 1.0f) ,
        new Point3f(1.0f, 1.0f, 1.0f) ,
        new Point3f(1.0f, 1.0f, -1.0f),  };
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

    shape = new Shape3D();
    shape.setGeometry(geometry.getGeometryArray());
    shape.setAppearance(appearance);
    transform.setScale(new Vector3d(GameFrame.MAP_GRID_SCALE,this.height,GameFrame.MAP_GRID_SCALE));

    setLocation(x,z);

    transformGroup = new TransformGroup(transform);
    transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    transformGroup.addChild(shape);
    addChild(transformGroup);

    setCapability(BranchGroup.ALLOW_DETACH);

    setHeights();
  }

  /** Sets the geometry to something other then the default enlogated cube. */
  public void setGeometry(Geometry geometry)
  {
    shape.setGeometry(geometry);
  }

  /** Creates an appearance from a graphics file. */
  public static Appearance createAppearance(String filename)
  {
    if (filename==null) throw new IllegalArgumentException();

    URL fileurl = ClassLoader.getSystemResource(filename);

    Appearance appearance = new Appearance();
    TextureUnitState texUnits[] = new TextureUnitState[1];

    // load the texture
    File file = new File(filename);
    System.out.println("loading texture: " + file.getName());

    TextureLoader loader = new TextureLoader(fileurl,GameFrame.menuFrame);
    ImageComponent2D image = loader.getImage();

    if(image == null)
    {
      System.out.println("load failed for texture " + fileurl);
      System.exit(-1);
    }
    else
    {
      Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGB, image.getWidth(), image.getHeight());

      texture.setImage(0, image);
      texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
      texture.setBoundaryModeS(Texture.CLAMP);
      texture.setBoundaryModeT(Texture.CLAMP);
      appearance.setTexture(texture);
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
  /** This places the structure in alignment with the maptile. */
  private void setLocation(float x, float z)
  {
    // adjust x and z for the quadrant
    if (x<0) x += GameFrame.MAP_GRID_SCALE*(GameFrame.MAP_RESOLUTION-1);
    if (z<0) z += GameFrame.MAP_GRID_SCALE*(GameFrame.MAP_RESOLUTION-1);

    // get the x and y location (on the maptile)
    x_loc = (int)(x/GameFrame.MAP_GRID_SCALE)/2;
    y_loc = (int)(z/GameFrame.MAP_GRID_SCALE)/2;
    x_loc *=2;
    y_loc *=2;

    // find the position of the center in game units
    x_pos = (x_loc+1f)*GameFrame.MAP_GRID_SCALE;
    z_pos = (y_loc+1f)*GameFrame.MAP_GRID_SCALE;
    final_y_pos = GameFrame.renderer.getMap().getOriginalHeightAt(x_pos,z_pos);
    y_pos = final_y_pos - height;
  }

  /** Returns the distance between the center of the particle and the center
   *  of the building in the x,z plane */
  public final float distance(Particle p)
  {
    if (p == null) return Float.MAX_VALUE;

    float x = p.x_pos - this.x_pos;
    float z = p.z_pos - this.z_pos;

    float dist = (GameFrame.MAP_RESOLUTION - 1) * GameFrame.MAP_GRID_SCALE;
    if (x > dist / 2f) x = dist - x;
    if (x < -dist / 2f) x = dist + x;
    if (z > dist / 2f) z = dist - z;
    if (z < -dist / 2f) z = dist + z;

    return (float) Math.sqrt(x * x + z * z);
  }

  /** Gets the position of the building. */
  public Point3f getPosition()
  {
    return new Point3f(x_pos,final_y_pos,z_pos);
  }

  /** Sets the relevent heights around the wall */
  private void setHeights()
  {
    float x1 = (x_loc+0f)*GameFrame.MAP_GRID_SCALE;
    float x2 = (x_loc+1f)*GameFrame.MAP_GRID_SCALE;
    float x3 = (x_loc+2f)*GameFrame.MAP_GRID_SCALE;
    float y1 = (y_loc+0f)*GameFrame.MAP_GRID_SCALE;
    float y2 = (y_loc+1f)*GameFrame.MAP_GRID_SCALE;
    float y3 = (y_loc+2f)*GameFrame.MAP_GRID_SCALE;

    float h = GameFrame.renderer.getMap().getOriginalHeightAt(x2,y2) + height;

    if (GameFrame.renderer.getMap().getHeightAt(x1,y1) <= h) GameFrame.renderer.getMap().setHeightAt(x1,y1,h);
    if (GameFrame.renderer.getMap().getHeightAt(x1,y2) <= h) GameFrame.renderer.getMap().setHeightAt(x1,y2,h);
    if (GameFrame.renderer.getMap().getHeightAt(x1,y3) <= h) GameFrame.renderer.getMap().setHeightAt(x1,y3,h);
    if (GameFrame.renderer.getMap().getHeightAt(x2,y1) <= h) GameFrame.renderer.getMap().setHeightAt(x2,y1,h);
    if (GameFrame.renderer.getMap().getHeightAt(x2,y2) <= h) GameFrame.renderer.getMap().setHeightAt(x2,y2,h);
    if (GameFrame.renderer.getMap().getHeightAt(x2,y3) <= h) GameFrame.renderer.getMap().setHeightAt(x2,y3,h);
    if (GameFrame.renderer.getMap().getHeightAt(x3,y1) <= h) GameFrame.renderer.getMap().setHeightAt(x3,y1,h);
    if (GameFrame.renderer.getMap().getHeightAt(x3,y2) <= h) GameFrame.renderer.getMap().setHeightAt(x3,y2,h);
    if (GameFrame.renderer.getMap().getHeightAt(x3,y3) <= h) GameFrame.renderer.getMap().setHeightAt(x3,y3,h);
  }

  /** Returns the heights to the original values */
  public void unsetHeights()
  {
    float x1 = (x_loc+0f)*GameFrame.MAP_GRID_SCALE;
    float x2 = (x_loc+1f)*GameFrame.MAP_GRID_SCALE;
    float x3 = (x_loc+2f)*GameFrame.MAP_GRID_SCALE;
    float y1 = (y_loc+0f)*GameFrame.MAP_GRID_SCALE;
    float y2 = (y_loc+1f)*GameFrame.MAP_GRID_SCALE;
    float y3 = (y_loc+2f)*GameFrame.MAP_GRID_SCALE;

    GameFrame.renderer.getMap().setHeightAt(x1,y1,GameFrame.renderer.getMap().getOriginalHeightAt(x1,y1));
    GameFrame.renderer.getMap().setHeightAt(x1,y2,GameFrame.renderer.getMap().getOriginalHeightAt(x1,y2));
    GameFrame.renderer.getMap().setHeightAt(x1,y3,GameFrame.renderer.getMap().getOriginalHeightAt(x1,y3));
    GameFrame.renderer.getMap().setHeightAt(x2,y1,GameFrame.renderer.getMap().getOriginalHeightAt(x2,y1));
    GameFrame.renderer.getMap().setHeightAt(x2,y2,GameFrame.renderer.getMap().getOriginalHeightAt(x2,y2));
    GameFrame.renderer.getMap().setHeightAt(x2,y3,GameFrame.renderer.getMap().getOriginalHeightAt(x2,y3));
    GameFrame.renderer.getMap().setHeightAt(x3,y1,GameFrame.renderer.getMap().getOriginalHeightAt(x3,y1));
    GameFrame.renderer.getMap().setHeightAt(x3,y2,GameFrame.renderer.getMap().getOriginalHeightAt(x3,y2));
    GameFrame.renderer.getMap().setHeightAt(x3,y3,GameFrame.renderer.getMap().getOriginalHeightAt(x3,y3));
  }


  /** Buildings need to 'move' so that they are in the correct quadrant relative to the camera.
   * They also rise out of the ground when they are created. */
  public void move(long time)
  {
    // handle camera movement wrapping
    float cam_x = GameFrame.renderer.getCamera().getCamX();
    float cam_z = GameFrame.renderer.getCamera().getCamZ();
    float size = (GameFrame.MAP_RESOLUTION-1)*GameFrame.MAP_GRID_SCALE;
    if (z_pos-cam_z > size/2) z_pos -= size;
    if (z_pos-cam_z < -size/2) z_pos += size;
    if (x_pos-cam_x > size/2) x_pos -= size;
    if (x_pos-cam_x < -size/2) x_pos += size;

    // rise out of the ground
    if (y_pos < final_y_pos) y_pos += speed * time/1000f;
    if (y_pos > final_y_pos) y_pos = final_y_pos;

    translation.set(x_pos, y_pos, z_pos);
    transform.setTranslation(translation);
    transformGroup.setTransform(transform);
  }

  /** Removes the building. */
  public void destroy()
  {
    alive = false;
  }

  /** is the building still alive?*/
  public boolean isAlive()
  {
    return alive;
  }
}