package enemyAI;

import org.newdawn.slick.Color;

public class Enemy extends PlayerCreator {
	public int enemy;
	public String name;
	public int age = rand.nextInt(7) + 12;
	public Enemy(int categorize){
		enemy = categorize;
	}
	public void getInfo(){
		getName(0);
		WhichDistrict();
		System.out.println(name + ": " + enemy + " " + WhichDistrict() + " " + convertGender());
	}
	public int whichGender(){
		if(enemy % 2 == 0)
			return 2;
		else
			return 1;
	}
	public void getName(int namenumber){
		NameReader reader = new NameReader();
		if(whichGender() == 1){
			if(namenumber == 0)
				name = reader.getMaleName(0);
			if(namenumber != 0)
				name = reader.getMaleName(namenumber);
		}
		if(whichGender() == 2){
			if(namenumber == 0)
				name = reader.getFemaleName(0);
			if(namenumber != 0)
				name = reader.getFemaleName(namenumber);
		}
	}
	public String convertGender(){
		String gender = null;
		if(whichGender() == 2)
			gender = "girl";
		if(whichGender() == 1)
			gender = "boy";
		return gender;
	}
	public int WhichDistrict(){
		switch (enemy){
			case 2:
				return 1;
			case 3:
				return 2;
			case 5:
				return 3;
			case 7:
				return 4;
			case 9:
				return 5;
			case 11:
				return 6;
			case 13:
				return 7;
			case 15:
				return 8;
			case 17:
				return 9;
			case 19:
				return 10;
			case 21:
				return 11;
			case 23:
				return 12;
			default:
				return enemy / whichGender();
		}
	}
	public Color getColor(){
		switch (enemy){
			case 1:
				return new Color(255, 0, 0);
			case 2:
				return new Color(232, 17, 45);
			case 3:
				return new Color(0, 158, 173);
			case 4:
				return new Color(0, 162, 232);
			case 5:
				return new Color(140, 214, 0);
			case 6:
				return new Color(99, 176, 0);
			case 7:
				return new Color(252, 209, 21);
			case 8:
				return new Color(239, 170, 3);
			case 9:
				return new Color(93, 138, 168);
			case 10:
				return new Color(0, 48, 143);
			case 11:
				return new Color(255, 28, 102);
			case 12:
				return new Color(255, 22, 54);
			case 13:
				return new Color(226, 166, 53);
			case 14:
				return new Color(123, 63, 0);
			case 15:
				return new Color(0, 255, 255);
			case 16:
				return new Color(0, 183, 168);
			case 17:
				return new Color(195, 176, 145);
			case 18:
				return new Color(189, 183, 107);
			case 19:
				return new Color(97, 46, 32);
			case 20:
				return new Color(134, 89, 113);
			case 21:
				return new Color(0, 114, 187);
			case 22:
				return new Color(0, 35, 102);
			case 23:
				return new Color(5, 144, 51);
			case 24:
				return new Color(6, 198, 69);
			default:
				return new Color(0, 0, 0);
		}
	}
}
