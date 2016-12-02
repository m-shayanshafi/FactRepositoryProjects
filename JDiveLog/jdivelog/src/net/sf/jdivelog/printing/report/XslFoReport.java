/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: XslFoReport.java
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

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.jdivelog.printing.Report;

public class XslFoReport implements Report {
    
    private String name;
    private Templates translet;
    
    public XslFoReport(String name, InputStream xsl) {
        this.name = name;
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            translet = tFactory.newTemplates(new StreamSource(xsl));
        } catch (TransformerConfigurationException tce) {
            throw new RuntimeException(tce);
        }
    }
    
    public String getName() {
        return name;
    }

    public void run(InputStream in, OutputStream out) {
        try {
            Transformer t = translet.newTransformer();
            t.transform(new StreamSource(in), new StreamResult(out));
        } catch (TransformerConfigurationException tce) {
            throw new RuntimeException(tce);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

}
