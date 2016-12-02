/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: SmartComputerType.java
 *
 * @author Andr&eacute; Schenk <andre_schenk@users.sourceforge.net>
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
package net.sf.jdivelog.ci;

import static net.sf.jdivelog.comm.SerialPort.DataBits;
import static net.sf.jdivelog.comm.SerialPort.Parity;
import static net.sf.jdivelog.comm.SerialPort.StopBits;

/**
 * Description: Enumeration for Suunto computer models.
 *
 * @author Andr&eacute; Schenk
 * @version $Revision: 548 $
 */
public enum SuuntoComputerType
{
    STINGER     (0x03, "Stinger",       2400, DataBits.DataBits_8, Parity.ODD,  StopBits.StopBits_1),
    MOSQUITO    (0x04, "Mosquito",      2400, DataBits.DataBits_8, Parity.ODD,  StopBits.StopBits_1),
    NEW_VYPER   (0x0a, "new Vyper",     2400, DataBits.DataBits_8, Parity.ODD,  StopBits.StopBits_1),
    VYPER_COBRA (0x0c, "Vyper / Cobra", 2400, DataBits.DataBits_8, Parity.ODD,  StopBits.StopBits_1),
    VYTEC       (0x0b, "VyTec",         2400, DataBits.DataBits_8, Parity.ODD,  StopBits.StopBits_1),
    GEKKO       (0x0d, "Gekko",         2400, DataBits.DataBits_8, Parity.ODD,  StopBits.StopBits_1),
    ZOOP        (0x16, "Zoop",          2400, DataBits.DataBits_8, Parity.ODD,  StopBits.StopBits_1),
    D6          (0x0f, "D6",            9600, DataBits.DataBits_8, Parity.NONE, StopBits.StopBits_1),
    D9          (0x0e, "D9",            9600, DataBits.DataBits_8, Parity.NONE, StopBits.StopBits_1);

    private final int identifier;
    private final String label;
    private final int baudRate;
    private final DataBits numDataBits;
    private final Parity parity;
    private final StopBits numStopBits;

    SuuntoComputerType (int identifier, String label, int baudRate,
                        DataBits numDataBits, Parity parity,
                        StopBits numStopBits)
    {
        this.identifier  = identifier;
        this.label       = label;
        this.baudRate    = baudRate;
        this.numDataBits = numDataBits;
        this.parity      = parity;
        this.numStopBits = numStopBits;
    }

    public int getBaudRate ()
    {
        return baudRate;
    }

    public int getIdentifier ()
    {
        return identifier;
    }

    public DataBits getNumDataBits ()
    {
        return numDataBits;
    }

    public StopBits getNumStopBits ()
    {
        return numStopBits;
    }

    public Parity getParity ()
    {
        return parity;
    }

    public String toString ()
    {
        return label;
    }

    public static SuuntoComputerType getFromOrdinal (int ordinal)
    {
        for (SuuntoComputerType e : SuuntoComputerType.values ()) {
            if (e.ordinal () == ordinal) {
                return e;
            }
        }
        throw new RuntimeException ("no Suunto computer with ordinal " +
                                    ordinal + " found");
    }

    public static SuuntoComputerType getFromIdentifier (int identifier)
    {
        for (SuuntoComputerType e : SuuntoComputerType.values ()) {
            if (e.identifier == identifier) {
                return e;
            }
        }
        throw new RuntimeException ("no Suunto computer with identifier " +
                                    identifier + " found");
    }
}
