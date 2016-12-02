/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ExportSettings.java
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
package net.sf.jdivelog.model;

/**
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class ExportSettings {
    
    private String indexTitle = "";
    private int indexImages = 0;
    private boolean indexGroupByYear = false;
    private boolean indexDate = true;
    private boolean indexTime = false;
    private boolean indexLocation = true;
    private boolean indexCity = true;
    private boolean indexCountry = true;
    private boolean indexDepth = false;
    private boolean indexDuration = false;
    private boolean indexPictureCount = true;
    
    private boolean detailVisible = true;
    private boolean detailDate = true;
    private boolean detailTime = true;
    private boolean detailLocation = true;
    private boolean detailCity = true;
    private boolean detailCountry = true;
    private boolean detailWaters = true;
    private boolean detailDepth = true;
    private boolean detailDuration = true;
    private boolean detailEquipment = false;
    private boolean detailProfile = true;
    private boolean detailBuddy = false;
    private boolean detailVisibility = false;
    private boolean detailComment = false;
    private int detailProfileWidth = 600;
    private int detailProfileHeight = 400;
    
    private boolean pictureVisible = true;
    private int pictureImageMaxWidth = 800;
    private int pictureImageMaxHeight = 600;
    private int pictureThumbnailMaxWidth = 100;
    private int pictureThumbnailMaxHeight = 100;
    private boolean pictureName = true;
    private boolean pictureDescription = true;
    private int imageQuality = 80;
    
    private boolean watermarkEnabled = false;
    private String watermarkText = "";
    
    private String skinFile = "bluebubbles";
    
    private boolean fullExport = false;
    private boolean keepImages = true;
    private String exportDirectory = "public_html";
    private String footerLinkName = "";
    private String footerLink = "";
    
    private boolean scpEnabled;
    private String scpHost;
    private String scpDirectory;
    private String scpUser;
    
    public ExportSettings deepClone() {
        ExportSettings clone = new ExportSettings();
        clone.indexTitle = indexTitle;
        clone.indexImages = indexImages;
        clone.indexGroupByYear = indexGroupByYear;
        clone.indexDate = indexDate;
        clone.indexTime = indexTime;
        clone.indexLocation = indexLocation;
        clone.indexCity = indexCity;
        clone.indexCountry = indexCountry;
        clone.indexDepth = indexDepth;
        clone.indexDuration = indexDuration;
        clone.detailVisible = detailVisible;
        clone.detailDate = detailDate;
        clone.detailTime = detailTime;
        clone.detailLocation = detailLocation;
        clone.detailCity = detailCity;
        clone.detailCountry = detailCountry;
        clone.detailWaters = detailWaters;
        clone.detailDepth = detailDepth;
        clone.detailDuration = detailDuration;
        clone.detailEquipment = detailEquipment;
        clone.detailBuddy = detailBuddy;
        clone.detailComment = detailComment;
        clone.detailVisibility = detailVisibility;
        clone.detailProfile = detailProfile;
        clone.detailProfileWidth = detailProfileWidth;
        clone.detailProfileHeight = detailProfileHeight;
        clone.pictureVisible = pictureVisible;
        clone.pictureImageMaxWidth = pictureImageMaxWidth;
        clone.pictureImageMaxHeight = pictureImageMaxHeight;
        clone.pictureThumbnailMaxWidth = pictureThumbnailMaxWidth;
        clone.pictureThumbnailMaxHeight = pictureThumbnailMaxHeight;
        clone.imageQuality = imageQuality;
        clone.watermarkEnabled = watermarkEnabled;
        clone.watermarkText = watermarkText;
        clone.skinFile = skinFile;
        clone.fullExport = fullExport;
        clone.keepImages = keepImages;
        clone.exportDirectory = exportDirectory;
        clone.indexPictureCount = indexPictureCount;
        clone.footerLinkName = footerLinkName;
        clone.footerLink = footerLink;
        clone.scpEnabled = scpEnabled;
        clone.scpHost = scpHost;
        clone.scpDirectory = scpDirectory;
        clone.scpUser = scpUser;
        return clone;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<ExportSettings>");
        sb.append("<indexTitle>");
        sb.append(indexTitle);
        sb.append("</indexTitle>");
        sb.append("<footerLinkName>");
        sb.append(footerLinkName);
        sb.append("</footerLinkName>");
        sb.append("<footerLink>");
        sb.append(footerLink);
        sb.append("</footerLink>");
        sb.append("<indexImages>");
        sb.append(indexImages);
        sb.append("</indexImages>");
        sb.append("<indexGroupByYear>");
        sb.append(indexGroupByYear);
        sb.append("</indexGroupByYear>");
        sb.append("<indexDate>");
        sb.append(indexDate);
        sb.append("</indexDate>");
        sb.append("<indexTime>");
        sb.append(indexTime);
        sb.append("</indexTime>");
        sb.append("<indexLocation>");
        sb.append(indexLocation);
        sb.append("</indexLocation>");
        sb.append("<indexCity>");
        sb.append(indexCity);
        sb.append("</indexCity>");
        sb.append("<indexCountry>");
        sb.append(indexCountry);
        sb.append("</indexCountry>");
        sb.append("<indexDepth>");
        sb.append(indexDepth);
        sb.append("</indexDepth>");
        sb.append("<indexDuration>");
        sb.append(indexDuration);
        sb.append("</indexDuration>");
        sb.append("<indexPictureCount>");
        sb.append(indexPictureCount);
        sb.append("</indexPictureCount>");
        sb.append("<detailVisible>");
        sb.append(detailVisible);
        sb.append("</detailVisible>");
        sb.append("<detailDate>");
        sb.append(detailDate);
        sb.append("</detailDate>");
        sb.append("<detailTime>");
        sb.append(detailTime);
        sb.append("</detailTime>");
        sb.append("<detailLocation>");
        sb.append(detailLocation);
        sb.append("</detailLocation>");
        sb.append("<detailCountry>");
        sb.append(detailCountry);
        sb.append("</detailCountry>");
        sb.append("<detailWaters>");
        sb.append(detailWaters);
        sb.append("</detailWaters>");
        sb.append("<detailDepth>");
        sb.append(detailDepth);
        sb.append("</detailDepth>");
        sb.append("<detailDuration>");
        sb.append(detailDuration);
        sb.append("</detailDuration>");
        sb.append("<detailEquipment>");
        sb.append(detailEquipment);
        sb.append("</detailEquipment>");
        sb.append("<detailBuddy>");
        sb.append(detailBuddy);
        sb.append("</detailBuddy>");
        sb.append("<detailVisibility>");
        sb.append(detailVisibility);
        sb.append("</detailVisibility>");
        sb.append("<detailComment>");
        sb.append(detailComment);
        sb.append("</detailComment>");
        sb.append("<detailProfile>");
        sb.append(detailProfile);
        sb.append("</detailProfile>");
        sb.append("<detailProfileWidth>");
        sb.append(detailProfileWidth);
        sb.append("</detailProfileWidth>");
        sb.append("<detailProfileHeight>");
        sb.append(detailProfileHeight);
        sb.append("</detailProfileHeight>");
        sb.append("<pictureVisible>");
        sb.append(pictureVisible);
        sb.append("</pictureVisible>");
        sb.append("<pictureImageMaxWidth>");
        sb.append(pictureImageMaxWidth);
        sb.append("</pictureImageMaxWidth>");
        sb.append("<pictureImageMaxHeight>");
        sb.append(pictureImageMaxHeight);
        sb.append("</pictureImageMaxHeight>");
        sb.append("<pictureThumbnailMaxWidth>");
        sb.append(pictureThumbnailMaxWidth);
        sb.append("</pictureThumbnailMaxWidth>");
        sb.append("<pictureThumbnailMaxHeight>");
        sb.append(pictureThumbnailMaxHeight);
        sb.append("</pictureThumbnailMaxHeight>");
        sb.append("<imageQuality>");
        sb.append(imageQuality);
        sb.append("</imageQuality>");
        sb.append("<watermarkEnabled>");
        sb.append(watermarkEnabled);
        sb.append("</watermarkEnabled>");
        sb.append("<watermarkText>");
        sb.append(watermarkText);
        sb.append("</watermarkText>");
        sb.append("<skinFile>");
        sb.append(skinFile);
        sb.append("</skinFile>");
        sb.append("<fullExport>");
        sb.append(fullExport);
        sb.append("</fullExport>");
        sb.append("<keepImages>");
        sb.append(keepImages);
        sb.append("</keepImages>");
        sb.append("<exportDirectory>");
        sb.append(exportDirectory);
        sb.append("</exportDirectory>");
        sb.append("<pictureName>");
        sb.append(pictureName);
        sb.append("</pictureName>");
        sb.append("<pictureDescription>");
        sb.append(pictureDescription);
        sb.append("</pictureDescription>");
        sb.append("<scpEnabled>");
        sb.append(scpEnabled);
        sb.append("</scpEnabled>");
        sb.append("<scpHost>");
        sb.append(scpHost);
        sb.append("</scpHost>");
        sb.append("<scpDirectory>");
        sb.append(scpDirectory);
        sb.append("</scpDirectory>");
        sb.append("<scpUser>");
        sb.append(scpUser);
        sb.append("</scpUser>");
        sb.append("</ExportSettings>");
        return sb.toString();
    }
    
    //
    // setters for digester
    //
    
    public void setIndexImages(String type) {
        indexImages = Integer.parseInt(type);
    }
    public void setGroupByYear(String bool) {
        indexGroupByYear = Boolean.parseBoolean(bool);
    }
    public void setIndexDate(String bool) {
        indexDate = Boolean.parseBoolean(bool);
    }
    public void setIndexTime(String bool) {
        indexTime = Boolean.parseBoolean(bool);
    }
    public void setIndexLocation(String bool) {
        indexLocation = Boolean.parseBoolean(bool);
    }
    public void setIndexCity(String bool) {
        indexCity = Boolean.parseBoolean(bool);
    }
    public void setIndexCountry(String bool) {
        indexCountry = Boolean.parseBoolean(bool);
    }
    public void setIndexDepth(String bool) {
        indexDepth = Boolean.parseBoolean(bool);
    }
    public void setIndexDuration(String bool) {
        indexDuration = Boolean.parseBoolean(bool);
    }
    public void setIndexPictureCount(String bool) {
        indexPictureCount = Boolean.parseBoolean(bool);
    }
    public void setDetailVisible(String bool) {
        detailVisible = Boolean.parseBoolean(bool);
    }
    public void setDetailDate(String bool) {
        detailDate = Boolean.parseBoolean(bool);
    }
    public void setDetailTime(String bool) {
        detailTime = Boolean.parseBoolean(bool);
    }
    public void setDetailLocation(String bool) {
        detailLocation = Boolean.parseBoolean(bool);
    }
    public void setDetailCity(String bool) {
        detailCity = Boolean.parseBoolean(bool);
    }
    public void setDetailCountry(String bool) {
        detailCountry = Boolean.parseBoolean(bool);
    }
    public void setDetailWaters(String bool) {
        detailWaters = Boolean.parseBoolean(bool);
    }
    public void setDetailDepth(String bool) {
        detailDepth = Boolean.parseBoolean(bool);
    }
    public void setDetailDuration(String bool) {
        detailDuration = Boolean.parseBoolean(bool);
    }
    public void setDetailEquipment(String bool) {
        detailEquipment = Boolean.parseBoolean(bool);
    }
    public void setDetailProfile(String bool) {
        detailProfile = Boolean.parseBoolean(bool);
    }
    public void setDetailProfileWidth(String integer) {
        detailProfileWidth = Integer.parseInt(integer);
    }
    public void setDetailProfileHeight(String integer) {
        detailProfileHeight = Integer.parseInt(integer);
    }
    public void setPictureVisible(String bool) {
        pictureVisible = Boolean.parseBoolean(bool);
    }
    public void setPictureImageMaxWidth(String integer) {
        pictureImageMaxWidth = Integer.parseInt(integer);
    }
    public void setPictureImageMaxHeight(String integer) {
        pictureImageMaxHeight = Integer.parseInt(integer);
    }
    public void setPictureThumbnailMaxWidth(String integer) {
        pictureThumbnailMaxWidth = Integer.parseInt(integer);
    }
    public void setPictureThumbnailMaxHeight(String integer) {
        pictureThumbnailMaxHeight = Integer.parseInt(integer);
    }
    public void setDetailBuddy(String bool) {
        detailBuddy = Boolean.parseBoolean(bool);
    }
    public void setDetailVisibility(String bool) {
        detailVisibility = Boolean.parseBoolean(bool);
    }
    public void setDetailComment(String bool) {
        detailComment = Boolean.parseBoolean(bool);
    }
    public void setPictureName(String bool) {
        pictureName = Boolean.parseBoolean(bool);
    }
    public void setPictureDescription(String bool) {
        pictureDescription = Boolean.parseBoolean(bool);
    }
    
    //
    // normal getters and setters
    //

    public boolean showDetailCountry() {
        return detailCountry;
    }
    public void setDetailCountry(boolean detailCountry) {
        this.detailCountry = detailCountry;
    }
    public boolean showDetailDate() {
        return detailDate;
    }
    public void setDetailDate(boolean detailDate) {
        this.detailDate = detailDate;
    }
    public boolean showDetailDepth() {
        return detailDepth;
    }
    public void setDetailDepth(boolean detailDepth) {
        this.detailDepth = detailDepth;
    }
    public boolean showDetailDuration() {
        return detailDuration;
    }
    public void setDetailDuration(boolean detailDuration) {
        this.detailDuration = detailDuration;
    }
    public boolean showDetailEquipment() {
        return detailEquipment;
    }
    public void setDetailEquipment(boolean detailEquipment) {
        this.detailEquipment = detailEquipment;
    }
    public boolean showDetailLocation() {
        return detailLocation;
    }
    public void setDetailLocation(boolean detailLocation) {
        this.detailLocation = detailLocation;
    }
    public boolean showDetailProfile() {
        return detailProfile;
    }
    public void setDetailProfile(boolean detailProfile) {
        this.detailProfile = detailProfile;
    }
    public int getDetailProfileHeight() {
        return detailProfileHeight;
    }
    public void setDetailProfileHeight(int detailProfileHeight) {
        this.detailProfileHeight = detailProfileHeight;
    }
    public int getDetailProfileWidth() {
        return detailProfileWidth;
    }
    public void setDetailProfileWidth(int detailProfileWidth) {
        this.detailProfileWidth = detailProfileWidth;
    }
    public boolean showDetailTime() {
        return detailTime;
    }
    public void setDetailTime(boolean detailTime) {
        this.detailTime = detailTime;
    }
    public boolean isDetailVisible() {
        return detailVisible;
    }
    public void setDetailVisible(boolean detailVisible) {
        this.detailVisible = detailVisible;
    }
    public boolean showIndexCountry() {
        return indexCountry;
    }
    public void setIndexCountry(boolean indexCountry) {
        this.indexCountry = indexCountry;
    }
    public boolean showIndexDate() {
        return indexDate;
    }
    public void setIndexDate(boolean indexDate) {
        this.indexDate = indexDate;
    }
    public boolean showIndexDepth() {
        return indexDepth;
    }
    public void setIndexDepth(boolean indexDepth) {
        this.indexDepth = indexDepth;
    }
    public boolean showIndexDuration() {
        return indexDuration;
    }
    public void setIndexDuration(boolean indexDuration) {
        this.indexDuration = indexDuration;
    }
    public int getIndexImages() {
        return indexImages;
    }
    public void setIndexImages(int indexImages) {
        this.indexImages = indexImages;
    }
    public boolean showIndexLocation() {
        return indexLocation;
    }
    public void setIndexLocation(boolean indexLocation) {
        this.indexLocation = indexLocation;
    }
    public boolean showIndexTime() {
        return indexTime;
    }
    public void setIndexTime(boolean indexTime) {
        this.indexTime = indexTime;
    }
    public String getIndexTitle() {
        return indexTitle;
    }
    public void setIndexTitle(String indexTitle) {
        this.indexTitle = indexTitle;
    }
    public int getPictureImageMaxHeight() {
        return pictureImageMaxHeight;
    }
    public void setPictureImageMaxHeight(int pictureImageMaxHeight) {
        this.pictureImageMaxHeight = pictureImageMaxHeight;
    }
    public int getPictureImageMaxWidth() {
        return pictureImageMaxWidth;
    }
    public void setPictureImageMaxWidth(int pictureImageMaxWidth) {
        this.pictureImageMaxWidth = pictureImageMaxWidth;
    }
    public int getPictureThumbnailMaxHeight() {
        return pictureThumbnailMaxHeight;
    }
    public void setPictureThumbnailMaxHeight(int pictureThumbnailMaxHeight) {
        this.pictureThumbnailMaxHeight = pictureThumbnailMaxHeight;
    }
    public int getPictureThumbnailMaxWidth() {
        return pictureThumbnailMaxWidth;
    }
    public void setPictureThumbnailMaxWidth(int pictureThumbnailMaxWidth) {
        this.pictureThumbnailMaxWidth = pictureThumbnailMaxWidth;
    }
    public boolean isPictureVisible() {
        return pictureVisible;
    }
    public void setPictureVisible(boolean pictureVisible) {
        this.pictureVisible = pictureVisible;
    }
    public boolean isFullExport() {
        return fullExport;
    }
    public void setFullExport(boolean b) {
        fullExport = b;
    }
    public boolean isKeepImages() {
        return keepImages;
    }
    public void setKeepImages(boolean b) {
        keepImages = b;
    }
    public String getExportDirectory() {
        return exportDirectory;
    }
    public void setExportDirectory(String exportDirectory) {
        this.exportDirectory = exportDirectory;
    }
    public boolean showDetailBuddy() {
        return detailBuddy;
    }
    public void setDetailBuddy(boolean detailBuddy) {
        this.detailBuddy = detailBuddy;
    }
    public boolean showDetailComment() {
        return detailComment;
    }
    public void setDetailComment(boolean detailComment) {
        this.detailComment = detailComment;
    }
    public boolean showDetailVisibility() {
        return detailVisibility;
    }
    public void setDetailVisibility(boolean detailVisibility) {
        this.detailVisibility = detailVisibility;
    }
    public boolean showIndexPictureCount() {
        return indexPictureCount;
    }
    public void setIndexPictureCount(boolean indexPictureCount) {
        this.indexPictureCount = indexPictureCount;
    }
    public void setPictureName(boolean showPictureName) {
        pictureName = showPictureName;
    }
    public boolean showPictureName() {
        return pictureName;
    }
    public void setPictureDescription(boolean showPictureDescription) {
        pictureDescription = showPictureDescription;
    }
    public boolean showPictureDescription() {
        return pictureDescription;
    }

    public String getFooterLink() {
        return footerLink;
    }

    public void setFooterLink(String footerLink) {
        this.footerLink = footerLink;
    }

    public String getFooterLinkName() {
        return footerLinkName;
    }

    public void setFooterLinkName(String footerLinkName) {
        this.footerLinkName = footerLinkName;
    }

    public boolean groupByYear() {
        return indexGroupByYear;
    }

    public void setGroupByYear(boolean indexGroupByYear) {
        this.indexGroupByYear = indexGroupByYear;
    }

    public boolean showDetailCity() {
        return detailCity;
    }

    public void setDetailCity(boolean detailCity) {
        this.detailCity = detailCity;
    }

    public boolean showDetailWaters() {
        return detailWaters;
    }

    public void setDetailWaters(boolean detailWaters) {
        this.detailWaters = detailWaters;
    }

    public boolean showIndexCity() {
        return indexCity;
    }

    public void setIndexCity(boolean indexCity) {
        this.indexCity = indexCity;
    }
    
    public boolean isScpEnabled() {
        return scpEnabled;
    }
    
    public void setScpEnabled(boolean b) {
        scpEnabled = b;
    }
    
    public String getScpHost() {
        return scpHost;
    }
    
    public void setScpHost(String s) {
        scpHost = s;
    }
    
    public String getScpDirectory() {
        return scpDirectory;
    }
    
    public void setScpDirectory(String s) {
        scpDirectory = s;
    }
    
    public String getScpUser() {
        return scpUser;
    }
    
    public void setScpUser(String s) {
        scpUser = s;
    }
    
    @Deprecated
    public void setScpPassword(String s) {
        // do nothing... obsolete
    }

    public String getSkinFile() {
        return skinFile;
    }

    public void setSkinFile(String skinFile) {
        this.skinFile = skinFile;
    }

    public boolean isWatermarkEnabled() {
        return watermarkEnabled;
    }

    public void setWatermarkEnabled(boolean watermarkEnabled) {
        this.watermarkEnabled = watermarkEnabled;
    }

    public String getWatermarkText() {
        return watermarkText;
    }

    public void setWatermarkText(String watermarkText) {
        this.watermarkText = watermarkText;
    }
    public int getImageQuality() {
        return imageQuality;
    }
    public void setImageQuality(int imageQuality) {
        this.imageQuality = imageQuality;
    }
    public void setImageQuality(String imageQuality) {
        this.imageQuality = Integer.parseInt(imageQuality);
    }

}
