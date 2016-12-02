/*
 * SleepClause.java
 *
 * Created on July 22, 2007, 1:54 AM
 *
 * This file is a part of Shoddy Battle.
 * Copyright (C) 2007  Colin Fitzpatrick
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * The Free Software Foundation may be visited online at http://www.fsf.org.
 */

package mechanics.clauses;
import mechanics.statuses.StatusEffect;
import mechanics.statuses.field.FieldEffect;
import shoddybattle.*;

/**
 * This clause prevents a trainer from putting a particular status on more
 * than one of the opponent's pokemon at a time.
 * 
 * @author Colin
 */
public abstract class EffectClause extends Clause {

    private Class m_effect;
    
    /**
     * @param effect the status effect to restrict
     */
    public EffectClause(String name, Class effect) {
        super(name);
        m_effect = effect;
    }
    
    public boolean equals(Object o2) {
        if (!(o2 instanceof EffectClause))
            return false;
        if (o2 == null)
            return false;
        return ((EffectClause)o2).m_effect.equals(m_effect);
    }
    
    public boolean allowsStatus(StatusEffect eff, Pokemon source, Pokemon target) {
        if (source == target)
            return true;
        if (!m_effect.isAssignableFrom(eff.getClass()))
            return true;
        /** See if the opponent already has a pokemon with this effect and that
         *  that effect was induced by this enemy trainer. */
        Pokemon[] party = target.getTeammates();
        for (int i = 0; i < party.length; ++i) {
            Pokemon p = party[i];
            if (p.isFainted())
                continue;
            StatusEffect effect = p.getEffect(m_effect);
            if (effect != null) {
                Pokemon inducer = effect.getInducer();
                if ((inducer != null) &&
                    (inducer.getParty() == source.getParty()))
                    return false;
            }
        }
        return true;
    }

}
