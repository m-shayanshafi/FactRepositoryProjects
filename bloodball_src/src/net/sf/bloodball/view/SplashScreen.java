package net.sf.bloodball.view;

import de.vestrial.util.swing.JFrameHelper;
import java.awt.*;
import java.awt.GraphicsEnvironment;
import javax.swing.*;
import javax.swing.JWindow;
import net.sf.bloodball.BloodBall;

public class SplashScreen extends JWindow {
  
  public SplashScreen() {
    super(new JFrame());
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(new JEditorPane("text/html", "<body bgcolor=#00BB00><h1 align=center><font color=#000099>Blood Ball</font></h1><div align=center>v " + BloodBall.VERSION + "</div></body>"), BorderLayout.CENTER);
    pack();
    JFrameHelper.centerOnScreen(this);
  }

  public Dimension getPreferredSize() {
    return new Dimension(200, 100);
  }
  
  public Dimension getMinimumSize() {
    return new Dimension(200, 100);
  }

}
