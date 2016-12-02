package inf101.games.life.brett;

import inf101.tabell2d.ITabell2D;
import inf101.tabell2d.RektangelTabell;

/**
 * Superklasse for Life mønster
 * 
 * @author Anya Helene Bagge
 *
 */
public class AbstractPattern implements IPattern {

	private final ITabell2D<Boolean> pattern;
	private final String name;

	/**
	 * Setter opp et mønster.
	 * 
	 * Mønstrene er definert slik:
	 * 
	 *  - En streng per rad
	 *  - Hver streng inneholder ett tegn per kolonne
	 *  - " " (space) svarer til tomt felt, alt annet er levende felt
	 *  
	 * @param pattern Mønsteret 
	 * @param name Navnet på mønsteret
	 */
	AbstractPattern(String[] pattern, String name) {
		int høyde = pattern.length;
		int bredde = pattern[0].length();
		this.pattern = new RektangelTabell<Boolean>(bredde, høyde);
		for(int y = 0; y < høyde; y++) {
			for(int x = 0; x < bredde; x++) {
				this.pattern.sett(x, y, pattern[y].charAt(x) != ' ');
			}
		}
		this.name = name;
	}
	
	@Override
	public boolean isAlive(int x, int y) {
		return pattern.hent(x, y);
	}

	@Override
	public int getWidth() {
		return pattern.bredde();
	}

	@Override
	public int getHeight() {
		return pattern.høyde();
	}

	@Override
	public String getName() {
		return name;
	}

}
