package org.gcs.robot;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.MessageFormat;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The RCInfo class is a view of the game's highlights.
 * It observes the game model and examines the current score & level,
 * diplaying the highest ones obtained. It also displays random kudos
 * after a new high score. As a convenience, mouse clicks in the panel
 * allow the player to jump out of harm's way.
 *
 * @see RCModel
 * @serial exclude
 * @author John B. Matthews
 */
public class RCInfo extends JPanel implements MouseListener, Observer {

    private RCModel game;
    private JLabel score = new JLabel("", JLabel.CENTER);
    private JLabel message = new JLabel("", JLabel.CENTER);
    private Random random = new Random(System.currentTimeMillis());
    private int highScore = RCPrefs.getHighScore();
    private int highLevel = RCPrefs.getHighLevel();
    private int highJumps = RCPrefs.getHighJumps();
    private Cursor moveCursor = new Cursor(Cursor.MOVE_CURSOR);
    private Cursor sysCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    private MessageFormat scoreForm = new MessageFormat(
        "High score: {0} on level {1} with {2} jumps");
    private static final int height = 24;
    private static final int textHeight = 12;
    private static final String helpMessage = "Press H for help.";
    private static final String resetMessage = "High score reset.";
    private static final Color c = RCImage.borderColor;
    private static final String [] messageArray = new String [] {
        "New high score!",
        "Well done!",
        "A new personal best!",
        "Good show!",
        "A stunning achievment!"
    };

    /**
     * Construct an informative view of the specified game.
     *
     * @param game a game board
     */
    public RCInfo(RCModel game) {
        this.game = game;
        this.setLayout(new GridLayout(1, 2));

        score.setForeground(Color.LIGHT_GRAY);
        score.setBackground(Color.DARK_GRAY);
        score.setOpaque(true);
        score.setFont(new Font("SansSerif", Font.BOLD, textHeight));
        score.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 2, c));
        score.setText(scoreMessage());

        message.setForeground(Color.LIGHT_GRAY);
        message.setBackground(Color.DARK_GRAY);
        message.setOpaque(true);
        message.setFont(new Font("SansSerif", Font.BOLD, textHeight));
        message.setBorder(BorderFactory.createMatteBorder(1, 2, 2, 1, c));
        this.showSize();

        this.setBackground(Color.DARK_GRAY);
        this.add(message);
        this.add(score);

        this.addMouseListener(this);
        this.setCursor(moveCursor);
        this.game.addObserver(this);
    }

    /** Return this panel's preferred size. */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(0, height);
    }

    /** Update the game's highlights. */
    public void update(Observable model, Object arg) {
        int currentScore = game.getDeadRobots();
        int currentLevel = game.getLevel();
        int currentJumps = game.getSafeJumps();
        if ((currentScore > highScore) ||
            (currentLevel > highLevel) ||
            (currentJumps > highJumps)) {
            highScore = currentScore;
            highLevel = currentLevel;
            highJumps = currentJumps;
            message.setText(randomMessage());
            score.setText(scoreMessage());
            RCPrefs.putHighScore(highScore);
            RCPrefs.putHighLevel(highLevel);
            RCPrefs.putHighJumps(highJumps);
        }
    }

    /** Clear the user's previous high score & level; reset the game. */
    public void resetHilights() {
        highScore = 0;
        highLevel = 0;
        highJumps = 0;
        game.resetGame();
        message.setText(resetMessage);
    }

    /** Show the current board size. */
    public void showSize() {
        message.setText(helpMessage + " [" +
            game.getWidth() + "x" + game.getHeight() + "]");
    }

    /** Show the board size while resizing. */
    public void showSize(int w, int h) {
        message.setText(helpMessage + " [" + w + "x" + h + "]");
    }

    private String scoreMessage() {
        Object [] scoreArgs = {highScore, highLevel, highJumps};
        return scoreForm.format(scoreArgs);
    }

    private String randomMessage() {
        int randomIndex = random.nextInt(messageArray.length);
        return messageArray[randomIndex];
    }

    /** Handle mouseClicked events. */
    public void mouseClicked(MouseEvent e) {
        game.move();
    }

    /** Handle mouseEntered events to set the cursor. */
    public void mouseEntered(MouseEvent e) { setCursor(moveCursor); }

    /** Handle mouseExited events to restore the cursor. */
    public void mouseExited(MouseEvent e) { setCursor(sysCursor); }

    /** Handle mouseReleased events. */
    public void mouseReleased(MouseEvent e) { }

    /** Handle mousePressed events. */
    public void mousePressed(MouseEvent e) {}

}