package Game;

/**
 * Created by piano_000 on 6/1/2015.
 */
public enum Illness {
    NONE(1), POISON(2), FREEZING(3), BURNING(4);

    private int value;
    protected int duration;

    private Illness(int value) {
        this.value = value;
    }
    /*
    Not at all confident in this implementation
     */
    public void setDuration(int d) {
        this.duration = d;
    }

    public int getDuration() {
        return this.duration;
    }
}
