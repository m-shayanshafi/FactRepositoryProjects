package enemyAI;

import java.util.Random;

import org.newdawn.slick.Color;

public class PlayerCreator{
	Random rand = new Random();
	public int PlayerA = 12;
	public int PlayerP = 0;
	public String PlayerG;
	public int PlayerGU;
	public int PlayerD;
	public String PlayerN = "";
	public int PlayerNN = 0;
	public Color PlayerC;
	public int PlayerSt = rand.nextInt(12) + 1;
	public int PlayerSp = rand.nextInt(12) + 1;
	public int PlayerSm = rand.nextInt(12) + 1;
	public int PlayerWs = rand.nextInt(12) + 1;
	public String[] EnemyN = new String[23];
	public int[] EnemyD = new int[23];
	public int[] EnemyP = new int[23];
	public int[] EnemyA = new int[23];
	public int[] EnemySt = new int[23];
	public int[] EnemySp = new int[23];
	public int[] EnemySm = new int[23];
	public int[] EnemyWs = new int[23];
	public Color[] EnemyC = new Color[23];
	public int i = 1;
//	district: 1 to 12
//	gender: 1 = boy, 2 = girl
	public void RunCode(){
		if(PlayerP == 0)
			makePlayer(rand.nextInt(24) + 1);
		if(PlayerP != 0)
			makePlayer(PlayerP);
		int categorize = 0;
		for (int p = 0; p < 23; p++){
			categorize++;
			if(categorize == PlayerP){
				categorize++;
				i = 2;
			}
			makeEnemy(categorize);
		}
	}
	public int makePlayer(int categorize){
		Player player = new Player(PlayerNN);
		player.PC = categorize;
		PlayerP = categorize;
		player.getInfo();
		PlayerG = player.convertGender();
		PlayerGU = player.WhichGender();
		PlayerD = player.WhichDistrict();
		PlayerN = player.name;
		PlayerC = player.getColor();
		return player.PC;
	}
	public void makeEnemy(int categorize){
		Enemy enemy = new Enemy(categorize);
		enemy.getInfo();
		EnemyC[categorize - i] = enemy.getColor();
		EnemyA[categorize - i] = enemy.age;
		EnemyP[categorize - i] = categorize;
		EnemyN[categorize - i] = enemy.name;
		EnemyD[categorize - i] = enemy.WhichDistrict();
		EnemySt[categorize - i] = rand.nextInt(12) + 1;
		EnemySp[categorize - i] = rand.nextInt(12) + 1;
		EnemySm[categorize - i] = rand.nextInt(12) + 1;
		EnemyWs[categorize - i] = rand.nextInt(12) + 1;
	}
	public boolean getGenderBoolean(int categorize){
		boolean gender = true;
		if(categorize % 2 == 0)gender = false;
		if(categorize % 2 != 0)gender = true;
		return gender;
	}
	public String getGenderString(int categorize){
		String gender = null;
		if(categorize % 2 == 0)gender = "Girl";
		if(categorize % 2 != 0)gender = "Boy";
		return gender;
	}
	public int getTotalSkill(int strength, int speed, int smarts, int weaponskill){
		int totalskill = (strength + speed + smarts + weaponskill) / 4;
		return totalskill;
	}
}