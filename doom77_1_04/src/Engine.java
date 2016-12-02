import java.util.*;

public class Engine {
  public final static double size = 648; //square size (width & height) in pixels
  public final static double hsize = size/2.0;

  private final static int isize = (int)size;
  private final static double mind = 0.000000001;

  protected static double x;
  protected static double y;
  protected static double angle;
  protected static double radius;
  protected static Monster mon;

  private static boolean shot=false;

  //to remember current xline
  private static double dw;
  private static double xw;
  private static double yw;
  private static int xsw;
  private static int ysw;
  private static double aw;

  protected static void init(double x, double y, double a, double r) {
    Engine.x = x;
    Engine.y = y;
    angle = a;
    radius = r;
  }

  public static void checkCollision() {
    mon.x = x;
    mon.y = y;
    if (xyCollision()) checkCollision();
    if (cornerCollision()) checkCollision();
    if (monCollision()) checkCollision();
  }

  private static boolean xyCollision() {
    int xs = (int)(x/size);
    int ys = (int)(y/size);
    for (int yy=ys-1; yy<ys+2; yy++) {
      for (int xx=xs-1; xx<xs+2; xx++) {
        if (Map.is(xx, yy)) {
          if (x>=xx*size && x<=xx*size+size) {
            if (yy==ys-1) {
              double yyy = yy*size+size;
              if (y-yyy<radius) {
                y = yyy+radius+mind;
                return true;
              }
            } else if (yy==ys+1) {
              double yyy = yy*size;
              if (yyy-y<radius) {
                y = yyy-radius-mind;
                return true;
              }
            }
          } else if (y>=yy*size && y<=yy*size+size) {
            if (xx==xs-1) {
              double xxx = xx*size+size;
              if (x-xxx<radius) {
                x = xxx+radius+mind;
                return true;
              }
            } else if (xx==xs+1) {
              double xxx = xx*size;
              if (xxx-x<radius) {
                x = xxx-radius-mind;
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }
  public static void shoot() {
    shot = true;
    aw = angle;
    xBackground(Gfx.wh);
    xTransparent(Gfx.wh);
    shot = false;
  }

  private static Comparator closestMonComp() {
    return new Comparator() {
      public int compare(Object o1, Object o2) {
        Monster m = Player.player;
        Monster m1 = (Monster)o1;
        Monster m2 = (Monster)o2;
        return (Mat.dist(m.x-m1.x, m.y-m1.y)<Mat.dist(m.x-m2.x, m.y-m2.y))?-1:1;
      }
    };
  }

  private static Comparator farestMonComp() {
    return new Comparator() {
      public int compare(Object o1, Object o2) {
        Monster m = Player.player;
        Monster m1 = (Monster)o1;
        Monster m2 = (Monster)o2;
        return (Mat.dist(m.x-m1.x, m.y-m1.y)>Mat.dist(m.x-m2.x, m.y-m2.y))?-1:1;
      }
    };
  }

  private static boolean monCollision() {
    for (Monster m: Adventure.monsters) {
      if (m.x!=x && m.y!=y) {
        double d = Mat.dist(x-m.x, y-m.y);
        if (d<radius+m.radius) {
          double a = Mat.angle(m.x-x, m.y-y)+Math.PI;
          d = radius+m.radius+mind;
          x = Math.sin(a)*d+m.x;
          y = Math.cos(a)*d+m.y;
          return true;
        }
      }
    }
    return false;
  }


  private static boolean cornerCollision() {
    int xs = (int)(x/size);
    int ys = (int)(y/size);
    for (int yy=ys-1; yy<ys+2; yy++) {
      for (int xx=xs-1; xx<xs+2; xx++) {
        if (Map.is(xx, yy)) {
          double[] xy = closestCorner(xx, yy);
          double d = Mat.dist(x-xy[0], y-xy[1]);
          if (d < radius) {
            double a = Mat.angle(xy[0]-x, xy[1]-y)+Math.PI;
            xy = Mat.move(x, y, a, radius-d+0.0001);
            x = xy[0];
            y = xy[1];
            return true;
          }
        }
      }
    }
    return false;
  }

  private static double[] closestCorner(int xx, int yy) {
    double xy[] = new double[2];
    xy[0] = Math.abs(x-(xx*size))<Math.abs(x-(xx*size+size)) ? xx*size : xx*size+size;
    xy[1] = Math.abs(y-(yy*size))<Math.abs(y-(yy*size+size)) ? yy*size : yy*size+size;
    return xy;
  }

  public static void draw() {
    drawGround();
    for (int sx=0; sx<Gfx.w; sx++) {
      double k = sx<Gfx.wh ? -1 : 1;
      aw = Mat.angle(k*Math.atan((k*sx-(k*Gfx.wh))/Gfx.wh)+angle);
      xBackground(sx);
      while (xTransparent(sx));
    }
  }

  private static void xBackground(int sx) {
    double d = dist(aw);
    if (!shot) {
      double ad = Mat.angle(Math.abs(aw-angle));
      d = Math.cos(ad)*d;
      int sz = Map.sprite(xsw, ysw).w;
      double h = (double)size/2/d*Gfx.hh;
      xw = xw/size*sz;
      yw = yw/size*sz;
      if (xw/sz==(int)xw/sz) {
        if (aw>Math.PI) {
          xw = yw-(sz*(int)(yw/sz));
        } else {
          xw = sz*(int)(yw/sz)+sz-yw;
        }
      } else {
        if (aw>Mat.hpi || aw<Mat.pi34) {
          xw = xw-(sz*(int)(xw/sz));
        } else {
          xw = sz*(int)(xw/sz)+sz-xw;
        }
      }
      if (xw==sz) xw=0;
      Gfx.b.vl(sx, Gfx.hh-h, h*size/sz/hsize, Map.sprite(xsw, ysw), (int)xw);
    }
  }

  private static void drawGround() {
    int[] p = Gfx.b.p;
    Sprite sp = Gfx.sprites.get("kitch");
    int[] t = sp.p;
    int ofst = 0;
    double a = Mat.angle(angle-Mat.hhpi);//xy plane angle of leftest y screen line
    double sxla = Mat.angle(angle+Mat.hpi);//xy plane angle of x screen line
    double nsxla = Mat.angle(angle-Mat.hpi);//xy plane negative angle of x screen line
    double kb[] = Mat.kb(x, y, a);//for xy plane and leftest y screen line projection
    for (int sy=0; sy<Gfx.hh-5; sy++) {
      double ztang = ((double)(Gfx.hh-sy))/Gfx.hh;//for z angle of center y screen line
      double xyd = hsize/ztang;//xy distance till center y screen line
      double xc = Math.sin(angle)*xyd+x;//x of center pixel
      double yc = Math.cos(angle)*xyd+y;//y of center pixel
      double xx = Mat.rem(Math.sin(nsxla)*xyd+xc, size);//start
      double yy = Mat.rem(Math.cos(nsxla)*xyd+yc, size);//start
      double xystep = xyd/Gfx.hh;
      double xstep = xystep*Math.sin(sxla);
      double ystep = xystep*Math.cos(sxla);
      int ofst2 = (Gfx.h-sy-1)*Gfx.w;
      for (int sx=0; sx<Gfx.w; sx++) {
        xx = Mat.rem(xx+xstep, size);
        yy = Mat.rem(yy+ystep, size);
        int c = t[((int)yy)*isize+(int)xx];
        p[ofst++] = c;
        p[ofst2++] = c;
      }
    }
  }

  private static double dist(double a) {
    a = a(a);
    dw = 999999999999999.0;
    udd(a);
    lrd(a);
    return dw;
  }

  private static boolean xTransparent(int sx) {
    double aa = a(Mat.angle(angle-Mat.hpi));
    if (shot) {
      Collections.sort(Adventure.monsters, closestMonComp());
    } else {
      Collections.sort(Adventure.monsters, farestMonComp());
    }
    Player p = Player.player;
    for (Monster m: Adventure.monsters) {
      if ((m.x!=x || m.y!=y) && !(m.x==p.x && m.y==p.y && x==p.x && y==p.y)) {
        double k = 1/Math.tan(aw);
        double b = y-(k*x);
        double kk = 1/Math.tan(aa);
        double xx = (m.y-(kk*m.x)-b)/(k-kk);
        double yy = k*xx+b;
        double ad = Mat.closest(Mat.aangle(m.x-x, m.y-y), aw);
        double adpos = Math.abs(ad);
        if (adpos<Mat.hhpi) {
          xw = Mat.dist(xx-m.x, yy-m.y);
          if (xw<m.radius) {
            double td = Mat.dist(x-xx, y-yy);
            if (td<dw) {
              if (shot) {
                m.dmg();
                return true;
              }
              dw = td;
              Sprite sp = m.sp();
              if (xw>=m.radius) continue;
              xw*=sp.h/m.radius/2.0;
              xw = ad<0?sp.hh-xw:sp.hh+xw;
              double angledist = Mat.dist(x-m.x, y-m.y);
              double dsy = Gfx.hh-(hsize*Gfx.hh/angledist);
              Gfx.b.vls(sx, dsy, sp, hsize/angledist*Gfx.h/sp.w, (int)xw);
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  private static void udd(double a) {
    if (a<Mat.hpi || a>Mat.pi34) {
      double yy = size*(int)(y/size+1);
      while (yy<Map.size*size) {
        double xx = ycross(a, yy);
        int xs = (int)(xx/size);
        int ys = (int)(yy/size);
        if (xs<0 || xs>=Map.size) return;
        if (Map.is(xs, ys)) {
          double td = Mat.dist(x-xx, y-yy);
          if (td<dw) {
            xw = xx;
            yw = yy;
            dw = td;
            xsw = xs;
            ysw = ys;
          }
          return;
        }
        yy += size;
      }
    } else {
      double yy = size*(int)(y/size);
      while (yy>0) {
        double xx = ycross(a, yy);
        int xs = (int)(xx/size);
        int ys = (int)(yy/size)-1;
        if (xs<0 || xs>=Map.size) return;
        if (Map.is(xs, ys)) {
          double td = Mat.dist(x-xx, y-yy);
          if (td<dw) {
            xw = xx;
            yw = yy;
            dw = td;
            xsw = xs;
            ysw = ys;
          }
          return;
        }
        yy -= size;
      }
    }
    throw new RuntimeException("should not be x:"+x+" y:"+y);
  }

  private static void lrd(double a) {
    if (a>Math.PI) {
      double xx = size*(int)(x/size);
      while (xx<Map.size*size) {
        double yy = xcross(a, xx);
        int ys = (int)(yy/size);
        int xs = (int)(xx/size)-1;
        if (ys<0 || ys>=Map.size) return;
        if (Map.is(xs, ys)) {
          double td = Mat.dist(x-xx, y-yy);
          if (td<dw) {
            xw = xx;
            yw = yy;
            dw = td;
            xsw = xs;
            ysw = ys;
          }
          return;
        }
        xx -= size;
      }
    } else {
      double xx = size*(int)(x/size+1);
      while (xx>0) {
        double yy = xcross(a, xx);
        int ys = (int)(yy/size);
        int xs = (int)(xx/size);
        if (ys<0 || ys>=Map.size) return;
        if (Map.is(xs, ys)) {
          double td = Mat.dist(x-xx, y-yy);
          if (td<dw) {
            xw = xx;
            yw = yy;
            dw = td;
            xsw = xs;
            ysw = ys;
          }
          return;
        }
        xx += size;
      }
    }
    throw new RuntimeException("should not be x:"+x+" y:"+y);
  }

  private static double a(double a) {
    if (a==0 || a==Mat.hpi || a==Mat.pi34) a+=0.000000001;
    return a;
  }

  private static double xcross(double a, double xx) {
    //y = k*xx + b
    //k = 1/tan(a)
    //b = y - k*x
    double k = 1/Math.tan(a);
    double b = y - (k*x);
    return k*xx + b;
  }

  private static double ycross(double a, double yy) {
    //yy = k*x + b    x = (yy-b)/k
    //k = 1/tan(a)
    //b = y - k*x
    double k = 1/Math.tan(a);
    double b = y - (k*x);
    return (yy - b)/k;
  }
}
