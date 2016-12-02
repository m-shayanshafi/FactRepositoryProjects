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

import java.awt.Dimension;

import javax.swing.JComponent;

public class GuiUtils {
	public static int getMaxHeight(final JComponent[] components_) {
		int maxHeight_ = Integer.MIN_VALUE;
		int preferredHeight_ = 0;

		for (int i = 0; i < components_.length; ++i) {
			if ((preferredHeight_ = components_[i].getPreferredSize().height) > maxHeight_) {
				maxHeight_ = preferredHeight_;
			}
		}

		return maxHeight_;
	}
		
	public static int getMaxWidth(final JComponent[] components_) {
		int maxWidth_ = Integer.MIN_VALUE;
		int preferredWidth_ = 0;

		if (components_.length > 0) {
			for (int i = 0; i < components_.length; ++i) {
				if ((preferredWidth_ = components_[i].getPreferredSize().width) > maxWidth_) {
					maxWidth_ = preferredWidth_;
				}
			}
		}

		return maxWidth_;
	}
		
	public static void printSize(final JComponent component_) {
		printSize(component_.getName(), component_);
	}

	public static void printSize(final String name_, final JComponent component_) {
		final Dimension minSize_ = component_.getMinimumSize();
		final Dimension prefSize_ = component_.getPreferredSize();
		final Dimension maxSize_ = component_.getMaximumSize();

		System.out.println(name_ + ":  (" + minSize_.width + ", " + minSize_.height + ") (" + prefSize_.width + ", " + prefSize_.height + ") (" + maxSize_.width + ", " + maxSize_.height + ").");
	}

	public static void setAllHeights(final JComponent component_, final int height_) {
		component_.setMaximumSize(new Dimension(component_.getMaximumSize().width, height_));
		component_.setMinimumSize(new Dimension(component_.getMinimumSize().width, height_));
		component_.setPreferredSize(new Dimension(component_.getPreferredSize().width, height_));
	}

	public static void setAllSizes(final JComponent componentA_, final JComponent componentB_) {
		componentA_.setMaximumSize(componentB_.getMaximumSize());
		componentA_.setMinimumSize(componentB_.getMinimumSize());
		componentA_.setPreferredSize(componentB_.getPreferredSize());
	}

	public static void setAllSizes(final JComponent component_, final Dimension size_) {
		component_.setMaximumSize(size_);
		component_.setMinimumSize(size_);
		component_.setPreferredSize(size_);
	}

	public static void setAllSizes(final JComponent component_, final int width_, final int height_) {
		setAllSizes(component_, new Dimension(width_, height_));
	}

	public static void setAllWidths(final JComponent componentA_, final JComponent componentB_) {
		componentA_.setMaximumSize(new Dimension(componentB_.getMaximumSize().width, componentA_.getMaximumSize().height));
		componentA_.setMinimumSize(new Dimension(componentB_.getMinimumSize().width, componentA_.getMinimumSize().height));
		componentA_.setPreferredSize(new Dimension(componentB_.getPreferredSize().width, componentA_.getPreferredSize().height));
	}

	public static void setAllWidths(final JComponent component_, final int width_) {
		component_.setMaximumSize(new Dimension(width_, component_.getMaximumSize().height));
		component_.setMinimumSize(new Dimension(width_, component_.getMinimumSize().height));
		component_.setPreferredSize(new Dimension(width_, component_.getPreferredSize().height));
	}

	public static void setAllWidthsToMax(final JComponent[] components_) {
		final int maxWidth_ = getMaxWidth(components_);

		for (int i = 0; i < components_.length; ++i) {
			setAllWidths(components_[i], maxWidth_);
		}
	}	

	public static void setMaxHeight(final JComponent component_, final int height_) {
		component_.setMaximumSize(new Dimension(component_.getMaximumSize().width, height_));
	}

	public static void setMaxSize(final JComponent component_, final Dimension dimension_) {
		component_.setMaximumSize(dimension_);
	}

	public static void setMaxSize(final JComponent component_, final int width_, final int height_) {
		setMaxSize(component_, new Dimension(width_, height_));
	}

	public static void setMaxWidth(final JComponent component_, final int width_) {
		component_.setMaximumSize(new Dimension(width_, component_.getMaximumSize().height));
	}

	public static void setMaxWidth(final JComponent componentA_, final JComponent componentB_) {
		componentA_.setMaximumSize(new Dimension(componentB_.getMaximumSize().width, componentA_.getMaximumSize().height));
	}

	public static void setMinHeight(final JComponent component_, final int height_) {
		setMinSize(component_, component_.getMinimumSize().width, height_);
	}

	public static void setMinHeight(final JComponent componentA_, final JComponent componentB_) {
		setMinSize(componentA_, componentA_.getMinimumSize().width, componentB_.getMinimumSize().height);
	}

	public static void setMinSize(final JComponent component_, final int width_, final int height_) {
		component_.setMinimumSize(new Dimension(width_, height_));
	}

	public static void setMinWidth(final JComponent componentA_, final JComponent componentB_) {
		componentA_.setMinimumSize(new Dimension(componentB_.getMinimumSize().width, componentA_.getMinimumSize().height));
	}

	public static void setMinWidth(final JComponent componentA_, final int width_) {
		componentA_.setMinimumSize(new Dimension(width_, componentA_.getMinimumSize().height));
	}

	public static void setPreferredSize(final JComponent component_, final int width_, final int height_) {
		component_.setPreferredSize(new Dimension(width_, height_));
	}

	public static void setPreferredHeight(final JComponent component_, final int height_) {
		setPreferredSize(component_, component_.getPreferredSize().width, height_);
	}

	public static void setPreferredHeight(final JComponent componentA_, final JComponent componentB_) {
		setPreferredSize(componentA_, componentA_.getPreferredSize().width, componentB_.getPreferredSize().height);
	}

	public static void setPreferredWidth(final JComponent componentA_, final JComponent componentB_) {
		componentA_.setPreferredSize(new Dimension(componentB_.getPreferredSize().width, componentA_.getPreferredSize().height));
	}
}

