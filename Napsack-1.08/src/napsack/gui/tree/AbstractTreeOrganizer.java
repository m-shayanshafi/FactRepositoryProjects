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

import java.util.Collections;
import java.util.Comparator;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public abstract class AbstractTreeOrganizer implements TreeOrganizer {
	protected final static Comparator STRING_COMPARATOR = new Comparator() {
		public int compare(final Object object1_, final Object object2_) {
			return object1_.toString().compareTo(object2_.toString());
		}
	};

	private TreeOrganizer treeOrganizer;

	protected AbstractTreeOrganizer() {
	}

	protected AbstractTreeOrganizer(final TreeOrganizer treeOrganizer_) {
		setTreeOrganizer(treeOrganizer_);
	}

	public void addUserObject(final TreeModel treeModel_, final TreePath treePath_, final Object userObject_) {
		final NapsackTreeNode parentNode_ = getParent(treePath_);
		NapsackTreeNode childNode_ = createChildNode(treeModel_, treePath_, userObject_);
		final int childNodeIndex_ = indexOfChild(parentNode_, childNode_);

		if (childNodeIndex_ < 0) {
			childNode_ = childNotFound(treeModel_, parentNode_, childNode_, childNodeIndex_);
		} else {
			childNode_ = childFound(treeModel_, parentNode_, childNode_, childNodeIndex_);
		}

		final TreeOrganizer treeOrganizer_ = getTreeOrganizer();

		if (treeOrganizer_ != null) {
			treeOrganizer_.addUserObject(treeModel_, treePath_.pathByAddingChild(childNode_), userObject_);
		}
	}

	protected NapsackTreeNode childFound(final TreeModel treeModel_, final NapsackTreeNode parentNode_, final NapsackTreeNode childNode_, int foundIndex_) {
		return (NapsackTreeNode) parentNode_.getChildAt(foundIndex_);
	}

	protected NapsackTreeNode  childNotFound(final TreeModel treeModel_, final NapsackTreeNode parentNode_, final NapsackTreeNode childNode_, final int wouldBeFoundIndex_) {
		((DefaultTreeModel) treeModel_).insertNodeInto(childNode_, parentNode_, -1 * wouldBeFoundIndex_ - 1);

		return childNode_;
	}

	protected abstract NapsackTreeNode createChildNode(TreeModel treeModel_, TreePath treePath_, Object userObject_);

	protected NapsackTreeNode getParent(final TreePath treePath_) {
		return (NapsackTreeNode) treePath_.getPathComponent(treePath_.getPathCount() - 1);
	}

	public TreeOrganizer getTreeOrganizer() {
		return treeOrganizer;
	}

	protected int indexOfChild(final NapsackTreeNode parentNode_, final NapsackTreeNode childNode_) {
		return Collections.binarySearch(parentNode_.getChildren(), childNode_, STRING_COMPARATOR);
	}

	public void removeUserObject(final TreeModel treeModel_, final TreePath treePath_, final Object userObject_) {
	}

	public void setTreeOrganizer(final TreeOrganizer treeOrganizer_) {
		treeOrganizer = treeOrganizer_;
	}
}

