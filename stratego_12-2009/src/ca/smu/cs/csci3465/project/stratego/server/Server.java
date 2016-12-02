/*
    This file is part of Stratego.

    Stratego is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Stratego is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Stratego.  If not, see <http://www.gnu.org/licenses/>.
*/

package ca.smu.cs.csci3465.project.stratego.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Server
{
	private ArrayList<Game> games = new ArrayList<Game>();
	private ReentrantLock gamesLock = new ReentrantLock();
	private Semaphore ignore = new Semaphore(1);
	private ServerController controller = null;
	private Server me = null;
	private Game active = null;
	private boolean running;
	private boolean closing;
	private int port;
	
	private Thread main = new Thread()
	{
		public void run()
		{
			while(running)
			{
				
				try
				{
					ignore.acquireUninterruptibly();
					ignore.release();
					
					active = new Game(port, me);
					gamesLock.lock();
					{
						games.add(active);
					}
					gamesLock.unlock();
					active.start();
					active = null;
				}
				catch (IOException e)
				{
					controller.print("Port unavailable, please select a different port.");
					controller.print("Ignoring new connections.");
					ignore(true);
				}
			}
		}
	};
	
	public Server(int p, ServerController sc)
	{
		port = p;
		me = this;
		controller = sc;
		running = true;
		closing = false;
		main.start();
	}
	
	public void gameOver(Game s)
	{
		gamesLock.lock();
		{
			games.remove(s);
		}
		gamesLock.unlock();
		
		if (closing && games.size() <= 1)
		{
			close();
			System.exit(0);
		}
	}
	
	public void ignore(boolean b)
	{
		if (b == (0 == ignore.availablePermits()))
			return;
		
		if (0 == ignore.availablePermits())
		{
			ignore.release();
		}
		else
		{
			ignore.acquireUninterruptibly();
			
			if (active != null)
				active.disconnect();
		}
	}
	
	public boolean isIgnoring()
	{
		return 0 == ignore.availablePermits();
	}
	
	public void closeWhenEmpty(boolean b)
	{
		closing = b;

		if (closing && (games.size() <= 1))
		{
			close();
			System.exit(0);
		}
	}
	
	public boolean isClosingWhenEmpty()
	{
		return closing;
	}

	public int getPort()
	{
		return port;
	}
	
	public void setPort(int p)
	{
		port = p;			
		try
		{
			if (active != null)
				active.setPort(port);
		}
		catch (Exception e){}
	}
	
	@SuppressWarnings("deprecation")
	public void close()
	{
		running = false;
		if (isIgnoring())
			main.stop(); //don't worry, we have no resources while ignoring
			
		gamesLock.lock();
		{
			while (games.size() > 0)
				games.get(0).disconnect();
		}
		gamesLock.unlock();
		
		try
		{
			main.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public String games()
	{
		String ret = "";
		gamesLock.lock();
		{
			for (int i=0;i<games.size();i++)
			{
				ret += "Game " + (i+1) + "\n";
				ret += games.get(i).status() + "\n\n";
			}
		}
		gamesLock.unlock();
		return ret;
	}
	
	public String status()
	{
		return games.size() + " games active";
	}
}
