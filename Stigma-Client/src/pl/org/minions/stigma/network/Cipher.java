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
package pl.org.minions.stigma.network;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import pl.org.minions.utils.logger.Log;

/**
 * Static-only (singleton) class for asymmetric encryption.
 * It can run in two modes (changed by proper init*
 * function):
 * <ul>
 * <li><i>server</i> - generates private and public key,
 * using private key for decryption, only decryption is
 * allowed in this mode</li>
 * <li><i>client</i> - uses remote (server's) public key for
 * initialization, and for further encryption, only
 * encryption is allowed in this mode
 * </ul>
 */
public abstract class Cipher
{
    private static enum State
    {
        SERVER, CLIENT
    }

    private static final int KEY_LENGHT = 1024;
    private static final String ALGORITHM = "RSA";
    private static PrivateKey privateKey;
    private static byte[] publicKey;
    private static PublicKey remotePublicKey;

    private static javax.crypto.Cipher cipher;

    private static State state;

    static
    {
        try
        {
            cipher = javax.crypto.Cipher.getInstance(ALGORITHM);
        }
        catch (NoSuchAlgorithmException e)
        {
            Log.logger.fatal(e);
            System.exit(1);
        }
        catch (NoSuchPaddingException e)
        {
            Log.logger.fatal(e);
            System.exit(1);
        }
    }

    /** Static-only */
    private Cipher()
    {
    }

    /**
     * Available only in "server" mode. Uses server private
     * key for decryption of bytes vector.
     * @param b
     *            bytes to decrypt
     * @return decrypted bytes
     */
    public static byte[] decrypt(byte[] b)
    {
        assert state == State.SERVER;
        try
        {
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(b);
        }
        catch (InvalidKeyException e)
        {
            Log.logger.error(e);
        }
        catch (IllegalBlockSizeException e)
        {
            Log.logger.error(e);
        }
        catch (BadPaddingException e)
        {
            Log.logger.error(e);
        }
        return new byte[0];
    }

    /**
     * Available only in "client" mode. Uses remote public
     * key (given in initialization) to encrypt bytes
     * vector. Must be called after
     * {@link #getEncryptedSize(int)}.
     * @param b
     *            bytes to encrypt
     * @return encrypted bytes
     */
    public static byte[] encrypt(byte[] b)
    {
        assert state == State.CLIENT;
        try
        {
            return cipher.doFinal(b);
        }
        catch (IllegalBlockSizeException e)
        {
            Log.logger.error(e);
        }
        catch (BadPaddingException e)
        {
            Log.logger.error(e);
        }
        return new byte[0];
    }

    /**
     * Returns size of output made from encryption of bytes
     * array of given length. Must be called before
     * {@link #encrypt(byte[])} - initializes cipher.
     * @param inputLength
     *            length of input
     * @return encrypted output size
     */
    public static int getEncryptedSize(int inputLength)
    {
        try
        {
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, remotePublicKey);
            return cipher.getOutputSize(inputLength);
        }
        catch (InvalidKeyException e)
        {
            Log.logger.error(e);
        }
        return 0;
    }

    /**
     * Available only in "server" mode. Return server public
     * key.
     * @return server public key
     */
    public static byte[] getPublicKey()
    {
        assert state == State.SERVER;
        return publicKey;
    }

    /**
     * Initializes Cipher in "client" mode. Sets {@code
     * remotePublicKey} as default key for encryption.
     * Destroys effects of previous initialization.
     * @param remotePublicKeyBytes
     *            key to use for encryption
     */
    public static void initClient(byte[] remotePublicKeyBytes)
    {
        try
        {
            Cipher.remotePublicKey =
                    KeyFactory.getInstance(ALGORITHM)
                              .generatePublic(new X509EncodedKeySpec(remotePublicKeyBytes));
        }
        catch (InvalidKeySpecException e)
        {
            Log.logger.error(e);
        }
        catch (NoSuchAlgorithmException e)
        {
            Log.logger.error(e);
        }

        state = State.CLIENT;
    }

    /**
     * Initializes Cipher "server" mode - generates
     * asymmetric key pair. Sets private key as default key
     * for decryption. Destroys effects of previous
     * initialization.
     */
    public static void initServer()
    {
        KeyPair keyPair;
        try
        {
            KeyPairGenerator gen = KeyPairGenerator.getInstance(ALGORITHM);
            gen.initialize(KEY_LENGHT);
            keyPair = gen.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic().getEncoded();
        }
        catch (NoSuchAlgorithmException e)
        {
            Log.logger.fatal("KeyPair creation failed");
            System.exit(0);
        }

        state = State.SERVER;
    }
}
