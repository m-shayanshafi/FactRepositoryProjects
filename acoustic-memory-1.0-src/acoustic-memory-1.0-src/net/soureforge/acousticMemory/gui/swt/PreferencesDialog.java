/**
 * File:    PreferencesDialog.java
 * Created: 03.01.2006
 *
 *
 * Copyright (c) 2006  Markus Bauer <markusbauer@users.sourceforge.net>
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


package net.soureforge.acousticMemory.gui.swt;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import net.soureforge.acousticMemory.gui.Common;
import net.soureforge.acousticMemory.gui.Gui;




/**
 * Dialog to edit game preferences.
 * 
 * @version $Id: PreferencesDialog.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public class PreferencesDialog
    extends AbstractDialog
    implements SelectionListener
{
  /**
   * Maximum extend of the game field.
   */
  private static final int MAX_SIZE = 99;


  /**
   * Selection of game width.
   */
  private Spinner widthSpinner;


  /**
   * Selection of game height.
   */
  private Spinner heightSpinner;


  /**
   * The text field containing the directory for audio files.
   */
  private Text directoryTextField;


  /**
   * Button that selects the directory for audio files.
   */
  private Button selectDirectoryButton;


  /**
   * Button for OK.
   */
  private Button okButton;


  /**
   * Button for cancel.
   */
  private Button cancelButton;




  /**
   * @param parent
   */
  public PreferencesDialog(Shell parent)
  {
    super(parent);
  }



  /* (non-Javadoc)
   * @see akustikMemory.gui.swt.AbstractDialog#createGui(org.eclipse.swt.widgets.Shell)
   */
  @Override
  public void createGui(Shell parent)
  {
    parent.setText(Gui.MESSAGES.getString("gamePreferences"));

    GridLayout mainLayout = new GridLayout();
    mainLayout.numColumns = 1;
    parent.setLayout(mainLayout);


    // game dimensions
    Group sizeGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
    sizeGroup.setText(Gui.MESSAGES.getString("gamePreferencesField"));
    sizeGroup.setLayoutData(new GridData(GridData.FILL_BOTH));

    GridLayout sizeLayout = new GridLayout();
    sizeLayout.numColumns = 2;
    sizeGroup.setLayout(sizeLayout);

    Label widthLabel = new Label(sizeGroup, SWT.RIGHT);
    widthLabel.setText(Gui.MESSAGES.getString("gamePreferencesWidth"));
    widthLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    widthSpinner = new Spinner(sizeGroup, SWT.NONE);
    widthSpinner.setMinimum(1);
    widthSpinner.setMaximum(MAX_SIZE);
    widthSpinner.setIncrement(1);
    widthSpinner.setSelection(Common.getInstance().getFieldSize().width);

    Label heightLabel = new Label(sizeGroup, SWT.RIGHT);
    heightLabel.setText(Gui.MESSAGES.getString("gamePreferencesHeight"));
    heightLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    heightSpinner = new Spinner(sizeGroup, SWT.NONE);
    heightSpinner.setMinimum(1);
    heightSpinner.setMaximum(MAX_SIZE);
    heightSpinner.setIncrement(1);
    heightSpinner.setSelection(Common.getInstance().getFieldSize().width);


    // audio
    Group audioGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
    audioGroup.setText(Gui.MESSAGES.getString("gamePreferencesAudioDirectory"));
    audioGroup.setLayoutData(new GridData(GridData.FILL_BOTH));

    GridLayout audioLayout = new GridLayout();
    audioLayout.numColumns = 2;
    audioGroup.setLayout(audioLayout);

    directoryTextField = new Text(audioGroup, SWT.SINGLE);
    directoryTextField.setText(Common.getInstance().getAudioDirectory());
    directoryTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    selectDirectoryButton = new Button(audioGroup, SWT.PUSH);
    selectDirectoryButton.setText(Gui.MESSAGES
        .getString("gamePreferencesChangeDirectory"));
    selectDirectoryButton.setToolTipText(Gui.MESSAGES
        .getString("gamePreferencesChangeDirectoryToolTip"));
    selectDirectoryButton.addSelectionListener(this);


    // buttons
    Group buttonGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
    buttonGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    GridLayout buttonLayout = new GridLayout();
    buttonLayout.numColumns = 2;
    buttonGroup.setLayout(buttonLayout);

    okButton = new Button(buttonGroup, SWT.PUSH);
    okButton.setText(Gui.MESSAGES.getString("buttonOk"));
    okButton.addSelectionListener(this);
    okButton.setLayoutData(new GridData(SWT.END, SWT.NONE, true, false));

    cancelButton = new Button(buttonGroup, SWT.PUSH);
    cancelButton.setText(Gui.MESSAGES.getString("buttonCancel"));
    cancelButton.addSelectionListener(this);
    cancelButton.setLayoutData(new GridData(SWT.END, SWT.NONE, false, false));

    parent.pack();
  }



  /* (non-Javadoc)
   * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
   */
  public void widgetSelected(SelectionEvent e)
  {
    if (e.widget == okButton)
    {
      int newWidth = widthSpinner.getSelection();
      int newHeight = heightSpinner.getSelection();

      Common.getInstance().setPreferences(newWidth, newHeight,
                                          directoryTextField.getText());

      dispose();
    }
    else if (e.widget == cancelButton)
    {
      dispose();
    }
    else if (e.widget == selectDirectoryButton)
    {
      DirectoryDialog dialog = new DirectoryDialog(getShell());
      dialog.setFilterPath(directoryTextField.getText());
      String directory = dialog.open();

      if (directory != null)
      {
        directoryTextField.setText(directory);
      }
    }
  }



  /* (non-Javadoc)
   * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
   */
  public void widgetDefaultSelected(SelectionEvent e)
  {
    // not used
  }
}
