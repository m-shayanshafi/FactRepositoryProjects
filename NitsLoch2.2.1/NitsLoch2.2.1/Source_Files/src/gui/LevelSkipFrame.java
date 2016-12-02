/*
        This file is part of NitsLoch.

        Copyright (C) 2008 Darren Watts

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
import javax.swing.table.TableColumn;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.table.TableCellRenderer;

public class LevelSkipFrame extends JFrame {
	private static final long serialVersionUID = src.Constants.serialVersionUID;

	private String[] columnNames = {
			"City Name", "Start City", "Enemies"
	};

	private Controller controller;
	private CreatePlayerFrame plrFrame;

	private JTable table;
	private JScrollPane scroller;
	private LevelSkipModel tableModel;
	JMenuBar jMenuBar1 = new JMenuBar();
	JMenu jMenu1 = new JMenu();
	JMenuItem jMenuItem1 = new JMenuItem();

	public LevelSkipFrame(Controller controller, CreatePlayerFrame plrFrame) {
		this.controller = controller;
		this.plrFrame = plrFrame;
		try {
			jbInit();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		getContentPane().setLayout(new BorderLayout());
		tableModel = new LevelSkipModel(columnNames, controller);
		tableModel.addTableModelListener(new LevelSkipFrame.LevelSkipTableModelListener());
		table = new JTable(tableModel);
		table.setSurrendersFocusOnKeystroke(true);
		table.getTableHeader().setReorderingAllowed(false);

		TableColumn startCol = table.getColumnModel().getColumn(1);
		TableColumn enemyCol = table.getColumnModel().getColumn(2);
		JCheckBox chkStart = new JCheckBox();
		JCheckBox chkEnemy = new JCheckBox();

		startCol.setCellEditor(new DefaultCellEditor(chkStart));
		enemyCol.setCellEditor(new DefaultCellEditor(chkEnemy));

		startCol.setCellRenderer(new BooleanTableCellRenderer());
		enemyCol.setCellRenderer(new BooleanTableCellRenderer());


		scroller = new JScrollPane(table);
		this.setJMenuBar(jMenuBar1);
		jMenu1.setText("Start");
		jMenuItem1.setText("New Game");
		jMenuItem1.addActionListener(new
				LevelSkipFrame_jMenuItem1_actionAdapter(this));

		this.getContentPane().add(scroller);
		jMenuBar1.add(jMenu1);
		jMenu1.add(jMenuItem1);

		setSize(525, 440);
		setTitle("Level Skip");
		src.Constants.centerFrame(this);
		setVisible(true);
	}

	public void jMenuItem1_actionPerformed(ActionEvent e) {
		GameFrame frame = new GameFrame(controller);
		controller.setGameFrame(frame);

		controller.setEnemies(tableModel.getEnemies());
		controller.setStartLocations(tableModel.getStartLocations());
		controller.skipLevels();

		this.dispose();
		plrFrame.dispose();
	}

	class LevelSkipTableModelListener implements TableModelListener {
		public void tableChanged(TableModelEvent evt) {
			if (evt.getType() == TableModelEvent.UPDATE) {
				int row = evt.getFirstRow();
				try{
					table.setRowSelectionInterval(row, row);
				} catch(Exception e){}
			}
		}
	}

	class BooleanTableCellRenderer implements TableCellRenderer{
		public Component getTableCellRendererComponent(JTable table,
				Object value,
				boolean isSelected,
				boolean hasFocus, int row,
				int column) {

			JCheckBox box = new JCheckBox();
			box.setBackground(Color.white);
			try {
				if (((Boolean)value).booleanValue()) {
					box.setSelected(true);
					return box;
				} else {
					box.setSelected(false);
					return box;
				}
			} catch (Exception e) {}
			return new JCheckBox();
		}

	}
}


class LevelSkipFrame_jMenuItem1_actionAdapter implements ActionListener {
	private LevelSkipFrame adaptee;
	LevelSkipFrame_jMenuItem1_actionAdapter(LevelSkipFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jMenuItem1_actionPerformed(e);
	}
}
