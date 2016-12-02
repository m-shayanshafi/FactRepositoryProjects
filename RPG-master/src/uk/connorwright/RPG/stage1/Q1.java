package uk.connorwright.RPG.stage1;

import java.io.IOException;

import uk.connorwright.RPG.Main;

public class Q1 {

	public static void QOne() {
		Main.print("You wake up and find a shady figure standing over you, do you:");
		askQuestions();
	}

	private static void askQuestions(){
		Main.print("A: Go back to sleep,");
		Main.print("B: Turn on the lamp,");
		Main.print("C: Pretend you never saw them");
		try {
			System.in.read();
		} catch (IOException e) {

			e.printStackTrace();
		}
		// Stuck with checking answer, any help?
	}
}


