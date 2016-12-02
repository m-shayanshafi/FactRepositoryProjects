package net.sf.bloodball.model.player;

import net.sf.bloodball.resources.ResourceKeys;
import net.sf.bloodball.util.Dices;

public class Health {

  public static final Health OKAY = new Health(ResourceKeys.HEALTH_OKAY);
  public static final Health STUNNED = new Health(ResourceKeys.HEALTH_STUNNED) {
  	public Health getRecovery() {
      return OKAY;
    }
  };
  public static final Health KO = new Health(ResourceKeys.HEALTH_KO) {
  	public Health getRecovery() {
      return (Dices.D6.roll() < 4) ? STUNNED : OKAY;
    }
  };
  public static final Health INJURED = new Health(ResourceKeys.HEALTH_INJURED);
  public static final Health DEAD = new Health(ResourceKeys.HEALTH_DEAD);

  private String description;

  private Health(String description) {
    this.description = description;
  }
  
  public String toString() {
  	return description;
  }
  
  public Health getRecovery() {
  	return this;
  }

}