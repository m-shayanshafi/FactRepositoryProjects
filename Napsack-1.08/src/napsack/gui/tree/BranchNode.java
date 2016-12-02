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

package napsack.gui.tree;

import javax.swing.tree.MutableTreeNode;

public class BranchNode extends NapsackTreeNode {
	public BranchNode() {
	}

	public BranchNode(final Object userObject_) {
		super(userObject_);
	}

	public boolean getAllowsChildren() {
		return true;
	}

	public void insert(final MutableTreeNode child_, int index_) {
		getChildren().add(index_, child_);

		child_.setParent(this);
	}

	public boolean isLeaf() {
		return false;
	}

	public void remove(final int index_) {
		final MutableTreeNode child_ = (MutableTreeNode) getChildren().remove(index_);

		child_.setParent(null);
	}

	public void remove(final MutableTreeNode child_) {
		getChildren().remove(child_);

		child_.setParent(null);
	}
}

