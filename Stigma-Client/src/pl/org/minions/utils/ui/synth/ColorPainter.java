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

import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;

/**
 * A simple painter that paints background with backgrounds
 * color and borders with foreground color. It supports
 * transparency.
 */
public class ColorPainter extends SynthPainter
{
    private static final int ALPHA_OPAQUE = 255;

    private void paintBackground(SynthContext context,
                                 Graphics g,
                                 int x,
                                 int y,
                                 int w,
                                 int h)
    {
        final Color color =
                context.getStyle().getColor(context, ColorType.BACKGROUND);

        //Composite comp = null;
        if (color.getAlpha() < ALPHA_OPAQUE && g instanceof Graphics2D
            && !context.getComponent().isOpaque())
        {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(AlphaComposite.SrcOver);
        }

        g.setColor(color);
        g.fillRect(x, y, w, h);

    }

    private void paintBorder(SynthContext context,
                             Graphics g,
                             int x,
                             int y,
                             int w,
                             int h)
    {
        final Color color =
                context.getStyle().getColor(context, ColorType.FOREGROUND);
        if (color.getAlpha() > 0)
        {
            if (color.getAlpha() < ALPHA_OPAQUE && g instanceof Graphics2D
                && !context.getComponent().isOpaque())
            {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setComposite(AlphaComposite.SrcOver);
            }

            g.setColor(color);
            g.drawRect(x, y, w - 1, h - 1);
        }
    }

    // TODO: foreground, text background, text foreground

    /**
     *{@inheritDoc}
     */
    @Override
    public void paintArrowButtonBackground(SynthContext context,
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
    public void paintArrowButtonBorder(SynthContext context,
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
    public void paintArrowButtonForeground(SynthContext context,
                                           Graphics g,
                                           int x,
                                           int y,
                                           int w,
                                           int h,
                                           int direction)
    {
        // TODO Auto-generated method stub
        // super.paintArrowButtonForeground(context, g, x, y, w, h, direction);
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
    public void paintCheckBoxMenuItemBackground(SynthContext context,
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
    public void paintCheckBoxMenuItemBorder(SynthContext context,
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
    public void paintColorChooserBackground(SynthContext context,
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
    public void paintColorChooserBorder(SynthContext context,
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
    public void paintComboBoxBackground(SynthContext context,
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
    public void paintComboBoxBorder(SynthContext context,
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
    public void paintDesktopIconBackground(SynthContext context,
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
    public void paintDesktopIconBorder(SynthContext context,
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
    public void paintDesktopPaneBackground(SynthContext context,
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
    public void paintDesktopPaneBorder(SynthContext context,
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
    public void paintEditorPaneBackground(SynthContext context,
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
    public void paintEditorPaneBorder(SynthContext context,
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
    public void paintFileChooserBackground(SynthContext context,
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
    public void paintFileChooserBorder(SynthContext context,
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
    public void paintFormattedTextFieldBackground(SynthContext context,
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
    public void paintFormattedTextFieldBorder(SynthContext context,
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
    public void paintInternalFrameTitlePaneBackground(SynthContext context,
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
    public void paintInternalFrameTitlePaneBorder(SynthContext context,
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
    public void paintLabelBackground(SynthContext context,
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
    public void paintLabelBorder(SynthContext context,
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
    public void paintListBackground(SynthContext context,
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
    public void paintListBorder(SynthContext context,
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
    public void paintMenuBackground(SynthContext context,
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
    public void paintMenuBarBackground(SynthContext context,
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
    public void paintMenuBarBorder(SynthContext context,
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
    public void paintMenuBorder(SynthContext context,
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
    public void paintOptionPaneBackground(SynthContext context,
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
    public void paintOptionPaneBorder(SynthContext context,
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
    public void paintProgressBarBackground(SynthContext context,
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
    public void paintProgressBarBackground(SynthContext context,
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
    public void paintProgressBarBorder(SynthContext context,
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
    public void paintProgressBarBorder(SynthContext context,
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
    public void paintProgressBarForeground(SynthContext context,
                                           Graphics g,
                                           int x,
                                           int y,
                                           int w,
                                           int h,
                                           int orientation)
    {
        // TODO
        // super.paintProgressBarForeground(context, g, x, y, w, h, orientation);
    }

    /** {@inheritDoc} */
    @Override
    public void paintRadioButtonBackground(SynthContext context,
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
    public void paintRadioButtonBorder(SynthContext context,
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
    public void paintRadioButtonMenuItemBackground(SynthContext context,
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
    public void paintRadioButtonMenuItemBorder(SynthContext context,
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
    public void paintSeparatorBackground(SynthContext context,
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
    public void paintSeparatorBorder(SynthContext context,
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
    public void paintSeparatorForeground(SynthContext context,
                                         Graphics g,
                                         int x,
                                         int y,
                                         int w,
                                         int h,
                                         int orientation)
    {
        // TODO
        // super.paintSeparatorForeground(context, g, x, y, w, h, orientation);
    }

    /** {@inheritDoc} */
    @Override
    public void paintSliderBackground(SynthContext context,
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
    public void paintSliderBackground(SynthContext context,
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
    public void paintSliderBorder(SynthContext context,
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
    public void paintSliderBorder(SynthContext context,
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
    public void paintSliderThumbBackground(SynthContext context,
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
    public void paintSliderThumbBorder(SynthContext context,
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
    public void paintSliderTrackBackground(SynthContext context,
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
    public void paintSliderTrackBackground(SynthContext context,
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
    public void paintSliderTrackBorder(SynthContext context,
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
    public void paintSliderTrackBorder(SynthContext context,
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
    public void paintSpinnerBackground(SynthContext context,
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
    public void paintSpinnerBorder(SynthContext context,
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
    public void paintSplitPaneBackground(SynthContext context,
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
    public void paintSplitPaneBorder(SynthContext context,
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
    public void paintSplitPaneDividerBackground(SynthContext context,
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
    public void paintSplitPaneDividerBackground(SynthContext context,
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
    public void paintSplitPaneDividerForeground(SynthContext context,
                                                Graphics g,
                                                int x,
                                                int y,
                                                int w,
                                                int h,
                                                int orientation)
    {
        // TODO Auto-generated method stub
        // super.paintSplitPaneDividerForeground(context,
        // g,
        // x,
        // y,
        // w,
        // h,
        // orientation);
    }

    /** {@inheritDoc} */
    @Override
    public void paintSplitPaneDragDivider(SynthContext context,
                                          Graphics g,
                                          int x,
                                          int y,
                                          int w,
                                          int h,
                                          int orientation)
    {
        // TODO Auto-generated method stub
        // super.paintSplitPaneDragDivider(context, g, x, y, w, h, orientation);
    }

    /** {@inheritDoc} */
    @Override
    public void paintTabbedPaneBackground(SynthContext context,
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
    public void paintTabbedPaneBorder(SynthContext context,
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
    public void paintTabbedPaneContentBackground(SynthContext context,
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
    public void paintTabbedPaneContentBorder(SynthContext context,
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
    public void paintTabbedPaneTabAreaBackground(SynthContext context,
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
    public void paintTabbedPaneTabAreaBackground(SynthContext context,
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
    public void paintTabbedPaneTabAreaBorder(SynthContext context,
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
    public void paintTabbedPaneTabAreaBorder(SynthContext context,
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
    public void paintTabbedPaneTabBackground(SynthContext context,
                                             Graphics g,
                                             int x,
                                             int y,
                                             int w,
                                             int h,
                                             int tabIndex,
                                             int orientation)
    {
        paintBackground(context, g, tabIndex, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintTabbedPaneTabBackground(SynthContext context,
                                             Graphics g,
                                             int x,
                                             int y,
                                             int w,
                                             int h,
                                             int tabIndex)
    {
        paintBackground(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintTabbedPaneTabBorder(SynthContext context,
                                         Graphics g,
                                         int x,
                                         int y,
                                         int w,
                                         int h,
                                         int tabIndex,
                                         int orientation)
    {
        paintBorder(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintTabbedPaneTabBorder(SynthContext context,
                                         Graphics g,
                                         int x,
                                         int y,
                                         int w,
                                         int h,
                                         int tabIndex)
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
    public void paintTableHeaderBackground(SynthContext context,
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
    public void paintTableHeaderBorder(SynthContext context,
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
    public void paintTextAreaBackground(SynthContext context,
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
    public void paintTextAreaBorder(SynthContext context,
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
    public void paintTextPaneBackground(SynthContext context,
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
    public void paintTextPaneBorder(SynthContext context,
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
    public void paintToolBarBackground(SynthContext context,
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
    public void paintToolBarBackground(SynthContext context,
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
    public void paintToolBarBorder(SynthContext context,
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
    public void paintToolBarBorder(SynthContext context,
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
    public void paintToolBarContentBackground(SynthContext context,
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
    public void paintToolBarContentBackground(SynthContext context,
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
    public void paintToolBarContentBorder(SynthContext context,
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
    public void paintToolBarContentBorder(SynthContext context,
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
    public void paintToolBarDragWindowBackground(SynthContext context,
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
    public void paintToolBarDragWindowBackground(SynthContext context,
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
    public void paintToolBarDragWindowBorder(SynthContext context,
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
    public void paintToolBarDragWindowBorder(SynthContext context,
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
    public void paintToolTipBackground(SynthContext context,
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
    public void paintToolTipBorder(SynthContext context,
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
    public void paintTreeBackground(SynthContext context,
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
    public void paintTreeBorder(SynthContext context,
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
    public void paintTreeCellBackground(SynthContext context,
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
    public void paintTreeCellBorder(SynthContext context,
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
    public void paintTreeCellFocus(SynthContext context,
                                   Graphics g,
                                   int x,
                                   int y,
                                   int w,
                                   int h)
    {
        // TODO Auto-generated method stub
        // super.paintTreeCellFocus(context, g, x, y, w, h);
    }

    /** {@inheritDoc} */
    @Override
    public void paintViewportBackground(SynthContext context,
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
    public void paintViewportBorder(SynthContext context,
                                    Graphics g,
                                    int x,
                                    int y,
                                    int w,
                                    int h)
    {
        paintBorder(context, g, x, y, w, h);
    }

}
