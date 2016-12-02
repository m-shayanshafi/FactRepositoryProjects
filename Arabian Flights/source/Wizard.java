// Wizard.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import javax.vecmath.*;

/** The Wizard class encompases both player characters and AI wizard characters. Wizards
 * cast spells, summon monsters, have castles, and claim gold mines. Wizards have five levels: 0-4.
 * Each level bestows spells and dictates the size and configuration of the castle, as well as the
 * number of baloons and the maximum mana that wizard has.
 */

public abstract class Wizard extends Poseable
{
  public final static int SPELL_CASTLE = 1;
  public final static int SPELL_CLAIM = 2;
  public final static int SPELL_SPIDER = 3;
  public final static int SPELL_FIREBOLT = 4;
  public final static int SPELL_FIRESTALK = 7;
  public final static int SPELL_FIREWAVE = 8;
  public final static int SPELL_METEOR = 9;

  // this is how long the speed spell lasts, in ms
  private static final long speedTime = 600;

  private float fallrate = 0;

  // this is the rest time between spell casts, in ms
  private static long lastCastTime = 0;

  // the last time it touched a rolling particle
  private long touchtime = 0;

  private boolean speeding;
  private long speedStartTime;

  private int gold = 0;
  private int mana = 0;
  private int maxmana = 0;

  private int level = 0;

  private static long regentime = 1000;
  private long lastregentime = 0;

  private TeamInfo team;

  public Wizard(TeamInfo team)
  {
    this.team = team;
    team.setWizard(this);
    team.addMember(this);
    this.setScale(GameFrame.WIZARD_SCALE);
  }
  public void move(long time)
  {
    super.move(time);

    if (isSpeeding() && GameFrame.CURRENT_TIME > speedStartTime + speedTime) speeding = false;

    if (lastregentime + regentime < GameFrame.CURRENT_TIME)
    {
      regenerate();
      lastregentime = GameFrame.CURRENT_TIME;
    }
    // keep the wizard above the landscape, and make it float down
    float tileheight = GameFrame.renderer.getMap().getHeightAt(x_pos, z_pos) + GameFrame.WIZARD_FLOAT_HEIGHT;
    if (y_pos <= tileheight || GameFrame.CURRENT_TIME <touchtime + GameFrame.CHECK_PERIOD)
    {
      // the wizard doesn't fall when it is touching a rolling particle.
      if (GameFrame.CURRENT_TIME > touchtime + GameFrame.CHECK_PERIOD)
        y_pos = tileheight;
      fallrate = 0;
    }
    else
    {
      fallrate -= GameFrame.WIZARD_FLOAT_FALL_RATE * time / 1000f;
      y_pos += fallrate;
    }
  }

  /** when it touches a rolling particle, the wizard should stay on top of it.*/
  public void collided(Particle particle)
  {
    if (particle.getType() == Particle.TYPE_ROLLING)
    {
      touchtime = GameFrame.CURRENT_TIME;
      y_pos = (particle.y_pos + particle.getScale() + this.getScale()*0.9f);
    }
  }

  public void regenerate()
  {
    if (!GameFrame.physics.isFrozen())
    {
      increaseHealth(GameFrame.REGEN_HEALTH_RATE);
      increaseMana(GameFrame.REGEN_MANA_RATE);
    }
  }

  /** returns this wizard's team. */
  public TeamInfo getTeam()
  {
    return team;
  }

  /** This is called every time a wizard gains or loses gold. */
  public void checkGold()
  {

  }

  /** Sets the wizard's level. (0-4 inclusive) The level determines which spells can be cast.*/
  public void setLevel(int level)
  {
    this.level = level;

    if (level == 0) setMaxMana(75);
    else if (level == 1) setMaxMana(100);
    else if (level == 2) setMaxMana(125);
    else if (level == 3) setMaxMana(150);
    else if (level == 4) setMaxMana(200);
    else if (level == 5) setMaxMana(300);
    else System.out.println("ERROR: Bad Level: " + level);
  }

  /** Gets the wizard's level. The level determines which spells can be cast.*/
  public int getLevel()
  {
    return level;
  }

  /** Set the wizard's health. Note, the player's health is constrained
   * between 0 and 100. */
  public void setHealth(int health)
  {
    if (health > 100) health = 100;
    if (health < 0) health = 0;
    if (health == 0) destroy();

    super.setHealth(health);
  }

  /** Set the wizard's gold. */
  public void setGold(int gold)
  {
    this.gold = gold;
    checkGold();
  }

  /** Get the wizard's gold. */
  public int getGold()
  {
    return gold;
  }

  /** Increase the wizard's gold. */
  public void increaseGold(int amount)
  {
    setGold(gold + amount);
  }

  /** Decrease the wizard's gold. */
  public void decreaseGold(int amount)
  {
    setGold(gold - amount);
  }

  /** Set the wizard's Maximum mana. */
  public void setMaxMana(int maxmana)
  {
    this.maxmana = maxmana;
  }

  /** Set the wizard's mana. Note, the mana is constrained between
   * 0 and the Maximum Mana.
   */
  public void setMana(int mana)
  {
    if (mana < 0) mana = 0;
    if (mana > maxmana) mana = maxmana;

    this.mana = mana;
  }

  /** Get the wizard's Maximum Mana. */
  public int getMaxMana()
  {
    return maxmana;
  }

  /** Get the wizard's Mana. */
  public int getMana()
  {
    return mana;
  }

  /** Increase the wizard's mana. */
  public void increaseMana(int amount)
  {
    setMana(mana + amount);
  }

  /** Decrease the wizard's mana. */
  public void decreaseMana(int amount)
  {
    setMana(mana - amount);
  }

  /** do something when the castle is damaged */
  public void castleDamaged()
  {

  }

  /** When the wizard is destroyed, it is returned to it's castle. */
  public void destroy()
  {
    if (getTeam().getCastle() == null)
    {
      // remove the wizard from the game
      endGame();
    }
    else
    {
      // respawn him
      setHealth(100);
      setMana(0);
      setPosition(getTeam().getCastle().getFlag());

      GameFrame.sound.playSound(GameFrame.SOUND_HUM, getTeam().getCastle().getFlag());
    }
  }

  /** This removes the wizard from the game. */
  public void endGame()
  {
    super.destroy();
  }

  protected abstract Vector3f getLookVector();


  protected void castCastle()
  {
    if (getLevel() < GameFrame.SPELL_CASTLE_LEVEL) return;

    if (getMana() < GameFrame.SPELL_CASTLE_MANA_COST) return;
    decreaseMana(GameFrame.SPELL_CASTLE_MANA_COST);

    GameFrame.sound.playSound(GameFrame.SOUND_WHOOSH, this);
    CastleSpell spell = new CastleSpell(this, getLookVector(), getTeam());
    GameFrame.physics.addParticle(spell);

    System.out.println("castle cast");
  }

  protected void castClaim()
  {
    if (getLevel() < GameFrame.SPELL_CLAIM_LEVEL) return;

    if (getMana() < GameFrame.SPELL_CLAIM_MANA_COST) return;
    decreaseMana(GameFrame.SPELL_CLAIM_MANA_COST);

    GameFrame.sound.playSound(GameFrame.SOUND_WHOOSH, this);
    ClaimSpell spell = new ClaimSpell(this, getLookVector(), getTeam());
    GameFrame.physics.addParticle(spell);
  }

  protected void castFirebolt()
  {
    if (getLevel() < GameFrame.SPELL_FIREBOLT_LEVEL) return;

    if (getMana() < GameFrame.SPELL_FIREBOLT_MANA_COST) return;
    decreaseMana(GameFrame.SPELL_FIREBOLT_MANA_COST);

    GameFrame.sound.playSound(GameFrame.SOUND_WHOOSH, this);
    FireboltSpell spell = new FireboltSpell(this, getLookVector(), getTeam(), GameFrame.FIREBOLT_DAMAGE);
    GameFrame.physics.addParticle(spell);
  }

  protected void castFirewave()
  {
    if (getLevel() < GameFrame.SPELL_FIREWAVE_LEVEL) return;

    if (getMana() < GameFrame.SPELL_FIREWAVE_MANA_COST) return;
    decreaseMana(GameFrame.SPELL_FIREWAVE_MANA_COST);

    GameFrame.sound.playSound(GameFrame.SOUND_WHOOSH, this);

    // throw a bunch of spells in slightly different directions
    for (int i=0; i<GameFrame.FIREWAVE_COUNT; i++)
    {
      Vector3f direction = getLookVector();
      direction.add(new Vector3f((float)(Math.random()-.5)*GameFrame.FIREWAVE_SPREAD,
                                 (float)(Math.random()-.5)*GameFrame.FIREWAVE_SPREAD,
                                 (float)(Math.random()-.5)*GameFrame.FIREWAVE_SPREAD));
      direction.normalize();
      FirewaveSpell spell = new FirewaveSpell(this, direction, getTeam(), GameFrame.FIREWAVE_DAMAGE);
      GameFrame.physics.addParticle(spell);
    }
  }

  protected void castMeteor()
  {
    if (getLevel() < GameFrame.SPELL_METEOR_LEVEL) return;

    if (getMana() < GameFrame.SPELL_METEOR_MANA_COST) return;
    decreaseMana(GameFrame.SPELL_METEOR_MANA_COST);

    GameFrame.sound.playSound(GameFrame.SOUND_WHOOSH, this);
    MeteorSpell spell = new MeteorSpell(this, getLookVector(), getTeam(), GameFrame.METEOR_DAMAGE);
    GameFrame.physics.addParticle(spell);
  }

  protected void castSpeed()
  {
    if (getLevel() < GameFrame.SPELL_SPEED_LEVEL) return;

    if (getMana() < GameFrame.SPELL_SPEED_MANA_COST) return;
    decreaseMana(GameFrame.SPELL_SPEED_MANA_COST);

    speeding = true;
    speedStartTime = GameFrame.CURRENT_TIME;
  }

  protected void castSummonFirestalk()
  {
    if (getLevel() < GameFrame.SPELL_FIRESTALK_LEVEL) return;

    if (getMana() < GameFrame.SPELL_FIRESTALK_MANA_COST || getGold() < GameFrame.SPELL_FIRESTALK_GOLD_COST) return;
    decreaseMana(GameFrame.SPELL_FIRESTALK_MANA_COST);
    decreaseGold(GameFrame.SPELL_FIRESTALK_GOLD_COST);

    GameFrame.sound.playSound(GameFrame.SOUND_WHOOSH, this);
    SummonFirestalkSpell spell = new SummonFirestalkSpell(this, getLookVector(), getTeam());
    GameFrame.physics.addParticle(spell);
  }

  protected void castSummonSpider()
  {
    if (getLevel() < GameFrame.SPELL_SPIDER_LEVEL) return;

    if (getMana() < GameFrame.SPELL_SPIDER_MANA_COST || getGold() < GameFrame.SPELL_SPIDER_GOLD_COST) return;
    decreaseMana(GameFrame.SPELL_SPIDER_MANA_COST);
    decreaseGold(GameFrame.SPELL_SPIDER_GOLD_COST);

    GameFrame.sound.playSound(GameFrame.SOUND_WHOOSH, this);
    SummonSpiderSpell spell = new SummonSpiderSpell(this, getLookVector(), getTeam());
    GameFrame.physics.addParticle(spell);
  }

  /** returns true if the player is under the effects of the speed spell. */
  public boolean isSpeeding() { return speeding; }

  public long getLastCastTime() { return lastCastTime; }
  public void setLastCastTime(long time) { lastCastTime = time; }

}