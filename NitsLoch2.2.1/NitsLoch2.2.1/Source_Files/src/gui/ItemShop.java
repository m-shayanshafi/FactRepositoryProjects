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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import src.enums.Shops;
import src.enums.StoreItems;
import src.game.Enemy;
import src.game.EnemyMovement;
import src.game.Messages;
import src.game.Player;

/**
 * All shops that sell items inherit from this class.  You can select which item
 * from the list you want to purchase and click buy.  You can also try stealing and
 * canceling.  Implemented classes will have to specify what items the shop has
 * and implement the buy method.
 * @author Darren Watts
 * date 11/10/07
 *
 */
public class ItemShop extends JFrame {
	private static final long serialVersionUID = src.Constants.serialVersionUID;
	
	ArrayList<JRadioButton> itemGUIChoices;
	ArrayList<StoreItems> items;
	
	JButton btnOK = new JButton();
	JButton btnCancel = new JButton();
	JButton btnSteal = new JButton();
	ButtonGroup buttonGroup1 = new ButtonGroup();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	
	private Controller controller;
	
	private int row;
	private int col;
	
	/**
	 * Constructor that sets up the controller, the shop type
	 * and the permutation number of the shop.  It calls the
	 * setup method which will fill in the GUI based on the
	 * type and permutation of the shop.
	 * @param controller Controller : controller
	 * @param type Shops : type of shop
	 * @param permutation int : permutation number
	 */
	public ItemShop(Controller controller, Shops type,
			int permutation, int row, int col){
		this.controller = controller;
		items = type.getItems(permutation);
		itemGUIChoices = new ArrayList<JRadioButton>();
		this.row = row;
		this.col = col;
		try {
			setUpFrame();
		} catch(Exception ex){
			ex.printStackTrace();
		}
		if(type == Shops.GENERIC_L || type == Shops.GENERIC_R)
			btnSteal.setEnabled(false);
	}
	
	/**
	 * Gets the weapon level for the item being purchased.
	 * @param item StoreItems : the item
	 * @param plrLevel int : player's level
	 * @param plrMoney int : player's money
	 * @return int : weapon level
	 */
	private int getWeaponLevel(StoreItems item, int plrLevel, int plrMoney){
		if(plrLevel > 1){
			for(int i = plrLevel; i > 1; i--){
				if(plrMoney >= item.getPrice() * i){
					return i;
				}
			}
		}
		return 1;
	}
	
	/**
	 * Sets up the components on the GUI based on the preset for
	 * the shop.
	 */
	private void setUpFrame(){
		getContentPane().setLayout(gridBagLayout1);
		
		JRadioButton rbnNewItem;
		JLabel lblNewPrice;
		int count = 0;
		int weaponLevel, plrLevel, plrMoney;
		for(StoreItems item : items){
			rbnNewItem = new JRadioButton();
			weaponLevel = 1;
			plrLevel = controller.getLocalPlayer().getLevel();
			plrMoney = controller.getLocalPlayer().getMoney();
			
			weaponLevel = getWeaponLevel(item, plrLevel, plrMoney);
			
			if(weaponLevel == 1 || !item.isWeapon()){
				rbnNewItem.setText(item.getItemName());
				weaponLevel = 1;
			}
			else rbnNewItem.setText(item.getItemName() + " +" + weaponLevel);
			this.getContentPane().add(rbnNewItem,
					new GridBagConstraints(0, count, 2, 1, 0.0, 0.0
							, GridBagConstraints.WEST, GridBagConstraints.NONE,
							new Insets(0, 0, 0, 0), 0, 0));
			lblNewPrice = new JLabel();
			lblNewPrice.setText("$ " + item.getPrice() * weaponLevel);
			this.getContentPane().add(lblNewPrice,
					new GridBagConstraints(2, count, 1, 1, 0.0, 0.0
							, GridBagConstraints.WEST, GridBagConstraints.NONE,
							new Insets(0, 0, 0, 0), 0, 0));
			buttonGroup1.add(rbnNewItem);
			itemGUIChoices.add(rbnNewItem);
			
			count++;
		}
		
		btnCancel.setText("Cancel");
		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				closeWindow();
			}
		});
		this.getContentPane().add(btnCancel,
				new GridBagConstraints(0, count, 1, 1, 0.0, 0.0
						, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(5, 5, 0, 5), 0, 0));
		
		btnSteal.setText("Steal");
		btnSteal.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				stealItem();
				
				closeWindow();
			}
		});
		this.getContentPane().add(btnSteal,
				new GridBagConstraints(1, count, 1, 1, 0.0, 0.0
						, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(5, 5, 0, 5), 0, 0));
		
		btnOK.setText("OK");
		btnOK.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				buyItem();
				
				closeWindow();
			}
		});
		this.getContentPane().add(btnOK,
				new GridBagConstraints(2, count, 1, 1, 0.0, 0.0
						, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(5, 5, 0, 5), 0, 0));
		
		try {
			itemGUIChoices.get(0).setSelected(true);
		} catch(Exception ex){ }
		
		setSize(340, 35 * items.size() + 60);
        setTitle("Shop");
        src.Constants.centerFrame(this);
        setVisible(true);
	}
	
	/**
	 * Closes the GUI window.
	 */
	private void closeWindow(){
		this.dispose();
	}

	/**
	 * Purchases a selected item.
	 */
	private void buyItem(){
		int count = 0;
		for(JRadioButton radioButton : itemGUIChoices){
			if(radioButton.isSelected()){
				items.get(count).buyItem(controller.getLocalPlayer());
				return;
			}
			count++;
		}
		controller.activateEnemyAI();
	}
	
	/**
	 * Steals a selected item.
	 */
	private void stealItem(){
		int count = 0;
		for(JRadioButton radioButton : itemGUIChoices){
			if(radioButton.isSelected()){
				break;
			}
			count++;
		}
		
		Enemy shopkeeper;
		Player plr = controller.getLocalPlayer();
		
		// If there's a shopkeeper around.
		if((shopkeeper = controller.getShopkeeperAround(row, col)) != null){
			
			if(controller.successfullySteal(plr)){
				items.get(count).stealItem(plr);
				plr.addThievingPoints();
			}
			else {
				plr.setCaughtStealing(true);
				shopkeeper.setHasBeenAttacked(true);
				Messages.getInstance().addMessage("You were caught trying to steal. " +
				"The shopkeeper is coming after you!");
			}
		}
		else { // No shopkeeper around
			try {
				items.get(count).stealItem(plr);
			} catch(IndexOutOfBoundsException bounds){
				return;
			}

			if(Math.random() < src.Constants.SHOPKEEPER_SPAWN){
				//WorldInteractions.spawnNewShopkeeper(row, col, player);
				EnemyMovement.spawnShopkeeper(row, col, plr);
				Messages.getInstance().addMessage("Another shop keeper has come" +
				" to help tend the store.");
			}
			else Messages.getInstance().addMessage("There is no shopkeeper around.");
		}
		controller.activateEnemyAI();
	}
}
