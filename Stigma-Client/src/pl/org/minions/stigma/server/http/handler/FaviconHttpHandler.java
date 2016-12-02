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
package pl.org.minions.stigma.server.http.handler;

import java.io.IOException;
import java.io.OutputStream;

import pl.org.minions.stigma.databases.Resourcer;
import pl.org.minions.stigma.server.http.ServerHttpInterface;
import pl.org.minions.utils.logger.Log;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Class made to handle /favicon.ico HTTP request.
 */
public class FaviconHttpHandler implements HttpHandler
{
    public static final String CONTEXT = "/favicon.ico";

    private static final String FAVICON_FILE_PATH = "img/server/favicon.ico";

    private byte[] faviconBytes;

    /** {@inheritDoc} */
    @Override
    public void handle(HttpExchange arg0) throws IOException
    {
        handleFaviconRequest(arg0);
    }

    private void handleFaviconRequest(HttpExchange exchange)
    {
        if (faviconBytes == null)
        {
            faviconBytes = Resourcer.loadFileBytes(FAVICON_FILE_PATH);
        }

        try
        {
            exchange.sendResponseHeaders(ServerHttpInterface.HTTP_SUCCESS_CODE,
                                         faviconBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(faviconBytes);
            os.close();
        }
        catch (IOException e)
        {
            Log.logger.error("HTTP response creation failed (favicon): " + e);
        }

    }

}
