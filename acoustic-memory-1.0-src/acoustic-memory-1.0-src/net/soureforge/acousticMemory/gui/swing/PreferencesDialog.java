/**
 * File:    PreferencesDialog.java
 * Created: 29.12.2005
 *
 *
 * Copyright (c) 2005  Markus Bauer <markusbauer@users.sourceforge.net>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */


package net.soureforge.acousticMemory.gui.swing;


import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import net.soureforge.acousticMemory.gui.Common;
import net.soureforge.acousticMemory.gui.Gui;




/**
 * Dialog to edit game preferences.
 * 
 * @version $Id: PreferencesDialog.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public class PreferencesDialog
    extends JDialog
    implements ActionListener
{
  /**
   * Maximum extend of the game field.
   */
  private static final int MAX_SIZE = 99;


  /**
   * Selection of game width.
   */
  private JSpinner widthSpinner = new JSpinner();


  /**
   * Selection of game height.
   */
  private JSpinner heightSpinner = new JSpinner();


  /**
   * The text field containing the directory for audio files.
   */
  private JTextField directoryTextField = new JTextField();


  /**
   * Button that selects the directory for audio files.
   */
  private JButton selectDirectoryButton = new JButton();


  /**
   * Button for OK.
   */
  private JButton okButton = new JButton();


  /**
   * Button for cancel.
   */
  private JButton cancelButton = new JButton();




  /**
   * @param owner
   */
  public PreferencesDialog(Frame owner)
  {
    super(owner, Gui.MESSAGES.getString("gamePreferences"), false);

    Runnable action = new Runnable()
    {
      public void run()
      {
        initGui();
      }
    };
    SwingUtilities.invokeLater(action);
  }



  /**
   * Creations of components.
   */
  void initGui()
  {
    // spinner
    widthSpinner.setModel(new SpinnerNumberModel(Common.getInstance()
        .getFieldSize().width, 2, MAX_SIZE, 1));
    heightSpinner.setModel(new SpinnerNumberModel(Common.getInstance()
        .getFieldSize().height, 2, MAX_SIZE, 1));

    JLabel widthLabel = new JLabel(Gui.MESSAGES
        .getString("gamePreferencesWidth"));
    widthLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    JLabel heightLabel = new JLabel(Gui.MESSAGES
        .getString("gamePreferencesHeight"));
    heightLabel.setHorizontalAlignment(SwingConstants.RIGHT);

    JPanel sizePanel = new JPanel();
    sizePanel.setBorder(new TitledBorder(SwingGui.DEFAULT_BORDER, Gui.MESSAGES
        .getString("gamePreferencesField")));
    sizePanel.setLayout(new GridLayout(2, 2, 5, 5));

    sizePanel.add(widthLabel);
    sizePanel.add(widthSpinner);
    sizePanel.add(heightLabel);
    sizePanel.add(heightSpinner);


    // audio
    JPanel audioPanel = new JPanel();
    audioPanel.setBorder(new TitledBorder(SwingGui.DEFAULT_BORDER, Gui.MESSAGES
        .getString("gamePreferencesAudioDirectory")));
    audioPanel.setLayout(new BoxLayout(audioPanel, BoxLayout.X_AXIS));

    directoryTextField.setText(Common.getInstance().getAudioDirectory());

    selectDirectoryButton.setText(Gui.MESSAGES
        .getString("gamePreferencesChangeDirectory"));
    selectDirectoryButton.setToolTipText(Gui.MESSAGES
        .getString("gamePreferencesChangeDirectoryToolTip"));
    selectDirectoryButton.addActionListener(this);

    audioPanel.add(directoryTextField);
    audioPanel.add(selectDirectoryButton);


    // buttons
    okButton.setText(Gui.MESSAGES.getString("buttonOk"));
    okButton.addActionListener(this);

    cancelButton.setText(Gui.MESSAGES.getString("buttonCancel"));
    cancelButton.addActionListener(this);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setBorder(SwingGui.DEFAULT_BORDER);
    buttonPanel.add(okButton);
    buttonPanel.add(cancelButton);

    // main
    setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    setLocationRelativeTo(getOwner());

    add(sizePanel);
    add(audioPanel);
    add(buttonPanel);
    pack();
  }



  /* (non-Javadoc)
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == okButton)
    {
      int newWidth = ((Number) widthSpinner.getValue()).intValue();
      int newHeight = ((Number) heightSpinner.getValue()).intValue();

      Common.getInstance().setPreferences(newWidth, newHeight,
                                          directoryTextField.getText());

      dispose();
    }
    else if (e.getSource() == cancelButton)
    {
      dispose();
    }
    else if (e.getSource() == selectDirectoryButton)
    {
      JFileChooser chooser = new JFileChooser(directoryTextField.getText());
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      int result = chooser.showOpenDialog(this);

      if (result == JFileChooser.APPROVE_OPTION)
      {
        directoryTextField.setText(chooser.getSelectedFile().getPath());
      }
    }
  }
}
