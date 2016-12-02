// Player.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;


/** Player is a Wizard that is controlled by the human player. Spell selection
 * is handled here.
 */
public class Player extends Wizard
{
  // the spell that the player currently has selected
  private int currentSpell;

  public Player(TeamInfo team)
  {
    super(team);

    selectCastle();
  }

  /** The end game for the player. */
  public void endGame()
  {
    GameFrame.endGame();
  }

  protected javax.vecmath.Vector3f getLookVector()
  {
    return GameFrame.renderer.getCamera().getLookVector();
  }


  /** this is called every frame and is used to do input checking. Velocity of the player
   * is handled by the Renderer.Camera*/
  public void move(long time)
  {
    super.move(time);

    if (GameFrame.listener.LbuttonDown() && getLastCastTime() + GameFrame.CAST_TIME <= GameFrame.CURRENT_TIME)
    {
      castCurrentSpell();
      setLastCastTime(GameFrame.CURRENT_TIME);
    }

    if (GameFrame.listener.RbuttonDown() && !isSpeeding())
    {
      castSpeed();
    }

      // handle camera movement wrapping
    float size = GameFrame.renderer.getMap().getSize();
    if (z_pos > size/2) z_pos -= size;
    if (z_pos < -size/2) z_pos += size;
    if (x_pos > size/2) x_pos -= size;
    if (x_pos < -size/2) x_pos += size;

    GameFrame.renderer.getCamera().setCamPosition(x_pos, y_pos, z_pos);
  }

  /** Sets the x/z velocity. y is handled by Wizard.move()*/
  public void setVelocity(float x, float z)
  {
    x_vel = x;
    z_vel = z;
  }

  /** Sets the wizard's level. (0-4 inclusive) The level determines which spells can be cast.*/
  public void setLevel(int level)
  {
    super.setLevel(level);
    if (level < getLevel()) selectFirebolt();
    setSpellList();
  }

  /** This is called when the player wants to select the Castle spell. */
  public void selectCastle()
  {
    if (getLevel() < 0) return;

    currentSpell = SPELL_CASTLE;
    GameFrame.renderer.getHud().setCrosshair('^');
    GameFrame.renderer.getHud().setCurrentSpell("Selected: Castle");
    setSpellList();
  }

  /** This is called when the player wants to select the Firebolt spell. */
  public void selectFirebolt()
  {
    if (getLevel() < 0) return;

    currentSpell = SPELL_FIREBOLT;
    GameFrame.renderer.getHud().setCrosshair('o');
    GameFrame.renderer.getHud().setCurrentSpell("Selected: Firebolt");
    setSpellList();
  }

  /** This is called when the player wants to select the Claim spell. */
  public void selectClaim()
  {
    if (getLevel() < 1) return;

    currentSpell = SPELL_CLAIM;
    GameFrame.renderer.getHud().setCrosshair('*');
    GameFrame.renderer.getHud().setCurrentSpell("Selected: Claim");
    setSpellList();
  }

  /** This is called when the player wants to select the SummonSpider spell. */
  public void selectSummonSpider()
  {
    if (getLevel() < 2) return;

    currentSpell = SPELL_SPIDER;
    GameFrame.renderer.getHud().setCrosshair('M');
    GameFrame.renderer.getHud().setCurrentSpell("Selected: SummonSpider");
    setSpellList();
  }

  /** This is called when the player wants to select the Summon Firestalk spell. */
  public void selectSummonFirestalk()
  {
    if (getLevel() < 4) return;

    currentSpell = SPELL_FIRESTALK;
    GameFrame.renderer.getHud().setCrosshair('I');
    GameFrame.renderer.getHud().setCurrentSpell("Selected: Summon Firestalk");
    setSpellList();
  }

  /** This is called when the player wants to select the Firewave spell. */
  public void selectFirewave()
  {
    if (getLevel() < 3) return;

    currentSpell = SPELL_FIREWAVE;
    GameFrame.renderer.getHud().setCrosshair('~');
    GameFrame.renderer.getHud().setCurrentSpell("Selected: Firewave");
    setSpellList();
  }

  /** This is called when the player wants to select the Meteor spell. */
  public void selectMeteor()
  {
    if (getLevel() < 5) return;

    currentSpell = SPELL_METEOR;
    GameFrame.renderer.getHud().setCrosshair('@');
    GameFrame.renderer.getHud().setCurrentSpell("Selected: Meteor");
    setSpellList();
  }

  /** This is called when the player wants to select the next available spell. */
  public void selectNextSpell()
  {
    if (currentSpell == SPELL_CASTLE) selectFirebolt();
    else if (currentSpell == SPELL_FIREBOLT)
    {
      if (getLevel() < 1) selectCastle();
      else selectClaim();
    }
    else if (currentSpell == SPELL_CLAIM)
    {
      if (getLevel() < 2) selectCastle();
      else selectSummonSpider();
    }
    else if (currentSpell == SPELL_SPIDER)
    {
      if (getLevel() < 4) selectCastle();
      else selectFirewave();
    }
    else if (currentSpell == SPELL_FIREWAVE)
    {
      if (getLevel() < 4) selectCastle();
      else selectSummonFirestalk();
    }

    else if (currentSpell == SPELL_FIRESTALK)
    {
      if (getLevel() < 5) selectCastle();
      else selectMeteor();
    }
    else if (currentSpell == SPELL_METEOR) selectCastle();
    else System.out.println("SPELL SELECTION ERROR");

    setSpellList();
  }

  /** This is called when the player wants to select the prior available spell. */
  public void selectPreviousSpell()
  {
    if (currentSpell == SPELL_METEOR) selectSummonFirestalk();
    else if (currentSpell == SPELL_FIRESTALK) selectFirewave();
    else if (currentSpell == SPELL_FIREWAVE)selectSummonSpider();
    else if (currentSpell == SPELL_SPIDER) selectClaim();
    else if (currentSpell == SPELL_CLAIM) selectFirebolt();
    else if (currentSpell == SPELL_FIREBOLT) selectCastle();
    else if (currentSpell == SPELL_CASTLE)
    {
      if (getLevel() == 0) selectFirebolt();
      else if (getLevel() == 1) selectClaim();
      else if (getLevel() == 2) selectSummonSpider();
      else if (getLevel() == 3) selectFirewave();
      else if (getLevel() == 4) selectSummonFirestalk();
      else if (getLevel() == 5) selectMeteor();
    }
    else System.out.println("SPELL SELECTION ERROR");

    setSpellList();
  }

  // this sets the spell list
  private void setSpellList()
  {
    StringBuffer list = new StringBuffer("");

    if (currentSpell == SPELL_CASTLE) list.append(" -^-");
    else list.append("  ^ ");
    if (currentSpell == SPELL_FIREBOLT) list.append(" -o-");
    else list.append("  o ");

    if (getLevel() >=1)
    {
      if (currentSpell == SPELL_CLAIM) list.append(" -*-");
      else list.append("  * ");
    }
    if (getLevel() >= 2)
    {
      if (currentSpell == SPELL_SPIDER) list.append(" -M-");
      else list.append("  M ");
    }
    if (getLevel() >= 3)
    {
      if (currentSpell == SPELL_FIREWAVE) list.append(" -~-");
      else list.append("  ~ ");
    }
    if (getLevel() >= 4)
    {
      if (currentSpell == SPELL_FIRESTALK) list.append(" -I-");
      else list.append("  I ");
    }
    if (getLevel() >= 5)
    {
      if (currentSpell == SPELL_METEOR) list.append(" -@-");
      else list.append("  @ ");
    }

    GameFrame.renderer.getHud().setSpells(list.toString());
  }

  /** Set the Player's gold. */
  public void setGold(int gold)
  {
    super.setGold(gold);
    GameFrame.renderer.getHud().setGold(getGold());
  }

  /** Set the Player's mana. */
  public void setMana(int mana)
  {
    super.setMana(mana);
    GameFrame.renderer.getHud().setMana(getMana(), getMaxMana());
  }

  /** Set the Player's health. */
  public void setHealth(int health)
  {
    super.setHealth(health);
    GameFrame.renderer.getHud().setHealth(getHealth());
  }

  /** checks to see if the upgrade indicator should be displayed */
  public void checkGold()
  {
    if (getTeam().getCastle() == null)
    {
      GameFrame.renderer.getHud().showUpgradeIndicator();
    }
    else
    {
      if ((getTeam().getCastle().getLevel() == 1 && getGold() >= GameFrame.CASTLE_LEVEL2COST) ||
          (getTeam().getCastle().getLevel() == 2 && getGold() >= GameFrame.CASTLE_LEVEL3COST) ||
          (getTeam().getCastle().getLevel() == 3 && getGold() >= GameFrame.CASTLE_LEVEL4COST) ||
          (getTeam().getCastle().getLevel() == 4 && getGold() >= GameFrame.CASTLE_LEVEL5COST))
        GameFrame.renderer.getHud().showUpgradeIndicator();
      else GameFrame.renderer.getHud().hideUpgradeIndicator();
    }
  }


  /**  This is called when the player wants to fire his spell. */
  public void castCurrentSpell()
  {
    switch (currentSpell)
    {
      case SPELL_METEOR:    castMeteor();          break;
      case SPELL_FIRESTALK: castSummonFirestalk(); break;
      case SPELL_FIREWAVE:  castFirewave();        break;
      case SPELL_SPIDER:    castSummonSpider();    break;
      case SPELL_CLAIM:     castClaim();           break;
      case SPELL_FIREBOLT:  castFirebolt();        break;
      case SPELL_CASTLE:    castCastle();          break;
      default: System.out.println("Spell selection error, spell " + currentSpell + " not found.");
    }
  }
}

