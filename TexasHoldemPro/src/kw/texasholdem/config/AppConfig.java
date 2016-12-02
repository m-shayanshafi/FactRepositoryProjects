package kw.texasholdem.config;

public class AppConfig {

	public static final String BACKGROUND_IMG = "/images/texas-holdem-strategies.png";
	public static final String BACKGROUND_PORKER_TABLE_IMG = "/images/poker-table-layout.png";
	
	public static final String PLAYERS_FILE_PATH = "players.dat";
	public static final String TABLE_FILE_PATH = "table.dat";
	
	public static final int APP_WIDTH = 915;
	public static final int APP_HEIGHT = 631;
	
	public static final int APP_WIDTH_2 = 915 + 200;
	public static final int APP_HEIGHT_2 = 700;
	
	
	
	public static StartingPoint getMap(int i, int numOfPlayers) {
		switch(i) {
		case 0:
        	if(numOfPlayers == 4) {
        		return new StartingPoint(375, 490);
        	} else if (numOfPlayers == 5) {
        		return new StartingPoint(375, 470);
        	} else if (numOfPlayers == 6) {
        		return new StartingPoint(375, 470);
        	} else if (numOfPlayers == 7) {
        		return new StartingPoint(375, 470);
        	} else if (numOfPlayers == 8) {
        		return new StartingPoint(375, 470);
        	} 
            break;
        case 1:
        	if(numOfPlayers == 4) {
        		return new StartingPoint(10, 230);
        	} else if (numOfPlayers == 5) {
        		return new StartingPoint(25, 420);
        	} else if (numOfPlayers == 6) {
        		return new StartingPoint(20, 380);
        	} else if (numOfPlayers == 7) {
        		return new StartingPoint(30, 400);
        	} else if (numOfPlayers == 8) {
        		return new StartingPoint(100, 420);
        	} 
            break;
        case 2:
        	if(numOfPlayers == 4) {
        		return new StartingPoint(375, 0);
        	} else if (numOfPlayers == 5) {
        		return new StartingPoint(65, 15);
        	} else if (numOfPlayers == 6) {
        		return new StartingPoint(20, 25);
        	} else if (numOfPlayers == 7) {
        		return new StartingPoint(0, 75);
        	} else if (numOfPlayers == 8) {
        		return new StartingPoint(0, 220);
        	} 
            break;
        case 3:
        	if(numOfPlayers == 4) {
        		return new StartingPoint(735, 230);
        	} else if (numOfPlayers == 5) {
        		return new StartingPoint(700, 15);
        	} else if (numOfPlayers == 6) {
        		return new StartingPoint(375, 0);
        	} else if (numOfPlayers == 7) {
        		return new StartingPoint(235, 0);
        	} else if (numOfPlayers == 8) {
        		return new StartingPoint(100, 30);
        	} 
            break;
        case 4:
        	if(numOfPlayers == 5) {
        		return new StartingPoint(740, 420);
        	} else if (numOfPlayers == 6) {
        		return new StartingPoint(750, 25);
        	} else if (numOfPlayers == 7) {
        		return new StartingPoint(505, 0);
        	} else if (numOfPlayers == 8) {
        		return new StartingPoint(375, 0);
        	} 
        	break;
        case 5:
        	if (numOfPlayers == 6) {
        		return new StartingPoint(750, 380);
        	} else if (numOfPlayers == 7) {
        		return new StartingPoint(745, 75);
        	} else if (numOfPlayers == 8) {
        		return new StartingPoint(650, 30);
        	} 
        	break;
        case 6:
        	if (numOfPlayers == 7) {
        		return new StartingPoint(720, 400);
        	} else if (numOfPlayers == 8) {
        		return new StartingPoint(748, 220);
        	} 
        	break;
        case 7:
        	if (numOfPlayers == 8) {
        		return new StartingPoint(650, 420);
        	} 
        	break;
        default:
        	
		}
		return new StartingPoint(-1, -1);
	}

}
