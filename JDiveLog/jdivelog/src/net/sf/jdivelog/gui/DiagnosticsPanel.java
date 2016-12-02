/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DiagnosticsPanel.java
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
package net.sf.jdivelog.gui;

import java.awt.BorderLayout;
import java.io.File;
import java.util.Arrays;
import java.util.StringTokenizer;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.sf.jdivelog.comm.CommUtil;
import net.sf.jdivelog.comm.CommUtil.CommApi;
import net.sf.jdivelog.gui.commands.UndoableCommand;

/**
 * Not really a Panel for configuration, but a Panel showing Diagnostic hints
 * for checking the installation.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class DiagnosticsPanel extends AbstractSettingsPanel {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -3225781268317736827L;

    private JTextArea diagnosticArea;

    /**
     * Default Constructor
     */
    public DiagnosticsPanel() {
        super();
        setLayout(new BorderLayout());
        add(new JScrollPane(getDiagnosticArea()), BorderLayout.CENTER);
    }

    /**
     * @see net.sf.jdivelog.gui.AbstractSettingsPanel#load()
     */
    @Override
    public void load() {
        StringBuffer sb = new StringBuffer();
        
        sb.append("Comm-Environment\n");
        sb.append("================\n\n");
        CommUtil util = CommUtil.getInstance();
        sb.append("Comm API: "+util.isSupported(CommApi.JAVA_COMM_API)+"\n");
        sb.append("RXTX-2.1: "+util.isSupported(CommApi.RXTX_2_1)+"\n");
        if (util.isSupported(CommApi.JAVA_COMM_API) && util.isSupported(CommApi.RXTX_2_1)) {
            sb.append("WARNING: Comm API AND RXTX-2.1 installed, this may cause problems!!!\n");
        } else if (!util.isSupported(CommApi.JAVA_COMM_API) && !util.isSupported(CommApi.RXTX_2_1)) {
            sb.append("ERROR: Neither Comm API nor RXTX-2.1 installed, No Dive Computer support!!!\n");
        }
        sb.append("\n");
        sb.append("Comm Init Log: \n");
        sb.append(util.getInitProtocol());
        
        sb.append("\n");
        sb.append("Comm Libs:\n");
        String pathSeparator = System.getProperty("path.separator");
        String libPath = System.getProperty("java.library.path");
        StringTokenizer st = new StringTokenizer(libPath, pathSeparator);
        while (st.hasMoreTokens()) {
            String path = st.nextToken();
            File f = new File(path);
            if (!f.exists()) {
                sb.append("Native Lib Dir does not exist: "+f.getAbsolutePath()+"\n");
            } else if (!f.isDirectory()) {
                sb.append("Native Lib Dir is not a directory: "+f.getAbsolutePath()+"\n");
            } else if (!f.canRead()) {
                sb.append("Native Lib Dir is not readable: "+f.getAbsolutePath()+"\n");
            } else {
                File[] libs = f.listFiles();
                for (int i=0; i<libs.length; i++) {
                    String fn = libs[i].getName();
                    if (fn != null && fn.toLowerCase().indexOf("rxtx") > -1) {
                        sb.append("Possible RXTX Lib found: "+libs[i].getAbsolutePath()+" ("+libs[i].length()+" bytes, read:"+libs[i].canRead()+")\n");
                    }
                }
            }
        }
        sb.append("\n");
        sb.append("Comm Jars in java.ext.dirs:\n");
        String javaExtDir = System.getProperty("java.ext.dirs");
        if (javaExtDir != null) {
            File dir = new File(javaExtDir);
            if (!dir.exists()) {
                sb.append("java.ext.dirs does not exist: "+javaExtDir+"\n");
            } else if (!dir.canRead()) {
                sb.append("java.ext.dirs is not readable: "+javaExtDir+"\n");
            } else if (!dir.isDirectory()) {
                sb.append("java.ext.dirs is not a directory: "+javaExtDir+"\n");
            } else {
                File[] jars = dir.listFiles();
                for (int i=0; i<jars.length; i++) {
                    String fn = jars[i].getName();
                    if (fn != null && fn.toLowerCase().indexOf("comm") > -1) {
                        sb.append("Possible Comm Jar found: "+jars[i].getAbsolutePath()+" ("+jars[i].length()+" bytes, read:"+jars[i].canRead()+")\n");
                    }
                }
            }
        }
        
        sb.append("\n\n");
        sb.append("Path Information\n");
        sb.append("================\n");
        sb.append("working dir: "+System.getProperty("user.dir")+"\n");
        sb.append("home dir: "+System.getProperty("user.home")+"\n");
        String skinpath = System.getProperty("skindir");
        sb.append("skinpath: "+skinpath+"\n");
        if (skinpath != null) {
            File skindir = new File(skinpath);
            sb.append("skindir: "+skindir.getAbsolutePath()+"\n");
            sb.append("skindir readable: "+skindir.canRead()+"\n");
            sb.append("skindir is directory: "+skindir.isDirectory()+"\n");
            if (skindir.exists() && skindir.canRead() && skindir.isDirectory()) {
                sb.append("skindir content: "+skindir.list() == null || skindir.list().length == 0 ? null : Arrays.asList(skindir.list())+"\n");
            }
        }
        
        sb.append("\n\n");
        sb.append("JVM Information\n");
        sb.append("===============\n");
        appendSystemProperty(sb, "java.version");
        appendSystemProperty(sb, "java.vendor");
        appendSystemProperty(sb, "java.vendor.url");
        appendSystemProperty(sb, "java.home");
        appendSystemProperty(sb, "java.vm.specification.version");
        appendSystemProperty(sb, "java.vm.specification.vendor");
        appendSystemProperty(sb, "java.vm.specification.name");
        appendSystemProperty(sb, "java.vm.version");
        appendSystemProperty(sb, "java.vm.vendor");
        appendSystemProperty(sb, "java.vm.name");
        appendSystemProperty(sb, "java.specification.version");
        appendSystemProperty(sb, "java.specification.vendor");
        appendSystemProperty(sb, "java.specification.name");
        appendSystemProperty(sb, "java.class.version");
        appendSystemProperty(sb, "java.class.path");
        appendSystemProperty(sb, "java.library.path");
        appendSystemProperty(sb, "java.io.tmpdir");
        appendSystemProperty(sb, "java.compiler");
        appendSystemProperty(sb, "java.ext.dirs");
        
        sb.append("\n\n");
        sb.append("OS Information\n");
        sb.append("==============\n");
        appendSystemProperty(sb, "os.name");
        appendSystemProperty(sb, "os.arch");
        appendSystemProperty(sb, "os.version");
        appendSystemProperty(sb, "file.separator");
        appendSystemProperty(sb, "path.separator");
        appendSystemProperty(sb, "user.name");

        getDiagnosticArea().setText(sb.toString());
    }
    
    private void appendSystemProperty(StringBuffer sb, String property) {
        sb.append(property);
        sb.append(": ");
        try {
            sb.append(System.getProperty(property));
        } catch (Throwable t) {
            sb.append("== ERROR == "+t.toString());
        }
        sb.append("\n");
    }

    private JTextArea getDiagnosticArea() {
        if (diagnosticArea == null) {
            diagnosticArea = new JTextArea();
//            diagnosticArea.setEnabled(false);
            diagnosticArea.setEditable(false);
        }
        return diagnosticArea;
    }

    /**
     * @see net.sf.jdivelog.gui.AbstractSettingsPanel#getSaveCommand()
     */
    @Override
    public UndoableCommand getSaveCommand() {
        return new DummySaveCommand();
    }

    //
    // inner classes
    //

    private class DummySaveCommand implements UndoableCommand {

        public void undo() {
        }

        public void redo() {
        }

        public void execute() {
        }

    }

}
