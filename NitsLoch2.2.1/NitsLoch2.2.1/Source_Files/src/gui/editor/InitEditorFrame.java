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

package src.gui.editor;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.*;

import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JButton;

import src.file.map.MapFile;
import src.game.GameWorld;
import src.game.TheEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * This is the first window for the editor.  It asks the user for a
 * map name and the size of the level.  It will then create the main
 * editor window with a map with the specified stats.
 * @author Darren Watts
 * date 1/27/08
 */
public class InitEditorFrame extends JFrame {
	private static final long serialVersionUID = src.Constants.serialVersionUID;
	
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel lblLevelName = new JLabel();
    JTextField txtLevelName = new JTextField();
    JLabel lblSize = new JLabel();
    JLabel lblRows = new JLabel();
    JLabel lblColumns = new JLabel();
    JButton btnLoad = new JButton();
    JButton btnOK = new JButton();
    JTextField txtRows = new JTextField();
    JTextField txtColumns = new JTextField();
    
    private TheEditor editor;

    public InitEditorFrame(TheEditor editor) {
        try {
        	this.editor = editor;
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(gridBagLayout1);
        lblLevelName.setText("Enter a level name:");
        txtLevelName.setText("");
        lblSize.setText("Enter size of world:");
        lblColumns.setText("Columns:");
        lblRows.setText("Rows:");
        btnLoad.setText("Load");
        btnLoad.addActionListener(new InitEditorFrame_btnLoad_actionAdapter(this));
        btnOK.setText("OK");
        btnOK.addActionListener(new InitEditorFrame_btnOK_actionAdapter(this));
        txtRows.setText("");
        txtColumns.setText("");
        this.getContentPane().add(txtLevelName,
                                  new GridBagConstraints(0, 1, 3, 1, 1.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(lblLevelName,
                                  new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(lblSize,
                                  new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 0, 0, 0), 0, 0));
        this.getContentPane().add(btnOK,
                                  new GridBagConstraints(2, 5, 1, 2, 1.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(10, 5, 0, 5), 0, 0));
        this.getContentPane().add(btnLoad,
                                  new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(10, 5, 0, 5), 0, 0));
        this.getContentPane().add(lblColumns,
                                  new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 0, 0, 0), 0, 0));
        this.getContentPane().add(lblRows,
                                  new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 0, 0, 0), 0, 0));
        this.getContentPane().add(txtRows,
                                  new GridBagConstraints(1, 4, 1, 2, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        this.getContentPane().add(txtColumns,
                                  new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        
        setSize(200, 200);
		setTitle("New Level");
		src.Constants.centerFrame(this);
		setVisible(true);

    }

    public void btnLoad_actionPerformed(ActionEvent e) {
    	try {
    		final JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/maps"));
			int retVal = fc.showOpenDialog(this);
			if(retVal == JFileChooser.APPROVE_OPTION){
				/*FileInputStream fin = new FileInputStream(fc.getSelectedFile());
				ObjectInputStream ois = new ObjectInputStream(fin);
				GameWorld.getInstance().load(ois);
				ois.close();*/
				
				//Error checking
				File f = fc.getSelectedFile();
				if(f.exists())
					MapFile.getInstance().load(fc.getSelectedFile().getAbsolutePath());
				else return;
			}
			else return;

    	} catch(Exception ex){ return; }
    	
    	// Create editor window and hide this one.
    	new EditorControlFrame(editor);
    	
    	this.dispose();
    }

    public void btnOK_actionPerformed(ActionEvent e) {
    	GameWorld.getInstance().resetCities();
    	try {
    		String levelName = "";
    		int rows = 0, cols = 0;
    		levelName = txtLevelName.getText();
    		rows = Integer.parseInt(txtRows.getText());
    		cols = Integer.parseInt(txtColumns.getText());
    		
    		if(levelName.equals("") || rows < 1 || cols < 1) return;
    		
    		GameWorld.getInstance().newCity(levelName, rows, cols);
    	} catch(Exception ex){ return; }
    	
    	// Create editor window and hide this one.
    	new EditorControlFrame(editor);
    	//EditorWindow ed = new EditorWindow();
    	//editor.setWindow(ed);
    	this.dispose();
    }

}


class InitEditorFrame_btnOK_actionAdapter implements ActionListener {
    private InitEditorFrame adaptee;
    InitEditorFrame_btnOK_actionAdapter(InitEditorFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnOK_actionPerformed(e);
    }
}


class InitEditorFrame_btnLoad_actionAdapter implements ActionListener {
    private InitEditorFrame adaptee;
    InitEditorFrame_btnLoad_actionAdapter(InitEditorFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnLoad_actionPerformed(e);
    }
}
