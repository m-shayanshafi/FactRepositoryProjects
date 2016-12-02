/* MenuDefinitions.java
   This class defines all the menu items of TOS

   Copyright (C) 2004  Yu Zhang
 * with modifications by Vadim Kyrylov
 * January 2006

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

import soccer.client.action.*;

public interface MenuDefinitions {
	/**
	 * The menus for the application. Each of these is the class name of
	 * an action handler that deals with that menu item
	 */
	public static final Object[][] MENUS =
		new Object[][] { {
			"Game",
			SetUpServerAction.class,
			SetUpAIAction.class,
			/*---------------*/			null,
			PlayGameAction.class,
			ViewGameAction.class,
			StopGameAction.class,
			/*---------------*/			null, 
			ExitAction.class 
			}, 
			{
			"Coach",
			CoachLoadFileAction.class,
			CoachStepAction.class,
			CoachPlayAction.class,
            CoachForwardAction.class,
            SaveSnapshotAction.class
			},			
			{
			"Replay",
			LoadLogAction.class,
			RewindLogPlayAction.class,
			PlayBackLogPlayAction.class,
			PauseLogPlayAction.class,
			PlayLogPlayAction.class,
			FastForwardLogPlayAction.class,
			StopLogPlayAction.class 
			},
			{
			"Settings", 
			Display3DAction.class,
            Display3DResetAction.class,
			/*---------------*/			null, 
			TurnOnMusicAction.class, 
			TurnOnSoundAction.class,
			/*---------------*/			null, 
			ShowBallCoordAction.class,
			DisplayNumAction.class, 
			DisplayChatAction.class 
			}

	};
}