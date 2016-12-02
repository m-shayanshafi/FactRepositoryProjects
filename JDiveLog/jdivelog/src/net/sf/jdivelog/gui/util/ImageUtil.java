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
package net.sf.jdivelog.gui.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.RenderingHints;
import java.awt.*;

import org.jfree.util.WaitingImageObserver;

public class ImageUtil {

    /*
     * Grabbed from:
     * http://today.java.net/pub/a/today/2007/04/03/perils-of-image
     * -getscaledinstance.html
     */
    public static BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight, Object hint, boolean higherQuality) {
        int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = (BufferedImage) img;
        int w, h;
        if (higherQuality) {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }

        do {
            if (higherQuality && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (higherQuality && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }

    public static void transform(Image sourceImg, BufferedImage targetImg, int targetWidth, int targetHeight, int rotation, ImageObserver observer) {
        WaitingImageObserver o = new WaitingImageObserver(sourceImg);
        int sizeX = sourceImg.getWidth(o);
        int sizeY = sourceImg.getHeight(o);

        double factor = getFactor(sizeX, sizeY, targetWidth, targetHeight, rotation);
        double scaledX = sizeX * factor;
        double scaledY = sizeY * factor;

        AffineTransform at = new AffineTransform();

        BufferedImage temp = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d_temp = temp.createGraphics();
        g2d_temp.drawImage(sourceImg, at, new WaitingImageObserver(sourceImg));

        BufferedImage scaledImage = new BufferedImage((int) scaledX, (int) scaledY, BufferedImage.TYPE_INT_RGB);
        scaledImage = getScaledInstance(temp, targetWidth, targetHeight, RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);

        Graphics2D g2d = targetImg.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, targetWidth, targetHeight);
        at = new AffineTransform();
        at.translate((targetWidth - scaledX) / 2, (targetHeight - scaledY) / 2);
        at.rotate(Math.toRadians(rotation * 90), scaledX / 2, scaledY / 2);
        g2d.drawImage(scaledImage, at, observer);
        g2d.dispose();
    }

    public static BufferedImage transform(Image sourceImg, int maxWidth, int maxHeight, int rotation) {
        WaitingImageObserver o = new WaitingImageObserver(sourceImg);
        o.waitImageLoaded();
        int sizeX = sourceImg.getWidth(o);
        int sizeY = sourceImg.getHeight(o);

        double factor = getFactor(sizeX, sizeY, maxWidth, maxHeight, rotation);
        int scaledX;
        int scaledY;
        if (rotation == 0 || rotation == 2) {
            scaledX = (int) (sizeX * factor);
            scaledY = (int) (sizeY * factor);
        } else {
            scaledX = (int) (sizeY * factor);
            scaledY = (int) (sizeX * factor);
        }
        BufferedImage buf = new BufferedImage(scaledX, scaledY, BufferedImage.TYPE_INT_RGB);
        WaitingImageObserver obs = new WaitingImageObserver(buf);
        transform(sourceImg, buf, scaledX, scaledY, rotation, obs);
        obs.waitImageLoaded();
        return buf;
    }

    private static double getFactor(int sourceWidth, int sourceHeight, int maxWidth, int maxHeight, int rotation) {
        float factorX;
        float factorY;
        if (rotation == 0 || rotation == 2) {
            factorX = getScaleFactor(maxWidth, sourceWidth);
            factorY = getScaleFactor(maxHeight, sourceHeight);
        } else {
            factorX = getScaleFactor(maxWidth, sourceHeight);
            factorY = getScaleFactor(maxHeight, sourceWidth);
        }
        if (factorX < factorY) {
            return factorX;
        }
        return factorY;
    }

    private static float getScaleFactor(double preferredSize, int size) {
        return (float) preferredSize / size;
    }
}
