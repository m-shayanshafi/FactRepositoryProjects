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

import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;


public class Controller extends Thread
{
	private static ReentrantLock serverLock = new ReentrantLock();
	private ServerController owner = null;
	private RemoteServer remote = null;
	private PrintStream scn = null;
	private Scanner kbd = null;
	private int privilege;
	
	public Controller(ServerController sc, int p)
	{
		owner = sc;
		privilege = p;
	}
	
	public void run()
	{

		String input = "s";
		while (input.length() > 0)
		{
			switch (input.charAt(0))
			{
			case 'q':
			case 'Q':
				owner.exit();
				return;
			case 's': //status
			case 'S':
				scn.println(owner.getServer().status());
				scn.println("Port " + owner.getServer().getPort());
				scn.println("IP   " + getIP());
				if (owner.getServer().isIgnoring())
					scn.println("Ignoring new connections.");
				else
					scn.println("Accepting new connections.");
				if (owner.getServer().isClosingWhenEmpty())
					scn.println("Will close when no games are being played.");
				scn.flush();
				break;
			case 'g': //list games
			case 'G':
				scn.println(owner.getServer().status());
				scn.println(owner.getServer().games());
				scn.flush();
				break;
			case 'c': // list remote controllers
			case 'C':
				scn.println(owner.getRemote(this));
				break;
			case 'r': //restart
			case 'R':
				if (privilege > 1) break;
				
				scn.print("Are you sure? This will disconnect all games. (y/n) ");
				scn.flush();
				String yn = kbd.next();
				if (yn.length() != 0 && yn.charAt(0) == 'y')
				{
					int port = owner.getServer().getPort();
					serverLock.lock();
					{
						owner.getServer().close();
						owner.setServer(new Server(port, owner));
					}
					serverLock.unlock();
					scn.println("Restarted.");
					scn.flush();
				}
				break;
			case 'p': //port #####
			case 'P':
				if (privilege > 1) break;
				
				int port = readPort();
				owner.getServer().setPort(port);
				scn.println("Port set: " + port);
				scn.flush();
				break;
			case 'i': //ignore (toggle)
			case 'I':
				if (privilege > 1) break;
				
				owner.getServer().ignore(!owner.getServer().isIgnoring());
				if (owner.getServer().isIgnoring())
					scn.println("Ignore on");
				else
					scn.println("Ignore off");
				scn.flush();
				break;
			case 'x': //close when no games are being played (toggle)
			case 'X':
				if (privilege > 1) break;
				
				owner.getServer().closeWhenEmpty(!owner.getServer().isClosingWhenEmpty());
				if (owner.getServer().isClosingWhenEmpty())
					scn.println("ClosingWhenEmpty on");
				else
					scn.println("ClosingWhenEmpty off");
				scn.flush();
				break;
			case 'a': //activate remote input
			case 'A':
				if (privilege > 1) break;
				
				int priv;
				String p = kbd.next();
				int remotePort = readPort();
				String password = kbd.next();
				switch (p.charAt(0))
				{
				case 'a':
				case 'A':
				case '1':
					priv = 1;
					p = "Admin";
					break;
				case '2':
					priv = 2;
					p = "level 2";
					break;
				case '3':
					priv = 3;
					p = "level 3";
					break;
				case '4':
					priv = 4;
					p = "level 4";
					break;
				case 'g':
				case 'G':
				case '5':
				default:
					priv = 5;
					p = "Guest";
					break;
				}
				if (spawn(remotePort, password, priv))
				{
					scn.println("Accepting " + p + " connections on port " + remotePort);
					scn.println("Password: " + password);
				}
				break;
			case 'd': //disconnects remote connection(s)
			case 'D':
				if (privilege > 0) break;
				owner.disconnectAll();
				break;
			case 'l': //logout (remote only)
			case 'L':
				if (remote != null)
				{
					scn.println("Logging out...");
					scn.flush();
					logout();
					return;
				}
				break;
			case 'h':
			case 'H':
				scn.println("h - help");
				scn.println("s - status");
				scn.println("g - list games");
				scn.println("c - (local only) list controllers");
				scn.println("r - (admin) restart (kills all games)");
				scn.println("p - (admin) change port");
				scn.println("\tusage: p port\n\texample: p 12345");
				scn.println("i - (admin) (toggle) ignore new connections (kills games that haven't started)");
				scn.println("x - (admin) (toggle) close when there are no games being played");
				scn.println("a - (admin) activate remote input");
				scn.println("\tusage: a [guest|admin] port password\n\texample: a guest 12345 yxQ!7%a");
				scn.println("d - (local only) deactivate all remote input");
				scn.println("l - (remote only) logout");
				scn.println("See documentation for more details.");
				scn.flush();
				break;
			default:
				scn.println("Type help for instructions.");
				break;
			}
			
			try
			{
				input = "";
				input = kbd.next();
			}
			catch (Exception e)
			{
				//connection lost
				if (remote != null)
					logout();
				return;
			}
		}
	}
	
	public void connect(Scanner in, PrintStream out)
	{
		kbd = in;
		scn = out;
		if (remote != null)
		{
			owner.print("Remote connection established: " + remote.getIP() + "\n");
			spawn(remote.getPort(), remote.getPass(), privilege);
		}
	}
	
	public void setRemote(RemoteServer rc)
	{
		remote = rc;
	}
	
	public RemoteServer getRemote()
	{
		return remote;
	}
	
	public void logout()
	{
		disconnect();
		owner.remove(this);
	}
	
	public int getPrivilege()
	{
		return privilege;
	}
	
	public synchronized void disconnect()
	{
		if (remote != null)
		{
			if (scn != null) scn.close();
			if (kbd != null) kbd.close();
			remote.disconnect();
		}
	}
	
	public void print(String e)
	{
		if (scn != null)
		{
			scn.println(e);
			scn.flush();
		}
	}
	
	private boolean spawn(int port, String password, int priv)
	{
		Controller c = new Controller(owner, priv);
		c.setRemote(new RemoteServer(c, password));
		if (c.getRemote().init(port, this))
		{
			scn.flush();
			owner.add(c);
			c.getRemote().start();
			return true;
		}
		return false;
	}
	
	private String getIP()
	{
		try
		{
			return ""+InetAddress.getLocalHost();
		}
		catch (UnknownHostException e)
		{
			return "unknown";
		}
	}
	
	private int readPort()
	{
		int p;
		while (true)
		{
			try
			{
				p = kbd.nextInt();
			}
			catch (InputMismatchException e)
			{
				kbd.nextLine();
				scn.print("Enter port: ");
				scn.flush();
				continue;
			}
			break;
		}
		
		return p;
	}
}