// ServerPhysicsEngine.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

public class ServerPhysicsEngine extends PhysicsEngine
{
  private long lasttime;

  public ServerPhysicsEngine()
  {
    lasttime = GameFrame.CURRENT_TIME;
  }
  /** Checks all particles for collisions and expirations, and also moves them. */
  protected void checkParticles()
  {
    super.checkParticles();
    super.move(GameFrame.CURRENT_TIME - lasttime);
    lasttime = GameFrame.CURRENT_TIME;

    Profiler.setPeriodThreadBlock(Profiler.BLOCK_NET);
    sendUpdates();
    getUpdates();
    Profiler.setPeriodThreadBlock(Profiler.BLOCK_CHECK);
  }

  // send the updates to the clients
  private void sendUpdates()
  {
    for (int i=0; i<numParticles(); i++)
    {
      if (getParticle(i) instanceof SmokeMaker.Smoke);
      else GameFrame.serverSocket.sendAll(getParticle(i).getUpdates());
    }
  }

  // get and handle the updates from the clients
  private void getUpdates()
  {
    for (int i=0; i<GameFrame.serverSocket.numClients(); i++)
    {
      ArabianServerSocket.ClientConnection client = GameFrame.serverSocket.getClient(i);

      while (true)
      {
        String msg = client.getNextMessage();
        if (msg == null) break;

        // handle message
        System.out.println("got msg: "+msg);
      }
    }
  }
}