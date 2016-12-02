package org.gcs.robot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The RCStatus class is a view of the game's status.
 * It observes the game model and updates the current score, etc.
 *
 * @see RCModel
 * @serial exclude
 * @author John B. Matthews
 */
public class RCStatus extends JPanel implements Observer {

    private static final int height = 28;
    private static final int textHeight = 16;
    private RCModel game;
    private JLabel status = new JLabel("", JLabel.CENTER);
    private Color c = RCImage.borderColor;

    /**
     * Construct a status view of the specified game.
     *
     * @param game a game board
     */
    public RCStatus(RCModel game) {
        this.game = game;

        status.setForeground(Color.LIGHT_GRAY);
        status.setBackground(Color.DARK_GRAY);
        status.setOpaque(true);
        status.setFont(new Font("SansSerif", Font.BOLD, textHeight));

        this.setBackground(Color.DARK_GRAY);
        this.setBorder(BorderFactory.createMatteBorder(2, 2, 1, 2, c));
        this.add(status);

        this.game.addObserver(this);
    }

    /** Return this panel's preferred size. */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(0, height);
    }

    /** Update the game's status display. */
    public void update(Observable model, Object arg) {
        if (game.won()) status.setText(
            "You won! Click the mouse or press a key for the next level.");
        else if (game.lost()) status.setText(
            "You died! Click the mouse or press the N key for a new game.");
        else status.setText(
            "Level: "  + game.getLevel() + "  " +
            "Score: "  + game.getDeadRobots() + "  " +
            "Robots: " + game.getLiveRobots() + "  " +
            "Jumps: "  + game.getSafeJumps()
            );
    }

}