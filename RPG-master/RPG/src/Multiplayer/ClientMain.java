package Multiplayer;

import java.io.IOException;

/**
 * Created by piano_000 on 10/7/2015.
 */
public class ClientMain {
    public static void main(String [] args) throws IOException {
        ClientMultiplayer server = new ClientMultiplayer("localhost", 9503);
    }
}