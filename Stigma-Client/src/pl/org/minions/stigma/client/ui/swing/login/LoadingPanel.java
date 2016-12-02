/**
 *   Stigma - Multiplayer online RPG - http://stigma.sourceforge.net
 *   Copyright (C) 2005-2009 Minions Studio
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */
package pl.org.minions.stigma.client.ui.swing.login;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;

import pl.org.minions.stigma.databases.xml.client.XmlProgressMonitor;
import pl.org.minions.stigma.databases.xml.client.XmlProgressObserver;
import pl.org.minions.utils.i18n.Translated;
import pl.org.minions.utils.logger.Log;

/**
 * Panel used to present status of data loading.
 */
public class LoadingPanel extends JPanel implements XmlProgressObserver
{
    /**
     * Enumeration used for displaying different types of
     * loading
     * panels.
     */
    public enum LoadingPanelType
    {
        Loading, Connecting, Authenticating
    }

    private static final long serialVersionUID = 1L;

    private static final int PANEL_X = 250;
    private static final int PANEL_Y = 200;
    private static final int PANEL_HEIGHT = 150;
    private static final int PANEL_WIDTH = 240;

    @Translated
    private static String LOADING_LABEL = "Loading..."; //  @jve:decl-index=0:
    @Translated
    private static String CONNECTING_LABEL = "Connecting...";
    @Translated
    private static String AUTHENTICATING_LABEL = "Authenticating...";

    @Translated
    private static String NAME = "Loading";

    private JLabel loadingLabel;
    private JProgressBar progressBar;
    private JTextArea progressStatus;
    private ProgressWorker worker; //  @jve:decl-index=0:
    private String lastTask;

    private class ProgressWorker extends SwingWorker<Void, Void>
    {
        private XmlProgressMonitor monitor;

        /**
         * Default constructor.
         * @param monitor
         *            monitors data downloading.
         */
        public ProgressWorker(XmlProgressMonitor monitor)
        {
            this.monitor = monitor;
        }

        /** {@inheritDoc} */
        @Override
        protected Void doInBackground() throws Exception
        {
            if (lastTask != monitor.getUrl().toString())
            {
                clearProgress();
                if (monitor.getTotalSize() != 0)
                {
                    updateProgress((int) (monitor.getDownloadedSize()
                                       / monitor.getTotalSize() * progressBar.getMaximum()),
                                   monitor.getUrl().toString());
                }
                else
                {
                    updateProgress(progressBar.getMaximum(), monitor.getUrl()
                                                                    .toString());
                }
            }
            else
            {
                if (monitor.getTotalSize() != 0)
                {
                    setProgress((int) (monitor.getDownloadedSize()
                        / monitor.getTotalSize() * progressBar.getMaximum()));
                }
                else
                {
                    setProgress(progressBar.getMaximum());
                }
            }
            return null;
        }
    }

    /**
     * This is the default constructor.
     * @param type
     *            type of loading panel
     */
    public LoadingPanel(LoadingPanelType type)
    {
        super();
        initialize(type);
    }

    /**
     * Default constructor - Loading type is used.
     */
    public LoadingPanel()
    {
        this(LoadingPanelType.Loading);
    }

    private void initialize(LoadingPanelType type)
    {
        //CHECKSTYLE:OFF
        this.setSize(PANEL_WIDTH, PANEL_HEIGHT);
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setLocation(PANEL_X, PANEL_Y);
        this.setLayout(null);

        loadingLabel = new JLabel();
        loadingLabel.setBounds(new Rectangle(33, 14, 176, 31));
        loadingLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadingLabel.setHorizontalTextPosition(SwingConstants.CENTER);

        switch (type)
        {
            case Authenticating:
                loadingLabel.setText(AUTHENTICATING_LABEL);
                break;
            case Connecting:
                loadingLabel.setText(CONNECTING_LABEL);
                break;
            case Loading:
                loadingLabel.setText(LOADING_LABEL);
                break;
        }

        progressBar = new JProgressBar();
        progressBar.setBounds(new Rectangle(16, 60, 209, 16));
        progressBar.setValue(0);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true);

        progressStatus = new JTextArea();
        progressStatus.setBounds(new Rectangle(15, 89, 213, 51));
        progressStatus.setWrapStyleWord(true);
        progressStatus.setAutoscrolls(true);
        progressStatus.setLineWrap(true);
        progressStatus.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        progressStatus.setEditable(false);

        this.add(loadingLabel);
        this.add(progressBar);
        this.add(progressStatus);
        //CHECKSTYLE:ON
    }

    /**
     * Sets progress bounds.
     * @param value
     *            actual value of progress
     * @param extent
     *            step with which progress will extend
     * @param min
     *            minimum value of progress bar
     * @param max
     *            maximum value of progress bar
     */
    public void setProgressData(int value, int extent, int min, int max)
    {
        try
        {
            DefaultBoundedRangeModel dbrm =
                    new DefaultBoundedRangeModel(value, extent, min, max);
            progressBar.setModel(dbrm);
        }
        catch (IllegalArgumentException e)
        {
            Log.logger.error(e);
        }
    }

    private void setProgress(int value)
    {
        progressBar.setValue(value);
    }

    /**
     * Sets progress bar value to minimum.
     */
    public void clearProgress()
    {
        progressBar.setValue(progressBar.getMinimum());
    }

    private void updateProgress(int value, String task)
    {
        if (value < progressBar.getValue())
        {
            Log.logger.warn("New progress bar value lower than old one.");
        }
        setProgress(value);
        addTask(task);
    }

    private void addTask(String task)
    {
        lastTask = task;
        progressStatus.append(task + "\n");
    }

    /** {@inheritDoc} */
    @Override
    public void processingStarted(XmlProgressMonitor monitor)
    {
        if (worker != null && !worker.isDone())
        {
            worker.cancel(true);
        }
        worker = new ProgressWorker(monitor);
        worker.run();
    }

    /**
     * Returns default panel name.
     * @return panel name
     */
    public static String getDefaultName()
    {
        return NAME;
    }

    /**
     * Returns default panel height.
     * @return panel height
     */
    public static int getDefaultHeight()
    {
        return PANEL_HEIGHT;
    }

    /**
     * Returns default panel width.
     * @return panel width
     */
    public static int getDefaultWidth()
    {
        return PANEL_WIDTH;
    }

    /**
     * Returns default panel position X coordinate.
     * @return X coordinate
     */
    public static int getDefaultPositionX()
    {
        return PANEL_X;
    }

    /**
     * Returns default panel position Y coordinate.
     * @return X coordinate
     */
    public static int getDefaultPositionY()
    {
        return PANEL_Y;
    }
}
