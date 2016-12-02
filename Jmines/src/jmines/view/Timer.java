/*
 * This file is part of JMines.
 * Copyright (C) 2009 Zleurtor
 *
 * JMines is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JMines is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JMines.  If not, see <http://www.gnu.org/licenses/>.
 */
package jmines.view;

/*
 * This file is part of JMines.
 * Copyright (C) 2009 Zleurtor
 *
 * JMines is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JMines is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JMines.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.util.TimerTask;

import jmines.view.components.MainPanel;

/**
 * The class managing the in game timer.
 *
 * @author Zleurtor
 */
public class Timer extends java.util.Timer {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The number of milliseconds in one second.
     */
    private static final int ONE_THOUSAND = 1000;

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The JMines main panel.
     */
    private MainPanel mainPanel;
    /**
     * The task to execute each second.
     */
    private TimerTask task = null;
    /**
     * Tell whether or not this timer task has been canceled.
     */
    private boolean canceled = false;
    /**
     * Tell whether or not this timer has been paused.
     */
    private boolean paused = false;
    /**
     * Tell whether or not this timer has been started or resumed and not
     * canceled or paused.
     */
    private boolean running = false;
    /**
     * The number of milliseconds before resume first execution.
     */
    private long remaining = ONE_THOUSAND;

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new Timer with a given JMines main panel.
     *
     * @param newMainPanel The JMines main panel.
     */
    public Timer(final MainPanel newMainPanel) {
        super(false);

        this.mainPanel = newMainPanel;
    }


    //==========================================================================
    // Getters
    //==========================================================================
    /**
     * Tell whether or not this timer task has been canceled.
     *
     * @return true if this timer has been canceled, false otherwise.
     */
    public final boolean isCanceled() {
        return canceled;
    }

    /**
     * Tell whether or not this timer has been paused.
     *
     * @return true if this timer has been paused, false otherwise.
     */
    public final boolean isPaused() {
        return paused;
    }

    /**
     * Tell whether or not this timer has been started or resumed and not
     * canceled or paused.
     *
     * @return true if this timer has been started or resumed and not
     *         canceled or paused, false otherwise.
     */
    public final boolean isRunning() {
        return running;
    }


    //==========================================================================
    // Setters
    //==========================================================================

    //==========================================================================
    // Inherited methods
    //==========================================================================
    /**
     * Cancel this timer task.
     */
    @Override
    public final void cancel() {
        canceled = true;
        paused = false;
        running = false;
        remaining = ONE_THOUSAND;

        if (task != null) {
            task.cancel();
            task = null;
        }
        super.purge();

        super.cancel();

        mainPanel.setTimer(new Timer(mainPanel));
    }

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
    /**
     * Create a new timer task.
     */
    private void createTask() {
        task = new TimerTask() {
            /**
             * The method to run at each task execution.
             *
             * @see java.util.TimerTask#run()
             */
            public void run() {
                if (mainPanel.isSoundEnabled()) {
                    mainPanel.getAudioPlayer().playTimer();
                }
                mainPanel.getTopPanel().getTimePanel().setNumber(mainPanel.getTopPanel().getTimePanel().getNumber() + 1);
            }
        };
    }

    /**
     * Start this timer.
     */
    public final void start() {
        paused = false;
        canceled = false;
        running = true;
        remaining = ONE_THOUSAND;

        createTask();
        scheduleAtFixedRate(task, 0, ONE_THOUSAND);
    }

    /**
     * Pause this timer.
     */
    public final void pause() {
        canceled = false;
        paused = true;
        running = false;
        remaining = ONE_THOUSAND;

        if (task != null) {
            task.cancel();
            remaining = ONE_THOUSAND - (System.currentTimeMillis() - task.scheduledExecutionTime());
            task = null;
        }
        super.purge();
    }

    /**
     * Resume this timer.
     */
    public final void resume() {
        canceled = false;
        paused = false;
        running = true;

        createTask();
        scheduleAtFixedRate(task, remaining, ONE_THOUSAND);
    }

}
