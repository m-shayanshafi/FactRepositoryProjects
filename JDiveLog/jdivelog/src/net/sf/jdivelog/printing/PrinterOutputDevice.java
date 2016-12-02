/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: PrinterOutputDevice.java
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
package net.sf.jdivelog.printing;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

import net.sf.jdivelog.gui.JDiveLogException;
import net.sf.jdivelog.gui.resources.Messages;

import org.apache.fop.apps.MimeConstants;

public class PrinterOutputDevice implements OutputDevice {
    
    private static final Logger LOGGER = Logger.getLogger(PrinterOutputDevice.class.getName());

    private PipedOutputStream out;

    private String mimetype;

    public PrinterOutputDevice(String printername) {
        PrintService service = getService(printername);
        DocFlavor flavor = selectPreferredFlavor(service.getSupportedDocFlavors());
        mimetype = flavor.getMimeType();
        out = new PipedOutputStream();
        try {
            InputStream in = new BufferedInputStream(new PipedInputStream(out));
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            // Prints 1 copies
            aset.add(new Copies(1));
            // Specifies the size of the medium
            // aset.add(MediaSize.ISO.A4);
            // Prints two-sided
            // aset.add(Sides.DUPLEX);
            // Staples the pages
            // aset.add(FinishingsBinding.STAPLE);
            PrintThread pt = new PrintThread(in, flavor, null, service, aset);
            pt.start();
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "could not instantiate PrinterOutputDevice '"+printername+"'", ioe);
            throw new JDiveLogException(Messages.getString("printing.failed"), null, ioe);
        }
    }

    public OutputStream getOutputStream() {
        return out;
    }

    public static String[] getAvailablePrinters() {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        String[] names = new String[services.length];
        for (int i = 0; i < services.length; i++) {
            names[i] = services[i].getName();
        }
        return names;
    }

    public static String getDefaultPrinter() {
        return PrintServiceLookup.lookupDefaultPrintService().getName();
    }

    public String getExpectedMimeType() {
        return mimetype;
    }

    //
    // private methods
    //

    private PrintService getService(String printername) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for (int i = 0; i < services.length; i++) {
            if (printername.equals(services[i].getName())) {
                return services[i];
            }
        }
        return null;
    }

    private DocFlavor selectPreferredFlavor(DocFlavor[] flavors) {
        String[] mimeTypes = getMimeTypes();
        for (int i = 0; i < mimeTypes.length; i++) {
            for (int j = 0; j < flavors.length; j++) {
                if (flavors[j].getMimeType().equals(mimeTypes[i]) && "java.io.InputStream".equals(flavors[j].getRepresentationClassName())) {
                    return flavors[j];
                }
            }
        }
        return null;
    }

    private String[] getMimeTypes() {
        String[] mimeTypes = new String[] { MimeConstants.MIME_POSTSCRIPT, MimeConstants.MIME_PCL, MimeConstants.MIME_PCL_ALT, MimeConstants.MIME_PDF };
        return mimeTypes;
    }

    //
    // inner classes
    //

    private class PrintThread extends Thread {
        private InputStream in;

        private DocFlavor flavor;

        private DocAttributeSet attributes;

        private PrintService service;

        private PrintRequestAttributeSet reqAttrs;

        public PrintThread(InputStream in, DocFlavor flavor, DocAttributeSet attributes, PrintService service, PrintRequestAttributeSet reqAttrs) {
            this.in = in;
            this.flavor = flavor;
            this.attributes = attributes;
            this.service = service;
            this.reqAttrs = reqAttrs;
        }

        public void run() {
            SimpleDoc doc = new SimpleDoc(in, flavor, attributes);
            DocPrintJob job = service.createPrintJob();
            try {
                job.print(doc, reqAttrs);
            } catch (PrintException e) {
                LOGGER.log(Level.SEVERE, "Error occured while printing", e);
                throw new JDiveLogException(Messages.getString("printing.failed"), null, e);
            }
        }
    }

}
