package inf101.games.life;

import inf101.games.IGame;
import inf101.games.life.brett.*;
import inf101.tabell2d.ITabell2D;
import inf101.tabell2d.SmultringTabell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Life implements IGame {
	private ITabell2D<Boolean> brett;
	private int bredde;
	private int høyde;
	private static final String dead = "life/images/dead";
	private static final String alive = "life/images/alive";
	private final List<IPattern> patterns = new ArrayList<IPattern>();
	private final Random random = new Random();
	private String pattern = "Random";

	public Life(int x, int y){
		bredde = x;
		høyde = y;
		newGame();
		patterns.add(new Desert());
		patterns.add(new Toad());
		patterns.add(new Beacon());
		patterns.add(new QuadProp());
		patterns.add(new Glider());
		patterns.add(new LightweightSpaceship());
		patterns.add(new Pulsar());
		patterns.add(new Acorn());
	}

	@Override
	public void newGame() {
		if(pattern.equals("Random")) {
			brett = new SmultringTabell<Boolean>(bredde, høyde);
			for(int x = 0; x < bredde; x++)
				for(int y = 0; y < høyde; y++)
					brett.sett(x, y, random.nextInt(5) == 0);
		}
		else {
			for(IPattern p : patterns) {
				if(p.getName().equals(pattern)) {
					if(bredde < p.getWidth())
						bredde = p.getWidth();
					if(høyde < p.getHeight())
						høyde = p.getHeight();
					brett = new SmultringTabell<Boolean>(bredde, høyde);

					for(int x = 0; x < bredde; x++)
						for(int y = 0; y < høyde; y++)
							brett.sett(x, y, Boolean.FALSE);

					int xOffset = (bredde - p.getWidth()) / 2;
					int yOffset = (høyde - p.getHeight()) / 2;

					for(int x = 0; x < p.getWidth(); x++)
						for(int y = 0; y < p.getHeight(); y++)
							brett.sett(xOffset + x, yOffset + y, p.isAlive(x, y));
					return;
				}
			}
		}
	}

	/**
	 * Metode for å beregne antall naboer til cellen med koord (x,y)
	 * 
	 * @param x
	 * 			x-koordinat på brett
	 * @param y
	 * 			y-koordinat på brett
	 * @return 
	 * 			antall naboer
	 */
	public int naboer(int x, int y){
		int ant=0;
		if(brett.hent(x-1, y))
			ant++;
		if(brett.hent(x-1, y-1))
			ant++;
		if(brett.hent(x-1, y+1))
			ant++;
		if(brett.hent(x, y-1))
			ant++;
		if(brett.hent(x, y+1))
			ant++;
		if(brett.hent(x+1, y-1))
			ant++;
		if(brett.hent(x+1, y))
			ant++;
		if(brett.hent(x+1, y+1))
			ant++;
		return ant;
	}

	/**
	 * Metode for neste steg på brettet
	 * Kalles for hver oppdater
	 * 
	 * oppretter nyttBrett siden brett må brukes til sjekk i naboer()
	 */
	@Override
	public void timeStep(){
		ITabell2D<Boolean> nyttBrett = new SmultringTabell<Boolean>(bredde, høyde);
		for(int m=0; m < bredde; m++){
			for(int n=0; n < høyde; n++){
				nyttBrett.sett(m, n, false);
			}
		}
		for(int i=0; i < bredde; i++){
			for (int j=0; j < høyde; j++){
				int k=naboer(i,j);
				if ((k==2 || k==3) && brett.hent(i, j))
					nyttBrett.sett(i, j, true);
				if (k==3 && !(brett.hent(i, j)))
					nyttBrett.sett(i, j, true);
			}
		}
		brett = nyttBrett;
	} 


	/**
	 * Kalles ved valg av celle (når du trykker på brettet)
	 * Setter boolean til false (død) dersom true fra før, og motsatt
	 * 
	 * @param x
	 * 			x-verdi på brett
	 * @param y
	 * 			y-verdi på brett
	 */
	@Override
	public void select(int x, int y){
		if (!(brett.hent(x, y)))
			brett.sett(x, y, true);
		else if(brett.hent(x, y))
			brett.sett(x, y, false);
	}


	@Override
	public void setSize(int nyBredde, int nyHøyde) {
		if(bredde == nyBredde && høyde == nyHøyde)
			return;

		ITabell2D<Boolean> nyttBrett = new SmultringTabell<Boolean>(nyBredde, nyHøyde);
		for(int x = 0; x < nyBredde; x++)
			for(int y = 0; y < nyHøyde; y++)
				nyttBrett.sett(x, y, Boolean.FALSE);

		for(int x = 0; x < Math.min(bredde, nyBredde); x++)
			for(int y = 0; y < Math.min(høyde, nyHøyde); y++)
				nyttBrett.sett(x, y, brett.hent(x, y));
		bredde = nyBredde;
		høyde = nyHøyde;
		brett = nyttBrett;
	}

	@Override
	public int getWidth() {
		return bredde;
	}

	@Override
	public int getHeight() {
		return høyde;
	}


	@Override
	public boolean hasStepButton() {
		return true;
	}

	@Override
	public boolean hasStartStopButtons() {
		return true;
	}

	@Override
	public String getName() {
		return "Game of Life";
	}

	@Override
	public String getIconAt(int x, int y) {
		if(brett.hent(x, y))
			return alive;
		else
			return dead;
	}

	@Override
	public List<String> getBoardSizes() {
		return null;
	}

	@Override
	public boolean canChangeSize() {
		return true;
	}

	@Override
	public List<String> getMenuChoices() {
		List<String> result = new ArrayList<String>();
		result.add("Random");
		for(IPattern pattern : patterns)
			result.add(pattern.getName());
		return result;
	}

	@Override
	public void setMenuChoice(String s) {
		pattern = s;
		newGame();
	}
}
