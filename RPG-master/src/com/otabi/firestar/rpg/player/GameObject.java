package com.otabi.firestar.rpg.player;

import java.util.ListIterator;

/**
 * An object...........
 * Created by firestar115 on 5/23/15.
 */
public strictfp interface GameObject {

    default void update(ListIterator<GameObject> iterator) {

    }

    void draw(double x1, double y1, double x2, double y2) throws Throwable;

    default boolean collide(GameObject g, ListIterator<GameObject> iterator) {
        return false;
    }

}
