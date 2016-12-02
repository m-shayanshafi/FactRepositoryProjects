/* Executor.java
   The low level actions.
   
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
*/

package soccer.client;

import java.io.*;
import java.net.*;
import soccer.common.*;

public class Executor {

	private World world;
	private Transceiver transceiver;
	private InetAddress address;
	private int port;
	private DriveData driver = null;
	private KickData kicker = null;
	private Packet command = null;

	public Executor(World world, SoccerMaster soccerMaster) {
		this.world = world;
		this.transceiver = soccerMaster.getTransceiver();
		this.address = soccerMaster.getAddress();
		this.port = soccerMaster.getPort();
	}

	// predict ball's stop position
	private Vector2d ballStopPos() {
		Vector2d ballPos = new Vector2d(world.ball.position);
		Vector2d ballVel = new Vector2d(world.ballVelocity);
		double ballSpeed = ballVel.norm();
		while (ballSpeed > 0.1) {
			ballPos.add(ballVel);
			ballVel.times(1 - World.FRICTIONFACTOR);
			ballSpeed = ballVel.norm();
		}
		return ballPos;
	}

	// predict the best ball interception position
	private Vector2d interceptPos() {
		double force;
		if (world.distance2Ball >= 3)
			force = 100;
		else
			force = 50;

		Vector2d ballPos = new Vector2d(world.ball.position);
		Vector2d ballVel = new Vector2d(world.ballVelocity);

		Vector2d myPos = new Vector2d(world.me.position);
		Vector2d myVel = new Vector2d(world.myVelocity);
		Vector2d myAcc = new Vector2d();
		myAcc.setX(
			force * Math.cos(Util.Deg2Rad(world.me.direction)) * World.K1
				- myVel.getX() * World.K2);

		myAcc.setY(
			force * Math.sin(Util.Deg2Rad(world.me.direction)) * World.K1
				- myVel.getY() * World.K2);
		double dir2Ball;

		for (int i = 0; i < 100; i++) {
			ballPos.add(ballVel);
			ballVel.times(1 - World.FRICTIONFACTOR);

			dir2Ball = myPos.direction(ballPos);

			myPos.add(myVel);
			myVel.add(myAcc);
			myAcc.setX(
				force * Math.cos(Util.Deg2Rad(dir2Ball)) * World.K1
					- myVel.getX() * World.K2);

			myAcc.setY(
				force * Math.sin(Util.Deg2Rad(dir2Ball)) * World.K1
					- myVel.getY() * World.K2);

			if (myPos.distance(ballPos) < World.CONTROLRANGE)
				return ballPos;

		}

		return ballStopPos();

	}

	private void chaseBall() throws IOException {
		double force;
		if (world.distance2Ball >= 3)
			force = 100;
		else
			force = 50;
		double direction2Ball = world.me.position.direction(interceptPos());
		world.direction = direction2Ball;
		world.force = force;
		driver = new DriveData(direction2Ball, force);
		command = new Packet(Packet.DRIVE, driver, address, port);
		transceiver.send(command);
	}

	private void moveTo() throws IOException {
		double force;
		double distance = world.me.position.distance(world.destination);
		double direction = world.me.position.direction(world.destination);
		if (distance >= 5)
			force = 100;
		else
			force = 10;
		world.direction = direction;
		world.force = force;
		driver = new DriveData(direction, force);
		command = new Packet(Packet.DRIVE, driver, address, port);
		transceiver.send(command);
        //System.out.println("Drive: force->" + driver.force + " dir->" + driver.dir);
	}

	private void drive() throws IOException {

		driver = new DriveData(world.direction, world.force);
		command = new Packet(Packet.DRIVE, driver, address, port);
		transceiver.send(command);
	}

	private void shootGoal() throws IOException {
		Vector2d goal = null;

		goal = world.getGoalPosition();
		if (world.me.side != 'l')
			goal.setX(goal.getX() * -1);

		double dir = world.me.position.direction(goal);

		kicker = new KickData(dir, 100);
		command = new Packet(Packet.KICK, kicker, address, port);
		transceiver.send(command);
	}

	private void passTo() throws IOException {
		double force;
		double distance = world.me.position.distance(world.destination);
		double direction = world.me.position.direction(world.destination);
		if (distance >= 40)
			force = 100;
		else
			force = 70;

		kicker = new KickData(direction, force);
		command = new Packet(Packet.KICK, kicker, address, port);
		transceiver.send(command);
	}

	private void kick() throws IOException {

		kicker = new KickData(world.me.direction, 100);
		command = new Packet(Packet.KICK, kicker, address, port);
		transceiver.send(command);
	}

	public void executing() throws IOException {
		
		switch (world.actionType) {
			case World.SHOOT :
				shootGoal();
			break;
		
			case World.MOVE :
				moveTo();
			break;
	
			case World.PASS :
				passTo();
			break;
	
			case World.CHASE :
				chaseBall();
			break;
	
			case World.DRIVE :
				drive();
			break;
	
			case World.KICK :
				kick();
			break;
		}
	}

}
