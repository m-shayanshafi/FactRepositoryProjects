/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: HtmlExportUtil.java
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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.statusbar.StatusInterface;
import net.sf.jdivelog.gui.util.ImageUtil;
import net.sf.jdivelog.model.DiveSite;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.JDiveLog;
import net.sf.jdivelog.model.Picture;
import net.sf.jdivelog.model.Tank;
import net.sf.jdivelog.util.UnitConverter;

import org.apache.batik.ext.awt.image.codec.jpeg.JPEGImageWriter;
import org.apache.batik.ext.awt.image.spi.ImageWriterParams;

/**
 * Tool to export jdivelog logbooks to a web page
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class HtmlExportUtil {

    private static final Logger LOGGER = Logger.getLogger(HtmlExportUtil.class.getName());

    private static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat(Messages.getString("export.date_format")); //$NON-NLS-1$

    private static final SimpleDateFormat FORMAT_NICEDATE = new SimpleDateFormat(Messages.getString("export.nice_date_format")); //$NON-NLS-1$

    private static final SimpleDateFormat FORMAT_TIME = new SimpleDateFormat(Messages.getString("export.time_format")); //$NON-NLS-1$

    private static final NumberFormat DECIMALFORMAT = new DecimalFormat(Messages.getString("export.number_format")); //$NON-NLS-1$

    private static final SimpleDateFormat FORMAT_YEAR = new SimpleDateFormat("yyyy"); //$NON-NLS-1$

    private JDiveLog logBook;

    private File outputDir;

    private StatusInterface status;

    private StringBuffer indexPage;

    private HashMap<String, StringBuffer> indexPages;

    private String date;

    public HtmlExportUtil(StatusInterface status, JDiveLog logBook) {
        this.status = status;
        this.logBook = logBook;
        this.date = FORMAT_NICEDATE.format(new Date());
    }

    public int execute() throws ExportException {
        getSkinFile(); // to check for valid skin before doing anything else...
        int exportCount = 0;
        outputDir = new File(logBook.getExportSettings().getExportDirectory());
        if (!outputDir.isDirectory() || !outputDir.canWrite()) {
            throw new ExportException(Messages.getString("export.error.could_not_write_to_dir") + " '" + outputDir.getPath() + "'"); //$NON-NLS-1$
        }
        if (logBook.getDives() == null || logBook.getDives().size() == 0) {
            throw new ExportException(Messages.getString("export.error.no_dives_in_logbook")); //$NON-NLS-1$
        }

        status.messageInfo(Messages.getString("export.creating_html_page"));
        status.countingProgressbarStart(logBook.getDives().size(), true);
        indexPages = new HashMap<String, StringBuffer>();
        createDirectories();
        createStylesheet();
        startIndexPage();
        int count = 0;
        Iterator<JDive> it = logBook.getDives().iterator();
        while (it.hasNext()) {
            try {
                JDive dive = it.next();
                if (logBook.getExportSettings().isFullExport() || !dive.isHtmlExported()) {
                    if (logBook.getExportSettings().showDetailProfile() || logBook.getExportSettings().getIndexImages() == 1) {
                        status.messageInfo(Messages.getString("export.writing_profile")); //$NON-NLS-1$
                        createProfile(count, dive);
                    }
                    if (logBook.getExportSettings().isPictureVisible()) {
                        status.messageInfo(Messages.getString("export.creating_pictures_page")); //$NON-NLS-1$
                        createPicturesPage(count, dive);
                    } else if (logBook.getExportSettings().getIndexImages() == 2) {
                        status.messageInfo(Messages.getString("export.creating_picture_thumbnail")); //$NON-NLS-1$
                        if (dive.getPictures() != null && dive.getPictures().size() > 0)
                            createPicture(count, 0, dive.getPictures().get(0));
                    }
                    if (logBook.getExportSettings().isDetailVisible()) {
                        status.messageInfo(Messages.getString("export.creating_details_page")); //$NON-NLS-1$
                        createDetailsPage(count, dive);
                    }
                }
                addIndexEntry(count, dive);
                count++;
                if (!dive.isHtmlExported()) {
                    dive.setHtmlExported(true);
                    exportCount++;
                }
            } catch (ConcurrentModificationException e) {
                throw new ExportException(Messages.getString("export.error.concurrent_modification")); //$NON-NLS-1$
            }
            status.countingProgressbarIncrement();
        }
        endIndexPage();
        if (logBook.getExportSettings().isScpEnabled() && logBook.getExportSettings().isFullExport()) {
            scpFull();
        }
        status.countingProgressbarEnd();
        status.messageClear();
        return exportCount;
    }

    private void addIndexEntry(int count, JDive dive) {
        StringBuffer sb = new StringBuffer();
        UnitConverter c = new UnitConverter(UnitConverter.getSystem(dive.getUnits()), UnitConverter.getDisplaySystem());
        sb.append("<a class='diveindexlink' href='" + count + "/details.html'>\n");
        sb.append("  <div class='diveindexrow-" + getOddEvenString(count) + "' id='d" + count + "'>\n");
        sb.append("    <div class='diveindexrow-content'>\n");

        if (logBook.getExportSettings().getIndexImages() == 1) {
            sb.append("      <div class='diveindexcell-" + getOddEvenString(count) + "' id='thumb'><img class='thumb' src='" + count
                    + "/profile_thumb.jpg'></div>\n");
        } else if (logBook.getExportSettings().getIndexImages() == 2) {
            if (dive.getPictures() != null && dive.getPictures().size() > 0) {
                sb.append("      <div class='diveindexcell-" + getOddEvenString(count) + "' id='thumb'><img class='thumb' src='" + count
                        + "/image0_thumb.jpg'></div>\n");
            } else {
                sb.append("      <div class='diveindexcell-" + getOddEvenString(count) + "' id='thumb'>&nbsp;</div>\n");
            }
        }
        if (logBook.getExportSettings().showIndexDate()) {
            if (dive.getDate() != null) {
                sb.append("      <div class='diveindexcell-" + getOddEvenString(count) + "' id='date'>" + FORMAT_DATE.format(dive.getDate()) + "</div>\n");
            } else {
                sb.append("      <div class='diveindexcell-" + getOddEvenString(count) + "' id='date'>&nbsp;</div>\n");
            }
        }
        if (logBook.getExportSettings().showIndexTime()) {
            if (dive.getDate() != null) {
                sb.append("      <div class='diveindexcell-" + getOddEvenString(count) + "' id='time'>" + FORMAT_TIME.format(dive.getDate()) + "</div>\n");
            } else {
                sb.append("      <div class='diveindexcell-" + getOddEvenString(count) + "' id='time'>&nbsp;</div>\n");
            }
        }
        DiveSite site = logBook.getMasterdata().getDiveSiteByPrivateId(dive.getDiveSiteId());
        if (logBook.getExportSettings().showIndexLocation()) {
            if (site != null && site.getSpot() != null) {
                sb.append("      <div class='diveindexcell-" + getOddEvenString(count) + "' id='spot'>" + site.getSpot() + "</div>\n");
            } else {
                sb.append("      <div class='diveindexcell-" + getOddEvenString(count) + "' id='spot'>&nbsp;</div>\n");
            }
        }
        if (logBook.getExportSettings().showIndexCity()) {
            if (site != null && site.getCity() != null) {
                sb.append("      <div class='diveindexcell-" + getOddEvenString(count) + "' id='city'>" + site.getCity() + "</div>\n");
            } else {
                sb.append("      <div class='diveindexcell-" + getOddEvenString(count) + "' id='city'>&nbsp;</div>\n");
            }
        }
        if (logBook.getExportSettings().showIndexCountry()) {
            if (site != null && site.getCountry() != null) {
                sb.append("      <div class='diveindexcell-" + getOddEvenString(count) + "' id='country'>" + site.getCountry() + "</div>\n");
            } else {
                sb.append("      <div class='diveindexcell-" + getOddEvenString(count) + "' id='country'>&nbsp;</div>\n");
            }
        }
        if (logBook.getExportSettings().showIndexDepth()) {
            if (dive.getDepth() != null) {
                sb.append("      <div class='diveindexcell-" + getOddEvenString(count) + "' id='depth'>"
                        + DECIMALFORMAT.format(c.convertAltitude(dive.getDepth())) + " " + UnitConverter.getDisplayAltitudeUnit() + "</div>\n");
            } else {
                sb.append("      <div class='diveindexcell-" + getOddEvenString(count) + "' id='depth'>&nbsp;</div>\n");
            }
        }
        if (logBook.getExportSettings().showIndexDuration()) {
            if (dive.getDuration() != null) {
                sb.append("      <div class='diveindexcell-" + getOddEvenString(count) + "' id='duration'>"
                        + DECIMALFORMAT.format(c.convertTime(dive.getDuration())) + " " + UnitConverter.getDisplayTimeUnit() + "</div>\n");
            } else {
                sb.append("      <div class='diveindexcell-" + getOddEvenString(count) + "' id='duration'>&nbsp;</div>\n");
            }
        }
        if (logBook.getExportSettings().showIndexPictureCount()) {
            if (dive.getPictures() != null && dive.getPictures().size() > 0) {
                int picCount = dive.getPictures() == null ? 0 : dive.getPictures().size();
                sb.append("      <div class='diveindexcell-" + getOddEvenString(count) + "' id='pics'>");
                // Looks like some browsers do not support link in link (guess
                // it's not allowed in HTML)
                // sb.append("<a class='pictureslink' href='" + count +
                // "/pictures.html'>");
                sb.append(String.valueOf(picCount) + " " + Messages.getString("export.pictures"));
                // sb.append("</a>");
                sb.append("</div>\n");
            } else {
                sb.append("      <div class='diveindexcell-" + getOddEvenString(count) + "' id='pics'>&nbsp;</div>\n");
            }
        }
        sb.append("    </div>\n");
        sb.append("  </div>\n");
        sb.append("</a>\n\n");
        getIndexPage(dive).append(sb.toString());
    }

    private StringBuffer getIndexPage(JDive dive) {
        if (logBook.getExportSettings().groupByYear()) {
            String year = FORMAT_YEAR.format(dive.getDate());
            StringBuffer indexPage = indexPages.get(year);
            if (indexPage == null) {
                indexPage = new StringBuffer();

                indexPage.append(getPageHeader(logBook.getExportSettings().getIndexTitle() + " " + year, null));

                indexPage.append("<!-- begin navigation -->\n");
                indexPage.append("<div class='navigation'>\n");
                indexPage.append("<div id='br'><div id='tr'><div id='tl'><div id='bl'>\n");
                indexPage
                        .append("<a class='menulink' id='go_to_index' href='index.html'><div class='menuitem'><div id='tl'><div id='tr'><div id='br'><div id='bl'>zur&uuml;ck zur &Uuml;bersicht</div></div></div></div></div></a>\n");
                indexPage.append("</div></div></div></div>\n");
                indexPage.append("</div>\n");
                indexPage.append("<!-- end navigation -->\n\n");

                indexPage.append("<!-- begin content -->\n");
                indexPage.append("<div class='diveindexdata'>\n");
                indexPage.append("<div id='br'><div id='tr'><div id='tl'><div id='bl'>\n\n");

                indexPage.append("<div class='diveindextitle'>" + Messages.getString("dives") + " " + year + "</div>\n\n");

                indexPage.append("<div class='diveindexcontainer'>\n");
                indexPage.append("<div id='br'><div id='tr'><div id='tl'><div id='bl'>\n");
                indexPage.append("<div class='diveindextable' cellspacing='0' cellpadding='0'>\n\n");

                indexPage.append("<div class='diveindexheading-row'>\n");
                if (logBook.getExportSettings().getIndexImages() != 0) {
                    indexPage.append("<div class='diveindexheading-cell' id='thumb'>" + Messages.getString("image") + "</div>\n");
                }
                if (logBook.getExportSettings().showDetailDate()) {
                    indexPage.append("<div class='diveindexheading-cell' id='date'>" + Messages.getString("date") + "</div>\n");
                }
                if (logBook.getExportSettings().showIndexTime()) {
                    indexPage.append("<div class='diveindexheading-cell' id='time'>" + Messages.getString("time") + "</div>\n");
                }
                if (logBook.getExportSettings().showIndexLocation()) {
                    indexPage.append("<div class='diveindexheading-cell' id='spot'>" + Messages.getString("spot") + "</div>\n");
                }
                if (logBook.getExportSettings().showIndexCity()) {
                    indexPage.append("<div class='diveindexheading-cell' id='city'>" + Messages.getString("city") + "</div>\n");
                }
                if (logBook.getExportSettings().showIndexCountry()) {
                    indexPage.append("<div class='diveindexheading-cell' id='country'>" + Messages.getString("country") + "</div>\n");
                }
                if (logBook.getExportSettings().showIndexDuration()) {
                    indexPage.append("<div class='diveindexheading-cell' id='duration'>" + Messages.getString("duration") + "</div>\n");
                }
                if (logBook.getExportSettings().showIndexDepth()) {
                    indexPage.append("<div class='diveindexheading-cell' id='depth'>" + Messages.getString("depth") + "</div>\n");
                }
                if (logBook.getExportSettings().showIndexPictureCount()) {
                    indexPage.append("<div class='diveindexheading-cell' id='pics'>" + Messages.getString("images") + "</div>\n");
                }
                indexPage.append("</div>\n");
                indexPages.put(year, indexPage);
            }
            return indexPage;
        }
        return indexPage;
    }

    private void endIndexPage() throws ExportException {
        if (logBook.getExportSettings().groupByYear() && indexPages.size() > 0) {
            SortedSet<String> years = new TreeSet<String>();
            Iterator<String> it = indexPages.keySet().iterator();
            while (it.hasNext()) {
                String year = it.next();
                years.add(year);
                StringBuffer ip = indexPages.get(year);
                ip.append(getIndexEnd());
                ip.append(getPageFooter());
                try {
                    writeHTML(ip.toString(), new File(outputDir, "index" + year + ".html")); //$NON-NLS-1$//$NON-NLS-2$
                } catch (IOException ioe) {
                    LOGGER.log(Level.SEVERE, "error writing index", ioe);
                    throw new ExportException(Messages.getString("export.error.could_not_write_index_html")); //$NON-NLS-1$
                }
            }

            it = years.iterator();
            int count = 0;
            while (it.hasNext()) {
                String year = it.next();
                indexPage.append("<a class='indexlink' href='index");
                indexPage.append(year);
                indexPage.append(".html'>\n");
                indexPage.append("<div class='indexrow-" + getOddEvenString(count) + "'>\n");
                indexPage.append("<div class='indexcell-" + getOddEvenString(count) + "' id='year'>");
                indexPage.append(Messages.getString("dives") + " " + year);
                indexPage.append("</div>\n");
                indexPage.append("</div>\n");
                indexPage.append("</a>\n");
                count++;
            }
        }
        indexPage.append(getIndexEnd());
        indexPage.append(getPageFooter());
        try {
            writeHTML(indexPage.toString(), new File(outputDir, "index.html")); //$NON-NLS-1$
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "error writing index", ioe);
            throw new ExportException(Messages.getString("export.error.could_not_write_index_html")); //$NON-NLS-1$
        }
    }

    private String getIndexEnd() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n</div>\n");
        sb.append("</div></div></div></div>\n");
        sb.append("</div>\n\n");

        sb.append("</div></div></div></div>\n");
        sb.append("</div>\n");
        sb.append("<!-- end content -->\n");
        return sb.toString();
    }

    private void startIndexPage() {
        indexPage = new StringBuffer();

        indexPage.append(getPageHeader(logBook.getExportSettings().getIndexTitle(), null));

        indexPage.append("<!-- begin content -->\n");
        if (logBook.getExportSettings().groupByYear()) {
            indexPage.append("<div class='indexdata'>\n");
            indexPage.append("<div id='br'><div id='tr'><div id='tl'><div id='bl'>\n\n");

            indexPage.append("<div class='indextitle'>" + Messages.getString("dives") + "</div>\n\n");

            indexPage.append("<div class='indexcontainer'>\n");
            indexPage.append("<div id='br'><div id='tr'><div id='tl'><div id='bl'>\n");
            indexPage.append("<div class='indextable' cellspacing='0' cellpadding='0'>\n\n");
        } else {
            indexPage.append("<div class='diveindexdata'>\n");
            indexPage.append("<div id='br'><div id='tr'><div id='tl'><div id='bl'>\n\n");

            indexPage.append("<div class='diveindextitle'>" + Messages.getString("dives") + "</div>\n\n");

            indexPage.append("<div class='diveindexcontainer'>\n");
            indexPage.append("<div id='br'><div id='tr'><div id='tl'><div id='bl'>\n");
            indexPage.append("<div class='diveindextable' cellspacing='0' cellpadding='0'>\n\n");

            indexPage.append("<div class='diveindexheading-row'>\n");
            if (logBook.getExportSettings().getIndexImages() != 0) {
                indexPage.append("<div class='diveindexheading-cell' id='thumb'>" + Messages.getString("image") + "</div>\n");
            }
            if (logBook.getExportSettings().showDetailDate()) {
                indexPage.append("<div class='diveindexheading-cell' id='date'>" + Messages.getString("date") + "</div>\n");
            }
            if (logBook.getExportSettings().showIndexTime()) {
                indexPage.append("<div class='diveindexheading-cell' id='time'>" + Messages.getString("time") + "</div>\n");
            }
            if (logBook.getExportSettings().showIndexLocation()) {
                indexPage.append("<div class='diveindexheading-cell' id='spot'>" + Messages.getString("spot") + "</div>\n");
            }
            if (logBook.getExportSettings().showIndexCity()) {
                indexPage.append("<div class='diveindexheading-cell' id='city'>" + Messages.getString("city") + "</div>\n");
            }
            if (logBook.getExportSettings().showIndexCountry()) {
                indexPage.append("<div class='diveindexheading-cell' id='country'>" + Messages.getString("country") + "</div>\n");
            }
            if (logBook.getExportSettings().showIndexDuration()) {
                indexPage.append("<div class='diveindexheading-cell' id='duration'>" + Messages.getString("duration") + "</div>\n");
            }
            if (logBook.getExportSettings().showIndexDepth()) {
                indexPage.append("<div class='diveindexheading-cell' id='depth'>" + Messages.getString("depth") + "</div>\n");
            }
            if (logBook.getExportSettings().showIndexPictureCount()) {
                indexPage.append("<div class='diveindexheading-cell' id='pics'>" + Messages.getString("images") + "</div>\n");
            }
            indexPage.append("</div>\n");
        }
    }

    private String getPageHeader(String title, JDive dive) {
        boolean index = dive == null;
        StringBuffer sb = new StringBuffer();
        sb.append(Messages.getString("export.doctype")); //$NON-NLS-1$
        sb.append("\n<html>\n"); //$NON-NLS-1$
        sb.append("<head>\n"); //$NON-NLS-1$
        sb.append("<title>"); //$NON-NLS-1$
        sb.append(title);
        sb.append("</title>\n"); //$NON-NLS-1$
        sb.append("<meta http-equiv='content-type' content='text/html; charset="); //$NON-NLS-1$
        sb.append(Charset.defaultCharset().name());
        sb.append("'>\n"); //$NON-NLS-1$
        sb.append("<meta name='Generator' content='Page generated by JDivelog [http://www.jdivelog.org]'>\n"); //$NON-NLS-1$
        sb.append("<meta name='date' content='"); //$NON-NLS-1$
        sb.append(date);
        sb.append("'>\n"); //$NON-NLS-1$
        sb.append("<link rel='stylesheet' type='text/css' href='"); //$NON-NLS-1$
        if (!index) {
            sb.append("../"); //$NON-NLS-1$
        }
        sb.append("jdivelog.css'>\n"); //$NON-NLS-1$
        sb.append("</head>\n"); //$NON-NLS-1$
        sb.append("<body>\n\n"); //$NON-NLS-1$
        sb.append("<!-- begin container -->\n");
        sb.append("<div class='container'>\n");
        sb.append("<div id='br'><div id='tr'><div id='tl'><div id='bl'>\n\n");

        sb.append("<!-- begin header -->\n");
        sb.append("<div id='header'>\n");
        sb.append("<div id='br'><div id='tr'><div id='tl'><div id='bl'>\n");
        sb.append("<div id='pagetitle'>" + logBook.getExportSettings().getIndexTitle() + "</div>\n");
        sb.append("<span id='spacer'></span>\n");
        if (dive != null && dive.getDate() != null) {
            sb.append("<div id='date'>" + FORMAT_DATE.format(dive.getDate()) + "</div>\n");
        }
        if (dive != null && dive.getDiveSiteId() != null && !"".equals(dive.getDiveSiteId())) {
            DiveSite site = logBook.getMasterdata().getDiveSiteByPrivateId(dive.getDiveSiteId());
            StringBuffer place = new StringBuffer();
            if (site.getSpot() != null) {
                place.append(site.getSpot());
            }
            if (site.getCity() != null) {
                if (site.getSpot() != null) {
                    place.append(", ");
                }
                place.append(site.getCity());
            }
            if (site.getCountry() != null) {
                if (site.getSpot() != null || site.getCity() != null) {
                    place.append(" (");
                }
                place.append(site.getCountry());
                if (site.getSpot() != null || site.getCity() != null) {
                    place.append(")");
                }
            }
            sb.append("<div id='place'>" + place.toString() + "</div>\n");
        }
        sb.append("</div></div></div></div>\n");
        sb.append("</div>\n");
        sb.append("<!-- end header -->\n\n");

        return sb.toString();
    }

    private String getPageFooter() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n<!-- begin footer -->\n");
        sb.append("<div class='footer'>\n");
        sb.append("<div id='br'><div id='tr'><div id='tl'><div id='bl'>\n");
        sb.append("<div class='jdivelog'>");
        sb.append(Messages.getString("export.page_footer"));
        sb.append(" ");
        sb.append(date);
        sb.append("</div>\n");
        sb.append("<div class='customfooter'>");
        boolean link = logBook.getExportSettings().getFooterLink() != null && !"".equals(logBook.getExportSettings().getFooterLink());
        boolean linkName = logBook.getExportSettings().getFooterLinkName() != null && !"".equals(logBook.getExportSettings().getFooterLinkName());
        if (link && linkName) {
            sb.append("<a href='");
            sb.append(logBook.getExportSettings().getFooterLink());
            sb.append("'>");
        }
        if (linkName) {
            sb.append(toHtml(logBook.getExportSettings().getFooterLinkName(), false));
        }
        if (link && linkName) {
            sb.append("</a>");
        }
        sb.append("</div>\n");

        sb.append("</div></div></div></div>\n");
        sb.append("</div>\n");
        sb.append("<!-- end footer -->\n\n");
        sb.append("</div></div></div></div>\n");
        sb.append("</div>\n");
        sb.append("<!-- end container -->\n\n");

        sb.append("</body>\n");
        sb.append("</html>\n");
        return sb.toString();
    }

    private void createDirectories() {
        for (int i = 0; i < logBook.getDives().size(); i++) {
            File f = new File(outputDir, String.valueOf(i));
            f.mkdir();
        }
    }

    private File getSkinFile() throws ExportException {
        String skinname = logBook.getExportSettings().getSkinFile();
        File skinfile = null;
        if (skinname.toLowerCase().endsWith(".zip")) {
            // skinname is a filename
            skinfile = new File(skinname);
        } else {
            // skinname should exist in skins directory
            String skinpath = System.getProperty("skindir");
            if (skinpath == null) {
                throw new ExportException(Messages.getString("export.error.skindir_property_not_set"));
            }
            File skindir = new File(skinpath);
            if (!skindir.exists()) {
                LOGGER.severe("skindir does not exist: " + skindir.getAbsolutePath());
                throw new ExportException(Messages.getString("export.error.skindir_does_not_exist"));
            }
            if (!skindir.isDirectory()) {
                LOGGER.severe("skindir is not a directory: " + skindir.getAbsolutePath());
                throw new ExportException(Messages.getString("export.error.skindir_is_not_a_directory"));
            }
            if (!skindir.canRead()) {
                LOGGER.severe("skindir is not readable: " + skindir.getAbsolutePath());
                throw new ExportException(Messages.getString("export.error.skindir_is_not_readable"));
            }
            skinfile = new File(skindir, skinname + ".zip");
        }
        if (!skinfile.exists()) {
            LOGGER.severe("skinfile does not exist: " + skinfile.getAbsolutePath());
            throw new ExportException(Messages.getString("export.error.skinfile_does_not_exist"));
        }
        if (!skinfile.isFile()) {
            LOGGER.severe("skinfile is not a file: " + skinfile.getAbsolutePath());
            throw new ExportException(Messages.getString("export.error.skinfile_is_not_a_file"));
        }
        if (!skinfile.canRead()) {
            LOGGER.severe("skinfile is not readable: " + skinfile.getAbsolutePath());
            throw new ExportException(Messages.getString("export.error.skinfile_is_not_readable"));
        }
        return skinfile;
    }

    private void createStylesheet() throws ExportException {
        try {
            ZipFile zipfile = new ZipFile(getSkinFile());
            Enumeration<? extends ZipEntry> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String fn = entry.getName();
                if (entry.isDirectory()) {
                    new File(outputDir, entry.getName()).mkdir();
                } else {
                    File destfile = new File(outputDir, fn);
                    FileOutputStream fos = new FileOutputStream(destfile);
                    InputStream is = zipfile.getInputStream(entry);
                    copyStream(is, fos);
                    scp(destfile);
                }
            }
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "error writing stylesheet", ioe);
            throw new ExportException(Messages.getString("export.error.could_not_copy_skin")); //$NON-NLS-1$
        }
    }

    private void createProfile(int i, JDive dive) throws ExportException {
        DiveProfile dp = new DiveProfile(logBook.getProfileSettings(), dive);
        File diveDir = new File(outputDir, String.valueOf(i));
        int width = logBook.getExportSettings().getDetailProfileWidth();
        int height = logBook.getExportSettings().getDetailProfileHeight();
        int thumbMaxWidth = logBook.getExportSettings().getPictureThumbnailMaxWidth();
        int thumbMaxHeight = logBook.getExportSettings().getPictureThumbnailMaxHeight();
        dp.setSize(new Dimension(width, height));
        BufferedImage profile = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D profileG = profile.createGraphics();
        profileG.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        dp.paint(profileG);
        try {
            writeJPG(profile, new File(diveDir, "profile.jpg")); //$NON-NLS-1$
            writeScaledJPG(profile, thumbMaxWidth, thumbMaxHeight, 0, new File(diveDir, "profile_thumb.jpg"), false); //$NON-NLS-1$
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "error creating profile", e);
            throw new ExportException(Messages.getString("export.error.could_not_write_profile_images") + " " + diveDir.getPath()); //$NON-NLS-1$ //$NON-NLS-2$
        }

    }

    private void createDetailsPage(int diveNumber, JDive dive) throws ExportException {
        File diveDir = new File(outputDir, String.valueOf(diveNumber));
        UnitConverter c = new UnitConverter(UnitConverter.getSystem(dive.getUnits()), UnitConverter.getDisplaySystem());
        StringBuffer sb = new StringBuffer();
        sb.append(getPageHeader(getTitle(dive), dive));
        sb.append(getDetailsPageHeader(diveNumber, dive));
        if (logBook.getExportSettings().showDetailProfile()) {
            sb.append("<div class='profilecontainer'>\n");
            sb.append("<div id='br'><div id='tr'><div id='tl'><div id='bl'>\n");
            sb.append("<img class='profile' src='profile.jpg'>\n");
            sb.append("</div></div></div></div>\n");
            sb.append("</div>\n\n");
        }
        sb.append("<div class='datatablecontainer'>\n");
        sb.append("<div id='br'><div id='tr'><div id='tl'><div id='bl'>\n");
        sb.append("<div class='datatable' cellspacing='0' cellpadding='0'>\n");

        int count = 0;

        if (logBook.getExportSettings().showDetailDate()) {
            sb.append("<div class='datarow-" + getOddEvenString(count++) + "' id='daterow'>\n");
            sb.append("<div class='datalabel' id='datelabel'>");
            sb.append(Messages.getString("export.date"));
            sb.append("</div>\n");
            sb.append("<div class='datavalue' id='datevalue'>");
            if (dive.getDate() != null) {
                sb.append(FORMAT_DATE.format(dive.getDate()));
            } else {
                sb.append("&nbsp;");
            }
            sb.append("</div>\n");
            sb.append("</div>\n");
        }
        if (logBook.getExportSettings().showDetailTime()) {
            sb.append("<div class='datarow-" + getOddEvenString(count++) + "' id='timerow'>\n");
            sb.append("<div class='datalabel' id='timelabel'>");
            sb.append(Messages.getString("export.time"));
            sb.append("</div>\n");
            sb.append("<div class='datavalue' id='timevalue'>");
            if (dive.getDate() != null) {
                sb.append(FORMAT_TIME.format(dive.getDate()));
            } else {
                sb.append("&nbsp;");
            }
            sb.append("</div>\n");
            sb.append("</div>\n");
        }
        DiveSite site = logBook.getMasterdata().getDiveSiteByPrivateId(dive.getDiveSiteId());
        if (site != null) {
            if (logBook.getExportSettings().showDetailLocation() && site.getSpot() != null) {
                sb.append("<div class='datarow-" + getOddEvenString(count++) + "' id='spotrow'>\n");
                sb.append("<div class='datalabel' id='spotlabel'>");
                sb.append(Messages.getString("export.place"));
                sb.append("</div>\n");
                sb.append("<div class='datavalue' id='spotvalue'>");
                sb.append(toHtml(site.getSpot(), false));
                sb.append("</div>\n");
                sb.append("</div>\n");
            }
            if (logBook.getExportSettings().showDetailCity() && site.getCity() != null) {
                sb.append("<div class='datarow-" + getOddEvenString(count++) + "' id='cityrow'>\n");
                sb.append("<div class='datalabel' id='citylabel'>");
                sb.append(Messages.getString("export.city"));
                sb.append("</div>\n");
                sb.append("<div class='datavalue' id='cityvalue'>");
                sb.append(toHtml(site.getCity(), false));
                sb.append("</div>\n");
                sb.append("</div>\n");
            }
            if (logBook.getExportSettings().showDetailCountry() && site.getCountry() != null) {
                sb.append("<div class='datarow-" + getOddEvenString(count++) + "' id='countryrow'>\n");
                sb.append("<div class='datalabel' id='countrylabel'>");
                sb.append(Messages.getString("export.country"));
                sb.append("</div>\n");
                sb.append("<div class='datavalue' id='countryvalue'>");
                sb.append(toHtml(site.getCountry(), false));
                sb.append("</div>\n");
                sb.append("</div>\n");
            }
            if (logBook.getExportSettings().showDetailWaters() && site.getWaters() != null) {
                sb.append("<div class='datarow-" + getOddEvenString(count++) + "' id='watersrow'>\n");
                sb.append("<div class='datalabel' id='waterslabel'>");
                sb.append(Messages.getString("export.waters"));
                sb.append("</div>\n");
                sb.append("<div class='datavalue' id='watersvalue'>");
                sb.append(toHtml(site.getWaters(), false));
                sb.append("</div>\n");
                sb.append("</div>\n");
            }
        }
        if (logBook.getExportSettings().showDetailDepth() && dive.getDepth() != null) {
            sb.append("<div class='datarow-" + getOddEvenString(count++) + "' id='depthrow'>\n");
            sb.append("<div class='datalabel' id='depthlabel'>");
            sb.append(Messages.getString("export.depth"));
            sb.append("</div>\n");
            sb.append("<div class='datavalue' id='depthvalue'>");
            sb.append(DECIMALFORMAT.format(c.convertAltitude(dive.getDepth())));
            sb.append(" ");
            sb.append(UnitConverter.getDisplayAltitudeUnit());
            sb.append("</div>\n");
            sb.append("</div>\n");
        }
        if (logBook.getExportSettings().showDetailDuration() && dive.getDuration() != null) {
            sb.append("<div class='datarow-" + getOddEvenString(count++) + "' id='durationrow'>\n");
            sb.append("<div class='datalabel' id='durationlabel'>");
            sb.append(Messages.getString("export.duration"));
            sb.append("</div>\n");
            sb.append("<div class='datavalue' id='durationvalue'>");
            sb.append(DECIMALFORMAT.format(c.convertTime(dive.getDuration())));
            sb.append(" "); //$NON-NLS-1$
            sb.append(UnitConverter.getDisplayTimeUnit());
            sb.append("</div>\n");
            sb.append("</div>\n");
        }
        if (logBook.getExportSettings().showDetailVisibility() && dive.getVisibility() != null) {
            sb.append("<div class='datarow-" + getOddEvenString(count++) + "' id='visibilityrow'>\n");
            sb.append("<div class='datalabel' id='visibilitylabel'>");
            sb.append(Messages.getString("export.visibility"));
            sb.append("</div>\n");
            sb.append("<div class='datavalue' id='visibilityvalue'>");
            sb.append(toHtml(dive.getVisibility(), false));
            sb.append("</div>\n");
            sb.append("</div>\n");
        }
        if (logBook.getExportSettings().showDetailBuddy() && dive.getBuddy() != null) {
            sb.append("<div class='datarow-" + getOddEvenString(count++) + "' id='buddyrow'>\n");
            sb.append("<div class='datalabel' id='buddylabel'>");
            sb.append(Messages.getString("export.buddy"));
            sb.append("</div>\n");
            sb.append("<div class='datavalue' id='buddyvalue'>");
            sb.append(toHtml(dive.getBuddy(), true));
            sb.append("</div>\n");
            sb.append("</div>\n");
        }
        if (logBook.getExportSettings().showDetailComment() && dive.getComment() != null) {
            sb.append("<div class='datarow-" + getOddEvenString(count++) + "' id='commentrow'>\n");
            sb.append("<div class='datalabel' id='commentlabel'>");
            sb.append(Messages.getString("export.comment"));
            sb.append("</div>\n");
            sb.append("<div class='datavalue' id='commentvalue'>");
            sb.append(toHtml(dive.getComment(), true));
            sb.append("</div>\n");
            sb.append("</div>\n");
        }
        if (logBook.getExportSettings().showDetailEquipment() && dive.getEquipment() != null) {
            sb.append("<div class='datarow-" + getOddEvenString(count++) + "' id='suitrow'>\n");
            sb.append("<div class='datalabel' id='suitlabel'>");
            sb.append(Messages.getString("export.equipment.suit"));
            sb.append("</div>\n");
            sb.append("<div class='datavalue' id='suitvalue'>");
            sb.append(toHtml(dive.getEquipment().getSuit(), false));
            sb.append("</div>\n");
            sb.append("</div>\n");

            sb.append("<div class='datarow-" + getOddEvenString(count++) + "' id='glovesrow'>\n");
            sb.append("<div class='datalabel' id='gloveslabel'>");
            sb.append(Messages.getString("export.equipment.gloves"));
            sb.append("</div>\n");
            sb.append("<div class='datavalue' id='glovesvalue'>");
            sb.append(toHtml(dive.getEquipment().getGloves(), false));
            sb.append("</div>\n");
            sb.append("</div>\n");

            sb.append("<div class='datarow-" + getOddEvenString(count++) + "' id='weightsrow'>\n");
            sb.append("<div class='datalabel' id='weightslabel'>");
            sb.append(Messages.getString("export.equipment.weights"));
            sb.append("</div>\n");
            sb.append("<div class='datavalue' id='weightsvalue'>");
            sb.append(toHtml(dive.getEquipment().getWeight(), false));
            sb.append("</div>\n");
            sb.append("</div>\n");

            if (dive.getEquipment().getTanks() != null) {
                sb.append("<div class='datarow-" + getOddEvenString(count++) + "' id='tanksrow'>\n");
                sb.append("<div class='datalabel' id='tankslabel'>");
                sb.append(Messages.getString("export.equipment.tanks"));
                sb.append("</div>\n");
                sb.append("<div class='datavalue' id='tanksvalue'>");
                Iterator<Tank> it = dive.getEquipment().getTanks().iterator();
                for (int i = 0; it.hasNext(); i++) {
                    Tank t = it.next();
                    if (i > 0) {
                        sb.append(", ");
                    }
                    if (t.getGas().getTankvolume() != null) {
                        sb.append(DECIMALFORMAT.format(c.convertVolume(t.getGas().getTankvolume())));
                        sb.append(UnitConverter.getDisplayVolumeUnit());
                        sb.append(" ");
                    }
                    if (t.getGas().getName() != null) {
                        sb.append(toHtml(t.getGas().getName(), false));
                    }
                    if (t.getType() != null && !"".equals(t.getType())) {
                        sb.append(" (");
                        sb.append(toHtml(t.getType(), false));
                        sb.append(")");
                    }
                }
                sb.append("</div>\n");
                sb.append("</div>\n");
            }

            if (dive.getEquipment().getComment() != null && !"".equals(dive.getEquipment().getComment())) {
                sb.append("<div class='datarow-" + getOddEvenString(count++) + "' id='equipmentcommentrow'>\n");
                sb.append("<div class='datalabel' id='equipmentcommentlabel'>");
                sb.append(Messages.getString("export.equipment.comment"));
                sb.append("</div>\n");
                sb.append("<div class='datavalue' id='equipmentcommentvalue'>");
                sb.append(toHtml(dive.getEquipment().getComment(), true));
                sb.append("</div>\n");
                sb.append("</div>\n");
            }
        }

        sb.append("</div>\n");
        sb.append("</div></div></div></div>\n");
        sb.append("</div>\n\n");

        sb.append(getDetailsPageFooter());
        sb.append(getPageFooter());
        try {
            writeHTML(sb.toString(), new File(diveDir, "details.html")); //$NON-NLS-1$
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "error writing details", ioe);
            throw new ExportException(Messages.getString("export.error.could_not_write_details_html") + " " + diveDir.getPath()); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    private void createPicturesPage(int diveNumber, JDive dive) throws ExportException {
        File diveDir = new File(outputDir, String.valueOf(diveNumber));
        StringBuffer sb = new StringBuffer();
        sb.append(getPageHeader(getTitle(dive), dive));
        sb.append(getPicturesPageHeader(diveNumber, dive));

        ArrayList<Picture> pics = removeInvalidPictures(dive.getPictures());
        if (pics != null) {
            int count = 0;
            Iterator<Picture> it = pics.iterator();
            while (it.hasNext()) {
                Picture pic = it.next();
                if ((logBook.getExportSettings().isFullExport() && !logBook.getExportSettings().isKeepImages()) || !dive.isHtmlExported()) {
                    createPicture(diveNumber, count, pic);
                }
                createPicturePage(diveNumber, count, dive, pic, pics.size());
                Runtime.getRuntime().runFinalization();
                Runtime.getRuntime().gc();

                sb.append("<a class='imagelink' href='picture");
                sb.append(count);
                sb.append(".html'>\n");
                sb.append("<div class='thumbcontainer' id='" + count + "'>\n");
                sb.append("<div id='br'><div id='tr'><div id='tl'><div id='bl'>\n");
                sb.append("<div class='thumbdiv'>\n");
                sb.append("<img class='thumbimg' src='image" + count + "_thumb.jpg'/>\n");
                sb.append("</div>\n");
                sb.append("<div class='thumbdesc'>");
                sb.append(pic.getName());
                sb.append("</div>\n");
                sb.append("</div></div></div></div>\n");
                sb.append("</div>\n");
                sb.append("</a>\n\n");

                count++;
            }
        }

        sb.append(getPicturesPageFooter());
        sb.append(getPageFooter());
        try {
            writeHTML(sb.toString(), new File(diveDir, "pictures.html")); //$NON-NLS-1$
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "error writing pictures page", ioe);
            throw new ExportException(Messages.getString("export.error.could_not_write_pictures_html") + " " + diveDir.getPath()); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    private String getPicturesPageHeader(int diveNumber, JDive dive) {
        StringBuffer sb = new StringBuffer();
        sb.append("<!-- begin navigation -->\n");
        sb.append("<div class='navigation'>\n");
        sb.append("<div id='tl'><div id='tr'><div id='br'><div id='bl'>\n");
        sb.append("<a class='menulink' id='go_to_index' href='../index");
        if (logBook.getExportSettings().groupByYear()) {
            if (dive.getDate() != null) {
                sb.append(FORMAT_YEAR.format(dive.getDate()));
            } else {
                sb.append("XXXX");
            }
        }
        sb.append(".html#");
        sb.append(diveNumber);
        sb.append("'><div class='menuitem'><div id='br'><div id='tr'><div id='tl'><div id='bl'>");
        sb.append(Messages.getString("export.return_to_index"));
        sb.append("</div></div></div></div></div></a>\n");

        if (logBook.getExportSettings().isDetailVisible()) {
            sb.append("<a class='menulink' id='go_to_details' href='details.html'><div class='menuitem'><div id='br'><div id='tr'><div id='tl'><div id='bl'>");
            sb.append(Messages.getString("export.data"));
            sb.append("</div></div></div></div></div></a>\n");
        }

        sb.append("<a class='menulink' id='go_to_pictures' href='pictures.html'><div class='menuitem'><div id='br'><div id='tr'><div id='tl'><div id='bl'>");
        sb.append(Messages.getString("export.pictures"));
        sb.append("</div></div></div></div></div></a>\n");

        sb.append("</div></div></div></div>\n");
        sb.append("</div>\n");
        sb.append("<!-- end navigation -->\n\n");

        sb.append("<!-- begin pictures -->\n");
        sb.append("<div class='pictures'>\n");
        sb.append("<div id='br'><div id='tr'><div id='tl'><div id='bl'>\n\n");

        sb.append("<div class='picturestitle'>");
        sb.append(Messages.getString("export.pictures"));
        sb.append("</div>\n\n");

        sb.append("<!-- begin pictures container -->\n");
        sb.append("<div class='picturescontainer'>\n");
        sb.append("<div id='br'><div id='tr'><div id='tl'><div id='bl'>\n\n");
        return sb.toString();
    }

    private String getPicturesPageFooter() {
        StringBuffer sb = new StringBuffer();
        sb.append("</div></div></div></div>\n");
        sb.append("</div>\n");
        sb.append("<!-- end pictures container -->\n\n");

        sb.append("</div></div></div></div>\n");
        sb.append("</div>\n");
        sb.append("<!-- end pictures -->\n\n");
        return sb.toString();
    }

    private String getPicturePageHeader(int diveNumber, JDive dive) {
        StringBuffer sb = new StringBuffer();
        sb.append("<!-- begin navigation -->\n");
        sb.append("<div class='navigation'>\n");
        sb.append("<div id='tl'><div id='tr'><div id='br'><div id='bl'>\n");
        sb.append("<a class='menulink' id='go_to_index' href='../index");
        if (dive.getDate() != null) {
            sb.append(FORMAT_YEAR.format(dive.getDate()));
        } else {
            sb.append("XXXX");
        }
        sb.append(".html#");
        sb.append(diveNumber);
        sb.append("'><div class='menuitem'><div id='br'><div id='tr'><div id='tl'><div id='bl'>");
        sb.append(Messages.getString("export.return_to_index"));
        sb.append("</div></div></div></div></div></a>\n");

        if (logBook.getExportSettings().isDetailVisible()) {
            sb.append("<a class='menulink' id='go_to_details' href='details.html'><div class='menuitem'><div id='br'><div id='tr'><div id='tl'><div id='bl'>");
            sb.append(Messages.getString("export.data"));
            sb.append("</div></div></div></div></div></a>\n");
        }

        sb.append("<a class='menulink' id='go_to_pictures' href='pictures.html'><div class='menuitem'><div id='br'><div id='tr'><div id='tl'><div id='bl'>");
        sb.append(Messages.getString("export.pictures"));
        sb.append("</div></div></div></div></div></a>\n");

        sb.append("</div></div></div></div>\n");
        sb.append("</div>\n");
        sb.append("<!-- end navigation -->\n\n");

        return sb.toString();
    }

    private String getDetailsPageHeader(int diveNumber, JDive dive) {
        StringBuffer sb = new StringBuffer();
        sb.append("<!-- begin navigation -->\n");
        sb.append("<div class='navigation'>\n");
        sb.append("<div id='tl'><div id='tr'><div id='br'><div id='bl'>\n");
        sb.append("<a class='menulink' id='go_to_index' href='../index");
        if (logBook.getExportSettings().groupByYear()) {
            if (dive.getDate() != null) {
                sb.append(FORMAT_YEAR.format(dive.getDate()));
            } else {
                sb.append("XXXX");
            }
        }
        sb.append(".html#");
        sb.append(diveNumber);
        sb.append("'><div class='menuitem'><div id='br'><div id='tr'><div id='tl'><div id='bl'>");
        sb.append(Messages.getString("export.return_to_index"));
        sb.append("</div></div></div></div></div></a>\n");
        sb.append("<a class='menulink' id='go_to_details' href='details.html'><div class='menuitem'><div id='br'><div id='tr'><div id='tl'><div id='bl'>");
        sb.append(Messages.getString("export.data"));
        sb.append("</div></div></div></div></div></a>\n");

        if (logBook.getExportSettings().isPictureVisible() && dive.getPictures() != null && dive.getPictures().size() > 0) {
            sb
                    .append("<a class='menulink' id='go_to_pictures' href='pictures.html'><div class='menuitem'><div id='br'><div id='tr'><div id='tl'><div id='bl'>");
            sb.append(Messages.getString("export.pictures"));
            sb.append("</div></div></div></div></div></a>\n");
        }

        sb.append("</div></div></div></div>\n");
        sb.append("</div>\n");
        sb.append("<!-- end navigation -->\n\n");

        sb.append("<!-- begin detail data -->\n");
        sb.append("<div class='detaildata'>\n");
        sb.append("<div id='br'><div id='tr'><div id='tl'><div id='bl'>\n\n");

        sb.append("<div class='detailtitle'>");
        sb.append(Messages.getString("export.data"));
        sb.append("</div>\n");
        return sb.toString();
    }

    private String getDetailsPageFooter() {
        StringBuffer sb = new StringBuffer();
        sb.append("</div></div></div></div>\n");
        sb.append("</div>\n");
        sb.append("<!-- end detail data -->");
        return sb.toString();
    }

    private String getTitle(JDive dive) {
        StringBuffer title = new StringBuffer(logBook.getExportSettings().getIndexTitle());
        if (dive.getDate() != null) {
            title.append(" - "); //$NON-NLS-1$
            title.append(FORMAT_DATE.format(dive.getDate()));
        }
        DiveSite site = logBook.getMasterdata().getDiveSiteByPrivateId(dive.getDiveSiteId());
        if (site != null && site.getSpot() != null && !"".equals(site.getSpot())) { //$NON-NLS-1$
            title.append(" "); //$NON-NLS-1$
            title.append(toHtml(site.getSpot(), false));
        }
        if (site != null && site.getCity() != null && !"".equals(site.getCity())) {
            if (site.getSpot() != null && !"".equals(site.getSpot())) {
                title.append(", ");
            }
            title.append(toHtml(site.getCity(), false));
        }
        if (site != null && site.getCountry() != null && !"".equals(site.getCountry())) { //$NON-NLS-1$
            title.append(" ("); //$NON-NLS-1$
            title.append(toHtml(site.getCountry(), false));
            title.append(")"); //$NON-NLS-1$
        }
        return title.toString();
    }

    private void createPicturePage(int diveNumber, int pictureNumber, JDive dive, Picture picture, int numberOfImages) throws ExportException {
        File diveDir = new File(outputDir, String.valueOf(diveNumber));
        if ((logBook.getExportSettings().isFullExport() && !logBook.getExportSettings().isKeepImages()) || !dive.isHtmlExported()) {
            createPicture(diveNumber, pictureNumber, picture);
        }
        Runtime.getRuntime().runFinalization();
        Runtime.getRuntime().gc();
        StringBuffer sb = new StringBuffer();
        sb.append(getPageHeader(getTitle(dive), dive));
        sb.append(getPicturePageHeader(diveNumber, dive));

        sb.append("<!-- begin picture -->\n");
        sb.append("<div class='picture'>\n");
        sb.append("<div id='br'><div id='tr'><div id='tl'><div id='bl'>\n\n");

        sb.append("<!-- begin picture navigation -->\n");
        sb.append("<div class='picturenavigation'>\n");
        sb.append("<div id='br'><div id='tr'><div id='tl'><div id='bl'>\n");
        if (pictureNumber > 0) {
            sb.append("<a class='menulink' id='go_to_prev' href='picture");
            sb.append(pictureNumber - 1);
            sb.append(".html'><div class='menuitem' id='go_to_prev'><div id='tl'><div id='tr'><div id='br'><div id='bl'>");
            sb.append(Messages.getString("export.previous"));
            sb.append("</div></div></div></div></div></a>\n");
        }
        sb
                .append("<a class='menulink' id='go_to_index' href='pictures.html'><div class='menuitem' id='go_to_index'><div id='tl'><div id='tr'><div id='br'><div id='bl'>Bilder &Uuml;bersicht</div></div></div></div></div></a>\n");
        if (pictureNumber < numberOfImages - 1) {
            sb.append("<a class='menulink' id='go_to_next' href='picture");
            sb.append(pictureNumber + 1);
            sb.append(".html'><div class='menuitem' id='go_to_next'><div id='tl'><div id='tr'><div id='br'><div id='bl'>");
            sb.append(Messages.getString("export.next"));
            sb.append("</div></div></div></div></div></a>\n");
        }
        sb.append("</div></div></div></div>\n");
        sb.append("</div>\n");
        sb.append("<!-- end picture navigation -->\n\n");

        sb.append("<!-- begin picture container -->\n");
        sb.append("<div class='picturecontainer'>\n");
        sb.append("<div id='br'><div id='tr'><div id='tl'><div id='bl'>\n\n");

        sb.append("<div class='imagecontainer'>\n");
        sb.append("<div id='br'><div id='tr'><div id='tl'><div id='bl'>\n");
        sb.append("<img class='img' src='image" + pictureNumber + ".jpg'/>\n");
        sb.append("</div></div></div></div>\n");
        sb.append("</div>\n");

        if (logBook.getExportSettings().showPictureName() && picture.getName() != null && !"".equals(picture.getName())) {
            sb.append("<div class='imagename'>\n");
            sb.append("<div id='br'><div id='tr'><div id='tl'><div id='bl'>\n");
            sb.append(toHtml(picture.getName(), false));
            sb.append("</div></div></div></div>\n");
            sb.append("</div>\n");
        }

        if (logBook.getExportSettings().showPictureDescription() && picture.getDescription() != null && !"".equals(picture.getDescription())) {
            sb.append("<div class='imagedesc'>\n");
            sb.append("<div id='br'><div id='tr'><div id='tl'><div id='bl'>\n");
            sb.append(toHtml(picture.getDescription(), true));
            sb.append("</div></div></div></div>\n");
            sb.append("</div>\n");
        }

        sb.append("</div></div></div></div>\n");
        sb.append("</div>\n");
        sb.append("<!-- end picture container -->\n\n");

        sb.append("</div></div></div></div>\n");
        sb.append("</div>\n");
        sb.append("<!-- end picture -->\n\n");

        sb.append(getPageFooter());
        try {
            writeHTML(sb.toString(), new File(diveDir, "picture" + pictureNumber + ".html")); //$NON-NLS-1$ //$NON-NLS-2$
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "error writing picture page", ioe);
            throw new ExportException(
                    Messages.getString("export.error.could_not_write_picture_page_for_picture") + " " + pictureNumber + " " + Messages.getString("export.of_dive") + " " + diveNumber); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        }
    }

    private String toHtml(String text, boolean newlines) {
        if (text == null) {
            return "";
        }
        text = text.replace("<", "&lt;");
        text = text.replace(">", "&gt;");
        if (newlines) {
            text = text.replace("\n", "<br>");
        }
        return text;
    }

    private void createPicture(int diveNumber, int pictureNumber, Picture picture) throws ExportException {
        File diveDir = new File(outputDir, String.valueOf(diveNumber));
        File imgFile = new File(diveDir, "image" + pictureNumber + ".jpg"); //$NON-NLS-1$ //$NON-NLS-2$
        File thumbFile = new File(diveDir, "image" + pictureNumber + "_thumb.jpg"); //$NON-NLS-1$ //$NON-NLS-2$
        int thumbMaxX = logBook.getExportSettings().getPictureThumbnailMaxWidth();
        int thumbMaxY = logBook.getExportSettings().getPictureThumbnailMaxHeight();
        int imgMaxX = logBook.getExportSettings().getPictureImageMaxWidth();
        int imgMaxY = logBook.getExportSettings().getPictureImageMaxHeight();
        String filename = picture.getFilename();
        Image img = loadImage(filename);
        try {
            writeScaledJPG(img, imgMaxX, imgMaxY, picture.getRotation(), imgFile, logBook.getExportSettings().isWatermarkEnabled());
            writeScaledJPG(img, thumbMaxX, thumbMaxY, picture.getRotation(), thumbFile, false);
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "error creating picture", ioe);
            throw new ExportException(Messages.getString("export.error.could_not_write_images_to") + " " + diveDir.getPath()); //$NON-NLS-1$  //$NON-NLS-2$
        }
    }

    /**
     * @param img
     *            The image to save
     * @param imgMaxX
     *            maximum width of the image
     * @param imgMaxY
     *            maximum height of the image
     * @param imgRotation
     *            totation of the image (0=0°, 1=90°, 2=180°, 3=270°)
     * @param file
     *            the file which should be written
     * @param createWatermark
     *            whether a watermark should be created or not
     * @throws IOException
     *             an IOException if the Image could not be saved.
     */
    private void writeScaledJPG(Image img, int imgMaxX, int imgMaxY, int imgRotation, File file, boolean createWatermark) throws IOException {
        BufferedImage scaledImg = ImageUtil.transform(img, imgMaxX, imgMaxY, imgRotation);
        if (createWatermark) {
            int border = 20;

            Graphics2D g = (Graphics2D) scaledImg.getGraphics();
            AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
            g.setComposite(alpha);
            g.setColor(Color.white);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setFont(new Font("Arial", Font.BOLD, 25));

            FontMetrics fm = g.getFontMetrics();
            Rectangle2D wmShape = fm.getStringBounds(logBook.getExportSettings().getWatermarkText(), g);

            g.drawString(logBook.getExportSettings().getWatermarkText(), scaledImg.getWidth() - (int) wmShape.getWidth() - border, scaledImg.getHeight()
                    - (int) wmShape.getHeight());
            g.dispose();
        }
        writeJPG(scaledImg, file);
    }

    private void writeJPG(BufferedImage img, File file) throws IOException {
        int quality = logBook.getExportSettings().getImageQuality();
        quality = Math.max(0, Math.min(quality, 100));
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        JPEGImageWriter w = new JPEGImageWriter();
        ImageWriterParams params = new ImageWriterParams();
        params.setJPEGQuality(quality / 100.0f, false);
        w.writeImage(img, out, params);
        out.close();
        scp(file);
    }

    private void writeHTML(String content, File file) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(content);
        bw.flush();
        bw.close();
        scp(file);
    }

    private void scpFull() {
        String exportdir = logBook.getExportSettings().getExportDirectory();
        File destDir = new File(logBook.getExportSettings().getScpDirectory());
        String user = logBook.getExportSettings().getScpUser();
        String host = logBook.getExportSettings().getScpHost();
        String dest = user + "@" + host + ":" + destDir.getPath();

        String command = "scp -r " + exportdir + " " + dest;

        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            int result = p.waitFor();
            if (result != 0) {
                String s;
                LOGGER.warning(command + ":\n");
                while ((s = stdInput.readLine()) != null) {
                    LOGGER.warning(s);
                }
                while ((s = stdError.readLine()) != null) {
                    LOGGER.warning(s);
                }
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "interrupted while copying by scp", e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "error copying by scp", e);
        }
    }

    private void scp(File source) {
        if (logBook.getExportSettings().isScpEnabled() && !logBook.getExportSettings().isFullExport()) {
            String outpath = outputDir.getPath();
            String filepath = source.getPath();
            String relname = filepath.substring(outpath.length());
            File destDir = new File(logBook.getExportSettings().getScpDirectory());
            File destFile = new File(destDir, relname);
            String user = logBook.getExportSettings().getScpUser();
            String host = logBook.getExportSettings().getScpHost();
            String dest = user;
            dest += "@" + host + ":" + destFile.getPath();
            try {
                Process p = Runtime.getRuntime().exec(new String[] { "scp", filepath, dest });
                try {
                    int result = p.waitFor();
                    if (result != 0) {
                        dest = user;
                        dest += "@" + host + ":" + destFile.getParentFile().getParent();
                        p = Runtime.getRuntime().exec(new String[] { "scp", "-r", source.getParent(), dest });
                        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

                        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        result = p.waitFor();
                        if (result != 0) {
                            String s;
                            LOGGER.warning("scp -r " + source.getParent() + " " + dest + ":\n");
                            while ((s = stdInput.readLine()) != null) {
                                LOGGER.warning(s);
                            }
                            while ((s = stdError.readLine()) != null) {
                                LOGGER.warning(s);
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    LOGGER.log(Level.SEVERE, "interrupted while copying by scp", e);

                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "error copying by scp", e);
            }
        }
    }

    private Image loadImage(String filename) {
        File f = new File(filename);
        if (f.exists() && f.canRead()) {
            Image img = Toolkit.getDefaultToolkit().getImage(filename);
            int sizeX = img.getWidth(null);
            int sizeY = img.getHeight(null);
            MediaTracker mediaTracker = new MediaTracker(new Container());
            int id = 0;
            mediaTracker.addImage(img, id);
            try {
                mediaTracker.waitForID(id);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "interrupted while loading image", e);
            }
            while (sizeX == -1 || sizeY == -1) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e1) {
                    LOGGER.log(Level.SEVERE, "interrupted while loading image", e1);
                }
                sizeX = img.getWidth(null);
                sizeY = img.getHeight(null);
            }
            return img;
        }
        return null;
    }

    private ArrayList<Picture> removeInvalidPictures(ArrayList<Picture> pics) {
        if (pics == null) {
            return null;
        }
        ArrayList<Picture> filtered = new ArrayList<Picture>(pics.size());
        Iterator<Picture> it = pics.iterator();
        while (it.hasNext()) {
            Picture pic = it.next();
            if (pic.getFilename() != null) {
                File f = new File(pic.getFilename());
                if (f.exists() && f.canRead()) {
                    filtered.add(pic);
                } else {
                    LOGGER.warning("skipping pic '" + pic.getName() + "' (" + pic.getFilename() + "). File does not exists or could no be read!");
                }
            } else {
                LOGGER.warning("skipping pic '" + pic.getName() + "'. no filename specified");
            }
        }
        return filtered;
    }

    private String getOddEvenString(int num) {
        return num % 2 == 0 ? "even" : "odd";
    }

    private static final void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;

        while ((len = in.read(buffer)) >= 0)
            out.write(buffer, 0, len);

        in.close();
        out.close();
    }
}