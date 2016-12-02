/**
 * 
 */
package kw.sudoku.model;

/**
 * @author ken
 *
 */
public class Solution {

	private int[][] sol;
	public Solution(int[][] solution) {
		sol = solution;
	}
	
	public Solution(String solution) {
		int[][] s = new int[9][];
		for (int y = 0; y < 9; y++) {
			s[y] = new int[9];
			for (int x = 0; x < 9; x++) {
				int ind = y*9+x;
				String c = solution.substring(ind, ind+1);
				s[y][x] = Integer.valueOf(c);
			}
		}
		sol = s;
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
	public boolean equals (Object o) {
		Solution thatS = (Solution)o;
		return this.toString().equals(thatS.toString());
		
	}
	
	@Override
	public String toString () {
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++)
                sb.append(this.sol[y][x]+"");
        }
		return sb.toString();
	}
	
	public int[][] getSolution() {
		return this.sol;
	}
	
}
