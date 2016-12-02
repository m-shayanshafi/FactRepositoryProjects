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

package napsack.gui.properties;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import napsack.event.ExceptionEvent;
import napsack.event.NapigatorEvent;
import napsack.event.NapigatorListener;
import napsack.gui.CommandButtons;
import napsack.gui.DoubleList;
import napsack.gui.MainFrame;
import napsack.gui.MenuBar;
import napsack.gui.RefreshNapigatorListener;
import napsack.gui.VerticalCommandButtons;
import napsack.gui.ViewMenu;
import napsack.gui.properties.PropertiesPanel;
import napsack.servers.Napigator;
import napsack.util.StringUtils;
import napsack.util.properties.DelimiterProperty;
import napsack.util.properties.ExcludedNetworksProperty;
import napsack.util.properties.PropertyException;

public class ExcludedNetworksPanel extends PropertiesPanel implements NapigatorListener {
	private final static String EXCLUDED_NETWORKS_LABEL = "Excluded Networks:";
	public final static String ADD_BUTTON = "Add";
	public final static String REMOVE_BUTTON = "Remove";
	public final static String REFRESH_BUTTON = "Refresh";

	private final static char EXCLUDED_NETWORKS_MNEMONIC = 'x';
	private final static char ADD_MNEMONIC = 'A';
	private final static char REMOVE_MNEMONIC = 'R';
	private final static char REFRESH_MNEMONIC = 's';

	private final MainFrame mainFrame;
	private final DoubleList excludedNetworksLists;
	private final RefreshNapigatorListener refreshNapigatorListener;

	public ExcludedNetworksPanel(final MainFrame mainFrame_) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		final JLabel excludedNetworksLabel_ = new JLabel(EXCLUDED_NETWORKS_LABEL);
		final CommandButtons commandButtons_ = new VerticalCommandButtons(new String[] {ADD_BUTTON, REMOVE_BUTTON, REFRESH_BUTTON});
		mainFrame = mainFrame_;
		excludedNetworksLists = new DoubleList(commandButtons_);
		refreshNapigatorListener = new RefreshNapigatorListener(mainFrame_);

		final JButton addButton_ = commandButtons_.getButton(ADD_BUTTON);
		final JButton removeButton_ = commandButtons_.getButton(REMOVE_BUTTON);
		final JButton refreshButton_ = commandButtons_.getButton(REFRESH_BUTTON);
		final JList excludedNetworksList_ = excludedNetworksLists.getLeftList();
		final JList allNetworksList_ = excludedNetworksLists.getRightList();
		final DefaultListModel excludedNetworksListModel_ = (DefaultListModel) excludedNetworksList_.getModel();
		final Napigator napigator_ = Napigator.getInstance();

		excludedNetworksLabel_.setAlignmentX(Component.LEFT_ALIGNMENT);
		excludedNetworksLists.setAlignmentX(Component.LEFT_ALIGNMENT);

		add(excludedNetworksLabel_);
		add(Box.createVerticalStrut(12));
		add(excludedNetworksLists);

		excludedNetworksLabel_.setLabelFor(excludedNetworksList_);
		excludedNetworksLabel_.setDisplayedMnemonic(EXCLUDED_NETWORKS_MNEMONIC);

		addButton_.setEnabled(false);
		addButton_.setMnemonic(ADD_MNEMONIC);
		removeButton_.setEnabled(false);
		removeButton_.setMnemonic(REMOVE_MNEMONIC);
		refreshButton_.setMnemonic(REFRESH_MNEMONIC);

		excludedNetworksList_.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(final ListSelectionEvent listSelectionEvent_) {
				if (excludedNetworksList_.getSelectedIndices().length > 0) {
					removeButton_.setEnabled(true);
				} else {
					removeButton_.setEnabled(false);
				}
			}
		});
		addButton_.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent actionEvent_) {
				final Object[] selectedNetworks_ = allNetworksList_.getSelectedValues();
				Object[] currentNetworks_ = excludedNetworksListModel_.toArray();
				int insert_ = 0;

				for (int i = 0; i < selectedNetworks_.length; ++i) {
					if ((insert_ = Arrays.binarySearch(currentNetworks_, selectedNetworks_[i])) < 0) {
						excludedNetworksListModel_.add((insert_ + 1) * -1, selectedNetworks_[i]);
						currentNetworks_ = excludedNetworksListModel_.toArray();
					}
				}
			}
		});
		removeButton_.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent actionEvent_) {
				final Object[] selectedServices_ = excludedNetworksList_.getSelectedValues();
				excludedNetworksList_.clearSelection();

				for (int i = 0; i < selectedServices_.length; ++i) {
					excludedNetworksListModel_.removeElement(selectedServices_[i]);
				}
			}
		});
		refreshButton_.addActionListener(refreshNapigatorListener);

		allNetworksList_.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(final ListSelectionEvent listSelectionEvent_) {
				if (allNetworksList_.getSelectedIndices().length > 0) {
					addButton_.setEnabled(true);
				} else {
					addButton_.setEnabled(false);
				}
			}
		});

		napigator_.addNapigatorListener(this);
		refreshButton_.setEnabled(!napigator_.isRefreshing());

		populateFields();
	}

	private String createExcludedString() {
		final Object[] excludedNetworks_ = ((DefaultListModel) getExcludedNetworksLists().getLeftList().getModel()).toArray();
		String excludedNetworksString_ = null;

		if (excludedNetworks_.length > 0) {
			excludedNetworksString_ = StringUtils.join(excludedNetworks_, ((String) DelimiterProperty.getInstance().getValue()).substring(0, 1));
		}

		return excludedNetworksString_;
	}

	public DoubleList getExcludedNetworksLists() {
		return excludedNetworksLists;
	}

	private MainFrame getMainFrame() {
		return mainFrame;
	}

	public RefreshNapigatorListener getRefreshNapigatorListener() {
		return refreshNapigatorListener;
	}

	public void napigatorRefreshed(final NapigatorEvent napigatorEvent_) {
		final Thread populateAllNetworksList_ = populateAllNetworksList();
		boolean joined_ = false;

		while (!joined_) {
			try {
				populateAllNetworksList_.join();
				joined_ = true;
			} catch (InterruptedException interruptedException_) {
			}
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				((CommandButtons) getExcludedNetworksLists().getCenterComponent()).getButton(REFRESH_BUTTON).setEnabled(true);
			}
		});
	}

	public void napigatorRefreshing(final NapigatorEvent napigatorEvent_) {
      final JButton refreshButton_ = ((CommandButtons) getExcludedNetworksLists().getCenterComponent()).getButton(ExcludedNetworksPanel.REFRESH_BUTTON);

      final Runnable disableButton_ = new Runnable() {
         public void run() {
            refreshButton_.setEnabled(false);
         }
      };

      if (SwingUtilities.isEventDispatchThread()) {
         disableButton_.run();
      } else {
         boolean invoked_ = false;

         while (!invoked_) {
            try {
               SwingUtilities.invokeAndWait(disableButton_);
               invoked_ = true;
            } catch (InterruptedException interruptedException_) {
            } catch (InvocationTargetException invocationTargetException_) {
               throw new RuntimeException("InvocationTargetException");
            }
         }
      }
	}

	private Thread populateAllNetworksList() {
		final Thread populateThread_ = new Thread(new Runnable() {
			public void run() {
				try {
      			final DefaultListModel allNetworksListModel_ = (DefaultListModel) getExcludedNetworksLists().getRightList().getModel();
					final String[] networkNames_ = Napigator.getInstance().getNetworkNames();
					Arrays.sort(networkNames_);

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							allNetworksListModel_.clear();

							for (int i = 0; i < networkNames_.length; ++i) {
							allNetworksListModel_.addElement(networkNames_[i]);
							}
						}
					});
				} catch (IOException ioException_) {
					getMainFrame().showErrorMessage(ioException_.getMessage());
				}
			}
		});

		populateThread_.start();

		return populateThread_;
	}

	private void populateFields() {
		final String[] excludedNetworks_ = (String[]) ExcludedNetworksProperty.getInstance().getValue();
		final DefaultListModel excludedNetworksListModel_ = ((DefaultListModel) getExcludedNetworksLists().getLeftList().getModel());

		Arrays.sort(excludedNetworks_);

		for (int i = 0; i < excludedNetworks_.length; ++i) {
			excludedNetworksListModel_.addElement(excludedNetworks_[i]);
		}

		populateAllNetworksList();
	}

	public void setProperties() {
		ExcludedNetworksProperty.getInstance().setProperty(createExcludedString());
	}

	public void validateProperties() throws PropertyException {
		try {
			ExcludedNetworksProperty.getInstance().validate(createExcludedString());
			setInvalidComponent(null);
		} catch (PropertyException propertyException_) {
			setInvalidComponent(getExcludedNetworksLists().getLeftList());
			throw propertyException_;
		}
	}
}

