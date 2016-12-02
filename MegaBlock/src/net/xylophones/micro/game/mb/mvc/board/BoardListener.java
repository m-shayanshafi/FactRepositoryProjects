/*
 * BoardListener.java
 *
 * Copyright 2007 William Robertson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package net.xylophones.micro.game.mb.mvc.board;

/**
 * Interface which should be implemented by classes which
 * want to receive notification of events from the {@code BoardModel}
 *
 * @author william@xylophones.net
 */
public interface BoardListener {

    /**
     * Player has scored a single line
     */
    public static final int EVENT_LINE_SINGLE = 0;

    /**
     * Player has scored two lines
     */
    public static final int EVENT_LINE_DOUBLE = 1;

    /**
     * Player has scored three lines
     */
    public static final int EVENT_LINE_TRIPLE = 2;
  
    /**
     * Player has scored a tetris (4 lines)
     */
    public static final int EVENT_LINE_TETRIS = 3;

    /**
     * A block has landed
     */
    public static final int EVENT_BLOCK_LANDED = 4;

    /**
     * The board is fully stacked up
     */
    public static final int EVENT_BOARD_FULL = 5;
	
    /**
     * This will be called by {@code BoardController} when
     * various board events happen during play
     * 
     * @param event
     */
    public void boardEvent(int event);
}
