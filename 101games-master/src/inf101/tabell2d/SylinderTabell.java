package inf101.tabell2d;

public class SylinderTabell<E> extends RektangelTabell<E> {

	public SylinderTabell() {
		this(10, 10);
	}
	
	public SylinderTabell(int bredde, int høyde) {
		super(bredde, høyde);
	}

	@Override
	public SylinderTabell<E> copy() {
		SylinderTabell<E> tab = new SylinderTabell<E>(bredde(), høyde());
		copy(tab);
		return tab;
	}

	@Override
	public E hent(int x, int y) {
		if(x < 0)
			x = bredde() + x % bredde();
		x = x % bredde();

		return super.hent(x, y);
	}
	
	@Override
	public void sett(int x, int y, E elem) {
		if(x < 0)
			x = bredde() + x % bredde();
		x = x % bredde();
		
		super.sett(x, y, elem);
	}
}
