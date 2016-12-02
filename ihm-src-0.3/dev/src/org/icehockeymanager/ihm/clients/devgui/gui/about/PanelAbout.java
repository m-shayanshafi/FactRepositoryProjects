/************************************************************* 
 * 
 * Ice Hockey Manager 
 * ================== 
 * 
 * Copyright (C) by the IHM Team (see doc/credits.txt) 
 * 
 * This program is released under the GPL (see doc/gpl.txt) 
 * 
 * Further informations: http://www.icehockeymanager.org  
 * 
 *************************************************************/ 
  
package org.icehockeymanager.ihm.clients.devgui.gui.about;

import java.awt.*;

import javax.swing.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.user.*;

import com.jgoodies.animation.*;
import com.jgoodies.animation.animations.*;
import com.jgoodies.animation.components.*;

/**
 * The PanelAbout contains: A simple about panel, showing the file about.html in
 * the same package.
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class PanelAbout extends JIhmPanel {
  static final long serialVersionUID = -4038083926312988430L;

  BorderLayout borderLayout1 = new BorderLayout();

  ImageIcon aboutIcon = new ImageIcon();

  private JLabel lblLogo;

  private JPanel panelAnimation;

  private BasicTextLabel lblAnimation1;

  private BasicTextLabel lblAnimation2;

  /**
   * Constructs the frame, sets the user, calls ihmInit
   * 
   * @param user
   *          User to show this frame for
   */
  public PanelAbout(User user) {
    try {
      initGUI();
      ihmInit();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * JBuilder stuff
   * 
   * @exception Exception
   *              Exception
   */
  private void initGUI() throws Exception {
    this.setSize(new Dimension(580, 500));
    this.setLayout(borderLayout1);
    aboutIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/about18x18.png"));
    setLocation(30, 30);
    this.setPreferredSize(new java.awt.Dimension(580, 342));
    {
      lblLogo = new JLabel();
      this.add(lblLogo, BorderLayout.CENTER);
      lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
      lblLogo.setHorizontalTextPosition(SwingConstants.CENTER);
    }
    {
      panelAnimation = new JPanel();
      this.add(panelAnimation, BorderLayout.SOUTH);
      panelAnimation.setLayout(null);
      panelAnimation.setPreferredSize(new java.awt.Dimension(580, 75));
      {
        lblAnimation1 = new BasicTextLabel(" ");
        lblAnimation1.setOpaque(false);
        lblAnimation1.setBounds(10, 15, 564, 51);
        panelAnimation.add(lblAnimation1);

        lblAnimation2 = new BasicTextLabel(" ");
        lblAnimation2.setOpaque(false);
        lblAnimation2.setBounds(10, 15, 564, 51);
        panelAnimation.add(lblAnimation2);
      }
    }

  }

  /** Create the panelLeagueStandings and add it to the frame */
  private void ihmInit() {
    this.setTitleKey("title.about");

    lblLogo.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("ihm_logo.png")));

    lblAnimation1.setFont(new Font("Tahoma", Font.BOLD, 20));
    lblAnimation2.setFont(new Font("Tahoma", Font.BOLD, 20));

    Animation all = Animations.sequential(new Animation[] { Animations.pause(1000), createAnimation(ClientController.getInstance().getTranslation("app.title") + " " + GameVersion.IHM_VERSION), Animations.pause(1000), createAnimation("Design & Code by"),
        Animations.pause(1000), createAnimation("Bernhard '3b' von Gunten"), Animations.pause(1000), createAnimation("Arik Dasen"), Animations.pause(1000), createAnimation("Contributions by"), Animations.pause(1000), createAnimation("Michael Kolanos"),
        Animations.pause(1000), createAnimation("Greetings fly out to"), Animations.pause(1000), createAnimation("The 'M & M's"), Animations.pause(1000), createAnimation("SC Bern"), Animations.pause(1000), createAnimation("HC Davos"),
        Animations.pause(1000), createAnimation("Calgary Flames"), Animations.pause(1000)

    });

    Animator animator = new Animator(all, 30);
    animator.start();

  }

  private Animation createAnimation(String text) {
    return BasicTextAnimation.defaultFade(lblAnimation1, 3000, text, Color.darkGray);
  }



}
