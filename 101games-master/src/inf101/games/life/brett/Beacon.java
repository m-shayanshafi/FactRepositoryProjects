package inf101.games.life.brett;

/**
 * Et blinkende fyrtårn.
 * 
 * Kilde: Wikipedia
 * 
 * @author Anya Helene Bagge
 *
 */
public class Beacon extends AbstractPattern {
	public Beacon() {
		super(new String[] {
				"      ",
				" XX   ",
				" XX   ",
				"   XX ",
				"   XX ",
				"      "
		}, "Beacon");
	}
}
