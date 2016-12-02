package net.sf.bloodball;
import de.vestrial.util.Range;
import java.awt.Point;
import net.sf.bloodball.gameflow.GameFlowController;
import net.sf.bloodball.model.FieldExtents;
import net.sf.bloodball.model.player.Player;
import net.sf.bloodball.model.player.Team;
public interface ModelFacade {
	final static Range PLAYING_ZONE = new Range(FieldExtents.HOME_SETUP_ZONE.getLowerBound(), FieldExtents.GUEST_SETUP_ZONE.getUpperBound());

	Point getBallPosition();
  
  Team getHomeTeam();

  Team getGuestTeam();
  
	int getProneTurns(Point square);

	Player getPlayerAt(Point square);

	boolean isGuestTeamPlayerAt(Point square);
	
	boolean isPlayerActive(Player player);

	boolean isHomeTeamPlayerAt(Point square);

	boolean isPlayerActiveAt(Point square);

	boolean isPlayerOffCallAt(Point square);

	boolean isPronePlayerAt(Point position);

	void setController(GameFlowController controller);
}