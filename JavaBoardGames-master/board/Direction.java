package board;

public class Direction {
	public enum direction {E,W,S,N,NE,NW,SE,SW};

	public static direction[] getAll(){
		direction[] ds = {direction.S, direction.N, direction.E, direction.W, direction.NE, direction.NW, direction.SE, direction.SW};
		return ds;
	}
}
