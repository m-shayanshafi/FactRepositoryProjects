package kw.sudoku.test;

import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import kw.sudoku.model.Game;
import kw.sudoku.model.Solution;

import org.junit.Before;
import org.junit.Test;

public class testGame {

	int[][] cGame;
	
	@Before
	public void init() {
 		cGame = new int[9][];
        for (int y = 0; y < 9; y++) {
        	cGame[y] = new int [9];
            for (int x = 0; x < 9; x++)
                cGame[y][x] = 0;
        }

        /*
        cGame[0][0] = 4;
        cGame[3][1] = 9;
        cGame[4][1] = 8;
        cGame[6][5] = 2;
        cGame[8][8] = 7;
        */
        
        
        cGame = new int[][] {{0, 0, 5, 0, 3, 0, 0, 0, 0}, 
        					 {0, 8, 0, 0, 0, 0, 0, 0, 0}, 
        					 {7, 0, 0, 5, 0, 2, 4, 0, 0}, 
        					 {0, 0, 0, 0, 0, 1, 0, 3, 0}, 
        					 {0, 0, 1, 9, 0, 0, 0, 6, 0}, 
        					 {0, 0, 7, 0, 0, 0, 0, 0, 5}, 
        					 {0, 0, 0, 0, 0, 8, 0, 0, 0}, 
        					 {8, 0, 0, 6, 9, 0, 0, 2, 0}, 
        					 {5, 6, 0, 7, 0, 0, 1, 0, 8}};
        					 
	}
	
	@Test
	public void testSingleSol() {
		Game game = new Game();
		int[][] sols = game.generateFinalSolution(cGame);
		Assert.assertNotNull(sols);
		//Game.print(sols);
	}
	
	@Test
	public void testMultiSols() {
		Game game = new Game();
		Set<Solution> sols = game.generateFinalSolutions(cGame);
		Assert.assertNotSame(sols.size(), 0);
		Game.print(sols);
	}
	
	//@Test
	public void testSolution() {
		Solution sol = new Solution("425839671189476352736512489648251937351987264297364815973128546814695723562743198");
		Assert.assertTrue(Game.isThisGameASolution(sol));
		//Game.print(sol);
	}

}
