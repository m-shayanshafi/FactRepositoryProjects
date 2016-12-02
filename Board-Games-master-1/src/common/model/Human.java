package common.model;

import java.util.Scanner;

public class Human extends Player {

	Human(int number, GameBoard board) {
		super(number, board);
	}
	
	public Turn play(){
		
		System.out.println("Quelle position ?");
		Scanner sc = new Scanner(System.in);
		System.out.println("Veuillez saisir un mot :");
		String positionString = sc.nextLine();
		sc.close();
		Position position = Position.parse(positionString);
		super.play(position);
		
		return new Turn(NUMBER,position);
	}
}
