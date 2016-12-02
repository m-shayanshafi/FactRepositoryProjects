package inf101.games.life.brett;

/**
 * Et veldig langt-levende mønster.
 * 
 * Kilde: Wikipedia
 * 
 * @author Anya Helene Bagge
 *
 */
public class Acorn extends AbstractPattern {
	public Acorn() {
		super(new String[] {
				"         ",
				"  X      ",
				"    X    ",
				" XX  XXX ",
				"         "
		}, "Acorn");
	}
}
