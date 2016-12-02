// Written by Chris Redekop <waveform@users.sourceforge.net>
// Released under the conditions of the GPL (See below).
//
// Thanks to Travis Whitton for the Gnap Fetch code used by Napsack.
//
// Also thanks to Radovan Garabik for the pynap code used by Napsack.
//
// Napsack - a specialized client for launching cross-server Napster queries.
// Copyright (C) 2000-2002 Chris Redekop
// 
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2
// of the License, or (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA

package napsack.clients;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import napsack.gui.LoginDialog;
import napsack.gui.MainFrame;
import napsack.gui.MenuBar;
import napsack.gui.ViewMenu;
import napsack.gui.results.song.ResultsTableModel;
import napsack.util.SongSorter;
import napsack.util.model.SongField;
import napsack.util.properties.EmptyNickPropertyException;
import napsack.util.properties.EmptyPasswordPropertyException;
import napsack.util.properties.EmptyQueriesPropertyException;
import napsack.util.properties.NapsackProperties;
import napsack.util.properties.PropertyException;
import napsack.util.properties.SongSorterProperty;
import napsack.util.properties.TableColumnsProperty;

public class GuiClient extends Client {
	private final static String EXCEPTION_TITLE = "Error";

   private static void validateProperties() throws PropertyException, IOException {
		try {
			NapsackProperties.getInstance().validate();
		} catch (EmptyNickPropertyException emptyNickPropertyException_) {
		} catch (EmptyPasswordPropertyException emptyPasswordPropertyException_) {
		} catch (EmptyQueriesPropertyException emptyQueryPropertyException_) {
		}
   }

	private MainFrame mainFrame;

	protected GuiClient() {
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public void initialize(final String[] args_) throws PropertyException, IOException {
      parseQueries(args_);

      validateProperties();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
            mainFrame = new MainFrame();

				final ResultsTableModel resultsTableModel_ = (ResultsTableModel) getMainFrame().getResultsPane().getResultsTable().getModel();

				resultsTableModel_.setSongSorter((SongSorter) SongSorterProperty.getInstance().getValue());
				resultsTableModel_.setColumns((SongField[]) TableColumnsProperty.getInstance().getValue());
				mainFrame.setVisible(true);

				try {
					NapsackProperties.getInstance().validate();
				} catch (EmptyNickPropertyException emptyNickPropertyException_) {
					new LoginDialog(mainFrame).setVisible(true);
				} catch (EmptyPasswordPropertyException emptyPasswordPropertyException_) {
					new LoginDialog(mainFrame).setVisible(true);
				} catch (EmptyQueriesPropertyException emptyQueriesPropertyException_) {
				} catch (PropertyException propertyException_) {
				}
			}
		});
	}
}
