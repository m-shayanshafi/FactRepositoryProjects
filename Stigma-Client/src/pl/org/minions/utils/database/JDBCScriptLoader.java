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
package pl.org.minions.utils.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import pl.org.minions.utils.logger.Log;

/**
 * Class which contains most logic to load scripts.
 */
public class JDBCScriptLoader
{
    private static final int SAMPLE_STATEMENT_LENGTH = 50;

    private JDBCScriptLoaderConfig config;

    private Connection connection;

    /**
     * Default constructor.
     * @param config
     *            configuration of JDBCScriptLoader
     */
    public JDBCScriptLoader(JDBCScriptLoaderConfig config)
    {
        this.config = config;
    }

    /**
     * Loads scripts to databases.
     */
    public void loadScripts()
    {
        for (String database : config.getDatabasesList())
        {
            Log.logger.info("Executing scripts for database: " + database);

            try
            {
                connection = createConnection(database);

                String filename = config.getPreCreateScriptURL(database);
                if (filename != null)
                {
                    executeScripts(new File(filename));

                }

                filename = config.getCreateScriptURL(database);
                if (filename != null)
                {
                    executeScripts(new File(filename));
                    connection.commit();
                }

                filename = config.getPostCreateScriptURL(database);
                if (filename != null)
                {
                    executeScripts(new File(filename));
                    connection.commit();
                }

                filename = config.getPreLoadScriptURL(database);
                if (filename != null)
                {
                    executeScripts(new File(filename));
                    connection.commit();
                }

                filename = config.getLoadScriptURL(database);
                if (filename != null)
                {
                    executeScripts(new File(filename));
                    connection.commit();
                }

                filename = config.getPostLoadScriptURL(database);
                if (filename != null)
                {
                    executeScripts(new File(filename));
                    connection.commit();
                }
            }
            catch (IOException e)
            {
                Log.logger.fatal(e);
                System.exit(-1);
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
        }
    }

    private Connection createConnection(String database) throws InstantiationException,
                                                        IllegalAccessException,
                                                        ClassNotFoundException,
                                                        SQLException
    {
        Log.logger.debug("Creating connection for driver: "
            + config.getDBDriver(database));
        Class.forName(config.getDBDriver(database)).newInstance();
        Connection conn =
                DriverManager.getConnection(config.getDBURL(database),
                                            config.getDBUser(database),
                                            config.getDBPassword(database));
        conn.setAutoCommit(false);

        ResultSet rs = conn.createStatement().executeQuery("SELECT 1");

        if (rs != null)
        {
            Log.logger.info("Connection to database established");
            rs.close();
        }
        else
        {
            Log.logger.fatal("Cannot connect to database: "
                + config.getDBURL(database));
            System.exit(-1);
        }

        return conn;
    }

    private void executeScripts(File file) throws IOException, SQLException
    {
        Log.logger.info("Executing scripts from file: "
            + file.getAbsolutePath());

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line = reader.readLine();
        StringBuffer sqlStatement = null;

        long totalExecutionTime = System.currentTimeMillis();
        int linenr = 1;
        while (line != null)
        {
            line = line.trim();

            //ignore comments and empty lines
            if (line.startsWith("--") || line.isEmpty())
            {
                line = reader.readLine();
                ++linenr;
                continue;
            }

            //if no statement specified 
            if (sqlStatement == null)
            {
                sqlStatement = new StringBuffer();
            }

            //add white character
            sqlStatement.append(" ");

            //add line
            sqlStatement.append(line);

            //if line is end of statement execute it
            if (line.endsWith(";"))
            {
                try
                {
                    Statement statement = connection.createStatement();

                    Log.logger.info("Executing script ["
                        + linenr
                        + "]: "
                        + sqlStatement.toString()
                                      .substring(0,
                                                 SAMPLE_STATEMENT_LENGTH <= sqlStatement.toString()
                                                                                        .length() ? SAMPLE_STATEMENT_LENGTH
                                                                                                 : sqlStatement.length())
                        + "..");

                    long time = System.currentTimeMillis();
                    statement.execute(sqlStatement.toString());
                    time = System.currentTimeMillis() - time;

                    Log.logger.info("Statement execution time: " + time + "ms.");
                }
                catch (SQLException e)
                {
                    Log.logger.error("Error while executing line ["
                        + linenr
                        + "]:  "
                        + sqlStatement.toString()
                                      .substring(0, SAMPLE_STATEMENT_LENGTH)
                        + "...", e);
                }

                //empty statement
                sqlStatement = null;
            }

            line = reader.readLine();
            ++linenr;
        }

        connection.commit();
        totalExecutionTime = System.currentTimeMillis() - totalExecutionTime;
        Log.logger.info("Total execution time: " + totalExecutionTime + "ms");
    }
}
