package inf101.tabell2d;

import java.util.Arrays;


/**
 * (Løsningsforslag)
 * 
 * @author anya
 *
 * @param <E> Typen elementer som skal lagres
 */
public class RektangelTabell<E> implements ITabell2D<E> {
	private E[] data;
	private int w, h;
	
	@SuppressWarnings("unchecked")
	public RektangelTabell(int bredde, int høyde) {
		w = bredde;
		h = høyde;
		data = (E[]) new Object[w*h];
	}
	@Override
	public int bredde() {
		return w;
	}

	@Override
	public E hent(int x, int y) {
		assert x >= 0 && x < w && y >= 0 && y < h;
		return data[y*w + x];
	}

	@Override
	public int høyde() {
		return h;
	}

	@Override
	public void sett(int x, int y, E e) {
		assert x >= 0 && x < w && y >= 0 && y < h;
		data[y*w + x] = e;
	}
	
	@Override
	public RektangelTabell<E> copy() {
		RektangelTabell<E> tab = new RektangelTabell<E>(w, h);
		for(int i = 0; i < w*h; i++)
			tab.data[i] = data[i];
		return tab;
	}

	protected void copy(RektangelTabell<E> tab) {
		assert tab.bredde() == w;
		assert tab.høyde() == h;
		for(int i = 0; i < w*h; i++)
			tab.data[i] = data[i];
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(data);
		result = prime * result + h;
		result = prime * result + w;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		RektangelTabell<E> other = (RektangelTabell<E>) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		if (h != other.h)
			return false;
		if (w != other.w)
			return false;
		return true;
	}
	
}
