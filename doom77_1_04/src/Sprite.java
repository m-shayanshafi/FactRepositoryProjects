import java.awt.image.*;
import java.awt.*;

public class Sprite {
  public int[] p;
  public int w;
  public int h;
  public int wh;
  public int hh;

  public Sprite(String filename, int width, int height) {
    try {
      this.w = width;
      this.h = height;
      setHalfs();
      Image img = Doom77.d.getImage(Doom77.d.getDocumentBase(), filename);
      p = new int[w*h];
      PixelGrabber pg= new PixelGrabber(img, 0, 0, w, h, p, 0, w);
      pg.grabPixels();
    } catch(Exception ignored) {}
  }

  public Sprite(Sprite sp) {
    p = new int[sp.p.length];
    System.arraycopy(sp.p, 0, p, 0, sp.p.length);
    w = sp.w;
    h = sp.h;
    setHalfs();
  }

  public Sprite(String name, Sprite s) {
    w = s.w;
    h = s.h;
    setHalfs();
    p = new int[h*w];
    System.arraycopy(s.p, 0, p, 0, h*w);
    Gfx.sprites.put(name, this);
  }

  public void tile(int count) {
    B b = new B(w*count, h*count);
    for (int y=0; y<count; y++) {
      for (int x=0; x<count; x++) {
        b.d(this, x*w, y*h);
      }
    }
    p = b.p;
    w*=count;
    h*=count;
    setHalfs();
  }

  public void tile() {
    B b = new B(w*2, h*2);
    b.d(this, 0, 0);
    b.d(this, w, 0, true, false);
    b.d(this, 0, h, false, true);
    b.d(this, w, h, true, true);
    p = b.p;
    w*=2;
    h*=2;
    setHalfs();
  }

  private void setHalfs() {
    wh = w/2;
    hh = h/2;
  }
}
