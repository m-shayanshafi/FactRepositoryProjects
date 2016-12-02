package inf101.tabell2d;

public class SmultringTabell<E> extends RektangelTabell<E> {

	public SmultringTabell() {
		this(10, 10);
	}

	public SmultringTabell(int bredde, int høyde) {
		super(bredde, høyde);
	}

	@Override
	public SmultringTabell<E> copy() {
		SmultringTabell<E> tab = new SmultringTabell<E>(bredde(), høyde());
		copy(tab);
		return tab;
	}

	@Override
	public E hent(int x, int y) {
		if(x < 0)
			x = bredde() + x % bredde();
		x = x % bredde();
		if(y < 0)
			y = høyde() + y % høyde();
		y = y % høyde();

		return super.hent(x, y);
	}

	@Override
	public void sett(int x, int y, E elem) {
		if(x < 0)
			x = bredde() + x % bredde();
		x = x % bredde();
		if(y < 0)
			y = høyde() + y % høyde();
		y = y % høyde();

		super.sett(x, y, elem);
	}
}
