public class Character extends Entity //Character is a type of Entity
{
    public Character()
    {
        super(); //call to the super constructor
        this.stats = new Stats("The Necromancer"); //create stats object, pass in name
        this.armor = new ArmorT1(); //create instance of lowest level of each item
        this.weapon = new WeaponT1();
        this.ring = new RingOfHealth();
        this.hat = new HatOfHealth();
        
    }
}
