package net.sf.bloodball.resources;

import java.util.ListResourceBundle;

public class Resources_de extends ListResourceBundle {
  private static final Object[][] contents = { { ResourceKeys.FRAME_TITLE, "Blood Ball" }, {
      ResourceKeys.WELCOME_MESSAGE, "Willkommen bei Blood Ball" }, {
      ResourceKeys.GAME_MENU, "Spiel" }, {
      ResourceKeys.HELP_MENU, "Hilfe" }, {
      ResourceKeys.ABOUT_MENUITEM, "Info..." }, {
      ResourceKeys.SAVE_MENUITEM, "Speichern" }, {
      ResourceKeys.OPEN_MENUITEM, "Öffnen" }, {
      ResourceKeys.QUIT_MENUITEM, "Programm beenden" }, {
      ResourceKeys.NEW_GAME_MENUITEM, "Neues Spiel" }, {
      ResourceKeys.FINISH_TEAM_SETUP, "Aufstellung Beenden" }, {
      ResourceKeys.FINISH_BALL_SETUP, "Ballzuweisung Beenden" }, {
      ResourceKeys.PICK_UP_BALL, "Ball aufnehmen" }, {
      ResourceKeys.END_TURN, "Zug Beenden" }, {
      ResourceKeys.TEAM_SETUP_REQUEST, "{0}, Sie haben noch {1} Spieler aufzustellen." }, {
      ResourceKeys.BALL_SETUP_REQUEST, "Bitte geben Sie einem ihrer Spieler den Ball!" }, {
      ResourceKeys.BLOCK_REQUEST, "Bitte blocken Sie einen benachbarten Spieler. [Rechts-Klick zum Beenden]" }, {
      ResourceKeys.HAND_OFF_REQUEST, "Geben Sie den Ball einem benachbarten Spieler. [Rechts-Klick zum Abbrechen]" }, {
      ResourceKeys.SUBSTITUTE_REQUEST, "Platzieren Sie den Ersatzspieler. [Rechts-Klick zum Abbrechen]" }, {
      ResourceKeys.MOVE_COUNTER, "Noch {0} Felder zu bewegen. [Rechts-Klick zum Beenden]" }, {
      ResourceKeys.EXTRA_MOVE_COUNTER, "Noch {0} Extra-Felder zu gehen. [Rechts-Klick zum Beenden]" }, {
      ResourceKeys.SPRINT_COUNTER, "Noch {0} Felder zu sprinten. [Rechts-Klick zum Beenden]" }, {
      ResourceKeys.SELECT_ACTIVE_PLAYER, "Bitte wählen Sie einen ihrer Spieler zum Handeln aus." }, {
      ResourceKeys.LOAD_GAME, "Spiel laden" }, {
      ResourceKeys.LOAD_SUCCEEDED, "Spielstand wurde erfolgreich geladen." }, {
      ResourceKeys.LOAD_FAILED, "Fehler beim Laden des Spielstands." }, {
      ResourceKeys.SAVE_GAME, "Spiel speichern" }, {
      ResourceKeys.SAVE_SUCCEEDED, "Spiel wurde erfolgreich gespeichert." }, {
      ResourceKeys.SAVE_FAILED, "Spiel konnte nicht gespeichert werden." }, {
      ResourceKeys.GAME_ENDED, "{0} haben gewonnen. Neues Spiel?" }, {
      ResourceKeys.GAME_ENDED, "{0} haben gewonnen. Neues Spiel?" }, {
      ResourceKeys.PRONE_TOOL_TIP, "Züge niedergeschlagen: {0}" }, {
      ResourceKeys.GUEST_TEAM, "Gäste" }, {
      ResourceKeys.HOME_TEAM, "Gastgeber" }, {
      ResourceKeys.ERROR_MESSAGE_ARGUMENT_COUNT, "Ungültige Parameter-Anzahl." }, {

      ResourceKeys.HEALTH_OKAY, ""}, {
      ResourceKeys.HEALTH_STUNNED, "Benommen"}, {
      ResourceKeys.HEALTH_KO, "KO"}, {
      ResourceKeys.HEALTH_INJURED, "Ernsthaft verletzt"}, {
      ResourceKeys.HEALTH_DEAD, "Tot"}
  };


  protected Object[][] getContents() {
    return contents;
  }
}