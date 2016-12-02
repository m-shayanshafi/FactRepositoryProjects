package com.kbrahney.pkg;

import com.kbrahney.pkg.gui.SplashScreen;
import java.awt.EventQueue;

/**
 *
 * @author Kieran
 * File: Main.java.
 * Created: 20:22:39
 */
public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                SplashScreen ss = new SplashScreen();
                ss.setVisible(true);
            }

        });
    }

}
