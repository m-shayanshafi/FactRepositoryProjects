// ArabianClientSocket.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import java.net.*;
import java.io.*;
import java.util.LinkedList;

public class ArabianClientSocket
{
  Socket socket = null;
  PrintWriter out = null;
  BufferedReader in = null;

  private LinkedList msgQueue = new LinkedList();

  /** Set up the client socket and start queueing messages */
  public ArabianClientSocket(String host)
  {
    try
    {
      System.out.println("Connecting to " + host + ":" + GameFrame.PORT);
      socket = new Socket(host, GameFrame.PORT);
      System.out.println("Connected!");

      out = new PrintWriter(socket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    catch (UnknownHostException e)
    {
      System.err.println("Don't know about host: " + host);
      System.exit(1);
    }
    catch (IOException e)
    {
      System.err.println("Couldn't get I/O for the connection to: " + host);
      System.exit(1);
    }

    new QueueMessages().start();
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
      socket.close();
    }

    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private class QueueMessages extends Thread
  {
    /** Queue received messages */
    public void run()
    {
      try
      {
        out = new PrintWriter(socket.getOutputStream(), true);
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
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }
}