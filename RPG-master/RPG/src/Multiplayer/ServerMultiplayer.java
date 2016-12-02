package Multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by piano_000 on 10/7/2015.
 */
public class ServerMultiplayer {

    private int port;

    public ServerMultiplayer(int port) {
        this.port = port;
        startServer();
    }

    protected void startServer() {
        System.out.println("Initialize Server Running.");
        try (
                ServerSocket serverSocket = new ServerSocket(port);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        ) {
            String inputLine, outputLine;

            //TODO Initiate conversation with client
            out.println("Server Running! Listening on port " + port);

            while((inputLine = in.readLine()) != null) {
                outputLine = "Client: " + inputLine;
                out.println(outputLine);
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + port + "or listening for a connection");
            System.out.println(e.getMessage());
        }

    }
}
