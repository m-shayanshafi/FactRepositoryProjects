package com.otabi.firestar.glutils;

import static org.lwjgl.opengl.GL11.*;

/**
 * GLUtil
 * Created by firestar115 on 5/23/15.
 */
public class GL11Util {

    public static void glRect(double x1, double y1, double x2, double y2) {
        glBegin(GL_QUADS);
        glVertex2d(x1, y1);
        glVertex2d(x2, y1);
        glVertex2d(x2, y2);
        glVertex2d(x1, y2);
        glEnd();
    }

}
