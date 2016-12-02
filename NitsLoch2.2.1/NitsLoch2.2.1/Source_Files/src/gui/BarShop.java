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

import java.awt.*;

import javax.swing.*;

import src.enums.Drinks;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import src.Constants;

/**
 * GUI file for the bar.
 * @author Darren Watts
 * date 1/8/08
 */
public class BarShop extends JFrame {
	private static final long serialVersionUID = Constants.serialVersionUID;

    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JRadioButton rbnTonicWater = new JRadioButton();
    JRadioButton rbnSoda = new JRadioButton();
    JRadioButton rbnGin = new JRadioButton();
    JRadioButton rbnRum = new JRadioButton();
    JRadioButton rbnScotch = new JRadioButton();
    JRadioButton rbnRedEye = new JRadioButton();
    JButton btnCancel = new JButton();
    JButton btnOK = new JButton();
    ButtonGroup buttonGroup1 = new ButtonGroup();

    private int permutation;
    JLabel lblTonicWaterPrice = new JLabel();
    JLabel lblSodaPrice = new JLabel();
    JLabel lblGinPrice = new JLabel();
    JLabel lblRumPrice = new JLabel();
    JLabel lblScotchPrice = new JLabel();
    JLabel lblRedeyePrice = new JLabel();
    
    private Controller controller;

    public BarShop(Controller controller, int permutation) {
        try {
        	this.controller = controller;
        	this.permutation = permutation;
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        getContentPane().setLayout(gridBagLayout1);
        rbnTonicWater.setText("Tonic Water");
        rbnSoda.setText("Soda");
        rbnRum.setText("Rum");
        rbnScotch.setText("Scotch");
        rbnRedEye.setText("Red Eye");
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new BarShop_btnCancel_actionAdapter(this));
        btnOK.setText("OK");
        btnOK.addActionListener(new BarShop_btnOK_actionAdapter(this));
        lblTonicWaterPrice.setText("0");
        lblSodaPrice.setText("0");
        lblGinPrice.setText("0");
        lblRumPrice.setText("0");
        lblScotchPrice.setText("0");
        lblRedeyePrice.setText("0");
        this.getContentPane().add(rbnSoda,
                                  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        rbnGin.setText("Gin");
        this.getContentPane().add(rbnTonicWater,
                                  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(rbnGin,
                                  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(rbnRum,
                                  new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(rbnScotch,
                                  new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(rbnRedEye,
                                  new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        buttonGroup1.add(rbnTonicWater);
        buttonGroup1.add(rbnSoda);
        buttonGroup1.add(rbnGin);
        buttonGroup1.add(rbnRum);
        buttonGroup1.add(rbnScotch);
        buttonGroup1.add(rbnRedEye);
        this.getContentPane().add(btnOK,
                                  new GridBagConstraints(0, 7, 2, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 0, 5), 0, 0));

        this.getContentPane().add(btnCancel,
                                  new GridBagConstraints(0, 6, 2, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 0, 5), 0, 0));
        this.getContentPane().add(lblRedeyePrice,
                                  new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 5, 0, 0), 0, 0));
        this.getContentPane().add(lblScotchPrice,
                                  new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 5, 0, 0), 0, 0));
        this.getContentPane().add(lblRumPrice,
                                  new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 5, 0, 0), 0, 0));
        this.getContentPane().add(lblGinPrice,
                                  new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 5, 0, 0), 0, 0));
        this.getContentPane().add(lblTonicWaterPrice,
                                  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 6, 0, 0), 0, 0));
        this.getContentPane().add(lblSodaPrice,
                                  new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 5, 0, 0), 0, 0));
        lblTonicWaterPrice.setText("$ " + Drinks.TONIC_WATER.getCost());
        lblSodaPrice.setText("$ " + Drinks.SODA.getCost());
        lblGinPrice.setText("$ " + Drinks.GIN.getCost());
        lblRumPrice.setText("$ " + Drinks.RUM.getCost());
        lblScotchPrice.setText("$ " + Drinks.SCOTCH.getCost());
        lblRedeyePrice.setText("$ " + Drinks.REDEYE.getCost());

        setSize(150, 270);
        setTitle("Bar");
        src.Constants.centerFrame(this);
        setVisible(true);

    }

    /**
     * Closes the Bar GUI window.
     */
    private void closeWindow(){
    	this.dispose();
    }

    /**
     * Action listener for the cancel button.  Closes the window.
     * @param e ActionEvent
     */
    public void btnCancel_actionPerformed(ActionEvent e) {
    	closeWindow();
    }

    /**
     * Action listener for the OK button.  Purchases the item.
     * @param e ActionEvent
     */
    public void btnOK_actionPerformed(ActionEvent e) {
    	Drinks selectedDrink;

    	if(rbnTonicWater.isSelected())
    		selectedDrink = Drinks.TONIC_WATER;
    	else if(rbnSoda.isSelected())
    		selectedDrink = Drinks.SODA;
    	else if(rbnGin.isSelected())
    		selectedDrink = Drinks.GIN;
    	else if(rbnRum.isSelected())
    		selectedDrink = Drinks.RUM;
    	else if(rbnScotch.isSelected())
    		selectedDrink = Drinks.SCOTCH;
    	else if(rbnRedEye.isSelected())
    		selectedDrink = Drinks.REDEYE;
    	else return;

    	controller.purchaseDrink(selectedDrink, permutation);

    	closeWindow();
    }
}


class BarShop_btnOK_actionAdapter implements ActionListener {
    private BarShop adaptee;
    BarShop_btnOK_actionAdapter(BarShop adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnOK_actionPerformed(e);
    }
}


class BarShop_btnCancel_actionAdapter implements ActionListener {
    private BarShop adaptee;
    BarShop_btnCancel_actionAdapter(BarShop adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnCancel_actionPerformed(e);
    }
}
