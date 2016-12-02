public class Map {
  public static final int size = 30;
  public static MapElem[][] map;
  public static int endx;
  public static int endy;

  private static final String sm =
    "**************m**XXXuuXXjXXuX*" +
    "*******XXXXXXX XX            *" +
    "******X        Xu XXXuXX m X *" +
    "******j uXXXuXXu   uXXXX XuX *" +
    "******u       Xu   XXX   XX  X" +
    "******X       XX X   X jXXu X*" +
    "******X       XX XXX X   X   X" +
    "******X            X XXXXX   X" +
    "******X       XXXX X     j   m" +
    "****s*X       X**X XXXXX u   X" +
    "***sEsX       X**X       X   X" +
    "***r lX mXmXXm****jjjjjjjb   b" +
    "***X XX X****************x   x" +
    "***X    X****************x   x" +
    "****XXuX*****************X   X" +
    "*************************X   x" +
    "*************************j   X" +
    "*************************X   X" +
    "**************************X X*" +
    "**************************x X*" +
    "**************************X X*" +
    "**************************X  X" +
    "*********2xxx**************3 3" +
    "*gl**   b   xx1l***********j 3" +
    "r     *   b      *b          3" +
    "* ***   b   x1l* ****b3****j 3" +
    "* *******2xx**** *3b3  **r3X X" +
    "* *******ssssslb b         3 3" +
    "* ******s              **r   *" +
    "*********sssssl***3b3b3*******";

  public static void init() {
    map = new MapElem[size][size];
    for (int y=size-1; y>=0; y--) {
      for (int x=0; x<size; x++) {
        if (sm(y, x, " ")) {
          map[y][x] = new MapElem("kitch", false);
        } else if (sm(y, x, "*")) {
          map[y][x] = new MapElem("greenkitch", true);
        } else if (sm(y, x, "j")) {
          map[y][x] = new MapElem("jamesGreen", true);
        } else if (sm(y, x, "m")) {
          map[y][x] = new MapElem("mosesBlueRed", true);
        } else if (sm(y, x, "u")) {
          map[y][x] = new MapElem("boyKitch", true);
        } else if (sm(y, x, "X")) {
          map[y][x] = new MapElem("greenwall", true);
        } else if (sm(y, x, "x")) {
          map[y][x] = new MapElem("redkitch", true);
        } else if (sm(y, x, "g")) {
          map[y][x] = new MapElem("kitch", true);
        } else if (sm(y, x, "b")) {
          map[y][x] = new MapElem("bluered", true);
        } else if (sm(y, x, "3")) {
          map[y][x] = new MapElem("greenred", true);
        } else if (sm(y, x, "s")) {
          map[y][x] = new MapElem("spotlight", true);
        } else if (sm(y, x, "r")) {
          map[y][x] = new MapElem("roundl", true);
        } else if (sm(y, x, "l")) {
          map[y][x] = new MapElem("roundr", true);
        } else if (sm(y, x, "1")) {
          map[y][x] = new MapElem("redroundl", true);
        } else if (sm(y, x, "2")) {
          map[y][x] = new MapElem("redroundr", true);
        } else if (sm(y, x, "E")) {
          map[y][x] = new MapElem("kitch", false);
          endx = x;
          endy = y;
        }
      }
    }
    System.gc();
  }

  public static boolean is(int x, int y) {
    return map[y][x].is;
  }

  public static Sprite sprite(int x, int y) {
    return map[y][x].sprite;
  }

  private static boolean sm(int y, int x, String c) {
    return c.equals(""+sm.charAt((size-1-y)*size+x));
  }
}
