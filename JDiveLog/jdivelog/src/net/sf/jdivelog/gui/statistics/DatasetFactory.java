/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DatasetFactory.java
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
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
package net.sf.jdivelog.gui.statistics;

import java.util.Iterator;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class DatasetFactory {
    
    public static PieDataset getPieDataset(ChartData data) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Iterator<String> it = data.getNames().iterator();
        while (it.hasNext()) {
            String name = it.next();
            dataset.setValue(name, data.getCount(name));
        }
        return dataset;
    }
    
    public static CategoryDataset getCategoryDataset(ChartData data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Iterator<String> it = data.getNames().iterator();
        while(it.hasNext()) {
            String name = it.next();
            dataset.setValue(data.getCount(name), "", name);
        }
        return dataset;
    }
    
    public static CategoryDataset getCategoryDataset(CountChartData3D data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(String row : data.getRowNames()) {
            for(String col : data.getColNames()) {
                dataset.setValue(data.getDouble(row, col), row, col);
            }
        }
        return dataset;
    }
    
}
