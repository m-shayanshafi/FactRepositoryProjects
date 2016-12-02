package GameElements;

/**
 * Created by doga on 23/12/14.
 */
public class PowerUp {

    private PowerUpType type;
    final double duration;

    public PowerUp(PowerUpType type){
        this.type = type;
        this.duration = 0;
    }

    public PowerUpType getType(){
        return type;
    }

}
