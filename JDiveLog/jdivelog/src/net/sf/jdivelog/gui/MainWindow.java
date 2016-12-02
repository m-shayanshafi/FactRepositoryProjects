/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: MainWindow.java
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
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import mvplan.main.Mvplan;
import net.sf.jdivelog.ci.ComputerInterface;
import net.sf.jdivelog.ci.DriverManager;
import net.sf.jdivelog.ci.InvalidConfigurationException;
import net.sf.jdivelog.ci.NotInitializedException;
import net.sf.jdivelog.ci.TransferException;
import net.sf.jdivelog.gui.commands.Command;
import net.sf.jdivelog.gui.commands.CommandAssemblyDives;
import net.sf.jdivelog.gui.commands.CommandDeleteDives;
import net.sf.jdivelog.gui.commands.CommandDeleteSites;
import net.sf.jdivelog.gui.commands.CommandHtmlExport;
import net.sf.jdivelog.gui.commands.CommandLoadFile;
import net.sf.jdivelog.gui.commands.CommandManager;
import net.sf.jdivelog.gui.commands.CommandManagerListener;
import net.sf.jdivelog.gui.commands.CommandNewFile;
import net.sf.jdivelog.gui.commands.CommandSaveFile;
import net.sf.jdivelog.gui.commands.UndoableCommand;
import net.sf.jdivelog.gui.gasblending.GasBlendingPanel;
import net.sf.jdivelog.gui.gasblending.GasOverflowPanel;
import net.sf.jdivelog.gui.ostc.OSTCSettingsPanel;
import net.sf.jdivelog.gui.ostc.flash.OSTCFlashPanel;
import net.sf.jdivelog.gui.ostc.sim.DiveSimulationPanel;
import net.sf.jdivelog.gui.printing.PrintWindow;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.statusbar.StatusBar;
import net.sf.jdivelog.gui.util.ExtensionFileFilter;
import net.sf.jdivelog.gui.util.TableSorter;
import net.sf.jdivelog.model.DiveSite;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.JDiveLog;
import net.sf.jdivelog.model.Mix;
import net.sf.jdivelog.model.MixDatabase;
import net.sf.jdivelog.model.Picture;
import net.sf.jdivelog.model.aladin.AladinFileLoader;
import net.sf.jdivelog.model.cressi.CressiFileLoader;
import net.sf.jdivelog.model.cressi.CressiLGB;
import net.sf.jdivelog.model.datatrak.DataTrakFileLoader;
import net.sf.jdivelog.model.jdivelog.JDiveLogFileLoader;
import net.sf.jdivelog.model.macdivelog.MacDiveLogFileLoader;
import net.sf.jdivelog.model.sdm2.SDM2Dive;
import net.sf.jdivelog.model.sdm2.SDM2FileLoader;
import net.sf.jdivelog.model.smart.SmartFileLoader;
import net.sf.jdivelog.model.udcf.UdcfFileLoader;
import net.sf.jdivelog.model.wlog.WLOGFileLoader;
import net.sf.jdivelog.util.UnitConverter;


/**
 * @author pascal
 */
public class MainWindow extends JFrame implements ActionListener, CommandManagerListener, WindowListener, LogbookChangeListener {

    private static final Logger LOGGER = Logger.getLogger(MainWindow.class.getName());

    private static final long serialVersionUID = 3545800996174770486L;
    
    private JPanel mainPanel = new JPanel();
    private DiveDetailPanel detailPane = null;
    private CommandManager commandManager = CommandManager.getInstance();
    private File file = null;
    private JDiveLog logBook = new JDiveLog();
    private LogBookTableModel logBookTableModel = null;
    private DivesiteTableModel diveSiteTableModel = null;
    private JPanel jContentPane = null;
    private JMenuBar jJMenuBar = null;
    private JMenu fileMenu = null;
    private JMenu editMenu = null;
    private JMenu helpMenu = null;
    private JMenuItem exitMenuItem = null;
    private JMenuItem aboutMenuItem = null;
    private JMenuItem undoMenuItem = null;
    private JMenuItem redoMenuItem = null;
    private JMenuItem saveMenuItem = null;
    private JMenuItem saveAsMenuItem = null;
    private JMenuItem buddylistMenuItem = null;
    private JMenuItem divetypeMenuItem = null;
    private JMenuItem diveactivityMenuItem = null;
    private JMenuItem equipmentMenuItem = null;
    private JToolBar jToolBar = null;
    private JToolBar jToolBarLogBook = null;
    private JToolBar diveSiteToolBar = null;
    private JTabbedPane jDivePlanningTabbedPane = null;
    private JTabbedPane jDiveComputerTabbedPane = null;    
    private JTabbedPane jConfigurationTabbedPane = null;    
    private JPanel buttonLogBookPanel = null;
    private JPanel diveSiteButtonPanel = null;
    private JPanel infoPanel = null;
    private JButton fileNewButton = null;
    private JPanel logBookPanel = null;
    private JPanel mvplanPanel = null;    
    private Mvplan mvplan = null;
    private JTable logBookTable = null;
    private JPanel diveSitePanel = null;
    private DocumentsPanel documentsPanel = null;
    private JTable diveSiteTable = null;
    private JButton fileOpenButton = null;
    private JButton fileSaveButton = null;
    private JMenuItem openMenuItem = null;
    private JMenuItem newMenuItem = null;
    private JMenuItem printMenuItem = null;
    private JMenuItem importMenuItem = null;
    private JMenuItem importWLOGMenuItem = null;
    private JMenuItem importAladinMenuItem = null;
    private JMenuItem importJDivelogMenuItem = null;
    private JMenuItem importSmartMenuItem = null;
    private JMenuItem importDataTrakMenuItem = null;
    private JMenuItem importSDEMenuItem = null;
    private JMenuItem importCressiMenuItem = null;
    private JMenuItem importMacDiveLogItem = null;
    private JMenuItem importCressiLgbMenuItem = null;
    private JMenuItem settingsMenuItem = null;
    private StatisticPanel statisticPanel = null;
    private DivePlanningPanel diveplanningPanel = null;
    private GasBlendingPanel gasBlendingPanel = null;
    private GasOverflowPanel gasOverflowPanel = null;
    private JButton htmlExportButton = null;
    private JScrollPane logBookTablePane = null;
    private JScrollPane diveSiteTablePane = null;
    private JLabel count_divesLabel = null;
    private JLabel complete_divetimeLabel = null;
    private JLabel average_depthLabel = null;
    private JLabel average_amvLabel = null;
    private JLabel average_temperaturLabel = null;
    private JTextField count_divesField = null;
    private JTextField complete_divetimeField = null;
    private JTextField average_depthField = null;
    private JTextField average_amvField = null;
    private JTextField average_temperaturField = null;
    private JMenuItem downloadMenuItem;
    private JMenuItem renumberingMenuItem;
    private JMenu extraMenu;
    private JMenuItem slideshowMenuItem;
    private TableSorter diveSiteModel;
    private TableSorter logBookModel;
    private JButton newDiveButton = null;
    private JButton deleteDiveButton = null;
    private JButton assemblyDiveButton = null;
    private JButton newSiteButton = null;
    private JButton deleteSiteButton = null;
    private JButton downloadButton = null;

    private JLabel searchLabel = null;
    private JTextField searchField = null;    
    private JButton searchButton = null;
    
    private JToggleButton logBookButton = null;
    private JToggleButton documentsButton = null;
    private JToggleButton divePlanningButton = null;
    private JToggleButton diveComputerButton = null;
    private JToggleButton diveStatisticButton = null;
    private JToggleButton configurationButton = null;
    private ButtonGroup toolbarButtons = null;
    
    private StatusBar statusBar;

    private SuuntoConfigurationPanel suuntoSettingsPanel;

    private OSTCSettingsPanel ostcSettingsPanel;

    private OSTCFlashPanel ostcFlashPanel;

    private DiveSimulationPanel ostcSimulationPanel;
    private LogbookChangeNotifier logbookChangeNotifier;
    private MixDatabase gasDatabase;

    private DiveComputerDownloadThread downloadThread;

    public MainWindow() {
        super(Messages.getString("jdivelog")); //$NON-NLS-1$
        logbookChangeNotifier = new LogbookChangeNotifier();
        logbookChangeNotifier.addLogbookChangeListener(this);
        initialize();

        SwingUtilities.invokeLater(new Runnable() {
            public void run(){               
                sortLogBookModel();
                getLogBookTable().requestFocusInWindow();
                getLogBookTable().changeSelection(0, 0, false, false);
                int row = logBookTable.getSelectedRow();
                if (row > -1)
                {
                    JDive dive = (JDive) logBookModel.getRow(row);
                    viewDive(dive);
                }
            }
        });
    }
    
    public LogbookChangeNotifier getLogbookChangeNotifier() {
        return logbookChangeNotifier;
    }

    public void sortLogBookModel() {
        logBookModel.sortColumn(0, isStartSortEnabled());
    }
    
    
    public void viewDive(JDive dive) {
        int lastSelectedTab = -1;
        if (detailPane != null) {
            lastSelectedTab = detailPane.getSelectedTab();
            mainPanel.remove(detailPane);
        }
        detailPane = new DiveDetailPanel(null, this, dive, logBook.getMasterdata(), true);
        if (lastSelectedTab != -1) {
            detailPane.setSelectedTab(lastSelectedTab);
        }
        mainPanel.add(detailPane);
    }

    private void editDive(JDive dive) {
        DiveDetailWindow ddw = new DiveDetailWindow(this, this, dive, logBook.getMasterdata(), false);
        ddw.setVisible(true);
    }

    public void editSite(DiveSite site) {
        DivesiteDetailWindow ddw = new DivesiteDetailWindow(this, this, site);
        ddw.setVisible(true);
    }

    private void initialize() {
        gasDatabase = new DelegatingMixDatabase();
        JPopupMenu.setDefaultLightWeightPopupEnabled(false); // To avoid Menu disappearing behind Simulation Canvas
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/logo.gif")));
        this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        this.setJMenuBar(getJJMenuBar());
        Properties p = loadProperties();
        String width = p.getProperty(net.sf.jdivelog.JDiveLog.PROPERTY_WINDOW_WIDTH);
        String height = p.getProperty(net.sf.jdivelog.JDiveLog.PROPERTY_WINDOW_HEIGHT);
        String posx = p.getProperty(net.sf.jdivelog.JDiveLog.PROPERTY_WINDOW_X);
        String posy = p.getProperty(net.sf.jdivelog.JDiveLog.PROPERTY_WINDOW_Y);
        if (width == null || height == null) {
            // maximize the MainWindow
            this.setSize(getToolkit().getScreenSize());
        } else {
            int w = getToolkit().getScreenSize().width;
            int h = getToolkit().getScreenSize().height;
            int x = 0;
            int y = 0;
            try {
                w = Integer.parseInt(width);
                h = Integer.parseInt(height);
                x = Integer.parseInt(posx);
                y = Integer.parseInt(posy);
            } catch (NumberFormatException nfe) {
            }
            setSize(w, h);
            setLocation(x, y);
        }

        this.setContentPane(getJContentPane());
        this.setTitle(Messages.getString("jdivelog")); //$NON-NLS-1$
        this.setName(Messages.getString("jdivelog")); //$NON-NLS-1$
        this.commandManager.addCommandListener(this);
        this.addWindowListener(this);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        new MnemonicFactory(this);
        downloadThread = new DiveComputerDownloadThread();
        downloadThread.start();
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(new java.awt.BorderLayout());
            jContentPane.add(getJToolBar(), BorderLayout.NORTH);
            jContentPane.add(getLogBookPanel(), BorderLayout.CENTER);            
            jContentPane.add(getStatusBar(), BorderLayout.SOUTH);
        }
        return jContentPane;
    }

    private JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new javax.swing.JMenuBar();
            jJMenuBar.add(getFileMenu());
            jJMenuBar.add(getEditMenu());
            jJMenuBar.add(getExtraMenu());
            jJMenuBar.add(getHelpMenu());
        }
        return jJMenuBar;
    }

    /**
     * This method initializes jMenu
     *
     * @return javax.swing.JMenu
     */
    private JMenu getFileMenu() {
        if (fileMenu == null) {
            fileMenu = new javax.swing.JMenu();
            fileMenu.setText(Messages.getString("file")); //$NON-NLS-1$
            fileMenu.add(getNewMenuItem());
            fileMenu.add(getOpenMenuItem());
            fileMenu.add(getSaveMenuItem());
            fileMenu.add(getSaveAsMenuItem());
            fileMenu.addSeparator();
            fileMenu.add(getDownloadMenuItem());
            fileMenu.add(getImportMenuItem());
            fileMenu.add(getImportWLOGMenuItem());
            fileMenu.add(getImportAladinMenuItem());
            fileMenu.add(getImportJDivelogMenuItem());
            fileMenu.add(getImportSmartMenuItem());
            fileMenu.add(getImportDataTrakMenuItem());
            fileMenu.add(getImportSDEMenuItem());
            fileMenu.add(getimportCressiMenuItem());
            fileMenu.add(getimportCressiLgbMenuItem());
            fileMenu.add(getimportMacDiveLogItem());
            fileMenu.addSeparator();
            fileMenu.add(getPrintMenuItem());
            fileMenu.addSeparator();
            fileMenu.add(getSettingsMenuItem());
            fileMenu.addSeparator();
            fileMenu.add(getExitMenuItem());
        }
        return fileMenu;
    }

    /**
     * This method initializes jMenu
     *
     * @return javax.swing.JMenu
     */
    private JMenu getEditMenu() {
        if (editMenu == null) {
            editMenu = new JMenu();
            editMenu.setText(Messages.getString("edit")); //$NON-NLS-1$
            editMenu.add(getUndoMenuItem());
            editMenu.add(getRedoMenuItem());
            editMenu.addSeparator();
            editMenu.add(getRenumberingMenuItem());
            editMenu.addSeparator();
            editMenu.add(getBuddylistMenuItem());
            editMenu.add(getDiveTypeMenuItem());
            editMenu.add(getDiveActivityMenuItem());
            editMenu.add(getEquipmentMenuItem());
        }
        return editMenu;
    }

    private JMenu getExtraMenu() {
        if (extraMenu == null) {
            extraMenu = new JMenu();
            extraMenu.setText(Messages.getString("extra")); //$NON-NLS-1$
            extraMenu.add(getSlideshowMenuItem());
        }
        return extraMenu;
    }

    private JMenu getHelpMenu() {
        if (helpMenu == null) {
            helpMenu = new JMenu();
            helpMenu.setText(Messages.getString("help")); //$NON-NLS-1$
            helpMenu.setEnabled(true);
            helpMenu.add(getAboutMenuItem());
        }
        return helpMenu;
    }

    private JMenuItem getExitMenuItem() {
        if (exitMenuItem == null) {
            exitMenuItem = new JMenuItem();
            exitMenuItem.setText(Messages.getString("exit")); //$NON-NLS-1$
            exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    exit();
                }
            });
        }
        return exitMenuItem;
    }

    private JMenuItem getAboutMenuItem() {
        if (aboutMenuItem == null) {
            aboutMenuItem = new JMenuItem();
            aboutMenuItem.setText(Messages.getString("about")); //$NON-NLS-1$
            aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String message = "JDiveLog @version@\nhttp://www.jdivelog.org/\n\nCopyright (C) 2005 - 2009 Pascal Pellmont <jdivelog@ppo2.ch>\n\n" + //$NON-NLS-1$
                            "JDiveLog is distributed under the terms of the GPL v2.\n\n" + //$NON-NLS-1$
                            "JDiveLog comes with ABSOLUTELY NO WARRANTY!\n\nBe sure you have read and understand the LICENSE file."; //$NON-NLS-1$
                    JOptionPane.showMessageDialog(MainWindow.this, message, Messages.getString("about"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
                }
            });
        }
        return aboutMenuItem;
    }

    private JMenuItem getUndoMenuItem() {
        if (undoMenuItem == null) {
            undoMenuItem = new JMenuItem();
            undoMenuItem.setText(Messages.getString("undo")); //$NON-NLS-1$
            undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, java.awt.Event.CTRL_MASK, true));
            undoMenuItem.setEnabled(commandManager.canUndo());
            undoMenuItem.addActionListener(this);
        }
        return undoMenuItem;
    }

    private JMenuItem getRedoMenuItem() {
        if (redoMenuItem == null) {
            redoMenuItem = new JMenuItem();
            redoMenuItem.setText(Messages.getString("redo")); //$NON-NLS-1$
            redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, java.awt.Event.CTRL_MASK, true));
            redoMenuItem.setEnabled(commandManager.canRedo());
            redoMenuItem.addActionListener(this);
        }
        return redoMenuItem;
    }

    private JMenuItem getRenumberingMenuItem() {
        if (renumberingMenuItem == null) {
            renumberingMenuItem = new JMenuItem();
            renumberingMenuItem.setText(Messages.getString("renumbering")); //$NON-NLS-1$
            renumberingMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, Event.CTRL_MASK, true));
            renumberingMenuItem.addActionListener(this);
        }
        return renumberingMenuItem;
    }

    public JMenuItem getOpenMenuItem() {
        if (openMenuItem == null) {
            openMenuItem = new JMenuItem();
            openMenuItem.setText(Messages.getString("open_file")); //$NON-NLS-1$
            openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK, true));
            openMenuItem.addActionListener(this);
        }
        return openMenuItem;
    }

    public JMenuItem getNewMenuItem() {
        if (newMenuItem == null) {
            newMenuItem = new JMenuItem();
            newMenuItem.setText(Messages.getString("new_file")); //$NON-NLS-1$
            newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK, true));
            newMenuItem.addActionListener(this);
        }
        return newMenuItem;
    }

    public JMenuItem getPrintMenuItem() {
        if (printMenuItem == null) {
            printMenuItem = new JMenuItem();
            printMenuItem.setText(Messages.getString("print")); //$NON-NLS-1$
            printMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK, true));
            printMenuItem.addActionListener(this);
        }
        return printMenuItem;
    }

    public JMenuItem getImportMenuItem() {
        if (importMenuItem == null) {
            importMenuItem = new JMenuItem();
            importMenuItem.setText(Messages.getString("import_file")); //$NON-NLS-1$
            importMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0, true));
            importMenuItem.addActionListener(this);
        }
        return importMenuItem;
    }

    public JMenuItem getImportWLOGMenuItem() {
        if (importWLOGMenuItem == null) {
            importWLOGMenuItem = new JMenuItem();
            importWLOGMenuItem.setText(Messages.getString("import_WLOG_file")); //$NON-NLS-1$
            importWLOGMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Event.CTRL_MASK, true));
            importWLOGMenuItem.addActionListener(this);
        }
        return importWLOGMenuItem;
    }

    public JMenuItem getImportAladinMenuItem() {
        if (importAladinMenuItem == null) {
            importAladinMenuItem = new JMenuItem();
            importAladinMenuItem.setText(Messages.getString("import_Aladin_file")); //$NON-NLS-1$
            importAladinMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.CTRL_MASK, true));
            importAladinMenuItem.addActionListener(this);
        }
        return importAladinMenuItem;
    }

    public JMenuItem getImportJDivelogMenuItem() {
        if (importJDivelogMenuItem == null) {
        	importJDivelogMenuItem = new JMenuItem();
        	importJDivelogMenuItem.setText(Messages.getString("import_JDivelog_file")); //$NON-NLS-1$
        	importJDivelogMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, Event.CTRL_MASK, true));
        	importJDivelogMenuItem.addActionListener(this);
        }
        return importJDivelogMenuItem;
    }

    public JMenuItem getImportSmartMenuItem() {
        if (importSmartMenuItem == null) {
            importSmartMenuItem = new JMenuItem();
            importSmartMenuItem.setText(Messages.getString("import_Smart_file")); //$NON-NLS-1$
            importSmartMenuItem.addActionListener(this);
        }
        return importSmartMenuItem;
    }

    public JMenuItem getImportSDEMenuItem() {
        if (importSDEMenuItem == null) {
            importSDEMenuItem = new JMenuItem();
            importSDEMenuItem.setText(Messages.getString("import_suunto2_file")); //$NON-NLS-1$
            importSDEMenuItem.addActionListener(this);
        }
        return importSDEMenuItem;
    }

    public JMenuItem getimportCressiMenuItem() {
        if (importCressiMenuItem == null) {
            importCressiMenuItem = new JMenuItem();
            importCressiMenuItem.setText(Messages.getString("import_cressi_file")); //$NON-NLS-1$
            importCressiMenuItem.addActionListener(this);
        }
        return importCressiMenuItem;
    }

    public JMenuItem getimportMacDiveLogItem() {
        if (importMacDiveLogItem == null) {
            importMacDiveLogItem = new JMenuItem();
            importMacDiveLogItem.setText(Messages.getString("import_MacDiveLog_file")); //$NON-NLS-1$
            importMacDiveLogItem.addActionListener(this);
        }
        return importMacDiveLogItem;
    }

    public JMenuItem getimportCressiLgbMenuItem() {
        if (importCressiLgbMenuItem == null) {
            importCressiLgbMenuItem = new JMenuItem();
            importCressiLgbMenuItem.setText(Messages.getString("import_cressi_lgb_file")); //$NON-NLS-1$
            importCressiLgbMenuItem.addActionListener(this);
        }
        return importCressiLgbMenuItem;
    }

    public JMenuItem getImportDataTrakMenuItem() {
        if (importDataTrakMenuItem == null) {
            importDataTrakMenuItem = new JMenuItem();
            importDataTrakMenuItem.setText(Messages.getString("import_DataTrak_file")); //$NON-NLS-1$
            importDataTrakMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.CTRL_MASK, true));
            importDataTrakMenuItem.addActionListener(this);
        }
        return importDataTrakMenuItem;
    }

    public JMenuItem getSettingsMenuItem() {
        if (settingsMenuItem == null) {
            settingsMenuItem = new JMenuItem();
            settingsMenuItem.setText(Messages.getString("settings"));
            settingsMenuItem.addActionListener(this);
        }
        return settingsMenuItem;
    }

    private JMenuItem getDownloadMenuItem() {
        if (downloadMenuItem == null) {
            downloadMenuItem = new JMenuItem(Messages.getString("download"));
            downloadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0, true));
            downloadMenuItem.addActionListener(this);
        }
        return downloadMenuItem;
    }

    private JMenuItem getSaveAsMenuItem() {
        if (saveAsMenuItem == null) {
            saveAsMenuItem = new JMenuItem();
            saveAsMenuItem.setText(Messages.getString("save_as")); //$NON-NLS-1$
            saveAsMenuItem.addActionListener(this);
        }
        return saveAsMenuItem;
    }

    private JMenuItem getBuddylistMenuItem() {
        if (buddylistMenuItem == null) {
            buddylistMenuItem = new JMenuItem();
            buddylistMenuItem.setText(Messages.getString("buddylist")); //$NON-NLS-1$
            buddylistMenuItem.addActionListener(this);
        }
        return buddylistMenuItem;
    }

    private JMenuItem getDiveTypeMenuItem() {
        if (divetypeMenuItem == null) {
            divetypeMenuItem = new JMenuItem();
            divetypeMenuItem.setText(Messages.getString("divetypemenu")); //$NON-NLS-1$
            divetypeMenuItem.addActionListener(this);
        }
        return divetypeMenuItem;
    }

    private JMenuItem getDiveActivityMenuItem() {
        if (diveactivityMenuItem == null) {
            diveactivityMenuItem = new JMenuItem();
            diveactivityMenuItem.setText(Messages.getString("diveactivitymenu")); //$NON-NLS-1$
            diveactivityMenuItem.addActionListener(this);
        }
        return diveactivityMenuItem;
    }

    private JMenuItem getEquipmentMenuItem() {
        if (equipmentMenuItem == null) {
            equipmentMenuItem = new JMenuItem();
            equipmentMenuItem.setText(Messages.getString("equipment")); //$NON-NLS-1$
            equipmentMenuItem.addActionListener(this);
        }
        return equipmentMenuItem;
    }

    private JMenuItem getSaveMenuItem() {
        if (saveMenuItem == null) {
            saveMenuItem = new JMenuItem();
            saveMenuItem.setText(Messages.getString("save")); //$NON-NLS-1$
            saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK, true));
            saveMenuItem.addActionListener(this);
        }
        return saveMenuItem;
    }

    private JMenuItem getSlideshowMenuItem() {
        if (slideshowMenuItem == null) {
            slideshowMenuItem = new JMenuItem();
            slideshowMenuItem.setText(Messages.getString("slideshow")); //$NON-NLS-1$
            slideshowMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0, true));
            slideshowMenuItem.addActionListener(this);
        }
        return slideshowMenuItem;
    }

    private JToolBar getJToolBar() {
        if (jToolBar == null) {
            toolbarButtons = new ButtonGroup();
            jToolBar = new JToolBar();
            jToolBar.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            jToolBar.setPreferredSize(new java.awt.Dimension(44, 48));
            jToolBar.add(getFileNewButton());
            jToolBar.add(getFileOpenButton());
            jToolBar.add(getFileSaveButton());
            jToolBar.addSeparator();
            toolbarButtons.add(getLogBookButton());
            toolbarButtons.add(getDivePlanningButton());
            toolbarButtons.add(getDiveComputerButton());
            toolbarButtons.add(getDiveStatisticButton());
            toolbarButtons.add(getDocumentsButton());
            toolbarButtons.add(getConfigurationButton());
            toolbarButtons.setSelected(getLogBookButton().getModel(),true);            
            jToolBar.add(getLogBookButton());
            jToolBar.add(getDivePlanningButton());
            jToolBar.add(getDiveComputerButton());
            jToolBar.add(getDiveStatisticButton());
            //jToolBar.add(getDocumentsButton());
            jToolBar.add(getConfigurationButton());
        }
        return jToolBar;
    }

    private JToolBar getJToolBarLogBook() {
        if (jToolBarLogBook == null) {
            jToolBarLogBook = new JToolBar();
            jToolBarLogBook.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            jToolBarLogBook.setPreferredSize(new java.awt.Dimension(44, 48));
            jToolBarLogBook.add(getButtonLogBookPanel());
        }
        return jToolBarLogBook;
    }

    private JToolBar getDiveSiteToolBar() {
        if (diveSiteToolBar == null) {
            diveSiteToolBar = new JToolBar();
            diveSiteToolBar.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            diveSiteToolBar.setPreferredSize(new java.awt.Dimension(44, 48));
            diveSiteToolBar.add(getDiveSiteButtonPanel());
        }
        return diveSiteToolBar;
    }


    private JTabbedPane getJDivePlanningTabbedPane() {
        if (jDivePlanningTabbedPane == null) {
            jDivePlanningTabbedPane = new JTabbedPane();
            jDivePlanningTabbedPane.addTab(Messages.getString("mvplan"), null, getMvplanPanel(), null); //$NON-NLS-1$
            jDivePlanningTabbedPane.addTab(Messages.getString("gasblending"), null, getGasBlendingPanel(), null); //$NON-NLS-1$
            jDivePlanningTabbedPane.addTab(Messages.getString("gasoverflow"), null, getGasOverflowPanel(), null); //$NON-NLS-1$
            jDivePlanningTabbedPane.addTab(Messages.getString("deco2000diveplanning"), null, getDivePlanningPanel(), null); //$NON-NLS-1$
        }
        return jDivePlanningTabbedPane;
    }

    private JTabbedPane getJDiveComputerTabbedPane() {
        if (jDiveComputerTabbedPane == null) {
            jDiveComputerTabbedPane = new JTabbedPane();
            jDiveComputerTabbedPane.addTab(Messages.getString("ostc_settings"), null, getOSTCSettingsPanel(), null); // $NON-NLS-1$
            jDiveComputerTabbedPane.addTab(Messages.getString("ostc_flashutil"), null, getOSTCFlashPanel(), null); // $NON-NLS-1$
            jDiveComputerTabbedPane.addTab(Messages.getString("ostc_simulation"), null, getOSTCSimulationPanel(), null); // $NON-NLS-1$
            jDiveComputerTabbedPane.addTab(Messages.getString("suunto_settings"), null, getSuuntoSettingsPanel(), null); //$NON-NLS-1$
        }
        return jDiveComputerTabbedPane;
    }

    private JTabbedPane getJConfigurationTabbedPane() {
        if (jConfigurationTabbedPane == null) {
            jConfigurationTabbedPane = new JTabbedPane();
            jConfigurationTabbedPane.addTab(Messages.getString("divesites"), null, getDiveSitePanel(), null); //$NON-NLS-1$
            jConfigurationTabbedPane.addTab(Messages.getString("diveactivity"), null, new DiveActivityWindow(this, this, true, logbookChangeNotifier).getJContentPane(), null); //$NON-NLS-1$
            jConfigurationTabbedPane.addTab(Messages.getString("divetype"), null,  new DiveTypeWindow(this, this, true, logbookChangeNotifier).getJContentPane(), null); //$NON-NLS-1$
            jConfigurationTabbedPane.addTab(Messages.getString("buddy"), null,  new BuddyWindow(this, this, true, logbookChangeNotifier).getJContentPane(), null); //$NON-NLS-1$
            jConfigurationTabbedPane.addTab(Messages.getString("equipment"), null,  new EquipmentWindow(this, true, logbookChangeNotifier).getContentPanel(), null); //$NON-NLS-1$
        }
        return jConfigurationTabbedPane;
    }

    
    public JPanel getMvplanPanel() {
        if (mvplanPanel == null) {
            mvplanPanel = new JPanel();
            mvplan = new Mvplan(true, this);
            mvplan.getmvplanFrame().getMvPlanContentPane();
        }
        return mvplanPanel;
    }
    
    private JPanel getButtonLogBookPanel() {
        if (buttonLogBookPanel == null) {
            FlowLayout flowLayout1 = new FlowLayout();
            buttonLogBookPanel = new JPanel();
            buttonLogBookPanel.setLayout(flowLayout1);
            buttonLogBookPanel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            flowLayout1.setAlignment(java.awt.FlowLayout.LEFT);
            buttonLogBookPanel.add(getNewDiveButton(), null);
            buttonLogBookPanel.add(getDeleteDiveButton(), null);
            buttonLogBookPanel.add(getAssemblyDiveButton(), null);
            buttonLogBookPanel.add(getDownloadButton(), null);
            buttonLogBookPanel.add(getHtmlExportButton(), null);
            buttonLogBookPanel.add(getSearchLabel(),null);
            buttonLogBookPanel.add(getSearchField(),null);
            buttonLogBookPanel.add(getSearchButton(),null);
        }
        return buttonLogBookPanel;
    }

    private JPanel getDiveSiteButtonPanel() {
        if (diveSiteButtonPanel == null) {
            FlowLayout flowLayout = new FlowLayout();
            diveSiteButtonPanel = new JPanel();
            diveSiteButtonPanel.setLayout(flowLayout);
            diveSiteButtonPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            flowLayout.setAlignment(FlowLayout.LEFT);
            diveSiteButtonPanel.add(getNewSiteButton(), null);
            diveSiteButtonPanel.add(getDeleteSiteButton(), null);
        }
        return diveSiteButtonPanel;
    }

    private JPanel getInfoPanel() {
        if (infoPanel == null) {
            infoPanel = new JPanel();
            FlowLayout flowLayout1 = new FlowLayout();
            infoPanel.setLayout(flowLayout1);
            infoPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            flowLayout1.setAlignment(java.awt.FlowLayout.LEFT);
            infoPanel.setLayout(flowLayout1);
            infoPanel.add(getCount_dives(), null);
            infoPanel.add(getCount_divesField(), null);
            infoPanel.add(getComplete_divetime(), null);
            infoPanel.add(getComplete_divetimeField(), null);
            infoPanel.add(getAverage_depth(), null);
            infoPanel.add(getAverage_depthField(), null);
            infoPanel.add(getAverage_amv(), null);
            infoPanel.add(getAverage_amvField(), null);
            infoPanel.add(getAverage_temperatur(), null);
            infoPanel.add(getAverage_temperaturField(), null);
        }
        return infoPanel;
    }

    private JLabel getCount_dives() {
        if (count_divesLabel == null) {
            count_divesLabel = new JLabel(Messages.getString("count_dives"));//$NON-NLS-1$
            count_divesLabel.setForeground(Color.BLUE);
        }
        return count_divesLabel;
    }

    private JLabel getComplete_divetime() {
        if (complete_divetimeLabel == null) {
            complete_divetimeLabel = new JLabel(Messages.getString("complete_divetime") + "[DD:HH:MM]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            complete_divetimeLabel.setForeground(Color.BLACK);
        }
        return complete_divetimeLabel;
    }

    private JLabel getAverage_depth() {
        if (average_depthLabel == null) {
            average_depthLabel = new JLabel(Messages.getString("average_depth_label") + " [" + UnitConverter.getDisplayAltitudeUnit() + "]");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            average_depthLabel.setForeground(Color.BLUE);
        }
        return average_depthLabel;
    }

    public JTextField getAverage_depthField() {
        if (average_depthField == null) {
            average_depthField = new JTextField("0");//$NON-NLS-1$
            average_depthField.setForeground(Color.BLUE);
            average_depthField.setPreferredSize(new Dimension(50, 20));
            average_depthField.setEditable(false);
        }
        return average_depthField;
    }

    public JTextField getCount_divesField() {
        if (count_divesField == null) {
            count_divesField = new JTextField("0");//$NON-NLS-1$
            count_divesField.setForeground(Color.BLUE);
            count_divesField.setPreferredSize(new Dimension(50, 20));
            count_divesField.setEditable(false);
        }
        return count_divesField;
    }

    public JTextField getComplete_divetimeField() {
        if (complete_divetimeField == null) {
            complete_divetimeField = new JTextField("0");//$NON-NLS-1$
            complete_divetimeField.setForeground(Color.BLACK);
            complete_divetimeField.setPreferredSize(new Dimension(80, 20));
            complete_divetimeField.setEditable(false);
        }
        return complete_divetimeField;
    }

    private JLabel getAverage_amv() {
        if (average_amvLabel == null) {
            average_amvLabel = new JLabel(Messages.getString("average_amv_label") + " [" + UnitConverter.getDisplayAMVUnit() + "]");//$NON-NLS-1$ //$NON-NLS-23 //$NON-NLS-3$
            average_amvLabel.setForeground(Color.BLACK);
        }
        return average_amvLabel;
    }

    public JTextField getAverage_amvField() {
        if (average_amvField == null) {
            average_amvField = new JTextField("0");//$NON-NLS-1$
            average_amvField.setForeground(Color.BLACK);
            average_amvField.setPreferredSize(new Dimension(50, 20));
            average_amvField.setEditable(false);
        }
        return average_amvField;
    }

    private JLabel getSearchLabel() {
        if (searchLabel == null) {
            searchLabel = new JLabel(Messages.getString("search"));//$NON-NLS-1$ //$NON-NLS-23 //$NON-NLS-3$
            searchLabel.setForeground(Color.BLACK);
        }
        return searchLabel;
    }

    private JTextField getSearchField() {
        if (searchField == null) {
            searchField = new JTextField("");//$NON-NLS-1$
            searchField.setForeground(Color.BLACK);
            searchField.setPreferredSize(new Dimension(150, 20));
            searchField.setEditable(true);
            searchField.addKeyListener(new KeyListener() {
                public void keyTyped(KeyEvent e) {
                }

                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        searchDives();
                    }
                }

                public void keyReleased(KeyEvent e) {
                }
            });
            
        }
        return searchField;
    }

    private JButton getSearchButton() {
        if (searchButton == null) {
            searchButton = new JButton();
            searchButton.addActionListener(this);
            searchButton.setIcon(new ImageIcon(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/search.png"))); //$NON-NLS-1$
            searchButton.setToolTipText(Messages.getString("search")); //$NON-NLS-1$
            searchButton.setPreferredSize(new Dimension(32, 32));
            searchButton.setName(Messages.getString("search")); //$NON-NLS-1$
        }
        return searchButton;
    }

    private JLabel getAverage_temperatur() {
        if (average_temperaturLabel == null) {
            average_temperaturLabel = new JLabel(Messages.getString("average_temperatur_label") + " [" + UnitConverter.getDisplayTemperatureUnit() + "]");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            average_temperaturLabel.setForeground(Color.BLUE);
        }
        return average_temperaturLabel;
    }

    public JTextField getAverage_temperaturField() {
        if (average_temperaturField == null) {
            average_temperaturField = new JTextField("0");//$NON-NLS-1$
            average_temperaturField.setForeground(Color.BLUE);
            average_temperaturField.setPreferredSize(new Dimension(50, 20));
            average_temperaturField.setEditable(false);
        }
        return average_temperaturField;
    }

    private JButton getHtmlExportButton() {
        if (htmlExportButton == null) {
            htmlExportButton = new JButton();
            htmlExportButton.setIcon(new ImageIcon(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/web_export.gif"))); //$NON-NLS-1$
            htmlExportButton.setToolTipText(Messages.getString("export")); //$NON-NLS-1$
            htmlExportButton.addActionListener(this);
        }
        return htmlExportButton;
    }

    private JButton getFileNewButton() {
        if (fileNewButton == null) {
            fileNewButton = new JButton();
            fileNewButton.addActionListener(this);
            fileNewButton.setIcon(new ImageIcon(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/new.gif"))); //$NON-NLS-1$
            fileNewButton.setToolTipText(Messages.getString("new")); //$NON-NLS-1$
            fileNewButton.setName(Messages.getString("new")); //$NON-NLS-1$
        }
        return fileNewButton;
    }

    private JToggleButton getLogBookButton() {
        if (logBookButton == null) {
            logBookButton = new JToggleButton();
            logBookButton.addActionListener(this);
            logBookButton.setIcon(new ImageIcon(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/logbook.png"))); //$NON-NLS-1$
            logBookButton.setToolTipText(Messages.getString("logbook")); //$NON-NLS-1$
            logBookButton.setName(Messages.getString("logbook")); //$NON-NLS-1$
        }
        return logBookButton;
    }

    private JToggleButton getDocumentsButton() {
        if (documentsButton == null) {
            documentsButton = new JToggleButton();
            documentsButton.addActionListener(this);
            documentsButton.setIcon(new ImageIcon(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/documents.png"))); //$NON-NLS-1$
            documentsButton.setToolTipText(Messages.getString("documents")); //$NON-NLS-1$
            documentsButton.setName(Messages.getString("documents")); //$NON-NLS-1$
        }
        return documentsButton;
    }

    private JToggleButton getDivePlanningButton() {
        if (divePlanningButton == null) {
            divePlanningButton = new JToggleButton();
            divePlanningButton.addActionListener(this);
            divePlanningButton.setIcon(new ImageIcon(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/diveplanning.png"))); //$NON-NLS-1$
            divePlanningButton.setToolTipText(Messages.getString("diveplanning")); //$NON-NLS-1$
            divePlanningButton.setName(Messages.getString("diveplanning")); //$NON-NLS-1$
        }
        return divePlanningButton;
    }

    private JToggleButton getDiveComputerButton() {
        if (diveComputerButton == null) {
            diveComputerButton = new JToggleButton();
            diveComputerButton.addActionListener(this);
            diveComputerButton.setIcon(new ImageIcon(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/divecomputer.png"))); //$NON-NLS-1$
            diveComputerButton.setToolTipText(Messages.getString("divecomputer")); //$NON-NLS-1$
            diveComputerButton.setName(Messages.getString("divecomputer")); //$NON-NLS-1$
        }
        return diveComputerButton;
    }

    private JToggleButton getDiveStatisticButton() {
        if (diveStatisticButton == null) {
            diveStatisticButton = new JToggleButton();
            diveStatisticButton.addActionListener(this);
            diveStatisticButton.setIcon(new ImageIcon(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/divestatistics.png"))); //$NON-NLS-1$
            diveStatisticButton.setToolTipText(Messages.getString("divestatistic")); //$NON-NLS-1$
            diveStatisticButton.setName(Messages.getString("divestatistic")); //$NON-NLS-1$
        }
        return diveStatisticButton;
    }

    private JToggleButton getConfigurationButton() {
        if (configurationButton == null) {
            configurationButton = new JToggleButton();
            configurationButton.addActionListener(this);
            configurationButton.setIcon(new ImageIcon(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/configuration.png"))); //$NON-NLS-1$
            configurationButton.setToolTipText(Messages.getString("configuration")); //$NON-NLS-1$
            configurationButton.setName(Messages.getString("configuration")); //$NON-NLS-1$
        }
        return configurationButton;
    }

    private JPanel getLogBookPanel() {
        if (logBookPanel == null) {
            logBookPanel = new JPanel();
            if (isPreviewEnabled()) {
                logBookPanel.setName("logBookTab"); //$NON-NLS-1$
                JToolBar toolBarLogBook = getJToolBarLogBook();
                JPanel infPanel = getInfoPanel();
                mainPanel.setLayout(new java.awt.GridLayout());
                
                logBookPanel.setLayout(new BoxLayout(logBookPanel, BoxLayout.Y_AXIS));
                
                logBookPanel.add(infPanel);
                logBookPanel.add(toolBarLogBook);
                logBookPanel.add(mainPanel);
                
                mainPanel.add(getLogBookTablePane());
            } else {
                logBookPanel.setLayout(new BoxLayout(logBookPanel, BoxLayout.Y_AXIS));
                logBookPanel.setName("logBookTab"); //$NON-NLS-1$
                logBookPanel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
                logBookPanel.add(getInfoPanel(), null);
                logBookPanel.add(getJToolBarLogBook(), null);
                logBookPanel.add(getLogBookTablePane(), null);

            }
        }
        return logBookPanel;
    }

    private JScrollPane getLogBookTablePane() {
        if (logBookTablePane == null) {
            logBookTablePane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            logBookTablePane.setPreferredSize(new Dimension(800, 1000));
            logBookTablePane.setViewportView(getLogBookTable());
        }
        return logBookTablePane;
    }

    private JTable getLogBookTable() {
        if (logBookTable == null) {
            logBookModel = new TableSorter(getLogBookTableModel());
            logBookTable = new JTable();
            logBookTable.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            logBookTable.setShowVerticalLines(false);
            logBookTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            logBookTable.setShowGrid(false);
            logBookTable.setShowHorizontalLines(true);
            logBookTable.setModel(logBookModel);
            logBookTable.setIntercellSpacing(new Dimension(10, 3));
            TableColumnModel tcm = logBookTable.getColumnModel();
            tcm.getColumn(0).setResizable(true);
            tcm.getColumn(0).setPreferredWidth(40);
            tcm.getColumn(0).setHeaderValue(Messages.getString("dive_no")); //$NON-NLS-1$
            tcm.getColumn(1).setResizable(true);
            tcm.getColumn(1).setPreferredWidth(140);
            tcm.getColumn(1).setHeaderValue(Messages.getString("date")); //$NON-NLS-1$
            tcm.getColumn(2).setResizable(true);
            tcm.getColumn(2).setPreferredWidth(200);
            tcm.getColumn(2).setHeaderValue(Messages.getString("place")); //$NON-NLS-1$
            tcm.getColumn(3).setResizable(true);
            tcm.getColumn(3).setPreferredWidth(70);
            tcm.getColumn(3).setHeaderValue(Messages.getString("city")); //$NON-NLS-1$
            tcm.getColumn(4).setResizable(true);
            tcm.getColumn(4).setPreferredWidth(70);
            tcm.getColumn(4).setHeaderValue(Messages.getString("country")); //$NON-NLS-1$
            tcm.getColumn(5).setResizable(true);
            tcm.getColumn(5).setPreferredWidth(50);
            tcm.getColumn(5).setHeaderValue(Messages.getString("depth") + " [" + UnitConverter.getDisplayAltitudeUnit() + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            tcm.getColumn(6).setResizable(true);
            tcm.getColumn(6).setPreferredWidth(70);
            tcm.getColumn(6).setHeaderValue(Messages.getString("duration") + " [" + UnitConverter.getDisplayTimeUnit() + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            tcm.getColumn(7).setResizable(true);
            tcm.getColumn(7).setPreferredWidth(10);
            tcm.getColumn(7).setHeaderValue(""); //$NON-NLS-1$
            tcm.getColumn(7).setCellRenderer(new ImageCellRenderer());
            tcm.getColumn(8).setResizable(true);
            tcm.getColumn(8).setPreferredWidth(10);
            tcm.getColumn(8).setHeaderValue(""); //$NON-NLS-1$
            tcm.getColumn(8).setCellRenderer(new ImageCellRenderer());

            ListSelectionModel rowSM = logBookTable.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    //Ignore extra messages.
                    if (e.getValueIsAdjusting())
                    {
                        return;
                    }

                    ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                    if (!lsm.isSelectionEmpty()) {
                        int row = lsm.getMinSelectionIndex();
                        JDive dive = (JDive) logBookModel.getRow(row);
                        viewDive(dive);
                    }
                }
            });


            logBookModel.addMouseListenerToHeader(logBookTable);
            logBookTable.addKeyListener(new KeyListener() {
                public void keyTyped(KeyEvent e) {
                }

                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        int row = logBookTable.getSelectedRow();
                        JDive dive = (JDive) logBookModel.getRow(row);
                        editDive(dive);
                    }
                }

                public void keyReleased(KeyEvent e) {
                }
            });

            logBookTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2)
                    {
                        int row = logBookTable.getSelectedRow();
                        JDive dive = (JDive) logBookModel.getRow(row);
                        editDive(dive);
                    }
                }
            });
        }
        return logBookTable;
    }

    public LogBookTableModel getLogBookTableModel() {
        if (logBookTableModel == null) {
            logBookTableModel = new LogBookTableModel(this);
        }
        return logBookTableModel;
    }

    private JPanel getDiveSitePanel() {
        if (diveSitePanel == null) {
            diveSitePanel = new JPanel();
            diveSitePanel.setLayout(new BoxLayout(diveSitePanel, BoxLayout.Y_AXIS));
        }
        diveSitePanel.setName("diveSiteTab"); //$NON-NLS-1$
        diveSitePanel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
        diveSitePanel.add(getDiveSiteTablePane(), null);
        diveSitePanel.add(getDiveSiteToolBar(), null);
        return diveSitePanel;
    }

    private DocumentsPanel getDocumentsPanel() {
        if (documentsPanel == null) {
            documentsPanel = new DocumentsPanel(this);
        }
        return documentsPanel;
    }

    private JScrollPane getDiveSiteTablePane() {
        if (diveSiteTablePane == null) {
            diveSiteTablePane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            diveSiteTablePane.setPreferredSize(new Dimension(800, 1000));
            diveSiteTablePane.setViewportView(getDiveSiteTable());
        }
        return diveSiteTablePane;
    }

    private JTable getDiveSiteTable() {
        if (diveSiteTable == null) {
            diveSiteModel = new TableSorter(getDiveSiteTableModel());
            diveSiteTable = new JTable(diveSiteModel);
            diveSiteTable.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            diveSiteTable.setShowVerticalLines(false);
            diveSiteTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            diveSiteTable.setShowGrid(false);
            diveSiteTable.setShowHorizontalLines(true);
            diveSiteTable.setModel(diveSiteModel);
            diveSiteTable.setIntercellSpacing(new Dimension(10, 3));
            diveSiteModel.addMouseListenerToHeader(diveSiteTable);
            diveSiteTable.addKeyListener(new KeyListener() {
                public void keyTyped(KeyEvent e) {
                }

                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        int row = diveSiteTable.getSelectedRow();
                        DiveSite site = (DiveSite) diveSiteModel.getRow(row);
                        editSite(site);
                    }
                }

                public void keyReleased(KeyEvent e) {
                }
            });
            diveSiteTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int row = diveSiteTable.getSelectedRow();
                        DiveSite site = (DiveSite) diveSiteModel.getRow(row);
                        editSite(site);
                    }
                }
            });

        }
        return diveSiteTable;
    }

    public DivesiteTableModel getDiveSiteTableModel() {
        if (diveSiteTableModel == null) {
            diveSiteTableModel = new DivesiteTableModel(this);
        }
        return diveSiteTableModel;
    }

    private JButton getFileOpenButton() {
        if (fileOpenButton == null) {
            fileOpenButton = new JButton();
            fileOpenButton.addActionListener(this);
            fileOpenButton.setIcon(new ImageIcon(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/open.gif"))); //$NON-NLS-1$
            fileOpenButton.setToolTipText(Messages.getString("open")); //$NON-NLS-1$
            fileOpenButton.setName(Messages.getString("open")); //$NON-NLS-1$
        }
        return fileOpenButton;
    }

    private JButton getFileSaveButton() {
        if (fileSaveButton == null) {
            fileSaveButton = new JButton();
            fileSaveButton.addActionListener(this);
            fileSaveButton.setIcon(new ImageIcon(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/save.gif"))); //$NON-NLS-1$
            fileSaveButton.setToolTipText(Messages.getString("save")); //$NON-NLS-1$
            fileSaveButton.setName(Messages.getString("save")); //$NON-NLS-1$
        }
        return fileSaveButton;
    }

    private JButton getNewDiveButton() {
        if (newDiveButton == null) {
            newDiveButton = new JButton();
            newDiveButton.setIcon(new ImageIcon(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/dive_add.gif"))); //$NON-NLS-1$
            newDiveButton.setToolTipText(Messages.getString("new_dive")); //$NON-NLS-1$
            newDiveButton.setName(Messages.getString("new_dive")); //$NON-NLS-1$
            newDiveButton.addActionListener(this);
        }
        return newDiveButton;
    }

    private JButton getDeleteDiveButton() {
        if (deleteDiveButton == null) {
            deleteDiveButton = new JButton();
            deleteDiveButton.setIcon(new ImageIcon(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/dive_delete.gif"))); //$NON-NLS-1$
            deleteDiveButton.setToolTipText(Messages.getString("delete_dive")); //$NON-NLS-1$
            deleteDiveButton.setName(Messages.getString("delete_dive")); //$NON-NLS-1$
            deleteDiveButton.addActionListener(this);
        }
        return deleteDiveButton;
    }

    private JButton getAssemblyDiveButton() {
        if (assemblyDiveButton == null) {
            assemblyDiveButton = new JButton();
            assemblyDiveButton.setIcon(new ImageIcon(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/dive_assembly.png"))); //$NON-NLS-1$
            assemblyDiveButton.setToolTipText(Messages.getString("assembly_dive")); //$NON-NLS-1$
            assemblyDiveButton.setName(Messages.getString("assembly_dive")); //$NON-NLS-1$
            assemblyDiveButton.addActionListener(this);
        }
        return assemblyDiveButton;
    }

    private JButton getNewSiteButton() {
        if (newSiteButton == null) {
            newSiteButton = new JButton();
            // newSiteButton.setIcon(new
            // ImageIcon(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/site_add.gif")));
            // //$NON-NLS-1$
            newSiteButton.setText(Messages.getString("new_site"));
            newSiteButton.setToolTipText(Messages.getString("new_site")); //$NON-NLS-1$
            newSiteButton.setName(Messages.getString("new_site")); //$NON-NLS-1$
            newSiteButton.addActionListener(this);
        }
        return newSiteButton;
    }

    private JButton getDeleteSiteButton() {
        if (deleteSiteButton == null) {
            deleteSiteButton = new JButton();
            // deleteSiteButton.setIcon(new
            // ImageIcon(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/site_delete.gif")));
            // //$NON-NLS-1$
            deleteSiteButton.setText(Messages.getString("delete_site"));
            deleteSiteButton.setToolTipText(Messages.getString("delete_site")); //$NON-NLS-1$
            deleteSiteButton.setName(Messages.getString("delete_site")); //$NON-NLS-1$
            deleteSiteButton.addActionListener(this);
        }
        return deleteSiteButton;
    }

    private JButton getDownloadButton() {
        if (downloadButton == null) {
            downloadButton = new JButton(new ImageIcon(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/sync.gif"))); //$NON-NLS-1$
            downloadButton.setToolTipText(Messages.getString("download")); //$NON-NLS-1$
            downloadButton.setName(Messages.getString("download")); //$NON-NLS-1$
            downloadButton.addActionListener(this);
        }
        return downloadButton;
    }

    public StatisticPanel getStatisticPanel() {
        if (statisticPanel == null) {
            statisticPanel = new StatisticPanel(this);
        }
        return statisticPanel;
    }

    public DivePlanningPanel getDivePlanningPanel() {
        if (diveplanningPanel == null) {
            diveplanningPanel = new DivePlanningPanel(this);
        }
        return diveplanningPanel;
    }

    public GasBlendingPanel getGasBlendingPanel() {
        if (gasBlendingPanel == null) {
            gasBlendingPanel = new GasBlendingPanel(this);
        }
        return gasBlendingPanel;
    }

    public GasOverflowPanel getGasOverflowPanel() {
        if (gasOverflowPanel == null) {
        	gasOverflowPanel = new GasOverflowPanel(this);
        }
        return gasOverflowPanel;
    }

    public SuuntoConfigurationPanel getSuuntoSettingsPanel() {
        if (suuntoSettingsPanel == null) {
            suuntoSettingsPanel = new SuuntoConfigurationPanel(this);
        }
        return suuntoSettingsPanel;
    }

    public OSTCSettingsPanel getOSTCSettingsPanel() {
        if (ostcSettingsPanel == null) {
            ostcSettingsPanel = new OSTCSettingsPanel(this);
        }
        return ostcSettingsPanel;
    }

    public OSTCFlashPanel getOSTCFlashPanel() {
        if (ostcFlashPanel == null) {
            ostcFlashPanel = new OSTCFlashPanel(this);
        }
        return ostcFlashPanel;
    }

    public DiveSimulationPanel getOSTCSimulationPanel() {
        if (ostcSimulationPanel == null) {
            ostcSimulationPanel = new DiveSimulationPanel(this);
        }
        return ostcSimulationPanel;
    }

    public StatusBar getStatusBar() {
        if (statusBar == null) {
            statusBar = new StatusBar();
            statusBar.setPreferredSize(new Dimension(1000, 25));
            statusBar.setBackground(Color.YELLOW);
        }
        return statusBar;
    }
    
    public MixDatabase getGasDatabase() {
        return gasDatabase;
    }

    /**
     * Launches this application
     *
     * @param args
     *            The startup arguments. JDiveLog accepts only one argument: a
     *            filename
     */
    public static void main(String[] args) {
        System.setProperty("sun.awt.exception.handler", "net.sf.jdivelog.gui.JDiveLogExceptionHandler");
        MainWindow application = new MainWindow();
        JDiveLogExceptionHandler.setMainWindow(application);
        try {
            if (args.length > 0) {
                CommandLoadFile cmd = new CommandLoadFile(application, new File(args[0]));
                CommandManager.getInstance().execute(cmd);
            } else {
                Properties p = application.loadProperties();
                String filename = p.getProperty(net.sf.jdivelog.JDiveLog.PROPERTY_LASTFILE);
                if (filename != null) {
                    File f = new File(filename);
                    if (f.exists() && f.canRead()) {
                        CommandLoadFile cmd = new CommandLoadFile(application, f);
                        CommandManager.getInstance().execute(cmd);
                    }
                }
            }
        } finally {
            application.setVisible(true);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == fileOpenButton) {
            openFile();
        } else if (e.getSource() == fileNewButton || e.getSource() == newMenuItem) {
            newFile();
        } else if (e.getSource() == printMenuItem) {
            print();
        } else if (e.getSource() == fileSaveButton) {
            saveFile();
        } else if (e.getSource() == newDiveButton) {
            newDive();
        } else if (e.getSource() == searchButton) {
            searchDives();
        } else if (e.getSource() == deleteDiveButton) {
            int[] rows = getLogBookTable().getSelectedRows();
            ArrayList<JDive> dives = new ArrayList<JDive>();
            for (int i = 0; i < rows.length; i++) {
                JDive dive = (JDive) logBookModel.getRow(rows[i]);
                if (dive != null) {
                    dives.add(dive);
                }
            }
            if (dives.size() > 0) {
                deleteDives(dives);
            } else {
                // TODO PP: show error message when no dive selected
            }
        } else if (e.getSource() == assemblyDiveButton) {
            int[] rows = getLogBookTable().getSelectedRows();
            TreeSet<JDive> dives = new TreeSet<JDive>();
            for (int i = 0; i < rows.length; i++) {
                JDive dive = (JDive) logBookModel.getRow(rows[i]);
                if (dive != null) {
                    dives.add(dive);
                }
            }
            if (dives.size() > 1) {
                assemblyDives(dives);
            } else {
                // TODO PP: show error message when less than 2 dives selected
            }
        } else if (e.getSource() == newSiteButton) {
            newSite();
        } else if (e.getSource() == deleteSiteButton) {
            int[] rows = getDiveSiteTable().getSelectedRows();
            ArrayList<DiveSite> sites = new ArrayList<DiveSite>();
            for (int i = 0; i < rows.length; i++) {
                DiveSite site = (DiveSite) diveSiteModel.getRow(rows[i]);
                if (site != null) {
                    sites.add(site);
                }
            }
            if (sites.size() > 0) {
                deleteSites(sites);
            } else {
                // TODO PP: show error message no dive site selected
            }
        } else if (e.getSource() == logBookButton) {
            showLogbook();
        } else if (e.getSource() == documentsButton) {
            showDocuments();
        } else if (e.getSource() == divePlanningButton) {
            showDivePlanning();
        } else if (e.getSource() == diveComputerButton) {
            showDiveComputer();
        } else if (e.getSource() == diveStatisticButton) {
            showDivestatistics();
        } else if (e.getSource() == configurationButton) {
            showConfiguration();
        } else if (e.getSource() == downloadButton) {
            downloadFromComputer();
        } else if (e.getSource() == saveMenuItem) {
            saveFile();
        } else if (e.getSource() == saveAsMenuItem) {
            saveAsFile();
        } else if (e.getSource() == buddylistMenuItem) {
            addBuddy();
        } else if (e.getSource() == divetypeMenuItem) {
            addDiveType();
        } else if (e.getSource() == diveactivityMenuItem) {
            addDiveActivity();
        } else if (e.getSource() == undoMenuItem) {
            commandManager.undo();
        } else if (e.getSource() == redoMenuItem) {
            commandManager.redo();
        } else if (e.getSource() == openMenuItem) {
            openFile();
        } else if (e.getSource() == importMenuItem) {
            importUdcf();
        } else if (e.getSource() == importWLOGMenuItem) {
            importWLOG();
        } else if (e.getSource() == importAladinMenuItem) {
            importAladin();
        } else if (e.getSource() == importJDivelogMenuItem) {
            importJDiveLog();
        } else if (e.getSource() == importSmartMenuItem) {
            importSmart();
        } else if (e.getSource() == importDataTrakMenuItem) {
            importDataTrak();
        } else if (e.getSource() == importSDEMenuItem) {
            importSDE();
        } else if (e.getSource() == importCressiMenuItem) {
            importCressi();
        } else if (e.getSource() == importCressiLgbMenuItem) {
            importCressiLgb();
        } else if (e.getSource() == importMacDiveLogItem) {
            importMacDiveLog();
        } else if (e.getSource() == htmlExportButton) {
            exportHtml();
        } else if (e.getSource() == settingsMenuItem) {
            openSettingsWindow();
        } else if (e.getSource() == downloadMenuItem) {
            downloadFromComputer();
        } else if (e.getSource() == equipmentMenuItem) {
            editEquipment();
        } else if (e.getSource() == renumberingMenuItem) {
            renumbering();
        } else if (e.getSource() == slideshowMenuItem) {
            startSlideshow();
        }
    }

    
    private void showLogbook() {    
            getJContentPane().remove(getJContentPane().getComponent(1));
            getJContentPane().add(getLogBookPanel(), 1);
            getJContentPane().repaint();
    }
    
    private void showDocuments() {    
        getJContentPane().remove(getJContentPane().getComponent(1));
        getJContentPane().add(getDocumentsPanel(), 1);
        getJContentPane().repaint();
}

    private void showDivestatistics() {    
            getJContentPane().remove(getJContentPane().getComponent(1));
            getJContentPane().add(getStatisticPanel(), 1);
            getJContentPane().repaint();
    }
    
    private void showDivePlanning() {    
            getJContentPane().remove(getJContentPane().getComponent(1));
            getJContentPane().add(getJDivePlanningTabbedPane(), 1);
            getJContentPane().repaint();
    }

    private void showDiveComputer() {    
            getJContentPane().remove(getJContentPane().getComponent(1));
            getJContentPane().add(getJDiveComputerTabbedPane(), 1);
            getJContentPane().repaint();
    }

    private void showConfiguration() {    
            getJContentPane().remove(getJContentPane().getComponent(1));
            getJContentPane().add(getJConfigurationTabbedPane(), 1);
            getJContentPane().repaint();
    }

    private void openSettingsWindow() {
        updateDownloadInterval(0);
        SettingsWindow settingsWindow = new SettingsWindow(this, this);
        settingsWindow.setVisible(true);
        settingsWindow.addWindowListener(new WindowListener() {

            public void windowActivated(WindowEvent e) {
            }

            public void windowClosed(WindowEvent e) {
                updateDownloadInterval(getLogBook().getComputerDownloadInterval());
            }

            public void windowClosing(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowOpened(WindowEvent e) {
            }
            
        });
    }

    protected void addBuddy() {
        BuddyWindow bdw = new BuddyWindow(null, this, logbookChangeNotifier);
        bdw.setVisible(true);
    }

    protected void addDiveType() {
        DiveTypeWindow dtw = new DiveTypeWindow(null, this, logbookChangeNotifier);
        dtw.setVisible(true);
    }

    protected void addDiveActivity() {
        DiveActivityWindow daw = new DiveActivityWindow(null, this, logbookChangeNotifier);
        daw.setVisible(true);
    }

    private void editEquipment() {
        EquipmentWindow ew = new EquipmentWindow(this, logbookChangeNotifier);
        ew.setVisible(true);
    }

    private void newDive() {
        JDive dive = new JDive();
        dive.setDiveNumber(getLogBook().getNextDiveNumber());
        editDive(dive);
    }

    private void searchDives() {
        getLogBookTableModel().setSearchString(getSearchField().getText());
        getLogBookTableModel().fireTableDataChanged();
        sortLogBookModel();
    }

    
    
    private void deleteDives(ArrayList<JDive> dives) {
        UndoableCommand cmd = new CommandDeleteDives(this, dives);
        CommandManager.getInstance().execute(cmd);
    }

    private void assemblyDives(TreeSet<JDive> dives) {
        Command cmd = new CommandAssemblyDives(this, dives);
        CommandManager.getInstance().execute(cmd);
    }

    private void newSite() {
        DiveSite site = new DiveSite();
        editSite(site);
    }

    private void deleteSites(ArrayList<DiveSite> sites) {
        UndoableCommand cmd = new CommandDeleteSites(this, sites);
        CommandManager.getInstance().execute(cmd);
    }

    private void exit() {
        downloadThread.abort();
        if (mvplan !=null) { 
            mvplan.getmvplanFrame().savePrefs();
        }
        if (isChanged()) {
            if (askSave()) {
                saveDefaults();
                System.exit(0);
            }
        } else {
            saveDefaults();
            System.exit(0);
        }
    }

    private void startSlideshow() {
        int[] rows = getLogBookTable().getSelectedRows();
        ArrayList<Picture> pictures = new ArrayList<Picture>();
        for (int i = 0; i < rows.length; i++) {
            ArrayList<Picture> tmp = ((JDive) logBookModel.getRow(rows[i])).getPictures();
            if (tmp != null) {
                pictures.addAll(tmp);
            }
        }
        if (pictures.size() > 0) {
            PictureViewerWindow pvw = new PictureViewerWindow(this, pictures, getLogBook());
            pvw.setVisible(true);
        } else {
            new MessageDialog(this, Messages.getString("error.cannot_start_slideshow"), Messages.getString("message.no_images_selected"), null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$  //$NON-NLS-2$
        }
    }

    private boolean askSave() {
        int ret = JOptionPane
                .showConfirmDialog(
                        this,
                        Messages.getString("message.data_has_not_been_saved_save_now"), Messages.getString("message.data_has_been_modified"), JOptionPane.YES_NO_CANCEL_OPTION, //$NON-NLS-1$ //$NON-NLS-2$
                        JOptionPane.WARNING_MESSAGE);
        if (ret == JOptionPane.NO_OPTION) {
            return true;
        }
        if (ret == JOptionPane.YES_OPTION) {
            saveFile();
            return true;
        }
        return false;
    }

    private void saveDefaults() {
        try {
            Properties p = loadProperties();
            p.setProperty(net.sf.jdivelog.JDiveLog.PROPERTY_WINDOW_X, String.valueOf(getX()));
            p.setProperty(net.sf.jdivelog.JDiveLog.PROPERTY_WINDOW_Y, String.valueOf(getY()));
            p.setProperty(net.sf.jdivelog.JDiveLog.PROPERTY_WINDOW_WIDTH, String.valueOf(getWidth()));
            p.setProperty(net.sf.jdivelog.JDiveLog.PROPERTY_WINDOW_HEIGHT, String.valueOf(getHeight()));
            if (file != null) {
                p.setProperty(net.sf.jdivelog.JDiveLog.PROPERTY_LASTFILE, file.getPath());
            }
            File wd = FileChooser.getWorkingDirectory();
            if (wd != null && wd.exists() && wd.isDirectory()) {
                String dir = wd.getPath();
                p.setProperty(net.sf.jdivelog.JDiveLog.PROPERTY_WORKING_DIRECTORY, dir);
            }
            p.store(new FileOutputStream(net.sf.jdivelog.JDiveLog.CONFIG_FILE), "JDiveLog properties");
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.WARNING, "could not save defaults",e);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "could not save defaults",e);
        }

    }

    private Properties loadProperties() {
        Properties p = new Properties();
        if (net.sf.jdivelog.JDiveLog.CONFIG_FILE.exists() && net.sf.jdivelog.JDiveLog.CONFIG_FILE.canRead()) {
            try {
                p.load(new FileInputStream(net.sf.jdivelog.JDiveLog.CONFIG_FILE));
            } catch (FileNotFoundException e) {
                LOGGER.log(Level.WARNING, "could not load defaults",e);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "could not load defaults",e);
            }
        }
        return p;
    }

    private void exportHtml() {
        CommandHtmlExport cmd = new CommandHtmlExport(this);
        commandManager.execute(cmd);
    }
    private void importUdcf() {
        ExtensionFileFilter ff = new ExtensionFileFilter(Messages.getString("udcf_files"), "udcf"); //$NON-NLS-1$
        ff.addExtension("udcf"); //$NON-NLS-1$
        FileChooser fc = new FileChooser();
        fc.setFileFilter(ff);
        int ret = fc.showOpenDialog(this);
        if (ret == FileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                UdcfFileLoader ufl = new UdcfFileLoader(f);
                ImportWindow iw = new ImportWindow(this, ufl.getJDives());
                iw.setVisible(true);
            } catch (Exception ex) {
                throw new JDiveLogException(Messages.getString("error.could_not_open_file"), Messages.getString("error.could_not_open_file")+" "+f.getName(), ex);
            }
        }
    }

    private void renumbering() {
        getLogBookTable().selectAll();
        int[] rows = getLogBookTable().getSelectedRows();
        for (int i = 0; i < rows.length; i++) {
            JDive dive = (JDive) logBookModel.getRow(rows[i]);
            dive.setDiveNumber(new Long(i+1));
        }
        setChanged(true);
    }

    private void importWLOG() {
        ExtensionFileFilter ff = new ExtensionFileFilter(Messages.getString("wlog_files"), "csv"); //$NON-NLS-1$
        ff.addExtension("csv"); //$NON-NLS-1$
        FileChooser fc = new FileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setFileFilter(ff);
        int ret = fc.showOpenDialog(this);
        if (ret == FileChooser.APPROVE_OPTION) {
            File[] f = fc.getSelectedFiles();
            try {
                new WLOGFileLoader(this, f);
            } catch (Exception ex) {
                throw new JDiveLogException(Messages.getString("error.could_not_open_file"), null, ex);
            }
        }
    }

    public void updateDownloadInterval(int newInterval) {
        downloadThread.setInterval(newInterval);
    }

    private void downloadFromComputer() {
        final ComputerInterface driver = DriverManager.getInstance().getInterface(getLogBook().getComputerDriver());
        if (driver != null) {

            Thread downloader = new Thread() {
                @Override
                public void run() {
                    getStatusBar().messageInfo(Messages.getString("transferring_data"));
                    driver.initialize(getLogBook().getComputerSettings());

                    try {
                        driver.transfer(getStatusBar(), getLogBook());
                        TreeSet<JDive> dives = driver.getDives();
                        ImportWindow iw = new ImportWindow(MainWindow.this, dives);
                        iw.setVisible(true);
                    } catch (TransferException e) {
                        new MessageDialog(MainWindow.this, Messages.getString("transfer_failed"), e.getMessage(), null, MessageDialog.MessageType.ERROR);
                    } catch (NotInitializedException e) {
                        new MessageDialog(MainWindow.this, Messages.getString("transfer_failed"), e.getMessage(), null, MessageDialog.MessageType.ERROR);
                    } catch (InvalidConfigurationException e) {
                        new MessageDialog(MainWindow.this, Messages.getString("transfer_failed"), e.getMessage(), null, MessageDialog.MessageType.ERROR);
                    }
                    getStatusBar().messageClear();
                };
            };
            downloader.start();
        } else {
            new MessageDialog(MainWindow.this, Messages.getString("transfer_failed"), Messages.getString("driver_not_set"), null, MessageDialog.MessageType.ERROR);
        }
    }
    
    private void backgroundDownloadFromComputer() {
        if (getLogBook() != null && getLogBook().getComputerDriver() != null) {            
            final ComputerInterface driver = DriverManager.getInstance().getInterface(getLogBook().getComputerDriver());
            if (driver != null) {
                getStatusBar().messageInfo(Messages.getString("transferring_data"));
                driver.initialize(getLogBook().getComputerSettings());
        
                try {
                    driver.transfer(getStatusBar(), getLogBook());
                    TreeSet<JDive> dives = driver.getDives();
                    ImportWindow iw = new ImportWindow(MainWindow.this, dives);
                    if (iw.hasSelectedRows()) {
                        iw.setVisible(true);
                    }
                } catch (TransferException e) {
                    new MessageDialog(MainWindow.this, Messages.getString("transfer_failed"), e.getMessage(), null, MessageDialog.MessageType.ERROR);
                } catch (NotInitializedException e) {
                    new MessageDialog(MainWindow.this, Messages.getString("transfer_failed"), e.getMessage(), null, MessageDialog.MessageType.ERROR);
                } catch (InvalidConfigurationException e) {
                    new MessageDialog(MainWindow.this, Messages.getString("transfer_failed"), e.getMessage(), null, MessageDialog.MessageType.ERROR);
                }
                getStatusBar().messageClear();
            }
        }
    }
    private void importJDiveLog() {
        ExtensionFileFilter ff = new ExtensionFileFilter(Messages.getString("JDiveLog_files"), "jlb"); //$NON-NLS-1$
        ff.addExtension("jlb"); //$NON-NLS-1$
        FileChooser fc = new FileChooser();
        fc.setMultiSelectionEnabled(false);
        fc.setFileFilter(ff);
        int ret = fc.showOpenDialog(this);
        if (ret == FileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                JDiveLogFileLoader jdiveLogLoader = new JDiveLogFileLoader(this, f);
                jdiveLogLoader.load();
            } catch (Exception ex) {
                throw new JDiveLogException(Messages.getString("error.could_not_open_file"), Messages.getString("error.could_not_open_file")+" "+f.getName(), ex);
            }
        }
    }

    private void importAladin() {
        ExtensionFileFilter ff = new ExtensionFileFilter(Messages.getString("aladin_files"), "log"); //$NON-NLS-1$
        ff.addExtension("log"); //$NON-NLS-1$
        FileChooser fc = new FileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setFileFilter(ff);
        int ret = fc.showOpenDialog(this);
        if (ret == FileChooser.APPROVE_OPTION) {
            File[] f = fc.getSelectedFiles();
            try {
                new AladinFileLoader(this, f);
            } catch (Exception ex) {
                throw new JDiveLogException(Messages.getString("error.could_not_open_file"), null, ex);
            }
        }
    }

    private void importCressi() {
        ExtensionFileFilter ff = new ExtensionFileFilter(Messages.getString("cressi_files"), "csv"); //$NON-NLS-1$
        ff.addExtension("csv"); //$NON-NLS-1$
        FileChooser fc = new FileChooser();
        fc.setMultiSelectionEnabled(false);
        fc.setFileFilter(ff);
        int ret = fc.showOpenDialog(this);
        if (ret == FileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                CressiFileLoader cfl = new CressiFileLoader(this, f);
                cfl.Load();
            } catch (Exception ex) {
                throw new JDiveLogException(Messages.getString("error.could_not_open_file"), Messages.getString("error.could_not_open_file")+" "+f.getName(), ex);
            }
        }
    }

    private void importCressiLgb() {
        /* filter for original Cress LGB files */
        ExtensionFileFilter ff = null;

        /* file selection dialog */
        FileChooser fc = new FileChooser();

        /* value returned by file selection dialog execution */
        int ret = 0;

        /* loader of Cressi LGB files */
        CressiLGB lgb = null;

        ff = new ExtensionFileFilter(Messages.getString(
            "cressi_lgb_files"), "lgb"); //$NON-NLS-1$
        ff.addExtension("lgb"); //$NON-NLS-1$

        fc.setMultiSelectionEnabled(false);
        fc.setFileFilter(ff);

        ret = fc.showOpenDialog(this);
        if (ret == FileChooser.APPROVE_OPTION) {
            try {
                lgb = new CressiLGB(fc.getSelectedFile());
                lgb.Load(this, logBook);
            } catch (EOFException ex) {
                /* do nothing */
            } catch (IOException ex) {
                throw new JDiveLogException(
                    Messages.getString("error.could_not_open_file"),
                    Messages.getString("error.could_not_open_file") + " " +
                        fc.getSelectedFile().getName(),
                    ex);
            }
        }
    }

    private void importMacDiveLog() {
        ExtensionFileFilter ff = new ExtensionFileFilter(Messages.getString("macDiveLog_files"), "xml"); //$NON-NLS-1$
        ff.addExtension("xml"); //$NON-NLS-1$
        FileChooser fc = new FileChooser();
        fc.setMultiSelectionEnabled(false);
        fc.setFileFilter(ff);
        int ret = fc.showOpenDialog(this);
        if (ret == FileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                MacDiveLogFileLoader macDieveLogLoader = new MacDiveLogFileLoader(this, f);
                macDieveLogLoader.Load();
            } catch (Exception ex) {
                throw new JDiveLogException(Messages.getString("error.could_not_open_file"), Messages.getString("error.could_not_open_file")+" "+f.getName(), ex);
            }
        }
    }

    private void importSDE() {
        ExtensionFileFilter ff = new ExtensionFileFilter(Messages.getString("sde_files"), "sde"); //$NON-NLS-1$
        ff.addExtension("sde"); //$NON-NLS-1$
        FileChooser fc = new FileChooser();
        fc.setMultiSelectionEnabled(false);
        fc.setFileFilter(ff);
        int ret = fc.showOpenDialog(this);
        if (ret == FileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                SDM2FileLoader sfl = new SDM2FileLoader(f);
                TreeSet<SDM2Dive> sds = sfl.getDives();
                ImportSDM2Window importWindow = new ImportSDM2Window(this, this, sds);
                importWindow.setVisible(true);
            } catch (Exception ex) {
                throw new JDiveLogException(Messages.getString("error.could_not_open_file"), Messages.getString("error.could_not_open_file")+" "+f.getName(), ex);
            }
        }
    }

    private void importSmart() {
        ExtensionFileFilter ff = new ExtensionFileFilter(Messages.getString("smart_files"), "data"); //$NON-NLS-1$
        ff.addExtension("data"); //$NON-NLS-1$
        FileChooser fc = new FileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setFileFilter(ff);
        int ret = fc.showOpenDialog(this);
        if (ret == FileChooser.APPROVE_OPTION) {
            File[] f = fc.getSelectedFiles();
            try {
                new SmartFileLoader(this, f);
            } catch (Exception ex) {
                throw new JDiveLogException(Messages.getString("error.could_not_open_file"), null, ex);
            }
        }
    }

    private void importDataTrak() {
        ExtensionFileFilter ff = new ExtensionFileFilter(Messages.getString("datatrak_files"), "log"); //$NON-NLS-1$
        ff.addExtension("log"); //$NON-NLS-1$
        FileChooser fc = new FileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setFileFilter(ff);
        int ret = fc.showOpenDialog(this);
        if (ret == FileChooser.APPROVE_OPTION) {
            File[] f = fc.getSelectedFiles();
            try {
                new DataTrakFileLoader(this, f);
            } catch (Exception ex) {
                throw new JDiveLogException(Messages.getString("error.could_not_open_file"), null, ex);
            }
        }
    }

    private void openFile() {
        if (isChanged()) {
            if (!askSave()) {
                return;
            }
        }
        ExtensionFileFilter ff = new ExtensionFileFilter(Messages.getString("jdivelog_files"), "jlb"); //$NON-NLS-1$
        ff.addExtension("jlb"); //$NON-NLS-1$
        FileChooser fc = new FileChooser();
        fc.setFileFilter(ff);
        int ret = fc.showOpenDialog(this);
        if (ret == FileChooser.APPROVE_OPTION) {
            CommandLoadFile cmd = new CommandLoadFile(this, fc.getSelectedFile());
            CommandManager.getInstance().execute(cmd);
        }
    }

    private void newFile() {
        if (isChanged()) {
            if (!askSave()) {
                return;
            }
        }
        CommandNewFile cmd = new CommandNewFile(this);
        CommandManager.getInstance().execute(cmd);
    }

    private void saveFile() {
        if (getFile() == null) {
            saveAsFile();
        } else {
            CommandSaveFile cmd = new CommandSaveFile(this, getFile());
            CommandManager.getInstance().execute(cmd);
        }
    }

    private void saveAsFile() {
        ExtensionFileFilter ff = new ExtensionFileFilter(Messages.getString("jdivelog_files"), "jlb"); //$NON-NLS-1$
        ff.addExtension("jlb"); //$NON-NLS-1$
        FileChooser fc = new FileChooser();
        fc.setFileFilter(ff);
        int ret = fc.showSaveDialog(this);
        if (ret == FileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            if (f.getName().indexOf(".") < 0) { //$NON-NLS-1$
                String path = f.getPath();
                f = new File(path + ".jlb"); //$NON-NLS-1$
            }
            if (f.exists()) {
                int r = JOptionPane.showConfirmDialog(this, Messages.getString("file_exists_save_anyway"), Messages.getString("warning.file_already_exists"),
                        JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            CommandSaveFile cmd = new CommandSaveFile(this, f);
            CommandManager.getInstance().execute(cmd);
        }
    }

    private void print() {
        PrintWindow pw = new PrintWindow(this, getStatusBar(), logBook);
        pw.setVisible(true);
    }

    private boolean isPreviewEnabled() {
        String preview = System.getProperty(net.sf.jdivelog.JDiveLog.PROPERTY_PREVIEW);
        if (preview != null && !"".equals(preview)) {
            return Boolean.valueOf(preview);
        }
        return false;
    }

    private boolean isStartSortEnabled() {
        String startsort = System.getProperty(net.sf.jdivelog.JDiveLog.PROPERTY_STARTSORT);
        if (startsort != null && !"".equals(startsort)) {
            return Boolean.valueOf(startsort);
        }
        return false;
    }

    public JDiveLog getLogBook() {
        return this.logBook;
    }

    public void setLogBook(JDiveLog logBook) {
        this.logBook = logBook;
        logbookChangeNotifier.notifyLogbookLoaded();
    }
    
    public void logbookChanged(LogbookChangeEvent e) {
        updateHeaderData();
        StringBuffer sb = new StringBuffer(Messages.getString("jdivelog")); //$NON-NLS-1$
        sb.append(" "); //$NON-NLS-1$
        if (this.getFile() != null) {
            sb.append(this.getFile().getName());
        } else {
            sb.append(Messages.getString("filename_new")); //$NON-NLS-1$
        }
        if (logbookChangeNotifier.isChanged()) {
            sb.append(" *"); //$NON-NLS-1$
        }
        if (logBook != null) {
            updateDownloadInterval(logBook.getComputerDownloadInterval());
        }
        this.setTitle(sb.toString());
        sortLogBookModel();
    }

    private void updateHeaderData() {
        DecimalFormat format = new DecimalFormat("######0.##");
        getCount_divesField().setText(new Integer(logBook.getDives().size()).toString());
        getComplete_divetimeField().setText(logBook.getComplete_Divetime());
        getAverage_depthField().setText(format.format(logBook.getAverageDepth()));
        getAverage_amvField().setText(format.format(logBook.getAverageAmv()));
        getAverage_temperaturField().setText(format.format(logBook.getAverageTemperature()));
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isChanged() {
        return logbookChangeNotifier.isChanged();
    }

    public void setChanged(boolean changed) {
        logbookChangeNotifier.setChanged(changed);
    }

    public void commandManagerChanged() {
        getUndoMenuItem().setEnabled(commandManager.canUndo());
        getRedoMenuItem().setEnabled(commandManager.canRedo());
    }

    public void windowOpened(WindowEvent e) {
        // do nothing
    }

    public void windowClosing(WindowEvent e) {
        exit();
    }

    public void windowClosed(WindowEvent e) {
        // do nothing
    }

    public void windowIconified(WindowEvent e) {
        // do nothing
    }

    public void windowDeiconified(WindowEvent e) {
        // do nothing
    }

    public void windowActivated(WindowEvent e) {
        // do nothing
    }

    public void windowDeactivated(WindowEvent e) {
        // do nothing
    }
    
    private class DelegatingMixDatabase implements MixDatabase {

        public void addFavorite(Mix m) {
            getLogBook().getMasterdata().getFavoriteMixes().addFavorite(m);
        }

        public List<Mix> getFavorites() {
            return getLogBook().getMasterdata().getFavoriteMixes().getFavorites();
        }

        public void removeFavorite(Mix m) {
            getLogBook().getMasterdata().getFavoriteMixes().removeFavorite(m);
        }
        
        
        
    }
    
    private class DiveComputerDownloadThread extends Thread {
        private boolean aborted = false;
        private long interval;
        
        public DiveComputerDownloadThread() {
            super("DiveComputerDownloadThread");
        }
        
        @Override
        public void run() {
            while(!aborted) {
                if (interval != 0) {
                    backgroundDownloadFromComputer();
                }
                synchronized(this) {
                    try {
                        if (interval == 0) {
                            wait();
                        } else {
                            wait(interval);
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
        
        public void setInterval(int i) {
            this.interval = i * 1000;
            synchronized(this) {
                notifyAll();
            }
        }
        
        public void abort() {
            aborted = true;
        }
    }

} // @jve:decl-index=0:visual-constraint="22,5"
