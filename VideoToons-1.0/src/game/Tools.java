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

import java.awt.Rectangle;
import java.io.*;


/************************** TOOLS *******************************/

public class Tools
{

 // waits ms milliseconds

 static public void waitTime( int ms )
 {
    Object o = new Object();
    
    synchronized( o )
    {
      try{
         o.wait(ms);
      }
      catch(InterruptedException e) {}
    }
 }
 
 
 // Restrictions on Rectangles to fit the screen

  static public void fitOnScreen( Rectangle r )
  {
    if(r.x<0)
    {
      r.width += r.x;
      r.x = 0;             
    }
    else if( r.x+r.width >= 640 )
    {
        r.width = 640 - r.x;
    }

    if(r.width<0)
          r.width = 0;
  }


 // Compares two strings
  static boolean equal( String s1, String s2 )
  {
     if(s1.compareTo(s2)==0)
       return true;
     return false;
  }


  // index descending sort
  // we don't return the initial array but an index table
  // giving pointing on the sorted elements
  public static int[] sort( int tab[], int nbelm )
  {
     int index[] = new int[nbelm];
     int tab2[] = new int[nbelm];
     int nb_invert;

     for(int i=0; i<nbelm; i++) {
        index[i]=i;
        tab2[i] = tab[i];
     }

     do
     {
     	nb_invert = 0;
     	
        for(byte i=0; i<nbelm-1; i++)
          if(tab2[i]<tab2[i+1]) {
            nb_invert++;
            int tmp = tab2[i+1];
            tab2[i+1]=tab2[i];
            tab2[i]=tmp;
            
            tmp = index[i+1];
            index[i+1]=index[i];
            index[i]=tmp;
          }

       nbelm--;
     }
     while(nb_invert!=0);

    return index;
  }

  /*************************************************************************************/

    public static void sort( String list[] )
    {
      int nb_invert;
      int nbelm = list.length-1;

      do
      {
     	nb_invert = 0;
     	
        for(int i=0; i<nbelm; i++)
          if(list[i].compareTo(list[i+1])>0) {
            nb_invert++;
            String tmp = list[i+1];
            list[i+1]=list[i];
            list[i]=tmp;
          }

        nbelm--;
      }
      while(nb_invert!=0);
    }


  /*************************************************************************************/
  
   /** Saves the serializable object to the file pointed by filename.
    *
    * @param filename the complete filename (ex: /infres/pub/bob.dat )
    * @param data the object to save ( must implement the Serializable interface )
    *
    * @return true on success, false on failure
    */

   public static boolean saveObjectToFile( String filename, Object data )
   {
      try
      {
        ObjectOutputStream o_out = new ObjectOutputStream( new FileOutputStream(filename));
        o_out.writeObject(data);
        o_out.flush();
        o_out.close();
      }
      catch(IOException e) {
        System.out.println("Error: "+e.getMessage());
        return false;
      }
   
      return true;	
   }

 
  /************************************************************************************/
  
   /** Loads the serializable object pointed by the filename.
    *
    * @param filename the complete filepath (ex: /infres/pub/bob.dat )
    *
    * @return an object on success, null on failure
    */

   public static Object loadObjectFromFile( String filename )
   {
     Object data;

      try
      {
        ObjectInputStream o_in = new ObjectInputStream( new FileInputStream(filename));

          try{
               data = o_in.readObject();
               o_in.close();
          }
          catch(ClassNotFoundException ec ) {
            System.out.println("Error: "+ec.getMessage());
            data = null;
          }
      }
      catch(IOException e) {
         System.out.println("Error: "+e.getMessage());
         data = null;
      }
   
      return data;	
   }

  /********************************************************************************/
  
   /** returns the list of all the subdirectories...
    *
    * @param base_path the base path
    *
    * @return a String array on success, null on failure
    */

   public static String[] getPathList( String base_path )
   {
      File s_dir = new File(base_path);

   // is it really a directory ?
      if(!s_dir.isDirectory() )
           return null;	

      String list[];
       
        File s_list[] = s_dir.listFiles();
        int l=0;
        
          for( int i=0; i<s_list.length; i++ )
              if( s_list[i].isDirectory() )
      	          l++;

          if(l==0) return null;
          list = new String[l];
          l=0;

       // ...and we copy the file names
          for( int i=0; i<s_list.length; i++ )
          {
            if( s_list[i].isFile() )
      	          continue;

            list[l] = s_list[i].getName();
            l++;
          }

      return list;
   }


  /**********************************************************************************/

   /** Loads the config file pointed by filename then searches for
    *  the specified variable name. If it finds it, it returns its value.
    *  The config file is assumed to have the following aspect :
    *
    *  IMPORTANT: Variable names are case-sensitive
    *
    *  Config file example:
    *  
    *   # some comments
    *
    *     MY_VARIABLE_1 = my value 1
    *     my_variable_2 = /my/value2    # other comment
    *
    *
    * @param filename the complete filename (ex: /infres/pub/config.txt )
    * @param variable the exact variable name you want the value from...
    *
    * @return a string representing the variable's value on success, null on failure
    */

    public static String readConfigFile( String filename, String variable )
    {
       String value = null;
       String textline;
       int pos;

        try
        {
          BufferedReader r_in = new BufferedReader( new FileReader(filename));

           while( (textline = r_in.readLine())!=null )
           {
             // if there are comments on this line we remove them...
                pos=textline.indexOf("#");
                
                if( pos>0 )
                     textline = textline.substring(0,pos);
                else if(pos==0)
                     continue;

             // is there the variable pattern ?
                if( ( pos=textline.indexOf(variable)) != -1 )
                {
                  // is there a '=' ?
               	     pos = textline.indexOf( "=", pos+variable.length() );
               	    
               	  // ok we have the variable's value... we clean it (trim)...
               	     if(pos!=-1){
               	        textline = textline.substring(pos+1,textline.length());     
               	        value = textline.trim();
               	        break;
                     }
                }
           }

           r_in.close();
        }
        catch(IOException e) {
            System.out.println("Error: "+e.getMessage());
            value = null;
        }

      return value;
    }

  /*********************************************************************************/

    public static int findStringIndex( String tab[], String name )
    {
        for(int i=0; i<tab.length; i++)
          if(tab[i].compareTo(name)==0)
             return i;
    	
    	return -1;
    }

  /*********************************************************************************/
}