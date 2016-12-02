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

import java.util.ArrayList;
import java.util.Scanner;

public class ServerController
{
	private ArrayList<Controller> controllers = new ArrayList<Controller>();
	private Controller localController = null;
	private Server s = null;
	private int port;
	
	public ServerController(int p)
	{
		port = p;
	}
	
	public Server getServer()
	{
		return s;
	}
	
	public void setServer(Server srv)
	{
		s = srv;
	}
	
	public void start()
	{
		localController = new Controller(this, 0);
		localController.connect(new Scanner(System.in), System.out);
		controllers.add(localController);
		s = new Server(port, this);
		localController.start();
	}
	
	public void add(Controller c)
	{
		controllers.add(c);
	}
	
	public void disconnectAll()
	{
		print("Disconnecting all remote connections...");
		for (int i=0;i<controllers.size();)
		{
			if (controllers.get(i) != localController)
			{
				controllers.get(i).print("Disconnected by server.");
				controllers.get(i).disconnect();
				controllers.remove(i);
			}
			else
			{
				i++;
			}
		}
	}
	
	public void remove(Controller r)
	{
		controllers.remove(r);
	}
	
	public void exit()
	{
		s.close();
		System.exit(0);
	}
	
	public String getRemote(Controller c)
	{
		if (c == localController)
		{
			String ret = "Connected to " + (controllers.size()-1) + " remote controllers:";
			for (int i=0;i<controllers.size();i++)
			{
				if (controllers.get(i) != localController)
				{
					ret += "\n" + i
							 + ". " + controllers.get(i).getPrivilege()
							 + " "  + controllers.get(i).getRemote().getIP()
					         + ":"  + controllers.get(i).getRemote().getPort();
				}
			}
			return ret;
		}
		else
		{
			return "Connected as remote controller\n" +
					"on port " + c.getRemote().getPort()
					+ " from " + c.getRemote().getIP();
		}
	}

	public void print(String e)
	{
		for (int i=0;i<controllers.size();i++)
			controllers.get(i).print(e);
	}
}
