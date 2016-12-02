/**
 *   Stigma - Multiplayer online RPG - http://stigma.sourceforge.net
 *   Copyright (C) 2005-2009 Minions Studio
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */
package pl.org.minions.stigma.license;

import java.awt.ComponentOrientation;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.HyperlinkEvent;

import pl.org.minions.stigma.databases.Resourcer;
import pl.org.minions.utils.Version;
import pl.org.minions.utils.i18n.StandardText;
import pl.org.minions.utils.i18n.Translated;
import pl.org.minions.utils.logger.Log;

/**
 * 'About' panel with license info.
 */
public class LicenseInfoPanel extends JPanel
{
    private static final class UrlOpener implements
                                        javax.swing.event.HyperlinkListener
    {
        public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent e)
        {
            if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED)
                return;

            if (!Desktop.isDesktopSupported())
                return;

            URI uri = null;

            if (e.getURL() == null)
            {
                File f = new File("gpl-3.0.html");
                if (!f.exists())
                    try
                    {
                        uri = new URI("http://www.gnu.org/licenses/");
                    }
                    catch (URISyntaxException e1)
                    {
                        Log.logger.error(e1);
                        return;
                    }
                uri = f.toURI();
            }
            else
                try
                {
                    uri = e.getURL().toURI();
                }
                catch (URISyntaxException e2)
                {
                    Log.logger.error(e2);
                    return;
                }

            try
            {
                Desktop.getDesktop().browse(uri);
            }
            catch (IOException e1)
            {
                Log.logger.error(e1);
            }
        }
    }

    private static final long serialVersionUID = 1L;
    private static final Dimension DEFAULT_SIZE = new Dimension(660, 530);
    private static final String DEFAULT_FONT = "Dialog";
    private static final String ICON_PATH = "img/default/stigma_icon_64x64.png";

    private static final BufferedImage ICON = Resourcer.loadImage(ICON_PATH);

    @Translated
    private static String IS_PART_OF_TEXT = "is a part of";
    @Translated
    private static String VERSION_TEXT = "Version:";
    @Translated
    private static String COMPATIBLE_WITH_TEXT = "compatible with:";
    @Translated
    private static String SUPPORT_TEXT = "Support:";

    private JButton okButton;
    private JLabel appNameLabel;
    private JLabel isPartOfLabel;
    private JLabel stigmaLabel;
    private JEditorPane licensePane;
    private JPanel headerPanel;
    private JScrollPane licenseScroll;
    private JLabel versionLabel;
    private JPanel versionPanel;
    private JLabel fullVersionLabel;
    private JLabel compatibleLabel;
    private JLabel compatibleVersionLabel;
    private JLabel supportLabel;
    private JEditorPane supportLink;
    private JLabel iconLabel;

    /**
     * Constructor.
     */
    public LicenseInfoPanel()
    {
        initialize();
    }

    private JPanel getHeaderPanel()
    {
        if (headerPanel == null)
        {
            // CHECKSTYLE:OFF
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.gridx = 0;
            gridBagConstraints21.insets = new Insets(0, 0, 0, 10);
            gridBagConstraints21.gridy = 0;
            iconLabel = new JLabel();
            iconLabel.setIcon(new ImageIcon(ICON));
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 6;
            gridBagConstraints2.anchor = GridBagConstraints.SOUTH;
            gridBagConstraints2.ipady = 14;
            gridBagConstraints2.gridy = 0;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 5;
            gridBagConstraints1.anchor = GridBagConstraints.SOUTH;
            gridBagConstraints1.ipadx = 0;
            gridBagConstraints1.ipady = 16;
            gridBagConstraints1.insets = new Insets(0, 8, 0, 8);
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridy = 0;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.anchor = GridBagConstraints.SOUTHWEST;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.ipady = 14;
            gridBagConstraints.gridy = 0;
            headerPanel = new JPanel();
            headerPanel.setLayout(new GridBagLayout());
            headerPanel.add(appNameLabel, gridBagConstraints);
            headerPanel.add(isPartOfLabel, gridBagConstraints1);
            headerPanel.add(stigmaLabel, gridBagConstraints2);
            headerPanel.add(iconLabel, gridBagConstraints21);
            // CHECKSTYLE:ON
        }
        return headerPanel;
    }

    private JEditorPane getLicensePane()
    {
        if (licensePane == null)
        {
            licensePane = new JEditorPane();
            licensePane.setEditable(false);
            licensePane.setContentType(LicenseInfo.Format.HTML.getContentType());
            licensePane.setText(LicenseInfo.getLicenseText(LicenseInfo.Format.HTML));
            licensePane.addHyperlinkListener(new UrlOpener());
        }
        return licensePane;
    }

    private JScrollPane getLicenseScroll()
    {
        if (licenseScroll == null)
        {
            licenseScroll = new JScrollPane();
            licenseScroll.setViewportView(getLicensePane());
            licenseScroll.getViewport().setViewPosition(new Point(0, 0));
        }
        return licenseScroll;
    }

    private JButton getOkButton()
    {
        if (okButton == null)
        {
            okButton = new JButton();
            okButton.setText(StandardText.OK.get());
        }
        return okButton;
    }

    private JEditorPane getSupportLink()
    {
        if (supportLink == null)
        {
            supportLink = new JEditorPane();
            supportLink.setOpaque(false);
            supportLink.setEditable(false);
            supportLink.setContentType("text/html");
            String url = LicenseInfo.getSupportWeb();
            supportLink.setText("<html><a href=\"" + url + "\">" + url
                + "</a></html>");
            supportLink.addHyperlinkListener(new UrlOpener());
        }
        return supportLink;
    }

    private JPanel getVersionPanel()
    {
        if (versionPanel == null)
        {
            // CHECKSTYLE:OFF
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.fill = GridBagConstraints.BOTH;
            gridBagConstraints13.gridy = 2;
            gridBagConstraints13.weightx = 1.0;
            gridBagConstraints13.weighty = 1.0;
            gridBagConstraints13.gridwidth = 3;
            gridBagConstraints13.insets = new Insets(0, 3, 0, 0);
            gridBagConstraints13.gridx = 1;
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.gridx = 0;
            gridBagConstraints12.gridwidth = 1;
            gridBagConstraints12.gridy = 2;
            supportLabel = new JLabel();
            supportLabel.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 12));
            supportLabel.setText(SUPPORT_TEXT);
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.gridx = 3;
            gridBagConstraints10.insets = new Insets(0, 5, 0, 0);
            gridBagConstraints10.gridy = 1;
            compatibleVersionLabel = new JLabel();
            compatibleVersionLabel.setText(Version.COMPATIBILITY_RANGE);
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.gridx = 2;
            gridBagConstraints9.insets = new Insets(0, 20, 0, 0);
            gridBagConstraints9.gridy = 1;
            compatibleLabel = new JLabel();
            compatibleLabel.setText(COMPATIBLE_WITH_TEXT);
            compatibleLabel.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 12));
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 1;
            gridBagConstraints8.insets = new Insets(0, 5, 0, 0);
            gridBagConstraints8.gridy = 1;
            fullVersionLabel = new JLabel();
            fullVersionLabel.setText(Version.FULL_VERSION);
            versionPanel = new JPanel();
            versionLabel = new JLabel();
            versionLabel.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 12));
            versionLabel.setText(VERSION_TEXT);

            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.anchor = GridBagConstraints.WEST;
            gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
            gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints3.gridy = 1;

            versionPanel.setLayout(new GridBagLayout());
            versionPanel.add(versionLabel, gridBagConstraints3);
            versionPanel.add(fullVersionLabel, gridBagConstraints8);
            versionPanel.add(compatibleLabel, gridBagConstraints9);
            versionPanel.add(compatibleVersionLabel, gridBagConstraints10);
            versionPanel.add(supportLabel, gridBagConstraints12);
            versionPanel.add(getSupportLink(), gridBagConstraints13);
            // CHECKSTYLE:ON
        }
        return versionPanel;
    }

    private void initialize()
    {
        // CHECKSTYLE:OFF
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.fill = GridBagConstraints.BOTH;
        gridBagConstraints6.weighty = 1.0;
        gridBagConstraints6.gridx = 0;
        gridBagConstraints6.gridy = 3;
        gridBagConstraints6.insets = new Insets(10, 30, 10, 30);
        gridBagConstraints6.weightx = 1.0;
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.gridx = 0;
        gridBagConstraints7.anchor = GridBagConstraints.WEST;
        gridBagConstraints7.insets = new Insets(0, 15, 0, 0);
        gridBagConstraints7.gridy = 2;
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints5.gridy = 0;
        gridBagConstraints5.ipadx = 0;
        gridBagConstraints5.ipady = 0;
        gridBagConstraints5.anchor = GridBagConstraints.WEST;
        gridBagConstraints5.gridwidth = 1;
        gridBagConstraints5.fill = GridBagConstraints.NONE;
        gridBagConstraints5.gridx = 0;
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.insets = new Insets(10, 10, 10, 40);
        gridBagConstraints4.gridy = 6;
        gridBagConstraints4.ipadx = 30;
        gridBagConstraints4.ipady = 1;
        gridBagConstraints4.anchor = GridBagConstraints.EAST;
        gridBagConstraints4.gridx = 0;
        stigmaLabel = new JLabel();
        stigmaLabel.setText(LicenseInfo.getProductName());
        stigmaLabel.setVerticalAlignment(SwingConstants.TOP);
        stigmaLabel.setFont(new Font(DEFAULT_FONT, Font.BOLD, 24));
        isPartOfLabel = new JLabel();
        isPartOfLabel.setText(' ' + IS_PART_OF_TEXT + ' ');
        isPartOfLabel.setHorizontalAlignment(SwingConstants.LEADING);
        isPartOfLabel.setVerticalAlignment(SwingConstants.TOP);
        isPartOfLabel.setFont(new Font(DEFAULT_FONT, Font.ITALIC, 18));
        appNameLabel = new JLabel();
        appNameLabel.setText(Version.isInitialized() ? Version.getAppName()
                                                    : "APP_NAME");
        appNameLabel.setFont(new Font(DEFAULT_FONT, Font.BOLD, 24));
        appNameLabel.setVerticalAlignment(SwingConstants.TOP);
        appNameLabel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        this.setSize(DEFAULT_SIZE);
        this.setLayout(new GridBagLayout());
        this.setName("LicenseInfoPanel");
        this.add(getOkButton(), gridBagConstraints4);
        this.add(getHeaderPanel(), gridBagConstraints5);
        this.add(getVersionPanel(), gridBagConstraints7);
        this.add(getLicenseScroll(), gridBagConstraints6);
        // CHECKSTYLE:ON
    }

    /**
     * Enforces visibility of beginning of license text.
     */
    public void rewind()
    {
        licensePane.select(0, 0);
    }

    /**
     * Creates {@link JFrame} containing license panel.
     * @return new stand alone frame
     */
    public JFrame createFrame()
    {
        final JFrame frame = new JFrame();
        frame.setContentPane(this);
        frame.setTitle(StandardText.ABOUT.get() + ' '
            + (Version.isInitialized() ? Version.getAppName() : "APP_NAME"));
        frame.setIconImage(ICON);
        frame.setSize(DEFAULT_SIZE);

        getOkButton().addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                frame.setVisible(false);
            }
        });

        return frame;
    }

    /**
     * Creates {@link JInternalFrame} containing license
     * panel.
     * @return new internal frame
     */
    public JInternalFrame createInternalFrame()
    {
        final JInternalFrame frame = new JInternalFrame();
        frame.setContentPane(this);
        frame.setTitle(StandardText.ABOUT.get() + ' '
            + (Version.isInitialized() ? Version.getAppName() : "APP_NAME"));
        frame.setSize(DEFAULT_SIZE);

        getOkButton().addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                frame.setVisible(false);
            }
        });

        return frame;
    }

    /**
     * Shows floating frame containing license panel.
     * Equivalent to {@code new
     * LicenseInfoPanel().createFrame().setVisible(true)}.
     * @see #createFrame()
     */
    public static void showFrame()
    {
        new LicenseInfoPanel().createFrame().setVisible(true);
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
