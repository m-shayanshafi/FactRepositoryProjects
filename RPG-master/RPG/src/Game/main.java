package Game;

import java.util.Vector;

import Actor.*;
import FightClasses.*;
import Items.*;
import Skills.*;

public class main {

	public static void main(String[] args) {
		 /*
		 * This is just a testing ground for methods and different functionality
		 */
		
		CombatHandler BATTLE = new CombatHandler();
		TextHandler out = TextHandler.getInstance();

		out.printToConsole("Text Handler from Main: " + System.identityHashCode(out));
		Hero Link = new Hero("Link", new Knight());
		Hero Zelda = new Hero("Zelda", new Wizard());
		Hero Ganon = new Hero("Ganon", new BanditKing());
		
		out.printToConsole("Link Health: " + Link.getHealth());
		out.printToConsole("Link dmg mod: " + Link.getDamageMod());
		out.printToConsole("Zelda Health: " + Zelda.getHealth());
		out.printToConsole("Zelda dmg mod: " + Zelda.getDamageMod());
		
		Sword woodenSword = new Sword("Wooden Sword", "Just a sword made of wood", false);
		Bow woodenBow = new Bow("Wooden Bow", "A nice wooden bow", true);

		Link.setWeapon(woodenSword);
		Zelda.setWeapon(woodenBow);
		Ganon.setWeapon(woodenSword);

		Link.addSkill(new FireBreath());

		Vector<Actor> party = new Vector<>();
		party.add(Link);
		party.add(Zelda);
		party.add(Ganon);
		
		Vector<Actor> enemies = new Vector<>();

		Guard g1 = new Guard(); enemies.add(g1);
		Guard g2 = new Guard(); enemies.add(g2);
		Guard g3 = new Guard(); enemies.add(g3);

		//Goblin g1 = new Goblin();
		//Goblin g2 = new Goblin();
		//Hobgoblin h1 = new Hobgoblin();
		/*
		Club megaclub = new Club("Wooden Club", "A club", false);
		Lightning zap = new Lightning(new BaseAugment());
		megaclub.setAugment(zap);
		
		h1.setWeapon(megaclub);
		
		//enemies.add(g1);
		enemies.add(h1);
		//enemies.add(g2);

*/
		GreenHerb greenHerb = new GreenHerb();
		RedHerb redHerb = new RedHerb();
		Bomb bomb = new Bomb();
		g1.getItems().add(greenHerb);
		Link.getItems().add(greenHerb);
		Zelda.getItems().add(redHerb);
		Zelda.getItems().add(bomb);

		out.printToConsole("LINK INV:");
		Link.printInventory();
		out.printToConsole("ZELDA INV:");
		Zelda.printInventory();

		TripleStrike ts = new TripleStrike();
		Heal heals = new Heal();
		Fireball fb = new Fireball();
		Link.addSkill(new TripleStrike());
		Link.addSkill(new Fireball());
		Zelda.addSkill(heals);
		WeakHealAll wha = new WeakHealAll();
		Zelda.addSkill(wha);

		BATTLE.battleStart(party, enemies);
		/*out.printToConsole("BATTLE START!");
		while(Zelda.getHealth() > 0 && Link.getHealth() > 0) {

			Zelda.use(Link);
			Zelda.printStatus();
			Link.printStatus();
			Link.use(Zelda);
			Zelda.printStatus();
			if(Zelda.getHealth() < 10 && Zelda.getItems().contains(redHerb)) {
				Zelda.getItems().get(0).use(Zelda, Zelda);
			}
			if(Link.getHealth() < 10 && Link.getItems().contains(greenHerb)) {
				Link.getItems().get(0).use(Link, Link);
			}
			Link.printStatus();
			Zelda.useSpecial(Link);
			Zelda.printStatus();
			Link.printStatus();
			Link.useSpecial(Zelda);

			if(Zelda.getHealth() < 10 && !Zelda.getItems().contains(redHerb)) {
				Zelda.getItems().get(0).use(Zelda, Link);
			}
		}

		Link.printInventory();
		Zelda.printInventory();*/
	}
}
