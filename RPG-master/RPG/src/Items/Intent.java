package Items;

/**
 * Created by piano_000 on 5/24/2015.
 * HEAL refers to things that increase HP
 * HARM refers to things that decrease HP
 * RESTORE refers to things that increase TP
 * EQUIP refers to things that can be equipped by a character
 */
public enum Intent {
    HEAL(1), HARM(2), RESTORE(3), EQUIP(4);

    private int value;

    private Intent(int value) {
        this.value = value;
    }
}
