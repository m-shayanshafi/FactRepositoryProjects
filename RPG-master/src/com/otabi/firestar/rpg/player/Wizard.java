package com.otabi.firestar.rpg.player;

import com.otabi.firestar.glutils.GL11Util;
import org.newdawn.slick.opengl.Texture;

import java.io.IOException;
import java.util.ListIterator;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

/**
 * WIIIIIIIIZAAAAAAARD!
 * Created by firestar115 on 5/23/15.
 */
public class Wizard implements Character {
    public static Texture[] textures = new Texture[4];
    public int direction = 0;
    public void draw(double x1, double y1, double x2, double y2) throws IOException {
        glBindTexture(GL_TEXTURE_2D, textures[direction].getTextureID());
        glBegin(GL_QUADS);
        glTexCoord2d(0.0d, 0.0d);
        glVertex2d(x1, y1);
        glTexCoord2d(1.0d, 0.0d);
        glVertex2d(x2, y1);
        glTexCoord2d(1.0d, 1.0d);
        glVertex2d(x2, y2);
        glTexCoord2d(0.0d, 1.0d);
        glVertex2d(x1, y2);
        glEnd();
    }

    @Override
    public void shoot(int mx, int my, double cx, double cy) throws Throwable {
        Player.gameObjects.add(new Magic(getPositionXRad(10, getAngleRad(cx, cy, mx, my)), getPositionYRad(10, getAngleRad(cx, cy, mx, my)), cx, cy, false));
//        System.out.println("Shoot!");
    }

    public static double getAngleRad(double x, double y, double tx, double ty) {
        double deltax = tx - x;
        double deltay = ty - y;
        double v = Math.atan(deltax / deltay);
        if(deltay < 0) {
            v += Math.PI;
        }
//        System.out.println(deltax + " " + deltay);
//        System.out.println(v);
        return v;
    }

    public static double getPositionXRad(double rad, double angle) {
        return rad * Math.sin(angle);
    }

    public static double getPositionYRad(double rad, double angle) {
        return rad * Math.cos(angle);
    }

//    public static float getAngle(double x, double y, double tx, double ty) {
//        return (float) Math.toDegrees(Math.atan2(tx - x, ty - y));
//    }
//
//    public static double getPositionx(float radius, float angle) {
//
//        double x = radius * Math.sin(Math.toRadians(angle));
//
//        return x;
//    }
//    public static double getPositiony(float radius, float angle) {
//
//        double y = radius * Math.cos(Math.toRadians(angle));
//
//        return y;
//    }

    public static class Magic implements GameObject {

        public final boolean isClone;
        public double dx;
        public double dy;
        public double x;
        public double y;
        public double r, g, b;

        public Magic(double dx, double dy, double cx, double cy, boolean clone) {
            this.dx = dx;
            this.dy = dy;
            x = cx;
            y = cy;
            r = cx / 800.0d;
            g = cy / 600.0d;
            b = Math.abs(cx - cy) / 1400.0d;
            this.isClone = clone;
        }

        @Override
        public void update(ListIterator<GameObject> g) {
            x += dx;
            y += dy;
            if (new Random().nextInt(500) == 250 && !isClone && Player.gameObjects.size() < 4000) {
                g.add(new Magic(getPositionXRad(10, getAngleRad(x, y, dx, dy)), getPositionYRad(10, getAngleRad(x, y, dx, dy)), x, y, true));
            }
            try {
                if (x + 8 < 0 || x > 800 || y + 8 < 0 || y > 600) g.remove();
            } catch (IllegalStateException ise) {
                System.out.println("Oops!");
            }
        }

        @Override
        public void draw(double x1, double y1, double x2, double y2) throws Throwable { // Throws away args...
            glColor3d(r, g, b);
            GL11Util.glRect(x + x1, y + y1, x + 8 + x2, y + 8 + y2);
            glColor3d(1.0, 1.0, 1.0);
        }
    }
}
