package Multiplayer;

import Actor.Actor;
import Actor.Guard;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

/**
 * Created by piano_000 on 10/7/2015.
 */

/* TODO: Client side tasks:
Connect with Server
Send Party to Server
Wait for response for party update from server
Take turn
Send response to server
Wait for response fro party update from server

 */
public class ClientMultiplayer {

    protected String hostname;
    protected int port;

    public ClientMultiplayer(String host, int port) {
        this.hostname = host;
        this.port = port;
        startClient();
    }

    public void startClient() {
        try {
            // Create the socket
            Socket clientSocket = new Socket(hostname, port);
            // Create the input & output streams to the server
            ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());

            // Read modify
            // TODO here

        /* Create the object to send */
            Vector<Actor> party = new Vector<>();

            Guard g1 = new Guard(); party.add(g1);
            Guard g2 = new Guard(); party.add(g2);
            Guard g3 = new Guard(); party.add(g3);

        /* Send the Message Object to the server */
            outToServer.writeObject(party);

        /* Retrive the Message Object from server */
            BattleField inFromServerList;
            BattleField msgFrmServer = null;

        } catch (UnknownHostException e) {
            System.err.println("Unrecognized host: " + hostname);
            //TODO: Handle the problem
        } catch (IOException e) {
            System.err.println("Failed to establish connection to " + hostname);
            //TODO: Handle the problem
        }
    }

}
