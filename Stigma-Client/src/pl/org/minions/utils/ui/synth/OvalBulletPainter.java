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

import java.awt.Graphics2D;

import javax.swing.plaf.synth.SynthContext;

/**
 * Oval variant of {@link BulletPainter}.
 */
public class OvalBulletPainter extends BulletPainter
{
    /** {@inheritDoc} */
    @Override
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

        g.drawOval(x, y, w - 1, h - 1);
    }

    /** {@inheritDoc} */
    @Override
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
        g.fillOval(x, y, w - 1, h - 1);
    }
}
