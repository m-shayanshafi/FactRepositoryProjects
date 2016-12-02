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
  
package org.icehockeymanager.ihm.game.training;

import java.io.*;

/**
 * TrainingSchedule contains the weekly training sheet.
 * 
 * @author Bernhard von Gunten
 * @created July 8, 2002
 */
public class TrainingSchedule implements Cloneable, Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3545800983205852470L;

  /** Field player training sheet (7 days, 2 times a day) */
  private String[][] fieldPlayerTraining = new String[7][2];

  /** Goalie training sheet (7 days, 2 times a day) */
  private String[][] goalieTraining = new String[7][2];

  /**
   * Gets the goalieTrainingAttribute attribute of the TrainingSchedule object
   * 
   * @param day
   *          Description of the Parameter
   * @param time
   *          Description of the Parameter
   * @return The goalieTrainingAttribute value
   */
  public String getGoalieTrainingAttribute(int day, int time) {
    return goalieTraining[day][time];
  }

  /**
   * Gets the fieldPlayerTrainingAttribute attribute of the TrainingSchedule
   * object
   * 
   * @param day
   *          Description of the Parameter
   * @param time
   *          Description of the Parameter
   * @return The fieldPlayerTrainingAttribute value
   */
  public String getFieldPlayerTrainingAttribute(int day, int time) {
    return fieldPlayerTraining[day][time];
  }

  /**
   * Sets the fieldPlayerTrainingAttribute attribute of the TrainingSchedule
   * object
   * 
   * @param attribute
   *          The new fieldPlayerTrainingAttribute value
   * @param day
   *          The new fieldPlayerTrainingAttribute value
   * @param time
   *          The new fieldPlayerTrainingAttribute value
   */
  public void setFieldPlayerTrainingAttribute(String attribute, int day, int time) {
    fieldPlayerTraining[day][time] = attribute;
  }

  /**
   * Sets the goalieTrainingAttribute attribute of the TrainingSchedule object
   * 
   * @param attribute
   *          The new goalieTrainingAttribute value
   * @param day
   *          The new goalieTrainingAttribute value
   * @param time
   *          The new goalieTrainingAttribute value
   */
  public void setGoalieTrainingAttribute(String attribute, int day, int time) {
    goalieTraining[day][time] = attribute;
  }

  /**
   * Returns a cloned object of this TraingSchedule
   * @return Object 
   * @throws CloneNotSupportedException 
   */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

}