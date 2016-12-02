/*
 *  :tabSize=4:indentSize=4:noTabs=true:
 *  :folding=explicit:collapseFolds=1:
 *
 *  Copyright (C) 2007 Thomas Dilts.  This program is free
 *  software; you can redistribute it and/or modify it under the terms of the
 *  GNU General Public License as published by the Free Software Foundation;
 *  either version 2 of the License, or (at your option) any later version. This
 *  program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details. You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software Foundation,
 *  Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA For more
 *  information, surf to www.tarot.lazy8.nu or email tarot@lazy8.nu
 */

package nu.lazy8.oracle.engine;

import java.util.*;
import java.io.*;


public interface OracleIO {
	public void resetSetup(boolean resetSetup,boolean resetFiles,boolean resetSavedReadings);
	public void savePersistantSetup()throws Exception;
	public void setProperty( String key, String value ) ;
	public void setProperty( String key, int value ) ;
	public void setProperty( String key, boolean value );
	public int getIntProperty( String key ) ;
	public boolean getBooleanProperty( String key ) ;
	public void setBooleanProperty( String key,boolean value ) ;
	public String getProperty( String key ) ;
	public String getProperty( String key,String defaultValue ) ;
	public SaveableReading[] getReadings() throws Exception ;
	public void saveReadings( SaveableReading[] saveIt ) throws Exception;
	public Object getImage( String fileName, int imageNumber ) ;
	public InputStream getImageFileContentsStream( String fileName )throws Exception;
	public InputStream getInputStream( String fileName ) throws Exception;
    public boolean LoadSetupFile(String filename,boolean doAdd,StringBuffer errorMessage);
    //	public void saveTarImageFile( String fileName, InputStream inStream ) throws Exception;
}


