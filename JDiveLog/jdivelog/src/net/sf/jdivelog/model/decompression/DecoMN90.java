/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DecompressionStops.java
 * 
 * @author David Szerman <szdavid@gmail.com>
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



/**
 * Description: Class in order to implement the french decompression model.
 * It is based on the work made by Volker Holthaus <volker.urlaub@gmx.de> on Deco2000 tables
 * 
 * This simulation is simpler than the Deco2000 because there is less possibilities in the MN90 tables
 * @author szdavid
 *
 */
public class DecoMN90 implements DecompressionTable {
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
    
    private Double o2_ratio = null;

    private Integer duration = null;

    private Double complete_duration = null;

    private Double complete_up_dive_duration = null;

    private Integer add_dive_time = new Integer(0);
    
    private Integer altitude= new Integer (0);

    private Deco2000_ConfigurationPanel configurationPanel;

    /**
	 * Constructor
	 *
	 */
    public DecoMN90() {

        // generate the decostops 10 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 1);
        decoStops_with_time.put(360, decoStops);
        dive_depth.put(10, decoStops_with_time);

        // generate the decostops 12 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 2);
        decoStops_with_time.put(140, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 4);
        decoStops_with_time.put(150, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 6);
        decoStops_with_time.put(160, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 7);
        decoStops_with_time.put(170, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 9);
        decoStops_with_time.put(180, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 11);
        decoStops_with_time.put(190, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3,13);
        decoStops_with_time.put(200, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 14);
        decoStops_with_time.put(210, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 15);
        decoStops_with_time.put(220, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 16);
        decoStops_with_time.put(230, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 17);
        decoStops_with_time.put(240, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 18);
        decoStops_with_time.put(250, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 19);
        decoStops_with_time.put(255, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 22);
        decoStops_with_time.put(270, decoStops);
        dive_depth.put(12, decoStops_with_time);
        
        // generate the decostops 15 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 2);
        decoStops_with_time.put(80, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 4);
        decoStops_with_time.put(85, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 6);
        decoStops_with_time.put(90, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 8);
        decoStops_with_time.put(95, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 11);
        decoStops_with_time.put(100, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 13);
        decoStops_with_time.put(105, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 15);
        decoStops_with_time.put(110, decoStops);        
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 17);
        decoStops_with_time.put(115, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 18);
        decoStops_with_time.put(120, decoStops);
        dive_depth.put(15, decoStops_with_time);

        
//      generate the decostops 18 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 1);
        decoStops_with_time.put(55, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 5);
        decoStops_with_time.put(60, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 8);
        decoStops_with_time.put(65, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 11);
        decoStops_with_time.put(70, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 14);
        decoStops_with_time.put(75, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 17);
        decoStops_with_time.put(80, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 21);
        decoStops_with_time.put(85, decoStops);        
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 23);
        decoStops_with_time.put(90, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 26);
        decoStops_with_time.put(95, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 28);
        decoStops_with_time.put(100, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 31);
        decoStops_with_time.put(105, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 34);
        decoStops_with_time.put(110, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 36);
        decoStops_with_time.put(115, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 38);
        decoStops_with_time.put(120, decoStops);
        dive_depth.put(18, decoStops_with_time);
        

        
        
//      generate the decostops 20 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 1);
        decoStops_with_time.put(45, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 4);
        decoStops_with_time.put(50, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 9);
        decoStops_with_time.put(55, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 13);
        decoStops_with_time.put(60, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 16);
        decoStops_with_time.put(65, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 20);
        decoStops_with_time.put(70, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 24);
        decoStops_with_time.put(75, decoStops);        
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 27);
        decoStops_with_time.put(80, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 30);
        decoStops_with_time.put(85, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 34);
        decoStops_with_time.put(90, decoStops);
        dive_depth.put(20, decoStops_with_time);
        

        
//      generate the decostops 22 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 2);
        decoStops_with_time.put(40, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 7);
        decoStops_with_time.put(45, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 12);
        decoStops_with_time.put(50, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 16);
        decoStops_with_time.put(55, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 20);
        decoStops_with_time.put(60, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 25);
        decoStops_with_time.put(65, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 29);
        decoStops_with_time.put(70, decoStops);        
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 33);
        decoStops_with_time.put(75, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 37);
        decoStops_with_time.put(80, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 41);
        decoStops_with_time.put(85, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 41);
        decoStops_with_time.put(90, decoStops);
        dive_depth.put(22, decoStops_with_time);
        
        
//      generate the decostops 25 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 1);
        decoStops_with_time.put(25, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 2);
        decoStops_with_time.put(30, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 5);
        decoStops_with_time.put(35, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 10);
        decoStops_with_time.put(40, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 16);
        decoStops_with_time.put(45, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 21);
        decoStops_with_time.put(50, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 27);
        decoStops_with_time.put(55, decoStops);        
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 32);
        decoStops_with_time.put(60, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 37);
        decoStops_with_time.put(65, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 41);
        decoStops.put(6, 1);
        decoStops_with_time.put(70, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 43);
        decoStops.put(6, 4);
        decoStops_with_time.put(75, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 45);
        decoStops.put(6, 7);
        decoStops_with_time.put(80, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 48);
        decoStops.put(6, 9);
        decoStops_with_time.put(85, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 50);
        decoStops.put(6, 11);
        decoStops_with_time.put(90, decoStops);
        dive_depth.put(25, decoStops_with_time);
        
        
//      generate the decostops 28 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 1);
        decoStops_with_time.put(20, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 2);
        decoStops_with_time.put(25, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 6);
        decoStops_with_time.put(30, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 12);
        decoStops_with_time.put(35, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 19);
        decoStops_with_time.put(40, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 25);
        decoStops_with_time.put(45, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 32);
        decoStops_with_time.put(50, decoStops);        
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 36);
        decoStops.put(6, 2);
        decoStops_with_time.put(55, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 40);
        decoStops.put(6, 4);
        decoStops_with_time.put(60, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 43);
        decoStops.put(6, 8);
        decoStops_with_time.put(65, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 46);
        decoStops.put(6, 11);
        decoStops_with_time.put(70, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 48);
        decoStops.put(6, 14);
        decoStops_with_time.put(75, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 50);
        decoStops.put(6, 17);
        decoStops_with_time.put(80, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 53);
        decoStops.put(6, 20);
        decoStops_with_time.put(85, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        decoStops.put(3, 56);
        decoStops.put(6, 23);
        decoStops_with_time.put(90, decoStops);
        decoStops = new HashMap<Integer, Integer>();
        dive_depth.put(28, decoStops_with_time);

        
//      generate the decostops 30 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 1);
        decoStops_with_time.put(15, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 2);
        decoStops_with_time.put(20, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 4);
        decoStops_with_time.put(25, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 9);
        decoStops_with_time.put(30, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 17);
        decoStops_with_time.put(35, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 24);
        decoStops_with_time.put(40, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 31);
        decoStops.put(6, 1);
        decoStops_with_time.put(45, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 36);
        decoStops.put(6, 3);
        decoStops_with_time.put(50, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 39);
        decoStops.put(6, 6);
        decoStops_with_time.put(55, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 43);
        decoStops.put(6, 10);
        decoStops_with_time.put(60, decoStops);
        dive_depth.put(30, decoStops_with_time);
        
                
//      generate the decostops 32 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 1);
        decoStops_with_time.put(15, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 3);
        decoStops_with_time.put(20, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 6);
        decoStops_with_time.put(25, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 14);
        decoStops_with_time.put(30, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 22);
        decoStops_with_time.put(35, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 29);
        decoStops.put(6, 1);
        decoStops_with_time.put(40, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 34);
        decoStops.put(6, 4);
        decoStops_with_time.put(45, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 39);
        decoStops.put(6, 7);
        decoStops_with_time.put(50, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 43);
        decoStops.put(6, 11);
        decoStops_with_time.put(55, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 46);
        decoStops.put(6, 15);
        decoStops_with_time.put(60, decoStops);
        dive_depth.put(32, decoStops_with_time);
                

        
//      generate the decostops 35 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 2);
        decoStops_with_time.put(15, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 5);
        decoStops_with_time.put(20, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 11);
        decoStops_with_time.put(25, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 20);
        decoStops.put(6, 1);
        decoStops_with_time.put(30, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 27);
        decoStops.put(6, 2);
        decoStops_with_time.put(35, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 34);
        decoStops.put(6, 5);
        decoStops_with_time.put(40, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 39);
        decoStops.put(6, 9);
        decoStops_with_time.put(45, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 43);
        decoStops.put(6, 14);
        decoStops_with_time.put(50, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 47);
        decoStops.put(6, 18);
        decoStops_with_time.put(55, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 50);
        decoStops.put(6, 22);
        decoStops_with_time.put(60, decoStops);
        dive_depth.put(35, decoStops_with_time);
        

        
//      generate the decostops 38 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 1);
        decoStops_with_time.put(10, decoStops);
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 4);
        decoStops_with_time.put(15, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 8);
        decoStops_with_time.put(20, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 16);
        decoStops.put(6, 1);
        decoStops_with_time.put(25, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 24);
        decoStops.put(6, 3);
        decoStops_with_time.put(30, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 33);
        decoStops.put(6, 5);
        decoStops_with_time.put(35, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 38);
        decoStops.put(6, 10);
        decoStops_with_time.put(40, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 43);
        decoStops.put(6, 15);
        decoStops_with_time.put(45, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 47);
        decoStops.put(6, 20);
        decoStops_with_time.put(50, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 50);
        decoStops.put(6, 23);
        decoStops.put(9, 2);
        decoStops_with_time.put(55, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 53);
        decoStops.put(6, 27);
        decoStops.put(9, 5);
        decoStops_with_time.put(60, decoStops);
        dive_depth.put(38, decoStops_with_time);
        

//      generate the decostops 40 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 2);
        decoStops_with_time.put(10, decoStops);
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 4);
        decoStops_with_time.put(15, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 9);
        decoStops.put(6, 1);
        decoStops_with_time.put(20, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 19);
        decoStops.put(6, 2);
        decoStops_with_time.put(25, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 28);
        decoStops.put(6, 4);
        decoStops_with_time.put(30, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 35);
        decoStops.put(6, 8);
        decoStops_with_time.put(35, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 40);
        decoStops.put(6, 13);
        decoStops_with_time.put(40, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 45);
        decoStops.put(6, 18);
        decoStops.put(9, 1);
        decoStops_with_time.put(45, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 48);
        decoStops.put(6, 23);
        decoStops.put(9, 2);
        decoStops_with_time.put(50, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 52);
        decoStops.put(6, 26);
        decoStops.put(9, 5);
        decoStops_with_time.put(55, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 57);
        decoStops.put(6, 29);
        decoStops.put(9, 8);
        decoStops_with_time.put(60, decoStops);
        dive_depth.put(40, decoStops_with_time);

        
//      generate the decostops 42 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 2);
        decoStops_with_time.put(10, decoStops);
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 5);
        decoStops_with_time.put(15, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 12);
        decoStops.put(6, 1);
        decoStops_with_time.put(20, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 22);
        decoStops.put(6, 3);
        decoStops_with_time.put(25, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 31);
        decoStops.put(6, 6);
        decoStops_with_time.put(30, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 37);
        decoStops.put(6, 11);
        decoStops_with_time.put(35, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 43);
        decoStops.put(6, 16);
        decoStops.put(9, 1);
        decoStops_with_time.put(40, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 47);
        decoStops.put(6, 21);
        decoStops.put(9, 3);
        decoStops_with_time.put(45, decoStops);
        dive_depth.put(42, decoStops_with_time);

        
//      generate the decostops 45 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 3);
        decoStops_with_time.put(10, decoStops);
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 6);
        decoStops.put(6, 1);
        decoStops_with_time.put(15, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 15);
        decoStops.put(6, 3);
        decoStops_with_time.put(20, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 25);
        decoStops.put(6, 5);
        decoStops_with_time.put(25, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 35);
        decoStops.put(6, 9);
        decoStops_with_time.put(30, decoStops);
        dive_depth.put(45, decoStops_with_time);

        
//      generate the decostops 48 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 4);
        decoStops_with_time.put(10, decoStops);
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 7);
        decoStops.put(6, 2);
        decoStops_with_time.put(15, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 19);
        decoStops.put(6, 4);
        decoStops_with_time.put(20, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 30);
        decoStops.put(6, 7);
        decoStops_with_time.put(25, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 37);
        decoStops.put(6, 12);
        decoStops.put(9, 1);
        decoStops_with_time.put(30, decoStops);
        dive_depth.put(48, decoStops_with_time);

        
//      generate the decostops 50 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 1);
        decoStops_with_time.put(5, decoStops);
        decoStops.put(3, 4);
        decoStops_with_time.put(10, decoStops);
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 9);
        decoStops.put(6, 2);
        decoStops_with_time.put(15, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 22);
        decoStops.put(6, 4);
        decoStops_with_time.put(20, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 32);
        decoStops.put(6, 8);
        decoStops.put(9, 1);
        decoStops_with_time.put(25, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 39);
        decoStops.put(6, 14);
        decoStops.put(9, 2);
        decoStops_with_time.put(30, decoStops);
        dive_depth.put(50, decoStops_with_time);
        

//      generate the decostops 52 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 1);
        decoStops_with_time.put(5, decoStops);
        decoStops.put(3, 4);
        decoStops.put(6, 1);
        decoStops_with_time.put(10, decoStops);
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 10);
        decoStops.put(6, 3);
        decoStops_with_time.put(15, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 23);
        decoStops.put(6, 5);
        decoStops.put(9, 1);
        decoStops_with_time.put(20, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 34);
        decoStops.put(6, 9);
        decoStops.put(9, 2);
        decoStops_with_time.put(25, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 41);
        decoStops.put(6, 15);
        decoStops.put(9, 4);
        decoStops_with_time.put(30, decoStops);
        dive_depth.put(52, decoStops_with_time);

        
//      generate the decostops 55 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 1);
        decoStops_with_time.put(5, decoStops);
        decoStops.put(3, 5);
        decoStops.put(6, 1);
        decoStops_with_time.put(10, decoStops);
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 13);
        decoStops.put(6, 4);
        decoStops_with_time.put(15, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 27);
        decoStops.put(6, 6);
        decoStops.put(9, 1);
        decoStops_with_time.put(20, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 37);
        decoStops.put(6, 11);
        decoStops.put(9, 3);
        decoStops_with_time.put(25, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 44);
        decoStops.put(6, 18);
        decoStops.put(9, 6);
        decoStops_with_time.put(30, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 50);
        decoStops.put(6, 23);
        decoStops.put(9, 9);
        decoStops.put(12, 1);
        decoStops_with_time.put(35, decoStops);
        decoStops.put(3, 55);
        decoStops.put(6, 29);
        decoStops.put(9, 12);
        decoStops.put(12, 3);
        decoStops_with_time.put(40, decoStops);       
        dive_depth.put(55, decoStops_with_time);        
        
        
        
        
//      generate the decostops 58 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 2);
        decoStops_with_time.put(5, decoStops);
        decoStops.put(3, 5);
        decoStops.put(6, 2);
        decoStops_with_time.put(10, decoStops);
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 16);
        decoStops.put(6, 4);
        decoStops.put(9, 1);
        decoStops_with_time.put(15, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 30);
        decoStops.put(6, 7);
        decoStops.put(9, 2);
        decoStops_with_time.put(20, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 40);
        decoStops.put(6, 13);
        decoStops.put(9, 4);
        decoStops_with_time.put(25, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 46);
        decoStops.put(6, 21);
        decoStops.put(9, 7);
        decoStops.put(12, 1);
        decoStops_with_time.put(30, decoStops);
        decoStops.put(3, 52);
        decoStops.put(6, 26);
        decoStops.put(9, 11);
        decoStops.put(12, 2);
        decoStops_with_time.put(35, decoStops);   
        decoStops.put(3, 59);
        decoStops.put(6, 30);
        decoStops.put(9, 15);
        decoStops.put(12, 5);
        decoStops_with_time.put(40, decoStops);
        dive_depth.put(58, decoStops_with_time);
        
        
        

//      generate the decostops 60 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 2);
        decoStops_with_time.put(5, decoStops);
        decoStops.put(3, 6);
        decoStops.put(6, 2);
        decoStops_with_time.put(10, decoStops);
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 19);
        decoStops.put(6, 4);
        decoStops.put(9, 1);
        decoStops_with_time.put(15, decoStops);
        decoStops  = new HashMap<Integer, Integer>();
        decoStops.put(3, 32);
        decoStops.put(6, 8);
        decoStops.put(9, 3);
        decoStops_with_time.put(20, decoStops);
        decoStops.put(3, 41);
        decoStops.put(6, 15);
        decoStops.put(9, 5);
        decoStops_with_time.put(25, decoStops);
        decoStops.put(3, 48);
        decoStops.put(6, 22);
        decoStops.put(9, 8);
        decoStops.put(12, 1);
        decoStops_with_time.put(30, decoStops);
        decoStops.put(3, 54);
        decoStops.put(6, 28);
        decoStops.put(9, 11);
        decoStops.put(12, 4);
        decoStops_with_time.put(35, decoStops);        
        decoStops.put(3, 62);
        decoStops.put(6, 30);
        decoStops.put(9, 17);
        decoStops.put(12, 6);
        decoStops_with_time.put(40, decoStops);
        dive_depth.put(60, decoStops_with_time);        
        
//      generate the decostops 62 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 2);
        decoStops_with_time.put(5, decoStops);
        decoStops.put(3, 7);
        decoStops.put(6, 2);
        decoStops_with_time.put(10, decoStops);
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 21);
        decoStops.put(6, 5);
        decoStops.put(9, 1);
        decoStops_with_time.put(15, decoStops);
        dive_depth.put(62, decoStops_with_time);
        
//      generate the decostops 65 Meters
        decoStops = new HashMap<Integer, Integer>();
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 3);
        decoStops_with_time.put(5, decoStops);
        decoStops.put(3, 8);
        decoStops.put(6, 3);
        decoStops_with_time.put(10, decoStops);
        decoStops_with_time = new HashMap<Integer, HashMap<Integer, Integer>>();
        decoStops.put(3, 24);
        decoStops.put(6, 5);
        decoStops.put(9, 2);
        decoStops_with_time.put(15, decoStops);
        dive_depth.put(65, decoStops_with_time);

        
        // generate the zero hours
        zeroHours.put(10, 330);
        zeroHours.put(12, 135);
        zeroHours.put(15, 75);
        zeroHours.put(18,50);
        zeroHours.put(20, 40);
        zeroHours.put(22,35);
        zeroHours.put(25, 20);
        zeroHours.put(28,15);
        zeroHours.put(30,10);
        zeroHours.put(32,10);
        zeroHours.put(35,10);
        zeroHours.put(38,5);
        zeroHours.put(40,5);
        zeroHours.put(42,5);
        zeroHours.put(45,5);
        zeroHours.put(48,5);
        
        
        // generate the repeat group 6 Meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(15, "A");
        repeat_group.put(30, "B");
        repeat_group.put(45, "C");
        repeat_group.put(75, "D");
        repeat_group.put(105, "E");
        repeat_group.put(135, "F");
        repeat_group.put(180, "G");
        repeat_group.put(240, "H");
        repeat_group.put(315, "I");
        repeat_group.put(360, "J");
        repeat_groups.put(6, repeat_group);

        // generate the repeat group 8 Meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(15, "B");
        repeat_group.put(30, "C");
        repeat_group.put(45, "D");
        repeat_group.put(60, "E");
        repeat_group.put(90, "F");
        repeat_group.put(105, "G");
        repeat_group.put(135, "H");
        repeat_group.put(165, "I");
        repeat_group.put(195, "J");
        repeat_group.put(255, "K");
        repeat_group.put(300, "L");
        repeat_group.put(360, "M");
        repeat_groups.put(8, repeat_group);

        
        // generate the repeat group 10 Meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(15, "B");
        repeat_group.put(30, "C");
        repeat_group.put(45, "D");
        repeat_group.put(60, "F");
        repeat_group.put(75, "G");
        repeat_group.put(105, "H");
        repeat_group.put(120, "I");
        repeat_group.put(135, "J");
        repeat_group.put(165, "K");
        repeat_group.put(180, "L");
        repeat_group.put(240, "M");
        repeat_group.put(255, "N");
        repeat_group.put(315, "O");
        repeat_group.put(330, "P");
        repeat_group.put(360, "P");
        repeat_groups.put(10, repeat_group);

        
        // generate the repeat group 12 Meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(5, "A");
        repeat_group.put(15, "B");
        repeat_group.put(25, "C");
        repeat_group.put(35, "D");
        repeat_group.put(45, "E");
        repeat_group.put(55, "F");
        repeat_group.put(65, "G");
        repeat_group.put(80, "H");
        repeat_group.put(90, "I");
        repeat_group.put(105, "J");
        repeat_group.put(120, "K");
        repeat_group.put(135, "L");
        repeat_group.put(140, "L");
        repeat_group.put(150, "M");
        repeat_group.put(160, "M");
        repeat_group.put(170, "N");
        repeat_group.put(180, "N");
        repeat_group.put(190, "N");
        repeat_group.put(200, "O");
        repeat_group.put(210, "O");
        repeat_group.put(220, "O");
        repeat_group.put(230, "O");
        repeat_group.put(240, "O");
        repeat_group.put(250, "P");
        repeat_group.put(255, "P");
        repeat_group.put(270, "P");
        repeat_group.put(285, "P");
        repeat_group.put(300, "P");
        repeat_groups.put(12, repeat_group);
        
        // generate the repeat group 15 Meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(5, "A");
        repeat_group.put(10, "B");
        repeat_group.put(15, "C");
        repeat_group.put(20, "C");
        repeat_group.put(25, "D");
        repeat_group.put(30, "E");
        repeat_group.put(35, "E");
        repeat_group.put(40, "F");
        repeat_group.put(45, "G");
        repeat_group.put(50, "G");
        repeat_group.put(55, "H");
        repeat_group.put(60, "H");
        repeat_group.put(65, "I");
        repeat_group.put(70, "I");
        repeat_group.put(75, "J");
        repeat_group.put(80, "J");
        repeat_group.put(85, "K");
        repeat_group.put(90, "K");
        repeat_group.put(95, "L");
        repeat_group.put(100, "L");
        repeat_group.put(105, "L");
        repeat_group.put(110, "M");
        repeat_group.put(115, "M");
        repeat_group.put(120, "M");
        repeat_groups.put(15, repeat_group);
                
        
        //Repeat group for 18 meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(5, "B");
        repeat_group.put(10, "B");
        repeat_group.put(15, "C");
        repeat_group.put(20, "D");
        repeat_group.put(25, "E");
        repeat_group.put(30, "F");
        repeat_group.put(35, "F");
        repeat_group.put(40, "G");
        repeat_group.put(45, "H");
        repeat_group.put(50, "H");
        repeat_group.put(55, "I");
        repeat_group.put(60, "J");
        repeat_group.put(65, "J");
        repeat_group.put(70, "K");
        repeat_group.put(75, "K");
        repeat_group.put(80, "L");
        repeat_group.put(85, "L");
        repeat_group.put(90, "M");
        repeat_group.put(95, "M");
        repeat_group.put(100, "M");
        repeat_group.put(105, "N");
        repeat_group.put(110, "N");
        repeat_group.put(115, "N");
        repeat_group.put(120, "O");
        repeat_groups.put(18, repeat_group);

        
        //Repeat group for 20 meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(5, "B");
        repeat_group.put(10, "B");
        repeat_group.put(15, "D");
        repeat_group.put(20, "D");
        repeat_group.put(25, "E");
        repeat_group.put(30, "F");
        repeat_group.put(35, "G");
        repeat_group.put(40, "H");
        repeat_group.put(45, "I");
        repeat_group.put(50, "I");
        repeat_group.put(55, "J");
        repeat_group.put(60, "K");
        repeat_group.put(65, "K");
        repeat_group.put(70, "L");
        repeat_group.put(75, "L");
        repeat_group.put(80, "M");
        repeat_group.put(85, "M");
        repeat_group.put(90, "M");
        repeat_groups.put(20, repeat_group);
        
        
        //Repeat group for 22 meters 
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(5, "B");
        repeat_group.put(10, "C");
        repeat_group.put(15, "D");
        repeat_group.put(20, "E");
        repeat_group.put(25, "F");
        repeat_group.put(30, "G");
        repeat_group.put(35, "H");
        repeat_group.put(40, "I");
        repeat_group.put(45, "I");
        repeat_group.put(50, "J");
        repeat_group.put(55, "K");
        repeat_group.put(60, "K");
        repeat_group.put(65, "L");
        repeat_group.put(70, "L");
        repeat_group.put(75, "M");
        repeat_group.put(80, "M");
        repeat_group.put(85, "N");
        repeat_group.put(90, "N");
        repeat_groups.put(22, repeat_group);
        
        // Repeat group for 25 meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(5, "B");
        repeat_group.put(10, "C");
        repeat_group.put(15, "D");
        repeat_group.put(20, "E");
        repeat_group.put(25, "F");
        repeat_group.put(30, "H");
        repeat_group.put(35, "I");
        repeat_group.put(40, "J");
        repeat_group.put(45, "J");
        repeat_group.put(50, "K");
        repeat_group.put(55, "L");
        repeat_group.put(60, "L");
        repeat_group.put(65, "M");
        repeat_group.put(70, "M");
        repeat_group.put(75, "N");
        repeat_group.put(80, "N");
        repeat_group.put(85, "O");
        repeat_group.put(90, "O");
        repeat_groups.put(25, repeat_group);
        
        
        // Repeat group for 28 meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(5, "B");
        repeat_group.put(10, "D");
        repeat_group.put(15, "E");
        repeat_group.put(20, "F");
        repeat_group.put(25, "G");
        repeat_group.put(30, "H");
        repeat_group.put(35, "I");
        repeat_group.put(40, "J");
        repeat_group.put(45, "K");
        repeat_group.put(50, "L");
        repeat_group.put(55, "M");
        repeat_group.put(60, "M");
        repeat_group.put(65, "N");
        repeat_group.put(70, "N");
        repeat_group.put(75, "O");
        repeat_group.put(80, "O");
        repeat_group.put(85, "0");
        repeat_group.put(90, "P");
        repeat_groups.put(28, repeat_group);

        
        // Repeat groups for 30 meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(5, "B");
        repeat_group.put(10, "D");
        repeat_group.put(15, "E");
        repeat_group.put(20, "F");
        repeat_group.put(25, "H");
        repeat_group.put(30, "I");
        repeat_group.put(35, "J");
        repeat_group.put(40, "K");
        repeat_group.put(45, "L");
        repeat_group.put(50, "M");
        repeat_group.put(55, "M");
        repeat_group.put(60, "N");
        repeat_group.put(65, "N");
        repeat_group.put(70, "O");
        repeat_groups.put(30, repeat_group);
        
        // Repeat groups for 32 meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(5, "B");
        repeat_group.put(10, "D");
        repeat_group.put(15, "E");
        repeat_group.put(20, "G");
        repeat_group.put(25, "H");
        repeat_group.put(30, "I");
        repeat_group.put(35, "K");
        repeat_group.put(40, "K");
        repeat_group.put(45, "L");
        repeat_group.put(50, "M");
        repeat_group.put(55, "N");
        repeat_group.put(60, "N");
        repeat_group.put(65, "0");
        repeat_group.put(70, "O");
        repeat_groups.put(32, repeat_group);
        
        
        // Repeat groups for 35 meters 
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(5, "C");
        repeat_group.put(10, "D");
        repeat_group.put(15, "F");
        repeat_group.put(20, "H");
        repeat_group.put(25, "I");
        repeat_group.put(30, "J");
        repeat_group.put(35, "K");
        repeat_group.put(40, "L");
        repeat_group.put(45, "M");
        repeat_group.put(50, "N");
        repeat_group.put(55, "N");
        repeat_group.put(60, "O");
        repeat_groups.put(35, repeat_group);

        
        // Repeat groups for 38 meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(5, "C");
        repeat_group.put(10, "E");
        repeat_group.put(15, "F");
        repeat_group.put(20, "H");
        repeat_group.put(25, "J");
        repeat_group.put(30, "K");
        repeat_group.put(35, "L");
        repeat_group.put(40, "M");
        repeat_group.put(45, "N");
        repeat_group.put(50, "N");
        repeat_group.put(55, "O");
        repeat_group.put(60, "P");
        repeat_groups.put(38, repeat_group);
        
        
        
        // Repeat groups for 40 meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(5, "C");
        repeat_group.put(10, "E");
        repeat_group.put(15, "G");
        repeat_group.put(20, "H");
        repeat_group.put(25, "J");
        repeat_group.put(30, "K");
        repeat_group.put(35, "L");
        repeat_group.put(40, "M");
        repeat_group.put(45, "N");
        repeat_group.put(50, "O");
        repeat_group.put(55, "O");
        repeat_group.put(60, "P");
        repeat_groups.put(40, repeat_group);
                
        
        
        // Repeat groups for 42 meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(5, "C");
        repeat_group.put(10, "E");
        repeat_group.put(15, "G");
        repeat_group.put(20, "I");
        repeat_group.put(25, "J");
        repeat_group.put(30, "L");
        repeat_group.put(35, "M");
        repeat_group.put(40, "N");
        repeat_groups.put(42, repeat_group);
                
        // Repeat group for 45 meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(5, "C");
        repeat_group.put(10, "F");
        repeat_group.put(15, "H");
        repeat_group.put(20, "I");
        repeat_group.put(25, "K");
        repeat_group.put(30, "L");
        repeat_group.put(35, "M");
        repeat_group.put(40, "N");
        repeat_groups.put(45, repeat_group);
                
        
        
        // Repeat group for 48 meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(5, "D");
        repeat_group.put(10, "F");
        repeat_group.put(15, "H");
        repeat_group.put(20, "J");
        repeat_group.put(25, "K");
        repeat_group.put(30, "M");
        repeat_group.put(35, "N");
        repeat_group.put(40, "O");
        repeat_groups.put(48, repeat_group);
                
        
        // Repeat groups for 50 meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(5, "D");
        repeat_group.put(10, "F");
        repeat_group.put(15, "H");
        repeat_group.put(20, "J");
        repeat_group.put(25, "L");
        repeat_group.put(30, "M");
        repeat_group.put(35, "N");
        repeat_group.put(40, "O");
        repeat_groups.put(50, repeat_group);
                
        
        
        
        // Repeat group for 52 meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(5, "D");
        repeat_group.put(10, "F");
        repeat_group.put(15, "I");
        repeat_group.put(20, "K");
        repeat_group.put(25, "L");
        repeat_group.put(30, "M");
        repeat_group.put(35, "O");
        repeat_group.put(40, "O");
        repeat_groups.put(52, repeat_group);
                
        
        
        // Repeat groups for 55 meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(5, "D");
        repeat_group.put(10, "G");
        repeat_group.put(15, "I");
        repeat_group.put(20, "K");
        repeat_group.put(25, "M");
        repeat_group.put(30, "N");
        repeat_group.put(35, "O");
        repeat_group.put(40, "P");
        repeat_groups.put(55, repeat_group);
                
        
        
        // Repeat groups for 58 meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(5, "D");
        repeat_group.put(10, "G");
        repeat_group.put(15, "J");
        repeat_group.put(20, "K");
        repeat_group.put(25, "M");
        repeat_group.put(30, "N");
        repeat_group.put(35, "O");
        repeat_group.put(40, "P");
        repeat_groups.put(58, repeat_group);
                

        
        
        // Repeat groups for the 60 meters
        repeat_group = new HashMap<Integer, String>();
        repeat_group.put(5, "D");
        repeat_group.put(10, "G");
        repeat_group.put(15, "J");
        repeat_group.put(20, "L");
        repeat_group.put(25, "M");
        repeat_group.put(30, "O");
        repeat_group.put(35, "P");
        repeat_group.put(40, "P");
        repeat_groups.put(60, repeat_group);
        
        
        // For 62 meters and 65 meters, no following dived is allowed for 24 hours
                        
        // generate the no flight time
        // in French Federation, no flight for 24 hours
        no_flight.put("A", 24);
        no_flight.put("B", 24);
        no_flight.put("C", 24);
        no_flight.put("D", 24);
        no_flight.put("E", 24);
        no_flight.put("F", 24);
        no_flight.put("G", 24);
        no_flight.put("H", 24);
        no_flight.put("I", 24);
        no_flight.put("J", 24);
        no_flight.put("K", 24);
        no_flight.put("L", 24);
        no_flight.put("M", 24);
        no_flight.put("N", 24);
        no_flight.put("O", 24);
        no_flight.put("P", 24);

        
        
        // generate the add on time for repeat dive
        Integer[] surface_time = new Integer[] { 15, 30, 45, 60, 90, 120, 150, 180 , 240, 360, 460, 600, 720 };
        surface_duration.put("P", surface_time);
        surface_time = new Integer[] { 15, 30, 45, 60, 90, 120, 150, 180 , 240, 360, 460, 600, 720 };
        surface_duration.put("O", surface_time);
        surface_time = new Integer[] { 15, 30, 45, 60, 90, 120, 150, 180 , 240, 360, 460, 600, 720 };
        surface_duration.put("N", surface_time);
        surface_time = new Integer[] { 15, 30, 45, 60, 90, 120, 150, 180 , 240, 360, 460, 600, 720 };
        surface_duration.put("M", surface_time);
        surface_time = new Integer[] { 15, 30, 45, 60, 90, 120, 150, 180 , 240, 360, 460, 600, 720 };
        surface_duration.put("L", surface_time);
        surface_time = new Integer[] { 15, 30, 45, 60, 90, 120, 150, 180 , 240, 360, 460, 600, 720 };
        surface_duration.put("K", surface_time);
        surface_time = new Integer[] { 15, 30, 45, 60, 90, 120, 150, 180 , 240, 360, 460, 600, 720 };
        surface_duration.put("J", surface_time);
        surface_time = new Integer[] { 15, 30, 45, 60, 90, 120, 150, 180 , 240, 360, 460, 600, 720 };
        surface_duration.put("I", surface_time);
        surface_time = new Integer[] { 15, 30, 45, 60, 90, 120, 150, 180 , 240, 360, 460, 600, 720 };
        surface_duration.put("H", surface_time);
        surface_time = new Integer[] { 15, 30, 45, 60, 90, 120, 150, 180 , 240, 360, 460, 600, 720 };
        surface_duration.put("G", surface_time);
        surface_time = new Integer[] { 15, 30, 45, 60, 90, 120, 150, 180 , 240, 360, 460, 600 };
        surface_duration.put("F", surface_time);
        surface_time = new Integer[] { 15, 30, 45, 60, 90, 120, 150, 180 , 240, 360, 460, 600 };
        surface_duration.put("E", surface_time);
        surface_time = new Integer[] { 15, 30, 45, 60, 90, 120, 150, 180 , 240, 360, 460, 600 };
        surface_duration.put("D", surface_time);
        surface_time = new Integer[] { 15, 30, 45, 60, 90, 120, 150, 180 , 240, 360, 460 };
        surface_duration.put("C", surface_time);
        surface_time = new Integer[] { 15, 30, 45, 60, 90, 120, 150, 180 , 240, 360, 460 };
        surface_duration.put("B", surface_time);
        surface_time = new Integer[] { 15, 30, 45, 60, 90, 120, 150, 180 , 240, 360, 460 };
        surface_duration.put("A", surface_time);

        // generate the time quantity
        Integer[] add_on_times = new Integer[] {0, 196, 180, 160, 139, 124, 106, 93, 81, 68, 57, 47, 38, 29, 23, 17, 11, 7, 4 };
        add_on_time.put(12, add_on_times);
        add_on_times = new Integer[] {0, 135, 126, 114, 101, 91, 79, 70, 62, 52, 44, 37, 30, 23, 18, 13, 9, 6, 3 };
        add_on_time.put(15, add_on_times);
        add_on_times = new Integer[] { 0, 104, 97, 89, 79, 72, 63, 56, 50,42, 36, 30, 24, 19, 15, 11, 7, 5, 2 };
        add_on_time.put(18, add_on_times);
        add_on_times = new Integer[] { 0, 90, 85, 78, 70, 63, 56, 50, 44, 37, 32, 27, 22, 17, 13, 10, 7, 4, 2 };
        add_on_time.put(20, add_on_times);
        add_on_times = new Integer[] { 0, 80, 75, 69, 62, 56, 50, 45, 40, 34, 29, 20, 15, 12, 9, 6, 4, 2 };
        add_on_time.put(22, add_on_times);
        add_on_times = new Integer[] { 0, 68, 64, 59, 53, 49, 43, 39, 34, 29, 25, 21, 17, 13, 11, 8, 5, 3, 2 };
        add_on_time.put(25, add_on_times);
        add_on_times = new Integer[] { 0, 59, 56, 52, 47, 43, 38, 34, 30, 26, 22, 19, 15, 12, 10, 7, 5, 3, 2 };
        add_on_time.put(28, add_on_times);
        add_on_times = new Integer[] { 0, 55, 52, 48, 43, 40, 35, 32, 28, 24, 21, 17, 14, 11, 9, 7, 4, 3, 1 };
        add_on_time.put(30, add_on_times);
        add_on_times = new Integer[] { 0, 51, 48, 44, 40, 37, 33, 29, 26, 22, 19, 16, 13, 10, 8, 6, 4, 3, 1 };
        add_on_time.put(32, add_on_times);
        add_on_times = new Integer[] { 0, 46, 43, 40, 36, 33, 30, 27, 24, 20, 18, 15, 12, 10, 8, 6, 4, 2, 1 };
        add_on_time.put(35, add_on_times);
        add_on_times = new Integer[] { 0, 42, 39, 37, 33, 30, 27, 24, 22, 19, 16, 14, 11, 9, 7, 5, 3, 2, 1 };
        add_on_time.put(48, add_on_times);
        add_on_times = new Integer[] { 0, 39, 37, 35, 31, 29, 26, 23, 21, 18, 15, 13, 11, 8, 7, 5, 3, 2, 1 };
        add_on_time.put(40, add_on_times);
        add_on_times = new Integer[] { 0, 37, 35, 33, 30, 27, 24, 22, 20, 17, 15, 12, 10, 8, 6, 5, 3, 2, 1 };
        add_on_time.put(42, add_on_times);
        add_on_times = new Integer[] { 0, 34, 33, 30, 28, 25, 23, 20, 18, 16, 13, 11, 9, 7, 6, 4, 3, 2, 1  };
        add_on_time.put(45, add_on_times);
        add_on_times = new Integer[] { 0, 32, 30, 28, 26, 24, 21, 19, 17, 15, 13, 11, 9, 7, 5, 4, 3, 2, 1 };
        add_on_time.put(48, add_on_times);
        add_on_times = new Integer[] { 0, 31, 29, 27, 25, 23, 20, 18, 16, 14, 12, 10, 8, 7, 5, 4, 3, 2, 1 };
        add_on_time.put(50, add_on_times);
        add_on_times = new Integer[] { 0, 29, 28, 26, 24, 22, 19, 18, 16, 13, 12, 10, 8, 6, 5, 4, 3, 2, 1 };
        add_on_time.put(52, add_on_times);
        add_on_times = new Integer[] { 0, 28, 26, 24, 22, 20, 18, 17, 15, 13, 11, 9, 8, 6, 5, 4, 2, 2, 1 };
        add_on_time.put(55, add_on_times);
        add_on_times = new Integer[] { 0, 26, 25, 23, 21, 29, 17, 16, 14, 12, 10, 9, 7, 6, 5, 3, 2, 1, 1 };
        add_on_time.put(58, add_on_times);
        add_on_times = new Integer[] { 0, 25, 24, 22, 20, 19, 17, 15, 13, 12, 10, 9, 7, 5, 4, 3, 2, 1, 1 };
        add_on_time.put(60, add_on_times);

    }

    
    
    
    /**
	 * @return Returns the altitude.
	 */
	public Integer getAltitude() {
		return altitude;
	}

	/**
	 * @return Returns the stop time if the stop is done with 02
	 * @param depth
	 * @param duration
	 * @return new calculated time
	 */
	public Integer get02time(int depth,int duration) {
		Integer a;
		if (depth<=6) {
			//It is possible to use O2 during stops
			if (duration*2/3<5) {
				//The minimum time with 02 is 5 minutes
				a=new Integer (5);
			}
			else {
				//We meet the minimum required time (5 minutes)
				// so we can apply the calculated time
				a= new Integer (duration *2/3);
			}
		}
		else {
			//the depth is too deep for O2 use
			a=duration;
		}
		return a;
	}





	/**
	 * @param altitude The altitude to set.
	 */
	public void setAltitude(Integer altitude) {
		this.altitude = altitude;
	}

	
	/**
	 * Get the air pressure in bar
	 * It supposes that there is a lost of 0.1 bar every 1000 meters
	 * @return the air pressure
	 */
	public double getAirPressure(){
		return 1-(0.1/1000*this.altitude);
	}

	/**
	 * While diving in altitude, the real depth of the stops is less than the planned one.
	 * It is due to the difference of air pressure
	 * 
	 * @param stopDepth : the planned depth for the stop from the MN90 model
	 * @return the real depth of the stop
	 */
	public double getRealStopDepth(double stopDepth){
		return stopDepth*this.getAirPressure();
	}
	
	
	
	
	/**
	 * Get the time without any decompression need
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
     *
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
        this.o2_ratio = o2_ratio;
        setCalculated_depth_higher();
        setCalculated_depth_lower();

        // recalculate the add on time
        setAdd_dive_time(0);
        if (!repeat_group.trim().equals("") && surface_interval != 0) {
            setAdd_dive_time(getAdd_on_time(repeat_group, surface_interval));
        }

        setDuration(duration + add_dive_time);
        setCalculated_duration();
        if (calculated_duration == 999) {
            calculated_duration = getZeroHour();
        }
    }

    
    /**
     * Get time to add to the depth time
     * @param repeating_group : the repeating group for the next dive
     * @param surface_interval : Interval surface
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

        private DecoMN90 DecoMN90 = null;

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
         * 
         * @param mainWindow: the window where to include the panel
         */
        public Deco2000_ConfigurationPanel(MainWindow mainWindow) {

            this.mainWindow = mainWindow;

            DecoMN90 = new DecoMN90();

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
            this.add(l, gc);

            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.gridy = 8;
            gc.gridx = 0;
            this.add(getDiveProfilePanel(), gc);
            
        }

        /**
         * The textfield with maximum depth
         * @return JTextfield containing the max depth
         */
        private JTextField getMax_depth() {
            if (max_depth == null) {
                max_depth = new JTextField();
            }
            return max_depth;
        }

        
        /**
         * Get the 02 percentage
         * @return the textfield which contains the 02 information
         */
        private JTextField getO2_ratio() {
            if (o2_ratio == null) {
                o2_ratio = new JTextField();
                o2_ratio.setText(O2_PART);
            }
            return o2_ratio;
        }

        
        /**
         * The textfield containing the consumption information
         * @return JTextField
         */
        private JTextField getAmv() {
            if (amvField == null) {
                amvField = new JTextField();
                amvField.setText(mainWindow.getLogBook().getAverageAmv().toString());
            }
            return amvField;
        }

        /**
         * Information about the dive speed to go up
         * @return the jtextfield containing the information
         */
        private JTextField getUp_dive_speed() {
            if (up_dive_speed == null) {
                up_dive_speed = new JTextField();
                up_dive_speed.setText(UP_DIVE_SPEED);
            }
            return up_dive_speed;
        }

        
        /**
         * Information about the speed to go down
         * @return the JTextField containing the information
         */
        private JTextField getDown_dive_speed() {
            if (down_dive_speed == null) {
                down_dive_speed = new JTextField();
                down_dive_speed.setText(DOWN_DIVE_SPEED);
            }
            return down_dive_speed;
        }

        
        /**
         * the information about the dive duration
         * @return the textfield containing the information
         */
        private JTextField getDuration() {
            if (duration == null) {
                duration = new JTextField();
            }
            return duration;
        }

        
        /**
         * Text field to get de dive data
         * @return the JTextField containing the information
         */
        private JTextArea getDive_data() {
            if (dive_data == null) {
                dive_data = new JTextArea(15, 2);
            }
            return dive_data;
        }
		/**
		 * Button to launch the decompression calcul
		 * @return the JButton for the calcul
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
         * Text field containing the surf time
         * @return the jtextfield containing the information
         */
        private JTextField getSurface_time() {
            if (surface_time == null) {
                surface_time = new JTextField();
            }
            return surface_time;
        }

        /**
         * JComboBox get the the repeating group
         * @return the JComboBox to get the information
         */
        private JComboBox getRepeating_groups() {
            if (repeating_groups == null) {
                repeating_groups = new JComboBox(REPEATING_GROUPS);
            }
            return repeating_groups;
        }

        
        /**
         * Get the panel where the dive profile is set
         * @return JPanel with the profile
         */
        private JPanel getDiveProfilePanel() {
            if (diveprofilePanel == null) {
                diveprofilePanel = new JPanel();
                diveprofilePanel.add(getChartPanel(getDivedata_chart(null)));
            }
            return diveprofilePanel;
        }

        
        /**
         * Return a panel
         * @param chart
         * @return the chart panel
         */
        private ChartPanel getChartPanel(JFreeChart chart) {
            ChartPanel chartPanel = new ChartPanel(chart);
            return chartPanel;
        }

        
        /**
         * Return the chart to draw the profile
         * @param data_xy
         * @return the chart
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
         * A collection of x,y coordinates
         * @return a collection of points 
         */
        private XYSeriesCollection diveProfile_data() {
            Double temp = new Double(0);
            Double temp_up_dive_time = new Double(0);
            XYSeries series = new XYSeries(Messages.getString("diveplanning.diveprofile"));

            // begin of the dive profil
            series.add(0, 0);
            // down dive
            temp = new Double(DecoMN90.depth) / new Double(down_dive_speed.getText());
            series.add(temp.doubleValue(), -1 * DecoMN90.depth);
            airconsumption = amv * temp.doubleValue() * ((DecoMN90.depth / 10) + 1);
            // dive time at the ground
            temp += DecoMN90.duration;
            series.add(temp.doubleValue(), -1 * DecoMN90.depth);
            airconsumption += amv * temp.doubleValue() * ((DecoMN90.depth / 10) + 1);
            // up dive time with stops
            temp_up_dive_time -= temp;
            if (DecoMN90.getDecompressionStops() != null) {
                if (DecoMN90.getDecompressionStops().get(15) != null) {
                    temp += (new Double(DecoMN90.depth - 15) / new Double(up_dive_speed.getText()));
                    series.add(temp.doubleValue(), -15);
                    airconsumption += amv * (new Double(DecoMN90.depth - 15) / new Double(up_dive_speed.getText())) * 2.5;
                    temp += DecoMN90.getDecompressionStops().get(15);
                    series.add(temp.doubleValue(), -15);
                    airconsumption += amv * DecoMN90.getDecompressionStops().get(15) * 2.5;
                }
                if (DecoMN90.getDecompressionStops().get(12) != null) {
                    if (DecoMN90.getDecompressionStops().get(15) == null) {
                        temp += (new Double(DecoMN90.depth - 12) / new Double(up_dive_speed.getText()));
                        airconsumption += amv * (new Double(DecoMN90.depth - 12) / new Double(up_dive_speed.getText())) * 2.5;
                    } else {
                        temp += (new Double(3) / new Double(up_dive_speed.getText()));
                        airconsumption += amv * (new Double(3) / new Double(up_dive_speed.getText())) * 2.5;
                    }
                    series.add(temp.doubleValue(), -12);
                    temp += DecoMN90.getDecompressionStops().get(12);
                    series.add(temp.doubleValue(), -12);
                    airconsumption += amv * DecoMN90.getDecompressionStops().get(12) * 2.2;
                }
                if (DecoMN90.getDecompressionStops().get(9) != null) {
                    if (DecoMN90.getDecompressionStops().get(12) == null) {
                        temp += (new Double(DecoMN90.depth - 9) / new Double(up_dive_speed.getText()));
                        airconsumption += amv * (new Double(DecoMN90.depth - 9) / new Double(up_dive_speed.getText())) * 1.9;
                    } else {
                        temp += (new Double(3) / new Double(up_dive_speed.getText()));
                        airconsumption += amv * (new Double(3) / new Double(up_dive_speed.getText())) * 1.9;
                    }
                    series.add(temp.doubleValue(), -9);
                    temp += DecoMN90.getDecompressionStops().get(9);
                    series.add(temp.doubleValue(), -9);
                    airconsumption += amv * DecoMN90.getDecompressionStops().get(9) * 1.9;
                }
                if (DecoMN90.getDecompressionStops().get(6) != null) {
                    if (DecoMN90.getDecompressionStops().get(9) == null) {
                        temp += (new Double(DecoMN90.depth - 6) / new Double(up_dive_speed.getText()));
                        airconsumption += amv * (new Double(DecoMN90.depth - 6) / new Double(up_dive_speed.getText())) * 1.6;
                    } else {
                        temp += (new Double(3) / new Double(up_dive_speed.getText()));
                        airconsumption += amv * (new Double(3) / new Double(up_dive_speed.getText())) * 1.6;
                    }
                    series.add(temp.doubleValue(), -6);
                    temp += DecoMN90.getDecompressionStops().get(6);
                    series.add(temp.doubleValue(), -6);
                    airconsumption += amv * DecoMN90.getDecompressionStops().get(6) * 1.6;
                }
                if (DecoMN90.getDecompressionStops().get(3) != null) {
                    if (DecoMN90.getDecompressionStops().get(6) == null) {
                        temp += (new Double(DecoMN90.depth - 3) / new Double(up_dive_speed.getText()));
                        airconsumption += amv * (new Double(DecoMN90.depth - 3) / new Double(up_dive_speed.getText())) * 1.3;
                    } else {
                        temp += (new Double(3) / new Double(up_dive_speed.getText()));
                        airconsumption += amv * (new Double(3) / new Double(up_dive_speed.getText())) * 1.3;
                    }
                    series.add(temp.doubleValue(), -3);
                    temp += DecoMN90.getDecompressionStops().get(3);
                    series.add(temp.doubleValue(), -3);
                    airconsumption += amv * DecoMN90.getDecompressionStops().get(3) * 1.3;
                }
                // the surface
                temp += (new Double(3) / new Double(up_dive_speed.getText()));
                airconsumption += amv * (new Double(3) / new Double(up_dive_speed.getText())) * 1.3;
            } else {
                temp += (new Double(DecoMN90.depth) / new Double(up_dive_speed.getText()));
                airconsumption += amv * (new Double(DecoMN90.depth) / new Double(up_dive_speed.getText())) * 1.3;
            }
            temp_up_dive_time += temp;
            series.add(temp.doubleValue(), 0);

            // set the complete_duration and up dive duration
            DecoMN90.setComplete_duration(temp);
            DecoMN90.setComplete_up_dive_duration(temp_up_dive_time);

            XYSeriesCollection data_xy = new XYSeriesCollection(series);
            return data_xy;
        }

        
        /**
         * The volume of the tank
         * @return the volume of the tank
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
         * Set the profile of the dive
         *
         */
        private void setDiveProfile() {
            diveprofilePanel.removeAll();
            diveprofilePanel.add(getChartPanel(getDivedata_chart(diveProfile_data())));
        }

        
        /**
         * Method for the actions when an action happens
         */
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == calculateButton) {
                String divedata = new String();
                amv = new Double(amvField.getText());

                // calculate the depth and the duration or this table
                if (repeating_groups.getSelectedIndex() != -1 && !surface_time.getText().trim().equals("")) {
                    DecoMN90.initialize(new Integer(getMax_depth().getText()), new Integer(getDuration().getText()), (String) repeating_groups
                            .getSelectedItem(), new Integer(surface_time.getText()), new Double(o2_ratio.getText()), null); // TODO take care of descent rate
                } else {
                    DecoMN90.initialize(new Integer(getMax_depth().getText()), new Integer(getDuration().getText()), "", 0, new Double(o2_ratio.getText()), null);  // TODO take care of descent rate
                }

                if (repeating_groups.getSelectedIndex() != -1 && !surface_time.getText().trim().equals("")) {
                    divedata += Messages.getString("diveplanning.repeat_dive") + "\n";
                    divedata += Messages.getString("diveplanning.add_on_time") + ": \t" + DecoMN90.getAdd_dive_time() + "\n";
                    divedata += "======================================================================================\n";
                }

                // set the dive profile
                setDiveProfile();
                // calculate cns
                Double ppo = NitroxCalculationUtil.calculatePPO(DecoMN90.getDepth().doubleValue(), new Double(o2_ratio.getText()));
                Double singleCNS = null;
                Double dailyCNS = null;
                if (ppo <= 1.6) {
                    singleCNS = NitroxCalculationUtil.getSingleDiveCNS(ppo, DecoMN90.getDuration().doubleValue());
                    dailyCNS = NitroxCalculationUtil.getDailyCNS(ppo, DecoMN90.getDuration().doubleValue());
                    if (DecoMN90.getDecompressionStops() != null) {
                        Iterator<Integer> it = DecoMN90.getDecompressionStops().keySet().iterator();
                        while (it.hasNext()) {
                            Integer depth = it.next();
                            Integer duration = DecoMN90.getDecompressionStops().get(depth);
                            ppo = NitroxCalculationUtil.calculatePPO(depth.doubleValue(), new Double(o2_ratio.getText()));
                            singleCNS += NitroxCalculationUtil.getSingleDiveCNS(ppo, duration.doubleValue());
                            dailyCNS += NitroxCalculationUtil.getDailyCNS(ppo, duration.doubleValue());
                        }
                    }
                }
                // Zero time for the dive
                divedata += Messages.getString("diveplanning.zerohour") + ": \t" + DecoMN90.getZeroHour() + " ";
                divedata += Messages.getString("minutes") + "\t";
                divedata += Messages.getString("diveplanning.repeating_group") + ": \t" + (DecoMN90.getRepeatGroup()==null?"":DecoMN90.getRepeatGroup()) + "\t";
                divedata += Messages.getString("diveplanning.no_flight") + ": \t" + (DecoMN90.no_flight.get(DecoMN90.getRepeatGroup())==null?"":DecoMN90.no_flight.get(DecoMN90.getRepeatGroup())
                        + " [h]") + "\n";
                divedata += Messages.getString("diveplanning.complete_duration") + ": \t" + Math.round(DecoMN90.getComplete_duration()) + " ["
                        + UnitConverter.getDisplayTimeUnit() + "]\t";
                divedata += Messages.getString("diveplanning.complete_up_dive_duration") + ": \t\t"
                        + Math.round(DecoMN90.getComplete_up_dive_duration()) + " [" + UnitConverter.getDisplayTimeUnit() + "]\n";
                divedata += Messages.getString("diveplanning.air_consumption") + ": \t" + Math.round(airconsumption) + " [bar l]\t"
                        + Messages.getString("diveplanning.tank_volume") + ": \t\t" + this.getTankVolume() + " [" + UnitConverter.getDisplayVolumeUnit()
                        + "]\t";
                divedata += Messages.getString("diveplanning.ead") + ": \t" + Math.round(NitroxCalculationUtil.calculateEAD(DecoMN90.getDepth().doubleValue(), new Double(o2_ratio.getText()))) + " ["
                        + UnitConverter.getDisplayAltitudeUnit() + "]\n";
                if (singleCNS != null && dailyCNS != null) {
                    divedata += Messages.getString("diveplanning.single_cns")+": \t"+CNS_FORMAT.format(singleCNS)
                    +"%\t\t"+Messages.getString("diveplanning.daily_cns")+": \t"+CNS_FORMAT.format(dailyCNS)+"%\n";
                } else {
                    divedata += Messages.getString("diveplanning.cns_warning")+"\n";
                }
                divedata += "======================================================================================\n";
                // the duration is to long
                if (DecoMN90.getDecompressionStops() == null
                        && (DecoMN90.getZeroHour() == null || DecoMN90.getZeroHour() < new Integer(getDuration().getText()))
                        || DecoMN90.getDecompressionStops() != null
                        && (DecoMN90.getDecompressionStops().get(3) == null && DecoMN90.getZeroHour() < new Integer(getDuration()
                                .getText()))) {
                    new MessageDialog(mainWindow, Messages.getString("diveplanning"), Messages.getString("diveplanning.to_long_duration"), null, MessageDialog.MessageType.ERROR);
                    this.repaint();
                } else {
                    // decompression stops for the dive
                    if (DecoMN90.getDecompressionStops() != null && DecoMN90.getDecompressionStops().get(3) != null) {
                        divedata += Messages.getString("diveplanning.duration_on_3m") + ": \t";
                        divedata += DecoMN90.getDecompressionStops().get(3) + " ";
                        divedata += Messages.getString("minutes") + "\n";
                    }
                    if (DecoMN90.getDecompressionStops() != null && DecoMN90.getDecompressionStops().get(6) != null) {
                        divedata += Messages.getString("diveplanning.duration_on_6m") + ": \t";
                        divedata += DecoMN90.getDecompressionStops().get(6) + " ";
                        divedata += Messages.getString("minutes") + "\n";
                    }
                    if (DecoMN90.getDecompressionStops() != null && DecoMN90.getDecompressionStops().get(9) != null) {
                        divedata += Messages.getString("diveplanning.duration_on_9m") + ": \t";
                        divedata += DecoMN90.getDecompressionStops().get(9) + " ";
                        divedata += Messages.getString("minutes") + "\n";
                    }
                    if (DecoMN90.getDecompressionStops() != null && DecoMN90.getDecompressionStops().get(12) != null) {
                        divedata += Messages.getString("diveplanning.duration_on_12m") + ": \t";
                        divedata += DecoMN90.getDecompressionStops().get(12) + " ";
                        divedata += Messages.getString("minutes") + "\n";
                    }
                    if (DecoMN90.getDecompressionStops() != null && DecoMN90.getDecompressionStops().get(15) != null) {
                        divedata += Messages.getString("diveplanning.duration_on_15m") + ": \t";
                        divedata += DecoMN90.getDecompressionStops().get(15) + " ";
                        divedata += Messages.getString("minutes") + "\n";
                    }
                    dive_data.setText(divedata);
                }
            }
        }

    }

    /**
     * The bottom duration of the time
     * @return the bottom duration
     */
    public Integer getDuration() {
        return duration;
    }

    
    /**
     * Set the bottom time
     * @param duration : the time under the water
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * Get the depth of the dive
     * @return the depth
     */
    public Integer getDepth() {
        return depth;
    }

    
    /**
     * Set the depth of the dive
     * @param depth
     */
    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    
    /**
     * Get the complete time duration
     * @return the complete time duration
     */
    public Double getComplete_duration() {
        return complete_duration;
    }

    
    /**
     * Set the complete duration of the dive
     * @param complete_duration the time to set
     */
    public void setComplete_duration(Double complete_duration) {
        this.complete_duration = complete_duration;
    }

    /**
     * To get the time from bottom to surface
     * @return the time
     */
    public Double getComplete_up_dive_duration() {
        return complete_up_dive_duration;
    }

    /**
     * Set the time to go from bottom to surface
     * @param complete_up_dive_duration the time
     */
    public void setComplete_up_dive_duration(Double complete_up_dive_duration) {
        this.complete_up_dive_duration = complete_up_dive_duration;
    }

    
    /**
     * Get the time to add to the real dive time
     * @return the time to add
     */
    public Integer getAdd_dive_time() {
        return add_dive_time;
    }
    
    
    /**
     * Set the time to add to the real dive time
     * @param add_dive_time the time to add
     */
    public void setAdd_dive_time(Integer add_dive_time) {
        this.add_dive_time = add_dive_time;
    }

}
