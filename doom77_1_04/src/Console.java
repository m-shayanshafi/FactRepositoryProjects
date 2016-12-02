import java.util.*;

public class Console {
  private static List<String> strings;
  private static List<String> timed;
  private static List<Double> timers;

  public static void init() {
    strings = new ArrayList<String>();
    timed = new ArrayList<String>();
    timers = new ArrayList<Double>();
  }

  public static void set(String s) {
    int i=0;
    while (i<s.length()) {
      int index = s.indexOf("\n", i);
      if (index==-1) {
        strings.add(s.substring(i));
        return;
      } else {
        strings.add(s.substring(i, index));
        i = index+1;
      }
    }
  }

  public static void set(String s, double period) {
    timed.add(s);
    timers.add(new Double(Doom77.time+period));
  }

  public static void print() {
    for (int i=0; i<timed.size(); i++) {
      if (timers.get(i)>Doom77.time) {
        set(timed.get(i));
      } else {
        timed.remove(i);
        timers.remove(i--);
      }
    }
    int y = (Gfx.h-(strings.size()*16))/2;
    for (String s: strings) {
      int x = (Gfx.w-(s.length()*8))/2;
      if (x>0 && y>0 && y<Gfx.h-16) {
        F.p(s, x, y);
      }
      y+=16;
    }
    strings.clear();
  }
}
