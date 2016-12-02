/************************************************************* 
 * 
 * Ice Hockey Manager 
 * ================== 
 * 
 * Copyright (C) by the IHM Team (see doc/credits.txt) 
 * 
 * This program is released under the GPL (see doc/gpl.txt) 
 * 
 * Further informations: http://www.icehockeymanager.org  
 * 
 *************************************************************/ 
  
package org.icehockeymanager.ihm.game;

import org.icehockeymanager.ihm.game.scenario.*;
import org.icehockeymanager.ihm.game.scheduler.*;

/**
 * The GameController class is the "main class" of the game. It is also the
 * interface between the game and the client part.
 * <p>
 * <p>
 * A possible Gui calls the init function with a scenario, settings and
 * SchedulerListener and may start the game with the startGame() function.
 * <p>
 * <p>
 * Most important functions of the GameController are getScenario (Data
 * Container with teams, players, leagues etc.), getSettings (directories etc.)
 * etc. which are callable for every game & gui part.
 * <p>
 * <p>
 * 
 * @author Bernhard von Gunten & Arik Dasen
 * @created December 2001
 */
public class GameController {

  /** The Game thread */
  private GameThread gameThread = null;

  /** The Scenario of this game */
  private Scenario scenario = null;

  /** The one and only GameController instance */
  private static GameController instance = null;

  /** Constructor for the Controller object */
  private GameController() {
  }

  /**
   * Initializes a new game (including Thread). This function is the entry point
   * for a client and also links the SchedulerListener to the game Thread.
   * 
   * @param scen
   *          Scenario
   * @param SchedulerListener
   *          SchedulerListener
   */
  public void init(Scenario scen, SchedulerListener SchedulerListener) {
    scenario = scen;
    gameThread = new GameThread();
    gameThread.addSchedulerListener(SchedulerListener);
  }

  /**
   * Returns the one and only GameController instance.
   * 
   * @return GameController
   */
  public static GameController getInstance() {
    if (instance == null) {
      instance = new GameController();
    }
    return instance;
  }

  /**
   * Starts the game thread.
   * 
   */
  public void startGame() {
    gameThread.start();
  }

  /**
   * After the gamethread is suspended (i.E. a Calendear Event had the
   * "needsonline" flag set) the client may continue the game trough this
   * function.
   */
  public void moveOn() {
    gameThread.moveOn();
  }

  /**
   * Returns the scenario of this game. This function is called by client and
   * game.
   * 
   * @return The scenario of this game
   */
  public Scenario getScenario() {
    return scenario;
  }

  /**
   * Sets the scenario of this Game (load function)
   * 
   * @param scenario
   */
  public void setScenario(Scenario scenario) {
    this.scenario = scenario;
  }

}
