package GUI;

/**
 * Created by doga on 23/12/14.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SubMenu extends JPanel {

    protected JLabel title;
    protected GameFrame gameFrame;
    protected JButton backButton;

    public SubMenu(GameFrame frame){
        gameFrame = frame;
        setBackground(new Color(238, 233, 233));
        setLayout(null);
        setSize(640, 480);
        backButton = new JButton("Back");
        backButton.addActionListener(new ButtonListener());
        backButton.setBounds(10, 420, 107, 33);

        title = new JLabel("");
        title.setBounds(248,13,228,45);
        title.setFont(new Font("Arial", 1, 36));
        title.setForeground(new Color(108, 166, 205));

        add(title);
        add(backButton);

        backButton.setFont(new Font("Arial", 1, 20));


    }
    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == backButton) {
                gameFrame.switchPanel("main");
            }
        }
    }
}
