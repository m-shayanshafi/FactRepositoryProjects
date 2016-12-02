// ArabianServerSocket.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import java.net.*;
import java.io.*;
import java.util.*;

public class ArabianServerSocket
{
  private PrintWriter out = null;
  private BufferedReader in = null;


  private ServerSocket serverSocket = null;
  private Socket clientSocket = null;


  private Vector clients = new Vector();

  /** Set up the server socket and start queueing messages */
  public ArabianServerSocket()
  {
    new WaitForConnections().start();
  }

  /** returns the specified client*/
  public ClientConnection getClient(int index)
  {
    return (ClientConnection)clients.get(index);
  }

  /** returns the number of clients*/
  public int numClients()
  {
    return clients.size();
  }

  /** Sends a message to all clients */
  public void sendAll(String msg)
  {
    for (int i = 0; i<clients.size(); i++)
    {
      ((ClientConnection)clients.get(i)).send(msg);
    }
  }


  // this thread is used to wait for connections
  private class WaitForConnections extends Thread
  {
    public void run()
    {
      // start listening for connections
      try
      {
        serverSocket = new ServerSocket(GameFrame.PORT);
      }
      catch (IOException e)
      {
        System.err.println("Could not listen on port: " + GameFrame.PORT);
        System.exit(1);
      }

      while (true)
      {
        // accept connections
        try
        {
          System.out.println("Waiting for connection...");
          clientSocket = serverSocket.accept();

          // create the client connection
          ClientConnection conn = new ClientConnection(clientSocket);
          conn.start();

          // add it to the client list
          clients.add(conn);

          System.out.println("Connection accepted!");
        }
        catch (IOException e)
        {
          System.err.println("Accept failed.");
          System.exit(1);
        }
      }
    }
  }

  // this class is used to communicate with a client. Each client will have one
  // of these objects associated with it.
  public class ClientConnection extends Thread
  {
    private Socket socket = null;
  private LinkedList msgQueue = new LinkedList();

    public ClientConnection(Socket clientSocket)
    {
      socket = clientSocket;
    }

    /** Queue received messages */
    public void run()
    {
      try
      {
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(
            new InputStreamReader(
            socket.getInputStream()));
        String inputLine;

        // store all incoming messages in a queue
        System.out.println("Queueing Messages");
        while ( (inputLine = in.readLine()) != null)
        {
          synchronized (this)
          {
            // add the message to the queue
            msgQueue.addFirst(inputLine);
          }
        }
      }
      catch (SocketException e)
      {
        System.out.println("Connection Terminated");

        // remove this client from the list
        clients.remove(this);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }

    /* Send a message */
    public void send(String msg)
    {
      out.println(msg);
    }

    /* Get the next message in the queue, or null if it is empty*/
    public synchronized String getNextMessage()
    {
      if (msgQueue.isEmpty()) return null;
      return (String) msgQueue.removeLast();
    }

    /* Close the connection */
    public void close()
    {
      try
      {
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
      }

      catch (IOException e)
      {
        e.printStackTrace();
      }
    }

  }
}