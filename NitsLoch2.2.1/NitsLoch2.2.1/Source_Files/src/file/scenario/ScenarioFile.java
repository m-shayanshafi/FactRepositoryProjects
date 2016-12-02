/*
 	This file is part of NitsLoch.
 	
 	Copyright (C) 2007--2008  Jonathan Irons

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package src.file.scenario;

import java.io.InputStream;
import java.io.DataInputStream;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import src.Constants;

/**
 * The file format for a Zip-packaged scenario.
 * @author Jonathan Irons
 * date 2/16/2008
 *
 */
public class ScenarioFile {
    
    private static ZipFile file = null;
    private static ScenarioFile instance;

    /**
     * Private constructor. Attempts to detect a zip file called Scenario.nits.
     */
    private ScenarioFile() {
	try {
	    file = new ZipFile(src.scenario.MiscScenarioData.SCENARIO_FILE);
	} catch (ZipException z) {
	    Constants.debugPrint("The file specified is not a zip file.");
	} catch (Exception e) {
	    e.printStackTrace();
	}
	ZipEntry zipEntry = null;
	if (file != null)
	    zipEntry = file.getEntry(Constants.SCENARIO_XML);
	if (zipEntry == null)
	    file = null;
    }
    
    /**
     * Retrieves the running instance of the ScenarioFile. A return value of
     * null indicates that the member ZipFile could not be created. In this
     * case, the ScenarioFile is useless.
     * @return ScenarioFile : The ScenarioFile instance contained in the class.
     */
    public static ScenarioFile getInstance() {
	if (instance == null)
	    instance = new ScenarioFile();
	if (file == null)
	    return null;
	return instance;
    }

    /**
     * Retrieves an InputStream from a given filname. A return value of null
     * indicates that the file could not be found.
     * @param name String : Attempt to get an InputStream from zipped file name.
     * @return InputStream : The InputStream resulting from the file search.
     */
    public InputStream getStream(String name) {
	InputStream stream = null;
	ZipEntry entry = file.getEntry(name);
	if (entry != null) {
	    try {
		stream = file.getInputStream(entry);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	return stream;
    }

    public  byte[] getByteArray(String name) {
	ZipEntry entry = file.getEntry(name);
	byte[] data = null;
	if (entry != null) {
	    try {
		data = new byte[(int)entry.getSize()];
		DataInputStream dis =
		    new DataInputStream(file.getInputStream(entry));
		dis.read(data);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	return data;
    }
}
