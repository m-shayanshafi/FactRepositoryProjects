/*
 * NetMessage.java
 *
 * Created on December 19, 2006, 5:48 PM
 *
 * This file is a part of Shoddy Battle.
 * Copyright (C) 2006  Colin Fitzpatrick
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
 * along with this program; if not, visit the Free Software Foundation, Inc.
 * online at http://gnu.org.
 */

package netbattle.messages;
import java.io.*;

/**
 *
 * @author Colin
 */
public abstract class NetMessage implements Serializable {
    
    /** Increase this by one for each change that breaks server<->client
     *  compatibility. */
    private static final long serialVersionUID = 1L;
    
    public static final int FIELD_TEXT = 0;
    public static final int BATTLE_READY = 1;
    public static final int JOIN_SERVER = 2;
    public static final int WELCOME_MESSAGE = 3;
    public static final int SUCCESS_MESSAGE = 4;
    public static final int USER_LIST = 5;
    public static final int STATUS_CHANGE = 6;
    public static final int ISSUE_CHALLENGE = 7;
    public static final int ACCEPT_CHALLENGE = 8;
    public static final int ACCEPTED_CHALLENGE = 9;
    public static final int FINALISE_CHALLENGE = 10;
    public static final int REQUEST_MOVE = 11;
    public static final int USE_MOVE = 12;
    public static final int SELECTION_END = 13;
    public static final int REPLACE_POKEMON = 14;
    public static final int STAT_REFRESH = 15;
    public static final int PARTY_MESSAGE = 16;
    public static final int ADD_BATTLE = 17;
    public static final int REGISTER_ACCOUNT = 18;
    public static final int INFORM_DAMAGE = 19;
    public static final int BAN_USER = 20;
    public static final int USER_TABLE = 21;
    public static final int BATTLE_END = 22;
    public static final int WITHDRAW_CHALLENGE = 23;
    public static final int RATIO_REFRESH = 24;
    public static final int UPDATE_POKEMON_STATUS = 25;
    public static final int SPECTATOR_MESSAGE = 26;
    public static final int WELCOME_TEXT_MESSAGE = 27;
    public static final int ACTIVITY_MESSAGE = 28;
    public static final int ERROR_LOG_MESSAGE = 29;
    public static final int FIND_ALIASES_MESSAGE = 30;
    public static final int ERROR_MESSAGE = 31;
    
    public NetMessage() {
        
    }
    
    public NetMessage(int message) {
        
    }
    
    public abstract int getMessage();
    
    /**
     * Returns whether the client must be logged in before he should
     * receive this message.
     */
    public boolean logInRequired() {
        return true;
    }
}
