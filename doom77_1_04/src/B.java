public class B {//gfx buf
  public static final int transp = -1;

  public int[] p;
  public int w;
  public int h;
  public int wh;//half width
  public int hh;//half height
  public double dwh;//double half width;

  public B(Sprite s) {
    this(s.p, s.w, s.h);
  }

  public B(int width, int height) {
    p = new int[width*height];
    w = width;
    h = height;
    setHalfs();
  }

  public B(int[] pix, int width, int height) {
    p = pix;
    w = width;
    h = height;
    setHalfs();
  }

  private void setHalfs() {
    wh = w/2;
    hh = h/2;
    dwh = ((double)w)/2;
  }

  public void bluegreen() {
    int[] rgb;
    for (int i=w*h-1; i>=0; i--) {
      rgb = C.rgb(p[i]);
      p[i] = C.c(rgb[0], rgb[2], rgb[1]);
    }
  }

  public void whitened(double k) {
    for (int i=w*h-1; i>=0; i--) {
      p[i] = C.w(p[i], k);
    }
  }

  public void invertedBut(int c) {
    int clr;
    for (int i=w*h-1; i>=0; i--) {
      clr = p[i];
      if (clr!=c) p[i] = C.i(clr);
    }
  }

  /**
   * Each color of each pixel  the more effected by param koef [0; 1]
   * the more horizontal it's closer to center.
   */
  public void horizontalColor(double r, double g, double b) {
    r = 1-r;
    g = 1-g;
    b = 1-b;
    int ofst = 0;
    double[] rgb;
    for (int y=0; y<h; y++) {
      for (int x=0; x<w; x++) {
        double k = Math.abs(dwh-x)/dwh;
        k = 1-(k*k);
        rgb = C.drgb(p[ofst]);
        p[ofst++] = C.c(rgb[0]-(rgb[0]*k*r), rgb[1]-(rgb[1]*k*g), rgb[2]-(rgb[2]*k*b));
      }
    }
  }

  public void leftrightroundColor(double r, double g, double b, boolean left) {
    r = 1-r;
    g = 1-g;
    b = 1-b;
    int ofst = 0;
    double[] rgb;
    for (int y=0; y<h; y++) {
      for (int x=0; x<w; x++) {
        double k;
        if (left) {
          if (y<=hh) {
            k = Math.abs(dwh-x)/dwh;
          } else {
            k = Mat.dist(dwh-x, dwh-y)/dwh;
            if (k>1) k=1;
          }
        } else {
          if (y>hh) {
            k = Math.abs(dwh-x)/dwh;
          } else {
            k = Mat.dist(dwh-x, dwh-y)/dwh;
            if (k>1) k=1;
          }
        }
        k = 1-(k*k);
        rgb = C.drgb(p[ofst]);
        p[ofst++] = C.c(rgb[0]-(rgb[0]*k*r), rgb[1]-(rgb[1]*k*g), rgb[2]-(rgb[2]*k*b));
      }
    }
  }

  public void spotlight(double r, double g, double b) {
    r = 1-r;
    g = 1-g;
    b = 1-b;
    int ofst = 0;
    double[] rgb;
    for (int y=0; y<h; y++) {
      for (int x=0; x<w; x++) {
        double k = Mat.dist(dwh-x, dwh-y)/dwh;
        if (k>1) k=1;
        k = 1-(k*k);
        rgb = C.drgb(p[ofst]);
        p[ofst++] = C.c(rgb[0]-(rgb[0]*k*r), rgb[1]-(rgb[1]*k*g), rgb[2]-(rgb[2]*k*b));
      }
    }
  }

  /**
   * x - from x included
   * x1 - till x included
   */
  public void filRect(int x, int y, int x1, int y1, int c) {
    if (x<0) x=0;
    if (y<0) y=0;
    int xb = x1+1;
    if (xb>w) xb = w;
    int yb = y1+1;
    if (yb>h) yb = h;
    int ofst;
    for (int yy=y; yy<yb; yy++) {
      ofst = yy*w+x;
      for (int xx=x; xx<xb; xx++) {
        p[ofst++] = c;
      }
    }
  }

  public void dCenter(Sprite s) {
    d(s, wh-s.wh, hh-s.hh);
  }

  public void d(String s, int x, int y) {
    d(Gfx.sprites.get(s), x, y);
  }

  public void d(Sprite s, int x, int y) {
    d(s.p, s.w, s.h, x, y);
  }

  public void d(Sprite s, int x, int y, boolean xrot, boolean yrot) {
    d(s.p, s.w, s.h, x, y, xrot, yrot);
  }

  private void d(int[] b, int ww, int hh, int x, int y) {
    if (x<0) x=0;
    if (y<0) y=0;
    if (x+ww>=w) x = w-ww;
    if (y+hh>=h) y = h-hh;
    int xb = x + ww;
    int yb = y + hh;
    int pix = 0;
    int c;
    int ofst;
    for (int yy=y; yy<yb; yy++) {
      ofst = yy*w+x;
      for (int xx=x; xx<xb; xx++) {
        c = b[pix++];
        if (c != transp) {
          p[ofst++] = c;
        } else ofst++;
      }
    }
  }

  private void d(int[] b, int ww, int hh, int x, int y, boolean xrot, boolean yrot) {
    int[] pp = new int[ww*hh];
    if (xrot) {
      for (int yy=0; yy<hh; yy++) {
        for (int xx=0; xx<ww; xx++) {
          pp[yy*ww+xx] = b[yy*ww+ww-xx-1];
        }
      }
    } else {
      System.arraycopy(b, 0, pp, 0, hh*ww);
    }
    int[] ppp = new int[ww*hh];
    if (yrot) {
      for (int yy=0; yy<hh; yy++) {
        for (int xx=0; xx<ww; xx++) {
          ppp[yy*ww+xx] = pp[(hh-yy-1)*ww+xx];
        }
      }
    } else {
      System.arraycopy(pp, 0, ppp, 0, hh*ww);
    }
    b = ppp;
    if (x<0) x=0;
    if (y<0) y=0;
    if (x+ww>=w) x = w-ww;
    if (y+hh>=h) y = h-hh;
    int xb = x + ww;
    int yb = y + hh;
    int pix = 0;
    int c;
    int ofst;
    for (int yy=y; yy<yb; yy++) {
      ofst = yy*w+x;
      for (int xx=x; xx<xb; xx++) {
        c = b[pix++];
        if (c != transp) {
          p[ofst++] = c;
        } else ofst++;
      }
    }
  }

  public void zoom(int[]b, double ww, double hh, double xc, double yc, double zoom) {
    int sx = (int)(xc - (ww/2*zoom));
    int sy = (int)(yc - (hh/2*zoom));
    int xb = (int)(sx + (ww*zoom));
    int yb = (int)(sy + (hh*zoom));
    double www = ww*zoom;
    double hhh = hh*zoom;
    double pix;
    int c;
    int ofst;
    int y;
    double x;
    double step = 1/zoom;
    for (int yy=sy; yy<yb; yy++) {
      ofst = yy*w+sx;
      y = (int)(((double)(yy-sy))/hhh*hh);
      pix = ww*y;
      for (int xx=sx; xx<xb; xx++) {
        p[ofst++] = b[(int)pix];
        pix += step;
      }
    }
  }

  public void dOn(Sprite s, int x, int y, int clr) {
    int[] b = s.p;
    int xb = x + s.w;
    int yb = y + s.h;
    int pix = 0;
    int c;
    int ofst;
    for (int yy=y; yy<yb; yy++) {
      ofst = yy*w+x;
      for (int xx=x; xx<xb; xx++) {
        c = b[pix++];
        if (c != transp) {
          if (p[ofst] == clr) p[ofst++] = c; else ofst++;
        } else ofst++;
      }
    }
  }

  public void vl(double x, double ys, double ye, int c) { //vertical line
    int xx = (int)(x+0.5);
    int iys = (int)(ys+0.5);
    int iyb = (int)(ye+1.5);
    int ofst = iys*w+xx;
    for (;iys<iyb; iys++) {
      p[ofst] = c;
      ofst+=w;
    }
  }

  public void vl(int x, double y, double zoom, Sprite sp, int sy) {
    double starttextx = 0;//for line out of screen
    int ys = (int) (((int)y==y) ? y : y+1);
    if (ys < 0) {
      starttextx = -y*sp.w/(hh-y)/2;
      ys = 0;
    }
    double dyb = zoom*sp.w+y;//double y border
    int yb = (int)dyb;
    if (yb>h) yb = h;
    int ofst = ys*w+x;
    double dyp = (-y+ys)/zoom;//double start x text missed
    double pix = sy*sp.h + dyp;
    double step = 1.0/zoom;
    int[] spix = sp.p;
    int i = ys;
    try {
      for (; i<yb; i++) {
        p[ofst] = spix[(int)pix];
        ofst += w;
        pix += step;
      }
    } catch(Exception ign) {
     //System.out.println(ign+"\nx:"+x+" y:"+y+" zoom:"+zoom+" sy:"+sy+" i:"+i+"\nyb:"+yb+" ofst:"+ofst+" pix:"+pix+" dyp:"+dyp+" step:"+step+" sp.h:"+sp.h+" spix.length:"+spix.length);
    }
  }

  public void vls(int x, double y, Sprite sp, double zoom, int sy) {
    double starttextx = 0;//for line out of screen
    int ys = (int) (((int)y==y) ? y : y+1);
    if (ys < 0) {
      starttextx = -y*sp.w/(hh-y)/2;
      ys = 0;
    }
    double dyb = zoom*sp.w+y;//double y border
    int yb = (int)dyb;
    if (yb>h) yb = h;
    int ofst = ys*w+x;
    double dyp = (-y+ys)/zoom;//double start x text missed
    double pix = sy*sp.w + dyp;
    double step = 1.0/zoom;
    int[] spix = sp.p;
    int i = ys;
    int c;
    try {
      for (; i<yb; i++) {
        c = spix[(int)pix];
        if (c!=transp) p[ofst]=c;
        ofst += w;
        pix += step;
      }
    } catch(Exception ign) {
     //System.out.println("vls:\n"+ign+"\nx:"+x+" y:"+y+" zoom:"+zoom+" sy:"+sy+" i:"+i+"\nyb:"+yb+" ofst:"+ofst+" pix:"+pix+" dyp:"+dyp+" step:"+step+" sp.h:"+sp.h+" spix.length:"+spix.length);
    }
  }
}
