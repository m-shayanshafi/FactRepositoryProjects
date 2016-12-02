package org.gcs.robot;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * RCHelp  displays the game's instructions.
 *
 * @serial exclude
 * @author John B. Matthews
 */
public class RCHelp extends JDialog implements ActionListener {

    private static final String helpText = "/help.html";
    private JFrame parent;
    
    /**
     * Construct an instance of RCHelp.
     *
     * @param parent the parent JFrame for this help dialog.
     */
    public RCHelp(JFrame parent) {
        super(parent, true);
        this.parent = parent;
        this.setLocationByPlatform(true);
        this.setLayout(new BorderLayout(10, 10));
        this.add(genTopPanel(), BorderLayout.NORTH);
        this.add(genCenterPanel(), BorderLayout.CENTER);
        this.add(genBottomPanel(), BorderLayout.SOUTH);
        this.add(genWestPanel(), BorderLayout.WEST);
        Component box = Box.createRigidArea(new Dimension(10, 0));
        this.add(box, BorderLayout.EAST);
        this.pack();
    }
    
    private JPanel genTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ImageIcon icon = RCImage.scale(RCImage.Robot, 64, 64);
        JLabel label = new JLabel(
            "What will you do when the robots come for you?",
            icon, JLabel.LEADING);
        label.setIconTextGap(10);
        panel.add(label);
        return panel;
    }
    
    private JPanel genWestPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JPanel p1 = new JPanel();
        Component box = Box.createRigidArea(new Dimension(10, 0));
        p1.add(box);
        panel.add(p1);

        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
        p2.add(genIconLabel("You", RCImage.Me));
        p2.add(Box.createVerticalGlue());
        p2.add(genIconLabel("Robot", RCImage.Robot));
        p2.add(Box.createVerticalGlue());
        p2.add(genIconLabel("Bomb", RCImage.Bomb));
        p2.add(Box.createVerticalGlue());
        p2.add(genIconLabel("Dead", RCImage.Dead));
        p2.add(Box.createVerticalGlue());
        p2.add(genIconLabel("Post", RCImage.Post));
        p2.add(Box.createVerticalGlue());
        p2.add(genIconLabel("Wreck", RCImage.Wreck));
        panel.add(p2);

        return panel;
    }
    
    private JLabel genIconLabel(String name, Image image) {
        ImageIcon icon = RCImage.scale(image, 48, 48);
        JLabel label = new JLabel(name, icon, JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setHorizontalTextPosition(JLabel.CENTER);
        return label;
    }
    
    /** Show the help dialog. */
    private JScrollPane genCenterPanel() {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setPreferredSize(new Dimension(450, 500));
        URL url = RCHelp.class.getResource(helpText);
        if (url != null) {
            try {
                editorPane.setPage(url);
            } catch (IOException e) {
                String s = "Unable to setPage(" + url.toString() + ")";
                editorPane.setText(s);
            }
        } else {
            String s = "Unable to find " + helpText + ".";
            editorPane.setText(s);
        }
        return scrollPane;
    }

    private JPanel genBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton closeButton = new JButton("Close");
        closeButton.setActionCommand("close");
        closeButton.setToolTipText("Close the help window.");
        panel.add(closeButton);
        this.getRootPane().setDefaultButton(closeButton);
        closeButton.requestFocusInWindow();
        closeButton.addActionListener(this);

        return panel;
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if ("close".equals(cmd)) {
            this.setVisible(false);
        }
    }
}