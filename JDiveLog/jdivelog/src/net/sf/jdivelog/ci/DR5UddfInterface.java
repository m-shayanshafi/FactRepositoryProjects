/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DecosoftX1Interface.java
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
package net.sf.jdivelog.ci;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Properties;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.statusbar.StatusInterface;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.JDiveLog;
import net.sf.jdivelog.model.uddf.UddfException;
import net.sf.jdivelog.model.uddf.UddfFileParser;

public class DR5UddfInterface implements ComputerInterface {
    
    public static final String DRIVER_NAME = "DR5 UDDF-Files";
    private static final String PROPERTY_DIRECTORY = "dr5.uddf.directory";
    
    private File directory;
    private TreeSet<JDive> dives;
    private DR5UddfInterfaceConfiguration configPanel;
    
    public DR5UddfInterface() {
        directory = null;
        dives = null;
    }

    public DR5UddfInterfaceConfiguration getConfigurationPanel() {
        if (configPanel == null) {
            configPanel = new DR5UddfInterfaceConfiguration();
        }
        return configPanel;
    }

    public TreeSet<JDive> getDives() {
        return dives;
    }

    public String getDriverName() {
        return DRIVER_NAME;
    }

    public String[] getPropertyNames() {
        return new String[] { PROPERTY_DIRECTORY };
    }

    public void initialize(Properties properties) {
        if (properties != null) {
            String dir = (String) properties.get(PROPERTY_DIRECTORY);
            if (dir != null) {
                directory = new File(dir);
            } else {
                directory = new File(System.getProperty("user.home"));
            }
            getConfigurationPanel().getDirectoryField().setText(dir);
        }
    }

    public Properties saveConfiguration() {
        Properties props = new Properties();
        props.put(PROPERTY_DIRECTORY, getConfigurationPanel().getDirectory().getPath());
        return props;
    }

    public void transfer(StatusInterface statusInterface, JDiveLog logbook) throws TransferException, NotInitializedException, InvalidConfigurationException {
        dives = new TreeSet<JDive>();
        if (directory == null) {
            throw new NotInitializedException();
        }
        if (!directory.exists()) {
            throw new InvalidConfigurationException(Messages.getString("directory_does_not_exist"));
        }
        if (!directory.isDirectory()) {
            throw new InvalidConfigurationException(Messages.getString("not_a_directory")+": "+directory.getPath());
        }
        if (!directory.canRead()) {
            throw new InvalidConfigurationException(Messages.getString("directory_not_readable"));
        }
        String[] files = directory.list(new DR5UddfFilenameFilter());
        for (int i=0; i<files.length; i++) {
            TreeSet<JDive> importedDives;
			try {
				importedDives = parseFile(files[i], logbook);
			} catch (UddfException e) {
				throw new TransferException("UDDF Parsing Error: "+e.getMessage());
			}
            if (dives != null) {
                dives.addAll(importedDives);
            }
        }
    }
    
    private TreeSet<JDive> parseFile(String filename, JDiveLog logbook) throws UddfException {
        File file = new File(directory, filename);
        UddfFileParser uddfFileParser = new UddfFileParser();
		return uddfFileParser.parse(file);
    }

    private static class DR5UddfFilenameFilter implements FilenameFilter {
        
        private static final String PATTERN_PREFIX = "^[lL][oO][gG]_[uU][dD][dD][fF]_";
        private static final String PATTERN_POSTFIX = "_[0-9]+.uddf";
        private String pattern;
        
        public DR5UddfFilenameFilter() {
            StringBuffer sb = new StringBuffer(PATTERN_PREFIX);
            sb.append(PATTERN_POSTFIX);
            pattern = sb.toString();
        }

        public boolean accept(File dir, String name) {
            if (name != null) {
                return name.matches(pattern);
            }
            return false;
        }

    }
    
    private class DR5UddfInterfaceConfiguration extends JPanel {

        private static final long serialVersionUID = 1L;
        private JTextField directoryField;
        private JButton openDirectoryButton;
        
        public DR5UddfInterfaceConfiguration() {
            init();
        }
        
        public File getDirectory() {
            return new File(getDirectoryField().getText());
        }
        
        private void init() {
            setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(5,5,5,5);
            gc.anchor = GridBagConstraints.WEST;
            gc.gridy = 0;
            gc.gridx = 0;
            add(new JLabel(Messages.getString("directory")), gc);
            gc.gridx = 1;
            add(getDirectoryField(), gc);
            gc.gridx = 2;
            add(getOpenDirectoryButton());
        }
        
        private JTextField getDirectoryField() {
            if (directoryField == null) {
                directoryField = new JTextField();
                directoryField.setPreferredSize(new Dimension(200, directoryField.getPreferredSize().height));
            }
            return directoryField;
        }

        private JButton getOpenDirectoryButton() {
            if (openDirectoryButton == null) {
                openDirectoryButton = new JButton(new ImageIcon(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/open.gif"))); //$NON-NLS-1$
                openDirectoryButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fc = new JFileChooser();
                        File f = new File(getDirectoryField().getText());
                        if (f.exists() && f.isDirectory()) {
                            fc.setCurrentDirectory(f);
                        }
                        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        int ret = fc.showOpenDialog(DR5UddfInterfaceConfiguration.this);
                        if (ret == JFileChooser.APPROVE_OPTION) {
                            getDirectoryField().setText(fc.getSelectedFile().getPath());
                        }
                    }
                    
                });
            }
            return openDirectoryButton;
        }

    }

}
