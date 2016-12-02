import java.util.*;
import java.io.*;
/*
 * TextIO is a consolidation of methods (primarily ease-of-use) for delaying, clearing, or displaying logos.
 */
public class TextIO
{
    /*
     * delay sleeps the thread for a passed-in time. Exceptions are caught but not dealt with.
     */
    public void delay(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException exp) {
        }
    }

    /*
     * For ease of use when writing the story, made clear command. Seems this only works RELIABLY on BlueJ.
     */
    public final static void clear()
    {
        System.out.print ('\f');
    }

    /*
     * Dramatic is used for when a line has to be outputted and then cleared. Saves lines in the grand scheme
     * of things, regardless of how frivolous it seems now.
     */
    public void dramatic(int time, String dialogue){
        for(char c : dialogue.toCharArray()){
            System.out.print(c);
            delay(40);
        }
        delay(time);
        clear();
    }

    /*
     * A simpler way, rather than typing out System.out.println() every time.
     */
    public void say(Object dialogue){
        System.out.println(dialogue);
    }

    /*
     * Mainly used for logos, blank() offers a simple way of entering spaces.
     */   
    public void blank(int lines){
        for (int i = 0; i<lines; i++){
            System.out.println();
        }
    }

    /*
     * Because I don't want to create a new scanner every time in every class.
     */
    public int getInt(String question){
        Scanner scanner = new Scanner(System.in);
        say(question);
        return (scanner.nextInt());
    }

    public String getString(String question){
        Scanner scanner = new Scanner(System.in);
        say(question);
        String response = scanner.nextLine();
        return response;
    }
}
