/*	
	This file is part of NitsLoch.

	Copyright (C) 2007 Darren Watts

    NitsLoch is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    NitsLoch is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with NitsLoch.  If not, see <http://www.gnu.org/licenses/>.
 */

package src.scenario;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import src.file.scenario.ScenarioFile;

/**
 * This is a singleton class will contain a hash map of BufferedImages.
 * The key is the path to the image, and the value is a BufferedImage.
 * @author Darren Watts
 * date 11/20/07
 *
 */
public class Images {
	private HashMap<String, BufferedImage> map;
	
	private static Images instance = null;
	
	/**
	 * Private constructor
	 */
	private Images() {
		map = new HashMap<String, BufferedImage>();
	}
	
	/**
	 * Returns the instance of images.
	 * @return Images : instance
	 */
	public static Images getInstance(){
		if(instance == null)
			instance = new Images();
		return instance;
	}
	
	/**
	 * Sets the instance to null.
	 */
	public void clearInstance(){
		instance = null;
	}
	
	/**
	 * Creates a BufferedImage from the file at the given location
	 * and adds it to the map.
	 * @param image String : path to the image
	 */
	public void add(String image){
		BufferedImage img;
		try {
			if(src.Constants.SCENARIO_DEBUG || src.Constants.EDITOR)
				img = ImageIO.read(new File(image));
			else img = ImageIO.read(ScenarioFile.getInstance().getStream(image));
			map.put(image, img);
		} catch (IOException e) {
			System.err.println("Image: " + image + " not found.");
		} catch (Exception ex){
			//ex.printStackTrace();
			System.err.println("Problem reading image: " + image + ".");
		}
	}
	
	/**
	 * Gets a BufferedImage from the map.
	 * @param str String : path to the image
	 * @return BufferedImage : the image
	 */
	public BufferedImage getImage(String str){
		return map.get(str);
	}
}
