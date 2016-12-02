/* Modified to make Field class a subclass of Arena class.  Also, world and
   methods setFont, setWorld, getWorld, and isFocusTraversable were moved to 
   Arena.
						          jdm, June 7 2001
*/
/* Field.java
   This class shows the field and players and ball.
   
   Copyright (C) 2001  Yu Zhang

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the 
   Free Software Foundation, Inc., 
   59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

	Modifications by Vadim Kyrylov 
							January 2006
*/

package soccer.client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

import soccer.client.SoccerMaster;
import soccer.common.*;

public class Field 	extends Arena 
{
	private static Color bg = Color.green.darker();
	private static Color fg = Color.red;

	public static final int LENGTH = 100; 	// use even-number value for this parameter
	public static final int WIDTH = 66;		// use even-number value for this parameter
	
	public static final int SIDEWALK = 5;
	public static final int RADIUS = 9;
	public static final int GOAL_DEPTH = 2;
	public static final int GOAL_WIDTH = 8;
	public static final int GOALAREA_WIDTH = 18;
	public static final int GOALAREA_DEPTH = 6;
	public static final int PENALTY_WIDTH = 40;
	public static final int PENALTY_DEPTH = 16;
	public static final int PENALTY_CENTER = 12;
	public static final int CORNER = 1;

	public static final float METER = 8.0F;

	public static final double BALLSIZE = 0.6;
	public static final double PLAYERSIZE = 0.9;

	public static double ballSize = BALLSIZE;
	public static double playerSize = PLAYERSIZE;
	
	private SoccerMaster aSoccerMaster;

	// the upper left corner for moving objects
	private double x;
	private double y;

	// the center of the moving object
	private Vector2d c = new Vector2d();

	// for loop
	private Player player = null;
	private Enumeration players = null;

	
	public Field(SoccerMaster aSoccerMaster) 
	{
		//Initialize drawing colors, border, opacity.
		setBackground(bg);
		setForeground(fg);

		Dimension d =
			new Dimension(
				(int)( (LENGTH + SIDEWALK * 2) * METER + 0.5 ),
				(int)( (WIDTH + SIDEWALK * 2) * METER + 0.5 ) );
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		setBorder(BorderFactory.createRaisedBevelBorder());

		this.aSoccerMaster = aSoccerMaster;
	}

	public void paintComponent(Graphics g) {

		// clears the background
		super.paintComponent(g);
		
		// draw the soccer field
		Graphics2D g2 = (Graphics2D) g;
		g2 = (Graphics2D) g;
		g2.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);

		// draw all static elements
		drawStatics( g2 );
		
		// set font size to the player size
		setFont(g2, (int)(METER * 2), 6);

		// draws all moving objects
		if (world != null) {
			drawTeams( g2 );
			drawBall( g2 );
			drawUserControlledPlayer( g2 );
		}
	}


	// draw a circle with given center point
	public void drawSpot( Graphics2D g2, double xspot, double yspot ) 
	{
		g2.setColor( Color.ORANGE );
		for ( double r = 16.0; r < 21.0; r=r+2.0 )
			g2.draw( new Ellipse2D.Double( xspot-r/2, yspot-r/2, r, r ) );
		Vector2d v = new Vector2d( xspot, yspot ); 
		user2soccer( v ); 
		g2.drawString((float)v.getX() + " ", (float)xspot+20, (float)yspot-15 );
		g2.drawString((float)v.getY() + " ", (float)xspot+20, (float)yspot-5 );
	}
	
	
	// draw static elements
	private void drawStatics( Graphics2D g2 )
	{
		// set line color
		Color lineColor = Color.white;
		g2.setColor(lineColor);

		// boundary lines
		g2.draw(
			new Rectangle2D.Double(
				SIDEWALK * METER,
				SIDEWALK * METER,
				LENGTH * METER,
				WIDTH * METER));

		// halfway line
		g2.draw(
			new Line2D.Double(
				(LENGTH / 2.0 + SIDEWALK) * METER,
				SIDEWALK * METER,
				(LENGTH / 2.0 + SIDEWALK) * METER,
				(WIDTH + SIDEWALK) * METER));

		// center circle
		g2.draw(
			new Ellipse2D.Double(
				(LENGTH / 2.0 + SIDEWALK - RADIUS) * METER,
				(WIDTH / 2.0 + SIDEWALK - RADIUS) * METER,
				RADIUS * 2 * METER,
				RADIUS * 2 * METER));

		// left goal area
		g2.draw(
			new Rectangle2D.Double(
				SIDEWALK * METER,
				(SIDEWALK + (WIDTH - GOALAREA_WIDTH) / 2.0) * METER,
				GOALAREA_DEPTH * METER,
				GOALAREA_WIDTH * METER));

		// right goal area
		g2.draw(
			new Rectangle2D.Double(
				(SIDEWALK + LENGTH - GOALAREA_DEPTH) * METER,
				(SIDEWALK + (WIDTH - GOALAREA_WIDTH) / 2.0) * METER,
				GOALAREA_DEPTH * METER,
				GOALAREA_WIDTH * METER));

		// left penalty area
		g2.draw(
			new Rectangle2D.Double(
				SIDEWALK * METER,
				(SIDEWALK + (WIDTH - PENALTY_WIDTH) / 2.0) * METER,
				PENALTY_DEPTH * METER,
				PENALTY_WIDTH * METER));

		// right penalty area
		g2.draw(
			new Rectangle2D.Double(
				(SIDEWALK + LENGTH - PENALTY_DEPTH) * METER,
				(SIDEWALK + (WIDTH - PENALTY_WIDTH) / 2.0) * METER,
				PENALTY_DEPTH * METER,
				PENALTY_WIDTH * METER));

		// left penalty circle			       
		g2.draw(
			new Arc2D.Double(
				(PENALTY_CENTER - RADIUS + SIDEWALK) * METER,
				(SIDEWALK + WIDTH / 2.0 - RADIUS) * METER,
				RADIUS * 2 * METER,
				RADIUS * 2 * METER,
				297,
				126,
				Arc2D.OPEN));

		// right penalty circle	
		g2.draw(
			new Arc2D.Double(
				(LENGTH - PENALTY_CENTER - RADIUS + SIDEWALK) * METER,
				(SIDEWALK + WIDTH / 2.0 - RADIUS) * METER,
				RADIUS * 2 * METER,
				RADIUS * 2 * METER,
				117,
				126,
				Arc2D.OPEN));

		// left top corner
		g2.draw(
			new Arc2D.Double(
				(SIDEWALK - CORNER) * METER,
				(SIDEWALK - CORNER) * METER,
				CORNER * 2 * METER,
				CORNER * 2 * METER,
				270,
				90,
				Arc2D.OPEN));

		// left bottom corner
		g2.draw(
			new Arc2D.Double(
				(SIDEWALK - CORNER) * METER,
				(SIDEWALK + WIDTH - CORNER) * METER,
				CORNER * 2 * METER,
				CORNER * 2 * METER,
				0,
				90,
				Arc2D.OPEN));

		// right top corner
		g2.draw(
			new Arc2D.Double(
				(SIDEWALK + LENGTH - CORNER) * METER,
				(SIDEWALK - CORNER) * METER,
				CORNER * 2 * METER,
				CORNER * 2 * METER,
				180,
				90,
				Arc2D.OPEN));

		// right bottom corner
		g2.draw(
			new Arc2D.Double(
				(SIDEWALK + LENGTH - CORNER) * METER,
				(SIDEWALK + WIDTH - CORNER) * METER,
				CORNER * 2 * METER,
				CORNER * 2 * METER,
				90,
				90,
				Arc2D.OPEN));

		// center mark
		g2.fill(
			new Ellipse2D.Double(
				(SIDEWALK + LENGTH / 2.0) * METER - 2,
				(SIDEWALK + WIDTH / 2.0) * METER - 2,
				5,
				5));

		// left penalty mark
		g2.fill(
			new Ellipse2D.Double(
				(SIDEWALK + PENALTY_CENTER) * METER - 2,
				(SIDEWALK + WIDTH / 2.0) * METER - 2,
				5,
				5));

		// right penalty mark
		g2.fill(
			new Ellipse2D.Double(
				(SIDEWALK + LENGTH - PENALTY_CENTER) * METER - 2,
				(SIDEWALK + WIDTH / 2.0) * METER - 2,
				5,
				5));

		// set goal color
		Color goalColor = Color.blue;
		g2.setColor(goalColor);

		// left goal
		g2.draw(
			new Rectangle2D.Double(
				(SIDEWALK - GOAL_DEPTH) * METER,
				(SIDEWALK + (WIDTH - GOAL_WIDTH) / 2.0) * METER,
				GOAL_DEPTH * METER,
				GOAL_WIDTH * METER));

		// right goal
		g2.draw(
			new Rectangle2D.Double(
				(SIDEWALK + LENGTH) * METER,
				(SIDEWALK + (WIDTH - GOAL_WIDTH) / 2.0) * METER,
				GOAL_DEPTH * METER,
				GOAL_WIDTH * METER));
	}
	


	// draw ball 
	private void drawBall( Graphics2D g2 )
	{
		if (world.ball != null) {

			// *** draw the ball                 
			c.setXY(world.ball.position);
			soccer2user(c);

			x = c.getX() - ballSize * METER;
			y = c.getY() - ballSize * METER;

			BufferedImage bi =
				new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB);
			Graphics2D big = bi.createGraphics();
			big.setColor(Color.white);
			big.fillRect(0, 0, 4, 4);
			big.setColor(Color.black);
			big.fillOval(0, 0, 4, 4);
			Rectangle r = new Rectangle(0, 0, 4, 4);
			g2.setPaint(new TexturePaint(bi, r));
			g2.fill(
				new Ellipse2D.Double(
					x,
					y,
					ballSize * METER * 2.0,
					ballSize * METER * 2.0));
		
			if ( aSoccerMaster.getShowBallCoord() ) {
				g2.setColor(Color.white);
				Vector2d v = new Vector2d(world.ball.position);
				g2.drawString((float)v.getX() + " ", (float)x-30, (float)y-15 );
				g2.drawString((float)v.getY() + " ", (float)x-30, (float)y-5 );
			}
			
			if ( world.ball.isGrabbed )
				drawSpot( g2, c.getX(), c.getY() );
		}		
	}
	
	
	// draw all players in both teams
	private void drawTeams( Graphics2D g2 )
	{
		// draw messages from player clients
		if (world.leftM != null && aSoccerMaster.isDisplayChat()) {
			g2.setColor(Color.yellow);
			g2.drawString(
				"(" + world.leftM.id + "):" + world.leftM.message,
				SIDEWALK * METER,
				2 * METER);
		}
		if (world.rightM != null && aSoccerMaster.isDisplayChat()) {
			g2.setColor(Color.red);
			g2.drawString(
				"(" + world.rightM.id + "):" + world.rightM.message,
				(SIDEWALK + LENGTH / 2.0F) * METER,
				2 * METER);
		}

		if (world.leftTeam != null) {
			// *** draw left players
			players = world.leftTeam.elements();
			while (players.hasMoreElements()) {
				player = (Player) players.nextElement();
				drawPlayer( g2, player, Color.YELLOW, Color.RED );
			}
		}

		if (world.rightTeam != null) {
			// *** draw right players
			players = world.rightTeam.elements();
			while (players.hasMoreElements()) {
				player = (Player) players.nextElement();
				drawPlayer( g2, player, Color.RED, Color.YELLOW );
			}
		}
	}
	

	// draw one player
	private void drawPlayer( Graphics2D g2, Player player, 
									Color teamColor1, Color teamColor2 )
	{
		c.setXY(player.position);
		soccer2user(c);
		x = c.getX() - playerSize * METER;
		y = c.getY() - playerSize * METER;

		g2.setColor(teamColor1);
		g2.fill(
			new Ellipse2D.Double(
				x,
				y,
				playerSize * METER * 2,
				playerSize * METER * 2));
		
		if ( player.id == 1 ) {
			// mark the goalie
			g2.setColor(Color.black);
			double scale = 0.7;
			x = c.getX() - playerSize * METER * scale;
			y = c.getY() - playerSize * METER * scale;
			g2.draw(
				new Ellipse2D.Double(
					x,
					y,
					playerSize * METER * scale * 2,
					playerSize * METER * scale * 2));
		}
				
		g2.setColor(teamColor2);
		g2.draw(
			new Line2D.Double(
				c.getX(),
				c.getY(),
				c.getX()
					+ playerSize
						* METER
						* Math.cos(Util.Deg2Rad(player.direction)),
				c.getY()
					- playerSize
						* METER
						* Math.sin(Util.Deg2Rad(player.direction))));
		if (aSoccerMaster.isDisplayID())
			g2.drawString(
				Integer.toString(player.id),
				(int) x,
				(int) y);
		
		if ( player.isGrabbed )
			drawSpot( g2, c.getX(), c.getY() );

	}


	// draw ball 
	private void drawUserControlledPlayer( Graphics2D g2 )
	{
		// identify myself on the field
		if (world.me != null) {
			c.setXY(world.me.position);
			soccer2user(c);

			x = c.getX() - playerSize * METER;
			y = c.getY() - playerSize * METER;

			if (world.me.side == 'l')
				g2.setColor(Color.yellow);
			else
				g2.setColor(Color.red);
			g2.fill(
				new Ellipse2D.Double(
					x,
					y,
					playerSize * METER * 2,
					playerSize * METER * 2));

			if (world.me.side == 'l')
				g2.setColor(Color.red);
			else
				g2.setColor(Color.yellow);
			g2.draw(
				new Line2D.Double(
					c.getX(),
					c.getY(),
					c.getX()
						+ playerSize
							* METER
							* Math.cos(Util.Deg2Rad(world.me.direction)),
					c.getY()
						- playerSize
							* METER
							* Math.sin(Util.Deg2Rad(world.me.direction))));

			g2.setColor(Color.blue);
			g2.fill(
				new Arc2D.Double(
					x,
					y,
					playerSize * METER * 2,
					playerSize * METER * 2,
					world.me.direction + 90,
					180,
					Arc2D.OPEN));

			if (aSoccerMaster.isDisplayID())
				g2.drawString(
					Integer.toString(world.me.id),
					(int) x,
					(int) y);
		}	
	}

	
	// Coordinate System Conversions

	// User space is a device-independent logical coordinate system. 
	// the coordinate space that your program uses. All geometries passed into 
	// Java 2D rendering routines are specified in user-space coordinates.
	// the origin of user space is the upper-left corner of the component's drawing
	// area. The x coordinate increases to the right, and the y coordinate increases downward.

	// Soccer space is used in soccer server.
	// the origin of soccer space is the center of the soccer field. The x coordinate increases 
	// to the right, and the y coordinate increases upward.

	// convert from Java 2d user space to soccer space
	public void user2soccer(Vector2d p) {
		double x = p.getX() / METER;
		double y = p.getY() / METER;

		double origin_x = SIDEWALK + LENGTH / 2;
		double origin_y = SIDEWALK + WIDTH / 2;

		x = x - origin_x;
		y = - (y - origin_y);

		p.setXY(x, y);

		return;

	}

	// convert from soccer space to Java 2d user space 
	public void soccer2user(Vector2d p) {
		double x = p.getX();
		double y = p.getY();

		double origin_x = (-SIDEWALK - LENGTH / 2);
		double origin_y = SIDEWALK + WIDTH / 2;

		x = (x - origin_x) * METER;
		y = - (y - origin_y) * METER;

		p.setXY(x, y);

		return;

	}


}
