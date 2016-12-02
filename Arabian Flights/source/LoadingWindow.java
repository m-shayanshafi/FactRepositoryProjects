// LoadingWindow.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import java.awt.*;
import javax.swing.*;
import java.net.URL;

/** The loading window displays a picture while the game loads.
 * It also provides a message box for loading messags.
 * */
public class LoadingWindow extends Window
{
  private JPanel jPanel1 = new JPanel();
  private JButton jButton1 = new JButton();
  private JLabel jLabel = new JLabel();

  private URL pic;


  public LoadingWindow(URL pic, Frame frame)
  {
    super(frame);
    this.pic = pic;

    jbInit();
  }
  private void jbInit()
  {
    jPanel1.setBackground(Color.black);
    jPanel1.setBounds(new Rectangle(0, 0, 600, 418));
    jPanel1.setLayout(null);
    this.setLayout(null);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    this.setBounds(
        (int)screenSize.getWidth()/2 - 300,
        (int)screenSize.getHeight()/2 - 209,
        600,418);

    jButton1.setBounds(new Rectangle(0, 0, 600, 400));
    jButton1.setIcon(new ImageIcon(pic));

    jLabel.setBounds(0,380,600,50);
    jLabel.setBackground(Color.black);
    jLabel.setForeground(Color.green);
    jLabel.setText("Loading...");
    jLabel.setVisible(true);

    this.add(jPanel1, null);
    jPanel1.add(jButton1, null);
    jPanel1.add(jLabel, null);
  }

  public void setMessage(String string)
  {
    jLabel.setText(string);
  }

}