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

public class LeafNode extends NapsackTreeNode {
	public LeafNode() {
	}

	public LeafNode(final Object userObject_) {
		super(userObject_);
	}

	public boolean getAllowsChildren() {
		return false;
	}

	public boolean isLeaf() {
		return true;
	}

	public void insert(final MutableTreeNode child_, int index_) {
		throw new IllegalArgumentException("Nodes may not be added to a LeafNode.");
	}

	public void remove(final int index_) {
		throw new IllegalArgumentException("Nodes may not be removed from a LeafNode.");
	}

	public void remove(final MutableTreeNode child_) {
		throw new IllegalArgumentException("Nodes may not be removed from a LeafNode.");
	}
}

