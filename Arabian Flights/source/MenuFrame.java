// MenuFrame.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;
import java.net.URL;
import java.io.*;

public class MenuFrame extends JFrame {
  JPanel contentPane;
  JLabel titleLabel = new JLabel();
  JList levelList = new JList();
  JButton launchButton = new JButton();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextPane manualTextArea = new JTextPane();
  JLabel jLabel1 = new JLabel();
  JCheckBox invertMouseCheckButton = new JCheckBox();
  JCheckBox exclusiveCheckButton = new JCheckBox();
  JSlider mouseSlider = new JSlider(0, 100);
  JLabel jLabel2 = new JLabel();
  JCheckBox randomCheckBox = new JCheckBox();
  JSpinner monsterSpinner = new JSpinner();
  JLabel jLabel3 = new JLabel();
  JButton websiteButton = new JButton();

  //Construct the frame
  public MenuFrame()
  {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);

    jbInit();

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    this.setBounds(
        (int) screenSize.getWidth() / 2 - 200,
        (int) screenSize.getHeight() / 2 - 240,
            400,480);

    populateList();

    levelList.setSelectedIndex(0);

    showManual();
  }

  private void showManual()
  {
    StringBuffer manual = new StringBuffer();

    try
    {
      BufferedReader reader = new BufferedReader(new InputStreamReader(
          ClassLoader.getSystemResourceAsStream(GameFrame.
                                                STRING_MANUAL_FILE)));

      while (reader.ready())
      {
        manual.append(reader.readLine() + "\n");
      }
    }
    catch (IOException e)
    {
      return;
    }

    manualTextArea.setText(manual.toString());
  }

  private void populateList()
  {
    Vector listData = new Vector();
    levelList.setListData(listData);

    Vector files = new Vector();

    try
    {
      BufferedReader reader = new BufferedReader(new InputStreamReader(
          ClassLoader.getSystemResourceAsStream(GameFrame.STRING_WORLD_LIST_FILE)));
      while (reader.ready())
      {
        String world = reader.readLine();

        if (world == null || world.length() == 0) break;
        else files.add(world);

        System.out.println("Adding world: " + world);
      }
    }
    catch (IOException e)
    {
      return;
    }

    for (int i=0; i<files.size(); i++)
    {
      listData.add((String)files.get(i));
    }

    if (listData.size() == 0) levelList.setVisible(false);
  }

  //Component initialization
  private void jbInit()
  {
    contentPane = (JPanel) this.getContentPane();
    titleLabel.setBackground(Color.white);
    titleLabel.setFont(new java.awt.Font("Dialog", 3, 30));
    titleLabel.setForeground(Color.black);
    titleLabel.setText("Arabian Flights");
    titleLabel.setBounds(new Rectangle(7, 7, 224, 36));
    contentPane.setLayout(null);
    this.getContentPane().setBackground(Color.black);
    this.setResizable(false);
    this.setSize(new Dimension(400, 480));
    this.setTitle("Arabian Flights - version 1.3");
    contentPane.setBackground(Color.lightGray);
    levelList.setBorder(BorderFactory.createLoweredBevelBorder());
    levelList.setBorder(BorderFactory.createLoweredBevelBorder());
    levelList.setBounds(new Rectangle(6, 67, 220, 92));
    launchButton.setBackground(UIManager.getColor("Button.background"));
    launchButton.setBounds(new Rectangle(247, 112, 125, 69));
    launchButton.setFont(new java.awt.Font("Dialog", 1, 20));
    launchButton.setText("Launch !");
    launchButton.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        launchButton_actionPerformed(e);
      }
    });
    jScrollPane1.setBounds(new Rectangle(7, 195, 385, 248));
    manualTextArea.setEditable(false);
    manualTextArea.setText("");
    jLabel1.setText("Choose a map:");
    jLabel1.setBounds(new Rectangle(12, 51, 117, 15));
    invertMouseCheckButton.setBackground(Color.lightGray);
    invertMouseCheckButton.setText("Invert Mouse");
    invertMouseCheckButton.setBounds(new Rectangle(246, 36, 104, 23));
    invertMouseCheckButton.setSelected((GameFrame.MOUSEX_SCALE < 0) ? true : false);
    exclusiveCheckButton.setBackground(Color.lightGray);
    exclusiveCheckButton.setText("Fullscreen Exclusive");
    exclusiveCheckButton.setBounds(new Rectangle(246, 12, 149, 23));
    exclusiveCheckButton.setSelected(GameFrame.SCREEN_EXCLUSIVE);
    mouseSlider.setBackground(Color.lightGray);
    mouseSlider.setBounds(new Rectangle(235, 84, 157, 24));
    mouseSlider.setValue((int)(GameFrame.MOUSEY_SCALE * 10000));
    mouseSlider.setMajorTickSpacing(10);
    mouseSlider.setPaintTicks(true);
    jLabel2.setText("Mouse Sensitivity");
    jLabel2.setBounds(new Rectangle(248, 64, 129, 15));
    randomCheckBox.setBackground(Color.lightGray);
    randomCheckBox.setOpaque(true);
    randomCheckBox.setText("randomize");
    randomCheckBox.setBounds(new Rectangle(8, 165, 109, 23));
    randomCheckBox.setSelected(GameFrame.RANDOM_MONSTERS);
    monsterSpinner.setBackground(Color.lightGray);
    monsterSpinner.setBounds(new Rectangle(107, 165, 52, 24));
    monsterSpinner.setValue(new Integer(GameFrame.NUM_RANDOM_MONSTERS));
    jLabel3.setText("monsters");
    jLabel3.setBounds(new Rectangle(163, 168, 57, 15));
    websiteButton.setBounds(new Rectangle(149, 48, 73, 19));
    websiteButton.setMargin(new Insets(0, 0, 0, 0));
    websiteButton.setText("website");
    websiteButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        websiteButton_actionPerformed(e);
      }
    });
    contentPane.add(titleLabel, null);
    contentPane.add(levelList, null);
    contentPane.add(launchButton, null);
    contentPane.add(jScrollPane1, null);
    contentPane.add(jLabel1, null);
    jScrollPane1.getViewport().add(manualTextArea, null);
    contentPane.add(exclusiveCheckButton, null);
    contentPane.add(invertMouseCheckButton, null);
    contentPane.add(jLabel2, null);
    contentPane.add(mouseSlider, null);
    contentPane.add(websiteButton, null);
    contentPane.add(monsterSpinner, null);
    contentPane.add(randomCheckBox, null);
    contentPane.add(jLabel3, null);
  }
  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e)
  {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING)
    {
      System.exit(0);
    }
  }

  void launchButton_actionPerformed(ActionEvent e)
  {
    GameFrame.SCREEN_EXCLUSIVE = exclusiveCheckButton.isSelected();

    GameFrame.MOUSEX_SCALE = mouseSlider.getValue() * -.0001f;

    GameFrame.MOUSEY_SCALE = mouseSlider.getValue() * -.0001f
        * (invertMouseCheckButton.isSelected() ? -1 : 1);

    GameFrame.STRING_WORLD_FILE = "maps/" + (String)levelList.getSelectedValue() + ".world";

    GameFrame.RANDOM_MONSTERS = randomCheckBox.isSelected();
    Integer in = (Integer)(monsterSpinner.getValue());
    GameFrame.NUM_RANDOM_MONSTERS = in.intValue();

    new SingleLauncher().start();
  }

  private class SingleLauncher extends Thread
  {
    public void run()
    {
      GameFrame.loadingWindow.setVisible(true);
      GameFrame.loadSingleplayer();
    }
  }

  void websiteButton_actionPerformed(ActionEvent ev)
  {
    try
    {
      URL e = new URL("http://mikeprosser.tripod.com");
      Runtime.getRuntime().exec("cmd /c start " + e.toString());
    }
    catch (Exception ex) { ex.printStackTrace(); }
  }
}