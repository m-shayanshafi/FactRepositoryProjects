package Game;

/**
 * Created by piano_000 on 9/5/2015.
 *
 * TextHandler is implemented using the Singleton pattern
 * it routes all text through this class so it may be easier
 * redirected to the console, a log file, or a server
 */
public class TextHandler {

    private static TextHandler firstInstance = null;
    private Boolean isLocalGame = true;

    private void TextHandler(Boolean localGame) {
        isLocalGame = localGame;
    }

    private void TextHandler() {}

    public static TextHandler getInstance() {
        if(firstInstance == null) {
            firstInstance = new TextHandler();
        }
        return firstInstance;
    }

    /*
    Sends text to the console
     */
    public void printToConsole(String printString) {
        if(isLocalGame){
            System.out.println(printString);
        } else {
            //TODO Add code to send multiplayer data to connected members
        }
    }

    public void printToLog() {

    }
}
