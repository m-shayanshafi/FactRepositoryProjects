package inf101.tabell2d;

/**
 * Interface for todimensjonale tabeller.
 
 * @param <E> Typen til elementene
 */
public interface ITabell2D<E> {
	/**
	 * @param x X-posisjon
	 * @param y Y-posisjon
	 * @return Elementet på posisjon (x, y)
	 */
	public abstract E hent(int x, int y);
	
	/**
	 * @param x X-posisjon
	 * @param y Y-posisjon
	 * @param e Element som skal settes inn på posisjon (x, y)
	 */
	public abstract void sett(int x, int y, E e);
	
	/**
	 * @return Høyde til tabellen (høyeste lovlige X-posisjon + 1)
	 */
	public abstract int høyde();
	
	/**
	 * @return Bredden til tabellen (høyeste lovlige Y-posisjon + 1)
	 */
	public abstract int bredde();
	
	/**
	 * @return En kopi av tabellen, med de samme elementene
	 */
	public abstract ITabell2D<E> copy();
}
