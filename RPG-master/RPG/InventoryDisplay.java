
/**
 * Write a description of class InventoryDisplay here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JLayeredPane;
import javax.swing.ImageIcon;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class InventoryDisplay
{
    public static void InventoryDisplay()    {
        JPanel panel = new JPanel(new GridLayout(5,1));
        
        JLayeredPane healthPotionPane = new JLayeredPane();
        JLayeredPane energyPotionPane = new JLayeredPane();
        JLayeredPane bloodlustPotionPane = new JLayeredPane();
        JLayeredPane rejuvinationPotionPane = new JLayeredPane();
        JLayeredPane lifePotionPane = new JLayeredPane();      

        try {
            ImageIcon healthPotion = new ImageIcon(ImageIO.read(new File("Potions/Health Potion.png")));
            ImageIcon energyPotion = new ImageIcon(ImageIO.read(new File("Potions/Energy Potion.png")));
            ImageIcon bloodlustPotion = new ImageIcon(ImageIO.read(new File("Potions/Bloodlust Potion.png")));
            ImageIcon rejuvinationPotion = new ImageIcon(ImageIO.read(new File("Potions/Rejuvination Potion.png")));
            ImageIcon lifePotion = new ImageIcon(ImageIO.read(new File("Potions/Life Potion.png")));
        
            JLabel healthPotionImage = new JLabel(healthPotion);
            JLabel energyPotionImage = new JLabel(energyPotion);
            JLabel bloodlustPotionImage = new JLabel(bloodlustPotion);
            JLabel rejuvinationPotionImage = new JLabel(rejuvinationPotion);
            JLabel lifePotionImage = new JLabel(lifePotion);
            
            healthPotionImage.setBounds(0,0,100,100);
            energyPotionImage.setBounds(0,0,100,100);
            bloodlustPotionImage.setBounds(0,0,100,100);
            rejuvinationPotionImage.setBounds(0,0,100,100);
            lifePotionImage.setBounds(0,0,100,100); 
            
            healthPotionImage.setVisible(true);
            energyPotionImage.setVisible(true);
            bloodlustPotionImage.setVisible(true);
            rejuvinationPotionImage.setVisible(true);
            lifePotionImage.setVisible(true);
        
            healthPotionPane.add(healthPotionImage, new Integer(0));
            energyPotionPane.add(energyPotionImage, new Integer(0));
            bloodlustPotionPane.add(bloodlustPotionImage, new Integer(0));
            rejuvinationPotionPane.add(rejuvinationPotionImage, new Integer(0));
            lifePotionPane.add(lifePotionImage, new Integer(0));
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }      
        
        JLabel displayNumberOfHealthPotion = new JLabel();
        JLabel displayNumberOfEnergyPotion = new JLabel();
        JLabel displayNumberOfBloodlustPotion = new JLabel();
        JLabel displayNumberOfRejuvinationPotion = new JLabel();
        JLabel displayNumberOfLifePotion = new JLabel();
        
        displayNumberOfHealthPotion.setBounds(0,80,20,20);
        displayNumberOfEnergyPotion.setBounds(0,80,20,20);
        displayNumberOfBloodlustPotion.setBounds(0,80,20,20);
        displayNumberOfRejuvinationPotion.setBounds(0,80,20,20);
        displayNumberOfLifePotion.setBounds(0,80,20,20);
        
        int numberOfHealthPotion = 0;
        int numberOfEnergyPotion = 0;
        int numberOfBloodlustPotion = 0;
        int numberOfRejuvinationPotion = 0;
        int numberOfLifePotion = 0;
        
        for (Item s: Inventory.getListOfPotions())    {
            if (s.getName()!=null)    {
                if (s.getName().equals("Potion of Health"))   {
                    numberOfHealthPotion++;
                }
                else if (s.getName().equals("Potion of Energy"))  {
                    numberOfEnergyPotion++;
                }
                else if (s.getName().equals("Potion of Bloodlust"))   {
                    numberOfBloodlustPotion++;
                }
                else if (s.getName().equals("Potion of Rejuvination"))    {
                    numberOfRejuvinationPotion++;
                }
                else if (s.getName().equals("Potion of Life"))    {
                    numberOfLifePotion++;
                }
            }
        }
        
        displayNumberOfHealthPotion.setText("X" + Integer.toString(numberOfHealthPotion));
        displayNumberOfEnergyPotion.setText("X" + Integer.toString(numberOfEnergyPotion));
        displayNumberOfBloodlustPotion.setText("X" + Integer.toString(numberOfBloodlustPotion));
        displayNumberOfRejuvinationPotion.setText("X" + Integer.toString(numberOfRejuvinationPotion));
        displayNumberOfLifePotion.setText("X" + Integer.toString(numberOfLifePotion));
        
        displayNumberOfHealthPotion.setVisible(true);
        displayNumberOfEnergyPotion.setVisible(true);
        displayNumberOfBloodlustPotion.setVisible(true);
        displayNumberOfRejuvinationPotion.setVisible(true);
        displayNumberOfLifePotion.setVisible(true);
        
        healthPotionPane.add(displayNumberOfHealthPotion, new Integer(1));
        energyPotionPane.add(displayNumberOfEnergyPotion, new Integer(1));
        bloodlustPotionPane.add(displayNumberOfBloodlustPotion, new Integer(1));
        rejuvinationPotionPane.add(displayNumberOfRejuvinationPotion, new Integer(1));
        lifePotionPane.add(displayNumberOfLifePotion, new Integer(1));
        
        healthPotionPane.setVisible(true);
        energyPotionPane.setVisible(true);
        bloodlustPotionPane.setVisible(true);
        rejuvinationPotionPane.setVisible(true);
        lifePotionPane.setVisible(true);
        
        panel.add(healthPotionPane);
        panel.add(energyPotionPane);
        panel.add(bloodlustPotionPane);
        panel.add(rejuvinationPotionPane);
        panel.add(lifePotionPane);
        
        panel.add(healthPotionPane);
        panel.add(energyPotionPane);
        panel.add(bloodlustPotionPane);
        panel.add(rejuvinationPotionPane);
        panel.add(lifePotionPane);
        
        panel.setVisible(true);
    }
}
