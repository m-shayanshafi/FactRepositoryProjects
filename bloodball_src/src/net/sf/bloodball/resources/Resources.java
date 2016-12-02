package net.sf.bloodball.resources;

import java.util.ListResourceBundle;

public class Resources extends ListResourceBundle {
  private static final Object[][] contents = { { ResourceKeys.FRAME_TITLE, "Blood Ball" }, {
      ResourceKeys.WELCOME_MESSAGE, "Welcome to Blood Ball" }, {
      ResourceKeys.GAME_MENU, "Game" }, {
      ResourceKeys.HELP_MENU, "Help" }, {
      ResourceKeys.ABOUT_MENUITEM, "About..." }, {
      ResourceKeys.SAVE_MENUITEM, "Save" }, {
      ResourceKeys.OPEN_MENUITEM, "Open" }, {
      ResourceKeys.QUIT_MENUITEM, "Quit" }, {
      ResourceKeys.NEW_GAME_MENUITEM, "New game" }, {
      ResourceKeys.PICK_UP_BALL, "Pick up Ball" }, {
      ResourceKeys.FINISH_TEAM_SETUP, "Finish Setup" }, {
      ResourceKeys.FINISH_BALL_SETUP, "Finish Ball-Setup" }, {
      ResourceKeys.END_TURN, "End turn" }, {
      ResourceKeys.TEAM_SETUP_REQUEST, "{0}, you have {1} players left for setup." }, {
      ResourceKeys.BALL_SETUP_REQUEST, "Please give the Ball to one of your players!" }, {
      ResourceKeys.BLOCK_REQUEST, "Please block a neighbor opponent. [Right-click to cancel]" }, {
      ResourceKeys.HAND_OFF_REQUEST, "Hand-off the ball. [Right-click to cancel]" }, {
      ResourceKeys.SUBSTITUTE_REQUEST, "Place the substitution player. [Right-click to cancel]" }, {
      ResourceKeys.MOVE_COUNTER, "Player may move {0} squares. [Right-click to cancel]" }, {
      ResourceKeys.EXTRA_MOVE_COUNTER, "Player may move {0} extra squares. [Right-click to cancel]" }, {
      ResourceKeys.SPRINT_COUNTER, "Player may sprint {0} squares. [Right-click to cancel]" }, {
      ResourceKeys.SELECT_ACTIVE_PLAYER, "Please select one of your players to act." }, {
      ResourceKeys.LOAD_GAME, "load game" }, {
      ResourceKeys.LOAD_SUCCEEDED, "Game successfully loaded." }, {
      ResourceKeys.LOAD_FAILED, "Failed loading game." }, {
      ResourceKeys.SAVE_GAME, "save game" }, {
      ResourceKeys.SAVE_SUCCEEDED, "Game successfully saved." }, {
      ResourceKeys.SAVE_FAILED, "Game could not be saved." }, {
      ResourceKeys.GAME_ENDED, "{0} has won. New Game?" }, {
      ResourceKeys.OK_BUTTON, "OK" }, {
      ResourceKeys.PRONE_TOOL_TIP, "Prone turns: {0}" }, {
      ResourceKeys.GUEST_TEAM, "guest team" }, {
      ResourceKeys.HOME_TEAM, "home team" }, {
      ResourceKeys.ERROR_MESSAGE_ARGUMENT_COUNT, "Invalid number of arguments." }, {

      ResourceKeys.HEALTH_OKAY, ""}, {
      ResourceKeys.HEALTH_STUNNED, "Stunned"}, {
      ResourceKeys.HEALTH_KO, "KO'd"}, {
      ResourceKeys.HEALTH_INJURED, "Seriously injured"}, {
      ResourceKeys.HEALTH_DEAD, "Dead"}
  };

  protected Object[][] getContents() {
    return contents;
  }
}