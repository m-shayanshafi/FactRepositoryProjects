/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ReportManager.java
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
package net.sf.jdivelog.printing.report;

import java.util.HashMap;
import java.util.TreeSet;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.printing.Report;

public class ReportManager {
    
    public HashMap<String, Report> reportsByName;
    
    public ReportManager() {
        reportsByName = new HashMap<String, Report>();
        addReport(new XslFoReport(Messages.getString("report.divelist_a4"), getClass().getResourceAsStream("divelist_a4.xsl")));
        addReport(new XslFoReport(Messages.getString("report.divelist_a5"), getClass().getResourceAsStream("divelist_a5.xsl")));
        addReport(new XslFoReport(Messages.getString("report.divelist_simple_a4"), getClass().getResourceAsStream("divelist_simple_a4.xsl")));
        addReport(new XslFoReport(Messages.getString("report.divelist_simple_a5"), getClass().getResourceAsStream("divelist_simple_a5.xsl")));
    }
    
    public String[] getReportNames() {
        TreeSet<String> set = new TreeSet<String>(reportsByName.keySet());
        String[] names = new String[set.size()];
        return set.toArray(names);
    }
    
    public Report getReportByName(String name) {
        return reportsByName.get(name);
    }
    
    //
    // private methods
    //
    
    private void addReport(Report r) {
        reportsByName.put(r.getName(), r);
    }

}
