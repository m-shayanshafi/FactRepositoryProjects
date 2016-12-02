// Particle.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import java.awt.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;
import java.util.Vector;

/** The particle class is a collection of information about an entity in the
 * game world. It is extended to produce monsters and spell effects.
 */
public class Particle extends BranchGroup
{
  private float speed_limit = 50;

  protected float x_pos;
  protected float y_pos;
  protected float z_pos;
  protected float x_vel;
  protected float y_vel;
  protected float z_vel;

  protected float prev_x_pos;
  protected float prev_y_pos;
  protected float prev_z_pos;
  protected float prev_x_vel;
  protected float prev_y_vel;
  protected float prev_z_vel;

  private TransformGroup transformGroup;
  private Transform3D transform;
  private Vector3f translation;

  // the default appearance is totally transparent
  private static Appearance defaultApp = null;

  // used for calculating the angle between particles
  private static Vector2f dirvec = new Vector2f();
  private static Vector2f refvec = new Vector2f(0,1);

  // used for inverting the particle
  private static Vector3d invertVec = new Vector3d();
  private boolean isInverted;

  // a particle is alive from the time it is created to the time it is destroy()ed
  private boolean alive = true;
  private boolean prev_alive = true;

  // this lists the updates applied to the particle
  private StringBuffer updates = new StringBuffer();

  // type definitions
  /** Linear particles are not affected by the ground. */
  public final static int TYPE_LINEAR = 0;

  /** Rolling particles will stay right on top of the ground. */
  public final static int TYPE_ROLLING = 1;

  /** Invisible particles will not show up on the minimap. (for spell effects)*/
  public final static int INVISIBLE = 0;

  /** Visible particles will show up on the minimap when they are in range. (for monsters)*/
  public final static int VISIBLE = 1;

  /** Always Visible particles will always show up on the minimap and will be clamped to the borders. (for castles and baloons)*/
  public final static int ALWAYS_VISIBLE = 2;

  private int type = 0;
  private int visibility = 0;
  private float radius;
  private boolean solid = true;

  private int prev_type = 0;
  private int prev_visibility = 0;
  private float prev_radius;
  private boolean prev_solid = true;

  private Color3f color = new Color3f(Color.green);
  private Color3f prev_color = new Color3f(Color.green);

  private long expiretime = 0;

  private Vector lastGridVector;

  // all particles have the same geometry
  private static GeometryInfo geometry;

  private OrientedShape3D shape;

  public Particle()
  {
    init(OrientedShape3D.ROTATE_ABOUT_AXIS);
  }

  public Particle(int mode)
  {
    init(mode);
  }

  private void init(int mode)
  {
    setCapability(BranchGroup.ALLOW_DETACH);

    if (defaultApp == null)
    {
      defaultApp  = new Appearance();
      defaultApp.setTransparencyAttributes(
          new TransparencyAttributes(TransparencyAttributes.FASTEST,1));
    }

    transformGroup = new TransformGroup();
    transform = new Transform3D();
    translation = new Vector3f();
    shape = new OrientedShape3D();

    setAppearance(defaultApp);

    shape.setAlignmentMode(mode);

    shape.setAlignmentAxis(0.0f, 1.0f, 0.0f);

    // create the geometry if it is not yet created
    if (geometry == null)
    {
      System.out.println("Making Particle Geometry...");

      geometry = new GeometryInfo(GeometryInfo.QUAD_ARRAY);

      Point3f[] coordinates = {
        new Point3f(-1.0f, -1.0f, 0.0f) ,
        new Point3f(1.0f, -1.0f, 0.0f) ,
        new Point3f(1.0f, 1.0f, 0.0f) ,
        new Point3f(-1.0f, 1.0f, 0.0f)  };
      geometry.setCoordinates(coordinates);

      TexCoord2f[] texcoords = {
        new TexCoord2f(0.0f, 0.0f),
        new TexCoord2f(1.0f, 0.0f),
        new TexCoord2f(1.0f, 1.0f),
        new TexCoord2f(0.0f, 1.0f) };

      geometry.setTextureCoordinateParams(1, 2);
      geometry.setTextureCoordinates(0, texcoords);
    }

    setScale(1.0f);
    setPosition(0f,0f,0f);
    setVelocity(0f,0f,0f);

    shape.setGeometry(geometry.getGeometryArray());
    shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
    shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

    translation.set(0.0f, 0.0f, 0.0f);
    transform.setTranslation(translation);

    transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    transformGroup.setTransform(transform);

    this.addChild(transformGroup);

    transformGroup.addChild(shape);
  }

  /** The type of the particle affects how it interacts with the ground. */
  public void setType(int type) { this.type = type; }
  /** The type of the particle affects how it interacts with the ground. */
  public int getType() { return type; }

  /** The visibility of the particle determines if it is shown on the minimap. */
  public void setVisibility(int visibility) { this.visibility = visibility; }
  /** The visibility of the particle determines if it is shown on the minimap. */
  public int getVisibility() { return visibility; }

  /** The particle will remove itself from the world at the expiretime. */
  public void setExpireTime(long expireTime) {this.expiretime = expireTime;}
  /** The particle will remove itself from the world at the expiretime. */
  public long getExpireTime() {return expiretime;}

  /** Set the particle's position in meters from the origin */
  public void setPosition(Particle p)
  {
    x_pos = p.x_pos;
    y_pos = p.y_pos;
    z_pos = p.z_pos;
  }

  /** Set the particle's position in meters from the origin */
  public void setPosition(float x, float y, float z)
  {
    x_pos = x;
    y_pos = y;
    z_pos = z;
  }

  /** Returns the distance between the particles minus thier radii */
  public float distance(Particle p)
  {
    if (p==null) return Float.MAX_VALUE;

    float x = p.x_pos-this.x_pos;
    float z = p.z_pos-this.z_pos;

    float dist = (GameFrame.MAP_RESOLUTION-1)*GameFrame.MAP_GRID_SCALE;
    if (x >  dist/2f) x -= dist;
    if (x < -dist/2f) x += dist;
    if (z >  dist/2f) z -= dist;
    if (z < -dist/2f) z += dist;

    return (float)Math.sqrt(x*x + z*z)-radius-p.radius;
  }

  /** Returns the direction in the x,z plane between the particles in radians.*/
  public float direction(Particle p)
  {
    // calculate the angle
    float x = p.x_pos-this.x_pos;
    float z = p.z_pos-this.z_pos;

    // compensate for wrapping
    float dist = (GameFrame.MAP_RESOLUTION-1)*GameFrame.MAP_GRID_SCALE;
    if (x >  dist/2f) x -= dist;
    if (x < -dist/2f) x += dist;
    if (z >  dist/2f) z -= dist;
    if (z < -dist/2f) z += dist;

    dirvec.set(x, z);
    float angle = dirvec.angle(refvec);
    if (x>0) angle = 0-angle;

    return angle;
  }

  /** This will be called every frame. If it is overridden, it should be called
   * by the overriding method. Note: move() is always called before collided().
   */
  public void move(long time)
  {
    float ftime = time / 1000.0f;
    x_pos += x_vel * ftime;
    y_pos += y_vel * ftime;
    z_pos += z_vel * ftime;

    // handle camera movement wrapping
    float cam_x = GameFrame.renderer.getCamera().getCamX();
    float cam_z = GameFrame.renderer.getCamera().getCamZ();
    float size = (GameFrame.MAP_RESOLUTION-1)*GameFrame.MAP_GRID_SCALE;
    if (z_pos-cam_z > size/2) z_pos -= size;
    if (z_pos-cam_z < -size/2) z_pos += size;
    if (x_pos-cam_x > size/2) x_pos -= size;
    if (x_pos-cam_x < -size/2) x_pos += size;

    if (type == TYPE_ROLLING)
    {
      y_pos = (float)transform.getScale() + GameFrame.renderer.getMap().getHeightAt(x_pos, z_pos);
    }
    translation.set(x_pos, y_pos, z_pos);
    transform.setTranslation(translation);
    transformGroup.setTransform(transform);
  }

  /** This is run perodically to destroy expired particles and set up for collision checking */
  public void check()
  {
    // check for expiration
    if (expiretime > 0 && expiretime <=GameFrame.CURRENT_TIME) this.destroy();

    // put the point into the correct collision grid.
    Vector vec = GameFrame.physics.getCollisionVector(x_pos, z_pos);
    if (vec != lastGridVector)
    {
      if (lastGridVector != null) lastGridVector.remove(this);
      if (vec != null) vec.add(this);
      lastGridVector = vec;
    }
  }

  /** Returns a Vector containing all the particles in the same grid block as this one. */
  public Vector getGridVector()
  {
    return lastGridVector;
  }

  /** Sets the velocity of the particle, in meters per second.*/
  public void setVelocity(float x, float y, float z)
  {
    x_vel = x;
    y_vel = y;
    z_vel = z;
  }

  /** Returns a new Vector3f representing the velocity of the object.
   * (in Meters per Second)
   */
  public Vector3f getVelocity()
  {
    return new Vector3f(x_vel,y_vel,z_vel);
  }

  /** Sets the radius of the particle, in meters.
   * This affects the size of the sprite and the radius used for
   * collision detection. */
  public void setScale(float scale)
  {
    radius = scale;

    transform.setScale(radius);
    transformGroup.setTransform(transform);
  }

  /** Inverting the Particle will cause it's Appearance to be flipped horizontally. */
  public void invert()
  {
    if (isInverted) return;

    isInverted = true;

    invertVec.x = -radius;
    invertVec.y = radius;
    invertVec.z = radius;

    transform.setScale(invertVec);
    transformGroup.setTransform(transform);
  }

  /** Unnverting the Particle will cause it's Appearance not to be flipped horizontally. */
  public void uninvert()
  {
    if (!isInverted) return;

    isInverted = false;

    invertVec.x = -radius;
    invertVec.y = radius;
    invertVec.z = radius;

    transform.setScale(invertVec);
    transformGroup.setTransform(transform);
  }

  /** Flipping a particle will cause it to invert if it is uninverted, and vice versa. */
  public void flip()
  {
    if (isInverted) uninvert();
    else invert();
  }


  /** Returns a new Point3f object containing the position of the object.
   * (represented in Meters from the Origin)
   */
  public Point3f getPosition()
  {
    return new Point3f(x_pos,y_pos,z_pos);
  }

  /** Returns the distance in meters above the ground. */
  public float getAltitude()
  {
    return (y_pos - GameFrame.renderer.getMap().getHeightAt(x_pos, z_pos));
  }

  /** Sets the altitude (meters above the ground) of the particle without changing the x,z. */
  public void setAltitude(float altitude)
  {
    float newy_pos = altitude + GameFrame.renderer.getMap().getHeightAt(x_pos, z_pos);
    setPosition(x_pos, newy_pos, z_pos);
  }

  /** The scale of the object represents its radius, in Meters */
  public float getScale()
  {
    return radius;
  }

  /** If an object is not solid, it does not register colisions. */
  public boolean getSolid()
  {
    return solid;
  }

  /** A particle is not capable of making its own appearance, so one must
   * be provided. */
  public void setAppearance(Appearance appearance)
  {
    if (appearance == null) shape.setAppearance(defaultApp);
    else shape.setAppearance(appearance);
  }

  /** If an object is not solid, it does not register colisions. */
  public void setSolid(boolean solid)
  {
    this.solid = solid;
  }

  /** Returns the Appearance object that this particle is using. */
  public Appearance getAppearance()
  {
    return shape.getAppearance();
  }

  /** Returns the GeometryArray that this object is using. */
  public Geometry getGeometry()
  {
    return geometry.getGeometryArray();
  }

  /** Sets the color of the minimap dot.   */
  public void setColor3f(Color3f color)
  {
    this.color = color;
  }

  /** Returns the color of the minimap dot. */
  public Color3f getColor3f()
  {
    return color;
  }

  /** When two particles collide, the Physics Engine calls this method on both
   * Particles. Note: this method will be called for each particle that this particle
   * is in collision with, and will be called each frame for the duration of the
   * collision.
   *
   * Any class that extends particle should override this method to provide custom
   * collision behavior. It is not necessary for overriding methods to call this.
   * The default collided() behavior is nothing.
   *
   * Note: move() is always called before collided().
   */

  public void collided(Particle particle)
  {

  }

  /** returns true if this particle can see the target particle */
  public boolean canSee(Particle target)
  {
    if (!target.isAlive()) return false;

    if (x_pos - target.x_pos > GameFrame.SIGHT_RANGE) return false;
    if (target.x_pos - x_pos > GameFrame.SIGHT_RANGE) return false;
    if (z_pos - target.z_pos > GameFrame.SIGHT_RANGE) return false;
    if (target.z_pos - z_pos > GameFrame.SIGHT_RANGE) return false;

    if (distance(target) > GameFrame.SIGHT_RANGE) return false;

    return true;
  }

  /** sets the velocity in the direction of the target, at the specified speed. */
  public void moveTowards(Particle target, float speed)
  {
    Vector3f vec1 = new Vector3f(target.x_pos - x_pos, target.y_pos - y_pos,
                                 target.z_pos - z_pos);
    vec1.normalize();

    x_vel = vec1.x*speed;
    y_vel = vec1.y*speed;
    z_vel = vec1.z*speed;

  }

  /** removes this particle from the world. */
  public void destroy()
  {
    alive = false;
  }

  /** returns false if the particle has been destroyed. */
  public boolean isAlive()
  {
    return alive;
  }

  /** Returns a string containing information about this particle. */
  public String toString()
  {
    /*StringBuffer string = new StringBuffer();
    string.append("pos(" + x_pos + "," + y_pos + "," + z_pos + ")");
    string.append("vel(" + x_vel + "," + y_vel + "," + z_vel + ")");
    string.append("alive("+alive+")");
    string.append("color("+color.x+","+color.y+","+color.z+")");
    string.append("radius(" + radius + ")");
    string.append("solid(" + solid + ")");
    string.append("type(" + type + ")");
    string.append("visibility(" + visibility + ")");

    return string.toString();*/

    return this.getClass().toString() + " @ " + getPosition();
  }

  public String getUpdates()
  {
    // clear the updates
    updates.delete(0, updates.length());

    // add the position if it has changed
    if (x_pos != prev_x_pos || y_pos != prev_y_pos || z_pos != prev_z_pos)
    {
      updates.append("pos(" + x_pos + "," + y_pos + "," + z_pos + ")");
    }

    // add the velocity if it has changed
    if (x_vel != prev_x_vel || y_vel != prev_y_vel || z_vel != prev_z_vel)
    {
      updates.append("vel(" + x_vel + "," + y_vel + "," + z_vel + ")");
    }

    // add the alive if it has changed
    if (alive != prev_alive)
    {
      updates.append("alive("+alive+")");
    }

    // add the color if it has changed
    if (color != prev_color)
    {
      updates.append("color("+color.x+","+color.y+","+color.z+")");
    }

    // add the radius if it has changed
    if (radius != prev_radius)
    {
      updates.append("radius(" + radius + ")");
    }

    // add the solid if is has changed
    if (solid != prev_solid)
    {
      updates.append("solid(" + solid + ")");
    }

    // add the type if it has changed
    if (type != prev_type)
    {
      updates.append("type(" + type + ")");
    }

    // add the visibility if it has changed
    if (visibility != prev_visibility)
    {
      updates.append("visibility(" + visibility + ")");
    }

    // update the prevs
    prev_x_pos = x_pos;
    prev_y_pos = y_pos;
    prev_z_pos = z_pos;
    prev_x_vel = x_vel;
    prev_y_vel = y_vel;
    prev_z_vel = z_vel;
    prev_alive = alive;
    prev_color = color;
    prev_radius = radius;
    prev_solid = solid;
    prev_type = type;
    prev_visibility = visibility;



    return updates.toString();
  }
}