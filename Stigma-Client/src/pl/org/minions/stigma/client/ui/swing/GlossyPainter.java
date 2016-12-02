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
package pl.org.minions.stigma.client.ui.swing;

import static javax.swing.plaf.synth.ColorType.BACKGROUND;
import static javax.swing.plaf.synth.ColorType.FOCUS;
import static javax.swing.plaf.synth.ColorType.FOREGROUND;
import static javax.swing.plaf.synth.ColorType.TEXT_BACKGROUND;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;

import pl.org.minions.stigma.databases.Resourcer;
import pl.org.minions.utils.ui.synth.SynthPainterPlus;

/**
 * Paints glossy, rounded components.
 * <p>
 * Uses {@link ColorType#BACKGROUND} and
 * {@link ColorType#FOREGROUND} colors for most components.
 * Progress bar background is painted with
 * {@link ColorType#BACKGROUND} and
 * {@link ColorType#TEXT_BACKGROUND} instead, while
 * foreground is painted using {@link ColorType#FOREGROUND}
 * and {@link ColorType#FOCUS}.
 * @see Properties
 */
public class GlossyPainter extends SynthPainterPlus
{

    private static final Point GRADIENT_START_POINT = new Point(0, 0);

    /**
     * Properties configurable in synth configuration file.
     */
    public class Properties
    {
        /**
         * Property that provides path to image representing
         * a
         * north facing arrow icon.
         */
        public static final String ARROW_PATH = "GlossyPainter.arrowPath";

        public static final String GLOSS_COLOR = "GlossyPainter.glossColor";

        /**
         * Throws an {@link AssertionError}. Do not
         * instantiate.
         */
        protected Properties()
        {
            throw new AssertionError();
        }
    }

    //thread safe, maybe ;)
    private Map<String, BufferedImage> cachedImages =
            new HashMap<String, BufferedImage>();

    private Insets insets = new Insets(0, 0, 0, 0);
    private final Dimension arcSize = new Dimension();

    /**
     * Returns insets of currently painted component.
     * @return insets component insets
     */
    protected Insets getInsets()
    {
        return insets;
    }

    private Dimension getArcSize(int w,
                                 int h,
                                 Orientation orientation,
                                 Dimension arcSize)
    {
        switch (orientation)
        {
            case HORIZONTAL:
                arcSize.width = 2 * insets.left;
                arcSize.height = h - 1;
                break;
            case VERTICAL:
                arcSize.width = w - 1;
                arcSize.height = 2 * insets.top;
                break;
            default:
                arcSize.width = 0;
                arcSize.height = 0;
                break;
        }
        return arcSize;
    }

    /**
     * Override to change the shape of painted background.
     * <p>
     * The provided {@link Graphics2D} object already has
     * appropriate clip and paint set.
     * @param g2D
     *            graphics object
     * @param x
     *            horizontal origin of the area to fill
     * @param y
     *            vertical origin of the area to fill
     * @param w
     *            width of the area to fill
     * @param h
     *            height of the area to fill
     * @param opaque
     *            should the entire area be filled
     * @param gloss
     *            paint to the gloss area only
     * @param orientation
     *            bullet shape orientation
     */
    protected void fillShape(Graphics2D g2D,
                             int x,
                             int y,
                             int w,
                             int h,
                             boolean opaque,
                             boolean gloss,
                             Orientation orientation)
    {
        getArcSize(w, h, orientation, arcSize);

        final Rectangle glossClip = new Rectangle(0, 0, w, h / 2);

        final Shape oldClip = g2D.getClip();

        if (opaque)
        {
            if (gloss)
                g2D.clipRect(glossClip.x,
                             glossClip.y,
                             glossClip.width,
                             glossClip.height);

            g2D.fillRect(x, y, w - 1, h - 1);
        }
        else if (areSymmetric(insets))
        {
            if (gloss)
                g2D.clipRect(glossClip.x,
                             glossClip.y,
                             glossClip.width,
                             glossClip.height);
            g2D.fillRoundRect(x, y, w - 1, h - 1, arcSize.width, arcSize.height);
        }
        else
        //Asymmetric
        {
            int margin;
            switch (orientation)
            {
                case HORIZONTAL:
                    margin = w / 2;

                    g2D.clipRect(x, y, margin, h);
                    if (gloss)
                        g2D.clipRect(glossClip.x,
                                     glossClip.y,
                                     glossClip.width,
                                     glossClip.height);

                    g2D.fillRoundRect(x,
                                      y,
                                      w - 1,
                                      h - 1,
                                      arcSize.width,
                                      arcSize.height);

                    g2D.setClip(oldClip);
                    g2D.clipRect(x + margin, y, w, h);
                    if (gloss)
                        g2D.clipRect(glossClip.x,
                                     glossClip.y,
                                     glossClip.width,
                                     glossClip.height);

                    g2D.fillRoundRect(x,
                                      y,
                                      w - 1,
                                      h - 1,
                                      2 * insets.right,
                                      arcSize.height);
                    break;
                case VERTICAL:
                    margin = h / 2;
                    g2D.clipRect(x, y, w, margin);
                    if (gloss)
                        g2D.clipRect(glossClip.x,
                                     glossClip.y,
                                     glossClip.width,
                                     glossClip.height);

                    g2D.fillRoundRect(x,
                                      y,
                                      w - 1,
                                      h - 1,
                                      arcSize.width,
                                      arcSize.height);

                    g2D.setClip(oldClip);
                    g2D.clipRect(x, y + margin, w, h);
                    if (gloss)
                        g2D.clipRect(glossClip.x,
                                     glossClip.y,
                                     glossClip.width,
                                     glossClip.height);

                    g2D.fillRoundRect(x,
                                      y,
                                      w - 1,
                                      h - 1,
                                      arcSize.width,
                                      2 * insets.bottom);
                    break;
                default:
                    break;
            }
        }

        g2D.setClip(oldClip);
    }

    /**
     * Override to change the shape of painted border.
     * <p>
     * The provided {@link Graphics2D} object already has
     * appropriate clip and pen set.
     * @param g2D
     *            graphics object
     * @param x
     *            horizontal origin of the shape to draw
     * @param y
     *            vertical origin of the shape to draw
     * @param w
     *            width of the shape to draw
     * @param h
     *            height of the shape to draw
     * @param opaque
     *            should the entire area be filled
     * @param orientation
     *            bullet shape orientation
     */
    protected void drawShape(Graphics2D g2D,
                             int x,
                             int y,
                             int w,
                             int h,
                             boolean opaque,
                             Orientation orientation)
    {
        if (opaque)
        {
            g2D.drawRect(x, y, w, h);
            return;
        }

        getArcSize(w, h, orientation, arcSize);

        if (areSymmetric(insets))
            g2D.drawRoundRect(x, y, w - 1, h - 1, arcSize.width, arcSize.height);
        else
        {
            int margin;
            switch (orientation)
            {
                case HORIZONTAL:
                    // arcWidth == 2*insets.left
                    margin = w / 2;
                    g2D.setClip(x, y, margin, h);
                    g2D.drawRoundRect(x,
                                      y,
                                      w - 1,
                                      h - 1,
                                      arcSize.width,
                                      arcSize.height);
                    g2D.setClip(x + margin, y, w, h);
                    g2D.drawRoundRect(x,
                                      y,
                                      w - 1,
                                      h - 1,
                                      2 * insets.right,
                                      arcSize.height);
                    break;
                case VERTICAL:
                    // arcHeight = 2*insets.top
                    margin = h / 2;
                    g2D.setClip(x, y, w, margin);
                    g2D.drawRoundRect(x,
                                      y,
                                      w - 1,
                                      h - 1,
                                      arcSize.width,
                                      arcSize.height);
                    g2D.setClip(x, y + margin, w, h);
                    g2D.drawRoundRect(x,
                                      y,
                                      w - 1,
                                      h - 1,
                                      arcSize.width,
                                      2 * insets.bottom);
                    break;
                default:
                    break;
            }
        }
    }

    private void paintBackground(SynthContext context,
                                 Graphics g,
                                 int x,
                                 int y,
                                 int w,
                                 int h,
                                 Orientation orientation,
                                 ColorType innerColorType,
                                 ColorType outerColorType,
                                 Rectangle2D additionalClip)
    {
        final Graphics2D g2D = (Graphics2D) g.create();
        final boolean opaque = context.getComponent().isOpaque();

        final Color innerColor =
                context.getStyle().getColor(context, innerColorType);
        assert innerColor != null;

        final Color outerColor =
                context.getStyle().getColor(context, outerColorType);
        assert outerColor != null;

        final Color glossColor =
                (Color) context.getStyle().get(context, Properties.GLOSS_COLOR);

        context.getStyle().getInsets(context, insets);
        assert insets != null;

        final Point gradientStartPoint = new Point(0, h / 2);
        final Point gradientEndPoint = new Point(0, h);

        final Paint gradientPaint =
                new GradientPaint(gradientStartPoint,
                                  innerColor,
                                  gradientEndPoint,
                                  outerColor);

        gradientStartPoint.y = 0;
        gradientEndPoint.y = h / 2;

        final Paint glossPaint =
                new GradientPaint(GRADIENT_START_POINT,
                                  outerColor,
                                  gradientEndPoint,
                                  glossColor != null ? glossColor : innerColor);

        g2D.setClip(additionalClip);

        if (!areOpaque(innerColor, outerColor, glossColor) && !opaque)
            g2D.setComposite(AlphaComposite.SrcOver);

        g2D.setPaint(gradientPaint);
        fillShape(g2D, x, y, w, h, opaque, false, orientation);

        g2D.setPaint(glossPaint);
        fillShape(g2D, x, y, w, h, opaque, true, orientation);

        g2D.dispose();
    }

    private void paintBorder(SynthContext context,
                             Graphics g,
                             int x,
                             int y,
                             int w,
                             int h,
                             Orientation orientation,
                             ColorType colorType)
    {
        final Graphics2D g2D = (Graphics2D) g.create();

        final boolean opaque = context.getComponent().isOpaque();

        final Color color = context.getStyle().getColor(context, colorType);
        context.getStyle().getInsets(context, insets);
        assert insets != null;

        if (!opaque)
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                 RenderingHints.VALUE_ANTIALIAS_ON);

        if (!opaque && !isOpaque(color))
            g2D.setComposite(AlphaComposite.SrcOver);

        g2D.setColor(color);
        drawShape(g2D, x, y, w, h, opaque, orientation);

        g2D.dispose();
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
        paintBackground(context,
                        g,
                        x,
                        y,
                        w,
                        h,
                        Orientation.HORIZONTAL,
                        BACKGROUND,
                        FOREGROUND,
                        null);
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
        paintBorder(context, g, x, y, w, h, Orientation.HORIZONTAL, BACKGROUND);
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
        paintBackground(context,
                        g,
                        x,
                        y,
                        w,
                        h,
                        Orientation.HORIZONTAL,
                        BACKGROUND,
                        FOREGROUND,
                        null);
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
        paintBorder(context, g, x, y, w, h, Orientation.HORIZONTAL, BACKGROUND);
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
        paintBorder(context,
                    g,
                    x,
                    y,
                    w,
                    h,
                    getProgressBarOrientation(orientation),
                    BACKGROUND);
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
        paintBackground(context,
                        g,
                        x,
                        y,
                        w,
                        h,
                        getProgressBarOrientation(orientation),
                        BACKGROUND,
                        TEXT_BACKGROUND,
                        null);
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
        Rectangle progressClip = new Rectangle(component.getSize());
        switch (progressBarOrientation)
        {
            case HORIZONTAL:
                progressClip.width =
                        (int) (progressClip.width * component.getPercentComplete());
                break;
            case VERTICAL:
                progressClip.height =
                        (int) (progressClip.height * component.getPercentComplete());
                break;
            default:
                break;
        }
        paintBackground(context,
                        g,
                        x,
                        y,
                        w,
                        h,
                        progressBarOrientation,
                        FOREGROUND,
                        FOCUS,
                        progressClip);
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
        paintBackground(context,
                        g,
                        x,
                        y,
                        w,
                        h,
                        Orientation.HORIZONTAL,
                        BACKGROUND,
                        FOREGROUND,
                        null);
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
        paintBorder(context, g, x, y, w, h, Orientation.HORIZONTAL, BACKGROUND);
    }

    /** {@inheritDoc} */
    @Override
    public void paintArrowButtonForeground(SynthContext context,
                                           Graphics g,
                                           int x,
                                           int y,
                                           int w,
                                           int h,
                                           int direction)
    {
        String path =
                context.getStyle().getString(context,
                                             Properties.ARROW_PATH,
                                             null);

        if (path == null)
            return;
        final BufferedImage arrow;
        if (cachedImages.containsKey(path))
        {
            arrow = cachedImages.get(path);
        }
        else
        {
            arrow = Resourcer.loadImage(path);
            cachedImages.put(path, arrow);
        }

        if (arrow == null)
            return;

        AffineTransform xform;

        switch (direction)
        {
            case SwingConstants.NORTH:
                xform = new AffineTransform();
                break;
            case SwingConstants.SOUTH:
                xform =
                        new AffineTransform(1.,
                                            0.,
                                            0.,
                                            -1.,
                                            0.,
                                            arrow.getHeight());
                break;
            case SwingConstants.WEST:
                xform = new AffineTransform(0., 1., 1., 0., 0., 0.);
                break;
            case SwingConstants.EAST:
                xform =
                        new AffineTransform(0.,
                                            -1.,
                                            1.,
                                            0.,
                                            arrow.getHeight(),
                                            0.);
                break;
            default:
                return;
        }

        final JComponent component = context.getComponent();
        context.getStyle().getInsets(context, insets);
        int dx =
                component.getWidth() - insets.left - insets.right
                    - arrow.getWidth();
        int dy =
                component.getHeight() - insets.top - insets.bottom
                    - arrow.getHeight();
        xform.preConcatenate(AffineTransform.getTranslateInstance(dx / 2
            + insets.left, dy / 2 + insets.top));

        ((Graphics2D) g).drawRenderedImage(arrow, xform);
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
        paintBackground(context,
                        g,
                        x,
                        y,
                        w,
                        h,
                        Orientation.HORIZONTAL,
                        BACKGROUND,
                        FOREGROUND,
                        null);
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
        paintBorder(context, g, x, y, w, h, Orientation.HORIZONTAL, BACKGROUND);
    }
}
