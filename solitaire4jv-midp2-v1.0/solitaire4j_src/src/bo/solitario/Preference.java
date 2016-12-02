package bo.solitario;

public class Preference {

	private static int numberRound = 2;
	private static int currentRound = 2;

	public static int getNumberRound() {
		return currentRound;
	}

	public static void setNumberRound(int number) {
		numberRound = number;
		currentRound = number;
	}

	public static boolean continueRound() {
		// numberRound--;
		return (currentRound--) > 1;
	}

	public static void reset() {
		currentRound = numberRound;
	}

	public static void show() {
		//#mdebug info
		System.out.print(" Current  " + numberRound);
		System.out.print(" NUmber  " + currentRound);
		//#enddebug
	}

}
