package ChessGameUnitTests;
import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

import ChessGameKenai.Square;


public class SquareTest {

	@Test
	public void test() {
		Square square = new Square(Color.white, 10);
		
        assertEquals("Square color is not set", Color.white, square.getColor());
        assertEquals("Square position is not set", 10, square.getPosition());
	}

}
