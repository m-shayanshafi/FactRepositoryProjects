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
package pl.org.minions.utils.logger;

import java.net.URL;
import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import pl.org.minions.utils.Version;

/**
 * Class wrapping functionality of log4j Logger.
 */
public abstract class Log
{
    private static final long CONF_DELAY = 2000;
    private static final String PROGRAM_NAME = "program.name";
    /**
     * Static initialization of logger - it should be like
     * that - read log4j user guide.
     */
    // CHECKSTYLE:OFF
    public static Logger logger = Logger.getLogger(Log.class);

    // CHECKSTYLE:ON

    private Log()
    {
    }

    /**
     * Initialization of logger - it should be used as soon
     * as possible. If it is not invoked, all references to
     * logger will throw exception. Program name will be
     * taken from {@link Version#getAppName()}.
     * @param path
     *            configuration file path
     */
    public static void initialize(String path)
    {
        initialize(path, Version.getAppName());
    }

    /**
     * Initialization of logger - it should be used as soon
     * as possible. If it is not invoked, all references to
     * logger will throw exception.
     * @param path
     *            configuration file path
     * @param programName
     *            name of program - to replace in templates
     *            in configuration
     */
    public static void initialize(String path, String programName)
    {
        System.setProperty(PROGRAM_NAME, programName);
        PropertyConfigurator.configureAndWatch(path, CONF_DELAY);
        logger.info(MessageFormat.format(" --- Logger initialized (path: {0}, program name: {1} {2}) --- ",
                                         path,
                                         programName,
                                         Version.FULL_VERSION));
    }

    /**
     * Initialization of logger - it should be used as soon
     * as possible. If it is not invoked, all references to
     * logger will throw exception. Program name will be
     * taken from {@link Version#getAppName()}.
     * @param url
     *            configuration file URL address
     */
    public static void initialize(URL url)
    {
        initialize(url, Version.getAppName());
    }

    /**
     * Initialization of logger - it should be used as soon
     * as possible. If it is not invoked, all references to
     * logger will throw exception.
     * @param url
     *            configuration file URL address
     * @param programName
     *            name of program - to replace in templates
     *            in configuration
     */
    public static void initialize(URL url, String programName)
    {
        System.setProperty(PROGRAM_NAME, programName);
        PropertyConfigurator.configure(url);
        logger.info(MessageFormat.format(" --- Logger initialized (URL: {0}, program name: {1} {2}) --- ",
                                         url,
                                         programName,
                                         Version.FULL_VERSION));
    }

    /**
     * used to check if logging level is set to DEBUG.
     * @return {@code true} when DEBUG level is enabled
     */
    public static boolean isDebugEnabled()
    {
        return logger.isDebugEnabled();
    }

    /**
     * used to check if logging level is set to INFO.
     * @return {@code true} when INFO level is enabled
     */
    public static boolean isInfoEnabled()
    {
        return logger.isInfoEnabled();
    }

    /**
     * used to check if logging level is set to TRACE.
     * @return {@code true} when TRACE level is enabled
     */
    public static boolean isTraceEnabled()
    {
        return logger.isTraceEnabled();
    }
}
