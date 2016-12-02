package kabalpackage.utilities;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.TableColumn;



/**
 * When the user has finished the solitaire, this class recives the time of
 * completion. The operating systems temporary directory is determined, where 
 * the highscore will be read and written. The highscore will then present the
 * list read from file.
 */
public class HighScore extends JFrame {
    
    private int time;
    private HighScoreTable highScoreTable = new HighScoreTable();
    private JTextField input = new JTextField(20);
    private JFrame frame = new JFrame();
    private JLabel label;
    private String tmpDir;
    
    /** Creates a new instance of HighScore */
    public HighScore(int time) {
        
        this.time = time;
        
        // Set look and feel to GTK
	try {
	    String GTK = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
	    UIManager.setLookAndFeel(GTK);
	    UIManager.installLookAndFeel("GTK", GTK);
	} catch (Exception e) {
	    System.err.println("Could not install GTK");
	}
        
        if (highScoreTable.findOperatingSystem()) {
            presentHighScore();
        } else {
            JOptionPane.showMessageDialog(
                    null
                    , "Could not determine operating system" +
                    ", unable to save highscore");
        }
    }
    
    /**
     * Accesses the HighScoreTable presentable data from file if
     * possible, then puts it into a JTable and packs it in into a JFrame.
     */
    public void presentHighScore() {
        
        frame.setTitle("Register your name and time in the highscore");
        frame.setLayout(new FlowLayout());
        
        String[] columnNames = {"Name", "Score"};
        
        /* 
         * Check wether the file located in the operating systems temporary
         * directory already exists. If not, we make it.
         */
        if (!highScoreTable.fileExists()) {
            File file = new File(tmpDir);
            highScoreTable.writeHighScoreFile();
            highScoreTable.readHighScoreFile();
        } else {
            // If the file exists, we read it.
            highScoreTable.readHighScoreFile();
        }
        
        /*
         * Check wether the time is positive nonzero number, if it is negative,
         * we only present the highscore prepared to viewed without possibility
         * to register in the highscore.
         */
        if (time >= 0) {
            /* 
             * If the time of victory recived is good enough for the 
             * best ten players.
             */
            if (highScoreTable.isGood(time)) {
                // Present the possibility to register your name.
                ButtonListener buttonListener = new ButtonListener();
                input = new JTextField(20);
                frame.add(input);
                frame.add(label);
                frame.add(input);
                JButton button = new JButton("Register " 
                        + highScoreTable.formatTime(time));
                frame.add(button, FlowLayout.RIGHT);
                button.addActionListener(buttonListener);
            }
            else {
                label = new JLabel("<html>Your time did not make it to the " +
                        "highscore... <br>" 
                        + highScoreTable.formatTime(time) + "</html>");
            }
        } else {
            label = new JLabel("The incredible highscorers!");
            frame.add(label);
        }

        JTable presentHighScore = new JTable(highScoreTable.getResults()
        , columnNames);
        presentHighScore.setEnabled(false);
        presentHighScore.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int vColIndex = 1;
        TableColumn col = 
                presentHighScore.getColumnModel().getColumn(vColIndex);
        int width = 150;
        col.setPreferredWidth(width);
        presentHighScore.repaint();
        frame.setPreferredSize(new Dimension(280, 300));
        frame.setResizable(false);
        frame.add(presentHighScore);
        frame.setTitle("Highscores");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
    
    
    /**
     * This class creates a new instance of a Player when the HighScore class 
     * JButton is pressed. If the Player data i correct, the Player will be
     * added to the HighScoreTable. Later the HighScoreTable is sorted, 
     * before the HighScoreTable is presented to the user.
     */
    
    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            Player player = new Player(input.getText(), time);
            if (player.isPlayer()) {
                highScoreTable.addPlayer(player);
                highScoreTable.sortTable();
                highScoreTable.listCleanup();
                System.out.println(player.toString());
                frame.setVisible(false);
                highScoreTable.writeHighScoreFile();
            }
        }
    }
    
    /** 
     * This class represents a Player which we can add to the highscorelist and
     * later store to file.
     */
    
    private class Player implements Serializable {
        
        private String name;
        private int time;
        
        public Player(String name, int time) {
            this.name = name;
            this.time = time;
        }
        
        // Get the players name
        public String getName() {
            return name;
        }
        
        // Get the players accociated time
        public int getTime() {
            return time;
        }
        
        // Present players name and time
        public String toString() {
            return name + ", " + time + " sec";
        }
        
        // Returns true if player has a nonempty name
        public boolean isPlayer() {
            if (name != null) {
                if (name.equalsIgnoreCase("")) {
                    return false;
                } else return true;
            }
            return false;
        }
    }
    
    /**
     * Handles reading and writing to a file with location determined by the 
     * operating system, holds the highscore data and algorithms for sorting,
     * adding, checking, cleaning, presenting and debugging the data list.
     */
    
    private class HighScoreTable implements Serializable {
        
        // This ArrayList holds the highscorelist.
        private ArrayList<Player> list = new ArrayList<Player>();
        
        /** 
         * Writes the highscorelist to HighScore.ser in the temporary directory
         * determined by the operating system.
         */
        
        public void readHighScoreFile() {
            
            FileInputStream in = null;
            ObjectInputStream inObject = null;
            
            try {
                in = new FileInputStream(tmpDir);
                inObject = new ObjectInputStream(in);
                list = (ArrayList<Player>)inObject.readObject();
                inObject.close();
                in.close();
                System.out.println("HighScore.ser read");
            } catch (FileNotFoundException ex) {
                list = new ArrayList<Player>();
                ex.printStackTrace();
            } catch (IOException ex) {
                list = new ArrayList<Player>();
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                list = new ArrayList<Player>();
                ex.printStackTrace();
            } catch (ClassCastException ex) {
                list = new ArrayList<Player>();
                ex.printStackTrace();
            }
        }
        
        /** 
         * Writes the highscorelist to HighScore.ser in the temporary directory
         * determined by the operating system.
         */
        public void writeHighScoreFile() {
            FileOutputStream out = null;
            ObjectOutputStream outObject = null;
            
            try {
                out = new FileOutputStream(tmpDir);
                outObject = new ObjectOutputStream(out);
                outObject.writeObject(list);
            } catch (FileNotFoundException ex) {
                System.out.println(
                        "Could not write highscorelist to file!" +
                        " FileNotFoundException");
                ex.printStackTrace();
            } catch (IOException ex) {
                System.out.println(
                        "Could not write highscorelist to file!" +
                        " IOException");
                ex.printStackTrace();
            }
        }
        
        /**
         * Adds player at the end of highscore list. See sortTable()
         */
        public void addPlayer(Player player) {
            list.add(player);
            System.out.println(list.toString());
        }
        
        /** 
        * Uses the bubblesort algorithm for sorting the list of
        * players by their registered time.
         */
        public void sortTable() {
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    for (int j = 0; j < list.size(); j++) {
                        if (((j + 1) < list.size())) {
                            if (list.get(j).getTime()
                                > 
                                list.get(j + 1).getTime()){
                                Player tmpPlayer = new Player(
                                        list.get(j).getName()
                                        , list.get(j).getTime());
                                // Swap the two players position in the list.
                                list.set(j, list.get(j + 1));
                                list.set(j + 1, tmpPlayer);
                            }
                        }
                    }
                }
            }
            System.out.println("Done with sorting.");
        }
        
        /**
         * Check wether the time accomplished is among the top ten scorers.
         */
        public boolean isGood(int time) {
            if (list.size() < 10) {
                label = new JLabel("Please enter your name");
                return true;
            } else {
                for (int i = 0; i < 11; i++) {
                    if((list.get(i).getTime() > time)) {
                        label = new JLabel("Please enter your name");
                        return true;
                    }
                }
            }
            return false;
        }
        
        /**
         * Check if the HighScore.ser exists in the operating systems
         * temporary directory
         */
        public boolean fileExists() {
            File file = new File(tmpDir);
            return(file.exists()? true : false);
        }
        
        /** 
         * Removes all players in the highscorelist that has a position poorer
         * than 10'th place.
         */
        public void listCleanup() {
            if (list.size() > 10) {
                for (int i = 10; i < list.size(); i++) {
                    list.remove(i);
                    list.trimToSize();
                }
            }
        }
        
        /**
         * Copies the highscorelist of players accociated with time 
         * of victory.
         */
        public String[][] getResults() {
            String[][] returnThis = new String[list.size()][2];
            for (int i = 0; i < list.size(); i++) {
                returnThis[i][0] = list.get(i).getName();
                returnThis[i][1] = 
                        "" + this.formatTime(list.get(i).getTime());
            }
            return returnThis;
        }
        
        /**
         * Checks operating system and determines where we can store temporary
         * files. Returns boolean true if operating system is recognized, 
         * either Win Xp, Win Vista, Linux or Mac OS.
         */
        
        public boolean findOperatingSystem() {
            String os = System.getProperty("os.name");
            System.out.println(
                    "OS Name: " + System.getProperty("os.name"));
            System.out.println(
                    "OS Architecture: " + System.getProperty("os.arch"));
            System.out.println(
                    "OS Version: " + System.getProperty("os.version"));
            os.trim();
            if (os.contains("XP")) {
                tmpDir = (System.getProperty("java.io.tmpdir")+"HighScore.ser");
                System.out.println("Operatingsystem recognized is " + os);
                System.out.println("Storing highscore in " + tmpDir);
                return true;
            }
            if (os.contains("Vista")) {
                tmpDir = (System.getProperty("java.io.tmpdir")+"HighScore.ser");
                System.out.println("Operatingsystem recognized is " + os);
                System.out.println("Storing highscore in " + tmpDir);
                return true;
            }
            if (os.contains("Linux")) {
                tmpDir = (System.getProperty("java.io.tmpdir")
                + "/" + "HighScore.ser");
                System.out.println("Operatingsystem recognized is " + os);
                System.out.println("Storing highscore in " + tmpDir);
                return true;
            }
            if (os.contains("Mac")) {
                tmpDir = (System.getProperty("java.io.tmpdir")
                + "/" + "HighScore.ser");
                System.out.println("Operatingsystem recognized is " + os);
                System.out.println("Storing highscore in " + tmpDir);
                return true;
            }
            String tmpDir = (
                    System.getProperty("java.io.tmpdir")+"HighScore.ser");
            System.out.println(
                    "Operatingsystem not recognized by Solitaire, " + os);
            System.out.println("Storing highscore in " + tmpDir);
            return false;
        }
        
        /**
         * Formates the text displayed on JButton when registering highscore. 
         * Reads seconds elapsed and divides it into hours, min and sec.
         */
        public String formatTime(int time) {
            int hours = time/3600;
            int min = time/60;
            int sec = time%60;
            if (hours > 0) {
                if (hours > 1) {
                    return hours + "hrs, " + min + " min and " + sec + " sec.";
                } else {
                    return hours + "hr, " + min + " min and " + sec + " sec.";
                }
            } else {
                return min + " min and " + sec + " sec";
            }
        }
        
        /**
         * For debugging purpouses. Prints out the highscore to system console,
         * not used, but usable if needed.
         */
        
        public void toSysout() {
            for (int i = 0; i < list.size(); i++) {
                System.out.println("Player " + list.get(i).getName() 
                + " has time " + list.get(i).getTime() + " sec");
            }
        }
    }
}