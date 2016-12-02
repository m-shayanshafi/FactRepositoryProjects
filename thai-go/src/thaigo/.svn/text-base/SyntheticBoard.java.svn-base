package thaigo;

import java.util.List;

import thaigo.object.Pawn;
import thaigo.utility.PropertyManager;

/**
 * Testing position of pawns by the synthetic board.
 * 
 * @author TG_Dream_Team
 * @version 9/5/2013
 *
 */
public class SyntheticBoard {
	
	/**
	 * Disable constructor.
	 */
	private SyntheticBoard() {
		
	}

	/**
	 * Print position of pawns in the <code>Console</code>.
	 * @param yourPawn first series of pawns
	 * @param foePawn second series of pawns
	 */
	public static void print(List<Pawn> yourPawns, List<Pawn> foePawns) {
		int table = Integer.parseInt(PropertyManager.getProperty("table"));
		Character[][] check = new Character[table][table];
		for (int i = 0; i < table; i++)
			for (int j = 0; j < table; j++)
				check[i][j] = '_';

		System.out.println("=====================================");
		for (Pawn p : yourPawns) {
			check[p.getPosition().getX()][table-1-p.getPosition().getY()] = 'O';
		}
		for (Pawn p : foePawns) {
			check[p.getPosition().getX()][table-1-p.getPosition().getY()] = 'X';
		}

		for (int y = 0; y < table; y++) {
			for (int x = 0; x < table; x++)
				System.out.print(check[x][y]);
			System.out.println();
		}

		System.out.println("=====================================");
	}
	
	/**
	 * Print position of pawns in the <code>Console</code>.
	 * @param yourPawn series of pawns
	 */
	public static void print(List<Pawn> pawns) {
		int table = Integer.parseInt(PropertyManager.getProperty("table"));
		Character[][] check = new Character[table][table];
		for (int i = 0; i < table; i++)
			for (int j = 0; j < table; j++)
				check[i][j] = '_';

		System.out.println("=====================================");
		for (Pawn p : pawns) {
			check[p.getPosition().getX()][table-1-p.getPosition().getY()] = 'O';
		}

		for (int y = 0; y < table; y++) {
			for (int x = 0; x < table; x++)
				System.out.print(check[x][y]);
			System.out.println();
		}

		System.out.println("=====================================");
	}
	
}
