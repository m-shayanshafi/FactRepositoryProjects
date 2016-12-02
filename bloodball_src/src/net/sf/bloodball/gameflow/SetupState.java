package net.sf.bloodball.gameflow;

import java.awt.Point;
import net.sf.bloodball.model.actions.Setup;
import net.sf.bloodball.model.player.Player;

public abstract class SetupState extends State {
  private Setup setup;

  public SetupState(GameFlowController context) {
    super(context);
    setup = new Setup(context.getGame());
  }

  protected void putPlayer(Point position, Player player) {
    setup.setupPlayer(position, player);
  }

  protected void removePlayer(Point position) {
    setup.removePlayer(position);
    setMayEndTurn(false);
  }

  public Setup getSetup() {
    return setup;
  }

  protected void setSetup(Setup setup) {
    this.setup = setup;
  }

}