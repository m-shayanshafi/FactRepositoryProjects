package uk.connorwright.RPG;

import uk.connorwright.RPG.stage1.Q1;

public class Main {

	public static String TITLE = "Text-RPG";

	public Main(){
		print("Welcome to " + TITLE + ", Enjoy!");
	}

	public static void main(String[] args) {
		new Main();
		Q1.QOne();
	}

	public static void print(String MSG) {
		System.out.println(MSG);
	}

}
