package ChessGameUnitTests;
import static org.junit.Assert.*;

import org.junit.Test;

import ChessGameKenai.Packet;


public class PacketTest {

	@Test
	public void testSetPlayerIconPath() {
		Packet packet = new Packet();
		
		packet.setImgPath("image1");
		
        assertEquals("imagepath is not set", "image1", packet.getImgPath());
	}

	@Test
	public void testSetGuestName() {
		Packet packet = new Packet();
		
		packet.setGuestName("guest1");
		
        assertEquals("guestname is not set", "guest1", packet.getGuestName());
	}

}
