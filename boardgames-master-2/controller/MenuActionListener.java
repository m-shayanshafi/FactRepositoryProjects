package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import view.UI;

public class MenuActionListener implements ActionListener{
//	private static SaveManager sm =  new SaveManager();;
		  public void actionPerformed(ActionEvent e) {
		      JMenu jm = this.getMenuBarMenu((JMenuItem)e.getSource());
			 UI ui = (UI)jm.getRootPane().getParent();
		 //  MainUI ui =(MainUI) menu.getParent();
		   String command = e.getActionCommand();
		   if(command.equals("Restart")){
			   ui.setRollPanelVisible(false);
			   ui.setScorePanelVisible(false);
			  ui.launchWelcome();
		   }
        }

			  public JMenu getMenuBarMenu(JMenuItem item)
			  {
			      JMenuItem menu = null;

			      while (menu == null)
			      {
			          JPopupMenu popup = (JPopupMenu)item.getParent();
			          item = (JMenuItem)popup.getInvoker();

			          if (item.getParent() instanceof JMenuBar)
			                  menu = item;
			      }

			      return (JMenu)menu;
			  }
		  

}
