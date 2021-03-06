/*
        This file is part of NitsLoch.

        Copyright (C) 2010 Darren Watts

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

import src.Constants;
import src.scenario.Images;

public class SplashFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	BorderLayout borderLayout1 = new BorderLayout();
    ImageIcon image;
    JLabel jLabel1 = new JLabel();

    public SplashFrame() {
        try {
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
    	Images.getInstance().add("splash.png");
        getContentPane().setLayout(borderLayout1);
        this.getContentPane().add(jLabel1, java.awt.BorderLayout.NORTH);
        try {
            image = new ImageIcon(Images.getInstance().getImage("splash.png"));
            jLabel1.setIcon(image);
        } catch (Exception ex) {
            jLabel1.setText("Picture missing");
            return;
        }

        setSize(image.getIconWidth(), image.getIconHeight());
        Constants.centerFrame(this);
        setResizable(false);
        setUndecorated(true);
        setVisible(true);
    }
}