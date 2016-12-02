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
package pl.org.minions.stigma.server.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import pl.org.minions.stigma.server.ServerConfig;
import pl.org.minions.stigma.server.http.handler.FaviconHttpHandler;
import pl.org.minions.stigma.server.http.handler.ResourceHttpHandler;
import pl.org.minions.stigma.server.http.handler.SimpleHttpHandler;
import pl.org.minions.utils.logger.Log;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

/**
 * Singleton managing HTTP interface for game server.
 * Creates HTTP server on configured port number.
 */
public final class ServerHttpInterface
{
    public static final int HTTP_SUCCESS_CODE = 200;
    public static final int HTTP_NOT_FOUND_CODE = 404;

    private static ServerHttpInterface instance = new ServerHttpInterface();

    private HttpServer server;
    private final String header404;
    private final String footer404;

    private ServerHttpInterface()
    {
        header404 =
                "<HTML><HEAD><TITLE>404 - Not found</TITLE></HEAD><BODY>URL: ";
        footer404 = " not found on server.</BODY></HTML>";
    }

    /**
     * Returns global instance of this singleton.
     * @return global instance of this singleton
     */
    public static ServerHttpInterface globalInstance()
    {
        return instance;
    }

    /**
     * Sends 404 message for given exchange.
     * @param exchange
     *            exchange to be informed about 404 error
     */
    public void sendNotFoundError(HttpExchange exchange)
    {
        StringBuffer buf = new StringBuffer();
        buf.append(header404);
        buf.append(exchange.getRequestURI().getPath());
        buf.append(footer404);
        byte[] message = buf.toString().getBytes();

        try
        {
            exchange.sendResponseHeaders(HTTP_NOT_FOUND_CODE, message.length);
            OutputStream os = exchange.getResponseBody();
            os.write(message);
            os.close();
        }
        catch (IOException e)
        {
            Log.logger.debug("Sending 404 failed: " + e);
        }
    }

    /**
     * Starts HTTP server on port defined in
     * {@link ServerConfig#getHttpPort()}.
     */
    public void start()
    {
        int port = ServerConfig.globalInstance().getHttpPort();
        if (port <= 0)
            return;

        InetSocketAddress addr = new InetSocketAddress(port);
        try
        {
            server = HttpServer.create(addr, 0);
        }
        catch (IOException e)
        {
            Log.logger.error("HTTP server creation failed: " + e);
            return;
        }

        server.createContext(SimpleHttpHandler.CONTEXT, new SimpleHttpHandler());

        server.createContext(FaviconHttpHandler.CONTEXT,
                             new FaviconHttpHandler());

        // Add resource http handler only if resource-http-server-on proporty is set to true
        if (ServerConfig.globalInstance().isResourceHttpServerOn())
            server.createContext(ResourceHttpHandler.CONTEXT,
                                 new ResourceHttpHandler());

        server.setExecutor(null); // creates default executor
        server.start();
        Log.logger.debug("Started HTTP server on port: " + port);
    }

    /**
     * Stops HTTP server.
     */
    public void stop()
    {
        if (server == null)
            return;
        server.stop(0);
    }
}
