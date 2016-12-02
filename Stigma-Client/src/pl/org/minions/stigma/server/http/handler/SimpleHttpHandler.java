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
import java.io.StringWriter;

import pl.org.minions.stigma.server.Server;
import pl.org.minions.stigma.server.ServerConfig;
import pl.org.minions.stigma.server.ServerObserver;
import pl.org.minions.stigma.server.http.ServerHttpInterface;
import pl.org.minions.utils.logger.Log;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Class used to handle HTTP requests on "/" context. Shows
 * server status.
 */
public class SimpleHttpHandler implements HttpHandler
{
    /** Context on which this handler works. */
    public static final String CONTEXT = "/";

    private final String header;
    private final String footer;
    private byte[] message = new byte[0]; // just null pointer saver, I think it will never be sent...

    /**
     * Creates SimpleHttpHandler: sets header and footer and
     * adds observer to Server.
     */
    public SimpleHttpHandler()
    {
        final String title = "Stigma server status page";
        final int refreshIntervalSeconds = 60;

        StringWriter buf = new StringWriter();

        buf.append("<HTML>\n<HEAD>\n");
        buf.append("<TITLE>" + title + "</TITLE>\n");
        buf.append("<META http-equiv=\"refresh\" content=\""
            + refreshIntervalSeconds + "\"/>");
        buf.append("</HEAD>\n<BODY>\n");
        buf.append("Stigma server state is: <b>");

        header = buf.toString();
        buf = new StringWriter();
        buf.append("</b><br/><br/>");
        ServerConfig config = ServerConfig.globalInstance();
        String listElemEnd = "</li>\n";
        buf.append("Server configuration:<br/>\n");
        buf.append("<ul>\n");
        buf.append("<li><i>Milliseconds per turn</i>: "
            + config.getMillisecondsPerTurn());
        buf.append(listElemEnd);
        buf.append("<li><i>Resource set</i>: " + config.getResourceSet());
        buf.append(listElemEnd);
        buf.append("<li><i>Client resource root</i>: "
            + config.getClientResourceRoot());
        buf.append(listElemEnd);
        buf.append("<li><i>Client resource compression</i>: "
            + config.getClientResourceCompression());
        buf.append(listElemEnd);
        buf.append("</ul><br/>\n");
        buf.append("</BODY>\n</HTML>");
        footer = buf.toString();

        Server.globalInstance().addObserver(new ServerObserver()
        {
            @Override
            public void stateChanged()
            {
                message = buildResponse();
            }
        });
    }

    private byte[] buildResponse()
    {
        StringWriter buf = new StringWriter();

        buf.append(header);
        buf.append(Server.globalInstance().getState().name());
        buf.append(footer);

        return buf.toString().getBytes();
    }

    /** {@inheritDoc} */
    @Override
    public void handle(HttpExchange arg0) throws IOException
    {
        if (!arg0.getRequestURI().getPath().equals(CONTEXT))
            ServerHttpInterface.globalInstance().sendNotFoundError(arg0);
        else
            handleHttpRequest(arg0);
    }

    private void handleHttpRequest(HttpExchange exchange)
    {
        try
        {
            exchange.sendResponseHeaders(ServerHttpInterface.HTTP_SUCCESS_CODE,
                                         message.length);
            OutputStream os = exchange.getResponseBody();
            os.write(message);
            os.close();
        }
        catch (IOException e)
        {
            Log.logger.error("HTTP response creation failed: " + e);
        }
    }

}
