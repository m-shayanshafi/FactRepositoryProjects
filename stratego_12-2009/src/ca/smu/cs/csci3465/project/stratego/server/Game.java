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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import ca.smu.cs.csci3465.project.stratego.Board;
import ca.smu.cs.csci3465.project.stratego.Move;
import ca.smu.cs.csci3465.project.stratego.Piece;
import ca.smu.cs.csci3465.project.stratego.Rank;
import ca.smu.cs.csci3465.project.stratego.Spot;

public class Game
{
	private ReentrantLock engineLock = new ReentrantLock();
	private Semaphore socketLock = new Semaphore(1);
	private ServerSocket srv = null;
	private Socket player[] = new Socket[2];
	
	private ServerEngine engine = null;
	private Server server = null;

	private boolean closing;
	private boolean restart;
	
	public Game(int port, Server ss) throws IOException
	{
		server = ss;
		closing = false;
		
		setPort(port);
	}
	
	public void start()
	{
		do 
		{
			closing = false;
			restart = false;
			socketLock.acquireUninterruptibly();
			socketLock.release();
			getPlayers();
		}
		while (restart);
	}
	
	public void getPlayers()
	{
		try
		{
			if (player[0] == null)
				player[0] = srv.accept();
			if (player[1] == null)
				player[1] = srv.accept();
			
			boolean recheck;
			do
			{
				recheck = false;
				
				DataOutputStream out = null;
				DataInputStream in = null;
				try
				{
					out = new DataOutputStream(player[0].getOutputStream());
					in = new DataInputStream(player[0].getInputStream());
					
					//get their version number
					if (in.readInt() < Message.PROTOCOL_VERSION)
					{
						out.writeInt(Message.UPDATE.ordinal());
						new PrintStream(out).println(Message.DOWNLOAD_URL);
						out.flush();
						player[0].close();
					}
					
					//tell them what color they are
					out.writeInt(Message.SETUP.ordinal());
					out.writeInt(0);
					out.flush();
				}
				catch (IOException e)
				{
					if (closing)
						return;
					
					//if the connection was dropped this early just ignore it
					player[0] = null;
					player[0] = srv.accept();
					recheck = true;
				}
				
				try
				{
					out = new DataOutputStream(player[1].getOutputStream());
					in = new DataInputStream(player[1].getInputStream());
					
					//get their version number
					if (in.readInt() < Message.PROTOCOL_VERSION)
					{
						out.writeInt(Message.UPDATE.ordinal());
						new PrintStream(out).println(Message.DOWNLOAD_URL);
						out.flush();
						player[1].close();
					}
					
					//tell them what color they are
					out.writeInt(Message.SETUP.ordinal());
					out.writeInt(1);
					out.flush();
				}
				catch (IOException e)
				{
					if (closing)
						return;

					//if the connection was dropped this early just ignore it
					player[1] = null;
					player[1] = srv.accept();
					recheck = true;
				}
			} while (recheck);
			
			srv.close();
			srv = null;
		}
		catch (IOException e)
		{
			if (closing)
				return;
			e.printStackTrace();
			System.exit(1);
		}
		
		engine = new ServerEngine(this);
		engine.newGame();
		
		new Thread()
		{
			public void run()
			{
				update();
				while(player[0] != null && !player[0].isClosed())
				{
					Move m = listen(player[0]);
					engineLock.lock();
					{
						if (m != null)
							engine.requestMove(m, 0);
						else
							engine.play(0);
					}
					engineLock.unlock();
					update();
				}
			}
		}.start();

		new Thread()
		{
			public void run()
			{
				update();
				while(player[0] != null && !player[1].isClosed())
				{
					Move m = listen(player[1]);
					engineLock.lock();
					{
						if (m != null)
							engine.requestMove(m, 1);
						else
							engine.play(1);
					}
					engineLock.unlock();
					update();
				}
			}
		}.start();
	}
	
	public Move listen(Socket sock)
	{
		try
		{
			DataInputStream in = new DataInputStream(sock.getInputStream());
			int type = in.readInt();
			
			switch (Message.values()[type])
			{
			case MOVE:
				int x1, x2, y1, y2, rank;
				Spot from, to;
				Piece p;
				x1 = in.readInt();
				y1 = in.readInt();
				x2 = in.readInt();
				y2 = in.readInt();
				rank = in.readInt();
				if (x1 < 0)
				{
					from = Board.IN_TRAY;
					p = new Piece(0, 0, Rank.values()[rank]);
				}
				else
				{
					from = new Spot(x1, y1);
					p = engine.getBoardPiece(x1, y1);
				}
				if (x2 < 0)
					to = Board.IN_TRAY;
				else
					to = new Spot(x2, y2);
				
				return new Move(p, from, to);
			case PLAY:
				break;
			case BYE:
				disconnect();
				break;
			}
		}
		catch (IOException e)
		{
			if (!sock.isClosed())
			{
				disconnect();
			}
		}
		
		return null;
	}
	
	public void setPort(int port) throws IOException
	{
		socketLock.acquireUninterruptibly();
		{
			closing = true;
			restart = true;
				
			try
			{
				if (srv != null)
					srv.close();
			}
			catch (IOException e) {}
			finally
			{
				srv = new ServerSocket(port);
			}
		}
		socketLock.release();
	}
	
	public void disconnect()
	{
		socketLock.acquireUninterruptibly();
		{
			closing = true;
			DataOutputStream out = null;
			
			if (srv != null) try
			{
				srv.close();
			}
			catch (IOException e) {}
			finally
			{
				srv = null;
			}
			
			if (player[0] != null) try
			{
				out = new DataOutputStream(player[0].getOutputStream());
				out.writeInt(Message.BYE.ordinal());
				out.flush();
			}
			catch (IOException e){}
			 
			if (player[1] != null) try
			{
				out = new DataOutputStream(player[1].getOutputStream());
				out.writeInt(Message.BYE.ordinal());
				out.flush();
			}
			catch (IOException e){}
			
			try
			{
				if (player[0] != null)
					player[0].close();
				if (player[1] != null)
					player[1].close();
			}
			catch (IOException e){}
			
			player[0] = null;
			player[1] = null;
		}
		socketLock.release();
		
		server.gameOver(this);
	}

	public void gameOver(int c)
	{
		update();
		
		DataOutputStream out = null;
		try
		{
			for (int i=0;i<2;i++)
			{
				out = new DataOutputStream(player[i].getOutputStream());
				out.writeInt(Message.GAMEOVER.ordinal());
				out.writeInt(c);
				out.flush();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		disconnect();
	}

	public void update()
	{
		engineLock.lock();
		{
			DataOutputStream out = null;
			for (int i=0;i<2;i++)
			{
				try
				{
					out = new DataOutputStream(player[i].getOutputStream());
					for (int j=0;j<10;j++)
					for (int k=0;k<10;k++)
					{
						out.writeInt(Message.GRID.ordinal());
	
						out.writeInt(j);
						out.writeInt(k);
						if (engine.getBoardPiece(j, k) == null || engine.getBoardPiece(j, k).getColor() < 0)
						{
							out.writeInt(-1);
							out.writeInt(Rank.NIL.ordinal());
						}
						else
						{
							if (engine.getBoardPiece(j, k).isShown())
							{
								out.writeInt(engine.getBoardPiece(j, k).getColor()+2);
								out.writeInt(engine.getBoardPiece(j, k).getRank().ordinal());
							}
							else
							{
								out.writeInt(engine.getBoardPiece(j, k).getColor());
								if (engine.getBoardPiece(j, k).getColor() == i)
									out.writeInt(engine.getBoardPiece(j, k).getRank().ordinal());
								else
									out.writeInt(Rank.UNKNOWN.ordinal());
							}
						}
					}
	
					int p = 0,
					q = 0;
					for (int j=0;j<engine.getTraySize();j++)
					{
						out.writeInt(Message.TRAY.ordinal());
	
						if (engine.getTrayPiece(j).getColor() == 0)
						{
							out.writeInt(p);
							p++;
						}
						else
						{
							out.writeInt(q);
							q++;
						}
						if (engine.getTrayPiece(j).isShown())
						{
							out.writeInt(engine.getTrayPiece(j).getColor()+2);
							out.writeInt(engine.getTrayPiece(j).getRank().ordinal());
						}
						else
						{
							out.writeInt(engine.getTrayPiece(j).getColor());
							if (engine.getTrayPiece(j).getColor() == i)
								out.writeInt(engine.getTrayPiece(j).getRank().ordinal());
							else
								out.writeInt(Rank.UNKNOWN.ordinal());
						}
					}
	
					for (;p<40;p++)
					{
						out.writeInt(Message.TRAY.ordinal());
						out.writeInt(p);
						out.writeInt(0);
						out.writeInt(Rank.NIL.ordinal());
					}
	
					for (;q<40;q++)
					{
						out.writeInt(Message.TRAY.ordinal());
						out.writeInt(q);
						out.writeInt(1);
						out.writeInt(Rank.NIL.ordinal());
					}
	
					out.flush();
				}
				catch (Exception e)
				{
					disconnect();
				}
			}
		}
		engineLock.unlock();
	}
	
	public String status()
	{
		if (player[0] == null && player[1] == null)
			return "Waiting for players.";
		if (player[0] != null && player[1] != null)
			return "Game started\n" + player[0].getInetAddress().getHostAddress()
							 + "\n" + player[1].getInetAddress().getHostAddress();
		if (player[0] == null)
			return "Player " + player[1].getInetAddress().getHostAddress()
							 + "\nwaiting for opponent";
		else
			return "Player " + player[0].getInetAddress().getHostAddress()
							 + "\nwaiting for opponent";
	}
}
