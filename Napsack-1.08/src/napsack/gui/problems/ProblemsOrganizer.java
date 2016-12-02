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

package napsack.gui.problems;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import napsack.gui.tree.AbstractTreeOrganizer;
import napsack.gui.tree.BranchNode;
import napsack.gui.tree.LeafNode;
import napsack.gui.tree.NapsackTreeNode;
import napsack.gui.tree.TreeOrganizer;
import napsack.util.model.Error;

public class ProblemsOrganizer extends AbstractTreeOrganizer {
	private static TreeOrganizer createOrganizerChain() {
		return new AbstractTreeOrganizer() {
			public void addUserObject(final TreeModel treeModel_, final TreePath treePath_, final Object userObject_) {
				final NapsackTreeNode parentNode_ = getParent(treePath_);
				final NapsackTreeNode childNode_ = createChildNode(treeModel_, treePath_, userObject_);

				((DefaultTreeModel) treeModel_).insertNodeInto(childNode_, parentNode_, parentNode_.getChildCount());
			}

			protected NapsackTreeNode createChildNode(TreeModel treeModel_, TreePath treePath_, Object userObject_) {
				return new LeafNode(((Error) userObject_).getErrorMessage());
			}
		};
	}

	protected ProblemsOrganizer() {
		super(createOrganizerChain());
	}

	protected NapsackTreeNode createChildNode(TreeModel treeModel_, TreePath treePath_, Object userObject_) {
		return new BranchNode(((Error) userObject_).getNapsterServiceString());
	}
}

