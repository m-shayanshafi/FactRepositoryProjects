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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import napsack.Napsack;
import napsack.commands.CommandCampaign;
import napsack.commands.NapsterCommand;
import napsack.commands.SongSearchCommand;
import napsack.event.CommandCampaignAdapter;
import napsack.event.CommandCampaignEvent;
import napsack.event.CommandCampaignListener;
import napsack.event.ExceptionEvent;
import napsack.event.ExceptionListener;
import napsack.util.properties.ErrorMessagesProperty;
import napsack.util.properties.NapsackProperties;
import napsack.util.properties.PropertyException;
import napsack.util.properties.SaveOnExitProperty;

public class MainFrame extends JFrame implements ExceptionListener {
	private final static String ERROR_TITLE = "Error";
	private final static String WARNING_TITLE = "Error";

	private final MenuBar menuBar;
	private final SearchPanel searchPanel;
	private final ResultsPane resultsPane;
	private final StatusBar statusBar;
	private final ProgressBar progressBar;
	private final Thread shutdownHook;
	private final MainFrameListener mainFrameListener;
	private final Runnable resetButtons;
	private final CommandCampaignListener buttonResetter;
	private CommandCampaign songSearchCampaign;

	public MainFrame() {
		super(Napsack.APPLICATION_IDENTIFIER);

		menuBar = new MenuBar(this);
		searchPanel = new SearchPanel(this);
		resultsPane = new ResultsPane();
		statusBar = new StatusBar();
		progressBar = new ProgressBar();
		shutdownHook = new Thread() {
			public void run() {
      		if (((Boolean) SaveOnExitProperty.getInstance().getValue()).booleanValue()) {
         		try {
            		searchPanel.validateProperties();
            		searchPanel.setProperties();
               	NapsackProperties.getInstance().storeProperties();
         		} catch (PropertyException propertyException_) {
         		} catch (IOException ioException_) {
					}
      		}
			}
		};
		mainFrameListener = new MainFrameListener(this);
		final JPanel bottomBar_ = new JPanel();
		final JSplitPane splitPane_ = new JSplitPane(JSplitPane.VERTICAL_SPLIT, searchPanel, resultsPane);
		final Container contentPane_ = getContentPane();
		final CommandButtons commandButtons_ = searchPanel.getCommandButtons();
		final JButton searchButton_ = commandButtons_.getButton(SearchPanel.SEARCH_BUTTON);
		final JButton cancelButton_ = commandButtons_.getButton(SearchPanel.STOP_BUTTON);

		resetButtons = new Runnable() {
			public void run() {
				cancelButton_.setEnabled(false);
  				searchButton_.setEnabled(true);
			}
		};
		buttonResetter = new CommandCampaignAdapter() {
			public void campaignCompleted(final CommandCampaignEvent commandCampaignEvent_) {
				SwingUtilities.invokeLater(resetButtons);
			}
		};

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().createImage(ClassLoader.getSystemResource("icon-napsack.gif")));
		setJMenuBar(menuBar);
		getRootPane().setDefaultButton(searchPanel.getCommandButtons().getButton(SearchPanel.SEARCH_BUTTON));
		addComponentListener(mainFrameListener);
		addWindowListener(mainFrameListener);

		resultsPane.setBorder(null);
		bottomBar_.setLayout(new BoxLayout(bottomBar_, BoxLayout.X_AXIS));
		bottomBar_.add(statusBar);
		bottomBar_.add(Box.createHorizontalStrut(2));
		bottomBar_.add(progressBar);
		contentPane_.add(splitPane_);
		contentPane_.add(bottomBar_, BorderLayout.SOUTH);
		splitPane_.setOneTouchExpandable(true);
		searchButton_.setEnabled(true);
		cancelButton_.setEnabled(false);

		GuiUtils.setMinHeight(resultsPane, searchPanel);
		GuiUtils.setPreferredHeight(resultsPane, searchPanel);

		// Comment out the lines between <comment> and </comment> tags for
		// Java 1.2.2.  This will prevent Napsack from (optionally) saving
		// its settings on exit unless it is shutdown through the file menu or
		// through standard window controls.

		// <comment>
		Runtime.getRuntime().addShutdownHook(shutdownHook);
		// </comment>

		searchButton_.addActionListener(new ActionListener() {
         public void actionPerformed(final ActionEvent actionEvent_) {
				boolean completed_ = false;

				try {
           		searchButton_.setEnabled(false);
					cancelButton_.setEnabled(true);

          		searchPanel.validateProperties();
           		searchPanel.setProperties();

					createStartThread().start();

					completed_ = true;
            } catch (PropertyException propertyException_) {
             		showErrorMessage(propertyException_.getMessage());
            } catch (IOException ioException_) {
             		showWarningMessage(ioException_.getMessage());
            } finally {
					if (!completed_) {
						resetButtons.run();
					}
				}
         }
      });

		cancelButton_.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent actionEvent_) {
				createAbortThread().start();
			}
		});

		pack();
	}

	public Thread createAbortThread() {
		return new Thread() {
			public void run() {
				final CommandCampaign songSearchCampaign_ = getSongSearchCampaign();
				final SongSearchCommand songSearchCommand_ = (SongSearchCommand) songSearchCampaign_.getNapsterCommand();
				final ResultsPane resultsPane_ = getResultsPane();
				final ProgressBar progressBar_ = getProgressBar();
				final StatusBar statusBar_ = getStatusBar();
				final ProblemsDialog problemsDialog_ = ((ViewMenu) ((MenuBar) getJMenuBar()).getViewMenu()).getProblemsDialog();

				songSearchCampaign_.removeCommandThreadListener(progressBar_.getCommandThreadListener());

            final NapsterCommand connectCommand_ = songSearchCommand_.getConnectCommand();
            final NapsterCommand[] songQueryCommands_ = songSearchCommand_.getSongQueryCommands();
            final NapsterCommand disconnectCommand_ = songSearchCommand_.getDisconnectCommand();

				connectCommand_.removeCommandListener(statusBar_.getConnectListener());
				connectCommand_.removeCommandListener(problemsDialog_.getConnectListener());
				for (int i = 0; i < songQueryCommands_.length; ++i) {
					songQueryCommands_[i].removeCommandListener(resultsPane_);
					songQueryCommands_[i].removeCommandListener(statusBar_.getSongQueryListener());
					songQueryCommands_[i].removeCommandListener(problemsDialog_.getSongQueryListener());
				}
				disconnectCommand_.removeCommandListener(statusBar_.getDisconnectListener());
				disconnectCommand_.removeCommandListener(problemsDialog_.getDisconnectListener());

				songSearchCampaign.fireCampaignAborted();

				songSearchCampaign_.removeCommandCampaignListener(problemsDialog_);
				songSearchCampaign_.removeCommandCampaignListener(resultsPane_);
				songSearchCampaign_.removeCommandCampaignListener(statusBar_.getCommandCampaignListener());
				songSearchCampaign_.removeCommandCampaignListener(progressBar_.getCommandCampaignListener());
				songSearchCampaign_.removeCommandCampaignListener(getButtonResetter());
				songSearchCampaign_.removeExceptionListener(MainFrame.this);

				songSearchCampaign_.abort();
				setSongSearchCampaign(null);

				SwingUtilities.invokeLater(getResetButtons());
			}
		};
	}

	public Thread createStartThread() {
		return new Thread() {
			public void run() {
				final NapsackProperties napsackProperties_ = NapsackProperties.getInstance();
				final SongSearchCommand songSearchCommand_ = new SongSearchCommand(napsackProperties_.getClientInfo(), napsackProperties_.getSongQueryMessages());
				final CommandCampaign songSearchCampaign_ = new CommandCampaign(songSearchCommand_);
				final ResultsPane resultsPane_ = getResultsPane();
				final ProgressBar progressBar_ = getProgressBar();
				final StatusBar statusBar_ = getStatusBar();
				final ProblemsDialog problemsDialog_ = ((ViewMenu) ((MenuBar) getJMenuBar()).getViewMenu()).getProblemsDialog();

				setSongSearchCampaign(songSearchCampaign_);

				songSearchCampaign_.addCommandCampaignListener(resultsPane_);
				songSearchCampaign_.addCommandCampaignListener(statusBar_.getCommandCampaignListener());
				songSearchCampaign_.addCommandCampaignListener(progressBar_.getCommandCampaignListener());
				songSearchCampaign_.addCommandCampaignListener(getButtonResetter());
				songSearchCampaign_.addCommandCampaignListener(problemsDialog_);

				songSearchCampaign_.addExceptionListener(MainFrame.this);

				songSearchCampaign_.addCommandThreadListener(progressBar_.getCommandThreadListener());

				final NapsterCommand connectCommand_ = songSearchCommand_.getConnectCommand();
				final NapsterCommand[] songQueryCommands_ = songSearchCommand_.getSongQueryCommands();
				final NapsterCommand disconnectCommand_ = songSearchCommand_.getDisconnectCommand();

				connectCommand_.addCommandListener(statusBar_.getConnectListener());
				connectCommand_.addCommandListener(problemsDialog_.getConnectListener());
				for (int i = 0; i < songQueryCommands_.length; ++i) {
					songQueryCommands_[i].addCommandListener(resultsPane_);
					songQueryCommands_[i].addCommandListener(statusBar_.getSongQueryListener());
					songQueryCommands_[i].addCommandListener(problemsDialog_.getSongQueryListener());
				}
				disconnectCommand_.addCommandListener(statusBar_.getDisconnectListener());
				disconnectCommand_.addCommandListener(problemsDialog_.getDisconnectListener());

				songSearchCampaign_.start();
			}
		};
	}

	public void exceptionThrown(final ExceptionEvent exceptionEvent_) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				showWarningMessage(exceptionEvent_.getException().getMessage());
			}
		});
	}

	private CommandCampaignListener getButtonResetter() {
		return buttonResetter;
	}

	public MainFrameListener getMainFrameListener() {
		return mainFrameListener;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	private Runnable getResetButtons() {
		return resetButtons;
	}

	public ResultsPane getResultsPane() {
		return resultsPane;
	}

	public SearchPanel getSearchPanel() {
		return searchPanel;
	}

	public Thread getShutdownHook() {
		return shutdownHook;
	}

	public CommandCampaign getSongSearchCampaign() {
		return songSearchCampaign;
	}

	public StatusBar getStatusBar() {
		return statusBar;
	}

	private void setSongSearchCampaign(final CommandCampaign songSearchCampaign_) {
		songSearchCampaign = songSearchCampaign_;
	}

	public void showErrorMessage(final String error_) {
		JOptionPane.showMessageDialog(this, error_, ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
	}

	public void showWarningMessage(final String warning_) {
		JOptionPane.showMessageDialog(this, warning_, WARNING_TITLE, JOptionPane.WARNING_MESSAGE);
	}
}

