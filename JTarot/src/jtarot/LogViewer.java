/*
 * LogViewer.java
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 1999, 2003 Slava Pestov
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

//package org.gjt.sp.jedit.gui;
package jtarot;

//{{{ Imports
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Toolkit;
import java.io.*;
//}}}

public class LogViewer extends JPanel
{
	//{{{ LogViewer constructor
	public LogViewer()
	{
		super(new BorderLayout());

		JPanel caption = new JPanel();
		caption.setLayout(new BoxLayout(caption,BoxLayout.X_AXIS));
		caption.setBorder(new EmptyBorder(6,6,6,6));
		JLabel label = new JLabel(jTarot.menus.translate( "reportbugLabel" ) );
		caption.add(label);

		caption.add(Box.createHorizontalGlue());

		tailIsOn = false;
		tail = new JCheckBox("Show Tail",tailIsOn);
		tail.addActionListener(new ActionHandler());
		caption.add(tail);

		caption.add(Box.createHorizontalStrut(12));

		copy = new JButton("Copy");
		copy.addActionListener(new ActionHandler());
		caption.add(copy);

		ListModel model = Log.getLogListModel();
		model.addListDataListener(new ListHandler());
		list = new LogList(model);
		list.setVisibleRowCount(24);
//		list.setFont(jEdit.getFontProperty("view.font"));

		add(BorderLayout.NORTH,caption);
		JScrollPane scroller = new JScrollPane(list);
		Dimension dim = scroller.getPreferredSize();
		dim.width = Math.min(600,dim.width);
		scroller.setPreferredSize(dim);
		add(BorderLayout.CENTER,scroller);
	} //}}}

	//{{{ addNotify() method
	public void addNotify()
	{
		super.addNotify();
		if(tailIsOn)
		{
			int index = list.getModel().getSize() - 1;
			list.ensureIndexIsVisible(index);
		}
	} //}}}

	//{{{ Private members
	private JList list;
	private JButton copy;
	private JCheckBox tail;
	private boolean tailIsOn;
	//}}}

	//{{{ ActionHandler class
	class ActionHandler implements ActionListener, ClipboardOwner
	{
		/**
		* Empty implementation of the ClipboardOwner interface.
		*/
		public void lostOwnership( Clipboard aClipboard, Transferable aContents) {
		//do nothing
		}
		public void actionPerformed(ActionEvent e)
		{
			Object src = e.getSource();
			if(src == tail)
			{
				tailIsOn = !tailIsOn;
//				jEdit.setBooleanProperty("log-viewer.tail",tailIsOn);
				if(tailIsOn)
				{
					int index = list.getModel().getSize();
					if(index != 0)
					{
						list.ensureIndexIsVisible(index - 1);
					}
				}
			}
			else if(src == copy)
			{
				Object[] selected = list.getSelectedValues();
				StringBuffer buf = new StringBuffer();
				for(int i = 0; i < selected.length; i++)
				{
					buf.append(selected[i]);
					buf.append('\n');
				}
				StringSelection stringSelection = new StringSelection( buf.toString() );
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents( stringSelection, this );
			}
		}
	} //}}}

	//{{{ ListHandler class
	class ListHandler implements ListDataListener
	{
		public void intervalAdded(ListDataEvent e)
		{
			contentsChanged(e);
		}

		public void intervalRemoved(ListDataEvent e)
		{
			contentsChanged(e);
		}

		public void contentsChanged(ListDataEvent e)
		{
			if(tailIsOn)
			{
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						int index = list.getModel().getSize() - 1;
						list.ensureIndexIsVisible(index);
					}
				});
			}
		}
	} //}}}

	//{{{ LogList class
	class LogList extends JList
	{
		LogList(ListModel model)
		{
			super(model);
		}

		public void processMouseMotionEvent(MouseEvent evt)
		{
			if(evt.getID() == MouseEvent.MOUSE_DRAGGED)
			{
				int row = list.locationToIndex(evt.getPoint());
				if(row != -1)
				{
					list.addSelectionInterval(row,row);
					evt.consume();
				}
			}
			else
				super.processMouseMotionEvent(evt);
		}
	} //}}}
}
