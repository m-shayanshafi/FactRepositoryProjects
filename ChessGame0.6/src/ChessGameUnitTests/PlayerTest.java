package ChessGameUnitTests;
import static org.junit.Assert.*;

import org.junit.Test;

import ChessGameKenai.Packet;
import ChessGameKenai.Player;


public class PlayerTest {

	@Test
	public void testSetName() {
		Player player = new Player("Player1");
        assertEquals("playername is not set", "Player1", player.getName());
	}
	
	@Test
	public void testSetIconPath() {
		Player player = new Player("Player1", "image1");
				
        assertEquals("imagepath is not set", "image1", player.getIconPath());
	}

}
