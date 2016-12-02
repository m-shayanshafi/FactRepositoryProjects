package com.otabi.firestar.rpg.player;

import com.otabi.firestar.glutils.GL11Util;
import com.otabi.firestar.rpg.GameState;
import com.otabi.firestar.rpg.RPG;
import com.otabi.firestar.rpg.enemy.Basic;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * The player
 * Created by firestar115 on 5/23/15.
 */
public strictfp class Player {

    public static double reload = 0;

    public static double vx = 0.0d;
    public static double vy = 0.0d;
    public static double x = 0.0d;
    public static double y = 0.0d;
    public static Character c;

    public static int duration = 0;
    public static int score = 0;

    public static double hp = 100;

    public static final double VELOCITY = 5;

    public static List<GameObject> gameObjects = new ArrayList<>();
    private static double shield = 50.0d;
    private static boolean shieldOn;

    public static void setVelocityX(double vx) {
        Player.vx = vx;
    }

    public static void setVelocityY(double vy) {
        Player.vy = vy;
    }

    public static void drawPlayer() throws Throwable {
//        RPG.sd.makeCurrent();
        c.draw(x, y, x+64.0d, y+128.0d);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor3d(1.0,0.0,0.0);
        GL11Util.glRect(x, y-10, x+64, y-5);
        GL11.glColor3d(0.0,1.0,0.0);
        GL11Util.glRect(x, y-10, x+(64.0d*(hp/100.0d)), y-5);
        GL11.glColor3d(0.0, 0.0, 0.5);
        GL11Util.glRect(x, y-17, x+64, y-12);
        GL11.glColor3d(0.0, 0.0, 1.0);
        GL11Util.glRect(x, y-17, x+(64.0d*((20-reload > 20.0d ? 20.0d : 20-reload)/20.0d)), y-12);
        GL11.glColor3d(0.6, 0.7, 0.0);
        GL11Util.glRect(x, y-24, x+64, y-19);
        GL11.glColor3d(0.9, 1.0, 0.1);
        GL11Util.glRect(x, y-24, x+(64.0d*(shield/50.0d)), y - 19);
        GL11.glColor3d(1.0,1.0,1.0);
        GL11.glPopMatrix();
        if(Keyboard.isKeyDown(Keyboard.KEY_W) && y > 0) {
            setVelocityY(-VELOCITY);
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S) && y+128 < 600) {
            setVelocityY(VELOCITY);
        } else {
            setVelocityY(0);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A) && x > 0) {
            setVelocityX(-VELOCITY);
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D) && x+64 < 800) {
            setVelocityX(VELOCITY);
        } else {
            setVelocityX(0);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && shield > 0) {
//            gameObjects.clear();
            shield = shield - 1.0d < 0.0d ? 0.0d : shield - 1;
            if(shield > 1) {
                shieldOn = true;
                GL11Util.glRect(x, y, x + 4, y + 128);
                GL11Util.glRect(x, y + 124, x + 64, y + 128);
                GL11Util.glRect(x + 60, y + 128, x + 64, y);
                GL11Util.glRect(x + 64, y, x, y + 4);
            }
        } else {
            shield = shield + 0.25d > 50.0d ? 50.0d : shield + 0.25d;
            shieldOn = false;
        }
        if(Mouse.isButtonDown(0) && --reload <= 0) {
            c.shoot(Mouse.getX(), 600-Mouse.getY(), x + 56, y + 8);
            reload = 20;
        }
        x += vx;
        y += vy;
        ListIterator<GameObject> iterator = gameObjects.listIterator();
        GL11.glPushMatrix();
        while(iterator.hasNext()) {
            GameObject g = iterator.next();
            g.update(iterator);
            g.draw(0, 0, 0, 0);
            if(g instanceof Basic.Fire) {
                Rectangle me = new Rectangle((int)x,(int)y,64,128);
                Rectangle f = new Rectangle((int)((Basic.Fire)g).x,(int)((Basic.Fire)g).y,8,8);
                if(me.intersects(f)) {
                    if(!shieldOn)
                    hp--;
                    iterator.remove();
                }
            }
        }
        List<GameObject> tmp = new ArrayList<>();
        for (GameObject gameObject:gameObjects) {
            ListIterator<GameObject> obi = gameObjects.listIterator(); // wan kenobi
            while (obi.hasNext()) { // use the force, luke
                GameObject gameObject1 = obi.next(); // No! That's not true! That's IMPOSSIBLE!!!
                if(gameObject1.collide(gameObject, obi)) tmp.add(gameObject1);
            }
        }
        gameObjects.removeAll(tmp);
        drawNumberString(Integer.toString(score));
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        if(new Random().nextInt((int)hp+10) == 0 || gameObjects.size() < 3 || Keyboard.isKeyDown(Keyboard.KEY_N)) {
            gameObjects.add(new Basic());
            score += 50;
        }
        reload--;
        if(hp < 0) {
            RPG.state = GameState.MENU;
            duration = 0;
            reload = 0;
            shield = 50;
            hp = 100;
            gameObjects.clear();
            JOptionPane.showMessageDialog(null,"Your score is: " + score);
            score = 0;
            return;
        }
        score += ++duration*gameObjects.size();
    }

    private static void drawNumberString(String s) {
        char[] chars = s.toCharArray();
        int w_so_far = 0;
        for(char c:chars) {
            int scale=4;
            switch (c) {
                case '-':
                    LCDd(w_so_far, scale);
                    break;
                case '0':
                    LCDa(w_so_far, scale);
                    LCDb(w_so_far, scale);
                    LCDc(w_so_far, scale);
                    LCDe(w_so_far, scale);
                    LCDf(w_so_far, scale);
                    LCDg(w_so_far,scale);
                    break;
                case '1':
                    LCDc(w_so_far,scale);
                    LCDf(w_so_far, scale);
                    break;
                case '2':
                    LCDa(w_so_far,scale);
                    LCDc(w_so_far,scale);
                    LCDd(w_so_far,scale);
                    LCDe(w_so_far,scale);
                    LCDg(w_so_far,scale);
                    break;
                case '3':
                    LCDa(w_so_far, scale);
                    LCDc(w_so_far, scale);
                    LCDf(w_so_far, scale);
                    LCDg(w_so_far,scale);
                    LCDd(w_so_far, scale);
                    break;
                case '4':
                    LCDb(w_so_far, scale);
                    LCDc(w_so_far, scale);
                    LCDf(w_so_far, scale);
                    LCDd(w_so_far, scale);
                    break;
                case '5':
                    LCDa(w_so_far, scale);
                    LCDb(w_so_far, scale);
                    LCDf(w_so_far, scale);
                    LCDg(w_so_far,scale);
                    LCDd(w_so_far, scale);
                    break;
                case '6':
                    LCDa(w_so_far, scale);
                    LCDb(w_so_far, scale);
                    LCDe(w_so_far, scale);
                    LCDf(w_so_far, scale);
                    LCDg(w_so_far,scale);
                    LCDd(w_so_far, scale);
                    break;
                case '7':
                    LCDa(w_so_far, scale);
                    LCDc(w_so_far, scale);
                    LCDf(w_so_far, scale);
                    break;
                case '8':
                    LCDa(w_so_far, scale);
                    LCDb(w_so_far, scale);
                    LCDc(w_so_far, scale);
                    LCDe(w_so_far, scale);
                    LCDf(w_so_far, scale);
                    LCDg(w_so_far,scale);
                    LCDd(w_so_far, scale);
                    break;
                case '9':
                    LCDa(w_so_far, scale);
                    LCDb(w_so_far, scale);
                    LCDc(w_so_far, scale);
                    LCDf(w_so_far, scale);
                    LCDg(w_so_far,scale);
                    LCDd(w_so_far, scale);
                    break;
            }
            w_so_far +=7*scale;
        }
    }

    private static void LCDg(int w_so_far, int scale) {
        GL11Util.glRect(w_so_far+scale, 10*scale, w_so_far+scale*5, 11*scale);
    }

    private static void LCDf(int w_so_far, int scale) {
        GL11Util.glRect(w_so_far + scale*5, scale*6, w_so_far+scale*6, scale*10);
    }

    private static void LCDe(int w_so_far, int scale) {
        GL11Util.glRect(w_so_far, scale*6, w_so_far+scale, scale*10);
    }

    private static void LCDc(int w_so_far, int scale) {
        GL11Util.glRect(w_so_far + scale * 5, scale, w_so_far+scale * 6, scale*5);
    }

    private static void LCDb(int w_so_far, int scale) {
        GL11Util.glRect(w_so_far, scale, w_so_far+scale, scale*5);
    }

    private static void LCDa(int w_so_far, int scale) {
        GL11Util.glRect(w_so_far+scale, 0, w_so_far+scale*5, scale);
    }

    private static void LCDd(int w_so_far, int scale) {
        GL11Util.glRect(w_so_far+scale, 5*scale, w_so_far+scale*5, 6*scale);
    }
}
