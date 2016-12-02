/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: StatisticPanel.java
 * 
 * @author Volker Holthaus <v.holthaus@procar.de>
 * 
 * This file is part of JDiveLog.
 * JDiveLog is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * JDiveLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JDiveLog; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sf.jdivelog.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneLayout;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.statistics.ChartData;
import net.sf.jdivelog.gui.statistics.CountChartData3D;
import net.sf.jdivelog.gui.statistics.DatasetFactory;
import net.sf.jdivelog.gui.statusbar.StatusInterface;
import net.sf.jdivelog.model.DiveSite;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.JDiveLog;
import net.sf.jdivelog.util.UnitConverter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;

/**
 * Description: Panel displaying the different kinds of statitics
 * 
 * @author Volker Holthaus <v.holthaus@procar.de>
 */
public class StatisticPanel extends JPanel implements ActionListener {

    public static final String TYPE_PIE = "pie";

    public static final String TYPE_PIE3D = "pie3d";

    public static final String TYPE_BAR = "bar";

    public static final String TYPE_BAR3D = "bar3d";

    public static final String ORIENTATION_HORIZONTAL = "horizontal";

    public static final String ORIENTATION_VERTICAL = "vertical";

    private static final long serialVersionUID = 3258134643768178998L;

    private JTable countryTable;

    private JScrollPane statisticDetailPane = null;

    private boolean initialized = false;

    private JButton buddyStatisticButton;

    private JButton divePlaceStatisticButton;

    private JButton countryStatisticButton;

    private JButton diveTypeStatisticButton;

    private MainWindow mainWindow;

    private JPanel buttonPanel;

    private JToolBar jToolBar = null;

    private JDiveLog divelog = null;

    private StatusInterface status;

    private JButton activityStatisticButton = null;

    private JButton watersStatisticButton = null;

    private JButton frequencyMonthStatisticButton = null;

    private JButton frequencyYearStatisticButton = null;

    private JButton frequencyYearTimeStatisticButton = null;

    /**
     * Default Constructor for GUI Builder, do not use!
     */
    @Deprecated
    public StatisticPanel() {
        super();
        init();
    }

    public StatisticPanel(MainWindow mainWindow) {
        super();
        this.mainWindow = mainWindow;
        this.status = mainWindow.getStatusBar();
        init();
    }

    //
    // private methods
    //

    private void init() {
        if (!initialized) {
            this.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            this.setLayout(new BorderLayout());
            this.add(getJToolBar(), BorderLayout.NORTH);
            this.add(getStatisticPane(), BorderLayout.CENTER);
        }
    }

    private JScrollPane getStatisticPane() {
        if (statisticDetailPane == null) {
            statisticDetailPane = new JScrollPane();
            statisticDetailPane.setLayout(new ScrollPaneLayout());
        }
        return statisticDetailPane;
    }

    private ChartPanel getChartPanel(JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart);
        return chartPanel;
    }

    //
    // methods for creating specific charts
    //

    private JFreeChart getBuddyStatisticChart() {
        return getChart(mainWindow.getLogBook().getStatisticSettings().getBuddyStatistic().getType(), mainWindow.getLogBook().getStatisticSettings()
                .getBuddyStatistic().getOrientation(), getBuddyData());
    }

    private JFreeChart getDivePlaceStatisticChart(String country) {
        return getChart(mainWindow.getLogBook().getStatisticSettings().getDivePlaceStatistic().getType(), mainWindow.getLogBook().getStatisticSettings()
                .getDivePlaceStatistic().getOrientation(), getPlaceData(country));
    }

    private JFreeChart getCountryStatisticChart() {
        return getChart(mainWindow.getLogBook().getStatisticSettings().getCountryStatistic().getType(), mainWindow.getLogBook().getStatisticSettings()
                .getCountryStatistic().getOrientation(), getCountryData());
    }

    private JFreeChart getDiveTypeStatisticChart() {
        return getChart(mainWindow.getLogBook().getStatisticSettings().getDiveTypeStatistic().getType(), mainWindow.getLogBook().getStatisticSettings()
                .getDiveTypeStatistic().getOrientation(), getDiveTypeData());
    }

    private JFreeChart getDiveActivityStatisticChart() {
        return getChart(mainWindow.getLogBook().getStatisticSettings().getDiveActivityStatistic().getType(), mainWindow.getLogBook().getStatisticSettings()
                .getDiveActivityStatistic().getOrientation(), getDiveActivityData());
    }

    private JFreeChart getWatersStatisticChart() {
        return getChart(mainWindow.getLogBook().getStatisticSettings().getWatersStatistic().getType(), mainWindow.getLogBook().getStatisticSettings()
                .getWatersStatistic().getOrientation(), getWatersData());
    }

    private JFreeChart getFrequencyMonthStatisticChart() {
        return getChart(getFrequencyMonthData());
    }

    private JFreeChart getFrequencyYearStatisticChart() {
        return getChart(getFrequencyYearData());
    }

    private JFreeChart getFrequencyYearTimeStatisticChart() {
        return getChart(getFrequencyYearTimeData());
    }

    //
    // generic methods for creating charts
    //

    private JFreeChart getChart(String type, String orientation, ChartData data) {
        try {
            status.messageInfo("creating_chart");
            status.infiniteProgressbarStart();
            if (TYPE_PIE.equals(type)) {
                PieDataset dataset = DatasetFactory.getPieDataset(data);
                JFreeChart chart = ChartFactory.createPieChart(data.getName(), dataset, true, true, false);
                return chart;
            } else if (TYPE_PIE3D.equals(type)) {
                PieDataset dataset = DatasetFactory.getPieDataset(data);
                JFreeChart chart = ChartFactory.createPieChart3D(data.getName(), dataset, true, true, false);
                return chart;
            } else if (TYPE_BAR3D.equals(type)) {
                CategoryDataset dataset = DatasetFactory.getCategoryDataset(data);
                JFreeChart chart = ChartFactory.createBarChart3D(data.getName(), data.getCatLabel(), data.getValLabel(), dataset, getOrientation(orientation),
                        false, true, false);
                return chart;
            }
            CategoryDataset dataset = DatasetFactory.getCategoryDataset(data);
            JFreeChart chart = ChartFactory.createBarChart(data.getName(), data.getCatLabel(), data.getValLabel(), dataset, getOrientation(orientation), false,
                    true, false);
            return chart;
        } finally {
            status.infiniteProgressbarEnd();
            status.messageClear();
        }
    }

    private JFreeChart getChart(CountChartData3D data) {
        try {
            status.messageInfo("creating_chart");
            status.infiniteProgressbarStart();
            CategoryDataset dataset = DatasetFactory.getCategoryDataset(data);
            JFreeChart chart = ChartFactory.createBarChart(data.getName(), data.getValLabel(), data.getCatLabel(), dataset, PlotOrientation.VERTICAL, true,
                    true, false);
            return chart;
        } finally {
            status.infiniteProgressbarEnd();
            status.messageClear();
        }
    }

    private PlotOrientation getOrientation(String orientation) {
        if (ORIENTATION_VERTICAL.equals(orientation)) {
            return PlotOrientation.VERTICAL;
        }
        return PlotOrientation.HORIZONTAL;
    }

    //
    // methods for getting chart data
    //

    private ChartData getBuddyData() {
        ChartData data = new ChartData(Messages.getString("buddystatistic"), Messages.getString("buddy"), Messages.getString("dives"));
        status.messageInfo(Messages.getString("calculate_statistic"));
        status.countingProgressbarStart(divelog.getDives().size(), true);
        try {
            Iterator<JDive> it = divelog.getDives().iterator();
            while (it.hasNext()) {
                JDive dive = it.next();
                if (dive.getBuddy() != null) {
                    StringTokenizer st = new StringTokenizer(dive.getBuddy(), ",");
                    while (st.hasMoreTokens()) {
                        String buddyname = st.nextToken().trim();
                        data.add(buddyname);
                    }
                }
                status.countingProgressbarIncrement();
            }
        } finally {
            status.countingProgressbarEnd();
            status.messageClear();
        }
        data.orderByCountDescending();
        data.firstElements(20);
        return data;
    }

    private ChartData getCountryData() {
        ChartData data = new ChartData(Messages.getString("countrystatistic"), Messages.getString("country"), Messages.getString("dives"));
        status.messageInfo(Messages.getString("calculate_statistic"));
        status.countingProgressbarStart(divelog.getDives().size(), true);
        try {
            Iterator<JDive> it = divelog.getDives().iterator();
            while (it.hasNext()) {
                JDive dive = it.next();
                DiveSite site = mainWindow.getLogBook().getMasterdata().getDiveSite(dive);
                if (site != null) {
                    String country = site.getCountry();
                    data.add(country);
                }
                status.countingProgressbarIncrement();
            }
        } finally {
            status.countingProgressbarEnd();
            status.messageClear();
        }
        data.orderByCountDescending();
        return data;
    }

    private ChartData getPlaceData(String country) {
        ChartData data = new ChartData(Messages.getString("diveplacestatistic") + " - " + country, Messages.getString("place"), Messages.getString("dives"));
        status.messageInfo(Messages.getString("calculate_statistic"));
        status.countingProgressbarStart(divelog.getDives().size(), true);
        try {
            Iterator<JDive> it = divelog.getDives().iterator();
            while (it.hasNext()) {
                JDive dive = it.next();
                DiveSite site = mainWindow.getLogBook().getMasterdata().getDiveSite(dive);
                if (site != null) {
                    if (country.equals(site.getCountry())) {
                        String spot = site.getSpot();
                        String city = site.getCity();
                        StringBuffer place = new StringBuffer();
                        if (spot != null && !"".equals(spot)) {
                            place.append(spot);
                            if (city != null && !"".equals(city)) {
                                place.append(", ");
                            }
                        }
                        if (city != null && !"".equals(city)) {
                            place.append(city);
                        }
                        data.add(place.toString());
                    }
                }
                status.countingProgressbarIncrement();
            }
        } finally {
            status.countingProgressbarEnd();
            status.messageClear();
        }
        data.orderByCountDescending();
        data.firstElements(25);
        return data;

    }

    private ChartData getDiveTypeData() {
        ChartData data = new ChartData(Messages.getString("divetypestatistic"), Messages.getString("divetype"), Messages.getString("dives"));
        status.messageInfo(Messages.getString("calculate_statistic"));
        status.countingProgressbarStart(divelog.getDives().size(), true);
        try {
            Iterator<JDive> it = divelog.getDives().iterator();
            while (it.hasNext()) {
                JDive dive = it.next();
                if (dive.getDiveType() != null) {
                    StringTokenizer st = new StringTokenizer(dive.getDiveType(), ",");
                    while (st.hasMoreTokens()) {
                        data.add(st.nextToken());
                    }
                }
                status.countingProgressbarIncrement();
            }
        } finally {
            status.countingProgressbarEnd();
            status.messageClear();
        }
        return data;
    }

    private ChartData getDiveActivityData() {
        ChartData data = new ChartData(Messages.getString("diveactivitystatistic"), Messages.getString("diveactivity"), Messages.getString("dives"));
        status.messageInfo(Messages.getString("calculate_statistic"));
        status.countingProgressbarStart(divelog.getDives().size(), true);
        try {
            Iterator<JDive> it = divelog.getDives().iterator();
            while (it.hasNext()) {
                JDive dive = it.next();
                if (dive.getDiveActivity() != null && !"".equals(dive.getDiveActivity())) {
                    StringTokenizer st = new StringTokenizer(dive.getDiveActivity(), ",");
                    while (st.hasMoreTokens()) {
                        data.add(st.nextToken());
                    }
                }
                status.countingProgressbarIncrement();
            }
        } finally {
            status.countingProgressbarEnd();
            status.messageClear();
        }
        return data;
    }

    private ChartData getWatersData() {
        ChartData data = new ChartData(Messages.getString("watersstatistic"), Messages.getString("waters"), Messages.getString("dives"));
        status.messageInfo(Messages.getString("calculate_statistic"));
        status.countingProgressbarStart(divelog.getDives().size(), true);
        try {
            Iterator<JDive> it = divelog.getDives().iterator();
            while (it.hasNext()) {
                JDive dive = it.next();
                DiveSite site = mainWindow.getLogBook().getMasterdata().getDiveSite(dive);
                if (site != null && site.getWaters() != null) {
                    data.add(site.getWaters());
                }
                status.countingProgressbarIncrement();
            }
        } finally {
            status.countingProgressbarEnd();
            status.messageClear();
        }
        data.orderByCountDescending();
        data.firstElements(25);
        return data;
    }

    private CountChartData3D getFrequencyYearData() {
        CountChartData3D data = new CountChartData3D(Messages.getString("frequency"), Messages.getString("dives"), Messages.getString("years"));
        status.messageInfo(Messages.getString("calculate_statistic"));
        status.countingProgressbarStart(divelog.getDives().size(), true);
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat yfmt = new SimpleDateFormat("yy");
        gc.set(Calendar.DAY_OF_MONTH, 1);
        try {
            Iterator<JDive> it = divelog.getDives().iterator();
            while (it.hasNext()) {
                JDive dive = it.next();
                if (dive.getDate() != null) {
                    String year = yfmt.format(dive.getDate());
                    data.add("", year);
                }
                status.countingProgressbarIncrement();
            }
        } finally {
            status.countingProgressbarEnd();
            status.messageClear();
        }
        return data;
    }

    private CountChartData3D getFrequencyYearTimeData() {
        CountChartData3D data = new CountChartData3D(Messages.getString("divetime"), Messages.getString("divetime"), Messages.getString("years"));
        status.messageInfo(Messages.getString("calculate_statistic"));
        status.countingProgressbarStart(divelog.getDives().size(), true);
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat yfmt = new SimpleDateFormat("yy");
        gc.set(Calendar.DAY_OF_MONTH, 1);
        try {
            Iterator<JDive> it = divelog.getDives().iterator();
            while (it.hasNext()) {
                JDive dive = it.next();
                if (dive.getDate() != null) {
                    String year = yfmt.format(dive.getDate());
                    Double dur = dive.getDuration();
                    if (dur != null) {
                        UnitConverter c = new UnitConverter(UnitConverter.getSystem(dive.getUnits()), UnitConverter.getDisplaySystem());
                        data.add("", year, c.convertTime(dur)/60);
                    }
                }
                status.countingProgressbarIncrement();
            }
        } finally {
            status.countingProgressbarEnd();
            status.messageClear();
        }
        return data;
    }

    private CountChartData3D getFrequencyMonthData() {
        CountChartData3D data = new CountChartData3D(Messages.getString("frequency"), Messages.getString("dives"), Messages.getString("months"));
        status.messageInfo(Messages.getString("calculate_statistic"));
        status.countingProgressbarStart(divelog.getDives().size(), true);
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat mfmt = new SimpleDateFormat("MMM");
        gc.set(Calendar.DAY_OF_MONTH, 1);
        for(int i=0; i<12; i++) {
            gc.set(Calendar.MONTH, i);
            data.addCol(mfmt.format(gc.getTime()));
        }
        try {
            Iterator<JDive> it = divelog.getDives().iterator();
            while (it.hasNext()) {
                JDive dive = it.next();
                if (dive.getDate() != null) {
                    String month = mfmt.format(dive.getDate());
                    data.add("", month);
                }
                status.countingProgressbarIncrement();
            }
        } finally {
            status.countingProgressbarEnd();
            status.messageClear();
        }
        return data;
    }

    //
    // methods for switching the statistic
    //

    private void addBuddyStatistic() {
        divelog = this.mainWindow.getLogBook();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(getChartPanel(getBuddyStatisticChart()), BorderLayout.CENTER);
        statisticDetailPane.setViewportView(panel);
    }

    private void addDivePlaceStatistic() {
        divelog = this.mainWindow.getLogBook();
        JPanel chartPanel = new JPanel();
        chartPanel.setLayout(new BorderLayout());
        JSplitPane panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(getDiveCountryTable(chartPanel)), chartPanel);
        panel.setDividerLocation(200);
        getStatisticPane().setViewportView(panel);
    }

    private void addCountryStatistic() {
        divelog = this.mainWindow.getLogBook();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(getChartPanel(getCountryStatisticChart()), BorderLayout.CENTER);
        getStatisticPane().setViewportView(panel);
    }

    private void addDiveTypeStatistic() {
        divelog = this.mainWindow.getLogBook();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(getChartPanel(getDiveTypeStatisticChart()), BorderLayout.CENTER);
        getStatisticPane().setViewportView(panel);
    }

    private void addDiveActivityStatistic() {
        divelog = this.mainWindow.getLogBook();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(getChartPanel(getDiveActivityStatisticChart()), BorderLayout.CENTER);
        getStatisticPane().setViewportView(panel);
    }

    private void addWatersStatistic() {
        divelog = this.mainWindow.getLogBook();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(getChartPanel(getWatersStatisticChart()), BorderLayout.CENTER);
        getStatisticPane().setViewportView(panel);
    }

    private void addFrequencyYearStatistic() {
        divelog = this.mainWindow.getLogBook();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(getChartPanel(getFrequencyYearStatisticChart()), BorderLayout.CENTER);
        getStatisticPane().setViewportView(panel);
    }

    private void addFrequencyYearTimeStatistic() {
        divelog = this.mainWindow.getLogBook();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(getChartPanel(getFrequencyYearTimeStatisticChart()), BorderLayout.CENTER);
        getStatisticPane().setViewportView(panel);
    }

    private void addFrequencyMonthStatistic() {
        divelog = this.mainWindow.getLogBook();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(getChartPanel(getFrequencyMonthStatisticChart()), BorderLayout.CENTER);
        getStatisticPane().setViewportView(panel);
    }

    //
    // other helper methods
    //

    private JTable getDiveCountryTable(final JPanel targetPanel) {
        ChartData data = getCountryData();
        List<String> names = data.getNames();
        String[][] countrys = new String[names.size()][2];

        String[] column_name = { data.getCatLabel(), data.getValLabel() };
        Iterator<String> it = names.iterator();
        int i = 0;
        while (it.hasNext()) {
            String country = it.next();
            countrys[i][0] = country;
            countrys[i][1] = String.valueOf(data.getCount(country));
            i++;
        }

        countryTable = new JTable(countrys, column_name);
        countryTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        countryTable.setCellEditor(null);
        countryTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    targetPanel.removeAll();
                    targetPanel.add(getChartPanel(getDivePlaceStatisticChart((String) countryTable.getValueAt(countryTable.getSelectedRow(), 0))),
                            BorderLayout.CENTER);
                    targetPanel.revalidate();
                }
            }
        });
        return countryTable;
    }

    private JToolBar getJToolBar() {
        if (jToolBar == null) {
            jToolBar = new JToolBar();
            jToolBar.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            jToolBar.setPreferredSize(new java.awt.Dimension(44, 64));
            jToolBar.add(getButtonPanel());
        }
        return jToolBar;
    }

    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(2, 2, 2, 2);
            gc.anchor = GridBagConstraints.NORTHWEST;
            gc.fill = GridBagConstraints.BOTH;
            gc.gridx = 0;
            gc.gridy = 0;
            buttonPanel.add(getBuddyStatisticButton(), gc);
            gc.gridx = 1;
            gc.gridy = 0;
            buttonPanel.add(getDivePlaceStatisticButton(), gc);
            gc.gridx = 2;
            gc.gridy = 0;
            buttonPanel.add(getCountryStatisticButton(), gc);
            gc.gridx = 3;
            gc.gridy = 0;
            buttonPanel.add(getDiveTypeStatisticButton(), gc);
            gc.gridx = 4;
            gc.gridy = 0;
            buttonPanel.add(getDiveActivityStatisticButton(), gc);
            gc.gridx = 0;
            gc.gridy = 1;
            buttonPanel.add(getWatersStatisticButton(), gc);
            gc.gridx = 1;
            gc.gridy = 1;
            buttonPanel.add(getFrequencyMonthStatisticButton(), gc);
            gc.gridx = 2;
            gc.gridy = 1;
            buttonPanel.add(getFrequencyYearStatisticButton(), gc);
            gc.gridx = 3;
            gc.gridy = 1;
            buttonPanel.add(getFrequencyYearTimeStatisticButton(), gc);

        }
        return buttonPanel;
    }

    private JButton getBuddyStatisticButton() {
        if (buddyStatisticButton == null) {
            buddyStatisticButton = new JButton(Messages.getString("buddystatistic")); //$NON-NLS-1$
            buddyStatisticButton.addActionListener(this);
        }
        return buddyStatisticButton;
    }

    private JButton getDivePlaceStatisticButton() {
        if (divePlaceStatisticButton == null) {
            divePlaceStatisticButton = new JButton(Messages.getString("diveplacestatistic")); //$NON-NLS-1$
            divePlaceStatisticButton.addActionListener(this);
        }
        return divePlaceStatisticButton;
    }

    private JButton getCountryStatisticButton() {
        if (countryStatisticButton == null) {
            countryStatisticButton = new JButton(Messages.getString("countrystatistic")); //$NON-NLS-1$
            countryStatisticButton.addActionListener(this);
        }
        return countryStatisticButton;
    }

    private JButton getDiveTypeStatisticButton() {
        if (diveTypeStatisticButton == null) {
            diveTypeStatisticButton = new JButton(Messages.getString("divetypestatistic")); //$NON-NLS-1$
            diveTypeStatisticButton.addActionListener(this);
        }
        return diveTypeStatisticButton;
    }

    private JButton getDiveActivityStatisticButton() {
        if (activityStatisticButton == null) {
            activityStatisticButton = new JButton(Messages.getString("diveactivitystatistic")); //$NON-NLS-1$
            activityStatisticButton.addActionListener(this);
        }
        return activityStatisticButton;
    }

    private JButton getWatersStatisticButton() {
        if (watersStatisticButton == null) {
            watersStatisticButton = new JButton(Messages.getString("watersstatistic")); //$NON-NLS-1$
            watersStatisticButton.addActionListener(this);
        }
        return watersStatisticButton;
    }

    private JButton getFrequencyMonthStatisticButton() {
        if (frequencyMonthStatisticButton == null) {
            frequencyMonthStatisticButton = new JButton(Messages.getString("frequencymonthstatistic")); //$NON-NLS-1$
            frequencyMonthStatisticButton.addActionListener(this);
        }
        return frequencyMonthStatisticButton;
    }

    private JButton getFrequencyYearStatisticButton() {
        if (frequencyYearStatisticButton == null) {
            frequencyYearStatisticButton = new JButton(Messages.getString("frequencyyearstatistic")); //$NON-NLS-1$
            frequencyYearStatisticButton.addActionListener(this);
        }
        return frequencyYearStatisticButton;
    }

    private JButton getFrequencyYearTimeStatisticButton() {
        if (frequencyYearTimeStatisticButton == null) {
            frequencyYearTimeStatisticButton = new JButton(Messages.getString("frequencyyeartimestatistic")); //$NON-NLS-1$
            frequencyYearTimeStatisticButton.addActionListener(this);
        }
        return frequencyYearTimeStatisticButton;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buddyStatisticButton) {
            addBuddyStatistic();
        } else if (e.getSource() == diveTypeStatisticButton) {
            addDiveTypeStatistic();
        } else if (e.getSource() == divePlaceStatisticButton) {
            addDivePlaceStatistic();
        } else if (e.getSource() == countryStatisticButton) {
            addCountryStatistic();
        } else if (e.getSource() == activityStatisticButton) {
            addDiveActivityStatistic();
        } else if (e.getSource() == watersStatisticButton) {
            addWatersStatistic();
        } else if (e.getSource() == frequencyMonthStatisticButton) {
            addFrequencyMonthStatistic();
        } else if (e.getSource() == frequencyYearStatisticButton) {
            addFrequencyYearStatistic();
        } else if (e.getSource() == frequencyYearTimeStatisticButton) {
            addFrequencyYearTimeStatistic();
        }
    }
}