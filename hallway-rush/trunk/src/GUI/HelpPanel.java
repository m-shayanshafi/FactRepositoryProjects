package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


/**
 * Created by mertuygur on 21/12/14.
 */
public class HelpPanel extends SubMenu {
    private GameFrame gameFrame;
    private JLabel arrowsLabel;
    private BufferedImage arrows;

    public HelpPanel(GameFrame frame)
    {
        super(frame);
        title.setText("Help");
        title.setBounds(248,13,228,45);
        try {
            arrows = ImageIO.read(getClass().getResource("arrows.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        arrowsLabel = new JLabel(new ImageIcon(arrows));
        arrowsLabel.setBounds(170,40,200,100);
        add(arrowsLabel);
    }

}
