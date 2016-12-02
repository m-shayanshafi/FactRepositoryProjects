package com.kbrahney.pkg.gui;

import com.kbrahney.pkg.Configuration;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Kieran
 */
public class SplashScreen extends JFrame
{

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    private JMenuBar menuBar = new JMenuBar();
    
    private JMenu fileMenu = new JMenu("File");
    private JMenuItem exit = new JMenuItem("Exit");
    
    private JMenu helpMenu = new JMenu("Help");
    private JMenuItem about = new JMenuItem("About");
    private JMenuItem updates = new JMenuItem("Check for updates");

    private JLabel boardSizeLabel = new JLabel("Please enter a board size: ");
    private JLabel rowsLabel = new JLabel("Number of rows: ");
    private JTextField rowsField = new JTextField("3");
    private JLabel colsLabel = new JLabel("Number of columns: ");
    private JTextField colsField = new JTextField("3");

    private JLabel selectLabel = new JLabel("Select a game: ");
    private JComboBox gamesList = new JComboBox(Configuration.getGames());
    private JButton playButton = new JButton("Play");

    public SplashScreen()
    {
        setTitle("Board Games");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set up the menu
        setJMenuBar(menuBar);
        menuBar.add(fileMenu); fileMenu.add(exit);
        menuBar.add(helpMenu); helpMenu.add(about); helpMenu.add(updates);

        // Set content pane
        JPanel mainPnl = new JPanel(new GridLayout(4, 1));
        setContentPane(mainPnl);

        // Add content to the content pane
        JPanel inputPnl = new JPanel(new GridLayout(2, 2));
        mainPnl.add(boardSizeLabel, BorderLayout.NORTH);
        inputPnl.add(rowsLabel); inputPnl.add(rowsField);
        inputPnl.add(colsLabel); inputPnl.add(colsField);

        mainPnl.add(inputPnl, BorderLayout.CENTER);

        JPanel gamesPnl = new JPanel(new FlowLayout());
        gamesPnl.add(selectLabel);
        gamesPnl.add(gamesList);
        mainPnl.add(gamesPnl);

        mainPnl.add(playButton, BorderLayout.SOUTH);

        pack();

        // Determine the new location of the window
        int x = ((screenSize.width - getContentPane().getSize().width) / 2);
        int y = ((screenSize.height - getContentPane().getSize().height) / 2);
        // Move the window
        setLocation(x, y);

        /* Add action listners */
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!rowsField.getText().equals("") && !colsField.getText().equals("")) {
                    Configuration.setBoardWidth(Integer.parseInt(rowsField.getText()));
                    Configuration.setBoardHeight(Integer.parseInt(colsField.getText()));

                    String game = Configuration.getGame(gamesList.getSelectedIndex());
                    if (game != null) {
                        try {
                            Class c = Class.forName("com.kbrahney.pkg.board.games." + game + ".GUI");
                            c.getDeclaredConstructor(int.class, int.class, char.class).newInstance(Configuration.getBoardWidth(), Configuration.getBoardHeight(), Configuration.getDefaultChar());
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(SplashScreen.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InstantiationException ex) {
                            Logger.getLogger(SplashScreen.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(SplashScreen.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (NoSuchMethodException ex) {
                            Logger.getLogger(SplashScreen.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InvocationTargetException ex) {
                            Logger.getLogger(SplashScreen.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
    }

}
