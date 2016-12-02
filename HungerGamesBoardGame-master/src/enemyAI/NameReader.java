package enemyAI;

import java.io.*;
import java.util.*;

public class NameReader {
	public String number = "";
	Random rand = new Random();
	public int name;
	public String getMaleName(int namenumber) {
		String nameS = null;
		File file = new File("NamesMale.txt");
		try {
			Scanner input = new Scanner(file);
			for (int i = 0; i < 1; i++){
				number = input.nextLine();
				int lines = Integer.parseInt(number);
				if(namenumber == 0)
					name = rand.nextInt(lines) + 1;
				if(namenumber != 0)
					name = namenumber;
			}
			for (int i = 0; i < name; i++){
				nameS = input.nextLine();
			}
			input.close();
		}
		catch(FileNotFoundException e){
			System.err.format("no file found");
		}
		catch(NoSuchElementException e){
			System.err.format("line not found");
		}
		return nameS;
	}
	public String getFemaleName(int namenumber) {
		String nameS = null;
		File file = new File("NamesFemale.txt");
		try {
			Scanner input = new Scanner(file);
			for (int i = 0; i < 1; i++){
				number = input.nextLine();
				int lines = Integer.parseInt(number);
				if(namenumber == 0)
					name = rand.nextInt(lines) + 1;
				if(namenumber != 0)
					name = namenumber;
			}
			for (int i = 0; i < name; i++){
				nameS = input.nextLine();
			}
			input.close();
		}
		catch(FileNotFoundException e){
			System.err.format("no file found");
		}
		catch(NoSuchElementException e){
			System.err.format("line not found");
		}
		return nameS;
	}
	public int getHighest(boolean gender){
		File file = null;
		if(gender == true)file = new File("NamesMale.txt");
		if(gender == false)file = new File("NamesFemale.txt");
		Scanner input;
		try {
			input = new Scanner(file);
		for (int i = 0; i < 1; i++)
			number = input.nextLine();
			input.close();
		} catch (FileNotFoundException e) {
			System.err.format("file not found");
		}
		int numberI = Integer.parseInt(number);
		return numberI;
	}
}
