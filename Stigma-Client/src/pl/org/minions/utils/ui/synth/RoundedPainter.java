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

import javax.swing.JSeparator;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;

/**
 * {@link SynthPainter} implementation that paints a shiny
 * bullet-like rounded rectangle.
 * <p>
 * Currently supports only button painting.
 */
public class RoundedPainter extends SynthPainter
{
    private static final int ALPHA_OPAQUE = 255;
    private Insets insets = new Insets(0, 0, 0, 0);

    /** {@inheritDoc} */
    @Override
    public void paintPanelBackground(SynthContext context,
                                     Graphics g,
                                     int x,
                                     int y,
                                     int w,
                                     int h)
    {
        paintBackground(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintScrollPaneBackground(SynthContext context,
                                          Graphics g,
                                          int x,
                                          int y,
                                          int w,
                                          int h)
    {
        paintBackground(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintScrollPaneBorder(SynthContext context,
                                      Graphics g,
                                      int x,
                                      int y,
                                      int w,
                                      int h)
    {
        paintBorder(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintPanelBorder(SynthContext context,
                                 Graphics g,
                                 int x,
                                 int y,
                                 int w,
                                 int h)
    {
        paintBorder(context, g, x, y, w, h);
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
        paintBackground(context, g, x, y, w, h);
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
        paintBorder(context, g, x, y, w, h);
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
        paintBackground(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintCheckBoxBackground(SynthContext context,
                                        Graphics g,
                                        int x,
                                        int y,
                                        int w,
                                        int h)
    {
        paintBackground(context, g, x, y, w, h);
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
        paintBorder(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintCheckBoxBorder(SynthContext context,
                                    Graphics g,
                                    int x,
                                    int y,
                                    int w,
                                    int h)
    {
        paintBorder(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintInternalFrameBackground(SynthContext context,
                                             Graphics g,
                                             int x,
                                             int y,
                                             int w,
                                             int h)
    {
        paintBackground(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintInternalFrameBorder(SynthContext context,
                                         Graphics g,
                                         int x,
                                         int y,
                                         int w,
                                         int h)
    {
        paintBorder(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintRootPaneBackground(SynthContext context,
                                        Graphics g,
                                        int x,
                                        int y,
                                        int w,
                                        int h)
    {
        paintBackground(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintRootPaneBorder(SynthContext context,
                                    Graphics g,
                                    int x,
                                    int y,
                                    int w,
                                    int h)
    {
        paintBorder(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintTableBackground(SynthContext context,
                                     Graphics g,
                                     int x,
                                     int y,
                                     int w,
                                     int h)
    {
        paintBackground(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintTableBorder(SynthContext context,
                                 Graphics g,
                                 int x,
                                 int y,
                                 int w,
                                 int h)
    {
        paintBorder(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintTextFieldBackground(SynthContext context,
                                         Graphics g,
                                         int x,
                                         int y,
                                         int w,
                                         int h)
    {
        paintBackground(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintTextFieldBorder(SynthContext context,
                                     Graphics g,
                                     int x,
                                     int y,
                                     int w,
                                     int h)
    {
        paintBorder(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintPasswordFieldBackground(SynthContext context,
                                             Graphics g,
                                             int x,
                                             int y,
                                             int w,
                                             int h)
    {
        paintBackground(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintPasswordFieldBorder(SynthContext context,
                                         Graphics g,
                                         int x,
                                         int y,
                                         int w,
                                         int h)
    {
        paintBorder(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintScrollBarBackground(SynthContext context,
                                         Graphics g,
                                         int x,
                                         int y,
                                         int w,
                                         int h,
                                         int orientation)
    {
        paintBackground(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintScrollBarBackground(SynthContext context,
                                         Graphics g,
                                         int x,
                                         int y,
                                         int w,
                                         int h)
    {
        paintBackground(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintScrollBarBorder(SynthContext context,
                                     Graphics g,
                                     int x,
                                     int y,
                                     int w,
                                     int h,
                                     int orientation)
    {
        paintBorder(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintScrollBarBorder(SynthContext context,
                                     Graphics g,
                                     int x,
                                     int y,
                                     int w,
                                     int h)
    {
        paintBorder(context, g, x, y, w, h);
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
        paintBackground(context, g, x, y, w, h);
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
        paintBorder(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintScrollBarTrackBackground(SynthContext context,
                                              Graphics g,
                                              int x,
                                              int y,
                                              int w,
                                              int h,
                                              int orientation)
    {
        paintBackground(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintScrollBarTrackBackground(SynthContext context,
                                              Graphics g,
                                              int x,
                                              int y,
                                              int w,
                                              int h)
    {
        paintBackground(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintScrollBarTrackBorder(SynthContext context,
                                          Graphics g,
                                          int x,
                                          int y,
                                          int w,
                                          int h,
                                          int orientation)
    {
        paintBorder(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintScrollBarTrackBorder(SynthContext context,
                                          Graphics g,
                                          int x,
                                          int y,
                                          int w,
                                          int h)
    {
        paintBorder(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintPopupMenuBackground(SynthContext context,
                                         Graphics g,
                                         int x,
                                         int y,
                                         int w,
                                         int h)
    {
        paintBackground(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintPopupMenuBorder(SynthContext context,
                                     Graphics g,
                                     int x,
                                     int y,
                                     int w,
                                     int h)
    {
        paintBorder(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintMenuItemBackground(SynthContext context,
                                        Graphics g,
                                        int x,
                                        int y,
                                        int w,
                                        int h)
    {
        paintBackground(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintMenuItemBorder(SynthContext context,
                                    Graphics g,
                                    int x,
                                    int y,
                                    int w,
                                    int h)
    {
        paintBorder(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintSeparatorBackground(SynthContext context,
                                         Graphics g,
                                         int x,
                                         int y,
                                         int w,
                                         int h)
    {
        paintBackground(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintSeparatorBackground(SynthContext context,
                                         Graphics g,
                                         int x,
                                         int y,
                                         int w,
                                         int h,
                                         int orientation)
    {
        paintSeparatorBackground(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintSeparatorBorder(SynthContext context,
                                     Graphics g,
                                     int x,
                                     int y,
                                     int w,
                                     int h)
    {
        paintBorder(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintSeparatorBorder(SynthContext context,
                                     Graphics g,
                                     int x,
                                     int y,
                                     int w,
                                     int h,
                                     int orientation)
    {
        paintSeparatorBorder(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintSeparatorForeground(SynthContext context,
                                         Graphics g,
                                         int x,
                                         int y,
                                         int w,
                                         int h,
                                         int orientation)
    {
        final Color color =
                context.getStyle().getColor(context, ColorType.TEXT_FOREGROUND);

        insets = context.getStyle().getInsets(context, insets);
        assert insets != null;

        final boolean opaque = context.getComponent().isOpaque();
        if (color.getAlpha() < ALPHA_OPAQUE && !opaque)
        {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(AlphaComposite.SrcOver);
        }

        w = w - insets.left - insets.right;
        h = h - insets.top - insets.bottom;

        final int arc;

        switch (orientation)
        {
            case JSeparator.HORIZONTAL:
                arc = h;
                h = arc + 1;
                break;
            case JSeparator.VERTICAL:
                arc = w;
                w = arc + 1;
                break;
            default:
                arc = 0;
                break;
        }

        g.setColor(color);
        if (opaque)
            g.fillRect(x, y, w, h);
        else
            g.fillRoundRect(x, y, w, h, arc, arc);
    }

    private void paintBackground(SynthContext context,
                                 Graphics g,
                                 int x,
                                 int y,
                                 int w,
                                 int h)
    {
        paintBackground(context, g, x, y, w, h, ColorType.BACKGROUND);
    }

    private void paintBackground(SynthContext context,
                                 Graphics g,
                                 int x,
                                 int y,
                                 int w,
                                 int h,
                                 final ColorType colorType)
    {
        final Color color = context.getStyle().getColor(context, colorType);

        insets = context.getStyle().getInsets(context, insets);
        assert insets != null;

        int arc =
                Math.max(Math.max(insets.left, insets.right),
                         Math.max(insets.top, insets.bottom));

        final boolean opaque = context.getComponent().isOpaque();
        if (color.getAlpha() < ALPHA_OPAQUE && !opaque)
        {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(AlphaComposite.SrcOver);
        }

        g.setColor(color);
        if (opaque)
            g.fillRect(x, y, w - 1, h - 1);
        else
            g.fillRoundRect(x, y, w - 1, h - 1, arc, arc);
    }

    /**
     * @param context
     * @param g
     * @param x
     * @param y
     * @param w
     * @param h
     */
    private void paintBorder(SynthContext context,
                             Graphics g,
                             int x,
                             int y,
                             int w,
                             int h)
    {
        final Color color =
                context.getStyle().getColor(context, ColorType.FOREGROUND);
        insets = context.getStyle().getInsets(context, insets);
        assert insets != null;

        int arc =
                Math.max(Math.max(insets.left, insets.right),
                         Math.max(insets.top, insets.bottom));

        final boolean opaque = context.getComponent().isOpaque();
        if (color.getAlpha() < ALPHA_OPAQUE && !opaque)
        {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(AlphaComposite.SrcOver);
        }

        g.setColor(color);
        if (opaque)
            g.drawRect(x, y, w - 1, h - 1);
        else
            g.drawRoundRect(x, y, w - 1, h - 1, arc, arc);
    }
}
