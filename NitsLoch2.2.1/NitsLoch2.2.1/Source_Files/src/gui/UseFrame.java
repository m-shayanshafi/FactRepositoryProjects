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

import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

import src.game.Messages;
import src.game.Player;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

/**
 * GUI frame for using an item.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public class UseFrame extends JFrame {

    private static final long serialVersionUID = src.Constants.serialVersionUID;

    private Player player;
    private Controller controller;

    ButtonGroup buttonGroup1 = new ButtonGroup();
    JRadioButton rbnGrenade = new JRadioButton();
    JRadioButton rbnDynamite = new JRadioButton();
    JRadioButton rbnBandaid = new JRadioButton();
    JButton btnCancel = new JButton();
    JButton btnOK = new JButton();
    GridBagLayout gridBagLayout1 = new GridBagLayout();

    private GameFrame gameFrame;

    public UseFrame(Player player, Controller controller, GameFrame gameFrame) {
        try {
            this.player = player;
            this.controller = controller;
            this.gameFrame = gameFrame;
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Closes the use frame window.
     */
    private void closeWindow() {
        gameFrame.setPanelFocus();
        this.dispose();
    }

    /**
     * Initializes the frame.
     * @throws Exception
     */
    private void jbInit() throws Exception {
        getContentPane().setLayout(gridBagLayout1);
        rbnGrenade.setText("Grenade");
        rbnBandaid.setToolTipText("");
        rbnBandaid.setText("Bandaid");
        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (rbnBandaid.isSelected()) {
                    player.removeBandaid(1);
                    player.heal(player.getMaxHitPoints());
                    Messages.getInstance().addMessage("You feel much better.");
                } else if (rbnGrenade.isSelected()) {
                    player.removeGrenade(1);
                    controller.setGrenade(player.getRow(), player.getCol());
                    Messages.getInstance().addMessage(
                            "You have two moves before the grenade goes off.");
                } else if (rbnDynamite.isSelected()) {
                    player.removeDynamite(1);
                    controller.setDynamite(player.getRow(), player.getCol());
                    Messages.getInstance().addMessage(
                            "You have two moves before the dynamite goes off.");
                }
                closeWindow();
            }
        });
        btnCancel.setText("Cancel");
        btnCancel.setMargin(new Insets(0, 0, 0, 0));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                closeWindow();
            }
        });
        rbnDynamite.setText("Dynamite");
        buttonGroup1.add(rbnBandaid);
        buttonGroup1.add(rbnGrenade);
        buttonGroup1.add(rbnDynamite);
        this.getContentPane().add(btnCancel,
                                  new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 0, 5), 0, 0));
        this.getContentPane().add(btnOK,
                                  new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(10, 5, 10, 5), 0, 0));
        this.getContentPane().add(rbnBandaid,
                                  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(rbnGrenade,
                                  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(rbnDynamite,
                                  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

        if (player.getNumBandaids() <= 0)
            rbnBandaid.setVisible(false);
        if (player.getNumGrenades() <= 0)
            rbnGrenade.setVisible(false);
        if (player.getNumDynamite() <= 0)
            rbnDynamite.setVisible(false);

        setSize(170, 170);
        setTitle("Use an item");
        src.Constants.centerFrame(this);
        setVisible(true);
    }
}
