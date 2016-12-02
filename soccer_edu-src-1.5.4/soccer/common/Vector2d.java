/* Vector2d.java

   Copyright (C) 2001  Yu Zhang

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the 
   Free Software Foundation, Inc., 
   59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/


package soccer.common;

/**
 * A utility class provides 2D vector calculation.
 *
 * @author Yu Zhang
 */
public class Vector2d
{

  private double x;
  private double y;

  /**
   * Constructs a default 2D vector set to (0,0).
   */
  public Vector2d()
  {
    this.x = 0;
    this.y = 0;
  } 

  /**
   * Constructs a 2D vector set to (x,y).
   *
   * @param x the x coordinate.
   * @param y the y coordinate.
   */
  public Vector2d(double x, double y)
  {
    this.x = x;
    this.y = y;
  }

  /**
   * Constructs a 2D vector, which has the same value as the other Vector vec.
   * 
   * @param vec a 2D vector.
   */
  public Vector2d(Vector2d vec)
  {
    this.x = vec.x;
    this.y = vec.y;
  }

  /**
   * Copys the given 2D vector.
   * @param vec the source 2D vector.
   */
   public void copy(Vector2d vec)
  {
    this.x = vec.x;
    this.y = vec.y;
  }

  /**
   * Adds the given 2D vector.
   *
   * @param pv the 2D vector being added.
   */
  public void add(Vector2d pv)
  {
    setXY(x + pv.getX(), y + pv.getY());
  }

   /**
   * Adds the given 2d vectors.
   *
   * @param v1 the first 2D vector being added.
   * @param v2 the second 2D vector being added.
   */
  public static Vector2d add(Vector2d v1, Vector2d v2)
  {
    Vector2d v = new Vector2d(v1.getX() + v2.getX(), v1.getY() + v2.getY());
    return v;
  }

   /**
   * Adds the given 2d vectors.
   *
   * @param v1 the first 2D vector being added.
   * @param v2 the second 2D vector being added.
   * @param v3 the result
   */
  public static void add(Vector2d v1, Vector2d v2, Vector2d v3)
  {
    v3.setXY(v1.getX() + v2.getX(), v1.getY() + v2.getY());
  }
  

  /**
   * Increase the value of x coordinate.
   * 
   * @param x the value being added.
   */
  public void addX(double x)
  {
    this.x += x;
  }

  /**
   * Increase the value of y coordinate.
   * 
   * @param y the value being added.
   */
  public void addY(double y)
  {
    this.y += y;
  }

  /**
   * The absolute direction of this vector.
   *
   * @return the absolute direction of this vector.
   */
  public double dir()
  {
    return(Util.Rad2Deg(Math.atan2(y, x)));
  }

  /**
   * The relative direction of the given vector to this vector.
   *
   * @param that the given 2D vector.
   * @return the relative direction of the given vector to this vector.
   */
  public double direction(Vector2d that)
  {
    double dir = Util.Rad2Deg(Math.atan2(that.getY() - getY(), that.getX() - getX()));
    return dir;
  }

  /**
   * The distance between this 2D vector and given (X,Y).
   *
   * @param X the given x coordinate.
   * @param Y the given y coordinate.
   * @return the distance between this 2D vector and given (X,Y).
   */
  public double distance(double X, double Y)
  {
    double x = (X - getX());
    double y = (Y - getY());
    return(Math.sqrt(x*x + y*y));
  }

  /**
   * The distance between this 2D vector and given vector.
   *
   * @param that the given vector.
   * @return the distance between this 2D vector and given vector.
   */
		    
  public double distance(Vector2d that)
  {
    double x = (that.getX() - getX());
    double y = (that.getY() - getY());
    return(Math.sqrt(x*x + y*y));
  }

  /**
   * Gets this vector's x coordinate.
   *
   * @return this vector's x coordinate.
   */
  public double getX()
  {
    return x;
  }  

  /**
   * Gets this vector's y coordinate.
   *
   * @return this vector's y coordinate.
   */
  public double getY()
  {
    return y;
  }

  /**
   * Inverses this vector. Change its direction 180 degrees.
   */
  public void inverse()
  {
    setXY(-x, -y);
  }  

  /**
   * Multiply this vector with the given vector, like V1.x * V2.x + V1.y * V2.y.
   *
   * @return the result of the multiplication.
   */
  public double multiply(Vector2d that)
  {
    return (this.x*that.x + this.y*that.y);
  }

  /**
   * The absolute value of this vector.
   * 
   * @return The absolute value of this vector.
   */
  public double norm()
  {
    return(Math.sqrt(x*x + y*y));
  }

  /**
   * Converts a vector in polar coordinate to (x,y) coordinate.
   *
   * @param dist the distance coordinate
   * @param dir the angle coordinate
   * @param the converted 2D vector
   */
  public static Vector2d polar(double dist, double dir)
  {
    double angle = Util.Deg2Rad(dir);
    Vector2d vec = new Vector2d(dist*Math.cos(angle), dist*Math.sin(angle));
    return vec;
  }

  /**
   * Gets the angle coordinate of the given 2D vector.
   *
   * @param vec the given 2D vector.
   * @return the angle coordinate
   */
  public static double polar_dir(Vector2d vec)
  {
    double dir = Util.Rad2Deg(Math.atan2(vec.getY(), vec.getX()));
    return dir;
  } 

  /**
   * Gets the distance coordinate of the given 2D vector.
   *
   * @param vec the given 2D vector.
   * @return the distance coordinate.
   */
  public static double polar_dist(Vector2d vec)
  {
    double dist = Math.sqrt(Util.Pow(vec.getY()) + Util.Pow(vec.getX()));
    return dist;
  } 

  /**
   * Sets the x coordinate of the vector.
   *
   * @param x the x coordinate.
   */
  public void setX(double x)
  {
    this.x = x;
  } 

  /**
   * Sets the x, y coordinate of the vector.
   *
   * @param x the x coordinate.
   * @param y the y coordinate.
   */
  public void setXY(double x, double y)
  {
    this.x = x;
    this.y = y;
  } 

  /**
   * Copys the given 2D vector.
   * @param vec the source 2D vector.
   */
  public void setXY(Vector2d vec)
  {
    this.x = vec.x;
    this.y = vec.y;
  }

  /**
   * Sets the y coordinate of the vector.
   *
   * @param y the y coordinate.
   */
  public void setY(double y)
  {
    this.y = y;
  }

  /**
   * Subtracts the given 2D vector.
   *
   * @param pv the 2D vector being subtracted.
   */
  public void subtract(Vector2d pv)
  {
    setXY(x - pv.getX(), y - pv.getY());
  }

   /**
   * Subtracts the given 2d vector.
   *
   * @param v1 the 2D vector being subtracted.
   * @param v2 subtrahend.
   */
  public static Vector2d subtract(Vector2d v1, Vector2d v2)
  {
    Vector2d v = new Vector2d(v1.getX() - v2.getX(), v1.getY() - v2.getY());
    return v;
  }
  
   /**
   * Subtracts the given 2d vector.
   *
   * @param v1 the 2D vector being subtracted.
   * @param v2 subtrahend.
   * @param v3 result vector
   */
  public static void subtract(Vector2d v1, Vector2d v2, Vector2d v3)
  {
    v3.setXY(v1.getX() - v2.getX(), v1.getY() - v2.getY());
  }
  


  /**
   * Changes the size of the vector.
   * If the value is negative, the direction is changed 180 degrees.
   *
   * @param value the value being multiplied by both x, y coordinates.
   */
  public void times(double value)
  {
    this.x *= value;
    this.y *= value;
  }    

  // returns new vector whose coordinates are multiplied
  public Vector2d timesV(double value)
  {
    return new Vector2d( this.x * value, this.y * value );
  }    
  
  
  //---------------------------------------------------------------------------
  public String toString(){
    
    return "x = " + x + " , y = " + y;
  }
}
