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

package src.enums;

import src.scenario.Images;

/**
 * Keeps track of the paths to the player images.
 * @author Darren Watts
 * date 11/18/07
 *
 */
public enum PlayerImages {
	// Type numbers must go from 0 to n-1
	I_00	(0),
	I_01	(1),
	I_02	(2),
	I_03	(3),
	I_04	(4),
	I_05	(5),
	I_06	(6),
	I_07	(7),
	I_08	(8),
	I_09	(9),
	I_10	(10),
	I_11	(11),
	I_12	(12),
	I_13	(13),
	I_14	(14),
	I_15	(15),
	I_16	(16),
	I_17	(17),
	I_18	(18),
	I_19	(19),
	I_20	(20),
	I_21	(21),
	I_22	(22),
	I_23	(23),
	I_24	(24),
	DEAD	(25);
	
	private int type;
	private String left;
	private String right;
	
	/**
	 * Constructor.  Sets the type number of the picture.
	 * @param type int : type number
	 */
	private PlayerImages(int type){
		this.type = type;
	}
	
	/**
	 * Accessor for the type number of the picture
	 * @return int : type number
	 */
	public int getType(){
		return type;
	}
	
	/**
	 * Sets the left and right image locations for this player image type.
	 * @param left String : path to left facing player image
	 * @param right String : path to right facing player image
	 */
	public void setImages(String left, String right){
		this.left = left;
		this.right = right;
		
		Images.getInstance().add(left);
		Images.getInstance().add(right);
	}
	
	/**
	 * Accessor for the left facing player image for this type.
	 * @return String : path to image
	 */
	public String getLeftImage(){
		return left;
	}
	
	/**
	 * Accessor for the right facing player image for this type.
	 * @return String : path to image
	 */
	public String getRightImage(){
		return right;
	}
	
	public static void clearAll() {
		for(PlayerImages p : PlayerImages.values()) {
			p.left = p.right = null;
		}
	}
	
	/* ******************************************************
	 * The following methods are used only for the scenario editor
	 * ******************************************************/

	public void setLeftImage(String text) {
		left = text;
	}
	
	public void setRightImage(String text) {
		right = text;
	}
}
