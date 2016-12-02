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

package napsack.gui;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import napsack.commands.SongQueryCommand;
import napsack.event.CommandCampaignEvent;
import napsack.event.CommandCampaignListener;
import napsack.event.CommandEvent;
import napsack.event.CommandListener;
import napsack.gui.results.song.ResultsTable;
import napsack.gui.results.song.ResultsTableModel;
import napsack.gui.results.song.ResultsTree;
import napsack.gui.tree.NapsackTreeNode;
import napsack.util.model.SongQueryResults;

public class ResultsPane extends JSplitPane implements CommandCampaignListener, CommandListener {
	private final ResultsTree resultsTree;
	private final ResultsTable resultsTable;

	public ResultsPane() {
		super(JSplitPane.HORIZONTAL_SPLIT);

		resultsTree = new ResultsTree();
		resultsTable = new ResultsTable();

		setLeftComponent(new JScrollPane(resultsTree));
		setRightComponent(new JScrollPane(resultsTable));

		resultsTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(final TreeSelectionEvent treeSelectionEvent_) {
				final TreePath path_ = treeSelectionEvent_.getNewLeadSelectionPath();
				final Object[] pathNodes_ = path_ == null ? null : path_.getPath();

				if (pathNodes_ != null && pathNodes_.length == 3) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							final SongQueryResults results_ = (SongQueryResults) ((NapsackTreeNode) pathNodes_[2]).getUserObject();

							((ResultsTableModel) resultsTable.getModel()).setResults(results_);
						}
					});
				}
			}
		});

		setOneTouchExpandable(true);
	}

	public void campaignAborted(final CommandCampaignEvent commandCampaignEvent_) {
	}

	public void campaignCompleted(final CommandCampaignEvent commandCampaignEvent_) {
	}

	public void campaignLaunched(final CommandCampaignEvent commandCampaignEvent_) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getResultsTree().reset();
				getResultsTable().reset();
			}
		});
	}

	public void commandExecuted(final CommandEvent commandEvent_) {
		final SongQueryResults songQueryResults_ = new SongQueryResults(commandEvent_.getNapsterService(), (SongQueryCommand) commandEvent_.getSource());

		if (songQueryResults_.getSongs().length != 0) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					getResultsTree().addResults(songQueryResults_);
				}
			});
		}
	}

	public void commandExecuting(final CommandEvent commandEvent_) {
	}

	public void commandFailed(final CommandEvent commandEvent_) {
	}

	public ResultsTree getResultsTree() {
		return resultsTree;
	}

	public ResultsTable getResultsTable() {
		return resultsTable;
	}
}

