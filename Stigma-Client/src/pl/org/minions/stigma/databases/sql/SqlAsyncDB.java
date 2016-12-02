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
package pl.org.minions.stigma.databases.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pl.org.minions.utils.logger.Log;

/**
 * Singleton representing asynchronous connection to SQL
 * database. It starts it's own thread for execution of
 * statements. Request are gathered in queue along with
 * proper references to callback interfaces - methods of
 * those interfaces will be called after execution of
 * statement.
 */
public final class SqlAsyncDB
{
    private class DBThread implements Runnable
    {
        @Override
        public void run()
        {
            Log.logger.debug("DBThread started");
            while (!Thread.interrupted())
            {
                SqlRequest request = null;

                try
                {
                    request = queue.take();
                }
                catch (InterruptedException e)
                {
                    break;
                }

                if (request == null)
                    break;

                request.process(connection);
            }

            if (flushMode)
            {
                Log.logger.info("DBThread flushing");

                while (!queue.isEmpty())
                {
                    SqlRequest request = queue.poll();
                    if (request == null)
                        break;
                    if (request.shouldBeFlushed())
                        request.process(connection);
                }
            }
            Log.logger.debug("DBThread stopped");
        }
    }

    private static SqlAsyncDB global;
    private BlockingQueue<SqlRequest> queue =
            new LinkedBlockingQueue<SqlRequest>();

    private Connection connection;

    private Thread thread;

    private volatile boolean flushMode;

    private SqlAsyncDB(String url, String login, String password, String driver)
    {
        try
        {
            Class.forName(driver).newInstance();
            connection = DriverManager.getConnection(url, login, password);
            connection.setAutoCommit(false);
        }
        catch (InstantiationException e)
        {
            Log.logger.fatal(e);
            System.exit(-1);
        }
        catch (IllegalAccessException e)
        {
            Log.logger.fatal(e);
            System.exit(-1);
        }
        catch (ClassNotFoundException e)
        {
            Log.logger.fatal(e);
            System.exit(-1);
        }
        catch (SQLException e)
        {
            Log.logger.fatal(e);
            System.exit(-1);
        }

        Log.logger.debug("Connection to database established");
        thread = new Thread(new DBThread(), "DBThread");
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Returns global instance of singleton.
     * @return global instance of singleton
     */
    public static SqlAsyncDB globalInstance()
    {
        return global;
    }

    /**
     * Starts global instance of singleton. Uses given
     * parameters to connect to SQL database using JDBC
     * driver.
     * @param url
     *            URL to database
     * @param login
     *            login name for database
     * @param password
     *            password for given login name to database
     * @param driver
     *            driver which should be used for connection
     *            (must be in class path)
     */
    public static void initGlobalInstance(String url,
                                          String login,
                                          String password,
                                          String driver)
    {
        global = new SqlAsyncDB(url, login, password, driver);
    }

    /**
     * Stops global instance thread, clears queue, destroys
     * global instance.
     * @param flush
     *            whether or not queue should be flushed
     */
    public static void shutGlobalInstance(boolean flush)
    {
        if (global != null)
        {
            global.freeResources(flush);
        }
        global = null;
    }

    /**
     * Puts request in queue for execution.
     * @param request
     *            request to execute
     */
    public void executeSQL(SqlRequest request)
    {
        try
        {
            queue.put(request);
        }
        catch (InterruptedException e)
        {
            Log.logger.error(e);
        }
    }

    private void freeResources(boolean flush)
    {
        flushMode = flush;

        thread.interrupt();

        if (flush)
            try
            {
                thread.join();
            }
            catch (InterruptedException e1)
            {
                Log.logger.error("Unexpected interruption: " + e1);
            }
        else
            queue.clear();

        try
        {
            connection.close();
        }
        catch (SQLException e)
        {
            Log.logger.error(e);
        }
    }

    /**
     * Prepares statement for further execution. Synchronous
     * call.
     * @param statement
     *            statement for preparation
     * @return prepared statement, or null on error
     */
    public PreparedStatement prepareStatement(String statement)
    {
        try
        {
            return connection.prepareStatement(statement);
        }
        catch (SQLException e)
        {
            Log.logger.error(e);
            return null;
        }
    }
}
