/*
 * Classname			: DragonChess
 * Author			: Davy Herben, Christophe Hertigers
 * Description			: Our little game of DragonChess
 * GPL disclaimer		:
 *   This program is free software; you can redistribute it and/or modify it
 *   under the terms of the GNU General Public License as published by the
 *   Free Software Foundation; version 2 of the License.
 *   This program is distributed in the hope that it will be useful, but
 *   WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *   or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *   for more details. You should have received a copy of the GNU General
 *   Public License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

//FIXME Add a debugging/verbose method....

/* package import */
import java.lang.*;
import javax.swing.UIManager;
import main.*;
//import backend.*;
import gui2d.*;

/**
 * The game of DragonChess (by Gary Gygax)
 *
 * <p>This is the start class containing the main() function.
 * It contains a backend and a frontend instance. It provides
 * no other functionality than creating these instances.
 *
 * @author				Davy Herben
 * @author				Christophe Hertigers
 * @author				Koen Nys
 * @author				Karen Theunis
 * @author				Dimitri Holsteens
 * @author				John Franssen
 * @author				Koenraad Heijlen
 * @version				2001/12/16
 */ 
public class DragonChess {

	/*
	 * VARIABLES
	 *
	 */
	public static Boolean			DEBUG;
	public static Boolean			VERBOSE;

	/* CLASS VARIABLES */

	/* INSTANCE VARIABLES */ 
	//private	static		DCGame				myDCGame;
	private	static 		DC2dGUI				myDC2dGUI;
	//private	static 		DCTestGUI			myDCTestGUI;

	/*
	 * CONSTRUCTORS
	 *
	 */

	/**
	 * Class constructor. Does nothing
	 */
	public DragonChess() {}

	/*
	 * METHODS
	 *
	 */
	/**
	 * Main function. Starts a back-end and a front-end.
	 * Which front-end to use is determined by the first command line parameter.
	 * This behaviour is not implemented yet.
	 *
	 * @param args[] 	the standard array of command line parameter strings
	 */
	public static void main(String args[]) {
		MyOptionsParser myOptions = new MyOptionsParser();
		Object guiOption, lookAndFeelOption;
		
		try {
			myOptions.parse(args);
		}
		catch ( CmdLineParser.UnknownOptionException e ) {
			System.err.println(e.getMessage());
			printUsage();
			System.exit(2);
		}
		catch ( CmdLineParser.IllegalOptionValueException e ) {
			System.err.println(e.getMessage());
			printUsage();
			System.exit(2);
		}

		CmdLineParser.Option[] allOptions =
			new CmdLineParser.Option[] { MyOptionsParser.VERBOSE,
			MyOptionsParser.DEBUG
	//		, MyOptionsParser.GUI 
			};
			
			if ( myOptions.getOptionValue(MyOptionsParser.DEBUG) != null ) {
				DEBUG = Boolean.TRUE;
				System.out.println("DEBUG = Boolean.TRUE");
			}
			else 
				DEBUG = Boolean.FALSE;

			if ( DEBUG == Boolean.TRUE )  {
				System.out.println("DEBUG: Command Line Arguments:");
				System.out.println("Default args:");
				for ( int j = 0; j<allOptions.length; ++j ) {
					System.out.println(allOptions[j].longForm() + ": " +
							myOptions.getOptionValue(allOptions[j]));
				}

				String[] otherArgs = myOptions.getRemainingArgs();
				System.out.println("Remaining args: ");
				for ( int i = 0; i<otherArgs.length; ++i ) {
					System.out.println(otherArgs[i]);
				}
			}
			
			lookAndFeelOption =	myOptions.getOptionValue(
												MyOptionsParser.LOOKANDFEEL);
			if ( lookAndFeelOption != null ) {
				try {
	        		UIManager.setLookAndFeel(lookAndFeelOption.toString());
			    } catch (Exception e) { 
					System.out.println(
								"Unable to process Look & Feel, using default");
					try {
						UIManager.setLookAndFeel(
    	                    UIManager.getCrossPlatformLookAndFeelClassName());
					} catch (Exception ex) { }
				}
			} else {
				try {
			        UIManager.setLookAndFeel(
			            UIManager.getCrossPlatformLookAndFeelClassName());	
			    } catch (Exception e) { }
			}
			
	//		guiOption = myOptions.getOptionValue(MyOptionsParser.GUI);
	//		if (( guiOption == null ) || ( guiOption.equals("2d") )) {
    //                           //myDCGame = new DCGame();
    //                           myDC2dGUI = new DC2dGUI();
    //                  }
    //                  else if (( guiOption.equals("test") )  || 
	//											( guiOption.equals("text") )) {
    //                           //myDCGame = new DCGame();
    //                           myDCTestGUI = new DCTestGUI();
    //                  }
	//		else {
	//			printUsage();
	//		}
	
	//		Gui choosing is disabled for now, so always use 2dGui
			myDC2dGUI = new DC2dGUI();

	}

	private static void printUsage() {
		System.err.println("usage: Dragonchess 	 \n\n" +
			"\t[{-v,--verbose}] \t\t be verbose \n" +
			"\t[{-d,--debug}] \t\t\t output debbugging information\n"+
	//		"\t[{-g,--gui} 2d|test] \t\t use 2d or test gui \n" +
	//		"\t                     \t\t The default gui is 2d \n" +
			"\t[{-l,--lookandfeel} look&feel] \t use specified look & feel \n" +
			"\t                            \t default : metalLookAndFeel \n"
			);
	}

	/* INNER CLASS 
	 * Option Parser
	 */

	private static class MyOptionsParser extends CmdLineParser {

		public static final Option VERBOSE = new
		CmdLineParser.Option.BooleanOption('v',"verbose");

		public static final Option DEBUG = new
		CmdLineParser.Option.BooleanOption('d',"debug");

	//	public static final Option GUI = new
	//	CmdLineParser.Option.StringOption('g',"gui");

		public static final Option LOOKANDFEEL = new
		CmdLineParser.Option.StringOption('l',"lookandfeel");

		public MyOptionsParser() {
			super();
			addOption(VERBOSE);
			addOption(DEBUG);
	//		addOption(GUI);
			addOption(LOOKANDFEEL);
		}
	}

} 

/* END OF FILE */
