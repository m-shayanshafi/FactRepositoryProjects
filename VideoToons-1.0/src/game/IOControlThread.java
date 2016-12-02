/*
 * VideoToons. A Tribute to old Video Games.
 * Copyright (C) 2001 - Bertrand Le Nistour
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package videotoons.game;

class IOControlThread extends Thread implements GameDefinitions
{

 /************************** DATA ACCESS CONTROL ****************************/

    public static final byte DATA_INPUT          = 1;
    public static final byte DATA_OUTPUT         = 2;
    public static final byte DATA_TRANSFERT_DONE = 3;

 // Here is an easy way to use objects to synchronize threads...
    private static byte IOLock[] = new byte[1];
    private static byte CounterLock[] = new byte[1];

 // IO_mode = [ DATA_INPUT, DATA_OUTPUT ]
    private static byte IO_mode;
    
 // IO_counter = [0...IO_max]
    private static byte IO_counter;

 // IO_max is initialized at player_number, but can decrement if player quits.
    private static byte IO_max;

 // The initial number of players
    private static byte player_number;


 /**************************************************************************/
 /******************************* CONSTRUCTOR ******************************/
 /**************************************************************************/
 
 // Constructor used by the ServerLevelProcess
    public IOControlThread( String s_th, byte player_number ) {
       super( s_th );
       resetIOParameters( player_number );
    }

 // Constructor used by GameServerThreads
    public IOControlThread( String s_th ) {
       super( s_th );
    }


 /**************************************************************************/
 /******************************** RESET ***********************************/
 /**************************************************************************/

    public static void resetIOParameters(byte playernumber)
    {
         IO_counter = 0;
         IO_mode = NONE;
         player_number = playernumber;
         IO_max = player_number;
    }

 /**************************************************************************/
 /******************************* IO MODES *********************************/
 /**************************************************************************/

    public static void waitForIOMode( byte mode )
    {
        synchronized( IOLock ){
           while( IO_mode != mode ) {
             try{
                  IOLock.wait();
             } catch(InterruptedException e) {}
           }
        }
    }

    public static void setIOMode( byte mode )
    {
        synchronized( IOLock )
        {
           if(IO_mode!=mode){
              IO_mode=mode;
              IOLock.notifyAll();
           }
        }
    }


 /**************************************************************************/
 /****************************** IO COUNTER ********************************/
 /**************************************************************************/

 // Increments the IO counter. 
 // If should_block = true we wait until the IO_counter == IO_max.
 // It returns true for the first process to reach the IO_max limit.
 
    public static boolean incrIOCounter( boolean should_block )
    {
        synchronized( CounterLock )
        {
           IO_counter++;        	

           if( IO_counter >= IO_max )
           {
              IO_counter = 0;  // we automatically reset the counter

              if (should_block==true)
                 CounterLock.notifyAll();

              return true;
           }

           if(should_block==true)
              try{
                   CounterLock.wait();
                  }catch(InterruptedException e) {}           
        }
        
      return false;
    }

 /**************************************************************************/

 // Resets the IO counter.
    public static void resetIOCounter(){
        synchronized( CounterLock ){
            IO_counter = 0;
        }
    }

 /**************************************************************************/

 // Decrements the IO_max limit (a player has left).
    public static void decrPlayerLimit()
    {
        synchronized( CounterLock )
        {
           IO_max--;

           if(IO_max<=IO_counter)
              CounterLock.notifyAll();
        }
    }

}