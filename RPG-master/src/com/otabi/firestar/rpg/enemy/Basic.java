package com.otabi.firestar.rpg.enemy;

import com.otabi.firestar.glutils.GL11Util;
import com.otabi.firestar.rpg.player.Character;
import com.otabi.firestar.rpg.player.GameObject;
import com.otabi.firestar.rpg.player.Player;
import com.otabi.firestar.rpg.player.Wizard;

import java.awt.*;
import java.util.ListIterator;
import java.util.Random;

import static org.lwjgl.opengl.GL11.glColor3d;

/**
 * Basic ENUMU
 * Created by firestar115 on 5/23/15.
 */
public class Basic implements Character{

    public Basic() {
        int side = new Random().nextInt(4);
        switch (side) {
            case 0:
                x = 0;
                y = new Random().nextInt(600-16);
                break;
            case 1:
                y = 0;
                x = new Random().nextInt(800-16);
                break;
            case 2:
                x = 800-16;
                y = new Random().nextInt(600-16);
                break;
            case 3:
                y = 600-16;
                x = new Random().nextInt(800-16);
                break;
        }
    }

    int shootTime = 0;

    @Override
    public void shoot(int mx, int my, double cx, double cy) throws Throwable {

    }

    @Override
    public void update(ListIterator<GameObject> iterator) {
        if(--shootTime <= 0) {
            iterator.add(new Fire(0, 0, x, y));
            shootTime = 200;
        }
    }

    @Override
    public boolean collide(GameObject g, ListIterator<GameObject> iterator) {
        if(g instanceof Wizard.Magic) {
            Wizard.Magic magic = (Wizard.Magic) g;
            Rectangle m = new Rectangle((int) magic.x, (int) magic.y, 8, 8);
            Rectangle me = new Rectangle(x, y, 16, 16);
            if(m.intersects(me)) {
//                System.out.println("Die");
                return true;
            }
        }
        return false;
    }

    public int x = 0, y = 0;

    @Override
    public void draw(double x1, double y1, double x2, double y2) throws Throwable {
        glColor3d(1.0, 0.0, 0.0);
        GL11Util.glRect(x, y, x+16, y+16);
        glColor3d(1.0, 1.0, 1.0);
    }

    public static class Fire implements GameObject {

        public double dx,dy;
        public double x, y;

        public Fire(double dx, double dy, double cx, double cy) {
            this.dx = dx;
            this.dy = dy;
            this.x = cx;
            this.y = cy;
        }

        @Override
        public void update(ListIterator<GameObject> iterator) {
            this.dx = Wizard.getPositionXRad(3, Wizard.getAngleRad(x,y, Player.x,Player.y));
            this.dy = Wizard.getPositionYRad(3, Wizard.getAngleRad(x, y, Player.x, Player.y));
            x += dx;
            y += dy;
        }

        @Override
        public void draw(double x1, double y1, double x2, double y2) throws Throwable {
            glColor3d(1.0, 0.3, 0.0);
            GL11Util.glRect(x, y, x+8, y+8);
            glColor3d(1.0, 1.0, 1.0);
        }
    }
}
