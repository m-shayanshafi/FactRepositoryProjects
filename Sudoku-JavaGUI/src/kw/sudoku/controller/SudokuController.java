package kw.sudoku.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import kw.sudoku.model.Game;
import kw.sudoku.model.UpdateAction;
import kw.sudoku.view.Field;
import kw.sudoku.view.SudokuPanel;

/**
 * This class controls all user actions from SudokuPanel.
 *
 * @author Ken Wu @ New York
 */
public class SudokuController implements MouseListener {
    private SudokuPanel sudokuPanel;    // Panel to control.
    private Game game;                  // Current Sudoku game.

    /**
     * Constructor, sets game.
     *
     * @param game  Game to be set.
     */
    public SudokuController(SudokuPanel sudokuPanel, Game game) {
        this.sudokuPanel = sudokuPanel;
        this.game = game;
    }

    /**
     * Recovers if user clicked field in game. If so it sets the selected number
     * at clicked position in game and updates clicked field. If user clicked a
     * field and used left mouse button, number at clicked position will be
     * cleared in game and clicked field will be updated.
     *
     * @param e MouseEvent.
     */
    public void mousePressed(MouseEvent e) {
        JPanel panel = (JPanel)e.getSource();
        Component component = panel.getComponentAt(e.getPoint());
        
        if (component instanceof Field) {
            Field field = (Field)component;
            int x = field.getFieldX();
            int y = field.getFieldY();

            if (e.getButton() == MouseEvent.BUTTON1 && (game.getNumber(x, y) == 0 || field.getForeground().equals(Color.BLUE))) {
                int number = game.getSelectedNumber();
                if (number == -1)
                    return;
                game.setNumber(x, y, number);
                field.setNumber(number, true);
            } else if (e.getButton() == MouseEvent.BUTTON3 && !field.getForeground().equals(Color.BLACK)) {
                game.setNumber(x, y, 0);
                field.setNumber(0, false);
            }
            sudokuPanel.update(game, UpdateAction.CANDIDATES);
        }
    }

    public void mouseClicked(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
}