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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import src.exceptions.NoSuchCityException;
import src.game.TheGame;
import src.game.Player;
import src.game.GameWorld;
import java.io.File;

/**
 * Creates a new player and GameFrame.  Adds a player to the GameWorld based
 * on the stats specified in this frame.
 * @author Darren Watts
 * date 11/10/07
 *
 */
public class CreatePlayerFrame extends JFrame {
	private static final long serialVersionUID = src.Constants.serialVersionUID;

	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JLabel lblCharacterName = new JLabel();
	JTextField txtName = new JTextField();
	JLabel lblHealth = new JLabel();
	JLabel lblFightingAbil = new JLabel();
	JLabel lblMarksmanship = new JLabel();
	JLabel lblMartialArts = new JLabel();
	JLabel lblThievingAbil = new JLabel();
	JLabel lblMoney = new JLabel();
	JLabel lblHealthNum = new JLabel();
	JLabel lblFightNum = new JLabel();
	JLabel lblMarksmanNum = new JLabel();
	JLabel lblMartialArtsNum = new JLabel();
	JLabel lblThievingNum = new JLabel();
	JLabel lblMoneyNum = new JLabel();
	JButton btnGenerate = new JButton();
	JButton btnAccept = new JButton();
	JButton btnLoad = new JButton();
	JButton btnQuit = new JButton();

	private Controller controller;
    JMenuBar jMenuBar1 = new JMenuBar();
    JMenu mnuOptions = new JMenu();
    JMenuItem mnuOptionsLevelSkip = new JMenuItem();
    public CreatePlayerFrame() {
		try {
			jbInit();
			controller = new Controller();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		getContentPane().setLayout(gridBagLayout1);
		lblCharacterName.setText("Character Name:");
		lblHealth.setText("Health:");
		lblFightingAbil.setText("Fighting Ability:");
		lblMarksmanship.setText("Marksmanship:");
		lblMartialArts.setText("Martial Arts Ability:");
		lblThievingAbil.setText("Thieving Ability:");
		lblMoney.setText("Money:");
		lblHealthNum.setText("10");
		lblFightNum.setText("10");
		lblMarksmanNum.setText("10");
		lblMartialArtsNum.setText("10");
		lblThievingNum.setText("10");
		lblMoneyNum.setText("10");
		btnGenerate.setText("Generate");
		btnGenerate.addActionListener(new
				CreatePlayerFrame_btnGenerate_actionAdapter(this));
		btnAccept.setText("Accept");
		btnAccept.addActionListener(new
				CreatePlayerFrame_btnAccept_actionAdapter(this));
		btnLoad.setText("Load Game");
		btnLoad.addActionListener(new CreatePlayerFrame_btnLoad_actionAdapter(this));
		btnQuit.setText("Quit Game");
		btnQuit.addActionListener(new CreatePlayerFrame_btnQuit_actionAdapter(this));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setJMenuBar(jMenuBar1);
        mnuOptions.setText("Options");
        mnuOptionsLevelSkip.setText("Level Skip");
        mnuOptionsLevelSkip.addActionListener(new
                CreatePlayerFrame_mnuOptionsLevelSkip_actionAdapter(this));
        this.getContentPane().add(lblCharacterName,
				new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
						, GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(lblMoneyNum,
				new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
						, GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(5, 5, 0, 0), 0, 0));
		this.getContentPane().add(lblThievingNum,
				new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
						, GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(5, 5, 0, 0), 0, 0));
		this.getContentPane().add(lblMartialArtsNum,
				new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
						, GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(5, 5, 0, 0), 0, 0));
		this.getContentPane().add(lblMarksmanNum,
				new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
						, GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(5, 5, 0, 0), 0, 0));
		this.getContentPane().add(lblFightNum,
				new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
						, GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(5, 5, 0, 0), 0, 0));
		this.getContentPane().add(lblHealthNum,
				new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0
						, GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(10, 5, 0, 0), 0, 0));
		this.getContentPane().add(lblMoney,
				new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
						, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 0, 0, 5), 0, 0));
		this.getContentPane().add(lblThievingAbil,
				new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
						, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 0, 0, 5), 0, 0));
		this.getContentPane().add(lblMartialArts,
				new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
						, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 0, 0, 5), 0, 0));
		this.getContentPane().add(lblMarksmanship,
				new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
						, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 0, 0, 5), 0, 0));
		this.getContentPane().add(lblFightingAbil,
				new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
						, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 0, 0, 5), 0, 0));
		this.getContentPane().add(lblHealth,
				new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
						, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(10, 0, 0, 5), 0, 0));
		this.getContentPane().add(txtName,
				new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0
						, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(0, 5, 0, 5), 0, 0));
		this.getContentPane().add(btnQuit,
				new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0
						, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(10, 5, 0, 5), 0, 0));
		this.getContentPane().add(btnLoad,
				new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
						, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(10, 5, 0, 5), 0, 0));
		this.getContentPane().add(btnAccept,
				new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
						, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(10, 5, 0, 5), 0, 0));
		this.getContentPane().add(btnGenerate,
				new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
						, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(10, 5, 0, 5), 0, 0));
        jMenuBar1.add(mnuOptions);
        mnuOptions.add(mnuOptionsLevelSkip);
        txtName.setText("");

		randomizeStats();

		setSize(310, 310);
		setTitle("Create a new Character");
		src.Constants.centerFrame(this);
		setVisible(true);
	}

	/**
	 * Closes the window.
	 */
	protected void closeWindow() {
		this.dispose();
	}

	/**
	 * Randomizes the stats for the player.
	 *
	 */
	private void randomizeStats() {
		int health, fighting, marksman, thieving, martial, money;
		health = (int) (Math.random() * 80) + 100;
		fighting = (int) (Math.random() * 20) + 40;
		marksman = (int) (Math.random() * 20) + 40;
		martial = (int) (Math.random() * 20) + 40;
		thieving = (int) (Math.random() * 35) + 10;
		money = (int) (Math.random() * 12) + 5;
		if (health > 173) health = 173;

		lblHealthNum.setText(String.valueOf(health));
		lblFightNum.setText(String.valueOf(fighting));
		lblMarksmanNum.setText(String.valueOf(marksman));
		lblMartialArtsNum.setText(String.valueOf(martial));
		lblThievingNum.setText(String.valueOf(thieving));
		lblMoneyNum.setText(String.valueOf(money));
	}


	public void btnGenerate_actionPerformed(ActionEvent e) {
		randomizeStats();
	}

	public void btnAccept_actionPerformed(ActionEvent e) {
		try {
			Player player = new Player(
					txtName.getText(),
					Integer.parseInt(lblHealthNum.getText()),
					Integer.parseInt(lblFightNum.getText()),
					Integer.parseInt(lblMarksmanNum.getText()),
					Integer.parseInt(lblMartialArtsNum.getText()),
					Integer.parseInt(lblThievingNum.getText()),
					Integer.parseInt(lblMoneyNum.getText()));

			int index = GameWorld.getInstance().addPlayer(player);
			TheGame.getInstance().setController(controller);
			TheGame.getInstance().setLocalPlayerIndex(index);

			GameFrame frame = new GameFrame(controller);
			controller.setGameFrame(frame);
			closeWindow();

		} catch (NoSuchCityException noCity) {
			//noCity.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Problem reading map file. Make sure you have the correct path specified in your scenario file.",
					"Roof Notification!",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	public void btnLoad_actionPerformed(ActionEvent e) {
		final JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(System.getProperty("user.dir") +
		"/saves"));
		int retVal = fc.showOpenDialog(CreatePlayerFrame.this);
		if (retVal == JFileChooser.APPROVE_OPTION) {

			TheGame.getInstance().setSavePath(fc.getSelectedFile().getPath());

			try {

				Player player = new Player(
						txtName.getText(),
						Integer.parseInt(lblHealthNum.getText()),
						Integer.parseInt(lblFightNum.getText()),
						Integer.parseInt(lblMarksmanNum.getText()),
						Integer.parseInt(lblMartialArtsNum.getText()),
						Integer.parseInt(lblThievingNum.getText()),
						Integer.parseInt(lblMoneyNum.getText()));

				int index = GameWorld.getInstance().addPlayer(player);
				TheGame.getInstance().setController(controller);
				TheGame.getInstance().setLocalPlayerIndex(index);

				GameFrame frame = new GameFrame(controller);
				controller.setGameFrame(frame);

				controller.loadGame(fc.getSelectedFile().getPath());

			} catch( Exception ex ) { ex.printStackTrace(); }

			closeWindow();
		} else return;

	}

	public void btnQuit_actionPerformed(ActionEvent e) {
		System.exit(0);
	}

    public void mnuOptionsLevelSkip_actionPerformed(ActionEvent e) {
        try {
            Player player = new Player(
                    txtName.getText(),
                    Integer.parseInt(lblHealthNum.getText()),
                    Integer.parseInt(lblFightNum.getText()),
                    Integer.parseInt(lblMarksmanNum.getText()),
                    Integer.parseInt(lblMartialArtsNum.getText()),
                    Integer.parseInt(lblThievingNum.getText()),
                    Integer.parseInt(lblMoneyNum.getText()));

            int index = GameWorld.getInstance().addPlayer(player);
            TheGame.getInstance().setController(controller);
            TheGame.getInstance().setLocalPlayerIndex(index);

           /* GameFrame frame = new GameFrame(controller);
                        controller.setGameFrame(frame);
                        frame.setVisible(false);*/


            //closeWindow();

        } catch (NoSuchCityException noCity) {
            //noCity.printStackTrace();
            JOptionPane.showMessageDialog(null,
                                          "Problem reading map file. Make sure you have the correct path specified in your scenario file.",
                                          "Roof Notification!",
                                          JOptionPane.ERROR_MESSAGE);
        }

        new LevelSkipFrame(controller, this);
    }
}


class CreatePlayerFrame_mnuOptionsLevelSkip_actionAdapter implements
        ActionListener {
    private CreatePlayerFrame adaptee;
    CreatePlayerFrame_mnuOptionsLevelSkip_actionAdapter(CreatePlayerFrame
            adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.mnuOptionsLevelSkip_actionPerformed(e);
    }
}


class CreatePlayerFrame_btnQuit_actionAdapter implements ActionListener {
	private CreatePlayerFrame adaptee;
	CreatePlayerFrame_btnQuit_actionAdapter(CreatePlayerFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.btnQuit_actionPerformed(e);
	}
}


class CreatePlayerFrame_btnLoad_actionAdapter implements ActionListener {
	private CreatePlayerFrame adaptee;
	CreatePlayerFrame_btnLoad_actionAdapter(CreatePlayerFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.btnLoad_actionPerformed(e);
	}
}


class CreatePlayerFrame_btnAccept_actionAdapter implements ActionListener {
	private CreatePlayerFrame adaptee;
	CreatePlayerFrame_btnAccept_actionAdapter(CreatePlayerFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.btnAccept_actionPerformed(e);
	}
}


class CreatePlayerFrame_btnGenerate_actionAdapter implements ActionListener {
	private CreatePlayerFrame adaptee;
	CreatePlayerFrame_btnGenerate_actionAdapter(CreatePlayerFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.btnGenerate_actionPerformed(e);
	}
}
