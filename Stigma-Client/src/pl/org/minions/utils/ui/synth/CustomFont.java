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
package pl.org.minions.utils.ui.synth;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.synth.SynthLookAndFeel;

/**
 * Font specialization that provides a String-parametrized
 * constructor to be used in {@link SynthLookAndFeel Synth
 * look and feel} configuration file.
 * <p>
 * Enables loading font as a resource via class loader or
 * from file.
 * <p>
 * Usage:
 * 
 * <pre>
 * &lt;synth&gt;
 *   &lt;style id=&quot;styleId&quot;&gt;
 *       &lt;object class=&quot;name.em3.synth.CustomFont&quot; id=&quot;customFontId&quot;&gt;
 *           &lt;string&gt;font file name.ttf&lt;/string&gt;
 *           &lt;int&gt;12&lt;/int&gt;
 *           &lt;string&gt;BOLD ITALIC&lt;/string&gt;
 *       &lt;/object&gt;
 *       &lt;opaque value=&quot;true&quot; /&gt;
 *       &lt;font idref=&quot;customFontId&quot; /&gt;
 *   &lt;/style&gt;
 * &lt;/synth&gt;
 * </pre>
 * <p>
 * Size is specified by an integer. Style is specified by a
 * space-delimited combination of {@value #PLAIN},
 * {@value #BOLD}, and {@value #ITALIC} (as in
 * <code>font</code> tag in Synth configuration files). Both
 * size and style parameters are optional and can appear in
 * any order <b>after</b> the path parameter.
 * <p>
 * Currently supports only true type fonts.
 * <p>
 * On error contains the default font, with appropriate size
 * and style applied, if possible.
 * @see Font
 */
public class CustomFont extends FontUIResource
{
    /**
     * String token for plain font style.
     */
    public static final String PLAIN = "PLAIN";

    /**
     * String token for bold font style.
     */
    public static final String BOLD = "BOLD";

    /**
     * String token for italic font style.
     */
    public static final String ITALIC = "ITALIC";

    private static final long serialVersionUID = 1L;

    private static final Font FALLBACK_FONT = Font.decode(null);

    private static final int DEFAULT_SIZE = FALLBACK_FONT.getSize();

    /**
     * Creates a custom font from specified resource or file
     * with default size and plain style.
     * @param path
     *            font resource or file path
     */
    public CustomFont(String path)
    {
        this(path, DEFAULT_SIZE, null);
    }

    /**
     * Creates a custom font from specified resource or file
     * with given size and plain style.
     * @param path
     *            font resource or file path
     * @param size
     *            font size
     */
    public CustomFont(String path, int size)
    {
        this(path, size, null);
    }

    /**
     * Creates a custom font from specified resource or file
     * with default size and given style.
     * @param path
     *            font resource or file path
     * @param style
     *            space delimited combination of font style
     *            tokens
     */
    public CustomFont(String path, String style)
    {
        this(path, DEFAULT_SIZE, style);
    }

    /**
     * Creates a custom font from specified resource or file
     * with given size and style.
     * @param path
     *            font resource or file path
     * @param size
     *            font size
     * @param style
     *            space delimited combination of font style
     *            tokens
     */
    public CustomFont(String path, int size, String style)
    {
        super(createCustomFont(path, size, style));
    }

    /**
     * Creates a custom font from specified resource or file
     * with given size and style.
     * @param path
     *            font resource or file path
     * @param style
     *            space delimited combination of font style
     *            tokens
     * @param size
     *            font size
     */
    public CustomFont(String path, String style, int size)
    {
        this(path, size, style);
    }

    private static Font createCustomFont(String path, float size, String style)
    {
        int fontStyle = Font.PLAIN;
        if (style != null)
        {
            final String[] styleTags = style.split(" ");
            for (String string : styleTags)
            {
                if (string.equals(PLAIN))
                    continue;
                else if (string.equals(BOLD))
                    fontStyle |= Font.BOLD;
                else if (string.equals(ITALIC))
                    fontStyle |= Font.ITALIC;
                else
                {
                    System.err.println("Invalid style string token: " + string);
                    return FALLBACK_FONT.deriveFont(Font.PLAIN, size);
                }
            }
        }

        InputStream fontStream;
        fontStream =
                CustomFont.class.getClassLoader().getResourceAsStream(path);
        if (fontStream == null)
        {
            try
            {
                fontStream = new FileInputStream(path);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
                return FALLBACK_FONT.deriveFont(fontStyle, size);
            }
        }

        try
        {
            return Font.createFont(Font.TRUETYPE_FONT, fontStream)
                       .deriveFont(fontStyle, size);
        }
        catch (FontFormatException e1)
        {
            e1.printStackTrace();
            return FALLBACK_FONT.deriveFont(fontStyle, size);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
            return FALLBACK_FONT.deriveFont(fontStyle, size);
        }
    }
}
