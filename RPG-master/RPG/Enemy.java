
public class Enemy extends Entity //Enemy is a type of Entity
{

    public Enemy(String n, int base, Weapon _weapon, Armor _armor, Ring _ring, Hat _hat) //Enemies are constructed with different types of items, so have them be explicit params
    {
        super(); //call to super constructor
        this.stats = new Stats(n,base); //create new stats object
        this.armor = _armor; //set items to explicit params
        this.weapon = _weapon;
        this.ring = _ring;
        this.hat = _hat;

    }

}
