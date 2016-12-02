/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DecompressionStops.java
 * 
 * @author Volker Holthaus <volker.urlaub@gmx.de>
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
package net.sf.jdivelog.model.decompression;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.sf.jdivelog.gui.MessageDialog;
import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.util.NitroxCalculationUtil;
import net.sf.jdivelog.util.UnitConverter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Deco2000_1500_surface implements DecompressionTable {

    private static final NumberFormat CNS_FORMAT = new DecimalFormat("#0.0");

    private static final String DOWN_DIVE_SPEED = new String("15");

    private static final String UP_DIVE_SPEED = new String("10");

    private static final String O2_PART = new String("21");

    private HashMap<Integer, Integer> decoStops = new HashMap<Integer, Integer>();

    private HashMap<Integer, Integer> zeroHours = new HashMap<Integer, Integer>();

    private HashMap<Integer, HashMap<Integer, Integer>> decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();

    private HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> dive_depth = new HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>>();

    private HashMap<Integer, HashMap<Integer, String>> repeat_groups = new HashMap<Integer, HashMap<Integer, String>>();

    private HashMap<Integer, String> repeat_group = new HashMap<Integer, String>();

    private HashMap<String, Integer> no_flight = new HashMap<String, Integer>();

    private HashMap<String, Integer[]> surface_duration = new HashMap<String, Integer[]>();

    private HashMap<Integer, Integer[]> add_on_time = new HashMap<Integer, Integer[]>();

    // the depth and the duration for the decompression calculation
    private Integer calculated_depth_higher = null;

    private Integer calculated_depth_lower = null;

    private Integer calculated_duration = null;

    private Integer depth = null;

    private Integer down_dive_speed = null;

    private Double o2_ratio = null;

    private Integer duration = null;

    private Double complete_duration = null;

    private Double complete_up_dive_duration = null;

    private Integer add_dive_time = new Integer(0);

    private Deco2000_ConfigurationPanel configurationPanel;

    /**
     * Constructor
     *
     */
    public Deco2000_1500_surface() {

        // generate the decostops 15 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 5);
        decoStops_with_time.put(72, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 10);
        decoStops_with_time.put(84, decoStops);
        dive_depth.put(15, decoStops_with_time);

        // generate the decostops 18 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 3);
        decoStops_with_time.put(45, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 9);
        decoStops_with_time.put(55, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 15);
        decoStops_with_time.put(65, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 22);
        decoStops_with_time.put(75, decoStops);
        dive_depth.put(18, decoStops_with_time);

        // generate the decostops 21 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 2);
        decoStops_with_time.put(31, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 5);
        decoStops_with_time.put(36, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 8);
        decoStops_with_time.put(41, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 12);
        decoStops_with_time.put(46, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 16);
        decoStops_with_time.put(51, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 20);
        decoStops.put(6, 1);
        decoStops_with_time.put(56, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 25);
        decoStops.put(3, 2);
        decoStops_with_time.put(61, decoStops);
        dive_depth.put(21, decoStops_with_time);

        // generate the decostops 24 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 2);
        decoStops_with_time.put(23, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 4);
        decoStops_with_time.put(27, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 7);
        decoStops_with_time.put(31, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 11);
        decoStops_with_time.put(35, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 1);
        decoStops.put(3, 14);
        decoStops_with_time.put(39, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 3);
        decoStops.put(3, 17);
        decoStops_with_time.put(43, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 4);
        decoStops.put(3, 20);
        decoStops_with_time.put(47, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 6);
        decoStops.put(3, 24);
        decoStops_with_time.put(51, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 8);
        decoStops.put(3, 28);
        decoStops_with_time.put(55, decoStops);
        dive_depth.put(24, decoStops_with_time);

        // generate the decostops 27 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 1);
        decoStops_with_time.put(18, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 4);
        decoStops_with_time.put(22, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 8);
        decoStops_with_time.put(26, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 2);
        decoStops.put(3, 10);
        decoStops_with_time.put(30, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 3);
        decoStops.put(3, 14);
        decoStops_with_time.put(34, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 5);
        decoStops.put(3, 18);
        decoStops_with_time.put(38, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 7);
        decoStops.put(3, 22);
        decoStops_with_time.put(42, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 10);
        decoStops.put(3, 26);
        decoStops_with_time.put(46, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 1);
        decoStops.put(6, 11);
        decoStops.put(3, 31);
        decoStops_with_time.put(50, decoStops);
        dive_depth.put(27, decoStops_with_time);

        // generate the decostops 30 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 2);
        decoStops_with_time.put(15, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 4);
        decoStops_with_time.put(18, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 7);
        decoStops_with_time.put(21, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 2);
        decoStops.put(3, 9);
        decoStops_with_time.put(24, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 3);
        decoStops.put(3, 12);
        decoStops_with_time.put(27, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 5);
        decoStops.put(3, 14);
        decoStops_with_time.put(30, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 7);
        decoStops.put(3, 17);
        decoStops_with_time.put(33, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 1);
        decoStops.put(6, 8);
        decoStops.put(3, 21);
        decoStops_with_time.put(36, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 2);
        decoStops.put(6, 9);
        decoStops.put(3, 25);
        decoStops_with_time.put(39, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 3);
        decoStops.put(6, 11);
        decoStops.put(3, 28);
        decoStops_with_time.put(42, decoStops);
        dive_depth.put(30, decoStops_with_time);

        // generate the decostops 33 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 1);
        decoStops_with_time.put(12, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 4);
        decoStops_with_time.put(15, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 1);
        decoStops.put(3, 6);
        decoStops_with_time.put(18, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 2);
        decoStops.put(3, 9);
        decoStops_with_time.put(21, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 4);
        decoStops.put(3, 12);
        decoStops_with_time.put(24, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 1);
        decoStops.put(6, 5);
        decoStops.put(3, 15);
        decoStops_with_time.put(27, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 2);
        decoStops.put(6, 7);
        decoStops.put(3, 19);
        decoStops_with_time.put(30, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 3);
        decoStops.put(6, 9);
        decoStops.put(3, 22);
        decoStops_with_time.put(33, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 4);
        decoStops.put(6, 10);
        decoStops.put(3, 27);
        decoStops_with_time.put(36, decoStops);
        dive_depth.put(33, decoStops_with_time);

        // generate the decostops 36 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 1);
        decoStops_with_time.put(10, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 5);
        decoStops_with_time.put(14, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 3);
        decoStops.put(3, 8);
        decoStops_with_time.put(18, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 5);
        decoStops.put(3, 11);
        decoStops_with_time.put(21, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 2);
        decoStops.put(6, 5);
        decoStops.put(3, 15);
        decoStops_with_time.put(24, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 3);
        decoStops.put(6, 7);
        decoStops.put(3, 19);
        decoStops_with_time.put(27, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 4);
        decoStops.put(6, 9);
        decoStops.put(3, 23);
        decoStops_with_time.put(30, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(12, 1);
        decoStops.put(9, 5);
        decoStops.put(6, 11);
        decoStops.put(3, 28);
        decoStops_with_time.put(33, decoStops);
        dive_depth.put(36, decoStops_with_time);

        // generate the decostops 39 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 1);
        decoStops_with_time.put(9, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 4);
        decoStops_with_time.put(12, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 2);
        decoStops.put(3, 7);
        decoStops_with_time.put(15, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 1);
        decoStops.put(6, 4);
        decoStops.put(3, 10);
        decoStops_with_time.put(18, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 2);
        decoStops.put(6, 6);
        decoStops.put(3, 14);
        decoStops_with_time.put(21, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 4);
        decoStops.put(6, 7);
        decoStops.put(3, 18);
        decoStops_with_time.put(24, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(12, 1);
        decoStops.put(9, 4);
        decoStops.put(6, 9);
        decoStops.put(3, 24);
        decoStops_with_time.put(27, decoStops);
        dive_depth.put(39, decoStops_with_time);

        // generate the decostops 42 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 4);
        decoStops_with_time.put(10, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 2);
        decoStops.put(3, 6);
        decoStops_with_time.put(13, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 1);
        decoStops.put(6, 4);
        decoStops.put(3, 10);
        decoStops_with_time.put(16, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 3);
        decoStops.put(6, 5);
        decoStops.put(3, 14);
        decoStops_with_time.put(19, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(12, 1);
        decoStops.put(9, 4);
        decoStops.put(6, 7);
        decoStops.put(3, 19);
        decoStops_with_time.put(22, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(12, 2);
        decoStops.put(9, 5);
        decoStops.put(6, 9);
        decoStops.put(3, 25);
        decoStops_with_time.put(25, decoStops);
        dive_depth.put(42, decoStops_with_time);

        // generate the decostops 45 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 2);
        decoStops_with_time.put(8, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 1);
        decoStops.put(3, 4);
        decoStops_with_time.put(10, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 3);
        decoStops.put(3, 6);
        decoStops_with_time.put(12, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 1);
        decoStops.put(6, 4);
        decoStops.put(3, 9);
        decoStops_with_time.put(14, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 2);
        decoStops.put(6, 5);
        decoStops.put(3, 12);
        decoStops_with_time.put(16, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(12, 1);
        decoStops.put(9, 3);
        decoStops.put(6, 6);
        decoStops.put(3, 15);
        decoStops_with_time.put(18, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(12, 1);
        decoStops.put(9, 4);
        decoStops.put(6, 8);
        decoStops.put(3, 18);
        decoStops_with_time.put(20, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(12, 2);
        decoStops.put(9, 5);
        decoStops.put(6, 9);
        decoStops.put(3, 22);
        decoStops_with_time.put(22, decoStops);
        dive_depth.put(45, decoStops_with_time);

        // generate the decostops 48 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 2);
        decoStops_with_time.put(7, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 1);
        decoStops.put(3, 4);
        decoStops_with_time.put(9, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 3);
        decoStops.put(3, 6);
        decoStops_with_time.put(11, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 2);
        decoStops.put(6, 3);
        decoStops.put(3, 10);
        decoStops_with_time.put(13, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 3);
        decoStops.put(6, 5);
        decoStops.put(3, 12);
        decoStops_with_time.put(15, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(12, 1);
        decoStops.put(9, 4);
        decoStops.put(6, 6);
        decoStops.put(3, 16);
        decoStops_with_time.put(17, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(12, 2);
        decoStops.put(9, 4);
        decoStops.put(6, 8);
        decoStops.put(3, 20);
        decoStops_with_time.put(19, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(15, 1);
        decoStops.put(12, 2);
        decoStops.put(9, 5);
        decoStops.put(6, 10);
        decoStops.put(3, 24);
        decoStops_with_time.put(21, decoStops);
        dive_depth.put(48, decoStops_with_time);

        // generate the decostops 51 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 2);
        decoStops_with_time.put(6, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 1);
        decoStops.put(3, 4);
        decoStops_with_time.put(8, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 1);
        decoStops.put(6, 2);
        decoStops.put(3, 7);
        decoStops_with_time.put(10, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 2);
        decoStops.put(6, 4);
        decoStops.put(3, 9);
        decoStops_with_time.put(12, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(12, 1);
        decoStops.put(9, 3);
        decoStops.put(6, 5);
        decoStops.put(3, 13);
        decoStops_with_time.put(14, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(12, 2);
        decoStops.put(9, 3);
        decoStops.put(6, 7);
        decoStops.put(3, 17);
        decoStops_with_time.put(16, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(15, 1);
        decoStops.put(12, 2);
        decoStops.put(9, 5);
        decoStops.put(6, 8);
        decoStops.put(3, 21);
        decoStops_with_time.put(18, decoStops);
        dive_depth.put(51, decoStops_with_time);

        // generate the decostops 54 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 3);
        decoStops_with_time.put(6, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(6, 2);
        decoStops.put(3, 5);
        decoStops_with_time.put(8, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 1);
        decoStops.put(6, 4);
        decoStops.put(3, 7);
        decoStops_with_time.put(10, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(12, 1);
        decoStops.put(9, 2);
        decoStops.put(6, 5);
        decoStops.put(3, 11);
        decoStops_with_time.put(12, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(12, 2);
        decoStops.put(9, 3);
        decoStops.put(6, 6);
        decoStops.put(3, 15);
        decoStops_with_time.put(14, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(15, 1);
        decoStops.put(12, 3);
        decoStops.put(9, 4);
        decoStops.put(6, 8);
        decoStops.put(3, 20);
        decoStops_with_time.put(16, decoStops);
        dive_depth.put(54, decoStops_with_time);

        // generate the decostops 57 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(6, 1);
        decoStops.put(3, 3);
        decoStops_with_time.put(6, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 1);
        decoStops.put(6, 2);
        decoStops.put(3, 6);
        decoStops_with_time.put(8, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 2);
        decoStops.put(6, 4);
        decoStops.put(3, 9);
        decoStops_with_time.put(10, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(12, 1);
        decoStops.put(9, 3);
        decoStops.put(6, 6);
        decoStops.put(3, 12);
        decoStops_with_time.put(12, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(15, 1);
        decoStops.put(12, 2);
        decoStops.put(9, 4);
        decoStops.put(6, 7);
        decoStops.put(3, 18);
        decoStops_with_time.put(14, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(15, 2);
        decoStops.put(12, 3);
        decoStops.put(9, 4);
        decoStops.put(6, 10);
        decoStops.put(3, 23);
        decoStops_with_time.put(16, decoStops);
        dive_depth.put(57, decoStops_with_time);

        // generate the decostops 60 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(6, 1);
        decoStops.put(3, 4);
        decoStops_with_time.put(6, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 1);
        decoStops.put(6, 3);
        decoStops.put(3, 7);
        decoStops_with_time.put(8, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(12, 1);
        decoStops.put(9, 2);
        decoStops.put(6, 5);
        decoStops.put(3, 10);
        decoStops_with_time.put(10, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(12, 2);
        decoStops.put(9, 4);
        decoStops.put(6, 6);
        decoStops.put(3, 15);
        decoStops_with_time.put(12, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(15, 1);
        decoStops.put(12, 2);
        decoStops.put(9, 4);
        decoStops.put(6, 7);
        decoStops.put(3, 18);
        decoStops_with_time.put(13, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(15, 1);
        decoStops.put(12, 3);
        decoStops.put(9, 4);
        decoStops.put(6, 8);
        decoStops.put(3, 21);
        decoStops_with_time.put(14, decoStops);
        dive_depth.put(60, decoStops_with_time);

        // generate the decostops 63 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(6, 2);
        decoStops.put(3, 5);
        decoStops_with_time.put(6, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(9, 2);
        decoStops.put(6, 3);
        decoStops.put(3, 8);
        decoStops_with_time.put(8, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(12, 2);
        decoStops.put(9, 2);
        decoStops.put(6, 5);
        decoStops.put(3, 13);
        decoStops_with_time.put(10, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(15, 1);
        decoStops.put(12, 2);
        decoStops.put(9, 3);
        decoStops.put(6, 6);
        decoStops.put(3, 15);
        decoStops_with_time.put(11, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(15, 1);
        decoStops.put(12, 2);
        decoStops.put(9, 4);
        decoStops.put(6, 7);
        decoStops.put(3, 17);
        decoStops_with_time.put(12, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(15, 2);
        decoStops.put(12, 2);
        decoStops.put(9, 5);
        decoStops.put(6, 8);
        decoStops.put(3, 20);
        decoStops_with_time.put(13, decoStops);
        dive_depth.put(63, decoStops_with_time);

        // generate the zero hours
        zeroHours.put(12, 112);
        zeroHours.put(15, 60);
        zeroHours.put(18, 38);
        zeroHours.put(21, 27);
        zeroHours.put(24, 20);
        zeroHours.put(27, 16);
        zeroHours.put(30, 13);
        zeroHours.put(33, 11);
        zeroHours.put(36, 9);
        zeroHours.put(39, 8);
        zeroHours.put(42, 7);
        zeroHours.put(45, 6);
        zeroHours.put(48, 5);
        zeroHours.put(51, 4);
        zeroHours.put(54, 3);
        zeroHours.put(57, 3);
        zeroHours.put(60, 2);
        zeroHours.put(63, 2);

        // generate the repeat group 12 Meters
        repeat_group.put(36, "D");
        repeat_group.put(54, "E");
        repeat_group.put(72, "F");
        repeat_group.put(90, "G");
        repeat_group.put(108, "G");
        repeat_groups.put(12, repeat_group);

        // generate the repeat group 15 Meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(24, "D");
        repeat_group.put(36, "E");
        repeat_group.put(48, "E");
        repeat_group.put(60, "F");
        repeat_group.put(72, "G");
        repeat_group.put(84, "G");
        repeat_groups.put(15, repeat_group);

        // generate the repeat group 18 Meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(15, "C");
        repeat_group.put(25, "D");
        repeat_group.put(35, "E");
        repeat_group.put(45, "F");
        repeat_group.put(55, "F");
        repeat_group.put(65, "G");
        repeat_group.put(75, "G");
        repeat_groups.put(18, repeat_group);

        // generate the repeat group 21 Meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(11, "C");
        repeat_group.put(16, "D");
        repeat_group.put(21, "D");
        repeat_group.put(26, "E");
        repeat_group.put(31, "E");
        repeat_group.put(36, "F");
        repeat_group.put(41, "F");
        repeat_group.put(46, "F");
        repeat_group.put(51, "G");
        repeat_group.put(56, "G");
        repeat_group.put(61, "G");
        repeat_groups.put(21, repeat_group);

        // generate the repeat group 24 Meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(7, "B");
        repeat_group.put(11, "C");
        repeat_group.put(15, "D");
        repeat_group.put(19, "D");
        repeat_group.put(23, "E");
        repeat_group.put(27, "E");
        repeat_group.put(31, "F");
        repeat_group.put(35, "F");
        repeat_group.put(39, "F");
        repeat_group.put(43, "G");
        repeat_group.put(47, "G");
        repeat_group.put(51, "G");
        repeat_group.put(55, "G");
        repeat_groups.put(24, repeat_group);

        // generate the repeat group 27 Meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(6, "B");
        repeat_group.put(10, "C");
        repeat_group.put(14, "D");
        repeat_group.put(18, "E");
        repeat_group.put(22, "E");
        repeat_group.put(26, "F");
        repeat_group.put(30, "F");
        repeat_group.put(34, "F");
        repeat_group.put(38, "G");
        repeat_group.put(42, "G");
        repeat_group.put(46, "G");
        repeat_group.put(50, "G");
        repeat_groups.put(27, repeat_group);

        // generate the repeat group 30 Meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(6, "B");
        repeat_group.put(9, "C");
        repeat_group.put(12, "D");
        repeat_group.put(15, "D");
        repeat_group.put(18, "E");
        repeat_group.put(21, "E");
        repeat_group.put(24, "F");
        repeat_group.put(27, "F");
        repeat_group.put(30, "F");
        repeat_group.put(33, "G");
        repeat_group.put(36, "G");
        repeat_group.put(39, "G");
        repeat_group.put(42, "G");
        repeat_groups.put(30, repeat_group);

        // generate the repeat group 33 Meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(6, "C");
        repeat_group.put(9, "D");
        repeat_group.put(12, "D");
        repeat_group.put(15, "E");
        repeat_group.put(18, "E");
        repeat_group.put(21, "F");
        repeat_group.put(24, "F");
        repeat_group.put(27, "F");
        repeat_group.put(30, "G");
        repeat_group.put(33, "G");
        repeat_group.put(36, "G");
        repeat_groups.put(33, repeat_group);

        // generate the repeat group 36 Meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(6, "C");
        repeat_group.put(10, "D");
        repeat_group.put(14, "E");
        repeat_group.put(18, "F");
        repeat_group.put(21, "F");
        repeat_group.put(24, "F");
        repeat_group.put(27, "G");
        repeat_group.put(30, "G");
        repeat_group.put(33, "G");
        repeat_groups.put(36, repeat_group);

        // generate the repeat group 39 Meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(6, "C");
        repeat_group.put(9, "D");
        repeat_group.put(12, "E");
        repeat_group.put(15, "E");
        repeat_group.put(18, "F");
        repeat_group.put(21, "F");
        repeat_group.put(24, "G");
        repeat_group.put(27, "G");
        repeat_groups.put(39, repeat_group);

        // generate the repeat group 42 Meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(4, "C");
        repeat_group.put(7, "D");
        repeat_group.put(10, "E");
        repeat_group.put(13, "E");
        repeat_group.put(16, "F");
        repeat_group.put(19, "F");
        repeat_group.put(22, "G");
        repeat_group.put(25, "G");
        repeat_groups.put(42, repeat_group);

        // generate the repeat group 45 Meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(6, "D");
        repeat_group.put(8, "D");
        repeat_group.put(10, "E");
        repeat_group.put(12, "E");
        repeat_group.put(14, "F");
        repeat_group.put(16, "F");
        repeat_group.put(18, "F");
        repeat_group.put(20, "G");
        repeat_group.put(22, "G");
        repeat_groups.put(45, repeat_group);

        // generate the repeat group 48 Meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(5, "C");
        repeat_group.put(7, "D");
        repeat_group.put(9, "E");
        repeat_group.put(11, "E");
        repeat_group.put(13, "F");
        repeat_group.put(15, "F");
        repeat_group.put(17, "F");
        repeat_group.put(18, "G");
        repeat_group.put(21, "G");
        repeat_groups.put(48, repeat_group);

        // generate the repeat group 51 Meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(6, "D");
        repeat_group.put(8, "E");
        repeat_group.put(10, "E");
        repeat_group.put(12, "F");
        repeat_group.put(14, "F");
        repeat_group.put(16, "F");
        repeat_group.put(18, "G");
        repeat_groups.put(51, repeat_group);

        // generate the repeat group 54 Meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(6, "D");
        repeat_group.put(8, "E");
        repeat_group.put(10, "E");
        repeat_group.put(12, "F");
        repeat_group.put(14, "F");
        repeat_group.put(16, "G");
        repeat_groups.put(54, repeat_group);

        // generate the repeat group 57 Meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(6, "D");
        repeat_group.put(8, "E");
        repeat_group.put(10, "F");
        repeat_group.put(12, "F");
        repeat_group.put(14, "F");
        repeat_group.put(16, "G");
        repeat_groups.put(57, repeat_group);

        // generate the repeat group 60 Meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(6, "E");
        repeat_group.put(8, "E");
        repeat_group.put(10, "F");
        repeat_group.put(12, "F");
        repeat_group.put(13, "F");
        repeat_group.put(14, "G");
        repeat_groups.put(60, repeat_group);

        // generate the repeat group 63 Meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(6, "E");
        repeat_group.put(8, "E");
        repeat_group.put(10, "F");
        repeat_group.put(11, "F");
        repeat_group.put(12, "F");
        repeat_group.put(13, "G");
        repeat_groups.put(63, repeat_group);

        // generate the no flight time
        no_flight.put("G", 36);
        no_flight.put("F", 30);
        no_flight.put("E", 24);
        no_flight.put("D", 18);
        no_flight.put("C", 12);
        no_flight.put("B", 6);

        // generate the add on time for repeat dive
        Integer[] surface_time = new Integer[] { 120, 180, 240, 300, 360, 420, 480, 540, 600, 720 };
        surface_duration.put("G", surface_time);
        surface_time = new Integer[] { 30, 60, 90, 145, 180, 225, 270, 330, 390, 600 };
        surface_duration.put("F", surface_time);
        surface_time = new Integer[] { 0, 0, 30, 60, 90, 120, 150, 180, 210, 480 };
        surface_duration.put("E", surface_time);
        surface_time = new Integer[] { 0, 0, 0, 0, 30, 45, 60, 90, 120, 360 };
        surface_duration.put("D", surface_time);
        surface_time = new Integer[] { 0, 0, 0, 0, 0, 0, 10, 20, 30, 240 };
        surface_duration.put("C", surface_time);
        surface_time = new Integer[] { 0, 0, 0, 0, 0, 0, 0, 10, 20, 120 };
        surface_duration.put("B", surface_time);

        // generate the time quantity
        Integer[] add_on_times = new Integer[] { 0, 66, 60, 54, 47, 35, 30, 25, 20 };
        add_on_time.put(12, add_on_times);
        add_on_times = new Integer[] { 0, 52, 47, 42, 37, 32, 27, 23, 19, 16 };
        add_on_time.put(15, add_on_times);
        add_on_times = new Integer[] { 0, 43, 39, 34, 30, 26, 22, 19, 16, 13 };
        add_on_time.put(18, add_on_times);
        add_on_times = new Integer[] { 0, 36, 33, 29, 26, 22, 19, 16, 13, 11 };
        add_on_time.put(21, add_on_times);
        add_on_times = new Integer[] { 0, 31, 28, 25, 22, 19, 16, 14, 12, 10 };
        add_on_time.put(24, add_on_times);
        add_on_times = new Integer[] { 0, 27, 25, 22, 19, 17, 14, 12, 10, 8 };
        add_on_time.put(27, add_on_times);
        add_on_times = new Integer[] { 0, 24, 22, 20, 17, 15, 13, 11, 9, 8 };
        add_on_time.put(30, add_on_times);
        add_on_times = new Integer[] { 0, 22, 20, 18, 16, 14, 12, 10, 8, 7 };
        add_on_time.put(33, add_on_times);
        add_on_times = new Integer[] { 0, 20, 18, 16, 14, 12, 11, 9, 7, 6 };
        add_on_time.put(36, add_on_times);
        add_on_times = new Integer[] { 0, 18, 17, 15, 13, 11, 10, 8, 7, 6 };
        add_on_time.put(39, add_on_times);
        add_on_times = new Integer[] { 0, 17, 15, 14, 12, 10, 9, 8, 6, 5 };
        add_on_time.put(42, add_on_times);
        add_on_times = new Integer[] { 0, 16, 14, 13, 11, 10, 8, 7, 6, 5 };
        add_on_time.put(45, add_on_times);
        add_on_times = new Integer[] { 0, 15, 13, 12, 10, 9, 8, 6, 5, 4 };
        add_on_time.put(48, add_on_times);
        add_on_times = new Integer[] { 0, 14, 12, 11, 10, 8, 7, 6, 5, 4 };
        add_on_time.put(51, add_on_times);
        add_on_times = new Integer[] { 0, 13, 12, 10, 9, 8, 7, 6, 5, 4 };
        add_on_time.put(54, add_on_times);
        add_on_times = new Integer[] { 0, 12, 11, 10, 9, 7, 6, 5, 5, 4 };
        add_on_time.put(57, add_on_times);
        add_on_times = new Integer[] { 0, 11, 10, 9, 8, 7, 6, 5, 4, 4 };
        add_on_time.put(60, add_on_times);
        add_on_times = new Integer[] { 0, 11, 10, 9, 8, 7, 6, 5, 4, 3 };
        add_on_time.put(63, add_on_times);
    }

    /**
     * Get the time without any stop
     * @return the no stop time
     */
    public Integer getZeroHour() {
        return zeroHours.get(calculated_depth_higher);
    }

    /**
     * Calculate the higher depth for the decompression calculation
     *
     */
    private void setCalculated_depth_higher() {
        if (depth != null) {
            // find the next higher depth
            calculated_depth_higher = new Double(Math.ceil(NitroxCalculationUtil.calculateEAD(depth.doubleValue(), o2_ratio))).intValue();
            while (dive_depth.get(calculated_depth_higher) == null && calculated_depth_higher <= 63) {
                calculated_depth_higher++;
            }
        }
    }

    /**
     * Calculate the lower depth for the add on time calculation
     *
     */
    private void setCalculated_depth_lower() {
        if (depth != null) {
            // find the next higher depth
            calculated_depth_lower = new Double(Math.ceil(NitroxCalculationUtil.calculateEAD(depth.doubleValue(), o2_ratio))).intValue();
            while (dive_depth.get(calculated_depth_lower) == null && calculated_depth_lower >= 12) {
                calculated_depth_lower--;
            }
        }
    }

    /**
     * Calculate the duration for the decompression calculation with the correct
     * depth
     */
    private void setCalculated_duration() {
        if (calculated_depth_higher != null && duration != null) {
            calculated_duration = duration;
            // dive is in zero time, return null
            if (calculated_duration > getZeroHour()) {
                // find the next higher depth and check, if the duration is too
                // long
                while (dive_depth.get(calculated_depth_higher).get(calculated_duration) == null && calculated_duration < 999) {
                    calculated_duration++;
                }
            }
        }
    }

    /**
     * set the global depth and duration
     */
    public void initialize(Integer depth, Integer duration, String repeat_group, Integer surface_interval, Double o2_ratio, Integer down_dive_speed) {
        // set the global depth, calculated_depth
        setDepth(depth);
        setDown_dive_speed(down_dive_speed);
        
        this.o2_ratio = o2_ratio;
        setCalculated_depth_higher();
        setCalculated_depth_lower();

        // recalculate the add on time
        setAdd_dive_time(0);
        if (!repeat_group.trim().equals("") && surface_interval != 0) {
            setAdd_dive_time(getAdd_on_time(repeat_group, surface_interval));
        }

        setDuration(duration + add_dive_time + new Integer(calculated_depth_higher / down_dive_speed));
        setCalculated_duration();
        if (calculated_duration == 999) {
            calculated_duration = getZeroHour();
        }
    }

    /**
     * Get the majoration time
     * @param repeating_group: the group after the first dive
     * @param surface_interval: Time between dives
     * @return
     */
    public Integer getAdd_on_time(String repeating_group, Integer surface_interval) {
        Integer[] surface_time = surface_duration.get(repeating_group);
        int index = 0;
        if (surface_time != null) {
            for (int i = 0; i < 10; i++) {
                if (surface_time[i] >= surface_interval) {
                    index = i;
                    break;
                }
            }
        }
        Integer[] add_on_time_array = add_on_time.get(calculated_depth_lower);
        if (add_on_time_array != null) {
            return add_on_time_array[index];
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.jdivelog.model.decompression.DecompressionTable#getDecompressionStops()
     */
    public HashMap<Integer, Integer> getDecompressionStops() {
        return dive_depth.get(calculated_depth_higher).get(calculated_duration);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.jdivelog.model.decompression.DecompressionTable#getSurfaceInterval()
     */
    public Integer getSurfaceInterval() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.jdivelog.model.decompression.DecompressionTable#getNoFlightDuration()
     */
    public Integer getNoFlightDuration() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.jdivelog.model.decompression.DecompressionTable#getRepeatGroup()
     */
    public String getRepeatGroup() {
        return repeat_groups.get(calculated_depth_higher).get(calculated_duration);
    }


    /**
     * ConfigurationPanel for Deco2000
     * @return the configuration panel
     */
    public Deco2000_ConfigurationPanel getConfigurationPanel(MainWindow mainWindow) {
        if (configurationPanel == null) {
            configurationPanel = new Deco2000_ConfigurationPanel(mainWindow);
        }
        return configurationPanel;
    }

    
    private static class Deco2000_ConfigurationPanel extends JPanel implements ActionListener {

        private static final long serialVersionUID = 3832621798402569012L;

        private JTextField max_depth = null;

        private JTextField duration = null;

        private JTextField up_dive_speed = null;

        private JTextField down_dive_speed = null;

        private JTextField o2_ratio = null;

        private JTextField amvField = null;

        private JTextArea dive_data = null;

        private JButton calculateButton = null;

        private Deco2000_1500_surface deco2000_1500_surface = null;

        private JTextField surface_time = null;

        private JComboBox repeating_groups = null;

        private static final String[] REPEATING_GROUPS = { "", "B", "C", "D", "E", "F", "G" };

        private JFreeChart diveprofile = null;

        private JPanel diveprofilePanel = null;

        private MainWindow mainWindow = null;

        private Double airconsumption = null;

        private Double amv = null;

        /**
         * Constructor 
         * @param mainWindow: The main window where to include the panel
         */
        public Deco2000_ConfigurationPanel(MainWindow mainWindow) {

            this.mainWindow = mainWindow;

            deco2000_1500_surface = new Deco2000_1500_surface();

            JLabel labelMax_depth = new JLabel(Messages.getString("diveplanning.max_depth") + " [" + UnitConverter.getDisplayAltitudeUnit() + "]");
            JLabel labelDuration = new JLabel(Messages.getString("diveplanning.duration") + " [" + UnitConverter.getDisplayTimeUnit() + "]");
            JLabel labelDive_data = new JLabel(Messages.getString("diveplanning.dive_data"));
            JLabel labelRepeating_group = new JLabel(Messages.getString("diveplanning.repeating_group"));
            JLabel labelSurface_time = new JLabel(Messages.getString("diveplanning.surface_time") + " [" + UnitConverter.getDisplayTimeUnit() + "]");
            JLabel labelUp_dive_speed = new JLabel(Messages.getString("diveplanning.up_dive_speed") + " [" + UnitConverter.getDisplaySpeedUnit() + "]");
            JLabel labelDown_dive_speed = new JLabel(Messages.getString("diveplanning.down_dive_speed") + " [" + UnitConverter.getDisplaySpeedUnit() + "]");
            JLabel labelO2_ration = new JLabel(Messages.getString("diveplanning.o2_ration") + " [%]");
            JLabel labelAmv = new JLabel(Messages.getString("diveplanning.amv") + " [" + UnitConverter.getDisplayAMVUnit() + "]");

            this.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.weightx = 0;
            gc.anchor = GridBagConstraints.NORTH;

            gc.gridy = 0;
            gc.gridx = 0;
            gc.insets = new java.awt.Insets(2, 2, 2, 2);
            this.add(labelMax_depth, gc);
            gc.gridy = 1;
            this.add(getMax_depth(), gc);

            gc.gridy = 2;
            gc.gridx = 0;
            this.add(labelDuration, gc);
            gc.gridy = 3;
            this.add(getDuration(), gc);

            gc.gridy = 2;
            gc.gridx = 3;
            this.add(labelRepeating_group, gc);
            gc.gridy = 3;
            this.add(getRepeating_groups(), gc);

            gc.gridy = 2;
            gc.gridx = 1;
            this.add(labelDown_dive_speed, gc);
            gc.gridy = 3;
            this.add(getDown_dive_speed(), gc);

            gc.gridy = 2;
            gc.gridx = 2;
            this.add(labelO2_ration, gc);
            gc.gridy = 3;
            this.add(getO2_ratio(), gc);

            gc.gridy = 0;
            gc.gridx = 3;
            this.add(labelSurface_time, gc);
            gc.gridy = 1;
            this.add(getSurface_time(), gc);

            gc.gridy = 0;
            gc.gridx = 1;
            this.add(labelUp_dive_speed, gc);
            gc.gridy = 1;
            this.add(getUp_dive_speed(), gc);

            gc.gridy = 0;
            gc.gridx = 2;
            this.add(labelAmv, gc);
            gc.gridy = 1;
            this.add(getAmv(), gc);

            gc.gridwidth = 4;
            gc.gridy = 4;
            gc.gridx = 0;
            this.add(labelDive_data, gc);
            gc.gridy = 5;
            this.add(getDive_data(), gc);

            gc.gridy = 6;
            gc.gridx = 0;
            this.add(getCalculateButton(), gc);

            gc.gridy = 7;
            gc.gridx = 0;
            JLabel l = new JLabel(Messages.getString("diveplanning.nowarranty"));
            l.setForeground(Color.RED);
            this.add(l,gc);

            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.gridy = 8;
            gc.gridx = 0;
            this.add(getDiveProfilePanel(), gc);
               
        }

        /**
         * The maximum depth 
         * @return JTextField containing the maximum depth
         */
        private JTextField getMax_depth() {
            if (max_depth == null) {
                max_depth = new JTextField();
            }
            return max_depth;
        }

        /**
         * Get O2 %
         * @return the JTextField containing the 02 information
         */
        private JTextField getO2_ratio() {
            if (o2_ratio == null) {
                o2_ratio = new JTextField();
                o2_ratio.setText(O2_PART);
            }
            return o2_ratio;
        }

        /**
         * The air consumption
         * @return the JTextField containing the information
         */
        private JTextField getAmv() {
            if (amvField == null) {
                amvField = new JTextField();
                amvField.setText(mainWindow.getLogBook().getAverageAmv().toString());
            }
            return amvField;
        }

        
        /**
         * The speed from bottom to surface
         * @return JTextField containing the speed
         */
        private JTextField getUp_dive_speed() {
            if (up_dive_speed == null) {
                up_dive_speed = new JTextField();
                up_dive_speed.setText(UP_DIVE_SPEED);
            }
            return up_dive_speed;
        }

        /**
         * The speed from surface to bottom
         * @return the JTextField containing the speed
         */
        private JTextField getDown_dive_speed() {
            if (down_dive_speed == null) {
                down_dive_speed = new JTextField();
                down_dive_speed.setText(DOWN_DIVE_SPEED);
            }
            return down_dive_speed;
        }

        /**
         * The dive time
         * @return the JTextField containing the duration
         */
        private JTextField getDuration() {
            if (duration == null) {
                duration = new JTextField();
            }
            return duration;
        }

        
        /**
         * The dive data
         * @return JTextArea with the dive data
         */
        private JTextArea getDive_data() {
            if (dive_data == null) {
                dive_data = new JTextArea(15, 2);
            }
            return dive_data;
        }

        
        /**
         * Button to calculate the simulation
         * @return the JButton in order to launch to calculate
         */
        private JButton getCalculateButton() {
            if (calculateButton == null) {
                calculateButton = new JButton();
                calculateButton.setText(Messages.getString("diveplanning.calculate")); //$NON-NLS-1$
                calculateButton.addActionListener(this);
            }
            return calculateButton;
        }
        
        /**
         * The surf time between dives
         * @return the JTextField containing the surface time
         */
        private JTextField getSurface_time() {
            if (surface_time == null) {
                surface_time = new JTextField();
            }
            return surface_time;
        }

        /**
         * Repeating group after the first dive
         * @return the JComboBox for the repeating group
         */
        private JComboBox getRepeating_groups() {
            if (repeating_groups == null) {
                repeating_groups = new JComboBox(REPEATING_GROUPS);
            }
            return repeating_groups;
        }

        
        /**
         * Panel containing the profile 
         * @return the JPanel for the profile
         */
        private JPanel getDiveProfilePanel() {
            if (diveprofilePanel == null) {
                diveprofilePanel = new JPanel();
                diveprofilePanel.add(getChartPanel(getDivedata_chart(null)));
            }
            return diveprofilePanel;
        }

        /**
         * The chart pannel
         * @param chart
         * @return
         */
        private ChartPanel getChartPanel(JFreeChart chart) {
            ChartPanel chartPanel = new ChartPanel(chart);
            return chartPanel;
        }

        /**
         * CHart for the drawing of the profile
         * @param data_xy
         * @return
         */
        private JFreeChart getDivedata_chart(XYSeriesCollection data_xy) {
            diveprofile = ChartFactory.createXYLineChart(Messages.getString("diveplanning.diveprofile"), Messages.getString("diveplanning.time") + "["
                    + UnitConverter.getDisplayTimeUnit() + "]", Messages.getString("diveplanning.depth") + "[" + UnitConverter.getDisplayAltitudeUnit() + "]",
                    data_xy, PlotOrientation.VERTICAL, false, false, false);
            XYPlot plot = diveprofile.getXYPlot();
            plot.setBackgroundPaint(Color.lightGray);
            return diveprofile;
        }

        /**
         * Collection of points for the profile (x,y)
         * @return the collection of points for the profile
         */
        private XYSeriesCollection diveProfile_data() {
            Double temp = new Double(0);
            Double temp_up_dive_time = new Double(0);
            XYSeries series = new XYSeries(Messages.getString("diveplanning.diveprofile"));

            // begin of the dive profil
            series.add(0, 0);
            // down dive
            temp = new Double(deco2000_1500_surface.depth) / new Double(down_dive_speed.getText());
            series.add(temp.doubleValue(), -1 * deco2000_1500_surface.depth);
            airconsumption = amv * temp.doubleValue() * ((deco2000_1500_surface.depth / 10) + 1);
            // dive time at the ground
            temp += deco2000_1500_surface.duration;
            series.add(temp.doubleValue(), -1 * deco2000_1500_surface.depth);
            airconsumption += amv * temp.doubleValue() * ((deco2000_1500_surface.depth / 10) + 1);
            // up dive time with stops
            temp_up_dive_time -= temp;
            if (deco2000_1500_surface.getDecompressionStops() != null) {
                if (deco2000_1500_surface.getDecompressionStops().get(15) != null) {
                    temp += (new Double(deco2000_1500_surface.depth - 15) / new Double(up_dive_speed.getText()));
                    series.add(temp.doubleValue(), -15);
                    airconsumption += amv * (new Double(deco2000_1500_surface.depth - 15) / new Double(up_dive_speed.getText())) * 2.5;
                    temp += deco2000_1500_surface.getDecompressionStops().get(15);
                    series.add(temp.doubleValue(), -15);
                    airconsumption += amv * deco2000_1500_surface.getDecompressionStops().get(15) * 2.5;
                }
                if (deco2000_1500_surface.getDecompressionStops().get(12) != null) {
                    if (deco2000_1500_surface.getDecompressionStops().get(15) == null) {
                        temp += (new Double(deco2000_1500_surface.depth - 12) / new Double(up_dive_speed.getText()));
                        airconsumption += amv * (new Double(deco2000_1500_surface.depth - 12) / new Double(up_dive_speed.getText())) * 2.5;
                    } else {
                        temp += (new Double(3) / new Double(up_dive_speed.getText()));
                        airconsumption += amv * (new Double(3) / new Double(up_dive_speed.getText())) * 2.5;
                    }
                    series.add(temp.doubleValue(), -12);
                    temp += deco2000_1500_surface.getDecompressionStops().get(12);
                    series.add(temp.doubleValue(), -12);
                    airconsumption += amv * deco2000_1500_surface.getDecompressionStops().get(12) * 2.2;
                }
                if (deco2000_1500_surface.getDecompressionStops().get(9) != null) {
                    if (deco2000_1500_surface.getDecompressionStops().get(12) == null) {
                        temp += (new Double(deco2000_1500_surface.depth - 9) / new Double(up_dive_speed.getText()));
                        airconsumption += amv * (new Double(deco2000_1500_surface.depth - 9) / new Double(up_dive_speed.getText())) * 1.9;
                    } else {
                        temp += (new Double(3) / new Double(up_dive_speed.getText()));
                        airconsumption += amv * (new Double(3) / new Double(up_dive_speed.getText())) * 1.9;
                    }
                    series.add(temp.doubleValue(), -9);
                    temp += deco2000_1500_surface.getDecompressionStops().get(9);
                    series.add(temp.doubleValue(), -9);
                    airconsumption += amv * deco2000_1500_surface.getDecompressionStops().get(9) * 1.9;
                }
                if (deco2000_1500_surface.getDecompressionStops().get(6) != null) {
                    if (deco2000_1500_surface.getDecompressionStops().get(9) == null) {
                        temp += (new Double(deco2000_1500_surface.depth - 6) / new Double(up_dive_speed.getText()));
                        airconsumption += amv * (new Double(deco2000_1500_surface.depth - 6) / new Double(up_dive_speed.getText())) * 1.6;
                    } else {
                        temp += (new Double(3) / new Double(up_dive_speed.getText()));
                        airconsumption += amv * (new Double(3) / new Double(up_dive_speed.getText())) * 1.6;
                    }
                    series.add(temp.doubleValue(), -6);
                    temp += deco2000_1500_surface.getDecompressionStops().get(6);
                    series.add(temp.doubleValue(), -6);
                    airconsumption += amv * deco2000_1500_surface.getDecompressionStops().get(6) * 1.6;
                }
                if (deco2000_1500_surface.getDecompressionStops().get(3) != null) {
                    if (deco2000_1500_surface.getDecompressionStops().get(6) == null) {
                        temp += (new Double(deco2000_1500_surface.depth - 3) / new Double(up_dive_speed.getText()));
                        airconsumption += amv * (new Double(deco2000_1500_surface.depth - 3) / new Double(up_dive_speed.getText())) * 1.3;
                    } else {
                        temp += (new Double(3) / new Double(up_dive_speed.getText()));
                        airconsumption += amv * (new Double(3) / new Double(up_dive_speed.getText())) * 1.3;
                    }
                    series.add(temp.doubleValue(), -3);
                    temp += deco2000_1500_surface.getDecompressionStops().get(3);
                    series.add(temp.doubleValue(), -3);
                    airconsumption += amv * deco2000_1500_surface.getDecompressionStops().get(3) * 1.3;
                }
                // the surface
                temp += (new Double(3) / new Double(up_dive_speed.getText()));
                airconsumption += amv * (new Double(3) / new Double(up_dive_speed.getText())) * 1.3;
            } else {
                temp += (new Double(deco2000_1500_surface.depth) / new Double(up_dive_speed.getText()));
                airconsumption += amv * (new Double(deco2000_1500_surface.depth) / new Double(up_dive_speed.getText())) * 1.3;
            }
            temp_up_dive_time += temp;
            series.add(temp.doubleValue(), 0);

            // set the complete_duration and up dive duration
            deco2000_1500_surface.setComplete_duration(temp);
            deco2000_1500_surface.setComplete_up_dive_duration(temp_up_dive_time);

            XYSeriesCollection data_xy = new XYSeriesCollection(series);
            return data_xy;
        }

        /**
         * The tank volume needed for the dive
         * @return the volume
         */
        private Integer getTankVolume() {
            if (airconsumption <= 1400) {
                return 7;
            } else if (airconsumption <= 1600) {
                return 8;
            } else if (airconsumption <= 2000) {
                return 10;
            } else if (airconsumption <= 2400) {
                return 12;
            } else if (airconsumption <= 2800) {
                return 14;
            } else if (airconsumption <= 3000) {
                return 15;
            } else if (airconsumption <= 3200) {
                return 16;
            } else if (airconsumption <= 4000) {
                return 20;
            }
            return null;
        }

        /**
         * Set the dive profile
         *
         */
        private void setDiveProfile() {
            diveprofilePanel.removeAll();
            diveprofilePanel.add(getChartPanel(getDivedata_chart(diveProfile_data())));
        }

        /**
         * Actions for the event
         * @param: ActionEvent e, the action which raised the action 
         */
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == calculateButton) {
                String divedata = new String();
                amv = new Double(amvField.getText());

                // calculate the depth and the duration or this table
                if (repeating_groups.getSelectedIndex() != -1 && !surface_time.getText().trim().equals("")) {
                    deco2000_1500_surface.initialize(new Integer(getMax_depth().getText()), new Integer(getDuration().getText()), (String) repeating_groups
                            .getSelectedItem(), new Integer(surface_time.getText()), new Double(o2_ratio.getText()), new Integer(down_dive_speed.getText()));
                } else {
                    deco2000_1500_surface.initialize(new Integer(getMax_depth().getText()), new Integer(getDuration().getText()), "", 0, new Double(o2_ratio.getText()), new Integer(down_dive_speed.getText()));
                }

                if (repeating_groups.getSelectedIndex() != -1 && !surface_time.getText().trim().equals("")) {
                    divedata += Messages.getString("diveplanning.repeat_dive") + "\n";
                    divedata += Messages.getString("diveplanning.add_on_time") + ": \t" + deco2000_1500_surface.getAdd_dive_time() + "\n";
                    divedata += "======================================================================================\n";
                }

                // set the dive profile
                setDiveProfile();
                // calculate cns
                Double ppo = NitroxCalculationUtil.calculatePPO(deco2000_1500_surface.getDepth().doubleValue(), new Double(o2_ratio.getText()));
                Double singleCNS = null;
                Double dailyCNS = null;
                if (ppo <= 1.6) {
                    singleCNS = NitroxCalculationUtil.getSingleDiveCNS(ppo, deco2000_1500_surface.getDuration().doubleValue());
                    dailyCNS = NitroxCalculationUtil.getDailyCNS(ppo, deco2000_1500_surface.getDuration().doubleValue());
                    if (deco2000_1500_surface.getDecompressionStops() != null) {
                        Iterator<Integer> it = deco2000_1500_surface.getDecompressionStops().keySet().iterator();
                        while (it.hasNext()) {
                            Integer depth = it.next();
                            Integer duration = deco2000_1500_surface.getDecompressionStops().get(depth);
                            ppo = NitroxCalculationUtil.calculatePPO(depth.doubleValue(), new Double(o2_ratio.getText()));
                            singleCNS += NitroxCalculationUtil.getSingleDiveCNS(ppo, duration.doubleValue());
                            dailyCNS += NitroxCalculationUtil.getDailyCNS(ppo, duration.doubleValue());
                        }
                    }
                }
                // Zero time for the dive
                divedata += Messages.getString("diveplanning.zerohour") + ": \t" + deco2000_1500_surface.getZeroHour() + " ";
                divedata += Messages.getString("minutes") + "\t";
                divedata += Messages.getString("diveplanning.repeating_group") + ": \t" + (deco2000_1500_surface.getRepeatGroup()==null?"":deco2000_1500_surface.getRepeatGroup()) + "\t";
                divedata += Messages.getString("diveplanning.no_flight") + ": \t" + (deco2000_1500_surface.no_flight.get(deco2000_1500_surface.getRepeatGroup())==null?"":deco2000_1500_surface.no_flight.get(deco2000_1500_surface.getRepeatGroup())
                        + " [h]") + "\n";
                divedata += Messages.getString("diveplanning.complete_duration") + ": \t" + Math.round(deco2000_1500_surface.getComplete_duration()) + " ["
                        + UnitConverter.getDisplayTimeUnit() + "]\t";
                divedata += Messages.getString("diveplanning.complete_up_dive_duration") + ": \t\t"
                        + Math.round(deco2000_1500_surface.getComplete_up_dive_duration()) + " [" + UnitConverter.getDisplayTimeUnit() + "]\n";
                divedata += Messages.getString("diveplanning.air_consumption") + ": \t" + Math.round(airconsumption) + " [bar l]\t"
                        + Messages.getString("diveplanning.tank_volume") + ": \t" + this.getTankVolume() + " [" + UnitConverter.getDisplayVolumeUnit() + "]\n";
                divedata += Messages.getString("diveplanning.ead") + ": \t" + Math.round(NitroxCalculationUtil.calculateEAD(deco2000_1500_surface.getDepth().doubleValue(), new Double(o2_ratio.getText()))) + " ["
                        + UnitConverter.getDisplayAltitudeUnit() + "]\n";
                if (singleCNS != null && dailyCNS != null) {
                    divedata += Messages.getString("diveplanning.single_cns") + ": \t" + CNS_FORMAT.format(singleCNS) + "%\t\t"
                            + Messages.getString("diveplanning.daily_cns") + ": \t" + CNS_FORMAT.format(dailyCNS) + "%\n";
                } else {
                    divedata += Messages.getString("diveplanning.cns_warning") + "\n";
                }
                divedata += "======================================================================================\n";
                // the duration is to long
                if (deco2000_1500_surface.getDecompressionStops() == null
                        && (deco2000_1500_surface.getZeroHour() == null || deco2000_1500_surface.getZeroHour() < new Integer(getDuration().getText()))
                        || deco2000_1500_surface.getDecompressionStops() != null
                        && (deco2000_1500_surface.getDecompressionStops().get(3) == null && deco2000_1500_surface.getZeroHour() < new Integer(getDuration()
                                .getText()))) {
                    new MessageDialog(mainWindow, Messages.getString("diveplanning"), Messages.getString("diveplanning.to_long_duration"), null, MessageDialog.MessageType.ERROR);
                    this.repaint();
                } else {
                    // decompression stops for the dive
                    if (deco2000_1500_surface.getDecompressionStops() != null && deco2000_1500_surface.getDecompressionStops().get(3) != null) {
                        divedata += Messages.getString("diveplanning.duration_on_3m") + ": \t";
                        divedata += deco2000_1500_surface.getDecompressionStops().get(3) + " ";
                        divedata += Messages.getString("minutes") + "\n";
                    }
                    if (deco2000_1500_surface.getDecompressionStops() != null && deco2000_1500_surface.getDecompressionStops().get(6) != null) {
                        divedata += Messages.getString("diveplanning.duration_on_6m") + ": \t";
                        divedata += deco2000_1500_surface.getDecompressionStops().get(6) + " ";
                        divedata += Messages.getString("minutes") + "\n";
                    }
                    if (deco2000_1500_surface.getDecompressionStops() != null && deco2000_1500_surface.getDecompressionStops().get(9) != null) {
                        divedata += Messages.getString("diveplanning.duration_on_9m") + ": \t";
                        divedata += deco2000_1500_surface.getDecompressionStops().get(9) + " ";
                        divedata += Messages.getString("minutes") + "\n";
                    }
                    if (deco2000_1500_surface.getDecompressionStops() != null && deco2000_1500_surface.getDecompressionStops().get(12) != null) {
                        divedata += Messages.getString("diveplanning.duration_on_12m") + ": \t";
                        divedata += deco2000_1500_surface.getDecompressionStops().get(12) + " ";
                        divedata += Messages.getString("minutes") + "\n";
                    }
                    if (deco2000_1500_surface.getDecompressionStops() != null && deco2000_1500_surface.getDecompressionStops().get(15) != null) {
                        divedata += Messages.getString("diveplanning.duration_on_15m") + ": \t";
                        divedata += deco2000_1500_surface.getDecompressionStops().get(15) + " ";
                        divedata += Messages.getString("minutes") + "\n";
                    }
                    dive_data.setText(divedata);
                }
            }
        }

    }

    /**
     * The bottom time
     * @return the dive time
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * Set the duration of the dive
     * @param duration: the bottom time
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * The depth of the dive
     * @return the depth of the dive
     */
    public Integer getDepth() {
        return depth;
    }
    
    /**
     * The down dive speed
     * @return the depth
     */
    public Integer getDown_dive_speed() {
        return down_dive_speed;
    }

    /**
     * Set the depth
     * @param depth
     */
    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    /**
     * Set the down dive speed
     * @param depth: the depth
     */
    public void setDown_dive_speed(Integer down_dive_speed) {
        this.down_dive_speed = down_dive_speed;
    }

    /**
     * The complete dive duration
     * @return the duration of the dive from surface to surface
     */
    public Double getComplete_duration() {
        return complete_duration;
    }

    /**
     * Set the complete time from surface to surface
     * @param complete_duration the complete dive time
     */
    public void setComplete_duration(Double complete_duration) {
        this.complete_duration = complete_duration;
    }

    /**
     * The time from bottom to surface
     * @return the complete tome to go up
     */
    public Double getComplete_up_dive_duration() {
        return complete_up_dive_duration;
    }

    /**
     * Set the time from bottom to surface
     * @param complete_up_dive_duration
     */
    public void setComplete_up_dive_duration(Double complete_up_dive_duration) {
        this.complete_up_dive_duration = complete_up_dive_duration;
    }

    /**
     * The additional time
     * @return the time to add to the second dive time
     */
    public Integer getAdd_dive_time() {
        return add_dive_time;
    }

    /**
     * Defines the time to add for the consecutive dive
     * @param add_dive_time: the additional time
     */
    public void setAdd_dive_time(Integer add_dive_time) {
        this.add_dive_time = add_dive_time;
    }

}
