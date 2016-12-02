/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: PictureTableModel.java
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

import java.awt.Container;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.statusbar.StatusInterface;
import net.sf.jdivelog.gui.util.ImageUtil;
import net.sf.jdivelog.model.Picture;

import org.apache.batik.ext.awt.image.codec.jpeg.JPEGImageWriter;
import org.apache.batik.ext.awt.image.spi.ImageWriterParams;

/**
 * Description: Table of pictures showing the name, the picture, the description and the rating
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class PictureTableModel extends AbstractTableModel {

    private static final Logger LOGGER = Logger.getLogger(PictureTableModel.class.getName());
    
    private static final long serialVersionUID = 4049916069575865906L;

    private static final String[] COLUMN_NAMES = { Messages.getString("image"), Messages.getString("rotation"), Messages.getString("name"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            Messages.getString("description"), Messages.getString("rating")}; //$NON-NLS-1$ //$NON-NLS-2$

    public static final int THUMB_MAX_X = 120;

    public static final int THUMB_MAX_Y = 100;

    private HashMap<Picture,Image> thumbs = new HashMap<Picture,Image>();

    private ArrayList<Picture> pictures = null;

    private HashSet<File> filenames = new HashSet<File>();
    
    private String[] rotationLabels;
    
    private HashMap<String,Integer> rotationLabelMap;
    
    private String[] ratingLabels;
    
    private HashMap<String, Integer> ratingLabelMap;
    
    private StatusInterface status;
    
    public PictureTableModel(StatusInterface status) {
        this.status = status;
        rotationLabels = new String[] {Messages.getString("rotate0label"), Messages.getString("rotate90label"), Messages.getString("rotate180label"), Messages.getString("rotate270label")};
        rotationLabelMap = new HashMap<String, Integer>();
        for (int i=0; i<rotationLabels.length;i++) {
            rotationLabelMap.put(rotationLabels[i], i);
        }
        ratingLabels = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ratingLabelMap = new HashMap<String, Integer>();
        for (int i=0; i<ratingLabels.length; i++) {
            ratingLabelMap.put(ratingLabels[i], i);
        }
    }

    public int getRowCount() {
        if (pictures == null) {
            return 0;
        }
        return pictures.size();
    }

    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (pictures != null && rowIndex >= 0 && rowIndex < pictures.size()) {
            Picture p = pictures.get(rowIndex);
            switch (columnIndex) {
            case 0:
                return thumbs.get(p);
            case 1:
                return rotationLabels[p.getRotation()];
            case 2:
                return p.getName();
            case 3:
                return p.getDescription();
            case 4:
                return ratingLabels[p.getRating()];
            }
        }
        return null;
    }
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Picture p = pictures.get(rowIndex);
        switch (columnIndex) {
            case 1 :
                int newRotation = rotationLabelMap.get(aValue);
                if (newRotation != p.getRotation()) {
                    p.setRotation(newRotation);
                    updateThumb(p);
                }
                break;
            case 2 : p.setName(aValue.toString());
                break;
        	case 3 : p.setDescription(aValue.toString());
        		break;
            case 4 :
                int newRating = ratingLabelMap.get(aValue);
                p.setRating(newRating);
                break;
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1 || columnIndex == 2 || columnIndex == 3 || columnIndex == 4;
    }

    public void setPictures(ArrayList<Picture> pictures) {
        this.pictures = pictures;
        this.thumbs = new HashMap<Picture,Image>();
        reloadThumbs(false);
    }

    public ArrayList<Picture> getPictures() {
        return pictures;
    }

    public void reloadThumbs(boolean recreateThumbs) {
        if (pictures != null && pictures.size() > 0) {
            status.messageInfo(Messages.getString("loading_images"));
            status.countingProgressbarStart(pictures.size(), true);
            for (int i = 0; i < pictures.size(); i++) {
                Picture p = pictures.get(i);
                File thumbFile = getThumbFile(new File(p.getFilename()));
                if (!recreateThumbs && thumbFile.exists() && thumbFile.canRead()) {
                    Image thumb = Toolkit.getDefaultToolkit().createImage(
                            thumbFile.getPath());
                    MediaTracker mt = new MediaTracker(new Container());
                    mt.addImage(thumb, i);
                    try {
                        mt.waitForID(i);
                    } catch (InterruptedException e) {
                        LOGGER.log(Level.SEVERE, "interrupted while loading image", e);
                    }
                    setThumb(p, thumb);
                } else {
                    updateThumb(pictures.get(i));
                }
                status.countingProgressbarIncrement();
            }
            status.countingProgressbarEnd();
            status.messageClear();
        } else {
            thumbs.clear();
            filenames.clear();
        }
    }

    //
    // private methods
    //

    private void setThumb(Picture p, Image img) {
        thumbs.put(p, img);
        fireTableCellUpdated(indexOf(p), 0);
    }

    private void updateThumb(Picture picture) {
        File f = new File(picture.getFilename());
        filenames.add(f);
        Image img = Toolkit.getDefaultToolkit().getImage(picture.getFilename());
        BufferedImage thumb = ImageUtil.transform(img, THUMB_MAX_X, THUMB_MAX_Y, picture.getRotation());
        setThumb(picture, thumb);
        try {
            BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream(
                            getThumbFile(new File(picture.getFilename()))));
            JPEGImageWriter w = new JPEGImageWriter();
            ImageWriterParams params = new ImageWriterParams();
            int quality = 100;
            quality = Math.max(0, Math.min(quality, 100));
            params.setJPEGQuality(quality/100.0f, false);
            w.writeImage(thumb, out, params);
            out.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "error while updating thumbnail", e);
        }
    }

    private File getThumbFile(File image) {
        File f = new File(image.getParent(), image.getName().substring(0,
                image.getName().lastIndexOf(".")) //$NON-NLS-1$
                + ".tmb"); //$NON-NLS-1$
        return f;
    }

    private int indexOf(Picture p) {
        for (int i = 0; i < pictures.size(); i++) {
            if (p.equals(pictures.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public boolean containsFile(File file) {
        return filenames.contains(file);
    }

    public void add(Picture p) {
        if (pictures == null) {
            pictures = new ArrayList<Picture>();
        }
        pictures.add(p);
        updateThumb(p);
        fireTableDataChanged();
    }
    
    public void remove(int[] rows) {
        ArrayList<Picture> picsToRemove = new ArrayList<Picture>(rows.length);
        for (int i=0; i<rows.length; i++) {
            picsToRemove.add(pictures.get(rows[i]));
        }
        pictures.removeAll(picsToRemove);
        fireTableDataChanged();
    }

    public void add(ArrayList<Picture> pics) {
        if (pictures == null) {
            pictures = new ArrayList<Picture>();
        }
        pictures.addAll(pics);
        reloadThumbs(false);
        fireTableDataChanged();
    }

    public String[] getColumnNames() {
        return COLUMN_NAMES;
    }
    
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }
    
    public String[] getRotationLabels() {
        return rotationLabels;
    }
    
    public String[] getRatingLabels() {
        return ratingLabels;
    }

}