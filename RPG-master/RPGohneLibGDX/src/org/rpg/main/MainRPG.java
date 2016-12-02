package org.rpg.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.rpg.attacks.DataAttacks;
import org.rpg.attacks.DataAttacks.AttackE;
import org.rpg.entitys.DataEntity;
import org.rpg.entitys.DataEntity.EntityE;
import org.rpg.read.ReadEntity;

public class MainRPG {
	public static void main(String[] args) throws ClassNotFoundException {

		File file = new File(System.getProperty("user.home") + "/.rpg/Entitys");
		file.mkdirs();

		String goodS = "Entchen";
		EntityE good = ReadEntity.readEntity(goodS);
		EntityE bad = EntityE.Hero;
		EntityE winner = null;
		int hpg = ReadEntity.readHP(goodS);
		int hpb = DataEntity.getHP(bad);
		int attackg = DataEntity.getAttack(good);
		int attackb = DataEntity.getAttack(bad);
		int attackCompleteg = 0;
		int attackCompleteb = 0;
		int defg = DataEntity.getDef(good);
		int defb = DataEntity.getDef(bad);
		boolean turn = true;
		int round = 0;
		AttackE[] attacksg = ReadEntity.readAttacks(goodS);
		AttackE[] attacksb = DataEntity.getAttacks(bad);
		AttackE Attackg = null;
		AttackE Attackb = null;
		System.out.println("Anfang");
		System.out.println("HP von " + good.name() + ": " + hpg);
		System.out.println("HP von " + bad.name() + ": " + hpb);
		while (hpg > 0 && hpb > 0) {
			if (turn) {
				// Good greift an.
				int decisiong = 0;
				System.out.println("(1)Angreifen\t(2)Item\n(3)Kämpfer\t(4)Fliehen");
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
					decisiong = Integer.parseInt(reader.readLine());
				} catch (IOException e) {
				}
				switch (decisiong) {
				case 1:
					System.out.println("\n(1)" + attacksg[0].name() + "\t(2)" + attacksg[1].name() + "\n(3)" + attacksg[2].name() + "\t(4)" + attacksg[3].name());
					int decisionAttack = 0;
					try {
						BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
						decisionAttack = Integer.parseInt(reader.readLine());
					} catch (IOException e) {

					}
					switch (decisionAttack) {
					case 1:
						Attackg = attacksg[0];
						break;
					case 2:
						Attackg = attacksg[1];
						break;
					case 3:
						Attackg = attacksg[2];
						break;
					case 4:
						Attackg = attacksg[3];
						break;
					}
					int Accuracy = DataAttacks.getAccuracy(Attackg);
					int random = (int) (Math.random() * 100);
					System.out.println(random);
					if (random < Accuracy){
					attackCompleteg = attackg * (int)((double)DataAttacks.getAttack(Attackg) / (double)100) - (defb / 2);
					if (attackCompleteg < 0) {
						attackCompleteg = 0;
					}
					hpb = hpb - attackCompleteg;
					if (hpb < 0) {
						hpb = 0;
					}
					} else {
						System.out.println("Leider Fehlgeschlagen");
					}
					break;
				case 2:

					break;
				case 3:

					break;
				case 4:

					break;
				default:
					System.out.println("Kannst du nicht bis 4 zählen?");
					break;
				}
			} else {
				// Bad greift an.

				int decisionb = 0;
				System.out.println("(1)Angreifen\t(2)Item\n(3)Kämpfer\t(4)Fliehen");
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
					decisionb = Integer.parseInt(reader.readLine());
					System.out.println(decisionb);
				} catch (IOException e) {
					e.printStackTrace();
				}
				switch (decisionb) {
				case 1:
					attackCompleteb = attackb - (defg / 2);
					if (attackCompleteb < 0) {
						attackCompleteb = 0;
					}
					hpg = hpg - attackCompleteb;
					if (hpg < 0) {
						hpg = 0;
					}
					break;
				case 2:

					break;
				case 3:

					break;
				case 4:
					
					break;
				default:
					System.out.println("Kannst du nicht bis 4 zählen");
					break;
				}
				round++;
				System.out.println("\nRunde: " + round);
				System.out.println("HP von " + good.name() + ": " + hpg);
				System.out.println("HP von " + bad.name() + ": " + hpb);
			}
			turn = !turn;
		}
		if (hpg == 0) {
			winner = bad;
		} else {
			winner = good;
		}

		System.out.println("----------------------\n" + winner.name() + " hat gewonnen");
	}
}
