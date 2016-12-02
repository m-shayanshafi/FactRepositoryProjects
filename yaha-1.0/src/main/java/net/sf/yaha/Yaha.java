/**
 * @(#)Yaha.java Version 1.3 2004/08/31
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
import java.applet.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;
import java.text.*;

/**
 * Main class of Yaha applet. It can also run as a standlone application.
 * Draws basic interface and coordinates among playing board, clock and score keeper.
 * @see        DraggingArea
 * @see        Clock
 * @see        ScoreKeeper
 * @version    1.0
 * @author     Huahai Yang
 */
public class Yaha extends Applet
                          implements ActionListener,
                                     ItemListener,
                                     Observer
{
    
    private static final long serialVersionUID = 19980307L;
    static Frame appFrame;
    static final int SIZE_WIDTH = 640,
	             SIZE_HEIGHT = 400;

    Button noSolutionButton,
    doneButton;
    TextArea  feedBackLabel;
    Clock  clock;
    Choice difficultyLevel;
    ScoreKeeper score;
    DraggingArea playingBoard;

    // time limit
    final int BEGINNER_TIME = 120,
	      INTERMEDIATE_TIME = 90,
              EXPERT_TIME = 60;

    SoundList soundList;
    String correctSound = "well_done.au",
          wrongSound = "missed.au",
          badExpSound = "Hah.au",
          timeOutSound = "bell.au",
          fillSlotSound = "click2.au",
          clickDeckSound = "click1.au";

    /**
    * Make this game run as an application
    */
    public static void main( String args [] )
    {
        //create a window for the application
        appFrame = new Frame("A Traditional Chinese Math Game");
        appFrame.setSize(SIZE_WIDTH, SIZE_HEIGHT);
        appFrame.show();
        appFrame.addWindowListener(
            new WindowAdapter()
            {
                public void windowClosing(WindowEvent winEvent)
                {
                    System.exit(0);
                }
            } );

        Yaha applet = new Yaha();
        appFrame.add(applet, BorderLayout.CENTER);

        //get the local directory
        String curDir = System.getProperty("user.dir");
        StringBuffer localURLString = new StringBuffer();
        //replace ' ' with %20, '\' with '/'
        for (int i = 0; i < curDir.length(); i++)
        {
            if (curDir.charAt(i) == ' ')
                localURLString.append("%20");
            else if (curDir.charAt(i) == '\\')
                localURLString.append('/');
            else
                localURLString.append(curDir.charAt(i));
        }
        localURLString.append('/');

        //Now try to get an applet stub for this class.
        RunAppletStub stub = new RunAppletStub(appFrame,
               applet, "Yaha", localURLString.toString());

        applet.setStub(stub);

        //start the applet in the window
        applet.init();
        appFrame.validate();
        applet.start();

    } //main()


    /**
    * Sets up basic layout of applet and prepare sound files
    */
    public void init()
    {
      setSize(SIZE_WIDTH, SIZE_HEIGHT);
      setBackground(Color.white);
      playingBoard = new DraggingArea();
      Panel panel1 = new Panel(),
            panel2 = new Panel();

      difficultyLevel = new Choice();
      difficultyLevel.addItem("Beginner");
      difficultyLevel.addItem("Intermediate");
      difficultyLevel.addItem("Expert");
      difficultyLevel.addItemListener(this);
      difficultyLevel.setEnabled(true);
      panel1.add(difficultyLevel);

      clock = new Clock(BEGINNER_TIME);
      clock.setActionCommand("timeout");
      clock.addActionListener(this);
      panel1.add(clock);

      feedBackLabel = new TextArea("Your goal: compute numbers so the result equals to 24. Click the card deck to start.", 2, 40, TextArea.SCROLLBARS_NONE);
      feedBackLabel.setFont(new Font("Times", Font.BOLD, 12));
      feedBackLabel.setForeground(Color.blue);
      panel1.add(feedBackLabel);

      score = new ScoreKeeper();
      panel1.add(score);

   	GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gridbag);

	c.fill = GridBagConstraints.BOTH;
	c.gridwidth = GridBagConstraints.REMAINDER;
   	gridbag.setConstraints(panel1, c);
   	add(panel1);

   	c.gridwidth = GridBagConstraints.REMAINDER;
   	c.gridheight = 4;
   	gridbag.setConstraints(playingBoard, c);
   	add(playingBoard);

   	panel2.setLayout(gridbag);
   	c.gridwidth = GridBagConstraints.RELATIVE;
	c.ipadx = 15;
	c.ipady = 10;
   	c.insets = new Insets (5, 20, 5, 20);

   	noSolutionButton = new Button();
	noSolutionButton.setActionCommand("no solution");
	noSolutionButton.setLabel("No Solutions.");
	noSolutionButton.setFont(new Font("Dialog", Font.PLAIN, 14));
	noSolutionButton.setForeground(new Color(255));
	noSolutionButton.addActionListener(this);
        gridbag.setConstraints(noSolutionButton, c);
	panel2.add(noSolutionButton);

	doneButton = new Button();
	doneButton.setActionCommand("done");
	doneButton.setLabel("I got a solution!");
	doneButton.setFont(new Font("Dialog", Font.PLAIN, 14));
	doneButton.setForeground(new Color(16711935));
	doneButton.addActionListener(this);
	gridbag.setConstraints(doneButton, c);
	panel2.add(doneButton);

	c.gridheight = GridBagConstraints.RELATIVE;
	gridbag.setConstraints(panel2, c);
	add(panel2);

	startLoadingSounds();

    } //init

   /**
    * Start asynchronous sound loading.
    */
   void startLoadingSounds()
   {
      soundList = new SoundList(this);
      soundList.startLoading(clickDeckSound);
      soundList.startLoading(fillSlotSound);
      soundList.startLoading(timeOutSound);
      soundList.startLoading(badExpSound);
      soundList.startLoading(correctSound);
      soundList.startLoading(wrongSound);
   } //startLoadingSounds

   /**
    * Starts applet by starting dragging thread
    * @see DraggingArea
    */
   public void start()
   {
      playingBoard.start();
   } // start

   /**
    * Stop applet by stopping dragging thread
    * @see DraggingArea
    */
    public void stop()
    {
	   clock.stop();
	   playingBoard.stop();
    } // stop

	/**
	 * Called before retrieve the solution.  In the case that
	 * the solution is not available yet, progarm will wait here,
	 * so enhance the solution thread's priority to minimize the
	 * waiting time
	 */
	private void beginWaitSolution()
	{
   	    feedBackLabel.setText("I am thinking...");
   	    playingBoard.cardDeck.setThreadPriority(Thread.NORM_PRIORITY);
    } //beginWaitSolution

   /**
    * reset solution thread's priority
    */
   private void endWaitSolution()
   {
      playingBoard.cardDeck.setThreadPriority(Thread.MIN_PRIORITY);
   } //endWaitSolution

   /**
    * Handles playing status changes
    * @see PlayingStatus
    * @see DraggingArea
    */
   public void update(Observable observable, Object status)
   {
      if(observable instanceof PlayingStatus)
      {
         switch( ((Integer)status).intValue() )
         {
            case PlayingStatus.DEALED:
               clock.start();
               noSolutionButton.setEnabled(true);
               doneButton.setEnabled(true);
               difficultyLevel.setEnabled(false);
               feedBackLabel.setText("Drag cards and operaters into slots below to form a formula, so the result number equals 24.");
               break;
            case PlayingStatus.WAITING:
               noSolutionButton.setEnabled(false);
               doneButton.setEnabled(false);
               difficultyLevel.setEnabled(true);
               break;
            case PlayingStatus.ROUND_OVER:
               //TO DO: record user score
               playingBoard.stop();
               feedBackLabel.setText("You got " + score.getScore() +
                  " points. Lets play again.");
               score.resetScore();
               playingBoard.start();
               noSolutionButton.setEnabled(false);
               doneButton.setEnabled(false);
               break;
         } // switch
      } // if
   } // update for observer

   /**
    * Handles action events fired by buttons and clock
    * @see Clock
    */
   public void actionPerformed(ActionEvent e)
   {
      double timePassed = (double)clock.getTime() / clock.getTimeLimit();
      String command = e.getActionCommand();

      if(command == "no solution")
      {
         beginWaitSolution();
         if( playingBoard.currentSolution() == null )
         {
            score.updateScore(ScoreKeeper.NO_NO, timePassed);
            soundList.playClip(correctSound);
            feedBackLabel.setText("Correct.");
         } // if no solution
         else
         {
            soundList.playClip(wrongSound);
            feedBackLabel.setText("Let me show you a solution.");
            score.updateScore(ScoreKeeper.HAS_NO, timePassed);
            playingBoard.beginAnimation();
         } // else if has solution
         endWaitSolution();
      } // if press "no solution"
      else if(command == "done")
      {
         Expression userDid;
         double value;

         if(playingBoard.isFullExpression())
         {
             userDid= new
               Expression(playingBoard.userCreatedExpression());
         } // if
         else
         {
            soundList.playClip(badExpSound);
            feedBackLabel.setText("This is a bad formula, please try again.");
            return;
         } // else

         try
         {
            value = userDid.getValue();
         } // try
         catch(IllegalExpressionException exception)
         {
            soundList.playClip(badExpSound);
            feedBackLabel.setText("This is a bad formula, please try again.");
            return;
         } //catch

         if( value == 24.0 )
         {
            soundList.playClip(correctSound);
            feedBackLabel.setText("Right!!!");
            score.updateScore(ScoreKeeper.HAS_RIGHT, timePassed);
         } // if equals 24
         else
         {
            DecimalFormat form = new DecimalFormat("#0.0#");

            String userExpStr = new String("Your solution is wrong: " + userDid.getExpression()
              + "=" + form.format(value) + ";");
            beginWaitSolution();
            soundList.playClip(wrongSound);
            if( playingBoard.currentSolution() == null )
            {
               feedBackLabel.setText(userExpStr + " There's no solution for these cards.");
               score.updateScore(ScoreKeeper.NO_HAS, timePassed);
            } // if no solution
            else
            {
               feedBackLabel.setText(userExpStr + " A correct one is shown below:");
               score.updateScore(ScoreKeeper.HAS_WRONG, timePassed);
               playingBoard.beginAnimation();
            } // else has solution
            endWaitSolution();
         } // else not equals 24
      } // else if press "done"
      else
      {
         soundList.playClip(timeOutSound);
         score.updateScore(ScoreKeeper.TIME_OUT, 1);
         if( playingBoard.currentSolution() != null )
         {
            feedBackLabel.setText("Time out, let me show you a solution.");
            playingBoard.beginAnimation();
         } //if
         else
         {
            feedBackLabel.setText("Time out, there is no solution.");
         } //else
      } // else timeout

      clock.stop();
      playingBoard.cardDeck.enableClick();
      playingBoard.setStatus(PlayingStatus.WAITING);

   } // actionPerformed

   /**
    * Handles difficultyLevel choice event
    */
   public void itemStateChanged(ItemEvent e)
   {
      String select = new String(difficultyLevel.getSelectedItem());
      if(select.equals("Beginner"))
      {
         clock.setTimeLimit( BEGINNER_TIME );
         score.setLevelWeight( ScoreKeeper.BEGINNER );
      } // if
      else if( select.equals("Intermediate") )
      {
         score.setLevelWeight( ScoreKeeper.INTERMEDIATE );
         clock.setTimeLimit( INTERMEDIATE_TIME );
      } // else if
      else
      {
         score.setLevelWeight( ScoreKeeper.EXPERT );
         clock.setTimeLimit( EXPERT_TIME );
      } // else

   } // itemStateChanged

} //Yaha