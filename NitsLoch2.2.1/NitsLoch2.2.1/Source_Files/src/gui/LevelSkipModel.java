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

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class LevelSkipModel extends AbstractTableModel{
	private static final long serialVersionUID = src.Constants.serialVersionUID;

	public LevelSkipModel() {
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private String[] colNames;
	private ArrayList<String> cityNames;
	private ArrayList<Boolean> startLocation;
	private ArrayList<Boolean> enemies;
	private Controller controller;

	private final int CITY_NAME = 0;
	private final int START_LOCATION = 1;
	private final int ENEMIES = 2;

	public LevelSkipModel(String[] colNames, Controller controller) {
		this.colNames = colNames;
		this.controller = controller;
		loadTable();
	}

	public void loadTable(){
		startLocation = new ArrayList<Boolean>();
		enemies = new ArrayList<Boolean>();
		cityNames = new ArrayList<String>();
		for (int i = 0; i < controller.getNumCities(); i++) {
			startLocation.add(false);
			enemies.add(true);
			cityNames.add(controller.getCityName(i));
		}
		startLocation.set(0, true);
	}

	public String getColumnName(int col){
		return colNames[col];
	}

	public boolean isCellEditable(int row, int column) {
		try{
			if (column == CITY_NAME)
				return false;
			return true;
		} catch(Exception e){
			return false;
		}
	}

	public Object getValueAt(int row, int column) {
		switch (column) {
		case CITY_NAME:
			try{
				return cityNames.get(row);
			}catch(Exception e) { return "No city name"; }
		case START_LOCATION:
			try{
				return startLocation.get(row);
			}catch(Exception e) { return false; }
		case ENEMIES:
			try{
				return enemies.get(row);
			}catch(Exception e) { return true; }
		default:
			return new Object();
		}
	}

	public void setValueAt(Object value, int row, int column) {
		boolean boolValue = ((Boolean)value).booleanValue();
		try{
			switch (column) {
			case START_LOCATION:
				if (boolValue) {
					for(int i = 0; i < controller.getNumCities(); i++){
						startLocation.set(i, false);
						fireTableCellUpdated(i, 1);
					}
					startLocation.set(row, true);
				}
				else {
					for(int i = 0; i < controller.getNumCities(); i++){
						startLocation.set(i, false);
					}
					startLocation.set(0, true);
					fireTableCellUpdated(0, 1);
				}
				controller.setStartLocations(startLocation);
				break;
			case ENEMIES:
				enemies.set(row, boolValue);
				controller.setEnemies(enemies);
				break;
			default:
				break;
			}
		}catch(Exception e) {System.out.println(e);}
		fireTableCellUpdated(row, column);
	}

	public int getRowCount() {
		return cityNames.size();
	}

	public int getColumnCount(){
		return colNames.length;
	}

	public ArrayList<Boolean> getStartLocations(){
		return startLocation;
	}

	public ArrayList<Boolean> getEnemies(){
		return enemies;
	}

	private void jbInit() throws Exception {
	}

}
