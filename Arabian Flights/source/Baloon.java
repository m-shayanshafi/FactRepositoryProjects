// Baloon.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import java.util.Vector;

/** The baloon flies between the castle an a gold mine and brings back gold.*/
public class Baloon extends Damageable
{
  private static float scale = 2f; // the scale of the baloon in meters

  // the owning castle
  private Castle castle;

  // the destination
  private Particle destination;

  // the gold it is carrying
  private int gold;

  // the states
  private static int STATE_RISING = 0;
  private static int STATE_FALLING = 1;
  private static int STATE_FLOATING =2;

  private int state;

  public Baloon(Castle castle)
  {
    this.castle = castle;

    setTeam(castle.getTeam());
    GameFrame.physics.addParticle(this);
    setPosition(castle.getFlag());
    setAppearance(castle.getTeam().getBaloonApp());
    setScale(scale);
    setVisibility(VISIBLE);
    setColor3f(getTeam().getColor3f());

    setHealth((int)(GameFrame.BALOON_HEALTH_BASE + Math.random()*GameFrame.BALOON_HEALTH_VAR));

    state = STATE_FALLING;
    destination = castle.getFlag();
    gold = 0;
  }

  /** It rises until it gets to the right height and then floats towards the
   * destination. When it arrives, it falls.
   */
  public void move(long time)
  {
    super.move(time);

    if (state == STATE_FLOATING)
    {
      // descend to the target when it is above it
      if (distance(destination) + destination.getScale() < 0 && destination.y_pos < y_pos)
      {
        setVelocity(0f,-GameFrame.BALOON_SPEED,0f);
        state = STATE_FALLING;
      }

      // keep the baloon high enough
      else if (getAltitude() < GameFrame.BALOON_ALT * .5)
      {
        setVelocity(0f, GameFrame.BALOON_SPEED, 0f);
        state = STATE_RISING;
      }

      // move towards the target
      else
      {
        float angle = direction(destination);
        setVelocity(-GameFrame.BALOON_SPEED*(float)Math.sin(angle), 0, GameFrame.BALOON_SPEED*(float)Math.cos(angle));
      }
    }
    else if (state == STATE_RISING)
    {
      if (getAltitude() > GameFrame.BALOON_ALT) state = STATE_FLOATING;
    }
  }

  /** When it collides with a castle flag, it drops off the gold and sets
   * the destination to a gold mine. When it collides with a gold mine, it gets the gold
   * and sets the destinatino to the castle.
   */
  public void collided(Particle p)
  {
    if (p instanceof Castle.Flag || p instanceof GoldMine.Flag)
    {
      if (state == STATE_FALLING)
      {
        if (p instanceof GoldMine.Flag && destination instanceof GoldMine.Flag)
        {
          // get the gold
          gold = ((GoldMine.Flag)p).consumeGold();

          // fly back to the castle
          destination = castle.getFlag();
        }
        else if (p instanceof Castle.Flag && destination instanceof Castle.Flag) // fly to a random gold mine
        {
          // drop off the gold
          if (gold > 0) getTeam().getWizard().increaseGold(gold);
          gold = 0;

          // make a list of the mines
          Vector mines = new Vector();
          for (int i=0; i<castle.getTeam().members(); i++)
          {
            Particle d = castle.getTeam().getMember(i);
            if (d instanceof GoldMine.Flag) mines.add(d);
          }

          // wait if there are no mines
          if (mines.size() == 0)
          {
            setVelocity(0,0,0);
            return;
          }

          // otherwise, pick one at random
          int num = (int) (Math.random() * mines.size());
          if (num == mines.size()) num = 0;
          destination = (Particle)mines.elementAt(num);
        }
        else return;

        setVelocity(0f, GameFrame.BALOON_SPEED, 0f);
        state = STATE_RISING;
      }
    }
  }
}