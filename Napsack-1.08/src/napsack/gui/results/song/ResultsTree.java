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

package napsack.gui.results.song;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;

import napsack.gui.tree.BranchNode;
import napsack.gui.tree.NapsackTreeModel;
import napsack.gui.tree.TreeOrganizer;
import napsack.util.model.SongQueryResults;
import napsack.util.properties.Property;
import napsack.util.properties.FlipResultsTreeProperty;

public class ResultsTree extends JTree {
	private final static String ROOT_NODE_TEXT = "Search Results";

	private static TreeModel createEmptyTreeModel() {
		return new NapsackTreeModel(new BranchNode(ROOT_NODE_TEXT), createTreeOrganizer());
	}

	private static TreeModel createPopulatedTreeModel(final NapsackTreeModel napsackTreeModel_) {
		return new NapsackTreeModel(new BranchNode(ROOT_NODE_TEXT), createTreeOrganizer(), napsackTreeModel_);
	}

	private static TreeOrganizer createTreeOrganizer() {
		TreeOrganizer treeOrganizer_ = null;

      if (((Boolean) FlipResultsTreeProperty.getInstance().getValue()).booleanValue()) {
         treeOrganizer_ = new FlippedResultsOrganizer();
      } else {
         treeOrganizer_ = new DefaultResultsOrganizer();
      }

		return treeOrganizer_;
	}

	public ResultsTree() {
		super(createEmptyTreeModel());

		final Property flipResultsTreeProperty_ = FlipResultsTreeProperty.getInstance();

		flipResultsTreeProperty_.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent propertyChangeEvent_) {
				setModel(createPopulatedTreeModel((NapsackTreeModel) getModel()));
			}
		});

		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		setRootVisible(false);
		setShowsRootHandles(true);
		setScrollsOnExpand(true);
	}

   public void addResults(final SongQueryResults songQueryResults_) {
		((NapsackTreeModel) getModel()).addUserObject(songQueryResults_);
   }

	public void reset() {
		setModel(createEmptyTreeModel());
	}
}

