// NightSky.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import javax.media.j3d.*;
import javax.vecmath.*;

/** This is a list of dots to put in the sky, with random positions and colors.
 * They are playced randomly on a heisphere above the origin. */
public class NightSky
{
  private final static int NUM = 1500;

  /** Generates the list. scale is the distance away from origin they are. */
  public Shape3D generate(float scale)
  {
    PointArray points = new PointArray(NUM, PointArray.COORDINATES | PointArray.COLOR_3);
    Point3f[] coords = new Point3f[NUM];
    Color3f[] colors = new Color3f[NUM];

    for (int i=0; i<coords.length; i++)
    {
      // get random longitude and latitude for a star
      double latang = Math.random()*Math.PI/2;
      double longang = Math.random()*Math.PI*2;

      // generate x,y,z position for it
      float x = (float)(Math.sin(longang)*Math.cos(latang))*scale;
      float y = (float)Math.sin(latang)*scale;
      float z = (float)(Math.cos(longang)*Math.cos(latang))*scale;

      // set the coordinate
      coords[i] = new Point3f(x,y,z);

      // generate a random color scalar
      float brightness = (float)Math.random()*.5f;
      float redness = (float)Math.random()*.1f;
      float greenness = (float)Math.random()*.1f;
      float blueness = (float)Math.random()*.2f;
      colors[i] = new Color3f(brightness+redness, brightness+greenness, brightness+blueness);
    }

    points.setCoordinates(0,coords);
    points.setColors(0, colors);

    Appearance appearance = new Appearance();

    Shape3D shape = new Shape3D(points, appearance);

    return shape;
  }
}