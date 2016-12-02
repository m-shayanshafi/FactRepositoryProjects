/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: OSTCProtocol070511.java
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
package net.sf.jdivelog.ci.ostc;

import java.io.IOException;

import net.sf.jdivelog.ci.TransferException;
import net.sf.jdivelog.comm.PortInUseException;
import net.sf.jdivelog.comm.PortNotFoundException;
import net.sf.jdivelog.comm.UnsupportedCommOperationException;
import net.sf.jdivelog.gui.statusbar.StatusInterface;

/**
 * Protocol for the first version of the OSTC
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class OSTCProtocol070511 extends OSTCProtocol080115 {
    
    private static final String PROTOCOL_NAME = "OSTC 0.23, 1.0";
    private static final String[] FIRMWARE_VERSIONS = new String[] { "0.23", "1.0"};
    private static final String[] HASHKEYS = new String[] { "9969aa6a27e3ec8a7a879221624d9887", "7629254e5166ad700ad3053246e50829", "89fcad02249648d71b1a25cd0493512f"};
    
    @Override
    public void downloadSettings(StatusInterface status) throws PortNotFoundException, PortInUseException, UnsupportedCommOperationException, IOException, TransferException {
        // this version doesn't support Send EEPROM Command (0x67)
        super.download(status);
    }

    @Override
    public String[] getFirmwareVersions() {
        return FIRMWARE_VERSIONS;
    }

    @Override
    public String[] getHashkeys() {
        return HASHKEYS;
    }

    @Override
    public String getName() {
        return PROTOCOL_NAME;
    }
    
    @Override
    public boolean hasDecoInformationResetFunction() {
        return false;
    }

}
