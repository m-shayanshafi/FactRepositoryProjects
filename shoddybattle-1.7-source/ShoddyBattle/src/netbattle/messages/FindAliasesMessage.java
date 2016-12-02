/*
 * FindAliasesMessage.java
 *
 * Created on August 20, 7:56 AM.
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

package netbattle.messages;
import java.util.List;

/**
 *
 * @author Colin
 */
public class FindAliasesMessage extends NetMessage {

    private String m_user;
    private List m_aliases;
    
    /** Creates a new instance of FindAliasesMessage */
    public FindAliasesMessage(String user, List aliases) {
        m_user = user;
        m_aliases = aliases;
    }
    
    public String getUser() {
        return m_user;
    }
    
    public List getAliases() {
        return m_aliases;
    }
    
    public int getMessage() {
        return FIND_ALIASES_MESSAGE;
    }

}
