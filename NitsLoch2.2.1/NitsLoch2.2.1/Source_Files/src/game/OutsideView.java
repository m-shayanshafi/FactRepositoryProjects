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

package src.game;

import java.awt.image.BufferedImage;

import src.enums.ExplosionImages;
import src.land.Land;
import src.scenario.Images;

/**
 * Class that controls what is to be drawn to the main GUI window
 * when the player is outside, not looking at the map.
 * @author Darren Watts
 * date 12/5/07
 */
public class OutsideView {

	/**
	 * Gets the view that will be drawn to the main GUI window.  Sets
	 * up the buffered images and returns them to the main window.
	 * @param worldView Land[][] : the land that is within view of the
	 * player.
	 * @return BufferedImage[][] : images for each cell in the main GUI
	 * window.
	 */
	public static BufferedImage[][] getView(Land[][] worldView){
		int explodeRow = GameWorld.getInstance().getExplodeRow();
		int explodeCol = GameWorld.getInstance().getExplodeCol();
		// Draw the explosion, if there is one
		BufferedImage[][] bufImages = new BufferedImage[src.Constants.WORLD_VIEW_SIZE]
		                                                [src.Constants.WORLD_VIEW_SIZE];
		Images images = Images.getInstance();
		
		/* 
		 * Checks the land at each cell within visible range and sets the
		 * BufferedImage array to the correct image.  If there is an
		 * explosion, it will overwrite the getImage call for that land
		 * object with the picture of the explosion.
		 */
		for(int row = 0; row < src.Constants.WORLD_VIEW_SIZE; row++){
			for(int col = 0; col < src.Constants.WORLD_VIEW_SIZE; col++){
				
				bufImages[row][col] = images.getImage(worldView[row][col].getImage());
				
				if(explodeRow > -1){ // There is an explosion
					try{
						//bufImages[row][col] = null;
						if(row == explodeRow-1 && 
								col == explodeCol-1)
							bufImages[row][col] = images.getImage(ExplosionImages.NORTHWEST.getImage());//ImageIO.read(new File("images/explosionNW" + Constants.IMG_EXTENSION));
						else if(row == explodeRow-1 && 
								col == explodeCol)
							bufImages[row][col] = images.getImage(ExplosionImages.NORTH.getImage());
						else if(row == explodeRow-1 && 
								col == explodeCol+1)
							bufImages[row][col] = images.getImage(ExplosionImages.NORTHEAST.getImage());
						else if(row == explodeRow && 
								col == explodeCol+1)
							bufImages[row][col] = images.getImage(ExplosionImages.EAST.getImage());
						else if(row == explodeRow+1 && 
								col == explodeCol+1)
							bufImages[row][col] = images.getImage(ExplosionImages.SOUTHEAST.getImage());
						else if(row == explodeRow+1 && 
								col == explodeCol)
							bufImages[row][col] = images.getImage(ExplosionImages.SOUTH.getImage());
						else if(row == explodeRow+1 && 
								col == explodeCol-1)
							bufImages[row][col] = images.getImage(ExplosionImages.SOUTHWEST.getImage());
						else if(row == explodeRow && 
								col == explodeCol-1)
							bufImages[row][col] = images.getImage(ExplosionImages.WEST.getImage());
						else if(row == explodeRow && 
								col == explodeCol)
							bufImages[row][col] = images.getImage(ExplosionImages.CENTER.getImage());

						//if(row == 3 && col == 3) bufImages[row][col] = null;
					} catch(Exception e){
						System.out.println("Cannot find explosion picture");
					}
				}
			}
		}
		return bufImages;
	}
	
	/**
	 * Returns images of the world in view without the objects.
	 * @param worldView Land[][] : world that is in view
	 * @return BufferedImage[][] : images to be drawn.
	 */
	public static BufferedImage[][] getViewNoObjects(Land[][] worldView){
		// Draw the explosion, if there is one
		BufferedImage[][] bufImages = new BufferedImage[src.Constants.WORLD_VIEW_SIZE]
		                                                [src.Constants.WORLD_VIEW_SIZE];
		Images images = Images.getInstance();
		
		/* 
		 * Checks the land at each cell within visible range and sets the
		 * BufferedImage array to the correct image ignoring objects.
		 */
		for(int row = 0; row < src.Constants.WORLD_VIEW_SIZE; row++){
			for(int col = 0; col < src.Constants.WORLD_VIEW_SIZE; col++){
				
				bufImages[row][col] = images.getImage(worldView[row][col].getLandImage());
			}
		}
		return bufImages;
	}
}
