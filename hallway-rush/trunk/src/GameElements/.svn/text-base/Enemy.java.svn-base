package GameElements;

/**
 * Created by mertuygur on 22/12/14.
 */
public abstract class Enemy<T extends DynamicObject> extends DynamicObject {

    protected boolean isMob;
    protected int health;
    protected int damage;



    public boolean isMob() {

        return isMob;
    }

    public void setMob(boolean isMob) {
        this.isMob = isMob;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getHealth() {

        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }


    public void move() {
        y += velY;

    }
}
