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
package pl.org.minions.utils;

/**
 * Class containing constants describing version of
 * application. Should be filled with proper values during
 * automated build.
 */
public final class Version
{
    public static final String DEVELOPMENT = "Development";

    public static final String MAJOR_NUMBER = "@MajorVersion@";

    public static final String MINOR_NUMBER = "@MinorVersion@";

    public static final String PATCH_NUMBER = "@PatchVersion@";

    public static final boolean DEV_VERSION = MAJOR_NUMBER.indexOf('@') != -1;

    public static final String FULL_VERSION =
            isDevelopment() ? DEVELOPMENT : MAJOR_NUMBER + '.' + MINOR_NUMBER
                + '.' + PATCH_NUMBER;

    public static final String SIMPLIFIED_VERSION =
            isDevelopment() ? DEVELOPMENT : MAJOR_NUMBER + '.' + MINOR_NUMBER;
    public static final String COMPATIBILITY_RANGE =
            isDevelopment() ? "All versions" : SIMPLIFIED_VERSION + ".x";
    private static String appName;

    private Version()
    {
    }

    /**
     * Returns application name given to
     * {@link #initialize(String)}.
     * @return application name
     */
    public static String getAppName()
    {
        if (Version.appName == null)
            throw new IllegalStateException("Version not initialized");
        return appName;
    }

    /**
     * Initializes Version class. Can be called only once.
     * @param appName
     *            application name
     */
    public static void initialize(String appName)
    {
        if (Version.appName != null)
            throw new IllegalStateException("Version already initialized");
        Version.appName = appName;
    }

    /**
     * Returns if class was initialized properly.
     * @return {@code true} when class was initialized
     *         properly
     */
    public static boolean isInitialized()
    {
        return appName != null;
    }

    /**
     * Returns {@code true} when application is in
     * development version.
     * @return {@code true} when application is in
     *         development version.
     */
    public static boolean isDevelopment()
    {
        return DEV_VERSION;
    }
}
