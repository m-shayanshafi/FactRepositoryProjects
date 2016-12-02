package enemyAI;

import org.newdawn.slick.Color;

public class Player extends enemyAI.PlayerCreator{
	public int PC;
	public int Age = PlayerA;
	public String name;
	public Player(int namenumber){
		PlayerNN = namenumber;
	}
	public void getInfo(){
		getName(PlayerNN);
		WhichDistrict();
		System.out.println(name + ": " + PC + " " + WhichDistrict() + " " + convertGender());
	}
	public int WhichGender(){
		int gender = 0;
		if(PC % 2 == 0)gender = 2;
		if(PC % 2 != 0)gender = 1;
		return gender;
	}
	public String convertGender(){
		String gender = null;
		if(WhichGender() == 2)gender = "girl";
		if(WhichGender() == 1)gender = "boy";
		return gender;
	}
	public void getName(int namenumber){
		NameReader reader = new NameReader();
		if(WhichGender() == 1){
			if(namenumber == 0)
				name = reader.getMaleName(0);
			if(namenumber != 0)
				name = reader.getMaleName(namenumber);
		}
		if(WhichGender() == 2){
			if(namenumber == 0)
				name = reader.getFemaleName(0);
			if(namenumber != 0)
				name = reader.getFemaleName(namenumber);
		}
	}
	public int WhichDistrict(){
		int district = PC / WhichGender();
		if(PC == 2)district = 1;
		if(PC == 3)district = 2;
		if(PC == 5)district = 3;
		if(PC == 7)district = 4;
		if(PC == 9)district = 5;
		if(PC == 11)district = 6;
		if(PC == 13)district = 7;
		if(PC == 15)district = 8;
		if(PC == 17)district = 9;
		if(PC == 19)district = 10;
		if(PC == 21)district = 11;
		if(PC == 23)district = 12;
		return district;
	}
	public Color getColor(){
		Color color = null;
		if(PC == 1)color = new Color(255, 0, 0);
		if(PC == 2)color = new Color(232, 17, 45);
		if(PC == 3)color = new Color(0, 158, 173);
		if(PC == 4)color = new Color(0, 162, 232);
		if(PC == 5)color = new Color(140, 214, 0);
		if(PC == 6)color = new Color(99, 176, 0);
		if(PC == 7)color = new Color(252, 209, 21);
		if(PC == 8)color = new Color(239, 170, 3);
		if(PC == 9)color = new Color(93, 138, 168);
		if(PC == 10)color = new Color(0, 48, 143);
		if(PC == 11)color = new Color(255, 28, 102);
		if(PC == 12)color = new Color(255, 22, 54);
		if(PC == 13)color = new Color(226, 166, 53);
		if(PC == 14)color = new Color(123, 63, 0);
		if(PC == 15)color = new Color(0, 255, 255);
		if(PC == 16)color = new Color(0, 183, 168);
		if(PC == 17)color = new Color(195, 176, 145);
		if(PC == 18)color = new Color(189, 183, 107);
		if(PC == 19)color = new Color(97, 46, 32);
		if(PC == 20)color = new Color(134, 89, 113);
		if(PC == 21)color = new Color(0, 114, 187);
		if(PC == 22)color = new Color(0, 35, 102);
		if(PC == 23)color = new Color(5, 144, 51);
		if(PC == 24)color = new Color(6, 198, 69);
		return color;
	}
}
