import java.util.*;

public class Mat { //math
  public static final double hhpi = Math.PI/4;
  public static final double hpi = Math.PI/2;
  public static final double pi34 = Math.PI*3/2;
  public static final double dpi = Math.PI*2;

  private static final Random rnd = new Random();

  public static double dist(double x, double y) {
    return Math.sqrt(x*x + (y*y));
  }

  public static double angle(double a) { //normailized angle
    while (a<0) a+=dpi;
    while (a>=dpi) a-=dpi;
    return a;
  }

  public static double angle(double x, double y) {
    return Math.atan2(x, y);
  }

  public static double aangle(double x, double y) {
    double a = Math.atan2(x, y);
    while (a<0) a+=dpi;
    while (a>=dpi) a-=dpi;
    return a;
  }

  /**
   * from a0 to a1
   */
  public static double closest(double a0, double a1) {
    if (a0>=a1) {
      double res = a0-a1;
      return res>Math.PI?dpi-res:-res;
    } else {
      double res = a1-a0;
      return res<Math.PI?res:res-dpi;
    }
  }

  public static double closestpos(double a0, double a1) {
    double res = Math.abs(a0-a1);
    return res>Math.PI ? Math.PI-res : res;
  }

  public static double[] move(double x, double y, double a, double d) {
    return new double[] {Math.sin(a)*d+x, Math.cos(a)*d+y};
  }

  public static double[] kb(double x, double y, double a) {
    //y = k*x + b
    //k = 1/tan(a)
    //b = y - k*x
    double k = 1/Math.tan(a);
    return new double[] {k, -k*x+y};
  }

  /**
   * (-1.1, 5) = 3.9
   * (1.1, 5) = 1.1
   */
  public static double rem(double a, double b) {
    double r = a%b;
    if (r<0) r = b+r;
    return (r==b)?0:r;
  }

  public static double rnd(double max) {
    return rnd.nextDouble()*max;
  }
}
