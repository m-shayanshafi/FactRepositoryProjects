public class C {//color
  public static int br(int c, double br) {//brighted color
    return 128 + (int)(br*(c-128));
  }

  public static int c(int r, int g, int b) {
    return (0xff000000 | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF));
  }

  public static int c(double r, double g, double b) {
    return c((int)r, (int)g, (int)b);
  }

  public static int br(int r, int g, int b, double br) {//bright koeff should be <=0
    return c(br(r, br), br(g, br), br(b, br));
  }

  public static int[] rgb(int c) {
    int[] r = new int[3];
    r[0] = (c >> 16) & 0xFF;
    r[1] = (c >> 8) & 0xFF;
    r[2] = c & 0xFF;
    return r;
  }

  public static double[] drgb(int c) {
    int[] rgb = rgb(c);
    double[] r = new double[3];
    r[0] = (double)rgb[0];
    r[1] = (double)rgb[1];
    r[2] = (double)rgb[2];
    return r;
  }

  /**
   * whitening
   */
  public static int w(int c, double k) {//whitened k:0 - the same, k:1 - white
    int r = (c >> 16) & 0xFF;
    int g = (c >> 8) & 0xFF;
    int b = c & 0xFF;
    return c(r+(int)(k*(255-r)), g+(int)(k*(255-g)), b+(int)(k*(255-b)));
  }

  /**
   * inverting
   */
  public static int i(int c) {
    int r = (c >> 16) & 0xFF;
    int g = (c >> 8) & 0xFF;
    int b = c & 0xFF;
    return c(255-r, 255-g, 255-b);
  }

  public static String string(int c) {
    return "[" + ((c >> 16) & 0xFF) + " " + ((c >> 8) & 0xFF) + " " + (c & 0xFF) + "]";
  }
}
