package Game;

/**
 * Created by piano_000 on 6/1/2015.
 */
public class Status {
    private State state;
    private int duration; //A duration of -1 is meant to last forever only applies for noraml and fainted status.

    public Status(State value) {
        this.state = value;
    }

    public Status(State value, int duration) {
        this.state = value;
        this.duration = duration;
    }

    public enum State {
        NORMAL(1), SLEEPING(2), PARALYZED(3), FROZEN(4), GUARDING(5), FAINTED(6);
        private int value;

        State(int value) {
            this.value = value;
        }
    }

    public State getState() {
        return state;
    }

    public int getDuration() {
        return duration;
    }

    public void setState(State t, int d) {
        state = t;
        duration = d;
    }

    public void elapse(int numberOfTurns) {
        duration = duration - numberOfTurns;
    }
}
