package Multiplayer;
import Actor.Actor;
import java.util.Vector;

/**
 * A struct to be sent between host and client that contains the two parties in a given battle
 * Created by piano_000 on 10/10/2015.
 */
public class BattleField {

    protected Vector<Actor> player;
    protected Vector<Actor> opponent;

    public BattleField(Vector<Actor> playerParty, Vector<Actor> enemyParty) {
        player = playerParty;
        opponent = enemyParty;
    }

    public Vector<Actor> getPlayer() {
        return player;
    }

    public Vector<Actor> getOpponent() {
        return opponent;
    }

    public void setPlayer(Vector<Actor> playerParty) {
        player = playerParty;
    }

    public void setOpponent(Vector<Actor> enemyParty) {
        opponent = enemyParty;
    }
}
