/* Sensor.java
   This class get sensing info from the server and build the world from it

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

import soccer.common.*;
import java.io.*;
import java.util.*;

public class Sensor 
{
    // End time (in min)
    public static int END = 30;
    private int round;
    
	private World world;
	private Executor executor;
	private SoccerMaster soccerMaster;

	public Sensor(World world, Executor executor, SoccerMaster soccerMaster) {
		this.world = world;
		round = (int) (END * (60 / world.SIM_STEP_SECONDS));
		this.executor = executor;
		this.soccerMaster = soccerMaster;
	}

	public void sensing(Packet info) throws IOException {

		int min;
		int sec;

		Player player = null;
		Enumeration players = null;
		// process the info
		if (info.packetType == Packet.SEE) {
			world.see = (SeeData) info.data;

			world.me = world.see.player;
			world.status = world.see.status;
			world.ball = world.see.ball;
			world.leftTeam = world.see.leftTeam;
			world.rightTeam = world.see.rightTeam;

			// get the time information
			sec = world.see.time / (int)(1 / world.SIM_STEP_SECONDS);
			min = sec / 60;
			sec = sec % 60;

			soccerMaster.timeJLabel.setText(min + ":" + sec);

			// find out if somebody has kicked the ball
			if (world.ball.controllerType != world.preController) {
				world.preController = world.ball.controllerType;
				if (world.ball.controllerType == 'f')
					soccerMaster.getSoundSystem().playClip("kick");
			}

			// find out if I have the ball
			if (world.ball.controllerType == world.me.side
				&& world.ball.controllerId == world.me.id)
				world.isBallKickable = true;
			else
				world.isBallKickable = false;

			// find out ball's velocity
			Vector2d.subtract(
				world.ball.position,
				world.ballPosition,
				world.ballVelocity);
			world.ballPosition.setXY(world.ball.position);

			// find out my velocity
			Vector2d.subtract(
				world.me.position,
				world.prePosition,
				world.myVelocity);
			world.prePosition.setXY(world.me.position);

			// ball's relative position 
			world.distance2Ball =
				world.see.player.position.distance(world.see.ball.position);
			world.direction2Ball =
				world.see.player.position.direction(world.see.ball.position);

			// find out if last step's action has been successful or not
			synchronized (world) {
				if (world.actionType == World.KICK
					|| world.actionType == World.SHOOT
					|| world.actionType == World.PASS) {
					if (!world.isBallKickable) {
						world.force = 0;
						world.actionType = World.DRIVE;
					}
				} else if (world.actionType == World.CHASE) {
					if (world.isBallKickable) {
						world.force = 0;
						world.actionType = World.DRIVE;
					}
				} else if (world.actionType == World.MOVE) {
					double dist = world.destination.distance(world.me.position);
					if (dist < 5) {
						world.force = 0;
						world.actionType = World.DRIVE;
					}
				}
			}
			if(soccerMaster.isIn3D()) {
				soccerMaster.arena3D.repaint();
			}
			else {
				soccerMaster.arena2D.repaint();
			}

			// execute the commands
            int  reactionTime = world.see.time - world.actionTime;
            if(reactionTime < 0) reactionTime += round;
			if (reactionTime >= World.INERTIA
				|| world.isBallKickable) {
				executor.executing();
				world.actionTime = world.see.time;
			}
		} else if (info.packetType == Packet.HEAR) {
			world.message = (HearData) info.data;
			if (world.message.side == 'l')
				world.leftM = world.message;
			else if (world.message.side == 'r')
				world.rightM = world.message;
		} else if (info.packetType == Packet.REFEREE) {
			world.referee = (RefereeData) info.data;

			// get the time information
			sec = world.referee.time / (int)(1 / world.SIM_STEP_SECONDS);
			min = sec / 60;
			sec = sec % 60;

			soccerMaster.periodJLabel.setText(
				RefereeData.periods[world.referee.period] + ":");
			soccerMaster.modeJLabel.setText(
				RefereeData.modes[world.referee.mode] + ":");
			soccerMaster.timeJLabel.setText(min + ":" + sec);

			soccerMaster.leftName.setText(world.referee.leftName);
			String scoreL = world.referee.score_L 
									+ " ("+ world.referee.total_score_L + ")";
			soccerMaster.leftScore.setText(":" + scoreL );

			soccerMaster.rightName.setText(world.referee.rightName);
			String scoreR = world.referee.score_R 
									+ " ("+ world.referee.total_score_R + ")";
			soccerMaster.rightScore.setText(":" + scoreR );

			if (world.referee.total_score_L > world.leftGoal) {
				world.leftGoal = world.referee.total_score_L;
				soccerMaster.getSoundSystem().playClip("applause");
			} else if (world.referee.total_score_R > world.rightGoal) {
				world.rightGoal = world.referee.total_score_R;
				soccerMaster.getSoundSystem().playClip("applause");
			} else if (world.referee.period != world.prePeriod) {
				soccerMaster.getSoundSystem().playClip("referee2");
				world.prePeriod = world.referee.period;
			} else if (world.referee.mode != world.preMode) {
				soccerMaster.getSoundSystem().playClip("referee1");
				world.preMode = world.referee.mode;
			}
		}

	}
}
