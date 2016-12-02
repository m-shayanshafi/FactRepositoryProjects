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

import pl.org.minions.stigma.globals.GlobalConfig;
import pl.org.minions.stigma.globals.ResourceSetProperties;
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;
import pl.org.minions.stigma.network.Cipher;
import pl.org.minions.stigma.network.messaging.NetworkMessage;
import pl.org.minions.stigma.network.messaging.NetworkMessageType;
import pl.org.minions.utils.logger.Log;

/**
 * Message representing authorization request. Contains
 * public key - acquired automatically from global (for
 * current application instance) cipher.
 */
public class LoginRequest extends NetworkMessage
{
    private static final int CONST_SIZE =
            SizeOf.BYTE + SizeOf.SHORT + SizeOf.SHORT;
    private GlobalConfig config;

    private LoginRequest()
    {
        super(NetworkMessageType.LOGIN_REQUEST);
    }

    /**
     * Constructor.
     * @param config
     *            configuration to send to client
     */
    public LoginRequest(GlobalConfig config)
    {
        super(NetworkMessageType.LOGIN_REQUEST);
        this.config = config;
    }

    /**
     * Creates empty incorrect message.
     * @return empty message
     */
    public static LoginRequest create()
    {
        return new LoginRequest();
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        byte[] key = buffer.decodeBytes();
        if (key == null)
        {
            Log.logger.error("Bad key in AuthRequest message");
            return false;
        }
        Cipher.initClient(key);

        ResourceSetProperties resProp =
                new ResourceSetProperties(buffer.decodeString(),
                                          buffer.decodeString());
        config =
                new GlobalConfig(resProp,
                                 buffer.decodeString(),
                                 buffer.decodeString(),
                                 buffer.decodeShort(),
                                 buffer.decodeShort(),
                                 buffer.decodeByte() != 0);

        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        buffer.encode(Cipher.getPublicKey());

        ResourceSetProperties resProp = config.getResourceSetProperties();
        buffer.encode(resProp.getName());
        buffer.encode(resProp.getDescription());

        buffer.encode(config.getClientResourceRoot());
        buffer.encode(config.getResourceSet());
        buffer.encode((short) config.getNetworkTimeout());
        buffer.encode((short) config.getMillisecondsPerTurn());
        buffer.encode(config.getClientResourceCompression() ? (byte) 1
                                                           : (byte) 0);
        return true;
    }

    /**
     * Configuration encoded in this message.
     * @return configuration which is transported using this
     *         message.
     */
    public GlobalConfig getConfig()
    {
        return config;
    }

    /** {@inheritDoc} */
    @Override
    protected int innerGetParamsLength()
    {
        int ret = 0;
        if (Cipher.getPublicKey() != null)
            ret += Cipher.getPublicKey().length + 2;
        ret += Buffer.stringBytesCount(config.getClientResourceRoot());
        ret += Buffer.stringBytesCount(config.getResourceSet());
        ResourceSetProperties resProp = config.getResourceSetProperties();
        ret += Buffer.stringBytesCount(resProp.getName());
        ret += Buffer.stringBytesCount(resProp.getDescription());
        ret += CONST_SIZE;
        return ret;
    }

}
