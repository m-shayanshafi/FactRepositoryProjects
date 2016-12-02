package inf101.games;

import java.util.List;

/**
 * Interface for brettspill som passer til brettspill-GUIen
 * 
 * Spillene er ment å bli kontrollert av GameGUI-en, som kaller metodene definert
 * under basert på knappene som brukerne trykker på.
 * 
 * En del av metodene styrer oppsettet til GameGUI-en ved å, f.eks., velge hvor vidt 
 * enkelte knapper skal være skrudd på, eller hvilke menyvalg som finnes.
 * 
 * @author Anya Helene Bagge
 *
 */
public interface IGame {

	/**
	 * Sett størrelsen på spillet.
	 * 
	 * MERK: hvis spillet av en eller annen grunn ikke kan ha den gitt bredden/høyden,
	 * blir den nye bredden/høyden uendret, eller et sted mellom den gamle og den ønskede
	 * nye bredden/høyden. Sjekk alltid getWidth() / getHeight() etter å ha kalt setSize().
	 * 
	 * Det kan være nødvendig å kalle newGame() etterpå for at spillet skal virke – i såfall
	 * vil canChangeSize() returnere false.
	 * 
	 * @param width Ønsket ny bredde
	 * @param height Ønsket ny høyde
	 * @throws IllegalArgumentException hvis width eller height er mindre enn 1
	 */
	void setSize(int width, int height);
	
	/**
	 * @return Bredden på spillet, i felter.
	 */
	int getWidth();
	
	/**
	 * @return Høyden på spillet, i felter.
	 */
	int getHeight();

	/**
	 * Reinitialiser spillebrettet.
	 */
	void newGame();

	/**
	 * Kalles når brukeren klikker på et felt.
	 * 
	 * @param x X-posisjonen, må være 0 <= x < getWidth()
	 * @param y Y-posisjonen, må være 0 <= y < getHeight()
	 * @throws IndexOutOfBoundsException hvis posisjonen er utenfor spillet
	 */
	void select(int x, int y);
	
	/**
	 * Kalles hver gang timeren slår ett slag (hver tidels sekund, f.eks.)
	 */
	void timeStep();
	
	/**
	 * Styrer tilgjengelige knapper i GameGUI-en.
	 * 
	 * @return True hvis spillet skal ha "Neste Steg"-knapp
	 */
	boolean hasStepButton();
	
	/**
	 * Styrer tilgjengelige knapper i GameGUI-en.
	 * 
	 * @return True hvis spillet skal ha start / stopp knapper
	 */
	boolean hasStartStopButtons();
	
	/**
	 * @return True hvis spillet kan endre størrelse uten at man kaller newGame() etterpå
	 */
	boolean canChangeSize();
	/**
	 * @return Navnet på spillet
	 */
	String getName();

	String getIconAt(int x, int y);

	/**
	 * Returnerer brettstørrelse-valg til menyen.
	 * 
	 * Hver størrelse må strenger være på formen "WxH", der W og H er heltall.
	 * 
	 * @return En liste med brettstørrelser, eller null for å bruke standard størrelser.
	 */
	List<String> getBoardSizes();
	
	/**
	 * Egne menyvalg for spillet.
	 * 
	 * Denne metoden skal returnere enten null, hvis spillet ikke skal ha sin egen meny,
	 * eller en liste med menyvalg for spillet.
	 * 
	 * @return En liste med menyvalg til en egen spill-meny, eller null hvis spillet ikke har sin egen meny.
	 */
	List<String> getMenuChoices();
	
	/**
	 * Kalles når spilleren har gjort et menyvalg i GameGUI-en.
	 * 
	 * @param s En streng som tidligere er returnert fra getMenuChoices()
	 * @throws UnsupportedOperationException hvis spillet ikke har sin egen meny
	 */
	void setMenuChoice(String s);
}
