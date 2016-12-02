package com.otabi.firestar.rpg.player;

/**
 * Created by firestar115 on 5/23/15.
 * Below the author thingy...
 */
public interface Character extends GameObject{

    void shoot(int mx, int my, double cx, double cy) throws Throwable;
}
