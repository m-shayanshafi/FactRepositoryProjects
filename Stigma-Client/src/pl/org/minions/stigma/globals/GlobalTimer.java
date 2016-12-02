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
package pl.org.minions.stigma.globals;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Singleton class holding one global {@link Timer}. It is
 * mainly used to minimize {@link Timer} threads count.
 */
public final class GlobalTimer
{
    private static Timer timer = new Timer("globalTimer", true);
    private static final int PURGING_PERIOD = 60 * 1000; // 1 min

    private GlobalTimer()
    {
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                timer.purge();
            }
        }, PURGING_PERIOD, PURGING_PERIOD);
    }

    /**
     * Returns global {@link Timer} instance.
     * @return global {@link Timer} instance.
     */
    public static Timer getTimer()
    {
        return timer;
    }
}
