package net.sf.bloodball.view;

import java.awt.event.ActionEvent;
import javax.swing.*;
import net.sf.bloodball.GameController;
import net.sf.bloodball.resources.*;

public class MenuBar {

  private JMenuBar menuBar;

  private JMenu getHelpMenu() {
    JMenu helpMenu = new JMenu(ResourceHandler.getString(ResourceKeys.HELP_MENU));
    helpMenu.add(new LocalizedAction(ResourceKeys.ABOUT_MENUITEM) {
      public void actionPerformed(ActionEvent e) {
        AboutDialog.showDialog();
      }
    });
    return helpMenu;
  }

  private JMenu getGameMenu(final GameController controller) {
    JMenu menu = new JMenu(ResourceHandler.getString(ResourceKeys.GAME_MENU));
    menu.add(new LocalizedAction(ResourceKeys.NEW_GAME_MENUITEM) {
      public void actionPerformed(ActionEvent e) {
        controller.startNewGame();
      }
    });
    menu.add(new LocalizedAction(ResourceKeys.QUIT_MENUITEM) {
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });
    return menu;
  }

  public MenuBar(GameController controller) {
    menuBar = new JMenuBar();
    menuBar.add(getGameMenu(controller));
    menuBar.add(getHelpMenu());
  }

  public JMenuBar getMenuBar() {
    return menuBar;
  }
}