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
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public abstract class NapsackTreeNode implements MutableTreeNode {
	private MutableTreeNode parent;
   private List children;
	private Object userObject;

	public NapsackTreeNode() {
	}

	public NapsackTreeNode(final Object userObject_) {
		setUserObject(userObject_);
	}

   public Enumeration children() {
      return Collections.enumeration(getChildren());
   }

   public TreeNode getChildAt(final int index_) {
      return (TreeNode) getChildren().get(index_);
   }

   public int getChildCount() {
      return getChildren().size();
   }

   public List getChildren() {
      if (children == null) {
         children = new ArrayList();
      }

      return children;
   }

   public int getIndex(final TreeNode child_) {
      return getChildren().indexOf(child_);
   }

	public MutableTreeNode getMutableParent() {
		return parent;
	}

	public TreeNode getParent() {
		return parent;
	}

	public Object getUserObject() {
		return userObject;
	}

   public boolean isLeaf() {
      return getChildCount() <= 0;
   }

   public void removeFromParent() {
      getMutableParent().remove(this);
   }

   protected void setChildren(final List children_) {
      children = children_;
   }

	public void setParent(final MutableTreeNode parent_) {
		parent = parent_;
	}

	public void setUserObject(final Object userObject_) {
		userObject = userObject_;
	}

	public String toString() {
		final Object userObject_ = getUserObject();

		return userObject_ == null ? "" : userObject_.toString();
	}
}

