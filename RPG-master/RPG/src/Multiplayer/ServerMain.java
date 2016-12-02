package Multiplayer;

import java.io.IOException;

/**
 * Created by piano_000 on 10/7/2015.
 */
public class ServerMain {
    public static void main(String [] args) throws IOException {
        ServerMultiplayer server = new ServerMultiplayer(9503);
    }
}
