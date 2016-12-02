
/**
 * Write a description of class SaveGame here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveGame
{

    public static JButton button; //making instances so it can be initialised by the constructor so the method has acess to it

    public static Item[] backpack;

    public SaveGame(JButton b,Item[] pack) {
        button = b; //amking the instance varaible the value passed in by the explicit parameter
        backpack = pack;
    }

    public void saveGame()  {

        class saveGameClicker implements ActionListener { //inner class to add to the button to get it to work
            public void actionPerformed(ActionEvent e)  { //overriding the mehod in actionListerner
                JFileChooser fileChooser = new JFileChooser(); //making an instance of this class which you can invoke upon later

                fileChooser.setDialogTitle("Specify a file to save");   //setting title 

                int userSelection = fileChooser.showSaveDialog(button); //displaying the option of what to name the file and save

                if (userSelection == JFileChooser.APPROVE_OPTION) { // if the file is approved then execute
                    File fileToSave = fileChooser.getSelectedFile(); //creating the file to save it into
                    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileToSave)))   { //try this and the out object reference only exists for this try catch
                        out.writeObject(backpack);  //writing the array of backpack into the file
                    }
                    catch (IOException ioe) {
                        //do something if there is an error, at least this so you
                        //know if something went wrong
                        ioe.printStackTrace();
                    }
                }
            }
        }

        button.addActionListener(new saveGameClicker()); //adding the actionlistener to the button
    }
}

