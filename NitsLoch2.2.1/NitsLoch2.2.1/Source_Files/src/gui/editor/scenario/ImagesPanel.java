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

package src.gui.editor.scenario;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import src.enums.ExitType;
import src.enums.ExplosionImages;
import src.enums.GroundItems;
import src.enums.HitImages;
import src.enums.PlayerImages;
import src.enums.Shops;

public class ImagesPanel {
	
	// Ground items
	private static JTextField txtGrenade;
	private static JTextField txtDynamite;
	private static JTextField txtBandaids;
	private static JTextField txtBullets;
	private static JTextField txtRockets;
	private static JTextField txtMoney;
	private static JTextField txtGrave;
	
	// Shop images
	private static JTextField txtAmmoLeft;
	private static JTextField txtAmmoRight;
	private static JTextField txtArmorLeft;
	private static JTextField txtArmorRight;
	private static JTextField txtBarLeft;
	private static JTextField txtBarRight;
	private static JTextField txtHospitalLeft;
	private static JTextField txtHospitalRight;
	private static JTextField txtWeaponLeft;
	private static JTextField txtWeaponRight;
	private static JTextField txtMagicLeft;
	private static JTextField txtMagicRight;
	private static JTextField txtGenericLeft;
	private static JTextField txtGenericRight;
	
	// Player images
	private static JTextField txtPlrDead;
	private static JTextField txtHitImage1;
	private static JTextField txtHitImage2;
	private static JTextField txtHitImage3;
	private static JTextField txtHitImage4;
	private static JTextField txtHitImage5;
	
	// Explosion images
	private static JTextField txtExplC;
	private static JTextField txtExplN;
	private static JTextField txtExplNE;
	private static JTextField txtExplE;
	private static JTextField txtExplSE;
	private static JTextField txtExplS;
	private static JTextField txtExplSW;
	private static JTextField txtExplW;
	private static JTextField txtExplNW;
	
	// Exit images
	private static JTextField txtCityGateOpen;
	private static JTextField txtCityGateClosed;
	private static JTextField txtDungeonOpen;
	private static JTextField txtDungeonClosed;
	private static JTextField txtLadderUp;
	private static JTextField txtLadderDown;

	public static JPanel getImagesPanel() {
		JPanel pnlImages = new JPanel();
		pnlImages.setLayout(new BoxLayout(pnlImages, BoxLayout.Y_AXIS));
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.add(getItemImagesPanel(), "Ground Items");
		tabbedPane.add(getShopImagesPanel(), "Shops");
		tabbedPane.add(getPlayerImagesPanel(), "Player");
		tabbedPane.add(getExplosionImages(), "Explosions");
		tabbedPane.add(getExitImagesPanel(), "Exits");
		pnlImages.add(tabbedPane);
		
		return pnlImages;
	}

	private static JPanel getExplosionImages() {
		JPanel pnlExploionImages = new JPanel();
		pnlExploionImages.setLayout(new GridBagLayout());
		
		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.insets = new Insets(0, 20, 0, 20);
		
		JLabel lblExplC = new JLabel();
		lblExplC.setText("Center:");
		JLabel lblExplN = new JLabel();
		lblExplN.setText("North:");
		JLabel lblExplNE = new JLabel();
		lblExplNE.setText("North East:");
		JLabel lblExplE = new JLabel();
		lblExplE.setText("East:");
		JLabel lblExplSE = new JLabel();
		lblExplSE.setText("South East:");
		JLabel lblExplS = new JLabel();
		lblExplS.setText("South:");
		JLabel lblExplSW = new JLabel();
		lblExplSW.setText("South West:");
		JLabel lblExplW = new JLabel();
		lblExplW.setText("West:");
		JLabel lblExplNW = new JLabel();
		lblExplNW.setText("North West:");

		txtExplC = new JTextField();
		txtExplC.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				ExplosionImages.CENTER.setImage(txtExplC.getText());
			}
		});
		txtExplN = new JTextField();
		txtExplN.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				ExplosionImages.NORTH.setImage(txtExplN.getText());
			}
		});
		txtExplNE = new JTextField();
		txtExplNE.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				ExplosionImages.NORTHEAST.setImage(txtExplNE.getText());
			}
		});
		txtExplE = new JTextField();
		txtExplE.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				ExplosionImages.EAST.setImage(txtExplE.getText());
			}
		});
		txtExplSE = new JTextField();
		txtExplSE.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				ExplosionImages.SOUTHEAST.setImage(txtExplSE.getText());
			}
		});
		txtExplS = new JTextField();
		txtExplS.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				ExplosionImages.SOUTH.setImage(txtExplS.getText());
			}
		});
		txtExplSW = new JTextField();
		txtExplSW.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				ExplosionImages.SOUTHWEST.setImage(txtExplSW.getText());
			}
		});
		txtExplW = new JTextField();
		txtExplW.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				ExplosionImages.WEST.setImage(txtExplW.getText());
			}
		});
		txtExplNW = new JTextField();
		txtExplNW.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				ExplosionImages.NORTHWEST.setImage(txtExplNW.getText());
			}
		});
		
		// Add labels
		pnlExploionImages.add(lblExplC, lstObjectsC);
		lstObjectsC.gridy = 2;
		pnlExploionImages.add(lblExplN, lstObjectsC);
		lstObjectsC.gridy = 4;
		pnlExploionImages.add(lblExplNE, lstObjectsC);
		lstObjectsC.gridy = 6;
		pnlExploionImages.add(lblExplE, lstObjectsC);
		lstObjectsC.gridy = 8;
		pnlExploionImages.add(lblExplSE, lstObjectsC);
		lstObjectsC.gridy = 10;
		pnlExploionImages.add(lblExplS, lstObjectsC);
		lstObjectsC.gridy = 12;
		pnlExploionImages.add(lblExplSW, lstObjectsC);
		lstObjectsC.gridy = 14;
		pnlExploionImages.add(lblExplW, lstObjectsC);
		lstObjectsC.gridy = 16;
		pnlExploionImages.add(lblExplNW, lstObjectsC);
		
		// Add text fields
		lstObjectsC.insets = new Insets(0, 20, 5, 20);
		lstObjectsC.gridy = 1;
		pnlExploionImages.add(txtExplC, lstObjectsC);
		lstObjectsC.gridy = 3;
		pnlExploionImages.add(txtExplN, lstObjectsC);
		lstObjectsC.gridy = 5;
		pnlExploionImages.add(txtExplNE, lstObjectsC);
		lstObjectsC.gridy = 7;
		pnlExploionImages.add(txtExplE, lstObjectsC);
		lstObjectsC.gridy = 9;
		pnlExploionImages.add(txtExplSE, lstObjectsC);
		lstObjectsC.gridy = 11;
		pnlExploionImages.add(txtExplS, lstObjectsC);
		lstObjectsC.gridy = 13;
		pnlExploionImages.add(txtExplSW, lstObjectsC);
		lstObjectsC.gridy = 15;
		pnlExploionImages.add(txtExplW, lstObjectsC);
		lstObjectsC.gridy = 17;
		pnlExploionImages.add(txtExplNW, lstObjectsC);
		
		fillExplosionPanel();
		
		return pnlExploionImages;
	}
	
	private static void fillExplosionPanel() {
		txtExplC.setText(ExplosionImages.CENTER.getImage());
		txtExplN.setText(ExplosionImages.NORTH.getImage());
		txtExplNE.setText(ExplosionImages.NORTHEAST.getImage());
		txtExplE.setText(ExplosionImages.EAST.getImage());
		txtExplSE.setText(ExplosionImages.SOUTHEAST.getImage());
		txtExplS.setText(ExplosionImages.SOUTH.getImage());
		txtExplSW.setText(ExplosionImages.SOUTHWEST.getImage());
		txtExplW.setText(ExplosionImages.WEST.getImage());
		txtExplNW.setText(ExplosionImages.NORTHWEST.getImage());
	}

	private static JPanel getPlayerImagesPanel() {
		JPanel pnlPlayerImages = new JPanel();
		pnlPlayerImages.setLayout(new GridBagLayout());
		
		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.insets = new Insets(0, 20, 0, 20);
		
		JLabel lblPlrDead = new JLabel();
		lblPlrDead.setText("Dead:");
		JLabel lblHit1 = new JLabel();
		lblHit1.setText("Hit Image 1:");
		JLabel lblHit2 = new JLabel();
		lblHit2.setText("Hit Image 2 (Optional):");
		JLabel lblHit3 = new JLabel();
		lblHit3.setText("Hit Image 3 (Optional):");
		JLabel lblHit4 = new JLabel();
		lblHit4.setText("Hit Image 4 (Optional):");
		JLabel lblHit5 = new JLabel();
		lblHit5.setText("Hit Image 5 (Optional):");

		txtPlrDead = new JTextField();
		txtPlrDead.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				PlayerImages.DEAD.setImages(txtPlrDead.getText(), txtPlrDead.getText());
			}
		});
		txtHitImage1 = new JTextField();
		txtHitImage1.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				HitImages.HIT1.setLocation(txtHitImage1.getText());
			}
		});
		txtHitImage2 = new JTextField();
		txtHitImage2.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				HitImages.HIT2.setLocation(txtHitImage2.getText());
			}
		});
		txtHitImage3 = new JTextField();
		txtHitImage3.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				HitImages.HIT3.setLocation(txtHitImage3.getText());
			}
		});
		txtHitImage4 = new JTextField();
		txtHitImage4.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				HitImages.HIT4.setLocation(txtHitImage4.getText());
			}
		});
		txtHitImage5 = new JTextField();
		txtHitImage5.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				HitImages.HIT5.setLocation(txtHitImage5.getText());
			}
		});
		
		// Add labels
		pnlPlayerImages.add(lblPlrDead, lstObjectsC);
		lstObjectsC.gridy = 2;
		pnlPlayerImages.add(lblHit1, lstObjectsC);
		lstObjectsC.gridy = 4;
		pnlPlayerImages.add(lblHit2, lstObjectsC);
		lstObjectsC.gridy = 6;
		pnlPlayerImages.add(lblHit3, lstObjectsC);
		lstObjectsC.gridy = 8;
		pnlPlayerImages.add(lblHit4, lstObjectsC);
		lstObjectsC.gridy = 10;
		pnlPlayerImages.add(lblHit5, lstObjectsC);
		
		// Add text fields
		lstObjectsC.insets = new Insets(0, 20, 5, 20);
		lstObjectsC.gridy = 1;
		pnlPlayerImages.add(txtPlrDead, lstObjectsC);
		lstObjectsC.gridy = 3;
		pnlPlayerImages.add(txtHitImage1, lstObjectsC);
		lstObjectsC.gridy = 5;
		pnlPlayerImages.add(txtHitImage2, lstObjectsC);
		lstObjectsC.gridy = 7;
		pnlPlayerImages.add(txtHitImage3, lstObjectsC);
		lstObjectsC.gridy = 9;
		pnlPlayerImages.add(txtHitImage4, lstObjectsC);
		lstObjectsC.gridy = 11;
		pnlPlayerImages.add(txtHitImage5, lstObjectsC);
		
		fillPlayerPanel();
		
		return pnlPlayerImages;
	}

	private static void fillPlayerPanel() {
		txtPlrDead.setText(PlayerImages.DEAD.getLeftImage());
		txtHitImage1.setText(HitImages.HIT1.getImage());
		txtHitImage2.setText(HitImages.HIT2.getImage());
		txtHitImage3.setText(HitImages.HIT3.getImage());
		txtHitImage4.setText(HitImages.HIT4.getImage());
		txtHitImage5.setText(HitImages.HIT5.getImage());
	}

	private static JPanel getShopImagesPanel() {
		JPanel pnlShopImages = new JPanel();
		pnlShopImages.setLayout(new GridBagLayout());
		
		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.insets = new Insets(0, 10, 0, 10);
		
		JLabel lblAmmoLeft = new JLabel();
		lblAmmoLeft.setText("Ammo Left:");
		JLabel lblAmmoRight = new JLabel();
		lblAmmoRight.setText("Ammo Right:");
		JLabel lblArmorLeft = new JLabel();
		lblArmorLeft.setText("Armor Left:");
		JLabel lblArmorRight = new JLabel();
		lblArmorRight.setText("Armor Right:");
		JLabel lblBarLeft = new JLabel();
		lblBarLeft.setText("Bar Left:");
		JLabel lblBarRight = new JLabel();
		lblBarRight.setText("Bar Right:");
		JLabel lblHospitalLeft = new JLabel();
		lblHospitalLeft.setText("Hospital Left:");
		JLabel lblHospitalRight = new JLabel();
		lblHospitalRight.setText("Hospital Right:");
		JLabel lblWeaponLeft = new JLabel();
		lblWeaponLeft.setText("Weapon Left:");
		JLabel lblWeaponRight = new JLabel();
		lblWeaponRight.setText("Weapon Right:");
		JLabel lblMagicLeft = new JLabel();
		lblMagicLeft.setText("Magic Left:");
		JLabel lblMagicRight = new JLabel();
		lblMagicRight.setText("Magic Right:");
		JLabel lblGenericLeft = new JLabel();
		lblGenericLeft.setText("Generic Left:");
		JLabel lblGenericRight = new JLabel();
		lblGenericRight.setText("Generic Right:");

		txtAmmoLeft = new JTextField();
		txtAmmoLeft.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				Shops.AMMO_L.setImageLocation(txtAmmoLeft.getText());
			}
		});
		txtAmmoRight = new JTextField();
		txtAmmoRight.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				Shops.AMMO_R.setImageLocation(txtAmmoRight.getText());
			}
		});
		txtArmorLeft = new JTextField();
		txtArmorLeft.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				Shops.ARMOR_L.setImageLocation(txtArmorLeft.getText());
			}
		});
		txtArmorRight = new JTextField();
		txtArmorRight.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				Shops.ARMOR_R.setImageLocation(txtArmorRight.getText());
			}
		});
		txtBarLeft = new JTextField();
		txtBarLeft.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				Shops.BAR_L.setImageLocation(txtBarLeft.getText());
			}
		});
		txtBarRight = new JTextField();
		txtBarRight.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				Shops.BAR_R.setImageLocation(txtBarRight.getText());
			}
		});
		txtHospitalLeft = new JTextField();
		txtHospitalLeft.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				Shops.HOSPITAL_L.setImageLocation(txtHospitalLeft.getText());
			}
		});
		txtHospitalRight = new JTextField();
		txtHospitalRight.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				Shops.HOSPITAL_R.setImageLocation(txtHospitalRight.getText());
			}
		});
		txtWeaponLeft = new JTextField();
		txtWeaponLeft.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				Shops.WEAPON_L.setImageLocation(txtWeaponLeft.getText());
			}
		});
		txtWeaponRight = new JTextField();
		txtWeaponRight.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				Shops.WEAPON_R.setImageLocation(txtWeaponRight.getText());
			}
		});
		txtMagicLeft = new JTextField();
		txtMagicLeft.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				Shops.MAGIC_L.setImageLocation(txtMagicLeft.getText());
			}
		});
		txtMagicRight = new JTextField();
		txtMagicRight.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				Shops.MAGIC_R.setImageLocation(txtMagicRight.getText());
			}
		});
		txtGenericLeft = new JTextField();
		txtGenericLeft.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				Shops.GENERIC_L.setImageLocation(txtGenericLeft.getText());
			}
		});
		txtGenericRight = new JTextField();
		txtGenericRight.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				Shops.GENERIC_R.setImageLocation(txtGenericRight.getText());
			}
		});
		
		// Add labels
		pnlShopImages.add(lblAmmoLeft, lstObjectsC);
		lstObjectsC.gridx = 1;
		pnlShopImages.add(lblAmmoRight, lstObjectsC);
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 2;
		pnlShopImages.add(lblArmorLeft, lstObjectsC);
		lstObjectsC.gridx = 1;
		pnlShopImages.add(lblArmorRight, lstObjectsC);
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 4;
		pnlShopImages.add(lblBarLeft, lstObjectsC);
		lstObjectsC.gridx = 1;
		pnlShopImages.add(lblBarRight, lstObjectsC);
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 6;
		pnlShopImages.add(lblHospitalLeft, lstObjectsC);
		lstObjectsC.gridx = 1;
		pnlShopImages.add(lblHospitalRight, lstObjectsC);
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 8;
		pnlShopImages.add(lblWeaponLeft, lstObjectsC);
		lstObjectsC.gridx = 1;
		pnlShopImages.add(lblWeaponRight, lstObjectsC);
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 10;
		pnlShopImages.add(lblMagicLeft, lstObjectsC);
		lstObjectsC.gridx = 1;
		pnlShopImages.add(lblMagicRight, lstObjectsC);
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 12;
		pnlShopImages.add(lblGenericLeft, lstObjectsC);
		lstObjectsC.gridx = 1;
		pnlShopImages.add(lblGenericRight, lstObjectsC);
		
		// Add text fields
		lstObjectsC.insets = new Insets(0, 10, 5, 10);
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 1;
		pnlShopImages.add(txtAmmoLeft, lstObjectsC);
		lstObjectsC.gridx = 1;
		pnlShopImages.add(txtAmmoRight, lstObjectsC);
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 3;
		pnlShopImages.add(txtArmorLeft, lstObjectsC);
		lstObjectsC.gridx = 1;
		pnlShopImages.add(txtArmorRight, lstObjectsC);
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 5;
		pnlShopImages.add(txtBarLeft, lstObjectsC);
		lstObjectsC.gridx = 1;
		pnlShopImages.add(txtBarRight, lstObjectsC);
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 7;
		pnlShopImages.add(txtHospitalLeft, lstObjectsC);
		lstObjectsC.gridx = 1;
		pnlShopImages.add(txtHospitalRight, lstObjectsC);
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 9;
		pnlShopImages.add(txtWeaponLeft, lstObjectsC);
		lstObjectsC.gridx = 1;
		pnlShopImages.add(txtWeaponRight, lstObjectsC);
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 11;
		pnlShopImages.add(txtMagicLeft, lstObjectsC);
		lstObjectsC.gridx = 1;
		pnlShopImages.add(txtMagicRight, lstObjectsC);
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 13;
		pnlShopImages.add(txtGenericLeft, lstObjectsC);
		lstObjectsC.gridx = 1;
		pnlShopImages.add(txtGenericRight, lstObjectsC);
		
		fillShopPanel();
		
		return pnlShopImages;
	}

	private static void fillShopPanel() {
		txtAmmoLeft.setText(Shops.AMMO_L.getImage());
		txtAmmoRight.setText(Shops.AMMO_R.getImage());
		txtArmorLeft.setText(Shops.ARMOR_L.getImage());
		txtArmorRight.setText(Shops.ARMOR_R.getImage());
		txtBarLeft.setText(Shops.BAR_L.getImage());
		txtBarRight.setText(Shops.BAR_R.getImage());
		txtHospitalLeft.setText(Shops.HOSPITAL_L.getImage());
		txtHospitalRight.setText(Shops.HOSPITAL_R.getImage());
		txtWeaponLeft.setText(Shops.WEAPON_L.getImage());
		txtWeaponRight.setText(Shops.WEAPON_R.getImage());
		txtMagicLeft.setText(Shops.MAGIC_L.getImage());
		txtMagicRight.setText(Shops.MAGIC_R.getImage());
		txtGenericLeft.setText(Shops.GENERIC_L.getImage());
		txtGenericRight.setText(Shops.GENERIC_R.getImage());
	}

	private static JPanel getItemImagesPanel() {
		JPanel pnlItemImages = new JPanel();
		pnlItemImages.setLayout(new GridBagLayout());
		
		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.insets = new Insets(0, 20, 0, 20);
		
		JLabel lblGrenade = new JLabel();
		lblGrenade.setText("Grenade:");
		JLabel lblDynamite = new JLabel();
		lblDynamite.setText("Dynamite:");
		JLabel lblBandaids = new JLabel();
		lblBandaids.setText("Bandaids:");
		JLabel lblBullets = new JLabel();
		lblBullets.setText("Bullets:");
		JLabel lblRockets = new JLabel();
		lblRockets.setText("Rockets:");
		JLabel lblMoney = new JLabel();
		lblMoney.setText("Money:");
		JLabel lblGrave = new JLabel();
		lblGrave.setText("Grave:");

		txtGrenade = new JTextField();
		txtGrenade.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				GroundItems.GRENADE.setImageLocation(txtGrenade.getText());
			}
		});
		txtDynamite = new JTextField();
		txtDynamite.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				GroundItems.DYNAMITE.setImageLocation(txtDynamite.getText());
			}
		});
		txtBandaids = new JTextField();
		txtBandaids.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				GroundItems.BANDAIDS.setImageLocation(txtBandaids.getText());
			}
		});
		txtBullets = new JTextField();
		txtBullets.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				GroundItems.BULLETS.setImageLocation(txtBullets.getText());
			}
		});
		txtRockets = new JTextField();
		txtRockets.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				GroundItems.ROCKETS.setImageLocation(txtRockets.getText());
			}
		});
		txtMoney = new JTextField();
		txtMoney.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				GroundItems.MONEY.setImageLocation(txtMoney.getText());
			}
		});
		txtGrave = new JTextField();
		txtGrave.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				GroundItems.GRAVE.setImageLocation(txtGrave.getText());
			}
		});
		
		// Add labels
		pnlItemImages.add(lblGrenade, lstObjectsC);
		lstObjectsC.gridy = 2;
		pnlItemImages.add(lblDynamite, lstObjectsC);
		lstObjectsC.gridy = 4;
		pnlItemImages.add(lblBandaids, lstObjectsC);
		lstObjectsC.gridy = 6;
		pnlItemImages.add(lblBullets, lstObjectsC);
		lstObjectsC.gridy = 8;
		pnlItemImages.add(lblRockets, lstObjectsC);
		lstObjectsC.gridy = 10;
		pnlItemImages.add(lblMoney, lstObjectsC);
		lstObjectsC.gridy = 12;
		pnlItemImages.add(lblGrave, lstObjectsC);
		
		// Add text fields
		lstObjectsC.insets = new Insets(0, 20, 5, 20);
		lstObjectsC.gridy = 1;
		pnlItemImages.add(txtGrenade, lstObjectsC);
		lstObjectsC.gridy = 3;
		pnlItemImages.add(txtDynamite, lstObjectsC);
		lstObjectsC.gridy = 5;
		pnlItemImages.add(txtBandaids, lstObjectsC);
		lstObjectsC.gridy = 7;
		pnlItemImages.add(txtBullets, lstObjectsC);
		lstObjectsC.gridy = 9;
		pnlItemImages.add(txtRockets, lstObjectsC);
		lstObjectsC.gridy = 11;
		pnlItemImages.add(txtMoney, lstObjectsC);
		lstObjectsC.gridy = 13;
		pnlItemImages.add(txtGrave, lstObjectsC);
		
		fillItemPanel();
		
		return pnlItemImages;
	}

	private static void fillItemPanel() {
		txtGrenade.setText(GroundItems.GRENADE.getImage());
		txtDynamite.setText(GroundItems.DYNAMITE.getImage());
		txtBandaids.setText(GroundItems.BANDAIDS.getImage());
		txtBullets.setText(GroundItems.BULLETS.getImage());
		txtRockets.setText(GroundItems.ROCKETS.getImage());
		txtMoney.setText(GroundItems.MONEY.getImage());
		txtGrave.setText(GroundItems.GRAVE.getImage());
	}
	
	private static JPanel getExitImagesPanel() {
		JPanel pnlExitImages = new JPanel();
		pnlExitImages.setLayout(new GridBagLayout());
		
		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.insets = new Insets(0, 20, 0, 20);
		
		JLabel lblCityGateOpen = new JLabel();
		lblCityGateOpen.setText("City Gate (open):");
		JLabel lblCityGateClosed = new JLabel();
		lblCityGateClosed.setText("City Gate (closed):");
		JLabel lblDungeonOpen = new JLabel();
		lblDungeonOpen.setText("Dungeon (open):");
		JLabel lblDungeonClosed = new JLabel();
		lblDungeonClosed.setText("Dungeon (closed):");
		JLabel lblLadderUp = new JLabel();
		lblLadderUp.setText("Ladder Up:");
		JLabel lblLadderDown = new JLabel();
		lblLadderDown.setText("Ladder Down:");

		txtCityGateOpen = new JTextField();
		txtCityGateOpen.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				ExitType.CITY_GATE.setOpenImage(txtCityGateOpen.getText());
			}
		});
		txtCityGateClosed = new JTextField();
		txtCityGateClosed.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				ExitType.CITY_GATE.setClosedImage(txtCityGateClosed.getText());
			}
		});
		txtDungeonOpen = new JTextField();
		txtDungeonOpen.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				ExitType.DUNGEON.setOpenImage(txtDungeonOpen.getText());
			}
		});
		txtDungeonClosed = new JTextField();
		txtDungeonClosed.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				ExitType.DUNGEON.setClosedImage(txtDungeonClosed.getText());
			}
		});
		txtLadderUp = new JTextField();
		txtLadderUp.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				ExitType.LADDER_UP.setImage(txtLadderUp.getText(), txtLadderUp.getText());
			}
		});
		txtLadderDown = new JTextField();
		txtLadderDown.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				ExitType.LADDER_DOWN.setImage(txtLadderDown.getText(), txtLadderDown.getText());
			}
		});
		
		// Add labels
		pnlExitImages.add(lblCityGateOpen, lstObjectsC);
		lstObjectsC.gridy = 2;
		pnlExitImages.add(lblCityGateClosed, lstObjectsC);
		lstObjectsC.gridy = 4;
		pnlExitImages.add(lblDungeonOpen, lstObjectsC);
		lstObjectsC.gridy = 6;
		pnlExitImages.add(lblDungeonClosed, lstObjectsC);
		lstObjectsC.gridy = 8;
		pnlExitImages.add(lblLadderUp, lstObjectsC);
		lstObjectsC.gridy = 10;
		pnlExitImages.add(lblLadderDown, lstObjectsC);
		
		// Add text fields
		lstObjectsC.insets = new Insets(0, 20, 5, 20);
		lstObjectsC.gridy = 1;
		pnlExitImages.add(txtCityGateOpen, lstObjectsC);
		lstObjectsC.gridy = 3;
		pnlExitImages.add(txtCityGateClosed, lstObjectsC);
		lstObjectsC.gridy = 5;
		pnlExitImages.add(txtDungeonOpen, lstObjectsC);
		lstObjectsC.gridy = 7;
		pnlExitImages.add(txtDungeonClosed, lstObjectsC);
		lstObjectsC.gridy = 9;
		pnlExitImages.add(txtLadderUp, lstObjectsC);
		lstObjectsC.gridy = 11;
		pnlExitImages.add(txtLadderDown, lstObjectsC);
		
		fillExitPanel();
		
		return pnlExitImages;
	}

	private static void fillExitPanel() {
		txtCityGateOpen.setText(ExitType.CITY_GATE.getOpenImage());
		txtCityGateClosed.setText(ExitType.CITY_GATE.getClosedImage());
		txtDungeonOpen.setText(ExitType.DUNGEON.getOpenImage());
		txtDungeonClosed.setText(ExitType.DUNGEON.getClosedImage());
		txtLadderUp.setText(ExitType.LADDER_UP.getOpenImage());
		txtLadderDown.setText(ExitType.LADDER_DOWN.getOpenImage());
	}
}
