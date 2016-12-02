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

import java.util.InputMismatchException;
import java.util.Scanner;


public class RemoteDriver
{
	public static void main(String[] args)
	{
		Scanner kbd = new Scanner(System.in);
		int port;
		String ip, pass;
		
		if (args.length > 0)
			port = Integer.parseInt(args[0]);
		else
		{
			while (true)
			{
				try
				{
					System.out.print("Enter port: ");
					port = kbd.nextInt();
				}
				catch (InputMismatchException e)
				{
					kbd.nextLine();
					continue;
				}
				break;
			}
		}
		if (args.length > 1)
			ip = args[1];
		else
		{
			System.out.print("Enter ip: ");
			ip = kbd.next();
		}
		if (args.length > 2)
			pass = args[1];
		else
		{
			System.out.print("Enter password: ");
			pass = kbd.next();
		}
		
		RemoteController rc = new RemoteController();
		if (rc.init(port, ip, pass))
			rc.start();
	}
}
