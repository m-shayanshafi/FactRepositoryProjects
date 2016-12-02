package arena;

import org.newdawn.slick.Color;

public class Biome extends RandomTerrainGenerator{
	public byte Biome;
	public byte Position;
	public Biome(byte biome, byte position){
		Biome = biome;
		Position = position;
		System.out.println(Biome + " " + Position);
	}
	public Color getShading(){
		int red = 0;
		int green = 0;
		int blue = 0;
		if(Biome == 1){
			red = 124;
			green = 252;
		}
		if(Biome == 2){
			red = 34;
			green = 139;
			blue = 34;
		}
		if(Biome == 3){
			red = 1;
			green = 68;
			blue = 33;
		}
		if(Biome == 4){
			red = 255;
			green = 204;
			blue = 51;
		}
		if(Biome == 5){
			red = 1;
			green = 1;
			blue = 1;
		}
		if(Biome == 6){
		}
		if(Biome == 7){
			red = 85;
			green = 85;
			blue = 85;
		}
		Color shading = new Color(red, green, blue);
		return shading;
	}
}
