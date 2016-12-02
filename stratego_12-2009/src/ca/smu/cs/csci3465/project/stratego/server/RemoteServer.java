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
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class RemoteServer 
{
	private Controller controller = null;
	private ServerSocket ss = null;
	private Socket sock = null;
	private Scanner in = null;
	private PrintStream out = null;
	private Thread listen = null;
	private String password = "";
	private int port;
	private boolean closing = false;
	
	public RemoteServer(Controller sc, String pass)	
	{
		controller = sc;
		password = pass;
	}
	
	public boolean init(int p, Controller notify)
	{
		port = p;
		try
		{
			ss = new ServerSocket(port);
		}
		catch (IOException e)
		{
			if (notify != null)
				notify.print("Port unavailable.");
			controller.disconnect();
			return false;
		}
		return true;
	}
	
	public void start()
	{
		listen = new Thread()
		{
			public void run()
			{
				boolean valid = false;;
				do try
				{
					if (sock != null)
						sock.close();
					
					sock = ss.accept();

					in = new Scanner(sock.getInputStream());
					out = new PrintStream(sock.getOutputStream());
					valid = validate();
					if (!valid)
						out.println("Invalid password");
				}
				catch (Exception e)
				{
					if (!closing)
					{
						controller.disconnect();
						return;
					}
				} while (!valid && !closing);
				
				try
				{
					ss.close();
					ss = null;
					controller.connect(in, out);
					controller.start();
				}
				catch (Exception e)
				{
					if (!closing)
					{
						controller.disconnect();
						return;
					}
				}
			}
		};
		
		listen.start();
	}
	
	public int getPort()
	{
		return port;
	}
	
	public void setPort(int p)
	{
		port = p;
	}
	
	public String getPass()
	{
		return password;
	}
	
	public void setPass(String p)
	{
		password = p;
	}
	
	public String getIP()
	{
		if (sock != null && sock.isConnected())
			return sock.getInetAddress().getHostAddress();
		else
			return "Not connected";
	}

	public void disconnect()
	{
		closing = true;
		
		if (ss != null) try
		{
			ss.close();
		}
		catch (IOException e){}
		finally
		{
			ss = null;
		}
		
		if (sock != null) try
		{
			sock.close();
		}
		catch (IOException e){}
		finally
		{
			sock = null;
		}
	}
	
	public String status()
	{
		if (ss != null)
			return "Accepting remote controller on port " + ss.getLocalPort();
		if (sock.isConnected())
			return "Connected to remote controller " + sock.getInetAddress().getHostAddress();
		return "Remote Controller Error!";
	}
	
	private boolean validate()
	{
		String salt = Hash.Sha1(""+System.currentTimeMillis());
		out.println(salt);
		String pass = in.next();
		if (Hash.Sha1(salt+password).equals(pass))
			return true;
		return false;
	}
}
