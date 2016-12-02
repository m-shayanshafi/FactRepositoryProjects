# 101Games – A framework for simple board games in Java

The code and documentation is mostly in Norwegian.

## Running

The main entry point is in src/inf101/games/Main101Games.java, which can be run as
either an application or an applet.

## Extending

The code comes with one example game, Conway's Life (in src/inf101/games/life).

Further games can be added to the menus in the application by implementing the
IGame interface, and then adding it to the GUI in the ```startGame``` method
of ```Main101Games```. For example:

         public static GameGUI startGame() {
             return new GameGUI(Arrays.asList(new Life(15, 17), new MyGame(15, 17))); 
         }

         
## Authors

 * Anya Helene Bagge, University of Bergen
 * Anna Eilertsen, University of Bergen
 * Alexandre Vivmond, University of Bergen
