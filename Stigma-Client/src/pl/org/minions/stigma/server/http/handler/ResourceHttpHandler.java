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

import pl.org.minions.stigma.globals.GlobalConfig;
import pl.org.minions.stigma.server.http.ServerHttpInterface;
import pl.org.minions.stigma.server.resource.ResourceManager;
import pl.org.minions.utils.logger.Log;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Class used to handle HTTP requests for resources. Those
 * requests should be made with context /resource/. Returned
 * data is not compressed.
 */
public class ResourceHttpHandler implements HttpHandler
{
    /** Context on which this handler works. */
    public static final String CONTEXT = "/resource/";

    private ResourceManager resourceManager;

    private byte[] message = new byte[0];

    /**
     * Default constructor - initiates ResourceManager.
     */
    public ResourceHttpHandler()
    {
        this.resourceManager = new ResourceManager();
        Log.logger.debug("Started ResourceHttpHandler.");
    }

    private byte[] getErrorMessage(String path)
    {
        StringWriter buf = new StringWriter();

        final int refreshIntervalSeconds = 60;

        buf.append("<HTML>\n<HEAD>\n");
        buf.append("<TITLE>" + "Stigma server resource page" + "</TITLE>\n");
        buf.append("<META http-equiv=\"refresh\" content=\""
            + refreshIntervalSeconds + "\"/>");
        buf.append("</HEAD>\n<BODY>\n");

        String header = buf.toString();
        buf = new StringWriter();
        buf.append("<br/><br/>");
        buf.append("</BODY>\n</HTML>");
        String footer = buf.toString();
        buf = new StringWriter();
        buf.append(header);
        buf.append("<h>Error resource context page.</h><br/><br/>");
        buf.append("Context of request is:<br/>");
        buf.append("<b>" + path + "</b><br/>");
        buf.append("Please specify correct resource context.");
        buf.append(footer);

        return buf.toString().getBytes();
    }

    /** {@inheritDoc} */
    @Override
    public void handle(HttpExchange arg0) throws IOException
    {
        handleResourceRequest(arg0);
    }

    private void handleResourceRequest(HttpExchange exchange)
    {
        try
        {
            String path = exchange.getRequestURI().getPath();
            path = path.replace(CONTEXT, "");
            byte[] resource =
                    resourceManager.getResourceData(path,
                                                    GlobalConfig.globalInstance()
                                                                .getClientResourceCompression());
            if (resource != null)
            {
                message = resource;
                exchange.sendResponseHeaders(ServerHttpInterface.HTTP_SUCCESS_CODE,
                                             message.length);
            }
            else
            {
                message = getErrorMessage(path);
                exchange.sendResponseHeaders(ServerHttpInterface.HTTP_NOT_FOUND_CODE,
                                             message.length);
            }

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
