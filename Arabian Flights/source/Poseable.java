// Poseable.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import java.awt.*;
import javax.media.j3d.*;
import javax.vecmath.*;

/** The poseable class contains the mechanisms for displaying the correct appearance
 * of a particle based on the angle it is facing relative to the camera. This allows
 * the particle to look different from the eight cardinal angles. It extends from
 * Damageable because all Poesables are Damageable, but not all Damageables are
 * Poseable.
 */
public class Poseable extends Damageable
{

  // the currently used Pose
  private Poseable.Pose currentPose;
  // the direction the monster is facing, in radians
  private float faceAngle;

  // used for calculating the camera angle
  private Vector2f camvec = new Vector2f();
  private Vector2f refvec = new Vector2f(0,1);

  // the angle between the particle and the camera
  private float camangle;

  /** This method is called every frame, and calls the Particle.move()  method.
   * It also sets up the correct appearance for the current pose.*/
  public void move(long time)
  {
    if (!isAlive()) return;
    super.move(time);

    // calculate the angle
    float x = GameFrame.renderer.getCamera().getCamX()-x_pos;
    float z = GameFrame.renderer.getCamera().getCamZ()-z_pos;
    camvec.set(x, z);
    camangle = camvec.angle(refvec);
    if (x>0) camangle = 0-camangle;

    // set the right appearance
    Pose.View view = chosePerspective(getPose(), camangle-faceAngle);
    if (view != null)
      if (getAppearance() != view.appearance)
    {
      setAppearance(view.appearance);
      if (view.flip) invert();
      else uninvert();
    }
  }

  /** Gets the angle direction from the monster to the camera, in radians. */
  public final float getCameraDirection()
  {
    return camangle;
  }

  /** Sets the pose to use in determining the appearance. */
  public final void setPose(Pose pose)
  {
    this.currentPose = pose;
  }

  /** Gets the current pose. */
  public final Pose getPose()
  {
    return this.currentPose;
  }

  /** Sets the angle that the monster is facing. */
  public final void setFaceAngle(float angle)
  {
    this.faceAngle = angle;
  }

  /** Gets the current angle that the monster is facing. */
  public final float getFaceAngle()
  {
    return this.faceAngle;
  }

  // this method will choose which perspective to show of the given veiw
  // based on the angle between the camera and the facing, in radians.
  private Pose.View chosePerspective(Pose pose, float angle)
  {
    if (pose == null) return null;

    float pi = (float)Math.PI;

    // first, constrain the angle
    while (angle > 2*pi) angle -= 2*pi;
    while (angle < 0) angle += 2*pi;

    if (angle <= pi*(1/8f) || angle >=  pi*(15/8f)) return pose.backView;
    if (angle >= pi*(1/8f) && angle <=  pi*(3/8f)) return pose.backleftView;
    if (angle >= pi*(3/8f) && angle <=  pi*(5/8f)) return pose.leftView;
    if (angle >= pi*(5/8f) && angle <=  pi*(7/8f)) return pose.frontleftView;
    if (angle >= pi*(7/8f) && angle <=  pi*(9/8f)) return pose.frontView;
    if (angle >= pi*(9/8f) && angle <=  pi*(11/8f)) return pose.frontrightView;
    if (angle >= pi*(11/8f) && angle <=  pi*(13/8f)) return pose.rightView;
    if (angle >= pi*(13/8f) && angle <=  pi*(15/8f)) return pose.backrightView;

    return null;
  }

  /** Each frame of each animation is a View, which contains all eight perspectives. */
  public static class Pose
  {
    // the eight viewpoints
    public View frontView;
    public View frontleftView;
    public View frontrightView;
    public View leftView;
    public View rightView;
    public View backView;
    public View backleftView;
    public View backrightView;

    /** The base is the pathname and first part of the filenames of the set of images.
     * for example, demon_a1 would become demon_a1_fr.png, demon_a1_bl.png, etc.
     */
    public Pose(String base, Color tint)
    {
      frontView = new View(PhysicsEngine.createAppearance(base+"_f.png", tint),false);
      frontleftView = new View(PhysicsEngine.createAppearance(base+"_fl.png", tint),false);
      leftView = new View(PhysicsEngine.createAppearance(base+"_l.png", tint),false);
      backView = new View(PhysicsEngine.createAppearance(base+"_b.png", tint),false);
      backleftView = new View(PhysicsEngine.createAppearance(base+"_bl.png", tint),false);


      frontrightView = new View(frontleftView.appearance, true);//PhysicsEngine.createAppearance(base+"_fr.png", tint);
      rightView = new View(leftView.appearance, true);//PhysicsEngine.createAppearance(base+"_r.png", tint);
      backrightView = new View(backleftView.appearance, true);//PhysicsEngine.createAppearance(base+"_br.png", tint);
    }

    /** Each Veiw contains an Appearance and a boolean to indicate if it should be flipped. */
    public static class View
    {
      public Appearance appearance;
      public boolean flip;

      public View(Appearance app, boolean flip)
      {
        this.appearance = app;
        this.flip = flip;
      }
    }
  }
}