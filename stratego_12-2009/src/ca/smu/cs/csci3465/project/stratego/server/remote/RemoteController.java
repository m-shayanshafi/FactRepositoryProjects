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

package ca.smu.cs.csci3465.project.stratego.server.remote;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import ca.smu.cs.csci3465.project.stratego.server.Hash;


public class RemoteController
{
	private Socket sock = null;
	private PrintStream out = null;
	private Scanner in = null;
	
	public boolean init(int port, String ip, String password)
	{
		try
		{
			sock = new Socket(ip, port);
			out = new PrintStream(sock.getOutputStream());
			in = new Scanner(sock.getInputStream());
		}
		catch (UnknownHostException e)
		{
			System.out.println("Host not found.");
			return false;
		}
		catch (IOException e)
		{
			System.out.println("Connection failed.");
			return false;
		}
		
		authenticate(password);
		out.println(" ");
		out.flush();
		if (!sock.isConnected())
		{
			System.out.println("Incorrect password.");
			return false;
		}
		return true;
	}
	
	public void start()
	{
		new Thread()
		{
			public void run()
			{
				Scanner kbd = new Scanner(System.in);
				String input;
				while (sock.isConnected())
				{
					input = kbd.next();
					out.println(input);
					out.flush();
				}
			}
		}.start();
		
		new Thread()
		{
			public void run()
			{
				String input;
				while (sock.isConnected())
				{
					try
					{
						input = in.nextLine();
						System.out.println(input);
					}
					catch (NoSuchElementException e)
					{

						System.out.println("Disconnected.");
						System.exit(0);
					}
				}
			}
		}.start();
	}
	
	private void authenticate(String pass)
	{
		String salt = in.next();
		out.println(Hash.Sha1(salt+pass));
	}
}
