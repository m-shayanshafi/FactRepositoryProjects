package com.game.run;

public class Main {
	public static void main(String[] args) {

		if (args.length <= 0) {
			System.out.println("Please invoke the jar with a game name: tic, connect, mega");
			return;
		}

		String game = args[0];

		if (game.equals("tic")) {
			RunTicTacToe t = new RunTicTacToe();
			t.run();
		} else if (game.equals("connect")) {
			RunConnectFour t = new RunConnectFour();
			t.run();
		} else if (game.equals("mega")) {
			RunNTicTacToe t = new RunNTicTacToe();
			t.run();
		} else {
			System.out.println("Invalid input");
		}
	}
}
