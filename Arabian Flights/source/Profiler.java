// Profiler.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

public class Profiler
{
  private static final long PERIOD = 100;

  public static final int BLOCK_UNKNOWN = 0;
  public static final int BLOCK_CAMERA = 1;
  public static final int BLOCK_MOVE = 2;
  public static final int BLOCK_THINK = 3;
  public static final int BLOCK_AITHINK = 4;
  public static final int BLOCK_COLLIDED = 5;
  public static final int BLOCK_CHECK = 6;
  public static final int BLOCK_PSLEEP = 7;
  public static final int BLOCK_NET = 8;

  private static final int BLOCKS = 9;

  private static String[] blockStrings = {
      "External  ",
      "Camera    ",
      "Movement  ",
      "Monster AI",
      "Wizard AI ",
      "Collisions",
      "Checking  ",
      "P Sleeping"
  };

  private static int render_thread_block = 0;
  private static int period_thread_block = 0;

  private static int[] render_thread_block_count = new int[BLOCKS];
  private static int[] period_thread_block_count = new int[BLOCKS];

  public static void start()
  {
    new Thread(new Ticker()).start();
  }

  public static void setRenderThreadBlock(int block)
  {
    render_thread_block = block;
  }

  public static void setPeriodThreadBlock(int block)
  {
    period_thread_block = block;
  }

  private static void tick()
  {
    render_thread_block_count[render_thread_block]++;
    period_thread_block_count[period_thread_block]++;
  }

  public static void report()
  {
    System.out.println();
    System.out.println("---Render Thread Stats:---");
    for (int i=0; i<BLOCKS; i++)
    {
      if (render_thread_block_count[i] > 0)
        System.out.println(blockStrings[i] + " : " + render_thread_block_count[i]);
    }

    System.out.println();
    System.out.println("---Period Thread Stats:---");
    for (int i = 0; i < BLOCKS; i++)
    {
      if (period_thread_block_count[i] > 0)
        System.out.println(blockStrings[i] + " : " + period_thread_block_count[i]);
    }
    System.out.println();
  }

  public static void reset()
  {
    System.out.println("Profiler Reset");

    for (int i=0; i<BLOCKS; i++)
    {
      render_thread_block_count[i] = 0;
      period_thread_block_count[i] = 0;

      render_thread_block = BLOCK_UNKNOWN;
      period_thread_block = BLOCK_UNKNOWN;
    }
  }

  private static class Ticker implements Runnable
  {
    public void run()
    {
      System.out.println("Profiler Started");

      while (true)
      {
        tick();
        try
        {
          Thread.sleep(PERIOD);
        }
        catch (InterruptedException e) { }
      }
    }
  }
}















