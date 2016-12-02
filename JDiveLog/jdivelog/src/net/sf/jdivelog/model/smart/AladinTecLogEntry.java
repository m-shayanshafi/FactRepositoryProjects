/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: AladinTecLogEntry.java
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
package net.sf.jdivelog.model.smart;

/**
 * Description: container for one logbook entry of an Aladin Tec 2G file.
 *
 * @author Andr&eacute; Schenk
 * @version $Revision: 423 $
 */
public class AladinTecLogEntry extends LogEntry
{
    // DTI types
    private static final int UNKNOWN = -1;
    private static final int DELTA_DEPTH = 0;
    private static final int LARGE_ALARMS = 1;
    private static final int DELTA_TEMPERATURE = 2;
    private static final int LARGE_DELTA_DEPTH = 3;
    private static final int LARGE_DELTA_TEMPERATURE = 4;
    private static final int ALARMS = 5;
    private static final int TIME = 6;
    private static final int ABSOLUTE_DEPTH = 7;
    private static final int ABSOLUTE_TEMPERATURE = 8;

    private static final int HEADER_LENGTH = 116;

    private final transient byte [] read_data;
    private final transient int offset;
    private final transient int length;

    private transient int index = 0;

    private transient byte alarms = 0;
    private transient int depth = 0;
    private transient int depthCalibration = 0;
    private transient int temperature = 0;
    private transient int time = 4;

    public AladinTecLogEntry (byte [] read_data, int offset)
    {
        this.read_data = read_data;
        this.offset = offset + HEADER_LENGTH;

        diveDataLength = readULong (read_data, offset + 4);
        diveStartTime = getDateFromAladin ((int) readULong (read_data, offset + 8));
        //unknown1 = readULong (read_data, offset + 12);
        utcOffset = readUShort (read_data, offset + 16);
        repNo = readUShort (read_data, offset + 17);
        mbLevel = readUShort (read_data, offset + 18);
        battery = readUShort (read_data, offset + 19);
        //unknown2 = readUShort (read_data, offset + 20);
        alarmsDuringDive = readUShort (read_data, offset + 21);
        maximumDepth = new Float (readUInt (read_data, offset + 22)) / 100;
        duration = readUInt (read_data, offset + 26);
        minimumTemperature = new Float (readInt (read_data, offset + 30)) / 10;
        maximumTemperature = new Float (readInt (read_data, offset + 28)) / 10;;
        o2Percentage = readUInt (read_data, offset + 30);
        airTemperature = new Float (readInt (read_data, offset + 32)) / 10;
        surfaceInterval = readUInt (read_data, offset + 34);
        cnsPercentage = readUInt (read_data, offset + 36);
        altitudeLevel = readUInt (read_data, offset + 38);
        //unknown3 = readUInt (read_data, offset + 40);
        po2Limit = readUInt (read_data, offset + 42);
        depthLimit = readUInt (read_data, offset + 44);
        //unknown4 = readUInt (read_data, offset + 46);
        desatBeforeDive = readUInt (read_data, offset + 48);
        //unknown5 = readUInt (read_data, offset + 50);

        length = (int) diveDataLength - HEADER_LENGTH - 4;
        offset += HEADER_LENGTH;

        index = offset;
        while (available () > 0) {
            final int dti = readDTI ();

            switch (dti) {
            case DELTA_DEPTH :
                readDeltaDepth ();
                break;
            case LARGE_ALARMS :
                readLargeAlarms ();
                break;
            case DELTA_TEMPERATURE :
                readDeltaTemperature ();
                break;
            case LARGE_DELTA_DEPTH :
                readLargeDeltaDepth ();
                break;
            case LARGE_DELTA_TEMPERATURE :
                readLargeDeltaTemperature ();
                break;
            case ALARMS :
                readAlarms ();
                break;
            case TIME :
                readTime ();
                break;
            case ABSOLUTE_DEPTH :
                readAbsoluteDepth ();
                break;
            case ABSOLUTE_TEMPERATURE :
                readAbsoluteTemperature ();
                break;
            default :
                System.out.println ("received unknown DTI value " + dti);
                break;
            }
        }
    }

    private int available ()
    {
        return offset + length - index;
    }

    private void completeSegment ()
    {
        profile.add (new DepthProfileEntry
                     (alarms, depth * 2, new Float (temperature * 10) / 25, time));
        time += 4;
    }

    private void readAbsoluteDepth ()
    {
        boolean calibration = depth == 0;
        int depth = ByteToU17 (new byte [] {read_data [index + 2],
                                            read_data [index + 1],
                                            read_data [index + 0]});

        index += 3;
        if (calibration) {
            this.depthCalibration = depth;
        }
        else {
            this.depth = depth - this.depthCalibration;
            completeSegment ();
        }
    }

    private void readAbsoluteTemperature ()
    {
        temperature = ByteToS16 (new byte [] {read_data [index + 2],
                                              read_data [index + 1]});
        index += 3;
    }

    private void readAlarms ()
    {
        alarms = (byte) (read_data [index] & 0x0f);
        index++;
    }

    private void readDeltaDepth ()
    {
        depth += ByteToS7 (read_data [index]);
        index++;
        completeSegment ();
    }

    private void readDeltaTemperature ()
    {
        temperature += ByteToS6 (read_data [index]);
        index++;
    }

    private int readDTI ()
    {
        int result = UNKNOWN;

        if (available () > 0) {
            byte dti = read_data [index];

            if(dti != -1) {
                if ((dti & -1) == -2) {
                    if (available () >= 3) {
                        result = ABSOLUTE_TEMPERATURE;
                    }
                }
                else if ((dti & -2) == -4) {
                    if (available () >= 3) {
                        result = ABSOLUTE_DEPTH;
                    }
                }
                else if ((dti & -4) == -8) {
                    if (available () >= 2) {
                        result = LARGE_DELTA_TEMPERATURE;
                    }
                }
                else if ((dti & -8) == -16) {
                    if (available () >= 2) {
                        result = LARGE_DELTA_DEPTH;
                    }
                }
                else if ((dti & -16) == -32) {
                    if (available () >= 1) {
                        result = ALARMS;
                    }
                }
                else if ((dti & -32) == -64) {
                    if (available () >= 1) {
                        result = TIME;
                    }
                }
                else if ((dti & -64) == -128) {
                    if (available () >= 1) {
                        result = DELTA_TEMPERATURE;
                    }
                }
                else if ((dti & -128) == 0) {
                    if (available () >= 1) {
                        result = DELTA_DEPTH;
                    }
                }
            }
            else if (available() > 1) {
                index++;
                dti = read_data [index];
                if ((dti & -128) == 0) {
                    if (available () >= 2) {
                        result = LARGE_ALARMS;
                    }
                }
            }
        }
        return result;
    }

    private void readLargeAlarms ()
    {
        alarms = read_data [index + 1];
        index += 2;
    }

    private void readLargeDeltaDepth ()
    {
        depth += ByteToS11 (new byte [] {read_data [index + 0],
                                         read_data [index + 1]});
        index += 2;
        completeSegment ();
    }

    private void readLargeDeltaTemperature ()
    {
        temperature += ByteToS10 (new byte [] {read_data [index + 0],
                                               read_data [index + 1]});
        index += 2;
    }

    private void readTime ()
    {
        int count = ByteToU8 ((byte) (read_data [index] & 0x1f));

        index++;
        for (int i = 1; i <= count; i++) {
            completeSegment ();
        }
    }
}
