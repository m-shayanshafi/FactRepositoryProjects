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
package pl.org.minions.stigma.license;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.EnumMap;

import pl.org.minions.utils.i18n.Translated;
import pl.org.minions.utils.i18n.Translator;
import pl.org.minions.utils.logger.Log;

/**
 * Class that provides copyrights and license informations
 * about program. All text can be localized to meet local
 * standards.
 */
public final class LicenseInfo
{
    /**
     * Format of license text.
     */
    public enum Format
    {
        HTML
        {
            /** {@inheritDoc} */
            @Override
            public String getContentType()
            {
                return "text/html";
            }

            /** {@inheritDoc} */
            @Override
            public String getExtension()
            {
                return ".html";
            }
        },
        TEXT
        {
            /** {@inheritDoc} */
            @Override
            public String getContentType()
            {
                return "text/plain; charset=UTF-8";
            }

            /** {@inheritDoc} */
            @Override
            public String getExtension()
            {
                return ".txt";
            }
        };

        /**
         * Returns MIME content type for given format.
         * @return MIME content type for given format
         */
        public abstract String getContentType();

        /**
         * Returns extension connected with given type.
         * @return extension connected with given type.
         */
        public abstract String getExtension();
    }

    @Translated
    private static String PRODUCT_NAME = "Stigma - The Game";
    @Translated
    private static String PRODUCT_DESCRIPTION = "Multiplayer online RPG";
    @Translated
    private static String PRODUCT_WEB = "http://stigma.sourceforge.net";
    @Translated
    private static String COPYRIGHT_INFO =
            "Copyright (C) 2005-2009 Minions Studio";
    @Translated
    private static String DEVELOPERS_GROUP_WEB = "http://www.minions.org.pl";
    @Translated
    private static String TRANSLATION_INFO = "Minions Studio";
    @Translated
    private static String SUPPORT_WEB =
            "http://sourceforge.net/apps/trac/stigma";

    private static EnumMap<Format, String> shortTextMap =
            new EnumMap<Format, String>(Format.class);

    private static EnumMap<Format, String> fullTextMap =
            new EnumMap<Format, String>(Format.class);
    private static final long serialVersionUID = 1L;

    private LicenseInfo()
    {
    }

    /**
     * Returns full text of copyright notice.
     * @return full text of copyright notice.
     */
    public static String getCopyrightInfo()
    {
        return COPYRIGHT_INFO;
    }

    /**
     * Returns address of developers group.
     * @return address of developers group.
     */
    public static String getDevelopresGroupWeb()
    {
        return DEVELOPERS_GROUP_WEB;
    }

    /**
     * Returns full text of product license in given format.
     * @param format
     *            requested format of license text
     * @return translated full text of license, or original
     *         text if translated was not found
     */
    public static synchronized String getFullLicenseText(Format format)
    {
        String text = fullTextMap.get(format);
        if (text == null)
        {
            text = readText(format, "gpl-3.0");
            if (text == null)
            {
                Log.logger.error("Full license text null!");
                return "";
            }
            shortTextMap.put(format, text);
        }
        return text;
    }

    /**
     * Returns short text of product license in given
     * format.
     * @param format
     *            requested format of license text
     * @return translated short text of license, or original
     *         text if translated was not found
     */
    public static synchronized String getLicenseText(Format format)
    {
        String text = shortTextMap.get(format);
        if (text == null)
        {
            text = readText(format, "LICENSE");
            if (text == null)
            {
                Log.logger.error("Short license text null!");
                return "";
            }
            shortTextMap.put(format, text);
        }
        return text;
    }

    /**
     * Returns product description.
     * @return product description
     */
    public static String getProductDescription()
    {
        return PRODUCT_DESCRIPTION;
    }

    /**
     * Returns official product name.
     * @return official product name.
     */
    public static String getProductName()
    {
        return PRODUCT_NAME;
    }

    /**
     * Returns product official website.
     * @return product official website.
     */
    public static String getProductWeb()
    {
        return PRODUCT_WEB;
    }

    /**
     * Returns support web page address.
     * @return support web page address.
     */
    public static String getSupportWeb()
    {
        return SUPPORT_WEB;
    }

    /**
     * Returns information about current translation.
     * @return information about current translation.
     */
    public static String getTranslationInfo()
    {
        return TRANSLATION_INFO;
    }

    private static String readText(Format format, String name)
    {
        InputStream in =
                Translator.globalInstance().openTranslatedFile(name
                    + format.getExtension());
        if (in == null)
        {
            Log.logger.error("File not read: " + name + format.getExtension());
            return "";
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(in));
        StringBuffer buf = new StringBuffer();
        String text = null;
        do
        {
            try
            {
                text = rd.readLine();
            }
            catch (IOException e)
            {
                Log.logger.error(e);
                break;
            }
            if (text == null)
                break;
            buf.append(text);
            buf.append('\n');
        } while (true);

        return buf.toString();
    }

}
