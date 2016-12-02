package arena;

import java.util.Random;

import org.newdawn.slick.Color;

public class RandomTerrainGenerator{
	Random rand = new Random();
	public boolean[] biome = new boolean[8];
	public byte[] biomeint = new byte[4];
	public Color[] shading = new Color[5];
	public byte[] buildings = new byte[64];
	public byte blank = 0;
	public byte lakes = 0;
	public byte supplyd = 0;
	public byte killc = 0;
	public byte sleepp = 0;
	private byte[] between = new byte[4];
//	biomes: 1 = plains, 2 = temperate forest, 3 = jungle, 4 = hot desert, 5 =  taiga, 6 = mountain range, 7 = city
//	what biomes do: 1: +1 sight, 2: ~2* chance of lake, 3: -1 sight, 4: 4 days before dead of water, 5: die of cold after 7 days, 6: no kill cards, 7: ~2* supply drops
	public void getBiomes(){
		for(byte random = 0, pos = 1, biomecount = 0; biomecount < 4; biomecount++){
			random = (byte) (rand.nextInt(7) + 1);
			if(biome[random] == true)
				biomecount--;
			if(biome[random] == false){
				biome[random] = true;
				Biome biome = new Biome(random, pos);
				shading[pos] = biome.getShading();
				biomeint[pos - 1] = biome.Biome;
				pos++;
			}
		}
	}
//	0 = blank, 1 = lake, 2 = supply drop, 3 = kill card, 4 = sleep place
	public void getBuildings(){
		between[0] = 7;
		between[1] = 14;
		between[2] = 24;
		between[3] = 28;
		for(int i = 0; i < 64; i++){
			int building = rand.nextInt(5);
			if(building <= 2)buildings[i] = 0;
			if(building > 2){
				building = rand.nextInt(29);
				if(building <= between[0])buildings[i] = 1;
				if(building > between[0] && building <= between[1])buildings[i] = 2;
				if(building > between[1] && building <= between[2])buildings[i] = 4;
				if(building > between[2] && building <= between[3])buildings[i] = 3;
				if(i >= 0 && i <= 15){
					if(biomeint[0] == 2){
						between[0] = 14;
						between[1] = 18;
						between[2] = 23;
						between[3] = 26;
						if(building <= between[0])buildings[i] = 1;
						if(building > between[0] && building <= between[1])buildings[i] = 2;
						if(building > between[1] && building <= between[2])buildings[i] = 4;
						if(building > between[2] && building <= between[3])buildings[i] = 3;
						if(building > between[3])buildings[i] = 1;
					}
					if(biomeint[0] == 6){
						if(buildings[i] == 3)buildings[i] = 0;
					}
					if(biomeint[0] == 7){
						between[0] = 4;
						between[1] = 18;
						between[2] = 23;
						between[3] = 26;
						if(building <= between[0])buildings[i] = 1;
						if(building > between[0] && building <= between[1])buildings[i] = 2;
						if(building > between[1] && building <= between[2])buildings[i] = 4;
						if(building > between[2] && building <= between[3])buildings[i] = 3;
						if(building > between[3])buildings[i] = 1;
					}
					else{
						between[0] = 7;
						between[1] = 14;
						between[2] = 24;
						between[3] = 28;
					}
				}
				if(i >= 16 && i <= 31){
					if(biomeint[1] == 2){
						between[0] = 14;
						between[1] = 18;
						between[2] = 23;
						between[3] = 26;
						if(building <= between[0])buildings[i] = 1;
						if(building > between[0] && building <= between[1])buildings[i] = 2;
						if(building > between[1] && building <= between[2])buildings[i] = 4;
						if(building > between[2] && building <= between[3])buildings[i] = 3;
						if(building > between[3])buildings[i] = 1;
					}
					if(biomeint[1] == 6){
						if(buildings[i] == 3)buildings[i] = 0;
					}
					if(biomeint[1] == 7){
						between[0] = 4;
						between[1] = 18;
						between[2] = 23;
						between[3] = 26;
						if(building <= between[0])buildings[i] = 1;
						if(building > between[0] && building <= between[1])buildings[i] = 2;
						if(building > between[1] && building <= between[2])buildings[i] = 4;
						if(building > between[2] && building <= between[3])buildings[i] = 3;
						if(building > between[3])buildings[i] = 1;
					}
					else{
						between[0] = 7;
						between[1] = 14;
						between[2] = 24;
						between[3] = 28;
					}
				}
				if(i >= 32 && i <= 47){
					if(biomeint[2] == 2){
						between[0] = 14;
						between[1] = 18;
						between[2] = 23;
						between[3] = 26;
						if(building <= between[0])buildings[i] = 1;
						if(building > between[0] && building <= between[1])buildings[i] = 2;
						if(building > between[1] && building <= between[2])buildings[i] = 4;
						if(building > between[2] && building <= between[3])buildings[i] = 3;
						if(building > between[3])buildings[i] = 1;
					}
					if(biomeint[2] == 6){
						if(buildings[i] == 3)buildings[i] = 0;
					}
					if(biomeint[2] == 7){
						between[0] = 4;
						between[1] = 18;
						between[2] = 23;
						between[3] = 26;
						if(building <= between[0])buildings[i] = 1;
						if(building > between[0] && building <= between[1])buildings[i] = 2;
						if(building > between[1] && building <= between[2])buildings[i] = 4;
						if(building > between[2] && building <= between[3])buildings[i] = 3;
						if(building > between[3])buildings[i] = 1;
					}
					else{
						between[0] = 7;
						between[1] = 14;
						between[2] = 24;
						between[3] = 28;
					}
				}
				if(i >= 48 && i <= 63){
					if(biomeint[3] == 2){
						between[0] = 14;
						between[1] = 18;
						between[2] = 23;
						between[3] = 26;
						if(building <= between[0])buildings[i] = 1;
						if(building > between[0] && building <= between[1])buildings[i] = 2;
						if(building > between[1] && building <= between[2])buildings[i] = 4;
						if(building > between[2] && building <= between[3])buildings[i] = 3;
						if(building > between[3])buildings[i] = 1;
					}
					if(biomeint[3] == 6){
						if(buildings[i] == 3)buildings[i] = 0;
					}
					if(biomeint[3] == 7){
						between[0] = 4;
						between[1] = 18;
						between[2] = 23;
						between[3] = 26;
						if(building <= between[0])buildings[i] = 1;
						if(building > between[0] && building <= between[1])buildings[i] = 2;
						if(building > between[1] && building <= between[2])buildings[i] = 4;
						if(building > between[2] && building <= between[3])buildings[i] = 3;
						if(building > between[3])buildings[i] = 1;
					}
					else{
						between[0] = 7;
						between[1] = 14;
						between[2] = 24;
						between[3] = 28;
					}
				}
			}
			if(buildings[i] == 0)blank++;
			if(buildings[i] == 1)lakes++;
			if(buildings[i] == 2)supplyd++;
			if(buildings[i] == 3)killc++;
			if(buildings[i] == 4)sleepp++;
		}
		buildings[15] = 0;
		buildings[19] = 0;
		buildings[44] = 0;
		buildings[48] = 0;
		System.out.println(blank + " " + lakes + " " + supplyd + " " + killc + " " + sleepp);
	}
	public void averageBuildings(int times){
		int blanka = blank / times;
		int lakesa = lakes / times;
		int supplyda = supplyd / times;
		int killca = killc / times;
		int sleeppa = sleepp / times;
		System.out.println(blanka + " " + lakesa + " " + supplyda + " " + killca + " " + sleeppa);
	}
}
