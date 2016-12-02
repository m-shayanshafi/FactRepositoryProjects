/**
 * @(#)ScoreKeeper.java Version 1.0 98/03/12
 * 
 * Yaha! A math game for everyone. 
 * Copyright (c) 1998, 2007 Huahai Yang <huahai@yyhh.org>
 *
 * This file is part of Yaha!
 *
 * Yaha! is free software: you can redistribute it and/or modify it under the 
 * terms of the GNU Affero General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public  
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */

package net.sf.yaha;

import java.awt.*;

public class ScoreKeeper extends Label
{
   int score;
   int increase;
   double levelWeight;
   
   // value of answer
   static final int NO_NO = 1,      //no solution, answer no solution
                    NO_HAS = -2,    //no solution, answer has
                    HAS_NO = -1,    //has solution, answer no
                    HAS_WRONG = -2,
                    HAS_RIGHT = 2,
                    TIME_OUT = -1;
   
   // value of levelWeight
   static final double BEGINNER = 1,
                       INTERMEDIATE = 1.5,
                       EXPERT = 2;
                    
   public ScoreKeeper()
   {
      super("Score:    0", Label.LEFT);
      setFont(new Font("Helvetica", Font.BOLD, 16));
      setForeground(Color.black);
      score = 0;
      increase = 100;
      levelWeight = BEGINNER;
   } // constructor   
   
   public void resetScore()
   {
      score = 0;
      setText("Score: " + score);
   } // resetScore   
   
   public int getScore()
   {
      return score;
   } //getScore   
   
   public void updateScore(int answer, double timePassProportion)
   {
      double timeBonus;
      if(answer > 0)     
      {
         // correct answer, the less the time passed, the more bonus
         timeBonus = 1 - timePassProportion;
      } //if
      else timeBonus = timePassProportion;
      
      score = score + (int)(levelWeight * answer * increase * timeBonus);
      setText("Score: " + score);
   } //updateScore
   
   public void setLevelWeight(double levelWeight)
   {
      this.levelWeight = levelWeight;
   } // setLevelWeight   
   
} // scoreKeeper
