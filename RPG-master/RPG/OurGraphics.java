import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * Write a description of class Graphics here.
 * 
 * @author (your name) Sukrit Arora
 * @version (a version number or a date)
 */
public class OurGraphics
{
    // instance variables
    final JLayeredPane openingPane;
    final JPanel menuPanel;
    final JButton inventory;
    final JButton loadout;
    final JButton stats;
    final JButton save;
    final JButton quit;
    final JButton back;
    final JButton menuButton;
    final JPanel menuOptionDisplay;
    ImageDrawer openingImage; 

    public OurGraphics()
    {
        openingPane = new JLayeredPane(); //Constructs a new instance of the JLayeredPane class with identifier openingPane
        
        menuOptionDisplay = new JPanel();//Constructs a new instance of the JPanel class with identifier menuOptionDisplay

        menuButton = new JButton(new ImageIcon("Resources/Assets/menuicon.png")); //Constructs a new instance of the JButton class with identifier menuButton with explicit parameter of a new instance of the ImageIcon class with explicit parameter of the directory in which the image is located
        menuButton.setOpaque(false); //Invokes the setOpaque method with explicit parameter of false on the menuButton object
        menuButton.setContentAreaFilled(false);//Invokes the setContentAreaFilled method with explicit parameter of false on the menuButton object
        menuButton.setBorderPainted(false);//Invokes the setBorderPainted method with explicit parameter of false on the menuButton object
        menuButton.setBounds((int)util.screen_size.getWidth()-50, 0, 50, 50); //Invokes the setBounds method on the menuButton object to make it at the top right corner of any screen and a size of 50 x 50

        menuPanel = new JPanel(null); //Constructs a new instance of the JPanel class with identifier menuPanel with explicit parameter of null (to reset any LayoutManagers)
        menuPanel.setBounds(0, 0, (int)util.screen_size.getWidth(), (int)util.screen_size.getHeight());  //Invokes the setBounds method on the menuPanel object to make cover the whole area of any screen
        menuPanel.setBackground(Color.black);  //Invokes the setBackground method on the menuPanel object to make the background black
        
        inventory = new JButton("Inventory"); //Constructs a new instance of the JButton class with identifier inventory with explicit parameter of a string
        loadout = new JButton("Loadout"); //Constructs a new instance of the JButton class with identifier loadout with explicit parameter of a string
        stats = new JButton("Stats"); //Constructs a new instance of the JButton class with identifier stats with explicit parameter of a string
        save = new JButton("Save"); //Constructs a new instance of the JButton class with identifier save with explicit parameter of a string
        quit = new JButton("Quit"); //Constructs a new instance of the JButton class with identifier quit with explicit parameter of a string
        back = new JButton(new ImageIcon()); //Constructs a new instance of the JButton class with identifier back with explicit parameter of a new empty instance of the ImageIcon class
        
        back.setOpaque(true); //Invokes the setOpaque method with explicit parameter of true on the back object
        back.setContentAreaFilled(false); //Invokes the setContentAreaFilled method with explicit parameter of false on the bcak object
        back.setBorderPainted(true); //Invokes the setBorderPainted method with explicit parameter of true on the back object
        back.setIcon(new ImageIcon("Resources/Assets/backicon.png")); //Invokes the setIcon method in the back object to a new instance of the ImageIcon class with explicit parameter of the directory in which the image is located
 
        inventory.setBounds(((int)util.screen_size.getWidth()/4)-150, ((int)util.screen_size.getHeight()/4)-130, 300, 100); //Invokes the setBounds method on the inventory object to make it positioned on any screen with a size 300 x 100
        loadout.setBounds(((int)util.screen_size.getWidth()/4)-150, ((int)util.screen_size.getHeight()/4)-5, 300, 100);  //Invokes the setBounds method on the loadout object to make it positioned on any screen with a size 300 x 100
        stats.setBounds(((int)util.screen_size.getWidth()/4)-150, ((int)util.screen_size.getHeight()/4)+120, 300, 100);  //Invokes the setBounds method on the stats object to make it positioned on any screen with a size 300 x 100
        save.setBounds(((int)util.screen_size.getWidth()/4)-150, ((int)util.screen_size.getHeight()/4)+245, 300, 100);  //Invokes the setBounds method on the save object to make it positioned on any screen with a size 300 x 100
        quit.setBounds(((int)util.screen_size.getWidth()/4)-150, ((int)util.screen_size.getHeight()/4)+370, 300, 100);  //Invokes the setBounds method on the quit object to make it positioned on any screen with a size 300 x 100
        back.setBounds(0, 0, 100, 100);  //Invokes the setBounds method on the back object to make it positioned on the top left of any screen with a size 100 x 100

        menuPanel.add(inventory); //Invokes the add method on the menuPanel object with the explicit parameter of the JButton instance inventory in order to add it to the frame
        menuPanel.add(loadout); //Invokes the add method on the menuPanel object with the explicit parameter of the JButton instance loadout in order to add it to the frame
        menuPanel.add(stats); //Invokes the add method on the menuPanel object with the explicit parameter of the JButton instance stats in order to add it to the frame
        menuPanel.add(save); //Invokes the add method on the menuPanel object with the explicit parameter of the JButton instance save in order to add it to the frame
        menuPanel.add(quit); //Invokes the add method on the menuPanel object with the explicit parameter of the JButton instance quit in order to add it to the frame
        menuPanel.add(back); //Invokes the add method on the menuPanel object with the explicit parameter of the JButton instance back in order to add it to the frame

        openingImage = new ImageDrawer(0); //Constructs a new instance of the ImageDrawer class with identifier openingImage and explicit parameter of 0 to retrieve the first image
        openingImage.setBounds(0, 0, (int)util.screen_size.getWidth(),(int)util.screen_size.getHeight()); //Invokes the setBounds method on the openingImage object to make cover the whole area of any screen

        class ButtonListener implements ActionListener //An inner class to respond to button presses
        {
            public void actionPerformed(ActionEvent e)
            {
                if (e.getSource() == quit) //If the source of the button press is the quit button
                {
                    System.exit(0); //Invokes the exit method on the System class with explicit parameter 0 to exit the game
                }

                if (e.getSource() == menuButton) //If the source of the button press is the menu button
                {
                    openingPane.add(menuPanel, new Integer(2)); //Invokes the add method onto the openingPane object with explicit parameters of the menuPanel (what is added) and a new instance of the Integer wrapper class (the layer to which it is added)

                }
                
                if (e.getSource() == back) //If the source of the button press is the back button
                { 
                    openingPane.remove(openingPane.getIndexOf(menuPanel)); //Invokes the remove method onto the openingPane object with explicit parameter of the returned value from the invocation of the getIndexOf method on the openingPane object with explicit paramter of the menuPanel object
                    Starter.frame.repaint(); //Invokes the repaint method on the final variable of the Started class in order to repaint the main frame

                }

            }
        }

        class MainPlayerMover implements MouseListener //An inner class to respond to mouse events
        {
            public void mouseClicked(MouseEvent e)
            {
        
               
                moveThreader.cancel(); //Sid please comment code this
                GameLogic.character.moveTo(new Point((int)util.screen_point_adjust(e.getPoint()).getX(), piecewise_handler.getY((int)util.screen_point_adjust(e.getPoint()).getX(), Starter.getCurrentFrame())) , 500);
            }

            public void mousePressed(MouseEvent e)
            {

            }

            public void mouseReleased(MouseEvent e)
            {

            }

            public void mouseEntered(MouseEvent e)
            {

            }

            public void mouseExited(MouseEvent e)
            {

            }
        }

        quit.addActionListener(new ButtonListener()); //Invokes the addActionListener method onto the quit object with the explicit parameter of a new instance of the inner ButtonListener class
        menuButton.addActionListener(new ButtonListener()); //Invokes the addActionListener method onto the menuButton object with the explicit parameter of a new instance of the inner ButtonListener class
        Starter.frame.addMouseListener(new MainPlayerMover()); //Invokes the addMouseListener method onto the the final object frame from the starter class with the explicit parameter of a new instance of the inner MainPlayerMover class
        back.addActionListener(new ButtonListener()); //Invokes the addActionListener method onto the back object with the explicit parameter of a new instance of the inner ButtonListener class

        SaveGame gameSave = new SaveGame(save,Inventory.getListOfPotions()); //Constructs a new instance of the SaveGame class with identifier gameSave and explicit parameters of the save JButton object, and the list of potions from the Inventory class
        gameSave.saveGame(); //Inovkes the saveGame method onto the gameSave object

    }

    public void addEntity(Entity e)
    {
        //Sid please comment code this   
        e.setBounds(0,0, (int)util.screen_size.getWidth(), (int)util.screen_size.getHeight());
        openingPane.add(e, new Integer(1));
    }

    public JLayeredPane getGraphics(int chapterNum) //Given the explicit variable of the chapter number, will return the layered pane with the necessary elements
    {
        switch (chapterNum)
        {
            case 0: //If it is the opening chapter
            {
                openingPane.add(menuButton, new Integer(1)); //The menu button is added to the first layer
                openingPane.add(openingImage, new Integer(0)); //The opening image is added to the bottom layer
                return openingPane; //The pane is returned

            }
            default: 
            {
                return null;
            }
        }

    }


}
