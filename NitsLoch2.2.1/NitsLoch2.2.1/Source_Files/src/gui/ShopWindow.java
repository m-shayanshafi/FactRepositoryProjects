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

package src.gui;

import src.enums.Shops;

/**
 * Singleton class that acts as a factory for shop GUI windows.  This way
 * the shop GUI windows will have access to the controller.  The
 * code in the data layer calls a method in this class to create
 * a new shop window.
 * @author Darren Watts
 * date 1/11/08
 *
 */
public class ShopWindow {
	
	private static ShopWindow instance = null;

	private Controller controller;
	
	/**
	 * Private constructor that does nothing.
	 */
	private ShopWindow() {}
	
	/**
	 * Accessor for the instance of the ShopWindow.
	 * @return ShopWindow : instance
	 */
	public static ShopWindow getInstance(){
		if(instance == null)
			instance = new ShopWindow();
		return instance;
	}
	
	/**
	 * Sets the controller for the ShopWindow instance.
	 * @param con Controller : controller for the GUI.
	 */
	public void setController(Controller con){
		controller = con;
	}
	
	/**
	 * Creates a new BarShop GUI window.
	 * @param permutation int : permutation of the shop.
	 */
	public void createBarWindow(int permutation){
		new BarShop(controller, permutation);
	}
	
	/**
	 * Creates a new HospitalShop GUI window.
	 */
	public void createHospitalWindow(){
		new HospitalShop(controller);
	}
	
	/**
	 * Creates a regular item shop GUI window.
	 * @param type Shops : type of shop
	 * @param permutation int : permutation number for the shop
	 */
	public void createShopWindow(Shops type, int permutation,
			int row, int col){
		new ItemShop(controller, type, permutation, row, col);
	}
}
