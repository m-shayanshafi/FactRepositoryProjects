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
package pl.org.minions.utils.ui.synth;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import javax.swing.JProgressBar;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;

/**
 * {@link SynthPainter} implementation that paints a shiny
 * bullet-like rounded rectangle.
 * <p>
 * Currently supports only button painting.
 */
public class BulletPainter extends SynthPainterPlus
{
    private static final float[] GRADIENT_FRACTIONS =
    { 0.0f, 0.1f, 0.5f, 1.0f };

    private static final Point GRADIENT_START_POINT = new Point(0, 0);

    private Insets insets = new Insets(0, 0, 0, 0);

    /**
     * Returns insets of currently painted component.
     * @return insets component insets
     */
    protected Insets getInsets()
    {
        return insets;
    }

    /**
     * Override to change the shape of painted background.
     * <p>
     * The provided {@link Graphics2D} object already has
     * appropriate clip and paint set.
     * @param context
     *            synth context
     * @param g
     *            graphics object
     * @param x
     *            horizontal origin of the area to fill
     * @param y
     *            vertical origin of the area to fill
     * @param w
     *            width of the area to fill
     * @param h
     *            height of the area to fill
     * @param orientation
     *            bullet shape orientation
     */
    protected void fillBulletShape(SynthContext context,
                                   Graphics2D g,
                                   int x,
                                   int y,
                                   int w,
                                   int h,
                                   Orientation orientation)
    {
        if (context.getComponent().isOpaque())
        {
            g.fillRect(x, y, w - 1, h - 1);
            return;
        }

        int arcWidth = 0;
        int arcHeight = 0;
        final boolean asymmetric = !areSymmetric(insets);

        switch (orientation)
        {
            case HORIZONTAL:
                arcWidth = 2 * insets.left;
                arcHeight = h - 1;
                break;
            case VERTICAL:
                arcWidth = w - 1;
                arcHeight = 2 * insets.top;
                break;
            default:
                arcWidth = 0;
                arcHeight = 0;
                break;
        }

        final Shape oldClip = g.getClip();

        if (!asymmetric)
        {
            g.fillRoundRect(x, y, w - 1, h - 1, arcWidth, arcHeight);
        }
        else
        {
            int margin;
            switch (orientation)
            {
                case HORIZONTAL:
                    // arcWidth == 2*insets.left
                    margin = w / 2;
                    g.clipRect(x, y, margin, h);
                    g.fillRoundRect(x, y, w - 1, h - 1, arcWidth, arcHeight);

                    g.setClip(oldClip);
                    g.clipRect(x + margin, y, w, h);
                    g.fillRoundRect(x,
                                    y,
                                    w - 1,
                                    h - 1,
                                    2 * insets.right,
                                    arcHeight);
                    break;
                case VERTICAL:
                    // arcHeight == 2*insets.top
                    margin = h / 2;
                    g.clipRect(x, y, w, margin);
                    g.fillRoundRect(x, y, w - 1, h - 1, arcWidth, arcHeight);

                    g.setClip(oldClip);
                    g.clipRect(x, y + margin, w, h);
                    g.fillRoundRect(x,
                                    y,
                                    w - 1,
                                    h - 1,
                                    arcWidth,
                                    2 * insets.bottom);
                    break;
                default:
                    break;
            }
        }

        g.setClip(oldClip);
    }

    /**
     * Override to change the shape of painted border.
     * <p>
     * The provided {@link Graphics2D} object already has
     * appropriate clip and pen set.
     * @param context
     *            synth context
     * @param g
     *            graphics object
     * @param x
     *            horizontal origin of the shape to draw
     * @param y
     *            vertical origin of the shape to draw
     * @param w
     *            width of the shape to draw
     * @param h
     *            height of the shape to draw
     * @param orientation
     *            bullet shape orientation
     */
    protected void drawBulletShape(SynthContext context,
                                   Graphics2D g,
                                   int x,
                                   int y,
                                   int w,
                                   int h,
                                   Orientation orientation)
    {
        if (context.getComponent().isOpaque())
        {
            g.drawRect(x, y, w, h);
            return;
        }

        int arcWidth = 0;
        int arcHeight = 0;
        final boolean asymmetric = !areSymmetric(insets);

        switch (orientation)
        {
            case HORIZONTAL:
                arcWidth = 2 * insets.left;
                arcHeight = h - 1;
                break;
            case VERTICAL:
                arcWidth = w - 1;
                arcHeight = 2 * insets.top;
                break;
            default:
                arcWidth = 0;
                arcHeight = 0;
                break;
        }

        if (!asymmetric)
            g.drawRoundRect(x, y, w - 1, h - 1, arcWidth, arcHeight);
        else
        {
            int margin;
            switch (orientation)
            {
                case HORIZONTAL:
                    // arcWidth == 2*insets.left
                    margin = w / 2;
                    g.setClip(x, y, margin, h);
                    g.drawRoundRect(x, y, w - 1, h - 1, arcWidth, arcHeight);
                    g.setClip(x + margin, y, w, h);
                    g.drawRoundRect(x,
                                    y,
                                    w - 1,
                                    h - 1,
                                    2 * insets.right,
                                    arcHeight);
                    break;
                case VERTICAL:
                    // arcHeight = 2*insets.top
                    margin = h / 2;
                    g.setClip(x, y, w, margin);
                    g.drawRoundRect(x, y, w - 1, h - 1, arcWidth, arcHeight);
                    g.setClip(x, y + margin, w, h);
                    g.drawRoundRect(x,
                                    y,
                                    w - 1,
                                    h - 1,
                                    arcWidth,
                                    2 * insets.bottom);
                    break;
                default:
                    break;
            }
        }
    }

    private void paintBulletBackground(SynthContext context,
                                       Graphics g,
                                       int x,
                                       int y,
                                       int w,
                                       int h,
                                       Orientation orientation)
    {
        paintBulletBackground(context,
                              g,
                              x,
                              y,
                              w,
                              h,
                              orientation,
                              ColorType.BACKGROUND,
                              ColorType.FOREGROUND,
                              ColorType.TEXT_BACKGROUND,
                              null);
    }

    private void paintBulletBackground(SynthContext context,
                                       Graphics g,
                                       int x,
                                       int y,
                                       int w,
                                       int h,
                                       Orientation orientation,
                                       ColorType grad1,
                                       ColorType grad2,
                                       ColorType grad3,
                                       Rectangle2D uberClip)
    {
        final Color backgroundColor =
                context.getStyle().getColor(context, grad1);
        final Color foregroundColor =
                context.getStyle().getColor(context, grad2);
        final Color textBacgroundColor =
                context.getStyle().getColor(context, grad3);

        assert backgroundColor != null;
        assert foregroundColor != null;
        assert textBacgroundColor != null;

        insets = context.getStyle().getInsets(context, insets);
        assert insets != null;

        Point gradientEndPoint = new Point();

        switch (orientation)
        {
            case HORIZONTAL:
                gradientEndPoint.x = 0;
                gradientEndPoint.y = h;
                break;
            case VERTICAL:
                gradientEndPoint.x = w;
                gradientEndPoint.y = 0;
                break;
            default:
                break;
        }

        final Graphics2D g2D = (Graphics2D) g;
        final Color[] gradientColors =
                { backgroundColor, foregroundColor, textBacgroundColor,
                 backgroundColor, };
        final LinearGradientPaint gradientPaint =
                new LinearGradientPaint(GRADIENT_START_POINT,
                                        gradientEndPoint,
                                        GRADIENT_FRACTIONS,
                                        gradientColors);
        g2D.setPaint(gradientPaint);
        if ((backgroundColor.getAlpha() < ALPHA_OPAQUE
            || foregroundColor.getAlpha() < ALPHA_OPAQUE || textBacgroundColor.getAlpha() < ALPHA_OPAQUE)
            && !context.getComponent().isOpaque())
        {
            g2D.setComposite(AlphaComposite.SrcOver);
        }

        final Shape oldClip = g.getClip();

        g.setClip(uberClip);
        fillBulletShape(context, g2D, x, y, w, h, orientation);

        g.setClip(oldClip);
    }

    private void drawBulletBorder(SynthContext context,
                                  Graphics g,
                                  int x,
                                  int y,
                                  int w,
                                  int h,
                                  Orientation orientation)
    {
        final Color color =
                context.getStyle().getColor(context, ColorType.BACKGROUND);
        insets = context.getStyle().getInsets(context, insets);
        assert insets != null;

        final Graphics2D g2D = (Graphics2D) g;
        Object antialiasing = null;
        antialiasing = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        if (color.getAlpha() < ALPHA_OPAQUE
            && !context.getComponent().isOpaque())
        {
            g2D.setComposite(AlphaComposite.SrcOver);
        }
        g.setColor(color);

        drawBulletShape(context, g2D, x, y, w, h, orientation);

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialiasing);
        g.setClip(null);
    }

    /** {@inheritDoc} */
    @Override
    public void paintButtonBackground(SynthContext context,
                                      Graphics g,
                                      int x,
                                      int y,
                                      int w,
                                      int h)
    {
        paintBulletBackground(context, g, x, y, w, h, Orientation.HORIZONTAL);
    }

    /** {@inheritDoc} */
    @Override
    public void paintButtonBorder(SynthContext context,
                                  Graphics g,
                                  int x,
                                  int y,
                                  int w,
                                  int h)
    {
        drawBulletBorder(context, g, x, y, w, h, Orientation.HORIZONTAL);
    }

    /** {@inheritDoc} */
    @Override
    public void paintToggleButtonBackground(SynthContext context,
                                            Graphics g,
                                            int x,
                                            int y,
                                            int w,
                                            int h)
    {
        paintBulletBackground(context, g, x, y, w, h, Orientation.HORIZONTAL);
    }

    /** {@inheritDoc} */
    @Override
    public void paintToggleButtonBorder(SynthContext context,
                                        Graphics g,
                                        int x,
                                        int y,
                                        int w,
                                        int h)
    {
        drawBulletBorder(context, g, x, y, w, h, Orientation.HORIZONTAL);
    }

    /** {@inheritDoc} */
    @Override
    public void paintProgressBarBackground(SynthContext context,
                                           Graphics g,
                                           int x,
                                           int y,
                                           int w,
                                           int h,
                                           int orientation)
    {
        paintBulletBackground(context,
                              g,
                              x,
                              y,
                              w,
                              h,
                              getProgressBarOrientation(orientation),
                              ColorType.FOCUS,
                              ColorType.FOREGROUND,
                              ColorType.FOCUS,
                              new Rectangle(x, y, w, h));
    }

    /** {@inheritDoc} */
    @Override
    public void paintProgressBarBorder(SynthContext context,
                                       Graphics g,
                                       int x,
                                       int y,
                                       int w,
                                       int h,
                                       int orientation)
    {
        drawBulletBorder(context,
                         g,
                         x,
                         y,
                         w,
                         h,
                         getProgressBarOrientation(orientation));
    }

    /** {@inheritDoc} */
    @Override
    public void paintProgressBarForeground(SynthContext context,
                                           Graphics g,
                                           int x,
                                           int y,
                                           int w,
                                           int h,
                                           int orientation)
    {
        final JProgressBar component = (JProgressBar) context.getComponent();
        final Orientation progressBarOrientation =
                getProgressBarOrientation(orientation);
        Rectangle uberClip = new Rectangle(component.getSize());
        switch (progressBarOrientation)
        {
            case HORIZONTAL:
                uberClip.width =
                        (int) (uberClip.width * component.getPercentComplete());
                break;
            case VERTICAL:
                uberClip.height =
                        (int) (uberClip.height * component.getPercentComplete());
                break;
            default:
                break;
        }
        paintBulletBackground(context,
                              g,
                              0,
                              0,
                              component.getWidth(),
                              component.getHeight(),
                              progressBarOrientation,
                              ColorType.BACKGROUND,
                              ColorType.FOREGROUND,
                              ColorType.TEXT_BACKGROUND,
                              uberClip);
    }

    /** {@inheritDoc} */
    @Override
    public void paintProgressBarBorder(SynthContext context,
                                       Graphics g,
                                       int x,
                                       int y,
                                       int w,
                                       int h)
    {
        paintProgressBarBorder(context, g, x, y, w, h, JProgressBar.HORIZONTAL);
    }

    /** {@inheritDoc} */
    @Override
    public void paintArrowButtonBackground(SynthContext context,
                                           Graphics g,
                                           int x,
                                           int y,
                                           int w,
                                           int h)
    {
        paintBulletBackground(context, g, x, y, w, h, Orientation.HORIZONTAL);
    }

    /** {@inheritDoc} */
    @Override
    public void paintArrowButtonBorder(SynthContext context,
                                       Graphics g,
                                       int x,
                                       int y,
                                       int w,
                                       int h)
    {
        drawBulletBorder(context, g, x, y, w, h, Orientation.HORIZONTAL);
    }

    /** {@inheritDoc} */
    @Override
    public void paintTableHeaderBackground(SynthContext context,
                                           Graphics g,
                                           int x,
                                           int y,
                                           int w,
                                           int h)
    {
        paintBulletBackground(context, g, x, y, w, h, Orientation.HORIZONTAL);
    }

    /** {@inheritDoc} */
    @Override
    public void paintTableHeaderBorder(SynthContext context,
                                       Graphics g,
                                       int x,
                                       int y,
                                       int w,
                                       int h)
    {
        drawBulletBorder(context, g, x, y, w, h, Orientation.HORIZONTAL);
    }

    /** {@inheritDoc} */
    @Override
    public void paintScrollBarThumbBackground(SynthContext context,
                                              Graphics g,
                                              int x,
                                              int y,
                                              int w,
                                              int h,
                                              int orientation)
    {
        paintBulletBackground(context, g, x, y, w, h, Orientation.HORIZONTAL);
    }

    /** {@inheritDoc} */
    @Override
    public void paintScrollBarThumbBorder(SynthContext context,
                                          Graphics g,
                                          int x,
                                          int y,
                                          int w,
                                          int h,
                                          int orientation)
    {
        drawBulletBorder(context, g, x, y, w, h, Orientation.HORIZONTAL);
    }

    /** {@inheritDoc} */
    @Override
    public void paintMenuBackground(SynthContext context,
                                    Graphics g,
                                    int x,
                                    int y,
                                    int w,
                                    int h)
    {
        paintBulletBackground(context, g, x, y, w, h, Orientation.HORIZONTAL);
    }

    /** {@inheritDoc} */
    @Override
    public void paintMenuBorder(SynthContext context,
                                Graphics g,
                                int x,
                                int y,
                                int w,
                                int h)
    {
        drawBulletBorder(context, g, x, y, w, h, Orientation.HORIZONTAL);
    }
}
