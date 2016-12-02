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

package ca.smu.cs.csci3465.project.stratego.player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JOptionPane;

import ca.smu.cs.csci3465.project.stratego.Board;
import ca.smu.cs.csci3465.project.stratego.Move;
import ca.smu.cs.csci3465.project.stratego.Piece;
import ca.smu.cs.csci3465.project.stratego.Rank;
import ca.smu.cs.csci3465.project.stratego.Spot;
import ca.smu.cs.csci3465.project.stratego.server.Message;

public class Client
{
	private String ip = null;
	private Integer port = null;
	private Socket sock = null;
	private static DataInputStream in = null;
	private static DataOutputStream out = null;

	private static View view = null;
	
	public Client(View v)
	{
		view = v;
	}
	
	public boolean init()
	{
		ip = JOptionPane.showInputDialog("Enter IP to connect to:");
		if (ip == null) return false;
		try
		{
			port = Integer.parseInt(JOptionPane.showInputDialog("Enter port to connect to:"));
		}
		catch (Exception e)
		{
			return false;
		}
		
		return connect();
	}
	
	public boolean restart()
	{
		if (ip == null || port == null)
			return false;
		return connect();
	}
	
	private boolean connect()
	{
		try {
			sock = new Socket(ip, port);
		} catch (UnknownHostException e)
		{
			JOptionPane.showMessageDialog(null, "Host Not Found",
					"Connection Error", JOptionPane.ERROR_MESSAGE);
			sock = null;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),
					"Connection Error", JOptionPane.ERROR_MESSAGE);
			sock = null;
		}
		
		if (sock != null)
		{
			try {
				in = new DataInputStream(sock.getInputStream());
				out = new DataOutputStream(sock.getOutputStream());
				
				out.writeInt(Message.PROTOCOL_VERSION);
				
				listen();
				return true;
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}
	
	private void listen()
	{
		new Thread() 
		{
			public void run()
			{
				while (true)
				{
					try
					{
						int x, y, color, rank;
						
						int type = in.readInt();
						switch (Message.values()[type])
						{
						case SETUP:
							x = in.readInt();
							view.setColor(x);
							break;
						case GRID:
							x = in.readInt();
							y = in.readInt();
							color = in.readInt();
							rank = in.readInt();
							view.update(new Spot(x, y), new Piece(0, color, Rank.values()[rank]));
							break;
						case TRAY:
							x = in.readInt();
							color = in.readInt();
							rank = in.readInt();
							view.update(Board.IN_TRAY, new Piece(x, color, Rank.values()[rank]));
							break;
						case GAMEOVER:
							x = in.readInt();
							view.gameOver(x);
							return;
						case BYE:
							view.gameOver(-1);
							return;
						case UPDATE:
							String downloadURL = new Scanner(in).next();
							JOptionPane.showMessageDialog(null, "Your version of Stratego is incompatable with this server.\n" +
									"Please download the latest version at " + downloadURL, "Stratego", JOptionPane.ERROR_MESSAGE);
							view.gameOver(-1);
							return;
						}
					}
					catch (IOException e)
					{
						view.gameOver(-1);
						return;
					}
				}
			}
		}.start();
	}
	
	public void disconnect()
	{
		try
		{
			out.writeInt(Message.BYE.ordinal());
			out.flush();
		}
		catch (IOException e){}
		finally
		{
			try {
				sock.close();
			} catch (IOException e){}
		}
	}

	public  boolean move(Move m)
	{
		try
		{
			out.writeInt(Message.MOVE.ordinal());

			out.writeInt(m.getFrom().getX());
			out.writeInt(m.getFrom().getY());
			out.writeInt(m.getTo().getX());
			out.writeInt(m.getTo().getY());
			out.writeInt(m.getPiece().getRank().ordinal());
			
			out.flush();
		}
		catch (IOException e)
		{
			return false;
		}
		return true;
	}

	public void play()
	{
		try
		{
			out.writeInt(Message.PLAY.ordinal());
			out.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
