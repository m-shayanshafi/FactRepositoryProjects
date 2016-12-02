/*
 * This class is where all events, and story, take place. Starter calls events from here.
 * Ease-of-use methods are at the very bottom - the most commonly used being delay and clear, which we created.
 * TextIO (t) is the primary source of methods for text involving delays and such - as well as opening and closing
 * logos.
 * 
 */
import java.util.Scanner;
public class GameLogic
{
    TextIO t = new TextIO(); //new textIO for text input/output
    static final Character character = new Character(); //person you play as
    Enemy enemy; // generic enemy
    Entity victor = null; //for battle
    Battle battle; // new battle for events
    /*
     * Exposition explains the story so far and leads into the opening credits.
     */
    public void exposition(){
        t.dramatic(2500,"We are a necromancer.");
        t.dramatic(4000,"necromancy: a form of magic involving communication with the deceased – either by summoning their spirit as an apparition or raising them bodily");
        t.dramatic(3000,"We have surpassed life. Most do this only once."); 
        t.dramatic(3500,"We are not most.");
    }

    /*
     * Intro is the beginning of the game (expectably), and introduces the person to the game. The character does not
     * get to define his own stats. Such is life.
     */
    public void intro(){
        t.dramatic(1750,"Welcome.");
        t.dramatic(2750,"This game is meant to be played in fullscreen. Please maximize now.");
        //t.openingLogo();
    }

    public void chapterone(){
        //t.chapterOneLogo();
        t.dramatic(3500,"We awoke far, far away in lands strange and barren.");
        t.dramatic(3500,"We could feel the ancient power flowing through the sand and reverberating in the air.");
        t.dramatic(3500,"We saw the yellow sands stretch forever. It culminated near the east in a great earthen spire.");
        t.dramatic(3500,"We set off.");
        t.dramatic(3500,"The sands stirred a few feet ahead. We clutched our staff tightly.");
        t.dramatic(3500,"With a roar of rage, a draugr rose from the ground, holding a sceptre and prepared to attack.");
        t.dramatic(3500,"When we held power over the dead, we commanded legions of undead like this.");
        t.dramatic(3500,"With our power stolen, we must use other magic to quell our enemies.");                                    //dialogue
        enemy = new Enemy("Draugr", 80, new WeaponT2(), new ArmorT2(), new RingOfHealth(), new HatOfHealth());  // new enemy for first battle
        battle = new Battle(character, enemy);  //start battle
        while (victor == null) //while battle undecided
        {
            victor = battle.promptUser(); //next step
        }
        if (victor == character)
        {
            battle.collect(); //pick up dead persons items
        }
        else{
            outro(true); //you died, sucks to be you
        }
        victor = null; //reset for next battle
        Inventory.modify(character); //give chance to organize inventory
        t.dramatic(3500,"We sense the land's hostility as well. It seems to be controlled by another.");
        t.dramatic(3500,"The spire in the distance looked all the more interesting.");
        t.dramatic(3500,"A swath of desert lay between. We continued onwards.");
        t.dramatic(2500,"It seemed our presence was attracting the evils of the desert.");
        t.dramatic(2500,"We continued our trek.");
        t.dramatic(3000,"Taking a break at a dune closer to the spire, we examined it for a while.");
        t.dramatic(2500,"It started thickly, thinning near to the peak, where it culminated in a hall of earth.");
        t.dramatic(2500,"The whole spire grayed out towards near the top. No - the grey was spreading to the bottom, interestingly.");
        t.dramatic(3000,"We marched on, our footsteps droning on through the desert.");
        t.dramatic(2500,"As we approached the tower, we noticed the winds biting at us and the sand whipping fiercely against our gear.");
        t.dramatic(2500,"We walked closer, leaning forward and digging our heels in.");
        t.dramatic(2500,"The winds started to part, as a great pair of oaken doors became manifest from the air.");
        t.dramatic(2600,"The doors parted, creaking as they revealed a figure standing behind.");
        t.dramatic(3000,"We beheld a figure of clay and stone, clutching a great staff with energy glowing on its surface.");
        t.dramatic(2000,"We clutched our staff and approached.");
        enemy = new Enemy("Earth Guardian", 90, new WeaponT2(), new ArmorT3(), new RingOfHealth(), new HatOfHealth());
        System.out.println(enemy.stats.health);
        battle = new Battle(character, enemy);
        while (victor == null)
        {
            victor = battle.promptUser();
        }
        if (victor == character)
        {
            battle.collect();
        }
        else{
            outro(true);
        }
        Inventory.modify(character);
        t.dramatic(2500,"Something was satisfying about the way its remnants melded back into the ground.");
        t.dramatic(2500,"We walked into the room, letting the doors close behind us with a resonating thud.");
        t.dramatic(2500,"Surveying the room, we saw a collection of gear pinned up on the walls, emanating with magical energy.");
        t.dramatic(2500, "The floor shook, dust drifted down from the ceiling");
        t.dramatic(2500, "A massic rock fell from the ceiling, blocking the entrance");
        t.dramatic(2500, "We could not turn back, so we climbed out to the top");
        t.dramatic(2500, "A massive whirlwind had formed around the tower, surrounding us");
        t.dramatic(2500, "From this vortex descended an ephemeral shape... one that moved too quickly to see");
        t.dramatic(2500, "My staff crackled with energy... I knew not what it was, or why, but I knew I had to fight it.");
        enemy = new Enemy("Air Goddess", 100, new WeaponT4(), new ArmorT1(), new RingOfHealth(), new HatOfHealth());
        System.out.println(enemy.stats.health);
        battle = new Battle(character, enemy);
        victor= null;
        while (victor == null)
        {
            victor = battle.promptUser();
        }
        if (victor == character)
        {
            battle.collect();
        }
        else{
            outro(true);
        }
        Inventory.modify(character);
        t.dramatic(2500, "The withered body of the enemy lay before me... With its dying breath it touched my forehead with its... arm, if one could describe the appendage as such.");
        t.dramatic(3000, "I transcended reality and witnessed the glory of the higher planes.");
        t.dramatic(3000, "The universal truth... everything could be explained by one simple concept.");
        t.dramatic(3000, "Every philosopher, scientist, every mortal being had searched for this truth which now filled my being");
        t.dramatic(2500, "Unfortunately, I collapsed of blood loss at that point and suffered severe brain damage. The truth was lost to the mortal realm, but at least I'm still alive, right? Right?");
        t.dramatic(1000, "-> Continue playing \n   Quit");
        t.dramatic(2000,"   Continue playing \n-> Quit");
        t.clear();
        outro(false); //you didnt die so pass in false
        return;
    }

    public void chaptertwo(){
        //t.chapterTwoLogo(); //not in yet
    }

    public void chapterthree(){
        //t.chapterThreeLogo(); //same
    }

    public void outro(boolean lose){

        if (lose) { //lose is true if you died
            t.clear();
            t.dramatic(3000, "We've failed.");
            t.dramatic(2000, "Death is no stranger to us.");
            t.dramatic(3000, "Many times we've called Death to claim our enemies.");
            t.dramatic(3000, "How ironic.");
            t.dramatic(2000, "It's a shame you couldn't make it.");
            t.dramatic(3000, "But not all of us do.");
        }
        t.clear();
        t.dramatic(2000, "Thanks for playing.");
        t.clear();
        t.dramatic(3000, "          coded and designed by \n                          Anish Katukam and Sid Mani");
        System.exit(0);
    }
}
