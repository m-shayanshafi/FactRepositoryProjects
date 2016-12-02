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
package pl.org.minions.stigma.network.messaging.auth;

import pl.org.minions.stigma.network.Buffer;
import pl.org.minions.stigma.network.Cipher;
import pl.org.minions.stigma.network.messaging.NetworkMessage;
import pl.org.minions.stigma.network.messaging.NetworkMessageType;
import pl.org.minions.utils.Version;
import pl.org.minions.utils.logger.Log;

/**
 * Message containing data needed for authorization: client
 * version, login and password. Login and password are
 * ciphered during transportation via network.
 */
public class LoginData extends NetworkMessage
{
    private String version;
    private String user;
    private String passwd;

    private LoginData()
    {
        super(NetworkMessageType.LOGIN_DATA);
    }

    /**
     * Constructor. Client version set automatically - using
     * proper imports.
     * @param user
     *            user name
     * @param passwd
     *            password
     */
    public LoginData(String user, String passwd)
    {
        this();
        this.version = Version.SIMPLIFIED_VERSION;
        this.user = user;
        this.passwd = passwd;
    }

    /**
     * Creates empty object (needed for parsing network
     * message) Object state and information are unknown.
     * @return empty object
     */
    public static LoginData create()
    {
        return new LoginData();
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        this.version = buffer.decodeString();
        String s = buffer.decryptAndDecodeString();
        String[] t = s.split("\n");
        if (t.length < 2)
        {
            Log.logger.error("user-pass string improper format");
            return false;
        }
        this.user = t[0];
        this.passwd = t[1];
        return version != null && user != null && passwd != null;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        buffer.encode(version);
        String s = user + '\n' + passwd;
        buffer.encryptAndEncodeString(s);
        return true;
    }

    /**
     * Returns password.
     * @return password
     */
    public String getPasswd()
    {
        return passwd;
    }

    /**
     * Returns user name.
     * @return user name
     */
    public String getUser()
    {
        return user;
    }

    /**
     * Returns system version.
     * @return system version.
     */
    public String getVersion()
    {
        return version;
    }

    /** {@inheritDoc} */
    @Override
    protected int innerGetParamsLength()
    {
        return Buffer.stringBytesCount(version)
            + Cipher.getEncryptedSize(Buffer.stringBytesCount(user)
                + Buffer.stringBytesCount(passwd) + 1);
    }

}
