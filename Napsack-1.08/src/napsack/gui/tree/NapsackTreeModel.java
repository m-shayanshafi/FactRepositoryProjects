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

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class NapsackTreeModel extends DefaultTreeModel {
	private final TreeOrganizer treeOrganizer; 
	private List userObjects;

	public NapsackTreeModel(final TreeNode root_, final TreeOrganizer treeOrganizer_) {
		this(root_, treeOrganizer_, (List) null);
	}

	public NapsackTreeModel(final TreeNode root_, final TreeOrganizer treeOrganizer_, final List userObjects_) {
		super(root_);

		treeOrganizer = treeOrganizer_;
		setUserObjects(userObjects_);

		if (userObjects_ != null ) {
			final int size_ = userObjects_.size();

			for (int i = 0; i < size_; ++i) {
				getTreeOrganizer().addUserObject(this, new TreePath(getRoot()), userObjects_.get(i));
			}
		}
	}

	public NapsackTreeModel(final TreeNode root_, final TreeOrganizer treeOrganizer_, final NapsackTreeModel napsackTreeModel_) {
		this(root_, treeOrganizer_, napsackTreeModel_.getUserObjects());
	}

	public void addUserObject(final Object userObject_) {
		getUserObjects().add(userObject_);
		getTreeOrganizer().addUserObject(this, new TreePath(getRoot()), userObject_);
	}

	private TreeOrganizer getTreeOrganizer() {
		return treeOrganizer;
	}

	private List getUserObjects() {
		if (userObjects == null) {
			setUserObjects(new ArrayList());
		}

		return userObjects;
	}

	public void removeUserObject(final Object userObject_) {
		getUserObjects().remove(userObject_);
		getTreeOrganizer().removeUserObject(this, new TreePath(getRoot()), userObject_);
	}

	private void setUserObjects(final List userObjects_) {
		userObjects = userObjects_;
	}
}

