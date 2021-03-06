/**
 * MegaMek - Copyright (C) 2005 Ben Mazur (bmazur@sev.org)
 * 
 *  This program is free software; you can redistribute it and/or modify it 
 *  under the terms of the GNU General Public License as published by the Free 
 *  Software Foundation; either version 2 of the License, or (at your option) 
 *  any later version.
 * 
 *  This program is distributed in the hope that it will be useful, but 
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 *  for more details.
 */
package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class ISSRM2 extends SRMWeapon {

    /**
     * 
     */
    private static final long serialVersionUID = -8486208221700793591L;

    /**
     * 
     */
    public ISSRM2() {
        super();
        this.techLevel = TechConstants.T_INTRO_BOXSET;
        this.name = "SRM 2";
        this.setInternalName(this.name);
        this.addLookupName("IS SRM-2");
        this.addLookupName("ISSRM2");
        this.addLookupName("IS SRM 2");
        this.heat = 2;
        this.rackSize = 2;
        this.shortRange = 3;
        this.mediumRange = 6;
        this.longRange = 9;
        this.extremeRange = 12;
        this.tonnage = 1.0f;
        this.criticals = 1;
        this.bv = 21;
        this.flags |= F_NO_FIRES;
        this.cost = 10000;
        this.shortAV = 2;
        this.maxRange = RANGE_SHORT;
    }
}
