
package game.penguincards.debug;

public class Debug extends Exception {
	
	public Debug() {
	}
	
	public static void debug(String message) {
		System.out.println(message);
	}
}
