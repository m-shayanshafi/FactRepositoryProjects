package com.kbrahney.pkg.board.games.Noughts_And_Crosses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;

/**
 *
 * @author Kieran
 */
public class GUI extends JFrame {

    private int numRows;
    private int numCols;
    private char defaultChar;

    private JLabel playerLabel = new JLabel("Current player: ");
    private JTextField currentPlayer = new JTextField("");
    private JLabel p1Label = new JLabel("Player 1 symbol: ");
    private JTextField p1Field = new JTextField("");
    private JLabel p2Label = new JLabel("Player 2 symbol: ");
    private JTextField p2Field = new JTextField("");
    private JButton resetButton = new JButton("Reset");

    private JButton[][] btns;
    private static JTextField displayBox = new JTextField("");

    private Noughts_And_Crosses nc;

    public GUI(int nRows, int nCols, char c) {
        nc = new Noughts_And_Crosses(nRows, nCols, c);

        numRows = nRows;
        numCols = nCols;
        defaultChar = c;

        displayBox.setEditable(false);
        currentPlayer.setEditable(false);
        p1Field.setEditable(false);
        p2Field.setEditable(false);
        currentPlayer.setText("" + nc.getPlayer());

        resetButton.setEnabled(false);

        setTitle("Board Game");
        setSize(400, 400);

        JPanel pnl = new JPanel(new BorderLayout());
        setContentPane(pnl);

        JPanel topPanel = new JPanel(new GridLayout(4, 1));
        topPanel.add(playerLabel); topPanel.add(currentPlayer);
        topPanel.add(p1Label); p1Field.setText(Character.toString(nc.getPlayer1Char())); topPanel.add(p1Field);
        topPanel.add(p2Label); p2Field.setText(Character.toString(nc.getPlayer2Char())); topPanel.add(p2Field);
        topPanel.add(resetButton);
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetGame();
                resetButton.setEnabled(false);
            }
        });

        JPanel gridPanel = new JPanel(new GridLayout(numRows, numCols));
        btns = new JButton[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int k = 0; k < numCols; k++) {
                btns[i][k] = new JButton(Character.toString(defaultChar));

                final int row = i, col = k;

                btns[i][k].addActionListener(new ActionListener()  {
                    public void actionPerformed(ActionEvent e) {
                        placeMark(row, col);
                    }
                });

                gridPanel.add(btns[i][k]);
            }
        }

        pnl.add(topPanel, BorderLayout.NORTH);
        pnl.add(gridPanel, BorderLayout.CENTER);
        pnl.add(displayBox, BorderLayout.SOUTH);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void placeMark(int row, int col) {
        if (row != -1 && col != -1) {
            if (nc.isValidCoord(row, col) && !nc.hasWon() && !nc.isDraw()) {
                if (nc.placeMark(row, col)) {
                    if (nc.getPlayer() == 1)
                        btns[row][col].setText(Character.toString(nc.getPlayer1Char()));
                    else if (nc.getPlayer() == 2)
                        btns[row][col].setText(Character.toString(nc.getPlayer2Char()));

                    nc.switchPlayer();
                    currentPlayer.setText("" + nc.getPlayer());

                    if (nc.hasWon() || nc.isDraw())
                        resetButton.setEnabled(true);
                }
            }
        }
    }

    private void resetGame() {
        /* reset array */
        nc.initialise();
        
        /* reset player */
        nc.setPlayer(1);
        currentPlayer.setText("" + nc.getPlayer());

        /* reset gui */
        for (int i = 0; i < btns.length; i++)
            for (int k = 0; k < btns[0].length; k++)
                btns[i][k].setText("-");
    }

    public static void setDisplayText(String txt) {
        displayBox.setText(txt);
    }

}
