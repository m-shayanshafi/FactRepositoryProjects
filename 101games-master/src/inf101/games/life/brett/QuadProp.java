package inf101.games.life.brett;

/**
 * Et veldig langt-levende mønster.
 * 
 * Kilde: Wikipedia
 * 
 * @author Anya Helene Bagge
 *
 */
public class QuadProp extends AbstractPattern {
	public QuadProp() {
		super(new String[] {
				"      X     ",
				"      X     ",
				"      X     ",
				"            ",
				"  XXX   XXX ",
				"            ",
				"      X     ",
				"      X     ",
				"      X     "
		}, "QuadProp");
	}
}
