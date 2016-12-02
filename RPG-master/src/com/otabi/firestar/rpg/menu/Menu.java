package com.otabi.firestar.rpg.menu;

import com.otabi.firestar.rpg.GameState;
import com.otabi.firestar.rpg.RPG;
import com.otabi.firestar.rpg.player.Player;
import com.otabi.firestar.rpg.player.Wizard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

import static com.otabi.firestar.glutils.GL11Util.glRect;
import static org.lwjgl.opengl.GL11.*;

/**
 * Manu!
 * Created by firestar115 on 5/23/15.
 */
public class Menu {
    public static void drawMenu() throws IOException {
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        glColor3d(1.0, 1.0, 1.0);
        glRect(20, 20, 145, 270);
        glEnable(GL_TEXTURE_2D);
        Wizard w = new Wizard();
        w.draw(20, 20, 145, 270);
        glPopMatrix();
        if(Mouse.isButtonDown(0) && Mouse.getX() >= 20 && Mouse.getX() <= 145 && 600-Mouse.getY() >= 20 && 600-Mouse.getY() <= 270 ) {
            Player.c = new Wizard();
            RPG.state = GameState.PLAY;
            System.out.println("Let's play!");
        }
    }
}
