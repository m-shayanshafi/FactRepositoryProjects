/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ImageUtil.java
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.JDiveLog;
import net.sf.jdivelog.model.Picture;

public class ImageUtil {
    
    private static ArrayList<String> imageFileNames;
    private static int currentIdx;
    
    public static void setLogbook(JDiveLog logbook) {
        imageFileNames = new ArrayList<String>();
        TreeSet<RatedPicture> set = new TreeSet<RatedPicture>();
        Iterator<JDive> diveIt = logbook.getDives().iterator();
        while (diveIt.hasNext()) {
            JDive dive = diveIt.next();
            if (dive.getPictures() != null) {
                Iterator<Picture> picIt = dive.getPictures().iterator();
                while (picIt.hasNext()) {
                    Picture pic = picIt.next();
                    set.add(new RatedPicture(pic.getRating(), pic.getFilename()));
                }
            }
        }
        Iterator<RatedPicture> picIt = set.iterator();
        while(picIt.hasNext()) {
            imageFileNames.add(picIt.next().filename);
        }
        currentIdx = 0;
        
    }
    
    public static String getNextImage() {
        if (imageFileNames.size() == 0) {
            return null;
        }
        if (currentIdx >= imageFileNames.size()) {
            currentIdx = 0;
        }
        return imageFileNames.get(currentIdx++);
    }
    
    //
    // inner classes
    //
    
    private static final class RatedPicture implements Comparable<RatedPicture>{
        
        private int rating;
        private String filename;
        
        public RatedPicture(int rating, String filename) {
            this.rating = rating;
            this.filename = filename;
        }

        public int compareTo(RatedPicture o) {
            if (rating > o.rating) {
                return -1;
            }
            if (rating < o.rating) {
                return 1;
            }
            return filename.compareTo(o.filename);
        }
        
    }

}
