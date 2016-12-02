/*
 * TrickTakingGame - Trick-taking games platform on-line
 * Copyright (C) 2008  Sébastien Doeraene
 * All Rights Reserved
 *
 * This file is part of TrickTakingGame.
 *
 * TrickTakingGame is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * TrickTakingGame is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * TrickTakingGame.  If not, see <http://www.gnu.org/licenses/>.
 */
package sjrd.util;

import java.io.*;
import java.security.*;

/**
 * Utilitaires de hash
 * @author sjrd
 */
public class HashUtils
{
	/**
	 * Calcule le hash MD5 d'une chaîne et le renvoie en chaîne hexa
	 * @param string Chaîne à hasher
	 * @return Hash MD5 de la chaîne <tt>string</tt>
	 */
	public static String md5String(String string)
	{
		try
		{
			MessageDigest msgDigest = MessageDigest.getInstance("MD5");
			msgDigest.update(string.getBytes("UTF-8"));
			byte[] digest = msgDigest.digest();
			
			String result = "";
			for (int i = 0; i < digest.length; i++)
			{
				int value = digest[i];
				if (value < 0)
					value += 256;
				
				result += Integer.toHexString(value);
			}
			
			return result;
		}
		catch (UnsupportedEncodingException error)
		{
			throw new IllegalArgumentException(error);
		}
		catch (NoSuchAlgorithmException error)
		{
			throw new IllegalArgumentException(error);
		}
	}
}
