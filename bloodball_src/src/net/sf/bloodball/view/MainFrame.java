package net.sf.bloodball.view;

import de.vestrial.util.swing.JFrameHelper;
import de.vestrial.util.swing.WindowExitAdapter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.sf.bloodball.GameController;
import net.sf.bloodball.resources.ResourceHandler;
import net.sf.bloodball.resources.ResourceKeys;

public class MainFrame extends JFrame {
  private JPanel mainPanel;
  private GameBoard gameBoard;
  private JButton endTurnButton = new JButton("Dummy");
  private java.awt.event.ActionListener endTurnButtonListener;
  private StatusBar statusBar = new StatusBar();
  private ToolBar toolBar = new ToolBar();
  private JLabel scoreBoard = new JLabel("0 : 0 ");

  public void showMessageDialog(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

  public MainFrame(GameBoard gameBoard, GameController gameController) {
    super(ResourceHandler.getString(ResourceKeys.FRAME_TITLE));
    this.gameBoard = gameBoard;
    buildAndAddMainPanel();
    setJMenuBar(new MenuBar(gameController).getMenuBar());
    addWindowListener(new WindowExitAdapter());
    pack();
    JFrameHelper.centerOnScreen(this);
  }

  private void buildAndAddMainPanel() {
    mainPanel = new JPanel(new BorderLayout());
    getContentPane().add(mainPanel);
    mainPanel.add(getGameBoardPanel(), BorderLayout.CENTER);
    mainPanel.add(statusBar.getComponent(), BorderLayout.SOUTH);
    JPanel north = new JPanel(new BorderLayout());
    north.add(toolBar.getComponent(), BorderLayout.CENTER);
    north.add(scoreBoard, BorderLayout.EAST);
    mainPanel.add(north, BorderLayout.NORTH);
    toolBar.addTool(endTurnButton);
  }

  public JPanel getGameBoardPanel() {
    JPanel panel = new JPanel();
    panel.add(gameBoard);
    panel.setBackground(Colors.LINE);
    return panel;
  }

  public void setActionListener(ActionListener listener) {
    endTurnButton.removeActionListener(endTurnButtonListener);
    endTurnButtonListener = listener;
    endTurnButton.addActionListener(endTurnButtonListener);
  }

  public void setEndTurnButtonEnabled(boolean enabled) {
    endTurnButton.setEnabled(enabled);
  }

  public void setEndTurnButtonText(String text) {
    endTurnButton.setText(text);
  }

  public void setStatusBarColor(Color color) {
    statusBar.setTextColor(color);
  }

  public void setStatusBarText(String text) {
    statusBar.setText(text);
  }

  public void setScore(int homeTouchdowns, int guestTouchdowns) {
    scoreBoard.setText(homeTouchdowns + " : " + guestTouchdowns + " ");
  }

}